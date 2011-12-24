//package com.roeschter.jsl;
 


import java.sql.*;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.HashMap;
//import java.util.List;
import java.util.Properties;
//import java.util.Calendar;
import java.io.*;
//import java.util.StringTokenizer;
import java.util.regex.*;

/*import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.HttpVersion;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;*/

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterBase;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import com.rosaloves.bitlyj.Url;
import static com.rosaloves.bitlyj.Bitly.*;

import org.apache.commons.mail.*;





public class UtilityFunctions
{
	
	//public static Connection con;
	static public boolean bCalledByJsp;
	String strCapturedOutput;
	static CustomBufferedWriter stdoutwriter;
	

	
	/*public Connection getConnection()
	{
		return(UtilityFunctions.con);
		
	}*/
	
	
		
		
	
	
	

		
	
	public UtilityFunctions()
	{
	
		//try
		//{
		try
		{
			/*Class.forName("com.mysql.jdbc.Driver");
			//String url = "jdbc:mysql://localhost:3306/" + strDatabase;
			String url = "jdbc:mysql://" + strDBHost + ":" + strDBPort + "/" + strDatabase;
			UtilityFunctions.con = DriverManager.getConnection(url,strUser, strPass);*/
			//this.bCalledByJsp = bCalled;
			UtilityFunctions.stdoutwriter = new CustomBufferedWriter();
	

		}
		catch (Exception e)
		{
			System.out.println("UtilityFunctions constructor failed.");
			e.printStackTrace();
		}
			
		
		
		
	}
	
	/*
	 * The following 2 functions were written because I thought there was a multithreading 
	 * issue with how HttpClient was being utilitzed. The issue at that time turned out to
	 * be caused by something else. I still think that the current HttpCLient implementation
	 * is thread-safe and I don't want to rework something right now that looks to be working.
	 * But having said that, I'm leaving these functions in here for now in case it's later
	 * deemed necessary to make a change and go with the THreadSafeConnectionManager.
	 * 
	 * 
	 */
	
	/*private void createThreadSafeConnectionManager()
	{
		
		this.params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, 100);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        
        // Create and initialize scheme registry 
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        this.cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        this.httpClient = new DefaultHttpClient(this.cm, this.params);	
				
	}
	
	public HttpClient getHttpClient()
	{
		return(this.httpClient);
	}*/
	
	/*public static void db_update_query(String strUpdateStmt) throws SQLException
	{

	

		//try
		//{
			stdoutwriter.writeln("Executing SQL: " + strUpdateStmt,Logs.SQL,"UF1");
			stdoutwriter.writeln(strUpdateStmt,Logs.SQL,"UF2");
			Statement stmt = con.createStatement();
			stmt.execute(strUpdateStmt);
			if (strUpdateStmt.substring(0,6).equals("update") && stmt.getUpdateCount() == 0)
				stdoutwriter.writeln("No rows were updated",Logs.ERROR,"UF2.5");
				
			
		///}
		//catch (SQLException sqle)
		//{
		//	stdoutwriter.writeln("1 SQL statement failed: " + strUpdateStmt);
		//	stdoutwriter.writeln(sqle);
	//
			
		//}
		
		
		
		
	}*/
	

	
	
	
	
	
	//public static HashMap<String,HashMap<String,String>> convertResultSetToHashMap(ResultSet rs,String strColumnKey) throws SQLException
	public static HashMap<TheKey,HashMap<String,String>> convertResultSetToHashMap(ResultSet rs,String strColumnKey1,String strColumnKey2) throws SQLException
	{
		//HashMap<String,HashMap<String,String>> parentHash = null;
		HashMap<TheKey,HashMap<String,String>> parentHash = null;
	
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();
		String strHashKey1=null;
		String strHashKey2=null;
		
		while (rs.next())
		{
			if (parentHash==null)
				//parentHash = new HashMap<String,HashMap<String,String>>();
				parentHash = new HashMap<TheKey,HashMap<String,String>>();
				
			//String[] tmpArray = new String[numberOfColumns];
			HashMap<String,String> childHash = new HashMap<String,String>();
		
			for (int i=1;i<=numberOfColumns;i++)
			{
				int j = rsMetaData.getColumnType(i);
				String strValue="";
				switch(j)
				{
				
			
					case java.sql.Types.BIT:
						strValue = "false";
						if (rs.getInt(i) != 0)
							strValue = "true";
						break;
						
					case java.sql.Types.DATE:
						if (rs.getDate(i) != null)
							strValue = rs.getDate(i).toString();
						else
							strValue = null;
						break;
						
						
						
					case java.sql.Types.INTEGER:
						strValue = rs.getInt(i) + "";
						break;
						
					case java.sql.Types.VARCHAR:
						strValue = rs.getString(i);
						break;
						
					case java.sql.Types.DECIMAL:
						if (rs.getBigDecimal(i) == null)
							strValue = null;
						else
							strValue = rs.getBigDecimal(i).toString();
						break;
					
					case java.sql.Types.TIMESTAMP:
						if (rs.getDate(i) == null)
							strValue = null;
						else
						{
							//rs.getDate() drops the time						
							strValue = rs.getTimestamp(i).toString();
						}
						break;
						
					default:
						stdoutwriter.writeln("Problem converting ResultSet to HashMap - unhandled column type",Logs.ERROR,"UF18.5");
						throw new SQLException("Custom SQL Exception - Unable to convert ResultSet to HashMap");
						
						
				}
				if (rsMetaData.getColumnName(i).equals(strColumnKey1))
					strHashKey1 = strValue;
				if (rsMetaData.getColumnName(i).equals(strColumnKey2))
					strHashKey2 = strValue;
					
				childHash.put(rsMetaData.getTableName(i)+"."+rsMetaData.getColumnName(i), strValue);
				//tmpArray[i-1] = strValue;
			
			}
			if (strHashKey1 == null) {
				stdoutwriter.writeln("Problem converting ResultSet to HashMap - column for hash key not found",Logs.ERROR,"UF18.7");
				throw new SQLException("Custom SQL Exception - Unable to convert ResultSet to HashMap");
			}
			
			TheKey tk = new TheKey(strHashKey1,strHashKey2);
			//parentHash.put(strHashKey,childHash);
			parentHash.put(tk, childHash);
		}
		
		return(parentHash);
	}
	
	public static ArrayList<String[]> convertResultSetToArrayList2(ResultSet rs) throws SQLException {
		ArrayList<String[]> tmpArrayList = new ArrayList<String[]>();
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();
		int nRow=0;
		
		while (rs.next())
		{
			String[] tmpArray = new String[numberOfColumns];
			for (int i=1;i<=numberOfColumns;i++)
			{
				int j = rsMetaData.getColumnType(i);
				String strValue="";
				switch(j)
				{
				
			
					case java.sql.Types.BIT:
						strValue = "false";
						if (rs.getInt(i) != 0)
							strValue = "true";
						break;
						
					case java.sql.Types.DATE:
						if (rs.getDate(i) != null)
							strValue = rs.getDate(i).toString();
						else
							strValue = null;
						break;
						
					case java.sql.Types.INTEGER:
						strValue = rs.getInt(i) + "";
						break;
						
					case java.sql.Types.VARCHAR:
						strValue = rs.getString(i);
						break;
						
					case java.sql.Types.DECIMAL:
						if (rs.getBigDecimal(i) == null)
							strValue = null;
						else
							strValue = rs.getBigDecimal(i).toString();
						break;
						  
						  //try to find the fact key in fact_data
					case java.sql.Types.TIMESTAMP:
						if (rs.getDate(i) == null)
							strValue = null;
						else
							strValue = rs.getTimestamp(i).toString();
						break;
						
					default:
						stdoutwriter.writeln("Problem converting ResultSet to ArrayList - unhandled column type",Logs.ERROR,"UF18.5");
						throw new SQLException("Custom SQL Exception - Unable to convert ResultSet to ArrayList");
						
						
				}
				//tmpHash.put(rsMetaData.getTableName(i)+"."+rsMetaData.getColumnName(i), strValue);
				tmpArray[i-1] = strValue;
				
			}
			tmpArrayList.add(tmpArray);
			nRow++;
			/*if (nRow==ntmp)
			{
				String tmp = "blap";
			}*/
		}
		
		
		return(tmpArrayList);
		
		
		
		
		
		
		
	}
	
	//public static ArrayList<HashMap<String,String>> convertResultSetToArrayList(ResultSet rs) throws SQLException
	public static ArrayList<HashMap<String,String>> convertResultSetToArrayList(ResultSet rs) throws SQLException
	{
		
		//ArrayList<HashMap<String,String>> tmpArrayList = null;
		ArrayList<HashMap<String,String>> tmpArrayList = null;
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();
		int nRow=0;
		//int ntmp = 200;
		while (rs.next())
		{
			if (tmpArrayList == null)
				//tmpArrayList = new ArrayList<HashMap<String,String>>();
				tmpArrayList = new ArrayList<HashMap<String,String>>();
			
			//HashMap<String,String> tmpHash = new HashMap<String,String>();
			HashMap<String,String> tmpHash = new HashMap<String,String>();
			for (int i=1;i<=numberOfColumns;i++)
			{
				int j = rsMetaData.getColumnType(i);
				String strValue="";
				switch(j)
				{
				
			
					case java.sql.Types.BIT:
						strValue = "false";
						if (rs.getInt(i) != 0)
							strValue = "true";
						break;
						
					case java.sql.Types.DATE:
						if (rs.getDate(i) != null)
							strValue = rs.getDate(i).toString();
						else
							strValue = null;
						break;
						
					case java.sql.Types.INTEGER:
						strValue = rs.getInt(i) + "";
						break;
						
					case java.sql.Types.VARCHAR:
						strValue = rs.getString(i);
						break;
						
					case java.sql.Types.DECIMAL:
						if (rs.getBigDecimal(i) == null)
							strValue = null;
						else
							strValue = rs.getBigDecimal(i).toString();
						break;
						  
						  //try to find the fact key in fact_data
					case java.sql.Types.TIMESTAMP:
						if (rs.getDate(i) == null)
							strValue = null;
						else
							strValue = rs.getTimestamp(i).toString();
						break;
						
					default:
						stdoutwriter.writeln("Problem converting ResultSet to ArrayList - unhandled column type",Logs.ERROR,"UF18.5");
						throw new SQLException("Custom SQL Exception - Unable to convert ResultSet to ArrayList");
						
						
				}
				tmpHash.put(rsMetaData.getTableName(i)+"."+rsMetaData.getColumnName(i), strValue);
				
			}
			tmpArrayList.add(tmpHash);
			nRow++;
			/*if (nRow==ntmp)
			{
				String tmp = "blap";
			}*/
		}
		
		
		return(tmpArrayList);
	}
	
	
	
	
	public static void createCSV(ArrayList<String[]> tabledata,String filename,boolean append)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename,append));
			String strLine="";
			String[] rowdata;
	
	    for (int x=0;x<tabledata.size();x++)
	    {
	    	rowdata = tabledata.get(x);
	    	strLine="";
				for (int y=0;y<rowdata.length;y++)
				{
					if (y !=0)
						strLine = strLine +",";
						
		    	strLine = strLine + "\"" + rowdata[y] + "\"";
		    	   
		    }
		    writer.write(strLine);
		    writer.newLine();
	     
	    }
	
	
	    writer.close();  // Close to unlock and flush to disk.
	  }
	  catch (IOException ioe)
	  {
	  	stdoutwriter.writeln("Problem writing CSV file",Logs.ERROR,"UF18");
	  	stdoutwriter.writeln(ioe);
	  }
		
	}
	
	public static ArrayList<String[]> convertCSV(String strData, String strDelimiter, String strEncloseString) {
		//not fully implemented yet
		return null;
		
		
	}
	
	
	public static ArrayList<String[]> readInCSV(String strFilename, String strDelimiter, String strEncloseString)
	{
		/*This code will not correctly handle the delimiter character (e.g. ",") being inside a quoted
		string (e.g. "5,600,000"). One way to handle this is to do the following:
		1) Replace ,,, with |||
		2) Replace ,, with ||
		3) Replace "," with "|"
		4) Replace , with <blank>
		5) Replace | with ,
		
		You may optionally need to do:
		6) Replace ,, with ,"", (2 X)
		*/
		int nLineCount=0;
		String nTmp="";
		try
		{
			String strCurToken;
			//StringTokenizer st;
			int nTokenCount;
			ArrayList<String[]> arraylist = new ArrayList<String[]>();
			ArrayList<String> rowarraylist;
			boolean bEncloseBefore = false;
			boolean bEncloseAfter = false;
			
			BufferedReader in = new BufferedReader(new FileReader(strFilename));
			
			while ((nTmp = in.readLine()) != null)
			{
				/*if ((nLineCount < 70) || (nLineCount > 80))
					continue;*/
					
				rowarraylist = new ArrayList<String>();
				/*was using string tokenizer, switch to split() since it returns tokens for consecutive delimiters, e.g ,, */
				//st = new StringTokenizer(nTmp, strDelimiter);
				String[] tokens = nTmp.split(strDelimiter);
				nTokenCount = 0;
				for (int z=0;z<tokens.length;z++)
				{
					//strCurToken = st.nextToken();
					strCurToken = tokens[z];
					bEncloseBefore = false;
					bEncloseAfter = false;
	
					if (strCurToken.length() > 2)
					{
						if (strCurToken.substring(0,1).compareTo(strEncloseString) == 0)
							bEncloseBefore = true;
						if (strCurToken.substring(strCurToken.length() -1,strCurToken.length()).compareTo(strEncloseString) == 0)
							bEncloseAfter = true;
						if (((bEncloseBefore == true) && (bEncloseAfter != true)) ||
								((bEncloseBefore != true) && (bEncloseAfter == true)))
						/* there's an issue with the formatting where there's only one enclosure character*/
						{
							stdoutwriter.writeln("Problem with enclosure characters @ line " + nLineCount,Logs.ERROR,"UF19");
							return(null);
						}
						if ((bEncloseBefore == true) && (bEncloseAfter == true))
						/* Strip out the enclosure characters */
						{
					
							strCurToken = strCurToken.substring(1,strCurToken.length() - 1);
						}
					}
					rowarraylist.add(strCurToken);
					
						
					nTokenCount++;
				}
				
				String[] strArray = new String[rowarraylist.size()];
				for (int i =0;i<rowarraylist.size();i++)
				{
					strArray[i] = rowarraylist.get(i);
					if (i==0)
						stdoutwriter.writeln(strArray[i],Logs.STATUS2,"UF20");
					else
						stdoutwriter.writeln("," + strArray[i],Logs.STATUS2,"UF21");
				}

				
				arraylist.add(strArray);
							
				nLineCount++;	
			}
			stdoutwriter.writeln(nLineCount + " lines processed.",Logs.STATUS2,"UF22");
			return(arraylist);
			
		} 
		catch (Exception e)
		{
			stdoutwriter.writeln("Problem reading CSV @ line " + nLineCount,Logs.ERROR,"UF23");
			stdoutwriter.writeln(e);
			return(null);
		}
	}
	
	
	
	public static void scrubCSV(String strFilename, String strDelimiter, String strEnclosure, String strOutFilename)
	{
		String strCurLine;
		String strNewLine = "";
		BufferedReader in = null;
		BufferedWriter out = null;
		try
		{
		
			in = new BufferedReader(new FileReader(strFilename));
			
			out = new BufferedWriter(new FileWriter(strOutFilename));
			
			
		
			Matcher matcher;
			
			String strRegex = "(" + strEnclosure + ")";
			Pattern pattern = Pattern.compile(strRegex);
			
			//int nOpenEnclosure, nCloseEnclosure;
			int nCurOffset=0;
			int nLineCount =0;
	
	    
			while((strCurLine = in.readLine()) != null)
			{
				strNewLine = "";
				matcher = pattern.matcher(strCurLine);
				stdoutwriter.writeln(strCurLine,Logs.STATUS2,"UF25");
				//nOpenEnclosure = 0;
				//nCloseEnclosure = 0;
				nCurOffset=0;
				while (true)
				{
					//search for opening enclosure
					//System.out.println("1 curOffset: " + nCurOffset);
					if (matcher.find(nCurOffset) == false)
						break;
					//else
						//nOpenEnclosure = matcher.end();
					
					strNewLine = strNewLine + strCurLine.substring(nCurOffset,matcher.end());
					nCurOffset = matcher.end();
					//System.out.println(strNewLine);
					
					//search for ending enclosure
					if (matcher.find(nCurOffset) == false)
					{
						stdoutwriter.writeln("Invalid syntax for enclosure characters at line " + nLineCount,Logs.ERROR,"UF26");
						break;
					}
					//else
						//nCloseEnclosure = matcher.end();
					
					strNewLine = strNewLine + strCurLine.substring(nCurOffset,matcher.end()).replace(strDelimiter,"");
					//System.out.println(strNewLine);
					//System.out.println(strCurLine.length());
					if (strCurLine.length() == matcher.end())
						break;
					else
						nCurOffset = matcher.end();
					
					
					
				}
			
				
				out.write(strNewLine);
				out.newLine();
				stdoutwriter.writeln(strNewLine,Logs.STATUS2,"UF27");
				nLineCount++;	
			}
			out.close();
			in.close();
			
		}
		catch(Exception e)
		{
			stdoutwriter.writeln("Problem Scrubbing csv file",Logs.ERROR,"UF28");
			stdoutwriter.writeln(e);
		}

		
		
		
		
		
	}
	

	
	public static String getElapsedTimeHoursMinutesSecondsString(Long elapsedTime) 
	{        
		String format = String.format("%%0%dd", 2);   
	    elapsedTime = elapsedTime / 1000;   
	    String seconds = String.format(format, elapsedTime % 60);   
	    String minutes = String.format(format, (elapsedTime % 3600) / 60);   
	    String hours = String.format(format, elapsedTime / 3600);   
	    String time =  hours + ":" + minutes + ":" + seconds;   
	    return time;   
	}  

	
	
	
	public static String[] extendArray(String[] inputArray)
	{
		String[] tmpArray = new String[inputArray.length + 1];
		for (int i=0; i<inputArray.length;i++)
		{
			tmpArray[i] = inputArray[i];
		}
		return(tmpArray);
		
	}
	
	public static void mail(String strEmail, String strMessage, String strSubject, String strFromAddy)
	  {	
	  	//String host = "smtp.gmail.com";
	  	//int port = 587;
	  	//String username = "hastapasta99";
	  	//String password = "ginger1.";
		String username = (String)DataLoad.props.getProperty("emailuser");
		String password = (String)DataLoad.props.getProperty("emailpass");

	  	Properties props = new Properties();
	  	//props.put("mail.smtp.port","587");
	  	//props.put("mail.smtp.host", "smtp.gmail.com");
	  	props.put("mail.smtp.port", Integer.parseInt((String)DataLoad.props.getProperty("emailport")));
	  	props.put("mail.smtp.host", (String)DataLoad.props.getProperty("emailhost"));
	  	props.put("mail.smtp.auth", "true");
	  	props.put("mail.smtp.starttls.enable", "true");
	  	props.put("mail.debug", "false");
	  	
	  	/*
	  	 * Setting these timeout values since sending mail sometimes hangs.
	  	 */
	  	props.put("mail.smtp.timeout", 60000);
	  	props.put("mail.smtp.connectiontimeout", 60000);


	  	//Session session = Session.getInstance(props);
	  	Session session = Session.getInstance(props,new MyPasswordAuthenticator(username, password));



	  	try {

		    Message message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(strFromAddy));
		    message.setRecipients(Message.RecipientType.TO, 
	                      InternetAddress.parse(strEmail));
		    message.setSubject(strSubject);
		    message.setText(strMessage);

		    //Transport transport = session.getTransport("smtp");
		    //transport.connect(host, port, username, password);
		    //transport.connect(host,username,password);
		    
		    
		    //emails are disabled for testing
		    if (DataLoad.bDebugMode == false)
		    	Transport.send(message);

		    DataLoad.nMailMessageCount++;	

	  	} catch (MessagingException e) {
	  		
	  	    //throw new RuntimeException(e);
	  		stdoutwriter.writeln("Problem sending email.",Logs.ERROR,"UF28.5");
	  		stdoutwriter.writeln("Message Count: " + DataLoad.nMailMessageCount,Logs.ERROR,"UF28.52");
	  		stdoutwriter.writeln(e);
	  	}
	  	
	  	
	  }
	
	public static void htmlMail(String strEmail, String strMessage, String strSubject) {
		
		HtmlEmail email = new HtmlEmail();

		try {
			email.setHostName((String)DataLoad.props.getProperty("emailhost"));
			email.setAuthentication((String)DataLoad.props.getProperty("emailuser"), (String)DataLoad.props.getProperty("emailpass"));
			email.setSmtpPort(Integer.parseInt((String)DataLoad.props.getProperty("emailport")));
			email.setFrom((String)DataLoad.props.getProperty("fromaddy"));
			email.addTo(strEmail);
			email.setSubject(strSubject);
	
			email.setTextMsg("Pikefin Alert");
			email.setHtmlMsg(strMessage);
			email.setTLS(true);
	
			email.setDebug(false);
			
	
			email.send();
		}
		catch (EmailException ee) {
			stdoutwriter.writeln("Problem sending html email.",Logs.ERROR,"UF30.5");
	  		//stdoutwriter.writeln("Message Count: " + DataLoad.nMailMessageCount,Logs.ERROR,"UF28.52");
	  		stdoutwriter.writeln(ee);
			
		}

		
	}
	
	//public static void tweet(String strEmail, String strMessage, String strSubject, String strFromAddy)
	public static String tweet(String strTweet,String strUser, String strPass, String strAuth1, String strAuth2, String strAuth3, String strAuth4) {	
	  	//String host = "smtp.gmail.com";
	  	//int port = 587;
	  	//String username = "hastapasta99";
	  	//String password = "madmax1.";
		
		//boolean bSuccess = true;
		String strErrorMsg = "";
		try	{
			ConfigurationBuilder cb = new ConfigurationBuilder();
	
			/*cb.setUser("pikefindotcom");
			cb.setPassword("ginger1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("5Fe0I6bfXMgizg1MQ1jMQ")
			  .setOAuthConsumerSecret("sSbfFG5GEqAxUqSy1newCgkEAvTuIHf7y9HwB2QNA0")
			  .setOAuthAccessToken("260464945-DQseZfWVTzAzuOSqz8ZkVCJqSm2Y44p5TQdVxhzU")
			  .setOAuthAccessTokenSecret("1UwoxCB63MhTPSs0EECCHLAPxn6p9iKZNqM298Sqs88");
			
			
			cb.setUser("pfin_forex");
			cb.setPassword("madmax1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("KPkMWKCAV8CYXQeyCG9VA")
			  .setOAuthConsumerSecret("cp4bW9UgQ04NWK9pcQHjesUiR6Ya37ahtvdOXOGyCOw")
			  .setOAuthAccessToken("382701296-AZNdnD7E5xZK610LZHYD4IkqxinI12Td9hA5CQNy")
			  .setOAuthAccessTokenSecret("KhnKstEri9xMA7oeoLzzY4br9H4iyjBjMvCNEpvahcQ");
			
			cb.setUser("pfin_commodity");
			cb.setPassword("madmax1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("7GWDzfijJ9KGaW9q8oLOvw")
			  .setOAuthConsumerSecret("AbkTMtmMdC3IEOnyxJw1SB5EHcXXfdlEt6pEPif6HnQ")
			  .setOAuthAccessToken("382679101-KBaGgDu8ry1Rl2VwlNGpXW7WzM2L4FgCt5Fyrabb")
			  .setOAuthAccessTokenSecret("V5IKgsNQCBccJy3w6DW4Keuk9Qh95MJkVQtsVrIuM");
			
			cb.setUser("pfin_us_stocks");
			cb.setPassword("madmax1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("wyxaktdjEu6OmQWEcRdCw")
			  .setOAuthConsumerSecret("PuXYGpfWbW1OE1RnmC0YlF8gHxiUwbmDspMhrmrcSW0")
			  .setOAuthAccessToken("382684624-adVHfgSxLXiJK8Xr6RHINraC8HBr626gSbNNr2IB")
			  .setOAuthAccessTokenSecret("eKdTKG1602YW1kDOqpzFScbg3pH56MJnr3TVHVEtk");
			
			cb.setUser("pfin_equity_idx");
			cb.setPassword("madmax1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("nuRiyfSn9kLiXXCNtIu7g")
			  .setOAuthConsumerSecret("00lfsRJEhpwtEiN3rIo7TSp24ykvNbm4DjLmVZUs9sI")
			  .setOAuthAccessToken("382688292-2VkgKjeZUp1qcPsURlMveGqmBenFvl6lg04f3ptb")
			  .setOAuthAccessTokenSecret("fSuZttQlp7RiJGxDHeudg9t74AZc1Ksj5WLNfyj7uU");*/
			
			/*cb.setUser("pfin_gdp");
			cb.setPassword("madmax1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("PWXUU8IZMw9ECnyHulpaAQ")
			  .setOAuthConsumerSecret("iMWNlt0epLYVoCa7WNqHa9wsd2Vxd2cEKKBUg3NqE")
			  .setOAuthAccessToken("382694496-BGmNTLvseK7HXUC49271xJzArE3bHBJdeZr3fe8")
			  .setOAuthAccessTokenSecret("M1ECA4FJadLK4WY5ZVQDAB0T3nRHlRpdjx1L4opu0U");*/
			
			cb.setUser(strUser);
			cb.setPassword(strPass);
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey(strAuth1)
			  .setOAuthConsumerSecret(strAuth2)
			  .setOAuthAccessToken(strAuth3)
			  .setOAuthAccessTokenSecret(strAuth4);
			
			

			
					
			TwitterFactory tf = new TwitterFactory(cb.build());
			
			Twitter twitter = tf.getInstance();
		
			
			/*c.setUser(strUser);
			c.setPassword(strPass);
			c.setOtherThings(UNIQUE);
			c.setOAuthConsumer/Secret(CONSTANT);
			c.OAuthAccessToken/Secret(UNIQUE);*/
			
			//AccessToken accessToken = loadAccessToken(Integer.parseInt(args[0]));
			try
			{
				//RequestToken requestToken = twitter.getOAuthRequestToken();
				
				
				//twitter.getOAuthRequestToken("x");

				/*AccessToken accessToken = null;
				while (null == accessToken)
				{
					#*System.out.println("Open the following URL and grant access to your account:");
					System.out.println(requestToken.getAuthorizationURL());
					System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
					String pin = br.readLine();*#
					String pin = "ginger1.";
					try
					{
						if (pin.length() > 0)
						{
						    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
						} 
						else
						{
						    accessToken = twitter.getOAuthAccessToken(requestToken);
						}
					} 
					catch (TwitterException te)
					{
						stdoutwriter.writeln("Problem authenticating with twitter",Logs.ERROR,"UF45.5");
						stdoutwriter.writeln(te);
						#*if (401 == te.getStatusCode()) {
						    System.out.println("Unable to get the access token.");
						} else {
						    te.printStackTrace();
						}*#
					}
				}	*/
			  
			}
			catch (IllegalStateException ie)
			{
				
			    // access token is already available, or consumer key/secret is not set.
				if (!twitter.getAuthorization().isEnabled())
				{
				    //System.out.println("OAuth consumer key/secret is not set.");
					stdoutwriter.writeln("OAuth consumer key/secret is not set.",Logs.ERROR,"UF47.5");
					strErrorMsg = "OAuth consumer key/secret is not set.";
				        //System.exit(-1);
				}
				else
					strErrorMsg = ie.getMessage();
				return(strErrorMsg);
			}
			
			if (strTweet.length() > 140)
			{
			
				stdoutwriter.writeln("Tweet longer than 140 characters; tweet truncated.",Logs.WARN,"UF49.5");
				stdoutwriter.writeln(strTweet,Logs.WARN,"UF49.5");
				strErrorMsg = "Tweet longer than 140 characters; tweet truncated.";
			}
				
			
			if (DataLoad.bDebugMode == false)
				twitter.updateStatus(strTweet);
		
		}
		catch (TwitterException te)
		{
		    //te.printStackTrace();
		    //System.out.println("Failed to get timeline: " + te.getMessage());
		    //System.exit(-1);
			stdoutwriter.writeln("Problem sending tweet",Logs.ERROR,"UF48.5");
			stdoutwriter.writeln(te);
			strErrorMsg = te.getMessage();
		}
		
		
		return(strErrorMsg);
		
		    
	}
	  	
	public static String shortenURL(String strInputURL)
	{
		
		try
		{
			Url url = as("pikefin", "R_f08326b12abd18288243b65ef2c71c40").call(shorten(strInputURL));
			return(url.getShortUrl());
		}
		catch (Exception ex)
		{
			stdoutwriter.writeln("Failed to shorten url: " + strInputURL,Logs.ERROR,"UF51.5");
			stdoutwriter.writeln(ex);
		}
		
		return("");

		
		
		
	}  
	


	
	
	
	
}
	
	class TestException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4426155592602941937L;

		/*void TestException()
		{
			return;
		}*/
		
		
	}
	
	class SkipLoadException extends Exception
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6553765592848567969L;
		
	}
	

	class MyPasswordAuthenticator extends Authenticator 
	{
		String user;
		String pw;
		public MyPasswordAuthenticator (String username, String password)
		{
			super();
			this.user = username;
			this.pw = password;
		}
		public PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(user, pw);
			
		}
	}
	
	
		 
		 
		
		
		
	

	
	 

	
	
	
	







