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

DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

String strUserId = request.getParameter("userid");

if (strUserId==null)
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","No userid request parameter.","PF ERROR CODE 9-1"));
	//out.println("No begindate request parameter.");
	return;
}

String strLimitCount = "20";

if (request.getParameter("limitcount") != null)
	strLimitCount = request.getParameter("limitcount");

String strEntityGroupId = "all";

if (request.getParameter("entgroup") != null)
	strEntityGroupId = request.getParameter("entgroup");

String strTimeEventId = "all";
if (request.getParameter("timeeventid") != null)
	strTimeEventId = request.getParameter("timeeventid");


String strTimeFrame = "all";

if (request.getParameter("timeframe") != null)
	strTimeFrame = request.getParameter("timeframe");

Calendar calEnd = Calendar.getInstance();
Calendar calBegin = Calendar.getInstance();

//System.out.println(calBegin.getTime().toString());



if (Debug.RELEASE == true)  
{
	calEnd.set(Calendar.YEAR,2011);
	calEnd.set(Calendar.DAY_OF_MONTH,15);
	calEnd.set(Calendar.MONTH,3);
	
	calBegin.set(Calendar.YEAR,2011);
	calBegin.set(Calendar.DAY_OF_MONTH,15);
	calBegin.set(Calendar.MONTH,3);

	
}


if (strTimeFrame.toUpperCase().equals("HOUR"))
{
	calBegin.add(Calendar.HOUR,-1);
}
else if (strTimeFrame.toUpperCase().equals("DAY"))
{
	calBegin.add(Calendar.DAY_OF_YEAR,-1);
}
else if (strTimeFrame.toUpperCase().equals("WEEK"))
{
	calBegin.add(Calendar.WEEK_OF_YEAR,-1);
}
else if (strTimeFrame.toUpperCase().equals("MONTH"))
{
	calBegin.add(Calendar.MONTH,-1);
}
else if (strTimeFrame.toUpperCase().equals("YEAR"))
{
	calBegin.add(Calendar.YEAR,-1);
}













UtilityFunctions uf = new UtilityFunctions();




//String[] columns = tmpArrayList.get(0);

//DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");

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
query2 += " join entities_entity_groups on entities_entity_groups.entity_id=entities.id ";
query2 += " where log_alerts.user_id=" + strUserId + " ";
if (!strTimeFrame.toUpperCase().equals("ALL"))
{
	query2 += " AND date_time_fired> '" + formatter.format(calBegin.getTime()) + "' ";
	query2 += " AND date_time_fired< '" + formatter.format(calEnd.getTime()) + "' ";
}
if (!strEntityGroupId.toUpperCase().equals("ALL"))
	query2 += " AND entities_entity_groups.entity_group_id=" + strEntityGroupId;
if (!strTimeEventId.toUpperCase().equals("ALL"))
	query2 += " AND alerts.time_event_id=" + strTimeEventId;
query2 += " group by entities.ticker ";
query2 += " order by alert_count DESC limit " + strLimitCount;





DBFunctions dbf = null;
boolean bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query2);
	while (dbf.rs.next()) {
		String [] tmp = new String[4];
		tmp[0] = tmp[1] = dbf.rs.getString("entities.ticker");
		tmp[2] = tmp[3] = dbf.rs.getString("alert_count");

				
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 9-2"));
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
