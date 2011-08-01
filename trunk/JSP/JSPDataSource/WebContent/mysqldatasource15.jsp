<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>

 



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
String strMetricId = request.getParameter("metricid");
String strAlertId = request.getParameter("alertid");
String strUserId = request.getParameter("userid");

String strEndDate = request.getParameter("enddate");
String strBeginDate = request.getParameter("begindate"); 

if (strBeginDate==null)
{
	out.println("No begindate request parameter.");
	return;
}

if ((strMetricId == null || strEntityId==null || strUserId==null) && strAlertId==null)
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","Either AlertId or MetricId-EntityId-UserId combo is required.","15-1"));
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










UtilityFunctions uf = new UtilityFunctions();

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();




//ideally i'd like to use last() here but mysql doesn't support it - it would have to be 
//coded by hand - so going with max() instead.
String query = "select date_format(fact_data.date_collected,'%m-%d-%Y') as date_col, date_format(fact_data.date_collected,'%H:%i:%s') as time_col, ";
query += "fact_data.batch_id,fact_data.value as fdvalue,entities.ticker  ";
query += "from fact_data ";
query += "JOIN entities on fact_data.entity_id=entities.id ";
query += "JOIN batches on fact_data.batch_id=batches.id ";
if (strMetricId.equals("0")) {
	query += "JOIN entities_metrics on fact_data.entity_id=entities_metrics.entity_id ";
}
query += "JOIN tasks on batches.task_id=tasks.id ";

query += " where entities.id=" + strEntityId;
if (strMetricId.equals("0")) {
	query += " and entities_metrics.default_metric=1 ";
	query += " and entities_metrics.metric_id=tasks.metric_id ";
}
else
	query += " and tasks.metric_id=" + strMetricId;
query += " AND date_format(fact_data.date_collected,'%Y-%m-%d')>'" + strBeginDate + "' ";
if (strEndDate!=null && !strEndDate.isEmpty())
	query += " AND date_format(fact_data.date_collected,'%Y-%m-%d')<'" + strEndDate + "' ";
//query += " group by date_format(fact_data.date_collected,'%Y-%m-%d'),entities.ticker,fact_data.value ";
query += " order by date_col ASC ,entities.ticker ASC, time_col ASC";

String query2 = "select count(log_alerts.id) as cnt,date_format(log_alerts.date_time_fired,'%m-%d-%Y') as datefired,entities.ticker ";
query2 += " from log_alerts ";
query2 += " JOIN alerts on log_alerts.alert_id=alerts.id ";
query2 += " JOIN entities on entities.id=log_alerts.entity_id ";
query2 += " JOIN tasks on alerts.task_id=tasks.id ";
query2 += " JOIN entities_metrics on log_alerts.entity_id=entities_metrics.entity_id ";
query2 += " where 1=1 ";
if (strAlertId==null)
{
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



//out.println(query); if (1==1) return;





DBFunctions dbf = null;
boolean bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		String [] tmp = new String[4];
		tmp[0] = dbf.rs.getString("date_col");
		tmp[1] = dbf.rs.getString("entities.ticker");	
		tmp[2] = dbf.rs.getString("fdvalue");
		tmp[3] = dbf.rs.getString("time_col");
				
		
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

ArrayList<String[]> arrayListRows2 = new ArrayList<String[]>();

dbf=null;
bException=false;
try
{
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
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15-3"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}

int[] tmpArray = {0,1};
arrayListRows = PopulateSpreadsheet.getLastGroupBy(arrayListRows,tmpArray);






arrayListRows = PopulateSpreadsheet.removeLastColumn(arrayListRows);

arrayListRows = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows);


if (arrayListRows2.size()!=0)
	arrayListRows2 = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows2);



arrayListRows = PopulateSpreadsheet.joinListsOuter(arrayListRows,arrayListRows2,0);






/*
*
* If doing a pivot, have to populate the columns after the rows
*/

String[] pivotColumns = arrayListRows.remove(0);

String[] col1 = {"date_collected","date collected","date"};
arrayListCols.add(col1);
for (int i=0;i<pivotColumns.length;i++)
{
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

//String[] col2 = {"value","value1","number"};
//arrayListCols.add(col2);

ArrayList<String[]> saveListRows = arrayListRows;

arrayListRows = new ArrayList<String[]>();


for (int i=0;i<saveListRows.size();i++)
{
	String[] tmpSave = saveListRows.get(i);
	//String[] tmp = new String[(2*tmpSave.length)];
	String[] tmp = new String[8];
	
	/*for (int j=0;j<tmpSave.length;j++)
	{
		tmp[(j*2)] = tmp[(j*2)+1] = tmpSave[j];
	}*/
	
	tmp[0] = tmp[1] = tmpSave[0];
	tmp[2] = tmp[3] = tmpSave[1];
	/*tmp[4] = tmp[5] = "Alert Count";
	tmp[6] = tmp[7] = tmpSave[2];*/
	if (tmpSave[2] != null)
	{
		tmp[4] = tmp[5] = "<i>Alert Count</i>";
		tmp[6] = tmp[7] = tmpSave[2];
	}
	else
	{
		tmp[4] = tmp[5] = null;
		tmp[6] = tmp[7] = null;
	}
	
	arrayListRows.add(tmp);
	
}

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 






out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,true));



%> 
