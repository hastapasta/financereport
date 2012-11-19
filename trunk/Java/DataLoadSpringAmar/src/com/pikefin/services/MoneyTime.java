package com.pikefin.services;

import java.util.Calendar;

import pikefin.log4jWrapper.Logs;

import com.pikefin.ApplicationSetting;
import com.pikefin.businessobjects.Entity;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.EntityService;


public class MoneyTime {
	
	EntityService entityService;
	String strMonth; //assuming this is the quarter ending month
	String strCalYear;
	Integer nMonth;
	Integer nCalYear;
	Integer nCalAdjYear; 
	Integer nCalQtr;
	Integer nFiscalQtr;
	Integer nFiscalYear;
	String strFiscalQtr;
	String strFiscalYear;
	

	
	public MoneyTime(String strTmpMonth, String strTmpCalYear, String strTicker) throws GenericException {
		strMonth = strTmpMonth;
		if (strTmpCalYear.length() == 2)
			strCalYear = "20" + strTmpCalYear;
		else 
			strCalYear = strTmpCalYear;
		nMonth = convertMonthStringtoInt(strMonth);
		nCalYear = Integer.parseInt(strCalYear);
		String tmpStr = MoneyTime.getFiscalYearAndQuarter(strTicker, nMonth, nCalYear);
		nFiscalQtr = Integer.parseInt(tmpStr.substring(0,1));
		nFiscalYear = Integer.parseInt(tmpStr.substring(1,5));
		strFiscalQtr = Integer.toString(nFiscalQtr);
		strFiscalYear = Integer.toString(nFiscalYear);
		
		nCalAdjYear = nCalYear;
		nCalQtr = this.retrieveCalQuarter(nMonth);
		
	}
	
	public static int convertMonthStringtoInt(String strMonth) {
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
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Unable to match month string.", Logs.ERROR,"MT2");
			return(0);
		}
		
		
	}
	
	public static String convertMonthInttoString(int nMonth)	{
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
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Invalid month integer value", Logs.ERROR,"MT3");
			return "";
		}
	}
	
	public int retrieveCalQuarter(int nMonth)	{
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
	
	
	public int retrieveAdjustedQuarter(int fiscalquarter,int fiscalyear, String strTicker)	throws GenericException{
	 
	    Entity e =entityService.loadEntityInfoByTicker(strTicker) ;
		Integer adjustment_code = 0;
	    if (e.getBeginFiscalCalendar().equalsIgnoreCase("February") ||
	    		e.getBeginFiscalCalendar().equalsIgnoreCase("March") ||
	    		e.getBeginFiscalCalendar().equalsIgnoreCase("April"))
	    	adjustment_code = 3;
	    else if (e.getBeginFiscalCalendar().equalsIgnoreCase("May") ||
	    		e.getBeginFiscalCalendar().equalsIgnoreCase("June") ||
	    		e.getBeginFiscalCalendar().equalsIgnoreCase("July"))
	    	adjustment_code = 2;
	    else if (e.getBeginFiscalCalendar().equalsIgnoreCase("August") ||
	    		e.getBeginFiscalCalendar().equalsIgnoreCase("September") ||
	    		e.getBeginFiscalCalendar().equalsIgnoreCase("October"))
	    	adjustment_code = 1;
	    else if (e.getBeginFiscalCalendar().equalsIgnoreCase("November") ||
	    		e.getBeginFiscalCalendar().equalsIgnoreCase("December") ||
	    		e.getBeginFiscalCalendar().equalsIgnoreCase("January"))
	    	adjustment_code = 0;
	    else {
	    	ApplicationSetting.getInstance().getStdoutwriter().writeln("Invalid begin fiscal calendar month", Logs.ERROR,"MT1");
	    }
	    	
	    
	    
	 
		
	    
	    Integer unadjustedqtr;
	    if (fiscalyear > 100)
	    	unadjustedqtr = (4*(fiscalyear-2000)) + fiscalquarter;
	    else
	    	unadjustedqtr = (4*fiscalyear) + fiscalquarter;
	    
		
		return(unadjustedqtr-adjustment_code);
	
		
	}
	

	
	static EntityService entityServiceStatic=ApplicationSetting.getInstance().getApplicationContext().getBean(EntityService.class);
	public static String getCalendarYearAndQuarter(String strTicker, int fiscalquarter, int fiscalyear) throws GenericException	{
		int calquarter=0;
		int calyear=fiscalyear;
		Entity e = entityServiceStatic.loadEntityInfoByTicker(strTicker) ;
		int nBeginFiscalYear = convertMonthStringtoInt(e.getBeginFiscalCalendar());
		
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
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Begin fiscal year month string not found",Logs.ERROR,"UF24.5");
			
		if (calquarter != 4)
			calquarter = calquarter % 4;
		
		return calquarter + "" + calyear;
	}
	
	public static String getFiscalYearAndQuarter(String strTicker, int curmonth, int curyear) throws GenericException {
			Integer nBeginFiscalYear=0;
			Entity e = entityServiceStatic.loadEntityInfoByTicker(strTicker) ;
			String strBeginFiscalYear = e.getBeginFiscalCalendar();
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
			
		}
	
	
	

}
