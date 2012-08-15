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
	
	
	String strQuery2 = "select concat(alerts.id,entities.ticker) as val1, alerts.id as alertid,entities.id as entityid from alerts ";
	strQuery2 += " join entities on entities.id=alerts.entity_id ";
	strQuery2 += " where entity_id=1 ";
	strQuery2 += " limit 10 ";
	
	DBFunctions dbf = null;
	boolean bException = false;
	ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
	try {
		dbf = new DBFunctions();
		//dbf.db_run_query(strQuery);
		dbf.db_run_query(strQuery2);
		
		hm = UtilityFunctions.convertResultSetToArrayList(dbf.rs,null,false);
		/*if (dbf.rs.next()) {
			count = 1;
			out.println("<tr><th></th><th>" + dbf.rs.getString("ticker") + "</th></tr>\n");
			out.println("<tr><th>" + dbf.rs.getString("date") + "</th><td>" + dbf.rs.getString("value") + "</td></tr>\n");	
		}*/
		
	}
	catch (SQLException sqle) {
		out.println("issue with selecting data to build table");
		out.println(sqle.getMessage());
		return;
		
	}
	
	
	

	
	
	String output = PopulateSpreadsheet.generateVelocity(hm, "alertlist", "inputtest.vm");
	
	
	
	out.println(output);
	
	/*String query = "select ticker, date, value from input_test ";
	query += " where sequence=" + strSeq;
	query += " and data_set=" + strDataSet;
	
	out.println("<html><body><table border=1>\n");
	int count = 0;


	DBFunctions dbf = null;
	boolean bException = false;
	ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
	try {
		dbf = new DBFunctions();
		dbf.db_run_query(query);
		if (dbf.rs.next()) {
			count = 1;
			out.println("<tr><th></th><th>" + dbf.rs.getString("ticker") + "</th></tr>\n");
			out.println("<tr><th>" + dbf.rs.getString("date") + "</th><td>" + dbf.rs.getString("value") + "</td></tr>\n");	
		}
		
	}
	catch (SQLException sqle) {
		out.println("issue with selecting data to build table");
		out.println(sqle.getMessage());
		return;
		
	}
	
	out.println("</table></body></html>\n");*/

%>