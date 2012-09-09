package com.pikefin.oldlogic;
 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pikefin.log4jWrapper.Logs;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

import com.pikefin.businessobjects.Job;

/*
 * GENERAL COMMENTS ON PROCESSING FUNCTIONS
 * OFP 12/13/2010: Since the data_group field was removed from the fact_table (because it can be referenced in
 * the jobs table through the data_set), it was removed from all processing functions that were inserting it
 * into the fact_table.
 * 
 */


class ProcessingFunctions {
	/*Individual extraction processing function parameters*/
	
	String strDataValue;

	/*RIght now this is only set during the preProcessing function */
	String strTicker;
	String strTemp1, strTemp2, strTemp3;
	
	/*table extraction processing function parameters*/
	ArrayList<String[]> propTableData;
	//String propStrTableDataSet;
	Job j;
	
	//UtilityFunctions uf;
	DataGrab dg;
	DBFunctions dbf;
	
	//CustomBufferedWriter stdoutwriter;
	
	public ProcessingFunctions(DataGrab tmpDG) {
		//this.uf = tmpUF;
		this.dg = tmpDG;
		this.dbf = dg.dbf;
	}
	
	/*public ProcessingFunctions() {
		
	}*/
	
	public boolean preNoDataCheck(String strDataSet) {
		try	{
			String query;
			/*if ((strDataSet.substring(0,5)).compareTo("table") == 0)
				query = "select pre_nodata_check_func from extract_tables where Data_Set='" + strDataSet + "'";
			else
				query = "select pre_nodata_check_func from extract_singles where Data_Set='" + strDataSet + "'";*/
			
			query = "select pre_nodata_check_func from jobs where data_set='" + strDataSet + "'";
			
			//ResultSet rs = dbf.db_run_query(query);
			SqlRowSet rs = dbf.dbSpringRunQuery(query);
			rs.next();
			String strFunctionName = rs.getString("pre_nodata_check_func");
			
			UtilityFunctions.stdoutwriter.writeln("Pre No Data Check function name: " + strFunctionName,Logs.STATUS2,"PF1");
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
			{
				UtilityFunctions.stdoutwriter.writeln("No data check function, assuming data is ok",Logs.STATUS2,"PF2");
				return(false);
			}

			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception e)
		{
			UtilityFunctions.stdoutwriter.writeln("preNoDataCheck method call failed",Logs.ERROR,"PF3");
			UtilityFunctions.stdoutwriter.writeln(e);
			return(false);
			
		}
		
	}
	
	public boolean preJobProcessing(Job j)	{
		//String query = "select pre_job_process_func_name from jobs where Data_Set='" + strDataSet + "'";
		
		
		
		
		try	{
			//ResultSet rs = dbf.db_run_query(query);
			//SqlRowSet rs = dbf.dbSpringRunQuery(query);
			//rs.next();
			String strFunctionName = j.getPreJobProcessFuncName();
			
			UtilityFunctions.stdoutwriter.writeln("Pre Job Process Func Name: " + strFunctionName,Logs.STATUS2,"PF3.2");
			//this.strTicker = strTicker;
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
				UtilityFunctions.stdoutwriter.writeln("No pre job process function, exiting...",Logs.STATUS2,"PF3.3");
				return(true);
			}
	
			/*strDataValue = "";
			strTemp1 = "";
			strTemp2 = "";
			strTemp3 = "";*/
			//strStaticDataSet = strDataSet;

			UtilityFunctions.stdoutwriter.writeln(strFunctionName,Logs.STATUS2,"PF3.35");
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception tmpE)
		{
			UtilityFunctions.stdoutwriter.writeln("pre Job Processing method call failed",Logs.ERROR,"PF3.4");
			UtilityFunctions.stdoutwriter.writeln(tmpE);
			return(false);
		}
	}
	
	public boolean postJobProcessing(Job j)	{
		//String query = "select post_job_process_func_name from jobs where Data_Set='" + strDataSet + "'";
		
		
		
		
		try	{
			//ResultSet rs = dbf.db_run_query(query);
			//SqlRowSet rs = dbf.dbSpringRunQuery(query);
			
			//rs.next();
			String strFunctionName = j.getPostJobProcessFuncName();
			
			UtilityFunctions.stdoutwriter.writeln("Post Job Process Func Name: " + strFunctionName,Logs.STATUS2,"PF3.5");
			//this.strTicker = strTicker;
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
			{
				UtilityFunctions.stdoutwriter.writeln("No post job process function, exiting...",Logs.STATUS2,"PF3.7");
				return(true);
			}
	
			/*strDataValue = "";
			strTemp1 = "";
			strTemp2 = "";
			strTemp3 = "";*/
			//strStaticDataSet = strDataSet;

			UtilityFunctions.stdoutwriter.writeln(strFunctionName,Logs.STATUS2,"PF3.8");
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception tmpE)
		{
			UtilityFunctions.stdoutwriter.writeln("post Job Processing method call failed",Logs.ERROR,"PF3.9");
			UtilityFunctions.stdoutwriter.writeln(tmpE);
			return(false);
		}
	}
	

	public boolean preProcessing(Job j, String strTicker) {
	  /* For the return value, return false if the attempt to grab the value should be skipped. */
	  
	

		//String query = "select pre_process_func_name from jobs where Data_Set='" + strDataSet + "'";
		
		
		
	
		try	{
			//ResultSet rs = dbf.db_run_query(query);
			//SqlRowSet rs = dbf.dbSpringRunQuery(query);
			
			//rs.next();
			String strFunctionName = j.getPreProcessFuncName();
			
			UtilityFunctions.stdoutwriter.writeln("Pre Process Func Name: " + strFunctionName,Logs.STATUS2,"PF4");
			this.strTicker = strTicker;
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
			{
				UtilityFunctions.stdoutwriter.writeln("No pre process function, exiting...",Logs.STATUS2,"PF5");
				return(true);
			}
	
			strDataValue = "";
			strTemp1 = "";
			strTemp2 = "";
			strTemp3 = "";
			//strStaticDataSet = strDataSet;

			UtilityFunctions.stdoutwriter.writeln(strFunctionName,Logs.STATUS2,"PF6");
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception tmpE)
		{
			UtilityFunctions.stdoutwriter.writeln("preProcessing method call failed",Logs.ERROR,"PF7");
			UtilityFunctions.stdoutwriter.writeln(tmpE);
			return(false);
		}

}


public ArrayList<String []> postProcessing(ArrayList<String []> tabledata , Job inputJob) throws SkipLoadException,CustomEmptyStringException {
	
		//String query = "select post_process_func_name from jobs where Data_Set='" + strDataSet + "'";
		
		/*
		 * This is for non-table extraction. However this will throw a nullpointerexception if no column header
		 * data is extracted during a table extraction.
		 */
		if (tabledata.get(0) != null)
			strDataValue = tabledata.get(0)[0];

	
		try	{
			
			//ResultSet rs = dbf.db_run_query(query);
			//SqlRowSet rs = dbf.dbSpringRunQuery(query);
			
			//rs.next();
			String strFunctionName = inputJob.getPostProcessFuncName();
			
			UtilityFunctions.stdoutwriter.writeln("Post Process Func Name: " + strFunctionName,Logs.STATUS2,"PF8");
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
				UtilityFunctions.stdoutwriter.writeln("No post process function, exiting...",Logs.STATUS2,"PF9");
				return(tabledata);
			}
			
			propTableData = tabledata;
			//propStrTableDataSet = strDataSet;
			j = inputJob;
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			m.invoke(this, new Object[] {});
		}
		/* Need to break this down into individual exceptions */
		catch (IllegalAccessException iae)
		{
			UtilityFunctions.stdoutwriter.writeln("postProcessing method call failed",Logs.ERROR,"PF16.1");
			UtilityFunctions.stdoutwriter.writeln(iae);
		
		}
		//any exceptions thrown by the function pointer are wrapped in an InvocationTargetException
		catch (InvocationTargetException ite)
		{
			if (ite.getTargetException().getClass().getSimpleName().equals("SkipLoadException"))
				throw new SkipLoadException();
			else if (ite.getTargetException().getClass().getSimpleName().equals("CustomEmptyStringException")) {
				/*
				 * To do: pass along the exception message.
				 */
				throw new CustomEmptyStringException();
			}
			else{ 
				UtilityFunctions.stdoutwriter.writeln("postProcessing method call failed",Logs.ERROR,"PF16.3");
				if (ite.getCause() != null) {
					UtilityFunctions.stdoutwriter.writeln((Exception)ite.getCause());
				}

			}
		
		}
		catch (NoSuchMethodException nsme) {
			UtilityFunctions.stdoutwriter.writeln("postProcessing method call failed",Logs.ERROR,"PF16.5");
			UtilityFunctions.stdoutwriter.writeln(nsme);
		}
		catch (DataAccessException tmpE) {
			UtilityFunctions.stdoutwriter.writeln("postProcessing method call failed",Logs.ERROR,"PF17");
			UtilityFunctions.stdoutwriter.writeln(tmpE);
		}
		
		return(propTableData);
	



}

/* 
 * The table and non-table general processing methods can probably be merged.
 */

/*public boolean preProcessingTable(String strDataSet, String strCurTicker)
{
	  //For the return value, return false if the attempt to grab the value should be skipped. 
	  
		//UtilityFunctions.stdoutwriter.writeln("TEST5",Logs.ERROR);

		String query = "select pre_process_func_name from jobs where Data_Set='" + strDataSet + "'";
		
		try
		{
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			String strFunctionName = rs.getString("pre_process_func_name");
			
			UtilityFunctions.stdoutwriter.writeln("Pre Process Func Name: " + strFunctionName,Logs.STATUS2,"PF11");
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
			{
				UtilityFunctions.stdoutwriter.writeln("No pre process function, exiting...",Logs.STATUS2,"PF12");
				return(true);
			}
			
			strDataValue = "";
			strTemp1 = "";
			strTemp2 = "";
			strTemp3 = "";
			//strStaticDataSet = strDataSet;
			strTicker = strCurTicker;
			UtilityFunctions.stdoutwriter.writeln(strFunctionName,Logs.STATUS2,"PF13");
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception tmpE)
		{
			UtilityFunctions.stdoutwriter.writeln("preProcessingTable method call failed",Logs.ERROR,"PF14");
			UtilityFunctions.stdoutwriter.writeln(tmpE);
			return(false);
		}

}


public ArrayList<String[]> postProcessingTable(ArrayList<String[]> tabledata,String strDataSet) throws SkipLoadException
{
	String query = "select post_process_func_name from jobs where Data_Set='" + strDataSet + "'";

	try
	{
		ResultSet rs = dbf.db_run_query(query);
		rs.next();
		String strFunctionName = rs.getString("post_process_func_name");
		
		UtilityFunctions.stdoutwriter.writeln("Table Post Process Func Name: " + strFunctionName,Logs.STATUS2,"PF15");
		if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
		{
			UtilityFunctions.stdoutwriter.writeln("No table post process function, exiting...",Logs.STATUS2,"PF16");
			return(tabledata);
		}
		propTableData = tabledata;
		propStrTableDataSet = strDataSet;
		Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
		m.invoke(this, new Object[] {});
	
	}
	catch (IllegalAccessException iae)
	{
		UtilityFunctions.stdoutwriter.writeln("postProcessing method call failed",Logs.ERROR,"PF16.1");
		UtilityFunctions.stdoutwriter.writeln(iae);
	
	}
	//any exceptins thrown by the function pointer are wrapped in an InvocationTargetException
	catch (InvocationTargetException ite)
	{
		if (ite.getTargetException().getClass().getSimpleName().equals("SkipLoadException"))
			throw new SkipLoadException();
		else
		{
			UtilityFunctions.stdoutwriter.writeln("postProcessing method call failed",Logs.ERROR,"PF16.3");
			UtilityFunctions.stdoutwriter.writeln(ite);
		}
	
	}
	catch (NoSuchMethodException nsme)
	{
		UtilityFunctions.stdoutwriter.writeln("postProcessing method call failed",Logs.ERROR,"PF16.5");
		UtilityFunctions.stdoutwriter.writeln(nsme);
	}
	catch (DataAccessException tmpE)
	{
		UtilityFunctions.stdoutwriter.writeln("postProcessing method call failed",Logs.ERROR,"PF17");
		UtilityFunctions.stdoutwriter.writeln(tmpE);
	}
	
	return(propTableData);
	
	
}*/





/*
 * This function is still hear for historical data loads.
 */



public void preJobProcessTableXrateorg() throws DataAccessException
{
	//clean out all fact_data items that aren't linked to in the notify table
	/*
	 * I want to change this so that one item per hour is retained for the long term.
	 */
	
	
	//this query will retrieve the maximum batch # for each ticker from each hour, this is the
	//record we will retain.
/*	String query = "select ticker,HOUR(date_collected), date_collected , primary_key, max(batch) from " +
			"fact_data where datediff(date_collected,NOW())=-3 and " +
			"data_set like '%xrateorg%' group by ticker, HOUR(date_collected)";*/
	
	
//	String query2 = "delete from fact_data where datediff(date_collected,NOW())=-3 and not in (" + query + ")";
	
	
	
	
	/*String query2 = "select distinct ticker from fact_data where data_set='" + this.propStrTableDataSet + "'";
	ResultSet rs2 = dbf.db_run_query(query2);
	while (rs2.next())
	{
	
	
	
		String query = "select * from fact_data where datediff(date_collected,NOW())=-3 and data_set='" + this.propStrTableDataSet + "'";
		query = query + "order by date_collected desc";
		ResultSet rs1 = dbf.db_run_query(query);
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		 * loop through all records and only retain the 1st record from each hour
		 
		while(rs1.next())
		{
			cal.setTime(rs1.getDate("date_collected"));
			cal.add(Calendar.HOUR,-1);
	
			while(rs1.next())
			{
				cal2.setTime(rs1.getDate("date_collected"));
				if (cal2.after(cal))
				{
					query = "delete from fact_data where primary_key=" + rs1.getInt("primary_key");
					dbf.db_update_query(query);
				}
				
			}
			
			
		}
		
	}*/
	
	
	
	
	
	
	/*String query = "select * from fact_data where primary_key NOT IN ";
	query = query + "(select fact_data.primary_key from fact_data,notify where fact_data.primary_key=notify.fact_data_key)";
	ResultSet rs = dbf.db_run_query(query);
	while(rs.next())
	{
		query = "delete from fact_data where primary_key=" + rs.getInt("primary_key");
		dbf.db_update_query(query);
	}*/
}





/*public void postProcessIMFGdpPPPActual() throws DataAccessException {
	
	String[] tmpArray = {"value","date_collected","entity_id","calyear","scale"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	String[] rowdata, newrow;
	//String[] colheaders = propTableData.get(0);
	
	String[] colheaders = propTableData.remove(0);
	String[] rowheaders = propTableData.remove(0);
	
	for (int row=0;row<propTableData.size();row++) {
		rowdata = propTableData.get(row);
		
		for (int col=0;col<colheaders.length;col++)
		{
		
		String strCountry = rowheaders[row];
		newrow = new String[tmpArray.length];
		
		if (rowdata[0].equals("n/a"))
		{
			UtilityFunctions.stdoutwriter.writeln("Retrieved n/a value, skipping processing for entity " + strCountry,Logs.WARN,"PF44");
			continue;
		}
		
		newrow[0] = rowdata[col].replace(",", "");
		BigDecimal bdTmp = new BigDecimal(newrow[0]);
		
		newrow[0] = bdTmp.toString();

		newrow[1] = "NOW()";
		}
		
	}
	
}

public void postProcessIMFGdpPPPEst() throws DataAccessException {
	
}*/



public void postProcessExchRate()
{
	String[] newrow;
	/*String[] colheaders = propTableData.get(0);
	String[] rowheaders = propTableData.get(1);*/
	
	String[] tmpArray = {"data_set","value","date_collected"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	newTableData.add(tmpArray);
	
	newrow = new String[tmpArray.length];
	
	newrow[0] = dg.strCurDataSet;
	newrow[1] = this.propTableData.get(0)[0];
	newrow[2] = "NOW()";
	//newrow[3] = "forex";
	
	newTableData.add(newrow);
	
	propTableData = newTableData;

	

}
	
	/*SEC begin fiscal year notes*/
	/*
	 * PG ends in June not April.
	 */
	
}







