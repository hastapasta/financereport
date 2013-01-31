package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.RepeatTypeEnum;
import com.pikefin.businessobjects.Schedule;
import com.pikefin.exceptions.GenericException;

public interface ScheduleDao {
	public Schedule saveScheduleInfo(Schedule scheduleEntity) 
	    throws GenericException;
	public Schedule updateScheduleInfo(Schedule scheduleEntity) 
	    throws GenericException;
	public List<Schedule> updateSchedulesBatch(List<Schedule> schedules) 
	    throws GenericException;
	public Boolean deleteScheduleInfo(Schedule scheduleEntity ) 
	    throws GenericException;
	public Boolean deleteScheduleInfoById(Integer scheduleId ) 
	    throws GenericException;
	public Schedule loadScheduleInfo(Integer scheduleId) 
	    throws GenericException;
	public List<Schedule> loadAllSchedules() 
	    throws GenericException;
	public List<Schedule> loadAllSchedulesForBrokerExecutor() 
	    throws GenericException;
}
