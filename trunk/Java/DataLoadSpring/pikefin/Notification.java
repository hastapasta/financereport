package pikefin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;


import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.support.rowset.SqlRowSet;

import pikefin.hibernate.*;
import pikefin.log4jWrapper.Logs;

public class Notification extends Thread {

	//UtilityFunctions uf;
	DBFunctions dbf;
	//ArrayList<String[]> listAlerts;
	ArrayList<HashMap<String,String>> listAlerts;
	ArrayList<HashMap<String, String>> listResultSet;
	DateFormat formatter;


	public Notification(DBFunctions dbfparam) {

		this.dbf = dbfparam;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	}
	


	public void run() {
		
		/*
		 * 
		 * loop through all alert email targets, generating a list of alerts for each target
		 * send email to each target
		 * 
		 * 
		 * at end update all log_alerts as having been processed.
		 */

		int nInterval = 15000;
		boolean bDone = false;
		Calendar calBegin;
		Calendar calEnd;
		int nCount = 0;
	
		//String strLogAlertIds;
		ArrayList<LogAlert> listLogAlerts = new ArrayList<LogAlert>();

		while (!bDone) {

			try {
				//dbf.cycleConnection();
				calBegin = Calendar.getInstance();
				
				/* OFP 5/23/2012 - Refactoring for Hibernate and Spring. I believe this query assumes on email alert target per user.
				 * It looks as if the query should be order by user and then by alert target id - so I added the second sorting criteria.
				 */
				
				/* Actually don't even need to order by user, so for each alert target, gather all of the alerts and send them out. */
				
				/* select * from alert_targets 
				 * join alerts_alert_targets ...
				 * join alerts on ....
				 * join log_alerts on log_alerts.alert_id=alerts.id and log_alerts.email_sent=0
				 * where alert_targets.type=1
				 */

				//String query1 = "select log_alerts.id,alerts.id,time_events.name,log_alerts.date_time_fired,alerts.email_alert,log_alerts.user_id,entities.ticker,log_alerts.diff,bfd.value,afd.value,bfd.date_collected,afd.date_collected, users.email ";
				/*String query1 = "select log_alerts.id,alerts.id,time_events.name,log_alerts.date_time_fired,log_alerts.user_id,entities.ticker, ";
				query1 += " log_alerts.diff,bfd.value,afd.value,bfd.date_collected,afd.date_collected, alert_targets.address, alert_targets.id ";
				query1 += " from log_alerts ";
				query1 += " JOIN entities on log_alerts.entity_id=entities.id ";
				query1 += " JOIN alerts on log_alerts.alert_id=alerts.id ";
				query1 += " JOIN fact_data as bfd on log_alerts.bef_fact_data_id=bfd.id ";
				query1 += " JOIN fact_data as afd on log_alerts.aft_fact_data_id=afd.id ";
				query1 += " JOIN alerts_alert_targets on alerts_alert_targets.alert_id=log_alerts.alert_id ";
				query1 += " JOIN alert_targets on alert_targets.id=alerts_alert_targets.alert_target_id ";		
				//query1 += " JOIN users on log_alerts.user_id=users.id ";
				query1 += " JOIN time_events on alerts.time_event_id=time_events.id ";
				query1 += " where log_alerts.email_sent=0 ";
				//query1 += " and alerts.email_alert=1 ";
				query1 += " and alert_targets.type=1 ";
				query1 += " order by log_alerts.user_id ";*/
				
				/*String query1 = "select LogAlert.logAlertId,Alert.alertId,TimeEvent.name,LogAlert.dateTimeFired,LogAlert.userId,Entity.ticker, ";
				query1 += " LogAlert.diff,beforeFactData.value,afterFactData.value,beforeFactData.dateCollected,afterFactData.dateCollected, AlertTarget.address, AlertTarget.alertTargetId ";
				query1 += " from LogAlert ";
				//query1 += " JOIN Entity ";
				//query1 += " JOIN Alert ";
				//query1 += " JOIN  ";
				//query1 += " JOIN fact_data as afd on log_alerts.aft_fact_data_id=afd.id ";
				//query1 += " JOIN alerts_alert_targets on alerts_alert_targets.alert_id=log_alerts.alert_id ";
				//query1 += " JOIN alert_targets on alert_targets.id=alerts_alert_targets.alert_target_id ";		
				//query1 += " JOIN users on log_alerts.user_id=users.id ";
				//query1 += " JOIN time_events on alerts.time_event_id=time_events.id ";
				query1 += " where LogAlert.emailSent=0 ";
				//query1 += " and alerts.email_alert=1 ";
				query1 += " and AlertTarget.type=1 ";
				query1 += " order by LogAlert.userId ";*/
				
				String query1 = " from LogAlert la";
				query1 += " inner join la.logAlertAlert a ";
				query1 += " inner join a.alertTarget at ";
				query1 += " inner join a.alertEntity e ";
				query1 += " inner join a.alertTimeEvent te";
				query1 += " inner join la.beforeFactData bfd ";
				query1 += " inner join la.afterFactData afd ";
				//query1 += " inner join AlertTarget at ";
				query1 += " where la.emailSent=0 ";
				//query1 += " and alerts.email_alert=1 ";
				query1 += " and at.type=1 ";
				query1 += " order by at.alertTargetId asc";
				
				
				

				//ResultSet rs1 = dbf.db_run_query(query1);
				//SqlRowSet rs1 = dbf.dbSpringRunQuery(query1);
				//List<Object[]> rs1 = dbf.dbHibernateRunQueryLeaveOpen(query1);
				DBObjectSession<List,Session> dbobjs  = dbf.dbHibernateRunQueryLeaveOpen(query1);
				List<Object[]> rs1 = dbobjs.a;
				
				
				//strLogAlertIds = "";
				//listAlerts = new ArrayList<String[]>();
				listAlerts = new ArrayList<HashMap<String,String>>();

				int nCurTargetId = 0;
				String strCurEmail = "";

				// listResultSet =
				// UtilityFunctions.convertResultSetToArrayList(rs1);

				// for (HashMap<String,String> hm : listResultSet){
				//while (rs1.next()) {
				Iterator it = rs1.iterator();
				Object[] objArray = null;
				LogAlert la = null;
				Alert a = null;
				AlertTarget at = null;
				Entity e = null;
				TimeEvent te = null;
				FactData bfd = null;
				FactData afd = null;
				/*
				 * OFP 5/24/2012 - This is the model for any type of member - group processing that requires a look ahead to 
				 * determine if the next group has reached, and you want to minimize the amount of duplicated code.
				 * 
				 * firstpass = true;
				 * loop {
				 * 	if (firstpass == false) {
				 * 		process node;
				 *   }
				 *   firstpass = false;
				 *   processGroup = false;
				 *   doneProcess = false;
				 *   if (no more nodes) {
				 *   	processGroup = true;
				 *      doneProcess = true;
				 *   }
				 *   else {
				 *   	initialize node;
				 *      if (node.next == different group) {
				 *  		processGroup = true;	
				 *      }
				 *    if (processGroup && bFirstpass = false)
				 *    	processgroup;
				 *    if (doneProcess)
				 *    	break;  	
				 *  
				 *   }
				 *   	
				 * 
				 * 
				 * }
				 */
			
				boolean bFirstPass = true;
				//listAlerts = new ArrayList<String[]>();
				listLogAlerts = new ArrayList<LogAlert>();
				while (true) {
						
					if (bFirstPass == false) {
						/*
						 * Processs node
						 */
					
						//String[] tmpArray = new String[9];
						HashMap<String,String> tmpHM = new HashMap<String,String>();
						
						
						tmpHM.put("DateTimeFired", la.getDateTimeFired().getTime().toString());
						tmpHM.put("Ticker", e.getTicker());
						tmpHM.put("TimeEventName", te.getName());
						tmpHM.put("Diff", la.getDiff()+"");
						tmpHM.put("BeforeValue", bfd.getValue()+"");
						tmpHM.put("BeforeDateCollected", bfd.getDateCollected().getTime().toString());
						tmpHM.put("AfterValue", afd.getValue()+"");
						tmpHM.put("AfterDateCollected", afd.getDateCollected().getTime().toString());
						tmpHM.put("CakeEditAlertURL", UtilityFunctions.getEmailCakeUrl()
								+ "alerts/edit?alert=" + a.getAlertId());
						
						
	
						/*tmpArray[0] = "<td align=\"center\">"
								+ la.getDateTimeFired().getTime().toString()
								+ "</td>";
						tmpArray[1] = "<td align=\"center\">"
								+ e.getTicker() + "</td>";
						tmpArray[2] = "<td align=\"center\">"
								+ te.getName() + "</td>";
						tmpArray[3] = "<td align=\"right\">"
								+ la.getDiff() + "</td>";
						tmpArray[4] = "<td align=\"right\">"
								+ bfd.getValue()
								+ "</td>";
						tmpArray[5] = "<td align=\"center\">"
								+ bfd.getDateCollected().getTime().toString()
								+ "</td>";
						tmpArray[6] = "<td align=\"right\">"
								+ afd.getValue()
								+ "</td>";
						tmpArray[7] = "<td align=\"center\">"
								+afd.getDateCollected().getTime().toString()
								+ "</td>";
						tmpArray[8] = "<td align=\"center\"><a href=\""
								+ (String) UtilityFunctions.getEmailCakeUrl()
								+ "alerts/edit?alert=" + a.getAlertId()
								+ "\">View/Edit Alert</a></td>";*/
						
						listAlerts.add(tmpHM);
						listLogAlerts.add(la);
					}
					
					
					boolean bProcessGroup = false;
					boolean bProcessFinish = false;
					
					if (it.hasNext() == false) {
						bProcessGroup = true;
						bProcessFinish = true;
					}
					else {
						
						/*
						 * Initialize Node
						 */
						objArray = (Object[])it.next();
						
						la = (LogAlert)objArray[0];
						a = (Alert)objArray[1];
						at = (AlertTarget)objArray[2];
						e = (Entity)objArray[3];
						te = (TimeEvent)objArray[4];
						bfd = (FactData)objArray[5];
						afd = (FactData)objArray[6];
						
						if (bFirstPass == true) {
							nCurTargetId = at.getAlertTargetId();
							strCurEmail = at.getAddress();
						}
							
						
						if (at.getAlertTargetId() != nCurTargetId)
							bProcessGroup = true;

					}
					
					if ((bProcessGroup == true) && (bFirstPass == false)) {
						
						/*
						 * Process Group
						 */
						generateBulkEmail(strCurEmail);
						
						//updateSentEmail(listLogAlerts);
						for (LogAlert la2 : listLogAlerts) {
							la2.setEmailSent(true);
						}
						
						listLogAlerts = new ArrayList<LogAlert>();
						listAlerts = new ArrayList<HashMap<String,String>>();
						
						nCurTargetId = at.getAlertTargetId();
						strCurEmail = at.getAddress();
						
					}
					
					if (bProcessFinish)
						break;
							
					if (bFirstPass)
						bFirstPass = false;
					
						
				}
				
				//dbobjs.b.getTransaction().commit();
				
				/*for (Object[] objArray : rs1) {
					
					LogAlert la = (LogAlert)objArray[0];
					Alert a = (Alert)objArray[1];
					AlertTarget at = (AlertTarget)objArray[2];
					Entity e = (Entity)objArray[3];
					TimeEvent te = (TimeEvent)objArray[4];
					FactData bfd = (FactData)objArray[5];
					FactData afd = (FactData)objArray[6];
					
					
					if (nTargetId!= 0) {
						//if (nTargetId != rs1.getInt("alert_targets.id")) {
						
						if (nTargetId != at.getAlertTargetId()) { 
							nCount++;
							
					
			
						generateBulkEmail(strCurEmail);
							//strLogAlertIds = strLogAlertIds.substring(0,
							//		strLogAlertIds.length() - 1);
							
							updateSentEmail(listLogAlerts);
							nTargetId = at.getAlertTargetId();
							strCurEmail = at.getAddress();
							listAlerts = new ArrayList<String[]>();
							listLogAlerts = new ArrayList<LogAlert>();
						}

					}

					else {
						nTargetId = at.getAlertTargetId();
						strCurEmail = at.getAddress();
						nCount++;
					}

					String[] tmpArray = new String[9];

					tmpArray[0] = "<td align=\"center\">"
							+ la.getDateTimeFired().getTime().toString()
							+ "</td>";
					tmpArray[1] = "<td align=\"center\">"
							+ e.getTicker() + "</td>";
					tmpArray[2] = "<td align=\"center\">"
							+ te.getName() + "</td>";
					tmpArray[3] = "<td align=\"right\">"
							+ la.getDiff() + "</td>";
					tmpArray[4] = "<td align=\"right\">"
							+ bfd.getValue()
							+ "</td>";
					tmpArray[5] = "<td align=\"center\">"
							+ bfd.getDateCollected().getTime().toString()
							+ "</td>";
					tmpArray[6] = "<td align=\"right\">"
							+ afd.getValue()
							+ "</td>";
					tmpArray[7] = "<td align=\"center\">"
							+afd.getDateCollected().getTime().toString()
							+ "</td>";
					tmpArray[8] = "<td align=\"center\"><a href=\""
							+ (String) UtilityFunctions.getEmailCakeUrl()
							+ "alerts/edit?alert=" + a.getAlertId()
							+ "\">View/Edit Alert</a></td>";

					listAlerts.add(tmpArray);
					listLogAlerts.add(la);
					//strLogAlertIds += la.getLogAlertId() + ",";

				}

				if (listAlerts.size() != 0) {
					generateBulkEmail(strCurEmail);
					//strLogAlertIds = strLogAlertIds.substring(0,
							//strLogAlertIds.length() - 1);
					updateSentEmail(listLogAlerts);
				}

				calEnd = Calendar.getInstance();
				
				LogNotification ln = new LogNotification(); 
				ln.setCount(nCount);
				ln.setProcessBegin(calBegin);
				ln.setProcessEnd(calEnd);
				
				dbf.dbHibernateSaveQuery(ln);
				

				/*String insert = "insert into log_notifications (count,process_begin,process_end) values (";
				insert += nCount + ",";
				insert += "'" + formatter.format(calBegin.getTime()) + "',";
				insert += "'" + formatter.format(calEnd.getTime()) + "')";*/

				/*try {
					//dbf.db_update_query(insert);
					dbf.dbSpringUpdateQuery(insert);
					
				} catch (DataAccessException sqle) {
					UtilityFunctions.stdoutwriter.writeln(
							"Problem inserting into log_notifications table.",
							Logs.ERROR, "N2.0");
					UtilityFunctions.stdoutwriter.writeln(sqle);
				}*/

			} catch (DataAccessException sqle) {
				UtilityFunctions.stdoutwriter.writeln(
						"Problem with sql statement.", Logs.ERROR, "N3.0");
				UtilityFunctions.stdoutwriter.writeln(sqle);

			}

			try {
				Notification.sleep(nInterval);
			} catch (InterruptedException ie) {
				UtilityFunctions.stdoutwriter.writeln(
						"Thread interrupted. Terminating.", Logs.STATUS1,
						"N1.0");
				return;
			}
		}
	}

	public void generateBulkEmail(/*int nUserId, */String strEmail) {

		
		String strMessage = "";
		/*String strMessage = "<html><body><table>";

		strMessage += "<th>Alert Timestamp</th>";
		strMessage += "<th>Ticker</th>";
		strMessage += "<th>Observation Period</th>";
		strMessage += "<th>% Change</th>";
		strMessage += "<th>Begin Value</td>";
		strMessage += "<th>Begin Timestamp</td>";
		strMessage += "<th>End Value</th>";
		strMessage += "<th>End Timestamp</th>";*/

		//String strLogAlertIds = "";
		
		strMessage = UtilityFunctions.generateVelocity(listAlerts,"alertList","templates/bulkemail.vm");
		//strMessage = UtilityFunctions.generateVelocity2();

		//for (HashMap<String,String> item : listAlerts) {
		//	strMessage += "<tr>";

			//for (String s : item) {
		//		strMessage += UtilityFunctions.generateVelocity(item,"alertList","templates/bulkemail.vm");
			//}
			/*
			 * for (int i=0;i<item.length-1;i++) { strMessage += "<td>" +
			 * item[i] + "</td>";
			 * 
			 * }
			 */

			// strMessage += "<a http=\"" +
			// (String)DataLoad.props.get("cakebaseurl") + "alerts/edit?alert="
			// + nAlertId;

		//	strMessage += "</tr>";

		//}

		//strMessage += "</table></body></html>";
		String strSubject = UtilityFunctions.getEmailSubjectText()
				+ " Bulk Email Notification";

		
		if (Broker.getDebugMode() == false) {
			// UtilityFunctions.mail(strEmail,strMessage,strSubject,(String)DataLoad.props.get("fromaddy"));
			UtilityFunctions.htmlMail(strEmail, strMessage, strSubject);

		}

	}

	//public void updateSentEmail(String strAlertIds) throws DataAccessException {
	/*public void updateSentEmail(ArrayList<LogAlert> inputList) {
		for (LogAlert la : inputList) {
			la.setEmailSent(true);
		}
		
	

		//String query1 = "update log_alerts set email_sent=1 where id in ("
				+ strAlertIds + ")";
		//dbf.db_update_query(query1);
		//dbf.dbSpringUpdateQuery(query1);

	}*/

}
