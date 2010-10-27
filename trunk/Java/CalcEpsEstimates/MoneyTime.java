

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class MoneyTime {
	
	String strMonth; //assuming this is the quarter ending month
	String strCalYear;
	Integer nMonth;
	Integer nCalYear;
	
	
	
	/*
	 * The following year property has been adjusted to get the calendar quarters to match up.
	 * This means if the quarter ends on Jan, then 1 is subtracted from the year.
	 */
	Integer nCalAdjYear; 
	Integer nCalQtr;
	
	Integer nFiscalQtr;
	Integer nFiscalYear;
	String strFiscalQtr;
	String strFiscalYear;
	

	//Integer nAdjustedQuarter;
	
	UtilityFunctions uf;
	
	public MoneyTime(String strTmpMonth, String strTmpCalYear, String strTicker) throws SQLException
	{
		strMonth = strTmpMonth;
		if (strTmpCalYear.length() == 2)
			strCalYear = "20" + strTmpCalYear;
		else 
			strCalYear = strTmpCalYear;
		
		nMonth = convertMonthStringtoInt(strMonth);
		nCalYear = Integer.parseInt(strCalYear);
		
		String tmpStr = this.getFiscalYearAndQuarter(strTicker, nMonth, nCalYear);
		nFiscalQtr = Integer.parseInt(tmpStr.substring(0,1));
		nFiscalYear = Integer.parseInt(tmpStr.substring(1,5));
		strFiscalQtr = Integer.toString(nFiscalQtr);
		strFiscalYear = Integer.toString(nFiscalYear);
		
		nCalAdjYear = nCalYear;
		nCalQtr = this.retrieveCalQuarter(nMonth);
		//nAdjustedQuarter = this.retrieveAdjustedQuarter(nFiscalQtr, nFiscalYear, strTicker);	
		
	}
	
	public static int convertMonthStringtoInt(String strMonth)
	{
		strMonth = strMonth.toUpperCase();
		
		if (strMonth.substring(0,3).equals("JAN")) return(1);
		else if (strMonth.substring(0,3).equals("FEB")) return(2);
		else if (strMonth.substring(0,3).equals("MAR")) return(3);
		else if (strMonth.substring(0,3).equals("APR")) return(4);
		else if (strMonth.substring(0,3).equals("MAY")) return(5);
		else if (strMonth.substring(0,3).equals("JUN")) return(6);
		else if (strMonth.substring(0,3).equals("JUL")) return(7);
		else if (strMonth.substring(0,3).equals("AUG")) return(8);
		else if (strMonth.substring(0,3).equals("SEP")) return(9);
		else if (strMonth.substring(0,3).equals("OCT")) return(10);
		else if (strMonth.substring(0,3).equals("NOV")) return(11);
		else if (strMonth.substring(0,3).equals("DEC")) return(12);
		else
		{
			UtilityFunctions.stdoutwriter.writeln("Unable to match month string.", Logs.ERROR,"PF42");
			return(0);
		}
		
		
	}
	
	public static String convertMonthInttoString(int nMonth)
	{
		if (nMonth==1)
			return "January";
		else if (nMonth==2)
			return "February";	
		else if (nMonth==3)
			return "March";
		else if (nMonth==4)
			return "April";
		else if (nMonth==5)
			return "May";
		else if (nMonth==6)
			return "June";
		else if (nMonth==7)
			return "July";
		else if (nMonth==8)
			return "Aurgust";
		else if (nMonth==9)
			return "September";
		else if (nMonth==10)
			return "October";
		else if (nMonth==11)
			return "November";
		else if (nMonth==12)
			return "December";
		else 
		{
			UtilityFunctions.stdoutwriter.writeln("Invalid month integer value", Logs.ERROR,"PF42");
			return "";
		}
	}
	
	public int retrieveCalQuarter(int nMonth)
	{
		if ((2 <= nMonth) && (nMonth <= 4))
			return(1);
		else if ((5 <= nMonth) && (nMonth <=7))
			return(2);
		else if ((8 <= nMonth) && (nMonth <= 10))
			return(3);
		else
		{
			/*
			 * Jan is boundary condition
			 */
			if (nMonth == 1)
				this.nCalAdjYear = nCalYear - 1;
			return(4);
		}
	}
	
	
	public int retrieveAdjustedQuarter(int fiscalquarter,int fiscalyear, String strTicker)
	{
		/*Adjusted quarter starts with the first quarter of calendar year 2000 as being 1*/
		
		/*May need to review this calculation*/
		String query="select begin_fiscal_calendar, company.ticker,";
	    query = query + " (case when begin_fiscal_calendar in ('February','March','April') then 3";
	    query = query + " when begin_fiscal_calendar in ('May','June','July') then 2 when";
	    query = query + " begin_fiscal_calendar in ('August','September','October') then 1";
	    query = query + " else 0 end) as qtr_code_adjusted from company where";
	    query = query + " company.ticker='" + strTicker + "'";
		
	    
	    Integer unadjustedqtr;
	    if (fiscalyear > 100)
	    	unadjustedqtr = (4*(fiscalyear-2000)) + fiscalquarter;
	    else
	    	unadjustedqtr = (4*fiscalyear) + fiscalquarter;
	    
		Integer adjustment_code = 0;
		
		try
		{
			ResultSet rs = UtilityFunctions.db_run_query(query);
			rs.next();
			adjustment_code = rs.getInt("qtr_code_adjusted");
			
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem adjusting quarter value",Logs.ERROR,"UF24");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
		}
		
		return(unadjustedqtr-adjustment_code);
	    
	 
	    
		
		
	}
	

	
	public static String getCalendarYearAndQuarter(String strTicker, int fiscalquarter, int fiscalyear) throws SQLException
	{
		int calquarter=0;
		int calyear=fiscalyear;
		String query = "select begin_fiscal_calendar from company where ticker='" + strTicker +"'";
		ResultSet rs = UtilityFunctions.db_run_query(query);
		rs.next();
		int nBeginFiscalYear = convertMonthStringtoInt(rs.getString("begin_fiscal_calendar"));
		
		if (nBeginFiscalYear==12 || nBeginFiscalYear==1 || nBeginFiscalYear==2)
		{
			if (nBeginFiscalYear==2)
				calyear--;
			calquarter = fiscalquarter;
		}
		else if (nBeginFiscalYear==3 || nBeginFiscalYear==4 || nBeginFiscalYear==5)
		{
			if (fiscalquarter != 4)
				calyear--;
			calquarter = fiscalquarter+1;
		}
		else if (nBeginFiscalYear==6 || nBeginFiscalYear==7 || nBeginFiscalYear==8)
		{
			if (fiscalquarter != 4 && fiscalquarter != 3)
				calyear--;
			calquarter = fiscalquarter+2;
		}
		else if (nBeginFiscalYear==9 || nBeginFiscalYear==10 || nBeginFiscalYear==11)
		{
			if (fiscalquarter ==1)
				calyear--;
			calquarter = fiscalquarter+3;
			
		}
		else
			UtilityFunctions.stdoutwriter.writeln("Begin fiscal year month string not found",Logs.ERROR,"UF24.5");
			
		if (calquarter != 4)
			calquarter = calquarter % 4;
		
		return calquarter + "" + calyear;
	}
	
	public static String getFiscalYearAndQuarter(String strTicker, int curmonth, int curyear) throws SQLException
	{
		/*NOTE: If a 2 digit year is submitted, a 2 digit year is returned. 
		 *  4 digit year submitted, 4 digits returned.
		 * 
		 * Need to do a calculation to figure out the current fiscal quarter
		 * 
		 * cur month = 10, begin month = 1, then cur fiscal quarter is 4, fiscal year same as calendar year
		 * cur month = 5, begin month = 6, the cur quarter is 4, 
		 * cur month = 1, begin month = 6 then cur quarter is 3
		 * cur month = 1, begin month = 12
		 * cur month = 10, begin month = 5, the cur fiscal quarter is 2
		 * 
		 * (cur month - begin month) = x
		 * if x>0   (x div 3) + 1
		 * if x<0   ((12 + x) div 3) + 1
		 * 
		 * calculation for current year
		 * 
		 * begin fiscal calendar = 1 (boundary condition) fiscal year always = current year
		 * 
		 * other wise if current fiscal quarter <= current calendar quarter fiscal year = next year
		 * else
		 * current year 
		 * 
		 */
		//try
		//{
			Integer nBeginFiscalYear=0;
			String query = "select begin_fiscal_calendar from company where ticker='" + strTicker +"'";
			ResultSet rs = UtilityFunctions.db_run_query(query);
			rs.next();
			String strBeginFiscalYear = rs.getString("begin_fiscal_calendar");
			Calendar cal = Calendar.getInstance(); 
			if (curmonth == -1)
			/* if curmonth = -1, get month and year from current data, otherwise use parameter values */
			{
				curmonth = cal.get(Calendar.MONTH) + 1;      
				curyear = cal.get(Calendar.YEAR);
			}
			
			nBeginFiscalYear = convertMonthStringtoInt(strBeginFiscalYear);
		
			Integer nCurFiscalQuarter, nCurFiscalYear;
			if ((curmonth - nBeginFiscalYear)>=0)
				nCurFiscalQuarter = ((curmonth - nBeginFiscalYear) / 3) + 1;
			else
				nCurFiscalQuarter = ((12 + (curmonth - nBeginFiscalYear)) / 3) + 1;
			
			if (nBeginFiscalYear == 1)
			//Boundary condition if begin fiscal year is january, fiscal year is always the same as the current year
				nCurFiscalYear = curyear;
			else if ((curmonth - nBeginFiscalYear) >= 0)
				nCurFiscalYear = curyear + 1;
			else
				nCurFiscalYear = curyear;
			
			String retval = Integer.toString(nCurFiscalQuarter) + Integer.toString(nCurFiscalYear);
			return(retval);
			
			
			
		//}
		/*catch (SQLException sqle)
		{
			stdoutwriter.writeln("Problem with query",Logs.ERROR);
			stdoutwriter.writeln(sqle);
			return("000000");
		}*/
		
		
		
		
		
	}
	
	
	

}
