package com.pikefin.businessobjects;


import java.util.Date;


public class TimeEvent implements java.io.Serializable {

	private Integer timeEventId;
	private String name;
	private Integer years;
	private Integer months;
	private Integer days;
	private Integer hours;
	private Integer delay;
	private Date startDatetime;
	private Date lastDatetime;
	private Date nextDatetime;
	private String description;
	public Integer getTimeEventId() {
		return timeEventId;
	}
	public void setTimeEventId(Integer timeEventId) {
		this.timeEventId = timeEventId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getYears() {
		return years;
	}
	public void setYears(Integer years) {
		this.years = years;
	}
	public Integer getMonths() {
		return months;
	}
	public void setMonths(Integer months) {
		this.months = months;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getHours() {
		return hours;
	}
	public void setHours(Integer hours) {
		this.hours = hours;
	}
	public Integer getDelay() {
		return delay;
	}
	public void setDelay(Integer delay) {
		this.delay = delay;
	}
	public Date getStartDatetime() {
		return startDatetime;
	}
	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}
	public Date getLastDatetime() {
		return lastDatetime;
	}
	public void setLastDatetime(Date lastDatetime) {
		this.lastDatetime = lastDatetime;
	}
	public Date getNextDatetime() {
		return nextDatetime;
	}
	public void setNextDatetime(Date nextDatetime) {
		this.nextDatetime = nextDatetime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
}
