package pikefin;



import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//import pikefin.log4jWrapper.Logs;

          
                 
class HibernateTest {                                               


  static Test1 test1;


  
  public void setTest1(Test1 input) {
	  
	  test1 = input;
  }
  


  public static void main (String[] argv) throws Exception {       
	  
	  /*Don't remove this line*/
	  BeanFactory beanfactory = new ClassPathXmlApplicationContext(
		        "spring.xml");
    			      
    
    
	  test1.start();


  }
  
  


}              


 






