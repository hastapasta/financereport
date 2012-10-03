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
import com.pikefin.businessobjects.Groups;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.GroupDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have method related to group operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class GroupDaoImpl extends AbstractDao<Groups> implements GroupDao {
	@Autowired
	private SessionFactory sessionFactory;
	public GroupDaoImpl(SessionFactory sessionFactory) {
		 super(Groups.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public GroupDaoImpl() {
		 super(Groups.class);
	    }
	@Override
	/**
	 * Saves the Group information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Group entity
	 * @returns returns the persisted Group instance
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={GenericException.class,ArithmeticException.class})
	public Groups saveGroupInfo(Groups groupEntity) throws GenericException {
		Session session;
		try{
			 session=sessionFactory.openSession();
			 groupEntity=super.save(groupEntity);
		}catch (HibernateException e) {
			throw new GenericException(ErrorCode.COULD_NOT_SAVE_GROUP_DATA,e.getMessage(),e.getCause());
		}
			return groupEntity;
	}

	@Override
	/**
	 * Updates the Groups information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Groups entity
	 * @returns returns the persisted Groups instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Groups updateGroupInfo(Groups groupEntity) throws GenericException {
		try{
			Session session=sessionFactory.openSession();
			super.update(groupEntity);
		}catch (HibernateException e) {
			throw new GenericException(ErrorCode.COULD_NOT_UPDATE_GROUP_DATA,e.getMessage(),e.getCause());
		}
		return groupEntity;
	}

	@Override
	/**
	 * Deleted the Groups information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Groups entity
	 * @returns returns true if the Groups is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteGroupInfo(Groups groupEntity) throws GenericException {
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			result= super.delete(groupEntity);
		   }catch (Exception e) {
			   throw new GenericException(ErrorCode.COULD_NOT_DELETE_GROUP_INFORMATION,e.getMessage(),e.getCause());
		}
			return result;
	}

	@Override
	/**
	 * Deleted the Group information from the database based on supplied GroupID. It first loads the Group then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the GroupId of the Group entity
	 * @returns returns true if the Group is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteGroupInfoById(Integer groupId) throws GenericException {
		Groups groupEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			groupEntity=loadGroupInfo(groupId);
			result=super.delete(groupEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_GROUP_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the Group information from the database based on supplied GroupID.
	 * @author Amar_Deep_Singh
	 * @param takes the GroupId of the Group entity
	 * @returns returns Group entity if the Group is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Groups loadGroupInfo(Integer groupId) throws GenericException {
		Groups groupEntity;
		try{
			Session session=sessionFactory.openSession();
			groupEntity=super.find(groupId);
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_GROUP_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
			return groupEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Group entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Groups> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Groups> loadAllGroups() throws GenericException {
		List<Groups> groups=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(Groups.class);
			groups=(List<Groups>)criteria.list();
			
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

}
