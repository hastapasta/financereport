package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.TimeEvent;
import com.pikefin.exceptions.GenericException;

public interface TimeEventDao {
	public TimeEvent saveTimeEventInfo(TimeEvent timeEventEntity) throws GenericException;
	public TimeEvent updateTimeEventInfo(TimeEvent timeEventEntity) throws GenericException;
	public Boolean deleteTimeEventInfo(TimeEvent timeEventEntity ) throws GenericException;
	public Boolean deleteTimeEventInfoById(Integer timeEventId ) throws GenericException;
	public TimeEvent loadTimeEventInfo(Integer timeEventId) throws GenericException;
	public List<TimeEvent> loadAllTimeEvents() throws GenericException;
	public List<TimeEvent> updateBatchTimeEvents(List<TimeEvent> batchEvent) throws GenericException;
}
