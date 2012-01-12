package pikefin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import pikefin.CustomInvalidInputException;



public class GarbageCollect {
	static String strBeginDate;
	static String strEndDate;
	static int[] arrayTasks;
	static DBFunctions dbf;
	static UtilityFunctions uf;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		strBeginDate = "2011-01-01 00:00:00";
		strEndDate = "2011-04-01 00:00:00";
		arrayTasks = new int[4];
		/*
		 * Only focus on the most data intensive tasks.
		 */
		arrayTasks[0] = 1; arrayTasks[1] = 6; arrayTasks[2] = 10; arrayTasks[3] = 12;
		
		try {
			dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Problem creating DBFucntions",Logs.ERROR,"BV1.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
		}
		
		uf = new UtilityFunctions();
		
		checkBatchDependency();
		//markDelete();
		
		
		

	}
	
	public static void markDelete() {
		
		/*
		 * First initialize garbage collect to zero for all batches.
		 */
		String strUpdateQuery = "update batches ";
		strUpdateQuery += "set garbage_collect=0 ";
		ArrayList<String> listBatches = new ArrayList<String>();
		
		try {
			dbf.db_update_query(strUpdateQuery);
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Problem initializing garbage collect flag",Logs.ERROR,"GC2.0");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			return;
			
		}
		
		
		
		
		/*
		 * mark entire batches for deletion.
		 */
		String strWhereClause = " task_id in (";
		for (int i=0;i<arrayTasks.length;i++) {
			strWhereClause += arrayTasks[i] + ",";
		}
		strWhereClause = strWhereClause.substring(0,strWhereClause.length()-2);
		
		strUpdateQuery = "update batches ";
		strUpdateQuery += " set garbage_collect=1 ";
		strUpdateQuery += " where batch_date_collected>'" + GarbageCollect.strBeginDate + "' ";
		strUpdateQuery += " AND batch_date_collected<" + GarbageCollect.strEndDate + "' ";
		strUpdateQuery += " AND " + strWhereClause;
		
		try {
			dbf.db_update_query(strUpdateQuery);
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Problem setting garbage collect flag.",Logs.ERROR,"GC3.0");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			return;
			
		}
		
		UtilityFunctions.stdoutwriter.writeln("Successfully update garbage collect flag.",Logs.STATUS2,"GC4.0");

		String strQuery = "select * from batches ";
		strQuery += " where batch_date_collected>'" + GarbageCollect.strBeginDate + "' ";
		strQuery += " AND batch_date_collected<" + GarbageCollect.strEndDate + "' ";
		strQuery += " AND " + strWhereClause;
		ArrayList<String[]> batchArrayList = null;
		
		try {
			ResultSet rs = dbf.db_run_query(strQuery);
			batchArrayList = uf.convertResultSetToArrayList2(rs);
			
		}
		catch (SQLException sqle) {
			//UtilityFunctions.stdoutwriter.writeln("Problem creating DBFucntions",Logs.ERROR,"BV1.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			return;
			
		}
		
		for (int i=0;i<batchArrayList.size();i++) {
			String strBatchId = batchArrayList.get(i)[0];
			
			/*
			 * Need to check if any members of that batch are referenced by alerts or log_alerts.
			 */
			
			/*String strQuery2 = " select count(fact_data.id) as cnt from fact_data ";
			strQuery2 += " join log_alerts on log_alerts.bef_fact_data.id=fact_data.id ";
			strQuery2 += " where batch_id=" + strBatchId;*/
			
			String strQuery1 = "select fact_data.id ";
			strQuery1 += " from fact_data ";
			strQuery1 += " join alerts on alerts.initial_fact_data_id=fact_data.id ";
			strQuery1 += " where batch_id=" + strBatchId;
			strQuery1 += " UNION ";
			strQuery1 += " select fact_data.id ";
			strQuery1 += " from fact_data ";
			strQuery1 += " join alerts on alerts.current_fact_data_id=fact_data.id ";
			strQuery1 += " where batch_id=" + strBatchId;
			strQuery1 += " UNION ";
			strQuery1 += " select fact_data.id ";
			strQuery1 += " from fact_data ";
			strQuery1 += " join log_alerts on log_alerts.bef_fact_data_id=fact_data.id ";
			strQuery1 += " where batch_id=" + strBatchId;
			strQuery1 += " UNION ";
			strQuery1 += " select fact_data.id ";
			strQuery1 += " from fact_data ";
			strQuery1 += " join log_alerts on log_alerts.aft_fact_data_id=fact_data.id ";
			strQuery1 += " where batch_id=" + strBatchId;
			
			String strQuery2 = " select count(union_query.id) as cnt from (" + strQuery1 + ") as union_query";
			
			int nCount=-1;
			
			try {
				ResultSet rs = dbf.db_run_query(strQuery2);
				nCount = rs.getInt("cnt");
				rs.close();
				
				if (nCount > 0) {
					listBatches.add(strBatchId);
					continue;
				}
				
		
			}
			catch (SQLException sqle) {
				//UtilityFunctions.stdoutwriter.writeln("Problem creating DBFucntions",Logs.ERROR,"BV1.5");
				UtilityFunctions.stdoutwriter.writeln(sqle);
				return;
				
			}
			
			
			
			
			
		}
		
		
		
		
	}
	
	public static void checkBatchDependency() {
		
		//DBFunctions dbf2 = null;
		//DBFunctions dbf = null;
		ArrayList<String[]> batchArrayList = null;
		
		
		/*
		 * Get a list of all batches in the date range.
		 */
		String strWhereClause = " where task_id in (";
		for (int i=0;i<arrayTasks.length;i++) {
			strWhereClause += arrayTasks[i] + ",";
		}
		strWhereClause = strWhereClause.substring(0,strWhereClause.length()-2);
		strWhereClause += ") and batch_date_collected>\'"+strBeginDate+"' ";
		strWhereClause += " and batch_date_collected<'" + strEndDate+"' ";
		
		
		
		String strQuery = "select id,batch_date_collected,task_id,0 as garbage_collect from batches " + strWhereClause;
		
		try {
			ResultSet rs = dbf.db_run_query(strQuery);
			batchArrayList = uf.convertResultSetToArrayList2(rs);
			
		}
		catch (SQLException sqle) {
			//UtilityFunctions.stdoutwriter.writeln("Problem creating DBFucntions",Logs.ERROR,"BV1.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			return;
			
		}
		
		/*
		 * Find those batches which have a fact_data memeber referenced by a foreign key.
		 */
		
		int nCount = 0;
		
		for (int i=0;i<batchArrayList.size();i++) {
			String strBatchId = batchArrayList.get(i)[0];
			
			if (i % 1000 == 0) {
				UtilityFunctions.stdoutwriter.writeln("Batch Processing Count: " + i + ",batch id: " + strBatchId,Logs.STATUS1,"GC3.5");
			}
			
			String strQuery1 = "select fact_data.id ";
			strQuery1 += " from fact_data ";
			strQuery1 += " join alerts on alerts.initial_fact_data_id=fact_data.id ";
			strQuery1 += " where batch_id=" + strBatchId;
			strQuery1 += " UNION ";
			strQuery1 += " select fact_data.id ";
			strQuery1 += " from fact_data ";
			strQuery1 += " join alerts on alerts.current_fact_data_id=fact_data.id ";
			strQuery1 += " where batch_id=" + strBatchId;
			strQuery1 += " UNION ";
			strQuery1 += " select fact_data.id ";
			strQuery1 += " from fact_data ";
			strQuery1 += " join log_alerts on log_alerts.bef_fact_data_id=fact_data.id ";
			strQuery1 += " where batch_id=" + strBatchId;
			strQuery1 += " UNION ";
			strQuery1 += " select fact_data.id ";
			strQuery1 += " from fact_data ";
			strQuery1 += " join log_alerts on log_alerts.aft_fact_data_id=fact_data.id ";
			strQuery1 += " where batch_id=" + strBatchId;
			
			String strQuery2 = " select count(union_query.id) as cnt from (" + strQuery1 + ") as union_query";
			
			try {
			
				ResultSet rs2 = dbf.db_run_query(strQuery2);
				rs2.next();
				if (rs2.getInt("cnt") > 0) {
					nCount++;
				}
				
			}
			catch (SQLException sqle) {
				uf.stdoutwriter.writeln(sqle);
				continue;
			}
			
			
			
			
			
			
			
		}
		
		UtilityFunctions.stdoutwriter.writeln("Total batches selected for gc: " + batchArrayList.size(),Logs.STATUS1,"gc1.5");
		UtilityFunctions.stdoutwriter.writeln("Batches with foreign key references: " + nCount,Logs.STATUS1,"GC2.5");
		
		
	}
	

		
		
	
	
	


}
