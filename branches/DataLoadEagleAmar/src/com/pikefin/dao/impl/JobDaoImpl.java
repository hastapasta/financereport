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
import com.pikefin.businessobjects.Job;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.JobDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to Job operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class JobDaoImpl extends AbstractDao<Job> implements JobDao {
	@Autowired
	private SessionFactory sessionFactory;
	public JobDaoImpl(SessionFactory sessionFactory) {
		 super(Job.class);
	     this.sessionFactory=sessionFactory;
	}
	
	public JobDaoImpl() {
		 super(Job.class);
	    }
	@Override
	/**
	 * Saves the Job information into the database, it runs into a transaction
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Job entity
	 * @returns returns the persisted Job instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Job saveJobInfo(Job jobEntity) throws GenericException {
		Session session;
		try{
			session=getOpenSession();
			jobEntity=super.save(jobEntity);
		
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_SAVE_JOB_DATA,e.getMessage(),e.getCause());
		}
		return jobEntity;
	}

	
	/**
	 * Updates the Job information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Job entity
	 * @returns returns the persisted Job instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Job updateJobInfo(Job jobEntity) throws GenericException {
	
		try{
			Session session=getOpenSession();
			super.update(jobEntity);
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_UPDATE_JOB_DATA,e.getMessage(),e.getCause());
		}
		return jobEntity;
	}

	@Override
	/**
	 * Deleted the Job information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Job entity
	 * @returns returns true if the Job is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteJobInfo(Job jobEntity) throws GenericException {
		
		boolean result;
		try{
			Session session=getOpenSession();
		 result= super.delete(jobEntity);
		}catch (Exception e) {		
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_JOB_INFORMATION,e.getMessage(),e.getCause());
		}
	
		return result;
	}

	@Override
	/**
	 * Deleted the Job information from the database based on supplied JobID. It first loads the Job then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the JobId of the Job entity
	 * @returns returns true if the Job is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteJobInfoById(Integer jobId) throws GenericException {
		Job jobEntity=null;
		boolean result;
		try{
			Session session=getOpenSession();
			jobEntity=loadJobInfo(jobId);
			result=super.delete(jobEntity);
			
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_DELETE_JOB_INFORMATION,e.getMessage(),e.getCause());
		}
		return result;
	}

	@Override
	/**
	 * Retrieve the Job information from the database based on supplied JobID.
	 * @author Amar_Deep_Singh
	 * @param takes the JobId of the Job entity
	 * @returns returns Job entity if the Job is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Job loadJobInfo(Integer jobId) throws GenericException {
		Job jobEntity;
		try{
			Session session=getOpenSession();
			jobEntity=super.find(jobId);
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_JOB_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return jobEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Job entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Job> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Job> loadAllJobs() throws GenericException {
		List<Job> jobs=null;
		try{
			Session session=getOpenSession();
			Criteria criteria=session.createCriteria(Job.class);
			jobs=(List<Job>)criteria.list();
		}catch (HibernateException e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
				throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return jobs;
	}
	/**
	 * Returns the Job entity by dataSet
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Job getJobByDataSet(String dataSet) throws GenericException {
		Job job=null;
		Session session;
		try{ 
			session=getOpenSession();
			Query query=session.createQuery("select c from Job c where c.dataSet='"+dataSet+"'");
			job=(Job)query.uniqueResult();
		}catch (HibernateException e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
	}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
	}
		return job;
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
