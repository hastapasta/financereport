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
import com.pikefin.businessobjects.Column;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.ColumnDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to Column operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class ColumnDaoImpl extends AbstractDao<Column> implements ColumnDao {
	@Autowired
	private SessionFactory sessionFactory;
	public ColumnDaoImpl(SessionFactory sessionFactory) {
		 super(Column.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public ColumnDaoImpl() {
		 super(Column.class);
	    }
	@Override
	/**
	 * Saves the Column information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Column entity
	 * @returns returns the persisted Column instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Column saveColumnInfo(Column columnEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			columnEntity=super.save(columnEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_COLUMN_DATA,e.getMessage(),e.getCause());
		}
		return columnEntity;
	}

	
	/**
	 * Updates the Column information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Column entity
	 * @returns returns the persisted Column instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Column updateColumnInfo(Column columnEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(columnEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_COLUMN_DATA,e.getMessage(),e.getCause());
		}
		return columnEntity;
	}

	@Override
	/**
	 * Deleted the Column information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Column entity
	 * @returns returns true if the Column is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteColumnInfo(Column columnEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(columnEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_COLUMN_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the Column information from the database based on supplied ColumnID. It first loads the Column then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the ColumnId of the Column entity
	 * @returns returns true if the Column is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteColumnInfoById(Integer columnId) throws GenericException {
		Column columnEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			columnEntity=loadColumnInfo(columnId);
			result=super.delete(columnEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_COLUMN_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the Column information from the database based on supplied ColumnID.
	 * @author Amar_Deep_Singh
	 * @param takes the ColumnId of the Column entity
	 * @returns returns Column entity if the Column is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Column loadColumnInfo(Integer columnId) throws GenericException {
		Column columnEntity;
		try{
			Session session=getOpenSession();
			columnEntity=super.find(columnId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_COLUMN_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return columnEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Column entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Column> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Column> loadAllColumns() throws GenericException {
		List<Column> columns=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(Column.class);
			columns=(List<Column>)criteria.list();
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
