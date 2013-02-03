package com.pikefin.services;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import pikefin.log4jWrapper.Logs;

import com.pikefin.ApplicationSetting;

@Service
public class DataLoadService {

  public DataLoadService() {
  }

  public static void main(String args[]){
    ApplicationSetting.getInstance().getStdoutwriter()
      .writeln("******* Starting the Application *********",
          Logs.STATUS1,"DLS1");
    ApplicationContext context =
        new ClassPathXmlApplicationContext("classpath:spring.xml");
    BrokerExecutor brokerExecutor=context.getBean(BrokerExecutor.class);
    brokerExecutor.start();
    ApplicationSetting.getInstance().getStdoutwriter()
      .writeln("******* Application services are created and BrokerExecutor " +
          "thread is started *********",Logs.STATUS1,"DLS2");
  }
}
