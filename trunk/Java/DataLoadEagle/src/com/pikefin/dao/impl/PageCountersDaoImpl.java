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
import com.pikefin.businessobjects.PageCounters;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.PageCountersDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to PageCounters operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class PageCountersDaoImpl extends AbstractDao<PageCounters> implements PageCountersDao {
	@Autowired
	private SessionFactory sessionFactory;
	public PageCountersDaoImpl(SessionFactory sessionFactory) {
		 super(PageCounters.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public PageCountersDaoImpl() {
		 super(PageCounters.class);
	    }
	@Override
	/**
	 * Saves the PageCounters information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the PageCounters entity
	 * @returns returns the persisted PageCounters instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public PageCounters savePageCountersInfo(PageCounters pageCountersEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			pageCountersEntity=super.save(pageCountersEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_PAGE_COUNTER_DATA,e.getMessage(),e.getCause());
		}
		return pageCountersEntity;
	}

	
	/**
	 * Updates the PageCounters information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached PageCounters entity
	 * @returns returns the persisted PageCounters instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public PageCounters updatePageCountersInfo(PageCounters pageCountersEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(pageCountersEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_PAGE_COUNTER_DATA,e.getMessage(),e.getCause());
		}
		return pageCountersEntity;
	}

	@Override
	/**
	 * Deleted the PageCounters information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached PageCounters entity
	 * @returns returns true if the PageCounters is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deletePageCountersInfo(PageCounters pageCountersEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(pageCountersEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_PAGE_COUNTER_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the PageCounters information from the database based on supplied PageCountersID. It first loads the PageCounters then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the PageCountersId of the PageCounters entity
	 * @returns returns true if the PageCounters is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deletePageCountersInfoById(Integer pageCountersId) throws GenericException {
		PageCounters pageCountersEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			pageCountersEntity=loadPageCountersInfo(pageCountersId);
			result=super.delete(pageCountersEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_PAGE_COUNTER_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the PageCounters information from the database based on supplied PageCountersID.
	 * @author Amar_Deep_Singh
	 * @param takes the PageCountersId of the PageCounters entity
	 * @returns returns PageCounters entity if the PageCounters is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public PageCounters loadPageCountersInfo(Integer pageCountersId) throws GenericException {
		PageCounters pageCountersEntity;
		try{
			Session session=getOpenSession();
			pageCountersEntity=super.find(pageCountersId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_PAGE_COUNTER_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return pageCountersEntity;
	}

	@Override
	/**
	 * Retrieve the list of all PageCounters entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<PageCounters> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<PageCounters> loadAllPageCounters() throws GenericException {
		List<PageCounters> pageCounters=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(PageCounters.class);
			pageCounters=(List<PageCounters>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return pageCounters;
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
