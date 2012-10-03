package com.pikefin.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.pikefin.businessobjects.Alert;
import com.pikefin.businessobjects.AlertTarget;
import com.pikefin.businessobjects.Entity;
import com.pikefin.businessobjects.FactData;
import com.pikefin.businessobjects.LogAlert;
import com.pikefin.businessobjects.TimeEvent;
import com.pikefin.services.inter.LogAlertService;
import pikefin.log4jWrapper.Logs;

public class NotificationExecuter extends Thread {
	@Autowired
	private LogAlertService logAlertService;
	private ArrayList<HashMap<String,String>> listAlerts;

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
			calBegin = Calendar.getInstance();
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
			
			
			

		
			DBObjectSession<List,Session> dbobjs  = dbf.dbHibernateRunQueryLeaveOpen(query1);
			List<Object[]> rs1 = dbobjs.a;
			
			List<LogAlert> logAlertListNew=logAlertService.loadAllLogAlertsForNotification();
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

}
