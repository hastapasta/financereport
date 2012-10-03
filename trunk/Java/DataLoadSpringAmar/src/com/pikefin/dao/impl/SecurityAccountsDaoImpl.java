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
import com.pikefin.businessobjects.SecurityAccounts;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.SecurityAccountsDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to SecurityAccounts operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class SecurityAccountsDaoImpl extends AbstractDao<SecurityAccounts> implements SecurityAccountsDao {
	@Autowired
	private SessionFactory sessionFactory;
	public SecurityAccountsDaoImpl(SessionFactory sessionFactory) {
		 super(SecurityAccounts.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public SecurityAccountsDaoImpl() {
		 super(SecurityAccounts.class);
	    }
	@Override
	/**
	 * Saves the SecurityAccounts information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the SecurityAccounts entity
	 * @returns returns the persisted SecurityAccounts instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public SecurityAccounts saveSecurityAccountsInfo(SecurityAccounts securityAccountEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			securityAccountEntity=super.save(securityAccountEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_SECURITY_ACCOUNT_DATA,e.getMessage(),e.getCause());
		}
		return securityAccountEntity;
	}

	
	/**
	 * Updates the SecurityAccounts information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached SecurityAccounts entity
	 * @returns returns the persisted SecurityAccounts instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public SecurityAccounts updateSecurityAccountsInfo(SecurityAccounts securityAccountEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(securityAccountEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_SECURITY_ACCOUNT_DATA,e.getMessage(),e.getCause());
		}
		return securityAccountEntity;
	}

	@Override
	/**
	 * Deleted the SecurityAccounts information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached SecurityAccounts entity
	 * @returns returns true if the SecurityAccounts is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteSecurityAccountsInfo(SecurityAccounts securityAccountEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(securityAccountEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_SECURITY_ACCOUNT_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the SecurityAccounts information from the database based on supplied SecurityAccountsID. It first loads the SecurityAccounts then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the SecurityAccountsId of the SecurityAccounts entity
	 * @returns returns true if the SecurityAccounts is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteSecurityAccountsInfoById(Integer securityAccountId) throws GenericException {
		SecurityAccounts securityAccountEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			securityAccountEntity=loadSecurityAccountsInfo(securityAccountId);
			result=super.delete(securityAccountEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_SECURITY_ACCOUNT_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the SecurityAccounts information from the database based on supplied SecurityAccountsID.
	 * @author Amar_Deep_Singh
	 * @param takes the SecurityAccountsId of the SecurityAccounts entity
	 * @returns returns SecurityAccounts entity if the SecurityAccounts is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public SecurityAccounts loadSecurityAccountsInfo(Integer securityAccountId) throws GenericException {
		SecurityAccounts securityAccountEntity;
		try{
			Session session=sessionFactory.openSession();
			securityAccountEntity=super.find(securityAccountId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_SECURITY_ACCOUNT_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return securityAccountEntity;
	}

	@Override
	/**
	 * Retrieve the list of all SecurityAccounts entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<SecurityAccounts> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<SecurityAccounts> loadAllSecurityAccounts() throws GenericException {
		List<SecurityAccounts> securityAccounts=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(SecurityAccounts.class);
			securityAccounts=(List<SecurityAccounts>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return securityAccounts;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
