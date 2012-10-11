package com.pikefin.services;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class DataLoadService {
	
	//private static BrokerExecuter brokerExecuter;
	public DataLoadService() {
		
		
	}

	public static void main(String args[]){
		System.out.println("######## Starting the Application Context");
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring.xml");
		BrokerExecuter brokerExecuter=context.getBean(BrokerExecuter.class);
				brokerExecuter.start();
	}
	
	
}
