
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;
import java.math.BigDecimal;
import org.apache.log4j.NDC;

public class CalcEstimates 
{

	/**
	 * @param args
	 */
	static UtilityFunctions uf;
	static String runtoticker="";
	static int nBatch;
	static Vector<String> vMetaDataSet = new Vector<String>();
	
	public static void main(String[] args)
	{
		
		
		
		try
		{
			uf = new UtilityFunctions("findata","root","madmax1.","full.log","error.log","sql.log","thread.log");
			
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
				//query = "select * from company where groups like '%sandp%' order by ticker ASC";
				
				NDC.push("Year:" + nProcessYear);
				
				query = "select entities.* from entities,entity_groups,entities_entity_groups where entity_groups.name='sandp' and entities_entity_groups.entity_group_id=entity_groups.id and entities.id=entities_entity_groups.entity_id";
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
					
					UtilityFunctions.stdoutwriter.writeln("Processing ticker " + rs.getString("ticker"),Logs.STATUS2,"CE9.4");
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
			
			NDC.pop();
			}
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem issuing sql statement",Logs.ERROR,"CE10");
			UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		

		updateMetaDataSet();

	

	}
	
	
	public static void readAnnualEstimates(String strTicker, String strBeginFiscalCalendar, int nProcessYear) throws SQLException
	{
		/*String query = "select count(*) from fact_data where data_set='table_yahoo_y_eps_est_body' AND ticker='" + 
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
		}*/
			
		
		
		String query;
		ResultSet rs2;
		int nFetchSize;
		

		
		//query = "select count(*) from tmp_eps_est where ticker='" + strTicker + "' AND fiscalyear=" + nProcessYear;
		
		/*String query = "select count(*) from fact_data where ticker='"+ strTicker + "' AND fiscalyear=" + nProcessYear + 
			" AND data_set IN ('table_yahoo_q_eps_est_body','table_nasdaq_q_eps_body','table_nasdaq_q_eps_est_body')";
		ResultSet rs2 = UtilityFunctions.db_run_query(query);
		rs2.next();
		int nFetchSize = rs2.getInt("count(*)");*/
		
		
		
		//query = "select * from tmp_eps_est where ticker='" + strTicker + "' AND fiscalyear=" + nProcessYear;
		/*query = "select * from fact_data where ticker='"+ strTicker + "' AND fiscalyear=" + nProcessYear + 
		" AND data_set IN ('table_yahoo_q_eps_est_body','table_nasdaq_q_eps_body','table_nasdaq_q_eps_est_body')";*/
		
		
		
		java.math.BigDecimal[] arrayExistQuarters = {null,null,null,null};
		//long[] lMissingQuarters = {0,0,0,0};
		boolean[] bMissQuarters = {true,true,true,true};
		//boolean bNoMissedQuarters = true;
		int nMissingQuarters=0;
		
		
		
		for (int nQtr=1; nQtr<=4;nQtr++)
		{
			/*
			 * Try to select an actual. We're using a MAX here to take the latest one but there obviously should only
			 * be one actual.
			 * UPDATE: I was using the max function, but it returns NULL if there are no rows selected, so I switched to an order by and we'll
			 * pick the first row.
			 * UPDATE: now using a priority value to determine the ordering of the values.
			 */
			/*
			 * I SHOULD change this code to filter by data_group and not by actual data_set in case I change the source from
			 * where we pull values.
			 */
			/*query = "select fd.date_collected,fd.data_set,fd.value,fd.ticker,fd.primary_key,ji.data_set_alias from fact_data fd,job_info ji where ticker='" + strTicker + "' AND fiscalyear=" + 
			nProcessYear + " AND fiscalquarter=" + nQtr + " AND fd.data_set=ji.data_set " +
			" AND fd.data_set IN ('table_nasdaq_q_eps_body','table_google_q_eps_excxtra_body') ORDER BY ji.data_set_alias ASC, date_collected DESC";*/
			
			
			query = "select fact_data.date_collected,tasks.name,fact_data.value,entities.ticker,fact_data.id,tasks.eps_est_priority from fact_data,tasks,entities " + 
					"where entities.ticker='" + strTicker + "' AND fiscalyear=" + nProcessYear + " AND fiscalquarter=" + nQtr + " AND tasks.name IN ('nasdaq_q_eps','google_q_eps_excxtra') AND " +
					"fact_data.entity_id=entities.id AND fact_data.task_id=tasks.id order by tasks.eps_est_priority, fact_data.date_collected DESC";
			
			rs2 = UtilityFunctions.db_run_query(query);
			if (rs2.next())
			{
				vMetaDataSet.add(rs2.getInt("fact_data.id")+ "");
				arrayExistQuarters[nQtr-1] = rs2.getBigDecimal("value");
				bMissQuarters[nQtr-1] = false;
		
			}
			else
			{
				/*
				 * Try to select an estimate. We're using a MAX here to select the latest estimate.
				 * Update: see above.
				 */
				/*query = "select fd.date_collected,fd.data_set,fd.value,fd.ticker,fd.primary_key,ji.data_set_alias from fact_data fd, job_info ji where ticker='" + strTicker + "' AND fiscalyear=" + 
				nProcessYear + " AND fiscalquarter=" + nQtr + " AND fd.data_set=ji.data_set " +
				" AND fd.data_set IN ('table_nasdaq_q_eps_est_body','table_mwatch_q_eps_est') order by ji.data_set_alias ASC, date_collected DESC";*/
				
				query = "select fact_data.date_collected,tasks.name,fact_data.value,entities.ticker,fact_data.id,tasks.eps_est_priority from fact_data,tasks,entities " + 
				"where entities.ticker='" + strTicker + "' AND fiscalyear=" + nProcessYear + " AND fiscalquarter=" + nQtr + " AND tasks.name IN ('nasdaq_q_eps_est','marketwatch_q_eps_est') AND " +
				"fact_data.entity_id=entities.id AND fact_data.task_id=tasks.id order by tasks.eps_est_priority, fact_data.date_collected DESC";
				
				rs2 = UtilityFunctions.db_run_query(query);
				if (rs2.next())
				{
					vMetaDataSet.add(rs2.getInt("fact_data.id")+ "");
					arrayExistQuarters[nQtr-1] = rs2.getBigDecimal("value");
					bMissQuarters[nQtr-1] = false;
			
				}
				else
				{
					nMissingQuarters++;
				}
				
				
				
			}
				
			
			
			
			
			
		}
		
		/*
		 * No quarters for this fiscal year are missing data. No need to calculate.
		 */
		if (nMissingQuarters==0)
		{
			UtilityFunctions.stdoutwriter.writeln("No missing data",Logs.STATUS2,"CE9.75");
			return;
		}
		
		
		/*
		 * I moved this down here because I want to know if there 
		 * are missing quarters AND we have no annual estimate.
		 */
		/*query = "select count(*) from fact_data where data_set in ('table_nasdaq_y_eps_est_body','table_mwatch_y_eps_est') AND ticker='" + 
		strTicker + "' AND fiscalyear=" + nProcessYear;*/
		
		query = "select count(*) from fact_data,tasks,entities where tasks.name in ('nasdaq_y_eps_est','marketwatch_y_eps_est') AND entities.ticker='" + 
		strTicker + "' AND fact_data.fiscalyear=" + nProcessYear + " AND fact_data.entity_id=entities.id AND fact_data.task_id=tasks.id";
		ResultSet rs = UtilityFunctions.db_run_query(query);
		rs.next();
		int nCountYearEstimates = rs.getInt("count(*)");
		if (nCountYearEstimates == 0)
		{
			UtilityFunctions.stdoutwriter.writeln(strTicker + " PROBLEM: THERE IS MISSING QUARTERLY DATA AND WE HAVE NO ANNUAL ESTIMATE FROM WHICH TO CALCULATE.",Logs.STATUS1,"CE20");
			return;
		}

		
		
		/*query = "select date_collected,value from fact_data fd, job_info ji" +
		" where fd.data_set in ('table_nasdaq_y_eps_est_body','table_mwatch_y_eps_est') AND fd.data_set=ji.data_set AND ticker='" + 
		strTicker + "' AND fiscalyear=" + nProcessYear + " ORDER BY ji.data_set_alias ASC, date_collected DESC";*/
		
		query = "select fact_data.date_collected,fact_data.value from fact_data,tasks,entities " + 
		"where entities.ticker='" + strTicker + "' AND fiscalyear=" + nProcessYear + " AND tasks.name IN ('nasdaq_y_eps_est','marketwatch_y_eps_est') AND " +
		"fact_data.entity_id=entities.id AND fact_data.task_id=tasks.id order by tasks.eps_est_priority, fact_data.date_collected DESC";
		
		
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
	
			
		
		
		
		
		
		java.math.BigDecimal dAdjustedAnnualEst = dAnnualEstimate;
		

		for (int i=0;i<4;i++)
		{
			if (arrayExistQuarters[i]!=null)
				dAdjustedAnnualEst = dAdjustedAnnualEst.subtract(arrayExistQuarters[i]);
		}
		
		BigDecimal dAdjustedQuarterEst = dAdjustedAnnualEst.divide(new BigDecimal(nMissingQuarters),BigDecimal.ROUND_HALF_UP);
		
		
		
		
		
		
		
		
		for(int i=0;i<4;i++)
		{
			String strQuarterYear = MoneyTime.getCalendarYearAndQuarter(strTicker,i+1,nProcessYear);
			
			/* May want to add code here to not insert estimates for quarters that are in the past */
			
			String query2 = "select id from entities where ticker='" + strTicker + "'";	
			ResultSet rs3 = UtilityFunctions.db_run_query(query2);
			rs3.next();
			int nEntityId=rs3.getInt("id");
			
			query2 = "select id from tasks where name='calc_q_eps_est'";
			rs3 = UtilityFunctions.db_run_query(query2);
			rs3.next();
			int nTaskId = rs3.getInt("id");
			
			
			if (bMissQuarters[i] == true)
			{
				query = "insert into fact_data_stage_est " +
				"(value,entity_id,task_id,fiscalquarter,fiscalyear,batch,date_collected,data_group,calquarter,calyear,meta_data_set) values" +
				"(" + dAdjustedQuarterEst + "," +
				nEntityId + "," + 
				nTaskId + "," +
				(i+1) + "," +
				nProcessYear + "," +
				nBatch + "," +
				"NOW()," +
				"'eps_est'," +
				strQuarterYear.substring(0,1) + "," +
				strQuarterYear.substring(1,5) + "," +
				"'eps_act_and_est')";
			
				UtilityFunctions.db_update_query(query);
				
				UtilityFunctions.stdoutwriter.writeln(strTicker + ": Generated estimate for fiscalyear: " + nProcessYear + " fiscalquarter: " + (i + 1),Logs.STATUS1,"CE44");
			}
			
		}
		


		
	}
	
	public static void updateMetaDataSet()
	{
		String strUpdate;
		
		UtilityFunctions.stdoutwriter.writeln("Attempting to update meta_data_set field for " + vMetaDataSet.size() + " records.",Logs.STATUS1,"CE44.5");
		
		for (int i=0;i<vMetaDataSet.size();i++)
		{
			try
			{
				strUpdate = "update fact_data set meta_data_set='eps_act_and_est' where id=" + vMetaDataSet.get(i);
				UtilityFunctions.db_update_query(strUpdate);
			}
			catch(SQLException sql)
			{
				UtilityFunctions.stdoutwriter.writeln("Update meta_data_set failed for primary_key: " + vMetaDataSet.get(i),Logs.ERROR,"CE44.5");
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
		//query = query + "select batch from fact_data_stage union all ";
		query = query + "select batch from fact_data_stage_est) t1";
		ResultSet rs = UtilityFunctions.db_run_query(query);
		rs.next();
		return (rs.getInt("max(batch)") + 1);
			
	
		
	}
	
	public static void cleanData()
	{
		/*
		 *  Remove all duplicate 
		 */
		
		
		
		
	
	}

}
