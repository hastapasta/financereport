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
import com.pikefin.businessobjects.Metric;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.MetricDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to Metric operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class MetricDaoImpl extends AbstractDao<Metric> implements MetricDao {
	@Autowired
	private SessionFactory sessionFactory;
	public MetricDaoImpl(SessionFactory sessionFactory) {
		 super(Metric.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public MetricDaoImpl() {
		 super(Metric.class);
	    }
	@Override
	/**
	 * Saves the Metric information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Metric entity
	 * @returns returns the persisted Metric instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Metric saveMetricInfo(Metric metricEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			metricEntity=super.save(metricEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_METRIC_DATA,e.getMessage(),e.getCause());
		}
		return metricEntity;
	}

	
	/**
	 * Updates the Metric information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Metric entity
	 * @returns returns the persisted Metric instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Metric updateMetricInfo(Metric metricEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(metricEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_METRIC_DATA,e.getMessage(),e.getCause());
		}
		return metricEntity;
	}

	@Override
	/**
	 * Deleted the Metric information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Metric entity
	 * @returns returns true if the Metric is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteMetricInfo(Metric metricEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(metricEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_METRIC_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the Metric information from the database based on supplied MetricID. It first loads the Metric then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the MetricId of the Metric entity
	 * @returns returns true if the Metric is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteMetricInfoById(Integer groupId) throws GenericException {
		Metric metricEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			metricEntity=loadMetricInfo(groupId);
			result=super.delete(metricEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_METRIC_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the Metric information from the database based on supplied MetricID.
	 * @author Amar_Deep_Singh
	 * @param takes the MetricId of the Metric entity
	 * @returns returns Metric entity if the Metric is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Metric loadMetricInfo(Integer groupId) throws GenericException {
		Metric metricEntity;
		try{
			Session session=getOpenSession();
			metricEntity=super.find(groupId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_METRIC_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return metricEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Metric entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Metric> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Metric> loadAllMetrics() throws GenericException {
		List<Metric> columns=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(Metric.class);
			columns=(List<Metric>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return columns;
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
