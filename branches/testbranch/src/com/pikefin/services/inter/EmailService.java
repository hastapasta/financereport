package com.pikefin.services.inter;

import java.util.HashMap;
import java.util.List;

import com.pikefin.exceptions.GenericException;

public interface EmailService {

	public void generateBulkEmail(List<HashMap<String,String>> listAlerts ,String emailAddress) throws GenericException;
	public void sendHtmlEmail(String emailAddress, String emailMessage, String emailSubject) throws GenericException;
}
