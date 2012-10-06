package com.pikefin.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import pikefin.log4jWrapper.Logs;
import com.pikefin.ApplicationSetting;
import com.pikefin.Constants;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.BrokerExecuter;
import com.pikefin.services.inter.EmailService;
import com.pikefin.services.inter.NotificationService;

public class EmailServiceImpl implements EmailService {
	Logger log=Logger.getLogger(EmailServiceImpl.class);
	private NotificationService notificatioNService;
	@Override
	public void generateBulkEmail(List<HashMap<String,String>> listAlerts, String emailAddress) throws GenericException {

		String emailMessage = "";
			
		emailMessage = notificatioNService.generateVelocity(listAlerts,"alertList",ApplicationSetting.getInstance().getBulkEmailTemplate());
		
		String emailSubject = ApplicationSetting.getInstance().getEmailSubjectText()
				+ " Bulk Email Notification";

		
		if (ApplicationSetting.getInstance().isDebugMode() == false) {
			this.sendHtmlEmail(emailAddress, emailMessage, emailSubject);

		}

	}
	@Override
	public void sendHtmlEmail(String emailAddress, String emailMessage,
			String emailSubject) throws GenericException {
		
		HtmlEmail email = new HtmlEmail();

		try {
			email.setHostName(ApplicationSetting.getInstance().getEmailHost());
			email.setAuthentication(ApplicationSetting.getInstance().getEmailUser(), ApplicationSetting.getInstance().getEmailPassword());
			email.setSmtpPort(ApplicationSetting.getInstance().getEmailPort());
			email.setFrom(ApplicationSetting.getInstance().getEmailFromAddress());
			email.addTo(emailAddress);
			email.setSubject(emailAddress);
	
			email.setTextMsg("Pikefin Alert");
			email.setHtmlMsg(emailMessage);
			email.setTLS(true);
	
			email.setDebug(false);
			
	
			email.send();
		}
		catch (EmailException ee) {
			log.error("Problem sending html email."+ee);
	  				
		}

				
	}

}
