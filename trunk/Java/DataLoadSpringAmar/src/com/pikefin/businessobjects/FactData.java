package com.pikefin.businessobjects;

// Generated Jun 18, 2012 7:54:36 AM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;


public class FactData implements java.io.Serializable {

	 private Integer factDataId;
     private BigDecimal value;
     private Integer scale;
     private Boolean manualCorrection;
     private Date dateCollected;
     //TODO need to be converted into relationship
     private int entityId;
     private Integer metricId;
     private String dataGroup;
     private Integer fiscalquarter;
     private Integer fiscalyear;
     private Integer calquarter;
     private Integer calyear;
     private Integer calmonth;
     private Integer day;
     private Batches batchId;
     private Boolean raw;
     private Boolean garbageCollect;
     private Integer metaSetId;
     
     public Integer getFactDataId() {
		return factDataId;
	}
	public void setFactDataId(Integer factDataId) {
		this.factDataId = factDataId;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public Integer getScale() {
		return scale;
	}
	public void setScale(Integer scale) {
		this.scale = scale;
	}
	public Boolean isManualCorrection() {
		return manualCorrection;
	}
	public void setManualCorrection(Boolean manualCorrection) {
		this.manualCorrection = manualCorrection;
	}
	public Date getDateCollected() {
		return dateCollected;
	}
	public void setDateCollected(Date dateCollected) {
		this.dateCollected = dateCollected;
	}
	public int getEntityId() {
		return entityId;
	}
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	public Integer getMetricId() {
		return metricId;
	}
	public void setMetricId(Integer metricId) {
		this.metricId = metricId;
	}
	public String getDataGroup() {
		return dataGroup;
	}
	public void setDataGroup(String dataGroup) {
		this.dataGroup = dataGroup;
	}
	public Integer getFiscalquarter() {
		return fiscalquarter;
	}
	public void setFiscalquarter(Integer fiscalquarter) {
		this.fiscalquarter = fiscalquarter;
	}
	public Integer getFiscalyear() {
		return fiscalyear;
	}
	public void setFiscalyear(Integer fiscalyear) {
		this.fiscalyear = fiscalyear;
	}
	public Integer getCalquarter() {
		return calquarter;
	}
	public void setCalquarter(Integer calquarter) {
		this.calquarter = calquarter;
	}
	public Integer getCalyear() {
		return calyear;
	}
	public void setCalyear(Integer calyear) {
		this.calyear = calyear;
	}
	public Integer getCalmonth() {
		return calmonth;
	}
	public void setCalmonth(Integer calmonth) {
		this.calmonth = calmonth;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Batches getBatchId() {
		return batchId;
	}
	public void setBatchId(Batches batchId) {
		this.batchId = batchId;
	}
	public Boolean isRaw() {
		return raw;
	}
	public void setRaw(Boolean raw) {
		this.raw = raw;
	}
	public Boolean isGarbageCollect() {
		return garbageCollect;
	}
	public void setGarbageCollect(Boolean garbageCollect) {
		this.garbageCollect = garbageCollect;
	}
	public Integer getMetaSetId() {
		return metaSetId;
	}
	public void setMetaSetId(Integer metaSetId) {
		this.metaSetId = metaSetId;
	}
	

}
