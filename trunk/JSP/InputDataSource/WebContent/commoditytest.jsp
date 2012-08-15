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
<%@ page import="java.util.HashMap" %>

<% 
    /*
    Sample url: localhost:8080/InputDataSource/commoditytest.jsp?seq=1&dataset=12&startbatchid=308758
    */
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
	
	if (strStartBatchId == null) {
		out.println("startbatchid is required");
	}
	
	int nBatchId = getBatchFromSequence(Integer.parseInt(strStartBatchId),Integer.parseInt(strSeq));
	if (nBatchId < 1) {
		out.println("Get Batch from sequence failed with code " + nBatchId);
		return;
	}
		

	
	//String filepath = pageContext.getServletContext().getRealPath("/") + "WEB-INF" + java.io.File.separator + "templates" + java.io.File.separator + "bulkemail.vm";
	
	ArrayList<HashMap<String,String>> al;
	HashMap<String,String> hm;
	
	String strQuery = "select fact_data.entity_id as entity_id,fact_data.value as value,fact_data.date_collected as date,entities.ticker as ticker from fact_data ";
	strQuery += " join entities on entities.id=fact_data.entity_id ";
	//strQuery += " where fact_data.entity_id in (649,650) ";
	strQuery += " where batch_id=" + nBatchId;
	
	
	DBFunctions dbf = null;
	boolean bException = false;
	ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
	try {
		dbf = new DBFunctions();

		dbf.db_run_query(strQuery);
		
		//hm = UtilityFunctions.convertResultSetToArrayList(dbf.rs,null,false);
		
		hm = UtilityFunctions.convertResultSetToHashMap(dbf.rs,"entity_id","value");

		
	}
	catch (SQLException sqle) {
		out.println("issue with selecting data to build table");
		out.println(sqle.getMessage());
		return;
		
	}

	
	
	String output = PopulateSpreadsheet.generateVelocity(hm, "list", "commodities.vm");
	
	
	
	out.println(output);
	


%>

<%! 
	
	public static int getBatchFromSequence(int nStartBatchId,int nSequence) {
		/*
		 * sequence numbering starts with 1
		 */
		
		String strQuery = " select * from batches ";
		strQuery += " where task_id=(select task_id from batches where id=" + nStartBatchId + ") ";
		strQuery += " and id>=" + nStartBatchId;
		strQuery += " order by id asc ";
		strQuery += " limit " + (nSequence -1) + ",1 ";
		
		DBFunctions dbf = null;
		boolean bException = false;
		ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
		try {
			dbf = new DBFunctions();

			dbf.db_run_query(strQuery);
			
			if (!dbf.rs.next()) {
				return -1;
			}
			else
				return (dbf.rs.getInt("id"));
				

			
		}
		catch (SQLException sqle) {
			return -2;
		}
		
		
		

		
	}
	
%>