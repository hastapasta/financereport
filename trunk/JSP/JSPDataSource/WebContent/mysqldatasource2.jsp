<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.apache.log4j.Logger" %>



 
<% 

Logger fulllogger = Logger.getLogger("FullLogging");
fulllogger.info("Test in mysqldatasource2.jsp");
/*

This datasource pulls from the fact_data table  the earliest and latest values for all the entities from 
a certain task for a certain data range. 

This is the datasource to use to figure out the maximum gainers/losers for a task for a particular date range.


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

String strTimeFrame = request.getParameter("timeframe");
//String strBeginDate = request.getParameter("begindate"); 
//String strEndDate = request.getParameter("enddate");
String strTaskId = request.getParameter("taskid"); 
String strOrder = request.getParameter("order");

if (strTimeFrame==null)
{
	out.println("No timeframe parameter passed in the url. Exiting.");
	return;
}

if (strTaskId==null)
{
	out.println("No taskid parameter passed in the url. Exiting.");
	return;
}

Calendar calEnd = Calendar.getInstance();
Calendar calBegin = Calendar.getInstance();



if (Debug.RELEASE == true) 
{
	calEnd.set(Calendar.YEAR,2011);
	calEnd.set(Calendar.DAY_OF_MONTH,20);
	calEnd.set(Calendar.MONTH,2);
	
	calBegin.set(Calendar.YEAR,2011);
	calBegin.set(Calendar.DAY_OF_MONTH,20);
	calBegin.set(Calendar.MONTH,2);
	
}



String strGranularity;

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

/*if (!strBeginDate.startsWith("20") && !strBeginDate.startsWith("19"))
{
	out.println("begindate format needs to be yyyy-mm-dd");
	return;
}

if (!strEndDate.startsWith("20") && !strEndDate.startsWith("19"))
{
	out.println("enddate format needs to be yyyy-mm-dd");
	return;
}*/



if (strTaskId.equals("all"))
	strTaskId = "%";

UtilityFunctions uf = new UtilityFunctions();

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();
//String[] columns = tmpArrayList.get(0);
//String[] blap1 = {"task name","task name","string"};
String[] blap2 = {"ticker","ticker","string"};
//String[] blap3 = {"time event name","time event name","string"};
//String[] blap4 = {"full_name","full_name","string"};
String[] blap5 = {"initial value","initial value","number"};
String[] blap6 = {"current value","current value","number"};
String[] blap7 = {"% change","% change","number"};
String[] blap8 = {"date initial","date initial","datetime"};
String[] blap9 = {"date current","date current","datetime"};
//String[] blap10 = {"te period last","te period last","datetime"};
//String[] blap11 = {"te period next","te period next","datetime"};


//arrayListCols.add(blap1);
arrayListCols.add(blap2);
//arrayListCols.add(blap3);
//arrayListCols.add(blap4);
arrayListCols.add(blap5);
arrayListCols.add(blap6);
arrayListCols.add(blap7);
arrayListCols.add(blap8);
arrayListCols.add(blap9);
//arrayListCols.add(blap10);
//arrayListCols.add(blap11);



if (calEnd == null)
	fulllogger.info("calEnd is null");
if (strTaskId == null)
	fulllogger.info("strTaskId is null");

String query = "select entities.ticker,entities.full_name,";
query += "fd1.value,";
query += "date_format(fd1.date_collected,'%m-%d-%Y %H:%i') as date_begin,";
query += "fd2.value,";
query += "date_format(fd2.date_collected,'%m-%d-%Y %H:%i') as date_end, ";
query += "tasks.name, ";
query += "(if (fd1.value=0,fd1.value,round(((fd2.value - fd1.value)/fd1.value * 100),2))) as pctchange ";
query += " from fact_data as fd1";
//query += " JOIN (select entity_id,task_id,value,date_collected from fact_data where batch=(select max(batch) from fact_data where date_format(date_collected,'%Y-%m-%d')<'" + strEndDate + "' and task_id=" + strTaskId + ")) as fd2 on fd1.entity_id=fd2.entity_id AND fd1.task_id=fd2.task_id";
query += " JOIN (select entity_id,task_id,value,date_collected from fact_data where batch=(select max(batch) from fact_data where date_format(date_collected,'%Y-%m-%d %T')<'" + formatter.format(calEnd.getTime()) + "' and task_id=" + strTaskId + ")) as fd2 on fd1.entity_id=fd2.entity_id AND fd1.task_id=fd2.task_id";
query += " JOIN entities on entities.id=fd1.entity_id ";
query += " JOIN tasks on tasks.id=fd1.task_id ";
query += " where ";
//query += " fd1.batch=(select min(batch) from fact_data where date_format(date_collected,'%Y-%m-%d')>'" + strBeginDate + "' and task_id=" + strTaskId + ") ";
query += " fd1.batch=(select min(batch) from fact_data where date_format(date_collected,'%Y-%m-%d %T')>'" + formatter.format(calBegin.getTime()) + "' and task_id=" + strTaskId + ") ";
query += " order by pctchange " + strOrder;


//out.println(query); if (1==1) return;





DBFunctions dbf = null;
boolean bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		String [] tmp = new String[14];
		//tmp[0] = tmp[1] = dbf.rs.getString("tasks.name");
		tmp[0] = tmp[1] = dbf.rs.getString("entities.ticker");
		//tmp[4] = tmp[5] = dbf.rs.getString("time_events.name");
		//tmp[6] = tmp[7] = dbf.rs.getString("usedbf.rs.username");
		tmp[2] = tmp[3] = dbf.rs.getString("fd1.value");
		tmp[4] = tmp[5] = dbf.rs.getString("fd2.value");
		tmp[6] = tmp[7] = dbf.rs.getString("pctchange");
		tmp[8] = tmp[9] = dbf.rs.getString("date_begin");
		tmp[10] = tmp[11] = dbf.rs.getString("date_end");
		//tmp[18] = tmp[19] = dbf.rs.getString("time_events.last_datetime");
		//tmp[20] = tmp[21] = dbf.rs.getString("time_events.next_datetime");
		
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 2-1"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}









out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,false));


//google.visualization.Query.setResponse({version:'0.6',reqId:'0',status:'ok',sig:'5982206968295329967',table:{cols:[{id:'Col1',label:'label1',type:'number'},{id:'Col2',label:'label2',type:'number'},
//{id:'Col3',label:'label3',type:'number'}],rows:[{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]},{c:[{v:2.0,f:'2'},{v:3.0,f:'3'},{v:4.0,f:'4'}]},{c:[{v:3.0,f:'3'},{v:4.0,f:'4'},{v:5.0,f:'5'}]},{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]}]}}); 

//out.println("[{\"\":\" Gold certificate account\",\"2011-01-26 \":\"11037\",\"2011-01-19 \":\"11037\",\"diff \":\"0.00%\"}, {\"\":\" Special drawing rights certificate account\",\"2011-01-26 \":\"5200\",\"2011-01-19 \":\"5200\",\"diff \":\"0.00%\"}, {\"\":\" Coin\",\"2011-01-26 \":\"2318\",\"2011-01-19 \":
//	\"2246\",\"diff \":\"0.00%\"}, {\"\":\"Bills\",\"2011-01-26 \":\"18422\",\"2011-01-19 \":\"18422\",\"diff \":\"0.00%\"}, {\"\":\"Notes and bonds\",\"2011-01-26 \":\"1096024\",\"2011-01-19 \":\"1061154\",\"diff \":\"0.00%\"}, {\"\":\" Federal agency debt securities\",\"2011-01-26 \":\"144623\",\"2011-01-19 \":\"145887\",\"diff \":\"0.00%\"}, {\"\":\" Mortgage-backed securities\",\"2011-01-26 \":\"965078\",\"2011-01-19 \":\"980155\",\"diff \":\"0.00%\"}, {\"\":\" Repurchase agreements\",\"2011-01-26 \":\"0\",\"2011-01-19 \":\"0\",\"diff \":\"0.00%\"}, {\"\":\" Term auction credit\",\"2011-01-26 \":\"0\",\"2011-01-19 \":\"0\",\"diff \":\"0.00%\"}, {\"\":\" Other loans\",\"2011-01-26 \":\"23260\",\"2011-01-19 \":\"23689\",\"diff \":\"0.00%\"}]"); 



//out.println(" Hello Oracle World4"); %> 
