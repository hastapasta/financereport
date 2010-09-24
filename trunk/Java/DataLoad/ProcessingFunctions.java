package com.roeschter.jsl;

import java.sql.*;
import java.lang.reflect.Method;
import java.util.ArrayList;


class ProcessingFunctions
{
	String strStaticDataSet;
	String strStaticDataValue;
	String strTemp1, strTemp2, strTemp3;
	
	/* Need to figure out why two log files are being written to... uf.stdoutwriter and stdoutwriter */
	UtilityFunctions uf;
	DataGrab dg;
	CustomBufferedWriter stdoutwriter;
	
	public ProcessingFunctions(UtilityFunctions tmpUF, DataGrab tmpDG)
	{
		this.uf = tmpUF;
		this.dg = tmpDG;
	}
	

	

public boolean preProcessing(String strDataSet, UtilityFunctions tmpUF)
{
	  /* For the return value, return false if the attempt to grab the value should be skipped. */
	  
	

		String query = "select pre_process_func_name from extract_info where Data_Set='" + strDataSet + "'";
		
		
		
	
		try
		{
			ResultSet rs = uf.db_run_query(query);
			rs.next();
			String strFunctionName = rs.getString("pre_process_func_name");
			
			uf.stdoutwriter.writeln("Pre Process Func Name: " + strFunctionName);
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
			{
				uf.stdoutwriter.writeln("No pre process function, exiting...");
				return(true);
			}
			strStaticDataSet = "";
			strStaticDataValue = "";
			strTemp1 = "";
			strTemp2 = "";
			strTemp3 = "";
			strStaticDataSet = strDataSet;
			uf.stdoutwriter.writeln(strFunctionName);
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception tmpE)
		{
			uf.stdoutwriter.writeln("preProcessing method call failed");
			uf.stdoutwriter.writeln(tmpE);
			return(false);
		}
		
	
	



}

public String postProcessing(String strDataSet,String strDataValue)
{
	
		String query = "select post_process_func_name from extract_info where Data_Set='" + strDataSet + "'";
		
		strStaticDataValue = strDataValue;

	
		try
		{
			ResultSet rs = uf.db_run_query(query);
			rs.next();
			String strFunctionName = rs.getString("post_process_func_name");
			
			stdoutwriter.writeln("Post Process Func Name: " + strFunctionName);
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
			{
				stdoutwriter.writeln("No post process function, exiting...");
				return(strDataValue);
			}
			strStaticDataSet = strDataSet;
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			m.invoke(this, new Object[] {});
		}
		catch (Exception tmpE)
		{
			uf.stdoutwriter.writeln("postProcessing method call failed");
			stdoutwriter.writeln(tmpE);
		}
		
		return(strStaticDataValue);
	



}

public boolean preNasdaqEPSEst()
{
	/*
		I'm going to store the estimated values as the adjusted quarter values whereas the
		actual eps values are stored unadjusted (hence the adjusting is done in the query for 
		the actual values)
	*/
	//initialize the row value
	String query; 
	
	
	boolean done = false;
	boolean bSuccess = false;
	//loop through doing a data grab of all the dates
	// when the correct data is found then do the actual data grab for the value
	int nDataSetYear = Integer.parseInt(strStaticDataSet.substring(9,11));
	int nDataSetQuarter = Integer.parseInt(strStaticDataSet.substring(8,9));
	uf.stdoutwriter.writeln("DataSetYear: " + nDataSetYear + " DataSetQuarter: " + nDataSetQuarter);
	int curRowCount=4;
	String ticker;
	try
	{
		query = "select url_dynamic from extract_info where data_set='" + strStaticDataSet + "'";
		ResultSet rs = uf.db_run_query(query);
		rs.next();
		ticker = rs.getString("url_dynamic");
		uf.stdoutwriter.writeln("Processing ticker: " + ticker);
		
	}
	catch(SQLException sqle)
	{
		uf.stdoutwriter.writeln("Problem running query in prenasdaqEPSEst");
		stdoutwriter.writeln(sqle);
		return(false);
	}
	
	while (!done)
	{
		uf.stdoutwriter.writeln("Row Count: " + curRowCount);
		query = "update extract_info set Row_Count=" + curRowCount + ",url_dynamic='" + ticker + "' where data_set='nasdaq_eps_est_quarter'";
		
		try
		{
			uf.db_update_query(query);
		}
		catch (SQLException sqle)
		{
				uf.stdoutwriter.writeln("Problem with update query.");
				uf.stdoutwriter.writeln(sqle);
				return(false);
			
		}
		
		String curValue = dg.get_value("nasdaq_eps_est_quarter");
		if (curValue.length() < 7)
		//string returned too short
			break;
		int nDataValueYear = Integer.parseInt(curValue.substring(5,7));
		uf.stdoutwriter.writeln("DatValueYear: " + nDataValueYear );
		if (nDataValueYear != nDataSetYear)
		{
			curRowCount++;
			continue;
		}
		String nDataValueMonth = curValue.substring(0,3);
		uf.stdoutwriter.writeln("DataValueMonth: " + nDataValueMonth);
		uf.stdoutwriter.writeln("DatValueYear: " + nDataValueYear + " DataValueMonth: " + nDataValueMonth);
		
		if ((nDataValueMonth.compareTo("Nov") == 0) || 
		(nDataValueMonth.compareTo("Dec") == 0) || 
		(nDataValueMonth.compareTo("Jan") == 0))
		{
			if (nDataSetQuarter == 4)
			{
				bSuccess = true;
				break;
			}

		}
		else if ((nDataValueMonth.compareTo("Aug") == 0) || 
		(nDataValueMonth.compareTo("Sep") == 0) || 
		(nDataValueMonth.compareTo("Oct") == 0))
		{
			if (nDataSetQuarter == 3)
			{
				bSuccess = true;
				break;
			}

		}
		else if ((nDataValueMonth.compareTo("May") == 0) || 
		(nDataValueMonth.compareTo("Jun") == 0) || 
		(nDataValueMonth.compareTo("Jul") == 0))
		{
			if (nDataSetQuarter == 2)
			{
				bSuccess = true;
				break;
			}
		
		}
		else if ((nDataValueMonth.compareTo("Feb") == 0) || 
		(nDataValueMonth.compareTo("Mar") == 0) || 
		(nDataValueMonth.compareTo("Apr") == 0))
		{
			if (nDataSetQuarter == 1)
			{
				bSuccess = true;
				break;
			}

		}
		// or if a blank (or some incorrect value) is returned then the end was reached without
		//finding the correct date
		else
			done = true;
			
			
		curRowCount++;
		
		
	}
	if (bSuccess == true)
	{
		try
		{
			uf.db_update_query(query);
			return(true);
		}
		catch (SQLException sqle)
		{
			uf.stdoutwriter.writeln("Problem with update query");
			uf.stdoutwriter.writeln(sqle);
			return(false);
		}
	}
	else
		return(false);
		
	
	
}


public boolean preNasdaqEPS()
{
	try
	{
		String query = "select url_dynamic from extract_info where Data_Set='" + strStaticDataSet + "'";
		ResultSet rs = uf.db_run_query(query);
		rs.next();
		uf.stdoutwriter.writeln("Processing ticker: " + rs.getString("url_dynamic"));
		query = "update extract_info set url_dynamic='" + rs.getString("url_dynamic") + "' where data_set='nasdaq_current_fiscal_year'";
		//have to save the value here because the get_value() call wipes it out.
		String tmpStaticDataSet = strStaticDataSet;
		uf.db_update_query(query);
		String strCurValue = dg.get_value("nasdaq_current_fiscal_year");
		uf.stdoutwriter.writeln("Nasdaq_current_fiscal_year: " + strCurValue);
		int nDataSetYear = Integer.parseInt(tmpStaticDataSet.substring(9,11));
		
		if 	((strCurValue.compareTo("2009") == 0) && (nDataSetYear == 10))
		/* Data not available yet...return false (skip) */
			return false;
	
		
		if ((strCurValue.compareTo("2010") == 0) && (nDataSetYear != 10))
		{
			//need to shift things over one.
			query = "select Cell_Count,url_static from extract_info where Data_Set='" + tmpStaticDataSet + "'";
			uf.stdoutwriter.writeln(query);
			rs = uf.db_run_query(query);
			rs.next();
			int nCellCount = rs.getInt("Cell_Count");
			strTemp2 = String.valueOf(nCellCount);
			strTemp1 = (String)rs.getString("url_static");
			
			if (nDataSetYear == 7)
			{
				query = "update extract_info set url_static='http://fundamentals.nasdaq.com/redpage.asp?page=2&selected=', Cell_Count=2 where Data_Set='" + tmpStaticDataSet + "'";
			}
			else 
			{
				query = "update extract_info set Cell_Count=" + String.valueOf(nCellCount + 1) + " where Data_Set='" + tmpStaticDataSet + "'";
			}
			uf.db_update_query(query);
			
			
		}
		
		return(true);
		
		
		
	}
	catch (SQLException sqle)
	{
		uf.stdoutwriter.writeln("preNasdaqEPS2 failed...");
		stdoutwriter.writeln(sqle);
		return(false);
	}
	
}

/* I think this one is obsolete.
public static void preNasdaqEPS2()
{
	uf.stdoutwriter.writeln("Inside PreNasdaqEPS...");
	strTemp1 = "";
	strTemp2 = "";
	
	//these are the companies that have already released results for the previous qtr
	String[] updated_tickers = {"BA", "AA", "HPQ", "TRV", "DIS"};
	String query = "select url_dynamic,url_static,Cell_Count from extract_info where Data_Set='" + strStaticDataSet + "'";
	
	String strCurTicker = "";
	String strURLStatic = "";
	String strCellCount = "";
	int nYear = Integer.parseInt(strStaticDataSet.substring(10,11));
	int nQuarter = Integer.parseInt(strStaticDataSet.substring(8,9));
	ResultSet rs;
	try
	{
		rs = uf.db_run_query(query);
		rs.next();
		strCurTicker = rs.getString("url_dynamic");
		strURLStatic = rs.getString("url_static");
		strCellCount = rs.getString("Cell_Count");
	}
	catch(SQLException sqle)
	{
		uf.stdoutwriter.writeln("Something wrong with sql in preNasdaqEPS");
		stdoutwriter.writeln((sqle.getStackTrace()).toString());
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
				uf.db_update_query("update extract_info set url_static=http://fundamentals.nasdaq.com/redpage.asp?page=2&selected=, Cell_Count=2 where Data_Set = '" + strStaticDataSet + "'");
			}
			//if year 2008 or 2009, change column
			else if (nYear == 8)
			{
				uf.stdoutwriter.writeln("PreProcessing Data_Set: " + strStaticDataSet + " with ticker " + strCurTicker);
				strTemp2 = strCellCount;
				uf.db_update_query("update extract_info set Cell_Count=4 where Data_Set = '" + strStaticDataSet + "'");
			}
			else if (nYear == 9)
			{
				strTemp2 = strCellCount;
				uf.db_update_query("update extract_info set Cell_Count=3 where Data_Set = '" + strStaticDataSet + "'");
			}
				
				
			
			
		}
	}
	/@
		Now have to add code to handle non-standard fiscal calendars.
		January: DIS, HPQ - Don't have to do anything for Jan, position is the same.
		October: CSCO
		September: MSFT, PG
		April: WMT, JNJ, HD
	
		Normal positionings:
		Q1 - row 9, Q2 - row 14, Q3 - row 19, Q4 - row 24 
		2009 - cell 2, 2008 - cell 3, 2007 - cell 4 
	@/
	int nNewRow, nNewCell;
	String strNewUrl;
	rs = uf.db_run_query("select ticker from company where begin_fiscal_calendar='Oct' OR begin_fiscal_calendar='Sep'");
	/@ Normal Q1 is now Q3 @/
	
	/@if (nYear == 2009)
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
	}@/
	
	
			
			
	
	
		
	
	
	
}*/

public void postNasdaqEPS()
{
	uf.stdoutwriter.writeln("Inside postNasdaqEPS...");
	
	strStaticDataValue = strStaticDataValue.replace("(m)","");
	try
	{
		//restore URL static
		if (strTemp1.compareTo("") != 0)
		{
			uf.db_update_query("update extract_info set url_static='" + strTemp1 + "' where Data_Set = '" + strStaticDataSet + "'");
			strTemp1 = "";
		}
		//restore Cell Count
		if (strTemp2.compareTo("") != 0)
		{
			uf.db_update_query("update extract_info set Cell_Count='" + strTemp2 + "' where Data_Set = '" + strStaticDataSet + "'");
			strTemp2 = "";
		}
	}
	catch (SQLException sqle)
	{
		uf.stdoutwriter.writeln("Problem updating fields for Data Set " + strStaticDataSet + "in postNasdaqEPS! Fields may be corrupted...");
		uf.stdoutwriter.writeln(sqle);
	}
}

public void postNasdaqFiscalYear()
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

public ArrayList<String[]> processNasdaqEPSEstTable(ArrayList<String[]> tabledata,String strDataSet,UtilityFunctions tmpUF)
{
	String[] rowdata,newrow;
	String curCell, strMonth;
	String strTicker;
	int nQuarter=0;
	int nYear;
	uf = tmpUF;
	try
	{
		String query = "select url_dynamic from extract_table where Data_Set='" + strDataSet + "'";
		uf.stdoutwriter.writeln("about to execute query");
		ResultSet rs = uf.db_run_query(query);
		uf.stdoutwriter.writeln("finished executing query");
		rs.next();
		strTicker = rs.getString("url_dynamic");
		
		for (int x=0;x<tabledata.size();x++)
		{
			rowdata = tabledata.get(x);
			newrow = new String[(rowdata.length + 2)*2];
			newrow[0] = "VARCHAR";
			newrow[1] = strTicker;
			newrow[2] = "FUNCTION";
			newrow[3] = "NOW()";
			newrow[4] = "VARCHAR";
			newrow[6] = "INTEGER";
			for (int y=0;y<rowdata.length;y++)
			{
				curCell = rowdata[y];
				curCell = curCell.replace(",","");
	  		curCell = curCell.replace("&nbsp;","");
	 
	  		if (y==0)
	  		//covert month/year to data set
	  		{
	  			strMonth = curCell.substring(0,3);
	  			if ((strMonth.compareTo("Feb") == 0) ||
	  			(strMonth.compareTo("Mar") == 0) ||
	  			(strMonth.compareTo("Apr") == 0))
	  			{
	  				nQuarter = 1;
	  			}
	  			else if ((strMonth.compareTo("May") == 0) ||
	  			(strMonth.compareTo("Jun") == 0) ||
	  			(strMonth.compareTo("Jul") == 0))
	  			{
	  				nQuarter = 2;
	  			}
	  			else if ((strMonth.compareTo("Aug") == 0) ||
	  			(strMonth.compareTo("Sep") == 0) ||
	  			(strMonth.compareTo("Oct") == 0))
	  			{
	  				nQuarter = 3;
	  			}
	  			else if ((strMonth.compareTo("Nov") == 0) ||
	  			(strMonth.compareTo("Dec") == 0) ||
	  			(strMonth.compareTo("Jan") == 0))
	  			{
	  				nQuarter = 4;
	  			}
	  			else
	  			{
	  				uf.stdoutwriter.writeln("Quarter not identified.");
	  			}
	  			nYear = Integer.parseInt(curCell.substring(5,7));
	  			rowdata[y] = "nasdaq_q" + nQuarter + "" + nYear + "_eps_est";
	  				
	  		}
	  		newrow[(y*2)+5] = rowdata[y];
	  		
	  		
	  		
	  		
	  		
			}
			tabledata.set(x,newrow);
		}
	}
	catch (SQLException sqle)
	{
		uf.stdoutwriter.writeln("is this thing being caught?");
		uf.stdoutwriter.writeln("Problem processing table data");
		uf.stdoutwriter.writeln(sqle);
	}
	return(tabledata);
}
		

	
	
	
	
	
}







