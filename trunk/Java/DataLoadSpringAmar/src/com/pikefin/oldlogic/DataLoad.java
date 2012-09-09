package com.pikefin.oldlogic;



import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//import pikefin.log4jWrapper.Logs;

          
                 
class DataLoad {                                               


  static Broker broker;


  
  public void setBroker(Broker input) {
	  
	  broker = input;
  }
  


  public static void main (String[] argv) throws Exception {       
	  
	  /*Don't remove this line*/
	  BeanFactory beanfactory = new ClassPathXmlApplicationContext(
		        "spring.xml");
    			      
    
    
	  broker.start();


  }
  
  


}              


 






