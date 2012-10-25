package com.pikefin.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.Alert;
import com.pikefin.businessobjects.AlertTarget;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.AlertTargetDao;
import com.pikefin.exceptions.GenericException;
import com.sun.org.apache.bcel.internal.generic.RETURN;
/**
 * Have methods related to AlertTarget operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class AlertTargetDaoImpl extends AbstractDao<AlertTarget> implements AlertTargetDao {
	@Autowired
	private SessionFactory sessionFactory;
	public AlertTargetDaoImpl(SessionFactory sessionFactory) {
		 super(AlertTarget.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public AlertTargetDaoImpl() {
		 super(AlertTarget.class);
	    }
	@Override
	/**
	 * Saves the AlertTarget information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the AlertTarget entity
	 * @returns returns the persisted AlertTarget instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public AlertTarget saveAlertTargetInfo(AlertTarget alertTargetEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			alertTargetEntity=super.save(alertTargetEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_ALERT_TARGET_DATA,e.getMessage(),e.getCause());
		}
		return alertTargetEntity;
	}

	
	/**
	 * Updates the AlertTarget information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached AlertTarget entity
	 * @returns returns the persisted AlertTarget instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public AlertTarget updateAlertTargetInfo(AlertTarget alertTargetEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(alertTargetEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_ALERT_TARGET_DATA,e.getMessage(),e.getCause());
		}
		return alertTargetEntity;
	}

	@Override
	/**
	 * Deleted the AlertTarget information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached AlertTarget entity
	 * @returns returns true if the AlertTarget is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteAlertTargetInfo(AlertTarget alertTargetEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(alertTargetEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_ALERT_TARGET_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the AlertTarget information from the database based on supplied AlertTargetID. It first loads the AlertTarget then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the AlertTargetId of the AlertTarget entity
	 * @returns returns true if the AlertTarget is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteAlertTargetInfoById(Integer groupId) throws GenericException {
		AlertTarget alertTargetEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			alertTargetEntity=loadAlertTargetInfo(groupId);
			result=super.delete(alertTargetEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_ALERT_TARGET_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the AlertTarget information from the database based on supplied AlertTargetID.
	 * @author Amar_Deep_Singh
	 * @param takes the AlertTargetId of the AlertTarget entity
	 * @returns returns AlertTarget entity if the AlertTarget is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public AlertTarget loadAlertTargetInfo(Integer groupId) throws GenericException {
		AlertTarget alertTargetEntity;
		try{
			Session session=getOpenSession();
			alertTargetEntity=super.find(groupId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_ALERT_TARGET_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return alertTargetEntity;
	}

	@Override
	/**
	 * Retrieve the list of all AlertTarget entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<AlertTarget> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<AlertTarget> loadAllAlertTargets() throws GenericException {
		List<AlertTarget> columns=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(AlertTarget.class);
			columns=(List<AlertTarget>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return columns;
	}
	/**
	 * Loads all the AlertTargets based on supplied Alert
	 * @author Amar_Deep_Singh
	 * @throws GenericException
	 * @return  List<AlertTarget>
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<AlertTarget> loadAllTargets(Alert alert)
			throws GenericException {
		List<AlertTarget> alertTargets=null;
		try{
			Session session=getOpenSession();
			String queryString="Select at from AlertTarget at inner join at.alerts a where a.alertId="+alert.getAlertId();
			Query query=session.createQuery(queryString);
			alertTargets=(List<AlertTarget>)query.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return alertTargets;
		
	}
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Session getOpenSession(){
		Session session;
		if(sessionFactory.getCurrentSession()!=null){
			session=sessionFactory.getCurrentSession();
		}else{
			session=sessionFactory.openSession();

		}
		return session;
	}

	

}
