package com.pikefin.services.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import pikefin.log4jWrapper.Logs;
import com.pikefin.ApplicationSetting;
import com.pikefin.businessobjects.Job;
import com.pikefin.dao.inter.JobDao;
import com.pikefin.exceptions.CustomEmptyStringException;
import com.pikefin.exceptions.GenericException;
import com.pikefin.exceptions.SkipLoadException;
import com.pikefin.services.inter.JobService;

public class JobServiceImpl implements JobService{

	private JobDao jobDao;
	@Override
	public Job getJobByDataSet(String dataSet) throws GenericException {
		
		return jobDao.getJobByDataSet(dataSet);
	}

	
	
}
