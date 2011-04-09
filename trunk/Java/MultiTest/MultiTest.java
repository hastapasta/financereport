import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	

}

