package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Task;
import com.pikefin.exceptions.GenericException;

public interface TaskDao {

	public Task saveTaskInfo(Task taskEntity) throws GenericException;
	public Task updateTaskInfo(Task taskEntity) throws GenericException;
	public Boolean deleteTaskInfo(Task taskEntity ) throws GenericException;
	public Boolean deleteTaskInfoById(Integer alertId ) throws GenericException;
	public Task loadTaskInfo(Integer alertId) throws GenericException;
	public List<Task> loadAllTasks() throws GenericException;
}
