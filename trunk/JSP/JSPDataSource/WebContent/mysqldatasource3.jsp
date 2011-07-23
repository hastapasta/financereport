<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>




<% 

/*
This is the same as mysqldatasource2 but the fields have been made dynamic ( the fields in the select statement
		have been stored in an array and Row and Column array lists are populated dynamically.
		
NOTE: No longer exactly the same as mysqldatasource2 which uses begindate,enddate input parameters and this uses 
timeeventid.

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

String strTimeEventId = request.getParameter("timeeventid");
String strUserId = request.getParameter("userid");
String strTaskId = request.getParameter("taskid") ; 



//out.println(strTimeEventId + "," + strUserId + "," + strTaskId + "," + strReqId); if (1==1) return;

if ((strTimeEventId == null) || strTimeEventId.equals("all"))
	strTimeEventId = "%";

if ((strUserId == null) || strUserId.equals("all"))
	strUserId = "%";

if ((strTaskId == null) || strTaskId.equals("all"))
	strTaskId = "%";

UtilityFunctions uf = new UtilityFunctions();

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();
//String[] columns = tmpArrayList.get(0);
String[] fields = 
	{"tasks.description","tasks.description","task description", "string",
		"entities.ticker","entities.ticker", "ticker", "string",
		"time_events.name","time_events.name", "time event name", "string",
		"fd1.value","fd1.value", "initial value", "number",
		"fd2.value","fd2.value", "current value", "number",
		"(if (fd1.value=0,fd1.value,round(((fd2.value - fd1.value)/fd1.value),3))) * 100 as pctchange","pctchange", "% change", "number",
		"fd1.date_collected","fd1.date_collected", "date initial", "datetime",
		"fd2.date_collected","fd2.date_collected", "date current", "datetime",
		"time_events.last_datetime", "time_events.last_datetime","observation period begin", "datetime",
		"time_events.next_datetime","time_events.next_datetime", "observation period end", "datetime"
};

String query = "select ";



for (int i=0;i<fields.length;i=i+4)
{
	String[] tmp1 = new String[3];
	tmp1[0] = fields[i+2];
	tmp1[1] = fields[i+2];
	tmp1[2] = fields[i+3];
	arrayListCols.add(tmp1);
	
	if (i!=0)
		query += ",";
	
	query += fields[i];
}


//String query = "select tasks.name,entities.ticker,time_events.name,users.username,fd1.value, fd2.value, (if (fd1.value=0,fd1.value,round(((fd2.value - fd1.value)/fd1.value),3))) * 100 as pctchange,  ";
//query += "fd1.date_collected,fd2.date_collected,time_events.last_datetime,time_events.next_datetime ";
query += " from alerts ";
query += "JOIN schedules on alerts.task_id=schedules.task_id ";
query += "JOIN users on alerts.user_id=users.id ";
query += "JOIN entities on alerts.entity_id=entities.id ";
query += "JOIN tasks on alerts.task_id=tasks.id ";
query += "JOIN time_events on alerts.time_event_id=time_events.id ";
query += "LEFT JOIN fact_data as fd1 on alerts.initial_fact_data_id=fd1.id ";
query += "LEFT JOIN fact_data as fd2 on alerts.current_fact_data_id=fd2.id ";
query += "where !isnull(fd1.value) AND !isnull(fd2.value)";
query += " AND time_events.id LIKE '" + strTimeEventId + "' ";
query += " AND users.id LIKE '" + strUserId +"' ";
query += " AND tasks.id LIKE '" + strTaskId +"' ";
query += " order by pctchange";

//out.println(query); if (1==1) return;





DBFunctions dbf = null;
boolean bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		
		String [] tmp = new String[22];
		for (int i=0;i<fields.length;i=i+4)
		{
			tmp[(i/4)*2] = tmp[((i/4)*2)+1] = dbf.rs.getString(fields[i+1]);		
		}
		
		/*tmp[0] = tmp[1] = dbf.rs.getString("tasks.name");
		tmp[2] = tmp[3] = dbf.rs.getString("entities.ticker");
		tmp[4] = tmp[5] = dbf.rs.getString("time_events.name");
		tmp[6] = tmp[7] = dbf.rs.getString("users.username");
		tmp[8] = tmp[9] = dbf.rs.getString("fd1.value");
		tmp[10] = tmp[11] = dbf.rs.getString("fd2.value");
		tmp[12] = tmp[13] = dbf.rs.getString("pctchange");
		tmp[14] = tmp[15] = dbf.rs.getString("fd1.date_collected");
		tmp[16] = tmp[17] = dbf.rs.getString("fd2.date_collected");
		tmp[18] = tmp[19] = dbf.rs.getString("time_events.last_datetime");
		tmp[20] = tmp[21] = dbf.rs.getString("time_events.next_datetime");*/
		
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 3-1"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}






/*out.println("google.visualization.Query.setResponse({version:'0.6',reqId:'0',status:'ok',sig:'5982206968295329967',");
out.println(",table:{cols:[{id:'Col1',label:'2011-01-26',type:'number'},{id:'Col2',label:'2011-01-19',type:'number'}],");

out.println("google.visualization.Query.setResponse({");
out.println("version:'0.6',");
out.println("reqId:'0',");*/






/*for (int i=0;i<tmpArrayList.size();i++)
{
	String[] tmp2 = tmpArrayList.get(i);
	for (int j=0;j<tmp2.length;j++)
	{
		out.println(tmp2[j]);
		out.println("<BR>");
	}
	
	
	
}*/






/*String[] blap1 = {"ticker","ticker","string"};
String[] blap2 = {"calyear","calyear","number"};
String[] blap3 = {"value","eps","number"};*/


//arrayListCols.add(blap3);

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



/*String[] blap4 = {"ADSK","empty","20091","20091","0","1"};
String[] blap5 = {"ANFG","empty","20091","20091","5000","5000"};
String[] blap6 = {"BIG","empty","3000","3000","10000","10000"};

arrayListRows.add(blap4);
arrayListRows.add(blap5);
arrayListRows.add(blap6);*/

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


//google.visualization.Query.setResponse({version:'0.6',reqId:'0',status:'ok',sig:'5982206968295329967',table:{cols:[{id:'Col1',label:'label1',type:'number'},{id:'Col2',label:'label2',type:'number'},
//{id:'Col3',label:'label3',type:'number'}],rows:[{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]},{c:[{v:2.0,f:'2'},{v:3.0,f:'3'},{v:4.0,f:'4'}]},{c:[{v:3.0,f:'3'},{v:4.0,f:'4'},{v:5.0,f:'5'}]},{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]}]}}); 

//out.println("[{\"\":\" Gold certificate account\",\"2011-01-26 \":\"11037\",\"2011-01-19 \":\"11037\",\"diff \":\"0.00%\"}, {\"\":\" Special drawing rights certificate account\",\"2011-01-26 \":\"5200\",\"2011-01-19 \":\"5200\",\"diff \":\"0.00%\"}, {\"\":\" Coin\",\"2011-01-26 \":\"2318\",\"2011-01-19 \":
//	\"2246\",\"diff \":\"0.00%\"}, {\"\":\"Bills\",\"2011-01-26 \":\"18422\",\"2011-01-19 \":\"18422\",\"diff \":\"0.00%\"}, {\"\":\"Notes and bonds\",\"2011-01-26 \":\"1096024\",\"2011-01-19 \":\"1061154\",\"diff \":\"0.00%\"}, {\"\":\" Federal agency debt securities\",\"2011-01-26 \":\"144623\",\"2011-01-19 \":\"145887\",\"diff \":\"0.00%\"}, {\"\":\" Mortgage-backed securities\",\"2011-01-26 \":\"965078\",\"2011-01-19 \":\"980155\",\"diff \":\"0.00%\"}, {\"\":\" Repurchase agreements\",\"2011-01-26 \":\"0\",\"2011-01-19 \":\"0\",\"diff \":\"0.00%\"}, {\"\":\" Term auction credit\",\"2011-01-26 \":\"0\",\"2011-01-19 \":\"0\",\"diff \":\"0.00%\"}, {\"\":\" Other loans\",\"2011-01-26 \":\"23260\",\"2011-01-19 \":\"23689\",\"diff \":\"0.00%\"}]"); 



//out.println(" Hello Oracle World4"); %> 
