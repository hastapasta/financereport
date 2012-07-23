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
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="org.apache.log4j.Logger" %>



<% 

/*

This datasource is currently used to verify how current quarterly eps data is and what 
the meta_set_id value is set to.

This datasource is currently being viewed directly and is not set up to be used by any php
interface (yet).


*/

UtilityFunctions uf = new UtilityFunctions();

Calendar calBeginTimer = Calendar.getInstance(); 

Logger fulllogger = Logger.getLogger("FullLogging");

String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null) {
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";




String strEntityId = request.getParameter("entityid");
String strTicker = request.getParameter("ticker");

if (strEntityId==null && strTicker==null) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","Url parameter entityid or ticker is required.","PF ERROR CODE 20-2"));
	return;
}

/*
* Look up the id for the ticker.
*/
String query;
DBFunctions dbf;
boolean bException;

if (strTicker != null) {


	//String[] tickerarray = strTickers.split(",");
	dbf = null;
	bException = false;

	
	try	{
		query = "select id from entities where ticker='" + strTicker + "'";
		/*for (String strTick : tickerarray) { 
			query += "'" + strTick + "',";
		}*/
		//query = query.substring(0,query.length()-1);
		//query += ")";
		
		dbf = new DBFunctions();
		dbf.db_run_query(query);
			
		if (dbf.rs.next())
			strEntityId = dbf.rs.getInt("id") + "";
		else {
			out.println(PopulateSpreadsheet.createGoogleError(strReqId,"data_issue","No entity id from for ticker " + strTicker,"PF ERROR CODE 15s-20"));
			bException = true;
		}
		/*while (dbf.rs.next()) {
			strEntityId +=  dbf.rs.getInt("id") + ",";
		    
		}*/
		//strEntityId = strEntityId.substring(0,strEntityId.length()-1);
	}
	catch (SQLException sqle) {
		//out.println(sqle.toString());
		out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15s-5"));
		bException = true;
	}
	finally	{
		if (dbf !=null) dbf.closeConnection();
		if (bException == true)
			return;
	}
}

String strGroupBy = "false";
if (request.getParameter("groupby") != null)
	strGroupBy = request.getParameter("groupby");



if (strEntityId==null) {
	out.println("No entityid passed in the url. Exiting.");
	return;
}


boolean bDebug = false;
if (request.getParameter("debug") != null)
	if (request.getParameter("debug").equalsIgnoreCase("true")) {
		out.println("here");
		bDebug = true;
	}












ArrayList<String[]> arrayListCols = new ArrayList<String[]>();




String[] blap1 = {"calyearquarter", "calyearquarter", "string"};
String[] blap7 = {"cal quarter", "cal quarter", "number"};
String[] blap8 = {"cal year", "cal year ", "number"};
String[] blap2 = {"task name","task name","string"};
String[] blap3 = {"date collected","date collected","datetime"};
String[] blap4 = {"value","value","number"};
String[] blap5 = {"metric name","metric name","string"};
String[] blap6 = {"meta set id","meta set id","number"};

arrayListCols.add(blap1);
arrayListCols.add(blap2);
arrayListCols.add(blap7);
arrayListCols.add(blap8);
arrayListCols.add(blap3);
arrayListCols.add(blap4);
arrayListCols.add(blap5);
arrayListCols.add(blap6);


String query3 = "select CONCAT(fact_data.calyear,'Y',fact_data.calquarter,'Q') as calyearquarter, fact_data.calquarter, fact_data.calyear, tasks.name, fact_data.date_collected ";
query3 += " ,fact_data.value, fact_data.meta_set_id, metrics.name ";
query3 += " from fact_data ";
query3 += " join batches on fact_data.batch_id=batches.id ";
query3 += " join tasks on batches.task_id=tasks.id ";
query3 += " join metrics on metrics.id = fact_data.metric_id ";
query3 += " where entity_id=" + strEntityId;
query3 += " and fact_data.metric_id in (4,5) ";
query3 += " order by calyearquarter asc, tasks.name, fact_data.date_collected ";




boolean bDone = false;
dbf = null;
bException = false;
int code=1;

bDone = false;

bException = false;
ArrayList<String[]> arrayListRows2 = new ArrayList<String[]>();
while (!bDone) {
	try
	{
		dbf = new DBFunctions();
		dbf.db_run_query(query3);
	
		while (dbf.rs.next()) {
			String [] tmp = new String[8];
			
			tmp[0] = dbf.rs.getString("calyearquarter");
			tmp[1] = dbf.rs.getString("tasks.name");
			tmp[2] = dbf.rs.getString("fact_data.calquarter");
			tmp[3] = dbf.rs.getString("fact_data.calyear");
			tmp[4] = dbf.rs.getString("fact_data.date_collected");
			tmp[5] = dbf.rs.getString("fact_data.value");
			tmp[6] = dbf.rs.getString("metrics.name");
			
			if (dbf.rs.getString("fact_data.meta_set_id") == null)
				tmp[7] = "";
			else
				tmp[7] = dbf.rs.getString("fact_data.meta_set_id");
			
				
			
			arrayListRows2.add(tmp);
		    
		}
		bDone = true;
	}
	catch (SQLException sqle)
	{
		//out.println(sqle.toString());
		fulllogger.info("PF ERROR CODE 2eh2-2,subcode:"+code);
		fulllogger.info(sqle.getMessage());	
		if (!sqle.getMessage().contains("The last packet successfully received")) {
			bDone = true;
			out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 2eh2-2"));
			bException = true;
		}
	}
	finally
	{
		if (dbf !=null) dbf.closeConnection();
		if (bException == true)
			return;
	}
}


if (arrayListRows2.size() == 0) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_no_data","Query returned no data.","PF ERROR CODE 2eh2-4"));
	return;	
}


if (strGroupBy.compareToIgnoreCase("true") == 0) {
	int[] tmpArray = {0,1};
	arrayListRows2 = PopulateSpreadsheet.getLastGroupBy(arrayListRows2,tmpArray);
}




if (bDebug == true)
	out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows2,1000));
else {
	/*
	* Added function to duplicate the array elements since this is what createGoogleJSON expects.
	*/
	arrayListRows2 = PopulateSpreadsheet.duplicateArrayList(arrayListRows2);
	out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows2,strReqId,false));
}
 %> 
