package pikefin.hibernate;

// Generated Jun 13, 2012 10:24:38 AM by Hibernate Tools 3.4.0.CR1

import java.util.Calendar;

/**
 * JobQueue generated by hbm2java
 */
public class JobQueue implements java.io.Serializable {

	private Integer jobQueueId;
	private String startTime;
	private Integer taskId;
	private String status;
	private Integer priority;
	private Calendar queuedTime;

	public JobQueue() {
	}

	public JobQueue(Integer priority) {
		this.priority = priority;
	}

	public JobQueue(String startTime, Integer taskId, String status,
			Integer priority, Calendar queuedTime) {
		this.startTime = startTime;
		this.taskId = taskId;
		this.status = status;
		this.priority = priority;
		this.queuedTime = queuedTime;
	}

	public Integer getJobQueueId() {
		return this.jobQueueId;
	}

	public void setJobQueueId(Integer jobQueueId) {
		this.jobQueueId = jobQueueId;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Calendar getQueuedTime() {
		return this.queuedTime;
	}

	public void setQueuedTime(Calendar queuedTime) {
		this.queuedTime = queuedTime;
	}

}
