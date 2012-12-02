package com.pikefin.services.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pikefin.log4jWrapper.Logs;
import twitter4j.internal.logging.Logger;

import com.pikefin.ApplicationSetting;
import com.pikefin.PikefinUtil;
import com.pikefin.TheKey;
import com.pikefin.businessobjects.Alert;
import com.pikefin.businessobjects.AlertTarget;
import com.pikefin.businessobjects.Country;
import com.pikefin.businessobjects.EntityGroup;
import com.pikefin.businessobjects.FactData;
import com.pikefin.businessobjects.LogAlert;
import com.pikefin.businessobjects.LogTweets;
import com.pikefin.businessobjects.Task;
import com.pikefin.dao.inter.AlertDao;
import com.pikefin.dao.inter.AlertTargetDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.exceptions.PikefinException;
import com.pikefin.services.DataGrabExecutor;
import com.pikefin.services.inter.AlertService;
import com.pikefin.services.inter.AlertTargetService;
import com.pikefin.services.inter.EmailService;
import com.pikefin.services.inter.EntityGroupService;
import com.pikefin.services.inter.FactDataService;
import com.pikefin.services.inter.LogAlertService;
import com.pikefin.services.inter.LogTweetsService;
import com.pikefin.services.inter.NotificationService;
@Service
public class AlertServiceImpl implements AlertService{
	Logger logger=Logger.getLogger(AlertServiceImpl.class);
	@Autowired
	private FactDataService factService;
	@Autowired
	private AlertDao alertDao;
	@Autowired
	private AlertTargetService alertTargetService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private EntityGroupService entityGroupService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private LogTweetsService logTweetService;
	@Autowired
	private LogAlertService logAlertService;
	
	private BigDecimal dChange;
	@Override
	//@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void checkAlerts(DataGrabExecutor dataGrab) throws GenericException {
		  
				
			String strTicker;
			String taskDescription;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Task currentTask = dataGrab.getCurrentTask();
			if (currentTask != null) {
				taskDescription = currentTask.getDescription();
				List<Alert> allAlertList=alertDao.loadAllAlertsByTask(currentTask);
				//check if there are any alerts to process
				if (allAlertList == null)		
					return;
				List<FactData> factDataList= factService.loadFactDataByTaskForMaxBatch(currentTask);
				 Map<TheKey,FactData> factDataMap=this.convertListToFactDataMap(factDataList);
				 if (factDataMap == null || factDataMap.size()==0)  {
	                ApplicationSetting.getInstance().getStdoutwriter().writeln("No fact_data entries found for task " + currentTask  + ". Alert processing terminated.",Logs.WARN,"A4.0");
	                return;
	            }
				 // contains alerts for update
				 Map<Integer,Alert> alertsToUpdate=new HashMap<Integer, Alert>();
				 for(Alert tempAlert: allAlertList){
					 strTicker=tempAlert.getAlertEntity().getTicker();
					 Alert newAlertCopy=tempAlert.clone();
					if(tempAlert.getDisabled()){
						 continue;
					 }
					 
					 	TheKey key=new TheKey(tempAlert.getAlertEntity().getEntityId(),tempAlert.getCalyear());
						boolean bAutoResetPeriod = tempAlert.getAutoResetPeriod();
						boolean bAutoResetFired =tempAlert.getAutoResetFired();
						boolean bAlreadyFired =tempAlert.getFired();
					 if(!factDataMap.containsKey(key)){
						ApplicationSetting.getInstance().getStdoutwriter().writeln("No recent fact data collected for ticker: " + tempAlert.getAlertEntity().getTicker()  + ",alert id: "+ tempAlert.getAlertId() + ",entity id: " + tempAlert.getAlertEntity().getEntityId() + ". Skipping alert processing",Logs.WARN,"A2.7358");
								continue;
						 }
					 	Calendar calJustCollected = Calendar.getInstance();
						Calendar calInitialFactDataCollected = Calendar.getInstance();
						Calendar calObservationPeriodBegin = Calendar.getInstance();
						Calendar calObservationPeriodEnd = Calendar.getInstance();
						boolean bPopulateInitialFactDataId = false;
						if (tempAlert.getAlertInitialFactData()==null || tempAlert.getAlertInitialFactData().getValue()==null) {
							bPopulateInitialFactDataId = true;
						}
						else {
							calInitialFactDataCollected.setTime(tempAlert.getAlertInitialFactData().getDateCollected());
						}
						FactData fact=factDataMap.get(key);
						//HashMap<String,String> tmpHash = hmCurFactData.get(new TheKey(nEntityId+"",strCalYear));
						calJustCollected.setTime(fact.getDateCollected());
						calObservationPeriodBegin.setTime(tempAlert.getAlertTimeEvent().getLastDatetime());
						calObservationPeriodEnd.setTime(tempAlert.getAlertTimeEvent().getNextDatetime());
						FactData rsFactData=factService.loadFactDataForAlerts(tempAlert, currentTask) ;
						if (rsFactData==null) {
							ApplicationSetting.getInstance().getStdoutwriter().writeln("No fact_data entry found to populate initial_fact_data_id for Alert id: " + tempAlert.getAlertId() + ". Skipping Alert.",Logs.ERROR,"A10.3");
							continue;
						}
					
						if (tempAlert.getType()==1) {
							if ((calObservationPeriodBegin.after(calInitialFactDataCollected) && bAutoResetPeriod) || (bPopulateInitialFactDataId == true)) {
									
								newAlertCopy.setAlertInitialFactData(rsFactData);
								newAlertCopy.setAlertCurrentFactData(factDataMap.get(key));
								newAlertCopy.setNotificationCount(0);
								newAlertCopy.setFired(false);
									//TODO Need to Update in DB					
								alertsToUpdate.put(newAlertCopy.getAlertId(), newAlertCopy);
									continue;
							}
						}else if (tempAlert.getType() == 2) {
							if (bPopulateInitialFactDataId == true) {
													
								BigDecimal bdCurrent = new BigDecimal(rsFactData.getValue());
								BigDecimal bdTarget = new BigDecimal( tempAlert.getLimitValue());
								
								if (bdCurrent.equals(bdTarget)) {
									ApplicationSetting.getInstance().getStdoutwriter().writeln("Attempting to populate initial_fact_data_id with same value as target (alert type 2, alert id: " + tempAlert.getAlertId() + "). Skipping Alert.",Logs.WARN,"A10.324");
									continue;
								}
								newAlertCopy.setAlertInitialFactData(rsFactData);
								newAlertCopy.setNotificationCount(0);
								newAlertCopy.setFired(false);
								//TODO Need to Update in DB					
								alertsToUpdate.put(newAlertCopy.getAlertId(), newAlertCopy);

								/*
								 * We also have to a continue here as well since we use the initial fact data value
								 * to determine the direction of the move.
								 */
								continue;
							}
						
						}else if (tempAlert.getType() == 3) {

							newAlertCopy.setAlertInitialFactData(rsFactData);	
							newAlertCopy.setNotificationCount(0);
							newAlertCopy.setFired(false);
							//TODO Need to Update in DB					
							alertsToUpdate.put(newAlertCopy.getAlertId(), newAlertCopy);

					}
						int nUserId =tempAlert.getAlertUser().getUserId();
						
						int nNotificationCount =tempAlert.getNotificationCount();
						newAlertCopy.setAlertCurrentFactData(factDataMap.get(key));
						//TODO Need to Update in DB					
						alertsToUpdate.put(newAlertCopy.getAlertId(), newAlertCopy);

						int nMaxNotifications = 1;
						if (this.getTriggered(tempAlert,factDataMap,key)) {
							/* Process the triggered alert */
						 

							boolean bEmailSent = false;
							List<AlertTarget> alertTargets=alertTargetService.loadAllTargets(tempAlert);
							for(AlertTarget tempTarget:alertTargets)
							{
								switch (tempTarget.getType()) {
								case 1:
									/*
									 * Email notification.
									 */
									
									nMaxNotifications = tempTarget.getMaxNotifications();									/*
									 * Need to check the bulk_email for each alert_target
									 */
									if (!tempTarget.getBulkEmail()) {
											  
										if (nNotificationCount<nMaxNotifications)  {
											String strMsg;
											strMsg = "ALERT\r\n";
													  
											if (strTicker != null && !(strTicker.isEmpty()))
												strMsg += "Entity: " + strTicker;				  
												
											if (tempAlert.getAlertTask().getMetric().getMetricId().equals("11"))
												  strMsg += " (Futures)";
											strMsg += "\r\n";
													  
											//Get the full ticker description
											if (tempAlert.getAlertEntity().getFullName()!= null)
												strMsg = strMsg + "Full Description: " + tempAlert.getAlertEntity().getFullName() +" \r\n";
											else
												strMsg = strMsg + "Full Description: \r\n";
													  
													  
											strMsg = strMsg + "Task: " + currentTask.getDescription() + "\r\n";
											strMsg = strMsg + "Amount: " + dChange.multiply(new BigDecimal(100)).toString() + "%\r\n";
											strMsg = strMsg + "Current Value : " +factDataMap.get(key).getValue()+ "\r\n";
											strMsg = strMsg + "Previous Value: " +tempAlert.getAlertInitialFactData().getValue()+ "\r\n";
											SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy HH:mm:ss Z");
											strMsg = strMsg + "Current Timestamp : " + sdf.format(calJustCollected.getTime()) + "\r\n";
											strMsg = strMsg + "Previous Timestamp: " + sdf.format(calInitialFactDataCollected.getTime()) + "\r\n";
											strMsg += "Observation Period End: " + sdf.format(calObservationPeriodEnd.getTime()) + "\r\n";
											strMsg += "Observation Period Begin: " + sdf.format(calObservationPeriodBegin.getTime()) + "\r\n";
											strMsg += "Chart: " + ApplicationSetting.getInstance().getEmailPhpUrl() + "charts/allassets/linechart.php?a=" + tempAlert.getAlertId() + "\r\n";
													  
											//Add link for Increase Alert Limit form
											strMsg = strMsg + "Modify/View Alert Properties: " + ApplicationSetting.getInstance().getEmailCakeUrl() + "alerts/edit?alert=" + tempAlert.getAlertId();
											  
											String strSubject = ApplicationSetting.getInstance().getEmailSubjectText() + " : " + strTicker + " : " +tempAlert.getAlertTimeEvent().getName() + " Observation Period";
											  
								//TODO used the email service
											emailService.sendHtmlEmail(tempTarget.getAddress(),strMsg,strSubject);
					
											  
											 newAlertCopy.setNotificationCount(nNotificationCount+1) ;
											 //TODO needs to update in DataBase
											alertsToUpdate.put(newAlertCopy.getAlertId(), newAlertCopy);

										//TODO even if the email is not sent this and exception came this still set the emailSent to true	  
											bEmailSent = true;
										}
									}
									break;
									
								case 2:
									/*
									 * Twitter update.
									 */
										  
									String strTweet="";
									if (tempAlert.getType()== 1) {
										/*
										 * dChange gets used for inserting into log_alerts so using temp
										 * variable for tweet format modifications.
										 */
										BigDecimal bdTemp = dChange.multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
										//TODO uncomment scaling
										strTweet = bdTemp.toString() + "% ";
										strTweet += tempAlert.getAlertTimeEvent().getName();
										  
										if (tempAlert.getAlertTask().getMetric().getMetricId().equals("11"))
											 strTweet += " Futures";
										strTweet += " move in " + strTicker;
										if (tempAlert.getAlertEntity().getFullName()!= null)
											strTweet += " (" +tempAlert.getAlertEntity().getFullName() + ")";									  
										
										String strUrl = ApplicationSetting.getInstance().getEmailPhpUrl() + "charts/allassets/";
										if (tempAlert.getAlertTimeEvent().getTimeEventId().equals("1") || tempAlert.getAlertTimeEvent().getTimeEventId().equals("3"))
											strUrl += "linechart15min.php";
										else
											strUrl += "linechart.php";
										strUrl += "?a=" + tempAlert.getAlertId();
										strTweet += " " + PikefinUtil.shortenURL(strUrl);
									  
										/*
										 * Figure out if this alert represents an actual stock ticker. If so, then use the $ tag; otherwise use the # tag.
										 */
										List<EntityGroup> entityGroupList= entityGroupService.loadEntityGroupsByEntity(tempAlert.getAlertEntity());
										
										
										if (entityGroupList!=null && entityGroupList.size()!=0) {
											EntityGroup group=entityGroupList.get(0);
											switch (group.getEntityGroupId()) {
												
												/* S&P 500 */
												case 1:
													strTweet += " $" + strTicker;
													break;
													
												/* Forex */
												case 3:
													strTweet += " #" + strTicker;
													break;
												
												/* Global Equity Index Futures */
												/* Global Equity Indexes */
												case 1008:
												case 5:
													
													List<Country> entityCountries= tempAlert.getAlertEntity().getCountries();
													 
													if (entityCountries!=null && entityCountries.get(0)!=null && entityCountries.get(0).getHash()!= null && entityCountries.get(0).getHash().trim().length()>0) {
															strTweet += " " + entityCountries.get(0).getHash();
														
													}
													if (group.getEntityGroupId()==1008)
														strTweet += " #Futures";
													break;
												
												
												/* Commodity Futures */
												case 4:
													break;
													
												default:
													//should never get here.
													break;
												
											}
												
												
												
										}
											
									if(tempAlert.getAlertEntity().getHash() != null && tempAlert.getAlertEntity().getHash().trim().length()>0) {
												strTweet += " " + tempAlert.getAlertEntity().getHash();
											
										}
									}
									else if (tempAlert.getType() == 2) {
									/*
									 * Fixed threshold alert type
									 */
									/*
									 * Thinking of having notification templates and then in alerts_alert_targets have
									 * a template_id column to indicate which template to use. 
									 */
										strTweet += "Interest rate for bond x is now y.";
										
										
									}
										
										
								
									int nTweetLimit = tempTarget.getTweetLimit();
									Long nTweetCount = new Long(0);
								
									if (nTweetLimit >= 0) {
										
									
										  
										//rs11.next();
										nTweetCount = logTweetService.getTweetCounts(nUserId);
										
									}
									
									String strErrorMsg = "";
									
									
									
									if (nTweetCount >= nTweetLimit && !(nTweetLimit<0)) {
										strErrorMsg = "Tweet not sent because internal tweet limit of " + nTweetLimit + " exceeded.";
									}
									else {
										/*
										 * Debug mode is handled inside the tweet function
										 */
										if(tempTarget.getUserId() !=null  && tempTarget.getSecurityAccountId()!=null){
										strErrorMsg = notificationService.tweet(strTweet,tempTarget.getUserId().getUsername(),tempTarget.getUserId().getPassword(),tempTarget.getSecurityAccountId().getAuth1(),tempTarget.getSecurityAccountId().getAuth2(),tempTarget.getSecurityAccountId().getAuth3(),tempTarget.getSecurityAccountId().getAuth4());
										try {
											/*
											 *  Need a delay for unlimited tweeting.
											 */
											if (nTweetLimit<0) {
												ApplicationSetting.getInstance().getStdoutwriter().writeln("Unlimited tweeting. Pausing for 30 seconds...",Logs.STATUS2,"A4.63");
												Thread.sleep(30000);
											}
										}
										catch (InterruptedException ie) {
											
										}
									}else{
										strErrorMsg="Sweet not sent because user information is not avilable";
									}
									}
									  
									try {
										LogTweets logTweets=new LogTweets();
										logTweets.setDateTime(new Date());
										logTweets.setMessage(strTweet.length()>140?strTweet.substring(0,140):strTweet);
										logTweets.setAlert(tempAlert);
										if(strErrorMsg!=null && strErrorMsg.length()>0){
											logTweets.setErrorMessage(strErrorMsg.length()>200?strErrorMsg.substring(0,200):strErrorMsg);
											logger.info("Tweet Message couldsent-"+strTweet);
											}else{
												logger.info("Tweet Message sent-"+strTweet);
											}
										logTweetService.saveLogTweetsInfo(logTweets);
									} 
									catch (GenericException sqle) {
										ApplicationSetting.getInstance().getStdoutwriter().writeln("Issue inserting into log_tweets "+sqle.getErrorCode()+" "+sqle.getErrorDescription(),Logs.ERROR,"A4.6");
										ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
									}
								break;
								
								case 3:
									/*
									 * Facebook update.
									 */
									break;
									
								case 4:
									/*
									 * Google+ update.
									 */
									break;
									
								default:
									/*
									 * Invalid target code
									 */
									ApplicationSetting.getInstance().getStdoutwriter().writeln("Invalid alert target code.",Logs.ERROR,"A4.72");
									
									break;
							
								}
							}
							
							LogAlert logAlertEntity=new LogAlert();	
							logAlertEntity.setLogAlertAlert(tempAlert);
							logAlertEntity.setType(tempAlert.getType());
							logAlertEntity.setDateTimeFired(new Date());
							logAlertEntity.setBeforeFactData(tempAlert.getAlertInitialFactData());
							logAlertEntity.setAfterFactData(factDataMap.get(key));
							logAlertEntity.setLimitValue(tempAlert.getLimitValue());
							logAlertEntity.setLogAlertEntity(tempAlert.getAlertEntity());
							logAlertEntity.setLogAlertUser(tempAlert.getAlertUser());
							if(bEmailSent){
								logAlertEntity.setEmailSent(true);
							}else{
								logAlertEntity.setEmailSent(false);
	
							}
							
							logAlertEntity.setDiff(dChange.doubleValue());
							logAlertService.saveLogAlertInfo(logAlertEntity);
							if (bAutoResetFired)	  {
							newAlertCopy.setNotificationCount(0);
							newAlertCopy.setAlertInitialFactData(factDataMap.get(key));
							}
							else  {
								newAlertCopy.setFired(true);
							}
										  
						
							//TODO update tempAlert		
							alertsToUpdate.put(newAlertCopy.getAlertId(), newAlertCopy);

							//dg.dbf.db_update_query(query8);
									  
						}
				 }
				 Set<Entry<Integer, Alert>> entrySet=alertsToUpdate.entrySet();
				Iterator i=entrySet.iterator();
				while(i.hasNext()){
				Entry<Integer, Alert> e=(Entry<Integer, Alert>)	i.next();
				Alert alert=(Alert)e.getValue();
				alertDao.updateAlertInfo(alert);
				}
			} 
		  
	logger.info("********** ############  Check Alerts Completed *************************************************	^^^^^^^^^^^^^");
		
		
	}

	
	
	public Map<TheKey,FactData> convertListToFactDataMap(List<FactData> factDataList) throws GenericException	{
		
		Map<TheKey, FactData> factDataMap=new HashMap<TheKey, FactData>();
		
		for(FactData factEntity:factDataList){
			if (factEntity.getEntity()==null || factEntity.getEntity().getEntityId() == null) {
				ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem converting List<FactData> to Map - column for hash key not found",Logs.ERROR,"UF18.7");
				throw new GenericException("Custom  Exception - Unable to convert List<FactData> to HashMap");
			}
			TheKey key=new TheKey(factEntity.getEntity().getEntityId(), factEntity.getFiscalyear());
			factDataMap.put(key, factEntity);
		}
		
		return factDataMap;
		}
	
		
	
	
private boolean getTriggered(Alert alertDetails,Map<TheKey,FactData> factDataMap,TheKey key) {
	
	if (alertDetails.getFired()== true)
		return false;
	BigDecimal dInitialFactDataValue =new BigDecimal(alertDetails.getAlertInitialFactData().getValue());
	BigDecimal dJustCollectedValue =new BigDecimal(factDataMap.get(key).getValue());
	BigDecimal bdLimitValue =new BigDecimal(alertDetails.getLimitValue());

	switch (alertDetails.getType()) {
		case 1:
			/*
			 * Walking time frame, % change. (e.g. if it was hourly then 8-9,9-10,10-11 and so on.)
			 * 
			 */
		case 3:
			/*  Sliding time frame, % change.
			 * 
			 */
			//Not sure of the scale
			if (dJustCollectedValue.equals(new Double("0.000")) || dJustCollectedValue.equals(new Double("0.00")))  {
				ApplicationSetting.getInstance().getStdoutwriter().writeln("Zero was the last value collected. Probably a bad print. Skipping ticker " + alertDetails.getAlertEntity().getTicker(),Logs.WARN,"A2.7385");
				return false;
			}
			
			  
			try  {
				dChange = dJustCollectedValue.subtract(dInitialFactDataValue).divide(dInitialFactDataValue,BigDecimal.ROUND_HALF_UP);
			}
			catch (ArithmeticException ae) {
				ApplicationSetting.getInstance().getStdoutwriter().writeln("Error doing BigDecimal arithmetic",Logs.ERROR,"A2.7385");
				ApplicationSetting.getInstance().getStdoutwriter().writeln("Error occured processing alert id: " + alertDetails.getAlertId(),Logs.ERROR,"A2.7386");
				ApplicationSetting.getInstance().getStdoutwriter().writeln(ae);
				return false;
			}
			  
			if ((dChange.abs().compareTo(bdLimitValue) > 0))
				return true;
		
			return false;
		case 2:
			/*
			 * Absolute Target with no time frame
			 */
			/*
			 * Set dChange to dInitialFactDataValue in case the alert has been triggered, dChange is what gets stored
			 * as diff in log_alerts.
			 */
			dChange = dInitialFactDataValue;
			
			
			if (bdLimitValue.compareTo(dInitialFactDataValue)>0)
				if (dJustCollectedValue.compareTo(bdLimitValue)>=0)
					return true;
				else 
					return false;
			else
				if (dJustCollectedValue.compareTo(bdLimitValue)<=0)
					return true;
				else
					return false;
			
		
		//case 3:
			/*
			 * Sliding Time Frame, % change.
			 */
			
			//return false;
			
		case 4:
			/*
			 * Sliding Time Frame, absolute delta.
			 */
			
			return false;
			
		case 5:
			/*
			 * Walking Time Frame, absolute delta.
			 */
			
			return false;
			
		case 6:
			/*
			 * Sliding Time Frame, moving average.
			 */
			return false;
		default:
			return false;
			
	}
}

}
