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
import com.pikefin.businessobjects.JobQueue;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.JobQueueDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to JobQueue operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class JobQueueDaoImpl extends AbstractDao<JobQueue> implements JobQueueDao {
	@Autowired
	private SessionFactory sessionFactory;
	public JobQueueDaoImpl(SessionFactory sessionFactory) {
		 super(JobQueue.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public JobQueueDaoImpl() {
		 super(JobQueue.class);
	    }
	@Override
	/**
	 * Saves the JobQueue information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the JobQueue entity
	 * @returns returns the persisted JobQueue instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public JobQueue saveJobQueueInfo(JobQueue jobQueueEntity) throws GenericException {
		Session session;
		try{
			session=sessionFactory.openSession();
			jobQueueEntity=super.save(jobQueueEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_JOB_QUEUE_DATA,e.getMessage(),e.getCause());
		}
		return jobQueueEntity;
	}

	
	/**
	 * Updates the JobQueue information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached JobQueue entity
	 * @returns returns the persisted JobQueue instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public JobQueue updateJobQueueInfo(JobQueue jobQueueEntity) throws GenericException {
	
		try{
			Session session=sessionFactory.openSession();
			super.update(jobQueueEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_JOB_QUEUE_DATA,e.getMessage(),e.getCause());
		}
		return jobQueueEntity;
	}

	@Override
	/**
	 * Deleted the JobQueue information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached JobQueue entity
	 * @returns returns true if the JobQueue is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteJobQueueInfo(JobQueue jobQueueEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=sessionFactory.openSession();
		 result= super.delete(jobQueueEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_JOB_QUEUE_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the JobQueue information from the database based on supplied JobQueueID. It first loads the JobQueue then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the JobQueueId of the JobQueue entity
	 * @returns returns true if the JobQueue is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteJobQueueInfoById(Integer jobQueueId) throws GenericException {
		JobQueue jobQueueEntity=null;
		boolean result;
		try{
			Session session=sessionFactory.openSession();
			jobQueueEntity=loadJobQueueInfo(jobQueueId);
			result=super.delete(jobQueueEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_JOB_QUEUE_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the JobQueue information from the database based on supplied JobQueueID.
	 * @author Amar_Deep_Singh
	 * @param takes the JobQueueId of the JobQueue entity
	 * @returns returns JobQueue entity if the JobQueue is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public JobQueue loadJobQueueInfo(Integer jobQueueId) throws GenericException {
		JobQueue jobQueueEntity;
		try{
			Session session=sessionFactory.openSession();
			jobQueueEntity=super.find(jobQueueId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_JOB_QUEUE_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return jobQueueEntity;
	}

	@Override
	/**
	 * Retrieve the list of all JobQueue entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<JobQueue> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<JobQueue> loadAllJobQueues() throws GenericException {
		List<JobQueue> jobQueues=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(JobQueue.class);
			jobQueues=(List<JobQueue>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return jobQueues;
	}
	/**
	 * Delete all JobQueue entities from database
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteAllJobQueues() throws GenericException {
		try{
			Session session=sessionFactory.openSession();
			Query query=session.createQuery("delete from JobQueue c");
		int i=	query.executeUpdate();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_ALL_JOB_QUEUES,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_ALL_JOB_QUEUES,e.getMessage() , e.getCause());
		}
		
	
		
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	

}
