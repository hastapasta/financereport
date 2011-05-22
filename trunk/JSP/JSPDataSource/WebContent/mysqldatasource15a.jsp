<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.math.BigDecimal" %>

 



<%

/*
* This data_source pulls from the fact_data based off of entities, a date range and a metric.

This is similar to mysqldatasource15 except it doesn't use annotations, it can handle multiple entities 

and it returns pctchange.
*/



String strEntityId = request.getParameter("entityid");
String strMetricId = request.getParameter("metricid");
String strUserId = request.getParameter("userid");
String[] entityIds = null;
if (strEntityId!=null)
	entityIds = strEntityId.split(",");
String strEndDate = request.getParameter("enddate");
String strBeginDate = request.getParameter("begindate"); 

ArrayList<String[]> listInitVals = new ArrayList<String[]>();

if (strBeginDate==null)
{
	out.println("No begindate request parameter.");
	return;
}

if (strMetricId == null)
{
	out.println("No metricid parameter. Exiting.");
	return;
}

if (strEntityId == null)
{
	out.println("No entityid parameter. Exiting.");
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

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();

String strInClause="in (";
for (int i=0;i<entityIds.length;i++)
{
	if (i!=0)
		strInClause += ",";
	strInClause += entityIds[i];
}
strInClause += ") ";

DBFunctions dbf = null;
dbf = new DBFunctions();

boolean bException = false;

/*
* Obtain the initial value for all of the entities.
*/
try
{
	for (int i=0;i<entityIds.length;i++)
	{
		String query2 = "select value,entity_id ";
		query2 += " from fact_data ";
		query2 += " where entity_id=" + entityIds[i] + " ";
		query2 += " and date_collected>'" + strBeginDate + "' ";
		query2 += " order by date_collected ASC ";
		query2 += " limit 1";
		
		dbf.db_run_query(query2);
		dbf.rs.next();
		
		String[] tmp = new String[2];
		tmp[0] = dbf.rs.getString("value");
		tmp[1] = dbf.rs.getString("entity_id");
		
		listInitVals.add(tmp);
		
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15a-1"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}



String query = "select date_format(fact_data.date_collected,'%m-%d-%Y') as date_col, date_format(fact_data.date_collected,'%H:%m:%s') as time_col, ";
query += "fact_data.value as fdvalue,entities.ticker,entities.id,entities.ticker  ";
//query += "(if (initval=0,initval,round(((value - initval)/initval) * 100,2)))as pctchange ";
query += "from fact_data ";
query += "JOIN entities on fact_data.entity_id=entities.id ";
//query += " join (" + query3 + ") as t1 on t1.entity_id=fact_data.entity_id ";
//query += "JOIN tasks on fact_data.task_id=tasks.id ";
query += " where entities.id " + strInClause;
query += " and fact_data.metric_id=" + strMetricId;
query += " AND date_format(fact_data.date_collected,'%Y-%m_%d')>'" + strBeginDate + "' ";
if (strEndDate!=null && !strEndDate.isEmpty())
	query += " AND date_format(fact_data.date_collected,'%Y-%m-%d')<'" + strEndDate + "' ";
//query += " group by date_format(fact_data.date_collected,'%Y-%m-%d'),entities.ticker,fact_data.value ";
query += " order by date_col ASC ,entities.ticker ASC, time_col ASC";




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
		tmp[0] = dbf.rs.getString("date_col");
		tmp[1] = dbf.rs.getString("entities.id");	
		tmp[2] = dbf.rs.getString("fdvalue");
		tmp[3] = dbf.rs.getString("entities.ticker");

				
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15a-2"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}

ArrayList<String[]> arrayListRows2 = new ArrayList<String[]>();



int[] tmpArray = {0,1};
arrayListRows = PopulateSpreadsheet.getLastGroupBy(arrayListRows,tmpArray,3);


//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows));if (1==1) return; 

arrayListRows = PopulateSpreadsheet.joinListsOuter(arrayListRows,listInitVals,1);




//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows));if (1==1) return; 

for (int i=0;i<arrayListRows.size();i++)
{
	String[] tmp = arrayListRows.get(i);
	
	BigDecimal bdBegin = new BigDecimal(tmp[2]);
	BigDecimal bdEnd = new BigDecimal(tmp[4]);
	
	bdEnd = bdEnd.subtract(bdBegin).divide(bdBegin,BigDecimal.ROUND_HALF_UP);
	
	tmp[5] = bdEnd.toString();
	
	arrayListRows.set(i,tmp);
	
}

arrayListRows = PopulateSpreadsheet.removeLastColumn(arrayListRows);


arrayListRows = PopulateSpreadsheet.removeColumn(arrayListRows,4);
arrayListRows = PopulateSpreadsheet.removeColumn(arrayListRows,2);
arrayListRows = PopulateSpreadsheet.removeColumn(arrayListRows,1);



arrayListRows = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows);

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows));if (1==1) return; 




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
	
	
}

//String[] col2 = {"value","value1","number"};
//arrayListCols.add(col2);

ArrayList<String[]> saveListRows = arrayListRows;

arrayListRows = new ArrayList<String[]>();


for (int i=0;i<saveListRows.size();i++)
{
	String[] tmpSave = saveListRows.get(i);
	String[] tmp = new String[(2*tmpSave.length)];
	
	
	for (int j=0;j<tmpSave.length;j++)
	{
		tmp[(j*2)] = tmp[(j*2)+1] = tmpSave[j];
	}
	

	
	
	arrayListRows.add(tmp);
	
}








out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,true));



%> 
