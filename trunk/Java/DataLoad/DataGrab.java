/*CAREFUL with using a data extraction pattern that matches the last searched table,row,cell,div pattern. */

package com.roeschter.jsl;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;


class DataGrab
{
	
static String returned_content;

static int regexSeekLoop(String regex, int nCount, int nCurOffset) throws TagNotFoundException
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
			System.out.println("Regex search exceeded.");
			throw new TagNotFoundException();
		}
		
		nCurOffset = matcher.start() + 1;
		
		System.out.println("regex iteration " + i + ", offset: " + nCurOffset);
		
	}
	return(nCurOffset);
}

static String regexSnipValue(String strBeforeUniqueCodeRegex, String strAfterUniqueCodeRegex, int nCurOffset) throws CustomEmptyStringException
{
  String strDataValue="";
  //try
 // {
	  System.out.println(strBeforeUniqueCodeRegex);
	  
	  Pattern pattern = Pattern.compile(strBeforeUniqueCodeRegex);
	  System.out.println("after strbeforeuniquecoderegex compile");
	  
	  Matcher matcher = pattern.matcher(returned_content);
	  
	  System.out.println("Current offset before final data extraction: " + nCurOffset);
	  
	  matcher.find(nCurOffset);
	  
	  int nBeginOffset = matcher.end();
	  System.out.println("begin offset: " + nBeginOffset);
	  
	  pattern = Pattern.compile(strAfterUniqueCodeRegex);
	  System.out.println("after strAfterUniqueCodeRegex compile");
	  
	  matcher = pattern.matcher(returned_content);
	  
	  matcher.find(nCurOffset);
	  
	  int nEndOffset = matcher.start();
	  System.out.println("end offset: " + nEndOffset);
	  
	  if (nEndOffset <= nBeginOffset)
	  {
	  	System.out.println("EndOffset is < BeginOffset");
	  	throw new CustomEmptyStringException();
	  }
	  strDataValue = returned_content.substring(nBeginOffset,nEndOffset);
	  
	  System.out.println ("Raw Data Value: " + strDataValue);
	/*}
	catch (IOException ioe)
	{
		Sy
	}*/
  
  return(strDataValue);
	
	
}
	
	
	
public static String get_value(String local_data_set)
{
	String strDataValue="";
			
	
	try
	{
		if (ProcessingFunctions.preProcessing(local_data_set) != true)
		{
			throw new CustomEmptyStringException();
		}
	//run sql to get info about the data_set
	//Connection con = UtilityFunctions.db_connect();
	
	String query = "select * from extract_info where Data_Set='" + local_data_set + "'";
	
	System.out.println(query);
	
  //Statement stmt = con.createStatement();
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
	nCurOffset = regexSeekLoop("(?i)(<TABLE[^>]*>)",tables,nCurOffset);	
	
	System.out.println("Before table row searches.");
	nCurOffset = regexSeekLoop("(?i)(<tr[^>]*>)",rows,nCurOffset);

	System.out.println("Before table cell searches.");
	nCurOffset = regexSeekLoop("(?i)(<td[^>]*>)",cells,nCurOffset);
	
	System.out.println("Before div searches");
	nCurOffset = regexSeekLoop("(?i)(<div[^>]*>)",divs,nCurOffset);
	
	
	String strBeforeUniqueCode = rs.getString("Before_Unique_Code");
	String strBeforeUniqueCodeRegex = "(?i)(" + strBeforeUniqueCode + ")";
  String strAfterUniqueCode = rs.getString("After_Unique_Code");
  String strAfterUniqueCodeRegex = "(?i)(" + strAfterUniqueCode + ")";
  
  strDataValue = regexSnipValue(strBeforeUniqueCodeRegex,strAfterUniqueCodeRegex,nCurOffset);
  
  /*System.out.println(strBeforeUniqueCodeRegex);
  
  pattern = Pattern.compile(strBeforeUniqueCodeRegex);
  System.out.println("after strbeforeuniquecoderegex compile");
  
  matcher = pattern.matcher(returned_content);
  
  System.out.println("Current offset before final data extraction: " + nCurOffset);
  
  matcher.find(nCurOffset);
  
  int nBeginOffset = matcher.end();
  System.out.println("begin offset: " + nBeginOffset);
  
  pattern = Pattern.compile(strAfterUniqueCodeRegex);
  System.out.println("after strAfterUniqueCodeRegex compile");
  
  matcher = pattern.matcher(returned_content);
  
  matcher.find(nCurOffset);
  
  int nEndOffset = matcher.start();
  System.out.println("end offset: " + nEndOffset);
  
  if (nEndOffset <= nBeginOffset)
  {
  	System.out.println("EndOffset is < BeginOffset");
  	throw new CustomEmptyStringException();
  }
  strDataValue = returned_content.substring(nBeginOffset,nEndOffset);
  
  System.out.println ("Raw Data Value: " + strDataValue);*/
  
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
  
  
  
  //remove commas
  
  //return(strDataValue);
  
  /*query = "INSERT INTO fact_data (data_set,value,date_collected) VALUES ('" + local_data_set + "','" + strDataValue + "',NOW())";
  
  System.out.println(query);
  
  stmt = con.createStatement();
  boolean bRet = stmt.execute(query);*/
  
  
    
  
               
    
  //con.close();
    
  }catch (IllegalStateException ise)
  {
  	System.out.println("No regex match");
  	ise.printStackTrace();
  }
  catch (CustomEmptyStringException cese)
  {
  	System.out.println("CustomEmptyStringException thrown");
  }
  catch (TagNotFoundException tnfe)
  {
  	System.out.println("TagNotFoundException thrown");
  }
  catch( Exception e )
  {
      e.printStackTrace();
  }
  finally
  {
   	strDataValue = ProcessingFunctions.postProcessing(local_data_set,strDataValue);
   	System.out.println("Data Value: " + strDataValue);
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
		ResultSet rs = UtilityFunctions.db_run_query(query);
		rs.next();
		
  	
		
	
		//read in the url
		String strUrlStatic;
	  
	  strUrlStatic = rs.getString("url_static");
	  
	  if (rs.getString("url_dynamic") != "")
	  	strUrlStatic = strUrlStatic + rs.getString("url_dynamic");
	  	
	  System.out.println("Retrieving URL: " + strUrlStatic);
	  
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
		
		System.out.println("Done reading url contents");
	  	
		
		//seek to the top corner of the table
		System.out.println("Before table searches.");
		
		nCurOffset = regexSeekLoop("(?i)(<TABLE[^>]*>)",rs.getInt("table_count"),nCurOffset);
		
		nCurOffset = regexSeekLoop("(?i)(<tr[^>]*>)",rs.getInt("top_corner_row"),nCurOffset);
		
		/*String tmp = returned_content.substring(nCurOffset -200, nCurOffset +200);
		
		System.err.println(tmp);*/
		
		
		
	

	
		//iterate over rows, iterate over columns, writing values out to a csv file
		boolean done = false;
		int nNumOfColumns = rs.getInt("number_of_columns");
		
		
		String[] rowdata; 
		
		
		String strBeforeUniqueCodeRegex, strAfterUniqueCodeRegex, strDataValue;
		
		while (!done)
		{
			rowdata = new String[nNumOfColumns];
			
			for(int i=0;i< nNumOfColumns;i++)
			{
				
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
			
		}
	//import the csv file in using load file
	}
	catch (SQLException sqle)
	{
		System.err.println("Problem with query");
		sqle.printStackTrace();
	}
	catch (TagNotFoundException tnfe)
	{
		System.out.println("TagNotFoundException thrown");
	}
	catch (CustomEmptyStringException cese)
	{
		System.out.println("CustomEmptyStringException thrown");
	}
	catch (IOException ioe)
	{
		System.err.println("Problem with io");
		ioe.printStackTrace();		
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
		String query = "select * from company where in_dow=1 order by ticker";
		Statement stmt = con.createStatement();
		ResultSet rs = UtilityFunctions.db_run_query(query);
			
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
					
					/*if (strCurTicker.compareTo("AA") != 0)
						continue;*/
					
				
				
					if ((strCurDataSet.substring(0,5)).compareTo("table") == 0)
					{
						query = "update extract_table set url_dynamic='" + strCurTicker + "' where data_set='" + strCurDataSet + "'";
						stmt = con.createStatement();
						boolean bRet = stmt.execute(query);
						ArrayList<String[]> tabledata = get_table(strCurDataSet);
						tabledata = ProcessingFunctions.processTable(tabledata,strCurDataSet);
						
						UtilityFunctions.createCSV(tabledata,"fact_data_stage.csv",(count==0?false:true));
						String[] rowdata;
						for (int x=0;x<tabledata.size();x++)
						{
							rowdata = tabledata.get(x);
							for (int y=0;y<rowdata.length;y++)
							{
								System.out.print(rowdata[y]+"     ");
							}
							System.out.println("");
						}
					}
					else
					{
						query = "update extract_info set url_dynamic='" + strCurTicker + "' where Data_Set='" + strCurDataSet + "'";
						stmt = con.createStatement();
						boolean bRet = stmt.execute(query);
					
				
						System.out.println(query);
					
						System.out.println("Calling get value.");
						strDataValue = get_value(strCurDataSet);
						
						if (strDataValue.compareTo("") == 0)
						{
							System.out.println("Returned empty value '', skipping ");
							continue;
						}
				
							query = "INSERT INTO fact_data_stage (data_set,value,quarter,ticker,date_collected) VALUES ('" + strCurDataSet + "','" + 
							strDataValue + "','" + Integer.toString(40+i) + "','" + strCurTicker + "',NOW())";
							
							/* Use the following update statement for populating fiscal_calendar_begin */
							/*query = "UPDATE company set begin_fiscal_calendar='" + strDataValue + "' where ticker='" + strCurTicker + "'";*/
		 	  			System.out.println(query);
		  	 			stmt = con.createStatement();
		  				bRet = stmt.execute(query);
	  			}
	  		}
  			catch (SQLException sqle)
  			{
  				System.out.println("problem with insert sql statement");
  				System.err.println("Processing of data_set " + strCurDataSet + " with ticker " + strCurTicker + " FAILED ");
  				sqle.printStackTrace();
  			}
  				
				
				count++;	
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
	
	
	
	


