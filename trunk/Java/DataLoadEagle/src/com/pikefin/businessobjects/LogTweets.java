package com.pikefin.businessobjects;

import java.util.Date;

public class LogTweets {
private Integer logTweetId;
private String message;
private Date dateTime;
private String errorMessage;
private Alert alert;
public Integer getLogTweetId() {
	return logTweetId;
}
public void setLogTweetId(Integer logTweetId) {
	this.logTweetId = logTweetId;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public Date getDateTime() {
	return dateTime;
}
public void setDateTime(Date dateTime) {
	this.dateTime = dateTime;
}
public String getErrorMessage() {
	return errorMessage;
}
public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}
public Alert getAlert() {
	return alert;
}
public void setAlert(Alert alert) {
	this.alert = alert;
}
}
