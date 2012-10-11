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
import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.User;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.UserDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to country operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
	@Autowired
	private SessionFactory sessionFactory;
	public UserDaoImpl(SessionFactory sessionFactory) {
		 super(User.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public UserDaoImpl() {
		 super(User.class);
	    }
	@Override
	/**
	 * Saves the User information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the User entity
	 * @returns returns the persisted User instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public User saveUserInfo(User userEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			userEntity=super.save(userEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_USER_DATA,e.getMessage(),e.getCause());
		}
		return userEntity;
	}

	
	/**
	 * Updates the User information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached User entity
	 * @returns returns the persisted User instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public User updateUserInfo(User userEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(userEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_USER_DATA,e.getMessage(),e.getCause());
		}
		return userEntity;
	}

	@Override
	/**
	 * Deleted the User information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached User entity
	 * @returns returns true if the User is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteUserInfo(User userEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(userEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_USER_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the User information from the database based on supplied UserID. It first loads the User then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the UserId of the User entity
	 * @returns returns true if the User is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteUserInfoById(Integer userId) throws GenericException {
		User userEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			userEntity=loadUserInfo(userId);
			result=super.delete(userEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_USER_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the User information from the database based on supplied UserID.
	 * @author Amar_Deep_Singh
	 * @param takes the UserId of the User entity
	 * @returns returns User entity if the User is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public User loadUserInfo(Integer userId) throws GenericException {
		User userEntity;
		try{
			Session session=getOpenSession();
			userEntity=super.find(userId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_USER_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return userEntity;
	}

	@Override
	/**
	 * Retrieve the list of all User entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<User> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<User> loadAllUsers() throws GenericException {
		List<User> users=null;
		try{
			Session session=getOpenSession();
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
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public User loadUserInfoByUserName(String userName) throws GenericException {
		Session session;
		User user=null;
		try{
			session=getOpenSession();
			Criteria criteria=session.createCriteria(User.class);
			criteria.add(Restrictions.eq("username", userName.trim()));
			user=(User)criteria.uniqueResult();
		}catch(HibernateException ex){
			throw new GenericException(ErrorCode.MULTIPLE_USERS_FOUND_FOR_USER_NAME,ex.getMessage(),ex.getCause());
		}
		catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_USER_DATA_WITH_ID,e.getMessage(),e.getCause());
		
		}
		if(user==null){
			throw new GenericException(ErrorCode.NO_USER_FOUND_FOR_GIVEN_USER_NAME,"NO USERnAME FOUND",null);
		}
		return user;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<User> loadAllUsersByAccountEmail(String accountEmail)
			throws GenericException {
		Session session;
		List<User> users=null;
		try{
			session=getOpenSession();
			Criteria criteria=session.createCriteria(User.class);
			criteria.add(Restrictions.eq("accountEmail", accountEmail.trim()));
			users=(List<User>)criteria.list();
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage(),e.getCause());
		
		}
		
		return users;
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
