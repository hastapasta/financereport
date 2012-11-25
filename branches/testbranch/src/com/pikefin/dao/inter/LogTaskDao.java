package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.LogTask;
import com.pikefin.exceptions.GenericException;

public interface LogTaskDao {
	public LogTask saveLogTaskInfo(LogTask logTaskEntity) throws GenericException;
	public LogTask updateLogTaskInfo(LogTask logTaskEntity) throws GenericException;
	public Boolean deleteLogTaskInfo(LogTask logTaskEntity ) throws GenericException;
	public Boolean deleteLogTaskInfoById(Integer logTaskId ) throws GenericException;
	public LogTask loadLogTaskInfo(Integer logTaskId) throws GenericException;
	public List<LogTask> loadAllLogTasks() throws GenericException;
}
