package com.pikefin.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
	
	/**
	 * Saves the business object to database
	 * @param businessEntity
	 * @author Amar_Deep_Singh
	 */
	
	public T save(T businessEntity){
		Session session=getSessionFactory().openSession();
		session.save(businessEntity);
		
	return businessEntity;
	}
	
	/**
	 * updates the business object to database
	 * @param businessEntity
	 * @author Amar_Deep_Singh
	 */
	
	public T update(T businessEntity){
		Session session=getSessionFactory().openSession();
		session.update(businessEntity);
		
		return businessEntity;
	}
	
	/**
	 * deletes a business object from database
	 * @param businessEntity
	 * @author Amar_Deep_Singh
	 */
	
	public boolean delete(T businessEntity){
		Session session=getSessionFactory().openSession();
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
		Session session=getSessionFactory().openSession();
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
		Session session=getSessionFactory().openSession();
		return (T)session.load(businessEntity, businessEntityId);
	}
	
	/**
	 * finds a all business entities based on type <T> 
	 * @param businessEntity
	 * @return List of Business entity of type <T>
	 * @author Amar_Deep_Singh
	 */
	
	public List<T> findAll(){
		Session session=getSessionFactory().openSession();
		Criteria query =session.createCriteria(getClass());
		List<T> entitiesList=(List<T>)query.list();
		
	return entitiesList;
			
	}
}

