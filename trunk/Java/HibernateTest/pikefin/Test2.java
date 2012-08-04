package pikefin;


import java.util.*; 

import pikefin.hibernate.*;

class Test2 extends TestParent {
  
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
  
  public void runTest() {
	  
	  System.out.println("blap");
	  String query2  = " from Alert a ";
		//query2 += " left join a.alertEntity e ";
	  //query2 += " left join a.alertInitialFactData fd with a.calyear<=>fd.calyear ifd";
	    query2 += " left join a.alertInitialFactData fd with a.calyear=fd.calyear ";
		//query2 += " left join a.alertUser u ";
		//query2 += " left join a.alertTimeEvent te ";
		//query2 += " left join a.alertTask t ";
		//query2 += " left join e.countryWrapper c ";
		//query2 += " left join e.country c";
		query2 += " where a.alertTask.taskId=10 ";
		//query2 += " and (e.countryWrapper.default_country=true or e.countryWrapper.default_country is null)";
		query2 += " order by a.alertTimeEvent.timeEventId";
	  
	  //dbf.dbHibernateRunQueryUnique(query);
	  //List<Alert> l = dbf.dbHibernateRunQuery(query2);
	  //Alert a = (Alert)l.get(0);
	  
	  List<Object[]> l = dbf.dbHibernateRunQuery(query2);
	  Alert a = (Alert)l.get(0)[0];
	  
	  
	  
	  Entity e = a.getAlertEntity();
	  Set<CountryWrapper> tmpCountries = (Set<CountryWrapper>)e.getCountryWrapper();
			  
	  //Set<CountryEntity> tmpCountries = (Set<CountryEntity>)l.get(0).getCountryEntities();
	  //Set<CountryWrapper> tmpCountries2 = (Set<CountryWrapper>)l.get(0).getAlertEntity().getCountryWrapper();
	  
	  System.out.println(l.size());
	  //System.out.println(tmpCountries.size());
	  System.out.println(tmpCountries.size());
	  
	  for (CountryWrapper c : tmpCountries) {
		  System.out.println(c.isDefaultCountry());	
		  System.out.println(c.getCountry().getName());
	  }
	  
	  System.out.println("here");
  }
  
  
}

 






