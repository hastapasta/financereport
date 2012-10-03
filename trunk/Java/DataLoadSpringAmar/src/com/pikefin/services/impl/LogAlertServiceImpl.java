package com.pikefin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pikefin.businessobjects.LogAlert;
import com.pikefin.dao.inter.LogAlertDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.LogAlertService;

public class LogAlertServiceImpl implements LogAlertService{
@Autowired
private LogAlertDao logAlertDao;
	@Override
	public List<LogAlert> loadAllLogAlertsForNotification()
			throws GenericException {
		return logAlertDao.loadAllLogAlertsForNotification();
	}

}
