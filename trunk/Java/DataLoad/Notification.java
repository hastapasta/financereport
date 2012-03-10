import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import pikefin.log4jWrapper.Logs;

public class Notification extends Thread {

	UtilityFunctions uf;
	DBFunctions dbf;
	ArrayList<String[]> listAlerts;
	ArrayList<HashMap<String, String>> listResultSet;
	DateFormat formatter;

	public Notification(UtilityFunctions tmpUF, DBFunctions dbfparam) {

		this.uf = tmpUF;
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
	
		String strLogAlertIds;

		while (!bDone) {

			try {
				dbf.cycleConnection();
				calBegin = Calendar.getInstance();

				//String query1 = "select log_alerts.id,alerts.id,time_events.name,log_alerts.date_time_fired,alerts.email_alert,log_alerts.user_id,entities.ticker,log_alerts.diff,bfd.value,afd.value,bfd.date_collected,afd.date_collected, users.email ";
				String query1 = "select log_alerts.id,alerts.id,time_events.name,log_alerts.date_time_fired,log_alerts.user_id,entities.ticker, ";
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
				query1 += " order by log_alerts.user_id ";

				ResultSet rs1 = dbf.db_run_query(query1);
				
				strLogAlertIds = "";
				listAlerts = new ArrayList<String[]>();

				int nTargetId = 0;
				String strCurEmail = "";

				// listResultSet =
				// UtilityFunctions.convertResultSetToArrayList(rs1);

				// for (HashMap<String,String> hm : listResultSet){
				while (rs1.next()) {
					if (nTargetId!= 0) {
						if (nTargetId != rs1.getInt("alert_targets.id")) {
							nCount++;
							generateBulkEmail(/*nTargetId,*/ strCurEmail);
							strLogAlertIds = strLogAlertIds.substring(0,
									strLogAlertIds.length() - 1);
							updateSentEmail(strLogAlertIds);
							nTargetId = rs1.getInt("alert_targets.id");
							strCurEmail = rs1.getString("alert_targets.address");
							listAlerts = new ArrayList<String[]>();
							strLogAlertIds = "";
						}

					}

					else {
						nTargetId = rs1.getInt("alert_targets.id");
						strCurEmail = rs1.getString("alert_targets.address");
						nCount++;
					}

					String[] tmpArray = new String[9];

					tmpArray[0] = "<td align=\"center\">"
							+ rs1.getTimestamp("date_time_fired").toString()
							+ "</td>";
					tmpArray[1] = "<td align=\"center\">"
							+ rs1.getString("ticker") + "</td>";
					tmpArray[2] = "<td align=\"center\">"
							+ rs1.getString("name") + "</td>";
					tmpArray[3] = "<td align=\"right\">"
							+ rs1.getBigDecimal("diff").toString() + "</td>";
					tmpArray[4] = "<td align=\"right\">"
							+ rs1.getBigDecimal("bfd.value").toString()
							+ "</td>";
					tmpArray[5] = "<td align=\"center\">"
							+ rs1.getTimestamp("bfd.date_collected").toString()
							+ "</td>";
					tmpArray[6] = "<td align=\"right\">"
							+ rs1.getBigDecimal("afd.value").toString()
							+ "</td>";
					tmpArray[7] = "<td align=\"center\">"
							+ rs1.getTimestamp("afd.date_collected").toString()
							+ "</td>";
					tmpArray[8] = "<td align=\"center\"><a href=\""
							+ (String) DataLoad.props.get("cakebaseurl")
							+ "alerts/edit?alert=" + rs1.getInt("alerts.id")
							+ "\">View/Edit Alert</a></td>";

					listAlerts.add(tmpArray);
					strLogAlertIds += rs1.getInt("log_alerts.id") + ",";

				}

				if (listAlerts.size() != 0) {
					generateBulkEmail(/*nTargetId, */strCurEmail);
					strLogAlertIds = strLogAlertIds.substring(0,
							strLogAlertIds.length() - 1);
					updateSentEmail(strLogAlertIds);
				}

				calEnd = Calendar.getInstance();

				String insert = "insert into log_notifications (count,process_begin,process_end) values (";
				insert += nCount + ",";
				insert += "'" + formatter.format(calBegin.getTime()) + "',";
				insert += "'" + formatter.format(calEnd.getTime()) + "')";

				try {
					dbf.db_update_query(insert);
				} catch (SQLException sqle) {
					UtilityFunctions.stdoutwriter.writeln(
							"Problem inserting into log_notifications table.",
							Logs.ERROR, "N2.0");
					UtilityFunctions.stdoutwriter.writeln(sqle);
				}

			} catch (SQLException sqle) {
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

		String strMessage = "<html><body><table>";

		strMessage += "<th>Alert Timestamp</th>";
		strMessage += "<th>Ticker</th>";
		strMessage += "<th>Observation Period</th>";
		strMessage += "<th>% Change</th>";
		strMessage += "<th>Begin Value</td>";
		strMessage += "<th>Begin Timestamp</td>";
		strMessage += "<th>End Value</th>";
		strMessage += "<th>End Timestamp</th>";

		String strLogAlertIds = "";

		for (String[] item : listAlerts) {
			strMessage += "<tr>";

			for (String s : item) {
				strMessage += s;
			}
			/*
			 * for (int i=0;i<item.length-1;i++) { strMessage += "<td>" +
			 * item[i] + "</td>";
			 * 
			 * }
			 */

			// strMessage += "<a http=\"" +
			// (String)DataLoad.props.get("cakebaseurl") + "alerts/edit?alert="
			// + nAlertId;

			strMessage += "</tr>";

		}

		strMessage += "</table></body></html>";
		String strSubject = DataLoad.props.get("subjecttext")
				+ " Bulk Email Notification";

		if (DataLoad.bDebugMode == false) {
			// UtilityFunctions.mail(strEmail,strMessage,strSubject,(String)DataLoad.props.get("fromaddy"));
			UtilityFunctions.htmlMail(strEmail, strMessage, strSubject);

		}

	}

	public void updateSentEmail(String strAlertIds) throws SQLException {

		String query1 = "update log_alerts set email_sent=1 where id in ("
				+ strAlertIds + ")";
		dbf.db_update_query(query1);

	}

}
