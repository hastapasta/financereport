package com.pikefin.tests.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.pikefin.businessobjects.Groups;
import com.pikefin.businessobjects.User;
import com.pikefin.dao.inter.GroupDao;
import com.pikefin.dao.inter.UserDao;
import com.pikefin.exceptions.GenericException;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
@TransactionConfiguration(transactionManager="transactionManager")
public class UserDaoTest {
@Autowired
private  GroupDao groupDao;
@Autowired
private  UserDao userDao;
	Logger log=Logger.getLogger(UserDaoTest.class);
	@Before
	public  void setUp(){
		//factory=new ClassPathXmlApplicationContext("spring.xml");
		//groupDao=applicationContext.getBean(GroupDao.class);
		//applicationContext
	}
	private static Integer USER_ID;
	private static String USER_NAME="testingUSER";
	private static String ACCOUNT_EMAIL="testemail@test.com";
	private static String USER_PASSWORD="testPassword";
	private static Integer PAGINATION_LIMIT=20;
	private static String ACCOUNT_EMAIL_UPDATED="testemailupdated@updated.com";
	private static Integer PAGINATION_LIMIT_UPDATED=30;
	private static Integer GROUP_ID=1;
	@Test
	public void  testUserSave() throws Exception{
		User user=new User();
		user.setAccountEmail(ACCOUNT_EMAIL);
		user.setCreated(new Date());
		user.setActive(true);
		user.setPaginationLimit(PAGINATION_LIMIT);
		user.setObsoleteBulkEmail(false);
		user.setPassword(USER_PASSWORD);
		user.setUsername(USER_NAME);
		Groups userGroup=groupDao.loadGroupInfo(GROUP_ID);
		assertNotNull(userGroup);
		user.setUserGroup(userGroup);
		user.setObsoleteBulkEmail(true);
		user.setObsoleteMaxNotifications(10);
		user=userDao.saveUserInfo(user);
		assertNotNull(user);
		assertNotNull(user.getUserId());
		USER_ID=user.getUserId();
		assertEquals(USER_NAME, user.getUsername());
		assertEquals(ACCOUNT_EMAIL, user.getAccountEmail());
		assertEquals(USER_PASSWORD, user.getPassword());
		log.debug("User Added sucessfully with id-"+GROUP_ID);
	}
	@Test
	public void  testUserLoad() throws GenericException{
	User user =userDao.loadUserInfo(USER_ID);
	assertNotNull(user);
	assertEquals(USER_ID, user.getUserId());
	assertEquals(USER_NAME, user.getUsername());
	assertEquals(ACCOUNT_EMAIL, user.getAccountEmail());
	Groups group=user.getUserGroup();
	assertNotNull(group);
	assertEquals(GROUP_ID, group.getId());
	assertNotSame(new Date(), user.getCreated());
	log.debug("User Entity loaded sucessfully with id-"+USER_ID);

	}
	
	@Test	
	public void  testUserUpdate() throws GenericException{
		User user =userDao.loadUserInfo(USER_ID);
		assertNotNull(user);
		assertEquals(USER_ID, user.getUserId());
		assertEquals(USER_NAME, user.getUsername());
		user.setAccountEmail(ACCOUNT_EMAIL_UPDATED);
		user.setPaginationLimit(PAGINATION_LIMIT_UPDATED);
		user.setModified(new Date());
		user=userDao.updateUserInfo(user);
		assertNotNull(user);
		assertNotSame(ACCOUNT_EMAIL, user.getAccountEmail());
		assertEquals(ACCOUNT_EMAIL_UPDATED, user.getAccountEmail());
		assertEquals(USER_ID, user.getUserId());
		assertEquals(PAGINATION_LIMIT_UPDATED, user.getPaginationLimit());
		assertNotNull(user.getModified());
		assertNotSame(new Date(), user.getCreated());
		log.debug("User update executed sucessfully  with id-"+USER_ID);
	}
	
	@Test
	public void  testUserDelete() throws GenericException{
		User user;
		boolean result =userDao.deleteUserInfoById(USER_ID);
		assertTrue(result);
		user =userDao.loadUserInfo(USER_ID);
		assertNull(user);
		log.debug("User deleted sucessfully with id-"+USER_ID);
	}
}
