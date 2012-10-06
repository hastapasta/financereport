package com.pikefin.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.pikefin.ApplicationSetting;
import com.pikefin.Constants;
import com.pikefin.businessobjects.Alert;
import com.pikefin.businessobjects.AlertTarget;
import com.pikefin.businessobjects.Entity;
import com.pikefin.businessobjects.FactData;
import com.pikefin.businessobjects.LogAlert;
import com.pikefin.businessobjects.TimeEvent;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.EmailService;
import com.pikefin.services.inter.LogAlertService;

public class NotificationExecuter extends Thread {
	Logger log=Logger.getLogger(NotificationExecuter.class);
	@Autowired
	private LogAlertService logAlertService;
	private List<HashMap<String,String>> listAlerts;
	@Autowired
	private EmailService emailService;
	
public void run() {
	int nInterval = 15000;
	boolean bDone = false;
	Calendar calBegin;
	List<LogAlert> listLogAlerts = new ArrayList<LogAlert>();
	while (!bDone) {
		try {
			calBegin = Calendar.getInstance();
			List<Object> rs1=logAlertService.loadAllLogAlertsForNotification();
			listAlerts = new ArrayList<HashMap<String,String>>();
			int nCurTargetId = 0;
			String strCurEmail = "";
			Iterator it = rs1.iterator();
			Object[] objArray = null;
			LogAlert la = null;
			Alert a = null;
			AlertTarget at = null;
			Entity e = null;
			TimeEvent te = null;
			FactData bfd = null;
			FactData afd = null;
			boolean bFirstPass = true;
			listLogAlerts = new ArrayList<LogAlert>();
			while (true) {
					
				if (bFirstPass == false) {
					HashMap<String,String> tmpHM = new HashMap<String,String>();	
					tmpHM.put(Constants.NotificationData.NOTIFICATION_DATE_TIME_FIRED, String.valueOf(la.getDateTimeFired()));
					tmpHM.put(Constants.NotificationData.NOTIFICATION_TICKER, e.getTicker());
					tmpHM.put(Constants.NotificationData.NOTIFICATION_TIME_EVENT_NAME, te.getName());
					tmpHM.put(Constants.NotificationData.NOTIFICATION_DIFF, la.getDiff()+"");
					tmpHM.put(Constants.NotificationData.NOTIFICATION_BEFORE_VALUE, bfd.getValue()+"");
					tmpHM.put(Constants.NotificationData.NOTIFICATION_BEFORE_DATE_COLLECTED, bfd.getDateCollected().toString());
					tmpHM.put(Constants.NotificationData.NOTIFICATION_AFTER_VALUE, afd.getValue()+"");
					tmpHM.put(Constants.NotificationData.NOTIFICATION_AFTER_DATE_COLLECTED, afd.getDateCollected().toString());
					tmpHM.put(Constants.NotificationData.NOTIFICATION_CAKE_EDIT_ALERT_URL, ApplicationSetting.getInstance().getEmailCakeUrl()
							+ Constants.NotificationData.NOTIFICATION_CAKE_EDIT_ALERT_SUB_URL + a.getAlertId());
					listAlerts.add(tmpHM);
					listLogAlerts.add(la);
				}
				boolean bProcessGroup = false;
				boolean bProcessFinish = false;
				if (it.hasNext() == false) {
					bProcessGroup = true;
					bProcessFinish = true;
				}else {
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
			/* Process Group */
					emailService.generateBulkEmail(listAlerts, strCurEmail);
				for (LogAlert la2 : listLogAlerts) {
						la2.setEmailSent(true);
					}
					 //updating list to the database in batch mode
					listLogAlerts=logAlertService.updateLogAlertInBulk(listLogAlerts);
					listLogAlerts=new ArrayList<LogAlert>();
					listAlerts = new ArrayList<HashMap<String,String>>();
					nCurTargetId = at.getAlertTargetId();
					strCurEmail = at.getAddress();
				}
				if (bProcessFinish)
					break;
				if (bFirstPass)
					bFirstPass = false;
			}
	} catch (GenericException sqle) {
			log.error("Problem with sql statement. "+sqle.getErrorCode()+" "+sqle.getErrorMessage()+" "+sqle.getErrorDescription());
		}
	try {
			NotificationExecuter.sleep(nInterval);
		} catch (InterruptedException ie) {
			log.error("NotificationExecuter Thread interrupted. Terminating.");
			return;
		}
	}
}

}
