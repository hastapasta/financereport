package com.pikefin.services.inter;

import com.pikefin.businessobjects.JobQueue;
import com.pikefin.exceptions.GenericException;

public interface JobQueueService {
	public JobQueue saveJobQueueInfo(JobQueue jobQueueEntity) throws GenericException;
	public void deleteAllJobQueues() throws GenericException;

}
