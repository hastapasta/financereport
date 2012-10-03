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

import pikefin.Broker;
import pikefin.DataGrab;
import pikefin.UtilityFunctions;
import pikefin.log4jWrapper.Logs;


import com.pikefin.PikefinUtil;
import com.pikefin.RepeatTypeEnum;
import com.pikefin.businessobjects.RepeatType;
import com.pikefin.businessobjects.Schedule;
import com.pikefin.exceptions.GenericException;
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
	private boolean loadHistoricalData = false;
	private boolean debugMode = false;
	private int threadSleepInteval;
	private int maxAllowedThreads;
	private int threadMinimumPriority;
	private int nMailMessageCount;
	private boolean brokerThreadPaused = false;
	private Calendar threadStartDate;
	private Calendar threadEndDate;
	private ArrayList<QueuedJob> waitingJobList;
	private DataGrabExecuter[] runningJobsArray;
	
	
	//  Notification notification;
	
	 public BrokerExecuter(){
		 runningJobsArray=new DataGrabExecuter[this.maxAllowedThreads];
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
		 
		 //this.notification.start();
		 if (!isBrokerThreadPaused()) {
				if (!isLoadHistoricalData() ){
					//starting the infinite loop
					while(true) {    
						try{
						timeEventService.updateTimeEventsForBroker();
						
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
	
	 public DataGrab initiateJob(int nPriority) throws DataAccessException  {
		  //String strDataSet;
		  String strTask;
		  DataGrab dg;
		  //Calendar cal;
		  
		 //Sorting the  
		 Collections.sort(waitingJobList);
			  
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
	  
	public boolean isLoadHistoricalData() {
		return loadHistoricalData;
	}

	public void setLoadHistoricalData(boolean loadHistoricalData) {
		this.loadHistoricalData = loadHistoricalData;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public int getThreadSleepInteval() {
		return threadSleepInteval;
	}

	public void setThreadSleepInteval(int threadSleepInteval) {
		this.threadSleepInteval = threadSleepInteval;
	}

	public int getMaxAllowedThreads() {
		return maxAllowedThreads;
	}

	public void setMaxAllowedThreads(int maxAllowedThreads) {
		this.maxAllowedThreads = maxAllowedThreads;
	}

	public boolean isBrokerThreadPaused() {
		return brokerThreadPaused;
	}

	public void setBrokerThreadPaused(boolean brokerThreadPaused) {
		this.brokerThreadPaused = brokerThreadPaused;
	}
}
