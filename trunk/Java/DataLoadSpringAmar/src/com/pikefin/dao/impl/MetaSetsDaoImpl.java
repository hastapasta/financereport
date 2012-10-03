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
import com.pikefin.businessobjects.MetaSets;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.MetaSetsDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to MetaSets operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class MetaSetsDaoImpl extends AbstractDao<MetaSets> implements MetaSetsDao {
	@Autowired
	private SessionFactory sessionFactory;
	public MetaSetsDaoImpl(SessionFactory sessionFactory) {
		 super(MetaSets.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public MetaSetsDaoImpl() {
		 super(MetaSets.class);
	    }
	@Override
	/**
	 * Saves the MetaSets information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the MetaSets entity
	 * @returns returns the persisted MetaSets instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public MetaSets saveMetaSetsInfo(MetaSets metaSetsEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			metaSetsEntity=super.save(metaSetsEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_META_SETS_DATA,e.getMessage(),e.getCause());
		}
		return metaSetsEntity;
	}

	
	/**
	 * Updates the MetaSets information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached MetaSets entity
	 * @returns returns the persisted MetaSets instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public MetaSets updateMetaSetsInfo(MetaSets metaSetsEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(metaSetsEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_META_SETS_DATA,e.getMessage(),e.getCause());
		}
		return metaSetsEntity;
	}

	@Override
	/**
	 * Deleted the MetaSets information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached MetaSets entity
	 * @returns returns true if the MetaSets is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteMetaSetsInfo(MetaSets metaSetsEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(metaSetsEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_META_SETS_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the MetaSets information from the database based on supplied MetaSetsID. It first loads the MetaSets then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the MetaSetsId of the MetaSets entity
	 * @returns returns true if the MetaSets is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteMetaSetsInfoById(Integer metaSetsId) throws GenericException {
		MetaSets metaSetsEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			metaSetsEntity=loadMetaSetsInfo(metaSetsId);
			result=super.delete(metaSetsEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_META_SETS_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the MetaSets information from the database based on supplied MetaSetsID.
	 * @author Amar_Deep_Singh
	 * @param takes the MetaSetsId of the MetaSets entity
	 * @returns returns MetaSets entity if the MetaSets is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public MetaSets loadMetaSetsInfo(Integer metaSetsId) throws GenericException {
		MetaSets metaSetsEntity;
		try{
			Session session=sessionFactory.openSession();
			metaSetsEntity=super.find(metaSetsId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_META_SETS_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return metaSetsEntity;
	}

	@Override
	/**
	 * Retrieve the list of all MetaSets entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<MetaSets> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<MetaSets> loadAllMetaSets() throws GenericException {
		List<MetaSets> metaSets=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(MetaSets.class);
			metaSets=(List<MetaSets>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return metaSets;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
