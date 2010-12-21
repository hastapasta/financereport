import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class GarbCollector {
	
	int nDay;
	Date dateInput;
	
	/*
	 * if this is a 0, then only set the Garbage_Collector column in the fact_data_* table to 1.
	 * If 1, take that sucker out immediately.
	 */
	boolean bDeleteNow;
	//DBFunctions dbf;
	Calendar calNextRunTime;
	
	
    	
	public GarbCollector(String strDay, String strTime, Boolean bNow) throws ParseException
	{
		
	    bDeleteNow = bNow;
		 
		nDay = Integer.parseInt(strDay);
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		dateInput = sdf.parse(strTime);
		Calendar calDateInput = Calendar.getInstance();
		calDateInput.setTime(dateInput);
		 		 
		//dbf = dbfparam;
		calNextRunTime = Calendar.getInstance();
		
		Calendar calForTimeComparison = (Calendar)calNextRunTime.clone();
		calForTimeComparison.set(Calendar.HOUR, calDateInput.get(Calendar.HOUR));
		calForTimeComparison.set(Calendar.MINUTE, calDateInput.get(Calendar.MINUTE));
		calForTimeComparison.set(Calendar.SECOND, calDateInput.get(Calendar.SECOND));
		
		if (nDay == calNextRunTime.get(Calendar.DAY_OF_WEEK))
		{
			if (calNextRunTime.after(calForTimeComparison))
				  calNextRunTime.add(Calendar.WEEK_OF_YEAR,1);
				  
		}
		else 
		{
			if (nDay < calNextRunTime.get(Calendar.DAY_OF_WEEK))
				calNextRunTime.add(Calendar.WEEK_OF_YEAR, 1);
		}
		  
		 
			  
		calNextRunTime.set(Calendar.DAY_OF_WEEK,nDay);
		calNextRunTime.set(Calendar.HOUR,calDateInput.get(Calendar.HOUR));
		calNextRunTime.set(Calendar.MINUTE,calDateInput.get(Calendar.MINUTE));
		calNextRunTime.set(Calendar.SECOND,calDateInput.get(Calendar.SECOND));
		
		 
		
		
		
		
	}
	
	public boolean shouldRun()
	{
		Calendar calNow = Calendar.getInstance();
		//Calendar calCompare = Calendar.getInstance();
		  
		 //calCompare.setTime(dateInput);
		 
		 if ((calNow.after(this.calNextRunTime)))
		 {
			 
			 return true;
		 }
		 else
			 return false;
	}
	
	public void takeOutTrash() throws SQLException
	{
		
		UtilityFunctions.stdoutwriter.writeln("RUNNING GARBAGE COLLECTOR",Logs.STATUS1,"GC2");
		Calendar calBegin = Calendar.getInstance();
		
		/*
		 * Data_Set xrateorg
		 */
		int nDaysPrior=3;
		String query = "select primary_key from fact_data where primary_key not in (select primary_key from" +
				" fact_data a, (select max(batch) maxb, ticker from fact_data where" +
				" dateDiff(date_collected,NOW())<-" + nDaysPrior + " and data_set like '%xrateorg%' group by ticker," +
				" HOUR(date_collected)) as b where a.batch=b.maxb and a.ticker=b.ticker) and data_set " +
				"like '%xrateorg%' and dateDiff(date_collected,NOW())<-" + nDaysPrior;
		
		ResultSet rs1 = DataLoad.dbf.db_run_query(query);
		int nCount=0;
		int nKey;
		ResultSet rs2;
		while(rs1.next())
		{
			nKey = rs1.getInt("primary_key");
			/*
			 * Check if record is referenced in the notify table. If it is, don't remove it.
			 */
			
			query = "select * from notify where fact_data_key=" + nKey;
			
			rs2 = DataLoad.dbf.db_run_query(query);
			
			if (!rs2.next())
			{
			
				if (bDeleteNow)
					query = "delete from fact_data where ";
				else
					query = "update fact_data set garbage_collect=1 where ";
			
				query = query + "primary_key='" + rs1.getInt("primary_key") + "'";
				DataLoad.dbf.db_update_query(query);
				nCount++;
			}
		}
		
		
		

		
		
		/*
		 * Move forward the garbage collection schedule
		 */
		calNextRunTime.add(Calendar.WEEK_OF_YEAR,1);
		
		Calendar calEnd = Calendar.getInstance();
		
		Long lDiff = calEnd.getTimeInMillis() - calBegin.getTimeInMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lDiff);
		
		UtilityFunctions.stdoutwriter.writeln("GARBAGE COLLECTOR COMPLETED RUN",Logs.STATUS1,"GC3");
		UtilityFunctions.stdoutwriter.writeln("GARBAGE COLLECTOR DURATION: " + UtilityFunctions.getElapsedTimeHoursMinutesSecondsString(lDiff),Logs.STATUS1,"GC4");
		
		UtilityFunctions.stdoutwriter.writeln("NUMBER OF RECORDS GARBAGE COLLECTED: " + nCount,Logs.STATUS1,"GC20");
		
		UtilityFunctions.stdoutwriter.writeln("GARBAGE COLLECTOR NEXT SCHEDULED RUN TIME: " + calNextRunTime.getTime().toString(),Logs.STATUS1,"GC21");
		

	}
	
	
	
	
	
	
	

}
