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



String strUserId = request.getParameter("userid");



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


UtilityFunctions uf = new UtilityFunctions();

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();
//String[] columns = tmpArrayList.get(0);



String[] blap1 = {"ticker","ticker","string"};
String[] blap2 = {"date","date","string"};
String[] blap3 = {"alert count","alert count","number"};
String[] blap4 = {"observation period","observation period","string"};



arrayListCols.add(blap1);
arrayListCols.add(blap2);
arrayListCols.add(blap3);
arrayListCols.add(blap4);


DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");

String query2 = "select entities.ticker,date_format(date_time_fired,'%XW%V') as date_fired,count(log_alerts.id) as alert_count,time_events.name ";
query2 += " from log_alerts ";
query2 += "join entities on entities.id=log_alerts.entity_id ";
query2 += "join alerts on alerts.id = log_alerts.alert_id ";
query2 += "join time_events on time_events.id=alerts.time_event_id ";
query2 += " where log_alerts.user_id=" + strUserId + " ";
query2 += "group by entities.ticker,alerts.time_event_id,date_format(date_time_fired,'%XW%V')";


/*String query2 = "select fact_data.value as initval, fact_data.entity_id ";
query2 += "from fact_data ";
query2 += "join (select min(calyear) as minyear,entity_id from fact_data where task_id=" + strTaskId + " group by entity_id) as t1 on fact_data.entity_id=t1.entity_id";
query2 += " where calyear = t1.minyear ";
query2 += " AND task_id=" + strTaskId;*/


//out.println(query2); if (1==1) return;






ResultSet rs=null;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
try
{
	rs = dbf.db_run_query(query2);
	while (rs.next()) {
		String [] tmp = new String[8];
		tmp[0] = tmp[1] = rs.getString("entities.ticker");
		tmp[2] = tmp[3] = rs.getString("date_fired");
		tmp[4] = tmp[5] = rs.getString("alert_count");
		tmp[6] = tmp[7] = rs.getString("time_events.name");

		
		
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



out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId).replace("\\",""));


//google.visualization.Query.setResponse({version:'0.6',reqId:'0',status:'ok',sig:'5982206968295329967',table:{cols:[{id:'Col1',label:'label1',type:'number'},{id:'Col2',label:'label2',type:'number'},
//{id:'Col3',label:'label3',type:'number'}],rows:[{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]},{c:[{v:2.0,f:'2'},{v:3.0,f:'3'},{v:4.0,f:'4'}]},{c:[{v:3.0,f:'3'},{v:4.0,f:'4'},{v:5.0,f:'5'}]},{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]}]}}); 

//out.println("[{\"\":\" Gold certificate account\",\"2011-01-26 \":\"11037\",\"2011-01-19 \":\"11037\",\"diff \":\"0.00%\"}, {\"\":\" Special drawing rights certificate account\",\"2011-01-26 \":\"5200\",\"2011-01-19 \":\"5200\",\"diff \":\"0.00%\"}, {\"\":\" Coin\",\"2011-01-26 \":\"2318\",\"2011-01-19 \":
//	\"2246\",\"diff \":\"0.00%\"}, {\"\":\"Bills\",\"2011-01-26 \":\"18422\",\"2011-01-19 \":\"18422\",\"diff \":\"0.00%\"}, {\"\":\"Notes and bonds\",\"2011-01-26 \":\"1096024\",\"2011-01-19 \":\"1061154\",\"diff \":\"0.00%\"}, {\"\":\" Federal agency debt securities\",\"2011-01-26 \":\"144623\",\"2011-01-19 \":\"145887\",\"diff \":\"0.00%\"}, {\"\":\" Mortgage-backed securities\",\"2011-01-26 \":\"965078\",\"2011-01-19 \":\"980155\",\"diff \":\"0.00%\"}, {\"\":\" Repurchase agreements\",\"2011-01-26 \":\"0\",\"2011-01-19 \":\"0\",\"diff \":\"0.00%\"}, {\"\":\" Term auction credit\",\"2011-01-26 \":\"0\",\"2011-01-19 \":\"0\",\"diff \":\"0.00%\"}, {\"\":\" Other loans\",\"2011-01-26 \":\"23260\",\"2011-01-19 \":\"23689\",\"diff \":\"0.00%\"}]"); 



//out.println(" Hello Oracle World4"); %> 
