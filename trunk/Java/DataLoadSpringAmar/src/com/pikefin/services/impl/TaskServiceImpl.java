package com.pikefin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.Exclude;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.ExcludedService;
import com.pikefin.services.inter.TaskService;
@Service
public class TaskServiceImpl implements TaskService {
	@Autowired 
	private ExcludedService  excludeService;
	@Override
	//@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean isTaskExcluded(Integer taskId) throws GenericException {
		List<Exclude> excludeList= excludeService.loadAllExcludesByTaskId(taskId);
		if(excludeList==null || excludeList.size()==0)
			return false;
		return excludeService.isTaskExcluded(excludeList);
	}

}
