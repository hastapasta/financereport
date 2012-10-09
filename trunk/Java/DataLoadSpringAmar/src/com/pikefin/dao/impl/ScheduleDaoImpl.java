package com.pikefin.dao.impl;

import java.util.Calendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.Constants;
import com.pikefin.ErrorCode;
import com.pikefin.RepeatTypeEnum;
import com.pikefin.businessobjects.Schedule;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.ScheduleDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to Schedule operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class ScheduleDaoImpl extends AbstractDao<Schedule> implements ScheduleDao {
	@Autowired
	private SessionFactory sessionFactory;
	public ScheduleDaoImpl(SessionFactory sessionFactory) {
		 super(Schedule.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public ScheduleDaoImpl() {
		 super(Schedule.class);
	    }
	@Override
	/**
	 * Saves the Schedule information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Schedule entity
	 * @returns returns the persisted Schedule instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Schedule saveScheduleInfo(Schedule scheduleEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			scheduleEntity=super.save(scheduleEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_SCHEDULE_DATA,e.getMessage(),e.getCause());
		}
		return scheduleEntity;
	}

	
	/**
	 * Updates the Schedule information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Schedule entity
	 * @returns returns the persisted Schedule instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Schedule updateScheduleInfo(Schedule scheduleEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(scheduleEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_SCHEDULE_DATA,e.getMessage(),e.getCause());
		}
		return scheduleEntity;
	}

	@Override
	/**
	 * Deleted the Schedule information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Schedule entity
	 * @returns returns true if the Schedule is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteScheduleInfo(Schedule scheduleEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(scheduleEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_SCHEDULE_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the Schedule information from the database based on supplied ScheduleID. It first loads the Schedule then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the ScheduleId of the Schedule entity
	 * @returns returns true if the Schedule is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteScheduleInfoById(Integer scheduleId) throws GenericException {
		Schedule scheduleEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			scheduleEntity=loadScheduleInfo(scheduleId);
			result=super.delete(scheduleEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_SCHEDULE_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the Schedule information from the database based on supplied ScheduleID.
	 * @author Amar_Deep_Singh
	 * @param takes the ScheduleId of the Schedule entity
	 * @returns returns Schedule entity if the Schedule is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Schedule loadScheduleInfo(Integer scheduleId) throws GenericException {
		Schedule scheduleEntity;
		try{
			Session session=sessionFactory.openSession();
			scheduleEntity=super.find(scheduleId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_SCHEDULE_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return scheduleEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Schedule entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Schedule> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Schedule> loadAllSchedules() throws GenericException {
		List<Schedule> schedules=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(Schedule.class);
			schedules=(List<Schedule>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return schedules;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Schedule> loadAllSchedulesForBrokerExecuter()
			throws GenericException {
		List<Schedule> schedules=null;
		try{
			Calendar currentTime = Calendar.getInstance();
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(Schedule.class);
			Criterion  repeatTypeRunOnceCriteria=Restrictions.eq("type", RepeatTypeEnum.RUNONCE.toString());
			Criterion  repeatTypeRunEveryCriteria=Restrictions.eq("type", RepeatTypeEnum.RUNEVERY.toString());
			Criterion  repeatTypeDateCriteria=Restrictions.gt("nextTrigger", currentTime.getTime());
			Criterion  repeatTypeNotNoneCriteria=Restrictions.ne("type", RepeatTypeEnum.NONE.toString());
			criteria=criteria.createCriteria("repeatType").add(Restrictions.or(repeatTypeRunOnceCriteria,repeatTypeRunEveryCriteria,repeatTypeDateCriteria)).add(Restrictions.and(repeatTypeNotNoneCriteria));
			schedules=(List<Schedule>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return schedules;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Schedule> updateSchedulesBatch(List<Schedule> schedules)
			throws GenericException {
		Session session=sessionFactory.openSession();
			return super.batchUpdate(schedules, Constants.BATCH_SIZE);
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
}
