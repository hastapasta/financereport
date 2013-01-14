package com.pikefin.services.inter;

import java.util.Calendar;

import com.pikefin.businessobjects.RepeatType;
import com.pikefin.exceptions.GenericException;

public interface RepeatTypeService {
	public RepeatType loadRepeatTypeNone() throws GenericException;
	public void updateRepeatTypesForNextTrigger(Calendar cal) throws GenericException;

}
