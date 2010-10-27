package com.roeschter.jsl;
 

import java.sql.*;
import java.util.ArrayList;
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






public class UtilityFunctions
{
	
	public static Connection con;
	static public boolean bCalledByJsp;
	String strCapturedOutput;
	static CustomBufferedWriter stdoutwriter;
	

	
	public Connection getConnection()
	{
		return(UtilityFunctions.con);
		
	}
	
	
		
		
	
	
	

		
	
	public UtilityFunctions(String strDatabase, String strUser, String strPass, String strFullLog, String strErrorLog, String strSQLLog, String strThreadLog)
	{
	
		//try
		//{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/" + strDatabase;
			UtilityFunctions.con = DriverManager.getConnection(url,strUser, strPass);
			//this.bCalledByJsp = bCalled;
			UtilityFunctions.stdoutwriter = new CustomBufferedWriter(strFullLog, strErrorLog, strSQLLog,strThreadLog, true, true, true);
	

		}
		catch (Exception e)
		{
			System.out.println("UtilityFunctions constructor failed.");
			e.printStackTrace();
		}
			
		
		
		
	}
	
	public static void db_update_query(String strUpdateStmt) throws SQLException
	{

	

		//try
		//{
			stdoutwriter.writeln("Executing SQL: " + strUpdateStmt,Logs.SQL,"UF1");
			stdoutwriter.writeln(strUpdateStmt,Logs.SQL,"UF2");
			Statement stmt = con.createStatement();
			stmt.execute(strUpdateStmt);
			if (strUpdateStmt.substring(0,6).equals("update") && stmt.getUpdateCount() == 0)
				stdoutwriter.writeln("No rows were updated",Logs.ERROR,"UF2.5");
				
			
		/*}
		catch (SQLException sqle)
		{
			stdoutwriter.writeln("1 SQL statement failed: " + strUpdateStmt);
			stdoutwriter.writeln(sqle);
	
			
		}*/
		
		
		
		
	}
	

	
	
	
	public static ResultSet db_run_query(String query) throws SQLException
	{

		ResultSet rs =null;
		//try
		//{

			stdoutwriter.writeln("Executing query: " + query, Logs.SQL,"UF3");
			Statement stmt = con.createStatement();
			stdoutwriter.writeln(query, Logs.SQL,"UF4");
			rs = stmt.executeQuery(query);

	
		/*}
		catch (SQLException sqle)
		{
			stdoutwriter.writeln("About to throw SQLExcpetion");
			throw sqle;
		
		}*/
		
		return(rs);
		/* Not going to worry about closing the connection, we'll let it be garbage collected.*/
		
		
		
	}
	
	public void importTableIntoDB(ArrayList<String[]> tabledata, String tablename, Integer nBatch)
	{
		/* This function expects an arraylist with 2X of the number of values to be inserted with each value
		preceeded by the datatype with the current 3 datatypes being VARCHAR, FUNCTIONS, INT */
		/*OFP 9/28/2010 - I believe passing in the datatypes with the table data is redundant since the types
		 * are retrieved from the database meta data.
		 */
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
		
					/*System.out.println(rsColumns.getString("COLUMN_NAME"));
					System.out.println(rsColumns.getString("TYPE_NAME"));*/
					stdoutwriter.writeln(columnnames[j],Logs.STATUS2,"UF5");
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
	}
	
	public void updateTableIntoDB(ArrayList<String[]> tabledata, String tablename)
	{
		
		/*OFP 9/28/2010 - I believe passing in the datatypes with the table data is redundant since the types
		 * are retrieved from the database meta data.
		 */
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
		
					/*System.out.println(rsColumns.getString("COLUMN_NAME"));
					System.out.println(rsColumns.getString("TYPE_NAME"));*/
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
	}
	
	public void customDataCheck(ArrayList<String[]> tabledata, String strTicker)
	{
		/*This is a custom function to compare the begin_fiscal_calendar from the nasdaq and the sec
		 * websites.
		 */
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
				/*was using string tokenizer, switch to split() since it returns tokens for adjacent delimiters*/
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
	
	public Integer getBatchNumber() throws SQLException
	{
		/*
		 * Get the current highest batch number from both fact_data & fact_data_stage and increase that by 1
		 */
		
		/*Integer nFact =0;
		Integer nFactStage=0;
		String query = "select max(batch) from fact_data_stage";
		ResultSet rs = db_run_query(query);
		rs.next();
		nFactStage = rs.getInt("max(batch)");
		query = "select max(batch) from fact_data";
		rs = db_run_query(query);
		rs.next();
		nFact = rs.getInt("max(batch)");
		
		if (nFact >= nFactStage)
			return(nFact+1);
		else
			return(nFactStage+1);*/
		
		String query = "select max(batch) from ";
		query = query + "(select batch from fact_data union all ";
		query = query + "select batch from fact_data_stage union all ";
		query = query + "select batch from fact_data_stage_est) t1";
		ResultSet rs = db_run_query(query);
		rs.next();
		return (rs.getInt("max(batch)") + 1);
			
	
		
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

	
	
	
	public String[] extendArray(String[] inputArray)
	{
		String[] tmpArray = new String[inputArray.length + 1];
		for (int i=0; i<inputArray.length;i++)
		{
			tmpArray[i] = inputArray[i];
		}
		return(tmpArray);
		
	}
	
	public static void mail(String strEmail, String strMessage)
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
	  	props.put("mail.debug", "true");


	  	//Session session = Session.getInstance(props);
	  	Session session = Session.getInstance(props,new MyPasswordAuthenticator(username, password));



	  	try {

		    Message message = new MimeMessage(session);
		    message.setFrom(new InternetAddress("hastapasta99@gmail.com"));
		    message.setRecipients(Message.RecipientType.TO, 
	                      InternetAddress.parse(strEmail));
		    message.setSubject("Pikefin Alert");
		    message.setText(strMessage);

		    //Transport transport = session.getTransport("smtp");
		    //transport.connect(host, port, username, password);
		    //transport.connect(host,username,password);
		    
		    

		    Transport.send(message);

		    System.out.println("Done");

	  	} catch (MessagingException e) {
	  	    throw new RuntimeException(e);
	  	}
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

	
	 

	
	
	
	







