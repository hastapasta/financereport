import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class Alerts {
	
	public Alerts()
	{
		
	}
	
	/*
	 * 
	 * PLEASE READ THIS!!!!
	 * Notes on Boolean.parseBoolean(String) - There is an issue with using this method since if the String parameter
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
	public void checkAlerts(DataGrab dg) throws SQLException {
		  
	
		//String strDataSet = dg.strCurDataSet;
		int nCurTask = dg.nCurTask;
		String strTaskName,strTaskDescription;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  
		//String query = "select schedules.*,tasks.name,tasks.description from schedules,tasks where schedules.task_id=" + nCurTask + " and schedules.task_id=tasks.id";
		String query = "select tasks.* from tasks where id=" + nCurTask;
		  
		ResultSet rsTask = dg.dbf.db_run_query(query);
		if (rsTask.next()) {
			strTaskName = rsTask.getString("tasks.name");
			strTaskDescription = rsTask.getString("tasks.description");
			  

			/*
			 * NOTE: YOU CURRENTLY CANNOT USE TABLE ALIASES (i.e as <table name>) IN THIS QUERY!
			 * It has to with how the resultset is getting converted to a hash map and the fact
			 * that java returns the actual table name and not the alias.
			 */
			  
			  
			query = "select alerts.disabled,alerts.id,alerts.initial_fact_data_id,";
			query += "alerts.notification_count,alerts.user_id,alerts.limit_value,";
			query += "alerts.auto_reset_period,alerts.auto_reset_fired,alerts.fired,";
			query += "alerts.email_alert,alerts.twitter_alert,";
			query += "entities.ticker,entities.id,entities.full_name,";
			query += "users.id,users.username,users.max_notifications,users.email,users.bulk_email,";
			query += "fact_data.value,fact_data.id,fact_data.date_collected,";
			query += "time_events.id,time_events.name,time_events.next_datetime,time_events.last_datetime, ";
			query += "tasks.metric_id ";
			query += "from alerts ";
			query += "LEFT JOIN entities ON alerts.entity_id=entities.id ";
			query += "LEFT JOIN users ON alerts.user_id=users.id ";
			query += "LEFT JOIN fact_data ON alerts.initial_fact_data_id=fact_data.id ";
			query += "LEFT JOIN time_events ON alerts.time_event_id=time_events.id ";
			query += "LEFT JOIN tasks ON alerts.task_id=tasks.id ";
			query += "where alerts.task_id=" + nCurTask;
			query += " order by time_events.id";
			  
			 
			ResultSet rsAlert1 = dg.dbf.db_run_query(query);
			  
			ArrayList<HashMap<String,String>> arrayListAlerts = UtilityFunctions.convertResultSetToArrayList(rsAlert1);
			  
			//check if there are any alerts to process
			if (arrayListAlerts == null)		
				return;
			 
			int nRow=0;
			  
			/*
			 * 
			 * Moved this code outside of the loop to speed up processing.
			 */
			query = "select max(batch_id) from fact_data,batches where fact_data.batch_id=batches.id AND task_id=" + nCurTask;
			ResultSet rs7 = dg.dbf.db_run_query(query);
			rs7.next();
			int nMaxBatch = rs7.getInt("max(batch_id)");
			  
			String query3;
			query3 = "select fact_data.* ";
			query3 += "from fact_data, batches ";
			query3 += " where fact_data.batch_id=batches.id ";
			query3 += " and task_id=" + nCurTask;
			query3 += " and batch_id=" + nMaxBatch;
			
			  
			ResultSet rsCurFactData1 = dg.dbf.db_run_query(query3);
			  
			  
			HashMap<String,HashMap<String,String>> hmCurFactData = UtilityFunctions.convertResultSetToHashMap(rsCurFactData1,"entity_id");
			  
			if (hmCurFactData == null)  {
                UtilityFunctions.stdoutwriter.writeln("No fact_data entries found for task " + nCurTask + "and batch " + nMaxBatch + ". Alert processing terminated.",Logs.WARN,"A4.0");
                return;
            }
			  

			  
	  
			int nAlertIndex = 0;
			 
			while (nAlertIndex<arrayListAlerts.size()) {
				  //HashMap<String,String> hmAlert = arrayListAlerts.get(nAlertIndex);
				HashMap<String,String> hmAlert = arrayListAlerts.get(nAlertIndex);
				nAlertIndex++;
				nRow++;
				try  {
				 
					boolean bDisabled = customParseBoolean(hmAlert.get("alerts.disabled"));

					if (bDisabled)
						continue;
					  
					SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					  
					int nEntityId = Integer.parseInt(hmAlert.get("entities.id"));//rsAlert.getInt("entities.id");
					int nAlertId = Integer.parseInt(hmAlert.get("alerts.id"));//rsAlert.getInt("alerts.id");
					String strTicker = hmAlert.get("entities.ticker");//rsAlert.getString("ticker");
					  
					boolean bAutoResetPeriod = customParseBoolean(hmAlert.get("alerts.auto_reset_period"));
					boolean bAutoResetFired = customParseBoolean(hmAlert.get("alerts.auto_reset_fired"));
					boolean bAlreadyFired = customParseBoolean(hmAlert.get("alerts.fired"));
					boolean bEmailAlert = customParseBoolean(hmAlert.get("alerts.email_alert"));
					boolean bTwitterAlert = customParseBoolean(hmAlert.get("alerts.twitter_alert"));
					  
					if ((hmCurFactData.get(nEntityId+"") == null)) {
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
				  
				  
					calJustCollected.setTime(inputFormat.parse(hmCurFactData.get(nEntityId+"").get("fact_data.date_collected")));
					calObservationPeriodBegin.setTime(inputFormat.parse(hmAlert.get("time_events.last_datetime")));
					calObservationPeriodEnd.setTime(inputFormat.parse(hmAlert.get("time_events.next_datetime")));

					/*
					 * This is the check to see if the alert period has ended and time to update the initial value and reset the adjustment to zero.
					 * If the last data value collected is before the beginning of the observation period, then we need a new initial_fact_data_id.
					 */

				
					if ((calObservationPeriodBegin.after(calInitialFactDataCollected) && bAutoResetPeriod) || (bPopulateInitialFactDataId == true))  {
	  
						/*
						 * Here we should retrieve the earliest fact_data.id that is after ObservationPeriodBegin.
						 * Most likely this will be the same fact_data.id as the one just collected, but there may be a reason, like 
						 * the schedule hasn't been run for awhile, that it could be a different id.
						 * 
						 */
							  
						/*
						 * The following query is really slow. One idea to speed it up is since for a give time_event.id, and a given task.id, all entities
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
						strQuery += " order by fact_data.date_collected asc";
							  
						ResultSet rsFactData = dg.dbf.db_run_query(strQuery);
						  
						if (!rsFactData.next()) {
							//we should never get here, but you never know.
							UtilityFunctions.stdoutwriter.writeln("No fact_data entry found to populate initial_fact_data_id for Alert id: " + nAlertId + ". Skipping Alert.",Logs.ERROR,"A10.3");
							continue;
								  
						}
							  
						String query4 = "update alerts set ";
						 
						query4 += "initial_fact_data_id=" +rsFactData.getInt("fact_data.id") + ",";
						query4 += "current_fact_data_id=" + hmCurFactData.get(nEntityId+"").get("fact_data.id") + ", ";
						query4 += "notification_count=0,";
						query4 += "fired=0 ";
						query4 += " where id=" + nAlertId;
						  
						dg.dbf.db_update_query(query4);
						  
						  
						/*
						 * We're past the end of the time period so don't do anything else and proceed to the next alert.
						 */
						continue;
					}
			
					int nUserId = Integer.parseInt(hmAlert.get("alerts.user_id"));//rsAlert.getInt("user_id");
				  
				  
					BigDecimal bdLimitValue = new BigDecimal(hmAlert.get("alerts.limit_value"));
			
					int nNotificationCount = Integer.parseInt(hmAlert.get("alerts.notification_count"));//rsAlert.getInt("alert_count");
				  	
					String strUpdateQuery = "update alerts set current_fact_data_id=" + hmCurFactData.get(nEntityId+"").get("fact_data.id") + " where alerts.id=" + nAlertId;
					dg.dbf.db_update_query(strUpdateQuery);

					BigDecimal dJustCollectedValue = new BigDecimal(hmCurFactData.get(nEntityId+"").get("fact_data.value"));
					  
					//Not sure of the scale
					if (dJustCollectedValue.equals(new BigDecimal("0.000")) || dJustCollectedValue.equals(new BigDecimal("0.00")))  {
						UtilityFunctions.stdoutwriter.writeln("Zero was the last value collected. Probably a bad print. Skipping ticker " + strTicker,Logs.WARN,"A2.7385");
						continue;
					}
					
					BigDecimal dInitialFactDataValue = new BigDecimal(hmAlert.get("fact_data.value"));
					  			  
					BigDecimal dChange;
							  
					try  {
						dChange = dJustCollectedValue.subtract(dInitialFactDataValue).divide(dInitialFactDataValue,BigDecimal.ROUND_HALF_UP);
					}
					catch (ArithmeticException ae) {
						UtilityFunctions.stdoutwriter.writeln("Error doing BigDecimal arithmetic",Logs.ERROR,"A2.7385");
						UtilityFunctions.stdoutwriter.writeln("Error occured processing alert id: " + nAlertId,Logs.ERROR,"A2.7386");
						UtilityFunctions.stdoutwriter.writeln(ae);
						continue;
					}
					  
					
					if (hmAlert.get("users.email") == null) {
						UtilityFunctions.stdoutwriter.writeln("Id " + nUserId + " not found in users table.",Logs.WARN,"A2.738");
						UtilityFunctions.stdoutwriter.writeln("Problem occured processing task: " + strTaskName + ",entity_id: " + nEntityId + ",ticker: " + strTicker,Logs.WARN,"A2.739");
						continue;
					}
					  
					String strEmail = hmAlert.get("users.email");
					int nMaxNotifications = Integer.parseInt(hmAlert.get("users.max_notifications"));
					  
					boolean bEmailSent = false;
					  
					/* The old protocol was if an alert was triggered, we will automatically reset the initial value.
					 * The new protocol is to include a link to a form to increase the value. 
					 * Alerts will continue to be sent until max_alerts in the account table is reached OR
					 * the an alert adjustment is submitted.
					 * 
					 */
			
					if ((dChange.abs().compareTo(bdLimitValue) > 0) && (bAlreadyFired == false))  {
							
						/*String strQuery10 = "select * from alerts_alerts_targets where alert_id=" + nAlertId;
						  
						ResultSet rs10 = dg.dbf.db_run_query(strQuery10);*/
								  

								  
						if (bEmailAlert == true && hmAlert.get("users.bulk_email").toUpperCase().equals("FALSE")) {
								  
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
								 
								  
								strMsg += "Chart: " + (String)DataLoad.props.getProperty("phpbaseurl") + "charts/allassets/linechart.php?a=" + nAlertId + "\r\n";
										  
								//Add link for Increase Alert Limit form
								strMsg = strMsg + "Modify/View Alert Properties: " + (String)DataLoad.props.getProperty("cakebaseurl") + "alerts/edit?alert=" + nAlertId;
								  
								String strSubject = (String)DataLoad.props.get("subjecttext") + " : " + strTicker + " : " + hmAlert.get("time_events.name") + " Observation Period";
								  
								//Emails and Tweets are globally disabled for testing
								if (DataLoad.bDisableEmails == false)  {
									UtilityFunctions.mail(strEmail,strMsg,strSubject,(String)DataLoad.props.get("fromaddy"));
								}
								  
								String query9 = "update alerts set notification_count=" + (nNotificationCount+1) + " where id=" + nAlertId;
								  
								dg.dbf.db_update_query(query9);
								  
								bEmailSent = true;
							}
						}
								  
						if (bTwitterAlert == true)  {
							dChange = dChange.multiply(new BigDecimal(100)).setScale(2);
							String strTweet = dChange.toString() + "% ";
							strTweet += hmAlert.get("time_events.name");
							  
							if (hmAlert.get("tasks.metric_id").equals("11"))
								 strTweet += " Futures";
							strTweet += " move in " + strTicker;
							if (hmAlert.get("entities.full_name") != null)
								strTweet += " (" + hmAlert.get("entities.full_name") + ")";
									  
									
									  
									  
							String strUrl = (String)DataLoad.props.getProperty("phpbaseurl") + "charts/allassets/linechart.php?a=" + nAlertId;
							  
							//Emails and Tweets are globally disabled for testing
							if (DataLoad.bDisableEmails == false)  {
									  
								strTweet += " " + UtilityFunctions.shortenURL(strUrl);
								  
								  
								  
								/*
								 * Figure out if this alert represents an actual stock ticker. If so, then use the $ tag; otherwise use the # tag.
								 */
								  
								String tmpQuery = "select * from entities_entity_groups where entity_id=" + hmAlert.get("entities.id") + " AND entity_group_id=1";
								  
								ResultSet rs10 = dg.dbf.db_run_query(tmpQuery);
								  
								if (rs10.next())
									strTweet += " $" + strTicker;
								else {
									String strTmp = strTicker.replace(" ","").replace("(","").replace(")","").replace(".", "").replace("-", "").replace("#", "").replace("&", "and");
									strTweet += " #" + strTmp;
								}
										  
								//strTweet += " $$";
								  
								String strErrorMsg = UtilityFunctions.tweet(strTweet);
								  
								try {
									String strTweetLogQuery = "insert into log_tweets (datetime,message,alert_id,error_message) ";
								
									strTweetLogQuery += " values (NOW(),'" + (strTweet.length()>140?strTweet.substring(0,140):strTweet) + "'," + hmAlert.get("alerts.id");
									if (strErrorMsg.isEmpty())
										strTweetLogQuery += ",null)";
									else
										strTweetLogQuery += ",'" + (strErrorMsg.length()>200?strErrorMsg.substring(0,200):strErrorMsg) + "')";
									dg.dbf.db_update_query(strTweetLogQuery);
								} 
								catch (SQLException sqle) {
									UtilityFunctions.stdoutwriter.writeln("Issue inserting into log_tweets",Logs.ERROR,"A4.6");
									UtilityFunctions.stdoutwriter.writeln(sqle);
								}
							}
  				  
						}
			 
						  
						String query8 = "insert into log_alerts (alert_id,date_time_fired,bef_fact_data_id,aft_fact_data_id,limit_value,entity_id,user_id,email_sent,diff) values (" 
						+ nAlertId + ",'"
						  
						/*
						 *  We already have a record of the after fact data time (linked to by aft_fact_data_id) so it's pointless to save it again as the
						 *  alert fired time. So we will switch here to using the current time.
						 * 
						 */

						+ formatter.format(Calendar.getInstance().getTime()) + "',"
								  
								  						  
						//+ rsCurFactData.getInt("id") + ","
						+ hmAlert.get("fact_data.id") + ","
						+ hmCurFactData.get(nEntityId+"").get("fact_data.id") + ","		
						//+ "'" + strFrequency + "',"
						//+ rsAlert.getBigDecimal("limit_value") + ","
						+ hmAlert.get("alerts.limit_value") + ","
						//+ rsAlert.getBigDecimal("limit_adjustment") + ","
						//+ hmAlert.get("alerts.limit_adjustment") + ","
						//+ rsAlert.getInt("entity_id") + ","
						+ hmAlert.get("entities.id") + ","
						//+ rsAlert.getInt("user_id") + ")";
						+ hmAlert.get("users.id") + ",";
						if (bEmailSent)
							query8 += "1,";
						else
							query8 += "0,";
						query8 += dChange + ")";
								  
						dg.dbf.db_update_query(query8);
						  
						if (bAutoResetFired)	  {
							query8 = "update alerts set notification_count=0,initial_fact_data_id=" + hmCurFactData.get(nEntityId+"").get("fact_data.id") + " where id=" + nAlertId;
						}
						else  {
							query8 = "update alerts set fired=1 where id=" + nAlertId;
						}
									  
					
								  
						dg.dbf.db_update_query(query8);
								  
					}

				}
				catch (ParseException pe)  {
					UtilityFunctions.stdoutwriter.writeln("Problem parsing date for alert " + hmAlert.get("alerts.id") + ", alert not updated",Logs.ERROR,"A2.753");
					UtilityFunctions.stdoutwriter.writeln(pe);
					  
				}
				  
			}
			  
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
	
	  

}
