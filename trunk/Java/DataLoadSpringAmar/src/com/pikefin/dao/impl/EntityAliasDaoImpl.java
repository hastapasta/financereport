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
import com.pikefin.businessobjects.EntityAlias;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.EntityAliasDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to EntityAlias operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class EntityAliasDaoImpl extends AbstractDao<EntityAlias> implements EntityAliasDao {
	@Autowired
	private SessionFactory sessionFactory;
	public EntityAliasDaoImpl(SessionFactory sessionFactory) {
		 super(EntityAlias.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public EntityAliasDaoImpl() {
		 super(EntityAlias.class);
	    }
	@Override
	/**
	 * Saves the EntityAlias information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the EntityAlias entity
	 * @returns returns the persisted EntityAlias instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public EntityAlias saveEntityAliasInfo(EntityAlias entityAliasEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			entityAliasEntity=super.save(entityAliasEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_ENTITY_ALIAS_DATA,e.getMessage(),e.getCause());
		}
		return entityAliasEntity;
	}

	
	/**
	 * Updates the EntityAlias information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached EntityAlias entity
	 * @returns returns the persisted EntityAlias instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public EntityAlias updateEntityAliasInfo(EntityAlias entityAliasEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(entityAliasEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_ENTITY_ALIAS_DATA,e.getMessage(),e.getCause());
		}
		return entityAliasEntity;
	}

	@Override
	/**
	 * Deleted the EntityAlias information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached EntityAlias entity
	 * @returns returns true if the EntityAlias is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteEntityAliasInfo(EntityAlias entityAliasEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(entityAliasEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_ENTITY_ALIAS_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the EntityAlias information from the database based on supplied EntityAliasID. It first loads the EntityAlias then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the EntityAliasId of the EntityAlias entity
	 * @returns returns true if the EntityAlias is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteEntityAliasInfoById(Integer entityAliasId) throws GenericException {
		EntityAlias entityAliasEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			entityAliasEntity=loadEntityAliasInfo(entityAliasId);
			result=super.delete(entityAliasEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_ENTITY_ALIAS_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the EntityAlias information from the database based on supplied EntityAliasID.
	 * @author Amar_Deep_Singh
	 * @param takes the EntityAliasId of the EntityAlias entity
	 * @returns returns EntityAlias entity if the EntityAlias is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public EntityAlias loadEntityAliasInfo(Integer entityAliasId) throws GenericException {
		EntityAlias entityAliasEntity;
		try{
			Session session=sessionFactory.openSession();
			entityAliasEntity=super.find(entityAliasId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_ENTITY_ALIAS_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return entityAliasEntity;
	}

	@Override
	/**
	 * Retrieve the list of all EntityAlias entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<EntityAlias> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<EntityAlias> loadAllEntityAlias() throws GenericException {
		List<EntityAlias> columns=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(EntityAlias.class);
			columns=(List<EntityAlias>)criteria.list();
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

}
