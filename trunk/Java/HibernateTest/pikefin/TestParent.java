package pikefin;


import java.util.*; 
import pikefin.hibernate.*;

public abstract class TestParent extends Thread {
  
  /*
   * OFP 5/10/2012 - Right now we are stuck in between using Spring for thread management with ThreadPoolTaskExecutor and using 
   * the legacy explicit thread management. In spring.xml, the app.max_threads property is used both for the Broker variable 
   * and for injecting into ThreadPoolTaskExecutor.
   * 
   * UPDATE 5/29/2012: I'm deciding not to go with ThreadPoolTaskExector for now - the main benefit of TPTE is the ability to swap out scheduler components
   * but we already have a custom built, functional scheduler component that is based off of the database table. A scheduler component stored entirely in memory
   * would have to be exposed for monitoring purposes. Also monitoring thread state doesn't work the same way with ThreadPoolTaskExecutor - getState() always returns
   * NEW, even after the run() method has completed.
   */
  
  static int nMaxThreads;
  
  
  static Properties props;
  //static boolean bDisableEmails;
  static private boolean bLoadingHistoricalData;
  static private boolean bDebugMode;
  static private int nSleepInterval;
  DBFunctions dbf;
  UtilityFunctions uf;
  
  //ThreadPoolTaskExecutor threadPool = null;

  /*
   * listWaitingJobs now holds  tasks.id,schedules.id,repeat_types.id,priority,queued time
   */
  //ArrayList<String[]> listWaitingJobs;
  
  public void setDbFunctions(DBFunctions inputDBF) {
		dbf = inputDBF;
  }

  public void setUtilityFunctions(UtilityFunctions inputUF) {
		uf = inputUF;
  }
  
  public abstract void runTest();
  
  public void run() {
	  
	  runTest();
  }
  
  
}

 






