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
import com.pikefin.businessobjects.Splits;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.SplitsDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to Splits operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class SplitsDaoImpl extends AbstractDao<Splits> implements SplitsDao {
	@Autowired
	private SessionFactory sessionFactory;
	public SplitsDaoImpl(SessionFactory sessionFactory) {
		 super(Splits.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public SplitsDaoImpl() {
		 super(Splits.class);
	    }
	@Override
	/**
	 * Saves the Splits information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Splits entity
	 * @returns returns the persisted Splits instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Splits saveSplitsInfo(Splits splitEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			splitEntity=super.save(splitEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_SPLITS_DATA,e.getMessage(),e.getCause());
		}
		return splitEntity;
	}

	
	/**
	 * Updates the Splits information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Splits entity
	 * @returns returns the persisted Splits instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Splits updateSplitsInfo(Splits splitEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(splitEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_SPLITS_DATA,e.getMessage(),e.getCause());
		}
		return splitEntity;
	}

	@Override
	/**
	 * Deleted the Splits information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Splits entity
	 * @returns returns true if the Splits is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteSplitsInfo(Splits splitEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(splitEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_SPLITS_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the Splits information from the database based on supplied SplitsID. It first loads the Splits then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the SplitsId of the Splits entity
	 * @returns returns true if the Splits is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteSplitsInfoById(Integer splitsId) throws GenericException {
		Splits splitEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			splitEntity=loadSplitsInfo(splitsId);
			result=super.delete(splitEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_SPLITS_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the Splits information from the database based on supplied SplitsID.
	 * @author Amar_Deep_Singh
	 * @param takes the SplitsId of the Splits entity
	 * @returns returns Splits entity if the Splits is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Splits loadSplitsInfo(Integer splitsId) throws GenericException {
		Splits splitEntity;
		try{
			Session session=sessionFactory.openSession();
			splitEntity=super.find(splitsId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_SPLITS_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return splitEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Splits entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Splits> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Splits> loadAllSplits() throws GenericException {
		List<Splits> splits=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(Splits.class);
			splits=(List<Splits>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return splits;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
