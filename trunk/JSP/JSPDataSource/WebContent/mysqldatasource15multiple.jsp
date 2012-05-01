<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Formatter" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.apache.log4j.Logger" %>

 



<%

/*
* This data_source pulls from the fact_data based off of entities, a date range and a metric and
also pulls from log_alerts based off of either entity_id & user_id combo, or alert_id

NOTE: This datasource, which also provides annotated alerts, can only handle a single entity id.
*/


Logger fulllogger = Logger.getLogger("FullLogging");
UtilityFunctions uf = new UtilityFunctions();

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



String strEntityId = request.getParameter("entityid");
String strMetrics = request.getParameter("metricid");
String strTickers = request.getParameter("tickers");
String strTaskIds = request.getParameter("taskid");

boolean bTableFormat = false;

if (request.getParameter("table")!=null)
	if (request.getParameter("table").toUpperCase().equals("TRUE"))
		bTableFormat = true;


boolean bPercent = true;
if (request.getParameter("percent")!=null)
	if (request.getParameter("percent").toUpperCase().equals("FALSE"))
		bPercent = false;



//String strUserId = request.getParameter("userid");

String strEndDate = request.getParameter("enddate");
String strBeginDate = request.getParameter("begindate"); 

if (strBeginDate==null) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","No begindate request parameter.","PF ERROR CODE 15multiple-1"));
	//out.println("No begindate request parameter.");
	return;
}

/*if (strMetricId == null)
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","metricid is required","15multiple-1"));
	return;
}*/

if (strEntityId == null && strTickers == null) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","either entityid or tickers is required","PF ERROR CODE 15multiple-2"));
	return;
	
}


String[] tmpArray2 = strBeginDate.split("-");



if ((!strBeginDate.startsWith("20") && !strBeginDate.startsWith("19")) || !(tmpArray2[1].length()==2)) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"invalid_parameter","begindate format needs to be yyyy-mm-dd","PF ERROR CODE 15multiple-3"));
	//out.println("begindate format needs to be yyyy-mm-dd");
	return;
}

if (strEndDate!=null) {
	tmpArray2 = strEndDate.split("-");
	if ((!strEndDate.startsWith("20") && !strEndDate.startsWith("19")) || !(tmpArray2[1].length()==2)) {
		out.println(PopulateSpreadsheet.createGoogleError(strReqId,"invalid_parameter","enddate format needs to be yyyy-mm-dd","PF ERROR CODE 15multiple-4"));
		//out.println("enddate format needs to be yyyy-mm-dd");
		return;
	}
}

/*
* Look up the id for each ticker.
*/
String query;
DBFunctions dbf;
boolean bException;

if (strTickers != null) {


	String[] tickerarray = strTickers.split(",");
	dbf = null;
	bException = false;
	
	
	
	
	try {
		query = "select id from entities where ticker in (";
		for (String strTick : tickerarray) { 
			query += "'" + strTick + "',";
		}
		query = query.substring(0,query.length()-1);
		query += ")";
		
		dbf = new DBFunctions();
		dbf.db_run_query(query);
		strEntityId="";
		while (dbf.rs.next()) {
			strEntityId +=  dbf.rs.getInt("id") + ",";
		    
		}
		strEntityId = strEntityId.substring(0,strEntityId.length()-1);
	}
	catch (SQLException sqle) {
		//out.println(sqle.toString());
		out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15multiple-5"));
		bException = true;
	}
	finally	{
		if (dbf !=null) dbf.closeConnection();
		if (bException == true)
			return;
	}
}

String[] entityIds = null;
if (strEntityId!=null)
	entityIds = strEntityId.split(",");

/*
 * Get the metricids for each entity id
 */ 
String[] metricIds = new String[entityIds.length];
String[] inputMetricsArray = strMetrics.split(",");

for (int i=0;i<metricIds.length;i++) {
	metricIds[i] = "0";
}

int nCount = (inputMetricsArray.length<metricIds.length ? inputMetricsArray.length : metricIds.length);

for (int i=0;i<nCount;i++) {
	metricIds[i] = inputMetricsArray[i];
}

for (int i=0;i<metricIds.length;i++){
	if (metricIds[i].equals("0")) {
		query = "select metric_id from entities_metrics where default_metric=1 AND entity_id=" + entityIds[i];
		
		dbf = null;
		bException = false;

		try	{
			dbf = new DBFunctions();
			dbf.db_run_query(query);
			dbf.rs.next();
			metricIds[i] = dbf.rs.getString("metric_id");
		}
		catch (SQLException sqle) {
			
			out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception","entity_id not in entities_metrics","PF ERROR CODE 15multiple-6"));
			bException = true;
			
		}
		finally {
			if (dbf !=null) dbf.closeConnection();
			if (bException == true)
				return;
			
		}
		
		
		
		
	}
	
	
	
}


ArrayList<String[]> arrayListCols = new ArrayList<String[]>();

String strInClause="in (";
for (int i=0;i<entityIds.length;i++) {
	if (i!=0)
		strInClause += ",";
	strInClause += entityIds[i];
}
strInClause += ") ";


//ideally i'd like to use last() here but mysql doesn't support it - it would have to be 
//coded by hand - so going with max() instead.

query = "select date_format(fact_data.date_collected,'%Y-%m-%d') as date_col, date_format(fact_data.date_collected,'%H:%i:%s') as time_col, ";
query += "fact_data.batch_id,fact_data.value as fdvalue,entities.ticker,fact_data.metric_id,metrics.name,fact_data.calyear ";
query += "from fact_data ";
query += "JOIN entities on fact_data.entity_id=entities.id ";
query += "JOIN batches on fact_data.batch_id = batches.id ";
query += " JOIN tasks on batches.task_id=tasks.id ";
query += " JOIN metrics on fact_data.metric_id = metrics.id ";
query += " where (1=1) AND (";

for (int i=0;i<entityIds.length;i++) {
	if (i!=0)
		query += " OR ";
	query += "(entities.id= " + entityIds[i] + " AND fact_data.metric_id=" + metricIds[i];
	                                                                                                                                                            
    query += ") ";
} 



	

query += ") AND date_format(fact_data.date_collected,'%Y-%m-%d')>'" + strBeginDate + "' ";
if (strEndDate!=null && !strEndDate.isEmpty())
	query += " AND date_format(fact_data.date_collected,'%Y-%m-%d')<'" + strEndDate + "' ";

query += " order by date_col ASC ,entities.ticker ASC, time_col ASC";



//out.println(query); if (1==1) return;





dbf = null;
bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

try {
	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		String [] tmp = new String[4];
		
		/*
		* OFP 4/29/2012 - Date was in the format of Y-m-d from the query for sorting purposes.
		Now we have to flip it back around to m-d-Y.
		*/
		
		String[] tmpDate = dbf.rs.getString("date_col").split("-");
		tmp[0] = tmpDate[1] + "-" + tmpDate[2] + "-" + tmpDate[0];
		
		//tmp[0] = dbf.rs.getString("date_col");
		tmp[1] = dbf.rs.getString("entities.ticker");
		tmp[2] = dbf.rs.getString("fdvalue");
		tmp[3] = dbf.rs.getString("time_col");



		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle) {
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15multiple-7"));
	bException = true;
}
finally {
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return;


//int[] tmpArray = {0,1};




//arrayListRows = PopulateSpreadsheet.getLastGroupBy(arrayListRows,tmpArray);

if (arrayListRows.size()==0) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"no_data","No data returned from query.","PF ERROR CODE 15multiple-8"));
	return;
}
	



if (strGranularity.equals("day")) {
	int[] tmpArray = {0,1};
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

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return;

//arrayListRows = PopulateSpreadsheet.removeLastColumn(arrayListRows);

/*
*
* If doing a pivot, have to populate the columns after the rows
*/


arrayListRows = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows);

String[] pivotColumns = arrayListRows.remove(0);

String[] col1 = {"date_collected","date collected","date"};
arrayListCols.add(col1);
//out.println(PopulateSpreadsheet.displayDebugTable(pivotColumns,1000));if (1==1) return;

for (int i=0;i<pivotColumns.length;i++) {
	
	
		String[] col3 = new String[3];
		col3[0] = pivotColumns[i];
		col3[1] = pivotColumns[i];
		col3[2] = "number";
		
		arrayListCols.add(col3);
		
}





ArrayList<String[]> saveListRows = arrayListRows;

arrayListRows = new ArrayList<String[]>();


String[] initvals = new String[entityIds.length];
for(String item : initvals) {
	item = null;
}




for (int i=0;i<saveListRows.size();i++) {
	String[] tmpSave = saveListRows.get(i);
	
	String[] tmp = new String[2 + (entityIds.length * 2)];
	
	
	
	tmp[0] = tmp[1] = tmpSave[0];
	

	for (int j=0;j<tmpSave.length-1;j++) {
		
		if (bPercent==true) {
	
			if (tmpSave[j+1] == null || tmpSave[j+1].isEmpty())
				tmp[2 + (j * 2)] = tmp[3 + (j * 2)] = "";
			else {
				if (initvals[j] == null)
					initvals[j] = tmpSave[j+1];
				
				float f1 = Float.parseFloat(initvals[j]);
				float f2 = Float.parseFloat(tmpSave[j+1]);
				float f3 = 0;
				
				if (f1!=0)
					f3 = (((f2-f1)/f1) * 100);
				
				Formatter formatter1 = new Formatter();
				formatter1.format(Locale.US,"%2.3f",f3);
				tmp[2 + (j * 2)] = tmp[3 + (j * 2)] = formatter1.toString();
	
				
			}
		}
		else {
			tmp[2+ (j * 2)] = tmp[3 + (j * 2)] = tmpSave[j+1];
		}
					
		
		
	}
	
	arrayListRows.add(tmp);
	
}




//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 



if (bTableFormat==true)
	out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));
else
	out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,true));









%> 
