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






}