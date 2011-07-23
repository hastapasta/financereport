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

 



<%

/*
* This data_source pulls from the fact_data based off of entities, a date range and a metric and
also pulls from log_alerts based off of either entity_id & user_id combo, or alert_id

NOTE: This datasource, which also provides annotated alerts, can only handle a single entity id.
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



String strEntityId = request.getParameter("entityid");
String strMetrics = request.getParameter("metricid");
String strTickers = request.getParameter("tickers");
String strTaskIds = request.getParameter("taskid");

boolean bDebug = false;
if (request.getParameter("debug")!=null)
	if (request.getParameter("debug").toUpperCase().equals("TRUE"))
		bDebug = true;



//String strUserId = request.getParameter("userid");

String strEndDate = request.getParameter("enddate");
String strBeginDate = request.getParameter("begindate"); 

if (strBeginDate==null) {
	out.println("No begindate request parameter.");
	return;
}

/*if (strMetricId == null)
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","metricid is required","15multiple-1"));
	return;
}*/

if (strEntityId == null && strTickers == null) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","either entityid or tickers is required","15multiple-2"));
	return;
	
}



if (!strBeginDate.startsWith("20") && !strBeginDate.startsWith("19"))
{
	out.println("begindate format needs to be yyyy-mm-dd");
	return;
}

if (strEndDate!=null)
{
	if (!strEndDate.startsWith("20") && !strEndDate.startsWith("19"))
	{
		out.println("enddate format needs to be yyyy-mm-dd");
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
	
	

	
	try
	{
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
	catch (SQLException sqle)
	{
		//out.println(sqle.toString());
		out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15-2"));
		bException = true;
	}
	finally
	{
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
		query = "select default_metric from entities_metrics where entity_id=" + entityIds[i];
		
		dbf = null;
		bException = false;

		try	{
			dbf = new DBFunctions();
			dbf.db_run_query(query);
			dbf.rs.next();
			metricIds[i] = dbf.rs.getString("default_metric");
		}
		catch (SQLException sqle) {
			
			out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15-2"));
			bException = true;
			
		}
		finally {
			if (dbf !=null) dbf.closeConnection();
			if (bException == true)
				return;
			
		}
		
		
		
		
	}
	
	
	
}

/*
 * Set up the task ids.
 */ 

 String[] taskIds = new String[entityIds.length];
for (int i=0;i<entityIds.length;i++) {
	taskIds[i] = "0";

}

if (strTaskIds != null) {
	String[] tmpTaskArray = strTaskIds.split(",");
	nCount = (tmpTaskArray.length > taskIds.length ? taskIds.length : tmpTaskArray.length);
	for (int i=0;i<nCount;i++) {
		taskIds[i] = tmpTaskArray[i];
	}
	
}
	










UtilityFunctions uf = new UtilityFunctions();

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();

String strInClause="in (";
for (int i=0;i<entityIds.length;i++)
{
	if (i!=0)
		strInClause += ",";
	strInClause += entityIds[i];
}
strInClause += ") ";
//String[] columns = tmpArrayList.get(0);

//DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");






//ideally i'd like to use last() here but mysql doesn't support it - it would have to be 
//coded by hand - so going with max() instead.
query = "select date_format(fact_data.date_collected,'%Y-%m') as date_col, date_format(fact_data.date_collected,'%d-%H:%m:%s') as time_col, ";
query += "fact_data.batch,fact_data.value as fdvalue,entities.ticker as ticker,date_format(fact_data.date_collected,'%Y-%m-%d') as date_col2 ";
query += " from fact_data ";
query += "JOIN entities on fact_data.entity_id=entities.id ";
/*if (strMetricId.equals("0"))
	query += "JOIN entities_metrics on fact_data.entity_id=entities_metrics.entity_id ";*/
query += " JOIN tasks on fact_data.task_id=tasks.id ";
query += " JOIN metrics on fact_data.metric_id = metrics.id ";
query += " where (1=1) AND (";

for (int i=0;i<entityIds.length-2;i++) {
	if (i!=0)
		query += " OR ";
	query += "(entities.id= " + entityIds[i] + " AND fact_data.metric_id=" + metricIds[i];
	if (!taskIds[i].equals("0"))
		query += " AND tasks.id=" + taskIds[i];
	                                                                                                                                                            
    query += ") ";
} 

	

query += ") AND date_format(fact_data.date_collected,'%Y-%m_%d')>'" + strBeginDate + "' ";
if (strEndDate!=null && !strEndDate.isEmpty())
	query += " AND date_format(fact_data.date_collected,'%Y-%m-%d')<'" + strEndDate + "' ";

query += " UNION ";
query += " select concat(calyear+1,'-01') as date_col, '01-00:00:00' as time_col, fact_data.batch,fact_data.value as fdvalue,'gdp' as ticker ";
query += " ,concat(calyear+1,'-01-01') date_col2 ";
query += " from fact_data ";
query += " JOIN entities on fact_data.entity_id=entities.id ";
/*if (strMetricId.equals("0"))
	query += "JOIN entities_metrics on fact_data.entity_id=entities_metrics.entity_id ";*/
query += " JOIN tasks on fact_data.task_id=tasks.id ";
query += " JOIN metrics on fact_data.metric_id = metrics.id ";
query += " where (1=1) AND (";
for (int i=entityIds.length-2;i<entityIds.length;i++) {
	if (i!=entityIds.length-2)
		query += " OR ";
	query += "(entities.id= " + entityIds[i] + " AND fact_data.metric_id=" + metricIds[i];
	if (!taskIds[i].equals("0"))
		query += " AND tasks.id=" + taskIds[i];
	                                                                                                                                                            
    query += ") ";
} 

query += ") AND concat(calyear,'-01-01')>='" + strBeginDate + "' ";
if (strEndDate!=null && !strEndDate.isEmpty())
	query += " AND concat(calyear,'-01-01')<'" + strEndDate + "' ";

query += " UNION ";
query += "select concat(calyear+1,'-01') as date_col, '01-00:00:00' as time_col, fact_data.batch,fact_data.value as fdbalue, 'gdp' as ticker ";
query += " ,concat(calyear+1,'-01-01') date_col2 ";
query += " from fact_data ";
query += " JOIN entities on fact_data.entity_id=entities.id ";
query += " JOIN tasks on fact_data.task_id=tasks.id ";
query += " JOIN metrics on fact_data.metric_id = metrics.id ";
query += " where (1=1) AND (";
for (int i=entityIds.length-2;i<entityIds.length;i++) {
	if (i!=entityIds.length-2)
		query += " OR ";
	query += "(entities.id= " + entityIds[i] + " AND fact_data.metric_id=" + metricIds[i];
	if (!taskIds[i].equals("0"))
		query += " AND tasks.id=" + taskIds[i];
	                                                                                                                                                            
    query += ") ";
} 
query += ") AND calyear+1=year('" + strBeginDate + "')";
query += " order by date_col ASC ,ticker ASC, time_col ASC";




//out.println(query); if (1==1) return;





dbf = null;
bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		String [] tmp = new String[4];
		
		/*if ((dbf.rs.getInt("fact_data.metric_id") == 2) || (dbf.rs.getInt("fact_data.metric_id") == 3)) {
			tmp[0] = "12-31-" + dbf.rs.getString("fact_data.calyear");
			tmp[1] = dbf.rs.getString("metrics.name");
			tmp[2] = dbf.rs.getString("fdvalue");
			tmp[3] = "00:00:01";
		}
		else {*/
			tmp[0] = dbf.rs.getString("date_col");
			tmp[1] = dbf.rs.getString("ticker");
			tmp[2] = dbf.rs.getString("fdvalue");
			tmp[3] = dbf.rs.getString("date_col2");
		//}


		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15-2"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}



int[] tmpArray = {0,1};




arrayListRows = PopulateSpreadsheet.getFirstGroupBy(arrayListRows,tmpArray);

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 

arrayListRows = PopulateSpreadsheet.removeLastColumn(arrayListRows);



arrayListRows = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows);

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 







/*
*
* If doing a pivot, have to populate the columns after the rows
*/

String[] pivotColumns = arrayListRows.remove(0);

String[] col1 = {"date_collected","date collected","string"};
arrayListCols.add(col1);
//out.println(PopulateSpreadsheet.displayDebugTable(pivotColumns,1000));if (1==1) return;

for (int i=0;i<pivotColumns.length;i++)
{
	
	
		String[] col3 = new String[3];
		col3[0] = pivotColumns[i];
		col3[1] = pivotColumns[i];
		col3[2] = "number";
		
		arrayListCols.add(col3);
		
}





ArrayList<String[]> saveListRows = arrayListRows;

arrayListRows = new ArrayList<String[]>();


String[] initvals = new String[pivotColumns.length];
for(String item : initvals) {
	item = null;
}




for (int i=0;i<saveListRows.size();i++)
{
	String[] tmpSave = saveListRows.get(i);
	
	String[] tmp = new String[2 + ((pivotColumns.length) * 2)];
	
	
	
	tmp[0] = tmp[1] = tmpSave[0];
	
	
	
	for (int j=0;j<pivotColumns.length;j++) {
		

	
		if (tmpSave[j+1] == null || tmpSave[j+1].isEmpty())
			tmp[2 + (j * 2)] = tmp[3 + (j * 2)] = "";
		else {
			if (initvals[j] == null)
				initvals[j] = tmpSave[j+1];
			
			float f1 = Float.parseFloat(initvals[j]);
			float f2 = Float.parseFloat(tmpSave[j+1]);
			
			float f3 = (((f2-f1)/f1) * 100);
			if (f1 != 0) {
				Formatter formatter1 = new Formatter();
				formatter1.format(Locale.US,"%2.3f",f3);
				tmp[2 + (j * 2)] = tmp[3 + (j * 2)] = formatter1.toString();
				//formatter1.flush();
			}
			else 
				tmp[2 + (j * 2)] = tmp[3 + (j * 2)] = "0";
			

			
			
		}
					
		
		
	}
	
	arrayListRows.add(tmp);
	
}





if (bDebug == true) {
	out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 
}




















out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,false));



%> 
