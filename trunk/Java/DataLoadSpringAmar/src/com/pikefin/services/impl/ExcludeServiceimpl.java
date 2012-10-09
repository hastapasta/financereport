package com.pikefin.services.impl;

import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikefin.Constants;
import com.pikefin.ErrorCode;
import com.pikefin.PikefinUtil;
import com.pikefin.businessobjects.Exclude;
import com.pikefin.dao.inter.ExcludeDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.ExcludedService;

@Service
public class ExcludeServiceimpl implements ExcludedService {
	Logger log = Logger.getLogger(ExcludeServiceimpl.class);
     @Autowired
     private ExcludeDao excludeDao;
	@Override
	public boolean isTaskExcluded(List<Exclude> excludeList)
			throws GenericException {
		try {
			Calendar currentTime = Calendar.getInstance();
			for (Exclude exclude : excludeList) {
				String strBeginTime = exclude.getBeginTime();
				String strEndTime = exclude.getEndTime();
				String[] arrayBeginTime = strBeginTime.split(":");
				String[] arrayEndTime = strEndTime.split(":");
				if (Constants.ExcludeType.EXCLUDE_TYPE_ONE.equals(exclude.getType())) {
					int nBeginDay = exclude.getBeginDay();
					int nEndDay = exclude.getEndDay();
					double fBegin = (double) nBeginDay
							+ (double) (((3600 * Integer
									.parseInt(arrayBeginTime[0]))
									+ (60 * Integer.parseInt(arrayBeginTime[1])) + (Integer
										.parseInt(arrayBeginTime[2]))) / (double) (3600 * 24));
					double fEnd = (double) nEndDay
							+ (double) (((3600 * Integer
									.parseInt(arrayEndTime[0]))
									+ (60 * Integer.parseInt(arrayEndTime[1])) + (Integer
										.parseInt(arrayEndTime[2]))) / (double) (3600 * 24));
					double fCurrent = (double) currentTime
							.get(Calendar.DAY_OF_WEEK)
							+ (double) (((3600 * currentTime
									.get(Calendar.HOUR_OF_DAY))
									+ (60 * currentTime.get(Calendar.MINUTE)) + (currentTime
										.get(Calendar.SECOND))) / (double) (3600 * 24));
					if ((fCurrent >= fBegin) && (fCurrent <= fEnd)) {
						log.debug("Excluding task "
								+ exclude.getTask().getTaskId()
								+ " according to excludes id "
								+ exclude.getExcludeId());
						return true;
					}

				} else if (exclude.getType() == 2) {
					if (exclude.getOneTimeDate() == null) {
						log.debug("Error with " + exclude.getTask().getTaskId()
								+ " excludes id " + exclude.getExcludeId()
								+ ", onetime_date null");
						return false;
					}

					String strDateBefore = exclude.getOneTimeDate() + " "
							+ strBeginTime;
					String strDateAfter = exclude.getOneTimeDate() + " "
							+ strEndTime;
					Calendar calBefore = Calendar.getInstance();
					Calendar calAfter = Calendar.getInstance();
					Calendar calCurrent = Calendar.getInstance();

					calBefore.setTime(PikefinUtil.formatter
							.parse(strDateBefore));
					calAfter.setTime(PikefinUtil.formatter.parse(strDateAfter));
					if ((calBefore.before(calCurrent))
							&& (calCurrent.before(calAfter))) {
						log.debug("Excluding task "
								+ exclude.getTask().getTaskId()
								+ " according to excludes id "
								+ exclude.getExcludeId());
						return true;
					}

				}

			}

		} catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_GET_EXCLUDED_TASK,
					e.getMessage(), e.getCause());
		}
		return false;
	}

	@Override
	public List<Exclude> loadAllExcludesByTaskId(Integer taskId)
			throws GenericException {
		return excludeDao.loadAllExcludesByTaskId(taskId);
		}

}
