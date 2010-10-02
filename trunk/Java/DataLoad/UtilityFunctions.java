package com.roeschter.jsl;

import java.sql.*;
import java.util.ArrayList;
import java.io.*;
import java.util.StringTokenizer;
import java.util.regex.*;






public class UtilityFunctions
{
	
	public Connection con;
	static public boolean bCalledByJsp;
	String strCapturedOutput;
	static CustomBufferedWriter stdoutwriter;
	

	
	public Connection getConnection()
	{
		return(this.con);
		
	}
	
	
		
		
	
	
	

		
	
	public UtilityFunctions(String strDatabase, String strUser, String strPass, String strFullLog, String strErrorLog, String strSQLLog)
	{
	
		//try
		//{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/" + strDatabase;
			this.con = DriverManager.getConnection(url,strUser, strPass);
			//this.bCalledByJsp = bCalled;
			UtilityFunctions.stdoutwriter = new CustomBufferedWriter(strFullLog, strErrorLog, strSQLLog, true, true);
	

		}
		catch (Exception e)
		{
			System.out.println("UtilityFunctions constructor failed.");
			e.printStackTrace();
		}
			
		
		
		
	}
	
	public void db_update_query(String strUpdateStmt) throws SQLException
	{

	

		//try
		//{
			stdoutwriter.writeln("Executing SQL: " + strUpdateStmt,Logs.SQL);
			stdoutwriter.writeln(strUpdateStmt,Logs.SQL);
			Statement stmt = con.createStatement();
			stmt.execute(strUpdateStmt);
			
		/*}
		catch (SQLException sqle)
		{
			stdoutwriter.writeln("1 SQL statement failed: " + strUpdateStmt);
			stdoutwriter.writeln(sqle);
	
			
		}*/
		
		
		
		
	}
	

	
	
	
	public ResultSet db_run_query(String query) throws SQLException
	{

		ResultSet rs =null;
		//try
		//{

			stdoutwriter.writeln("Executing query: " + query, Logs.SQL);
			Statement stmt = this.con.createStatement();
			stdoutwriter.writeln(query, Logs.SQL);
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
	
	public void importTableIntoDB(ArrayList<String[]> tabledata, String tablename)
	{
		/* This function expects an arraylist with 2X of the number of values to be inserted with each value
		preceeded by the datatype with the current 3 datatypes being VARCHAR, FUNCTIONS, INT */
		/*OFP 9/28/2010 - I believe passing in the datatypes with the table data is redundant since the types
		 * are retrieved from the database meta data.
		 */
		String[] rowdata;
		String query ="";
		String columns= "";
		String values = "";
		
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
					stdoutwriter.writeln(columnnames[j],Logs.STATUS2);
					if (columnnames[j].compareTo(rsColumns.getString("COLUMN_NAME")) == 0)
					{
						datatypes[j] = rsColumns.getString("TYPE_NAME");
						stdoutwriter.writeln(datatypes[j],Logs.STATUS2);
						break;
					}
					nCount++;
				}
			}
		}
		catch (SQLException sqle)
		{
			stdoutwriter.writeln("Problem retrieving column data types",Logs.ERROR);
			stdoutwriter.writeln(sqle);
		}
		stdoutwriter.writeln("Finished retrieving column data types",Logs.STATUS2);
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
			
			for (int y=0;y<rowdata.length;y=y+2)
			{
				
				if (y!=0)
				{
					values = values + ",";
					strColumns = strColumns + ",";
				}

				//System.out.println("{" + rowdata[y] + "} " + rowdata[y].length() + " " + datatypes[y]);
				if (rowdata[y].compareTo("VARCHAR") == 0)
				{
					values = values + "'" + rowdata[y+1].replace("'","\\'") + "'";
				}
				else if (((rowdata[y].compareTo("INT") == 0) || (rowdata[y].compareTo("BIGINT") == 0)) && ((rowdata[y+1].length() == 0)))
				{

					values = values + "'0'";
				}
				else
					values = values + rowdata[y+1];	
				
					
				strColumns = strColumns + columnnames[y/2];

				
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
				stdoutwriter.writeln("SQLException failed at row " + (x+1) + " " + query,Logs.ERROR);
				stdoutwriter.writeln(sqle);
			}
		
		}
		stdoutwriter.writeln(nInsertCount + " records inserted in db.",Logs.STATUS2);
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
	  	stdoutwriter.writeln("Problem writing CSV file",Logs.ERROR);
	  	stdoutwriter.writeln(ioe);
	  }
		
	}
	
	public static void loadCSV(String filename)
	//not quite functional yet
	{
	
		filename = filename.replace("\\","\\\\");
	
		String query = "LOAD DATA INFILE '" + filename + "' REPLACE INTO TABLE 'fact_data_stage' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n'('ticker','data_set','value')";
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
			StringTokenizer st;
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
							stdoutwriter.writeln("Problem with enclosure characters @ line " + nLineCount,Logs.ERROR);
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
						stdoutwriter.writeln(strArray[i],Logs.STATUS2);
					else
						stdoutwriter.writeln("," + strArray[i],Logs.STATUS2);
				}

				
				arraylist.add(strArray);
							
				nLineCount++;	
			}
			stdoutwriter.writeln(nLineCount + " lines processed.",Logs.STATUS2);
			return(arraylist);
			
		} 
		catch (Exception e)
		{
			stdoutwriter.writeln("Problem reading CSV @ line " + nLineCount,Logs.ERROR);
			stdoutwriter.writeln(e);
			return(null);
		}
	}
	
	public int retrieveAdjustedQuarter(int fiscalquarter,int fiscalyear, String strTicker)
	{

		String query="select begin_fiscal_calendar, company.ticker,";
	    query = query + " (case when begin_fiscal_calendar in ('February','March','April') then 3";
	    query = query + " when begin_fiscal_calendar in ('May','June','July') then 2 when";
	    query = query + " begin_fiscal_calendar in ('August','September','October') then 1";
	    query = query + " else 0 end) as qtr_code_adjusted from company where";
	    query = query + " company.ticker='" + strTicker + "'";
		
		/*String temp = strDataSet.substring(strDataSet.indexOf("_")+1, strDataSet.indexOf("_")+5);
		Integer quarter = Integer.parseInt(temp.substring(1,2));
		Integer year = Integer.parseInt(temp.substring(2,4));*/
		Integer unadjustedqtr = (4*fiscalyear) + fiscalquarter;
		Integer adjustment_code = 0;
		
		try
		{
			ResultSet rs = db_run_query(query);
			rs.next();
			adjustment_code = rs.getInt("qtr_code_adjusted");
			
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem adjusting quarter value",Logs.ERROR);
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
		}
		
		return(unadjustedqtr-adjustment_code);
	    
	 
	    
		
		
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
			
			int nOpenEnclosure, nCloseEnclosure;
			int nCurOffset=0;
			int nLineCount =0;
	
	    
			while((strCurLine = in.readLine()) != null)
			{
				strNewLine = "";
				matcher = pattern.matcher(strCurLine);
				stdoutwriter.writeln(strCurLine,Logs.STATUS2);
				nOpenEnclosure = 0;
				nCloseEnclosure = 0;
				nCurOffset=0;
				while (true)
				{
					//search for opening enclosure
					//System.out.println("1 curOffset: " + nCurOffset);
					if (matcher.find(nCurOffset) == false)
						break;
					else
						nOpenEnclosure = matcher.end();
					
					strNewLine = strNewLine + strCurLine.substring(nCurOffset,matcher.end());
					nCurOffset = matcher.end();
					//System.out.println(strNewLine);
					
					//search for ending enclosure
					if (matcher.find(nCurOffset) == false)
					{
						stdoutwriter.writeln("Invalid syntax for enclosure characters at line " + nLineCount,Logs.ERROR);
						break;
					}
					else
						nCloseEnclosure = matcher.end();
					
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
				stdoutwriter.writeln(strNewLine,Logs.STATUS2);
				nLineCount++;	
			}
			out.close();
			in.close();
			
		}
		catch(Exception e)
		{
			stdoutwriter.writeln("Problem Scrubbing csv file",Logs.ERROR);
			stdoutwriter.writeln(e);
		}

		
		
		
		
		
	}
}
	
	class TestException extends Exception
	{
		void TestException()
		{
			return;
		}
		
		
	}
	
	







