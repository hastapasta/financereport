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
import com.pikefin.businessobjects.Task;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.TaskDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have method related to task operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class TaskDaoImpl extends AbstractDao<Task> implements TaskDao {
	@Autowired
	private SessionFactory sessionFactory;
	public TaskDaoImpl(SessionFactory sessionFactory) {
		 super(Task.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public TaskDaoImpl() {
		 super(Task.class);
	    }
	@Override
	/**
	 * Saves the Task information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Task entity
	 * @returns returns the persisted Task instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Task saveTaskInfo(Task taskEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			taskEntity=super.save(taskEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_GROUP_DATA,e.getMessage(),e.getCause());
		}
		return taskEntity;
	}

	@Override
	/**
	 * Updates the Task information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Task entity
	 * @returns returns the persisted Task instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Task updateTaskInfo(Task taskEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			taskEntity=super.update(taskEntity);
		}catch (HibernateException e) {
			throw new GenericException(ErrorCode.COULD_NOT_UPDATE_GROUP_DATA,e.getMessage(),e.getCause());
		}
		return taskEntity;
	}

	@Override
	/**
	 * Deleted the Task information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Task entity
	 * @returns returns true if the Task is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteTaskInfo(Task taskEntity) throws GenericException {
		boolean result;
		Session session;
		try{
			 session=sessionFactory.openSession();
			 result= super.delete(taskEntity);
		
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_GROUP_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Deleted the Task information from the database based on supplied TaskID. It first loads the Task then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the TaskId of the Task entity
	 * @returns returns true if the Task is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteTaskInfoById(Integer taskId) throws GenericException {
		Task taskEntity=null;
		boolean result;
		Session session;
		try{
			session=sessionFactory.openSession();
			taskEntity=loadTaskInfo(taskId);
			result=super.delete(taskEntity);
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_GROUP_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the Task information from the database based on supplied TaskID.
	 * @author Amar_Deep_Singh
	 * @param takes the TaskId of the Task entity
	 * @returns returns Task entity if the Task is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Task loadTaskInfo(Integer taskId) throws GenericException {
		Task taskEntity;
		Session session;
		try{
			session=sessionFactory.openSession();
			taskEntity=super.find(taskId);
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_GROUP_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return taskEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Task entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Task> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Task> loadAllTasks() throws GenericException {
		List<Task> tasks=null;
		Session session;
		try{
			session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(Task.class);
			tasks=(List<Task>)criteria.list();
		}catch (HibernateException e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return tasks;
	}
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
