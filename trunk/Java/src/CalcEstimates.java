
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.math.BigDecimal;

public class CalcEstimates 
{

	/**
	 * @param args
	 */
	static UtilityFunctions uf;
	static String runtoticker="";
	static int nBatch;
	
	public static void main(String[] args)
	{
		
		
		
		try
		{
			uf = new UtilityFunctions("mydb","root","madmax1.","full.log","error.log","sql.log","thread.log");
			
			String query = "select max(fiscalyear) from fact_data";
			ResultSet rs = UtilityFunctions.db_run_query(query);
			rs.next();
			int nMaxYear = rs.getInt("max(fiscalyear)");
			
			Calendar cal = Calendar.getInstance();
			int nCurYear = cal.get(Calendar.YEAR);
			
			
			
			
			nBatch = getBatchNumber();
			
			for (int nProcessYear=nCurYear;nProcessYear<=nMaxYear;nProcessYear++)
			{
				UtilityFunctions.stdoutwriter.writeln("PROCESSING YEAR " + nProcessYear,Logs.STATUS1,"CE8.5");
				query = "select * from company where groups like '%sandp%'";
				rs = UtilityFunctions.db_run_query(query);
			while(rs.next())
			{
				
				/*
				 * -read in the fiscal annual estimates
				*/
				try
				{
					String strTicker = rs.getString("ticker");
					String strBeginFiscalCal = rs.getString("begin_fiscal_calendar");
					
					System.out.println("Processing ticker " + rs.getString("ticker"));
					if (!runtoticker.isEmpty())
						if (!runtoticker.equals(rs.getString("ticker")))
							continue;
					
					
					readAnnualEstimates(strTicker,strBeginFiscalCal,nProcessYear);
				}
				catch (SQLException sqle)
				{
					UtilityFunctions.stdoutwriter.writeln("Problem issuing sql statement",Logs.ERROR,"CE9.5");
					UtilityFunctions.stdoutwriter.writeln(sqle);
				}
				
				
				
				/* -convert to calendar quarterly estimates
				 * -subtract out the existing known estimates
				 * -calculate the cyclicality
				 * -calculate the missing quarterly estimates
				 * -write new estimates into fact_data_stage_est
				 */
			}
			}
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem issuing sql statement",Logs.ERROR,"CE10");
			UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		

	}
	
	
	public static void readAnnualEstimates(String strTicker, String strBeginFiscalCalendar, int nProcessYear) throws SQLException
	{
		String query = "select count(*) from view_fact_data where data_set='table_yahoo_y_eps_est_body' AND ticker='" + 
		strTicker + "' AND fiscalyear=" + nProcessYear;
		ResultSet rs = UtilityFunctions.db_run_query(query);
		rs.next();
		int nFetchSize = rs.getInt("count(*)");
		if (nFetchSize > 1)
		{
			UtilityFunctions.stdoutwriter.writeln("There is more than one row of annual estimates for ticker " + strTicker + " and fiscal year " + nProcessYear,Logs.STATUS1,"CE20");
			UtilityFunctions.stdoutwriter.writeln("Need to clean up the data",Logs.STATUS1,"CE20.1");
			return;
		}
		else if (nFetchSize == 0)
		{
			UtilityFunctions.stdoutwriter.writeln(strTicker + ": No annual estimates, skipping",Logs.STATUS1,"CE20");
			return;
		}
			
		
		
		
		query = "select * from view_fact_data where data_set='table_yahoo_y_eps_est_body' AND ticker='" + 
		strTicker + "' AND fiscalyear=" + nProcessYear;
		rs = UtilityFunctions.db_run_query(query);
		
		/*if (rs.getFetchSize()>1)
		{
			UtilityFunctions.stdoutwriter.writeln("There is more than one row of annual estimates for ticker " + strTicker,Logs.ERROR,"CE20");
			UtilityFunctions.stdoutwriter.writeln("Need to clean up the data",Logs.ERROR,"CE20.1");
		}*/
		rs.next();
		//java.math.BigDecimal dAnnualEstimate = rs.getBigDecimal("value");
		//String strValue = rs.getString("value");
		java.math.BigDecimal dAnnualEstimate = rs.getBigDecimal("value");

		
		query = "select count(*) from tmp_eps_est where ticker='" + strTicker + "' AND fiscalyear=" + nProcessYear;
		ResultSet rs2 = UtilityFunctions.db_run_query(query);
		rs2.next();
		nFetchSize = rs2.getInt("count(*)");
		
		query = "select * from tmp_eps_est where ticker='" + strTicker + "' AND fiscalyear=" + nProcessYear;
		rs2 = UtilityFunctions.db_run_query(query);
		
		if (nFetchSize > 4)
		{
			UtilityFunctions.stdoutwriter.writeln("There are more than 4 quarterly estimates for fisal year " + nProcessYear + " and ticker " + strTicker,Logs.STATUS1,"CE20.5");
			UtilityFunctions.stdoutwriter.writeln("Need to clean up the data",Logs.STATUS1,"CE21.5");
			return;
		} 
		
		
		java.math.BigDecimal[] arrayExistQuarters = {null,null,null,null};
		//long[] lMissingQuarters = {0,0,0,0};
		boolean[] bMissQuarters = {true,true,true,true};
		int nCount=0;
		
		while (rs2.next())
		{
			nCount++;
			
			//may have to add code to check for duplicates
			if (rs2.getInt("fiscalquarter")==1)
			{
				if (arrayExistQuarters[0]==null)
				{
					arrayExistQuarters[0] = rs2.getBigDecimal("value");
					bMissQuarters[0] = false;
				}
				else
				{
					UtilityFunctions.stdoutwriter.writeln("Duplicate Quarters of data for ticker " + strTicker,Logs.ERROR,"CE40");
					return;
				}
			}
			else if (rs2.getInt("fiscalquarter")==2)
			{
				if (arrayExistQuarters[1]==null)
				{
					arrayExistQuarters[1] = rs2.getBigDecimal("value");
					bMissQuarters[1] = false;
					
				}
				else
				{
					UtilityFunctions.stdoutwriter.writeln("Duplicate Quarters of data for ticker " + strTicker,Logs.ERROR,"CE41");
					return;
				}
			}
			else if (rs2.getInt("fiscalquarter")==3)
			{
				if (arrayExistQuarters[2]==null)
				{
					arrayExistQuarters[2] = rs2.getBigDecimal("value");
					bMissQuarters[2] = false;
			
				}
				else
				{
					UtilityFunctions.stdoutwriter.writeln("Duplicate Quarters of data for ticker " + strTicker,Logs.ERROR,"CE42");
					return;
				}
			}
			else if (rs2.getInt("fiscalquarter")==4)
			{
				if (arrayExistQuarters[3]==null)
				{
					arrayExistQuarters[3] = rs2.getBigDecimal("value");
					bMissQuarters[3] = false;
					
				}
				else
				{
					UtilityFunctions.stdoutwriter.writeln("Duplicate Quarters of data for ticker " + strTicker,Logs.ERROR,"CE43");
					return;
				}
			}
				
		}
		
		//Checking for a fetchsize of 4 here to give the while loop above a chance to
		//check for duplicates.
		if (nFetchSize==4)
			return;
		
		java.math.BigDecimal dAdjustedAnnualEst = dAnnualEstimate;
		

		for (int i=0;i<4;i++)
		{
			if (arrayExistQuarters[i]!=null)
				dAdjustedAnnualEst = dAdjustedAnnualEst.subtract(arrayExistQuarters[i]);
		}
		
		BigDecimal dAdjustedQuarterEst = dAdjustedAnnualEst.divide(new BigDecimal(4-nFetchSize),BigDecimal.ROUND_HALF_UP);
		
		
		UtilityFunctions.stdoutwriter.writeln(strTicker + ": Generating estimates for " + (4-nFetchSize) + " missing quarters",Logs.STATUS1,"CE44");
		
		
		
		
		
		for(int i=0;i<4;i++)
		{
			String strQuarterYear = MoneyTime.getCalendarYearAndQuarter(strTicker,i+1,nProcessYear);
			
			/* May want to add code here to not insert estimates for quarters that are in the past */
			
			if (bMissQuarters[i] == true)
			{
				query = "insert into fact_data_stage_est " +
				"(value,ticker,data_set,fiscalquarter,fiscalyear,batch,date_collected,data_group,calquarter,calyear) values" +
				"(" + dAdjustedQuarterEst + "," +
				"'" + strTicker + "'," +
				"'calc_yahoo_q_eps_est'," +
				(i+1) + "," +
				nProcessYear + "," +
				nBatch + "," +
				"NOW()," +
				"'eps_est'," +
				strQuarterYear.substring(0,1) + "," +
				strQuarterYear.substring(1,5) + ")";
				UtilityFunctions.db_update_query(query);
			}
			
		}
		


		
	}
	
	public static Integer getBatchNumber() throws SQLException
	{
		/*
		 * Get the current highest batch number from both fact_data & fact_data_stage and increase that by 1
		 */

		
		String query = "select max(batch) from ";
		query = query + "(select batch from fact_data union all ";
		query = query + "select batch from fact_data_stage union all ";
		query = query + "select batch from fact_data_stage_est) t1";
		ResultSet rs = UtilityFunctions.db_run_query(query);
		rs.next();
		return (rs.getInt("max(batch)") + 1);
			
	
		
	}

}
