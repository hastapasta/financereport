package com.pikefin.services.inter;


import java.util.HashMap;
import java.util.List;

import com.pikefin.exceptions.GenericException;

public interface NotificationService {
	public  String generateVelocity(List<HashMap<String, String>> hm, String strListName, String emailTemplate) throws GenericException;
	public String tweet(String strTweet,String userName, String password, String authentication1, String authentication2, String authentication3, String authentication4) throws GenericException;

}
