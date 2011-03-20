package pikefin;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ScrubDB {

	/**
	 * @param args
	 */
	
	public static void main2(String[] args) {
		
		try
		{
			DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");
			
			UtilityFunctions uf = new UtilityFunctions();
			
			String strQuery = "select entities.* from entities,entities_entity_groups ";
			strQuery += "where entities_entity_groups.entity_group_id=5 ";
			strQuery += " AND entities.id=entities_entity_groups.entity_id ";
			
			ResultSet rs = dbf.db_run_query(strQuery);
			
			while (rs.next())
			{
				String strFullName = rs.getString("entities.full_name");
				
				strFullName = strFullName.substring(0,strFullName.indexOf(" Equity Index"));
				
				System.out.println("Processing index: " + rs.getString("ticker") + ",country: " + strFullName);
				
				String strCountQuery = "select count(id) as cnt from countries where name like '" + strFullName + "'";
				
				ResultSet rsCnt = dbf.db_run_query(strCountQuery);
				
				rsCnt.next();
				
				if (rsCnt.getInt("cnt")>1)
				{
					System.out.println("Multiple country match, skipping.");
					continue;
				}
				else if (rsCnt.getInt("cnt")==0)
				{
					System.out.println("No countries match, skipping.");
					continue;
				}
				
				String strQuery2 = "select * from countries where name like '" + strFullName + "'";
				
				ResultSet rs2 = dbf.db_run_query(strQuery2);
				
				rs2.next();
				
				String strUpdate = "update entities set country_id=" + rs2.getInt("id") + " where id=" + rs.getInt("id");
				
				dbf.db_update_query(strUpdate);
								
				
			}
			
			
			
		   
		}
	    catch (SQLException sqle)
		{
			System.out.println(sqle.getMessage());
		}
		
		
	
	}
	
	public static void main(String[] args) {
		
		try
		{
			DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");
			
			UtilityFunctions uf = new UtilityFunctions();
			
			
		    //int nStartBatchNumber = 1529;
			int nStartBatchNumber = 9410;
		    
		    String query = "select distinct batch from fact_data where task_id=1 and batch>" + nStartBatchNumber + " and batch<25662 order by batch ASC";
		    
		    ResultSet rs = dbf.db_run_query(query);
		    
		    String[] batch = new String[5];
		    
		    while (rs.next())
		    {
		    	
		    	
		    	batch[0] = rs.getInt("batch") + "";
		    	rs.next();
		    	batch[1] = rs.getInt("batch") + "";
		    	rs.next();
		    	batch[2] = rs.getInt("batch")  + "";
		    	rs.next();
		    	batch[3] = rs.getInt("batch")  + "";
		    	rs.next();
		    	batch[4] = rs.getInt("batch") + "";
		    	
		    	String query2 = "select count(id) as cnt from fact_data where batch in (" + batch[0] + "," + batch[1] + "," + batch[2] + "," + batch[3] + "," + batch[4] + ")";
		    	
		    	ResultSet rs2 = dbf.db_run_query(query2);
		    	
		    	rs2.next();
		    	
		    	if (rs2.getInt("cnt") != 120)
		    	{
		    		System.out.println("Group of batches doesn't add up to 120. Terminating.");
		    		//break;
		    		continue;
		    	}
		    	
		    	/*
		    	 * 
		    	 * Checking that batch data_collected value is within a certain range.
		    	 */
		    	Calendar[] calDates = new Calendar[5];
		    	SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	
		    	for (int i=0;i<5;i++)
		    	{
		    	
			    	String query4 = "select max(date_collected) as dc from fact_data where batch=" + batch[i];
			    	
			    	ResultSet rs4 = dbf.db_run_query(query4);
			    	
			    	rs4.next();
			    				    	
			    	calDates[i] = Calendar.getInstance();
			    	
			    	
			    	try
			    	{
			    		calDates[i].setTime(inputFormat.parse(rs4.getString("dc")));		    		
			    				    		
			    		
			    	}
			    	catch (ParseException pe)
			    	{
			    		System.out.println(pe.getMessage());
					  
			    	}
			    	
		    	
		    	
		    	}
		    	
		    	calDates[0].add(Calendar.MINUTE,12);
		    	
		    	for (int j=1;j<5;j++)
		    	{
		    		if (calDates[j].after(calDates[0]))
		    		{
		    			System.out.println("Batch out of time range.");
		    			
		    			//return;
		    		}
		    		
		    		
		    	}
		    	
		    	
		    	/*
		    	 * 
		    	 * Checking that the ticker groups are all distinct.
		    	 * 
		    	 */
		    	
		    	
		    	String query6 = "select count(fact_data.id) as cnt from fact_data,entities where batch in (" + batch[0] + "," + batch[1] + "," + batch[2] + "," + batch[3] + "," + batch[4] + ") ";
		    	query6 += " AND fact_data.entity_id=entities.id ";
		    	query6 += " AND entities.ticker in ('USDANG','USDAUD','EURGBP','USDAED','USDBIF')";
		    	
		    	ResultSet rs6 = dbf.db_run_query(query6);
		    	
		    	rs6.next();
		    	
		    	if (rs6.getInt("cnt") != 5)
		    	{
		    		System.out.println("Groups aren't distinct.Skipping.");
		    		continue;
		    	}
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	for (int i=1; i<5;i++)
		    	{
		    		
		    		System.out.println("Updating batch " + batch[i] + " to batch " + batch[0]);
		    		String query3 = "update fact_data set batch=" + batch[0] + " where batch=" + batch[i];
		    		
		    		//dbf.db_update_query(query3);
		    				    		    		
		    		
		    	}
		    	
		    	
		    	
		    	
		    	
		    }
		    
		}
		catch (SQLException sqle)
		{
			System.out.println(sqle.getMessage());
		}
			
		
		
	}
	public static void main1(String[] args) {
		// TODO Auto-generated method stub
		
		try
		{
			DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");
			
			UtilityFunctions uf = new UtilityFunctions();
			
			
		    int nStartBatchNumber = 25668;
		    
		    boolean done = false;
		    
		    
		    int nInputBatchNumber = nStartBatchNumber;
		    
		    int nOutputBatchNumber = nStartBatchNumber;
		    
		    
		    
		    while (!done)
		    {
		    			
		    	String query = "select distinct task_id from fact_data where batch=" + nInputBatchNumber;
		    	
		    	ResultSet rs = dbf.db_run_query(query);
		    	
		    	
		    	while (rs.next())
		    	{
		    		System.out.println("Task Id: " + rs.getInt("task_id") + ",InputBatchNumber: " + nInputBatchNumber + ",OutputBatchNumber: " + nOutputBatchNumber);
		    		String query2 = "insert into fact_data_clean ";
		    		query2 += "(value,scale,manual_correction,date_collected,obsolete_data_set,task_id,id,obsolete_ticker,entity_id,data_group,fiscalquarter,fiscalyear,calquarter,calyear,calmonth,day,batch,raw,garbage_collect)";
		    		query2 += " (select value,scale,manual_correction,date_collected,obsolete_data_set,task_id,id,obsolete_ticker,entity_id,data_group,fiscalquarter,fiscalyear,calquarter,calyear,calmonth,day," + nOutputBatchNumber + ",raw,garbage_collect ";
		    		query2 += " from fact_data where task_id=" + rs.getInt("task_id") + " AND batch=" + nInputBatchNumber + ")";
		    				
		    		
		    		dbf.db_update_query(query2);
		    		
		    		nOutputBatchNumber++;
		    				    		
		    		
		    		
		    	}
		    	
		    	nInputBatchNumber++;
		    		
		    }
		}
		catch (SQLException sqle)
		{
			System.out.println(sqle.getMessage());
		}
		
		
		
		
		

	}

}
