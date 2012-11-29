package com.pikefin.services.inter;

import com.pikefin.exceptions.GenericException;

public interface TaskService {

	public boolean isTaskExcluded(Integer taskId) throws GenericException;
}
