/*CAREFUL with using a data extraction pattern that matches the last searched table,row,cell,div pattern. */

package com.roeschter.jsl;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;


class DataGrab
{
	
	
	
public String get_value(String local_data_set)
{
	ProcessingFunctions.preProcessing(local_data_set);
	String strDataValue="";
	try
	{
	//run sql to get info about the data_set
	Connection con = UtilityFunctions.db_connect();
	
	String query = "select * from extract_info where Data_Set='" + local_data_set + "'";
	
	System.out.println(query);
	
  Statement stmt = con.createStatement();
  ResultSet rs = UtilityFunctions.db_run_query(query);
  
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
  	
  System.out.println("Retrieving URL: " + strUrlStatic);
  
 
  	
  int nCurOffset = 0;
  
  //strUrlStatic = "http://localhost/tabletest2.html";
  
  URL urlStatic = new URL(strUrlStatic);
  
  BufferedReader in = new BufferedReader(
				new InputStreamReader(
				urlStatic.openStream()));
				
	 String returned_content = "";
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
	
	System.out.println("Done reading url contents");
	
	//System.out.println(returned_content);
	
	/*
		Initial regex search.
	*/
	
	String strInitialOpenUniqueCode = rs.getString("Initial_Bef_Unique_Code");
	Pattern pattern;
	Matcher matcher;
	if (strInitialOpenUniqueCode != null)
	{
		String strInitialOpenUniqueRegex = "(?i)(" + strInitialOpenUniqueCode + ")";
		
		System.out.println("Initial Open Regex: " + strInitialOpenUniqueRegex);
		
		pattern = Pattern.compile(strInitialOpenUniqueRegex);
		
		matcher = pattern.matcher(returned_content);
		
		matcher.find();
		
		//nCurOffset = matcher.end();
		nCurOffset = matcher.start();
		
		System.out.println("Offset after initial regex search: " + nCurOffset);
				
	}
	
	
	
	/*
		End initial regex search.
	*/
	
	System.out.println("Before table searches.");
	
	String strOpenTableRegex = "(?i)(<TABLE[^>]*>)";
	//String strOpenTableRegex = "START NEW";
	
	pattern = Pattern.compile(strOpenTableRegex);
	
	matcher = pattern.matcher(returned_content);
	
	
	for (int i=0;i<tables;i++)
	{
		
		
		matcher.find(nCurOffset);
		
		nCurOffset = matcher.start() + 1;
		
		System.out.println("table row tag iteration " + i + ", offset: " + nCurOffset);
		
	}
	
	System.out.println("Before table row searches.");

	
	pattern = Pattern.compile("(?i)(<tr[^>]*>)");
	
	matcher = pattern.matcher(returned_content);
	
	
	//System.out.println(returned_content.substring(nCurOffset, nCurOffset+500));
	
	
	
	for (int i=0;i<rows;i++)
	{
		
		
		matcher.find(nCurOffset);
		
		nCurOffset = matcher.start() + 1;
		
		System.out.println("table row tag iteration " + i + ", offset: " + nCurOffset);
		
	}
	
	pattern = Pattern.compile("(?i)(<td[^>]*>)");
	
	matcher = pattern.matcher(returned_content);
	
	//matcher = pattern.matcher(returned_content);
	
	System.out.println("Cell count: " + cells);
	
	for (int i=0;i<cells;i++)
	{
		
		
		matcher.find(nCurOffset);
		
		nCurOffset = matcher.start() + 1;
		
		System.out.println("table cell tag iteration " + i + ", offset: " + nCurOffset);
		
	}
	
	pattern = Pattern.compile("(?i)(<div[^>]*>)");
  
  matcher = pattern.matcher(returned_content);
  
  System.out.println("Div count" + divs);
  
  for (int i=0;i<divs;i++)
	{
		
		
		matcher.find(nCurOffset);
		
		nCurOffset = matcher.start() + 1;
		
		System.out.println("div tag iteration " + i + ", offset: " + nCurOffset);
		
	}
	
	
	String strBeforeUniqueCode = rs.getString("Before_Unique_Code");
	String strBeforeUniqueCodeRegex = "(?i)(" + strBeforeUniqueCode + ")";
  String strAfterUniqueCode = rs.getString("After_Unique_Code");
  String strAfterUniqueCodeRegex = "(?i)(" + strAfterUniqueCode + ")";
  
  System.out.println(strBeforeUniqueCodeRegex);
  
  pattern = Pattern.compile(strBeforeUniqueCodeRegex);
  System.out.println("after strbeforeuniquecoderegex compile");
  
  matcher = pattern.matcher(returned_content);
  
  System.out.println("Current offset before final data extraction: " + nCurOffset);
  
  matcher.find(nCurOffset);
  
  int nBeginOffset = matcher.start() + strBeforeUniqueCode.length();
  System.out.println("begin offset: " + nBeginOffset);
  
  pattern = Pattern.compile(strAfterUniqueCodeRegex);
  System.out.println("after strafteruniquecoderegex compile");
  
  matcher = pattern.matcher(returned_content);
  
  matcher.find(nCurOffset);
  
  int nEndOffset = matcher.start();
  System.out.println("end offset: " + nEndOffset);
  
  strDataValue = returned_content.substring(nBeginOffset,nEndOffset);
  
  System.out.println ("Raw Data Value: " + strDataValue);
  
  strDataValue = strDataValue.replace(",","");
  
  strDataValue = strDataValue.replace("&nbsp;","");
  
  if (strDataValue.compareTo("") != 0)
  {
  	System.out.println("checking for negative data value");
  	System.out.println(strDataValue.substring(0,1));
  	if (strDataValue.substring(0,1).compareTo("(") == 0)
 		{
 	
 			strDataValue = strDataValue.replace("(","");
 	 		strDataValue = "-" + strDataValue.replace(")","");
 		}
 	 
 	}
  
  System.out.println("Data Value: " + strDataValue);
  
  //remove commas
  
  //return(strDataValue);
  
  /*query = "INSERT INTO fact_data (data_set,value,date_collected) VALUES ('" + local_data_set + "','" + strDataValue + "',NOW())";
  
  System.out.println(query);
  
  stmt = con.createStatement();
  boolean bRet = stmt.execute(query);*/
  
  
    
  
               
    
  con.close();
    
  }catch (IllegalStateException ise)
  {
  	System.out.println("No regex match");
  	ise.printStackTrace();
  }
  	
    catch( Exception e ) {
      e.printStackTrace();
    }//end catch
    finally
    {
    	return(ProcessingFunctions.postProcessing(local_data_set,strDataValue));
    }




}

public ArrayList<String> get_list_dataset_run_once()
{
	ArrayList<String> tmpAL = new ArrayList<String>();
	
	int count=0;
	try
	{

		String query = "select data_set from schedule where run_once=1";
		ResultSet rs = UtilityFunctions.db_run_query(query);
		
		
		while(rs.next())
		{
		
			tmpAL.add(rs.getString("Data_Set"));
			count++;
				
			
		}

	}
	catch (SQLException sqle)
	{
		System.out.println("problem with retrieving data sets from schedule table");
		sqle.printStackTrace();
	}

		

	System.out.println("Processing " + count + " data sets.");
	return(tmpAL);

	
	
	
}



public void grab_dow_data_set()
{
	try
	{
	//String[] data_sets = {"yahoo_q109_income", "yahoo_q209_income", "yahoo_q309_income", "yahoo_q409_income"};
	//String[] data_sets = {""};
	Connection con = UtilityFunctions.db_connect();
	
	
	ArrayList<String> data_sets = get_list_dataset_run_once();

	
	for (int i=0;i<data_sets.size();i++)
	{
		
		String strCurDataSet = data_sets.get(i);
		System.out.println("PROCESSING DATA SET " + strCurDataSet);
		String query = "select * from dow";
		Statement stmt = con.createStatement();
		ResultSet rs = UtilityFunctions.db_run_query(query);
			
		String strCurTicker="";
		String fullUrl;
		String strDataValue="";
		
			
			while(rs.next())
			{
				try
				{
				strCurTicker = rs.getString("ticker");
				
				/*Active only to debug individual tickers */
				/*if (strCurTicker.compareTo("BA") != 0)
					continue;*/
				
					query = "update extract_info set url_dynamic='" + strCurTicker + "' where Data_Set='" + strCurDataSet + "'";
					stmt = con.createStatement();
					boolean bRet = stmt.execute(query);
				
			
				System.out.println(query);
				
				System.out.println("Calling get value.");
				strDataValue = get_value(strCurDataSet);
		
					query = "INSERT INTO fact_data_stage (data_set,value,quarter,ticker,date_collected) VALUES ('" + strCurDataSet + "','" + 
					strDataValue + "','" + Integer.toString(40+i) + "','" + strCurTicker + "',NOW())";
 	  			System.out.println(query);
  	 			stmt = con.createStatement();
  				bRet = stmt.execute(query);
  			}
  			catch (SQLException sqle)
  			{
  				System.out.println("problem with insert sql statement");
  				System.err.println("Processing of data_set " + strCurDataSet + " with ticker " + strCurTicker + " FAILED ");
  				sqle.printStackTrace();
  			}
  				
				
				
			}
	//read list of dow stocks
			
	//replace url with dow quote
	//call data grab function
		}
		con.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	}
	

	
	
}
	
	
	
	
	


/*
$open_table_regex="/<table[^>]*>/i";
  $previous_offset = 0;
  
  for ($i=0;$i<$tables;$i++)
  {
  	preg_match($open_table_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $current_offset);
  	$current_offset = $matches[0][1] + 1;
  	echo "<BR>table tag iteration " . $i . ",offset: " . $current_offset;
  	
  }
*/