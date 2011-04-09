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

String strEntityId = request.getParameter("entityid");
String[] entityIds = null;
if (strEntityId!=null)
	entityIds = strEntityId.split(",");
String strEndDate = request.getParameter("enddate");
String strBeginDate = request.getParameter("begindate"); 

if (strBeginDate==null)
{
	out.println("No begindate request parameter.");
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
//String[] columns = tmpArrayList.get(0);

DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");

//String query1 = "select ticker from entities where id " + strInClause;
//ResultSet rs1 = dbf.db_run_query(query1);




//String[] col;




//ideally i'd like to use last() here but mysql doesn't support it - it would have to be 
//coded by hand - so going with max() instead.
String query = "select date_format(fact_data.date_collected,'%m-%d-%Y') as date_col, fact_data.batch,fact_data.value as fdvalue,entities.ticker  ";
query += "from fact_data ";
query += "JOIN entities on fact_data.entity_id=entities.id ";
//query += "JOIN tasks on fact_data.task_id=tasks.id ";
query += " where entities.id " + strInClause;
query += " AND date_format(fact_data.date_collected,'%Y-%m_%d')>'" + strBeginDate + "' ";
if (strEndDate!=null && !strEndDate.isEmpty())
	query += " AND date_format(fact_data.date_collected,'%Y-%m-%d')<'" + strEndDate + "' ";
//query += " group by date_format(fact_data.date_collected,'%Y-%m-%d'),entities.ticker,fact_data.value ";
query += " order by date_format(fact_data.date_collected,'%Y-%m-%d') ASC";

String query1 = "select fact_data.value ";
query1 += "from fact_data ";
//query1 += " where task_id=9 "



//out.println(query); if (1==1) return;





ResultSet rs=null;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

try
{
	rs = dbf.db_run_query(query);
	while (rs.next()) {
		String [] tmp = new String[4];
		tmp[0] = rs.getString("date_col");
		tmp[1] = rs.getString("entities.ticker");
		tmp[2] = rs.getString("fdvalue");
		tmp[3] = rs.getString("fact_data.batch");
				
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	out.println(sqle.toString());
}


int[] tmpArray = {0,1};
arrayListRows = PopulateSpreadsheet.getMaxGroupBy(arrayListRows,tmpArray,3);

arrayListRows = PopulateSpreadsheet.removeLastColumn(arrayListRows);

arrayListRows = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows);



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
