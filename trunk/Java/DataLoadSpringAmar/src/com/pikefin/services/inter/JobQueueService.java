package com.pikefin.services.inter;

import com.pikefin.exceptions.GenericException;

public interface JobQueueService {
	public void deleteAllJobQueues() throws GenericException;

}
