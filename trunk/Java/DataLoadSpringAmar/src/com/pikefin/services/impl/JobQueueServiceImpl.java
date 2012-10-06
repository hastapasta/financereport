package com.pikefin.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.dao.inter.JobQueueDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.JobQueueService;

public class JobQueueServiceImpl implements JobQueueService {
	@Autowired
	private JobQueueDao jobQueueDao;
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteAllJobQueues() throws GenericException {
		jobQueueDao.deleteAllJobQueues();
	}

}
