package pikefin.hibernate;

// Generated Jun 18, 2012 7:54:36 AM by Hibernate Tools 3.4.0.CR1

import java.util.Calendar;

/**
 * TimeEvent generated by hbm2java
 */
public class TimeEvent implements java.io.Serializable {

	private Integer timeEventId;
	private String name;
	private Integer years;
	private Integer months;
	private Integer days;
	private Integer hours;
	private Integer delay;
	private Calendar startDateTime;
	private Calendar lastDateTime;
	private Calendar nextDateTime;

	public TimeEvent() {
	}

	public TimeEvent(String name, Integer years, Integer months, Integer days,
			Integer hours, Integer delay, Calendar startDateTime,
			Calendar lastDateTime, Calendar nextDateTime) {
		this.name = name;
		this.years = years;
		this.months = months;
		this.days = days;
		this.hours = hours;
		this.delay = delay;
		this.startDateTime = startDateTime;
		this.lastDateTime = lastDateTime;
		this.nextDateTime = nextDateTime;
	}

	public Integer getTimeEventId() {
		return this.timeEventId;
	}

	public void setTimeEventId(Integer timeEventId) {
		this.timeEventId = timeEventId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getYears() {
		return this.years;
	}

	public void setYears(Integer years) {
		this.years = years;
	}

	public Integer getMonths() {
		return this.months;
	}

	public void setMonths(Integer months) {
		this.months = months;
	}

	public Integer getDays() {
		return this.days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getHours() {
		return this.hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getDelay() {
		return this.delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Calendar getStartDateTime() {
		return this.startDateTime;
	}

	public void setStartDateTime(Calendar startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Calendar getLastDateTime() {
		return this.lastDateTime;
	}

	public void setLastDateTime(Calendar lastDateTime) {
		this.lastDateTime = lastDateTime;
	}

	public Calendar getNextDateTime() {
		return this.nextDateTime;
	}

	public void setNextDateTime(Calendar nextDateTime) {
		this.nextDateTime = nextDateTime;
	}

}