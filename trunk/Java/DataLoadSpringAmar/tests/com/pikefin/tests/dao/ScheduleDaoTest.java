package com.pikefin.tests.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import com.pikefin.businessobjects.Schedule;
import com.pikefin.dao.inter.ScheduleDao;
import com.pikefin.dao.inter.ScheduleDao;
import com.pikefin.exceptions.GenericException;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
//@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class ScheduleDaoTest {
@Autowired
private  ScheduleDao scheduleDao;
	Logger log=Logger.getLogger(ScheduleDaoTest.class);
	@Before
	public  void setUp(){
	}

	private static Integer COUNTRY_ID;
	@Test
	public void  testScheduleSave() throws Exception{
				log.debug("Group entity saved sucessfully with id-"+COUNTRY_ID);
	}
	@Test
	public void  testScheduleLoad() throws GenericException{
	
	log.debug("Schedule loaded sucessfully with id-"+COUNTRY_ID);

	}
	
	@Test	
	public void  testScheduleUpdate() throws GenericException{
				log.debug("Schedule update executed sucessfully  with id-"+COUNTRY_ID);
	}
	@Test
	public void  testloadAllSchedulesForBrokerExecuter() throws GenericException{
		List<Schedule> schedules= scheduleDao.loadAllSchedulesForBrokerExecuter();
		for(Schedule temp:schedules){
			System.out.println(temp);
			System.out.println("Task ID=="+temp.getTask().getName());
			
		}
		log.debug("Schedule update executed sucessfully  with id-"+COUNTRY_ID);
	}
	
	@Test
	public void  testLoadAllCountries() throws GenericException{
		
		log.debug("Schedule update executed sucessfully  with id-"+COUNTRY_ID);
	}
	@Test
	public void  testScheduleDelete() throws GenericException{
			log.debug("Schedule deleted sucessfully with id-"+COUNTRY_ID);
	}
}
