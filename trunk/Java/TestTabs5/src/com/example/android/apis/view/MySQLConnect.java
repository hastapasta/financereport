package com.example.android.apis.view;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MySQLConnect {
	
	public static JSONArray connect(String strConnectURL, ArrayList<String[]> pairs) throws CustomConnectionException
	{
		Log.i("MySQLConnect", "connect");
		String result = "";
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//nameValuePairs.add(new BasicNameValuePair("ticker","GE"));
		//nameValuePairs.add(new BasicNameValuePair("type","weaker"));
		if (pairs != null)
		{
			for (int i=0;i<pairs.size();i++)
			{
				nameValuePairs.add(new BasicNameValuePair(pairs.get(i)[0],pairs.get(i)[1]));
			}
		}
		InputStream is=null;
		//http post
		try
		{
				HttpClient httpclient = new DefaultHttpClient();
				//HttpPost httppost = new HttpPost("http://example.com/getAllPeopleBornAfter.php");
				//HttpPost httppost = new HttpPost("http://10.0.0.19/dev/datatest.php");
				//emulator is currently unable to resolve pikefin.com
				HttpPost httppost = new HttpPost(strConnectURL);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost); 
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
		}
		catch(Exception e)
		{
			Log.e("log_tag", "Error in http connection "+e.toString());
			throw new CustomConnectionException("Unable to connect to Server.");
		}
		//convert response to string
		StringBuilder sb = new StringBuilder();
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
		}
		catch(Exception e)
		{
			Log.e("log_tag", "Error converting result "+e.toString());
			throw new CustomConnectionException("Error reading data.");
		}
	
		//parse json data
		JSONObject json_data;
		JSONArray jArray=null;
		try
		{	
			jArray = new JSONArray(result);
			for(int i=0;i<jArray.length();i++)
			{
				json_data = jArray.getJSONObject(i);
				/*Log.i("log_tag","ticker: "+json_data.getString("ticker")+
						", value2: "+json_data.getDouble("value2")+
						", value: "+json_data.getDouble("value")+
						", frequency: "+json_data.getString("frequency"));*/
			}
		}
		catch(JSONException e)
		{
			Log.e("log_tag", "Error parsing data "+e.toString());
			throw new CustomConnectionException("Invalid data format.");
		}
		
		return(jArray);
		
	}

}

class CustomConnectionException extends Exception
{


	private static final long serialVersionUID = 7938367969464733L;
	
	public CustomConnectionException(String msg)
	{
		super(msg);
	}

	
	
	
}










