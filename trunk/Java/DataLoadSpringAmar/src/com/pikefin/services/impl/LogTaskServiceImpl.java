package com.pikefin.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikefin.businessobjects.LogTask;
import com.pikefin.dao.inter.LogTaskDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.LogTaskService;

@Service
public class LogTaskServiceImpl implements LogTaskService{
	@Autowired
	private LogTaskDao logTaskDao;
	@Override
	public LogTask saveLogTaskInfo(LogTask logTaskEntity)
			throws GenericException {
		return logTaskDao.saveLogTaskInfo(logTaskEntity);
	}

	
}
