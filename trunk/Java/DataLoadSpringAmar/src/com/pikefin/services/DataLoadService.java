package com.pikefin.services;

import org.springframework.beans.factory.annotation.Autowired;
 
public class DataLoadService {
	@Autowired
	private BrokerExecuter brokerExecuter;
	public DataLoadService() {
		brokerExecuter.start();
	}

}
