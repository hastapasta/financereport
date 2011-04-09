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



String strUserId = request.getParameter("userid");

String strLimitCount = "20";

if (request.getParameter("limitcount") != null)
	strLimitCount = request.getParameter("limitcount");




String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null)
{
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";






UtilityFunctions uf = new UtilityFunctions();




//String[] columns = tmpArrayList.get(0);

DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");

//String query1 = "select ticker from entities where id " + strInClause;
//ResultSet rs1 = dbf.db_run_query(query1);




//String[] col;

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();
//String[] columns = tmpArrayList.get(0);



String[] blap1 = {"ticker","ticker","string"};
String[] blap2 = {"alert count","alert count","number"};




arrayListCols.add(blap1);
arrayListCols.add(blap2);




String query2 = "select entities.ticker,count(log_alerts.id) as alert_count ";
query2 += " from log_alerts ";
query2 += "join entities on entities.id=log_alerts.entity_id ";
query2 += "join alerts on alerts.id = log_alerts.alert_id ";
query2 += "join time_events on time_events.id=alerts.time_event_id ";
query2 += " where log_alerts.user_id=" + strUserId + " ";
query2 += "group by entities.ticker ";
query2 += " order by alert_count DESC limit " + strLimitCount;





ResultSet rs=null;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

try
{
	rs = dbf.db_run_query(query2);
	while (rs.next()) {
		String [] tmp = new String[4];
		tmp[0] = tmp[1] = rs.getString("entities.ticker");
		tmp[2] = tmp[3] = rs.getString("alert_count");

				
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	out.println(sqle.toString());
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
