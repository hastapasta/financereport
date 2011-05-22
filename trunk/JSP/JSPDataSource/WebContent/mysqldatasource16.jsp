<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>




<%

/*
* This data_source pulls from the fact_data based off of entities and a date range
*/
String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null)
{
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";



String strAlertId = request.getParameter("alertid");

if (strAlertId==null)
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","No alertid request parameter.","PF ERROR CODE 16-1"));
	//out.println("No begindate request parameter.");
	return;
}
//String strEntityId = request.getParameter("entityid");













UtilityFunctions uf = new UtilityFunctions();




//String[] columns = tmpArrayList.get(0);

//DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");

//String query1 = "select ticker from entities where id " + strInClause;
//ResultSet rs1 = dbf.db_run_query(query1);




//String[] col;

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();
//String[] columns = tmpArrayList.get(0);



String[] blap1 = {"month","month","string"};
String[] blap2 = {"alert count","alert count","number"};




arrayListCols.add(blap1);
arrayListCols.add(blap2);




String query2 = "select date_format(log_alerts.date_time_fired,'%Y-%m') as dc,count(log_alerts.id) as alert_count ";
query2 += " from log_alerts ";
query2 += "join entities on entities.id=log_alerts.entity_id ";
query2 += "join alerts on alerts.id = log_alerts.alert_id ";
query2 += " where log_alerts.alert_id=" + strAlertId + " ";
query2 += " group by date_format(log_alerts.date_time_fired,'%Y-%m') ";
query2 += " order by dc DESC ";

//out.println(query2); if (1==1) return;





DBFunctions dbf = null;
boolean bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query2);
	while (dbf.rs.next()) {
		String [] tmp = new String[4];
		tmp[0] = tmp[1] = dbf.rs.getString("dc");
		tmp[2] = tmp[3] = dbf.rs.getString("alert_count");

				
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 16-2"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}







/*out.println("<table>");
for (int i=0;i<arrayListRows.size();i++)
{
	out.println("<tr>");
	String[] temp = arrayListRows.get(i);
	for (int j=0;j<temp.length;j++)
	{
		out.println("<td>" + temp[j] +"</td>");
	}
	out.println("</tr>");
}
out.println("</table>");

out.println(query); if (1==1) return;*/
























/*for (int i=0;i<arrayListCols.size();i++)
{
	String[] temp = arrayListCols.get(i);
	for (int j=0;j<temp.length;j++)
	{
		out.println(temp[j]);
		out.println("<BR>");
	}
		
}

for (int i=0;i<arrayListRows.size();i++)
{
	String[] temp = arrayListRows.get(i);
	for (int j=0;j<temp.length;j++)
	{
		out.println(temp[j]);
		out.println("<BR>");
	}
		
}*/



out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,false));



%> 
