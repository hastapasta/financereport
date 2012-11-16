package com.pikefin.businessobjects;

import java.util.Date;


public class Schedule implements java.io.Serializable {

	private Integer scheduleId;
	private Integer priority;
	private Boolean verifyMode;
	private Task task;
	private RepeatType repeatType;
	private String obsoleteDataSet;
	private Date startDate;
	private Date obsoleteLastRun;
	private Boolean obsoleteRunOnce;
	private Boolean configureNotification;
	
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Boolean getVerifyMode() {
		return verifyMode;
	}
	public void setVerifyMode(Boolean verifyMode) {
		this.verifyMode = verifyMode;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public RepeatType getRepeatType() {
		return repeatType;
	}
	public void setRepeatType(RepeatType repeatType) {
		this.repeatType = repeatType;
	}
	public String getObsoleteDataSet() {
		return obsoleteDataSet;
	}
	public void setObsoleteDataSet(String obsoleteDataSet) {
		this.obsoleteDataSet = obsoleteDataSet;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getObsoleteLastRun() {
		return obsoleteLastRun;
	}
	public void setObsoleteLastRun(Date obsoleteLastRun) {
		this.obsoleteLastRun = obsoleteLastRun;
	}
	public Boolean getObsoleteRunOnce() {
		return obsoleteRunOnce;
	}
	public void setObsoleteRunOnce(Boolean obsoleteRunOnce) {
		this.obsoleteRunOnce = obsoleteRunOnce;
	}
	public Boolean isConfigureNotification() {
		return configureNotification;
	}
	public void setConfigureNotification(Boolean configureNotification) {
		this.configureNotification = configureNotification;
	}
	  
	
}
