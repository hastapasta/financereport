package pikefin.hibernate;

// Generated Jun 13, 2012 10:24:38 AM by Hibernate Tools 3.4.0.CR1

import java.util.Calendar;

/**
 * FactData generated by hbm2java
 */
public class FactData implements java.io.Serializable {

	private Integer factDataId;
	private float value;
	private Calendar dateCollected;

	public FactData() {
	}

	public FactData(float value, Calendar dateCollected) {
		this.value = value;
		this.dateCollected = dateCollected;
	}

	public Integer getFactDataId() {
		return this.factDataId;
	}

	public void setFactDataId(Integer factDataId) {
		this.factDataId = factDataId;
	}

	public float getValue() {
		return this.value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public Calendar getDateCollected() {
		return this.dateCollected;
	}

	public void setDateCollected(Calendar dateCollected) {
		this.dateCollected = dateCollected;
	}

}