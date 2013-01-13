package com.pikefin.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.Alert;
import com.pikefin.businessobjects.Entity;
import com.pikefin.businessobjects.EntityGroup;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.EntityGroupDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to column operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class EntityGroupDaoImpl extends AbstractDao<EntityGroup> implements EntityGroupDao {
	@Autowired
	private SessionFactory sessionFactory;
	public EntityGroupDaoImpl(SessionFactory sessionFactory) {
		 super(EntityGroup.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public EntityGroupDaoImpl() {
		 super(EntityGroup.class);
	    }
	@Override
	/**
	 * Saves the EntityGroup information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the EntityGroup entity
	 * @returns returns the persisted EntityGroup instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public EntityGroup saveEntityGroupInfo(EntityGroup entityGroupEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			entityGroupEntity=super.save(entityGroupEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_ENTITY_GROUP_DATA,e.getMessage(),e.getCause());
		}
		return entityGroupEntity;
	}

	
	/**
	 * Updates the EntityGroup information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached EntityGroup entity
	 * @returns returns the persisted EntityGroup instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public EntityGroup updateEntityGroupInfo(EntityGroup entityGroupEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(entityGroupEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_ENTITY_GROUP_DATA,e.getMessage(),e.getCause());
		}
		return entityGroupEntity;
	}

	@Override
	/**
	 * Deleted the EntityGroup information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached EntityGroup entity
	 * @returns returns true if the EntityGroup is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteEntityGroupInfo(EntityGroup entityGroupEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(entityGroupEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_ENTITY_GROUP_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the EntityGroup information from the database based on supplied EntityGroupID. It first loads the EntityGroup then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the EntityGroupId of the EntityGroup entity
	 * @returns returns true if the EntityGroup is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteEntityGroupInfoById(Integer groupId) throws GenericException {
		EntityGroup entityGroupEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			entityGroupEntity=loadEntityGroupInfo(groupId);
			result=super.delete(entityGroupEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_ENTITY_GROUP_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the EntityGroup information from the database based on supplied EntityGroupID.
	 * @author Amar_Deep_Singh
	 * @param takes the EntityGroupId of the EntityGroup entity
	 * @returns returns EntityGroup entity if the EntityGroup is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public EntityGroup loadEntityGroupInfo(Integer groupId) throws GenericException {
		EntityGroup entityGroupEntity;
		try{
			Session session=getOpenSession();
			entityGroupEntity=super.find(groupId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_ENTITY_GROUP_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return entityGroupEntity;
	}

	@Override
	/**
	 * Retrieve the list of all EntityGroup entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<EntityGroup> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<EntityGroup> loadAllEntityGroups() throws GenericException {
		List<EntityGroup> groups=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(EntityGroup.class);
			groups=(List<EntityGroup>)criteria.list();
			
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return groups;
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

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<EntityGroup> loadEntityGroupsByEntity(Entity entity)
			throws GenericException {
		List<EntityGroup> groups=null;
		try{
			Session session=getOpenSession();
			Query query=session.createQuery("select eg from EntityGroup eg inner join eg.entities e where e.entityId="+entity.getEntityId()+" and eg.entityGroupId in(1,3,4,5,1008)");
			groups=(List<EntityGroup>)query.list();
			
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return groups;
	}

}
