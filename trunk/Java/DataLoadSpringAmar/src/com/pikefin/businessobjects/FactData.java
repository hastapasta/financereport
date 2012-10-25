package com.pikefin.businessobjects;



import java.util.Date;
import java.lang.Double;

public class FactData implements java.io.Serializable {

	 private Integer factDataId;
     private Double value;
     private Integer scale;
     private Boolean manualCorrection=false;
     private Date dateCollected;
     private Entity entity;
     private Metric metric;
     private String dataGroup;
     private Integer fiscalquarter;
     private Integer fiscalyear;
     private Integer calquarter;
     private Integer calyear;
     private Integer calmonth;
     private Integer day;
     private Batches batch;
     private Boolean raw=true;
     private Boolean garbageCollect=false;
     private MetaSets metaSet;
	public Integer getFactDataId() {
		return factDataId;
	}
	public void setFactDataId(Integer factDataId) {
		this.factDataId = factDataId;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Integer getScale() {
		return scale;
	}
	public void setScale(Integer scale) {
		this.scale = scale;
	}
	public Boolean getManualCorrection() {
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
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public Metric getMetric() {
		return metric;
	}
	public void setMetric(Metric metric) {
		this.metric = metric;
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
	public Batches getBatch() {
		return batch;
	}
	public void setBatch(Batches batch) {
		this.batch = batch;
	}
	public Boolean getRaw() {
		return raw;
	}
	public void setRaw(Boolean raw) {
		this.raw = raw;
	}
	public Boolean getGarbageCollect() {
		return garbageCollect;
	}
	public void setGarbageCollect(Boolean garbageCollect) {
		this.garbageCollect = garbageCollect;
	}
	public MetaSets getMetaSet() {
		return metaSet;
	}
	public void setMetaSet(MetaSets metaSet) {
		this.metaSet = metaSet;
	}
	     


}
