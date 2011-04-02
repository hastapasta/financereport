<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>




<%

/*
* This data source is similar to datasource5 except it returns % values.
* This data_source pulls from the fact_data based off of task_id
*
* 3/8/2011 - Right now this data source is primarily being used for gdp estimates.
*/

String strTaskId = request.getParameter("taskid");
String[] entityIds = null;


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




DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");

String query2 = "select fact_data.value as initval, fact_data.entity_id ";
query2 += "from fact_data ";
query2 += "join (select min(calyear) as minyear,entity_id from fact_data where task_id=" + strTaskId + " group by entity_id) as t1 on fact_data.entity_id=t1.entity_id";
query2 += " where calyear = t1.minyear ";
query2 += " AND task_id=" + strTaskId;


//out.println(query2); if (1==1) return;



String query = "select fact_data.calyear,entities.ticker,round(((fact_data.value - t2.initval)/t2.initval),3)*100 as pctchange ";
query += "from fact_data ";
query += "JOIN entities on fact_data.entity_id=entities.id ";
query += "JOIN tasks on fact_data.task_id=tasks.id ";
query += "JOIN (" + query2 + ") as t2 on t2.entity_id=fact_data.entity_id ";
query += " where ";
query += " fact_data.task_id=" + strTaskId;
query += " AND entities.ticker like 'A%'";
query += " order by fact_data.calyear,entities.ticker";


//out.println(query); if (1==1) return;





ResultSet rs=null;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

try
{
	rs = dbf.db_run_query(query);
	while (rs.next()) {
		String [] tmp = new String[3];
		tmp[0] = rs.getString("fact_data.calyear");
		tmp[1] = rs.getString("entities.ticker");
		tmp[2] = rs.getString("pctchange");
		


		
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	out.println(sqle.toString());
}

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

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();

String[] col1 = {"year","year","string"};
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
		
}*/

/*for (int i=0;i<arrayListRows.size();i++)
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
