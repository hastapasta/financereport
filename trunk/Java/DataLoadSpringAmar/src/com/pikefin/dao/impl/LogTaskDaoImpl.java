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
import com.pikefin.businessobjects.LogTask;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.LogTaskDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to LogTask operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class LogTaskDaoImpl extends AbstractDao<LogTask> implements LogTaskDao {
	@Autowired
	private SessionFactory sessionFactory;
	public LogTaskDaoImpl(SessionFactory sessionFactory) {
		 super(LogTask.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public LogTaskDaoImpl() {
		 super(LogTask.class);
	    }
	@Override
	/**
	 * Saves the LogTask information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the LogTask entity
	 * @returns returns the persisted LogTask instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public LogTask saveLogTaskInfo(LogTask logTaskEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			logTaskEntity=super.save(logTaskEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_LOG_TASK_DATA,e.getMessage(),e.getCause());
		}
		return logTaskEntity;
	}

	
	/**
	 * Updates the LogTask information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached LogTask entity
	 * @returns returns the persisted LogTask instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public LogTask updateLogTaskInfo(LogTask logTaskEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(logTaskEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_LOG_TASK_DATA,e.getMessage(),e.getCause());
		}
		return logTaskEntity;
	}

	@Override
	/**
	 * Deleted the LogTask information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached LogTask entity
	 * @returns returns true if the LogTask is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteLogTaskInfo(LogTask logTaskEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(logTaskEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_LOG_TASK_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the LogTask information from the database based on supplied LogTaskID. It first loads the LogTask then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the LogTaskId of the LogTask entity
	 * @returns returns true if the LogTask is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteLogTaskInfoById(Integer logTaskId) throws GenericException {
		LogTask logTaskEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			logTaskEntity=loadLogTaskInfo(logTaskId);
			result=super.delete(logTaskEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_LOG_TASK_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the LogTask information from the database based on supplied LogTaskID.
	 * @author Amar_Deep_Singh
	 * @param takes the LogTaskId of the LogTask entity
	 * @returns returns LogTask entity if the LogTask is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public LogTask loadLogTaskInfo(Integer logTaskId) throws GenericException {
		LogTask logTaskEntity;
		try{
			Session session=sessionFactory.openSession();
			logTaskEntity=super.find(logTaskId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_LOG_TASK_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return logTaskEntity;
	}

	@Override
	/**
	 * Retrieve the list of all LogTask entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<LogTask> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<LogTask> loadAllLogTasks() throws GenericException {
		List<LogTask> logTasks=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(LogTask.class);
			logTasks=(List<LogTask>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return logTasks;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
