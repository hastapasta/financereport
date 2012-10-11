package com.pikefin.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
/*
 * Abstract superclass for all data access objects, all data access classes should extend this class
 */

public abstract class AbstractDao<T> {
	private Class<T> businessEntity;
	
	public AbstractDao(Class<T> businessEntity) {
		this.businessEntity=businessEntity;
	}
	/// will be set by the extending entity
	public abstract  SessionFactory getSessionFactory();
	public abstract Session getOpenSession();
	/**
	 * Saves the business object to database
	 * @param businessEntity
	 * @author Amar_Deep_Singh
	 */
	
	public T save(T businessEntity){
		Session session=getSessionFactory().getCurrentSession();
		session.save(businessEntity);
		return businessEntity;
	}
	
	/**
	 * updates the business object to database
	 * @param businessEntity
	 * @author Amar_Deep_Singh
	 */
	
	public T update(T businessEntity){
		Session session=getSessionFactory().getCurrentSession();
		session.update(businessEntity);
		return businessEntity;
	}
	
	/**
	 * deletes a business object from database
	 * @param businessEntity
	 * @author Amar_Deep_Singh
	 */
	
	public boolean delete(T businessEntity){
		Session session=getSessionFactory().getCurrentSession();
		session.delete(businessEntity);
		
		return true;
	}
	
	/**
	 * finds a business entity based on the primacy key 
	 * @param businessEntity
	 * @return Business entity of type <T>
	 * @author Amar_Deep_Singh
	 */
	
	public T find(String businessEntityId){
		assert businessEntityId!=null;
		Session session=getSessionFactory().getCurrentSession();
		T entity=(T)session.load(businessEntity, businessEntityId.trim());
		return entity;
	}
	
	/**
	 * finds a business entity based on the primacy key of type Integer
	 * @param businessEntity
	 * @return Business entity of type <T>
	 * @author Amar_Deep_Singh
	 */
	public T find(Integer businessEntityId){
		Session session=getSessionFactory().getCurrentSession();
		return (T)session.get(businessEntity, businessEntityId);
	}
	
	/**
	 * finds a all business entities based on type <T> 
	 * @param businessEntity
	 * @return List of Business entity of type <T>
	 * @author Amar_Deep_Singh
	 */
	
	public List<T> findAll(){
		Session session=getSessionFactory().getCurrentSession();
		Criteria query =session.createCriteria(getClass());
		List<T> entitiesList=(List<T>)query.list();
		
	return entitiesList;
			
	}
	/**
	 * used for updating entities in batch
	 * @param entitiesList
	 * @param batchSize
	 * @return
	 */
	public List<T> batchUpdate(List<T> entitiesList,int batchSize){
		Session session=getSessionFactory().getCurrentSession();
		int count=0;
		for(T entity:entitiesList){
			session.update(entity);
			  if (batchSize!=0 &&  ++count % batchSize == 0 ) {
			        //flush a batch of updates and release memory:
			        session.flush();
			        session.clear();
			    }
		}
				
	return entitiesList;
			
	}
}

