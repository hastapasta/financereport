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

import com.pikefin.Constants;
import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.AlertTarget;
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
	public List<Object> loadAllLogAlertsForNotification()
			throws GenericException {
		List<Object> logAlerts=null;
		try{
			Session session=sessionFactory.openSession();
			String query1 = " from LogAlert la " ;
			query1 += " inner join la.logAlertAlert a ";
			query1 += " inner join a.alertTarget at ";
			query1 += " inner join a.alertEntity e ";
			query1 += " inner join a.alertTimeEvent te";
			query1 += " inner join la.beforeFactData bfd ";
			query1 += " inner join la.afterFactData afd ";
			//query1 += " inner join AlertTarget at ";
			query1 += " where la.emailSent=0 ";
			//query1 += " and alerts.email_alert=1 ";
			query1 += " and at.type=1 ";
			query1 += " order by at.alertTargetId asc";
			
	/*Below query can be used as a replacement for current query 
	 *
	String subQuery1="select a.id from Alert a  inner join a.alertTarget  at where at.type=1";
	String subQuery2="select la from LogAlert la where la.emailSent=false and la.logAlertAlert.id in ("+subQuery1+") order by la.logAlertAlert.id";
		*/
			Query query=session.createQuery(query1);
			logAlerts=query.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return logAlerts;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<LogAlert> updateLogAlertInBulk(
			List<LogAlert> logAlertEntitiesList) throws GenericException {
			return super.batchUpdate(logAlertEntitiesList, Constants.BATCH_SIZE);
	}
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	

	

}
