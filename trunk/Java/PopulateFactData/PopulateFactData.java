
import java.math.BigDecimal;

import java.sql.SQLException;

import java.util.Calendar;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import pikefin.log4jWrapper.Logs;


/*
 * Changes to make to fact_data tables.
 * 
 * Get rid of data_group varchar field and figure out what is using it. Make it a foreign key reference if necessary.
 * 
 * Switch batch_id to not allow nulls and default none.
 * 
 * TO DO:
 * 
 * May want to add a function to add back in the indexes and to track the size of the table along the way.
 * 
 * 
 */




public class PopulateFactData {

	/**
	 * @param args
	 */
	
	
	/*
	 * Using Calendar object format with Month starting at zero
	 */
	static int nCutoffYear;
	static int nCutoffMonth;
	static int nCutoffDay;	
	static String strCutoffDate;
	
	static String strDBHost;
	static String strDBPort;
	static String strDatabase;
	static String strDBUser;
	static String strDBPass;
	
	static String strMemoryTable;
	static String strDiskTable;
	
	static boolean bLoadAllData;
	
	static String strIndexType;
	
	
	
	
	static DBFunctions dbf;
	static UtilityFunctions uf;
	
	static int[] arrayTasks;// = {6,10,1,24};
	
	static String[] arrayIndexColumns;
	
	
	
	
	
	public static void main(String[] args) {
		
		try {
			
			BeanFactory beanfactory = new ClassPathXmlApplicationContext(
			        "context.xml");
			
			
			//FirstBean bean = (FirstBean) beanfactory.getBean("show");
		
			//uf = new UtilityFunctions();
			

			
			/*SqlRowSet srs = dbf.dbSpringRunQuery("select * from aros"); 
			
			while (srs.next()) {
				System.out.println(srs.getInt("id"));
			}*/
				
			UtilityFunctions.stdoutwriter.writeln("This is some text", Logs.STATUS1,"PFD12");
			
			truncateFactData();
			
			dropIndexes();
			
			if (bLoadAllData)
				loadAllData();
			else
				populateFactData();
			
			createIndexes();
			

		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		/*(catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			UtilityFunctions.stdoutwriter.writeln(ioe);
		}*/
		
	}
	
	public static void populateFactData() {
		try {
			
			
			
			UtilityFunctions.stdoutwriter.writeln("Begin Data Load", Logs.STATUS1,"PFD12");
			
		
			

	
			loadAllDataAllTasksByBatch();
			
			//loadHourGranularityTaskList();
			
			loadAllNotInTaskList();
			
			loadForeignKeyReferences();
			
			
			
		}
		catch(Exception ex) {
			UtilityFunctions.stdoutwriter.writeln(ex);
			
		}
		
	}
	
	public static void loadForeignKeyReferences() throws SQLException {
		
		/*
		 * Load in foreign key references from log_alerts and alerts. 
		 */
		
		String strInsert4a = "insert into " + strMemoryTable;
		strInsert4a += "(select " + strDiskTable + ".* from " + strDiskTable;
		strInsert4a += " join alerts on " + strDiskTable + ".id=alerts.initial_fact_data_id) ";
		strInsert4a += " on duplicate key update " + strMemoryTable + ".id=" + strDiskTable + ".id ";
		
		dbf.dbSpringUpdateQuery(strInsert4a);
		
		UtilityFunctions.stdoutwriter.writeln("inserted alerts initial", Logs.STATUS1,"PFD1");
		doPrintFull(0);
		
		String strInsert4b = "insert into " + strMemoryTable;
		strInsert4b += "(select " + strDiskTable + ".* from " + strDiskTable;
		strInsert4b += " join alerts on " + strDiskTable + ".id=alerts.current_fact_data_id) ";
		strInsert4b += " on duplicate key update " + strMemoryTable + ".id=" + strDiskTable + ".id ";
		
		dbf.dbSpringUpdateQuery(strInsert4b);
		
		UtilityFunctions.stdoutwriter.writeln("inserted alerts current", Logs.STATUS1,"PFD2");
		doPrintFull(0);
		
		String strInsert4c = "insert into " + strMemoryTable;
		strInsert4c += "(select " + strDiskTable + ".* from " + strDiskTable;
		strInsert4c += " join log_alerts on " + strDiskTable + ".id=log_alerts.bef_fact_data_id) ";
		strInsert4c += " on duplicate key update " + strMemoryTable + ".id=" + strDiskTable + ".id ";
		
		dbf.dbSpringUpdateQuery(strInsert4c);
		
		UtilityFunctions.stdoutwriter.writeln("inserted log_alerts bef", Logs.STATUS1,"PFD3");
		doPrintFull(0);
		
		String strInsert4d = "insert into " + strMemoryTable;
		strInsert4d += "(select " + strDiskTable + ".* from " + strDiskTable;
		strInsert4d += " join log_alerts on " + strDiskTable + ".id=log_alerts.aft_fact_data_id) ";
		strInsert4d += " on duplicate key update " + strMemoryTable + ".id=" + strDiskTable + ".id ";
		
		dbf.dbSpringUpdateQuery(strInsert4d);
		
		UtilityFunctions.stdoutwriter.writeln("inserted log_alerts aft", Logs.STATUS1,"PFD4");
		doPrintFull(0);
		
	}
	
	
	public static void loadAllData() {
		
		try {
			String strGetBatchCount ="select count(id) as cnt from batches ";
			
			
			SqlRowSet rs2 = dbf.dbSpringRunQuery(strGetBatchCount);
			rs2.next();
			int nBatchCount = rs2.getInt("cnt");
			
			String strGetBatches = "select id from batches ";
			strGetBatches += " where batch_date_collected>'" + getCutoffDate() + "' ";
			
			SqlRowSet rs = dbf.dbSpringRunQuery(strGetBatches);
			
			int nCount = 0;
			while (rs.next()) {
				
				
				String strInsert = "insert into " + strMemoryTable + " (select * from " + strDiskTable + " where batch_id=" + rs.getInt("id") + ")";
				
				dbf.dbSpringUpdateQuery(strInsert);
				
				nCount++;
				if (nCount % 20000 == 0) {
					UtilityFunctions.stdoutwriter.writeln("Loaded " + nCount + " out of " + nBatchCount + " batches.", Logs.STATUS1,"PFD20");
					doPrintFull(0);
		
				}
				
			}
			
			UtilityFunctions.stdoutwriter.writeln("Loaded " + nCount + " out of " + nBatchCount + " batches.", Logs.STATUS1,"PFD20.43");
			doPrintFull(0);
			
			
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln(sqle);
			System.out.println(sqle.getMessage());

			
		}
		
		
		
	}
	
	public static void loadAllDataAllTasksByBatch() throws SQLException {
		
		int nCount = 0;
		boolean bDone = false;
		
		Calendar cal = Calendar.getInstance();
		Calendar calCurrent = Calendar.getInstance();
		
		cal.set(Calendar.YEAR,nCutoffYear);
		cal.set(Calendar.MONTH, nCutoffMonth-1);
		cal.set(Calendar.DAY_OF_MONTH, nCutoffDay);
		
		Calendar calNext = (Calendar)cal.clone();
		calNext.add(Calendar.DAY_OF_MONTH, 1);
		
		/*
		 * Load in all data for all tasks from cutoff forward.
		 */
		
		while (!bDone) {
			
			String strDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
			String strDateNext = calNext.get(Calendar.YEAR) + "-" + (calNext.get(Calendar.MONTH) + 1) + "-" + calNext.get(Calendar.DAY_OF_MONTH);
			
			//System.out.println("Processing day " + strDate);
			UtilityFunctions.stdoutwriter.writeln("Processing day " + strDate, Logs.STATUS1,"PFD5");
			
			//String select = "select id from disk_fact_data where date_format(date_collected,'%Y-%c-%e')='" + strDate +"'";
			String select = "select id from batches where batch_date_collected>='" + strDate + "' and batch_date_collected<'" + strDateNext + "'";
								
			SqlRowSet rs = dbf.dbSpringRunQuery(select);
			
			while (rs.next()) {
				String strInsert = "insert into " + strMemoryTable + " (select * from " + strDiskTable + " where batch_id=" + rs.getInt("id") + ")";
				
				dbf.dbSpringUpdateQuery(strInsert);
				
				nCount++;
				if (nCount % 1000 == 0) {
					doPrintFull(nCount);

				}
			}
			
			cal.add(Calendar.DAY_OF_MONTH, 1);
			calNext.add(Calendar.DAY_OF_MONTH, 1);
			
			if (cal.after(calCurrent))
				break;
			
			
		}
		
		UtilityFunctions.stdoutwriter.writeln("Finished loadAllDataAllTasksByBatch() with cutoff date " + PopulateFactData.getCutoffDate(), Logs.STATUS1,"PFD21");
		doPrintFull(0);
		
	}
	
	public static void loadAllDataAllTasks() throws SQLException {
		
		int nCount = 0;
		boolean bDone = false;
		
		Calendar cal = Calendar.getInstance();
		Calendar calCurrent = Calendar.getInstance();
		
		cal.set(Calendar.YEAR,2012);
		cal.set(Calendar.MONTH, 2);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		
		Calendar calNext = (Calendar)cal.clone();
		calNext.add(Calendar.DAY_OF_MONTH, 1);
		
		/*
		 * Load in all data for all tasks from cutoff forward.
		 */
		
		while (!bDone) {
			
			String strDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
			String strDateNext = calNext.get(Calendar.YEAR) + "-" + (calNext.get(Calendar.MONTH) + 1) + "-" + calNext.get(Calendar.DAY_OF_MONTH);
			
			//System.out.println("Processing day " + strDate);
			UtilityFunctions.stdoutwriter.writeln("Processing day " + strDate, Logs.STATUS1,"PFD5");
			
			//String select = "select id from disk_fact_data where date_format(date_collected,'%Y-%c-%e')='" + strDate +"'";
			String select = "select id from " + strDiskTable + " where date_collected>='" + strDate + "' and date_collected<'" + strDateNext + "'";
								
			SqlRowSet rs = dbf.dbSpringRunQuery(select);
			
			while (rs.next()) {
				String strInsert = "insert into " + strMemoryTable + " (select * from " + strDiskTable + " where id=" + rs.getInt("id") + ")";
				
				dbf.dbSpringUpdateQuery(strInsert);
				
				nCount++;
				if (nCount % 100000 == 0) {
					doPrintFull(nCount);

				}
			}
			
			cal.add(Calendar.DAY_OF_MONTH, 1);
			calNext.add(Calendar.DAY_OF_MONTH, 1);
			
			if (cal.after(calCurrent))
				break;
			
			
		}
		
		UtilityFunctions.stdoutwriter.writeln("Finished loadAllDataAllTasks() with cutoff date " + getCutoffDate(), Logs.STATUS1,"PFD21");
		doPrintFull(0);
		
	}
	
	public static void createIndexes() throws SQLException {
		
		for (String strColumn : arrayIndexColumns) { 
			
			String strQuery = " create index " + strColumn;
			strQuery += " using " + strIndexType;
			strQuery += " on " + strMemoryTable + "(" + strColumn + ") ";
			
			dbf.dbSpringUpdateQuery(strQuery);
			
			UtilityFunctions.stdoutwriter.writeln("Created index " + strColumn, Logs.STATUS1,"PFD21.88");
			
			doPrintFull(0);

			
			//ALTER TABLE `fact_data` ADD INDEX ( `calyear` ) using 'btree'/'hash'
			
			//create index batch_id using BTREE on fact_data(batch_id)
		}
		
		
		
	}
	
	public static void dropIndexes() throws SQLException {
		
		for (String strColumn : arrayIndexColumns) { 
			
			String strShowQuery = "SHOW INDEX FROM " + strMemoryTable;
			strShowQuery += " where Key_name= '" + strColumn + "' ";
			
			SqlRowSet rs = dbf.dbSpringRunQuery(strShowQuery);
			
			if (rs.next()) {

				String strQuery = " drop index " + strColumn;
				strQuery += " on " + strMemoryTable;
				
				dbf.dbSpringUpdateQuery(strQuery);
				
				UtilityFunctions.stdoutwriter.writeln("Dropped index " + strColumn, Logs.STATUS1,"PFD21.88");
			}
						
			//drop index <col> on <tbl>
			
			//SHOW INDEX FROM fact_data WHERE Key_name = 'calyear';
		}
		
		
		
	}
	
	public static void loadHourGranularityTaskList() throws SQLException {
		/*
		 * Load in hour granularity for all tasks in task list prior to cutoff.
		 * 
		 */
		
		
		for (int nTask : arrayTasks) {

			String strInsertInner2 = " select min(batch_date_collected) as mbdc from batches ";
			strInsertInner2 += " where task_id=" + nTask;
			strInsertInner2 += " and batch_date_collected<'" + getCutoffDate() + "'";
			strInsertInner2 += " group by date_format(batch_date_collected,'%Y-%m-%d'),hour(batch_date_collected) ";
			
			
			String strInsertOuter2 = " select id from batches, (" + strInsertInner2 + ") as tbl2 ";
			strInsertOuter2 += " where task_id=" + nTask;
			strInsertOuter2 += " and batches.batch_date_collected = tbl2.mbdc ";
			
			SqlRowSet rs3 = dbf.dbSpringRunQuery(strInsertOuter2);

			while (rs3.next()) {
			
				String strInsert2 = " insert into " + strMemoryTable;
				strInsert2 += " (select * from " + strDiskTable;
				strInsert2 += " where batch_id=" + rs3.getInt("id");
				strInsert2 += " )";		
				
				dbf.dbSpringUpdateQuery(strInsert2);
				
			}
			
			UtilityFunctions.stdoutwriter.writeln("Loaded hour granularity for task " + nTask, Logs.STATUS1,"PFD10");
			doPrintFull(0);
			

			
		}
		
		UtilityFunctions.stdoutwriter.writeln("Completed loading task-list data (hour granularity)", Logs.STATUS1,"PFD8");
	}
	
	public static void loadAllNotInTaskList() throws SQLException {
		/*
		 * Load in all data for tasks not in task list prior to cutoff.
		 */
		
		String strTaskWhere = " (";
		for (int nTask : arrayTasks) {
			strTaskWhere += nTask + ",";
		}
		strTaskWhere = strTaskWhere.substring(0,strTaskWhere.length()-1) + ")";

		String strInsert3 = "insert into " + strMemoryTable;
		strInsert3 += " (select " + strDiskTable + ".* from " + strDiskTable;
		strInsert3 += " join batches on batches.id=" + strDiskTable + ".batch_id ";
		strInsert3 += " where task_id not in " + strTaskWhere;
		strInsert3 += " and batch_date_collected<'" + getCutoffDate() + "' ";
		strInsert3 += ")";
			
		
		dbf.dbSpringUpdateQuery(strInsert3);
		
		UtilityFunctions.stdoutwriter.writeln("Loaded non-task-list data (full granularity)", Logs.STATUS1,"PFD7");
		doPrintFull(0);
	}
	
	
	
	public static void truncateFactData() throws SQLException {
		String strTruncate = "truncate " + strMemoryTable;	
		
		dbf.dbSpringUpdateQuery(strTruncate);
		
		UtilityFunctions.stdoutwriter.writeln("Truncated table " + strMemoryTable, Logs.STATUS1,"PFD6.46");
		
	
	}
	
	
	
	public static void doPrintFull(int nCount) throws SQLException {
		String strStatus = "show table status like '" + strMemoryTable + "'";
		SqlRowSet rs2 = dbf.dbSpringRunQuery(strStatus);
		rs2.next();
		BigDecimal bdFull = new BigDecimal(rs2.getInt("data_length"));
		
		bdFull.setScale(3);
		bdFull = bdFull.divide(new BigDecimal(rs2.getInt("max_data_length")),3,BigDecimal.ROUND_HALF_UP);
		bdFull = bdFull.multiply(new BigDecimal(100));
		//bdFull.divide(new BigDecimal(1),BigDecimal.ROUND_HALF_UP);
		
		//int nFull = 100 * rs2.getInt("data_length") / rs2.getInt("max_data_length");
		//System.out.print(nCount + " rows loaded.");
		//System.out.println(" Fact_data table " + bdFull.toString() + "% full");
		//String strOutput = nCount + " rows loaded. ";
		String strOutput = strMemoryTable + " table " + bdFull.toString() + "% full";
		
		UtilityFunctions.stdoutwriter.writeln(strOutput, Logs.STATUS1,"PFD6");
		

	}
	
public PopulateFactData(String str1, String str2) {
		
		strDBHost = str1;
		strDBPort = str2;
	}
	
	public PopulateFactData() {
		
	}
	
	public void setDbHost(String strInput) {
		strDBHost = strInput;
	}
	
	public void setDbPort(String strInput) {
		strDBPort = strInput;
	}
	public void setDatabase(String strInput) {
		strDatabase = strInput;
	}
	public void setDbUser(String strInput) {
		strDBUser = strInput;
	}
	public void setDbPass(String strInput) {
		strDBPass = strInput;
	}
	
	public void setCutoffYear(int nInput) {
		nCutoffYear = nInput;
	}
	
	public void setCutoffMonth(int nInput) {
		nCutoffMonth = nInput;
	}
	
	public void setCutoffDay(int nInput) {
		nCutoffDay = nInput;
	}
	
	public void setMemoryTable(String strInput) {
		strMemoryTable = strInput;
	}
	
	public void setDiskTable(String strInput) {
		strDiskTable = strInput;
	}

	
	public void setListTasks(int...args) {
		arrayTasks = args.clone();
	}
	
	public void setLoadAllData(String strInput) {
		bLoadAllData = false;
		if (strInput.equalsIgnoreCase("true"))
			bLoadAllData = true;
	}
	
	public void setIndexColumns(String...args) {
		arrayIndexColumns = args.clone();
	}
	
	public void setIndexType(String strInput) {
		strIndexType = strInput;
	}
	
	public void setDbFunctions(DBFunctions inputDBF) {
		dbf = inputDBF;
	}
	
	public void setUtilityFunctions(UtilityFunctions inputUF) {
		uf = inputUF;
	}
	
	
	
	public static String getCutoffDate() {
		//return(nCutoffYear + "-" + (nCutoffMonth + 1) + "-" + nCutoffDay);
		return(nCutoffYear + "-" + (nCutoffMonth) + "-" + nCutoffDay);
	}
	
	
	
	
	
	
	
	
	
	

}

