<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Formatter" %>
<%@ page import="java.util.Locale" %>


<%

/*
* This data_source pulls from the fact_data based off of entities, a date range and a metric and
also pulls from log_alerts based off of either entity_id & user_id combo, or alert_id

NOTE: THIS DATASOURCE ONLY HANDLES A SINGLE ENTITY ID (OR TICKER OR ALERT).
Use msqldatasource15multiple.jsp for multiple ids.
*/

String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null) {
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";

String strGranularity = "day";
if (request.getParameter("gran")!=null)
	if (request.getParameter("gran").toUpperCase().equals("MINUTE"))
		strGranularity = "minute";

UtilityFunctions uf = new UtilityFunctions();

String strEntityId = request.getParameter("entityid");
String strMetricId = request.getParameter("metricid");
String strAlertId = request.getParameter("alertid");
String strUserId = request.getParameter("userid");
String strTicker = request.getParameter("ticker");
boolean bPercent = false;

boolean bTableFormat = false;

if (request.getParameter("table")!=null)
	if (request.getParameter("table").toUpperCase().equals("TRUE"))
		bTableFormat = true;



if (request.getParameter("percent")!=null)
	if (request.getParameter("percent").toUpperCase().equals("TRUE"))
		bPercent = true;

String strEndDate = request.getParameter("enddate");
String strBeginDate = request.getParameter("begindate"); 

if (strBeginDate==null) {
	out.println("No begindate request parameter.");
	return;
}

if ((strEntityId==null || strUserId==null) && strAlertId==null) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","Either AlertId or MetricId-EntityId-UserId combo is required.","PF ERROR CODE 15s-1"));
	return;
}



if (!strBeginDate.startsWith("20") && !strBeginDate.startsWith("19")) {
	//out.println("begindate format needs to be yyyy-mm-dd");
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","begindate format needs to be yyyy-mm-dd","PF ERROR CODE 15s-2"));
	return;
}

if (strEndDate!=null) {
	if (!strEndDate.startsWith("20") && !strEndDate.startsWith("19")) 	{
		//out.println("enddate format needs to be yyyy-mm-dd");
		out.println(PopulateSpreadsheet.createGoogleError(strReqId,"invalid_parameter","enddate format needs to be yyyy-mm-dd","PF ERROR CODE 15s-3"));
		return;
	}
}


	

boolean bQueryLogAlerts = false;

if (strUserId != null && !strUserId.equals("0"))
	bQueryLogAlerts = true;

/*
* Look up the id for each ticker.
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

if (strEntityId.contains(",") || strMetricId.contains(",")) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"invalid_parameter","This data source only accepts a single entity id and single metric id.","PF ERROR CODE 15s-4"));
	return;
}

//String[] entityIds = null;

/*if (strEntityId!=null)
	entityIds = strEntityId.split(",");*/

/*
 * Get the metricids for each entity id
 */ 
//String[] metricIds = new String[entityIds.length];
//String[] inputMetricsArray = strMetrics.split(",");

/*for (int i=0;i<metricIds.length;i++) {
	metricIds[i] = "0";
}*/

//int nCount = (inputMetricsArray.length<metricIds.length ? inputMetricsArray.length : metricIds.length);

/*for (int i=0;i<nCount;i++) {
	metricIds[i] = inputMetricsArray[i];
}*/

dbf = null;
bException = false;
dbf = new DBFunctions();
try	{
	
	//for (int i=0;i<metricIds.length;i++){
		if (strMetricId.equals("0")) {
			query = "select metric_id from entities_metrics where default_metric=1 AND entity_id=" + strEntityId;	
			dbf.db_run_query(query);
			if (dbf.rs.next())
				strMetricId = dbf.rs.getString("metric_id");
			else {
				out.println(PopulateSpreadsheet.createGoogleError(strReqId,"data_issue","No entry in entities_metrics","PF ERROR CODE 15s-6"));
				//technicall not an exception, should change the name of the variable
				bException = true;
			}
		}
	//}
}
catch (SQLException sqle) {
			
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15s-7"));
	bException = true;
			
}
catch (Exception e) {
	//System.out.println(e.getMessage());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"exception",e.getMessage(),"PF ERROR CODE 15s-8"));
	bException = true;
}
finally {
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
			
}
		
	


ArrayList<String[]> arrayListCols = new ArrayList<String[]>();





query = "select date_format(fact_data.date_collected,'%m-%d-%Y') as date_col, date_format(fact_data.date_collected,'%H:%i:%s') as time_col, ";
query += "fact_data.batch_id,fact_data.value as fdvalue,entities.ticker ";
query += "from fact_data ";
query += "JOIN entities on fact_data.entity_id=entities.id ";
query += "JOIN batches on fact_data.batch_id=batches.id ";
query += "JOIN tasks on batches.task_id=tasks.id ";
query += " where date_format(fact_data.date_collected,'%Y-%m-%d')>='" + strBeginDate + "' ";

if (strEndDate!=null && !strEndDate.isEmpty())
	query += " AND date_format(fact_data.date_collected,'%Y-%m-%d')<='" + strEndDate + "' ";
query += " AND entities.id=" + strEntityId;
query += " AND fact_data.metric_id=" + strMetricId;

query += " order by fact_data.date_collected ";

//out.println(query); if (1==1) return;


ArrayList<String[]> arrayListRows2 = new ArrayList<String[]>();

if (bQueryLogAlerts == true) {

	String query2 = "select count(log_alerts.id) as cnt,date_format(log_alerts.date_time_fired,'%m-%d-%Y') as datefired,entities.ticker ";
	query2 += " from log_alerts ";
	query2 += " JOIN alerts on log_alerts.alert_id=alerts.id ";
	query2 += " JOIN entities on entities.id=log_alerts.entity_id ";
	query2 += " JOIN tasks on alerts.task_id=tasks.id ";
	query2 += " JOIN entities_metrics on log_alerts.entity_id=entities_metrics.entity_id and tasks.metric_id=entities_metrics.metric_id ";
	query2 += " where 1=1 ";
	if (strAlertId==null) {
		query2 += " AND log_alerts.entity_id=" + strEntityId;
		query2 += " AND log_alerts.user_id=" + strUserId;
		
		if (strMetricId.equals("0"))
			query2 += " and entities_metrics.default_metric=1 ";
		else 
			query2 += " AND tasks.metric_id=" + strMetricId;
			
			
	}
	else
		query2 += " AND log_alerts.alert_id=" + strAlertId;
	query2 += " group by date_format(log_alerts.date_time_fired,'%m-%d-%Y')";
	
	
	
	
	dbf=null;
	bException=false;
	try	{
		dbf = new DBFunctions();
		dbf.db_run_query(query2);
		while (dbf.rs.next()) {
			String [] tmp = new String[3];
	
			tmp[0] = dbf.rs.getString("datefired");	
			tmp[1] = dbf.rs.getString("entities.ticker");
			tmp[2] = dbf.rs.getString("cnt");
	
					
			
			arrayListRows2.add(tmp);
		    
		}
	}
	catch (SQLException sqle) {
		//out.println(sqle.toString());
		out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15s-9"));
		bException = true;
	}
	finally	{
		if (dbf !=null) dbf.closeConnection();
		if (bException == true)
			return;
	}
	
}





dbf = null;
bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

int nCount = 0;
try {

	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		String [] tmp = new String[4];
		tmp[0] = dbf.rs.getString("date_col");
		tmp[1] = dbf.rs.getString("entities.ticker");
		//if (bPercent == false)
		tmp[2] = dbf.rs.getString("fdvalue");
		/*else {
			if (nCount == 0) {
				fInitial = Float.parseFloat(dbf.rs.getString("fdvalue"));
				tmp[2] = "0";	
			}
			else {
				Formatter formatter1 = new Formatter();
				float fCurrent = Float.parseFloat(dbf.rs.getString("fdvalue"));
				float fPercent = 0;
				if (fInitial!=0)
					fPercent = (((fCurrent-fInitial)/fInitial) * 100);
				formatter1.format(Locale.US,"%2.3f",fPercent);
				tmp[2] = formatter1.toString();
			}
			
			
			
		}*/
		tmp[3] = dbf.rs.getString("time_col");

				
		
		arrayListRows.add(tmp);
		
		nCount++;
	    
	}
}
catch (SQLException sqle) {
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15s-10"));
	bException = true;
}
finally {
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}


if (arrayListRows.size() == 0) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_no_data","Query returned no data.","PF ERROR CODE 15s-11"));
	return;	
}






//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,100));if (1==1) return; 

if (strGranularity.equals("day")) {
	int[] tmpArray= {0,1};
	arrayListRows = PopulateSpreadsheet.getLastGroupBy(arrayListRows,tmpArray);
	arrayListRows = PopulateSpreadsheet.removeLastColumn(arrayListRows);
}





if (strGranularity.equals("minute")) {
	for (int i=0;i<arrayListRows.size();i++) {
		String[] row = arrayListRows.get(i);
		
		row[0] += " " + row[3];
		
		arrayListRows.set(i,row);
		
		
	}
	
	arrayListRows = PopulateSpreadsheet.removeLastColumn(arrayListRows);
	
	
	
}


/* OFP 6/1/2012 - Move the % change calculation after doing the group by */


//ArrayList<String[]> tmpList = new ArrayList<String[]>();
float fInitial = 0;
if (bPercent == true) {
	int i=0;
	for (String[] tmp : arrayListRows) {
		if (i==0) {
			fInitial = Float.parseFloat(tmp[2]);
			tmp[2] = "0";
		}
		else {
			Formatter formatter1 = new Formatter();
			float fCurrent = Float.parseFloat(tmp[2]);
			float fPercent = 0;
			if (fInitial!=0)
				fPercent = (((fCurrent-fInitial)/fInitial) * 100);
			formatter1.format(Locale.US,"%2.3f",fPercent);
			tmp[2] = formatter1.toString();	
		}
	
		i++;
	}
}

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 



arrayListRows = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows);

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 


if (bQueryLogAlerts) {

	if (arrayListRows2.size()!=0)
		arrayListRows2 = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows2);
	
	
	
	arrayListRows = PopulateSpreadsheet.joinListsLeftOuter(arrayListRows,arrayListRows2,0);
}


//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 



/*
*
* If doing a pivot, have to populate the columns after the rows
*/

String[] pivotColumns = arrayListRows.remove(0);

String[] col1 = {"date_collected","date collected","date"};
arrayListCols.add(col1);
for (int i=0;i<pivotColumns.length;i++) {
	String[] col3 = new String[3];
	col3[0] = pivotColumns[i];
	col3[1] = pivotColumns[i];
	col3[2] = "number";
	
	arrayListCols.add(col3);
	
	String[] col4 = new String[3];
	col4[0] = "title";
	col4[1] = "title";
	col4[2] = "string";
	
	arrayListCols.add(col4);
	
	String[] col5 = new String[3];
	
	col5[0] = "text";
	col5[1] = "text";
	col5[2] = "string";
	
	arrayListCols.add(col5);
	
	
}

/*
* OFP 4/29/2012 - This uses extra memory to create a new arraylist but leaving as is for now. Also not
sure how ArrayList handles set() calls inside a foreach.
*/


ArrayList<String[]> saveListRows = arrayListRows;

arrayListRows = new ArrayList<String[]>();


for (String[] tmpSave : saveListRows) {

	String[] tmp = new String[8];

	
	tmp[0] = tmp[1] = tmpSave[0];
	tmp[2] = tmp[3] = tmpSave[1];

	if (tmpSave.length > 2) {
		tmp[4] = tmp[5] = "<i>Alert Count</i>";
		tmp[6] = tmp[7] = tmpSave[2];
	}
	else {
		tmp[4] = tmp[5] = null;
		tmp[6] = tmp[7] = null;
	}
	
	arrayListRows.add(tmp);
	
}



//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return;


/*
* If not using percent values, then we will not display zero values since they are typically bad prints.
*/
boolean bEmptyZeroes = true;
if (bPercent)
	bEmptyZeroes = false;


if (bTableFormat==true)
	out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));
else
	out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,bEmptyZeroes));
	
	




 










%> 
