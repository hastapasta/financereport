package com.pikefin.services.inter;

import java.util.List;

import com.pikefin.businessobjects.LogAlert;
import com.pikefin.exceptions.GenericException;

public interface LogAlertService {
	public List<LogAlert> loadAllLogAlertsForNotification() throws GenericException;

}
