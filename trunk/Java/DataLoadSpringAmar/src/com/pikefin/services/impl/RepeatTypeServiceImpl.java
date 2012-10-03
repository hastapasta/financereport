package com.pikefin.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pikefin.ErrorCode;
import com.pikefin.RepeatTypeEnum;
import com.pikefin.businessobjects.RepeatType;
import com.pikefin.dao.inter.RepeatTypeDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.RepeatTypeService;
@Service
public class RepeatTypeServiceImpl implements RepeatTypeService{

	@Autowired
	private RepeatTypeDao repeatDao;
	@Override
	@Transactional
	public RepeatType loadRepeatTypeNone() throws GenericException {
		List<RepeatType> noneRepeatList=repeatDao.loadRepeatTypesByType(RepeatTypeEnum.NONE);
		if(noneRepeatList!=null && noneRepeatList.size()>0)
			return noneRepeatList.get(0);
		else
		throw new GenericException(ErrorCode.COULD_NOT_FIND_REPEAT_TYPE_NONE,"",null);
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateRepeatTypesForNextTrigger(Calendar cal)
			throws GenericException {

		List<RepeatType> repeatTypes=repeatDao.loadAllRepeatTypes();
		String strType;
		Calendar triggerCal;
		Calendar newCal = Calendar.getInstance();
		List<RepeatType> repeatTypesForUpdate=new ArrayList<RepeatType>(); 
		
		 for (RepeatType tempRepeatType : repeatTypes) {
			 triggerCal = Calendar.getInstance();
			 triggerCal.setTime(tempRepeatType.getNextTrigger());
			 strType = tempRepeatType.getType();
			 newCal.setTime(cal.getTime());
			 int nUnit = 0;
			 
			if (newCal.after(triggerCal)) {
							
				/* we are going to overwrite the database with newcal, not triggercal, since if this code hasn't been
				 * run in a while, triggercal could lag by multiple cycles.
				 */
					
				 if ( strType.equals(RepeatTypeEnum.WEEKLY.toString())) {
					 nUnit = Calendar.WEEK_OF_YEAR;
				}else if (strType.equals(RepeatTypeEnum.MONTHLY.toString())) {
					 nUnit = Calendar.MONTH;
			   }else if (strType.equals(RepeatTypeEnum.HOURLY.toString())) {
					 nUnit = Calendar.HOUR;
			   }else if (strType.equals(RepeatTypeEnum.MINUTE.toString())) {
					 nUnit = Calendar.MINUTE;
				}else if (strType.equals(RepeatTypeEnum.DAILY.toString())) {
					 nUnit = Calendar.DAY_OF_YEAR;
				 }else {
				 //We'll get here if the REPEAT_TYPE is RUNONCE,RUNEVERY OR NONE.
					 continue;
				 }
							 
				 while (newCal.after(triggerCal)) {
					 triggerCal.add(nUnit,tempRepeatType.getMultiplier());
				 }
				tempRepeatType.setNextTrigger(triggerCal.getTime());
				repeatTypesForUpdate.add(tempRepeatType);
			 }
		 }
		 repeatDao.updateRepeatTypeBatch(repeatTypesForUpdate);
  
	}

}
