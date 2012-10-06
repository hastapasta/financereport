package com.pikefin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.LogAlert;
import com.pikefin.dao.inter.LogAlertDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.LogAlertService;

public class LogAlertServiceImpl implements LogAlertService{
@Autowired
private LogAlertDao logAlertDao;
	@Override
	public List<Object> loadAllLogAlertsForNotification()
			throws GenericException {
		return logAlertDao.loadAllLogAlertsForNotification();
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<LogAlert> updateLogAlertInBulk(
			List<LogAlert> logAlertEntitiesList) throws GenericException {
		return logAlertDao.updateLogAlertInBulk(logAlertEntitiesList);
	}

}
