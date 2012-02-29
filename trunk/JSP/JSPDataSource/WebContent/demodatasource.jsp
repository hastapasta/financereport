<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>


<%


String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null)
{
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";



String strTimeEventId = request.getParameter("timeeventid");
String strUserId = request.getParameter("userid");
String strTaskId = request.getParameter("taskid") ; 


/*if ((strTimeEventId==null) || (strTimeEventId.equals("all")))
	strTimeEventId = "%";

if ((strUserId==null) || (strUserId.equals("all")))
	strUserId = "%";

if ((strTaskId==null) || (strTaskId.equals("all")))
	strTaskId = "%";*/

UtilityFunctions uf = new UtilityFunctions();

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();


String[] blap1 = {"ticker","ticker","string"};
String[] blap2 = {"date","date","string"};
String[] blap3 = {"value","value","number"};



arrayListCols.add(blap1);
arrayListCols.add(blap2);
arrayListCols.add(blap3);





String query2 = "select ticker, year || '_' || 'month' as yrmth, value ";
query2 += "from fact_data_demo ";
query2 += " order by year,month";



DBFunctions dbf = null;
boolean bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
try {
	dbf = new DBFunctions();
	dbf.db_run_query(query2);
	while (dbf.rs.next()) {
		String [] tmp = new String[22];
		tmp[0] = tmp[1] = dbf.rs.getString("ticker");
		tmp[2] = tmp[3] = dbf.rs.getString("yrmth");
		tmp[4] = tmp[5] = dbf.rs.getString("value");		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 0-1"));
	bException = true;
}
finally {
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}


out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,false));


 %> 
