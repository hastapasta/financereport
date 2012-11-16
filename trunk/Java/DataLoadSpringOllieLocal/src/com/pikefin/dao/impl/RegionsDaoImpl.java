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
import com.pikefin.businessobjects.Regions;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.RegionsDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to Regions operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class RegionsDaoImpl extends AbstractDao<Regions> implements RegionsDao {
	@Autowired
	private SessionFactory sessionFactory;
	public RegionsDaoImpl(SessionFactory sessionFactory) {
		 super(Regions.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public RegionsDaoImpl() {
		 super(Regions.class);
	    }
	@Override
	/**
	 * Saves the Regions information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Regions entity
	 * @returns returns the persisted Regions instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Regions saveRegionsInfo(Regions regionsEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			regionsEntity=super.save(regionsEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_REGION_DATA,e.getMessage(),e.getCause());
		}
		return regionsEntity;
	}

	
	/**
	 * Updates the Regions information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Regions entity
	 * @returns returns the persisted Regions instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Regions updateRegionsInfo(Regions regionsEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(regionsEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_REGION_DATA,e.getMessage(),e.getCause());
		}
		return regionsEntity;
	}

	@Override
	/**
	 * Deleted the Regions information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Regions entity
	 * @returns returns true if the Regions is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteRegionsInfo(Regions regionsEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(regionsEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_REGION_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the Regions information from the database based on supplied RegionsID. It first loads the Regions then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the RegionsId of the Regions entity
	 * @returns returns true if the Regions is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteRegionsInfoById(Integer regionId) throws GenericException {
		Regions regionsEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			regionsEntity=loadRegionsInfo(regionId);
			result=super.delete(regionsEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_REGION_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the Regions information from the database based on supplied RegionsID.
	 * @author Amar_Deep_Singh
	 * @param takes the RegionsId of the Regions entity
	 * @returns returns Regions entity if the Regions is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Regions loadRegionsInfo(Integer regionId) throws GenericException {
		Regions regionsEntity;
		try{
			Session session=getOpenSession();
			regionsEntity=super.find(regionId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REGION_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return regionsEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Regions entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Regions> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Regions> loadAllRegionss() throws GenericException {
		List<Regions> regions=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(Regions.class);
			regions=(List<Regions>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return regions;
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
