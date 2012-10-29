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
import com.pikefin.businessobjects.Batches;
import com.pikefin.businessobjects.Task;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.AlertDao;
import com.pikefin.exceptions.GenericException;

/**
 * This Class contains the all DAO operations related to Alert business entity
 * @author Amar_Deep_Singh
 *
 */
@Component
public class AlertDaoImpl extends AbstractDao<Alert> implements AlertDao {
		@Autowired
		private SessionFactory sessionFactory;
		public AlertDaoImpl(SessionFactory sessionFactory){
			super(Alert.class);
			this.sessionFactory=sessionFactory;
		}
		
		public AlertDaoImpl() {
			 super(Alert.class);
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
				Session session=getOpenSession();
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
		@Override
		/**
		 * Loads alerts using the given task
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public List<Alert> loadAllAlertsByTask(Task currentTask) throws GenericException {
			/*String query;  
			
			query = "select alerts.disabled,alerts.id,alerts.initial_fact_data_id,";
			query += " alerts.notification_count,alerts.user_id,alerts.limit_value,";
			query += " alerts.auto_reset_period,alerts.auto_reset_fired,alerts.fired, alerts.calyear, alerts.type, ";
			query += " entities.ticker,entities.id,entities.full_name, entities.hash, ";
			query += " users.id, ";
			query += " fact_data.value,fact_data.id,fact_data.date_collected,";
			query += " time_events.id,time_events.name,time_events.next_datetime,time_events.last_datetime, ";
			query += " tasks.metric_id, ";
			query += " countries.hash ";
			query += " from alerts ";
			query += " LEFT JOIN entities ON alerts.entity_id=entities.id ";
			query += " LEFT JOIN users ON alerts.user_id=users.id ";
			query += " LEFT JOIN fact_data ON (alerts.initial_fact_data_id=fact_data.id and alerts.calyear<=>fact_data.calyear) ";
			query += " LEFT JOIN time_events ON alerts.time_event_id=time_events.id ";
			query += " LEFT JOIN tasks ON alerts.task_id=tasks.id ";
			query += " LEFT JOIN countries_entities ON countries_entities.entity_id=entities.id ";
			query += " LEFT JOIN countries ON countries.id=countries_entities.country_id ";
			query += " where alerts.task_id=" + currentTask.getTaskId();
			query += " and (countries_entities.default_country=1 OR countries_entities.default_country is null)";
			query += " order by time_events.id";*/
			/*String queryString="select a.id from Alert a " +
					"LEFT JOIN Task t ON a.alertTask.taskId=t.taskId " +
					"LEFT JOIN Entity e ON a.alertEntity.entityId=e.entityId " +
					"LEFT JOIN User u ON a.alertUser.userId=u.userId " +
					"LEFT JOIN FactData f ON (a.alertInitialFactData.factDataId=f.factDataId and (((a.calyear is null) and (f.calyear is null)) or (a.calyear=f.calyear))) " +
					"LEFT JOIN TimeEvent te ON a.alertTimeEvent.timeEventId=te.timeEventId" +
					"where a.task_id=" +currentTask.getTaskId()+
					" order by  a.alertTimeEvent.timeEventId";*/
			String queryString="select a from Alert as a " +
					"LEFT JOIN a.alertTask as t " +
					"LEFT JOIN a.alertEntity as e " +
					"LEFT JOIN a.alertUser as u " +
					"LEFT JOIN a.alertInitialFactData as f " +
					"LEFT JOIN a.alertTimeEvent as te " +
					"where  a.alertTask.taskId=" +currentTask.getTaskId()+
					" and (((a.calyear is null) and (f.calyear is null)) or (a.calyear=f.calyear)) order by  a.alertTimeEvent.timeEventId";
//LEFT JOIN e.countries ON ce.entity_id=e.id
//LEFT JOIN countries c ON c.id=ce.country_id
			List<Alert> alerts=null;
			try{
				/*String queryString="select a from Alert a,Country c where a.alertTask.taskId="+currentTask.getTaskId()+" and (((a.calyear is null) and (a.alertInitialFactData.calyear is null)) or (a.calyear=a.alertInitialFactData.calyear))  " +
						"order by a.alertTimeEvent.timeEventId";*/
				Session session=getOpenSession();
				Query query=session.createQuery(queryString);
				alerts=query.list();
			}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
			}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
			}
			return alerts;
		}

}
