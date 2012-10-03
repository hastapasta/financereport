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
import com.pikefin.businessobjects.Batches;
import com.pikefin.businessobjects.Country;
import com.pikefin.businessobjects.Groups;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.BatchesDao;
import com.pikefin.exceptions.GenericException;
/**
 * This Class contains the all DAO operations related to Batches business entity
 * @author Amar_Deep_Singh
 *
 */
@Component
public class BatchesDaoImpl extends AbstractDao<Batches> implements BatchesDao {
	@Autowired
	private SessionFactory sessionFactory;
	public BatchesDaoImpl(SessionFactory sessionFactory){
		super(Batches.class);
		this.sessionFactory=sessionFactory;
	}
	
	public BatchesDaoImpl() {
		 super(Batches.class);
	    }
	@Override
	/**
	 * Saves the Batches information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Batches entity
	 * @returns returns the persisted Batches instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Batches saveBatchesInfo(Batches batchesEntity)
			throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			batchesEntity=super.save(batchesEntity);
			}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_SAVE_BATCHES_DATA,e.getMessage(),e.getCause());
		}
		return batchesEntity;
	}
	
	/**
	 * Updates the Batches information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Batches entity
	 * @returns returns the persisted Batches instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Batches updateBatchesInfo(Batches batchesEntity)
			throws GenericException {
		try{
			batchesEntity=super.update(batchesEntity);
			
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_UPDATE_BATCHES_DATA,e.getMessage(),e.getCause());
		}
		return batchesEntity;
	}

	@Override
	/**
	 * Deleted the Batches information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Batches entity
	 * @returns returns true if the Batches is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteBatchesInfo(Batches batchesEntity)
			throws GenericException {
		try{
			return super.delete(batchesEntity);
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_BATCHES_INFORMATION,e.getMessage(),e.getCause());
		}
	}

	@Override
	/**
	 * Deleted the Batches information from the database based on supplied BatchesID. It first loads the Batches then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the BatchesId of the Batches entity
	 * @returns returns true if the Batches is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteBatchesInfoById(Integer batchesId)
			throws GenericException {
		Batches groupEntity=loadBatchesInfo(batchesId);
		try{
			super.delete(groupEntity);
			
		}catch (Exception e) {
		throw new GenericException(ErrorCode.COULD_NOT_DELETE_BATCHES_INFORMATION,e.getMessage(),e.getCause());
		}
		return false;
	}

	@Override
	/**
	 * Retrieve the Batches information from the database based on supplied BatchesID.
	 * @author Amar_Deep_Singh
	 * @param takes the BatchesId of the Batches entity
	 * @returns returns Batches entity if the Batches is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Batches loadBatchesInfo(Integer batchesId) throws GenericException {
		Batches batchesEntity;
		try{
			batchesEntity=super.find(batchesId);
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_BATCHES_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return batchesEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Batches entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Batches> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Batches> loadAllBatchess() throws GenericException {
		List<Batches> batches=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(Batches.class);
			batches=(List<Batches>)criteria.list();
			
		}catch (HibernateException e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return batches;
	}
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

}
