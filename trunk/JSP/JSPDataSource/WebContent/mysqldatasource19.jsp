<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>




<%

/*
* This data soruce pulls from log_tasks
*/


String strType = "total";
if (request.getParameter("type") != null)
	strType = request.getParameter("type");



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
//String[] blap1 = {"task name","task name","string"};
String[] blap2 = {"name","name","string"};

//String[] blap3 = {"time event name","time event name","string"};
//String[] blap4 = {"full_name","full_name","string"};
String[] blap3 = {"date","date","date"};
String[] blap4 = {"job duration","job duration","number"};
String[] blap5 = {"alerts duration","alerts duration","number"};
String[] blap6 = {"total duration","total duration","number"};
String[] blap7 = {"repeat type","repeat type", "string"};


arrayListCols.add(blap2);

arrayListCols.add(blap3);
arrayListCols.add(blap4);
arrayListCols.add(blap5);
arrayListCols.add(blap6);
arrayListCols.add(blap7);






String query = "select date_format(job_process_start,'%m/%d/%Y') as date1, round(time_to_sec(timediff(job_process_end,job_process_start))/60,2) as jobdiff, ";
query += " round(time_to_sec(timediff(alert_process_end,alert_process_start))/60,2) as alertdiff, round(time_to_sec(timediff(alert_process_end,job_process_start))/60,2) as totaldiff ";
query += " ,tasks.name, repeat_types.type ";
query += "from log_tasks ";
query += " join tasks on log_tasks.task_id=tasks.id ";
query += " join repeat_types on log_tasks.repeat_type_id=repeat_types.id ";
query += " order by date1 ASC, name ";







DBFunctions dbf = null;
boolean bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		String [] tmp = new String[6];
		tmp[0] =  dbf.rs.getString("tasks.name");
		tmp[1] =  dbf.rs.getString("date1");	
		tmp[2]  = dbf.rs.getString("jobdiff");
		tmp[3] =  dbf.rs.getString("alertdiff");
		tmp[4] =  dbf.rs.getString("totaldiff");
		tmp[5] =  dbf.rs.getString("repeat_types.type");
		
		/*if (strType.equals("job"))
			tmp[2]  = dbf.rs.getString("jobdiff");
		else if (strType.equals("alert"))
			tmp[2] =  dbf.rs.getString("alertdiff");
		else
			tmp[2] = = dbf.rs.getString("totaldiff");*/

				
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 19-1"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
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
out.println("</table>");*/

//out.println(query); if (1==1) return;

int[] tmpArray = {0,1};
arrayListRows = PopulateSpreadsheet.getMaxGroupBy(arrayListRows,tmpArray,4);



//arrayListRows = PopulateSpreadsheet.removeLastColumn(arrayListRows);

//arrayListRows = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows);



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






ArrayList<String[]> arrayListSave = new ArrayList<String[]>();
for (int i=0;i<arrayListRows.size();i++)
{
	String[] tmp = new String[12];
	
	tmp[0] = tmp[1] = arrayListRows.get(i)[0];
	tmp[2] = tmp[3] = arrayListRows.get(i)[1];
	tmp[4] = tmp[5] = arrayListRows.get(i)[2];
	tmp[6] = tmp[7] = arrayListRows.get(i)[3];
	tmp[8] = tmp[9] = arrayListRows.get(i)[4];
	tmp[10] = tmp[11] = arrayListRows.get(i)[5];
	
	arrayListSave.add(tmp);
	
	
}

arrayListRows = arrayListSave;










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



out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,true));



%> 
