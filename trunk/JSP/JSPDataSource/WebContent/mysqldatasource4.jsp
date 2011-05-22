<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>




<%

/*
* This data_source pulls from the fact_data based off of entities and a date range and returns pctchange.
* This data_source is similar to datasource1 but datasource1 returns actual value and this one pctchange.
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
String[] entityIds = null;
if (strEntityId!=null)
	entityIds = strEntityId.split(",");
String strEndDate = request.getParameter("enddate");
String strBeginDate = request.getParameter("begindate"); 

if (strEntityId==null)
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","No entityid request parameter.","PF ERROR CODE 4-1"));
	//out.println("No begindate request parameter.");
	return;
}

if (!strBeginDate.startsWith("20") && !strBeginDate.startsWith("19"))
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"badformat_parameter","begindate format needs to be yyyy-mm-dd","PF ERROR CODE 4-2"));
	//out.println("begindate format needs to be yyyy-mm-dd");
	return;
}

if (!strEndDate.startsWith("20") && !strEndDate.startsWith("19"))
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"badformat_parameter","enddate format needs to be yyyy-mm-dd","PF ERROR CODE 4-3"));
	//out.println("enddate format needs to be yyyy-mm-dd");
	return;
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

String query2 = "select max(value) as initval,entity_id ";
query2 += "from fact_data ";
query2 += "where date_format(date_collected,'%m-%d-%y') = ";
query2 += 	"(select min(date_format(date_collected,'%m-%d-%y')) from fact_data where entity_id=" + entityIds[0] + " AND date_collected>'" + strBeginDate + "') ";
query2 += " AND entity_id=" + entityIds[0];

//out.println(query2); if (1==1) return;

//ideally i'd like to use last() here but mysql doesn't support it - it would have to be 
//coded by hand - so going with max() instead.
String query = "select max(fact_data.value) as fdvalue,(max(fact_data.value) - t1.initval)/t1.initval as pctchange,fact_data.date_collected,entities.ticker  ";
query += "from fact_data ";
query += "JOIN entities on fact_data.entity_id=entities.id ";
query += "JOIN tasks on fact_data.task_id=tasks.id ";
query += "JOIN (" + query2 + ") as t1 on t1.entity_id=entities.id";

query += " where entities.id " + strInClause;
query += " AND date_collected>'" + strBeginDate + "' ";
if (strEndDate!=null && !strEndDate.isEmpty())
	query += " AND date_collected<'" + strEndDate + "' ";
query += " group by date_format(fact_data.date_collected,'%m-%d-%y'),entities.ticker ";
query += " order by fact_data.date_collected ASC";

//out.println(query); if (1==1) return;





DBFunctions dbf = null;
boolean bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		String [] tmp = new String[3];
		tmp[0] = dbf.rs.getString("fact_data.date_collected");
		tmp[1] = dbf.rs.getString("entities.ticker");
		tmp[2] = dbf.rs.getString("pctchange");
		


		
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 4-4"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}


arrayListRows = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows);

/*
*
* If doing a pivot, have to populate the columns after the rows
*/

String[] pivotColumns = arrayListRows.remove(0);

String[] col1 = {"date_collected","date collected","string"};
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


/*try
{
	rs = dbf.db_run_query(query);
	while (rs.next()) {
		String [] tmp = new String[6];
		tmp[0] = tmp[1] = rs.getString("fact_data.date_collected");
		tmp[2] = tmp[3] = rs.getString("entities.ticker");
		tmp[4] = tmp[5] = rs.getString("fdvalue");
		


		
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	out.println(sqle.toString());
}*/










;
/*for (int i=0;i<tmpArrayList.size();i++)
{
	String[] tmp2 = tmpArrayList.get(i);
	for (int j=0;j<tmp2.length;j++)
	{
		out.println(tmp2[j]);
		out.println("<BR>");
	}
	
	
	
}*/







/*

for (int i=1;i<tmpArrayList.size();i++)
{
	String[] blap7 = new String[4];
	
	blap7[0] = tmpArrayList.get(i)[0];
	blap7[1] = "empty";
	if (tmpArrayList.get(i)[3].equals("0.0"))
			blap7[2] = blap7[3] = "0";
	else
		blap7[2] = blap7[3] = tmpArrayList.get(i)[3];

	
	arrayListRows.add(blap7);
	
}*/




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
