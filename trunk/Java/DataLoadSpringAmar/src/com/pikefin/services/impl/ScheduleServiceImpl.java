package com.pikefin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikefin.businessobjects.Schedule;
import com.pikefin.dao.inter.ScheduleDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.ScheduleService;
@Service
public class ScheduleServiceImpl implements ScheduleService{

	@Autowired
	private ScheduleDao scheduleDao;
	@Override
	public List<Schedule> loadAllSchedulesForBrokerExecuter()
			throws GenericException {
			return scheduleDao.loadAllSchedulesForBrokerExecuter();
	}
	@Override
	public List<Schedule> updateSchedulesBatch(List<Schedule> schedules)
			throws GenericException {
		return scheduleDao.updateSchedulesBatch(schedules);
	}

}
