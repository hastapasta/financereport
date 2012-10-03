package com.pikefin.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.Exclude;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.ExcludeDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to exclude operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class ExcludeDaoImpl extends AbstractDao<Exclude> implements ExcludeDao {
	@Autowired
	private SessionFactory sessionFactory;
	public ExcludeDaoImpl(SessionFactory sessionFactory) {
		 super(Exclude.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public ExcludeDaoImpl() {
		 super(Exclude.class);
	    }
	@Override
	/**
	 * Saves the Exclude information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Exclude entity
	 * @returns returns the persisted Exclude instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Exclude saveExcludeInfo(Exclude excludeEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			excludeEntity=super.save(excludeEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_EXCLUDE_DATA,e.getMessage(),e.getCause());
		}
		return excludeEntity;
	}

	
	/**
	 * Updates the Exclude information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Exclude entity
	 * @returns returns the persisted Exclude instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Exclude updateExcludeInfo(Exclude excludeEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(excludeEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_EXCLUDE_DATA,e.getMessage(),e.getCause());
		}
		return excludeEntity;
	}

	@Override
	/**
	 * Deleted the Exclude information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Exclude entity
	 * @returns returns true if the Exclude is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteExcludeInfo(Exclude excludeEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(excludeEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_EXCLUDE_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the Exclude information from the database based on supplied ExcludeID. It first loads the Exclude then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the ExcludeId of the Exclude entity
	 * @returns returns true if the Exclude is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteExcludeInfoById(Integer groupId) throws GenericException {
		Exclude excludeEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			excludeEntity=loadExcludeInfo(groupId);
			result=super.delete(excludeEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_EXCLUDE_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the Exclude information from the database based on supplied ExcludeID.
	 * @author Amar_Deep_Singh
	 * @param takes the ExcludeId of the Exclude entity
	 * @returns returns Exclude entity if the Exclude is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Exclude loadExcludeInfo(Integer groupId) throws GenericException {
		Exclude excludeEntity;
		try{
			Session session=sessionFactory.openSession();
			excludeEntity=super.find(groupId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_EXCLUDE_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return excludeEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Exclude entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Exclude> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Exclude> loadAllExcludes() throws GenericException {
		List<Exclude> excludes=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(Exclude.class);
			excludes=(List<Exclude>)criteria.list();
			
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return excludes;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Exclude> loadAllExcludesByTaskId(Integer taskId)
			throws GenericException {
		List<Exclude> excludes=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(Exclude.class);
			Criterion taskICriteriad=Restrictions.eq("taskId", taskId);
			criteria=criteria.createCriteria("task").add(taskICriteriad);
			excludes=(List<Exclude>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return excludes;
	}

}
