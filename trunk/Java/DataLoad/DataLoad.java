package com.roeschter.jsl;

/*
 * Comments about multithreading:
 * -Should avoid passing short term information through the database. Ideally, only should write to database for
 * persistent storage. With current setup, cannot process the same dataset concurrently in multiple threads 
 * (one example is dynamic_url).
 * 
 * Also need to figure out how best to handle thread logging - ?simple as creating a global file handle
 * and have all threads write to that?
 */

import java.math.BigDecimal;
import java.net.*;
import java.io.*; 
import java.sql.*;
import java.util.Date;
import java.util.Properties;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*; 

/*import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;*/




                  
/**
  * A simple example for a java service.
  * This is a simple echoing Telnet Server.
  * It accepts telnet connections on port 23 and echos back every character typed.
*/                  
class DataLoad extends Thread implements Stopable
{                                               
  /**
    * The server cocket which accepts connections
  */
  ServerSocket ss;
  static boolean pause=false;
  static UtilityFunctions uf;
  static int nMaxThreads = 1;
  //Hashtable<String,Hashtable<String,String>> hashScheduleTable;
  ArrayList<String> listWaitingJobs;
  DataGrab[] arrayRunningJobs;
  //static printwriter
                                                                 
  /**                                           
    * Here the telnet server implements the Stopable interface
    * On exit close the server cocket on port 23
  */
  public void onServiceStop()
  {       
    System.out.println( "Stopping Telnet Echo" );           
    try {
      if ( ss != null )
        ss.close();
    } catch (Exception e) {}
  }              
  
  public void testCalendar()
  {
	  
	  String str_date="11-June-07";
      DateFormat formatter ; 
      java.util.Date date ; 
      try
      {
    	  formatter = new SimpleDateFormat("dd-MMM-yy");
    	  date = (Date)formatter.parse(str_date); 
    	  Calendar cal=Calendar.getInstance();
    	  cal.setTime(date);
    	  System.out.println("Today is " +cal );
      }
      catch (ParseException pe)
      {
    	  
      }

  }
                                        
  /**
    * Don't wait if onServiceStop does not return. Terminate immediately.
  */                                        
  public int timeToWait()
  {
    return 0;
  }                                     
  
  public void destroy()
  {
	  System.out.println("terminating");
  }
  
  public void getTriggeredJobs() throws SQLException
  {
	String query;
	try
	{
		//query = "LOCK TABLES repeat_types WRITE, schedule WRITE";
		//UtilityFunctions.db_update_query(query);
		//query = "LOCK TABLES schedule WRITE";
		//UtilityFunctions.db_update_query(query);
		
	
		query = "select * from repeat_types";
		ResultSet rs = UtilityFunctions.db_run_query(query);
		ResultSet rs2;
		java.util.Date dNextTrigger;
		listWaitingJobs = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		while (rs.next())
		{

			dNextTrigger = rs.getTimestamp("next_trigger");
			//cal2.set(dNextTrigger.getYear(),dNextTrigger.getMonth(),dNextTrigger.getDay(),dNextTrigger.getHours(),dNextTrigger.getMinutes(),dNextTrigger.getSeconds());
			cal2.setTime(dNextTrigger);

			if ((cal.after(cal2) || rs.getString("type").equals("RUNONCE") || rs.getString("type").equals("RUNEVERY"))
					&& (rs.getString("type").equals("NONE") != true))
			{
				//listTriggeredEvents.add(Integer.toString(rs.getInt("primary_key")));
				query = "select * from schedule where repeat_type=" + Integer.toString(rs.getInt("primary_key"));
				rs2 = UtilityFunctions.db_run_query(query);
				/*
				 * Data_set is guaranteed to be unique because of db constraint
				 */
				while (rs2.next())
				{
					listWaitingJobs.add(rs2.getString("data_set"));
					/*query = "update schedule set status='QUEUED' where data_set='" + rs2.getString("data_set") + "'" +
						" and status !='RUNNABLE'";
					UtilityFunctions.db_update_query(query);*/
					//turn off run once after job is in queue, 4 is the primary key for run type 'NONE'
					if (rs.getString("type").equals("RUNONCE"))
					{
						query = "select primary_key from repeat_types where type='NONE'";
						ResultSet rs3 = UtilityFunctions.db_run_query(query);
						rs3.next();
						query = "update schedule set repeat_type=" + rs3.getInt("primary_key") + " where data_set='" + rs2.getString("data_set") + "'";
						UtilityFunctions.db_update_query(query);
					}
				}
				
			}
		}
		/* Putting this here so that there is no differece between the time used to add jobs to the queue and 
		 * for recalculating the next trigger time.
		 */
		updateEventTimes(cal);
	}
	finally
	{
		//query = "UNLOCK TABLES";
		//UtilityFunctions.db_run_query(query);
	}
	
  }
  
  public void writeJobQueueIntoDB()
  {
	  
	  String query;
	  query = "delete from job_queue";
	  try
	  {
		  UtilityFunctions.db_update_query(query);
		  
		  for (int i=0;i<nMaxThreads;i++)
		  {
			  if (arrayRunningJobs[i] != null)
			  {
				  query = "insert into job_queue (data_set,status) values ('" + arrayRunningJobs[i].strCurDataSet + "','" +
				  arrayRunningJobs[i].getState().toString() + "')";
				  UtilityFunctions.db_update_query(query);
				  UtilityFunctions.stdoutwriter.writeln("Status of thread " + arrayRunningJobs[i].getName() + ": " + 
						  arrayRunningJobs[i].getState().toString(),Logs.THREAD,"DL2");
			  }
		  }
		  for (int k=0;k<listWaitingJobs.size();k++)
		  {
			  query = "insert into job_queue (data_set,status) values ('" + listWaitingJobs.get(k) + "','QUEUED')";
			  UtilityFunctions.db_update_query(query);
		  }
		  
	  }
	  catch (SQLException sqle)
	  {
		  UtilityFunctions.stdoutwriter.writeln("Problem issuing sql statement while writing queue into db",Logs.ERROR,"DL2.5");
		  UtilityFunctions.stdoutwriter.writeln(sqle);	  
	  }
		 
	  
	 
  }
  
  public DataGrab initiateJob() throws SQLException
  {
	  String strDataSet;
	  DataGrab dg;
	  //Calendar cal;
	 
	  if (listWaitingJobs.size() > 0)
	  {
		  //try LOCK TABLES
		  strDataSet = listWaitingJobs.get(0);
		  dg = new DataGrab(DataLoad.uf,strDataSet);
		  dg.startThread();
		  UtilityFunctions.stdoutwriter.writeln("Initiated DataGrab thread " + dg.getName(),Logs.THREAD,"DL4");
		  
		  
		  //DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 // String strDate;
		  /*try
		  {

			  strDate = formatter.format(dg.getStartTime().getTime());
			  String query = "update schedule set last_run='" + strDate + "' where data_set='" + strDataSet + "'";
			  UtilityFunctions.db_update_query(query);
		  }
		  catch (CustomGenericException cge)
		  {
			  //non fatal to not obtain correct start time, ALTHOUGH it should always be available by this point
			  UtilityFunctions.stdoutwriter.writeln("Problem retrieving start time",Logs.ERROR,"DL4.2");
		  }*/
		  
		 
		  listWaitingJobs.remove(0);
		  
		 
		  
		  
		  return(dg);
		  //finally UNLOCK TABLES
	  }
	  else
		  return null;
	  
	  
  }
  
  public void cleanTerminatedJobs() throws SQLException
  {
	  for (int j=0;j<nMaxThreads;j++)
	  {
		  if (arrayRunningJobs[j] != null)
		  {
			  if (Thread.State.TERMINATED == arrayRunningJobs[j].getState())
			  {
				 
				  updateJobStats(arrayRunningJobs[j]);
				  checkNotifications(arrayRunningJobs[j]);
				  UtilityFunctions.stdoutwriter.writeln("Cleaning up thread " + arrayRunningJobs[j].getName(),Logs.THREAD,"DL3");
				  arrayRunningJobs[j] = null;
				  
			  }
		  }
	  }
  }
  
  public void executeJobs() throws SQLException
  {
	 
	  //Clean out terminated threads and count open slots 
	  for (int j=0;j<nMaxThreads;j++)
	  {
		  if (arrayRunningJobs[j] == null)
			  arrayRunningJobs[j] = initiateJob();
		  /*else if (Thread.State.TERMINATED == arrayRunningJobs[j].getState())
		  {
			  UtilityFunctions.stdoutwriter.writeln("Cleaning up thread " + arrayRunningJobs[j].getName(),Logs.THREAD,"DL3");
			  arrayRunningJobs[j] = initiateJob();
		  }*/
		  
	  }
	  
	 
	  
	  
  }
  
  public void updateJobStats(DataGrab dg)
  {
	  try
	  {
		  String strDataSet = dg.strCurDataSet;
		  Calendar endCal = dg.getEndTime();
		  Calendar startCal = dg.getStartTime();
		 
		  
		  DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  //String strDate = formatter.format(endCal.getTime());
		  
		  //strDate = formatter.format(dg.getStartTime().getTime());
		  String query = "update schedule set last_run='" + formatter.format(endCal.getTime())+ "' where data_set='" + strDataSet + "'";
		  UtilityFunctions.db_update_query(query);
		  
		  query = "select * from job_stats where data_set='" + strDataSet + "'";
		  ResultSet rs = UtilityFunctions.db_run_query(query);
		  
		  Long lStart = startCal.getTimeInMillis();
		  Long lEnd = endCal.getTimeInMillis();
		  Long lDiff = lEnd - lStart;
		  Calendar cal = Calendar.getInstance();
		  Calendar cal2 = Calendar.getInstance();
		  cal.setTimeInMillis(lDiff);
		  
		  if (rs.next())
		  {
			  Integer nRunCount = rs.getInt("run_count");
			  
			  
			  //Long lAvg = cal.getTimeInMillis();
			  Long lAvg = rs.getLong("avg_run_duration");
			  
			  lAvg = ((nRunCount * lAvg) + lDiff)/(nRunCount + 1);
			  
			  nRunCount++;
			  
			  
			  cal2.setTimeInMillis(lAvg);
			  
			  query = "update job_stats " + 
			  "set run_count=" + nRunCount + 
			  ",last_run_duration=" +  lDiff.toString() + 
			  ",avg_run_duration=" + lAvg.toString() + 
			  ",last_run_dur_converted='" + UtilityFunctions.getElapsedTimeHoursMinutesSecondsString(lDiff) + "'" +
			  ",avg_run_dur_converted='" + UtilityFunctions.getElapsedTimeHoursMinutesSecondsString(lAvg) + "'" +
			  " where data_set='" + strDataSet + "'";
			  UtilityFunctions.db_update_query(query);
		  }
		  else
		  //data_set doesn't exist in job_stats, need to insert it.
		  {
			 
			  query = "insert into job_stats (data_set,run_count,avg_run_duration,last_run_duration,avg_run_dur_converted,last_run_dur_converted) VALUES ('" +
			  strDataSet + "',1," +
			  lDiff.toString() + "," +
			  lDiff.toString() + ",'" +
			  UtilityFunctions.getElapsedTimeHoursMinutesSecondsString(lDiff) + "','" +
			  UtilityFunctions.getElapsedTimeHoursMinutesSecondsString(lDiff) + "')";

			  UtilityFunctions.db_update_query(query);
		  }
	  }
	  catch (CustomGenericException cge)
	  {
		  UtilityFunctions.stdoutwriter.writeln("Problem retrieving start or end time",Logs.ERROR,"DL2.5");
		  UtilityFunctions.stdoutwriter.writeln(cge);
	  }
	  catch (SQLException sqle)
	  {
		  UtilityFunctions.stdoutwriter.writeln("Problem with sql statement while updating job stats.",Logs.ERROR,"DL2.7");
		  UtilityFunctions.stdoutwriter.writeln(sqle);
	  }
	  
  }
  
  public void updateEventTimes(Calendar cal) throws SQLException
  {
	 try
	 {
		 //String query = "LOCK TABLES repeat_types WRITE,schedule WRITE";
		// UtilityFunctions.db_update_query(query);
		 String query = "select * from repeat_types";
		 ResultSet rs = UtilityFunctions.db_run_query(query);
		 String strType;
		 Date dNextTrigger;
		 Calendar triggerCal;
		 //Saving off the parameter cause we're going to overwrite it, just in case.
		 Calendar newCal = Calendar.getInstance();
		 
		 while (rs.next())
		 {
			 triggerCal = Calendar.getInstance();
			 dNextTrigger = rs.getTimestamp("next_trigger");
			 strType = rs.getString("type");
			 triggerCal.setTime(dNextTrigger);
			 //UtilityFunctions.stdoutwriter.writeln("Old Trigger time" + triggerCal,Logs.STATUS1,"DL7");
			 newCal.setTime(cal.getTime());
	
			if (newCal.after(triggerCal))
				 //Next Trigger needs to be recalculated
				
			 {
				/* we are going to overwrite the database with newcal, not triggercal, since if this code hasn't been
				 * run in a while, triggercal could lag by multiple cycles.
				 */
				 if (strType.equals("WEEKLY"))
				 {
					 newCal.add(Calendar.WEEK_OF_YEAR, 1);
				 }
				 else if (strType.equals("MONTHLY"))
				 {
					 //May need to add code to handle the 28th - 31st
					 newCal.add(Calendar.MONTH, 1);
				 }
				 else if (strType.equals("HOURLY"))
				 {
					 newCal.add(Calendar.HOUR, 1);
				 }
				 else if (strType.equals("MINUTE"))
				 {
					 newCal.add(Calendar.MINUTE, 1);
				 }
				 else if (strType.equals("DAILY"))
				 {
					 newCal.add(Calendar.DAY_OF_YEAR, 1);
				 }
				 else
					 continue;
				 
				//mysql deault datetime format: 'YYYY-MM-DD HH:MM:SS' 
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strDate = formatter.format(newCal.getTime());
				query = "update repeat_types set next_trigger='" + strDate + "' where primary_key=" + rs.getInt("primary_key");
				UtilityFunctions.db_update_query(query);
			 }
		 }
		 
		 
		 //recalculate Trigger times
		 //clear run once here??
	 }
	 finally
	 {
		 //String query = "UNLOCK TABLES";
		 //UtilityFunctions.db_run_query(query);
	 }
	  
	  
	  
  }
  
  public void checkNotifications(DataGrab dg) throws SQLException
  {
	  String strDataSet = dg.strCurDataSet;
	  
	  String query = "select * from schedule where data_set='" + strDataSet + "'";
	  ResultSet rs = UtilityFunctions.db_run_query(query);
	  if (rs.next())
	  {
		  int nKey = rs.getInt("primary_key");
		  query = "select * from notify where schedule=" + nKey;
		  rs = UtilityFunctions.db_run_query(query);
		  //loop through the schedules associated with this notification
		  while (rs.next())
		  {
			  //String strType = rs.getString("type");
			  String strTicker = rs.getString("ticker");
			  String strFrequency = rs.getString("frequency");
			  String strEmail = rs.getString("email");
			  BigDecimal dLimit = rs.getBigDecimal("limit_value");
			  int nFactKey = rs.getInt("fact_data_key");
			  int nNotifyKey = rs.getInt("primary_key");
			  
			  //try to find the fact key in fact_data
			  String query2 = "select * from fact_data where primary_key=" + nFactKey;
			  ResultSet rs2 = UtilityFunctions.db_run_query(query2);
			  
			  String query3;
			  if (!(strTicker==null) && !(strTicker.isEmpty()))
				  query3 = "select * from fact_data where data_set='" + strDataSet + "' and ticker='" + strTicker + "' and batch=" +
				  "(select max(batch) from fact_data where data_set='" + strDataSet + "' and ticker='" + strTicker + "')";
			  else
				  query3 = "select * from fact_data where data_set='" + strDataSet + "' and batch=" +
				  "(select max(batch) from fact_data where data_set='" + strDataSet + "')";
			  
			  ResultSet rs3 = UtilityFunctions.db_run_query(query3);
			
			  if (rs2.next())
			  {
				  //if (strFrequency.equals("HOURLY"))
				  //{
					  Calendar calJustCollected;
					  Calendar calLastRun = Calendar.getInstance();
					  try
					  {
						  calJustCollected = dg.getEndTime();
					  }
					  catch (CustomGenericException cge)
					  //added this exception for multi-threading purposes - this should be populated by here
					  {
						  UtilityFunctions.stdoutwriter.writeln("Attempted to read empty end time, skipping notification for this job",Logs.ERROR,"DL2.73");
						  UtilityFunctions.stdoutwriter.writeln(cge);
						  return;
						  
					  }
					  
					
					//get the date
					
					  calLastRun.setTime(rs2.getDate("date_collected"));
					  if (strFrequency.equals("HOURLY"))
					  {
						  calLastRun.add(Calendar.HOUR,1);
					  }
					  else if (strFrequency.equals("WEEKLY"))
					  {
						  calLastRun.add(Calendar.WEEK_OF_YEAR,1);
					  }
					  else if (strFrequency.equals("MONTHLY"))
					  {
						  calLastRun.add(Calendar.MONTH,1);
					  }
					  else if (strFrequency.equals("DAILY"))
					  {
						  calLastRun.add(Calendar.DAY_OF_YEAR,1);
					  }
					  else if (strFrequency.equals("MINUTE"))
					  {
						  calLastRun.add(Calendar.MINUTE,1);
					  }
					  else
					  {
						  UtilityFunctions.stdoutwriter.writeln("Notify frequency not found, skipping job notification",Logs.ERROR,"DL2.735");
						  continue;
					  }
					  
					
					  
					  if (calJustCollected.after(calLastRun))
					  {
						  rs3.next();
						  
						  BigDecimal dJustCollectedValue = rs3.getBigDecimal("value");
						  BigDecimal dLastRunValue = rs2.getBigDecimal("value");
						  
						  //update with new primary key
						  String query4 = "update notify set fact_data_key=" + rs3.getInt("primary_key") + " where primary_key=" + nNotifyKey;
						  UtilityFunctions.db_update_query(query4);
						  
						  BigDecimal dChange = dLastRunValue.subtract(dJustCollectedValue).divide(dLastRunValue,BigDecimal.ROUND_HALF_UP);
						  
						 
						  if (dChange.compareTo(dLimit) > 0)
						  {
							  //send notification
							  System.out.println("ALERT: Send mail!");
							  UtilityFunctions.mail(strEmail,"ALERT: " + strDataSet + " moved " + dChange.multiply(new BigDecimal(100)).toString() + "%");
					  
						  }
						  
						  
							  
						  
					  }
					  
					  
					
				 // }  
			  }
			  else
				  //fact_data_key not found, assume this is the first time this notification has been used and needs to be populated
				  //if I base this off of the batch #, have to be very careful with how it is maintained.
			  {
				 
				  if (rs3.next())
				  {
					  query2 = "update notify set fact_data_key=" + rs3.getInt("primary_key") + " where primary_key=" + nNotifyKey;
					  UtilityFunctions.db_update_query(query2);
				  }
					  
				  else
				  //didn't find a fact_data_key with which to populate this notification and there should be at least one there
				  //print a message
					  UtilityFunctions.stdoutwriter.writeln("Attempting to populate notify table and did not find fact_data entry",Logs.ERROR,"DL2.75");
			  }
			  
			  
			  
			  
			  
		  }
		  
		  
		  
	  }
	  
	  
	  
	  
  
  }
  
  


  
  /**
    * Open server socket and wait for incoming connections
  */                                                   
  public void run()
  {	
  	uf = new UtilityFunctions("mydb","root","madmax1.","full.log","error.log","sql.log","thread.log");
  	//Scheduler sched = new Scheduler(uf);
  	//System.out.println("in run 1");
	//testCalendar();  
  	arrayRunningJobs = new DataGrab[nMaxThreads];
  	for (int i=0;i<nMaxThreads;i++)
  		arrayRunningJobs[i] = null;
  	//ArrayList<DataGrab> listDataGrab;
  	//ArrayList<String> listJobs;
  	//ArrayList<String[]> listSchedules;
  	
  	//DataGrab DG = new DataGrab(uf);
 
  	//DG.startThread();
  	//int nRunningThreads = 0;

  	
  	/*String userDir = System.getProperty("user.dir");
		System.out.println(userDir + "\\fact_data_stage.csv");
  	UtilityFunctions.loadCSV(userDir + "\\fact_data_stage.csv");*/
  	
    
	if (pause != true)
	{
		while(1 != 2)
		{    	
			//
			try 
			{ 
				
				getTriggeredJobs();
				executeJobs();
				writeJobQueueIntoDB();
				sleep(60000);
				//updateEventTimes();
				cleanTerminatedJobs();
				
				//checkNotifications();
				
				
				//new Echo( ss.accept() ).start();      
				//sleep(60000); //sleep for 1 minute
				//System.out.println("Awake.....");
				//Check if schedules are triggered
				
			        
			     /*ArrayList<String> listDataSets = this.checkSchedules();
			     boolean done = false;
			     while (!done == true)
			     {
			    	 if (nRunningThreads == this.nMaxThreads)
			    		 sleep(60000);
			    	 else
			    		 
			    		 
			    	 
			     }*/
			        	
			  
			        	//Check for run once
			        	//Recalculate trigger timess
				
				
				
				
				
			}
			catch(SQLException sqle)
			{
				UtilityFunctions.stdoutwriter.writeln("Problem issuing sql statement while processing schedules",Logs.ERROR,"DL10");
				UtilityFunctions.stdoutwriter.writeln(sqle);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}  
}                
  
  /**
    * The worker thread.
    * Get the socket input stream and echos bytes read
  */    
  class Echo extends Thread
  {              
    Socket s;
    Echo( Socket s )
    {
      this.s = s;
    }
    
    public void run()
    {
    	System.out.println("in run 2");
      try { 
        InputStream in = s.getInputStream();  
        OutputStream out = s.getOutputStream();  
                        
        while( true )
        {
          out.write( in.read() );
        }                
      } catch (Exception e)
      {}
    }
  }    
     
  public static void paramtest( String[] arg)
  {
    System.out.println( "param passsing test" );
    if ( arg == null )
    {
        System.out.println( arg );
        return;
    }
    for( int i=0; i<arg.length; i++ )
        System.out.println( arg[i] );
  }     
     
     
  public static void pause()
  {
    System.out.println( "Service paused" );
  }    
  
  public static int premain()
  {
    System.out.println( "Premain call" );   
    return 0;
  }    
  
  public static void cont()
  {
    System.out.println( "Service continued" );
  }     
  
  

                                                                                                                                
  /**                                                         
    * The main method called when the service starts.
  */
  public static void main (String[] argv) throws Exception
  {               	  			                  
    /*System.out.println( "This is the System.out stream" );
    System.err.println( "This is the System.err stream" );
    System.out.println( "This is a test. ");*/
    			      
    String inifile = System.getProperty( "service.inifile" ); 			      
    System.out.println( "Loading ini file: " + inifile );
    
    Properties props = new WindowsCompatibleProperties();
    props.load( new FileInputStream(inifile) );
    
    System.out.println( props );
    			                         
    //Create echo server class
    DataLoad dLoad = new DataLoad();
    //Register it with the ServiceStopper
    //This is a decprecated feature only demontrated for backwards compatibility.
    //Please use the stopmethod,stopclass configuration parameter for stopping a service
    
    //ServiceStopper.stop( echo );         
    
    //Start the echo server thread
    dLoad.start();
  }
  
  public ArrayList<String> checkSchedules() throws SQLException
  {

	  ArrayList<String> tmpList = new ArrayList<String>();
	  String query = "select next_trigger,data_set from repeat_types,schedule where repeat_types.primary_key=schedule.Repeat_Type";
	  ResultSet rs = UtilityFunctions.db_run_query(query);
	  Calendar cal = Calendar.getInstance();
	  while(rs.next())
	  {
		  Time t = rs.getTime("next_trigger");
		  t.before(cal.getTime());
		  tmpList.add(rs.getString("data_set"));  
	  }
	  
	
	  
	  return (tmpList);
	  
	  
  }


}              


 






