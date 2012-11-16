package com.pikefin.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.Job;
import com.pikefin.dao.inter.JobDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.JobService;
@Service
public class JobServiceImpl implements JobService{
	@Autowired
	private JobDao jobDao;
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Job getJobByDataSet(String dataSet) throws GenericException {
		
		return jobDao.getJobByDataSet(dataSet);
	}

	
	
}
