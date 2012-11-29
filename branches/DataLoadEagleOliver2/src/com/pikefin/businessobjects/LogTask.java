package com.pikefin.businessobjects;

import java.util.Date;


public class LogTask implements java.io.Serializable {

	private Integer logTaskId;
	// Made it One to One
	private Task task;
	private Date jobProcessStart;
	private Date jobProcessEnd;
	private Date alertProcessStart;
	private Date alertProcessEnd;
	private Batches batch;
	private RepeatType repeatType;
	private Schedule schedule;
	private Date stage1Start;
	private Date stage1End;
	private Date stage2Start;
	private Date stage2End;
	private Boolean verifyMode;
	public Integer getLogTaskId() {
		return logTaskId;
	}
	public void setLogTaskId(Integer logTaskId) {
		this.logTaskId = logTaskId;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Date getJobProcessStart() {
		return jobProcessStart;
	}
	public void setJobProcessStart(Date jobProcessStart) {
		this.jobProcessStart = jobProcessStart;
	}
	public Date getJobProcessEnd() {
		return jobProcessEnd;
	}
	public void setJobProcessEnd(Date jobProcessEnd) {
		this.jobProcessEnd = jobProcessEnd;
	}
	public Date getAlertProcessStart() {
		return alertProcessStart;
	}
	public void setAlertProcessStart(Date alertProcessStart) {
		this.alertProcessStart = alertProcessStart;
	}
	public Date getAlertProcessEnd() {
		return alertProcessEnd;
	}
	public void setAlertProcessEnd(Date alertProcessEnd) {
		this.alertProcessEnd = alertProcessEnd;
	}
	public Batches getBatch() {
		return batch;
	}
	public void setBatch(Batches batch) {
		this.batch = batch;
	}
	public RepeatType getRepeatType() {
		return repeatType;
	}
	public void setRepeatType(RepeatType repeatType) {
		this.repeatType = repeatType;
	}
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	public Date getStage1Start() {
		return stage1Start;
	}
	public void setStage1Start(Date stage1Start) {
		this.stage1Start = stage1Start;
	}
	public Date getStage1End() {
		return stage1End;
	}
	public void setStage1End(Date stage1End) {
		this.stage1End = stage1End;
	}
	public Date getStage2Start() {
		return stage2Start;
	}
	public void setStage2Start(Date stage2Start) {
		this.stage2Start = stage2Start;
	}
	public Date getStage2End() {
		return stage2End;
	}
	public void setStage2End(Date stage2End) {
		this.stage2End = stage2End;
	}
	public Boolean getVerifyMode() {
		return verifyMode;
	}
	public void setVerifyMode(Boolean verifyMode) {
		this.verifyMode = verifyMode;
	}

	
}
