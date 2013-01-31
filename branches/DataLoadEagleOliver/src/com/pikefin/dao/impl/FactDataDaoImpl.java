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
import com.pikefin.businessobjects.FactData;
import com.pikefin.businessobjects.Task;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.FactDataDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to FactData operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class FactDataDaoImpl extends AbstractDao<FactData> implements FactDataDao {
	@Autowired
	private SessionFactory sessionFactory;
	public FactDataDaoImpl(SessionFactory sessionFactory) {
		 super(FactData.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public FactDataDaoImpl() {
		 super(FactData.class);
	    }
	@Override
	/**
	 * Saves the FactData information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the FactData entity
	 * @returns returns the persisted FactData instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public FactData saveFactDataInfo(FactData factDataEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			factDataEntity=super.save(factDataEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_FACT_DATA_DATA,e.getMessage(),e.getCause());
		}
		return factDataEntity;
	}

	
	/**
	 * Updates the FactData information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached FactData entity
	 * @returns returns the persisted FactData instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public FactData updateFactDataInfo(FactData factDataEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(factDataEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_FACT_DATA_DATA,e.getMessage(),e.getCause());
		}
		return factDataEntity;
	}

	@Override
	/**
	 * Deleted the FactData information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached FactData entity
	 * @returns returns true if the FactData is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteFactDataInfo(FactData factDataEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(factDataEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_FACT_DATA_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the FactData information from the database based on supplied FactDataID. It first loads the FactData then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the FactDataId of the FactData entity
	 * @returns returns true if the FactData is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteFactDataInfoById(Integer groupId) throws GenericException {
		FactData factDataEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			factDataEntity=loadFactDataInfo(groupId);
			result=super.delete(factDataEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_FACT_DATA_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the FactData information from the database based on supplied FactDataID.
	 * @author Amar_Deep_Singh
	 * @param takes the FactDataId of the FactData entity
	 * @returns returns FactData entity if the FactData is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public FactData loadFactDataInfo(Integer groupId) throws GenericException {
		FactData factDataEntity;
		try{
			Session session=getOpenSession();
			factDataEntity=super.find(groupId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_FACT_DATA_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return factDataEntity;
	}

	@Override
	/**
	 * Retrieve the list of all FactData entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<FactData> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<FactData> loadAllFactDatas() throws GenericException {
		List<FactData> columns=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(FactData.class);
			columns=(List<FactData>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return columns;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<FactData> loadFactDataByTaskForMaxBatch(Task currentTask)
			throws GenericException {
		List<FactData> factDataList=null;
		try{
			Session session=getOpenSession();
			Query query=session.createQuery("select max(ba.batchId) from FactData fd,Batches ba where fd.batch.batchId=ba.batchId and ba.batchTask.taskId="+currentTask.getTaskId());
			Integer maxId=(Integer)query.uniqueResult();
			Query query2=session.createQuery("select f from FactData f where f.batch.batchId="+maxId);
			factDataList=(List<FactData>)query2.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_FACT_DATA_LIST_BY_TASK_ID_USING_MAX_BATCH,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_FACT_DATA_LIST_BY_TASK_ID_USING_MAX_BATCH,e.getMessage() , e.getCause());
		}
		return factDataList;
	}
	

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<FactData> loadFactDataForAlerts(Alert tempAlert,Task currentTask)
			throws GenericException {
		List<FactData> factDataList=null;
		try{
			Session session=getOpenSession();
			String queryString="Select f from FactData f,Batches b where f.batch.batchId=b.batchId and b.batchTask.taskId="+currentTask.getTaskId()+" " +
					"and f.entity.entityId="+tempAlert.getAlertEntity().getEntityId()+" and f.dateCollected >='"+tempAlert.getAlertTimeEvent().getLastDatetime()+"'";
	
			if (tempAlert.getCalyear() != null){
				queryString += " and f.calyear=" + tempAlert.getCalyear();
			}
			queryString += " order by f.dateCollected asc";
			Query query=session.createQuery(queryString);
			query.setMaxResults(1);
			factDataList=(List<FactData>)query.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_FACT_DATA_LIST_BY_TASK_ID_USING_ALERT,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_FACT_DATA_LIST_BY_TASK_ID_USING_ALERT,e.getMessage() , e.getCause());
		}
		return factDataList;
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
/**
 * Loads the fact data based on the ticker and collection date
 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public FactData loadFactDataInfoByTickerAndCollectionDate(String ticker,
			String collectionDate) throws GenericException {
		FactData factDataEntity;
		try{
			Session session=getOpenSession();
		String strQuery = "select f from FactData f,EntityAlias ea where ea.tickerAlias='"+ticker+"' and f.entity.id=ea.entity.id and f.dateCollected<"+ collectionDate+" order by f.dateCollected desc";
		Query query =session.createQuery(strQuery);
		query.setMaxResults(1);
			factDataEntity=(FactData)query.uniqueResult();
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_FACT_DATA_WITH_TICKER,e.getMessage(),e.getCause());
		}
		return factDataEntity;
	}


	
}
