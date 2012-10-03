package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.LogNotification;
import com.pikefin.exceptions.GenericException;

public interface LogNotificationDao {
	public LogNotification saveLogNotificationInfo(LogNotification logNotificationEntity) throws GenericException;
	public LogNotification updateLogNotificationInfo(LogNotification logNotificationEntity) throws GenericException;
	public Boolean deleteLogNotificationInfo(LogNotification logNotificationEntity ) throws GenericException;
	public Boolean deleteLogNotificationInfoById(Integer logNotificationId ) throws GenericException;
	public LogNotification loadLogNotificationInfo(Integer logNotificationId) throws GenericException;
	public List<LogNotification> loadAllLogNotifications() throws GenericException;
}
