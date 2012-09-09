package com.pikefin.dao;

import java.util.List;

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
	@Transactional(propagation=Propagation.MANDATORY)
	public void save(T businessEntity){
		Session session=getSessionFactory().openSession();
		session.save(businessEntity);
	}
	
	/**
	 * updates the business object to database
	 * @param businessEntity
	 * @author Amar_Deep_Singh
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public void update(T businessEntity){
		Session session=getSessionFactory().openSession();
		session.update(businessEntity);
	}
	
	/**
	 * deletes a business object from database
	 * @param businessEntity
	 * @author Amar_Deep_Singh
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public void delete(T businessEntity){
		Session session=getSessionFactory().openSession();
		session.delete(businessEntity);
	}
	
	/**
	 * finds a business entity based on the primacy key 
	 * @param businessEntity
	 * @return Business entity of type <T>
	 * @author Amar_Deep_Singh
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public T find(String businessEntityId){
		Session session=getSessionFactory().openSession();
		return (T)session.load(businessEntity, businessEntityId);
	}
	
	/**
	 * finds a business entity based on the primacy key 
	 * @param businessEntity
	 * @return Business entity of type <T>
	 * @author Amar_Deep_Singh
	 */
	@Transactional(propagation=Propagation.MANDATORY)
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
	@Transactional(propagation=Propagation.MANDATORY)
	public List<T> findAll(){
		Session session=getSessionFactory().openSession();
		//return (T)session.load(businessEntity, businessEntityId);
		return null;
	}
}

