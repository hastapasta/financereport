package pikefin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScrubDB {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
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
