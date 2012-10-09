package com.pikefin.services.impl;

import org.springframework.stereotype.Service;

import com.pikefin.businessobjects.Job;
import com.pikefin.dao.inter.JobDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.JobService;
@Service
public class JobServiceImpl implements JobService{

	private JobDao jobDao;
	@Override
	public Job getJobByDataSet(String dataSet) throws GenericException {
		
		return jobDao.getJobByDataSet(dataSet);
	}

	
	
}
