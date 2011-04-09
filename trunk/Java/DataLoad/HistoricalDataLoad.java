import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.log4j.NDC;


public class HistoricalDataLoad {
	
	public static Calendar calCurrent;
	
	public static void initiateHistoricalDataLoad(DBFunctions dbf,int nMaxBatch)
	{
		yahooFinanceVerify(dbf,nMaxBatch);
		//yahooFinanceDataLoad(dbf,nMaxBatch);
		
	}

	public static void yahooFinanceVerify(DBFunctions dbf, int nMaxBatch)
	{
		
		String strTask = "10";
		
		try
		{
			DBFunctions tmpdbf = new DBFunctions((String)DataLoad.props.get("dbhost"),(String)DataLoad.props.get("dbport"),(String)DataLoad.props.get("database"),(String)DataLoad.props.get("dbuser"),(String)DataLoad.props.get("dbpass"));
			DataGrab dg = new DataGrab(DataLoad.uf,tmpdbf,strTask,nMaxBatch);
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Issue with database",Logs.ERROR,"DL110.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
		}
		
		
		
		
	}
	
	public static void yahooFinanceDataLoad(DBFunctions dbf, int nMaxBatch)
	{
		String url = "http://ichart.finance.yahoo.com/table.csv?s=INTC&a=01&b=23&c=2009&d=02&e=05&f=2009&g=m";
		
		String baseURL="http://ichart.finance.yahoo.com/table.csv?g=d";
		
		calCurrent = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		
		calCurrent.set(Calendar.DAY_OF_MONTH, 1);
		calCurrent.set(Calendar.MONTH,11);
		calCurrent.set(Calendar.YEAR,2010);
		
		calEnd.set(Calendar.DAY_OF_MONTH, 30);
		calEnd.set(Calendar.MONTH,0);
		calEnd.set(Calendar.YEAR,2011);
		
		/* Set day to last day of month */
		calCurrent.add(Calendar.MONTH, 1);
		calCurrent.add(Calendar.DAY_OF_YEAR,-1);
		
		try
		{
			String strTask="25";
			String currentURL;
			
			//Need to use earlier batch #s
			nMaxBatch = 60047;
			
			
			while (calEnd.after(calCurrent))
			{
				
				
				NDC.push(calCurrent.getTime().toString());
				
				UtilityFunctions.stdoutwriter.writeln("This is a test",Logs.STATUS1,"HDL4");
				
				
			
				
				currentURL = baseURL;
				
				currentURL += "&b=" + calCurrent.get(Calendar.DAY_OF_MONTH);
				currentURL += "&a=" + calCurrent.get(Calendar.MONTH);
				currentURL += "&c=" + calCurrent.get(Calendar.YEAR);
				
							
				currentURL += "&e=" + calCurrent.get(Calendar.DAY_OF_MONTH);
				currentURL += "&d=" + calCurrent.get(Calendar.MONTH);
				currentURL += "&f=" + calCurrent.get(Calendar.YEAR);
				

								
				currentURL += "&s=${dynamic}";
				
				String update = "update jobs set url_static='" + currentURL + "' where id=11246";
				
				dbf.db_update_query(update);		
				
				DBFunctions tmpdbf = new DBFunctions((String)DataLoad.props.get("dbhost"),(String)DataLoad.props.get("dbport"),(String)DataLoad.props.get("database"),(String)DataLoad.props.get("dbuser"),(String)DataLoad.props.get("dbpass"));
				DataGrab dg = new DataGrab(DataLoad.uf,tmpdbf,strTask,nMaxBatch);
				
				dg.startThread();
				UtilityFunctions.stdoutwriter.writeln("Initiated DataGrab thread " + dg.getName(),Logs.STATUS1,"HDL4");
				
				while (dg.getState() != Thread.State.TERMINATED)
				{
					try
					{
						Thread.sleep(5000);
					}
					catch(InterruptedException ie)
					{
						UtilityFunctions.stdoutwriter.writeln(ie);
					}
				}
				
				//check if data got inserted if not, go back a day in the month
				String select = "select count(id) as cnt from fact_data where batch=" + nMaxBatch;
				
				ResultSet rs1 = dbf.db_run_query(select);
				rs1.next();
				if (rs1.getInt("cnt")==0)
				{
					
					
					calCurrent.add(Calendar.DAY_OF_YEAR,-1);					
				}
				else
				{
					
					nMaxBatch++;
					
					calCurrent.set(Calendar.DAY_OF_MONTH, 1);
					calCurrent.add(Calendar.MONTH, 2);
					calCurrent.add(Calendar.DAY_OF_YEAR, -1);
										
				}
				
				NDC.pop();
				  
				
				
				
			}
		}
		catch (SQLException sqle)
		{
			
			UtilityFunctions.stdoutwriter.writeln("Issue instantiating DBFunctions",Logs.ERROR,"DL100.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
			
		}
		
		
		
	}
}
