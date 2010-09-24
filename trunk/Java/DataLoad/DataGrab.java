/*CAREFUL with using a final data extraction pattern that matches the last searched table,row,cell,div pattern. */

package com.roeschter.jsl;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;


public class DataGrab extends Thread
{
	
static String returned_content;
UtilityFunctions uf;
ProcessingFunctions pf;
boolean bContinue;

  public DataGrab(UtilityFunctions tmpUF)
  {
  	this.uf = tmpUF;
  	this.pf = new ProcessingFunctions(tmpUF,this);
  }

	public void startThread()
	{
		bContinue = true;
		this.start();
	}
	
	public void stopThread()
	{
		bContinue = false;
	}
	
	public boolean getWillTerminate()
	{
		return(!bContinue);
	}

 public void run()
 {
 	try 
 	{
 		if (bContinue == false)
 			return;
 		uf.stdoutwriter.writeln("=========================================================");
 		uf.stdoutwriter.writeln("INITIATING THREAD");
 		grab_dow_data_set();
 		clear_run_once();
 		sleep(60000);
 	}
 	catch (InterruptedException ie)
 	{
 		uf.stdoutwriter.writeln("InterruptedException thrown");
 		stopThread();
 		
 	}
 	catch (SQLException sqle)
 	{
 		uf.stdoutwriter.writeln("SQLException thrown in the threads run method.");
 		uf.stdoutwriter.writeln(sqle);
 	}
 	
}

int regexSeekLoop(String regex, int nCount, int nCurOffset) throws TagNotFoundException
{
		//nCurOffset = regexSeekLoop("(?i)(<TABLE[^>]*>)",returned_content,tables);
	//String strOpenTableRegex = "(?i)(<TABLE[^>]*>)";
	//String strOpenTableRegex = "START NEW";
	
	Pattern pattern = Pattern.compile(regex);
	
	Matcher matcher = pattern.matcher(returned_content);
	
	
	for (int i=0;i<nCount;i++)
	{
		
		
		if (matcher.find(nCurOffset) == false)
		//Did not find regex
		{
			uf.stdoutwriter.writeln("Regex search exceeded.");
			throw new TagNotFoundException();
		}
		
		nCurOffset = matcher.start() + 1;
		
		uf.stdoutwriter.writeln("regex iteration " + i + ", offset: " + nCurOffset);
		
	}
	return(nCurOffset);
}

String regexSnipValue(String strBeforeUniqueCodeRegex, String strAfterUniqueCodeRegex, int nCurOffset) throws CustomEmptyStringException
{
  String strDataValue="";
  //try
 // {
	  uf.stdoutwriter.writeln(strBeforeUniqueCodeRegex);
	  
	  Pattern pattern = Pattern.compile(strBeforeUniqueCodeRegex);
	  uf.stdoutwriter.writeln("after strbeforeuniquecoderegex compile");
	  
	  Matcher matcher = pattern.matcher(returned_content);
	  
	  uf.stdoutwriter.writeln("Current offset before final data extraction: " + nCurOffset);
	  
	  matcher.find(nCurOffset);
	  
	  int nBeginOffset = matcher.end();
	  uf.stdoutwriter.writeln("begin offset: " + nBeginOffset);
	  
	  pattern = Pattern.compile(strAfterUniqueCodeRegex);
	  uf.stdoutwriter.writeln("after strAfterUniqueCodeRegex compile");
	  
	  matcher = pattern.matcher(returned_content);
	  
	  matcher.find(nCurOffset);
	  
	  int nEndOffset = matcher.start();
	  uf.stdoutwriter.writeln("end offset: " + nEndOffset);
	  
	  if (nEndOffset <= nBeginOffset)
	  {
	  	uf.stdoutwriter.writeln("EndOffset is < BeginOffset");
	  	throw new CustomEmptyStringException();
	  }
	  strDataValue = returned_content.substring(nBeginOffset,nEndOffset);
	  
	  uf.stdoutwriter.writeln ("Raw Data Value: " + strDataValue);
	/*}
	catch (IOException ioe)
	{
		Sy
	}*/
  
  return(strDataValue);
	
	
}

public void clear_run_once() throws SQLException
{
	String query = "update schedule set run_once=0";
	uf.db_update_query(query);
}
	
	
	
public String get_value(String local_data_set)
{
	String strDataValue="";
			
	
	try
	{
		if (pf.preProcessing(local_data_set,this.uf) != true)
		{
			throw new CustomEmptyStringException();
		}
	//run sql to get info about the data_set
	//Connection con = UtilityFunctions.db_connect();
	
	String query = "select * from extract_info where Data_Set='" + local_data_set + "'";
	
	uf.stdoutwriter.writeln(query);
	
  //Statement stmt = con.createStatement();
  ResultSet rs = uf.db_run_query(query);
  
  rs.next();
  
  int tables,cells,rows,divs;
  tables = rs.getInt("Table_Count");
  cells = rs.getInt("Cell_Count");
  rows = rs.getInt("Row_Count");
  divs = rs.getInt("Div_Count");
  
  String strUrlStatic;
  
  strUrlStatic = rs.getString("url_static");
  
  if (rs.getString("url_dynamic") != "")
  	strUrlStatic = strUrlStatic + rs.getString("url_dynamic");
  	
  //strUrlStatic = "http://localhost/tabletest.html";
  	
  uf.stdoutwriter.writeln("Retrieving URL: " + strUrlStatic);
  
 
  	
  int nCurOffset = 0;
  
  //strUrlStatic = "http://localhost/tabletest2.html";
  
  URL urlStatic = new URL(strUrlStatic);
  
  BufferedReader in = new BufferedReader(
				new InputStreamReader(
				urlStatic.openStream()));
				
	 returned_content = "";
	 String curLine = "";
	 int nTmp;

  //changed from readline to read so that end-of-line characters are included
  //this is to match the php regex searches
  //I'm sure this can be optimized performance-wise
	//while (( curLine= in.readLine()) != null)
	//    returned_content = returned_content + curLine;
	while ((nTmp = in.read()) != -1)
		returned_content = returned_content + (char)nTmp;
	

	in.close();
	
	uf.stdoutwriter.writeln("Done reading url contents");
	
	//uf.stdoutwriter.writeln(returned_content);
	
	/*
		Initial regex search.
	*/
	
	String strInitialOpenUniqueCode = rs.getString("Initial_Bef_Unique_Code");
	Pattern pattern;
	Matcher matcher;
	if (strInitialOpenUniqueCode != null)
	{
		String strInitialOpenUniqueRegex = "(?i)(" + strInitialOpenUniqueCode + ")";
		
		uf.stdoutwriter.writeln("Initial Open Regex: " + strInitialOpenUniqueRegex);
		
		pattern = Pattern.compile(strInitialOpenUniqueRegex);
		
		matcher = pattern.matcher(returned_content);
		
		matcher.find();
		
		//nCurOffset = matcher.end();
		nCurOffset = matcher.start();
		
		uf.stdoutwriter.writeln("Offset after initial regex search: " + nCurOffset);
				
	}
	
	
	
	/*
		End initial regex search.
	*/
	
	uf.stdoutwriter.writeln("Before table searches.");
	nCurOffset = regexSeekLoop("(?i)(<TABLE[^>]*>)",tables,nCurOffset);	
	
	uf.stdoutwriter.writeln("Before table row searches.");
	nCurOffset = regexSeekLoop("(?i)(<tr[^>]*>)",rows,nCurOffset);

	uf.stdoutwriter.writeln("Before table cell searches.");
	nCurOffset = regexSeekLoop("(?i)(<td[^>]*>)",cells,nCurOffset);
	
	uf.stdoutwriter.writeln("Before div searches");
	nCurOffset = regexSeekLoop("(?i)(<div[^>]*>)",divs,nCurOffset);
	
	
	String strBeforeUniqueCode = rs.getString("Before_Unique_Code");
	String strBeforeUniqueCodeRegex = "(?i)(" + strBeforeUniqueCode + ")";
  String strAfterUniqueCode = rs.getString("After_Unique_Code");
  String strAfterUniqueCodeRegex = "(?i)(" + strAfterUniqueCode + ")";
  
  strDataValue = regexSnipValue(strBeforeUniqueCodeRegex,strAfterUniqueCodeRegex,nCurOffset);
  
 
  strDataValue = strDataValue.replace(",","");
  
  strDataValue = strDataValue.replace("&nbsp;","");
  
  if (strDataValue.compareTo("") != 0)
  {
  	uf.stdoutwriter.writeln("checking for negative data value");
  	uf.stdoutwriter.writeln(strDataValue.substring(0,1));
  	if (strDataValue.substring(0,1).compareTo("(") == 0)
 		{
 	
 			strDataValue = strDataValue.replace("(","");
 	 		strDataValue = "-" + strDataValue.replace(")","");
 		}
 	 
 	}
  
  
  
  //remove commas
  
  //return(strDataValue);
  
  /*query = "INSERT INTO fact_data (data_set,value,date_collected) VALUES ('" + local_data_set + "','" + strDataValue + "',NOW())";
  
  uf.stdoutwriter.writeln(query);
  
  stmt = con.createStatement();
  boolean bRet = stmt.execute(query);*/
  
  
    
  
               
    
  //con.close();
    
  }catch (IllegalStateException ise)
  {
  	uf.stdoutwriter.writeln("No regex match");
  	uf.stdoutwriter.writeln(ise);
  }
  catch (CustomEmptyStringException cese)
  {
  	uf.stdoutwriter.writeln("CustomEmptyStringException thrown");
  	uf.stdoutwriter.writeln(cese);
  }
  catch (TagNotFoundException tnfe)
  {
  	uf.stdoutwriter.writeln("TagNotFoundException thrown");
  	uf.stdoutwriter.writeln(tnfe);
  }
  catch( Exception e )
  {
      uf.stdoutwriter.writeln(e);
  }
  finally
  {
   	strDataValue = pf.postProcessing(local_data_set,strDataValue);
   	uf.stdoutwriter.writeln("Data Value: " + strDataValue);
   	return(strDataValue);
  }




}

public ArrayList<String[]> get_table(String strTableSet)
{
	//retrieve the table data_set
	ArrayList<String[]> tabledata = new ArrayList<String[]>();
	try
	{
		String query = "select * from extract_table where data_set='" + strTableSet + "'";
		ResultSet rs = uf.db_run_query(query);
		rs.next();
		
  	
		
	
		//read in the url
		String strUrlStatic;
	  
	  strUrlStatic = rs.getString("url_static");
	  
	  if (rs.getString("url_dynamic") != "")
	  	strUrlStatic = strUrlStatic + rs.getString("url_dynamic");
	  	
	  uf.stdoutwriter.writeln("Retrieving URL: " + strUrlStatic);
	  
	  URL urlStatic = new URL(strUrlStatic);
	  
	  BufferedReader in = new BufferedReader(
					new InputStreamReader(
					urlStatic.openStream()));
					
		int nTmp;			
		int nCurOffset = 0;
		returned_content="";
		
		while ((nTmp = in.read()) != -1)
			returned_content = returned_content + (char)nTmp;
		
	
		in.close();
		
		uf.stdoutwriter.writeln("Done reading url contents");
		
		//seek to the top corner of the table
		uf.stdoutwriter.writeln("Before table searches.");
		
		nCurOffset = regexSeekLoop("(?i)(<TABLE[^>]*>)",rs.getInt("table_count"),nCurOffset);
		
		nCurOffset = regexSeekLoop("(?i)(<tr[^>]*>)",rs.getInt("top_corner_row"),nCurOffset);

		int nEndTableOffset = regexSeekLoop("(?i)(</TABLE>)",1,nCurOffset);
		

		
		
		
	

	
		//iterate over rows, iterate over columns, writing values out to a csv file
		boolean done = false;
		int nNumOfColumns = rs.getInt("number_of_columns");
		
		
		String[] rowdata; 
		int nRowCount=0;
		
		
		String strBeforeUniqueCodeRegex, strAfterUniqueCodeRegex, strDataValue;
		
		while (!done)
		{
			uf.stdoutwriter.writeln("row: " + nRowCount);
			rowdata = new String[nNumOfColumns];
			
			for(int i=0;i< nNumOfColumns;i++)
			{
				

				uf.stdoutwriter.writeln("Column: " + i);
				nCurOffset = regexSeekLoop("(?i)(<td[^>]*>)",rs.getInt("Column" + (i+1)),nCurOffset);
				
				//String strBeforeUniqueCode = rs.getString("bef_code_col" + (i+1));
				strBeforeUniqueCodeRegex = "(?i)(" + rs.getString("bef_code_col" + (i+1)) + ")";
			  //String strAfterUniqueCode = rs.getString("aft_code_col" + (i+1));
			  strAfterUniqueCodeRegex = "(?i)(" + rs.getString("aft_code_col" + (i+1)) + ")";
	  
			  strDataValue = regexSnipValue(strBeforeUniqueCodeRegex,strAfterUniqueCodeRegex,nCurOffset);
			  
			  rowdata[i] = strDataValue;
				
				
			}
			
			tabledata.add(rowdata);
			
			nCurOffset = regexSeekLoop("(?i)(<tr[^>]*>)",1,nCurOffset);
			if (nEndTableOffset < nCurOffset)
				break;
			nRowCount++;
			
		}
	//import the csv file in using load file
	}
	catch (SQLException sqle)
	{
		uf.stdoutwriter.writeln("Problem with query");
		uf.stdoutwriter.writeln(sqle);
	}
	catch (TagNotFoundException tnfe)
	{
		uf.stdoutwriter.writeln("TagNotFoundException thrown");
	}
	catch (CustomEmptyStringException cese)
	{
		uf.stdoutwriter.writeln("CustomEmptyStringException thrown");
		uf.stdoutwriter.writeln(cese);
	}
	catch (IOException ioe)
	{
		uf.stdoutwriter.writeln("Problem with io");
		uf.stdoutwriter.writeln(ioe);	
	}
	finally
	{
		return(tabledata);
	}
}

public ArrayList<String> get_list_dataset_run_once()
{
	ArrayList<String> tmpAL = new ArrayList<String>();
	
	int count=0;
	try
	{

		String query = "select data_set from schedule where run_once=1";
		ResultSet rs = uf.db_run_query(query);
		
		
		while(rs.next())
		{
		
			tmpAL.add(rs.getString("Data_Set"));
			count++;
				
			
		}

	}
	catch (SQLException sqle)
	{
		uf.stdoutwriter.writeln("problem with retrieving data sets from schedule table");
		uf.stdoutwriter.writeln(sqle);
	}

		

	uf.stdoutwriter.writeln("Processing " + count + " data sets.");
	return(tmpAL);

	
	
	
}



public void grab_dow_data_set()
{
	try
	{
	//String[] data_sets = {"yahoo_q109_income", "yahoo_q209_income", "yahoo_q309_income", "yahoo_q409_income"};
	//String[] data_sets = {""};
	
	
	
	ArrayList<String> data_sets = get_list_dataset_run_once();

	
	for (int i=0;i<data_sets.size();i++)
	{
		
		String strCurDataSet = data_sets.get(i);
		uf.stdoutwriter.writeln("PROCESSING DATA SET " + strCurDataSet);
		String query = "select * from company where in_dow=1 order by ticker";
	
		ResultSet rs = uf.db_run_query(query);
			
		String strCurTicker="";
		String fullUrl;
		String strDataValue="";
		int count=0;
		
			
			while(rs.next())
			{
				try
				{
					strCurTicker = rs.getString("ticker");
					
					/*Active only to debug individual tickers */
					
					/*if (strCurTicker.compareTo("T") != 0)
						continue;*/
					
				
				
					if ((strCurDataSet.substring(0,5)).compareTo("table") == 0)
					{
						query = "update extract_table set url_dynamic='" + strCurTicker + "' where data_set='" + strCurDataSet + "'";
						uf.stdoutwriter.writeln(query);
						uf.db_update_query(query);
						ArrayList<String[]> tabledata = get_table(strCurDataSet);
						tabledata = pf.processNasdaqEPSEstTable(tabledata,strCurDataSet,uf);
						
						String[] tmpArray = {"ticker","date_collected","data_set","value"};
						
						System.out.println(uf);
						
						try
						{
							uf.importTableIntoDB(tabledata,tmpArray,"fact_data_stage");
						}
						catch (Exception e)
						{
							uf.stdoutwriter.writeln("Import value into DB failed");
							uf.stdoutwriter.writeln(e);
						}
					
						//UtilityFunctions.createCSV(tabledata,"fact_data_stage.csv",(count==0?false:true));
						
						//UtilityFunctions.loadCSV("fact_data_stage.csv");
						
						String[] rowdata;
						for (int x=0;x<tabledata.size();x++)
						{
							rowdata = tabledata.get(x);
							for (int y=0;y<rowdata.length;y++)
							{
								System.out.print(rowdata[y]+"     ");
							}
							uf.stdoutwriter.writeln("");
						}
					}
					else
					{
						query = "update extract_info set url_dynamic='" + strCurTicker + "' where Data_Set='" + strCurDataSet + "'";
						uf.db_update_query(query);
					
				
						uf.stdoutwriter.writeln(query);
					
						uf.stdoutwriter.writeln("Calling get value.");
						strDataValue = get_value(strCurDataSet);
						
						if (strDataValue.compareTo("") == 0)
						{
							uf.stdoutwriter.writeln("Returned empty value '', skipping ");
							continue;
						}
				
							query = "INSERT INTO fact_data_stage (data_set,value,quarter,ticker,date_collected) VALUES ('" + strCurDataSet + "','" + 
							strDataValue + "','" + Integer.toString(40+i) + "','" + strCurTicker + "',NOW())";
							
							/* Use the following update statement for populating fiscal_calendar_begin */
							/*query = "UPDATE company set begin_fiscal_calendar='" + strDataValue + "' where ticker='" + strCurTicker + "'";*/
		 	  			uf.stdoutwriter.writeln(query);
		  	 			uf.db_update_query(query);
	  			}
	  		}
  			catch (SQLException sqle)
  			{
  				uf.stdoutwriter.writeln("problem with insert sql statement");
  				uf.stdoutwriter.writeln("Processing of data_set " + strCurDataSet + " with ticker " + strCurTicker + " FAILED ");
  				uf.stdoutwriter.writeln(sqle);
  			}
  				
				
				count++;	
			}
	//read list of dow stocks
			
	//replace url with dow quote
	//call data grab function
		}

	}
	catch (Exception e)
	{
		uf.stdoutwriter.writeln("Exception in grab_dow_data_set");
		uf.stdoutwriter.writeln(e);
	}
}


	
	

}

	
	

	

	
	


class CustomEmptyStringException extends Exception
{

	void CustomEmptyStringException()
	{
		//	super(); 
	}
}	

class TagNotFoundException extends Exception
{
	void TagNoutFoundException()
	{
		
	}
	
	
}
	
	
	
	


