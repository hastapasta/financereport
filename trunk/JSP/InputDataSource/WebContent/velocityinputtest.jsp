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
	UtilityFunctions uf = new UtilityFunctions();

	String strSeq = request.getParameter("seq");
	String strDataSet = request.getParameter("dataset");
	
	if (strSeq==null) {
		out.println("seq url parameter is required");
		return;		
	}
	
	if (strDataSet == null) {
		out.println("dataset url parameter is required");
		return;	
	}
	
	String filepath = pageContext.getServletContext().getRealPath("/") + "WEB-INF" + java.io.File.separator + "templates" + java.io.File.separator + "bulkemail.vm";
	
	ArrayList<HashMap<String,String>> hm;
	
	String strQuery = "select fact_data.value as value,fact_data.date_collected as date,entities.ticker as ticker from fact_data ";
	strQuery += " join entities on entities.id=fact_data.entity_id ";
	strQuery += " where fact_data.entity_id=1 ";
	strQuery += " and date_collected>'2012-03-01' and date_collected<'2012-03-02' ";
	
	
	DBFunctions dbf = null;
	boolean bException = false;
	ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
	try {
		dbf = new DBFunctions();

		dbf.db_run_query(strQuery);
		
		hm = UtilityFunctions.convertResultSetToArrayList(dbf.rs,null,false);

		
	}
	catch (SQLException sqle) {
		out.println("issue with selecting data to build table");
		out.println(sqle.getMessage());
		return;
		
	}

	
	
	String output = PopulateSpreadsheet.generateVelocity(hm, "list", "inputtest2.vm");
	
	
	
	out.println(output);
	


%>