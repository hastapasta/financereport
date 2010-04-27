package com.roeschter.jsl;

import java.sql.*;


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






}