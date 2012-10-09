package com.pikefin.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.Constants;
import com.pikefin.ErrorCode;
import com.pikefin.RepeatTypeEnum;
import com.pikefin.businessobjects.RepeatType;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.RepeatTypeDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to RepeatType operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class RepeatTypeDaoImpl extends AbstractDao<RepeatType> implements RepeatTypeDao {
	@Autowired
	private SessionFactory sessionFactory;
	public RepeatTypeDaoImpl(SessionFactory sessionFactory) {
		 super(RepeatType.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public RepeatTypeDaoImpl() {
		 super(RepeatType.class);
	    }
	@Override
	/**
	 * Saves the RepeatType information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the RepeatType entity
	 * @returns returns the persisted RepeatType instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public RepeatType saveRepeatTypeInfo(RepeatType repeatTypeEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			repeatTypeEntity=super.save(repeatTypeEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_REPEAT_TYPE_DATA,e.getMessage(),e.getCause());
		}
		return repeatTypeEntity;
	}

	
	/**
	 * Updates the RepeatType information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached RepeatType entity
	 * @returns returns the persisted RepeatType instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public RepeatType updateRepeatTypeInfo(RepeatType repeatTypeEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(repeatTypeEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_REPEAT_TYPE_DATA,e.getMessage(),e.getCause());
		}
		return repeatTypeEntity;
	}

	@Override
	/**
	 * Deleted the RepeatType information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached RepeatType entity
	 * @returns returns true if the RepeatType is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteRepeatTypeInfo(RepeatType repeatTypeEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(repeatTypeEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_REPEAT_TYPE_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the RepeatType information from the database based on supplied RepeatTypeID. It first loads the RepeatType then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the RepeatTypeId of the RepeatType entity
	 * @returns returns true if the RepeatType is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteRepeatTypeInfoById(Integer repeatTypeId) throws GenericException {
		RepeatType repeatTypeEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			repeatTypeEntity=loadRepeatTypeInfo(repeatTypeId);
			result=super.delete(repeatTypeEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_REPEAT_TYPE_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the RepeatType information from the database based on supplied RepeatTypeID.
	 * @author Amar_Deep_Singh
	 * @param takes the RepeatTypeId of the RepeatType entity
	 * @returns returns RepeatType entity if the RepeatType is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public RepeatType loadRepeatTypeInfo(Integer repeatTypeId) throws GenericException {
		RepeatType repeatTypeEntity;
		try{
			Session session=sessionFactory.openSession();
			repeatTypeEntity=super.find(repeatTypeId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REPEAT_TYPE_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return repeatTypeEntity;
	}

	@Override
	/**
	 * Retrieve the list of all RepeatType entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<RepeatType> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<RepeatType> loadAllRepeatTypes() throws GenericException {
		List<RepeatType> repeatTypes=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(RepeatType.class);
			repeatTypes=(List<RepeatType>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return repeatTypes;
	}
	
	@Override
	public List<RepeatType> loadAllActiveRepeatTypes() throws GenericException {
		
		List<RepeatType> repeatTypes=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(RepeatType.class);
			criteria.add(Restrictions.ne("type", RepeatTypeEnum.NONE));
			repeatTypes=(List<RepeatType>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return repeatTypes;
	}

	@Override
	public List<RepeatType> loadRepeatTypesByType(RepeatTypeEnum repeatType)
			throws GenericException {
		List<RepeatType> repeatTypes=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(RepeatType.class);
			criteria.add(Restrictions.eq("type", repeatType.toString()));
			repeatTypes=(List<RepeatType>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return repeatTypes;
	}
	@Override
	@Transactional
	public List<RepeatType> updateRepeatTypeBatch(List<RepeatType> batchEntities)
			throws GenericException {
		Session session=sessionFactory.openSession();
			return super.batchUpdate(batchEntities, Constants.BATCH_SIZE);
	}
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	

	

}
