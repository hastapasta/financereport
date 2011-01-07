import java.sql.Connection;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/* OFP: Something to be aware of:
 * 
 * It is important to remember that a Statement object represents a single SQL statement. 
 * A call to executeQuery(), executeUpdate(), or execute() implicitly closes any active ResultSet associated 
 * with the Statement. In other words, you need to be sure you are done with the results from a query before 
 * you execute another query with the same Statement object. If your application needs to execute more than 
 * one simultaneous query, you need to use multiple Statement objects. As a general rule, calling the close() 
 * method of any JDBC object also closes any dependent objects, such as a Statement generated by a Connection 
 * or a ResultSet generated by a Statement, but well-written JDBC code closes everything explicitly.
 */




public class DBFunctions {
	
	private Connection con;
	//private Statement stmt;
	private String strHost,strPort,strDatabase,strUser,strPass;

	//UtilityFunctions uf;
	
	public Connection getConnection()
	{
		return(UtilityFunctions.con);
		
	}
	
	public void closeConnection()
	{
		try
		{
			con.close();
		}
		catch(SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem closing database connection",Logs.ERROR,"DBF0.25");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
		}
		
	}
	
	public void openConnection() //throws SQLException
	{
		
		boolean bFirstAttempt=true;
		boolean bSuccess = false;
		
		while (!bSuccess)
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				//String url = "jdbc:mysql://localhost:3306/" + strDatabase;
				String url = "jdbc:mysql://" + strHost + ":" + strPort + "/" + strDatabase + "?tcpKeepAlive=true";
				this.con = DriverManager.getConnection(url,strUser, strPass);
				bSuccess=true;
	
			}
			catch (ClassNotFoundException cnfe)
			{
				UtilityFunctions.stdoutwriter.writeln("Problem opening database connection",Logs.ERROR,"DBF0.65");
				UtilityFunctions.stdoutwriter.writeln(cnfe);
			}
			catch (SQLException sqle)
			{
				UtilityFunctions.stdoutwriter.writeln("Problem opening database connection",Logs.ERROR,"DBF0.66");
				if (bFirstAttempt==true)
				{
					
					UtilityFunctions.stdoutwriter.writeln(sqle);
					bFirstAttempt=false;
				}
					
				try
				{
					Thread.sleep(10000);
				}
				catch (InterruptedException ite)
				{
					UtilityFunctions.stdoutwriter.writeln("Thread Interrupted",Logs.ERROR,"DBF0.67");
					UtilityFunctions.stdoutwriter.writeln(ite);
				}
				
			}
		}
	}
	
	public void cycleConnection() throws SQLException
	{
		this.closeConnection();
		this.openConnection();
		
	}
	
	public DBFunctions(String strDBHostParam, String strDBPortParam, String strDatabaseParam, String strUserParam, String strPassParam) throws SQLException
	{
	
		this.strHost = strDBHostParam;
		this.strPort = strDBPortParam;
		this.strDatabase = strDatabaseParam;
		this.strUser = strUserParam;
		this.strPass = strPassParam;
		this.openConnection();
		

		
	}
	
	public void db_update_query(String strUpdateStmt) throws SQLException
	{

	

		//try
		//{
			UtilityFunctions.stdoutwriter.writeln("Executing SQL: " + strUpdateStmt,Logs.SQL,"DBF1");
			UtilityFunctions.stdoutwriter.writeln(strUpdateStmt,Logs.SQL,"DBF2");
			Statement stmt = con.createStatement();
			stmt.execute(strUpdateStmt);
			if (strUpdateStmt.substring(0,6).equals("update") && stmt.getUpdateCount() == 0)
				UtilityFunctions.stdoutwriter.writeln("No rows were updated",Logs.ERROR,"DBF2.5");
			
				
			
		/*}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("1 SQL statement failed: " + strUpdateStmt);
			UtilityFunctions.stdoutwriter.writeln(sqle);
	
			
		}*/
		
		
		
		
	}
	

	
	
	
	public ResultSet db_run_query(String query) throws SQLException
	{

		ResultSet rs =null;
		//try
		//{

			UtilityFunctions.stdoutwriter.writeln("Executing query: " + query, Logs.SQL,"DBF3");
			Statement stmt = con.createStatement();
			UtilityFunctions.stdoutwriter.writeln(query, Logs.SQL,"DBF4");
			rs = stmt.executeQuery(query);

	
		/*}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("About to throw SQLExcpetion");
			throw sqle;
		
		}*/
		
		return(rs);
		/* Not going to worry about closing the connection, we'll let it be garbage collected.*/
		
		
		
	}
	
	public void importTableIntoDB(ArrayList<String[]> tabledata, String tablename, Integer nBatch)
	{
		/* This function expects an arraylist with 2X of the number of values to be inserted with each value
		preceeded by the datatype with the current 3 datatypes being VARCHAR, FUNCTIONS, INT */
		/*OFP 9/28/2010 - I believe passing in the datatypes with the table data is redundant since the types
		 * are retrieved from the database meta data.
		 */
		String[] rowdata;
		String query ="";
		//String columns= "";
		String values = "";
		
		if (tabledata.size() < 2)
		{
			UtilityFunctions.stdoutwriter.writeln("Not enough rows of data passed into importTableIntoDB\nThis may be a custom import and the flag needs to be set",Logs.ERROR,"DBF4.5");
			return;
		}
		
		String[] columnnames = tabledata.get(0);
		tabledata.remove(0);
		
		if (tablename.equals("fact_data_stage") || tablename.equals("fact_data"))
		{
				columnnames = UtilityFunctions.extendArray(columnnames);
				columnnames[columnnames.length - 1] = "batch";
				
		}
		
		String[] datatypes = new String[columnnames.length];
	
		try
		{
			ResultSet rsColumns = null;
			DatabaseMetaData meta = con.getMetaData();

		
	
			int nCount=0;
			for (int j=0;j<columnnames.length;j++)
			{	
	
			  rsColumns = meta.getColumns(null, null, tablename, null);

			  nCount=0;
	    	while (rsColumns.next())
				{
		
					/*System.out.println(rsColumns.getString("COLUMN_NAME"));
					System.out.println(rsColumns.getString("TYPE_NAME"));*/
					//UtilityFunctions.stdoutwriter.writeln(columnnames[j],Logs.STATUS2,"DBF5");
					if (columnnames[j].compareTo(rsColumns.getString("COLUMN_NAME")) == 0)
					{
						datatypes[j] = rsColumns.getString("TYPE_NAME");
						UtilityFunctions.stdoutwriter.writeln(datatypes[j],Logs.STATUS2,"DBF6");
						break;
					}
					nCount++;
				}
			}
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem retrieving column data types",Logs.ERROR,"DBF7");
			UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		UtilityFunctions.stdoutwriter.writeln("Finished retrieving column data types",Logs.STATUS2,"DBF8");
		String strColumns;
	
		int nInsertCount=0;
		
	
		
		
		for (int x=0;x<tabledata.size();x++)
		{
			//for debugging purposes
			//if (x!= 5052)
				//continue;
				
			rowdata = tabledata.get(x);
		
			//query = "INSERT INTO fact_data_stage (data_set,value,quarter,ticker,date_collected) VALUES ('" + strCurDataSe
			values ="";
			strColumns="";
			
			if (tablename.equals("fact_data_stage") || tablename.equals("fact_data"))
			{
					rowdata = UtilityFunctions.extendArray(rowdata);
					//using columnnames here since that is guaranteed to be the correct length
					rowdata[columnnames.length - 1] = Integer.toString(nBatch);
					
			}
			
			
			for (int y=0;y<columnnames.length;y++)
			{
				
				if (y!=0)
				{
					values = values + ",";
					strColumns = strColumns + ",";
				}

				//System.out.println("{" + rowdata[y] + "} " + rowdata[y].length() + " " + datatypes[y]);
				if (datatypes[y].compareTo("VARCHAR") == 0)
				{
					values = values + "'" + rowdata[y].replace("'","\\'") + "'";
				}
				else if (((datatypes[y].compareTo("INT") == 0) || (datatypes[y].compareTo("BIGINT") == 0)) && ((rowdata[y].length() == 0)))
				{

					values = values + "'0'";
				}
				else
					values = values + rowdata[y];	
				
					
				strColumns = strColumns + columnnames[y];

				
			}
			query = "insert into " + tablename + " (" + strColumns + ") values (" + values + ")";
			//System.out.println("insert row: " + query);
			try
			{
				db_update_query(query);
				nInsertCount++;
			}
			catch (SQLException sqle)
			{
				UtilityFunctions.stdoutwriter.writeln("SQLException failed at row " + (x+1) + " " + query,Logs.ERROR,"DBF9");
				UtilityFunctions.stdoutwriter.writeln(sqle);
			}
		
		}
		UtilityFunctions.stdoutwriter.writeln(nInsertCount + " records inserted in db.",Logs.STATUS2,"DBF10");
	}
	
	public void updateTableIntoDB(ArrayList<String[]> tabledata, String tablename)
	{
		
		/*OFP 9/28/2010 - I believe passing in the datatypes with the table data is redundant since the types
		 * are retrieved from the database meta data.
		 */
		String[] rowdata;
		String query ="";
		//String columns= "";
		//String values = "";
		
		String[] columnnames = tabledata.get(0);
		tabledata.remove(0);
		
		String[] datatypes = new String[columnnames.length];
	
		try
		{
			ResultSet rsColumns = null;
			DatabaseMetaData meta = con.getMetaData();

		
	
			int nCount=0;
			for (int j=0;j<columnnames.length;j++)
			{	
	
			  rsColumns = meta.getColumns(null, null, tablename, null);

			  nCount=0;
	    	while (rsColumns.next())
				{
		
					/*System.out.println(rsColumns.getString("COLUMN_NAME"));
					System.out.println(rsColumns.getString("TYPE_NAME"));*/
					UtilityFunctions.stdoutwriter.writeln(columnnames[j],Logs.STATUS2,"DBF11");
					if (columnnames[j].compareTo(rsColumns.getString("COLUMN_NAME")) == 0)
					{
						datatypes[j] = rsColumns.getString("TYPE_NAME");
						UtilityFunctions.stdoutwriter.writeln(datatypes[j],Logs.STATUS2,"DBF12");
						break;
					}
					nCount++;
				}
			}
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Problem retrieving column data types",Logs.ERROR,"DBF13");
			UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		UtilityFunctions.stdoutwriter.writeln("Finished retrieving column data types",Logs.STATUS2,"DBF14");
		//String strColumns;
	
		int nInsertCount=0;
		for (int x=0;x<tabledata.size();x++)
		{
			//for debugging purposes
			//if (x!= 5052)
				//continue;
				
			rowdata = tabledata.get(x);
		
			
			//values ="";
			//strColumns="";
			
			
			
			
			query = "update " + tablename + " set " + columnnames[1] + "='" + rowdata[1] + "' where " + columnnames[0] + "='" + rowdata[0] + "'";
			
			try
			{
				db_update_query(query);
				nInsertCount++;
			}
			catch (SQLException sqle)
			{
				UtilityFunctions.stdoutwriter.writeln("SQLException failed at row " + (x+1) + " " + query,Logs.ERROR,"DBF15");
				UtilityFunctions.stdoutwriter.writeln(sqle);
			}
		
		}
		UtilityFunctions.stdoutwriter.writeln("Updated " + nInsertCount + " records.",Logs.STATUS2,"DBF16");
	}
	
	public Integer getBatchNumber() throws SQLException
	{
		/*
		 * Get the current highest batch number from both fact_data & fact_data_stage and increase that by 1
		 */
		

		
		String query = "select max(batch) from ";
		query = query + "(select batch from fact_data union all ";
		query = query + "select batch from fact_data_stage union all ";
		query = query + "select batch from fact_data_stage_est) t1";
		ResultSet rs = db_run_query(query);
		rs.next();
		return (rs.getInt("max(batch)") + 1);
			
	
		
	}

}
