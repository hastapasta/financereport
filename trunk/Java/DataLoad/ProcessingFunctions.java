//package com.roeschter.jsl;
 
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * GENERAL COMMENTS ON PROCESSING FUNCTIONS
 * OFP 12/13/2010: Since the data_group field was removed from the fact_table (because it can be referenced in
 * the jobs table through the data_set), it was removed from all processing functions that were inserting it
 * into the fact_table.
 * 
 */


class ProcessingFunctions
{
	/*Individual extraction processing function parameters*/
	
	String strDataValue;

	/*RIght now this is only set during the preProcessing function */
	String strTicker;
	String strTemp1, strTemp2, strTemp3;
	
	/*table extraction processing function parameters*/
	ArrayList<String[]> propTableData;
	String propStrTableDataSet;
	
	
	/* Need to figure out why two log files are being written to... UtilityFunctions.stdoutwriter and stdoutwriter */
	UtilityFunctions uf;
	DataGrab dg;
	DBFunctions dbf;
	
	//CustomBufferedWriter stdoutwriter;
	
	public ProcessingFunctions(UtilityFunctions tmpUF, DataGrab tmpDG)
	{
		this.uf = tmpUF;
		this.dg = tmpDG;
		this.dbf = dg.dbf;
	}
	
	public boolean preNoDataCheck(String strDataSet)
	{
		try
		{
			String query;
			/*if ((strDataSet.substring(0,5)).compareTo("table") == 0)
				query = "select pre_nodata_check_func from extract_tables where Data_Set='" + strDataSet + "'";
			else
				query = "select pre_nodata_check_func from extract_singles where Data_Set='" + strDataSet + "'";*/
			
			query = "select pre_nodata_check_func from jobs where data_set='" + strDataSet + "'";
			
			ResultSet rs = dbf.db_run_query(query);
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
	
	public boolean preJobProcessing(String strDataSet)
	{
		String query = "select pre_job_process_func_name from jobs where Data_Set='" + strDataSet + "'";
		
		
		
		
		try
		{
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			String strFunctionName = rs.getString("pre_job_process_func_name");
			
			UtilityFunctions.stdoutwriter.writeln("Pre Job Process Func Name: " + strFunctionName,Logs.STATUS2,"PF3.2");
			//this.strTicker = strTicker;
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
			{
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
	
	public boolean postJobProcessing(String strDataSet)
	{
		String query = "select post_job_process_func_name from jobs where Data_Set='" + strDataSet + "'";
		
		
		
		
		try
		{
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			String strFunctionName = rs.getString("post_job_process_func_name");
			
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
	

public boolean preProcessing(String strDataSet, String strTicker)
{
	  /* For the return value, return false if the attempt to grab the value should be skipped. */
	  
	

		String query = "select pre_process_func_name from jobs where Data_Set='" + strDataSet + "'";
		
		
		
	
		try
		{
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			String strFunctionName = rs.getString("pre_process_func_name");
			
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


public ArrayList<String []> postProcessing(ArrayList<String []> tabledata , String strDataSet) throws SkipLoadException,CustomEmptyStringException
{
	
		String query = "select post_process_func_name from jobs where Data_Set='" + strDataSet + "'";
		
		/*
		 * This is for non-table extraction. However this will throw a nullpointerexception if no column header
		 * data is extracted during a table extraction.
		 */
		if (tabledata.get(0) != null)
			strDataValue = tabledata.get(0)[0];

	
		try
		{
			
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			String strFunctionName = rs.getString("post_process_func_name");
			
			UtilityFunctions.stdoutwriter.writeln("Post Process Func Name: " + strFunctionName,Logs.STATUS2,"PF8");
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
			{
				UtilityFunctions.stdoutwriter.writeln("No post process function, exiting...",Logs.STATUS2,"PF9");
				return(tabledata);
			}
			
			propTableData = tabledata;
			propStrTableDataSet = strDataSet;
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
				UtilityFunctions.stdoutwriter.writeln(ite);
			}
		
		}
		catch (NoSuchMethodException nsme)
		{
			UtilityFunctions.stdoutwriter.writeln("postProcessing method call failed",Logs.ERROR,"PF16.5");
			UtilityFunctions.stdoutwriter.writeln(nsme);
		}
		catch (SQLException tmpE)
		{
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
	catch (SQLException tmpE)
	{
		UtilityFunctions.stdoutwriter.writeln("postProcessing method call failed",Logs.ERROR,"PF17");
		UtilityFunctions.stdoutwriter.writeln(tmpE);
	}
	
	return(propTableData);
	
	
}*/









public void postProcessYahooSharePrice() throws SQLException
{
	//System.out.println("here");
	String[] strTmpValue = propTableData.get(0);
	
	String[] values = strTmpValue[0].split(",");
	
	
	
	//strTmpValue[0] = strTmpValue[0].substring(strTmpValue[0].indexOf(">")+1,strTmpValue[0].length());
	
	
	String[] tmpArray = {"value","date_collected","entity_id"};
	String[] rowdata = new String[tmpArray.length];
	
	//rowdata[0] = dg.nCurTask + "";
	//rowdata[0] = strTmpValue[0];

	if (DataLoad.bLoadingHistoricalData==false)
	{
		rowdata[0] = values[0];
		rowdata[1] = "NOW()";
	}
	else
	{
		// Use market close in arizona time zone
		rowdata[0] = values[4];
		rowdata[1] = "'" + values[0] + " 13:00:00'";
	}
	rowdata[2] = dg.nCurrentEntityId + "";
	//rowdata[4] = "share_price";
	
	
	propTableData.remove(0);
	propTableData.add(tmpArray);
	propTableData.add(rowdata);
	
	
	
	
	
	

}

public void postProcessCNBCCDSJson() throws SQLException {
	
	String strData = this.propTableData.get(0)[0];
	System.out.println("");
	
}

public void postProcessYahooSharePriceYQL() throws SQLException
{
	//System.out.println("here");
	String strTmpValue = propTableData.get(0)[0];
	
	//String[] values = strTmpValue[0].split(",");
	
	
	boolean bDone = false;
	
	
	
	propTableData.remove(0);
	String[] tmpArray = {"value","date_collected","entity_id"};
	propTableData.add(tmpArray);
	
	int nBegin=0;
	int nEnd =0;
	
	int nCount = 0;
	int nCount2 = 0;
	
	while (!bDone) {
		
		nCount2++;
		
		String[] rowdata = new String[tmpArray.length];
		
		
		
		
		
		nBegin = strTmpValue.indexOf("<LastTradeDate>",nEnd);
		if (nBegin == -1)
			break;
		nBegin += "<LastTradeDate>".length();
		nEnd = strTmpValue.indexOf("</LastTradeDate>",nBegin);
		
		String strDate = strTmpValue.substring(nBegin,nEnd);
		
		nBegin = strTmpValue.indexOf("<LastTradePriceOnly>",nEnd) + "<LastTradePriceOnly>".length();
		nEnd = strTmpValue.indexOf("</LastTradePriceOnly>",nBegin);
		
		String strValue = strTmpValue.substring(nBegin,nEnd);
		
		nBegin = strTmpValue.indexOf("<Symbol>",nEnd) + "<Symbol>".length();
		nEnd = strTmpValue.indexOf("</Symbol>",nBegin);
		
		String strSymbol = strTmpValue.substring(nBegin,nEnd);
		
		nBegin = strTmpValue.indexOf("<LastTradeTime>",nEnd) + "<LastTradeTime>".length();
		nEnd = strTmpValue.indexOf("</LastTradeTime>",nBegin);
		
		String strTime = strTmpValue.substring(nBegin,nEnd);
		
		
		rowdata[0] = strValue;
		String[] strDateArray = strDate.split("/");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Integer.parseInt(strDateArray[0])-1);
		cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(strDateArray[1]));
		cal.set(Calendar.YEAR,Integer.parseInt(strDateArray[2]));
		
		
		String strAMPM = strTime.substring(strTime.indexOf("m")-1,strTime.indexOf("m")+1);
		String strNewTime = strTime.substring(0,strTime.indexOf("m")-1);
		String[] strTimeArray = strNewTime.split(":");
		
		/*if (strAMPM.equals("pm"))
			cal.set(Calendar.AM_PM,Calendar.PM);
		else
			cal.set(Calendar.AM_PM,Calendar.AM);
		cal.set(Calendar.HOUR,Integer.parseInt(strTimeArray[0]));*/
		
		/*
		 * There's an issue with setting the AMPM value and then the HOUR value. The conversion
		 * happens incorrectly if the 2 values are set separately.
		 */
		
		
		int nHour = Integer.parseInt(strTimeArray[0]);
		if (strAMPM.equals("pm") && nHour != 12)
			cal.set(Calendar.HOUR_OF_DAY, nHour + 12);
		else if (strAMPM.equals("am") && nHour == 12)
			cal.set(Calendar.HOUR_OF_DAY, 0);
		else
			cal.set(Calendar.HOUR_OF_DAY,nHour);
		
		cal.set(Calendar.MINUTE, Integer.parseInt(strTimeArray[1]));
		cal.set(Calendar.SECOND, 0);
		
		
		cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		formatter.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
		
		Calendar currentCal = Calendar.getInstance();
		
		DateFormat formatter2 = new SimpleDateFormat("M/d/yyyy");
		
		/*
		 * Only perform this check for task 10.
		 */
		if (dg.nCurTask == 10)
			if (!formatter2.format(currentCal.getTime()).equals(formatter2.format(cal.getTime())))
				UtilityFunctions.stdoutwriter.writeln("Bad Yahoo Data, in postProcessing function for Symbol " + strSymbol,Logs.ERROR,"PF50.5");
		
		
		/*
		 * If the taskid is 10, we are going to use the input time for date_collected.
		 * Otherwise we are going to use real time collection time - delay (for yahoo, 20 minutes) for date_collected.
		 */
		if (dg.nCurTask == 10)
			rowdata[1] = "'" + formatter.format(cal.getTime()) + "'";
		else {
			currentCal.add(Calendar.MINUTE, -20);
			rowdata[1] = "'" + formatter.format(currentCal.getTime()) + "'";
		}
			
		
		
		
		
			
			
		/*
		 * These are the 5 non US tickers for task 24: ^N225, ^AORD, ^TWII, ^NZ50, ^AXJO
		 */
		
		
		
		if (strSymbol.equals("BRK-A"))
			strSymbol = "BRK/A";
		else if (strSymbol.equals("BF-B"))
			strSymbol = "BF/B";
		
		try {
			String query = "select id from entities where ticker='" + strSymbol + "'";
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			rowdata[2] = rs.getInt("id") + "";
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Issue looking up ticker " + strSymbol + ",skipping",Logs.ERROR,"PF50");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			continue;
			
		}
		
		

		//rowdata[4] = "share_price";
		
		nCount++;
	
		propTableData.add(rowdata);
	}
	
	//System.out.println("here");
	
	
	
	
	
	

}

public void postProcessGoogleSharePrice() throws SQLException
{
	//System.out.println("here");
	String strTmpValue = propTableData.get(0)[0];
	
	String lines[] = strTmpValue.split("\\r?\\n");
	
	
	
	
	propTableData.remove(0);
	String[] tmpArray = {"value","date_collected","entity_id"};
	propTableData.add(tmpArray);

	
	int nCount = 0;
	int nCount2 = 0;
	
	//skip the first and last lines 
	for (int i=1;i<lines.length-1;i++) {
		
		nCount2++;
		
		String[] rowdata = new String[tmpArray.length];
		String[] inputrow = lines[i].split(",");
		if (inputrow[2].contains("#")) {
			UtilityFunctions.stdoutwriter.writeln("Invalid data returned for ticker " + inputrow[0],Logs.WARN,"PF62.3");			
			continue;
		}
		
		String strSymbol = inputrow[0];
		
		if (strSymbol.equals("BRK.A"))
			strSymbol = "BRK/A";
		else if (strSymbol.equals("BF.B"))
			strSymbol = "BF/B";
		
		try {
			String query = "select id from entities where ticker='" + strSymbol + "'";
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			rowdata[2] = rs.getInt("id") + "";
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Issue looking up ticker " + strSymbol + ",skipping",Logs.ERROR,"PF50");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			continue;
			
		}
		
		rowdata[0] = inputrow[1];
		
		inputrow[2].trim();
		
		String strDate = inputrow[2].substring(0,inputrow[2].indexOf(" "));
		String strTime = inputrow[2].substring(inputrow[2].indexOf(" ") + 1,inputrow[2].length());
		
		String[] strDateArray = strDate.split("/");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Integer.parseInt(strDateArray[0])-1);
		cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(strDateArray[1]));
		cal.set(Calendar.YEAR,Integer.parseInt(strDateArray[2]));
		
		
		//String strAMPM = strTime.substring(strTime.indexOf("m")-1,strTime.indexOf("m")+1);
		//String strNewTime = strTime.substring(0,strTime.indexOf("m")-1);
		String[] strTimeArray = strTime.split(":");
		

		cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(strTimeArray[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(strTimeArray[1]));
		cal.set(Calendar.SECOND, Integer.parseInt(strTimeArray[2]));
		
		cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		formatter.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
	
		rowdata[1] = "'" + formatter.format(cal.getTime()) + "'";
		
		//Calendar currentCal = Calendar.getInstance();
		
		//DateFormat formatter2 = new SimpleDateFormat("M/d/yyyy");
		
		//if (!formatter2.format(currentCal.getTime()).equals(formatter2.format(cal.getTime())))
		//	UtilityFunctions.stdoutwriter.writeln("Bad Yahoo Data, in postProcessing function",Logs.ERROR,"PF50.5");
		
		
		
	
		
		

		//rowdata[4] = "share_price";
		
		nCount++;
	
		propTableData.add(rowdata);
	}
	
	//System.out.println("here");
	
	
	
	
	
	

}



public void postProcessNasdaqSharesOut() throws SQLException
{
	
	//query = "LOCK TABLES repeat_types WRITE, schedules WRITE";
	//dbf.db_update_query(query);
	strDataValue.replace(",", "");
	String query = "update entities set shares_outstanding=" + this.propTableData.get(0)[0] + " where ticker='" + this.strTicker + "'";
	dbf.db_update_query(query);
	
	//finally
	//query = "UNLOCK TABLES";
	
}


public void postNasdaqFiscalYear()
{
	/*
	 * This is the main data set for populating the entities->begin_fiscal_calendar field. 
	 * Some tickers have to be corrected with the sec_fiscal_calendar_field. These are designated with
	 * the nasdaq_begin_fiscal_year_correct group in the entities table.
	 */
	UtilityFunctions.stdoutwriter.writeln("Inside postNasdaqFiscalYear function",Logs.STATUS2,"PF34");
	
	if (strDataValue.compareTo("January") == 0)
		strDataValue = "November";
	else if (strDataValue.compareTo("February") == 0)
		strDataValue = "December";
	else if (strDataValue.compareTo("March") == 0)
		strDataValue = "January";
	else if (strDataValue.compareTo("April") == 0)
		strDataValue = "February";
	else if (strDataValue.compareTo("May") == 0)
		strDataValue = "March";
	else if (strDataValue.compareTo("June") == 0)
		strDataValue = "April";
	else if (strDataValue.compareTo("July") == 0)
		strDataValue = "May";
	else if (strDataValue.compareTo("August") == 0)
		strDataValue = "June";
	else if (strDataValue.compareTo("September") == 0)
		strDataValue = "July";
	else if (strDataValue.compareTo("October") == 0)
		strDataValue = "August";
	else if (strDataValue.compareTo("November") == 0)
		strDataValue = "September";
	else if (strDataValue.compareTo("December") == 0)
		strDataValue = "October";
	
	/* Use the following update statement for populating fiscal_calendar_begin */
	try
	{
		//String query = "UPDATE entities set begin_fiscal_calendar='" + strDataValue + "' where ticker=(select url_dynamic from extract_singles where data_set='nasdaq_fiscal_year_begin')";
		//dbf.db_update_query(query);
		
		String[] tmp = {"ticker","begin_fiscal_calendar"};
		
		propTableData.add(0,tmp);
		
		//String fiscalyearquarter = uf.getFiscalYearAndQuarter(strTicker);
		
		//int nAdjQtr = uf.retrieveAdjustedQuarter(Integer.parseInt(fiscalyearquarter.substring(0,1)),Integer.parseInt(fiscalyearquarter.substring(3,5)),strTicker);
		
		String[] values = {strTicker,strDataValue};
		
		propTableData.remove(1);
		propTableData.add(values);
	}
	catch(Exception e)
	{
		UtilityFunctions.stdoutwriter.writeln("Problem updating begin_fiscal_calendar field.",Logs.ERROR,"PF35");
		UtilityFunctions.stdoutwriter.writeln(e);
	}

	
	
	
}

public void processTableSAndPCoList(ArrayList<String[]> tabledata,String strDataSet,UtilityFunctions tmpUF)
{
	String[] rowdata;
	String query;
	String groups;
	UtilityFunctions.stdoutwriter.writeln("In function processTableSAndPCoList",Logs.STATUS2,"PF36");
	try
	{
		ResultSet rs;
		for (int x=0;x<tabledata.size();x++)
		{
			rowdata = tabledata.get(x);
			
			/* Some custom processing logic */
			rowdata[0] = rowdata[0].replace("VIA.B", "VIA");
			rowdata[0] = rowdata[0].replace("BRK.B", "BRK.A");
			/*BRK.A and BF.B data can't be found on nasdaq but can be found on msnmoney*/
			/*rowdata[0] = rowdata[0].replace("BF.B", "BF/A");*/

			/*End custom processing logic */
			
			/*Determine if ticker is already in entities table, if not, add it*/
			query = "select * from entities where ticker='" + rowdata[0] + "'";
			rs = dbf.db_run_query(query);
			if (rs.next() == false)
			{
				query = "insert into entities (ticker, groups) values ('" + rowdata[0] + "','sandp')";
				dbf.db_update_query(query);
			}
			else
			{
				groups = rs.getString("groups");
				if (groups.indexOf("sandp") == -1)
				{
					groups = groups + ",sandp";
					query = "update entities set groups='" + groups + "' where ticker='" + rowdata[0] + "'";
					dbf.db_update_query(query);
				}
				
				
				
			}
			
		}
	}
	catch (SQLException sqle)
	{
		UtilityFunctions.stdoutwriter.writeln("Problem processing S and P company list table data",Logs.ERROR,"PF37");
		UtilityFunctions.stdoutwriter.writeln(sqle);		
	}
	
	
	
	
}


public void postProcessTableNasdaqUpdateEPS() throws SQLException,SkipLoadException
{
	
	/*
	 * The purpose of this function is to only load the most recent quarter.
	 * 
	 * IF YOU WANT TO DO A FULL RELOAD OF DATA, set the postProcessFunction to postProcessNasdaqEPSTable.
	 */
	
	//String[] rowdata, newrow;
	String[] colheaders = propTableData.get(0);
	String[] rowheaders = propTableData.get(1);
	
	/*This is custom code to fix rowheader #4 since the html is not consistent.*/
	rowheaders[3] = rowheaders[3].replace("(FYE)","");
	int nRecentFisQtr=4;
	
	//find the most recent quarter
	for (int row=2;row<propTableData.size();row++)
	{
		if (propTableData.get(row)[0].equals("void") || propTableData.get(row)[0].isEmpty())
		//hopefully the first row isn't void, if it is, then we will assume the fiscal 4th quarter is the most recent.
			if (row==2)
			{
				//leaving FisQtr the same
				nRecentFisQtr=4;
				//need some special code since this situation screws up our colheaders processing logic below
				String[] tmp = new String[1];
				tmp[0] = colheaders[1];
				colheaders = tmp;
				break;
			}
			else
			{
				String[] tmp = new String[1];
				tmp[0] = colheaders[0];
				colheaders = tmp;
				nRecentFisQtr=row-2;
				break;
			}
		//didn't run into any emptys or voids but still have to reset colheaders
		else if (row==5)
		{
			String[] tmp = new String[1];
			tmp[0] = colheaders[0];
			colheaders = tmp;
			
		}
			
	}
	
	//check to see if the data already exists
	String query="select * from fact_data where ticker='" + dg.strOriginalTicker + "' and data_set='" + dg.strCurDataSet +"'";
	query = query + " and fiscalquarter=" + nRecentFisQtr + " and fiscalyear=" + colheaders[0];
	ResultSet rs = dbf.db_run_query(query);
	
	if (rs.next())
	{
		UtilityFunctions.stdoutwriter.writeln("MRQ already loaded, skipping",Logs.STATUS1,"PF38.5");
		propTableData = null;
		throw new SkipLoadException();
	}
		
	
	
	
	//void out all the other quarters so only the most recent gets loaded
	for (int i=2;i<propTableData.size();i++)
		if (i-1!=nRecentFisQtr)
			propTableData.get(i)[0] = "void";
	
	propTableData.set(0,colheaders);
	
	this.postProcessNasdaqEPSTable();
	
	
	
	
}

public void postProcessNasdaqEPSTable() throws SQLException
{
	/*
	 * Special Situations That need to be handled:
	 * -data values contain date in parentheses 
	 * -data values contain &nbsp
	 * -negative values use hyphen
	 */
	
	/*JNJ end of quarter months are incorrect, still need to code for this */
	
	/*
	 * Ticker urls currently without data: BF/B, BRK/A, AON
	 */
	//try
	//{
		//String query = "select url_dynamic from extract_tables where Data_Set='" + propStrTableDataSet + "'";
		//ResultSet rs = dbf.db_run_query(query);
		//rs.next();
		String strTicker = dg.strOriginalTicker;

		String[] rowdata, newrow;
		String[] colheaders = propTableData.get(0);
		String[] rowheaders = propTableData.get(1);
		
		/*This is custom code to fix rowheader #4 since the html is not consistent.*/
		rowheaders[3] = rowheaders[3].replace("(FYE)","");
		
		ArrayList<String[]> newTableData = new ArrayList<String[]>();
		String[] tmpArray = {"value","date_collected","entity_id","fiscalquarter","fiscalyear","calquarter","calyear"};
		newTableData.add(tmpArray);
	
		for (int row=2;row<propTableData.size();row++)
		{
			rowdata = propTableData.get(row);
			
			for (int col=0;col<colheaders.length;col++)
			{
				newrow = new String[16];
				if (rowdata[col].compareTo("void") != 0)
				{
					//newrow[0] = "VARCHAR";
					//newrow[0] = propStrTableDataSet;
					//newrow[2] = "INTEGER";
					
					if ((rowdata[col].contains("N/A") == true) || (rowdata[col].isEmpty() == true))
					{
						UtilityFunctions.stdoutwriter.writeln("N/A value or empty value, skipping...",Logs.STATUS2,"PF39");
						continue;
					}
					else if (rowdata[col].contains("(") == true)
						newrow[0]=rowdata[col].substring(0,rowdata[col].indexOf("("));
					else
						UtilityFunctions.stdoutwriter.writeln("Problem with data value formatting",Logs.ERROR,"PF40");
					
				
					newrow[1] = "NOW()";
					
					//String query = "select * from entities where ticker='"+strTicker+"'";
					
					newrow[2] = dg.nCurrentEntityId + "";
					
					//newrow[4] = "eps_exc_xtra";
				
					newrow[3] = Integer.toString(row-1);
				
					newrow[4] = colheaders[col];
					String strCalYearQuarter = MoneyTime.getCalendarYearAndQuarter(strTicker, Integer.parseInt(newrow[3]), Integer.parseInt(newrow[4]),dbf);
					
					newrow[5] = strCalYearQuarter.substring(0,1);
					newrow[6] = strCalYearQuarter.substring(1,5);
						
					newTableData.add(newrow);
					
				}
				
				
			}
			
		}
		propTableData = newTableData;

	
}

public void postProcessBloombergCommodities()
{
	String[] tmpArray = {"value","date_collected","entity_id"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	String[] rowdata, newrow;
	//String[] colheaders = propTableData.get(0);
	String[] rowheaders = propTableData.get(1);
	
	//newTableData.add(tmpArray);
	
	for (int row=2;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		if (rowdata[0].contains("N.A."))
			continue;
		
		newrow = new String[tmpArray.length];
		
		//newrow[0] = propStrTableDataSet;
		//newrow[2] = "FUNCTION";
		newrow[0] = rowdata[0].replace(",", "");
		//newrow[4] = "VARCHAR";
		newrow[1] = "NOW()";
		//newrow[6] = "INTEGER";
		
		/*
		 * If there are 2 sets of parens for the row header, we want to cutoff at the 2nd left paren.
		 */
		
		String tmp = rowheaders[row-2];
		int i = tmp.indexOf("(");
	
		if (tmp.indexOf("(",i+1)==-1)
			tmp = tmp.substring(0,i);
		else
			tmp = tmp.substring(0,tmp.indexOf("(",i+1));
		
			
		
		//newrow[3] = rowheaders[row-2].substring(0,rowheaders[row-2].indexOf("("));
		tmp = tmp.trim();
		/*newrow[3] = tmp.substring(tmp.indexOf(">")+1,tmp.indexOf("</"));
		newrow[3] = newrow[3].replace("&amp;", "&");*/
		
		//remove single quotes e.g. (coffee 'c')
		tmp = tmp.replace("'", "");
		
		String query = "select * from entities where ticker='"+tmp+"'";
		
		try
		{
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			newrow[2] = rs.getInt("id") + "";
			newTableData.add(newrow);
			
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem looking up ticker: " + tmp + ",row skipped",Logs.WARN,"PF99.55");
			
			/*
			 * This is not a fatal error so we won't display the full exception.
			 */
			//UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		
		
		
		//newTableData.add(newrow);
		
		
			
			
		
		
	}
	
	newTableData.add(0, tmpArray);
	propTableData = newTableData;
	
	

}

public void postProcessBloombergFutures()
{
	String[] lookup = 
	{"DJIA INDEX","913",
	"S&P 500","919",
	"NASDAQ 100","929",
	"S&P/TSX 60","961",
	"MEX BOLSA"," 963",
	"BOVESPA","972",
	"DJ EURO STOXX 50","680",
	"FTSE 100","699",
	"CAC 40 10 EURO","715",
	"DAX","706",
	"IBEX 35","722",
	"FTSE MIB","731",
	"AMSTERDAM","740",
	"OMXS30"," 758",
	"SWISS MARKET","725",
	"NIKKEI 225","848",
	"HANG SENG"," 855",
	"SPI 200"," 888"
	};
	
	
	String[] tmpArray = {"value","date_collected","entity_id"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	String[] rowdata, newrow;
	
	String[] rowheaders = propTableData.get(1);
	

	
	for (int row=2;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		if (rowdata[0].contains("N.A."))
			continue;
		
		newrow = new String[tmpArray.length];
		
	
		newrow[0] = rowdata[0].replace(",", "");
	
		newrow[1] = "NOW()";
	
		
		String tmp = rowheaders[row-2];
		
		tmp = tmp.trim();
	
		
		String strEntityIndex = "";
		
		for (int i=0;i<lookup.length;i+=2)
		{
			if (lookup[i].compareTo(tmp) == 0)
			{
				strEntityIndex = lookup[i+1];
				break;
			}
		}
		
		if (strEntityIndex.isEmpty())
		{
			UtilityFunctions.stdoutwriter.writeln("Problem with lookup for ticker" + tmp + ",Skipping...",Logs.ERROR,"PF90.2");
			continue;
		}
		
		
		newrow[2] = strEntityIndex;
		newTableData.add(newrow);
		
	
		
		
			
			
		
		
	}
	
	newTableData.add(0, tmpArray);
	propTableData = newTableData;
	
	

}


public void postProcessOneTimeYahooIndex()
{
	String[] rowdata;
	for (int row=1;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		String strInsert = "insert into entities_yahoo (ticker,full_name) values ('" + rowdata[0] + "','" + rowdata[1] + "')";
		try
		{
			dbf.db_update_query(strInsert);
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem inserting row" + strInsert,Logs.ERROR,"PF88.1");
		}
	}
	
	
}

public void postProcessBloombergGovtBonds() {
	
	String[] tmpArray = {"value","date_collected","entity_id"};
	String[] rowheaders = propTableData.get(1);
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	String[] rowdata, newrow;
	
	for (int row=2;row<propTableData.size();row++) {
		rowdata = propTableData.get(row);
		
		newrow = new String[tmpArray.length];
		
		
		String strQuery = null;
		String strTicker = null;
		
		newrow[1] = "NOW()";
		if (dg.strCurDataSet.contains("_japan")) {
			
			strTicker = rowheaders[row-2].replace("-","_").toLowerCase() + "_japan";
			strQuery = "select id from entities where ticker='" + strTicker + "'";
			newrow[0] = rowdata[0].substring(0,rowdata[0].indexOf("/")).trim();
		}
		else if (dg.strCurDataSet.contains("_australia")) {
			strTicker = rowheaders[row-2].replace("-","_").toLowerCase() + "_australia";
			strQuery = "select id from entities where ticker='" + strTicker + "'";
			newrow[0] = rowdata[0].substring(0,rowdata[0].indexOf("/")).trim();
		}
		else if (dg.strCurDataSet.contains("_us")) {
			
			/* 
			 * Prices aren't displayed and haven't figured out the proper way to cacluate them yet.
			 */
			if (rowheaders[row-2].contains("Month"))
				continue;
			
			if (rowdata[0].contains("&"))
				newrow[0] = rowdata[0].substring(0,rowdata[0].indexOf("&")).trim();
			else if (rowdata[0].contains("+"))
				newrow[0] = rowdata[0].substring(0,rowdata[0].indexOf("+")).trim();
			else if (rowdata[0].contains("/"))
				newrow[0] = rowdata[0].substring(0,rowdata[0].indexOf("/")).trim();
			else {
				UtilityFunctions.stdoutwriter.writeln("Unable to parse numeric value " + rowdata[0],Logs.ERROR,"PF46.51");
				continue;
			}
				
			String[] tmp = newrow[0].split("-");
					
			
			Double dTmp = (Double.parseDouble(tmp[0]) + (Double.parseDouble(tmp[1]) / 32));
			dTmp = Math.round(1000*dTmp) / 1000d;
			newrow[0] = dTmp + "";
			//newrow[0] = newrow[0].replace("-", ".");
			strTicker = rowheaders[row-2].replace("-","_").toLowerCase() + "_us";
			strQuery = "select id from entities where ticker='" + strTicker + "'";
		}
		else if (dg.strCurDataSet.contains("_uk")) {
			strTicker = rowheaders[row-2].replace("-","_").toLowerCase() + "_uk";
			strQuery = "select id from entities where ticker='" + strTicker + "'";
			newrow[0] = rowdata[0].substring(0,rowdata[0].indexOf("/")).trim();
		}
		else if (dg.strCurDataSet.contains("_germany")) {
			strTicker = rowheaders[row-2].replace("-","_").toLowerCase() + "_germany";
			strQuery = "select id from entities where ticker='" + strTicker + "'";
			newrow[0] = rowdata[0].substring(0,rowdata[0].indexOf("/")).trim();
			
		}
		else if (dg.strCurDataSet.contains("_brazil")) {
			strTicker = rowheaders[row-2].replace("-","_").toLowerCase() + "_brazil";
			strQuery = "select id from entities where ticker='" + strTicker + "'";
			newrow[0] = rowdata[0].substring(0,rowdata[0].indexOf("/")).trim();
		}
		else if (dg.strCurDataSet.contains("_hongkong")) {
			if (rowheaders[row-2].contains("Bill") || rowheaders[row-2].contains("HKSAR"))
				continue;
			
			newrow[0] = rowdata[0].trim();
			strTicker = rowheaders[row-2].substring(0,3);
			if (strTicker.contains("W"))
				strTicker = strTicker.replace("W","_week_");
			else if (strTicker.contains("M"))
				strTicker = strTicker.replace("M","_month_");
			else if (strTicker.contains("Y"))
				strTicker = strTicker.replace("Y","_year_");
			strTicker += "hongkong";
			strTicker = strTicker.replace(" ","");
			//strTicker = rowheaders[row-2].replace("-","_").toLowerCase() + "_hongkong";
			strQuery = "select id from entities where ticker='" + strTicker + "'";
		}
		else {
			UtilityFunctions.stdoutwriter.writeln("Unable to find dataset " + dg.strCurDataSet,Logs.ERROR,"PF44.51");
			continue;
		}
		
		newrow[0] = newrow[0].replace(",", "");
		/* This dataset can involve extended ascii characters. */
		newrow[0] = newrow[0].replaceAll("[^\\p{ASCII}]", "");
		
		if (newrow[0].isEmpty()) {
			UtilityFunctions.stdoutwriter.writeln("Ticker returned an empty value: " + strTicker + ", skipping",Logs.WARN,"PF44.59");
			continue;
		}
	

		
		try {
			ResultSet rs = dbf.db_run_query(strQuery);
			rs.next();
			newrow[2] = rs.getInt("id") + "";
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Problem looking up ticker: " + strTicker  + ",row skipped",Logs.WARN,"PF43.51");
			continue;
		}
		
		newTableData.add(newrow);
	}

	newTableData.add(0, tmpArray);
	propTableData = newTableData;
	
}

public void postProcessBloombergQuote() throws CustomEmptyStringException {

	String[] tmpArray = {"value","date_collected","entity_id"};
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	String[] rowdata, newrow;
	
	rowdata = this.propTableData.get(0);
	
	
	newrow = new String[tmpArray.length];
	newrow[0] = rowdata[0].replace("\n","").trim();
	if (newrow[0].isEmpty()) {
		UtilityFunctions.stdoutwriter.writeln("Empty Value Retrieved for ticker " + dg.strCurrentTicker + ",row skipped",Logs.WARN,"PF48.33");
		throw (new CustomEmptyStringException());
	}
	else if (newrow[0].equals("N.A.")) {
		UtilityFunctions.stdoutwriter.writeln("N.A. Value Retrieved for ticker " + dg.strCurrentTicker + ",row skipped",Logs.WARN,"PF48.40");
		throw (new CustomEmptyStringException());
	}
	
	newrow[1] = "NOW()";
	
	String query = "select id from entities where ticker='" + dg.strCurrentTicker + "'";
	
	try {
		ResultSet rs = dbf.db_run_query(query);
		rs.next();
		newrow[2] = rs.getInt("id") + "";
		
		
	} catch (SQLException sqle) {
		UtilityFunctions.stdoutwriter.writeln("Problem looking up ticker: " + dg.strCurrentTicker + ",row skipped",Logs.WARN,"PF47.33");
		return;
	}
	
	
	
	newTableData.add(tmpArray);
	newTableData.add(newrow);

	propTableData = newTableData;
}



public void postProcessBloombergIndexes()
{
	String[] tmpArray = {"value","date_collected","entity_id"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	String[] rowdata, newrow;
	//String[] colheaders = propTableData.get(0);
	String[] rowheaders = propTableData.get(1);
	
	//newTableData.add(tmpArray);
	
	for (int row=2;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		
		
		
		
		newrow = new String[tmpArray.length];
		
		//newrow[0] = propStrTableDataSet;
		//newrow[2] = "FUNCTION";
		newrow[0] = rowdata[0].replace(",", "");
		//newrow[4] = "VARCHAR";
		newrow[1] = "NOW()";
		//newrow[6] = "INTEGER";
		String tmp = rowheaders[row-2];
		
		String ticker = tmp.substring(tmp.indexOf(">")+1,tmp.indexOf("</"));
		ticker = ticker.replace("&amp;", "&");
		ticker = ticker.replace("&#x80;", "€");
		ticker = ticker.replace("&#x20AC;", "€");
		ticker = ticker.replace("&#x83;", "ƒ");
		//Next line condenses multiple spaces down to one.
		ticker = ticker.replaceAll("\\s+", " "); 
		
		if (rowdata[0].contains("N.A."))
		{	
			UtilityFunctions.stdoutwriter.writeln("N.A. value for ticker " + ticker,Logs.WARN,"PF2.75");
			continue;
		}
		
		
		
		String query = "select * from entities where ticker='"+ticker+"'";
		
		try
		{
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			newrow[2] = rs.getInt("id") + "";
			newTableData.add(newrow);
			
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem looking up ticker: " + ticker + ",row skipped",Logs.WARN,"PF42.51");
			
			/*
			 * This is not a fatal error so we won't display the full exception.
			 */
			//UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		
		
		
		
		
		/*newrow[2] = tmp.substring(tmp.indexOf(">")+1,tmp.indexOf("</"));
		newrow[2] = newrow[2].replace("&amp;", "&");
		newrow[2] = newrow[2].replace("&#x80;", "€");
		newrow[2] = newrow[2].replace("&#x83;", "ƒ");*/
		
		
		
		
		
			
			
		
		
	}
	
	newTableData.add(0, tmpArray);
	propTableData = newTableData;
	
	

}




public void postProcessNasdaqEPSEstTable()
{
	String[] rowdata, newrow;
	//String strMonth;
	String strTicker;
	//int nQuarter=0;
	//int nYear=0;
	//int nMonth=0;
	String[] tmpArray = {"entity_id","date_collected","value","fiscalyear"};
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	/*OFP 10/17/2010 - For Ticker T there is currently redundant data being displayed */
	
	//Need to add code to make the query insert column definitions the first row in the table data that is returned.
	
	if (propStrTableDataSet.contains("_q_") == true)
	{
		int i = tmpArray.length;
		tmpArray = UtilityFunctions.extendArray(tmpArray);
		tmpArray = UtilityFunctions.extendArray(tmpArray);
		tmpArray = UtilityFunctions.extendArray(tmpArray);
		tmpArray[i] = "calyear";
		tmpArray[i+1] = "fiscalquarter";
		tmpArray[i+2] = "calquarter";
		
	}

	
	
	try
	{
		String query = "select url_dynamic from jobs where jobs.Data_Set='" + propStrTableDataSet + "'";


		strTicker = dg.strOriginalTicker;
		
		//String[] colheaders = propTableData.get(0);
		String[] rowheaders = propTableData.get(1);
		
		
		
		for (int x=2;x<propTableData.size();x++)
		{
			rowdata = propTableData.get(x);
			newrow = new String[tmpArray.length];
			//newrow[0] = "VARCHAR";
			newrow[0] = dg.nCurrentEntityId + "";
			//newrow[2] = "FUNCTION";
			newrow[1] = "NOW()";
			//newrow[4] = "VARCHAR";
			//newrow[2] = propStrTableDataSet;
			//newrow[6] = "INTEGER";
			newrow[2] = rowdata[0];
			//newrow[8] = "INTEGER";
			
			MoneyTime mt = new MoneyTime(rowheaders[x-2].substring(0,3),
					rowheaders[x-2].replace("&nbsp;","").substring(5,7),
					strTicker,dbf);
			
			
			//strMonth = rowheaders[x-2].substring(0,3);
		
		
			//nYear = Integer.parseInt(rowheaders[x-2].replace("&nbsp;","").substring(5,7));
			
			//String strFiscalQtrYear = uf.getFiscalYearAndQuarter(strTicker, nMonth, nYear);
			
			/*if (strMonth.compareTo("Jan") == 0)
				//boundary condition, subtract 1 from  year
				nYear--;*/
			
			//newrow[4] = Integer.toString(uf.retrieveAdjustedQuarter(Integer.parseInt(strFiscalQtrYear.substring(0,1)),
					//Integer.parseInt(strFiscalQtrYear.substring(1,3)),strTicker));
			//newrow[10] = "VARCHAR";
			//newrow[4] = "eps_est";
			
			newrow[3] = mt.strFiscalYear;
			
			
			if (propStrTableDataSet.contains("_q_") == true)
			{
				newrow[4] = Integer.toString(mt.nCalAdjYear);
				newrow[5] = mt.strFiscalQtr;
				newrow[6] = Integer.toString(mt.nCalQtr);
				
			}
			//newrow[12] = "INTEGER";
			//newrow[6] = strFiscalQtrYear.substring(0,1);
			//newrow[14] = "INTEGER";
			//newrow[7] = strFiscalQtrYear.substring(1,3);

			
	
			newTableData.add(newrow);
		}
	
		newTableData.add(0, tmpArray);
		propTableData = newTableData;
	}
	catch (SQLException sqle)
	{
		UtilityFunctions.stdoutwriter.writeln("Problem processing table data",Logs.ERROR,"PF43");
		UtilityFunctions.stdoutwriter.writeln(sqle);
	}

}

public void postProcessMsnMoneyEPSEst()
{

	/*
	 * Negative values are enclosed in span tags which have to be removed.
	 */
	if (strDataValue.indexOf("<") != -1)
	{
		strDataValue = strDataValue.substring(strDataValue.indexOf(">")+1,strDataValue.length());
		strDataValue = strDataValue.substring(0,strDataValue.indexOf("<"));
	}

	
		
}

public boolean preProcessYahooEPSEst() throws SQLException {


	

	/*
	 * Negative values are enclosed in font changing tags which have to be removed
	 */
	if (strTicker.equals("BF/B"))
	{
		//dg.strCurrentTicker = "BF-B";		
		dg.strCurrentTicker="BF-B";
	}
	else if (strTicker.equals("BRK/A"))
	{
		//dg.strCurrentTicker = "BRK-A";	
		dg.strCurrentTicker="BRK-A";
	}
	
	
	
	/*
	 * Need to get rid of these return values and go with exceptions 
	 */
	return(true);
	

	

	
		
}

public boolean preProcessGoogleEPS() throws SQLException {


	

	/*
	 * Negative values are enclosed in font changing tags which have to be removed
	 */
	if (strTicker.equals("BF/B")) {
		//dg.strCurrentTicker = "BF-B";		
		dg.strCurrentTicker="NYSE:BF.B";
	}
	else if (strTicker.equals("BRK/A")) {
		//dg.strCurrentTicker = "BRK-A";	
		dg.strCurrentTicker="NYSE:BRK.A";
	}
	else if (strTicker.equals("PG") || strTicker.equals("SCHW") || strTicker.equals("DF") || strTicker.equals("MHS") || strTicker.equals("NWL")) {
		dg.strCurrentTicker="NYSE:" + strTicker;
	}
	
	
	
	/*
	 * Need to get rid of these return values and go with exceptions 
	 */
	return(true);
	

	

	
		
}

public void postProcessTableYahooBeginYearVerify() throws CustomEmptyStringException, SQLException
{ 
	/* The purpose of this function is to verify that the quarter end months
	 * reported in yahoo match the begin_fiscal_calendar property stated in the
	 * entities table.
	 * 
	 * It can be swapped into the 'table_yahoo_q_eps_est_body' data_set
	 */
	String[] colheaders = propTableData.get(0);
	String strQEndMonth = colheaders[0];
	String query = "select begin_fiscal_calendar from entities where ticker='" + this.strTicker + "'";
	ResultSet rs = dbf.db_run_query(query);
	rs.next();
	int nBeginFiscalCal = MoneyTime.convertMonthStringtoInt(rs.getString("begin_fiscal_calendar"));
	PrintWriter fullfilewriter = null;
	
	try
	{
		fullfilewriter = new PrintWriter( new FileWriter("YahooVerify.txt",true),true);
	}
	catch (IOException ioe)
	{
	}
	
	
	switch(nBeginFiscalCal)
	{
		case 1:
		case 4:
		case 7:
		case 10:
			if (!(strQEndMonth.substring(0,3).equalsIgnoreCase("Mar") ||
					strQEndMonth.substring(0,3).equalsIgnoreCase("Jun") ||
					strQEndMonth.substring(0,3).equalsIgnoreCase("Sep") ||
					strQEndMonth.substring(0,3).equalsIgnoreCase("Dec")))
			{
				System.out.println("begin_fiscal_calendar and quote.yahoo mismatch for ticker " + strTicker);
				fullfilewriter.write(strTicker + "\n");
			}
			break;
		case 2:
		case 5:
		case 8:
		case 11:
			if (!(strQEndMonth.substring(0,3).equalsIgnoreCase("Apr") ||
					strQEndMonth.substring(0,3).equalsIgnoreCase("Jul") ||
					strQEndMonth.substring(0,3).equalsIgnoreCase("Oct") ||
					strQEndMonth.substring(0,3).equalsIgnoreCase("Jan")))
			{
				System.out.println("begin_fiscal_calendar and quote.yahoo mismatch for ticker " + strTicker);
				fullfilewriter.write(strTicker + "\n");
			}
			break;
		case 3:
		case 6:
		case 9:
		case 12:
			if (!(strQEndMonth.substring(0,3).equalsIgnoreCase("May") ||
					strQEndMonth.substring(0,3).equalsIgnoreCase("Aug") ||
					strQEndMonth.substring(0,3).equalsIgnoreCase("Nov") ||
					strQEndMonth.substring(0,3).equalsIgnoreCase("Feb")))
			{
				System.out.println("begin_fiscal_calendar and quote.yahoo mismatch for ticker " + strTicker);
				fullfilewriter.write(strTicker + "\n");
			}
			break;
		
	}
	
	fullfilewriter.close();

}

public void postProcessTreasuryDebtTable6()
{
	int j=0;
	for (int i=0;i<10;i++)
	{
		j++;
	}
	
	
}


public void postProcessTreasuryDirect() throws SQLException
{
	
	String[] data = propTableData.get(1);
	
	data[0] = data[0].replace(",", "");
	data[0] = data[0].substring(0,data[0].indexOf("."));
	long lPublicDebt = Long.parseLong(data[0]);
	lPublicDebt = lPublicDebt/10000000;
	
	data[1] = data[1].replace(",", "");
	data[1] = data[1].substring(0,data[1].indexOf("."));
	long lIntraGov = Long.parseLong(data[1]);
	lIntraGov = lIntraGov/10000000;
	
	String strQuery = "insert into batches ";
	strQuery += " (id,date_collected,task_id) values (";
	strQuery += dg.nTaskBatch + ",NOW()," + dg.nCurTask;
	strQuery += ")";
	
	try
	{
		dbf.db_update_query(strQuery);
	}
	catch (SQLException sqle)
	{
		UtilityFunctions.stdoutwriter.writeln("Problem with custom insert",Logs.ERROR,"PF55.52");
		UtilityFunctions.stdoutwriter.writeln(sqle);
	}
	
	

	
	strQuery = "insert into fact_data ";
	strQuery += "(\"value\",\"scale\",\"date_collected\",\"entity_id\",\"metric_id\",\"batch_id\") ";
	strQuery += " values (";
	strQuery += lPublicDebt;
	strQuery += ",7,NOW(),1360,9," + dg.nTaskBatch + ")";
	
	try
	{
		dbf.db_update_query(strQuery);
	}
	catch (SQLException sqle)
	{
		UtilityFunctions.stdoutwriter.writeln("Problem with custom insert",Logs.ERROR,"PF55.5");
		UtilityFunctions.stdoutwriter.writeln(sqle);
	}
	
	strQuery = "insert into fact_data ";
	strQuery += "(\"value\",\"scale\",\"date_collected\",\"entity_id\",\"metric_id\",\"batch\") ";
	strQuery += " values (";
	strQuery += lIntraGov;
	strQuery += ",7,NOW(),1360,10," + dg.nTaskBatch + ")";
	
	try
	{
		dbf.db_update_query(strQuery);
	}
	catch (SQLException sqle)
	{
		UtilityFunctions.stdoutwriter.writeln("Problem with custom insert",Logs.ERROR,"PF55.6");
		UtilityFunctions.stdoutwriter.writeln(sqle);
	}
	
	
	
	
	
	
}

public void preJobProcessTableXrateorg() throws SQLException
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

public void postProcessTableXrateorg() 
{
	String[] rowheaders = propTableData.get(1);
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	


	String[] tmpArray = {"value","date_collected","entity_id"};
	
	newTableData.add(tmpArray);
	
	
	for (int i=0;i<rowheaders.length;i++)
	{
		String strTmp = rowheaders[i];
		strTmp = "USD" + strTmp.substring(strTmp.indexOf("(")+1,strTmp.indexOf(")"));
		
		//The first 2 crosses on the europe page are inverse: EURUSD and GBPUSD
		if (propStrTableDataSet.contains("europe"))
		{
			if (i==0)
				strTmp = "EURUSD";
			else if (i==1)
				strTmp = "GBPUSD";
		}
		String[] tmpA = new String[tmpArray.length];
		//tmpA[0] = propStrTableDataSet;
		tmpA[0] = propTableData.get(i+2)[0];
		tmpA[1] = "NOW()";
		
		String query = "select * from entities where ticker='"+strTmp+"'";
		
		try
		{
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			tmpA[2] = rs.getInt("id") + "";
			newTableData.add(tmpA);
			
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem looking up ticker: " + strTmp + ",row skipped",Logs.WARN,"PF42.5");
			
			/*
			 * This is not a fatal error so we won't display the full exception.
			 */
			//UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		//tmpA[3] = "forex";
	

		
	}
	
	
	propTableData = newTableData;
	
	
	
	
	
}

public void postProcessDescriptionXrateorg() 
{
	/* Function to populate the full description for the ticker names, e.g. Argentine Peso for ARS */
	//String[] rowheaders = propTableData.get(1);
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	


	String[] tmpArray = {"data_set","value","date_collected","ticker"};
	
	newTableData.add(tmpArray);
	String query;
	
	
	for (int i=0;i<propTableData.size();i++)
	{
		String strTmp = propTableData.get(i)[0];
		String ticker = "USD" + strTmp.substring(strTmp.indexOf("(")+1,strTmp.indexOf(")"));
		
		strTmp = strTmp.substring(strTmp.indexOf(">")+1,strTmp.indexOf(" ("));
		
		
		//The first 2 crosses on the europe page are inverse: EURUSD and GBPUSD
		if (propStrTableDataSet.contains("europe"))
		{
			if (i==0 || i==1)
				continue;
		}
		
		query = "insert into entities (ticker,full_name) values ('" + ticker + "','" + strTmp +"')";
		
		try
		{
			dbf.db_update_query(query);
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Failed to insert ticker " + ticker + " into entities table",Logs.STATUS2,"PF43.49");
			UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		
	}
	
	

	
	
	
}

public void postProcessTableYahooEPSAct() throws CustomEmptyStringException, SQLException
{
	String[] tmpArray = {"data_set","value","date_collected","ticker","fiscalquarter","fiscalyear","calquarter","calyear"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	String[] rowdata, newrow;
	String[] colheaders = propTableData.get(0);
	
	newTableData.add(tmpArray);
	
	for (int row=2;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		for (int col=0;col<colheaders.length;col++)
		{
			newrow = new String[16];
			if (rowdata[col].compareTo("void") != 0)
			{
				//newrow[0] = "VARCHAR";
				newrow[0] = propStrTableDataSet;
				//newrow[2] = "INTEGER";
				
				if (rowdata[col].equals("N/A"))
				{
					UtilityFunctions.stdoutwriter.writeln("N/A value or empty value, skipping...",Logs.STATUS2,"PF43.5");
					continue;
				}
				
				/*
				 * Negative values are enclosed in font changing tags which have to be removed
				 */
				if (rowdata[col].indexOf("<") != -1)
				{
					rowdata[col] = rowdata[col].substring(rowdata[col].indexOf(">")+1,rowdata[col].length());
					rowdata[col] = rowdata[col].substring(0,rowdata[col].indexOf("<"));
				}
				
				
				newrow[1] = rowdata[col].replace(",","");
				//newrow[4] = "FUNCTION";
				newrow[2] = "NOW()";
				//newrow[6] = "VARCHAR";
				newrow[3] = dg.strOriginalTicker;
				//newrow[8] = "INTEGER";
				
				MoneyTime mt = new MoneyTime(colheaders[col].substring(0,3),colheaders[col].substring(4,6),strTicker,dbf);
				
				//int nMonth = uf.convertMontStringtoInt(colheaders[col].substring(0,3));
				//int nYear = Integer.parseInt(colheaders[col].substring(4,8));
				
				//String strFiscalQtrYear = uf.getFiscalYearAndQuarter(strTicker, nMonth, nYear);
				
				
		
				
				//newrow[4] = "eps";
				
				newrow[4] = mt.strFiscalQtr;
				newrow[5] = mt.strFiscalYear;
				newrow[6] = Integer.toString(mt.nCalQtr);		
				newrow[7] = Integer.toString(mt.nCalAdjYear);
				
				
				
				newTableData.add(newrow);
				
			}
			
			
		}
		
	}
	
	propTableData = newTableData;
	
	

}


public void postProcessTableYahooEPSEst() throws CustomEmptyStringException, SQLException
{
	/*String query = "select url_dynamic from extract_tables where Data_Set='" + propStrTableDataSet + "'";
	//ResultSet rs = dbf.db_run_query(query);
	//rs.next();
	//String strTicker = rs.getString("url_dynamic");*/

	String[] rowdata, newrow;
	String[] colheaders = propTableData.get(0);
	//String[] rowheaders = propTableData.get(1);
	
	String[] tmpArray = {"data_set","value","date_collected","ticker","fiscalyear"};//"calquarter","fiscalquarter"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	if (propStrTableDataSet.contains("_q_") == true)
	{
		int i = tmpArray.length;
		tmpArray = UtilityFunctions.extendArray(tmpArray);
		tmpArray = UtilityFunctions.extendArray(tmpArray);
		tmpArray = UtilityFunctions.extendArray(tmpArray);
		tmpArray[i] = "calyear";
		tmpArray[i+1] = "fiscalquarter";
		tmpArray[i+2] = "calquarter";
		
	}

	
	newTableData.add(tmpArray);
	
	for (int row=2;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		for (int col=0;col<colheaders.length;col++)
		{
			newrow = new String[16];
			if (rowdata[col].compareTo("void") != 0)
			{
				//newrow[0] = "VARCHAR";
				newrow[0] = propStrTableDataSet;
				//newrow[2] = "INTEGER";
				
				if (rowdata[col].equals("N/A"))
				{
					UtilityFunctions.stdoutwriter.writeln("N/A value or empty value, skipping...",Logs.STATUS2,"PF43.5");
					continue;
				}
				
				/*
				 * Negative values are enclosed in font changing tags which have to be removed
				 */
				if (rowdata[col].indexOf("<") != -1)
				{
					rowdata[col] = rowdata[col].substring(rowdata[col].indexOf(">")+1,rowdata[col].length());
					rowdata[col] = rowdata[col].substring(0,rowdata[col].indexOf("<"));
				}
				
				
				newrow[1] = rowdata[col];
				//newrow[4] = "FUNCTION";
				newrow[2] = "NOW()";
				//newrow[6] = "VARCHAR";
				newrow[3] = dg.strOriginalTicker;
				//newrow[8] = "INTEGER";
				
				MoneyTime mt = new MoneyTime(colheaders[col].substring(0,3),colheaders[col].substring(4,6),strTicker,dbf);
				
				//int nMonth = uf.convertMontStringtoInt(colheaders[col].substring(0,3));
				//int nYear = Integer.parseInt(colheaders[col].substring(4,8));
				
				//String strFiscalQtrYear = uf.getFiscalYearAndQuarter(strTicker, nMonth, nYear);
				
				
		
				//newrow[10] = "VARCHAR";
				//newrow[4] = "eps_est";
				//newrow[12] = "INTEGER";
				
				//newrow[14] = "INTEGER";
				newrow[4] = mt.strFiscalYear;
	
				if (propStrTableDataSet.contains("_q_") == true)
				{
					newrow[5] = Integer.toString(mt.nCalAdjYear);
					newrow[6] = mt.strFiscalQtr;
					newrow[7] = Integer.toString(mt.nCalQtr);
				}
				newTableData.add(newrow);
				
			}
			
			
		}
		
	}
	
	propTableData = newTableData;
	
}

public void postProcessTableBriefIClaims() throws CustomEmptyStringException, SQLException
{
	String[] rowdata, newrow;
	String[] colheaders = propTableData.get(0);
	
	//String[] rowheaders = propTableData.get(1);
	
	String[] tmpArray = {"data_set","value","date_collected","calmonth","calyear","day"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	newTableData.add(tmpArray);
	
	Calendar cal = Calendar.getInstance();
	
	for (int row=2;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		for (int col=0;col<colheaders.length;col++)
		{
			if (rowdata[col] == null)
				break;
			
			newrow = new String[tmpArray.length];
			
			newrow[0] = propStrTableDataSet;
			//This will have to be fixed if we ever get a 7 figure initial claims print
			newrow[1] = rowdata[col].replace("K","000");
			newrow[2] = "NOW()";
			//newrow[3] = "ue_rate";
			
			newrow[3] = Integer.toString(MoneyTime.convertMonthStringtoInt(colheaders[col]));
			newrow[4] = Integer.toString(cal.get(Calendar.YEAR));
			newrow[5] = colheaders[col].substring(colheaders[col].length()-2,colheaders[col].length());
		
		
			newTableData.add(newrow);
			
		}
	}
	
	propTableData = newTableData;
}

/*public void postProcessIMFGdpPPPActual() throws SQLException {
	
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

public void postProcessIMFGdpPPPEst() throws SQLException {
	
}*/



public boolean preProcessImfGdp() {
	Calendar cal = Calendar.getInstance();
	int nMaxEndYear = 2015;
	int nMinBeginYear = 2004;
	
	int nTempCurrent = 2011;
	
	//dg.strStage1URL = dg.strStage1URL.replace("${dynamic8}", cal.get(Calendar.YEAR)+"");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic8}", nTempCurrent +"");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic6}", nMinBeginYear + "");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic7}", cal.get(Calendar.YEAR)+"");
	
	
	//dg.strStage1URL = dg.strStage1URL.replace("${dynamic5}", cal.get(Calendar.YEAR)+"");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic5}", nTempCurrent +"");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic3}", cal.get(Calendar.YEAR)+"");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic4}", nMaxEndYear + "");
	return true;

}

public void postProcessImfGdp() throws SQLException {
	
	String[] tmpArray = {"value","date_collected","entity_id","calyear","scale"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	String[] rowdata, newrow;
	//String[] colheaders = propTableData.get(0);
	
	String[] colheaders = propTableData.remove(0);
	String[] rowheaders = propTableData.remove(0);
	
	//newTableData.add(tmpArray);
	
	for (int row=0;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		for (int col=0;col<colheaders.length;col++)
		{
		
		String strCountry = rowheaders[row];
		newrow = new String[tmpArray.length];
		
		newrow[0] = rowdata[col].replace(",", "");
		
		if (newrow[0].contains("n/a"))
		{
			UtilityFunctions.stdoutwriter.writeln("Retrieved n/a value, skipping processing for entity " + strCountry,Logs.WARN,"PF44");
			continue;
		}
		
		
		BigDecimal bdTmp = null;
		if (this.dg.nCurTask == 29) {
			if (!newrow[0].contains("bgcolor")) {
				bdTmp = new BigDecimal(newrow[0].substring(newrow[0].indexOf("right\">")+7,newrow[0].length()));
			}
			else
				continue;
		}
		else if (this.dg.nCurTask == 30) {
			if (newrow[0].contains("bgcolor")) {
				bdTmp = new BigDecimal(newrow[0].substring(newrow[0].indexOf("\">")+2,newrow[0].length()));
			}
			else
				continue;
		}
		else if (this.dg.nCurTask == 22){
			if (newrow[0].contains("bgcolor")) {
				bdTmp = new BigDecimal(newrow[0].substring(newrow[0].indexOf("\">")+2,newrow[0].length()));
			}
			else
				continue;
		}
		else if (this.dg.nCurTask == 33) {
			if (!newrow[0].contains("bgcolor")) {
				bdTmp = new BigDecimal(newrow[0].substring(newrow[0].indexOf("right\">")+7,newrow[0].length()));
			}
			else
				continue;
			
		}
		else {
			UtilityFunctions.stdoutwriter.writeln("Task id not found, should not have reached this point in the code. Terminating processing of task.",Logs.ERROR,"PF44.32");
			return;
		}
			


		newrow[0] = bdTmp.toString();
		//newrow[0] = rowdata[0].replace(",", "");
		//newrow[4] = "VARCHAR";
		newrow[1] = "NOW()";
		//newrow[6] = "INTEGER";
	
		
		/*
		 * If there are 2 sets of parens for the row header, we want to cutoff at the 2nd left paren.
		 */
		
		
	
		strCountry = strCountry.trim();
		
		strCountry = strCountry.replace("'", "\\'");
		
		if (strCountry.toUpperCase().equals("KOREA"))
			strCountry = "South Korea";
		
		//String query = "select * from entities where ticker='"+strCountry+"'";
		String query = "select entities.id from entities ";
		query += " join countries_entities on countries_entities.entity_id=entities.id ";
		query += " join countries on countries.id=countries_entities.country_id ";
		query += " where ticker='macro' ";
		query += " and countries.name='"+strCountry+"'";
			
		
		try
		{
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			newrow[2] = rs.getInt("entities.id") + "";
			
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem looking up country: " + strCountry + ",row skipped",Logs.ERROR,"PF43.55");
			continue;	
			/*
			 * This is not a fatal error so we won't display the full exception.
			 */
			//UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		
		newrow[3] = colheaders[col];
		newrow[4] = "9";
		
		
		
		newTableData.add(newrow);
		
		
			
		}
		
		
	}
	
	newTableData.add(0, tmpArray);
	propTableData = newTableData;
	
	
	
	
}

public void postProcessTableBLSUERate() throws CustomEmptyStringException, SQLException
{
	String[] rowdata, newrow;
	String[] colheaders = propTableData.get(0);
	String[] rowheaders = propTableData.get(1);
	
	String[] tmpArray = {"data_set","value","date_collected","calmonth","calyear"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	newTableData.add(tmpArray);
	
	for (int row=2;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		for (int col=0;col<colheaders.length;col++)
		{
			if (rowdata[col] == null)
				break;
			
			newrow = new String[tmpArray.length];
			
			newrow[0] = propStrTableDataSet;
			newrow[1] = rowdata[col];
			newrow[2] = "NOW()";
			//newrow[3] = "ue_rate";
			
			newrow[3] = Integer.toString(MoneyTime.convertMonthStringtoInt(colheaders[col]));
			newrow[4] = rowheaders[row-2];
		
		
			newTableData.add(newrow);
			
		}
	}
	
	propTableData = newTableData;
	
	
	
}

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



public void postProcessYahooEPSEst() throws CustomEmptyStringException, SQLException
{
	
	
	//Integer nAdjQuarter = uf.retrieveAdjustedQuarter(Integer.parseInt(fiscalyearquarter.substring(0,1)),Integer.parseInt(fiscalyearquarter.substring(3,5)),strTicker);

	if (strDataValue.equals("N/A"))
	{
		UtilityFunctions.stdoutwriter.writeln("Retrieved N/A value, skipping",Logs.ERROR,"PF44");
		throw (new CustomEmptyStringException());
	}
	/*
	 * Negative values are enclosed in font changing tags which have to be removed
	 */
	if (strDataValue.indexOf("<") != -1)
	{
		strDataValue = strDataValue.substring(strDataValue.indexOf(">")+1,strDataValue.length());
		strDataValue = strDataValue.substring(0,strDataValue.indexOf("<"));
	}
	
	String[] tmp = {"data_set","value","adj_quarter","ticker","date_collected","fiscalquarter","fiscalyear"};
	
	propTableData.add(0,tmp);
	
	//String fiscalyearquarter = uf.getFiscalYearAndQuarter(strTicker,-1,-1);
	
	//Integer nFiscalY = Integer.parseInt(fiscalyearquarter.substring(3,5));
	//Integer nFiscalQ = Integer.parseInt(fiscalyearquarter.substring(0,1));
	
	//String[] values=null;
	
	/*if (DataGrab.strCurDataSet.equals("yahoo_next_q_eps_est") || DataGrab.strCurDataSet.equals("yahoo_cur_q_eps_est"))
	{
		if (DataGrab.strCurDataSet.equals("yahoo_next_q_eps_est"))
		{
			if (nFiscalQ == 4)
			{
				nFiscalQ = 1;
				nFiscalY++;
			}
			else
				nFiscalQ++;
			
		}
		
		int nAdjQtr = uf.retrieveAdjustedQuarter(nFiscalQ,nFiscalY,strTicker);
		
		String[] values2 = {DataGrab.strCurDataSet, strDataValue, Integer.toString(nAdjQtr),strTicker, "NOW()", 
							Integer.toString(nFiscalQ),Integer.toString(nFiscalY)};
		values = values2;
	}*/
	/*else //strCurDataSet is either yahoo_next_y_eps_est or yahoo_cur_y_eps_est
	{
		
		if (DataGrab.strCurDataSet.equals("yahoo_next_y_eps_est"))
			nFiscalY++;
		String[] values2 = {DataGrab.strCurDataSet, strDataValue, "" ,strTicker, "NOW()", 
				"",Integer.toString(nFiscalY)};
		
		values = values2;
	}*/
	
	//propTableData.remove(1);
	//propTableData.add(values);

	
		
}

public boolean preProcessSECFiscalYear() throws SQLException
{
	/*
	 * Currently no analyst estimates for IVZ or LUK.
	 */

	//String query;
	String strNewTicker="";
	/*
	 * Negative values are enclosed in font changing tags which have to be removed
	 */
	if (strTicker.equals("BF/B"))
	{
		strNewTicker = "BFB";		
	}
	else if (strTicker.equals("BRK/A"))
	{
		strNewTicker = "BRKA";
	}
	/*
	 * Can't find the ticker symbol for CareFusion on the SEC site so using the CIK # instead
	 */
	else if (strTicker.equals("CFN"))
	{
		strNewTicker ="0001457543";
	}
	else if (strTicker.equals("QEP"))
	{
		strNewTicker = "0001108827";
	}
	else if (strTicker.equals("SNI"))
	{
		strNewTicker = "0001430602";
	}
	else if (strTicker.equals("ZION"))
	{
		strNewTicker = "0000109380";
	}
	
	if (strNewTicker.equals("") == false)
	{
		dg.strCurrentTicker=strNewTicker;
	}
	
	/*
	 * Need to get rid of these return values and go with exceptions 
	 */
	return(true);
	

	


		
}

public void postProcessSECFiscalYearEndRaw()
{
	/* This is for populating actual_fiscal_year_end in the entities table */

	
	String[] tmp = {"ticker","actual_fiscal_year_end"};
		
	propTableData.add(0,tmp);
	
	String[] values = {strTicker,strDataValue};
		
		
	propTableData.remove(1);
	propTableData.add(values);
		
	dbf.updateTableIntoDB(propTableData,"entities");
	
}

public void postProcessSECBeginCalendarYear()
{
	/* This is for populating begin_fiscal_calendar in the entities table */
	String strNewDataValue="";
	int nMonth = Integer.parseInt(strDataValue.substring(0,2));
	
	/*Tickers where the SEC has an incorrect value AZO,DV,EMN,FDO,GME,HES,JNJ,KMX,KO,KR,LLTC,NSC,PFG,PG,SPLS,VFC,WYNN,YUM */
	/*AZO=September
	PG=LLTC=DV=July
	YUM=WYNN=VFC=PFG=NSC=KO=HES=EMN=January
	FDO=September
	SPLS=KR=JNJ=GME=February
	KMX=March*/
	
	if ((strTicker.equals("AZO")) || (strTicker.equals("FDO")))
		nMonth=8;
	else if ((strTicker.equals("PG")) || (strTicker.equals("LLTC")) || (strTicker.equals("DV")))
		nMonth=6;
	else if ((strTicker.equals("YUM")) || (strTicker.equals("WYNN")) || (strTicker.equals("VFC")) || 
			(strTicker.equals("PFG")) || (strTicker.equals("NSC")) || (strTicker.equals("KO")) ||
			(strTicker.equals("HES")) || (strTicker.equals("EMN")))
		nMonth=12;
	else if (strTicker.equals("KMX"))
		nMonth=2;
	else if ((strTicker.equals("SPLS")) || (strTicker.equals("KR")) || (strTicker.equals("JNJ")) || 
			(strTicker.equals("GME")))
		nMonth=1;
	
		
	
	
	
	/*These are mostly retail stocks whose fiscal calendar doesn't end directly at the end of a month */
	if ((strTicker.equals("AVY")) || (strTicker.equals("CERN")) || (strTicker.equals("CPB"))
		|| (strTicker.equals("CSC")) || (strTicker.equals("DIS")) || (strTicker.equals("JEC"))
		|| (strTicker.equals("JNJ"))|| (strTicker.equals("K"))|| (strTicker.equals("MU"))
		|| (strTicker.equals("SNA")) || (strTicker.equals("SNDK")) || (strTicker.equals("SWK"))
		|| (strTicker.equals("SWY"))|| (strTicker.equals("SYMC"))|| (strTicker.equals("TXT"))
		|| (strTicker.equals("VAR")) || (strTicker.equals("VFC")) || (strTicker.equals("XLNX")))
		nMonth--;
	
	//boundary condition
	if (nMonth==0)
		nMonth=12;
	
	if (nMonth == 1)
		strNewDataValue = "February";
	else if (nMonth == 2)
		strNewDataValue = "March";
	else if (nMonth == 3)
		strNewDataValue = "April";
	else if (nMonth == 4)
		strNewDataValue = "May";
	else if (nMonth == 5)
		strNewDataValue = "June";
	else if (nMonth == 6)
		strNewDataValue = "July";
	else if (nMonth == 7)
		strNewDataValue = "August";
	else if (nMonth == 8)
		strNewDataValue = "September";
	else if (nMonth == 9)
		strNewDataValue = "October";
	else if (nMonth == 10)
		strNewDataValue = "November";
	else if (nMonth == 11)
		strNewDataValue = "December";
	else if (nMonth == 12)
		strNewDataValue = "January";
	else
		UtilityFunctions.stdoutwriter.writeln("Problem converting month to begin calendar year month",Logs.ERROR,"PF44.5");
		
	

		
		String[] tmp = {"ticker","begin_fiscal_calendar"};
		
		propTableData.add(0,tmp);
				
		String[] values = {strTicker,strNewDataValue};
				
		propTableData.remove(1);
		propTableData.add(values);
		
		dbf.updateTableIntoDB(propTableData,"entities");
		
	
}

public void postProcessYahooBeginCalendarYear()
{
	/*Created these 2 yahoo data captures because there were so many errors in the SEC data.
	 * Your tax dollars at work.
	 */
	int nMonth = MoneyTime.convertMonthStringtoInt(strDataValue.substring(0,3));
	
	/*These are mostly retail stocks whose fiscal calendar doesn't end directly at the end of a month */
	if ((strTicker.equals("AVY")) || (strTicker.equals("CERN")) || (strTicker.equals("CPB"))
		|| (strTicker.equals("CSC")) || (strTicker.equals("DIS")) || (strTicker.equals("JEC"))
		|| (strTicker.equals("JNJ"))|| (strTicker.equals("K"))|| (strTicker.equals("MU"))
		|| (strTicker.equals("SNA")) || (strTicker.equals("SNDK")) || (strTicker.equals("SWK"))
		|| (strTicker.equals("SWY"))|| (strTicker.equals("SYMC"))|| (strTicker.equals("TXT"))
		|| (strTicker.equals("VAR")) || (strTicker.equals("VFC")) || (strTicker.equals("XLNX")))
		nMonth--;
	

	
	//We have end of year, need beginning of year 
	nMonth++;
	
	//boundary condition
	if (nMonth==13)
		nMonth=1;
	
	String strMonth = MoneyTime.convertMonthInttoString(nMonth);
	
	String[] tmp = {"ticker","begin_fiscal_calendar"};
	
	propTableData.add(0,tmp);
			
	String[] values = {strTicker,strMonth};
			
	propTableData.remove(1);
	propTableData.add(values);
	
	dbf.updateTableIntoDB(propTableData,"entities");
	
	
	
}

public void postProcessYahooCompanyName()
{
	String[] tmp = {"ticker","full_name"};
	
	propTableData.add(0,tmp);
	
	String[] values = {dg.strOriginalTicker,propTableData.get(1)[0]};
	
	propTableData.remove(1);
	propTableData.add(values);
	
	dbf.updateTableIntoDB(propTableData, "entities");
	
}

public void postProcessYahooFiscalYearEndRaw()
{
	/*Created these 2 yahoo data captures because there were so many errors in the SEC data.
	 * Your tax dollars at work.
	 */
	
	String[] tmp = {"ticker","actual_fiscal_year_end"};
	
	propTableData.add(0,tmp);
	
	String[] values = {strTicker,strDataValue};
		
		
	propTableData.remove(1);
	propTableData.add(values);
		
	dbf.updateTableIntoDB(propTableData,"entities");
	

}

public void postProcessGoogleEPSTable() throws SQLException {


		String strTicker = dg.strOriginalTicker;

		String[] rowdata, newrow;
		String[] colheaders = propTableData.get(0);
		//String[] rowheaders = propTableData.get(1);
		
		
		ArrayList<String[]> newTableData = new ArrayList<String[]>();
		String[] tmpArray = {"value","date_collected","entity_id","fiscalquarter","fiscalyear","calquarter","calyear"};
		newTableData.add(tmpArray);
		String tmpVal;
		for (int row=2;row<propTableData.size();row++)
		{
			rowdata = propTableData.get(row);
			
			for (int col=0;col<colheaders.length;col++)
			{
				newrow = new String[16];
				if (rowdata[col].compareTo("void") != 0)
				{
				
					//newrow[0] = propStrTableDataSet;
					
					tmpVal = rowdata[col];
					
					if (tmpVal.contains("span"))
					//this is a negative number
						newrow[0] = tmpVal.substring(tmpVal.indexOf(">-")+1,tmpVal.indexOf("</"));
					else
						newrow[0] = tmpVal;
					
					/*
					 * Some tickers have tables with only 4 columns. The table
					 * processing will wrap around to the next row in those cases.
					 */
					if (newrow[0].contains("Diluted"))
						continue;
					
					//Berkshire tends to be the only company with an EPS in the thousands.
					newrow[0] = newrow[0].replace(",", "");
			
				
					newrow[1] = "NOW()";
					
					newrow[2] = dg.nCurrentEntityId + "";
					
					/*
					 * OFP 11/12/2011 - I have to deal with this wacky issue where google has the end of quarter
					 * days off by one in some instances. 
					 * 
					 * CSC is one example. For calendar quarter 2, cal year 2011, they have the end date listed as 7/1/2011
					 * instead of 6/30/2011. 
					 * 
					 * So as a workaround, if the Day of month is 1, I'm going to subtract one day from the date.
					 * 
					 * It's not known at this time if this will screw up other tickers.
					 * 
					 * If other tickers behave differently then I may have to set up ticker specific code in here.
					 */
					
					Calendar cal = Calendar.getInstance();
					String[] date = colheaders[col].split("-");
					
					cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
					cal.set(Calendar.MONTH,Integer.parseInt(date[1])-1);
					cal.set(Calendar.YEAR,Integer.parseInt(date[0]));
					
					if (cal.get(Calendar.DAY_OF_MONTH)==1)
						cal.add(Calendar.DAY_OF_MONTH,-1);
					
					//String strFiscalYearQuarter = MoneyTime.getFiscalYearAndQuarter(strTicker,Integer.parseInt(colheaders[col].substring(5,7)), Integer.parseInt(colheaders[col].substring(0,4)),dbf);
					String strFiscalYearQuarter = MoneyTime.getFiscalYearAndQuarter(strTicker, cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR), dbf);
					String strCalYearQuarter = MoneyTime.getCalendarYearAndQuarter(strTicker, Integer.parseInt(strFiscalYearQuarter.substring(0,1)), Integer.parseInt(strFiscalYearQuarter.substring(1,5)),dbf);
					
				
					newrow[3] = strFiscalYearQuarter.substring(0,1);
					
					/*don't use they year returned from getFiscalYearAndQuarter - use the one retrieved from the web page*/
				
					newrow[4] = strFiscalYearQuarter.substring(1,5);
					
					
					newrow[5] = strCalYearQuarter.substring(0,1);
					newrow[6] = strCalYearQuarter.substring(1,5);
						
					newTableData.add(newrow);
					
				}
				
				
			}
			
		}
		propTableData = newTableData;

	
}

public void postProcessMWatchEPSEstTable() throws SQLException,SkipLoadException {
	
		/* OFP 11/12/2011 - One issue with this data source is because the column headers are
		 * relative (i.e. "this quarter", "next quarter") there is no way to guarantee which quarter
		 * marketwatch is referring to. If they are slow in updating the website, then the data will
		 * be collected for the wrong quarters.
		 */
		/*
		 * As of 12/15/2010 there is no data for RAND nor VIA and no obvious string to key off of for a no data check.
		 */

		

		String[] rowdata, newrow;
		String[] colheaders = propTableData.get(0);
		
		Calendar cal = Calendar.getInstance();
		
		
		
		
		
		ArrayList<String[]> newTableData = new ArrayList<String[]>();
		
		String[] tmpArray = {"value","date_collected","entity_id","fiscalyear"};
		
		if (propStrTableDataSet.contains("q"))
		{
			int nSize = tmpArray.length;
			tmpArray = UtilityFunctions.extendArray(tmpArray);
			tmpArray = UtilityFunctions.extendArray(tmpArray);
			tmpArray = UtilityFunctions.extendArray(tmpArray);
			tmpArray[nSize] = "fiscalquarter";
			tmpArray[nSize+1] = "calquarter";
			tmpArray[nSize+2] = "calyear";
			
			/*
			 * OFP 11/12/2011 - Ran into an issue where the column headers weren't being
			 * read correctly.
			 */
			
			if (!colheaders[0].toUpperCase().equals("THIS QUARTER")) {
				UtilityFunctions.stdoutwriter.writeln("Invalid column header format for entity_id " + dg.nCurrentEntityId,Logs.STATUS2,"PF46");
				throw new SkipLoadException();
			}
				
			
			
		}
		newTableData.add(tmpArray);
		String tmpVal;
		
		

		for (int row=2;row<propTableData.size();row++)
		{
			rowdata = propTableData.get(row);
			
			for (int col=0;col<colheaders.length;col++)
			{
				newrow = new String[7];
				if (rowdata[col].compareTo("void") != 0)
				{
				
					
					tmpVal = rowdata[col];
					
					if (tmpVal.contains("span"))
					//this is a negative number
						newrow[0] = tmpVal.substring(tmpVal.indexOf(">-")+1,tmpVal.indexOf("</"));
					else
						newrow[0] = tmpVal;
					
					//Berkshire tends to be the only company with an EPS in the thousands.
					newrow[0] = newrow[0].replace(",", "");
			
				
					newrow[1] = "NOW()";
					
					newrow[2] = dg.nCurrentEntityId + "";
					
					String strFiscalYearQuarter = MoneyTime.getFiscalYearAndQuarter(strTicker,cal.get(Calendar.MONTH), cal.get(Calendar.YEAR),dbf);
					
					int nFiscalQuarter=Integer.parseInt(strFiscalYearQuarter.substring(0,1));
					int nFiscalYear = Integer.parseInt(strFiscalYearQuarter.substring(1,5));
					
					if (colheaders[col].toUpperCase().equals("NEXT QUARTER"))
					{
						nFiscalQuarter++;
						if (nFiscalQuarter == 5)
						{
							nFiscalYear++;
							nFiscalQuarter=1;
						}
					}
					else if (colheaders[col].toUpperCase().equals("NEXT FISCAL"))
					{
						nFiscalYear++;
					}
						
						
					
					
					
					newrow[3] = nFiscalYear + "";
					//newrow[4] = colheaders[col].substring(5,7);//Integer.toString(row-1);
					
					
					/*don't use the year returned from getFiscalYearAndQuarter - use the one retrieved from the web page*/
					if (propStrTableDataSet.contains("q"))
					{
						String strCalYearQuarter = MoneyTime.getCalendarYearAndQuarter(strTicker, nFiscalQuarter, nFiscalYear,dbf);
					
						newrow[4] = nFiscalQuarter + "";
						newrow[5] = strCalYearQuarter.substring(0,1);
						newrow[6] = strCalYearQuarter.substring(1,5);
					}
						
					newTableData.add(newrow);
					
				}
				
				
			}
			
		}
		propTableData = newTableData;

	
}




	public boolean preNDCYahooEPSEst()
	{
		String strRegex = "(?i)(There is no Analyst Estimates)";
		  UtilityFunctions.stdoutwriter.writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF45");

		  
		  Pattern pattern = Pattern.compile(strRegex);
		  //UtilityFunctions.stdoutwriter.writeln("after strbeforeuniquecoderegex compile", Logs.STATUS2);
		  
		  Matcher matcher = pattern.matcher(dg.returned_content);
		  
		  //UtilityFunctions.stdoutwriter.writeln("Current offset before final data extraction: " + nCurOffset,Logs.STATUS2);
		  
		  return(matcher.find());
		
	}

	public boolean preNDCNasdaqEPSEst()
	{
		
	  String strRegex = "(?i)(No Data Available)";
	  UtilityFunctions.stdoutwriter.writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF46");

	  
	  Pattern pattern = Pattern.compile(strRegex);
	  //UtilityFunctions.stdoutwriter.writeln("after strbeforeuniquecoderegex compile", Logs.STATUS2);
	  
	  Matcher matcher = pattern.matcher(dg.returned_content);
	  
	  //UtilityFunctions.stdoutwriter.writeln("Current offset before final data extraction: " + nCurOffset,Logs.STATUS2);
	  
	  if (matcher.find())
		  return(true);
	  
	  strRegex = "(?i)(feature currently is unavailable)";
	  UtilityFunctions.stdoutwriter.writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF46.5");

	  
	  pattern = Pattern.compile(strRegex);
	  //UtilityFunctions.stdoutwriter.writeln("after strbeforeuniquecoderegex compile", Logs.STATUS2);
	  
	  matcher = pattern.matcher(dg.returned_content);
	  
	  return(matcher.find());
	  
	  
	  
	  

	}
	
	public boolean preNDCNasdaqFiscalYear()
	{
		String strRegex = "(?i)(This feature currently is unavailable)";
		UtilityFunctions.stdoutwriter.writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF47");
		
		Pattern pattern = Pattern.compile(strRegex);
		
		Matcher matcher = pattern.matcher(dg.returned_content);
		
		return(matcher.find());
		
	}
	
	/*SEC begin fiscal year notes*/
	/*
	 * PG ends in June not April.
	 */
	
}







