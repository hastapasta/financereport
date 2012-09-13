package com.pikefin.businessobjects;

import java.util.Date;


public class RepeatType implements java.io.Serializable {

	private Integer repeatTypeId;
	private String type;
	private Integer multiplier;
	private Integer obsoleteGroup;
	private Integer monthday;
	private Integer weekday;
	private Date time;
	private Date start;
	private Date nextTrigger;
	private String description;
	public Integer getRepeatTypeId() {
		return repeatTypeId;
	}
	public void setRepeatTypeId(Integer repeatTypeId) {
		this.repeatTypeId = repeatTypeId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(Integer multiplier) {
		this.multiplier = multiplier;
	}
	public Integer getObsoleteGroup() {
		return obsoleteGroup;
	}
	public void setObsoleteGroup(Integer obsoleteGroup) {
		this.obsoleteGroup = obsoleteGroup;
	}
	public Integer getMonthday() {
		return monthday;
	}
	public void setMonthday(Integer monthday) {
		this.monthday = monthday;
	}
	public Integer getWeekday() {
		return weekday;
	}
	public void setWeekday(Integer weekday) {
		this.weekday = weekday;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getNextTrigger() {
		return nextTrigger;
	}
	public void setNextTrigger(Date nextTrigger) {
		this.nextTrigger = nextTrigger;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
     
	
}
