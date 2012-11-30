package com.pikefin.businessobjects;


import java.util.Date;

public class JobQueue implements java.io.Serializable {

	private Integer jobQueueId;
	private Task task;
	private String status;
	private Integer priority=0;
    private Date startTime;
    private Date queuedTime;
	public Integer getJobQueueId() {
		return jobQueueId;
	}
	public void setJobQueueId(Integer jobQueueId) {
		this.jobQueueId = jobQueueId;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getQueuedTime() {
		return queuedTime;
	}
	public void setQueuedTime(Date queuedTime) {
		this.queuedTime = queuedTime;
	}

	

	
}
