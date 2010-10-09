package com.roeschter.jsl;

import java.net.*;
import java.io.*; 
import java.util.*; 

                  
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

  /**
    * Open server socket and wait for incoming connections
  */                                                   
  public void run()
  {	
  	
  	System.out.println("in run 1");
  	DataGrab DG = new DataGrab(new UtilityFunctions("mydb","root","madmax1.","full.log","error.log","sql.log"));
 
  	DG.startThread();
  	
  	/*String userDir = System.getProperty("user.dir");
		System.out.println(userDir + "\\fact_data_stage.csv");
  	UtilityFunctions.loadCSV(userDir + "\\fact_data_stage.csv");*/
    
    if (pause != true)
    {
    	while(1 != 2)
    	{    	
    	
      	try { 
      	  //new Echo( ss.accept() ).start();      
        	sleep(60000); //sleep for 1 minute
        	//System.out.println("Awake.....");
   	  	} catch (Exception e)
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
    System.out.println( "This is the System.out stream" );
    System.err.println( "This is the System.err stream" );
    System.out.println( "This is a test. ");
    			      
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
}               




