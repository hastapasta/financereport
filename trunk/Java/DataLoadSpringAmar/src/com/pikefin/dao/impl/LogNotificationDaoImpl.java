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
import com.pikefin.businessobjects.LogNotification;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.LogNotificationDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to LogNotification operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class LogNotificationDaoImpl extends AbstractDao<LogNotification> implements LogNotificationDao {
	@Autowired
	private SessionFactory sessionFactory;
	public LogNotificationDaoImpl(SessionFactory sessionFactory) {
		 super(LogNotification.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public LogNotificationDaoImpl() {
		 super(LogNotification.class);
	    }
	@Override
	/**
	 * Saves the LogNotification information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the LogNotification entity
	 * @returns returns the persisted LogNotification instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public LogNotification saveLogNotificationInfo(LogNotification logNotificationEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			logNotificationEntity=super.save(logNotificationEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_LOG_NOTIFICATION_DATA,e.getMessage(),e.getCause());
		}
		return logNotificationEntity;
	}

	
	/**
	 * Updates the LogNotification information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached LogNotification entity
	 * @returns returns the persisted LogNotification instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public LogNotification updateLogNotificationInfo(LogNotification logNotificationEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(logNotificationEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_LOG_NOTIFICATION_DATA,e.getMessage(),e.getCause());
		}
		return logNotificationEntity;
	}

	@Override
	/**
	 * Deleted the LogNotification information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached LogNotification entity
	 * @returns returns true if the LogNotification is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteLogNotificationInfo(LogNotification logNotificationEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(logNotificationEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_LOG_NOTIFICATION_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the LogNotification information from the database based on supplied LogNotificationID. It first loads the LogNotification then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the LogNotificationId of the LogNotification entity
	 * @returns returns true if the LogNotification is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteLogNotificationInfoById(Integer logNotificationId) throws GenericException {
		LogNotification logNotificationEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			logNotificationEntity=loadLogNotificationInfo(logNotificationId);
			result=super.delete(logNotificationEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_LOG_NOTIFICATION_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the LogNotification information from the database based on supplied LogNotificationID.
	 * @author Amar_Deep_Singh
	 * @param takes the LogNotificationId of the LogNotification entity
	 * @returns returns LogNotification entity if the LogNotification is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public LogNotification loadLogNotificationInfo(Integer logNotificationId) throws GenericException {
		LogNotification logNotificationEntity;
		try{
			Session session=sessionFactory.openSession();
			logNotificationEntity=super.find(logNotificationId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_LOG_NOTIFICATION_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return logNotificationEntity;
	}

	@Override
	/**
	 * Retrieve the list of all LogNotification entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<LogNotification> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<LogNotification> loadAllLogNotifications() throws GenericException {
		List<LogNotification> logNotifications=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(LogNotification.class);
			logNotifications=(List<LogNotification>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return logNotifications;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
