package com.pikefin.tests.dao;

import java.util.Date;


import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.Groups;
import com.pikefin.dao.inter.GroupDao;
import com.pikefin.exceptions.GenericException;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
//@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class GroupDaoTest {
@Autowired
private  GroupDao groupDao;
	Logger log=Logger.getLogger(GroupDaoTest.class);
	@Before
	public  void setUp(){
		//factory=new ClassPathXmlApplicationContext("spring.xml");
		//groupDao=applicationContext.getBean(GroupDao.class);
		//applicationContext
	}
	private static String GROUP_NAME="testGroup";
	private static String GROUP_NAME_UPDATED="testUpdatedGroup";
	private static Integer GROUP_ID;
	@Test
	public void  testGroupSave() throws Exception{
		Groups group=new Groups();
		group.setName(GroupDaoTest.GROUP_NAME);
		group.setCreated(new Date());
		group.setModified(null);
		group=groupDao.saveGroupInfo(group);
		assertNotNull(group.getId());
		assertEquals(GROUP_NAME, group.getName());
		GROUP_ID=group.getId();
		log.debug("Group entity saved sucessfully with id-"+GROUP_ID);
	}
	@Test
	public void  testGroupLoad() throws GenericException{
	Groups	group =groupDao.loadGroupInfo(GROUP_ID);
	assertNotNull(group);
	assertEquals(GROUP_ID, group.getId());
	assertEquals(GROUP_NAME, group.getName());
	assertNotNull(group.getCreated());
	assertNotSame(new Date(), group.getCreated());
	log.debug("loadGroupInfo loaded sucessfully with id-"+GROUP_ID);

	}
	
	@Test	
	public void  testGroupUpdate() throws GenericException{
		Groups	group =groupDao.loadGroupInfo(GROUP_ID);
		assertNotNull(group);
		assertEquals(GROUP_ID, group.getId());
		assertEquals(GROUP_NAME, group.getName());
		group.setName(GROUP_NAME_UPDATED);
		group.setModified(new Date());
		group=groupDao.updateGroupInfo(group);
		assertNotNull(group);
		assertNotSame(GROUP_NAME, group.getName());
		assertEquals(GROUP_NAME_UPDATED, group.getName());
		assertEquals(GROUP_ID, group.getId());
		assertNotNull(group.getModified());
		assertNotSame(new Date(), group.getCreated());
		log.debug("Group update executed sucessfully  with id-"+GROUP_ID);
	}
	
	@Test
	public void  testGroupDelete() throws GenericException{
		Groups	group =groupDao.loadGroupInfo(GROUP_ID);
		assertNotNull(group);
		assertEquals(GROUP_ID, group.getId());
		assertEquals(GROUP_NAME_UPDATED, group.getName());
		boolean result =groupDao.deleteGroupInfo(group);
		assertTrue(result);
		group =groupDao.loadGroupInfo(GROUP_ID);
		assertNull(group);
		log.debug("Group deleted sucessfully with id-"+GROUP_ID);
	}
}
