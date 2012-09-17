package com.pikefin.tests.dao;

/**
 * @author Amar_Deep_Singh
 *
 */
import java.util.Date;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pikefin.businessobjects.Groups;
import com.pikefin.businessobjects.User;
import com.pikefin.dao.inter.UserDao;
import com.pikefin.exceptions.GenericException;
import static org.junit.Assert.*;
public class UserDaoTest{
	private static BeanFactory factory;
	Logger Log=Logger.getLogger(UserDaoTest.class);
	//@Autowired
	private static UserDao userDao;
	@BeforeClass
	public static void setUp(){
		factory=new ClassPathXmlApplicationContext("spring.xml");
		userDao=factory.getBean(UserDao.class);
	}
	
	@Test
	public void testUserSave(){
		User user =new User();
		user.setAccountEmail("chaudharyamardeeep@gmail.com");
		user.setActive(true);
		user.setCreated(new Date());
		user.setObsoleteBulkEmail(false);
		user.setObsoleteMaxNotifications(0);
		user.setPaginationLimit(20);
	//	user.setUserGroup();
		user.setSuspended(false);
		UUID uid=UUID.randomUUID();
		user.setUsername(uid.toString());
		user.setPassword("password");
		assertNotNull(userDao);
		try{
		userDao.saveUserInfo(user);
		}catch (GenericException e) {
			Log.error(e.getErrorMessage()+" -"+e.getErrorDescription());
		}
	assertNotNull(userDao);
	assertEquals("chaudharyamardeeep@gmail.com", user.getAccountEmail());
	assertNotNull(user.getUserId());
	}
	
	@Test
	public void testUserLoad(){
	
		try{
		userDao.loadAllUsersByAccountEmail("chaudharyamardeeep@gmail.com");
		}catch (GenericException e) {
			Log.error(e.getErrorMessage()+" -"+e.getErrorDescription());
		}
	
	}
}
