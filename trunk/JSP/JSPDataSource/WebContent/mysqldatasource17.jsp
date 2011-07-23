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
String strUserId = request.getParameter("userid");
String strObservationPeriod = request.getParameter("obperiod");
int nOPeriodIndex = 0;
//String strEntityId = request.getParameter("entityid");


if (strObservationPeriod.toUpperCase().equals("HOURLY"))
	nOPeriodIndex = 3;
else if (strObservationPeriod.toUpperCase().equals("DAILY"))
	nOPeriodIndex = 1;
else if (strObservationPeriod.toUpperCase().equals("MONTHLY"))
	nOPeriodIndex = 2;
else if (strObservationPeriod.toUpperCase().equals("YEARLY"))
	nOPeriodIndex = 4;
else if (strObservationPeriod.toUpperCase().equals("ALLTIME"))
	nOPeriodIndex = 5;

	
	









if ((strAlertId == null) && (strUserId == null))
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"invalid_parameters","No userid or alertid parameter","PF ERROR CODE 17-1"));






UtilityFunctions uf = new UtilityFunctions();




//String[] columns = tmpArrayList.get(0);

//DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");

//String query1 = "select ticker from entities where id " + strInClause;
//ResultSet rs1 = dbf.db_run_query(query1);




//String[] col;

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();
//String[] columns = tmpArrayList.get(0);



String[] blap1 = {"alert fired timestamp","alert fired timestamp","string"};
String[] blap2 = {"initial value","initial value","number"};
String[] blap3 = {"final value","final value","number"};
String[] blap4 = {"initial date collected","initial date collected","string"};
String[] blap5 = {"final date collected","final date collected","string"};
String[] blap6 = {"limit","limit","number"};
String[] blap7 = {"ticker","ticker","string"};
String[] blap8 = {"description","description","string"};
String[] blap9 = {"observation period","observation period","string"};


arrayListCols.add(blap1);
arrayListCols.add(blap7);
arrayListCols.add(blap8);
arrayListCols.add(blap9);
arrayListCols.add(blap2);
arrayListCols.add(blap3);
arrayListCols.add(blap4);
arrayListCols.add(blap5);
arrayListCols.add(blap6);




String query2 = "select ticker,full_name,date_format(date_time_fired,'%m/%d/%y %T') as dtf,fd1.value,fd2.value, ";
query2 += " fd1.date_collected, fd2.date_collected, log_alerts.limit_value, time_events.name";
query2 += " from log_alerts ";
query2 += " join alerts on alerts.id=log_alerts.alert_id ";
query2 += " join entities on entities.id=alerts.entity_id ";
query2 += " join fact_data as fd1 on fd1.id=log_alerts.bef_fact_data_id ";
query2 += " join fact_data as fd2 on fd2.id=log_alerts.aft_fact_data_id ";
query2 += " join time_events on time_events.id=alerts.time_event_id ";
query2 += " where ";
if (strAlertId != null)
	query2 += " log_alerts.alert_id=" + strAlertId + " ";
else
	query2 += " log_alerts.user_id=" + strUserId + " ";
if (nOPeriodIndex != 0)
	query2 += " AND time_events.id=" + nOPeriodIndex;
query2 += " order by dtf DESC limit 30 ";

//out.println(query2); if (1==1) return;






ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
DBFunctions dbf = null;
boolean bException = false;
try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query2);
	while (dbf.rs.next()) {
		String [] tmp = new String[18];
		tmp[0] = tmp[1] = dbf.rs.getString("dtf");
		tmp[2] = tmp[3] = dbf.rs.getString("ticker");
		tmp[4] = tmp[5] = dbf.rs.getString("full_name");
		tmp[6] = tmp[7] = dbf.rs.getString("time_events.name");
		tmp[8] = tmp[9] = dbf.rs.getString("fd1.value");
		tmp[10] = tmp[11] = dbf.rs.getString("fd2.value");
		tmp[12] = tmp[13] = dbf.rs.getString("fd1.date_collected");
		tmp[14] = tmp[15] = dbf.rs.getString("fd2.date_collected");
		tmp[16] = tmp[17] = dbf.rs.getString("log_alerts.limit_value");

				
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 17-2"));
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
