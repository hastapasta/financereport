import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;




public class MultiTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (args[0].toUpperCase().equals("LINKTEST"))
		{
			linkLest(args);		
		}
		else if (args[0].toUpperCase().equals("DATAGRABALIVE"))
		{
			dataGrabAlive(args);
		}
		else if (args[0].toUpperCase().equals("JOBDURATION"))
		{
			maxJobDuration();
		}
		else if (args[0].toUpperCase().equals("YAHOOTEST")) {
			yahooDataTest(args);
		}
		else if (args[0].toUpperCase().equals("TWITTERTEST")) {
			twitterURLTest(args);
		}
		else
		{
			System.out.println("valid commands are: LINKTEST,DATAGRABALIVE,JOBDURATION");
			System.err.println("valid commands are: LINKTEST,DATAGRABALIVE,JOBDURATION");
		}
	}
	
	public static void maxJobDuration()
	{
		
		//Output the longest job duration from the job_queue table.
		
		try
		{
			
			UtilityFunctions uf = new UtilityFunctions();
			
			DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");
			
			String select = "select max(round(time_to_sec(timediff(NOW(),start_time))/60,0)) as maxduration from job_queue where status='RUNNABLE'";
			
			ResultSet rs = dbf.db_run_query(select);
			
			int nMaxTime = 0;
			
			if (rs.next())
				nMaxTime = rs.getInt("maxduration");
			
			System.out.println(nMaxTime);
			
		}
		catch (SQLException sqle)
		{
			
			/*
			 * 
			 * We'll output a negative number to indicate an error.
			 */
			
			System.out.println(-1);
			System.err.println(sqle.getMessage());
			
			
			
		}
		
		
		
		
		
		
		
	}
	
	
	
	public static void dataGrabAlive(String[] args)
	{
		
		
		
		try
		{
			
			UtilityFunctions uf = new UtilityFunctions();
			
			DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");
			
			
			
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String select = "select * from log_tasks order by alert_process_end DESC";
			
			ResultSet rs1 = dbf.db_run_query(select);
			
			rs1.next();
			
			Calendar calCurrent = Calendar.getInstance();
			
			Calendar calLastRun = Calendar.getInstance();
			
			calLastRun.setTime(inputFormat.parse(rs1.getString("alert_process_end")));
			
			calLastRun.add(Calendar.MINUTE,20);
			
			if (calLastRun.after(calCurrent))
				System.out.println(1);
			else
				System.out.println(0);		
			
		}
		catch(SQLException sqle)
		{
			/*
			 * Something wrong is happenging, we'll output a zero to throw a pandora alert
			 * and get someone to check it out.
			 */
			
			System.out.println(0);
			System.err.println(sqle.getMessage());
			
		}
		catch(ParseException pe)
		{
			System.out.println(0);
			System.err.println(pe.getMessage());
		}
		
		
	}
	
	
	
	
	public static void linkLest(String[] args)
	{
		long lElapsed = 0;
		
		HttpEntity entity;
		
		try
		{
			String strURL = args[1];
			
			
			
			HttpClient httpclient = new DefaultHttpClient();
			
			Calendar calBegin = Calendar.getInstance();
			
			HttpGet httpget = new HttpGet(strURL); 
			
			//httpclient.getParams().setParameter(HttpClientParams.SO_TIMEOUT, new Long(5000));
	
			
			HttpResponse response = httpclient.execute(httpget);
			
			entity = response.getEntity();
			
			Calendar calEnd = Calendar.getInstance();
			
			lElapsed = (calEnd.getTimeInMillis() - calBegin.getTimeInMillis());
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
					entity.getContent()));
			
			 int nTmp;			
	
			  String returned_content="";
			
			  while ((nTmp = in.read()) != -1)
				returned_content = returned_content + (char)nTmp;
			
	
			  in.close();
			  
			  if (returned_content.contains("FailureMode=1"))
				  lElapsed=-3;
			
		}
		catch(MalformedURLException MFUE)
		{
			lElapsed = -1;
		}
		catch (IOException ioe)
		{
			lElapsed = -2;
		}
		
		
	  
	
				
	 
		
		System.out.println(lElapsed);
	}
	
	public static void twitterURLTest(String[] args) {
		long lElapsed = 0;
		Calendar cal = Calendar.getInstance();
		
		/* 
		 * Only run the test in the first 7 minutes of the hour to avoid overloading
		 * the twitter system with requests.
		 */
		if (cal.get(Calendar.MINUTE) < 45) {
			System.out.println(lElapsed);
			return;
		}
		
		HttpEntity entity;
		
		try
		{
			//String strURL = args[1];
			
			
			
			HttpClient httpclient = new DefaultHttpClient();
			
			Calendar calBegin = Calendar.getInstance();
			
			//strURL = "http://download.finance.yahoo.com/d/quotes.csv?f=sl1d1t1c1ohgv&e=.csv&s=JCI,CPWR,IP,ETFC,ABC,AVB";
			String strURL = "http://api.twitter.com/1/statuses/user_timeline.json?screen_name=pikefindotcom&callback=TWTR.Widget.receiveCallback_1&include_rts=true&count=4&clientsource=TWITTERINC_WIDGET&1318454632840=cachebust";
			
			HttpGet httpget = new HttpGet(strURL); 
			
			//httpclient.getParams().setParameter(HttpClientParams.SO_TIMEOUT, new Long(5000));
	
			
			HttpResponse response = httpclient.execute(httpget);
			
			entity = response.getEntity();
			
			Calendar calEnd = Calendar.getInstance();
			
			//lElapsed = (calEnd.getTimeInMillis() - calBegin.getTimeInMillis());
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
					entity.getContent()));
			
			 int nTmp;			
	
			  String returned_content="";
			
			  while ((nTmp = in.read()) != -1)
				returned_content = returned_content + (char)nTmp;
			
	
			  in.close();
			  
			  if (returned_content.contains("retweet_count"))
				  lElapsed = 1;
			  else if (returned_content.contains("\"error\":\"Rate limit exceeded.\""))
				  lElapsed = -3;
			  else 
				  lElapsed = -4;
			  
			  
			
			
		}
		catch(MalformedURLException MFUE)
		{
			lElapsed = -1;
		}
		catch (IOException ioe)
		{
			lElapsed = -2;
		}
		
		
	  
	
				
	 
		
		System.out.println(lElapsed);
	}
	
		
	
	
	public static void yahooDataTest(String[] args)
	{
		long lElapsed = 0;
		
		HttpEntity entity;
		
		try
		{
			String strURL = args[1];
			
			
			
			HttpClient httpclient = new DefaultHttpClient();
			
			Calendar calBegin = Calendar.getInstance();
			
			strURL = "http://download.finance.yahoo.com/d/quotes.csv?f=sl1d1t1c1ohgv&e=.csv&s=JCI,CPWR,IP,ETFC,ABC,AVB";
			
			HttpGet httpget = new HttpGet(strURL); 
			
			//httpclient.getParams().setParameter(HttpClientParams.SO_TIMEOUT, new Long(5000));
	
			
			HttpResponse response = httpclient.execute(httpget);
			
			entity = response.getEntity();
			
			Calendar calEnd = Calendar.getInstance();
			
			lElapsed = (calEnd.getTimeInMillis() - calBegin.getTimeInMillis());
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
					entity.getContent()));
			
			 int nTmp;			
	
			  String returned_content="";
			
			  while ((nTmp = in.read()) != -1)
				returned_content = returned_content + (char)nTmp;
			
	
			  in.close();
			  
			  String[] data = returned_content.split(",");
			  
			  
			  ArrayList<String[]> list = new ArrayList<String[]>();
			  
			  data = UtilityFunctions.extendArray(data);
			  
			  data[data.length-1] = calBegin.getTime().toString();
			  
			  list.add(data);
			  
			  UtilityFunctions.createCSV(list, "/tmp/output.csv", true);
			  
			  if (returned_content.contains("FailureMode=1"))
				  lElapsed=-3;
			
		}
		catch(MalformedURLException MFUE)
		{
			lElapsed = -1;
		}
		catch (IOException ioe)
		{
			lElapsed = -2;
		}
		
		
	  
	
				
	 
		
		System.out.println(lElapsed);
	}
	

}

