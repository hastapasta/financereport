package com.pikefin.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.JobQueue;
import com.pikefin.dao.inter.JobQueueDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.JobQueueService;
@Service
public class JobQueueServiceImpl implements JobQueueService {
	@Autowired
	private JobQueueDao jobQueueDao;
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteAllJobQueues() throws GenericException {
		jobQueueDao.deleteAllJobQueues();
	}
	@Override
	public JobQueue saveJobQueueInfo(JobQueue jobQueueEntity)
			throws GenericException {
		return jobQueueDao.saveJobQueueInfo(jobQueueEntity);
	}

}
