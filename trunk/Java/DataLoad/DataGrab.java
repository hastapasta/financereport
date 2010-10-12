/*CAREFUL with using a final data extraction pattern that matches the last searched table,row,cell,div pattern. */

package com.roeschter.jsl;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*; 
import org.apache.http.protocol.HTTP;
import java.util.Arrays;





public class DataGrab extends Thread
{
	
static String returned_content;
static String strCurDataSet;
static int nBatch;
UtilityFunctions uf;
ProcessingFunctions pf;
boolean bContinue;
static String strStaticTickerLimit = "";

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
	 	/*try 
	 	{*/
	 		if (bContinue == false)
	 			return;
	 		UtilityFunctions.stdoutwriter.writeln("=========================================================",Logs.NONE,"");
	 		UtilityFunctions.stdoutwriter.writeln("INITIATING THREAD",Logs.STATUS1,"DG1");
	 		//UtilityFunctions.stdoutwriter.writeln("TEST2",Logs.ERROR);
	 		//UtilityFunctions.stdoutwriter.writeln("TEST2.1",Logs.ERROR);
	 		grab_data_set();
			//clear_run_once();
	 		//sleep(60000);
	 
	 	/*}
	 	catch (InterruptedException ie)
	 	{
	 		UtilityFunctions.stdoutwriter.writeln("InterruptedException thrown",Logs.ERROR);
	 		UtilityFunctions.stdoutwriter.writeln(ie);
	 		stopThread();
	 		
	 	}*/
	 
	 	
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
			UtilityFunctions.stdoutwriter.writeln("Regex search exceeded.",Logs.ERROR,"DG2");
			throw new TagNotFoundException();
		}
		
		nCurOffset = matcher.start() + 1;
		
		uf.stdoutwriter.writeln("regex iteration " + i + ", offset: " + nCurOffset,Logs.STATUS2,"DG3");
		
	}
	return(nCurOffset);
}

String regexSnipValue(String strBeforeUniqueCode, String strAfterUniqueCode, int nCurOffset) throws CustomRegexException
{
	
		Pattern pattern;
		String strDataValue="";
		int nBeginOffset;
		Matcher matcher;
		
		

  	if ((strBeforeUniqueCode != null) && (strBeforeUniqueCode.isEmpty() != true))
  	{
  		
  		String strBeforeUniqueCodeRegex = "(?i)(" + strBeforeUniqueCode + ")";
	  uf.stdoutwriter.writeln(strBeforeUniqueCodeRegex,Logs.STATUS2,"DG4");
	  
	  pattern = Pattern.compile(strBeforeUniqueCodeRegex);
	  uf.stdoutwriter.writeln("after strbeforeuniquecoderegex compile", Logs.STATUS2,"DG5");
	  
	  matcher = pattern.matcher(returned_content);
	  
	  uf.stdoutwriter.writeln("Current offset before final data extraction: " + nCurOffset,Logs.STATUS2,"DG6");
	  
	  matcher.find(nCurOffset);
	  
	  nBeginOffset = matcher.end();
  	}
  	else
  		nBeginOffset = nCurOffset;
  		
	  uf.stdoutwriter.writeln("begin offset: " + nBeginOffset,Logs.STATUS2,"DG7");
	  
  	String strAfterUniqueCodeRegex = "(?i)(" + strAfterUniqueCode + ")";
	  pattern = Pattern.compile(strAfterUniqueCodeRegex);
	  uf.stdoutwriter.writeln("after strAfterUniqueCodeRegex compile",Logs.STATUS2,"DG8");
	  
	  matcher = pattern.matcher(returned_content);
	  
	  matcher.find(nBeginOffset);
	  
	  int nEndOffset = matcher.start();
	  uf.stdoutwriter.writeln("end offset: " + nEndOffset,Logs.STATUS2,"DG9");
	  
	  if (nEndOffset <= nBeginOffset)
	  {
		/* If we get here, skip processing this table cell but continue processing the rest of the table.*/
	  	uf.stdoutwriter.writeln("EndOffset is < BeginOffset",Logs.STATUS2,"DG10");
	  	throw new CustomRegexException();
	  }
	  strDataValue = returned_content.substring(nBeginOffset,nEndOffset);
	  
	  uf.stdoutwriter.writeln ("Raw Data Value: " + strDataValue,Logs.STATUS2,"DG11");
	/*}
	catch (IOException ioe)
	{
		Sy
	}*/
  
  return(strDataValue);
	
	
}

public void clear_run_once()
{
	try
	{
		String query = "update schedule set run_once=0";
		uf.db_update_query(query);
	}
	catch (SQLException sqle)
	{
		//OFP 9/26/2010 - Need to put in a pause mechanism for when running under the jsp pages.
		//DataLoad.setPause();
		uf.stdoutwriter.writeln("Problem clearing run_once flag",Logs.ERROR,"DG13");
		uf.stdoutwriter.writeln(sqle);
		
	}
}

public String get_value(String local_data_set)
{
	String strDataValue="";
			
	
	try
	{
	
	//run sql to get info about the data_set
	//Connection con = UtilityFunctions.db_connect();
	
	String query = "select * from extract_info where Data_Set='" + local_data_set + "'";
	
  //Statement stmt = con.createStatement();
  ResultSet rs = uf.db_run_query(query);
  
  rs.next();
  
  int tables,cells,rows,divs;
  tables = rs.getInt("Table_Count");
  cells = rs.getInt("Cell_Count");
  rows = rs.getInt("Row_Count");
  divs = rs.getInt("Div_Count");
  
  /*String strUrlStatic;
  
  strUrlStatic = rs.getString("url_static");
  
  if (rs.getString("url_dynamic") != "")
  	strUrlStatic = strUrlStatic + rs.getString("url_dynamic");*/
  	
  //strUrlStatic = "http://localhost/tabletest.html";
  	
  //uf.stdoutwriter.writeln("Retrieving URL: " + strUrlStatic,Logs.STATUS2);
  
 
  	
  int nCurOffset = 0;
  
  //get_url(strUrlStatic);
  
  //strUrlStatic = "http://localhost/tabletest2.html";
  
  /*URL urlStatic = new URL(strUrlStatic);
  
  BufferedReader in = new BufferedReader(
				new InputStreamReader(
				urlStatic.openStream()));
				
	 returned_content = "";
	 String curLine = "";
	 int nTmp;*/

  //changed from readline to read so that end-of-line characters are included
  //this is to match the php regex searches
  //I'm sure this can be optimized performance-wise
	//while (( curLine= in.readLine()) != null)
	//    returned_content = returned_content + curLine;
	/*while ((nTmp = in.read()) != -1)
		returned_content = returned_content + (char)nTmp;
	

	in.close();
	
	uf.stdoutwriter.writeln("Done reading url contents",Logs.STATUS2);*/
	
	//uf.stdoutwriter.writeln(returned_content);
	
	/*
		Initial regex search.
	*/
	
	String strInitialOpenUniqueCode = rs.getString("Initial_Bef_Unique_Code");
	Pattern pattern;
	Matcher matcher;
	if ((strInitialOpenUniqueCode != null) && (strInitialOpenUniqueCode.isEmpty() == false))
	{
		String strInitialOpenUniqueRegex = "(?i)(" + strInitialOpenUniqueCode + ")";
		
		uf.stdoutwriter.writeln("Initial Open Regex: " + strInitialOpenUniqueRegex,Logs.STATUS2,"DG14");
		
		pattern = Pattern.compile(strInitialOpenUniqueRegex);
		
		matcher = pattern.matcher(returned_content);
		
		matcher.find();
		
		//nCurOffset = matcher.end();
		nCurOffset = matcher.start();
		
		uf.stdoutwriter.writeln("Offset after initial regex search: " + nCurOffset,Logs.STATUS2,"DG15");
				
	}
	
	
	
	/*
		End initial regex search.
	*/
	
	uf.stdoutwriter.writeln("Before table searches.",Logs.STATUS2,"DG16");
	nCurOffset = regexSeekLoop("(?i)(<TABLE[^>]*>)",tables,nCurOffset);	
	
	uf.stdoutwriter.writeln("Before table row searches.",Logs.STATUS2,"DG17");
	nCurOffset = regexSeekLoop("(?i)(<tr[^>]*>)",rows,nCurOffset);

	uf.stdoutwriter.writeln("Before table cell searches.",Logs.STATUS2,"DG18");
	nCurOffset = regexSeekLoop("(?i)(<td[^>]*>)",cells,nCurOffset);
	
	uf.stdoutwriter.writeln("Before div searches",Logs.STATUS2,"DG19");
	nCurOffset = regexSeekLoop("(?i)(<div[^>]*>)",divs,nCurOffset);
	
	
	String strBeforeUniqueCode = rs.getString("Before_Unique_Code");
	String strAfterUniqueCode = rs.getString("After_Unique_Code");
	/*String strBeforeUniqueCodeRegex = "(?i)(" + strBeforeUniqueCode + ")";

  String strAfterUniqueCodeRegex = "(?i)(" + strAfterUniqueCode + ")";*/
  
  strDataValue = regexSnipValue(strBeforeUniqueCode,strAfterUniqueCode,nCurOffset);
  
 
  strDataValue = strDataValue.replace(",","");
  
  strDataValue = strDataValue.replace("&nbsp;","");
  
  if (strDataValue.compareTo("") != 0)
  {
  	UtilityFunctions.stdoutwriter.writeln("checking for negative data value",Logs.STATUS2,"DG20");
  	UtilityFunctions.stdoutwriter.writeln(strDataValue.substring(0,1),Logs.STATUS2,"DG21");
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
  	uf.stdoutwriter.writeln("No regex match",Logs.ERROR,"DG22");
  	uf.stdoutwriter.writeln(ise);
  }
  /*catch (CustomEmptyStringException cese)
  {
  	uf.stdoutwriter.writeln("CustomEmptyStringException thrown",Logs.ERROR);
  	uf.stdoutwriter.writeln(cese);
  }*/
  catch (TagNotFoundException tnfe)
  {
  	uf.stdoutwriter.writeln("TagNotFoundException thrown",Logs.ERROR,"DG23");
  	uf.stdoutwriter.writeln(tnfe);
  }
  catch( Exception e )
  {
      uf.stdoutwriter.writeln(e);
  }
  finally
  {
	  uf.stdoutwriter.writeln("Data Value: " + strDataValue,Logs.STATUS2,"DG24");
	  return(strDataValue);
  }




}

public ArrayList<String[]> get_table_with_headers(String strTableSet) throws SQLException
{
	
	
	
	
	/* 
	 * OFP 9/26/2010: Here's how this works. There are three datasets in extract_table: <data_set>_body, <data_set>_colhead and <data_set>_rowhead.
	 * Only <data_set>_body will exist in the schedule table. This function is responsible for altering the dataset name and processing all 3.
	 */
	
	ArrayList<String[]> tabledatabody = get_table(strTableSet);

	
	String strTmpTableSet = strTableSet.replace("_body","_colhead");
	
	
	/* Only process table headers if the _colhead dataset exists */
	if (strTableSet.indexOf("body")!=-1)
	{
	
		ArrayList<String[]> tabledatacol = get_table(strTmpTableSet);
		
		strTmpTableSet = strTableSet.replace("_body","_rowhead");
		
		
		ArrayList<String[]> tabledatarow = get_table(strTmpTableSet);
	
		
		//Merge table data
		tabledatabody.add(0,tabledatacol.get(0));
		String[] temp = new String[tabledatarow.size()];
		for (int i=0;i<tabledatarow.size();i++)
		{
			temp[i] = tabledatarow.get(i)[0];
			
		}
		
		tabledatabody.add(1,temp);
	}
    return(tabledatabody);
	
}

public void get_url(String strDataSet) throws SQLException, MalformedURLException, IOException
{
	/*String query = "select * from extract_table where data_set='" + strTableSet + "'";*/
	
	
	  String query;
	 
	  	/*retrieve url data */
	  	if ((strDataSet.substring(0,5)).compareTo("table") == 0)
	  		query = "select * from extract_table where data_set='" + strDataSet + "'";
	  	else 
	  		query = "select * from extract_info where data_set='" + strDataSet + "'";
		ResultSet rs2 = uf.db_run_query(query);
		rs2.next();
		URL urlFinal;
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		
		
		 if ((rs2.getInt("input_source") != 0))
		 {
			 query = "select * from input_source where primary_key=" + rs2.getInt("input_source");
			 ResultSet rs3 = uf.db_run_query(query);
			 rs3.next();
			 String strStProperties = rs3.getString("form_static_properties");
			 String[] listItems = strStProperties.split(":");
			 String[] listItems2;
			 String data="";
			 for (int i=0;i<listItems.length;i++)
			 {
				 listItems2 = listItems[i].split("=");
				 if (i!=0)
				 {
					 data+= "&";
				 }
				 
				 //check to see if there is an equal sign or a value on the right side of the equality
				 if (listItems2.length ==1)
					 data+= URLEncoder.encode(listItems2[0], "UTF-8") + "=";
				 else
					 data+= URLEncoder.encode(listItems2[0], "UTF-8") + "=" + URLEncoder.encode(listItems2[1], "UTF-8");
				 
				
				 
			 }

			 //urlFinal = new URL(rs3.getString("url_form"));
			 
			 urlFinal = new URL("http://data.bls.gov/cgi-bin/surveymost");
			 
			 UtilityFunctions.stdoutwriter.writeln("Retrieving URL Form: " + rs3.getString("url_form"),Logs.STATUS2,"DG24.5");
			 //URLConnection conn = urlFinal.openConnection();
	
			 //HttpURLConnection hconn = (HttpURLConnection)urlFinal.openConnection();
			 //hconn.setRequestMethod("POST");
			 //HttpClient = new HttpClient();
			 
			 data="series_id=LNS14000000&survey=ln&format=&html_tables=&delimiter=&catalog=&print_line_length=&lines_per_page=&row_stub_key=&year=&date=&net_change_start=&net_change_end=&percent_change_start=&percent_change_end=";
			 HttpPost httppost = new HttpPost("http://data.bls.gov/cgi-bin/surveymost"); 
			 
			 /*
			  * The following line fixes an issue where a non-fatal error is displayed about an invalid cookie data format.
			  */
				httppost.getParams().setParameter("http.protocol.cookie-datepatterns", 
						Arrays.asList("EEE, dd MMM-yyyy-HH:mm:ss z", "EEE, dd MMM yyyy HH:mm:ss z"));
			 
			 List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		        nvps.add(new BasicNameValuePair("series_id", "LNS14000000"));
		        nvps.add(new BasicNameValuePair("survey", "ln"));

		     httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		     response = httpclient.execute(httppost);

			 
			 

			 
			 
			
			 



			 
			 /*hconn.setDoOutput(true);
			 hconn.setDoInput(true);
			 OutputStreamWriter wr = new OutputStreamWriter(hconn.getOutputStream());
			 OutputStream os = hconn.getOutputStream();
			 
			 wr.write(data);
			 wr.flush();*/
			 //os.write(data);
			 
	

			 
			 
			

			 
		 }
		 else
		 {
	 
			String strURL;
			strURL = rs2.getString("url_static");
					  
			if (rs2.getString("url_dynamic") != "")
				strURL = strURL + rs2.getString("url_dynamic");
			
			uf.stdoutwriter.writeln("Retrieving URL: " + strURL,Logs.STATUS2,"DG25");
			
			HttpGet httpget = new HttpGet(strURL); 
			/*
			 * The following line fixes an issue where a non-fatal error is displayed about an invalid cookie data format.
			 */
			httpget.getParams().setParameter("http.protocol.cookie-datepatterns", 
					Arrays.asList("EEE, dd MMM-yyyy-HH:mm:ss z", "EEE, dd MMM yyyy HH:mm:ss z"));
					  
			response = httpclient.execute(httpget);
			//urlFinal = new URL(strURL);
		 }
		 
		 HttpEntity entity = response.getEntity();
		 
		  BufferedReader in = new BufferedReader(
					new InputStreamReader(
					entity.getContent()));
	  
	
				
	  int nTmp;			

	  returned_content="";
	
	  while ((nTmp = in.read()) != -1)
		returned_content = returned_content + (char)nTmp;
	

	  in.close();
	
	  uf.stdoutwriter.writeln("Done reading url contents",Logs.STATUS2,"DG26");
		 
  

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
		int nDataRows = rs.getInt("rowsofdata");
		int nRowInterval = rs.getInt("rowinterval");
		String strEndDataMarker = rs.getString("end_data_marker");
		boolean bColTHtags = true;
		if (rs.getInt("column_th_tags") != 1)
			bColTHtags=false;
		
		int nCurOffset = 0;
		//seek to the top corner of the table
		uf.stdoutwriter.writeln("Before table searches.",Logs.STATUS2,"DG27");
		
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
			uf.stdoutwriter.writeln("row: " + nRowCount,Logs.STATUS2,"DG28");
			rowdata = new String[nNumOfColumns];
			
			for(int i=0;i< nNumOfColumns;i++)
			{
				

				uf.stdoutwriter.writeln("Column: " + i,Logs.STATUS2,"DG29");
				
				if (bColTHtags == false)
					nCurOffset = regexSeekLoop("(?i)(<td[^>]*>)",rs.getInt("Column" + (i+1)),nCurOffset);
				else
					nCurOffset = regexSeekLoop("(?i)(<th[^>]*>)",rs.getInt("Column" + (i+1)),nCurOffset);
				
				//String strBeforeUniqueCode = rs.getString("bef_code_col" + (i+1));
				strBeforeUniqueCodeRegex = "(?i)(" + rs.getString("bef_code_col" + (i+1)) + ")";
			  //String strAfterUniqueCode = rs.getString("aft_code_col" + (i+1));
			  strAfterUniqueCodeRegex = "(?i)(" + rs.getString("aft_code_col" + (i+1)) + ")";
			  try
			  {
				  strDataValue = regexSnipValue(strBeforeUniqueCodeRegex,strAfterUniqueCodeRegex,nCurOffset);
				  
				  /*
				   * Going to strip out &nbsp; for all data streams, let's see if this is a problem.
				   */
				  if (strEndDataMarker != null && (strEndDataMarker.length() > 0))
					  if (strDataValue.equals(strEndDataMarker))
					  {
						  /*
						   * Finished grabbing data for this table.
						   */
						  nCurOffset = nEndTableOffset; //this breaks us out of the while loop
						  break; //this breaks us out of the for loop
					  }
				  
				  rowdata[i] = strDataValue.replace("&nbsp;","");
			  }
			  catch (CustomRegexException cre)
			  {
				  uf.stdoutwriter.writeln("Empty cell in table in url stream. Voiding cell.",Logs.STATUS2,"DG30");
				  rowdata[i] = "void";
			  }
			  
			  
				
				
			}
			
			tabledata.add(rowdata);
			
			nCurOffset = regexSeekLoop("(?i)(<tr[^>]*>)",nRowInterval,nCurOffset);
			nRowCount++;
			
			/*
			 * Identified 3 table exit types
			 * 1) fixed size (fixed # of rows & columns) (handled below)
			 * -dynamic
			 * 2) end table tag (handled below)
			 * 3) end cell value (come across a cell value that is invalid or otherwise marks
			 * that there is no more data) (handled above with strEndDataMarker)
			 */
			if ((nEndTableOffset < nCurOffset) || (nDataRows == nRowCount))
				break;
			
			
		}
  	   
	
	}
	catch (SQLException sqle)
	{
		uf.stdoutwriter.writeln("Problem with query",Logs.ERROR,"DG31");
		uf.stdoutwriter.writeln(sqle);
	}
	catch (TagNotFoundException tnfe)
	{
		uf.stdoutwriter.writeln("TagNotFoundException thrown",Logs.ERROR,"DG32");
		uf.stdoutwriter.writeln(tnfe);
	}
	catch (Exception ex)
	{
		uf.stdoutwriter.writeln("Exception thrown",Logs.ERROR,"DG33");
		uf.stdoutwriter.writeln(ex);
	}
	finally
	{
		return(tabledata);
	}
}



public ArrayList<String> get_list_dataset_run_once()
{
	//UtilityFunctions.stdoutwriter.writeln("TEST2.7",Logs.ERROR);
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
		uf.stdoutwriter.writeln("problem with retrieving data sets from schedule table",Logs.ERROR,"DG34");
		uf.stdoutwriter.writeln(sqle);
	}

		

	uf.stdoutwriter.writeln("Processing " + count + " data sets.",Logs.STATUS1,"DG35");
	uf.stdoutwriter.writeln("Loading batch: " + this.nBatch,Logs.STATUS1,"DG36");
	return(tmpAL);
}



public void grab_data_set()
{
	//UtilityFunctions.stdoutwriter.writeln("TEST2.5",Logs.ERROR);
	try
	{
	//String[] data_sets = {"yahoo_q109_income", "yahoo_q209_income", "yahoo_q309_income", "yahoo_q409_income"};
	//String[] data_sets = {""};
	
	
	
	ArrayList<String> data_sets = get_list_dataset_run_once();
	
	//UtilityFunctions.stdoutwriter.writeln("TEST3",Logs.ERROR);

	
	for (int i=0;i<data_sets.size();i++)
	{
		
		strCurDataSet = data_sets.get(i);
		uf.stdoutwriter.writeln("PROCESSING DATA SET " + strCurDataSet,Logs.STATUS1,"DG37");
		Calendar cal = Calendar.getInstance();
		java.util.Date d = cal.getTime();
		uf.stdoutwriter.writeln("DATA SET START TIME: " + d.toString(),Logs.STATUS1,"DG38");
		
		nBatch = uf.getBatchNumber();
		
		
		String query = "select companygroup from schedule where data_set='" + strCurDataSet + "'";
		ResultSet rs = uf.db_run_query(query);
		rs.next();
		String group = rs.getString("companygroup");
		
		query = "update schedule set last_run=NOW() where data_set='" + strCurDataSet + "'";
		uf.db_update_query(query);
		
		if (group.compareTo("none") == 0)
		{
			//this extract process is not associated with a group of companies.
			if ((strCurDataSet.substring(0,5)).compareTo("table") == 0)
			{
				
				pf.preProcessingTable(strCurDataSet,"");
				
				get_url(strCurDataSet);
				 
				ArrayList<String[]> tabledata = get_table_with_headers(strCurDataSet);
				//pf.processTableSAndPCoList(tabledata,strCurDataSet,uf);
				//need to add the quarter values somewhere around here.
				
				ArrayList<String[]> tabledata2 = pf.postProcessingTable(tabledata, strCurDataSet);
				
				if (strCurDataSet.equals("table_sandp_co_list") != true) //already imported data in the processing function					
					uf.importTableIntoDB(tabledata2,"fact_data_stage");
			
				
				/*String[] rowdata;
				for (int x=0;x<tabledata.size();x++)
				{
					rowdata = tabledata.get(x);
					for (int y=0;y<rowdata.length;y++)
					{
						System.out.print(rowdata[y]+"     ");
					}
				}*/
			}
			/*
			 * NEED to add code for ELSE condition, i.e. NON-GROUP, NON-TABLE EXTRACTIONS
			 */

			
		}
		else
		{
		
			query = "select * from company where groups like '%" + group + "%' order by ticker";
			
		
			rs = uf.db_run_query(query);
				
			String strCurTicker="";
			String fullUrl;
			String strDataValue="";
			int count=0;
		
		
		
		
			/* Loop through the list of stock tickers */
			while(rs.next())
			{
				try
				{
					strCurTicker = rs.getString("ticker");
					
					uf.stdoutwriter.writeln("Processing ticker: " + strCurTicker,Logs.STATUS1,"DG39");
					
					/*Active only to debug individual tickers */
					if (strStaticTickerLimit.isEmpty() != true)
						if (strCurTicker.compareTo(strStaticTickerLimit) != 0) 
							continue;
						else
						{
							int o=0;
							o++;
						}
						
					
				
				
					if ((strCurDataSet.substring(0,5)).compareTo("table") == 0)
					{
						try
						{
								/*Updating the dynamic url in the preprocessing function for now */
								query = "update extract_table set url_dynamic='" + strCurTicker + "' where Data_Set='" + strCurDataSet + "'";
								uf.db_update_query(query);
								
							
							pf.preProcessingTable(strCurDataSet,strCurTicker);
							
	
							 
							 get_url(strCurDataSet);
							
							/*perform no data check here*/
							if (pf.preNoDataCheck(strCurDataSet) == true)
							{
								uf.stdoutwriter.writeln("URL contains no data, skipping ticker", Logs.STATUS1,"DG40");
								continue;
							}
							
							ArrayList<String[]> tabledata = get_table_with_headers(strCurDataSet);
						 
							ArrayList<String[]> tabledata2 = pf.postProcessingTable(tabledata, strCurDataSet);
											
						
							uf.importTableIntoDB(tabledata2,"fact_data_stage");
							
						
						
							//UtilityFunctions.createCSV(tabledata,"fact_data_stage.csv",(count==0?false:true));
							
							//UtilityFunctions.loadCSV("fact_data_stage.csv");
							
							/*String[] rowdata;
							for (int x=0;x<tabledata.size();x++)
							{
								rowdata = tabledata.get(x);
								for (int y=0;y<rowdata.length;y++)
								{
									uf.stdoutwriter.writeln(rowdata[y]+"     ",Logs.STATUS2);
									//System.out.print(rowdata[y]+"     ");
								}
	
							}*/
						}
						catch (MalformedURLException mue)
						{
							uf.stdoutwriter.writeln("Badly formed url, skipping ticker",Logs.ERROR,"DG41");
							uf.stdoutwriter.writeln(mue);
						}
						catch (SQLException sqle)
						{
							uf.stdoutwriter.writeln("Problem issuing sql statement, skipping ticker",Logs.ERROR,"DG42");
							uf.stdoutwriter.writeln(sqle);
						}
						catch (IOException ioe)
						{
							uf.stdoutwriter.writeln("Problem with I/O, skipping ticker",Logs.ERROR,"DG43");
							uf.stdoutwriter.writeln(ioe);
						}
						catch (Exception e)
						{
							uf.stdoutwriter.writeln("Processing table for ticker " + strCurTicker + " failed, skipping",Logs.ERROR,"DG44");
							uf.stdoutwriter.writeln(e);
						}
				
					}
					else
					{
						
							try
							{
								query = "update extract_info set url_dynamic='" + strCurTicker + "' where Data_Set='" + strCurDataSet + "'";
								uf.db_update_query(query);
							
								uf.stdoutwriter.writeln("Calling get value.",Logs.STATUS2,"DG45");
								
								if (pf.preProcessing(strCurDataSet,strCurTicker) != true)
								{
									throw new CustomEmptyStringException();
								}
								
								get_url(strCurDataSet);
								
								if (pf.preNoDataCheck(strCurDataSet) == true)
								{
									uf.stdoutwriter.writeln("URL contains no data, skipping ticker", Logs.STATUS1,"DG46");
									continue;
								}
										
								
								strDataValue = get_value(strCurDataSet);
							
								if (strDataValue.compareTo("") == 0)
								{
									uf.stdoutwriter.writeln("Returned empty value '', skipping ",Logs.STATUS2,"DG47");
									continue;
								}
								
								ArrayList<String []> tabledata = new ArrayList<String []>();
								
								String[] tmp = {strDataValue};
								
								tabledata.add(tmp);
								
								
							
								
								
								ResultSet rs2 = uf.db_run_query("select custom_insert from extract_info where data_set='" + strCurDataSet + "'");
								rs2.next();
				
								
								
							   	ArrayList<String []> tabledata2 = pf.postProcessing(tabledata, strCurDataSet);
							   	
							   	/*Probably need to add another column in the db for import table*/
							   	//if (strCurDataSet.equals("sec_fiscal_year_begin"))
							   	//	uf.customDataCheck(tabledata2, strCurTicker);
							   	if (strCurDataSet.equals("nasdaq_fiscal_year_begin") || strCurDataSet.equals("sec_fiscal_year_begin"))
							   		uf.updateTableIntoDB(tabledata2,"company");
							   	else
							   		uf.importTableIntoDB(tabledata2,"fact_data_stage");
							}
							catch (MalformedURLException mue)
							{
								uf.stdoutwriter.writeln("Badly formed url, skipping ticker",Logs.ERROR,"DG48");
								uf.stdoutwriter.writeln(mue);
							}
							catch (SQLException sqle)
							{
								uf.stdoutwriter.writeln("Problem issuing sql statement, skipping ticker",Logs.ERROR,"DG49");
								uf.stdoutwriter.writeln(sqle);
							}
							catch (IOException ioe)
							{
								uf.stdoutwriter.writeln("Problem with I/O, skipping ticker",Logs.ERROR,"DG50");
								uf.stdoutwriter.writeln(ioe);
							}
							catch (Exception e)
							{
								uf.stdoutwriter.writeln("Processing table for ticker " + strCurTicker + " failed, skipping",Logs.ERROR,"DG51");
								uf.stdoutwriter.writeln(e);
							}
							
						   	
							
							
							
							//Some of this can be sped up by not running these read queries on every ticker iteration.
							
							//if the insert is not handled here it should have already been handled in the postProcessing function.
							
							/*if (rs2.getBoolean("custom_insert")!=true)
							{
	 							query = "INSERT INTO fact_data_stage (data_set,value,adj_quarter,ticker,date_collected,batch) VALUES ('" + strCurDataSet + "','" + 
								strDataValue + "','" + Integer.toString(nAdjQuarter) + "','" + strCurTicker + "',NOW()," + Integer.toString(uf.getBatchNumber()) + ")";
	 							uf.db_update_query(query);
	 							
							}*/
							
			  	 			//uf.db_update_query(query);
					}
						
						
	  			
	  		}
  			catch (SQLException sqle)
  			{
  				uf.stdoutwriter.writeln("problem with sql statement in grab_data_set",Logs.ERROR,"DG52");
  				uf.stdoutwriter.writeln("Processing of data_set " + strCurDataSet + " with ticker " + strCurTicker + " FAILED ",Logs.ERROR,"DG53");
  				uf.stdoutwriter.writeln(sqle);
  			}

  			
  				
				
				count++;	
			}
		}
		
		cal = Calendar.getInstance();
		d = cal.getTime();
		uf.stdoutwriter.writeln("DATA SET END TIME: " + d.toString(),Logs.STATUS1,"DG54");
	}

	}
	catch (Exception e)
	{
		uf.stdoutwriter.writeln("Exception in grab_data_set",Logs.ERROR,"DG55");
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

class CustomRegexException extends Exception
{
	void CustomRegexException()
	{
		
	}
}

class TagNotFoundException extends Exception
{
	void TagNoutFoundException()
	{
		
	}
	
	
}
	
	
	
	


