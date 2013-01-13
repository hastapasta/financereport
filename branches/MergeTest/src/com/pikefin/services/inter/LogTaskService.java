package com.pikefin.services.inter;

import com.pikefin.businessobjects.LogTask;
import com.pikefin.exceptions.GenericException;

public interface LogTaskService {
	public LogTask saveLogTaskInfo(LogTask logTaskEntity) throws GenericException;

}
