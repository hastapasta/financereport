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
import com.pikefin.businessobjects.Entity;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.EntityDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to entity operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class EntityDaoImpl extends AbstractDao<Entity> implements EntityDao {
	@Autowired
	private SessionFactory sessionFactory;
	public EntityDaoImpl(SessionFactory sessionFactory) {
		 super(Entity.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public EntityDaoImpl() {
		 super(Entity.class);
	    }
	@Override
	/**
	 * Saves the Entity information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Entity entity
	 * @returns returns the persisted Entity instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Entity saveEntityInfo(Entity entityEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			entityEntity=super.save(entityEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_ENTITY_DATA,e.getMessage(),e.getCause());
		}
		return entityEntity;
	}

	
	/**
	 * Updates the Entity information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Entity entity
	 * @returns returns the persisted Entity instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Entity updateEntityInfo(Entity entityEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(entityEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_ENTITY_DATA,e.getMessage(),e.getCause());
		}
		return entityEntity;
	}

	@Override
	/**
	 * Deleted the Entity information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Entity entity
	 * @returns returns true if the Entity is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteEntityInfo(Entity entityEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(entityEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_ENTITY_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the Entity information from the database based on supplied EntityID. It first loads the Entity then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the EntityId of the Entity entity
	 * @returns returns true if the Entity is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteEntityInfoById(Integer groupId) throws GenericException {
		Entity entityEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			entityEntity=loadEntityInfo(groupId);
			result=super.delete(entityEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_ENTITY_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the Entity information from the database based on supplied EntityID.
	 * @author Amar_Deep_Singh
	 * @param takes the EntityId of the Entity entity
	 * @returns returns Entity entity if the Entity is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Entity loadEntityInfo(Integer groupId) throws GenericException {
		Entity entityEntity;
		try{
			Session session=getOpenSession();
			entityEntity=super.find(groupId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_ENTITY_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return entityEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Entity entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Entity> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Entity> loadAllEntities() throws GenericException {
		List<Entity> entities=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(Entity.class);
			entities=(List<Entity>)criteria.list();
			
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return entities;
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
