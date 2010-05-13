package com.roeschter.jsl;

import java.sql.*;
import java.util.ArrayList;
import java.io.*;
import java.util.StringTokenizer;
import java.util.regex.*;



public class UtilityFunctions
{
	
	public Connection con;
	
	public Connection getConnection()
	{
		return(this.con);
		
	}
	

		
	
	public UtilityFunctions(String strDatabase, String strUser, String strPass) throws Exception
	{
	
		//try
		//{
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/" + strDatabase;
			this.con = DriverManager.getConnection(url,strUser, strPass);
			
			//String url = "jdbc:mysql://localhost:3306/mydb";
			
			//System.err.println("Successfully created connection.");
			
		/*}
		catch (Exception e)
		{
			e.printStackTrace();
		
		}*/
		
		
		
		
	}
	
	public void db_update_query(String strUpdateStmt) throws SQLException
	{

		ResultSet rs =null;

		try
		{
	
			Statement stmt = con.createStatement();
			stmt.execute(strUpdateStmt);
		}
		catch (SQLException sqle)
		{
			System.out.println("SQL statement failed: " + strUpdateStmt);
			sqle.printStackTrace();
		}
		
		
		
		
	}
	
	
	
	public ResultSet db_run_query(String query) throws SQLException
	{

		ResultSet rs =null;
		//try
		//{
			Statement stmt = this.con.createStatement();
			rs = stmt.executeQuery(query);
	
		//}
		//catch (SQLException sqle)
		//{
			//System.out.println("SQL statement failed: " + query);
			//sqle.printStackTrace();
		
		//}
		return(rs);
		/* Not going to worry about closing the connection, we'll let it be garbage collected.*/
		
		
		
	}
	
	public void importTableIntoDB(ArrayList<String[]> tabledata, String[] columnnames, String tablename)
	{
		/* This function expects an arraylist with 2X of the number of values to be inserted with each value
		preceeded by the datatype with the current 3 datatypes being VARCHAR, FUNCTIONS, INT */
		String[] rowdata;
		String query ="";
		String columns= "";
		String values = "";
		/*for (int i=0;i<columnnames.length;i++)
		{
			if (i==0)
				columns = columns + columnnames[i];
			else
				columns = columns + "," + columnnames[i];
		}
		*/
		String[] datatypes = new String[columnnames.length];
	
		try
		{
			ResultSet rsColumns = null;
	    DatabaseMetaData meta = con.getMetaData();
		
			System.out.println("here");
			int nCount=0;
			for (int j=0;j<columnnames.length;j++)
			{	
				System.out.println("here2 " + j);
			  rsColumns = meta.getColumns(null, null, tablename, null);
			  nCount=0;
	    	while (rsColumns.next())
				{
					/*System.out.println("here3");
					System.out.println(rsColumns.getString("COLUMN_NAME"));
					System.out.println(rsColumns.getString("TYPE_NAME"));*/
		
					if (columnnames[j].compareTo(rsColumns.getString("COLUMN_NAME")) == 0)
					{
						datatypes[j] = rsColumns.getString("TYPE_NAME");
						break;
					}
					nCount++;
				}
			}
		}
		catch (SQLException sqle)
		{
			System.err.println("Problem retrieving column data types");
			sqle.printStackTrace();
		}
		
	
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
			
			for (int y=0;y<rowdata.length;y++)
			{
				if (y!=0)
				{
					values = values + ",";
					strColumns = strColumns + ",";
				}
				
					
					
				//if (rowdata[y].compareTo("VARCHAR") == 0)
			
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
				System.err.println("SQLException failed at row " + (x+1) + " " + query);
				sqle.printStackTrace();
			}
		
		}
		System.out.println(nInsertCount + " records inserted in db.");
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
	  	System.err.println("Problem writing CSV file");
	  	ioe.printStackTrace();
	  }
		
	}
	
	public static void loadCSV(String filename)
	//not quite functional yet
	{
	
		filename = filename.replace("\\","\\\\");
		System.out.println(filename);
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
							System.out.println("Problem with enclosure characters @ line " + nLineCount);
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
						System.out.print(strArray[i]);
					else
						System.out.print("," + strArray[i]);
				}
				System.out.println("");
				
				arraylist.add(strArray);
							
				nLineCount++;	
			}
			System.out.println(nLineCount + " lines processed.");
			return(arraylist);
			
		} 
		catch (Exception e)
		{
			System.err.println("Problem reading CSV @ line " + nLineCount);
			System.err.println(nTmp);
			e.printStackTrace();
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
			
			int nOpenEnclosure, nCloseEnclosure;
			int nCurOffset=0;
			int nLineCount =0;
	
	    
			while((strCurLine = in.readLine()) != null)
			{
				strNewLine = "";
				matcher = pattern.matcher(strCurLine);
				System.out.println(strCurLine);
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
						System.out.println("Invalid syntax for enclosure characters at line " + nLineCount);
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
				System.out.println(strNewLine);
				nLineCount++;	
			}
			out.close();
			in.close();
			
		}
		catch(Exception e)
		{
			System.err.println("Problem Scrubbing csv file");
			e.printStackTrace();
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
	
	







