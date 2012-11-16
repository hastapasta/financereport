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
import com.pikefin.businessobjects.ExtractTable;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.ExtractTableDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to ExtractTable operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class ExtractTableDaoImpl extends AbstractDao<ExtractTable> implements ExtractTableDao {
	@Autowired
	private SessionFactory sessionFactory;
	public ExtractTableDaoImpl(SessionFactory sessionFactory) {
		 super(ExtractTable.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public ExtractTableDaoImpl() {
		 super(ExtractTable.class);
	    }
	@Override
	/**
	 * Saves the ExtractTable information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the ExtractTable entity
	 * @returns returns the persisted ExtractTable instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public ExtractTable saveExtractTableInfo(ExtractTable extractTableEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			extractTableEntity=super.save(extractTableEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_EXTRACT_TABLE_DATA,e.getMessage(),e.getCause());
		}
		return extractTableEntity;
	}

	
	/**
	 * Updates the ExtractTable information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached ExtractTable entity
	 * @returns returns the persisted ExtractTable instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ExtractTable updateExtractTableInfo(ExtractTable extractTableEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(extractTableEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_EXTRACT_TABLE_DATA,e.getMessage(),e.getCause());
		}
		return extractTableEntity;
	}

	@Override
	/**
	 * Deleted the ExtractTable information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached ExtractTable entity
	 * @returns returns true if the ExtractTable is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteExtractTableInfo(ExtractTable extractTableEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(extractTableEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_EXTRACT_TABLE_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the ExtractTable information from the database based on supplied ExtractTableID. It first loads the ExtractTable then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the ExtractTableId of the ExtractTable entity
	 * @returns returns true if the ExtractTable is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteExtractTableInfoById(Integer extractTableId) throws GenericException {
		ExtractTable extractTableEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			extractTableEntity=loadExtractTableInfo(extractTableId);
			result=super.delete(extractTableEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_EXTRACT_TABLE_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the ExtractTable information from the database based on supplied ExtractTableID.
	 * @author Amar_Deep_Singh
	 * @param takes the ExtractTableId of the ExtractTable entity
	 * @returns returns ExtractTable entity if the ExtractTable is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public ExtractTable loadExtractTableInfo(Integer extractTableId) throws GenericException {
		ExtractTable extractTableEntity;
		try{
			Session session=getOpenSession();
			extractTableEntity=super.find(extractTableId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_EXTRACT_TABLE_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return extractTableEntity;
	}

	@Override
	/**
	 * Retrieve the list of all ExtractTable entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<ExtractTable> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<ExtractTable> loadAllExtractTables() throws GenericException {
		List<ExtractTable> columns=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(ExtractTable.class);
			columns=(List<ExtractTable>)criteria.list();
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
