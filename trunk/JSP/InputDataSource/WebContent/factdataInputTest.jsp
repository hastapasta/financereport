<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>

<% 
	UtilityFunctions uf = new UtilityFunctions();

	String strSeq = request.getParameter("seq");
	String strDataSet = request.getParameter("dataset");
	String strStartBatchId = request.getParameter("startbatchid");
	
	if (strSeq==null) {
		out.println("seq url parameter is required");
		return;		
	}
	
	if (strDataSet == null) {
		out.println("dataset url parameter is required");
		return;	
	}
	
	
	
	int nBatchId = getBatchFromSequence(strStartBatchId,Integer.parseInt(strSeq),Integer.parseInt(strDataSet));
	if (nBatchId < 1) {
		out.println("Get Batch from sequence failed with code " + nBatchId);
		return;
	}
	
	String strQuery = "select fact_data.entity_id as entity_id,fact_data.value as value,fact_data.date_collected as date,entities.ticker as ticker from fact_data ";
	strQuery += " join entities on entities.id=fact_data.entity_id ";
	//strQuery += " where fact_data.entity_id in (649,650) ";
	strQuery += " where batch_id=" + nBatchId;
	
	
	out.println("<html><body><table border=1>\n");
	int count = 0;


	DBFunctions dbf = null;
	boolean bException = false;
	ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
	try {
		dbf = new DBFunctions();
		dbf.db_run_query(strQuery);
		out.println("<tr><th>Date</th><th>Ticker</th><th>Entity Id</th><th>Value</th></tr>\n");
		while (dbf.rs.next()) {
			out.println("<tr><td id='date'>" + dbf.rs.getString("date") + "</td><td id='ticker'>" + dbf.rs.getString("ticker") + "</td><td id='entity_id'>" + dbf.rs.getString("entity_id") + "</td><td id='value'>" + dbf.rs.getString("value") + "</td></tr>\n");	
		}
		
	}
	catch (SQLException sqle) {
		out.println("issue with selecting data to build table");
		out.println(sqle.getMessage());
		bException = true;
		
	}
	finally	{
		if (dbf !=null) dbf.closeConnection();
		if (bException == true)
			return;
	}
	
	out.println("</table></body></html>\n");

%>

<%! 
	
	public static int getBatchFromSequence(String strStartBatchId,int nSequence,int nTaskId) {
		/*
		 * sequence numbering starts with 1
		 */
		DBFunctions dbf = null;
		boolean bException = false;
		int nStartBatchId = 0;
		int nRetVal = 0;
		
		if (strStartBatchId != null) {
			nStartBatchId = Integer.parseInt(strStartBatchId);
		}
		else {
			String strQ = "select id from batches ";
			strQ += " where task_id=" + nTaskId;
			strQ += " order by batch_date_collected asc ";
			strQ += " limit 1 ";

			try {
				dbf = new DBFunctions();

				dbf.db_run_query(strQ);
				
				if (!dbf.rs.next()) {
					return -1;
				}
				else
					nStartBatchId = dbf.rs.getInt("id");
					

				
			}
			catch (SQLException sqle) {
				bException = true;
			}
			finally	{
				if (dbf !=null) dbf.closeConnection();
				if (bException == true)
					return -4;
			}
			
			
		}
		
		String strQuery = " select * from batches ";
		strQuery += " where task_id=" + nTaskId;
		strQuery += " and id>=" + nStartBatchId;
		strQuery += " order by id asc ";
		strQuery += " limit " + (nSequence -1) + ",1 ";
		
		dbf = null;
		bException = false;
		//bException = false;
		ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
		try {
			dbf = new DBFunctions();

			dbf.db_run_query(strQuery);
			
			if (!dbf.rs.next()) {
				nRetVal = -1;
			}
			else
				nRetVal = dbf.rs.getInt("id");
				

			
		}
		catch (SQLException sqle) {
			nRetVal = -2;
			bException = true;
		}
		finally	{
			if (dbf !=null) dbf.closeConnection();
		}
		
		return nRetVal;
		
		
		

		
	}
	
%>