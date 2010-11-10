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
	DBFunctions dbf;
	
	
	
	public GarbCollector(DBFunctions dbfparam,String strDay, String strTime, Boolean bNow) throws ParseException
	{
		
		  nDay = Integer.parseInt(strDay);
		  DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		  dateInput = sdf.parse(strTime);
		  bDeleteNow = bNow;
		  dbf = dbfparam;
		  
		 
		
		
		
		
	}
	
	public boolean shouldRun()
	{
		 Calendar calNow = Calendar.getInstance();
		 Calendar calCompare = Calendar.getInstance();
		  
		 calCompare.setTime(dateInput);
		 
		 if ((calNow.get(Calendar.DAY_OF_WEEK) == nDay) && (calNow.after(calCompare)))
		 {
			 return true;
		 }
		 else
			 return false;
	}
	
	public void takeOutTrash() throws SQLException
	{
		
		/*
		 * Data_Set xrateorg
		 */
		int nDaysPrior=3;
		String query = "select primary_key from fact_data where primary_key not in (select primary_key from" +
				" fact_data a, (select max(batch) maxb, ticker from fact_data where" +
				" dateDiff(date_collected,NOW())=-" + nDaysPrior + " and data_set like '%xrateorg%' group by ticker," +
				" HOUR(date_collected)) as b where a.batch=b.maxb and a.ticker=b.ticker) where data_set " +
				"like '%xrateorg%' and dateDiff(date_collected,NOW())=-" + nDaysPrior;
		
		ResultSet rs1 = dbf.db_run_query(query);
		while(rs1.next())
		{
			if (bDeleteNow)
				query = "delete from fact_data where ";
			else
				query = "update fact_data set garbage_collect=1 where ";
			
			query = query + "primary_key='" + rs1.getInt("primary_key") + "'";
			dbf.db_update_query(query);
		}
		
		
		
		
		

	}
	
	
	
	
	
	
	

}
