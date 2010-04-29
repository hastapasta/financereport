package com.roeschter.jsl;

import java.sql.*;
import java.lang.reflect.Method;

class ProcessingFunctions
{
	static String strStaticDataSet;
	static String strStaticDataValue;
	static String strTemp1, strTemp2, strTemp3;
	

public static void preProcessing(String strDataSet)
{

		ProcessingFunctions pf = new ProcessingFunctions();
		String query = "select pre_process_func_name from extract_info where Data_Set='" + strDataSet + "'";
		ResultSet rs = UtilityFunctions.db_run_query(query);
		
		
	
		try
		{
			rs.next();
			String strFunctionName = rs.getString("pre_process_func_name");
			
			System.out.println("Pre Process Func Name: " + strFunctionName);
			if (strFunctionName == null)
			{
				System.out.println("No pre process function, exiting...");
				return;
			}
			strStaticDataSet = "";
			strStaticDataValue = "";
			strTemp1 = "";
			strTemp2 = "";
			strTemp3 = "";
			strStaticDataSet = strDataSet;
			System.err.println(strFunctionName);
			Method m = pf.getClass().getMethod(strFunctionName,new Class[] {});
			m.invoke(pf, new Object[] {});
		}
		catch (Exception tmpE)
		{
			System.err.println("preProcessing method call failed");
			tmpE.printStackTrace();
		}
	



}

public static String postProcessing(String strDataSet,String strDataValue)
{
	
		ProcessingFunctions pf = new ProcessingFunctions();
		String query = "select post_process_func_name from extract_info where Data_Set='" + strDataSet + "'";
		ResultSet rs = UtilityFunctions.db_run_query(query);
		strStaticDataValue = strDataValue;

	
		try
		{
			rs.next();
			String strFunctionName = rs.getString("post_process_func_name");
			
			System.out.println("Post Process Func Name: " + strFunctionName);
			if (strFunctionName == null)
			{
				System.out.println("No post process function, exiting...");
				return(strDataValue);
			}
			strStaticDataSet = strDataSet;
			Method m = pf.getClass().getMethod(strFunctionName,new Class[] {});
			m.invoke(pf, new Object[] {});
		}
		catch (Exception tmpE)
		{
			System.err.println("postProcessing method call failed");
			tmpE.printStackTrace();
		}
		
		return(strStaticDataValue);
	



}


public static void preNasdaqEPS()
{
	try
	{
		String query = "select url_dynamic from extract_info where Data_Set='" + strStaticDataSet + "'";
		ResultSet rs = UtilityFunctions.db_run_query(query);
		rs.next();
		System.err.println("Processing ticker: " + rs.getString("url_dynamic"));
		query = "update extract_info set url_dynamic='" + rs.getString("url_dynamic") + "' where data_set='nasdaq_current_fiscal_year'";
		//have to save the value here because the get_value() call wipes it out.
		String tmpStaticDataSet = strStaticDataSet;
		UtilityFunctions.db_update_query(query);
		String strCurValue = DataGrab.get_value("nasdaq_current_fiscal_year");
		System.err.println("Nasdaq_current_fiscal_year: " + strCurValue);
		
		
		if (strCurValue.compareTo("2010") == 0)
		{
			//need to shift things over one.
			query = "select Cell_Count,url_static from extract_info where Data_Set='" + tmpStaticDataSet + "'";
			System.err.println(query);
			rs = UtilityFunctions.db_run_query(query);
			rs.next();
			int nCellCount = rs.getInt("Cell_Count");
			strTemp2 = String.valueOf(nCellCount);
			strTemp1 = (String)rs.getString("url_static");
			
			if ((tmpStaticDataSet.substring(10,11)).compareTo("7")==0)
			{
				query = "update extract_info set url_static='http://fundamentals.nasdaq.com/redpage.asp?page=2&selected=', Cell_Count=2 where Data_Set='" + tmpStaticDataSet + "'";
			}
			else 
			{
				query = "update extract_info set Cell_Count=" + String.valueOf(nCellCount + 1) + " where Data_Set='" + tmpStaticDataSet + "'";
			}
			UtilityFunctions.db_update_query(query);
			
			
		}
		
		
		
	}
	catch (SQLException sqle)
	{
		System.err.println("preNasdaqEPS2 failed...");
		sqle.printStackTrace();
	}
	
}

public static void preNasdaqEPS2()
{
	System.out.println("Inside PreNasdaqEPS...");
	strTemp1 = "";
	strTemp2 = "";
	
	//these are the companies that have already released results for the previous qtr
	String[] updated_tickers = {"BA", "AA", "HPQ", "TRV", "DIS"};
	String query = "select url_dynamic,url_static,Cell_Count from extract_info where Data_Set='" + strStaticDataSet + "'";
	ResultSet rs = UtilityFunctions.db_run_query(query);
	String strCurTicker = "";
	String strURLStatic = "";
	String strCellCount = "";
	int nYear = Integer.parseInt(strStaticDataSet.substring(10,11));
	int nQuarter = Integer.parseInt(strStaticDataSet.substring(8,9));
	try
	{
		rs.next();
		strCurTicker = rs.getString("url_dynamic");
		strURLStatic = rs.getString("url_static");
		strCellCount = rs.getString("Cell_Count");
	}
	catch(SQLException sqle)
	{
		System.out.println("Something wrong with sql in preNasdaqEPS");
		sqle.printStackTrace();
		return;
	}
	
	for (int i=0;i<updated_tickers.length;i++)
	{
		if (strCurTicker.compareTo(updated_tickers[i]) == 0)
		{
			//if year 2007... then change url and column
			if (nYear == 7)
			{
				//preserve the original values
				strTemp1 = strURLStatic;
				strTemp2 = strCellCount;
				UtilityFunctions.db_update_query("update extract_info set url_static=http://fundamentals.nasdaq.com/redpage.asp?page=2&selected=, Cell_Count=2 where Data_Set = '" + strStaticDataSet + "'");
			}
			//if year 2008 or 2009, change column
			else if (nYear == 8)
			{
				System.out.println("PreProcessing Data_Set: " + strStaticDataSet + " with ticker " + strCurTicker);
				strTemp2 = strCellCount;
				UtilityFunctions.db_update_query("update extract_info set Cell_Count=4 where Data_Set = '" + strStaticDataSet + "'");
			}
			else if (nYear == 9)
			{
				strTemp2 = strCellCount;
				UtilityFunctions.db_update_query("update extract_info set Cell_Count=3 where Data_Set = '" + strStaticDataSet + "'");
			}
				
				
			
			
		}
	}
	/*
		Now have to add code to handle non-standard fiscal calendars.
		January: DIS, HPQ - Don't have to do anything for Jan, position is the same.
		October: CSCO
		September: MSFT, PG
		April: WMT, JNJ, HD
	
		Normal positionings:
		Q1 - row 9, Q2 - row 14, Q3 - row 19, Q4 - row 24 
		2009 - cell 2, 2008 - cell 3, 2007 - cell 4 
	*/
	int nNewRow, nNewCell;
	String strNewUrl;
	rs = UtilityFunctions.db_run_query("select ticker from company where begin_fiscal_calendar='Oct' OR begin_fiscal_calendar='Sep'");
	/* Normal Q1 is now Q3 */
	
	/*if (nYear == 2009)
	{
		if (nQuarter == 1)
			nNewRow = 19; nNewCell = 3;
		else if (nQuarter == 2)
			nNewRow = 24; nNewCell = 3;
		else if (nQuarter == 3)
			nNewRow = 9; nNewCell = 2;
		else if (nQuarter == 4)
			nNewRow = 15; nNewCell = 2;
	}
	else if (nYear == 2008)
	{
		if (nQuarter == 1)
			nNewRow = 19; nNewCell = 4;
		else if (nQuarter == 2)
			nNewRow = 24; nNewCell = 4;
		else if (nQuarter == 3)
			nNewRow = 9;  nNewCell = 3;
		else if (nQuarter == 4)
			nNewRow = 15; nNewCell = 3;
	}
	else if (nYear == 2007)
	{
		if (nQuarter == 1)
			nNewRow = 19; nNewCell  2; strNewUrl = "http://fundamentals.nasdaq.com/redpage.asp?page=2&selected=";
		else if (nQuarter == 2)
			nNewRow = 24; nNewCell = ; strNewUrl = "http://fundamentals.nasdaq.com/redpage.asp?page=2&selected=";
		else if (nQuarter == 3)
			nNewRow = 9;  nNewCell = 4;
		else if (nQuarter == 4)
			nNewRow = 15; nNewCell = 4;
	}*/
	
	
			
			
	
	
		
	
	
	
}

public static void postNasdaqEPS()
{
	System.out.println("Inside postNasdaqEPS...");
	
	//restore URL static
	if (strTemp1.compareTo("") != 0)
	{
		UtilityFunctions.db_update_query("update extract_info set url_static='" + strTemp1 + "' where Data_Set = '" + strStaticDataSet + "'");
		strTemp1 = "";
	}
	//restore Cell Count
	if (strTemp2.compareTo("") != 0)
	{
		UtilityFunctions.db_update_query("update extract_info set Cell_Count='" + strTemp2 + "' where Data_Set = '" + strStaticDataSet + "'");
		strTemp2 = "";
	}
}

public static void postNasdaqFiscalYear()
{
	if (strStaticDataValue.compareTo("January") == 0)
		strStaticDataValue = "November";
	else if (strStaticDataValue.compareTo("February") == 0)
		strStaticDataValue = "December";
	else if (strStaticDataValue.compareTo("March") == 0)
		strStaticDataValue = "January";
	else if (strStaticDataValue.compareTo("April") == 0)
		strStaticDataValue = "February";
	else if (strStaticDataValue.compareTo("May") == 0)
		strStaticDataValue = "March";
	else if (strStaticDataValue.compareTo("June") == 0)
		strStaticDataValue = "April";
	else if (strStaticDataValue.compareTo("July") == 0)
		strStaticDataValue = "May";
	else if (strStaticDataValue.compareTo("August") == 0)
		strStaticDataValue = "June";
	else if (strStaticDataValue.compareTo("September") == 0)
		strStaticDataValue = "July";
	else if (strStaticDataValue.compareTo("October") == 0)
		strStaticDataValue = "August";
	else if (strStaticDataValue.compareTo("November") == 0)
		strStaticDataValue = "September";
	else if (strStaticDataValue.compareTo("December") == 0)
		strStaticDataValue = "October";

	
	
	
}
		

	
	
	
	
	
}







