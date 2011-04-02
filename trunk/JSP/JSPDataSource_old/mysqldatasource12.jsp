<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>




<%

/*
* This data_source pulls earnings estimates from the fact_data table based off of an entity id
*/

String strEntityId = request.getParameter("entityid");

//String strEndDate = request.getParameter("enddate");
//String strBeginDate = request.getParameter("begindate"); 

/*if (strBeginDate==null)
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
}*/

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


//String[] columns = tmpArrayList.get(0);

DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");

//String query1 = "select ticker from entities where id " + strInClause;
//ResultSet rs1 = dbf.db_run_query(query1);

String query2 = "select value as initval,entity_id,(calyear*10+calquarter) as cyq,calyear,calquarter ";
query2 += "from fact_data,metrics ";
query2 += "where fact_data.metric_id=metrics.id AND ";
query2 += " entity_id=" + strEntityId;
query2 += " AND metric_id=5 ";
query2 += " order by cyq asc limit 1";

ResultSet rstmp = null;
try
{
	rstmp = dbf.db_run_query(query2);
	rstmp.next();
	
}
catch (SQLException sqle)
{
	out.println(sqle.toString());
}

int nMonth = rstmp.getInt("calquarter") * 3;


String strCase = "case month(date_collected) when 1 then 1 when 2 then 1 when 3 then 1 ";
strCase += " when 4 then 2 when 5 then 2 when 6 then 2 when 7 then 3 when 8 then 3 when 9 then 3 ";
strCase += " when 10 then 4 when 11 then 4 when 12 then 4 end ";




String query3 = "select value as initval,entity_id, (year(date_collected)*10 + (" + strCase + ")) as initcyq ";
query3 += " from fact_data ";
query3 += "where ";
query3 += " entity_id=" + strEntityId;
query3 += " and metric_id=1 and month(date_collected)=" + nMonth + " and year(date_collected)=" + rstmp.getInt("calyear");
query3 += " order by date_collected DESC";



//out.println(query3); if (1==1) return;







String query = "(select metrics.name,(fact_data.calyear*10+fact_data.calquarter) as cyq,value,batch,t1.initval,";
query += "(if (initval=0,initval,round(((value - initval)/initval),2))) * 100 as pctchange ";
query += " from fact_data ";
query += " join metrics on fact_data.metric_id=metrics.id ";
query += " join (" + query2 + ") as t1 on t1.entity_id=fact_data.entity_id ";
query += " where metric_id=4 and fact_data.entity_id=" + strEntityId + " and metrics.id=fact_data.metric_id) ";
query += " union ";
query += " (select metrics.name,(fact_data.calyear*10+fact_data.calquarter) as cyq,value,batch,t1.initval, ";
query += "(if (initval=0,initval,round(((value - initval)/initval),2))) * 100 as pctchange ";
query += " from fact_data ";
query += " join metrics on fact_data.metric_id=metrics.id ";
query += " join (" + query2 + ") as t1 on t1.entity_id=fact_data.entity_id ";
query += " where metric_id=5 and fact_data.entity_id=" + strEntityId + " and metrics.id=fact_data.metric_id) ";
query += " union ";
query += " (select metrics.name,(year(date_collected)*10+( " + strCase + ")) as cyq, ";
query += " value,batch,t2.initval,";
query += "(if (initval=0,initval,round(((value - initval)/initval),2))) * 100 as pctchange ";
query += " from fact_data ";
query += " join metrics on fact_data.metric_id=metrics.id ";
query += " join (" + query3 + ") as t2 on t2.entity_id=fact_data.entity_id ";
query += " where metric_id=1 and fact_data.entity_id=" + strEntityId + " and metrics.id=fact_data.metric_id ";
query += " and t2.initcyq<= (year(date_collected)*10+( " + strCase + ")))";
query += " order by cyq asc,name,batch ";


//out.println(query); if (1==1) return;





ResultSet rs=null;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

try
{
	rs = dbf.db_run_query(query);
	while (rs.next()) {
		String [] tmp = new String[4];
		tmp[0] = rs.getString("cyq");
		tmp[1] = rs.getString("name");
		tmp[2] = rs.getString("pctchange");
		tmp[3] = rs.getString("batch");
				
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	out.println(sqle.toString());
}

int[] tmpArray = {0,1};
arrayListRows = PopulateSpreadsheet.getLastGroupBy(arrayListRows,tmpArray,3);



arrayListRows = PopulateSpreadsheet.removeLastColumn(arrayListRows);



out.println("<table>");
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

out.println(query); if (1==1) return;



arrayListRows = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows);












/*
*
* If doing a pivot, have to populate the columns after the rows
*/

String[] pivotColumns = arrayListRows.remove(0);

String[] col1 = {"quarter/year","quarter/year","string"};
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



out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId));



%> 