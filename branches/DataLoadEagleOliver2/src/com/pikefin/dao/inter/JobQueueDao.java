package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.JobQueue;
import com.pikefin.exceptions.GenericException;

public interface JobQueueDao {
	public JobQueue saveJobQueueInfo(JobQueue jobQueueEntity) throws GenericException;
	public JobQueue updateJobQueueInfo(JobQueue jobQueueEntity) throws GenericException;
	public Boolean deleteJobQueueInfo(JobQueue jobQueueEntity ) throws GenericException;
	public Boolean deleteJobQueueInfoById(Integer jobQueueId ) throws GenericException;
	public void deleteAllJobQueues() throws GenericException;
	public JobQueue loadJobQueueInfo(Integer jobQueueId) throws GenericException;
	public List<JobQueue> loadAllJobQueues() throws GenericException;
}
