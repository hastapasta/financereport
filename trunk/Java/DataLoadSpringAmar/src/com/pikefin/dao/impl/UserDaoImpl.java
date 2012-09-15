package com.pikefin.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.User;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.UserDao;
import com.pikefin.exceptions.GenericException;

public class UserDaoImpl  extends AbstractDao<User> implements UserDao {
	
	private SessionFactory sessionFactory;
	public UserDaoImpl(SessionFactory sessionFactory) {
        super(User.class);
        this.sessionFactory=sessionFactory;
    }
	@Override
	/**
	 * Saves the user information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the User entity
	 * @returns returns the persisted User instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public User saveUserInfo(User userEntity) throws GenericException {
		try{
			userEntity=super.save(userEntity);
			
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_SAVE_USER_DATA,e.getMessage(),e.getCause());
		}
		return userEntity;
	}

	@Override
	/**
	 * Updates the user information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached User entity
	 * @returns returns the persisted User instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public User updateUserInfo(User userEntity) throws GenericException {
try{
	userEntity=super.update(userEntity);
	
}catch (Exception e) {
	throw new GenericException(ErrorCode.COULD_NOT_UPDATE_USER_DATA,e.getMessage(),e.getCause());
}
return userEntity;
	}

	@Override
	/**
	 * Deleted the user information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached User entity
	 * @returns returns true if the user is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteUserInfo(User userEntity) throws GenericException {
		try{
			return super.delete(userEntity);
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_UPDATE_USER_DATA,e.getMessage(),e.getCause());
		}
	
	}

	@Override
	/**
	 * Deleted the user information from the database based on supplied userID. It first loads the user then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the userId of the User entity
	 * @returns returns true if the user is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteUserInfoById(Integer userId) throws GenericException {
		User userEntity=loadUserInfo(userId);
		try{
			super.delete(userEntity);
			
		}catch (Exception e) {
		throw new GenericException(ErrorCode.COULD_NOT_DELETE_USER_INFORMATION,e.getMessage(),e.getCause());
		}
		return false;
	}

	@Override
	/**
	 * Retrieve the user information from the database based on supplied userID.
	 * @author Amar_Deep_Singh
	 * @param takes the userId of the User entity
	 * @returns returns User entity if the user is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public User loadUserInfo(Integer userId) throws GenericException {
		User userEntity;
		try{
			userEntity=super.find(userId);
			
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_USER_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return userEntity;
	}

	@Override
	/**
	 * Retrieve the user information from the database based on supplied userName.
	 * @author Amar_Deep_Singh
	 * @param takes the userId of the User entity
	 * @returns returns User entity if the user is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public User loadUserInfoByUserName(String userName) throws GenericException {
		User users=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(User.class);
			criteria.add(Restrictions.eq("username", userName.trim()));
			users=(User)criteria.uniqueResult();
			
		}catch (HibernateException e) {
			throw new GenericException(ErrorCode.MULTIPLE_USERS_FOUND_FOR_USER_NAME,e.getMessage() , e.getCause());
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		if(users==null){
			throw new GenericException(ErrorCode.NO_USER_FOUND_FOR_GIVEN_USER_NAME,null,null);
		}
		return users;
	}

	@Override
	/**
	 * Retrieve the list of all user entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<User> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<User> loadAllUsers() throws GenericException {
		List<User> users=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(User.class);
			users=(List<User>)criteria.list();
			
		}catch (HibernateException e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return users;
	}

	@Override
	/**
	 * Retrieve the list of all user entities from the database, which have the same account email
	 * @author Amar_Deep_Singh
	 * @returns returns List<User> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<User> loadAllUsersByAccountEmail(String accountEmail) throws GenericException {
		List<User> users=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(User.class);
			criteria.add(Restrictions.eq("accountEmail", accountEmail.trim()));
			users=(List<User>)criteria.list();
		}catch (HibernateException e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return users;
	}

	@Override
	/**
	 * Return the session factory instance
	 */
	public SessionFactory getSessionFactory() {
			return this.sessionFactory;
	}

}
