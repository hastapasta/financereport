package com.pikefin.services;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.pikefin.ApplicationSetting;

@Service
public class DataLoadService {
	
	//private static BrokerExecuter brokerExecuter;
	public DataLoadService() {
		
		
	}

	public static void main(String args[]){
		System.out.println("######## Starting the Application Context");
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring.xml");
		BrokerExecuter brokerExecuter=context.getBean(BrokerExecuter.class);
		//ApplicationSetting.getInstance().setStdoutwriter(stdoutwriter)
				brokerExecuter.start();
			
	}
	
	
}
