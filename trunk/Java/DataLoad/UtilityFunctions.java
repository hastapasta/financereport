package com.roeschter.jsl;

import java.sql.*;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


class UtilityFunctions
{


	public static Connection db_connect()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/mydb";
			return(DriverManager.getConnection(url,"root", "madmax1."));
			
		}
		catch (Exception e)
	{
		e.printStackTrace();
		return(null);
		
	}
	
		
		
	}
	
	public static void db_update_query(String strUpdateStmt)
	{
		Connection con=null;
		ResultSet rs =null;

		try
		{
			con = db_connect();
			Statement stmt = con.createStatement();
			stmt.execute(strUpdateStmt);
		}
		catch (SQLException sqle)
		{
			System.out.println("SQL statement failed: " + strUpdateStmt);
			sqle.printStackTrace();
		}
		
		
		
		
	}
	
	public static ResultSet db_run_query(String query)
	{
		Connection con=null;
		ResultSet rs =null;
		try
		{
			con = db_connect();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(query);
		}
		catch (SQLException sqle)
		{
			System.out.println("SQL statement failed: " + query);
			sqle.printStackTrace();
		}
		return(rs);
		/* Not going to worry about closing the connection, we'll let it be garbage collected.*/
		
		
		
	}
	
	public static void createCSV(ArrayList<String[]> tabledata,String filename,boolean append)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename,append));
			String strLine="";
			String[] rowdata;
	
	    for (int x=0;x<tabledata.size();x++)
	    {
	    	rowdata = tabledata.get(x);
	    	strLine="";
				for (int y=0;y<rowdata.length;y++)
				{
					if (y !=0)
						strLine = strLine +",";
						
		    	strLine = strLine + "\"" + rowdata[y] + "\"";
		    	   
		    }
		    writer.write(strLine);
		    writer.newLine();
	     
	    }
	
	
	    writer.close();  // Close to unlock and flush to disk.
	  }
	  catch (IOException ioe)
	  {
	  	System.err.println("Problem writing CSV file");
	  	ioe.printStackTrace();
	  }

		
		
		
		
	}






}