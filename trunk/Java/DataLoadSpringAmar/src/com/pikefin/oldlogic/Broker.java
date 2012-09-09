package com.pikefin.oldlogic;

/*
 * Comments about multithreading: 
 * -Should avoid passing short term information through the database. Ideally, only should write to database for
 * persistent storage. With current setup, cannot process the same dataset concurrently in multiple threads 
 * (one example is dynamic_url).
 * 
 * Also need to figure out how best to handle thread logging - ?simple as creating a global file handle
 * and have all threads write to that?
 */

//import java.io.*; 
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*; 
//import java.util.concurrent.ThreadPoolExecutor;
//import java.sql.SQLException;

import org.hibernate.Session;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.dao.DataAccessException;

import com.pikefin.businessobjects.Exclude;
import com.pikefin.businessobjects.JobQueue;
import com.pikefin.businessobjects.LogTask;
import com.pikefin.businessobjects.RepeatType;
import com.pikefin.businessobjects.Schedule;
import com.pikefin.businessobjects.TimeEvent;
//import org.springframework.jdbc.support.rowset.SqlRowSet;

import pikefin.log4jWrapper.Logs;


class Broker extends Thread {


  static boolean pause=false;
  static UtilityFunctions uf;
  
  /*
   * OFP 5/10/2012 - Right now we are stuck in between using Spring for thread management with ThreadPoolTaskExecutor and using 
   * the legacy explicit thread management. In spring.xml, the app.max_threads property is used both for the Broker variable 
   * and for injecting into ThreadPoolTaskExecutor.
   * 
   * UPDATE 5/29/2012: I'm deciding not to go with ThreadPoolTaskExector for now - the main benefit of TPTE is the ability to swap out scheduler components
   * but we already have a custom built, functional scheduler component that is based off of the database table. A scheduler component stored entirely in memory
   * would have to be exposed for monitoring purposes. Also monitoring thread state doesn't work the same way with ThreadPoolTaskExecutor - getState() always returns
   * NEW, even after the run() method has completed.
   */
  
  static int nMaxThreads;
  
  
  static Properties props;
  //static boolean bDisableEmails;
  static private boolean bLoadingHistoricalData;
  static private boolean bDebugMode;
  static private int nSleepInterval;
  
  //ThreadPoolTaskExecutor threadPool = null;

  /*
   * listWaitingJobs now holds  tasks.id,schedules.id,repeat_types.id,priority,queued time
   */
  //ArrayList<String[]> listWaitingJobs;
  ArrayList<Job> listWaitingJobs;
  
  DataGrab[] arrayRunningJobs;
  public static DBFunctions dbf;
  Notification notification;
  GarbCollector gc;
  
  //int nMaxBatch;
  
  static int nMinimumPriority;
  
  public static int nMailMessageCount;
  Calendar calMailBegin,calMailEnd;
  static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public void setThreadList(DataGrab[] inputArray) {
	  
	  arrayRunningJobs = inputArray;
  }
  
  /*public void setTaskExecutor(ThreadPoolTaskExecutor inputParam) {
	  
	  threadPool = inputParam;
  }*/
  
  public void setDbFunctions(DBFunctions inputDBF) {
		dbf = inputDBF;
  }
  
  public void setUtilityFunctions(UtilityFunctions inputUF) {
		uf = inputUF;
  }
  
  public void setNotification(Notification inputN) {
		notification = inputN;
  }
  
  public void setMaxThreads(int nInput) {
	  
	  nMaxThreads = nInput;
  }
  
  public int getMaxThreads() {
	  
	  return(nMaxThreads);
  }
  
  public static void setDebugMode(String input) {
	  bDebugMode = true;
	  if (input.equalsIgnoreCase("false"))
		  bDebugMode = false;
	  
  }
  
  public static boolean getDebugMode() {
	  return bDebugMode;
  }
  
  public static void setLoadingHistoricalData(String input) {
	  bLoadingHistoricalData = false;
	  if (input.equalsIgnoreCase("true"))
		  bLoadingHistoricalData = true;
	  
  }
  
  public static boolean getLoadingHistoricalData() {
	  return bLoadingHistoricalData;
  }
  
  public void setSleepInterval(int input) {
	 nSleepInterval = input;
	  
  }
  
  public int getSleepInterval() {
	  return nSleepInterval;
  }

  public void getTriggeredJobs() throws DataAccessException,ParseException  {
	String query;
	//ResultSet rs=null;
	//ResultSet rs2=null;
	//SqlRowSet rs=null;
	List<RepeatType> rs = null;
	List<Schedule> rs2 = null;
	//SqlRowSet rs2=null;
	try	{
		//query = "LOCK TABLES repeat_types WRITE, schedules WRITE";
		//dbf.db_update_query(query);
		//query = "LOCK TABLES schedules WRITE";
		//dbf.db_update_query(query);
		
	
		//query = "select * from repeat_types";
		query = " from RepeatType";
		

		//rs = dbf.db_run_query(query);
		//rs = dbf.dbSpringRunQuery(query);
		rs = dbf.dbHibernateRunQuery(query);
		

		java.util.Date dNextTrigger;
		
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		for (RepeatType rt : rs) { 
		//while (rs.next()) {

			//dNextTrigger = rs.getTimestamp("next_trigger");
			cal2 = rt.getNextTrigger();
			//cal2.set(dNextTrigger.getYear(),dNextTrigger.getMonth(),dNextTrigger.getDay(),dNextTrigger.getHours(),dNextTrigger.getMinutes(),dNextTrigger.getSeconds());
			//cal2.setTime(dNextTrigger);

			if ((cal.after(cal2) || rt.getType().equals("RUNONCE") || rt.getType().equals("RUNEVERY"))
					&& (rt.getType().equals("NONE") != true)) {
				//listTriggeredEvents.add(Integer.toString(rs.getInt("primary_key")));
				query = "from Schedule where repeatTypeId=" + Integer.toString(rt.getRepeatTypeId());
				//rs2 = dbf.db_run_query(query);
				rs2 = dbf.dbHibernateRunQuery(query); 
				/*
				 * Data_set is guaranteed to be unique because of db constraint
				 */
				for (Schedule s : rs2) { 
				//while (rs2.next()) {
					
					//check if task is being excluded
					if  (isTaskExcluded(s.getTaskId() + ""))
							continue;
					
					//check if job is already in queue, don't add it again if it is
					boolean bInQ=false;
					for (int i=0;i<listWaitingJobs.size();i++) {
						//if (listWaitingJobs.get(i).equals(rs2.getString("data_set")))
						//if (listWaitingJobs.get(i)[0].equals(rs2.getString("task_id")))
						if (listWaitingJobs.get(i).task_id == s.getTaskId()) 
							bInQ=true;
										
					}
					
					if (!bInQ) {
					
						
						/*String[] tmp = new String[5];
						tmp[0] = rs2.getString("task_id");
						tmp[1] = rs2.getString("id");
						tmp[2] = rs2.getString("repeat_type_id");
						tmp[3] = rs2.getString("priority");
						tmp[4] = formatter.format(Calendar.getInstance().getTime());*/
						Job j = new Job(s.getTaskId(),s.getScheduleId(),s.getRepeatTypeId(),
								s.getPriority(),Calendar.getInstance(),s.isVerifyMode());
						listWaitingJobs.add(j);
					}
				
					//turn off run once after job is in queue, 4 is the primary key for run type 'NONE'
					if (rt.getType().equals("RUNONCE"))	{
						//query = "select id from repeat_types where type='NONE'";
						query = "from repeat_types where type='NONE'";
						//ResultSet rs3 = dbf.db_run_query(query);
						//SqlRowSet rs3 = dbf.dbSpringRunQuery(query);
						DBObjectSession<Object,Session> dbobjs = dbf.dbHibernateRunQueryUniqueLeaveOpen(query);
						RepeatType rs3 = (RepeatType) dbobjs.a;
						//rs3.next();
						/*
						 * There could be potentially a resource contention here but right now schedules are limited to one at a time in the queue or executing.
						 */
						s.setRepeatTypeId(rs3.getRepeatTypeId());
						//query = "update schedules set repeat_type_id=" + rs3.getInt("id") + " where id='" + s.getScheduleId() + "'";
						dbobjs.b.getTransaction().commit();
						//dbf.db_update_query(query);
						//dbf.dbSpringUpdateQuery(query);
					}
				}
				
			}
		}
		/* Putting this here so that there is no differece between the time used to add jobs to the queue and 
		 * for recalculating the next trigger time.
		 */
		updateRepeatTypes(cal);
	}
	finally	{
		/*if (rs!= null)
			rs.close();
		if (rs2!=null)
			rs2.close();*/
			
			
		//query = "UNLOCK TABLES";
		//dbf.db_run_query(query);
	}
	
  }
  
  public void writeJobQueueIntoDB() {
	  /*
	   * Have to revisit converting this over from data_set to task.
	   */
	  
	  String query;
	  query = "delete from JobQueue ";
	  
	  
	  try {
		  //dbf.db_update_query(query);
		  //dbf.dbSpringUpdateQuery(query);
		  dbf.dbHibernateExecuteUpdateQuery(query);
		  
		  for (int i=0;i<this.getMaxThreads();i++) {
			  if (arrayRunningJobs[i] != null) {
				  JobQueue jq = new JobQueue(); 
				  
				  jq.setTaskId(arrayRunningJobs[i].nCurTask);
				  Thread t = (Thread)arrayRunningJobs[i];
				  
				  jq.setStatus(arrayRunningJobs[i].getState().toString());
				  jq.setStartTime(formatter.format(arrayRunningJobs[i].calJobProcessingStart.getTime()));
				  //jq.setPriority(arrayRunningJobs[i].nPriority);
				  
				  dbf.dbHibernateSaveQuery(jq);
				  
				  
				  /*query = "insert into job_queue (task_id,status,start_time) values (";
				  query += arrayRunningJobs[i].nCurTask + ",'";
				  query += arrayRunningJobs[i].getState().toString() + "','";
				  query += formatter.format(arrayRunningJobs[i].calJobProcessingStart.getTime()) + "')";*/
				  // dbf.db_update_query(query);
				  //dbf.dbSpringUpdateQuery(query);
				  UtilityFunctions.stdoutwriter.writeln("Status of thread " + arrayRunningJobs[i].getName() + ": " + 
						  arrayRunningJobs[i].getState().toString(),Logs.THREAD,"DL2");
			  }
		  }
		  for (int k=0;k<listWaitingJobs.size();k++) {
			  JobQueue jq = new JobQueue();
			  
			  jq.setTaskId(listWaitingJobs.get(k).task_id);
			  jq.setStatus("QUEUED");
			  jq.setQueuedTime(listWaitingJobs.get(k).queued_time);
			  jq.setPriority(listWaitingJobs.get(k).priority);
			  
			  dbf.dbHibernateSaveQuery(jq);
			  
			  
			  
			  
			  /*query = "insert into job_queue (task_id,status,queued_time,priority) values (" + listWaitingJobs.get(k).task_id + 
			  ",'QUEUED','" + listWaitingJobs.get(k).queued_time + "'," + listWaitingJobs.get(k).priority + ")";*/
			  //dbf.db_update_query(query);
			  //dbf.dbSpringUpdateQuery(query);
		  }
		  
	  }
	  catch (DataAccessException sqle)  {
		  UtilityFunctions.stdoutwriter.writeln("Problem issuing sql statement while writing queue into db",Logs.ERROR,"DL2.5");
		  UtilityFunctions.stdoutwriter.writeln(sqle);	  
	  }
		 
	  
	 
  }
  
  public DataGrab initiateJob(int nPriority) throws DataAccessException  {
	  //String strDataSet;
	  String strTask;
	  DataGrab dg;
	  //Calendar cal;
	 
	 
		  sortJobsByPriority();
		  
		  /*
		   * Loop through the list of waiting jobs and only execute one that isn't already running
		   * 
		   * NOTE: the non concurrency rule is based off of task not schedule!!!
		   * Ultimately I think I want to base it off of schedule (this would be less restrictive.)
		   */
		  boolean bAlreadyRunning;
		  for (int i=0;i<listWaitingJobs.size();i++) {
			  

			  bAlreadyRunning = false;
			  strTask = listWaitingJobs.get(i).task_id + "";
			  for (int j=0;j<arrayRunningJobs.length;j++) {
				  if (arrayRunningJobs[j] != null)  {
					  if (arrayRunningJobs[j].nCurTask == listWaitingJobs.get(i).task_id) {
						  UtilityFunctions.stdoutwriter.writeln("Task in waiting queue already running so won't get moved to run queue (task id: " + arrayRunningJobs[j].nCurTask, Logs.STATUS1, "DL3.99");
						  bAlreadyRunning=true;
						  break;
					  }
				  }
			  }
			  
			  if (bAlreadyRunning==false) {
				  
				  if (listWaitingJobs.get(i).priority < nPriority) {
					  UtilityFunctions.stdoutwriter.writeln("Not initiating schedule because one thread is reserved for minimum priority of " + nPriority + ". Task: " + listWaitingJobs.get(i).task_id + ", Priority: " + listWaitingJobs.get(i).priority, Logs.STATUS1, "DL4.36");
					  return null;
				  }
				  
				 /*
				  * OFP 3/12/2011 - Bug here that was resulting in the same TaskId being executed concurrently. If there
				  * was more than 1 job in the waiting queue but the first TaskId in the waiting queue was already running, it
				  * would still be initiated because we were always taking the first job at the head of the waiting queue, instead
				  * of the one just checked (index i).
				  */

				  strTask = listWaitingJobs.get(i).task_id + "";
				 // DBFunctions tmpdbf = new DBFunctions((String)props.get("dbhost"),(String)props.get("dbport"),(String)props.get("database"),(String)props.get("dbuser"),(String)props.get("dbpass"));
				  
				  /*
				   * OFP 3/16/2011 - Fixing a bug where different task_ids were using the same batch # (big no-no). Before, each individual thread would call
				   * dbf.getBatchNumber(). Now the batch number is maintained in the parent thread to avoid duplication.
				   * 
				   */
				
				  dg = new DataGrab(Broker.dbf,strTask,0,listWaitingJobs.get(i).id + "",listWaitingJobs.get(i).repeat_type_id +"",listWaitingJobs.get(i).verify_mode,listWaitingJobs.get(i).priority);
				  
			
				  //dg = new DataGrab(strTask,0,listWaitingJobs.get(i).id,listWaitingJobs.get(i).repeat_type_id,listWaitingJobs.get(i).verify_mode);

				  /*
				   * OFP 3/12/2011 - move the writeKeepAlive function call here because we were running into instances were the DataLoad thread was running
				   * fine but the DataGrab threads were locked up and no new DataGrab threads were being initiated.
				   */

				  dg.start();
				  //threadPool.execute(dg);
				  
				  UtilityFunctions.stdoutwriter.writeln("Initiated DataGrab thread " + dg.getName(),Logs.THREAD,"DL4");
				  
				  
				 //listWaitingJobs.remove(0);
				 listWaitingJobs.remove(i);
				 
				 /*
				  * OFP - We're going to keep it simple by only initiating one job per call of this function. This get a little
				  * funky since we are looping through a list and we are modifying the list (remove(i)) in the loop.
				  */
				 
				  
				 return(dg);
				  
				  
			  }
		  
		  }
		  
		  /*
		   * If we get here then all jobs in waiting queue are already running and we'll
		   * return null which keeps the spot open.
		   */
		  return null;
	  
	  
  }
  
  public void cleanTerminatedJobs() throws DataAccessException  {
	 
	   for (int j=0;j<this.getMaxThreads();j++) {
		  if (arrayRunningJobs[j] != null) {
			  if (Thread.State.TERMINATED == arrayRunningJobs[j].getState()) {
				 
				  updateJobStats(arrayRunningJobs[j]);
				  //checkNotifications(arrayRunningJobs[j]);
				  UtilityFunctions.stdoutwriter.writeln("Cleaning up thread " + arrayRunningJobs[j].getName(),Logs.THREAD,"DL3");
				  arrayRunningJobs[j] = null;
				  
			  }
		  }
	  }
  }
  
  
  public void executeJobs() throws DataAccessException {
	 
	  //Clean out terminated threads and count open slots 
	  /*
	   * We are going to reserve slot 1 for high priority jobs
	   */
	  for (int j=0;j<this.getMaxThreads();j++) {
		  
		  if (arrayRunningJobs[j] == null) {
			  if (j==1) {
				  arrayRunningJobs[j] = initiateJob(Broker.nMinimumPriority);
			  }
			  else
				  arrayRunningJobs[j] = initiateJob(0);	  
			  
		  }
			  
	  }
	  
	  
  }
  
  public void updateJobStats(DataGrab dg) {
	  
	  /* Have DataGrab store an area of all of the data_sets it processed, along with start time and end time.
	   * Write to job stats table task time and individual data_set times.
	   */
	  
	  /*
	   * OFP 2/25/2012 - Only update log_tasks if not loading historical data.
	   */
	  if (Broker.bLoadingHistoricalData != true) { 

		  try {
			  //String strDataSet = dg.strCurDataSet;
			  /*String strTask = dg.nCurTask + "";
			  Calendar endCal = dg.getAlertProcessingEndTime();
			  Calendar startCal = dg.getJobProcessingStartTime();*/
			 
				  
			  String query = "insert into log_tasks (task_id,batch,repeat_type_id,schedule_id,verify_mode,job_process_start,job_process_end,alert_process_start,alert_process_end,stage1_start,stage1_end,stage2_start,stage2_end) values ("
		      + dg.nCurTask + ","
		      + dg.nTaskBatch + ","
		      + dg.strRepeatTypeId + ","
		      + dg.strScheduleId + ","
		      + dg.bVerify + ","
		      + dg.getTaskMetric(TaskMetrics.JOB_START) + ","
		      + dg.getTaskMetric(TaskMetrics.JOB_END) + ","
		      + dg.getTaskMetric(TaskMetrics.ALERT_START) + ","
		      + dg.getTaskMetric(TaskMetrics.ALERT_END) + ","
		      + dg.getTaskMetric(TaskMetrics.STAGE1_START) + ","
		      + dg.getTaskMetric(TaskMetrics.STAGE1_END) + ","
		      + dg.getTaskMetric(TaskMetrics.STAGE2_START) + ","
		      + dg.getTaskMetric(TaskMetrics.STAGE2_END) + ")";
			  
			  LogTask lt = new LogTask();
			  lt.setTaskId(dg.nCurTask);
			  lt.setBatch(dg.nTaskBatch);
			  lt.setRepeatTypeId(Integer.parseInt(dg.strRepeatTypeId));
			  lt.setSchedule(Integer.parseInt(dg.strScheduleId));
			  lt.setVerifyMode(dg.bVerify);
			  lt.setJobProcessStart(dg.getTaskMetric(TaskMetrics.JOB_START));
			  lt.setJobProcessStart(dg.getTaskMetric(TaskMetrics.JOB_END));
			  lt.setJobProcessStart(dg.getTaskMetric(TaskMetrics.ALERT_START));
			  lt.setJobProcessStart(dg.getTaskMetric(TaskMetrics.ALERT_END));
			  lt.setJobProcessStart(dg.getTaskMetric(TaskMetrics.STAGE1_START));
			  lt.setJobProcessStart(dg.getTaskMetric(TaskMetrics.STAGE1_END));
			  lt.setJobProcessStart(dg.getTaskMetric(TaskMetrics.STAGE2_START));
			  lt.setJobProcessStart(dg.getTaskMetric(TaskMetrics.STAGE2_END));
			  
			  dbf.dbHibernateSaveQuery(lt);
			  
			  
			 /* + formatter.format(dg.getJobProcessingStartTime().getTime()) + "','" 
			  + formatter.format(dg.getJobProcessingEndTime().getTime()) + "','"
			  + dg.getAlertProcessingStartTimeString() + "','"
			  + dg.getAlertProcessingEndTimeString() + "','"
			  + formatter.format(dg.getStage1StartTime().getTime()) + "','"
			  + formatter.format(dg.getStage1EndTime().getTime()) + "','"
			  + formatter.format(dg.getStage2StartTime().getTime()) + "','"
			  + formatter.format(dg.getStage2EndTime().getTime()) + "')";*/
			  /*String query = "update schedules set last_run='" + formatter.format(endCal.getTime())+ "' where task_id=" + strTask;
			  dbf.db_update_query(query);*/
			  
			  //dbf.db_update_query(query);
			  //dbf.dbSpringUpdateQuery(query);
		  }
		  
	
		  catch (CustomGenericException cge) {
			  UtilityFunctions.stdoutwriter.writeln("Problem retrieving start or end time",Logs.ERROR,"DL2.57");
			  UtilityFunctions.stdoutwriter.writeln(cge);
		  }
		  catch (DataAccessException sqle) {
			  UtilityFunctions.stdoutwriter.writeln("Problem with sql statement while updating log_tasks table",Logs.ERROR,"DL2.7");
			  UtilityFunctions.stdoutwriter.writeln(sqle);
		  }
	  }

	  
  }
  
  public void updateRepeatTypes(Calendar cal) throws DataAccessException  {
	 
	
		//String query = "select * from repeat_types";
	  	String query = "from RepeatType";
		// ResultSet rs = dbf.db_run_query(query);
		 //SqlRowSet rs = dbf.dbSpringRunQuery(query);
	  	DBObjectSession<List,Session> dbobjs = dbf.dbHibernateRunQueryLeaveOpen(query);
	  	List<RepeatType> rs = (List<RepeatType>)dbobjs.a;
		String strType;
		Date dNextTrigger;
		Calendar triggerCal;
		 //Saving off the parameter cause we're going to overwrite it, just in case.
		Calendar newCal = Calendar.getInstance();
		 
		 //while (rs.next()) {
		 for (RepeatType rt : rs) {
			 triggerCal = Calendar.getInstance();
			 triggerCal = rt.getNextTrigger();
			 strType = rt.getType();
			 //triggerCal.setTime(dNextTrigger);
			 //UtilityFunctions.stdoutwriter.writeln("Old Trigger time" + triggerCal,Logs.STATUS1,"DL7");
			 newCal.setTime(cal.getTime());
			 
			 int nUnit = 0;
			 //int nMultiplier = 0;
	
			 
			 
			if (newCal.after(triggerCal)) {
				 //Next Trigger needs to be recalculated
				
				/* we are going to overwrite the database with newcal, not triggercal, since if this code hasn't been
				 * run in a while, triggercal could lag by multiple cycles.
				 */
					
				 if (strType.equals("WEEKLY")) {
					 nUnit = Calendar.WEEK_OF_YEAR;
					 
					 //newCal.add(Calendar.WEEK_OF_YEAR, rs.getInt("multiplier"));
				 }
				 else if (strType.equals("MONTHLY")) {
					 nUnit = Calendar.MONTH;
					 //May need to add code to handle the 28th - 31st
					 //newCal.add(Calendar.MONTH, rs.getInt("multiplier"));
				 }
				 else if (strType.equals("HOURLY")) {
					 nUnit = Calendar.HOUR;
					 //newCal.add(Calendar.HOUR, rs.getInt("multiplier"));
				 }
				 else if (strType.equals("MINUTE")) {
					 nUnit = Calendar.MINUTE;
					 //newCal.add(Calendar.MINUTE, rs.getInt("multiplier"));
				 }
				 else if (strType.equals("DAILY")) {
					 nUnit = Calendar.DAY_OF_YEAR;
					 //newCal.add(Calendar.DAY_OF_YEAR, rs.getInt("multiplier"));
				 }
				 else {
					 
					 //We'll get here if the REPEAT_TYPE is RUNONCE,RUNEVERY OR NONE.
				
					 continue;
				 }
				 
				 /*
				  * OFP 4/10/2011: Added the following code so that trigger times are recalculated at fixed intervals. 
				  * Previously they were being recalculated at random intervals.
				  * But we also want to maintain the ability increment multiple periods if the program has been
				  * idle for some time.
				  */
				 
				 while (newCal.after(triggerCal)) {
					 triggerCal.add(nUnit,rt.getMultiplier());
					 
				 }
				 
				 /*
				  * End 4/10/2011 code block.
				  */
				 
				//mysql deault datetime format: 'YYYY-MM-DD HH:MM:SS' 
				//DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//String strDate = formatter.format(triggerCal.getTime());
				
				rt.setNextTrigger(triggerCal);
				//System.out.println("here 1");
				
				
				//query = "update repeat_types set next_trigger='" + strDate + "' where id=" + rs.getInt("id");
				//dbf.db_update_query(query);
				//dbf.dbSpringUpdateQuery(query);
			 }
		 }
		 
		 dbobjs.b.getTransaction().commit();
		 
	   
	  
	  
  }
  
  public void garbageCollectionFunction()  {
	  /*
	   * Check if it is time to run the garbage collector
	   */
	  
	  
	  try {
		
		  /*
		   * One thing about this logic is that the log_tasks table won't get updated with the completed tasks
		   * until the gc finishes. 
		   * The alerts will be sent out though, so maybe this isn't a major concern, but it could cause some confusion.
		   */
		  
		  
		  
		  if (gc.shouldRun() == true) {
			  //wait for all jobs to complete
			  boolean alldone=false;
			  while(!alldone) {
				  alldone = true;
				  for (int j=0;j<this.getMaxThreads();j++) {
					  if ((null != arrayRunningJobs[j]) && Thread.State.TERMINATED != arrayRunningJobs[j].getState()) {
						  alldone=false;
						  break;
					  }
				  }
				  sleep(10000);
				  
			  }
			  //when we get here, no jobs are running
			  
			  gc.takeOutTrash();
			  
		  }
		  
		  
		

		  
		  
			  //run gc

				 
	  }

	  catch(InterruptedException ie) {
		  UtilityFunctions.stdoutwriter.writeln("Garbage Collector was interrupted waiting for all jobs" +
		  		" to complete.\nGarbage Collector did not execute.",Logs.ERROR,"DL2.722");
		  UtilityFunctions.stdoutwriter.writeln(ie);
		  
	  }
	  catch(DataAccessException dae) {
		  UtilityFunctions.stdoutwriter.writeln("Garbage Collector terminated prematurely from a SQLException." +
			  		" \nGarbage Collector did not complete execution.",Logs.ERROR,"DL2.725");
		  UtilityFunctions.stdoutwriter.writeln(dae);
	  }

	  
	  
  }
  
  
  
  


  
  /**
    * Open server socket and wait for incoming connections
  */                                                   
  public void run() {	
	 
	nMailMessageCount=0;
	
	calMailBegin = Calendar.getInstance();
	calMailBegin.set(Calendar.HOUR_OF_DAY, 0);           
	calMailBegin.set(Calendar.MINUTE, 0);                
	calMailBegin.set(Calendar.SECOND, 1);         

	//NDC.push("DataLoad");
	//uf = new UtilityFunctions();
	
	//UtilityFunctions.stdoutwriter.writeln("PROPERTIES LOADED FROM DATALOAD.INI",Logs.STATUS1,"DL11");
	UtilityFunctions.stdoutwriter.writeln("DATABASE URL: " + dbf.getUrl(),Logs.STATUS1,"DL12");
	//UtilityFunctions.stdoutwriter.writeln("DATABASE PORT: " + dbf.getPort(),Logs.STATUS1,"DL13");
	//UtilityFunctions.stdoutwriter.writeln("DATABASE NAME: " + dbf.getDatabase(),Logs.STATUS1,"DL14");
	UtilityFunctions.stdoutwriter.writeln("DATABASE USER: " + dbf.getUser(),Logs.STATUS1,"DL15");
	UtilityFunctions.stdoutwriter.writeln("SLEEP INTERVAL: " + this.getSleepInterval(),Logs.STATUS1,"DL16");
	
	
	//Broker.bDebugMode= Boolean.parseBoolean((String)props.getProperty("debugmode"));
	if (Broker.getDebugMode())
		UtilityFunctions.stdoutwriter.writeln("RUNNING IN DEBUG MODE",Logs.STATUS1,"DL18.5");
	else
		UtilityFunctions.stdoutwriter.writeln("RUNNING IN LIVE (NON DEBUG) MODE",Logs.STATUS1,"DL19.5");
	
	/*
	 * disableemails was replaced with debugmode property.
	 */
	/*DataLoad.bDisableEmails= Boolean.parseBoolean((String)props.getProperty("emaildisable"));
	if (DataLoad.bDisableEmails)
		UtilityFunctions.stdoutwriter.writeln("EMAIL AND TWITTER NOTIFICATIONS ARE DISABLED",Logs.STATUS1,"DL18");
	else
		UtilityFunctions.stdoutwriter.writeln("EMAIL AND TWITTER NOTIFICATIONS ARE ENABLED",Logs.STATUS1,"DL19");*/
	
	//Broker.bLoadingHistoricalData= Boolean.parseBoolean((String)props.getProperty("historicaldata"));
	if (Broker.getLoadingHistoricalData()==true)
		UtilityFunctions.stdoutwriter.writeln("LOADING HISTORICAL DATA",Logs.STATUS1,"DL20");
	else
		UtilityFunctions.stdoutwriter.writeln("NOT LOADING HISTORICAL DATA",Logs.STATUS1,"DL21");
	
	
	/*
	 * OFP 5/8/2012 - # of threads is not currently being controlled by this property setting.
	 */
	//Broker.nMaxThreads = Integer.parseInt((String)props.get("max_threads"));
	UtilityFunctions.stdoutwriter.writeln("MAXIMUM # OF DATAGRAB THREADS: " + this.getMaxThreads(),Logs.STATUS1,"DL19");
	
		
	
	

	/*try
	{
		gc = new GarbCollector((String)props.get("gcday"),(String)props.get("gctime"),false,(String)props.getProperty("gcenabled"));
	}
	catch(ParseException pe)
	{
		UtilityFunctions.stdoutwriter.writeln("Problem parsing time. Unable to initiate Garbage Collector. Terminating Program.",Logs.ERROR,"DL2.721");
		UtilityFunctions.stdoutwriter.writeln(pe);
		return;
	}*/
	
	//try {
		//DBFunctions tmpdbf = new DBFunctions((String)props.get("dbhost"),(String)props.get("dbport"),(String)props.get("database"),(String)props.get("dbuser"),(String)props.get("dbpass"));
		//this.notification = new Notification(Broker.uf,tmpdbf);
		this.notification.start();
		UtilityFunctions.stdoutwriter.writeln("Started Notification Thread",Logs.STATUS1,"DL2.727");
		
	/*}
	catch (SQLException sqle) {
		UtilityFunctions.stdoutwriter.writeln("Problem opening database connection. Terminating.",Logs.ERROR,"DL2.726");
		UtilityFunctions.stdoutwriter.writeln(sqle);
		return;
		
	}*/
	

  	arrayRunningJobs = new DataGrab[this.getMaxThreads()];
  	listWaitingJobs = new ArrayList<Job>();
  	for (int i=0;i<this.getMaxThreads();i++)
  		arrayRunningJobs[i] = null;

  	
    
	if (pause != true) {
		
		if (bLoadingHistoricalData == false){
			while(true) {    	
				//
				try { 
					
					//writeKeepAlive();
					updateTimeEvents();
					getTriggeredJobs();
					executeJobs();
					writeJobQueueIntoDB();			
					sleep(this.getSleepInterval()*1000);
					//updateEventTimes();
					
					/*
					 * OFP 5/12/2012 - Garbage collection function inside DataLoad is begin made obsolete.
					 * We will end up archiving data from fact_table using an external program.
					 */
					//garbageCollectionFunction();
					cleanTerminatedJobs();
					checkMessageCount();
					/*
					 * Right now we're just performing lazy database object cleanup by closing and reopening 
					 * the connection. If connection pooling is ever utilized, then this won't work and explicit
					 * close statements (for statements and/or resultsets) will have to be added to the code.
					 */
					//dbf.cycleConnection();
					
					//checkNotifications();
					
					
					//new Echo( ss.accept() ).start();      
					//sleep(60000); //sleep for 1 minute
					//System.out.println("Awake.....");
					//Check if schedules are triggered
					
				        
				     /*ArrayList<String> listDataSets = this.checkSchedules();
				     boolean done = false;
				     while (!done == true)
				     {
				    	 if (nRunningThreads == this.nMaxThreads)
				    		 sleep(60000);
				    	 else
				    		 
				    		 
				    	 
				     }*/
				        	
				  
				        	//Check for run once
				        	//Recalculate trigger timess
					
					
					
					
					
				}
				catch(DataAccessException sqle) {
					UtilityFunctions.stdoutwriter.writeln("Problem issuing sql statement while processing schedules",Logs.ERROR,"DL10");
					UtilityFunctions.stdoutwriter.writeln(sqle);
				}
				catch(InterruptedException ie) {
					//NDC.pop();
					break;
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else {
		//Loading History Data	
				
			HistoricalDataLoad.initiateHistoricalDataLoad(dbf,dbf.getBatchNumber());
			
		}
	
		
	}  
}                
  
 
  

  /*public ArrayList<String> checkSchedules() throws DataAccessException {
  
	  ArrayList<String> tmpList = new ArrayList<String>();
	  String query = "select next_trigger,data_set from repeat_types,schedules where repeat_types.id=schedules.repeat_type_id";
	  //ResultSet rs = dbf.db_run_query(query);
	  SqlRowSet rs = dbf.dbSpringRunQuery(query);
	  Calendar cal = Calendar.getInstance();
	  while(rs.next()) {
		  Time t = rs.getTime("next_trigger");
		  t.before(cal.getTime());
		  tmpList.add(rs.getString("data_set"));  
	  }
	  
	
	  
	  return (tmpList);
	  
	  
  }*/
  
	public void updateTimeEvents() throws DataAccessException {
		
		//String query = "select * from time_events";
		String query = "from TimeEvent";
		
		//ResultSet rsTimeEvents = dbf.db_run_query(query);
		//SqlRowSet rsTimeEvents = dbf.dbSpringRunQuery(query);
		DBObjectSession<List,Session> dbobjs = dbf.dbHibernateRunQueryLeaveOpen(query);
		List<TimeEvent> rs = (List<TimeEvent>)dbobjs.a;
		
		for (TimeEvent te : rs) {
		//while (rsTimeEvents.next()) {
			//Calendar calStart = Calendar.getInstance();
			Calendar calNext = Calendar.getInstance();
			Calendar calLast = Calendar.getInstance();
			Calendar calCurrent = Calendar.getInstance();
			Calendar calNextDelay;

			int nYears = te.getYears();
			int nMonths = te.getMonths();
			int nDays = te.getDays();
			int nHours = te.getHours();
		
			int nDelay = te.getDelay();
			
			
			
			
			
			if ((nYears==0) && (nMonths==0) && (nDays==0) && (nHours==0)) {
				UtilityFunctions.stdoutwriter.writeln("Problem with time event " + te.getTimeEventId(),Logs.ERROR,"DL5.3");
				UtilityFunctions.stdoutwriter.writeln("All time increment values are set to zero, skipping",Logs.ERROR,"DL5.3");
				continue;
			}
				
			
			/*
			 * First time being populated
			 */
			if (te.getNextDateTime() == null) {
				calNext = te.getStartDateTime();
				calLast = te.getStartDateTime();
			}
			else {
				calNext = te.getNextDateTime();
				calLast = te.getNextDateTime();
			}
			
			/*
			 * OFP 9/14/2011 - Added the code for the delay. We needed a mechanism to handle the situation
			 * where:
			 * A) Data that is being collected is delayed. (this is true in the majority of cases)
			 * AND
			 * B) The data that is being collected is being populated using the actual trade time (NOT the collection time).
			 * So if the date_collected field in fact_data is being populated with the correct time (without the delay), then
			 * the alerts for that task need to use a delayed time event. This was discovered with the yahoo yql task when we went
			 * to populating the correct last trade time instead of the collection time.
			 */
			
			calNextDelay = (Calendar)calNext.clone();
			
			calNextDelay.add(Calendar.MINUTE, nDelay);
			
			if (!calNextDelay.after(calCurrent) || (te.getNextDateTime() == null)) {
				
				//Keep adding cycles until next is after current
				while (!calNext.after(calCurrent)) {
					calNext.add(Calendar.YEAR,nYears);
					calNext.add(Calendar.MONTH, nMonths);
					calNext.add(Calendar.DAY_OF_YEAR,nDays);
					calNext.add(Calendar.HOUR,nHours);
				}
				
				//DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				/*String strUpdate = "update time_events set ";
				strUpdate += "last_datetime='" + formatter.format(calLast.getTime()) + "',";
				strUpdate += "next_datetime='" + formatter.format(calNext.getTime()) + "' ";
				strUpdate += "where ";
				strUpdate += "id=" + rsTimeEvents.getInt("id");*/
				
				te.setLastDateTime(calLast);
				te.setNextDateTime(calNext);
				
				
				
				//dbf.db_update_query(strUpdate);
				//dbf.dbSpringUpdateQuery(strUpdate);
			}
				
				
			
		}
		
		dbobjs.b.getTransaction().commit();
		
		
		
		
		
		
	}
	
	public boolean isTaskExcluded(String strTaskId) throws DataAccessException, ParseException {
		
		  //String query11 = "select * from excludes where task_id=" + strTaskId;
		String query11 = "from Exclude where taskId=" + strTaskId;
		  //ResultSet rs11 = dbf.db_run_query(query11);
		  //SqlRowSet rs11 = dbf.dbSpringRunQuery(query11);
		List<Exclude> rs = dbf.dbHibernateRunQuery(query11);
		  //DateFormat localFormatter = new SimpleDateFormat("yyyy-MM-dd");
		  //variable to indicate if there are any entires in the excludes table for this task id
		 
		  Calendar cal4 = Calendar.getInstance();
		  
		  for (Exclude e : rs) {
		  //while(rs11.next()) {
			  
			  //String strBeginTime = rs11.getString("begin_time");
			  String strBeginTime = e.getBeginTime();
			  String strEndTime = e.getEndTime();
			  String[] arrayBeginTime = strBeginTime.split(":");
			  String[] arrayEndTime = strEndTime.split(":");
		  
			  if (e.getType() == 1) {
  
				  int nBeginDay = e.getBeginDay();
				  int nEndDay = e.getEndDay();
				  
				 
				  
				  
				  //double test = ((3601 * Integer.parseInt(arrayBeginTime[0])) / (3600 * 24));
				  //double test2 = ((double)(3600 * Integer.parseInt(arrayBeginTime[0])) / (double)(3600 * 24));
				  
				  double fBegin = (double)nBeginDay + (double)(((3600 * Integer.parseInt(arrayBeginTime[0])) + (60 * Integer.parseInt(arrayBeginTime[1])) + (Integer.parseInt(arrayBeginTime[2]))) / (double)(3600 * 24));
				  double fEnd = (double)nEndDay + (double)(((3600 * Integer.parseInt(arrayEndTime[0])) + (60 * Integer.parseInt(arrayEndTime[1])) + (Integer.parseInt(arrayEndTime[2]))) / (double)(3600 * 24));
				  double fCurrent = (double)cal4.get(Calendar.DAY_OF_WEEK) + (double)(((3600 * cal4.get(Calendar.HOUR_OF_DAY)) + (60 * cal4.get(Calendar.MINUTE)) + (cal4.get(Calendar.SECOND))) / (double)(3600 * 24));
				  
		  
				  if ((fCurrent >= fBegin) && (fCurrent <= fEnd)) {
					  //listTimeEventExcludes.add(rs11.getInt("time_event_id") + "");
					  UtilityFunctions.stdoutwriter.writeln("Excluding task " +strTaskId + " according to excludes id " + e.getExcludeId(),Logs.WARN,"DL5.6");
					  return(true);
				  }
				 
			  }
			  else if (e.getType() == 2) {
				  
				  if (e.getOneTimeDate()== null) {
					  UtilityFunctions.stdoutwriter.writeln("Error with " +strTaskId + " excludes id " + e.getExcludeId() + ", onetime_date null",Logs.ERROR,"DL5.7");
					  return(false);
				  }
				  
				  String strDateBefore = e.getOneTimeDate() + " " + strBeginTime;
				  String strDateAfter = e.getOneTimeDate() + " " + strEndTime;
				  //DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  Calendar calBefore = Calendar.getInstance();
				  Calendar calAfter = Calendar.getInstance();
				  Calendar calCurrent = Calendar.getInstance();

				  calBefore.setTime(formatter.parse(strDateBefore));
				  calAfter.setTime(formatter.parse(strDateAfter));
				  
				
				  if ((calBefore.before(calCurrent)) && (calCurrent.before(calAfter))){
					  UtilityFunctions.stdoutwriter.writeln("Excluding task " +strTaskId + " according to excludes id " + e.getExcludeId(),Logs.WARN,"DL5.6");
					  return(true);
				  }
						  
			  }
			  
			  

			 
		  }
		
		  
		  
		  
		  return(false);
		  
		  
		  
		  
		  
		  
	
	}
	
	
  public void checkMessageCount() {
	  Calendar calCurrent = Calendar.getInstance();
	  
	  if (calCurrent.get(Calendar.DAY_OF_YEAR) > calMailBegin.get(Calendar.DAY_OF_YEAR)) {
		  Broker.nMailMessageCount = 0;
		  calMailBegin = calCurrent;
	  }
	  
	  
  }
  
  public void sortJobsByPriority() {
	  /*
	   * Sort in descending order. Higher priority tasks get executed first.
	   */
	  
	  Comparator<Job> comp2 = new Comparator<Job>() {
		  public int compare(Job first, Job second) {
			int nFirst = first.priority;
			int nSecond = second.priority;
			
			
			if(nFirst > nSecond)
				return -1;
			else if(nSecond > nFirst)
				return 1;
			else {
				Calendar calFirst= Calendar.getInstance();
				Calendar calSecond= Calendar.getInstance();
				try {
					Date d1 = formatter.parse(first.queued_time.getTime().toString());
					Date d2 = formatter.parse(second.queued_time.getTime().toString());
					calFirst.setTime(d1);
					calSecond.setTime(d2);
					if (calFirst.before(calSecond))
						return -1;
					else if (calSecond.before(calFirst))
						return 1;
					else
						return 0;
				}
				catch (ParseException pe) {
					/*
					 * If parse exception, just return 0.
					 */
					return 0;
				}
				
			    
			}
		
		  }
	};


	Collections.sort(listWaitingJobs,comp2);
	  
	  
  }

  
  /*public void writeKeepAlive()
  {
	  String strFilePath = (String)props.get("filelocation");
	  try
	  {
		  PrintWriter kafilewriter = new PrintWriter( new FileWriter(strFilePath,false),true);
		  
		  kafilewriter.println("1");
		  
		  kafilewriter.close();
		  
		 
	  }
	  catch (IOException ioe)
	  {
		  UtilityFunctions.stdoutwriter.writeln("Problem writing to keep alive file.",Logs.ERROR,"DL30");
		  UtilityFunctions.stdoutwriter.writeln(ioe);
		  
	  }
	  
	  
	  
  }*/
  
  
  	class Job {
	  
	 /* tmp[0] = rs2.getString("task_id");
		tmp[1] = rs2.getString("id");
		tmp[2] = rs2.getString("repeat_type_id");
		tmp[3] = rs2.getString("priority");
		tmp[4] = formatter.format(Calendar.getInstance().getTime());*/
	  
	  int task_id;
	  int id;
	  int repeat_type_id;
	  int priority;
	  Calendar queued_time;
	  boolean verify_mode;
	  
	  public Job(int inputtaskid, int inputid, int inputrepeattypeid, int inputpriority, Calendar inputqueuedtime, boolean inputverifymode) {
		  this.task_id = inputtaskid;
		  this.id = inputid;
		  this.repeat_type_id = inputrepeattypeid;
		  this.priority = inputpriority;
		  this.queued_time = inputqueuedtime;
		  this.verify_mode = inputverifymode;
		  
	  }
	  
	  
  }


}              


 






