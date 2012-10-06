package com.pikefin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import pikefin.log4jWrapper.MainWrapper;

public class ApplicationSetting {

	private  static ApplicationSetting instance;
	private ApplicationSetting(){
		
	}
	@Value(value="${email.user}")
	private  String emailUser;
	@Value(value="${email.pass}")
	private  String emailPassword;
	@Value(value="${email.host}")
	private  String emailHost;
	@Value(value="${email.port}")
	private  int emailPort;
	@Value(value="${email.fromaddy}")
	private  String emailFromAddress;
	@Value(value="${email.cakebaseurl}")
	private  String emailCakeUrl;
	@Value(value="${email.phpbaseurl}")
	private  String emailPhpUrl;
	@Value(value="${email.subjecttext}")
	private  String emailSubjectText;
	@Value(value="${app.debugmode}")
	private  boolean debugMode;
	@Value(value="${bulk.email.template}")
	private  String bulkEmailTemplate;
	@Value(value="${app.historicaldata}")
	private boolean loadHistoricalData;
	@Value(value="${app.sleep_interval}")
	private int threadSleepInteval;
	@Value(value="${app.max_threads}")
	private int maxAllowedThreads;
	
	@Autowired
	private MainWrapper stdoutwriter;
	
	
	public static ApplicationSetting getInstance(){
		if(instance==null){
			instance=new ApplicationSetting();
		}
		return instance;
	}

	public String getEmailUser() {
		return emailUser;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public String getEmailHost() {
		return emailHost;
	}

	public int getEmailPort() {
		return emailPort;
	}

	public String getEmailFromAddress() {
		return emailFromAddress;
	}

	public String getEmailCakeUrl() {
		return emailCakeUrl;
	}

	public String getEmailPhpUrl() {
		return emailPhpUrl;
	}

	public String getEmailSubjectText() {
		return emailSubjectText;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public String getBulkEmailTemplate() {
		return bulkEmailTemplate;
	}

	public boolean isLoadHistoricalData() {
		return loadHistoricalData;
	}

	public int getThreadSleepInteval() {
		return threadSleepInteval;
	}

	public int getMaxAllowedThreads() {
		return maxAllowedThreads;
	}

	public MainWrapper getStdoutwriter() {
		return stdoutwriter;
	}

	public void setStdoutwriter(MainWrapper stdoutwriter) {
		this.stdoutwriter = stdoutwriter;
	}
} 
