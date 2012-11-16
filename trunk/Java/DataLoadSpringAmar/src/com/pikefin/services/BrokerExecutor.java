package com.pikefin.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


import pikefin.log4jWrapper.Logs;
import com.pikefin.ApplicationSetting;
import com.pikefin.PikefinUtil;
import com.pikefin.RepeatTypeEnum;
import com.pikefin.TaskMetricsEnum;
import com.pikefin.businessobjects.JobQueue;
import com.pikefin.businessobjects.LogTask;
import com.pikefin.businessobjects.RepeatType;
import com.pikefin.businessobjects.Schedule;
import com.pikefin.businessobjects.Task;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.JobQueueService;
import com.pikefin.services.inter.LogTaskService;
import com.pikefin.services.inter.RepeatTypeService;
import com.pikefin.services.inter.ScheduleService;
import com.pikefin.services.inter.TaskService;
import com.pikefin.services.inter.TimeEventService;


@Service
public class BrokerExecutor extends Thread {
	Logger logger = Logger.getLogger(BrokerExecutor.class);
	
	@Autowired
	private TimeEventService timeEventService;
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepeatTypeService repeatService;
	@Autowired
	private NotificationExecutor notification;
	@Autowired
	private JobQueueService jobQueueService;
	@Autowired
	private LogTaskService logTaskService;;
	
	private int threadMinimumPriority;
	private int nMailMessageCount;
	private boolean brokerThreadPaused = false;
	private Calendar threadStartDate;
	private Calendar threadEndDate;
	private ArrayList<QueuedJob> waitingJobList;
	private DataGrabExecutor[] runningJobsArray;
	
	
	//  Notification notification;
	
	 public BrokerExecutor(){
		
	 }
	
	@Override
	public void run(){
		//Refreshing the waiting job list and runningJobsArray, to clear old objects
		 runningJobsArray=new DataGrabExecutor[ApplicationSetting.getInstance().getMaxAllowedThreads()];
		 waitingJobList = new ArrayList<QueuedJob>();
		waitingJobList.clear();
		 PikefinUtil.clearArray(runningJobsArray);
		 threadStartDate=Calendar.getInstance();
		 threadStartDate.set(Calendar.HOUR_OF_DAY, 0);
		 threadStartDate.set(Calendar.MINUTE, 0);
		 threadStartDate.set(Calendar.SECOND, 0);
		 threadStartDate.set(Calendar.MILLISECOND, 0);
			if (ApplicationSetting.getInstance().isDebugMode())
				ApplicationSetting.getInstance().getStdoutwriter().writeln("RUNNING IN DEBUG MODE",Logs.STATUS1,"DL18.5");
			else
				ApplicationSetting.getInstance().getStdoutwriter().writeln("RUNNING IN LIVE (NON DEBUG) MODE",Logs.STATUS1,"DL19.5");
			if (ApplicationSetting.getInstance().isLoadHistoricalData()==true)
				ApplicationSetting.getInstance().getStdoutwriter().writeln("LOADING HISTORICAL DATA",Logs.STATUS1,"DL20");
			else
				ApplicationSetting.getInstance().getStdoutwriter().writeln("NOT LOADING HISTORICAL DATA",Logs.STATUS1,"DL21");
			ApplicationSetting.getInstance().getStdoutwriter().writeln("MAXIMUM # OF DATAGRAB THREADS: " + ApplicationSetting.getInstance().getMaxAllowedThreads(),Logs.STATUS1,"DL19");
	//TODO commented notification thread for now		
	 
		 this.notification.start();
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Started Notification Thread",Logs.STATUS1,"DL2.727");
		 if (!isBrokerThreadPaused()) {
				if (!ApplicationSetting.getInstance().isLoadHistoricalData()){
					//starting the infinite loop
					while(true) {    
						try{
						timeEventService.updateTimeEventsForBroker();
						this.getTriggeredJobs();
						this.executeJobs();
						this.writeJobQueueIntoDB();
						sleep(ApplicationSetting.getInstance().getThreadSleepInteval()*1000);
						cleanTerminatedJobs();
						checkMessageCount();
						}catch (GenericException e) {
							ApplicationSetting.getInstance().getStdoutwriter().writeln(e.getErrorCode()+"\n "+e.getErrorMessage()+"\n"+e.getErrorDescription(),Logs.ERROR,"DL10");
							ApplicationSetting.getInstance().getStdoutwriter().writeln(e);

						}catch(InterruptedException ie) {
							//NDC.pop();
							break;
							
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
		 }
	}

	/**
	 * Loads the jobs for trigger and adds to the waiting job queue
	 */
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
				  jq= jobQueueService.saveJobQueueInfo(jq);
				 
				  ApplicationSetting.getInstance().getStdoutwriter().writeln("Status of thread " + runningJobsArray[i].getName() + ": " + 
						  runningJobsArray[i].getState().toString(),Logs.THREAD,"DL2");
			  }
		  }
		  for (int k=0;k<waitingJobList.size();k++) {
			  JobQueue jq = new JobQueue();
			  
			  jq.setTask(waitingJobList.get(k).getTask());
			  jq.setStatus("QUEUED");
			  jq.setQueuedTime(waitingJobList.get(k).getQueuedInTime().getTime());
			  jq.setPriority(waitingJobList.get(k).getPriority());
			  jq= jobQueueService.saveJobQueueInfo(jq);
			  
		  }
		  }catch (GenericException e) {
			  ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem while writing queue into db-"+e.getErrorCode()+" "+e.getErrorMessage()+" "+e.getErrorDescription(),Logs.ERROR,"DL10");
			  ApplicationSetting.getInstance().getStdoutwriter().writeln(e);

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
	 public DataGrabExecutor initiateJob(int nPriority) throws DataAccessException  {
		 
		  Task task;
		  DataGrabExecutor dg;
		  //Sorting the  Queued Jobs
		 Collections.sort(waitingJobList);
		 boolean isAlreadyRunning;
			  for (int i=0;i<waitingJobList.size();i++) {
				  isAlreadyRunning = false;
				  task = waitingJobList.get(i).getTask();
				  for (int j=0;j<runningJobsArray.length;j++) {
					  if (runningJobsArray[j] != null)  {
						  if (runningJobsArray[j].getCurrentTask().getTaskId() == task.getTaskId()) {
							  ApplicationSetting.getInstance().getStdoutwriter().writeln("Task in waiting queue already running so won't get moved to run queue (task id: " + runningJobsArray[j].getCurrentTask().getTaskId(), Logs.STATUS1, "DL3.99");
							  logger.info("Task in waiting queue already running so won't get moved to run queue (task id: " + runningJobsArray[j].getCurrentTask().getTaskId());
							  isAlreadyRunning=true;
							  break;
						  }
					  }
				  }
				  
				  if (isAlreadyRunning==false) {
					  if (waitingJobList.get(i).getPriority() < nPriority) {
						  ApplicationSetting.getInstance().getStdoutwriter().writeln("Not initiating schedule because one thread is reserved for minimum priority of " + nPriority + ". Task: " + waitingJobList.get(i).getTask().getTaskId() + ", Priority: " + waitingJobList.get(i).getPriority(), Logs.STATUS1, "DL4.36");
						  return null;
					  }
					  dg = new DataGrabExecutor(task,0,waitingJobList.get(i).getSchedule(),waitingJobList.get(i).getRepeatType(),waitingJobList.get(i).isVerifyMode(),waitingJobList.get(i).getPriority());
					  dg.start();
					  ApplicationSetting.getInstance().getStdoutwriter().writeln("Initiated DataGrab thread " + dg.getName(),Logs.THREAD,"DL4");
					  waitingJobList.remove(i);
					  
					 return dg;
				  
				  }
			  
			  }
				
			  return null;
	  
	  }
	  
	

	 public void cleanTerminatedJobs() throws DataAccessException  {
		 
		   for (int i=0;i<runningJobsArray.length;i++) {
			  if (runningJobsArray[i] != null) {
				  if (Thread.State.TERMINATED==runningJobsArray[i].getState()) {
					 
					  updateJobStats(runningJobsArray[i]);
					  ApplicationSetting.getInstance().getStdoutwriter().writeln("Cleaning up thread " + runningJobsArray[i].getName(),Logs.THREAD,"DL3");
					  runningJobsArray[i] = null;
					  
				  }
			  }
		  }
	  }

	 public void updateJobStats(DataGrabExecutor dg) {
		    if (ApplicationSetting.getInstance().isLoadHistoricalData()!= true) { 

			  try {
				  LogTask logTask = new LogTask();
				  logTask.setTask(dg.getCurrentTask());
				  logTask.setBatch(dg.getCurrentBatche());
				  logTask.setRepeatType(dg.getRepeatType());
				  logTask.setSchedule(dg.getSchedule());
				  logTask.setVerifyMode(dg.isVerifyMode());
				  Calendar jobProcessStart=dg.getTaskMetric(TaskMetricsEnum.JOB_START);
				  logTask.setJobProcessStart(jobProcessStart!=null?jobProcessStart.getTime():null);
				  Calendar jobProcessEnd=dg.getTaskMetric(TaskMetricsEnum.JOB_END);
				  logTask.setJobProcessEnd(jobProcessEnd!=null?jobProcessEnd.getTime():null);
				  Calendar alertProcessStart=dg.getTaskMetric(TaskMetricsEnum.ALERT_START);
				  logTask.setAlertProcessStart(alertProcessStart!=null?alertProcessStart.getTime():null);
				  Calendar alertProcessEnd=dg.getTaskMetric(TaskMetricsEnum.ALERT_END);
				  logTask.setAlertProcessEnd(alertProcessEnd!=null?alertProcessEnd.getTime():null);
				  Calendar stage1Start=dg.getTaskMetric(TaskMetricsEnum.STAGE1_START);
				  logTask.setStage1Start(stage1Start!=null?stage1Start.getTime():null);
				  Calendar stage1End=dg.getTaskMetric(TaskMetricsEnum.STAGE1_END);
				  logTask.setStage1End(stage1End!=null?stage1End.getTime():null);
				  Calendar stage2Start=dg.getTaskMetric(TaskMetricsEnum.STAGE2_START);
				  logTask.setStage2Start(stage2Start!=null?stage2Start.getTime():null);
				  Calendar stage2End=dg.getTaskMetric(TaskMetricsEnum.STAGE2_END);
				  logTask.setStage2End(stage2End!=null?stage2End.getTime():null);
				  logTask= logTaskService.saveLogTaskInfo(logTask);
						
			  }
			  
		
			  catch (GenericException e) {
				  ApplicationSetting.getInstance().getStdoutwriter().writeln("Error in updating Log Task data-"+e.getErrorCode()+" "+e.getErrorMessage()+" "+e.getErrorDescription(),Logs.ERROR,"DL2.57");
				  ApplicationSetting.getInstance().getStdoutwriter().writeln(e);
			  }
			 
		  }

		  
	  }
	 
	 public void checkMessageCount() {
		  Calendar calCurrent = Calendar.getInstance();
		  
		  if (calCurrent.get(Calendar.DAY_OF_YEAR) > threadStartDate.get(Calendar.DAY_OF_YEAR)) {
			  this.nMailMessageCount = 0;
			  threadStartDate = calCurrent;
		  }
		  
		  
	  }
	public boolean isBrokerThreadPaused() {
		return brokerThreadPaused;
	}

	public void setBrokerThreadPaused(boolean brokerThreadPaused) {
		this.brokerThreadPaused = brokerThreadPaused;
	}
}
