package pikefin.hibernate;

// Generated Jun 18, 2012 7:54:36 AM by Hibernate Tools 3.4.0.CR1

import java.util.Calendar;

/**
 * RepeatType generated by hbm2java
 */
public class RepeatType implements java.io.Serializable {

	private Integer repeatTypeId;
	private String type;
	private Calendar nextTrigger;
	private Integer multiplier;

	public RepeatType() {
	}

	public RepeatType(String type, Calendar nextTrigger, Integer multiplier) {
		this.type = type;
		this.nextTrigger = nextTrigger;
		this.multiplier = multiplier;
	}

	public Integer getRepeatTypeId() {
		return this.repeatTypeId;
	}

	public void setRepeatTypeId(Integer repeatTypeId) {
		this.repeatTypeId = repeatTypeId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Calendar getNextTrigger() {
		return this.nextTrigger;
	}

	public void setNextTrigger(Calendar nextTrigger) {
		this.nextTrigger = nextTrigger;
	}

	public Integer getMultiplier() {
		return this.multiplier;
	}

	public void setMultiplier(Integer multiplier) {
		this.multiplier = multiplier;
	}

}
