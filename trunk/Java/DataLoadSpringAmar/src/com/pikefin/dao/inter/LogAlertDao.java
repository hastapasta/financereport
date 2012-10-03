package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.LogAlert;
import com.pikefin.exceptions.GenericException;

public interface LogAlertDao {
	public LogAlert saveLogAlertInfo(LogAlert logAlertEntity) throws GenericException;
	public LogAlert updateLogAlertInfo(LogAlert logAlertEntity) throws GenericException;
	public Boolean deleteLogAlertInfo(LogAlert logAlertEntity ) throws GenericException;
	public Boolean deleteLogAlertInfoById(Integer logAlertId ) throws GenericException;
	public LogAlert loadLogAlertInfo(Integer logAlertId) throws GenericException;
	public List<LogAlert> loadAllLogAlerts() throws GenericException;
	public List<LogAlert> loadAllLogAlertsForNotification() throws GenericException;
}
