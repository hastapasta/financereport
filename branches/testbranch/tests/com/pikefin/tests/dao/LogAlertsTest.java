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

import com.pikefin.businessobjects.LogAlert;
import com.pikefin.dao.inter.LogAlertDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
public class LogAlertsTest {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private LogAlertDao logAlertDao;
	@Test
	public void  testLogAlert() throws Exception{
		String query1 = " from LogAlert la";
		query1 += " inner join la.logAlertAlert a ";
		query1 += " inner join a.alertTarget at ";
		query1 += " inner join a.alertEntity e ";
		query1 += " inner join a.alertTimeEvent te";
		query1 += " inner join la.beforeFactData bfd ";
		query1 += " inner join la.afterFactData afd ";
		//query1 += " inner join AlertTarget at ";
		query1 += " where la.emailSent=0 ";
		//query1 += " and alerts.email_alert=1 ";
		query1 += " and at.type=1 ";
		query1 += " order by at.alertTargetId asc";
		Session session=sessionFactory.openSession();
		Query query=session.createQuery(query1);
		List<Object> lst=query.list();
for(Object obj:lst){
		System.out.println(obj);
}
	} 
	@Test
	public void  testLogAlert2() throws Exception{
		
		List<Object> lst=logAlertDao.loadAllLogAlertsForNotification();
for(Object obj:lst){
		System.out.println(obj);
}
	} 
}
