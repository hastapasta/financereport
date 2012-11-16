package com.pikefin.tests.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pikefin.businessobjects.Entity;
import com.pikefin.businessobjects.LogAlert;
import com.pikefin.dao.inter.LogAlertDao;
import com.pikefin.services.inter.EntityService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
public class EntityServiceTest {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private EntityService entityService;
	@Test
	public void  testLogAlert() throws Exception{
		Integer entitygroupId=1;
		String query = "from EntityGroup eg ";
				query += " join eg.entities e ";
				query += " where eg.entityGroupId="+entitygroupId;
		Session session=sessionFactory.openSession();
		Query querys=session.createQuery(query);
		List<Object> lst=querys.list();
		System.out.println("Size="+lst.size());
for(Object obj:lst){
		System.out.println(obj);
}
	} 
	@Test
	public void  testLoadAllEntitiesForGroupId() throws Exception{
		Integer entitygroupId=1;
		List<Entity> lst=entityService.loadAllEntitiesForGroupId(entitygroupId);
		System.out.println("Size 2="+lst.size());
		for(Entity obj:lst){
		System.out.println(obj.getEntityId());
}
	} 
}
