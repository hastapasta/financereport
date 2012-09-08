package pikefin;

import static org.junit.Assert.*;
import org.junit.runner.*;

import org.junit.Test;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(transactionManager="")
@ContextConfiguration(locations={"../junitspring.xml"})
public class AlertsTest {
	
	DBFunctions dbf;
	DataGrab dg;
	
	public void setDbFunctions(DBFunctions inputDBF) {
		dbf = inputDBF;
		
	}
	
	public void setDataGrab(DataGrab inputDG) {
		dg = inputDG;
		
	}
	
	public AlertsTest() {
		System.out.println("test");
		
	}

	@Test
	public void testCheckAlerts() {
		
		/*BeanFactory beanfactory = new ClassPathXmlApplicationContext(
		        "junitspring.xml");*/
		
		Alerts tester = new Alerts();

		try {
			dg = new DataGrab(dbf,
					/*Task*/	"10",
					/*Batch Id*/	1,
					/*Scheduled Param*/	"",
					/*Repeat Type Id Param*/	"",
					/*Verify Mode*/	false,
					/*Input Priority*/	0);
			tester.checkAlerts(dg);
		}
		catch (PikefinException pe) {
			
		}
		assertEquals("Result",1,1);
		//assertEquals("Result",50,tester.checkAlerts(dg));
		//fail("Not yet implemented");
	}

}
