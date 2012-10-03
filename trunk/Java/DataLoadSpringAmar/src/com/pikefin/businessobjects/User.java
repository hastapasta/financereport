package com.pikefin.businessobjects;

import java.util.Date;


public class User implements java.io.Serializable {

	private Integer userId;
	private String username;
	private Groups userGroup;
	private Date created;
	private Date modified;
	private String accountEmail;
	private Integer obsoleteMaxNotifications;
	private Boolean suspended=false;
	private Boolean active=true;
	private Integer paginationLimit;
	private Boolean obsoleteBulkEmail=true;
	private String password;
	
	public User() {
	}

	public User(String username) {
		this.username = username;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Groups getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(Groups userGroup) {
		this.userGroup = userGroup;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	public Integer getObsoleteMaxNotifications() {
		return obsoleteMaxNotifications;
	}

	public void setObsoleteMaxNotifications(Integer obsoleteMaxNotifications) {
		this.obsoleteMaxNotifications = obsoleteMaxNotifications;
	}

	public Boolean getSuspended() {
		return suspended;
	}

	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getPaginationLimit() {
		return paginationLimit;
	}

	public void setPaginationLimit(Integer paginationLimit) {
		this.paginationLimit = paginationLimit;
	}

	public Boolean getObsoleteBulkEmail() {
		return obsoleteBulkEmail;
	}

	public void setObsoleteBulkEmail(Boolean obsoleteBulkEmail) {
		this.obsoleteBulkEmail = obsoleteBulkEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
