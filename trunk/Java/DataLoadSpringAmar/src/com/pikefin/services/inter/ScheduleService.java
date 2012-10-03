package com.pikefin.services.inter;

import java.util.List;

import com.pikefin.businessobjects.Schedule;
import com.pikefin.exceptions.GenericException;

public interface ScheduleService {
	public List<Schedule> loadAllSchedulesForBrokerExecuter() throws GenericException;
	public List<Schedule> updateSchedulesBatch(List<Schedule> schedules) throws GenericException;

}
