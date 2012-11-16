package com.pikefin.services;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import twitter4j.internal.logging.Logger;

import com.pikefin.ApplicationSetting;

@Service
public class DataLoadService {
	
	private static Logger  logger=Logger.getLogger(DataLoadService.class);
	public DataLoadService() {
		
		
	}

	public static void main(String args[]){
		logger.info("******* Starting the Application *********");
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring.xml");
		BrokerExecutor brokerExecutor=context.getBean(BrokerExecutor.class);
		brokerExecutor.start();
		logger.info("******* Application services are created and BrokerExecutor thread is started *********");

			
	}
	
	
}
