import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


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
		  
		  ResultSet rs = dg.dbf.db_run_query(query);
		  if (rs.next())
		  {
			  int nKey = rs.getInt("schedules.id");
			  strTask = rs.getString("tasks.name");
			  
			  query = "select alerts.*,entities.ticker,entities.id from alerts,entities where alerts.entity_id=entities.id and alerts.schedule_id=" + nKey;
			  rs = dg.dbf.db_run_query(query);
			  //loop through the schedules associated with this notification
			  int nRow=0;
			  
			  /*
			   * 
			   * Moved this code outside of the loop to speed up processing.
			   */
			  query = "select max(batch) from fact_data where task_id=" + nCurTask;
			  ResultSet rs7 = dg.dbf.db_run_query(query);
			  rs7.next();
			  int nMaxBatch = rs7.getInt("max(batch)");
			  
			  
			
			  while (rs.next())
			  {
				  nRow++;
				  if (rs.getInt("disabled")>0)
					  continue;
				  
				  //String strType = rs.getString("type");
				  String strTicker = rs.getString("ticker");
				  int nEntityId = rs.getInt("entities.id");
				  String strFrequency = rs.getString("frequency");
				  //String strEmail = rs.getString("email");
				  int nUserId = rs.getInt("user_id");
				 
				  
				  //Add the base limit adjustment to the base limit value
				  BigDecimal dLimit = rs.getBigDecimal("limit_value").add(rs.getBigDecimal("limit_adjustment"));
				  
				  int nFactKey = rs.getInt("fact_data_key");
				  int nNotifyKey = rs.getInt("id");
				  int nAlertCount = rs.getInt("alert_count");
				  
				  //try to find the fact key in fact_data
				  String query2 = "select * from fact_data where id=" + nFactKey;
				  ResultSet rs2 = dg.dbf.db_run_query(query2);
				  
				  String query3;
				  if (!(strTicker==null) && !(strTicker.isEmpty()))
					  query3 = "select * from fact_data where task_id=" + nCurTask + " and entity_id='" + nEntityId + "' and batch=" + nMaxBatch;
					//  "(select max(batch) from fact_data where task=" + nCurTask + " and ticker='" + strTicker + "')";
				  else
					  query3 = "select * from fact_data where task_id=" + nCurTask + " and batch=" + nMaxBatch;
					//  "(select max(batch) from fact_data where task=" + nCurTask + ")";
				  
				  ResultSet rs3 = dg.dbf.db_run_query(query3);
				
				  if (rs2.next())
				  {
					  //if (strFrequency.equals("HOURLY"))
					  //{
						  Calendar calJustCollected;
						  Calendar calLastRun = Calendar.getInstance();
						  try
						  {
							  calJustCollected = dg.getJobProcessingEndTime();
						  }
						  catch (CustomGenericException cge)
						  //added this exception for multi-threading purposes - this should be populated by here
						  {
							  UtilityFunctions.stdoutwriter.writeln("Attempted to read empty end time, skipping notification for this job",Logs.ERROR,"A2.73");
							  UtilityFunctions.stdoutwriter.writeln(cge);
							  return;
							  
						  }
						  
						
						//get the date
						  /*
						   * resultSet.getDate() drops the time value
						   */
						 //calLastRun.setTime(rs2.getDate("date_collected"));
						  calLastRun.setTime(rs2.getTimestamp("date_collected"));
						  if (strFrequency.equals("HOURLY"))
						  {
							  calLastRun.add(Calendar.HOUR,1);
						  }
						  else if (strFrequency.equals("YEARLY"))
						  {
							  calLastRun.add(Calendar.YEAR, 1);
						  }
						  else if (strFrequency.equals("WEEKLY"))
						  {
							  calLastRun.add(Calendar.WEEK_OF_YEAR,1);
						  }
						  else if (strFrequency.equals("MONTHLY"))
						  {
							  calLastRun.add(Calendar.MONTH,1);
						  }
						  else if (strFrequency.equals("DAILY"))
						  {
							  calLastRun.add(Calendar.DAY_OF_YEAR,1);
						  }
						  else if (strFrequency.equals("MINUTE"))
						  {
							  calLastRun.add(Calendar.MINUTE,1);
						  }
						  else if (strFrequency.equals("ALLTIME"))
						  {
							  //don't add any time
						  }
						  else
						  {
							  UtilityFunctions.stdoutwriter.writeln("alerts frequency not found, skipping job notification",Logs.ERROR,"A2.735");
							  continue;
						  }
						  
						
						  
						  //if (calJustCollected.after(calLastRun))
						  //{
						  
						  //Check if limit is exceeded, if so then send alert.
							  if (!rs3.next())
		                      {
		                          UtilityFunctions.stdoutwriter.writeln("Attempting to check for an alert but did not find fact_data entry",Logs.ERROR,"A2.7355");
		                          UtilityFunctions.stdoutwriter.writeln("Error occured processing task: " + strTask + ",entity_id: " + nEntityId + ",ticker: " + strTicker,Logs.ERROR,"A2.7356");
		                          continue;
		                      }
							  
							  BigDecimal dJustCollectedValue = rs3.getBigDecimal("value");
							  BigDecimal dLastRunValue = rs2.getBigDecimal("value");
							  			  
							  BigDecimal dChange;
							  
							  try
							  {
								  dChange = dJustCollectedValue.subtract(dLastRunValue).divide(dLastRunValue,BigDecimal.ROUND_HALF_UP);
							  }
							  catch (ArithmeticException ae)
							  {
								  UtilityFunctions.stdoutwriter.writeln("Error doing BigDecimal arithmetic",Logs.ERROR,"A2.7385");
								  UtilityFunctions.stdoutwriter.writeln("Error occured processing task: " + strTask + ",entity_id: " + nEntityId + ",ticker: " + strTicker,Logs.ERROR,"A2.7386");
								  UtilityFunctions.stdoutwriter.writeln(ae);
								  continue;
							  }
							  
							  String query6 = "select max_alerts,email from users where id='" + nUserId + "'";
							  ResultSet rs6 = dg.dbf.db_run_query(query6);
							  
							  if (!rs6.next())
							  {
								  UtilityFunctions.stdoutwriter.writeln("Id " + nUserId + " not found in users table.",Logs.ERROR,"A2.738");
								  UtilityFunctions.stdoutwriter.writeln("Error occured processing task: " + strTask + ",entity_id: " + nEntityId + ",ticker: " + strTicker,Logs.ERROR,"A2.739");
								  continue;
							  }
							  
							  String strEmail = rs6.getString("email");
							  
							 /* The old protocol was if an alert was triggered, we will automatically reset the initial value.
							  * The new protocol is to include a link to a form to increase the value. 
							  * Alerts will continue to be sent until max_alerts in the account table is reached OR
							  * the an alert adjustment is submitted.
							  * 
							  */
							 // String query4 = "update notify set fact_data_key=" + rs3.getInt("primary_key") + " where primary_key=" + nNotifyKey;
							  if ((dChange.abs().compareTo(dLimit) > 0) && (nAlertCount<rs6.getInt("max_alerts")))
							  {
								  //send notification
								  //System.out.println("ALERT: Send mail!");
								  String strMsg;
								  
								  
								  
								  strMsg = "ALERT\r\n";
								  
								  if (strTicker != null && !(strTicker.isEmpty()))
										  strMsg = strMsg + "Ticker: " + strTicker + "\r\n";
								  
								  //Get the full ticker description
								  String query5 = "select full_name from entities where ticker='"+ strTicker + "'";
								  ResultSet rs5 = dg.dbf.db_run_query(query5);
								  
								  if (rs5.next())
									  strMsg = strMsg + "Full Description: " + rs5.getString("full_name") +" \r\n";
								  else
									  strMsg = strMsg + "Full Description: \r\n";
								  
								  
								  strMsg = strMsg + "Task: " + strTask + "\r\n";
								  strMsg = strMsg + "Amount: " + dChange.multiply(new BigDecimal(100)).toString() + "%\r\n";
								  strMsg = strMsg + "Previous Value: " + dLastRunValue.toString() + "\r\n";
								  strMsg = strMsg + "Current Value : " + dJustCollectedValue.toString() + "\r\n";
								 
								  
								  SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy HH:mm:ss Z");

								  //strMsg = strMsg + "Previous Timestamp: " + rs2.getTimestamp("date_collected").toString() + "\r\n";
								  strMsg = strMsg + "Previous Timestamp: " + sdf.format(rs2.getTimestamp("date_collected")) + "\r\n";
								  //strMsg = strMsg + "Current Timestamp: " + calJustCollected.toString() + "\r\n";
								  strMsg = strMsg + "Current Timestamp : " + sdf.format(calJustCollected.getTime()) + "\r\n";
								  strMsg = strMsg + "Frequency: " + strFrequency + "\r\n";
								  
								  //Add link for Increase Alert Limit form
								  strMsg = strMsg + "Increase Alert Limit: " + (String)DataLoad.props.getProperty("formbaseurl") + nNotifyKey;							  
								  strMsg += "\r\n\r\nNote: The alert system will continue to send email alerts until 1 of the following conditions is met:";
								  strMsg += "\r\n1) The maximum alert notification count of " + rs6.getInt("max_alerts") + " is reached (configurable in the user properties).";
								  strMsg += "\r\n2) The user modifies the alert adjustment value (configurable in the alert properties)";
								  strMsg += "\r\n3) The alert condition is no longer true.";
								  
								  
								  
								  
								  UtilityFunctions.mail(strEmail,strMsg,(String)DataLoad.props.get("subjecttext"),(String)DataLoad.props.get("fromaddy"));
					
									
								  // this was the old way of updating alert values, now we are using the form 
								  //dbf.db_update_query(query4);
						  
							  }
							  
							  
							  /*
							   * This is the check to see if the alert period has ended and time to update the initial value and reset the adjustment to zero.
							   */
							  if (calJustCollected.after(calLastRun))
							  {
								  String query4 = "update alerts set fact_data_key=" + rs3.getInt("id") + ", limit_adjustment=0 where id=" + nNotifyKey;
								  dg.dbf.db_update_query(query4);
								  
								  						 
							  }
							  
							  
							  
							  
							  
							 
							  
							  
								  
							  
						 // }
						  
						  
						
					 // }  
				  }
				  else
					  //fact_data_key not found, assume this is the first time this notification has been used and needs to be populated
					  //if I base this off of the batch #, have to be very careful with how it is maintained.
				  {
					 
					  if (rs3.next())
					  { 
						  query2 = "update alerts set fact_data_key=" + rs3.getInt("id") + " where id=" + nNotifyKey;
						  dg.dbf.db_update_query(query2);
					  }
						  
					  else
					  //didn't find a fact_data_key with which to populate this notification and there should be at least one there
					  //print a message
						  UtilityFunctions.stdoutwriter.writeln("Attempting to populate row with primary key " + nKey + " in alerts table but did not find fact_data entry",Logs.ERROR,"A2.75");
				  }
				  
				  
				  
				  
				  
			  }
			  
			  
			  
		  }
		  
		  
		  
		  
	  
	  }
	  

}
