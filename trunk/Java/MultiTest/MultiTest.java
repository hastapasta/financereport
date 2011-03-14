import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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

}
