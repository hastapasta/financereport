
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
	static boolean bFirstUpdate=true;
	
	public static void main(String[] args)
	{
		
		
		
		try
		{
			uf = new UtilityFunctions("localhost", "findata","root","madmax1.","full.log","error.log","sql.log","thread.log");
			
			String query = "select max(fiscalyear) from fact_data";
			ResultSet rs = UtilityFunctions.db_run_query(query);
			rs.next();
			
			int nMaxYear = rs.getInt("max(fiscalyear)");
			
			Calendar cal = Calendar.getInstance();
			
			int nCurYear = cal.get(Calendar.YEAR);
			
			initializeMetaSetId(nCurYear);
			
			
			
			
			//nBatch = getBatchNumber();
			nBatch = uf.insertBatchesEntry(-1);
			
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

		
		
		String query;
		ResultSet rs2;
		int nFetchSize;
		
		
		
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
			
			
			query = "select fact_data.date_collected,tasks.name,fact_data.value,entities.ticker,fact_data.id,tasks.eps_est_priority ";
			query += " from fact_data ";
			query += " JOIN batches on fact_data.batch_id=batches.id ";
			query += " JOIN tasks on batches.task_id=tasks.id ";
			query += " JOIN entities on fact_data.entity_id=entities.id ";
			query += " where entities.ticker='" + strTicker +"' ";
			query += " AND fiscalyear=" + nProcessYear;
			query += " AND fiscalquarter=" + nQtr;
			query += " AND tasks.name IN ('nasdaq_q_eps','google_q_eps_excxtra') ";
			query += " order by tasks.eps_est_priority, fact_data.date_collected DESC";
			
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
				
				query = "select fact_data.date_collected,tasks.name,fact_data.value,entities.ticker,fact_data.id,tasks.eps_est_priority ";
				query += " from fact_data ";
				query += " JOIN batches on fact_data.batch_id=batches.id ";
				query += " JOIN tasks on batches.task_id=tasks.id ";
				query += " JOIN entities on fact_data.entity_id=entities.id ";
				query += " where entities.ticker='" + strTicker + "' ";
				query += " AND fiscalyear=" + nProcessYear;
				query += " AND fiscalquarter=" + nQtr;
				query += " AND tasks.name IN ('nasdaq_q_eps_est','marketwatch_q_eps_est') ";
				query += " order by tasks.eps_est_priority, fact_data.date_collected DESC";
				
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
		
		query = "select count(*) ";
		query += " from fact_data ";
		query += " JOIN batches on fact_data.batch_id=batches.id ";
		query += " JOIN tasks on batches.task_id=tasks.id ";
		query += " JOIN entities on fact_data.entity_id=entities.id ";
		query += " where tasks.name in ('nasdaq_y_eps_est','marketwatch_y_eps_est') ";
		query += " AND entities.ticker='" + strTicker + "' ";
		query += " AND fact_data.fiscalyear=" + nProcessYear;

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
		
		query = "select fact_data.date_collected,fact_data.value ";
		query += " from fact_data ";
		query += " JOIN batches on fact_data.batch_id=batches.id ";
		query += " JOIN tasks on batches.task_id=tasks.id ";
		query += " JOIN entities on fact_data.entity_id=entities.id ";
		query += " where entities.ticker='" + strTicker + "' ";
		query += " AND fiscalyear=" + nProcessYear;
		query += " AND tasks.name IN ('nasdaq_y_eps_est','marketwatch_y_eps_est') ";
		query += " order by tasks.eps_est_priority, fact_data.date_collected DESC ";
		
		
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
				query = "insert into fact_data " +
				"(value,entity_id,fiscalquarter,fiscalyear,batch_id,date_collected,data_group,calquarter,calyear,metric_id,meta_set_id) values" +
				"(" + dAdjustedQuarterEst + "," +
				nEntityId + "," + 
				//nTaskId + "," +
				(i+1) + "," +
				nProcessYear + "," +
				nBatch + "," +
				"NOW()," +
				"'eps_est'," +
				strQuarterYear.substring(0,1) + "," +
				strQuarterYear.substring(1,5) + "," +
				"4," +
				"1)";
			
				
				UtilityFunctions.db_update_query(query);
				
				/*
				 * OFP 11/7/2011 - Can't figure out why this FirstUpdate code is here.
				 * Going to remove it for now.
				 */
				
				/*if (CalcEstimates.bFirstUpdate==true) {
					String strDelete = "delete fd from fact_data fd ";
					strDelete += " JOIN batches on fd.batch_id=batches.id ";
					strDelete += " where batches.task_id=-1";
					
					UtilityFunctions.db_update_query(strDelete);
					
					CalcEstimates.bFirstUpdate=false;
				}*/
				
				UtilityFunctions.stdoutwriter.writeln(strTicker + ": Generated estimate for fiscalyear: " + nProcessYear + " fiscalquarter: " + (i + 1),Logs.STATUS1,"CE44");
			}
			
		}
		


		
	}
	
	public static void updateMetaDataSet()
	{
		String strUpdate;
		
		UtilityFunctions.stdoutwriter.writeln("Attempting to update meta_set_id field for " + vMetaDataSet.size() + " records.",Logs.STATUS1,"CE44.5");
		
		for (int i=0;i<vMetaDataSet.size();i++)
		{
			try
			{
				strUpdate = "update fact_data set meta_set_id=1 where id=" + vMetaDataSet.get(i);
				UtilityFunctions.db_update_query(strUpdate);
			}
			catch(SQLException sql)
			{
				UtilityFunctions.stdoutwriter.writeln("Update meta_set_id failed for id: " + vMetaDataSet.get(i),Logs.ERROR,"CE44.5");
				UtilityFunctions.stdoutwriter.writeln(sql);
			}
		}
	}
	
	public static void initializeMetaSetId(int nYear) throws SQLException
	{
		/*
		 * Initialize the meta_set_id for all years after and including nYear.
		 * 
		 * Have to think more about how to initialize this because for companies who's fiscalyear beings in feb,
		 * their fiscalyear-calyear shift is one whole year. So processing fiscal year 2011 is processing calendar
		 * year 2010. This program is based off of fiscal year (since the annual estimates are for fiscal year).
		 * 
		 * 
		 * So here's an example of an issue - I wipe out calendar year 2011 and after and start this program with fiscalyear 2011.
		 *  The februaries will go back and process 2010 again and duplicates will be created.
		 * 
		 * I wipe out 2010 and after, and then start this program with fiscalyear 2011. the februaries will go back and recalculate, but the januaries won't.
		 * 
		 * So there has to be more involved logic of initializing a certain range for each set of fiscalcalendar begin months.
		 */
		
		//String strUpdate = "update fact_data set meta_set_id=2 where meta_set_id=1 and fiscalYear>=" + nYear;
		
		//phase shift 4
		String strUpdate = "update fact_data,entities set meta_set_id=2 where entities.begin_fiscal_calendar='February' and ";
		strUpdate += " fact_data.entity_id=entities.id and ";
		strUpdate += "meta_set_id=1 and (calyear*10+calquarter)>=" + (((nYear-1)*10)+1);
		
		UtilityFunctions.db_update_query(strUpdate);
		
		//phase shift 3
		strUpdate = "update fact_data,entities set meta_set_id=2 where entities.begin_fiscal_calendar in ('March','April','May') and ";
		strUpdate += " fact_data.entity_id=entities.id and ";
		strUpdate += "meta_set_id=1 and (calyear*10+calquarter)>=" + (((nYear-1)*10)+2);
		
		UtilityFunctions.db_update_query(strUpdate);
		
		//phase shift 2
		strUpdate = "update fact_data,entities set meta_set_id=2 where entities.begin_fiscal_calendar in ('June','July','August') and ";
		strUpdate += " fact_data.entity_id=entities.id and ";
		strUpdate += "meta_set_id=1 and (calyear*10+calquarter)>=" + (((nYear-1)*10)+3);
		
		UtilityFunctions.db_update_query(strUpdate);
		
		//phase shift 1
		strUpdate = "update fact_data,entities set meta_set_id=2 where entities.begin_fiscal_calendar in ('September','October','November') and ";
		strUpdate += " fact_data.entity_id=entities.id and ";
		strUpdate += "meta_set_id=1 and (calyear*10+calquarter)>=" + (((nYear-1)*10)+4);
		
		UtilityFunctions.db_update_query(strUpdate);
		
		//phase shift 0
		strUpdate = "update fact_data,entities set meta_set_id=2 where entities.begin_fiscal_calendar in ('December','January') and ";
		strUpdate += " fact_data.entity_id=entities.id and ";
		strUpdate += "meta_set_id=1 and (calyear*10+calquarter)>=" + (((nYear)*10)+1);
		
		
		UtilityFunctions.db_update_query(strUpdate);
		
		
		
	}
	
	/*public static Integer getBatchNumber() throws SQLException
	{
		#*
		 * Get the current highest batch number from both fact_data & fact_data_stage and increase that by 1
		 *#
		
		String strQuery1 = "select max(batch

		
		
		String strInsert = "insert into batches (batch_date_collected,task_id) values ";
		strInsert += "(NOW(),-1)";
		
		UtilityFunctions.db_update_query(strInsert);
		
		
		String strQuery = "select max(id) from batches where task_id=-1";
		ResultSet rs = UtilityFunctions.db_run_query(strQuery);
		rs.next();
		
		return (rs.getInt("max(id)"));
			
	
		
	}*/
	
	public static void cleanData()
	{
		/*
		 *  Remove all duplicate 
		 */
		
		
		
		
	
	}

}
