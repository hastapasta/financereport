package com.pikefin.businessobjects;


import java.util.Date;


public class LogAlert implements java.io.Serializable {

	private Integer logAlertId;
	private Boolean emailSent;
	private User logAlertUser;
	private FactData beforeFactData;
	private FactData afterFactData;
	private Entity logAlertEntity;
	private Alert logAlertAlert;
    private Integer type;
    private Date dateTimeFired;
    private String obsoleteFrequency;
    private Double limitValue;
    private Double obsoleteLimitAdjustment;
    private Double diff;
    
	public Integer getLogAlertId() {
		return logAlertId;
	}
	public void setLogAlertId(Integer logAlertId) {
		this.logAlertId = logAlertId;
	}
	public Boolean getEmailSent() {
		return emailSent;
	}
	public void setEmailSent(Boolean emailSent) {
		this.emailSent = emailSent;
	}
	public User getLogAlertUser() {
		return logAlertUser;
	}
	public void setLogAlertUser(User logAlertUser) {
		this.logAlertUser = logAlertUser;
	}
	public FactData getBeforeFactData() {
		return beforeFactData;
	}
	public void setBeforeFactData(FactData beforeFactData) {
		this.beforeFactData = beforeFactData;
	}
	public FactData getAfterFactData() {
		return afterFactData;
	}
	public void setAfterFactData(FactData afterFactData) {
		this.afterFactData = afterFactData;
	}
	public Entity getLogAlertEntity() {
		return logAlertEntity;
	}
	public void setLogAlertEntity(Entity logAlertEntity) {
		this.logAlertEntity = logAlertEntity;
	}
	public Alert getLogAlertAlert() {
		return logAlertAlert;
	}
	public void setLogAlertAlert(Alert logAlertAlert) {
		this.logAlertAlert = logAlertAlert;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getDateTimeFired() {
		return dateTimeFired;
	}
	public void setDateTimeFired(Date dateTimeFired) {
		this.dateTimeFired = dateTimeFired;
	}
	public String getObsoleteFrequency() {
		return obsoleteFrequency;
	}
	public void setObsoleteFrequency(String obsoleteFrequency) {
		this.obsoleteFrequency = obsoleteFrequency;
	}
	public Double getLimitValue() {
		return limitValue;
	}
	public void setLimitValue(Double limitValue) {
		this.limitValue = limitValue;
	}
	public Double getObsoleteLimitAdjustment() {
		return obsoleteLimitAdjustment;
	}
	public void setObsoleteLimitAdjustment(Double obsoleteLimitAdjustment) {
		this.obsoleteLimitAdjustment = obsoleteLimitAdjustment;
	}
	public Double getDiff() {
		return diff;
	}
	public void setDiff(Double diff) {
		this.diff = diff;
	}
  
	
}
