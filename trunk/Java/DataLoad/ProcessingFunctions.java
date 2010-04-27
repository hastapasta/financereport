package com.roeschter.jsl;

import java.sql.*;
import java.lang.reflect.Method;

class ProcessingFunctions
{
	static String strStaticDataSet;
	static String strStaticDataValue;
	static String strTemp1;
	static String strTemp2;
	

public static void preProcessing(String strDataSet)
{

		ProcessingFunctions pf = new ProcessingFunctions();
		String query = "select pre_process_func_name from extract_info where Data_Set='" + strDataSet + "'";
		ResultSet rs = UtilityFunctions.db_run_query(query);
		
		strStaticDataSet = "";
		strStaticDataValue = "";
		strTemp1 = "";
		strTemp2 = "";
	
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
			strStaticDataSet = strDataSet;
			
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
	System.out.println("Inside PreNasdaqEPS...");
	strTemp1 = "";
	strTemp2 = "";
	String[] updated_tickers = {"BA", "AA", "HPQ", "TRV", "DIS"};
	String query = "select url_dynamic,url_static,Cell_Count from extract_info where Data_Set='" + strStaticDataSet + "'";
	ResultSet rs = UtilityFunctions.db_run_query(query);
	String strCurTicker = "";
	String strURLStatic = "";
	String strCellCount = "";
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
			if ((strStaticDataSet.substring(10,11)).compareTo("7") == 0)
			{
				//preserve the original values
				strTemp1 = strURLStatic;
				strTemp2 = strCellCount;
				UtilityFunctions.db_update_query("update extract_info set url_static=http://fundamentals.nasdaq.com/redpage.asp?page=2&selected=, Cell_Count=2 where Data_Set = '" + strStaticDataSet + "'");
			}
			//if year 2008 or 2009, change column
			else if ((strStaticDataSet.substring(10,11)).compareTo("8") == 0)
			{
				System.out.println("PreProcessing Data_Set: " + strStaticDataSet + " with ticker " + strCurTicker);
				strTemp2 = strCellCount;
				UtilityFunctions.db_update_query("update extract_info set Cell_Count=4 where Data_Set = '" + strStaticDataSet + "'");
			}
			else if ((strStaticDataSet.substring(10,11)).compareTo("9") == 0)
			{
				strTemp2 = strCellCount;
				UtilityFunctions.db_update_query("update extract_info set Cell_Count=3 where Data_Set = '" + strStaticDataSet + "'");
			}
				
				
			
			
		}
	}
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
		

	
	
	
	
	
}







