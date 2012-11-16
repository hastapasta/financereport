package com.pikefin.tests.dao;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
public class AlterDatabaseTableEngine {
	@Autowired
	private SessionFactory sessionFactory;
	
	public static final String MY_SQL_DATABASE_ENGINE="INNODB";
	@Test
	public void alterTableSchema(){
		Map<String, ClassMetadata> map=sessionFactory.getAllClassMetadata()	;
		Set<Entry<String, ClassMetadata>> entries=map.entrySet();
		AbstractEntityPersister aep;
		
		for(Entry<String,ClassMetadata> entry:entries){
			ClassMetadata metaData=entry.getValue();
			if(metaData instanceof AbstractEntityPersister){
				aep=(AbstractEntityPersister) metaData;
				String tableName=aep.getTableName();
				System.out.println(tableName);
				if(!tableName.equals("repeat_types"))
				changeTableEngine(tableName);
			}
			
		}
		
		
	}
	@Transactional
	private void changeTableEngine(String tableName){
		Session session=sessionFactory.openSession();
		String sql="Alter Table "+tableName +" ENGINE="+MY_SQL_DATABASE_ENGINE;
		System.out.println(sql);
		Query query=session.createSQLQuery(sql);
		query.executeUpdate();
	}
}
