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
	
	if (strSeq==null) {
		out.println("seq url parameter is required");
		return;		
	}
	
	if (strDataSet == null) {
		out.println("dataset url parameter is required");
		return;	
	}

	
	String query = "select ticker, date, value from input_test ";
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
	
	out.println("</table></body></html>\n");

%>

