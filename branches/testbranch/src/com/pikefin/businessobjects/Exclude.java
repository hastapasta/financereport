package com.pikefin.businessobjects;

// Generated Jun 18, 2012 7:54:36 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;


public class Exclude implements java.io.Serializable {

	private Integer excludeId;
	private Task task;
	private Integer beginDay;
	private String beginTime;
	private Integer endDay;
	private String endTime;
	private Integer type;
	private Date oneTimeDate;
	private TimeEvent obsoluteTimeEvent;
	
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Integer getBeginDay() {
		return beginDay;
	}
	public void setBeginDay(Integer beginDay) {
		this.beginDay = beginDay;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public Integer getEndDay() {
		return endDay;
	}
	public void setEndDay(Integer endDay) {
		this.endDay = endDay;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getOneTimeDate() {
		return oneTimeDate;
	}
	public void setOneTimeDate(Date oneTimeDate) {
		this.oneTimeDate = oneTimeDate;
	}
	public TimeEvent getObsoluteTimeEvent() {
		return obsoluteTimeEvent;
	}
	public void setObsoluteTimeEvent(TimeEvent obsoluteTimeEvent) {
		this.obsoluteTimeEvent = obsoluteTimeEvent;
	}
	public Integer getExcludeId() {
		return excludeId;
	}
	public void setExcludeId(Integer excludeId) {
		this.excludeId = excludeId;
	}

	

}
