//package com.roeschter.jsl;

/*
 * Comments about multithreading: 
 * -Should avoid passing short term information through the database. Ideally, only should write to database for
 * persistent storage. With current setup, cannot process the same dataset concurrently in multiple threads 
 * (one example is dynamic_url).
 * 
 * Also need to figure out how best to handle thread logging - ?simple as creating a global file handle
 * and have all threads write to that?
 */

import java.net.*;
import java.io.*; 
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*; 
//import org.apache.log4j.Logger;
//import org.apache.log4j.NDC;



/*import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;*/




                  
                 
class DataLoad extends Thread //implements Stopable
{                                               

	//static Logger fulllogger = Logger.getLogger("FullLogging");
	
	  /**
	    * The server socket which accepts connections
	  */
  ServerSocket ss;
  static boolean pause=false;
  static UtilityFunctions uf;
  static int nMaxThreads;
  static Properties props;
  static boolean bDisableEmails;
  //Hashtable<String,Hashtable<String,String>> hashScheduleTable;
  ArrayList<String> listWaitingJobs;
  DataGrab[] arrayRunningJobs;
  public static DBFunctions dbf;
  GarbCollector gc;
  
  int nMaxBatch;
  
  public static int nMailMessageCount;
  Calendar calMailBegin,calMailEnd;

  //static printwriter
                                                                 
  /**                                           
    * Here the telnet server implements the Stopable interface
    * On exit close the server cocket on port 23
  */
  public void onServiceStop()
  {       
    System.out.println( "Stopping Telnet Echo" );           
    try {
      if ( ss != null )
        ss.close();
    } catch (Exception e) {}
  }              
  
  public void testCalendar()
  {
	  
	  String str_date="11-June-07";
      DateFormat formatter ; 
      java.util.Date date ; 
      try
      {
    	  formatter = new SimpleDateFormat("dd-MMM-yy");
    	  date = (Date)formatter.parse(str_date); 
    	  Calendar cal=Calendar.getInstance();
    	  cal.setTime(date);
    	  System.out.println("Today is " +cal );
      }
      catch (ParseException pe)
      {
    	  
      }

  }
                                        
  /**
    * Don't wait if onServiceStop does not return. Terminate immediately.
  */                                        
  public int timeToWait()
  {
    return 0;
  }                                     
  
  public void destroy()
  {
	  System.out.println("terminating");
  }
  
  public void getTriggeredJobs() throws SQLException
  {
	String query;
	ResultSet rs=null;
	ResultSet rs2=null;
	try
	{
		//query = "LOCK TABLES repeat_types WRITE, schedules WRITE";
		//dbf.db_update_query(query);
		//query = "LOCK TABLES schedules WRITE";
		//dbf.db_update_query(query);
		
	
		query = "select * from repeat_types";

		rs = dbf.db_run_query(query);

		java.util.Date dNextTrigger;
		
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		while (rs.next())
		{

			dNextTrigger = rs.getTimestamp("next_trigger");
			//cal2.set(dNextTrigger.getYear(),dNextTrigger.getMonth(),dNextTrigger.getDay(),dNextTrigger.getHours(),dNextTrigger.getMinutes(),dNextTrigger.getSeconds());
			cal2.setTime(dNextTrigger);

			if ((cal.after(cal2) || rs.getString("type").equals("RUNONCE") || rs.getString("type").equals("RUNEVERY"))
					&& (rs.getString("type").equals("NONE") != true))
			{
				//listTriggeredEvents.add(Integer.toString(rs.getInt("primary_key")));
				query = "select * from schedules where repeat_type_id=" + Integer.toString(rs.getInt("id"));
				rs2 = dbf.db_run_query(query);
				/*
				 * Data_set is guaranteed to be unique because of db constraint
				 */
				while (rs2.next())
				{
					//check if job is already in queue, don't add it again if it is
					boolean bInQ=false;
					for (int i=0;i<listWaitingJobs.size();i++)
					{
						//if (listWaitingJobs.get(i).equals(rs2.getString("data_set")))
						if (listWaitingJobs.get(i).equals(rs2.getString("task_id")))
							bInQ=true;
										
					}
					
					if (!bInQ)
						//listWaitingJobs.add(rs2.getString("data_set"));
						listWaitingJobs.add(rs2.getString("task_id"));
				
					//turn off run once after job is in queue, 4 is the primary key for run type 'NONE'
					if (rs.getString("type").equals("RUNONCE"))
					{
						query = "select id from repeat_types where type='NONE'";
						ResultSet rs3 = dbf.db_run_query(query);
						rs3.next();
						/*
						 * There could be potentially a resource contention here but right now schedules are limited to one at a time in the queue or executing.
						 */
						query = "update schedules set repeat_type_id=" + rs3.getInt("id") + " where id='" + rs2.getString("id") + "'";
						dbf.db_update_query(query);
					}
				}
				
			}
		}
		/* Putting this here so that there is no differece between the time used to add jobs to the queue and 
		 * for recalculating the next trigger time.
		 */
		updateEventTimes(cal);
	}
	finally
	{
		if (rs!= null)
			rs.close();
		if (rs2!=null)
			rs2.close();
			
			
		//query = "UNLOCK TABLES";
		//dbf.db_run_query(query);
	}
	
  }
  
  public void writeJobQueueIntoDB()
  {
	  /*
	   * Have to revisit converting this over from data_set to task.
	   */
	  
	  String query;
	  query = "delete from job_queue";
	  try
	  {
		  dbf.db_update_query(query);
		  
		  for (int i=0;i<nMaxThreads;i++)
		  {
			  if (arrayRunningJobs[i] != null)
			  {
				  query = "insert into job_queue (data_set,status) values ('" + arrayRunningJobs[i].strCurTask + "','" +
				  arrayRunningJobs[i].getState().toString() + "')";
				  dbf.db_update_query(query);
				  UtilityFunctions.stdoutwriter.writeln("Status of thread " + arrayRunningJobs[i].getName() + ": " + 
						  arrayRunningJobs[i].getState().toString(),Logs.THREAD,"DL2");
			  }
		  }
		  for (int k=0;k<listWaitingJobs.size();k++)
		  {
			  query = "insert into job_queue (data_set,status) values ('" + listWaitingJobs.get(k) + "','QUEUED')";
			  dbf.db_update_query(query);
		  }
		  
	  }
	  catch (SQLException sqle)
	  {
		  UtilityFunctions.stdoutwriter.writeln("Problem issuing sql statement while writing queue into db",Logs.ERROR,"DL2.5");
		  UtilityFunctions.stdoutwriter.writeln(sqle);	  
	  }
		 
	  
	 
  }
  
  public DataGrab initiateJob() throws SQLException
  {
	  //String strDataSet;
	  String strTask;
	  DataGrab dg;
	  //Calendar cal;
	 
	 
		  
		  
		  /*
		   * Loop through the list of waiting jobs and only execute one that isn't already running
		   * 
		   * NOTE: the non concurrency rule is based off of task not schedule!!!
		   * Ultimately I think I want to base it off of schedule (this would be less restrictive.
		   */
		  boolean bAlreadyRunning;
		  for (int i=0;i<listWaitingJobs.size();i++)
		  {
			  bAlreadyRunning = false;
			  strTask = listWaitingJobs.get(i);
			  for (int j=0;j<arrayRunningJobs.length;j++)
			  {
				  if (arrayRunningJobs[j] != null)
				  {
					  if (arrayRunningJobs[j].nCurTask == Integer.parseInt(listWaitingJobs.get(i)))
					  {
						  UtilityFunctions.stdoutwriter.writeln("Job in waiting queue already running so won't get moved to run queue (task id: " + arrayRunningJobs[j].nCurTask, Logs.STATUS1, "DL3.99");
						  bAlreadyRunning=true;
						  break;
					  }
				  }
			  }
			  
			  if (bAlreadyRunning==false)
			  {
				  
				 /*
				  * OFP 3/12/2011 - Bug here that was resulting in the same TaskId being executed concurrently. If there
				  * was more than 1 job in the waiting queue but the first TaskId in the waiting queue was already running, it
				  * would still be initiated because we were always taking the first job at the head of the waiting queue, instead
				  * of the one just checked (index i).
				  */
				  //strTask = listWaitingJobs.get(0);
				  strTask = listWaitingJobs.get(i);
				  DBFunctions tmpdbf = new DBFunctions((String)props.get("dbhost"),(String)props.get("dbport"),(String)props.get("database"),(String)props.get("dbuser"),(String)props.get("dbpass"));
				  
				  /*
				   * OFP 3/16/2011 - Fixing a bug where different task_ids were using the same batch # (big no-no). Before, each individual thread would call
				   * dbf.getBatchNumber(). Now the batch number is maintained in the parent thread to avoid duplication.
				   * 
				   */
				  dg = new DataGrab(DataLoad.uf,tmpdbf,strTask,this.nMaxBatch);
				  
				  this.nMaxBatch++;
				  /*
				   * OFP 3/12/2011 - move the writeKeepAlive function call here because we were running into instances were the DataLoad thread was running
				   * fine but the DataGrab threads were locked up and no new DataGrab threads were being initiated.
				   */
				  writeKeepAlive();
				  dg.startThread();
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
  
  public void cleanTerminatedJobs() throws SQLException
  {
	  for (int j=0;j<nMaxThreads;j++)
	  {
		  if (arrayRunningJobs[j] != null)
		  {
			  if (Thread.State.TERMINATED == arrayRunningJobs[j].getState())
			  {
				 
				  updateJobStats(arrayRunningJobs[j]);
				  //checkNotifications(arrayRunningJobs[j]);
				  UtilityFunctions.stdoutwriter.writeln("Cleaning up thread " + arrayRunningJobs[j].getName(),Logs.THREAD,"DL3");
				  arrayRunningJobs[j] = null;
				  
			  }
		  }
	  }
  }
  
  public void executeJobs() throws SQLException
  {
	 
	  //Clean out terminated threads and count open slots 
	  for (int j=0;j<nMaxThreads;j++)
	  {
		  if (arrayRunningJobs[j] == null)
			  arrayRunningJobs[j] = initiateJob();
		  /*else if (Thread.State.TERMINATED == arrayRunningJobs[j].getState())
		  {
			  UtilityFunctions.stdoutwriter.writeln("Cleaning up thread " + arrayRunningJobs[j].getName(),Logs.THREAD,"DL3");
			  arrayRunningJobs[j] = initiateJob();
		  }*/
		  
	  }
	  
	 
	  
	  
  }
  
  public void updateJobStats(DataGrab dg)
  {
	  
	  /* Have DataGrab store an area of all of the data_sets it processed, along with start time and end time.
	   * Write to job stats table task time and individual data_set times.
	   */

	  try
	  {
		  //String strDataSet = dg.strCurDataSet;
		  /*String strTask = dg.nCurTask + "";
		  Calendar endCal = dg.getAlertProcessingEndTime();
		  Calendar startCal = dg.getJobProcessingStartTime();*/
		 
		  
		  DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  //String strDate = formatter.format(endCal.getTime());
		  
		  //strDate = formatter.format(dg.getStartTime().getTime());
		  
		  String query = "insert into log_tasks (task_id,batch,job_process_start,job_process_end,alert_process_start,alert_process_end) values ("
	      + dg.nCurTask + ","
	      + dg.nTaskBatch + ",'"
		  + formatter.format(dg.getJobProcessingStartTime().getTime()) + "','" 
		  + formatter.format(dg.getJobProcessingEndTime().getTime()) + "','"
		  + formatter.format(dg.getAlertProcessingStartTime().getTime()) + "','"
		  + formatter.format(dg.getAlertProcessingEndTime().getTime()) +"')";
		  /*String query = "update schedules set last_run='" + formatter.format(endCal.getTime())+ "' where task_id=" + strTask;
		  dbf.db_update_query(query);*/
		  
		  dbf.db_update_query(query);
	  }
	  
	  /*
	   * All this code is now obsolete since we are saving detailed entries and run counts and averages can
	   * be calculated from these entries.
	   */
		  //query = "select * from job_stats where data_set='" + strDataSet + "'";
		 /* query = "select * from job_stats where task_id=" + strTask;
		  
		  ResultSet rs = dbf.db_run_query(query);
		  
		  Long lStart = startCal.getTimeInMillis();
		  Long lEnd = endCal.getTimeInMillis();
		  Long lDiff = lEnd - lStart;
		  Calendar cal = Calendar.getInstance();
		  Calendar cal2 = Calendar.getInstance();
		  cal.setTimeInMillis(lDiff);
		  
		  if (rs.next())
		  {
			  Integer nRunCount = rs.getInt("run_count");
			  
			  
			  //Long lAvg = cal.getTimeInMillis();
			  Long lAvg = rs.getLong("avg_run_duration");
			  
			  lAvg = ((nRunCount * lAvg) + lDiff)/(nRunCount + 1);
			  
			  nRunCount++;
			  
			  
			  cal2.setTimeInMillis(lAvg);
			  
			  query = "update job_stats " + 
			  "set run_count=" + nRunCount + 
			  ",last_run_duration=" +  lDiff.toString() + 
			  ",avg_run_duration=" + lAvg.toString() + 
			  ",last_run_dur_converted='" + UtilityFunctions.getElapsedTimeHoursMinutesSecondsString(lDiff) + "'" +
			  ",avg_run_dur_converted='" + UtilityFunctions.getElapsedTimeHoursMinutesSecondsString(lAvg) + "'" +
			  " where task_id=" + strTask;
			  dbf.db_update_query(query);
		  }
		  else
		  //data_set doesn't exist in job_stats, need to insert it.
		  {
			 
			  query = "insert into job_stats (task_id,run_count,avg_run_duration,last_run_duration,avg_run_dur_converted,last_run_dur_converted) VALUES (" +
			  strTask + ",1," +
			  lDiff.toString() + "," +
			  lDiff.toString() + ",'" +
			  UtilityFunctions.getElapsedTimeHoursMinutesSecondsString(lDiff) + "','" +
			  UtilityFunctions.getElapsedTimeHoursMinutesSecondsString(lDiff) + "')";

			  dbf.db_update_query(query);
		  }
	
	  }*/
	  catch (CustomGenericException cge)
	  {
		  UtilityFunctions.stdoutwriter.writeln("Problem retrieving start or end time",Logs.ERROR,"DL2.5");
		  UtilityFunctions.stdoutwriter.writeln(cge);
	  }
	  catch (SQLException sqle)
	  {
		  UtilityFunctions.stdoutwriter.writeln("Problem with sql statement while updating log_tasks table",Logs.ERROR,"DL2.7");
		  UtilityFunctions.stdoutwriter.writeln(sqle);
	  }

	  
  }
  
  public void updateEventTimes(Calendar cal) throws SQLException
  {
	 try
	 {
		 //String query = "LOCK TABLES repeat_types WRITE,schedules WRITE";
		// dbf.db_update_query(query);
		 String query = "select * from repeat_types";
		 ResultSet rs = dbf.db_run_query(query);
		 String strType;
		 Date dNextTrigger;
		 Calendar triggerCal;
		 //Saving off the parameter cause we're going to overwrite it, just in case.
		 Calendar newCal = Calendar.getInstance();
		 
		 while (rs.next())
		 {
			 triggerCal = Calendar.getInstance();
			 dNextTrigger = rs.getTimestamp("next_trigger");
			 strType = rs.getString("type");
			 triggerCal.setTime(dNextTrigger);
			 //UtilityFunctions.stdoutwriter.writeln("Old Trigger time" + triggerCal,Logs.STATUS1,"DL7");
			 newCal.setTime(cal.getTime());
	
			if (newCal.after(triggerCal))
				 //Next Trigger needs to be recalculated
				
			 {
				/* we are going to overwrite the database with newcal, not triggercal, since if this code hasn't been
				 * run in a while, triggercal could lag by multiple cycles.
				 */
				 if (strType.equals("WEEKLY"))
				 {
					 newCal.add(Calendar.WEEK_OF_YEAR, rs.getInt("multiplier"));
				 }
				 else if (strType.equals("MONTHLY"))
				 {
					 //May need to add code to handle the 28th - 31st
					 newCal.add(Calendar.MONTH, rs.getInt("multiplier"));
				 }
				 else if (strType.equals("HOURLY"))
				 {
					 newCal.add(Calendar.HOUR, rs.getInt("multiplier"));
				 }
				 else if (strType.equals("MINUTE"))
				 {
					 newCal.add(Calendar.MINUTE, rs.getInt("multiplier"));
				 }
				 else if (strType.equals("DAILY"))
				 {
					 newCal.add(Calendar.DAY_OF_YEAR, rs.getInt("multiplier"));
				 }
				 else
					 continue;
				 
				//mysql deault datetime format: 'YYYY-MM-DD HH:MM:SS' 
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strDate = formatter.format(newCal.getTime());
				query = "update repeat_types set next_trigger='" + strDate + "' where id=" + rs.getInt("id");
				dbf.db_update_query(query);
			 }
		 }
		 
		 
		 //recalculate Trigger times
		 //clear run once here??
	 }
	 finally
	 {
		 //String query = "UNLOCK TABLES";
		 //dbf.db_run_query(query);
	 }
	  
	  
	  
  }
  
  public void garbageCollectionFunction()
  {
	  /*
	   * Check if it is time to run the garbage collector
	   */
	  
	  
	  try
	  {
		
		  /*
		   * One thing about this logic is that the log_tasks table won't get update with the completed tasks
		   * until the gc finishes. 
		   * The alerts will be sent out though, so maybe this isn't a major concern, but it could cause some confusion.
		   */
		  
		  
		  
		  if (gc.shouldRun() == true)
		  {
			  //wait for all jobs to complete
			  boolean alldone=false;
			  while(!alldone)
			  {
				  alldone = true;
				  for (int j=0;j<nMaxThreads;j++)
				  {
					  if ((null != arrayRunningJobs[j]) && Thread.State.TERMINATED != arrayRunningJobs[j].getState())
					  {
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

	  catch(InterruptedException ie)
	  {
		  UtilityFunctions.stdoutwriter.writeln("Garbage Collector was interrupted waiting for all jobs" +
		  		" to complete.\nGarbage Collector did not execute.",Logs.ERROR,"DL2.722");
		  UtilityFunctions.stdoutwriter.writeln(ie);
		  
	  }
	  catch(SQLException sqle)
	  {
		  UtilityFunctions.stdoutwriter.writeln("Garbage Collector terminated prematurely from a SQLException." +
			  		" \nGarbage Collector did not complete execution.",Logs.ERROR,"DL2.725");
		  UtilityFunctions.stdoutwriter.writeln(sqle);
	  }
	  
	  
	  
	  /*
	   * yes - wait for all current threads to finish and then run it.
	   */
	  /*
	   * no - exit;
	   */
	  
	  
  }
  
  
  
  


  
  /**
    * Open server socket and wait for incoming connections
  */                                                   
  public void run()
  {	
	 
	nMailMessageCount=0;
	
	calMailBegin = Calendar.getInstance();
	calMailBegin.set(Calendar.HOUR_OF_DAY, 0);           
	calMailBegin.set(Calendar.MINUTE, 0);                
	calMailBegin.set(Calendar.SECOND, 1);         

	//NDC.push("DataLoad");
	uf = new UtilityFunctions();
	
	UtilityFunctions.stdoutwriter.writeln("PROPERTIES LOADED FROM DATALOAD.INI",Logs.STATUS1,"DL11");
	UtilityFunctions.stdoutwriter.writeln("DATABASE SERVER: " + (String)props.get("dbhost"),Logs.STATUS1,"DL12");
	UtilityFunctions.stdoutwriter.writeln("DATABASE PORT: " + (String)props.get("dbport"),Logs.STATUS1,"DL13");
	UtilityFunctions.stdoutwriter.writeln("DATABASE NAME: " + (String)props.get("database"),Logs.STATUS1,"DL14");
	UtilityFunctions.stdoutwriter.writeln("DATABASE USER: " + (String)props.get("dbuser"),Logs.STATUS1,"DL15");
	UtilityFunctions.stdoutwriter.writeln("SLEEP INTERVAL: " + (String)props.get("sleep_interval"),Logs.STATUS1,"DL16");
	UtilityFunctions.stdoutwriter.writeln("KEEP ALIVE FILE LOCATION: " + (String)props.get("filelocation"),Logs.STATUS1,"DL17");
	//UtilityFunctions.stdoutwriter.writeln("GARBAGE COLLECTOR DAY: " + (String)props.get("gcday"),Logs.STATUS1,"DL17");
	//UtilityFunctions.stdoutwriter.writeln("GARBAGE COLLECTOR TIME: " + (String)props.get("gctime"),Logs.STATUS1,"DL18");
	
	DataLoad.bDisableEmails= Boolean.parseBoolean((String)props.getProperty("emaildisable"));
	if (DataLoad.bDisableEmails)
		UtilityFunctions.stdoutwriter.writeln("EMAIL NOTIFICATIONS ARE DISABLED",Logs.STATUS1,"DL18");
	else
		UtilityFunctions.stdoutwriter.writeln("EMAIL NOTIFICATIONS ARE ENABLED",Logs.STATUS1,"DL19");
	
	DataLoad.nMaxThreads = Integer.parseInt((String)props.get("max_threads"));
	UtilityFunctions.stdoutwriter.writeln("MAXIMUM # OF DATAGRAB THREADS: " + DataLoad.nMaxThreads,Logs.STATUS1,"DL19");
	
		
	
	
	/*
	 * This dbf is for just the DataLoad thread. Because of a memory leak issue, we will create a separte dbf for each job thread, and then
	 * close the database connection when that thread is done.
	 */
	try
	{
		dbf = new DBFunctions((String)props.get("dbhost"),(String)props.get("dbport"),(String)props.get("database"),(String)props.get("dbuser"),(String)props.get("dbpass"));
		
		nMaxBatch = dbf.getBatchNumber();
		
		
	}
	catch (SQLException sqle)
	{
		UtilityFunctions.stdoutwriter.writeln("Problem opening database connection. Terminating.",Logs.ERROR,"DL2.719");
		UtilityFunctions.stdoutwriter.writeln(sqle);
		return;
	}
	try
	{
		gc = new GarbCollector((String)props.get("gcday"),(String)props.get("gctime"),false,(String)props.getProperty("gcenabled"));
	}
	catch(ParseException pe)
	{
		UtilityFunctions.stdoutwriter.writeln("Problem parsing time. Unable to initiate Garbage Collector. Terminating Program.",Logs.ERROR,"DL2.721");
		UtilityFunctions.stdoutwriter.writeln(pe);
		return;
	}

  	arrayRunningJobs = new DataGrab[nMaxThreads];
  	listWaitingJobs = new ArrayList<String>();
  	for (int i=0;i<nMaxThreads;i++)
  		arrayRunningJobs[i] = null;

  	int nsleepinterval = Integer.parseInt((String)props.get("sleep_interval"));
  	
    
	if (pause != true)
	{
		while(1 != 2)
		{    	
			//
			try 
			{ 
				
				//writeKeepAlive();
				updateTimeEvents();
				getTriggeredJobs();
				executeJobs();
				writeJobQueueIntoDB();			
				sleep(nsleepinterval*1000);
				//updateEventTimes();
				garbageCollectionFunction();
				cleanTerminatedJobs();
				checkMessageCount();
				/*
				 * Right now we're just performing lazy database object cleanup by closing and reopening 
				 * the connection. If connection pooling is ever utilized, then this won't work and explicit
				 * close statements (for statements and/or resultsets) will have to be added to the code.
				 */
				dbf.cycleConnection();
				
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
			catch(SQLException sqle)
			{
				UtilityFunctions.stdoutwriter.writeln("Problem issuing sql statement while processing schedules",Logs.ERROR,"DL10");
				UtilityFunctions.stdoutwriter.writeln(sqle);
			}
			catch(InterruptedException ie)
			{
				//NDC.pop();
				break;
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}  
}                
  
  /**
    * The worker thread.
    * Get the socket input stream and echos bytes read
  */    
  /*class Echo extends Thread
  {              
    Socket s;
    Echo( Socket s )
    {
      this.s = s;
    }
    
    public void run()
    {
    	System.out.println("in run 2");
      try { 
        InputStream in = s.getInputStream();  
        OutputStream out = s.getOutputStream();  
                        
        while( true )
        {
          out.write( in.read() );
        }                
      } catch (Exception e)
      {}
    }
  } */   
     
  public static void paramtest( String[] arg)
  {
    System.out.println( "param passsing test" );
    if ( arg == null )
    {
        System.out.println( arg );
        return;
    }
    for( int i=0; i<arg.length; i++ )
        System.out.println( arg[i] );
  }     
     
     
  public static void pause()
  {
    System.out.println( "Service paused" );
  }    
  
  public static int premain()
  {
    System.out.println( "Premain call" );   
    return 0;
  }    
  
  public static void cont()
  {
    System.out.println( "Service continued" );
  }     
  
  

                                                                                                                                
  /**                                                         
    * The main method called when the service starts.
  */
  public static void main (String[] argv) throws Exception
  {               	  			                  
    /*System.out.println( "This is the System.out stream" );
    System.err.println( "This is the System.err stream" );
    System.out.println( "This is a test. ");*/
    			      
    String inifile = System.getProperty( "service.inifile" ); 			      
    System.out.println( "Loading ini file: " + inifile );
    
    props = new WindowsCompatibleProperties();
    props.load( new FileInputStream(inifile) );
    
    //System.out.println( props );
    
  
    
    			                         
    //Create echo server class
    DataLoad dLoad = new DataLoad();
    //Register it with the ServiceStopper
    //This is a decprecated feature only demontrated for backwards compatibility.
    //Please use the stopmethod,stopclass configuration parameter for stopping a service
    
    //ServiceStopper.stop( echo );         
    
    //Start the echo server thread
    dLoad.start();

  }
  
  public ArrayList<String> checkSchedules() throws SQLException
  {

	  ArrayList<String> tmpList = new ArrayList<String>();
	  String query = "select next_trigger,data_set from repeat_types,schedules where repeat_types.id=schedules.repeat_type_id";
	  ResultSet rs = dbf.db_run_query(query);
	  Calendar cal = Calendar.getInstance();
	  while(rs.next())
	  {
		  Time t = rs.getTime("next_trigger");
		  t.before(cal.getTime());
		  tmpList.add(rs.getString("data_set"));  
	  }
	  
	
	  
	  return (tmpList);
	  
	  
  }
  
	public void updateTimeEvents() throws SQLException
	{
		
		String query = "select * from time_events";
		
		ResultSet rsTimeEvents = dbf.db_run_query(query);
		
		while (rsTimeEvents.next())
		{
			//Calendar calStart = Calendar.getInstance();
			Calendar calNext = Calendar.getInstance();
			Calendar calLast = Calendar.getInstance();
			Calendar calCurrent = Calendar.getInstance();
			
			int nYears = rsTimeEvents.getInt("years");
			int nMonths = rsTimeEvents.getInt("months");
			int nDays = rsTimeEvents.getInt("days");
			int nHours = rsTimeEvents.getInt("hours");
			
			if ((nYears==0) && (nMonths==0) && (nDays==0) && (nHours==0))
			{
				
				UtilityFunctions.stdoutwriter.writeln("Problem with time event " + rsTimeEvents.getInt("id"),Logs.ERROR,"DL5.3");
				UtilityFunctions.stdoutwriter.writeln("All time increment values are set to zero, skipping",Logs.ERROR,"DL5.3");
				continue;
			}
				
			
			/*
			 * First time being populated
			 */
			if (rsTimeEvents.getTimestamp("next_datetime") == null)
			{
				calNext.setTime(rsTimeEvents.getTimestamp("start_datetime"));
				calLast.setTime(rsTimeEvents.getTimestamp("start_datetime"));
			}
			else
			{
				calNext.setTime(rsTimeEvents.getTimestamp("next_datetime"));
				calLast.setTime(rsTimeEvents.getTimestamp("next_datetime"));
			}
			
			if (!calNext.after(calCurrent) || (rsTimeEvents.getTimestamp("next_datetime") == null))
			{
				
				//Keep adding cycles until next is after current
				while (!calNext.after(calCurrent))
				{
					calNext.add(Calendar.YEAR,nYears);
					calNext.add(Calendar.MONTH, nMonths);
					calNext.add(Calendar.DAY_OF_YEAR,nDays);
					calNext.add(Calendar.HOUR,nHours);
				}
				
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				String strUpdate = "update time_events set ";
				strUpdate += "last_datetime='" + formatter.format(calLast.getTime()) + "',";
				strUpdate += "next_datetime='" + formatter.format(calNext.getTime()) + "' ";
				strUpdate += "where ";
				strUpdate += "id=" + rsTimeEvents.getInt("id");
				
				dbf.db_update_query(strUpdate);
			}
				
				
			
		}
		
		
		
		
		
		
	}
	
	
  public void checkMessageCount()
  {
	  Calendar calCurrent = Calendar.getInstance();
	  
	  if (calCurrent.get(Calendar.DAY_OF_YEAR) > calMailBegin.get(Calendar.DAY_OF_YEAR))
	  {
		  DataLoad.nMailMessageCount = 0;
		  calMailBegin = calCurrent;
	  }
	  
	  
  }

  
  public void writeKeepAlive()
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
	  
	  
	  
  }


}              


 






