<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>




<%
/*
* This data source pulls from the fact_data table using alerts (using alerts.initial_fact_data_id and
* alerts.current_fact_data_id)
*/




String strTimeEventId = request.getParameter("timeeventid");
String strUserId = request.getParameter("userid");
String strTaskId = request.getParameter("taskid") ; 

String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null)
{
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";

//out.println(strTimeEventId + "," + strUserId + "," + strTaskId + "," + strReqId); if (1==1) return;

if ((strTimeEventId==null) || (strTimeEventId.equals("all")))
	strTimeEventId = "%";

if ((strUserId==null) || (strUserId.equals("all")))
	strUserId = "%";

if ((strTaskId==null) || (strTaskId.equals("all")))
	strTaskId = "%";

UtilityFunctions uf = new UtilityFunctions();

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();
//String[] columns = tmpArrayList.get(0);



String[] blap1 = {"task name","task name","string"};
String[] blap2 = {"ticker","ticker","string"};
String[] blap3 = {"time event name","time event name","string"};
String[] blap4 = {"username","username","string"};
String[] blap5 = {"initial value","initial value","number"};
String[] blap6 = {"current value","current value","number"};
String[] blap7 = {"% change","% change","number"};
String[] blap8 = {"date initial","date initial","datetime"};
String[] blap9 = {"date current","date current","datetime"};
String[] blap10 = {"te period last","te period last","datetime"};
String[] blap11 = {"te period next","te period next","datetime"};


arrayListCols.add(blap1);
arrayListCols.add(blap2);
arrayListCols.add(blap3);
arrayListCols.add(blap4);
arrayListCols.add(blap5);
arrayListCols.add(blap6);
arrayListCols.add(blap7);
arrayListCols.add(blap8);
arrayListCols.add(blap9);
arrayListCols.add(blap10);
arrayListCols.add(blap11);

DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");



String query = "select tasks.name,entities.ticker,time_events.name,users.username,fd1.value, fd2.value, (if (fd1.value=0,fd1.value,round(((fd2.value - fd1.value)/fd1.value),3))) * 100 as pctchange,  ";
query += "fd1.date_collected,fd2.date_collected,time_events.last_datetime,time_events.next_datetime ";
query += "from alerts ";
query += "JOIN schedules on alerts.schedule_id=schedules.id ";
query += "JOIN users on alerts.user_id=users.id ";
query += "JOIN entities on alerts.entity_id=entities.id ";
query += "JOIN tasks on schedules.task_id=tasks.id ";
query += "JOIN time_events on alerts.time_event_id=time_events.id ";
query += "LEFT JOIN fact_data as fd1 on alerts.initial_fact_data_id=fd1.id ";
query += "LEFT JOIN fact_data as fd2 on alerts.current_fact_data_id=fd2.id ";
query += "where !isnull(fd1.value) AND !isnull(fd2.value)";
query += " AND time_events.id LIKE '" + strTimeEventId + "' ";
query += " AND users.id LIKE '" + strUserId +"' ";
query += " AND tasks.id LIKE '" + strTaskId +"' ";
query += " order by pctchange";

//out.println(query); if (1==1) return;





ResultSet rs=null;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
try
{
	rs = dbf.db_run_query(query);
	while (rs.next()) {
		String [] tmp = new String[22];
		tmp[0] = tmp[1] = rs.getString("tasks.name");
		tmp[2] = tmp[3] = rs.getString("entities.ticker");
		tmp[4] = tmp[5] = rs.getString("time_events.name");
		tmp[6] = tmp[7] = rs.getString("users.username");
		tmp[8] = tmp[9] = rs.getString("fd1.value");
		tmp[10] = tmp[11] = rs.getString("fd2.value");
		tmp[12] = tmp[13] = rs.getString("pctchange");
		tmp[14] = tmp[15] = rs.getString("fd1.date_collected");
		tmp[16] = tmp[17] = rs.getString("fd2.date_collected");
		tmp[18] = tmp[19] = rs.getString("time_events.last_datetime");
		tmp[20] = tmp[21] = rs.getString("time_events.next_datetime");
		
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	out.println(sqle.toString());
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



out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId));


//google.visualization.Query.setResponse({version:'0.6',reqId:'0',status:'ok',sig:'5982206968295329967',table:{cols:[{id:'Col1',label:'label1',type:'number'},{id:'Col2',label:'label2',type:'number'},
//{id:'Col3',label:'label3',type:'number'}],rows:[{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]},{c:[{v:2.0,f:'2'},{v:3.0,f:'3'},{v:4.0,f:'4'}]},{c:[{v:3.0,f:'3'},{v:4.0,f:'4'},{v:5.0,f:'5'}]},{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]}]}}); 

//out.println("[{\"\":\" Gold certificate account\",\"2011-01-26 \":\"11037\",\"2011-01-19 \":\"11037\",\"diff \":\"0.00%\"}, {\"\":\" Special drawing rights certificate account\",\"2011-01-26 \":\"5200\",\"2011-01-19 \":\"5200\",\"diff \":\"0.00%\"}, {\"\":\" Coin\",\"2011-01-26 \":\"2318\",\"2011-01-19 \":
//	\"2246\",\"diff \":\"0.00%\"}, {\"\":\"Bills\",\"2011-01-26 \":\"18422\",\"2011-01-19 \":\"18422\",\"diff \":\"0.00%\"}, {\"\":\"Notes and bonds\",\"2011-01-26 \":\"1096024\",\"2011-01-19 \":\"1061154\",\"diff \":\"0.00%\"}, {\"\":\" Federal agency debt securities\",\"2011-01-26 \":\"144623\",\"2011-01-19 \":\"145887\",\"diff \":\"0.00%\"}, {\"\":\" Mortgage-backed securities\",\"2011-01-26 \":\"965078\",\"2011-01-19 \":\"980155\",\"diff \":\"0.00%\"}, {\"\":\" Repurchase agreements\",\"2011-01-26 \":\"0\",\"2011-01-19 \":\"0\",\"diff \":\"0.00%\"}, {\"\":\" Term auction credit\",\"2011-01-26 \":\"0\",\"2011-01-19 \":\"0\",\"diff \":\"0.00%\"}, {\"\":\" Other loans\",\"2011-01-26 \":\"23260\",\"2011-01-19 \":\"23689\",\"diff \":\"0.00%\"}]"); 



//out.println(" Hello Oracle World4"); %> 
