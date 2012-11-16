package com.pikefin.tests.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pikefin.businessobjects.Exclude;
import com.pikefin.businessobjects.JobQueue;
import com.pikefin.businessobjects.LogAlert;
import com.pikefin.businessobjects.RepeatType;
import com.pikefin.dao.inter.ExcludeDao;
import com.pikefin.dao.inter.JobQueueDao;
import com.pikefin.dao.inter.LogAlertDao;
import com.pikefin.dao.inter.RepeatTypeDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
public class ExcludeDaoTest {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private ExcludeDao excludeDao;
	@Autowired
	private RepeatTypeDao repeatDao;
	@Autowired
	private JobQueueDao jobQueueDao;
	@Test
	public void  testLogAlert() throws Exception{
		Integer taskId=10000;
		List<Exclude> lst=excludeDao.loadAllExcludesByTaskId(taskId)	;
for(Exclude obj:lst){
		System.out.println("Exclude ID=="+obj.getExcludeId());
}
	} 
	@Test
	public void  testRepeatLoad() throws Exception{
		
		List<RepeatType> repeatTypes=repeatDao.loadAllRepeatTypes();
		for(RepeatType obj:repeatTypes){
			System.out.println("Repeat ID=="+obj.getRepeatTypeId());
	}
		
	} 
	
	@Test
	public void  testdeleteAllJobQueues() throws Exception{
		
	List<JobQueue> list =jobQueueDao.loadAllJobQueues();
	for(JobQueue obj:list){
		System.out.println("Repeat ID=="+obj.getJobQueueId());
}		
	} 
}
