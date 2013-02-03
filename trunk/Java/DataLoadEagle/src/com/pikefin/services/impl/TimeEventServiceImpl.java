package com.pikefin.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pikefin.log4jWrapper.Logs;

import com.pikefin.ApplicationSetting;
import com.pikefin.businessobjects.TimeEvent;
import com.pikefin.dao.inter.TimeEventDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.TimeEventService;
@Service
public class TimeEventServiceImpl implements TimeEventService{
  @Autowired
  private TimeEventDao timeEventDao;
  public static final Integer INTEGER_INITIAL=0;
  @Override
  @Transactional(propagation=Propagation.REQUIRES_NEW)
  public void  updateTimeEventsForNextCall(List<TimeEvent> timeEvents)
      throws GenericException{
    Calendar currentTime=Calendar.getInstance();
    Calendar calNext = Calendar.getInstance();;
    Calendar calLast = Calendar.getInstance();;
    Calendar calNextDelay;
    List<TimeEvent> timeEventsForUpdate=new ArrayList<TimeEvent>();

    for (TimeEvent timeEvent: timeEvents){
      if(INTEGER_INITIAL.equals(timeEvent.getYears()) && INTEGER_INITIAL
          .equals(timeEvent.getMonths()) &&
          INTEGER_INITIAL.equals(timeEvent.getDays()) &&
          INTEGER_INITIAL.equals(timeEvent.getHours())){
        ApplicationSetting.getInstance().getStdoutwriter()
        .writeln("Problem with time event "+timeEvent.getTimeEventId(),
            Logs.ERROR,"TESI20");
        ApplicationSetting.getInstance().getStdoutwriter()
        .writeln("All time increment values are set to zero, skipping",
            Logs.ERROR,"TESI21");
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
      if (!calNextDelay.after(currentTime)
          || (timeEvent.getNextDatetime() == null)) {

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
  @Transactional(propagation=Propagation.REQUIRES_NEW)
  public List<TimeEvent>  loadAllTimeEvents() throws GenericException{
      ApplicationSetting.getInstance().getStdoutwriter()
      .writeln("**TimeEventserviceImpl.loadAllTimeEvents method start " +
          "loading all timeEvents",
          Logs.STATUS1,"TESI22");
      return timeEventDao.loadAllTimeEvents();
  }

  @Override
  @Transactional(propagation=Propagation.REQUIRES_NEW)
  public void updateTimeEventsForBroker() throws GenericException {
    updateTimeEventsForNextCall(loadAllTimeEvents());
  }
}
