package com.roeschter.jsl;

import java.sql.*;
import java.lang.reflect.Method;

class ProcessingFunctions
{
	static strStaticDataSet=null;

public static void preProcessing(String strDataSet)
{
		String tmp = "some object";
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

public static void preNasdaqEPS()
{
	System.out.println("Inside PreNasdaqEPS...");
	String[] updated_tickers = new String["BA", "AA", "HPQ", "TRV", "DIS"];
	ResultSet rs = UtilityFunctions.run_db_query("select url_dynamic from extract_info where Data_Set='" + strStaticDataSet + "'";
	rs.next();
	String strCurTicker = rs.getString("url_dynamic");
	for (int i=0;i<updated_tickers.size();i++)
	{
		if (strCurTicker.compareTo(updated_tickers[i]) == 0)
		{
			//if year 2007... then change url and column
			
			//if year 2008 or 2009, change column
		}
	}
}
		

	
	
	
	
	
}






}