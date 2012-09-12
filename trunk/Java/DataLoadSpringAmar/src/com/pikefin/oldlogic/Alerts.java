package com.pikefin.oldlogic;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.pikefin.businessobjects.FactData;
import com.pikefin.businessobjects.Task;

import pikefin.log4jWrapper.Logs;




public class Alerts {
	
	int nAlertType;
	HashMap<TheKey,HashMap<String,String>> hmCurFactData;
	int nEntityId;
	String strCalYear;
	String strTicker;
	HashMap<String,String> hmAlert;
	int nAlertId;
	boolean bAlreadyFired;
	BigDecimal dChange;
	BigDecimal dJustCollectedValue;
	BigDecimal dInitialFactDataValue;
	BigDecimal bdLimitValue;
	
	public Alerts()	{
		
	}
	
	
	/*
	 * 
	 * PLEASE READ THIS!!!!
	 * Notes on Boolean.parseBoolean(String) - There is an issue with using this method. If the String parameter
	 * is null, parseBoolean accepts it and returns false. This can cause a problem in a line like the following:
	 *  boolean bAutoResetPeriod = Boolean.parseBoolean(hmAlert.get("alerts.auto_reset_period"));
	 *  
	 *  where a typo in the string "alerts.auto_reset_period" will cause the variable to be set to false and no runtimeexception
	 *  will be thrown. This situation can be extremely difficult to diagnose. Because of this, it is recommended to
	 *  use the customParseBoolean() function instead. This isn't an issue for the other datatypes (int,date,BigDecimal) since
	 *  they will cause Runtimeexceptions to be thrown. Strings will be set to null which will also eventually result in a
	 *  nullpointerexception being thrown.
	 *  
	 * 
	 */
	public void checkAlerts(DataGrab dg) throws DataAccessException, PikefinException {
		  
		int nCurTask = dg.nCurTask;
		String strTaskDescription;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  
		//String query = "select tasks.* from tasks where id=" + nCurTask;
		String query = " from Task where id=" + nCurTask;
		  
		//ResultSet rsTask = dg.dbf.db_run_query(query);
		//SqlRowSet rsTask = dg.dbf.dbSpringRunQuery(query);
		
		Task t = null;
		
		t = (Task)dg.dbf.dbHibernateRunQueryUnique(query);
		
		
		if (t != null) {
			//strTaskName = rsTask.getString("tasks.name");
			strTaskDescription = t.getDescription();
			  

			/*
			 * NOTE: YOU CURRENTLY CANNOT USE TABLE ALIASES (i.e as <table name>) IN THIS QUERY!
			 * It has to with how the resultset is getting converted to a hash map and the fact
			 * that java returns the actual table name and not the alias.
			 */
			  
			  
		
			query = "select alerts.disabled,alerts.id,alerts.initial_fact_data_id,";
			query += " alerts.notification_count,alerts.user_id,alerts.limit_value,";
			query += " alerts.auto_reset_period,alerts.auto_reset_fired,alerts.fired, alerts.calyear, alerts.type, ";
			query += " entities.ticker,entities.id,entities.full_name, entities.hash, ";
			query += " users.id, ";
			query += " fact_data.value,fact_data.id,fact_data.date_collected,";
			query += " time_events.id,time_events.name,time_events.next_datetime,time_events.last_datetime, ";
			query += " tasks.metric_id, ";
			query += " countries.hash ";
			query += " from alerts ";
			query += " LEFT JOIN entities ON alerts.entity_id=entities.id ";
			query += " LEFT JOIN users ON alerts.user_id=users.id ";
			query += " LEFT JOIN fact_data ON (alerts.initial_fact_data_id=fact_data.id and alerts.calyear<=>fact_data.calyear) ";
			query += " LEFT JOIN time_events ON alerts.time_event_id=time_events.id ";
			query += " LEFT JOIN tasks ON alerts.task_id=tasks.id ";
			query += " LEFT JOIN countries_entities ON countries_entities.entity_id=entities.id ";
			query += " LEFT JOIN countries ON countries.id=countries_entities.country_id ";
			query += " where alerts.task_id=" + nCurTask;
			
			
			/* OFP 2/12/2012 - Regarding the countries_entities.country_default field:
			 * 
			 * This was added because if an entity belongs to multiple countries, like USDXCD, then this query results in an alert
			 * getting duplicated. 
			 * 
			 * I added the is null comparison because there may be some entities that, for valid reasons, are not
			 * in the countries_entities table.
			 * 
			 * Added the 2nd parameter to convertResultSetToArrayList as a safety measure to ensure that all duplicates, 
			 * for whatever reason, are removed.
			 * 
			 * 
			*/
			//TODO this query need to be converted into HQL
			query += " and (countries_entities.default_country=1 OR countries_entities.default_country is null)";
			query += " order by time_events.id";
			
			String query2  = " from Alert a ";
			query2 += " left join a.alertEntity e ";
			query2 += " left join a.alertUser u ";
			query2 += " left join a.alertInitialFactData fd with a.calyear<=>fd.calyear ifd";
			query2 += " left join a.alertTimeEvent te ";
			query2 += " left join a.alertTask t ";
			query2 += " left join ce.";
			query2 += " left join e.country c";
			query2 += " where t.id="+nCurTask;
			query2 += " and (";
			
			  
			 
			//ResultSet rsAlert1 = dg.dbf.db_run_query(query);
			SqlRowSet rsAlert1 = dg.dbf.dbSpringRunQuery(query);
			 
			 
			ArrayList<HashMap<String,String>> arrayListAlerts = UtilityFunctions.convertRowSetToArrayList(rsAlert1,"alerts.id");
			
			  
			//check if there are any alerts to process
			if (arrayListAlerts == null)		
				return;
			 
			//int nRow=0;
			  
			/*
			 * 
			 * Moved this code outside of the loop to speed up processing.
			 */
		/*	query = "select max(batch_id) from fact_data,batches where fact_data.batch_id=batches.id AND task_id=" + nCurTask;
			//ResultSet rs7 = dg.dbf.db_run_query(query);
			SqlRowSet rs7 = dg.dbf.dbSpringRunQuery(query);
			
			
			rs7.next();
			int nMaxBatch = rs7.getInt("max(batch_id)");
			
			String query3;
			query3 = "select fact_data.* ";
			query3 += "from fact_data, batches ";
			query3 += " where fact_data.batch_id=batches.id ";
			query3 += " and task_id=" + nCurTask;
			query3 += " and batch_id=" + nMaxBatch;
			*/
			// modified by Amar on 10-Sep-2012 native equivalent of below query is as 
			//select fact.* from fact_data fact,Batches batch where fact.batch_id=batch.id AND batch.task_id=6 AND batch.id=(select max(batch.id) from fact_data fact,Batches batch where fact.batch_id=batch.id AND batch.task_id=6);
			
			query = "select max(batch.batchId) from FactData fact,Batches batch where fact.batchId.batchId=batch.batchId AND batch.batchTask.taskId=" + nCurTask;
			Integer nMaxBatch =(Integer) dg.dbf.dbHibernateRunQueryUnique(query);

			
			String query3="select fact.* from FactData fact,Batches batch where fact.batchId.batchId=batch.batchId AND batch.batchTask.taskId="+nCurTask +" AND batch.batch_id="+nMaxBatch;
					
			
			  
			//ResultSet rsCurFactData1 = dg.dbf.db_run_query(query3);
			//Commented by Amar to accepts HQL changes  
			//SqlRowSet rsCurFactData1 = dg.dbf.dbSpringRunQuery(query3);
			  
			
		//	this.hmCurFactData = UtilityFunctions.convertRowSetToHashMap(rsCurFactData1,"entity_id","calyear");
			List<FactData> factDataList = (List<FactData>)dg.dbf.dbHibernateRunQuery(query3);
			this.hmCurFactData=this.convertFactDataListToHashMap(factDataList);
			if (hmCurFactData == null)  {
                UtilityFunctions.stdoutwriter.writeln("No fact_data entries found for task " + nCurTask + "and batch " + nMaxBatch + ". Alert processing terminated.",Logs.WARN,"A4.0");
                return;
            }
			  

			  
	  
			//int nAlertIndex = 0;
			 
			for(Iterator<HashMap<String,String>> alertIterator = arrayListAlerts.iterator(); alertIterator.hasNext();){
				
				this.hmAlert = alertIterator.next(); 
			
				//nAlertIndex++;
				//nRow++;
				try  {
				 
					boolean bDisabled = customParseBoolean(hmAlert.get("alerts.disabled"));

					if (bDisabled)
						continue;
					  
					SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					  
					this.nEntityId = Integer.parseInt(hmAlert.get("entities.id"));
					this.nAlertId = Integer.parseInt(hmAlert.get("alerts.id"));
					this.strCalYear = hmAlert.get("alerts.calyear");
					this.strTicker = hmAlert.get("entities.ticker");
					this.nAlertType = Integer.parseInt(hmAlert.get("alerts.type"));
					  
					boolean bAutoResetPeriod = customParseBoolean(hmAlert.get("alerts.auto_reset_period"));
					boolean bAutoResetFired = customParseBoolean(hmAlert.get("alerts.auto_reset_fired"));
					this.bAlreadyFired = customParseBoolean(hmAlert.get("alerts.fired"));
				
					if (hmCurFactData.get(new TheKey(nEntityId+"",strCalYear)) == null) {
						UtilityFunctions.stdoutwriter.writeln("No recent fact data collected for ticker: " + strTicker  + ", batch: " + nMaxBatch + ",alert id: "+ nAlertId + ",entity id: " + nEntityId + ". Skipping alert processing",Logs.WARN,"A2.7358");
						continue;
					}
					  
					Calendar calJustCollected = Calendar.getInstance();
					Calendar calInitialFactDataCollected = Calendar.getInstance();
					  
					Calendar calObservationPeriodBegin = Calendar.getInstance();
					Calendar calObservationPeriodEnd = Calendar.getInstance();
			

					boolean bPopulateInitialFactDataId = false;
					if (hmAlert.get("fact_data.value") == null) {
						bPopulateInitialFactDataId = true;
					}
					else {
						calInitialFactDataCollected.setTime(inputFormat.parse(hmAlert.get("fact_data.date_collected")));
					}
				  
					
					//HashMap<String,String> tmpHash = hmCurFactData.get(new TheKey(nEntityId+"",strCalYear));
					calJustCollected.setTime(inputFormat.parse(hmCurFactData.get(new TheKey(nEntityId+"",strCalYear)).get("fact_data.date_collected")));
					calObservationPeriodBegin.setTime(inputFormat.parse(hmAlert.get("time_events.last_datetime")));
					calObservationPeriodEnd.setTime(inputFormat.parse(hmAlert.get("time_events.next_datetime")));
					

					//int nCount=0;

					/*
					 * This is the check to see if the alert period has ended and time to update the initial value and reset the adjustment to zero.
					 * If the last data value collected is before the beginning of the observation period, then we need a new initial_fact_data_id.
					 */
					
					/*
					 * There is a lot of code overlap between alert type 1 and 3 but from a comprehension stand point
					 * I want to keep the condition statement at the top level. Maybe move the common code into a function.
					 */
					

					
					if (nAlertType==1) {
				
						if ((calObservationPeriodBegin.after(calInitialFactDataCollected) && bAutoResetPeriod) || (bPopulateInitialFactDataId == true)) {
		  
							/*
							 * Here we should retrieve the earliest fact_data.id that is after ObservationPeriodBegin.
							 * Most likely this will be the same fact_data.id as the one just collected, but there may be a reason, like 
							 * the schedule hasn't been run for awhile, that it could be a different id.
							 * 
							 */
								  
							/*
							 * The following query is really slow. One idea to speed it up is since for a given time_event.id, and a given task.id, all entities
							 * should have the same batch #. So we should only have to run this query once for all alerts that share the same schedule.id (thus task.id) and time_events.id.
							 * Or the other thing is we could just set the initial_fact_data_id to the recently collected id rather than
							 * going out and trying to find the earliest one for the observation period, which might be ok for hour and day Obs. Periods but not
							 * so much for month or year.
							 * 
							 * 
							 */
						/*	  
							String strQuery = "select fact_data.id from fact_data,batches ";
							strQuery += "where fact_data.batch_id = batches.id ";
							strQuery += " and task_id=" + nCurTask + " AND entity_id=" + nEntityId + " AND fact_data.date_collected>='" + formatter.format(calObservationPeriodBegin.getTime()) +"'";
							if (strCalYear != null)
								strQuery += " and calyear=" + strCalYear;
							strQuery += " order by fact_data.date_collected asc";
													
							//ResultSet rsFactData = dg.dbf.db_run_query(strQuery);
							SqlRowSet rsFactData = dg.dbf.dbSpringRunQuery(strQuery);
							
							if (!rsFactData.next()) {
								//we should never get here, but you never know.
								UtilityFunctions.stdoutwriter.writeln("No fact_data entry found to populate initial_fact_data_id for Alert id: " + nAlertId + ". Skipping Alert.",Logs.ERROR,"A10.3");
								continue;
									  
							}
								  
							String query4 = "update alerts set ";
							 
							query4 += "initial_fact_data_id=" +rsFactData.getInt("fact_data.id") + ",";
							query4 += "current_fact_data_id=" + hmCurFactData.get(new TheKey(nEntityId+"",strCalYear)).get("fact_data.id") + ", ";
							query4 += "notification_count=0,";
							query4 += "fired=0 ";
							query4 += " where id=" + nAlertId;
							  
							//dg.dbf.db_update_query(query4);
							dg.dbf.dbSpringUpdateQuery(query4);
							*/

							String strQuery = "select fact.factDataId from FactData fact ,Batches batch where fact.batchId.batchId=batch.batchId AND batch.batchTask.taskId=" + nCurTask+" AND fact.entityId="+nEntityId+" AND fact.dateCollected>='"+calObservationPeriodBegin.getTime()+"'";
							if(strCalYear != null && strCalYear.trim().length()>0)
								strQuery+=" AND fact.calyear="+strCalYear;
							strQuery+=" order by fact.dateCollected asc";
																
							
							List rsFactData = dg.dbf.dbHibernateRunQuery(strQuery);
							
							if (rsFactData==null || rsFactData.size()==0) {
								//we should never get here, but you never know.
								UtilityFunctions.stdoutwriter.writeln("No fact_data entry found to populate initial_fact_data_id for Alert id: " + nAlertId + ". Skipping Alert.",Logs.ERROR,"A10.3");
								continue;
									  
							}
							//getting the first fact id from entire list
							Integer factId=(Integer) rsFactData.get(0); 
							//TODO
							String query4 = "update alerts set ";
							 
							query4 += "initial_fact_data_id=" +factId + ",";
							query4 += "current_fact_data_id=" + hmCurFactData.get(new TheKey(nEntityId+"",strCalYear)).get("fact_data.id") + ", ";
							query4 += "notification_count=0,";
							query4 += "fired=0 ";
							query4 += " where id=" + nAlertId;
							  
							//dg.dbf.db_update_query(query4);
							dg.dbf.dbSpringUpdateQuery(query4);
							  
							//nCount++;
							  
							/*
							 * We're past the end of the time period so don't do anything else and proceed to the next alert.
							 */
							
							continue;
						}
					}
					else if (nAlertType == 2) {
						if (bPopulateInitialFactDataId == true) {
							/*
							 * Absolute target with no time frame.
							 * We're just going to initialize the initial fact data value
							 * 
							 * OFP 3/8/2012 - Don't initialize and print a warning of current fact data value (which is going to be set as the initial) is
							 * the same as the target (alert.limit_value). 
							 */
							
							
							String strQuery = "select fact_data.id,fact_data.value from fact_data,batches ";
							strQuery += "where fact_data.batch_id = batches.id ";
							strQuery += " and task_id=" + nCurTask + " AND entity_id=" + nEntityId + " AND fact_data.date_collected>='" + formatter.format(calObservationPeriodBegin.getTime()) +"'";
							if (strCalYear != null)
								strQuery += " and calyear=" + strCalYear;
							strQuery += " order by fact_data.date_collected asc";
								  
							//ResultSet rsFactData = dg.dbf.db_run_query(strQuery);
							SqlRowSet rsFactData = dg.dbf.dbSpringRunQuery(strQuery);
							  
							if (!rsFactData.next()) {
								/*
								 * This particular entity id wasn't populated in this task run.
								 */
								UtilityFunctions.stdoutwriter.writeln("No fact_data entry found to populate initial_fact_data_id for Alert id: " + nAlertId + ". Skipping Alert.",Logs.ERROR,"A10.3");
								continue;
									  
							}
							
							BigDecimal bdCurrent = new BigDecimal(rsFactData.getString("value"));
							BigDecimal bdTarget = new BigDecimal(hmAlert.get("alerts.limit_value"));
							
							if (bdCurrent.equals(bdTarget)) {
								UtilityFunctions.stdoutwriter.writeln("Attempting to populate initial_fact_data_id with same value as target (alert type 2, alert id: " + nAlertId + "). Skipping Alert.",Logs.WARN,"A10.324");
								continue;
							}
							
							
							String query4 = "update alerts set ";
							 
							query4 += "initial_fact_data_id=" +rsFactData.getInt("fact_data.id") + ",";
							query4 += "notification_count=0,";
							query4 += "fired=0 ";
							query4 += " where id=" + nAlertId;
							  
							//dg.dbf.db_update_query(query4);
							dg.dbf.dbSpringUpdateQuery(query4);
							
							/*
							 * We also have to a continue here as well since we use the initial fact data value
							 * to determine the direction of the move.
							 */
							continue;
						}
					}
					else if (nAlertType == 3) {
					//	if (bPopulateInitialFactDataId == true) {
							/* Set initial fact data if we are initializing the alert.
							 * current fact data will get set after this is-else statement (we're not doing a continue).
							 */
	
							
							/*
							 * The following query is really slow. One idea to speed it up is since for a given time_event.id, and a given task.id, all entities
							 * should have the same batch #. So we should only have to run this query once for all alerts that share the same schedule.id (thus task.id) and time_events.id.
							 * Or the other thing is we could just set the initial_fact_data_id to the recently collected id rather than
							 * going out and trying to find the earliest one for the observation period, which might be ok for hour and day Obs. Periods but not
							 * so much for month or year.
							 * 
							 * 
							 */
							  
							String strQuery = "select fact_data.id from fact_data,batches ";
							strQuery += "where fact_data.batch_id = batches.id ";
							strQuery += " and task_id=" + nCurTask + " AND entity_id=" + nEntityId + " AND fact_data.date_collected>='" + formatter.format(calObservationPeriodBegin.getTime()) +"'";
							if (strCalYear != null)
								strQuery += " and calyear=" + strCalYear;
							strQuery += " order by fact_data.date_collected asc";
								  
							//ResultSet rsFactData = dg.dbf.db_run_query(strQuery);
							SqlRowSet rsFactData = dg.dbf.dbSpringRunQuery(strQuery);
							  
							if (!rsFactData.next()) {
								//we should never get here, but you never know.
								UtilityFunctions.stdoutwriter.writeln("No fact_data entry found to populate initial_fact_data_id for Alert id: " + nAlertId + ". Skipping Alert.",Logs.ERROR,"A10.3");
								continue;
									  
							}
							
							String query4 = "update alerts set ";
							 
							query4 += "initial_fact_data_id=" +rsFactData.getInt("fact_data.id") + ",";
							query4 += "notification_count=0,";
							query4 += "fired=0 ";
							query4 += " where id=" + nAlertId;
							  
							//dg.dbf.db_update_query(query4);
							dg.dbf.dbSpringUpdateQuery(query4);
					//	}
						
					}
				
					int nUserId = Integer.parseInt(hmAlert.get("alerts.user_id"));//rsAlert.getInt("user_id");
				  
			
					int nNotificationCount = Integer.parseInt(hmAlert.get("alerts.notification_count"));//rsAlert.getInt("alert_count");
				  	
					String strUpdateQuery = "update alerts set current_fact_data_id=" + hmCurFactData.get(new TheKey(nEntityId+"",strCalYear)).get("fact_data.id") + " where alerts.id=" + nAlertId;
					//dg.dbf.db_update_query(strUpdateQuery);
					dg.dbf.dbSpringUpdateQuery(strUpdateQuery);

					
			
					int nMaxNotifications = 1;
						
					if (this.getTriggered()) {
						/* Process the triggered alert */
					 

						boolean bEmailSent = false;
							
						String strQuery20 = "select alert_targets.*,security_accounts.* from alerts_alert_targets ";
						strQuery20 += " JOIN alert_targets on alerts_alert_targets.alert_target_id=alert_targets.id ";
						strQuery20 += " LEFT JOIN security_accounts on alert_targets.security_account_id=security_accounts.id ";
						strQuery20 += " where alert_id=" + nAlertId;
						  
						//ResultSet rs20 = dg.dbf.db_run_query(strQuery20);
						SqlRowSet rs20 = dg.dbf.dbSpringRunQuery(strQuery20);
						
						while (rs20.next()) {
							
							int nTargetType = rs20.getInt("type");
							
							
							switch (nTargetType) {


								case 1:
									/*
									 * Email notification.
									 */
									
									nMaxNotifications = rs20.getInt("max_notifications");

								
									
									/*
									 * Need to check the bulk_email for each alert_target
									 */
									if (rs20.getInt("bulk_email")==0) {
											  
										if (nNotificationCount<nMaxNotifications)  {
											String strMsg;
											strMsg = "ALERT\r\n";
													  
											if (strTicker != null && !(strTicker.isEmpty()))
												strMsg += "Entity: " + strTicker;				  
												
											if (hmAlert.get("tasks.metric_id").equals("11"))
												  strMsg += " (Futures)";
											strMsg += "\r\n";
													  
											//Get the full ticker description
													
													  
											if (hmAlert.get("entities.full_name") != null)
												strMsg = strMsg + "Full Description: " + hmAlert.get("entities.full_name") +" \r\n";
											else
												strMsg = strMsg + "Full Description: \r\n";
													  
													  
											strMsg = strMsg + "Task: " + strTaskDescription + "\r\n";
											strMsg = strMsg + "Amount: " + dChange.multiply(new BigDecimal(100)).toString() + "%\r\n";
											strMsg = strMsg + "Current Value : " + dJustCollectedValue.toString() + "\r\n";
											strMsg = strMsg + "Previous Value: " + dInitialFactDataValue.toString() + "\r\n";
													
													 
													  
											SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy HH:mm:ss Z");
			
											 
										
											strMsg = strMsg + "Current Timestamp : " + sdf.format(calJustCollected.getTime()) + "\r\n";
											strMsg = strMsg + "Previous Timestamp: " + sdf.format(calInitialFactDataCollected.getTime()) + "\r\n";
											
									
											strMsg += "Observation Period End: " + sdf.format(calObservationPeriodEnd.getTime()) + "\r\n";
											strMsg += "Observation Period Begin: " + sdf.format(calObservationPeriodBegin.getTime()) + "\r\n";
											 
											  
											strMsg += "Chart: " + UtilityFunctions.getEmailPhpUrl() + "charts/allassets/linechart.php?a=" + nAlertId + "\r\n";
													  
											//Add link for Increase Alert Limit form
											strMsg = strMsg + "Modify/View Alert Properties: " + UtilityFunctions.getEmailCakeUrl() + "alerts/edit?alert=" + nAlertId;
											  
											String strSubject = UtilityFunctions.getEmailSubjectText() + " : " + strTicker + " : " + hmAlert.get("time_events.name") + " Observation Period";
											  
								
											UtilityFunctions.mail(rs20.getString("address"),strMsg,strSubject,UtilityFunctions.getEmailFromAddy());
					
											  
											String query9 = "update alerts set notification_count=" + (nNotificationCount+1) + " where id=" + nAlertId;
											  
											//dg.dbf.db_update_query(query9);
											dg.dbf.dbSpringUpdateQuery(query9);
											  
											bEmailSent = true;
										}
									}
									break;
									
								case 2:
									/*
									 * Twitter update.
									 */
										  
									String strTweet="";
									if (nAlertType == 1) {
										/*
										 * dChange gets used for inserting into log_alerts so using temp
										 * variable for tweet format modifications.
										 */
										BigDecimal bdTemp = dChange.multiply(new BigDecimal(100)).setScale(2);
										strTweet = bdTemp.toString() + "% ";
										strTweet += hmAlert.get("time_events.name");
										  
										if (hmAlert.get("tasks.metric_id").equals("11"))
											 strTweet += " Futures";
										strTweet += " move in " + strTicker;
										if (hmAlert.get("entities.full_name") != null)
											strTweet += " (" + hmAlert.get("entities.full_name") + ")";									  
										
										String strUrl = UtilityFunctions.getEmailPhpUrl() + "charts/allassets/";
										if (hmAlert.get("time_events.id").equals("1") || hmAlert.get("time_events.id").equals("3"))
											strUrl += "linechart15min.php";
										else
											strUrl += "linechart.php";
										
										strUrl += "?a=" + nAlertId;
										
									
												  
										strTweet += " " + UtilityFunctions.shortenURL(strUrl);
											  
											  
											  
										/*
										 * Figure out if this alert represents an actual stock ticker. If so, then use the $ tag; otherwise use the # tag.
										 */
										String tmpQuery = "select entity_group_id from entities_entity_groups ";
										tmpQuery += " where entity_id=" + hmAlert.get("entities.id");
										tmpQuery += " AND entity_group_id in (1,3,4,5,1008) ";
										  
										//ResultSet rs10 = dg.dbf.db_run_query(tmpQuery);
										SqlRowSet rs10 = dg.dbf.dbSpringRunQuery(tmpQuery);
										
										if (rs10.next()) {
											switch (rs10.getInt("entity_group_id")) {
												
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
													if (hmAlert.get("countries.hash") != null) {
														if (!hmAlert.get("countries.hash").isEmpty()) {
															strTweet += " " + hmAlert.get("countries.hash");
														}
													}
													if (rs10.getInt("entity_group_id")==1008)
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
											
										if (hmAlert.get("entities.hash") != null) {
											if (!hmAlert.get("entities.hash").isEmpty()) {
												strTweet += " " + hmAlert.get("entities.hash");
											}
										}
									}
									else if (nAlertType == 2) {
									/*
									 * Fixed threshold alert type
									 */
									/*
									 * Thinking of having notification templates and then in alerts_alert_targets have
									 * a template_id column to indicate which template to use. 
									 */
										strTweet += "Interest rate for bond x is now y.";
										
										
									}
										
										
								
									int nTweetLimit = rs20.getInt("tweet_limit");
									int nTweetCount = 0;
								
									if (nTweetLimit >= 0) {
										String strTweetLimitQuery="select count(log_tweets.id) as cnt from log_tweets ";
										strTweetLimitQuery += " join alerts on log_tweets.alert_id=alerts.id ";
										strTweetLimitQuery += " where date_format(datetime,'%Y-%m-%d %H')=date_format(NOW(),'%Y-%m-%d %H') ";
										strTweetLimitQuery += " and (minute(datetime) div 15)=(minute(NOW()) div 15) ";
										strTweetLimitQuery += " and alerts.user_id=" + nUserId;
										
									
										  
										//ResultSet rs11 = dg.dbf.db_run_query(strTweetLimitQuery);
										SqlRowSet rs11 = dg.dbf.dbSpringRunQuery(strTweetLimitQuery);
										  
										rs11.next();
										nTweetCount = rs11.getInt("cnt");
									}
									
									String strErrorMsg = "";
									
									
									
									if (nTweetCount >= nTweetLimit && !(nTweetLimit<0)) {
										strErrorMsg = "Tweet not sent because internal tweet limit of " + nTweetLimit + " exceeded.";
									}
									else {
										/*
										 * Debug mode is handled inside the tweet function
										 */
	
										strErrorMsg = UtilityFunctions.tweet(strTweet,rs20.getString("username"),rs20.getString("password"),rs20.getString("auth1"),rs20.getString("auth2"),rs20.getString("auth3"),rs20.getString("auth4"));
										try {
											/*
											 *  Need a delay for unlimited tweeting.
											 */
											if (nTweetLimit<0) {
												UtilityFunctions.stdoutwriter.writeln("Unlimited tweeting. Pausing for 30 seconds...",Logs.STATUS2,"A4.63");
												Thread.sleep(30000);
											}
										}
										catch (InterruptedException ie) {
											
										}
									}  
									
									  
									try {
										String strTweetLogQuery = "insert into log_tweets (datetime,message,alert_id,error_message) ";
									
										strTweetLogQuery += " values (NOW(),'" + (strTweet.length()>140?strTweet.substring(0,140):strTweet) + "'," + hmAlert.get("alerts.id");
										if (strErrorMsg.isEmpty())
											strTweetLogQuery += ",null)";
										else
											strTweetLogQuery += ",'" + (strErrorMsg.length()>200?strErrorMsg.substring(0,200):strErrorMsg) + "')";
										//dg.dbf.db_update_query(strTweetLogQuery);
										dg.dbf.dbSpringUpdateQuery(strTweetLogQuery);
									} 
									catch (DataAccessException sqle) {
										UtilityFunctions.stdoutwriter.writeln("Issue inserting into log_tweets",Logs.ERROR,"A4.6");
										UtilityFunctions.stdoutwriter.writeln(sqle);
									}
								//}
		  				  
								//}
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
									UtilityFunctions.stdoutwriter.writeln("Invalid alert target code.",Logs.ERROR,"A4.72");
									
									break;
							}
						
						
						}
			 
						  
						String query8 = "insert into log_alerts (alert_id,type,date_time_fired,bef_fact_data_id,aft_fact_data_id,limit_value,entity_id,user_id,email_sent,diff) values (" 
						+ nAlertId + ","
						+ hmAlert.get("alerts.type") + ",'"
						  
						/*
						 *  We already have a record of the after fact data time (linked to by aft_fact_data_id) so it's pointless to save it again as the
						 *  alert fired time. So we will switch here to using the current time.
						 * 
						 */

						+ formatter.format(Calendar.getInstance().getTime()) + "',"
								  
								  						  
						
						+ hmAlert.get("fact_data.id") + ","
						+ hmCurFactData.get(new TheKey(nEntityId+"",strCalYear)).get("fact_data.id") + ","		
					
						+ hmAlert.get("alerts.limit_value") + ","
						
						+ hmAlert.get("entities.id") + ","
						
						+ hmAlert.get("users.id") + ",";
						if (bEmailSent)
							query8 += "1,";
						else
							query8 += "0,";
						query8 += dChange + ")";
								  
						//dg.dbf.db_update_query(query8);
						dg.dbf.dbSpringUpdateQuery(query8);
						  
						if (bAutoResetFired)	  {
							query8 = "update alerts set notification_count=0,initial_fact_data_id=" + hmCurFactData.get(new TheKey(nEntityId+"",strCalYear)).get("fact_data.id") + " where id=" + nAlertId;
						}
						else  {
							query8 = "update alerts set fired=1 where id=" + nAlertId;
						}
									  
					
								  
						//dg.dbf.db_update_query(query8);
						dg.dbf.dbSpringUpdateQuery(query8);
								  
					}

				}
				catch (ParseException pe)  {
					UtilityFunctions.stdoutwriter.writeln("Problem parsing date for alert " + hmAlert.get("alerts.id") + ", alert not updated",Logs.ERROR,"A2.753");
					UtilityFunctions.stdoutwriter.writeln(pe);
					  
				}
				  
			}
			  
		} 
	  
	}
	
	private boolean getTriggered() {
		
		if (bAlreadyFired == true)
			return false;
	 
		dInitialFactDataValue = new BigDecimal(hmAlert.get("fact_data.value"));
		this.dJustCollectedValue = new BigDecimal(hmCurFactData.get(new TheKey(nEntityId+"",this.strCalYear)).get("fact_data.value"));
		
		this.bdLimitValue = new BigDecimal(hmAlert.get("alerts.limit_value"));
		
		
	
		switch (nAlertType) {
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
				if (dJustCollectedValue.equals(new BigDecimal("0.000")) || dJustCollectedValue.equals(new BigDecimal("0.00")))  {
					UtilityFunctions.stdoutwriter.writeln("Zero was the last value collected. Probably a bad print. Skipping ticker " + strTicker,Logs.WARN,"A2.7385");
					return false;
				}
				
				  
				try  {
					dChange = dJustCollectedValue.subtract(dInitialFactDataValue).divide(dInitialFactDataValue,BigDecimal.ROUND_HALF_UP);
				}
				catch (ArithmeticException ae) {
					UtilityFunctions.stdoutwriter.writeln("Error doing BigDecimal arithmetic",Logs.ERROR,"A2.7385");
					UtilityFunctions.stdoutwriter.writeln("Error occured processing alert id: " + nAlertId,Logs.ERROR,"A2.7386");
					UtilityFunctions.stdoutwriter.writeln(ae);
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
	
	private boolean customParseBoolean(String str)	{
		if (str.toUpperCase().equals("FALSE"))
			return false;
		else if (str.toUpperCase().equals("TRUE"))
			return true;
		else
			throw new RuntimeException("Problem Parsing Boolean");
		
	}
	
	
	
	
	  
	/**
	 * This is the replacement of UtilityFunctions.convertRowSetToHashMap method to convert the FactData list into a map of type HashMap<TheKey,HashMap<String,String>>.
	 * This iterate over the List<FactData> and create the HashMap out of that.
	 * @param factDataList
	 * @return HashMap<TheKey,HashMap<String,String>>
	 * @throws DataAccessException
	 * @throws PikefinException
	 */
	public  HashMap<TheKey,HashMap<String,String>> convertFactDataListToHashMap(List<FactData> factDataList) throws DataAccessException, PikefinException	{
		
		HashMap<TheKey,HashMap<String,String>> parentHash = new HashMap<TheKey,HashMap<String,String>>();
	
		for(FactData item:factDataList){
			HashMap<String,String> childHash = new HashMap<String,String>();
			childHash.put(item.getClass().getName()+"."+"dataGroup",item.getDataGroup()!=null?item.getDataGroup():"");
			childHash.put(item.getClass().getName()+"."+"batchId",item.getBatchId()!=null?String.valueOf(item.getBatchId().getBatchId()):"");
			childHash.put(item.getClass().getName()+"."+"calmonth",item.getCalmonth()!=null?String.valueOf(item.getCalmonth()):"");
			childHash.put(item.getClass().getName()+"."+"calquarter",item.getCalquarter()!=null?String.valueOf(item.getCalquarter()):"");
			childHash.put(item.getClass().getName()+"."+"calyear",item.getCalyear()!=null?String.valueOf(item.getCalyear()):"");
			childHash.put(item.getClass().getName()+"."+"dateCollected",item.getDateCollected()!=null?String.valueOf(item.getDateCollected()):"");
			childHash.put(item.getClass().getName()+"."+"day",item.getDay()!=null?String.valueOf(item.getDay()):"");
			childHash.put(item.getClass().getName()+"."+"factDataId",item.getFactDataId()!=null?String.valueOf(item.getFactDataId()):"");
			childHash.put(item.getClass().getName()+"."+"fiscalquarter",item.getFiscalquarter()!=null?String.valueOf(item.getFiscalquarter()):"");
			childHash.put(item.getClass().getName()+"."+"fiscalyear",item.getFiscalyear()!=null?String.valueOf(item.getFiscalyear()):"");
			childHash.put(item.getClass().getName()+"."+"metaSetId",item.getMetaSetId()!=null?String.valueOf(item.getMetaSetId()):"");
			childHash.put(item.getClass().getName()+"."+"metricId",item.getMetricId()!=null?String.valueOf(item.getMetricId()):"");
			childHash.put(item.getClass().getName()+"."+"scale",item.getScale()!=null?String.valueOf(item.getScale()):"");
			childHash.put(item.getClass().getName()+"."+"value",item.getValue()!=null?String.valueOf(item.getValue()):"");
			childHash.put(item.getClass().getName()+"."+"garbageCollect",item.getValue()!=null?String.valueOf(item.isGarbageCollect()):"");
			childHash.put(item.getClass().getName()+"."+"manualCorrection",item.getValue()!=null?String.valueOf(item.isManualCorrection()):"");
			childHash.put(item.getClass().getName()+"."+"raw",item.getValue()!=null?String.valueOf(item.isRaw()):"");
			childHash.put(item.getClass().getName()+"."+"entityId",item.getValue()!=null?String.valueOf(item.getEntityId()):"");

			String strHashKey1 = item.getValue()!=null?String.valueOf(item.getEntityId()):"";
		
			String	strHashKey2 = item.getCalyear()!=null?String.valueOf(item.getCalyear()):"";
			if (strHashKey1 == null) {
				UtilityFunctions.stdoutwriter.writeln("Problem converting ResultSet to HashMap - column for hash key not found",Logs.ERROR,"UF18.7");
				throw new PikefinException("Custom SQL Exception - Unable to convert ResultSet to HashMap");
			}
			
			TheKey tk = new TheKey(strHashKey1,strHashKey2);
			parentHash.put(tk, childHash);	
			
		}
				
		return parentHash;
	}
}

class TheKey {
	
	 public final String strVal1;
	 public final String strVal2;
	 
	 public TheKey(String strVal1, String strVal2) {
		    this.strVal1 = strVal1; this.strVal2 = strVal2; //this.k3 = k3; this.k4 = k4;
		  }
	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		int outerhashcode = 7;
		//result = prime * result + getOuterType().hashCode();
		result = prime * result + outerhashcode;
		result = prime * result
				+ ((strVal1 == null) ? 0 : strVal1.hashCode());
		result = prime * result
				+ ((strVal2 == null) ? 0 : strVal2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TheKey other = (TheKey) obj;
		/*if (!getOuterType().equals(other.getOuterType()))
			return false;*/
		if (strVal1 == null) {
			if (other.strVal1 != null)
				return false;
		} else if (!strVal1.equals(other.strVal1))
			return false;
		if (strVal2 == null) {
			if (other.strVal2 != null)
				return false;
		} else if (!strVal2.equals(other.strVal2))
			return false;
		return true;
	}
	/*private TheKey getOuterType() {
		return this;
	}*/
	
	
	
}
