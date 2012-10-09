package com.pikefin.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.TimeEvent;
import com.pikefin.dao.inter.TimeEventDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.TimeEventService;
@Service
public class TimeEventserviceImpl implements TimeEventService{
	Logger log=Logger.getLogger(TimeEventserviceImpl.class);
	@Autowired
	private TimeEventDao timeEventDao;
	public static final Integer INTEGER_INITIAL=0;
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void  updateTimeEventsForNextCall(List<TimeEvent> timeEvents) throws GenericException{
		Calendar currentTime=Calendar.getInstance();
		Calendar calNext = Calendar.getInstance();;
		Calendar calLast = Calendar.getInstance();;
		Calendar calNextDelay;
		List<TimeEvent> timeEventsForUpdate=new ArrayList<TimeEvent>();
		
		for (TimeEvent timeEvent: timeEvents){
			if(INTEGER_INITIAL.equals(timeEvent.getYears()) && INTEGER_INITIAL.equals(timeEvent.getMonths()) && INTEGER_INITIAL.equals(timeEvent.getDays()) && INTEGER_INITIAL.equals(timeEvent.getHours())){
				log.error("Problem with time event "+timeEvent.getTimeEventId());
				log.error("All time increment values are set to zero, skipping");
				continue;
			}
			
			if(timeEvent.getNextDatetime()!=null){
				calLast.setTime(timeEvent.getNextDatetime());
				calNext.setTime(timeEvent.getNextDatetime());
			}else{
				calLast.setTime(timeEvent.getStartDatetime());
				calNext.setTime(timeEvent.getStartDatetime());
			}
			
			calNextDelay=(Calendar)calNext.clone();
			calNextDelay.add(Calendar.MINUTE, timeEvent.getDelay());
			if (!calNextDelay.after(currentTime) || (timeEvent.getNextDatetime() == null)) {
				
				//Keep adding cycles until next is after current
				while (!calNext.after(currentTime)) {
					calNext.add(Calendar.YEAR,timeEvent.getYears());
					calNext.add(Calendar.MONTH, timeEvent.getMonths());
					calNext.add(Calendar.DAY_OF_YEAR,timeEvent.getDays());
					calNext.add(Calendar.HOUR,timeEvent.getHours());
				}
				timeEvent.setLastDatetime(calLast.getTime()); 
				timeEvent.setNextDatetime(calNext.getTime());
				}
			timeEventsForUpdate.add(timeEvent);
		}
		
		timeEventDao.updateBatchTimeEvents(timeEventsForUpdate);
	}
	
	@Override
	public List<TimeEvent>  loadAllTimeEvents() throws GenericException{
		log.debug("**TimeEventserviceImpl.loadAllTimeEvents method start loading all timeEvents");
			return timeEventDao.loadAllTimeEvents();
	}

	@Override
	public void updateTimeEventsForBroker() throws GenericException {
		updateTimeEventsForNextCall(loadAllTimeEvents());
	}

	
	
}
