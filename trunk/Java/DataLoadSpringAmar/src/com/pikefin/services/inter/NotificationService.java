package com.pikefin.services.inter;


import java.util.HashMap;
import java.util.List;

import com.pikefin.exceptions.GenericException;

public interface NotificationService {
	public  String generateVelocity(List<HashMap<String, String>> hm, String strListName, String emailTemplate) throws GenericException;

}
