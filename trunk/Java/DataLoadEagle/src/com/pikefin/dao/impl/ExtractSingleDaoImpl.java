package com.pikefin.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.ExtractSingle;
import com.pikefin.businessobjects.Job;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.ExtractSingleDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to ExtractSingle operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class ExtractSingleDaoImpl extends AbstractDao<ExtractSingle> implements ExtractSingleDao {
	@Autowired
	private SessionFactory sessionFactory;
	public ExtractSingleDaoImpl(SessionFactory sessionFactory) {
		 super(ExtractSingle.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public ExtractSingleDaoImpl() {
		 super(ExtractSingle.class);
	    }
	@Override
	/**
	 * Saves the ExtractSingle information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the ExtractSingle entity
	 * @returns returns the persisted ExtractSingle instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public ExtractSingle saveExtractSingleInfo(ExtractSingle extractSingleEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			extractSingleEntity=super.save(extractSingleEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_EXTRACT_SINGLE_DATA,e.getMessage(),e.getCause());
		}
		return extractSingleEntity;
	}

	
	/**
	 * Updates the ExtractSingle information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached ExtractSingle entity
	 * @returns returns the persisted ExtractSingle instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ExtractSingle updateExtractSingleInfo(ExtractSingle extractSingleEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(extractSingleEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_EXTRACT_SINGLE_DATA,e.getMessage(),e.getCause());
		}
		return extractSingleEntity;
	}

	@Override
	/**
	 * Deleted the ExtractSingle information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached ExtractSingle entity
	 * @returns returns true if the ExtractSingle is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteExtractSingleInfo(ExtractSingle extractSingleEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(extractSingleEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_EXTRACT_SINGLE_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the ExtractSingle information from the database based on supplied ExtractSingleID. It first loads the ExtractSingle then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the ExtractSingleId of the ExtractSingle entity
	 * @returns returns true if the ExtractSingle is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteExtractSingleInfoById(Integer extractSingleId) throws GenericException {
		ExtractSingle extractSingleEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			extractSingleEntity=loadExtractSingleInfo(extractSingleId);
			result=super.delete(extractSingleEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_EXTRACT_SINGLE_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the ExtractSingle information from the database based on supplied ExtractSingleID.
	 * @author Amar_Deep_Singh
	 * @param takes the ExtractSingleId of the ExtractSingle entity
	 * @returns returns ExtractSingle entity if the ExtractSingle is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public ExtractSingle loadExtractSingleInfo(Integer extractSingleId) throws GenericException {
		ExtractSingle extractSingleEntity;
		try{
			Session session=getOpenSession();
			extractSingleEntity=super.find(extractSingleId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_EXTRACT_SINGLE_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return extractSingleEntity;
	}

	@Override
	/**
	 * Retrieve the list of all ExtractSingle entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<ExtractSingle> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<ExtractSingle> loadAllExtractSingles() throws GenericException {
		List<ExtractSingle> columns=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(ExtractSingle.class);
			columns=(List<ExtractSingle>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return columns;
	}
	/**
	 * Gets the ExtractSingle object based on data set value 
	 * @return
	 * @throws GenericException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ExtractSingle loadExtractSinglesByDataSet(String dataSet) throws GenericException {
		
		ExtractSingle extract=null;
		try{
			Session session=getOpenSession();
			//TODO it throws exception saying does not contain the property extractSingleId 
			//TODO confusion is that what is the relation between ExtractSingle/ExtractTable & Job
			//After discussion with oliver we dicided to add a new column to Job table for extract single.
			Query query=session.createQuery("select c from ExtractSingle c, Job j where c.extractSingleId=j.extractKeySingle.extractSingleId and j.dataSet='"+dataSet+"'");
			extract= (ExtractSingle)query.uniqueResult();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_EXTRACT_SINGLE_BY_DATA_SET,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_EXTRACT_SINGLE_BY_DATA_SET,e.getMessage() , e.getCause());
		}
		return extract;
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
