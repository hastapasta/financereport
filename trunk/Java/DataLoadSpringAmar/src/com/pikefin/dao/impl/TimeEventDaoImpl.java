package com.pikefin.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.TimeEvent;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.TimeEventDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to TimeEvent operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class TimeEventDaoImpl extends AbstractDao<TimeEvent> implements TimeEventDao {
	@Autowired
	private SessionFactory sessionFactory;
	public TimeEventDaoImpl(SessionFactory sessionFactory) {
		 super(TimeEvent.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public TimeEventDaoImpl() {
		 super(TimeEvent.class);
	    }
	@Override
	/**
	 * Saves the TimeEvent information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the TimeEvent entity
	 * @returns returns the persisted TimeEvent instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public TimeEvent saveTimeEventInfo(TimeEvent timeEventEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			timeEventEntity=super.save(timeEventEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_TIME_EVENT_DATA,e.getMessage(),e.getCause());
		}
		return timeEventEntity;
	}

	
	/**
	 * Updates the TimeEvent information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached TimeEvent entity
	 * @returns returns the persisted TimeEvent instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public TimeEvent updateTimeEventInfo(TimeEvent timeEventEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(timeEventEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_TIME_EVENT_DATA,e.getMessage(),e.getCause());
		}
		return timeEventEntity;
	}

	@Override
	/**
	 * Deleted the TimeEvent information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached TimeEvent entity
	 * @returns returns true if the TimeEvent is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteTimeEventInfo(TimeEvent timeEventEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(timeEventEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_TIME_EVENT_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the TimeEvent information from the database based on supplied TimeEventID. It first loads the TimeEvent then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the TimeEventId of the TimeEvent entity
	 * @returns returns true if the TimeEvent is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteTimeEventInfoById(Integer groupId) throws GenericException {
		TimeEvent timeEventEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			timeEventEntity=loadTimeEventInfo(groupId);
			result=super.delete(timeEventEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_TIME_EVENT_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the TimeEvent information from the database based on supplied TimeEventID.
	 * @author Amar_Deep_Singh
	 * @param takes the TimeEventId of the TimeEvent entity
	 * @returns returns TimeEvent entity if the TimeEvent is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public TimeEvent loadTimeEventInfo(Integer groupId) throws GenericException {
		TimeEvent timeEventEntity;
		try{
			Session session=sessionFactory.openSession();
			timeEventEntity=super.find(groupId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_TIME_EVENT_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return timeEventEntity;
	}

	@Override
	/**
	 * Retrieve the list of all TimeEvent entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<TimeEvent> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<TimeEvent> loadAllTimeEvents() throws GenericException {
		List<TimeEvent> timeEvents=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(TimeEvent.class);
			timeEvents=(List<TimeEvent>)criteria.list();
			
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return timeEvents;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<TimeEvent> updateBatchTimeEvents(List<TimeEvent> entitiesList)
			throws GenericException {
		
		try{
			Session session=sessionFactory.openSession();
			entitiesList=super.batchUpdate(entitiesList, 20);
			
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_TIME_EVENTS_IN_BATCH,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_TIME_EVENTS_IN_BATCH,e.getMessage() , e.getCause());
		}
		return entitiesList;
	}

}
