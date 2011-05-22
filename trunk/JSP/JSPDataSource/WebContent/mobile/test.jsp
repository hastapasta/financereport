<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.SQLException" %>

    
    
<% 

String query2 = "select * from metrics";

UtilityFunctions uf = new UtilityFunctions();

DBFunctions dbf = null;
boolean bException = false;
ArrayList<String[]> arrayListRows1 = new ArrayList<String[]>();
try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query2);
	out.println(PopulateSpreadsheet.createJSONFromResultSet(dbf.rs));
	
}
catch (SQLException sqle)
{
	out.println(sqle.toString());
	//out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 2eh2-1"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}%>