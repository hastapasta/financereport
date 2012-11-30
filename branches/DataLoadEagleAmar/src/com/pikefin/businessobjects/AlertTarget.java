package com.pikefin.businessobjects;

import java.util.HashSet;
import java.util.Set;



public class AlertTarget implements java.io.Serializable {

	private Integer alertTargetId;
	private String address;
	private Integer type;
	private SecurityAccounts securityAccountId;
    private User userId;
    private Boolean bulkEmail;
    private Integer maxNotifications;
    private Integer tweetLimit;
    private Set<Alert> alerts=new HashSet<Alert>();

	public AlertTarget() {
	}

	

	public Integer getAlertTargetId() {
		return this.alertTargetId;
	}

	public void setAlertTargetId(Integer alertTargetId) {
		this.alertTargetId = alertTargetId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}



	public SecurityAccounts getSecurityAccountId() {
		return securityAccountId;
	}



	public void setSecurityAccountId(SecurityAccounts securityAccountId) {
		this.securityAccountId = securityAccountId;
	}



	public User getUserId() {
		return userId;
	}



	public void setUserId(User userId) {
		this.userId = userId;
	}



	public Boolean getBulkEmail() {
		return bulkEmail;
	}



	public void setBulkEmail(Boolean bulkEmail) {
		this.bulkEmail = bulkEmail;
	}



	public Integer getMaxNotifications() {
		return maxNotifications;
	}



	public void setMaxNotifications(Integer maxNotifications) {
		this.maxNotifications = maxNotifications;
	}



	public Integer getTweetLimit() {
		return tweetLimit;
	}



	public void setTweetLimit(Integer tweetLimit) {
		this.tweetLimit = tweetLimit;
	}



	public Set<Alert> getAlerts() {
		return alerts;
	}



	public void setAlerts(Set<Alert> alerts) {
		this.alerts = alerts;
	}

}
