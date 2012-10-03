package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Job;
import com.pikefin.exceptions.GenericException;

public interface JobDao {
	public Job saveJobInfo(Job jobEntity) throws GenericException;
	public Job updateJobInfo(Job jobEntity) throws GenericException;
	public Boolean deleteJobInfo(Job jobEntity ) throws GenericException;
	public Boolean deleteJobInfoById(Integer jobId ) throws GenericException;
	public Job loadJobInfo(Integer jobId) throws GenericException;
	public List<Job> loadAllJobs() throws GenericException;
}
