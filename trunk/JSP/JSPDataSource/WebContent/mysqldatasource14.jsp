<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>




<% 
/*
This data set is for determing alerts that have been fired or are approaching fired status.

The query fields have been made dynamic.

*/



DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

String strTimeEventId = request.getParameter("timeeventid");
String strUserId = request.getParameter("userid");
String strTaskId = request.getParameter("taskid") ; 
String strFired = request.getParameter("fired") ; 

/*String strTimeEventId = "2";
String strUserId = "16";
String strTaskId = "1";*/












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

String strPctChange = " (if (fd1.value=0,fd1.value,((fd2.value - fd1.value)/fd1.value))) ";

String[] fields = 
{
	"entities.ticker","entities.ticker", "ticker", "string",
	"entities.full_name","entities.full_name","description","string",
	"time_events.name","time_events.name","observation period","string",
	"fd1.value","fd1.value", "initial value", "number",
	"fd2.value","fd2.value", "current value", "number",
	"alerts.limit_value * 100 as lv","lv","limit value %", "number",
	"round(" + strPctChange + " * 100,2) as pctchange","pctchange", "% change", "number",
	"round(abs(" + strPctChange + ")/(limit_value) * 100,2) as pctchange2","pctchange2", "% change of limit", "number",
	"date_format(fd1.date_collected,'%m-%d-%Y %H:%i') as date_begin","date_begin", "date initial", "datetime",
	"date_format(fd2.date_collected,'%m-%d-%Y %H:%i') as date_end","date_end", "date current", "datetime",
	"case alerts.fired when 1 then 'True' when 0 then 'False' end as fired1","fired1", "Fired", "string",
	"case alerts.auto_reset_fired when 1 then 'True' when 0 then 'False' end as arf","arf","Auto Reset Fired","string",
	"case alerts.auto_reset_period when 1 then 'True' when 0 then 'False' end as arp","arp","Auto Reset Period","string"

};

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();

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



DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");




/*String query = "select alerts.id,alerts.entity_id,alerts.limit_value, ";
query += " fd1.value,fd2.value, ";
query += "date_format(fd1.date_collected,'%m-%d-%Y %H:%i') as date_begin,";
query += "date_format(fd2.date_collected,'%m-%d-%Y %H:%i') as date_end,";
query += "(if (fd1.value=0,fd1.value,abs(round(((fd2.value - fd1.value)/fd1.value) * 100,2)))/(limit_value*100)) as pctchange2 ";
query += " ,entities.ticker ";*/

String querymodel = " from alerts ";
querymodel += " join fact_data as fd1 on alerts.initial_fact_data_id=fd1.id ";
querymodel += " join fact_data as fd2 on alerts.current_fact_data_id=fd2.id ";
querymodel += " join entities on entities.id=alerts.entity_id ";
querymodel += " join time_events on alerts.time_event_id=time_events.id ";
querymodel += " where 1=1 ";
if (!strTaskId.toUpperCase().equals("ALL"))
	querymodel += " and fd1.task_id=" + strTaskId;
if (!strTimeEventId.toUpperCase().equals("ALL"))
	querymodel += " and time_event_id=" + strTimeEventId; 
if (!strUserId.toUpperCase().equals("ALL"))
	querymodel += " and user_id=" + strUserId;
if (strFired.toUpperCase().equals("FALSE"))
	querymodel += " and fired=0 ";
querymodel += " order by pctchange2 desc ";








//out.println(query + querymodel); if (1==1) return;

String countquery = query + ",count(alerts.id) as cnt ";

ResultSet rs=null;
try
{
	rs = dbf.db_run_query(countquery + querymodel);
	rs.next();
	if (rs.getInt("cnt") > 500 )
	{
		out.println(PopulateSpreadsheet.createGoogleError(strReqId,"rows_exceeded","Maximum Number of Rows Exceeded (500 Max)","Maximum Number of Rows Exceeded (500 Max)"));
		return;
	}
	
	
}
catch (SQLException sqle)
{
	out.println(sqle.toString());
	return;
}


rs=null;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
try
{
	rs = dbf.db_run_query(query + querymodel);
	while (rs.next()) {
		
		String [] tmp = new String[(fields.length/2)];
		for (int i=0;i<fields.length;i=i+4)
		{
			tmp[(i/4)*2] = tmp[((i/4)*2)+1] = rs.getString(fields[i+1]);		
		}
		
		/*tmp[0] = tmp[1] = rs.getString("tasks.name");
		tmp[2] = tmp[3] = rs.getString("entities.ticker");
		tmp[4] = tmp[5] = rs.getString("time_events.name");
		tmp[6] = tmp[7] = rs.getString("users.username");
		tmp[8] = tmp[9] = rs.getString("fd1.value");
		tmp[10] = tmp[11] = rs.getString("fd2.value");
		tmp[12] = tmp[13] = rs.getString("pctchange");
		tmp[14] = tmp[15] = rs.getString("fd1.date_collected");
		tmp[16] = tmp[17] = rs.getString("fd2.date_collected");
		tmp[18] = tmp[19] = rs.getString("time_events.last_datetime");
		tmp[20] = tmp[21] = rs.getString("time_events.next_datetime");*/
		
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	out.println(sqle.toString());
	return;
}






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
