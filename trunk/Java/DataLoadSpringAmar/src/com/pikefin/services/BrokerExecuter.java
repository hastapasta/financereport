package com.pikefin.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;


import com.pikefin.services.DataLoadService;
import pikefin.log4jWrapper.Logs;
import com.pikefin.ApplicationSetting;
import com.pikefin.PikefinUtil;
import com.pikefin.RepeatTypeEnum;
import com.pikefin.businessobjects.JobQueue;
import com.pikefin.businessobjects.RepeatType;
import com.pikefin.businessobjects.Schedule;
import com.pikefin.businessobjects.Task;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.JobQueueService;
import com.pikefin.services.inter.RepeatTypeService;
import com.pikefin.services.inter.ScheduleService;
import com.pikefin.services.inter.TaskService;
import com.pikefin.services.inter.TimeEventService;



public class BrokerExecuter extends Thread {
	Logger logger = Logger.getLogger(BrokerExecuter.class);
	
	@Autowired
	private TimeEventService timeEventService;
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepeatTypeService repeatService;
	@Autowired
	private NotificationExecuter notification;
	@Autowired
	private JobQueueService jobQueueService;
	
	private int threadMinimumPriority;
	private int nMailMessageCount;
	private boolean brokerThreadPaused = false;
	private Calendar threadStartDate;
	private Calendar threadEndDate;
	private ArrayList<QueuedJob> waitingJobList;
	private DataGrabExecuter[] runningJobsArray;
	
	
	//  Notification notification;
	
	 public BrokerExecuter(){
		 runningJobsArray=new DataGrabExecuter[ApplicationSetting.getInstance().getMaxAllowedThreads()];
		 waitingJobList = new ArrayList<QueuedJob>();
	 }
	
	@Override
	public void run(){
		//Refreshing the waiting job list and runningJobsArray, to clear old objects
		 waitingJobList.clear();
		 PikefinUtil.clearArray(runningJobsArray);
		 threadStartDate=Calendar.getInstance();
		 threadStartDate.set(Calendar.HOUR_OF_DAY, 0);
		 threadStartDate.set(Calendar.MINUTE, 0);
		 threadStartDate.set(Calendar.SECOND, 0);
		 threadStartDate.set(Calendar.MILLISECOND, 0);
		 
		 this.notification.start();
		 if (!isBrokerThreadPaused()) {
				if (!ApplicationSetting.getInstance().isLoadHistoricalData()){
					//starting the infinite loop
					while(true) {    
						try{
						timeEventService.updateTimeEventsForBroker();
						this.getTriggeredJobs();
						
						}catch (GenericException e) {
							logger.error(e.getErrorCode()+"\n "+e.getErrorMessage()+"\n"+e.getErrorDescription());
						}
					}
				}
		 }
	}

	
	public void getTriggeredJobs(){
		RepeatType repeatTypeNone;
		Calendar currentTime=Calendar.getInstance();
		List<Schedule> schedulesToModifyRepeatType=new ArrayList<Schedule>();
		try{
		 repeatTypeNone=repeatService.loadRepeatTypeNone();
		List<Schedule> schedules= scheduleService.loadAllSchedulesForBrokerExecuter();
		for(Schedule tempSchedule:schedules){
			//check if task is being excluded
			if  (tempSchedule.getTask()!=null && taskService.isTaskExcluded(tempSchedule.getTask().getTaskId())){
					continue;
			}
			//check if job is already in queue, don't add it again if it is
			boolean bInQ=false;
			for(QueuedJob queuedJob:waitingJobList){
				if(queuedJob.getTask().equals(tempSchedule.getTask())){
					bInQ=true;
					break; 
				}
			}
			if (!bInQ) {
				QueuedJob queueNewJob=new QueuedJob(tempSchedule, tempSchedule.getTask(), tempSchedule.getRepeatType(), tempSchedule.getPriority(), Calendar.getInstance(),tempSchedule.getVerifyMode());
				waitingJobList.add(queueNewJob);
			}
			if(RepeatTypeEnum.RUNONCE.toString().equals(tempSchedule.getRepeatType().getType())) {
				tempSchedule.setRepeatType(repeatTypeNone);
				schedulesToModifyRepeatType.add(tempSchedule);	
			}
			
			
		}
		scheduleService.updateSchedulesBatch(schedulesToModifyRepeatType);
		repeatService.updateRepeatTypesForNextTrigger(currentTime);
		}catch (GenericException genException) {
			logger.debug("Error in retrieving Triggered Job"+genException.getErrorCode()+" "+genException.getErrorDescription());
		}
	}
	
	
	  public void writeJobQueueIntoDB() {
		  try{
			  
		  jobQueueService.deleteAllJobQueues();
		  for (int i=0;i<runningJobsArray.length;i++) {
			  if (runningJobsArray[i] != null) {
				  JobQueue jq = new JobQueue(); 
				  
				  jq.setTask(runningJobsArray[i].getCurrentTask());
				  Thread t = (Thread)runningJobsArray[i];
				  
				  jq.setStatus(runningJobsArray[i].getState().toString());
				  jq.setStartTime(runningJobsArray[i].calJobProcessingStart.getTime());
				  //jq.setPriority(runningJobsArray[i].nPriority);
				  
				  dbf.dbHibernateSaveQuery(jq);
				  
				  
				  /*query = "insert into job_queue (task_id,status,start_time) values (";
				  query += runningJobsArray[i].nCurTask + ",'";
				  query += runningJobsArray[i].getState().toString() + "','";
				  query += formatter.format(runningJobsArray[i].calJobProcessingStart.getTime()) + "')";*/
				  // dbf.db_update_query(query);
				  //dbf.dbSpringUpdateQuery(query);
				  UtilityFunctions.stdoutwriter.writeln("Status of thread " + runningJobsArray[i].getName() + ": " + 
						  runningJobsArray[i].getState().toString(),Logs.THREAD,"DL2");
			  }
		  }
		  for (int k=0;k<waitingJobList.size();k++) {
			  JobQueue jq = new JobQueue();
			  
			  jq.setTaskId(waitingJobList.get(k).task_id);
			  jq.setStatus("QUEUED");
			  jq.setQueuedTime(waitingJobList.get(k).queued_time);
			  jq.setPriority(waitingJobList.get(k).priority);
			  
			  dbf.dbHibernateSaveQuery(jq);
			  
			  
			  
			  
			  /*query = "insert into job_queue (task_id,status,queued_time,priority) values (" + waitingJobList.get(k).task_id + 
			  ",'QUEUED','" + waitingJobList.get(k).queued_time + "'," + waitingJobList.get(k).priority + ")";*/
			  //dbf.db_update_query(query);
			  //dbf.dbSpringUpdateQuery(query);
		  }
		  }catch (GenericException e) {
			  logger.error("Problem while writing queue into db-"+e.getErrorCode()+" "+e.getErrorMessage()+" "+e.getErrorDescription());

		}
	  }

	/**
	 * 
	 */
	public void executeJobs(){
		  //Clean out terminated threads and count open slots 
		  /*
		   * We are going to reserve slot 1 for high priority jobs
		   */
		
		  for (int j=0;j<runningJobsArray.length;j++) {
			  
			  if (runningJobsArray[j] == null) {
				  if (j==1) {
					  runningJobsArray[j] = initiateJob(this.threadMinimumPriority);
				  }
				  else
					  runningJobsArray[j] = initiateJob(0);	  
				  
			  }
				  
		  }	
	}
	/**
	 * Check if the Queued job is already not running then it initiates the job and remove it from waiting job list
	 * @param nPriority
	 * @return
	 * @throws DataAccessException
	 */
	 public DataGrabExecuter initiateJob(int nPriority) throws DataAccessException  {
		 
		  Task task;
		  DataGrabExecuter dg;
		  //Sorting the  Queued Jobs
		 Collections.sort(waitingJobList);
		 boolean isAlreadyRunning;
			  for (int i=0;i<waitingJobList.size();i++) {
				  isAlreadyRunning = false;
				  task = waitingJobList.get(i).getTask();
				  for (int j=0;j<runningJobsArray.length;j++) {
					  if (runningJobsArray[j] != null)  {
						  if (runningJobsArray[j].getCurrentTask().getTaskId() == task.getTaskId()) {
							  logger.info("Task in waiting queue already running so won't get moved to run queue (task id: " + runningJobsArray[j].getCurrentTask().getTaskId());
							  isAlreadyRunning=true;
							  break;
						  }
					  }
				  }
				  
				  if (isAlreadyRunning==false) {
					  if (waitingJobList.get(i).getPriority() < nPriority) {
						  logger.info("Not initiating schedule because one thread is reserved for minimum priority of " + nPriority + ". Task: " + waitingJobList.get(i).getTask().getTaskId() + ", Priority: " + waitingJobList.get(i).getPriority());
						  return null;
					  }
					  dg = new DataGrabExecuter(task,0,waitingJobList.get(i).getSchedule(),waitingJobList.get(i).getRepeatType(),waitingJobList.get(i).isVerifyMode(),waitingJobList.get(i).getPriority());
					  dg.start();
					  logger.info("Initiated DataGrab thread " + dg.getName());
					 waitingJobList.remove(i);
					 return dg;
				  
				  }
			  
			  }
				
			  return null;
	  
	  }
	  
	

	

	public boolean isBrokerThreadPaused() {
		return brokerThreadPaused;
	}

	public void setBrokerThreadPaused(boolean brokerThreadPaused) {
		this.brokerThreadPaused = brokerThreadPaused;
	}
}
