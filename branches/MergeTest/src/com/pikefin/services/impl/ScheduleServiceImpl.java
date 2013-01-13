package com.pikefin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.Schedule;
import com.pikefin.dao.inter.ScheduleDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.ScheduleService;
@Service
public class ScheduleServiceImpl implements ScheduleService{

	@Autowired
	private ScheduleDao scheduleDao;
	@Override
//	@Transactional(propagation=Propagation.REQUIRED)
	public List<Schedule> loadAllSchedulesForBrokerExecuter()
			throws GenericException {
			return scheduleDao.loadAllSchedulesForBrokerExecuter();
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Schedule> updateSchedulesBatch(List<Schedule> schedules)
			throws GenericException {
		return scheduleDao.updateSchedulesBatch(schedules);
	}

}
