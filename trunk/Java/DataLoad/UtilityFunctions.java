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
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.rosaloves.bitlyj.Url;
import static com.rosaloves.bitlyj.Bitly.*;




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
	

	
	
	
	/*public static ResultSet db_run_query(String query) throws SQLException
	{

		ResultSet rs =null;
		//try
		//{

			stdoutwriter.writeln("Executing query: " + query, Logs.SQL,"UF3");
			Statement stmt = con.createStatement();
			stdoutwriter.writeln(query, Logs.SQL,"UF4");
			rs = stmt.executeQuery(query);

	
		}
		
		
		return(rs);
		//Not going to worry about closing the connection, we'll let it be garbage collected.
		
		
		
	}*/
	
	/*public void importTableIntoDB(ArrayList<String[]> tabledata, String tablename, Integer nBatch)
	{
		#* This function expects an arraylist with 2X of the number of values to be inserted with each value
		preceeded by the datatype with the current 3 datatypes being VARCHAR, FUNCTIONS, INT *#
		#*OFP 9/28/2010 - I believe passing in the datatypes with the table data is redundant since the types
		 * are retrieved from the database meta data.
		 *#
		String[] rowdata;
		String query ="";
		//String columns= "";
		String values = "";
		
		if (tabledata.size() < 2)
		{
			stdoutwriter.writeln("Not enough rows of data passed into importTableIntoDB\nThis may be a custom import and the flag needs to be set",Logs.ERROR,"UF4.5");
			return;
		}
		
		String[] columnnames = tabledata.get(0);
		tabledata.remove(0);
		
		if (tablename.equals("fact_data_stage") || tablename.equals("fact_data"))
		{
				columnnames = extendArray(columnnames);
				columnnames[columnnames.length - 1] = "batch";
				
		}
		
		String[] datatypes = new String[columnnames.length];
	
		try
		{
			ResultSet rsColumns = null;
			DatabaseMetaData meta = con.getMetaData();

		
	
			int nCount=0;
			for (int j=0;j<columnnames.length;j++)
			{	
	
			  rsColumns = meta.getColumns(null, null, tablename, null);

			  nCount=0;
	    	while (rsColumns.next())
				{
		
					#*System.out.println(rsColumns.getString("COLUMN_NAME"));
					System.out.println(rsColumns.getString("TYPE_NAME"));*#
					//stdoutwriter.writeln(columnnames[j],Logs.STATUS2,"UF5");
					if (columnnames[j].compareTo(rsColumns.getString("COLUMN_NAME")) == 0)
					{
						datatypes[j] = rsColumns.getString("TYPE_NAME");
						stdoutwriter.writeln(datatypes[j],Logs.STATUS2,"UF6");
						break;
					}
					nCount++;
				}
			}
		}
		catch (SQLException sqle)
		{
			stdoutwriter.writeln("Problem retrieving column data types",Logs.ERROR,"UF7");
			stdoutwriter.writeln(sqle);
		}
		stdoutwriter.writeln("Finished retrieving column data types",Logs.STATUS2,"UF8");
		String strColumns;
	
		int nInsertCount=0;
		
	
		
		
		for (int x=0;x<tabledata.size();x++)
		{
			//for debugging purposes
			//if (x!= 5052)
				//continue;
				
			rowdata = tabledata.get(x);
		
			//query = "INSERT INTO fact_data_stage (data_set,value,quarter,ticker,date_collected) VALUES ('" + strCurDataSe
			values ="";
			strColumns="";
			
			if (tablename.equals("fact_data_stage") || tablename.equals("fact_data"))
			{
					rowdata = extendArray(rowdata);
					//using columnnames here since that is guaranteed to be the correct length
					rowdata[columnnames.length - 1] = Integer.toString(nBatch);
					
			}
			
			
			for (int y=0;y<columnnames.length;y++)
			{
				
				if (y!=0)
				{
					values = values + ",";
					strColumns = strColumns + ",";
				}

				//System.out.println("{" + rowdata[y] + "} " + rowdata[y].length() + " " + datatypes[y]);
				if (datatypes[y].compareTo("VARCHAR") == 0)
				{
					values = values + "'" + rowdata[y].replace("'","\\'") + "'";
				}
				else if (((datatypes[y].compareTo("INT") == 0) || (datatypes[y].compareTo("BIGINT") == 0)) && ((rowdata[y].length() == 0)))
				{

					values = values + "'0'";
				}
				else
					values = values + rowdata[y];	
				
					
				strColumns = strColumns + columnnames[y];

				
			}
			query = "insert into " + tablename + " (" + strColumns + ") values (" + values + ")";
			//System.out.println("insert row: " + query);
			try
			{
				db_update_query(query);
				nInsertCount++;
			}
			catch (SQLException sqle)
			{
				stdoutwriter.writeln("SQLException failed at row " + (x+1) + " " + query,Logs.ERROR,"UF9");
				stdoutwriter.writeln(sqle);
			}
		
		}
		stdoutwriter.writeln(nInsertCount + " records inserted in db.",Logs.STATUS2,"UF10");
	}*/
	
	/*public void updateTableIntoDB(ArrayList<String[]> tabledata, String tablename)
	{
		
		#*OFP 9/28/2010 - I believe passing in the datatypes with the table data is redundant since the types
		 * are retrieved from the database meta data.
		 *#
		String[] rowdata;
		String query ="";
		//String columns= "";
		//String values = "";
		
		String[] columnnames = tabledata.get(0);
		tabledata.remove(0);
		
		String[] datatypes = new String[columnnames.length];
	
		try
		{
			ResultSet rsColumns = null;
			DatabaseMetaData meta = con.getMetaData();

		
	
			int nCount=0;
			for (int j=0;j<columnnames.length;j++)
			{	
	
			  rsColumns = meta.getColumns(null, null, tablename, null);

			  nCount=0;
	    	while (rsColumns.next())
				{
		
					#*System.out.println(rsColumns.getString("COLUMN_NAME"));
					System.out.println(rsColumns.getString("TYPE_NAME"));*#
					stdoutwriter.writeln(columnnames[j],Logs.STATUS2,"UF11");
					if (columnnames[j].compareTo(rsColumns.getString("COLUMN_NAME")) == 0)
					{
						datatypes[j] = rsColumns.getString("TYPE_NAME");
						stdoutwriter.writeln(datatypes[j],Logs.STATUS2,"UF12");
						break;
					}
					nCount++;
				}
			}
		}
		catch (SQLException sqle)
		{
			stdoutwriter.writeln("Problem retrieving column data types",Logs.ERROR,"UF13");
			stdoutwriter.writeln(sqle);
		}
		stdoutwriter.writeln("Finished retrieving column data types",Logs.STATUS2,"UF14");
		//String strColumns;
	
		int nInsertCount=0;
		for (int x=0;x<tabledata.size();x++)
		{
			//for debugging purposes
			//if (x!= 5052)
				//continue;
				
			rowdata = tabledata.get(x);
		
			
			//values ="";
			//strColumns="";
			
			
			
			
			query = "update " + tablename + " set " + columnnames[1] + "='" + rowdata[1] + "' where " + columnnames[0] + "='" + rowdata[0] + "'";
			
			try
			{
				db_update_query(query);
				nInsertCount++;
			}
			catch (SQLException sqle)
			{
				stdoutwriter.writeln("SQLException failed at row " + (x+1) + " " + query,Logs.ERROR,"UF15");
				stdoutwriter.writeln(sqle);
			}
		
		}
		stdoutwriter.writeln("Updated " + nInsertCount + " records.",Logs.STATUS2,"UF16");
	}*/
	
	/*public void customDataCheck(ArrayList<String[]> tabledata, String strTicker)
	{
		#*This is a custom function to compare the begin_fiscal_calendar from the nasdaq and the sec
		 * websites.
		 *#
		try
		{
			String query = "select begin_fiscal_calendar from company where ticker='" + strTicker + "'";
			ResultSet rs = db_run_query(query);
			rs.next();
			String strNasdaqValue = rs.getString("begin_fiscal_calendar");
			
			if (strNasdaqValue.compareTo(tabledata.get(1)[1]) != 0)
				System.out.println("VALUES DO NOT MATCH. Nasdaq: " + strNasdaqValue + ". SEC: " + tabledata.get(1)[1]);
				
			
		}
		catch (SQLException sqle)
		{
			stdoutwriter.writeln("SQL statement failed.",Logs.ERROR,"UF17");
			stdoutwriter.writeln(sqle);
		
		}
		
		
	}*/
	
	/*public static void getColumnNames(ResultSet rs) throws SQLException {
	    if (rs == null) {
	      return;
	    }
	    ResultSetMetaData rsMetaData = rs.getMetaData();
	    int numberOfColumns = rsMetaData.getColumnCount();

	    // get the column names; column indexes start from 1
	    for (int i = 1; i < numberOfColumns + 1; i++) {
	      String columnName = rsMetaData.getColumnName(i);
	      // Get the name of the column's table name
	      String tableName = rsMetaData.getTableName(i);
	      System.out.println("column name=" + columnName + " table=" + tableName + "");
	    }
	  }*/
	
	//public static HashMap<String,HashMap<String,String>> convertResultSetToHashMap(ResultSet rs,String strColumnKey) throws SQLException
	public static HashMap<String,HashMap<String,String>> convertResultSetToHashMap(ResultSet rs,String strColumnKey) throws SQLException
	{
		//HashMap<String,HashMap<String,String>> parentHash = null;
		HashMap<String,HashMap<String,String>> parentHash = null;
	
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();
		String strHashKey=null;
		
		while (rs.next())
		{
			if (parentHash==null)
				//parentHash = new HashMap<String,HashMap<String,String>>();
				parentHash = new HashMap<String,HashMap<String,String>>();
				
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
							//rs.getData() drops the time						
							strValue = rs.getTimestamp(i).toString();
						}
						break;
						
					default:
						stdoutwriter.writeln("Problem converting ResultSet to HashMap - unhandled column type",Logs.ERROR,"UF18.5");
						throw new SQLException("Custom SQL Exception - Unable to convert ResultSet to HashMap");
						
						
				}
				if (rsMetaData.getColumnName(i).equals(strColumnKey))
					strHashKey = strValue;
					
				childHash.put(rsMetaData.getTableName(i)+"."+rsMetaData.getColumnName(i), strValue);
				//tmpArray[i-1] = strValue;
			
			}
			if (strHashKey == null)
			{
				stdoutwriter.writeln("Problem converting ResultSet to HashMap - column for hash key not found",Logs.ERROR,"UF18.7");
				throw new SQLException("Custom SQL Exception - Unable to convert ResultSet to HashMap");
			}
			parentHash.put(strHashKey,childHash);
		}
		
		return(parentHash);
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
	
	public static void loadCSV(String filename)
	//not quite functional yet
	{
	
		filename = filename.replace("\\","\\\\");
	
		//String query = "LOAD DATA INFILE '" + filename + "' REPLACE INTO TABLE 'fact_data_stage' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n'('ticker','data_set','value')";
		//db_update_query(query);

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
	  	String username = "hastapasta99";
	  	String password = "madmax1.";

	  	Properties props = new Properties();
	  	props.put("mail.smtp.port","587");
	  	props.put("mail.smtp.host", "smtp.gmail.com");
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
		    if (DataLoad.bDisableEmails == false)
		    	Transport.send(message);

		    DataLoad.nMailMessageCount++;	

	  	} catch (MessagingException e) {
	  		
	  	    //throw new RuntimeException(e);
	  		stdoutwriter.writeln("Problem sending email.",Logs.ERROR,"UF28.5");
	  		stdoutwriter.writeln("Message Count: " + DataLoad.nMailMessageCount,Logs.ERROR,"UF28.52");
	  		stdoutwriter.writeln(e);
	  	}
	  	
	  	
	  }
	
	//public static void tweet(String strEmail, String strMessage, String strSubject, String strFromAddy)
	public static void tweet(String strTweet)
	{	
	  	//String host = "smtp.gmail.com";
	  	//int port = 587;
	  	//String username = "hastapasta99";
	  	//String password = "madmax1.";
		try
		{
			Twitter twitter = new TwitterFactory().getInstance();
			try
			{
				RequestToken requestToken = twitter.getOAuthRequestToken();
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
				        //System.exit(-1);
				}
			}
			
			if (strTweet.length() > 140)
			{
				stdoutwriter.writeln("Tweet longer than 140 characters; tweet truncated.",Logs.WARN,"UF49.5");
				stdoutwriter.writeln(strTweet,Logs.WARN,"UF49.5");
			}
				
			/*
			 * Right now we're using the same property for both emails and tweets for 
			 * global disable.
			 */
			//if (DataLoad.bDisableEmails == false)
				twitter.updateStatus(strTweet);
		
		}
		catch (TwitterException te)
		{
		    //te.printStackTrace();
		    //System.out.println("Failed to get timeline: " + te.getMessage());
		    //System.exit(-1);
			stdoutwriter.writeln("Problem sending tweet",Logs.ERROR,"UF48.5");
			stdoutwriter.writeln(te);
		}
		    
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

	
	 

	
	
	
	







