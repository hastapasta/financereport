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
	
	
	public void checkAlerts(DataGrab dg) throws SQLException
	  {
		  
		  /*
		   * Ulitmately I'm going to have to move this function out of the parent thread since it will delay the initiating of tasks.
		   * 
		   */
		  //String strDataSet = dg.strCurDataSet;
		  int nCurTask = dg.nCurTask;
		  String strTask;
		  
		  String query = "select schedules.*,tasks.name from schedules,tasks where schedules.task_id=" + nCurTask + " and schedules.task_id=tasks.id";
		  
		  ResultSet rsSchedule = dg.dbf.db_run_query(query);
		  if (rsSchedule.next())
		  {
			  int nKey = rsSchedule.getInt("schedules.id");
			  strTask = rsSchedule.getString("tasks.name");
			  
			  //String strLockQuery = "lock table alerts write";
			  //dg.dbf.db_update_query(strLockQuery);
			  
			  
			  query = "select alerts.disabled,alerts.id,alerts.frequency,alerts.initial_fact_data_id,alerts.alert_count,alerts.user_id,alerts.limit_value,alerts.limit_adjustment,";
			  query += "entities.ticker,entities.id,entities.full_name,";
			  query += "users.id,users.username,users.max_alerts,users.email,";
			  query += "fact_data.value,fact_data.id,fact_data.date_collected ";
			  query += "from alerts ";
			  query += "LEFT JOIN entities ON alerts.entity_id=entities.id ";
			  query += "LEFT JOIN users ON alerts.user_id=users.id ";
			  query += "LEFT JOIN fact_data ON alerts.initial_fact_data_id=fact_data.id ";
			  query +=	"where alerts.schedule_id=" + nKey;
			  
			 
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
			  query = "select max(batch) from fact_data where task_id=" + nCurTask;
			  ResultSet rs7 = dg.dbf.db_run_query(query);
			  rs7.next();
			  int nMaxBatch = rs7.getInt("max(batch)");
			  
			  String query3;
			  query3 = "select * from fact_data where task_id=" + nCurTask + " and batch=" + nMaxBatch;
			  /*if (!(strTicker==null) && !(strTicker.isEmpty()))
				  query3 = "select * from fact_data where task_id=" + nCurTask + " and entity_id='" + nEntityId + "' and batch=" + nMaxBatch;
				//  "(select max(batch) from fact_data where task=" + nCurTask + " and ticker='" + strTicker + "')";
			  else
				  query3 = "select * from fact_data where task_id=" + nCurTask + " and batch=" + nMaxBatch;*/
				//  "(select max(batch) from fact_data where task=" + nCurTask + ")";
			  
			  ResultSet rsCurFactData1 = dg.dbf.db_run_query(query3);
			  
			  HashMap<String,HashMap<String,String>> hmCurFactData = UtilityFunctions.convertResultSetToHashMap(rsCurFactData1,"entity_id");
			  
			  if (hmCurFactData == null)
              {
                  UtilityFunctions.stdoutwriter.writeln("No fact_data entries found for task " + nCurTask + "and batch " + nMaxBatch + ". Alert processing terminated.",Logs.ERROR,"A4.0");
                  return;
              }
			  
			  
			  int nAlertIndex = 0;
			  //while (rsAlert.next())
			  while (nAlertIndex<arrayListAlerts.size())
			  {
				  HashMap<String,String> hmAlert = arrayListAlerts.get(nAlertIndex);
				  nAlertIndex++;
				  nRow++;
				  try
				  {
				  
				  
				  
				  boolean bDisabled = Boolean.parseBoolean(hmAlert.get("alerts.disabled"));
				  
				  
				  //if (rsAlert.getInt("disabled")>0)
				  if (bDisabled)
					  continue;
				  
				  //String strType = rsAlert.getString("type");
				  String strTicker = hmAlert.get("entities.ticker");//rsAlert.getString("ticker");
				  int nAlertId = Integer.parseInt(hmAlert.get("alerts.id"));//rsAlert.getInt("alerts.id");
				  int nEntityId = Integer.parseInt(hmAlert.get("entities.id"));//rsAlert.getInt("entities.id");
				  String strFrequency = hmAlert.get("alerts.frequency");//sAlert.getString("frequency");
				  //String strEmail = rsAlert.getString("email");
				  int nUserId = Integer.parseInt(hmAlert.get("alerts.user_id"));//rsAlert.getInt("user_id");
				  
			
					 
					  
				 
				  
				  //Add the base limit adjustment to the base limit value
				  
				  BigDecimal bdLimitValue = new BigDecimal(hmAlert.get("alerts.limit_value"));
				  BigDecimal bdLimitAdjustment = new BigDecimal(hmAlert.get("alerts.limit_adjustment"));
				 
				  BigDecimal dLimit = bdLimitValue.add(bdLimitAdjustment);//rsAlert.getBigDecimal("limit_value").add(rsAlert.getBigDecimal("limit_adjustment"));
				  
				  //int nFactKey = Integer.parseInt(hmAlert.get("alerts.initial_fact_data_id"));//rsAlert.getInt("fact_data_key");
				 // int nNotifyKey = Integer.parseInt(hmAlert.get("alerts.id"));//rsAlert.getInt("id");
				  int nAlertCount = Integer.parseInt(hmAlert.get("alerts.alert_count"));//rsAlert.getInt("alert_count");
				  
				  //try to find the fact key in fact_data
				  /*String query2 = "select * from fact_data where id=" + nFactKey;
				  ResultSet rsPrevFactData = dg.dbf.db_run_query(query2);*/
				  
				  //String query3;
				  //query3 = "select * from fact_data where task_id=" + nCurTask + " and entity_id='" + nEntityId + "' and batch=" + nMaxBatch;
				  /*if (!(strTicker==null) && !(strTicker.isEmpty()))
					  query3 = "select * from fact_data where task_id=" + nCurTask + " and entity_id='" + nEntityId + "' and batch=" + nMaxBatch;
					//  "(select max(batch) from fact_data where task=" + nCurTask + " and ticker='" + strTicker + "')";
				  else
					  query3 = "select * from fact_data where task_id=" + nCurTask + " and batch=" + nMaxBatch;*/
					//  "(select max(batch) from fact_data where task=" + nCurTask + ")";
				  
				  //ResultSet rsCurFactData1 = dg.dbf.db_run_query(query3);
				  
				  //HashMap<String,HashMap<String,String>> hmCurFactData = UtilityFunctions.convertResultSetToHashMap(rsCurFactData1,nEntityId+"");
				
				  if (hmAlert.get("fact_data.value") != null)
				  {
					  //if (strFrequency.equals("HOURLY"))
					  //{
					  
					  /*Calendar startthis = Calendar.getInstance();
					  SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					  startthis.setTime(inputFormat.parse(temp)); */
					  SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					  Calendar calOrigRun = Calendar.getInstance();
					  //Another variable since calOrigRun is modified
					  Calendar calOrigRunSaved = Calendar.getInstance();
					  calOrigRun.setTime(inputFormat.parse(hmAlert.get("fact_data.date_collected")));
					  calOrigRunSaved.setTime(inputFormat.parse(hmAlert.get("fact_data.date_collected")));
				
						  
					
						  Calendar calJustCollected;
						
						  try
						  {
							  calJustCollected = dg.getJobProcessingEndTime();
						  }
						  catch (CustomGenericException cge)
						  //added this exception for multi-threading purposes - this should be populated by here
						  {
							  UtilityFunctions.stdoutwriter.writeln("Attempted to read empty end time, terminating alerts processing",Logs.ERROR,"A2.73");
							  UtilityFunctions.stdoutwriter.writeln(cge);
							  return;
							  
						  }
						  
						
						//get the date
						  /*
						   * resultSet.getDate() drops the time value
						   */
						 //calLastRun.setTime(rsPrevFactData.getDate("date_collected"));
						  //calLastRun.setTime(rsPrevFactData.getTimestamp("date_collected"));
						  if (strFrequency.equals("HOURLY"))
						  {
							  calOrigRun.add(Calendar.HOUR,1);
						  }
						  else if (strFrequency.equals("YEARLY"))
						  {
							  calOrigRun.add(Calendar.YEAR, 1);
						  }
						  else if (strFrequency.equals("WEEKLY"))
						  {
							  calOrigRun.add(Calendar.WEEK_OF_YEAR,1);
						  }
						  else if (strFrequency.equals("MONTHLY"))
						  {
							  calOrigRun.add(Calendar.MONTH,1);
						  }
						  else if (strFrequency.equals("DAILY"))
						  {
							  calOrigRun.add(Calendar.DAY_OF_YEAR,1);
						  }
						  else if (strFrequency.equals("MINUTE"))
						  {
							  calOrigRun.add(Calendar.MINUTE,1);
						  }
						  else if (strFrequency.equals("ALLTIME"))
						  {
							  //don't add any time
						  }
						  else
						  {
							  UtilityFunctions.stdoutwriter.writeln("alerts frequency not found, skipping alert processing for alert id " + nAlertId,Logs.WARN,"A2.735");
							  continue;
						  }
						  
						
						  
						  //if (calJustCollected.after(calLastRun))
						  //{
						  
						  //Check if limit is exceeded, if so then send alert.
							  //if (!rsCurFactData.next())
						  	if (hmCurFactData.get(nEntityId+"") == null)
		                      {
		                          UtilityFunctions.stdoutwriter.writeln("Attempting to check for an alert but did not find fact_data entry for entity id: " + nEntityId + " and batch: " + nMaxBatch,Logs.WARN,"A2.7358");
		                          UtilityFunctions.stdoutwriter.writeln("Problem occured processing alert id: " + nAlertId,Logs.WARN,"A2.7358");
		                          continue;
		                      }
						  	
						  	String strUpdateQuery = "update alerts set current_fact_data_id=" + hmCurFactData.get(nEntityId+"").get("fact_data.id") + " where alerts.id=" + nAlertId;
						  	dg.dbf.db_update_query(strUpdateQuery);
						  	
							  
						  	//BigDecimal dJustCollectedValue = rsCurFactData.getBigDecimal("value");
							  BigDecimal dJustCollectedValue = new BigDecimal(hmCurFactData.get(nEntityId+"").get("fact_data.value"));		  
							  //BigDecimal dLastRunValue = rsPrevFactData.getBigDecimal("value");
							  BigDecimal dLastRunValue = new BigDecimal(hmAlert.get("fact_data.value"));
							  			  
							  BigDecimal dChange;
							  
							  try
							  {
								  dChange = dJustCollectedValue.subtract(dLastRunValue).divide(dLastRunValue,BigDecimal.ROUND_HALF_UP);
							  }
							  catch (ArithmeticException ae)
							  {
								  UtilityFunctions.stdoutwriter.writeln("Error doing BigDecimal arithmetic",Logs.WARN,"A2.7385");
								  UtilityFunctions.stdoutwriter.writeln("Error occured processing alert id: " + nAlertId,Logs.WARN,"A2.7386");
								  UtilityFunctions.stdoutwriter.writeln(ae);
								  continue;
							  }
							  
							  //String query6 = "select max_alerts,email from users where id='" + nUserId + "'";
							  //ResultSet rs6 = dg.dbf.db_run_query(query6);
							  
							  if (hmAlert.get("users.email") == null)
							  {
								  UtilityFunctions.stdoutwriter.writeln("Id " + nUserId + " not found in users table.",Logs.WARN,"A2.738");
								  UtilityFunctions.stdoutwriter.writeln("Problem occured processing task: " + strTask + ",entity_id: " + nEntityId + ",ticker: " + strTicker,Logs.WARN,"A2.739");
								  continue;
							  }
							  
							  String strEmail = hmAlert.get("users.email");
							  int nMaxAlerts = Integer.parseInt(hmAlert.get("users.max_alerts"));
							  
							 /* The old protocol was if an alert was triggered, we will automatically reset the initial value.
							  * The new protocol is to include a link to a form to increase the value. 
							  * Alerts will continue to be sent until max_alerts in the account table is reached OR
							  * the an alert adjustment is submitted.
							  * 
							  */
							 // String query4 = "update notify set fact_data_key=" + rsCurFactData.getInt("primary_key") + " where primary_key=" + nNotifyKey;
							  if ((dChange.abs().compareTo(dLimit) > 0) && (nAlertCount<nMaxAlerts))
							  {
								  //send notification
								  //System.out.println("ALERT: Send mail!");
								  String strMsg;
								  
								  
								  
								  strMsg = "ALERT\r\n";
								  
								  if (strTicker != null && !(strTicker.isEmpty()))
										  strMsg = strMsg + "Ticker: " + strTicker + "\r\n";
								  
								  //Get the full ticker description
								  //String query5 = "select full_name from entities where ticker='"+ strTicker + "'";
								  //ResultSet rs5 = dg.dbf.db_run_query(query5);
								  
								  if (hmAlert.get("entities.full_name") != null)
									  strMsg = strMsg + "Full Description: " + hmAlert.get("entities.full_name") +" \r\n";
								  else
									  strMsg = strMsg + "Full Description: \r\n";
								  
								  
								  strMsg = strMsg + "Task: " + strTask + "\r\n";
								  strMsg = strMsg + "Amount: " + dChange.multiply(new BigDecimal(100)).toString() + "%\r\n";
								  strMsg = strMsg + "Previous Value: " + dLastRunValue.toString() + "\r\n";
								  strMsg = strMsg + "Current Value : " + dJustCollectedValue.toString() + "\r\n";
								 
								  
								  SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy HH:mm:ss Z");

								 
								  //strMsg = strMsg + "Previous Timestamp: " + sdf.format(rsPrevFactData.getTimestamp("date_collected")) + "\r\n";
								  strMsg = strMsg + "Previous Timestamp: " + sdf.format(calOrigRunSaved.getTime()) + "\r\n";
								  strMsg = strMsg + "Current Timestamp : " + sdf.format(calJustCollected.getTime()) + "\r\n";
								  strMsg = strMsg + "Frequency: " + strFrequency + "\r\n";
								  
								  //Add link for Increase Alert Limit form
								  strMsg = strMsg + "Increase Alert Limit: " + (String)DataLoad.props.getProperty("formbaseurl") + nAlertId;
								  
								  
								  
								  //strMsg += "\r\n\r\nNote: The alert system will continue to send email alerts until 1 of the following conditions is met:";
								  //strMsg += "\r\n1) The maximum alert notification count of " + rs6.getInt("max_alerts") + " is reached (configurable in the user properties).";
								  //strMsg += "\r\n2) The user modifies the alert adjustment value (configurable in the alert properties)";
								  //strMsg += "\r\n3) The alert condition is no longer true.";
								  
								  String strSubject = (String)DataLoad.props.get("subjecttext") + ": " + strTicker;
								  
								  
								  
								  DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								  
								  String query8 = "insert into log_alerts (alert_id,date_time_fired,bef_fact_data_id,aft_fact_data_id,frequency,limit_value,limit_adjustment,entity_id,user_id) values (" 
									  + nAlertId + ",'"
									  + formatter.format(calJustCollected.getTime()) + "',"
									  //+ rsCurFactData.getInt("id") + ","
									  + hmAlert.get("fact_data.id") + ","
									  + hmCurFactData.get(nEntityId+"").get("fact_data.id") + ","		
									  + "'" + strFrequency + "',"
									  //+ rsAlert.getBigDecimal("limit_value") + ","
									  + hmAlert.get("alerts.limit_value") + ","
									  //+ rsAlert.getBigDecimal("limit_adjustment") + ","
									  + hmAlert.get("alerts.limit_adjustment") + ","
									  //+ rsAlert.getInt("entity_id") + ","
									  + hmAlert.get("entities.id") + ","
									  //+ rsAlert.getInt("user_id") + ")";
									  + hmAlert.get("users.id") + ")";
								  
								  dg.dbf.db_update_query(query8);
								  
								  query8 = "update alerts set alert_count=" + (nAlertCount+1) + " where id=" + nAlertId;
								  
								  dg.dbf.db_update_query(query8);
								  
						
								  
								  
								  
								  UtilityFunctions.mail(strEmail,strMsg,strSubject,(String)DataLoad.props.get("fromaddy"));
					
									
	
						  
							  }
							  
							  
							  /*
							   * This is the check to see if the alert period has ended and time to update the initial value and reset the adjustment to zero.
							   */
							  if (calJustCollected.after(calOrigRun) && !strFrequency.equals("ALLTIME"))
							  {
								  String query4 = "update alerts set initial_fact_data_id=" + hmCurFactData.get(nEntityId+"").get("fact_data.id") + ", alert_count=0,limit_adjustment=0 where id=" + nAlertId;
								  dg.dbf.db_update_query(query4);
								  
								  						 
							  }
							  
							  
							  
							  
							  
							 
							  
							  
								  
							  
						 // }
						  
						  
						
					 // }  
				  }
				  else
					  //fact_data_key not found, assume this is the first time this notification has been used and needs to be populated
					  //if I base this off of the batch #, have to be very careful with how it is maintained.
				  {
					 
					  //if (rsCurFactData.next())
					  if (hmCurFactData.get(nEntityId+"") != null)
					  { 
						  String query2 = "update alerts set initial_fact_data_id=" + hmCurFactData.get(nEntityId+"").get("fact_data.id") + " where id=" + nAlertId;
						  dg.dbf.db_update_query(query2);
					  }
						  
					  else
					  //didn't find a fact_data_key with which to populate this notification and there should be at least one there
					  //print a message
						  UtilityFunctions.stdoutwriter.writeln("Attempting to initially populate alert id " + nAlertId + " but did not find fact_data entry for entity id: " + nEntityId + " and batch: " + nMaxBatch,Logs.WARN,"A2.75");
				  }
				  
				  
				 
				  }
				  catch (ParseException pe)
				  {
					  UtilityFunctions.stdoutwriter.writeln("Problem parsing date for alert " + hmAlert.get("alerts.id") + ", alert not updated",Logs.ERROR,"A2.753");
					  UtilityFunctions.stdoutwriter.writeln(pe);
					  
				  }
				  
			  }//end while
			  
			  //strLockQuery = "unlock tables";
			  //dg.dbf.db_update_query(strLockQuery);
			  
			  
			  
		  }
		  
		  
		  
		  
	  
	  }
	  

}
