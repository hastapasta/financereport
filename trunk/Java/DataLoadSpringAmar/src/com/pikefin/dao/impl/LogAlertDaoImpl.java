package com.pikefin.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.LogAlert;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.LogAlertDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to LogAlert operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class LogAlertDaoImpl extends AbstractDao<LogAlert> implements LogAlertDao {
	@Autowired
	private SessionFactory sessionFactory;
	public LogAlertDaoImpl(SessionFactory sessionFactory) {
		 super(LogAlert.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public LogAlertDaoImpl() {
		 super(LogAlert.class);
	    }
	@Override
	/**
	 * Saves the LogAlert information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the LogAlert entity
	 * @returns returns the persisted LogAlert instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public LogAlert saveLogAlertInfo(LogAlert logAlertEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			logAlertEntity=super.save(logAlertEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_LOG_ALERT_DATA,e.getMessage(),e.getCause());
		}
		return logAlertEntity;
	}

	
	/**
	 * Updates the LogAlert information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached LogAlert entity
	 * @returns returns the persisted LogAlert instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public LogAlert updateLogAlertInfo(LogAlert logAlertEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(logAlertEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_LOG_ALERT_DATA,e.getMessage(),e.getCause());
		}
		return logAlertEntity;
	}

	@Override
	/**
	 * Deleted the LogAlert information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached LogAlert entity
	 * @returns returns true if the LogAlert is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteLogAlertInfo(LogAlert logAlertEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(logAlertEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_LOG_ALERT_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the LogAlert information from the database based on supplied LogAlertID. It first loads the LogAlert then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the LogAlertId of the LogAlert entity
	 * @returns returns true if the LogAlert is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteLogAlertInfoById(Integer logAlertId) throws GenericException {
		LogAlert logAlertEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			logAlertEntity=loadLogAlertInfo(logAlertId);
			result=super.delete(logAlertEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_LOG_ALERT_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the LogAlert information from the database based on supplied LogAlertID.
	 * @author Amar_Deep_Singh
	 * @param takes the LogAlertId of the LogAlert entity
	 * @returns returns LogAlert entity if the LogAlert is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public LogAlert loadLogAlertInfo(Integer logAlertId) throws GenericException {
		LogAlert logAlertEntity;
		try{
			Session session=sessionFactory.openSession();
			logAlertEntity=super.find(logAlertId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_LOG_ALERT_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return logAlertEntity;
	}

	@Override
	/**
	 * Retrieve the list of all LogAlert entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<LogAlert> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<LogAlert> loadAllLogAlerts() throws GenericException {
		List<LogAlert> logAlerts=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(LogAlert.class);
			logAlerts=(List<LogAlert>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return logAlerts;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<LogAlert> loadAllLogAlertsForNotification()
			throws GenericException {
		List<LogAlert> logAlerts=null;
		try{
			Session session=sessionFactory.openSession();
			Query query= session.createQuery("select c from LogAlert c where c.emailSent=true and c.logAlertAlert.alertTarget.alertTargetId");
			Criteria criteria=session.createCriteria(LogAlert.class);
			criteria.add(Restrictions.eq("emailSent", false));
		//	Criterion criterionJoin=Restrictions.
			logAlerts=(List<LogAlert>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return logAlerts;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	

}
