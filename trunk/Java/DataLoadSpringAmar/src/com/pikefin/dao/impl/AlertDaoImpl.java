package com.pikefin.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.Alert;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.AlertDao;
import com.pikefin.exceptions.GenericException;

/**
 * This Class contains the all DAO operations related to Alert business entity
 * @author Amar_Deep_Singh
 *
 */
public class AlertDaoImpl extends AbstractDao<Alert> implements AlertDao {

		private SessionFactory sessionFactory;
		public AlertDaoImpl(SessionFactory sessionFactory){
			super(Alert.class);
			this.sessionFactory=sessionFactory;
		}
		@Override
		/**
		 * Saves the Alert information into the database
		 * @author Amar_Deep_Singh
		 * @param takes the new instance of the Alert entity
		 * @returns returns the persisted Alert instance
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public Alert saveAlertInfo(Alert alertEntity)
				throws GenericException {
			try{
				alertEntity=super.save(alertEntity);
				
			}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_ALERT_DATA,e.getMessage(),e.getCause());
			}
			return alertEntity;
		}
		
		/**
		 * Updates the Alert information into the database
		 * @author Amar_Deep_Singh
		 * @param takes the detached Alert entity
		 * @returns returns the persisted Alert instance
		 */
		@Override
		@Transactional(propagation=Propagation.REQUIRED)
		public Alert updateAlertInfo(Alert alertEntity)
				throws GenericException {
			try{
				alertEntity=super.update(alertEntity);
				
			}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_ALERT_DATA,e.getMessage(),e.getCause());
			}
			return alertEntity;
		}

		@Override
		/**
		 * Deleted the Alert information from the database
		 * @author Amar_Deep_Singh
		 * @param takes the detached Alert entity
		 * @returns returns true if the Alert is deleted else return false
		 * @throws GenericException
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public Boolean deleteAlertInfo(Alert alertEntity)
				throws GenericException {
			try{
				return super.delete(alertEntity);
			}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_ALERT_INFORMATION,e.getMessage(),e.getCause());
			}
		}

		@Override
		/**
		 * Deleted the Alert information from the database based on supplied AlertID. It first loads the Alert then try to delete
		 * @author Amar_Deep_Singh
		 * @param takes the AlertId of the Alert entity
		 * @returns returns true if the Alert is deleted else return false
		 * @throws GenericException
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public Boolean deleteAlertInfoById(Integer alertId)
				throws GenericException {
			Alert groupEntity=loadAlertInfo(alertId);
			try{
				super.delete(groupEntity);
				
			}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_ALERT_INFORMATION,e.getMessage(),e.getCause());
			}
			return false;
		}

		@Override
		/**
		 * Retrieve the Alert information from the database based on supplied AlertID.
		 * @author Amar_Deep_Singh
		 * @param takes the AlertId of the Alert entity
		 * @returns returns Alert entity if the Alert is found else throws exception
		 * @throws GenericException
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public Alert loadAlertInfo(Integer alertId) throws GenericException {
			Alert alertEntity;
			try{
				alertEntity=super.find(alertId);
			}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_ALERT_DATA_WITH_ID,e.getMessage(),e.getCause());
			}
			return alertEntity;
		}

		@Override
		/**
		 * Retrieve the list of all Alert entities from the database.
		 * @author Amar_Deep_Singh
		 * @returns returns List<Alert> 
		 * @throws GenericException
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public List<Alert> loadAllAlerts() throws GenericException {
			List<Alert> alert=null;
			try{
				Session session=sessionFactory.openSession();
				Criteria criteria=session.createCriteria(Alert.class);
				alert=(List<Alert>)criteria.list();
				
			}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
			}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
			}
			return alert;
		}
		@Override
		public SessionFactory getSessionFactory() {
			return this.sessionFactory;
		}



}
