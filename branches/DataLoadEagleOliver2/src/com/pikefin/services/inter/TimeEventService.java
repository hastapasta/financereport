package com.pikefin.services.inter;

import java.util.List;

import com.pikefin.businessobjects.TimeEvent;
import com.pikefin.exceptions.GenericException;

public interface TimeEventService {
	public void  updateTimeEventsForBroker() throws GenericException;
	public void  updateTimeEventsForNextCall(List<TimeEvent> timeEvents) throws GenericException;
	public List<TimeEvent>  loadAllTimeEvents() throws GenericException;
}
