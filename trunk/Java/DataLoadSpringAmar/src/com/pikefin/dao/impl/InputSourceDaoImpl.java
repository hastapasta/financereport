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
import com.pikefin.businessobjects.InputSource;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.InputSourceDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to InputSource operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class InputSourceDaoImpl extends AbstractDao<InputSource> implements InputSourceDao {
	@Autowired
	private SessionFactory sessionFactory;
	public InputSourceDaoImpl(SessionFactory sessionFactory) {
		 super(InputSource.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public InputSourceDaoImpl() {
		 super(InputSource.class);
	    }
	@Override
	/**
	 * Saves the InputSource information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the InputSource entity
	 * @returns returns the persisted InputSource instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public InputSource saveInputSourceInfo(InputSource inputSourceEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			inputSourceEntity=super.save(inputSourceEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_INPUT_SOURCE_DATA,e.getMessage(),e.getCause());
		}
		return inputSourceEntity;
	}

	
	/**
	 * Updates the InputSource information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached InputSource entity
	 * @returns returns the persisted InputSource instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public InputSource updateInputSourceInfo(InputSource inputSourceEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(inputSourceEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_INPUT_SOURCE_DATA,e.getMessage(),e.getCause());
		}
		return inputSourceEntity;
	}

	@Override
	/**
	 * Deleted the InputSource information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached InputSource entity
	 * @returns returns true if the InputSource is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteInputSourceInfo(InputSource inputSourceEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(inputSourceEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_INPUT_SOURCE_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the InputSource information from the database based on supplied InputSourceID. It first loads the InputSource then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the InputSourceId of the InputSource entity
	 * @returns returns true if the InputSource is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteInputSourceInfoById(Integer inputSourceId) throws GenericException {
		InputSource inputSourceEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			inputSourceEntity=loadInputSourceInfo(inputSourceId);
			result=super.delete(inputSourceEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_INPUT_SOURCE_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the InputSource information from the database based on supplied InputSourceID.
	 * @author Amar_Deep_Singh
	 * @param takes the InputSourceId of the InputSource entity
	 * @returns returns InputSource entity if the InputSource is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public InputSource loadInputSourceInfo(Integer inputSourceId) throws GenericException {
		InputSource inputSourceEntity;
		try{
			Session session=getOpenSession();
			inputSourceEntity=super.find(inputSourceId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_INPUT_SOURCE_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return inputSourceEntity;
	}

	@Override
	/**
	 * Retrieve the list of all InputSource entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<InputSource> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<InputSource> loadAllInputSources() throws GenericException {
		List<InputSource> inputSources=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(InputSource.class);
			inputSources=(List<InputSource>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return inputSources;
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
