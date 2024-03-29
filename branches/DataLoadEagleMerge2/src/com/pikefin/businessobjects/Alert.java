package com.pikefin.businessobjects;

// Generated Jun 18, 2012 7:54:36 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;


/**
 * Alert generated by hbm2java
 */
public class Alert implements java.io.Serializable, Cloneable{

	private Integer alertId;
	private Double limitValue;
	private Entity alertEntity;
	private Task alertTask;
	private User alertUser;
	private TimeEvent alertTimeEvent;
	private FactData alertInitialFactData;
	private Integer type;
	private Integer notificationCount;
	private Boolean disabled;
	private Groups groupId;
	private FactData alertCurrentFactData;
	private Boolean autoResetPeriod;
	private Boolean autoResetFired;
	private Boolean fired;
	private Boolean emailAlert;
	private Boolean twitterAlert;
	private Integer calyear;
	// many to many relationship with AlertTarget
	private Set<AlertTarget> alertTarget = new HashSet<AlertTarget>(0);
	
	public Alert() {
	}

 @Override
	public Alert clone(){
	 try{
		return (Alert)super.clone();
	 }catch (CloneNotSupportedException e) {
		return null;
	}
	}

	public Integer getAlertId() {
		return this.alertId;
	}

	public void setAlertId(Integer alertId) {
		this.alertId = alertId;
	}

	public Double getLimitValue() {
		return this.limitValue;
	}

	public void setLimitValue(Double limitValue) {
		this.limitValue = limitValue;
	}

	public Entity getAlertEntity() {
		return this.alertEntity;
	}

	public void setAlertEntity(Entity alertEntity) {
		this.alertEntity = alertEntity;
	}

	public Task getAlertTask() {
		return this.alertTask;
	}

	public void setAlertTask(Task alertTask) {
		this.alertTask = alertTask;
	}

	public User getAlertUser() {
		return this.alertUser;
	}

	public void setAlertUser(User alertUser) {
		this.alertUser = alertUser;
	}

	public TimeEvent getAlertTimeEvent() {
		return this.alertTimeEvent;
	}

	public void setAlertTimeEvent(TimeEvent alertTimeEvent) {
		this.alertTimeEvent = alertTimeEvent;
	}

	public FactData getAlertInitialFactData() {
		return this.alertInitialFactData;
	}

	public void setAlertInitialFactData(FactData alertInitialFactData) {
		this.alertInitialFactData = alertInitialFactData;
	}

	public Set<AlertTarget> getAlertTarget() {
		return this.alertTarget;
	}

	public void setAlertTarget(Set<AlertTarget> alertTarget) {
		this.alertTarget = alertTarget;
	}




	public Integer getType() {
		return type;
	}




	public void setType(Integer type) {
		this.type = type;
	}




	public Integer getNotificationCount() {
		return notificationCount;
	}




	public void setNotificationCount(Integer notificationCount) {
		this.notificationCount = notificationCount;
	}




	public Boolean getDisabled() {
		return disabled;
	}




	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}




	public Groups getGroupId() {
		return groupId;
	}




	public void setGroupId(Groups groupId) {
		this.groupId = groupId;
	}




	public FactData getAlertCurrentFactData() {
		return alertCurrentFactData;
	}




	public void setAlertCurrentFactData(FactData alertCurrentFactData) {
		this.alertCurrentFactData = alertCurrentFactData;
	}




	public Boolean getAutoResetPeriod() {
		return autoResetPeriod;
	}




	public void setAutoResetPeriod(Boolean autoResetPeriod) {
		this.autoResetPeriod = autoResetPeriod;
	}




	public Boolean getAutoResetFired() {
		return autoResetFired;
	}




	public void setAutoResetFired(Boolean autoResetFired) {
		this.autoResetFired = autoResetFired;
	}




	public Boolean getFired() {
		return fired;
	}




	public void setFired(Boolean fired) {
		this.fired = fired;
	}




	public Boolean getEmailAlert() {
		return emailAlert;
	}




	public void setEmailAlert(Boolean emailAlert) {
		this.emailAlert = emailAlert;
	}




	public Boolean getTwitterAlert() {
		return twitterAlert;
	}




	public void setTwitterAlert(Boolean twitterAlert) {
		this.twitterAlert = twitterAlert;
	}




	public Integer getCalyear() {
		return calyear;
	}




	public void setCalyear(Integer calyear) {
		this.calyear = calyear;
	}

}
