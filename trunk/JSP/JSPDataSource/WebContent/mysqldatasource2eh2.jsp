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
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="org.apache.log4j.Logger" %>



<% 

/*

This datasource pulls from the fact_data table  the earliest and latest values for all the entities from 
a certain task for a certain data range. 

This is the datasource to use to figure out the maximum gainers/losers for a task for a particular date range.

Update: This is another attempt to improve the performance of this page. Also, the query should not be using 
batch #, since batch is a capture property and shouldn't be utilitzed in any end user facing data. 


*/

Calendar calBeginTimer = Calendar.getInstance(); 

Logger fulllogger = Logger.getLogger("FullLogging");

DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

String strTimeFrame = request.getParameter("timeframe");
//String strBeginDate = request.getParameter("begindate"); 
//String strEndDate = request.getParameter("enddate");
//String strTaskId = request.getParameter("taskid");
String strMetricId = request.getParameter("metricid");
String strEntityGroupId = request.getParameter("entitygroupid");


if (strTimeFrame==null)
{
	out.println("No timeframe parameter passed in the url. Exiting.");
	return;
}

if (strMetricId==null)
{
	out.println("No metricid parameter passed in the url. Exiting.");
	return;
}


if (strEntityGroupId == null)
{
	out.println("No entitygroupid parameter passed in the url. Exiting.");
	return;
	
}

String strOrder = "DESC";
if (request.getParameter("order") != null)
{
	strOrder = request.getParameter("order");
	
}


Calendar calEnd = Calendar.getInstance();
Calendar calBegin = Calendar.getInstance();

//System.out.println(calBegin.getTime().toString());



if (Debug.RELEASE == true)  
{
	calEnd.set(Calendar.YEAR,2011);
	calEnd.set(Calendar.DAY_OF_MONTH,21);
	calEnd.set(Calendar.MONTH,6);
	
	calBegin.set(Calendar.YEAR,2011);
	calBegin.set(Calendar.DAY_OF_MONTH,21);
	calBegin.set(Calendar.MONTH,6);

	
}


//System.out.println(calBegin.getTime().toString());



String strGranularity;

if (strTimeFrame.toUpperCase().equals("HOUR"))
{
	calBegin.add(Calendar.HOUR,-1);
}
else if (strTimeFrame.toUpperCase().equals("DAY"))
{
	calBegin.add(Calendar.DAY_OF_YEAR,-1);
}
else if (strTimeFrame.toUpperCase().equals("WEEK"))
{
	calBegin.add(Calendar.WEEK_OF_YEAR,-1);
}
else if (strTimeFrame.toUpperCase().equals("MONTH"))
{
	calBegin.add(Calendar.MONTH,-1);
}
else if (strTimeFrame.toUpperCase().equals("YEAR"))
{
	calBegin.add(Calendar.YEAR,-1);
}

/* We don't have much historical data*/
SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Calendar calEarliest = Calendar.getInstance();
calEarliest.setTime(inputFormat.parse("2011-01-20 00:00:00"));

/*
*Futures didn't get added until later.
*/
if (strEntityGroupId.equals("1008"))
	calEarliest.setTime(inputFormat.parse("2011-06-21 00:00:00"));

if (calBegin.before(calEarliest))
	calBegin = calEarliest;




/*
*   Here's where this gets a little funky... we will add two days to calBegin and then get the minimum.
*   And we will subtract 2 days from calEnd and take the maximum. 2 days just being an arbitrary number to try
*    to ensure that we grab at least one data point without grabbing too much data.

	Time line looks like this (for Time frames > 2 days)
    <Earlier-------------------------------------------------Later>		


		<----calBegin-----calBeginAdjust-------calEndAdjust-------calEnd->

   And like this (for time frames < 2 days): 
	   
	   <-----calEndAdjust------calBegin----calEnd-------calBeginAdjust---->
   
   OFP 5/17/2011 - Just noticed an issue with the "< 2 days" query in that potentially
   the calEnd time could come before calBegin;
*/

Calendar calBeginAdjust = Calendar.getInstance();
Calendar calEndAdjust = Calendar.getInstance();

calBeginAdjust.setTime(calBegin.getTime());
calEndAdjust.setTime(calEnd.getTime());

calBeginAdjust.add(Calendar.DAY_OF_MONTH,2);
calEndAdjust.add(Calendar.DAY_OF_MONTH,-2);

//System.out.println(calBegin.getTime().toString());

String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null)
{
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";

if (strEntityGroupId.equals("all"))
	strEntityGroupId = "%";

UtilityFunctions uf = new UtilityFunctions();

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();
//String[] columns = tmpArrayList.get(0);
//String[] blap1 = {"task name","task name","string"};
String[] blap2 = {"ticker","ticker","string"};

//String[] blap3 = {"time event name","time event name","string"};
//String[] blap4 = {"full_name","full_name","string"};
String[] blap3 = {"country","country","string"};
String[] blap4 = {"description","description","string"};
String[] blap5 = {"initial value","initial value","number"};
String[] blap6 = {"current value","current value","number"};
String[] blap7 = {"% change","% change","number"};
String[] blap8 = {"date initial","date initial","datetime"};
String[] blap9 = {"date current","date current","datetime"};

//String[] blap10 = {"te period last","te period last","datetime"};
//String[] blap11 = {"te period next","te period next","datetime"};




//arrayListCols.add(blap1);
arrayListCols.add(blap2);

//arrayListCols.add(blap3);
//arrayListCols.add(blap4);
arrayListCols.add(blap5);
arrayListCols.add(blap6);
arrayListCols.add(blap7);
arrayListCols.add(blap8);
arrayListCols.add(blap9);
arrayListCols.add(blap3);
arrayListCols.add(blap4);
//arrayListCols.add(blap10);
//arrayListCols.add(blap11);

//DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");



String query2 = "select 'mysqldatasource2eh2',fact_data.entity_id,'placeholder',date_format(date_collected,'%Y-%m-%d %T') as date_begin,value,ticker ";
query2 += " ,countries.name,entities.full_name ";
query2 += " from fact_data ";
query2 += " JOIN entities on entities.id=fact_data.entity_id ";
query2 += " LEFT JOIN countries on entities.country_id=countries.id ";
query2 += " JOIN entities_entity_groups on fact_data.entity_id=entities_entity_groups.entity_id ";
query2 += " where date_collected>'" + formatter.format(calBegin.getTime()) + "' ";
query2 += " AND date_collected< '" + formatter.format(calBeginAdjust.getTime()) + "' ";
query2 += " AND entities_entity_groups.entity_group_id=" + strEntityGroupId;
query2 += " AND metric_id=" + strMetricId;
query2 += " ORDER BY fact_data.entity_id ";

//out.println(query2); if (1==1) return;




String query3 = "select 'mysqldatasource2eh2',fact_data.entity_id,'placeholder',date_format(date_collected,'%Y-%m-%d %T') as date_end,value,ticker ";
query3 += " ,countries.name,entities.full_name ";
query3 += " from fact_data ";
query3 += " JOIN entities on entities.id=fact_data.entity_id ";
query3 += " LEFT JOIN countries on entities.country_id=countries.id ";
query3 += " JOIN entities_entity_groups on fact_data.entity_id=entities_entity_groups.entity_id ";
query3 += " where date_collected>'" + formatter.format(calEndAdjust.getTime()) + "'  ";
query3 += " AND date_collected<'" + formatter.format(calEnd.getTime()) + "' ";
query3 += " AND entities_entity_groups.entity_group_id=" + strEntityGroupId;
query3 += " AND metric_id=" + strMetricId;
query3 += " ORDER BY fact_data.entity_id ";

//out.println(query3); if (1==1) return;  



Calendar calEndTimer = null;



DBFunctions dbf = null;
boolean bException = false;
ArrayList<String[]> arrayListRows1 = new ArrayList<String[]>();
int code=1;
boolean bDone = false;
while (!bDone) {
	
	
	try
	{
		dbf = new DBFunctions();
		code=2;
		dbf.db_run_query(query2); 
		calEndTimer = Calendar.getInstance();
		code=3;
		while (dbf.rs.next()) {
			String [] tmp = new String[6];
			
			tmp[0] = dbf.rs.getString("fact_data.entity_id");
			tmp[1] = dbf.rs.getString("date_begin");
			tmp[2] = dbf.rs.getString("value");
			tmp[3] = dbf.rs.getString("ticker");
			tmp[4] = dbf.rs.getString("full_name");
			tmp[5] = dbf.rs.getString("name");
			
	
		
			
			
			
			arrayListRows1.add(tmp);
		    
		}
		bDone = true;
	}
	catch (SQLException sqle)
	{
		//out.println(sqle.toString());
		fulllogger.info("PF ERROR CODE 2eh2-1,subcode:"+code);
		fulllogger.info(sqle.getMessage());	

		
		if (!sqle.getMessage().contains("The last packet successfully received")) {
			bDone = true;
			out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 2eh2-1,subcode:"+code));
			bException = true;
		}
			
	}
	finally
	{
		if (dbf !=null) dbf.closeConnection();
		if (bException == true)
			return;
	}
}



//int diff = calBeginTimer.compareTo(calEndTimer);

//out.println(diff/1000 + " seconds");



//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows1));if (1==1) return; 

bDone = false;

bException = false;
ArrayList<String[]> arrayListRows2 = new ArrayList<String[]>();
while (!bDone) {
	try
	{
		dbf = new DBFunctions();
		dbf.db_run_query(query3);
	
		while (dbf.rs.next()) {
			String [] tmp = new String[6];
			
			tmp[0] = dbf.rs.getString("fact_data.entity_id");
			tmp[1] = dbf.rs.getString("date_end");
			tmp[2] = dbf.rs.getString("value");
			tmp[3] = dbf.rs.getString("ticker");
			tmp[4] = dbf.rs.getString("full_name");
			tmp[5] = dbf.rs.getString("name");
	
			
			arrayListRows2.add(tmp);
		    
		}
		bDone = true;
	}
	catch (SQLException sqle)
	{
		//out.println(sqle.toString());
		fulllogger.info("PF ERROR CODE 2eh2-2,subcode:"+code);
		fulllogger.info(sqle.getMessage());	
		if (!sqle.getMessage().contains("The last packet successfully received")) {
			bDone = true;
			out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 2eh2-2"));
			bException = true;
		}
	}
	finally
	{
		if (dbf !=null) dbf.closeConnection();
		if (bException == true)
			return;
	}
}

int[] tmpArray = {0};

if (arrayListRows1.size() == 0) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_no_data","Query returned no data.","PF ERROR CODE 2eh2-3"));
	return;	
}

if (arrayListRows2.size() == 0) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_no_data","Query returned no data.","PF ERROR CODE 2eh2-4"));
	return;	
}

arrayListRows1 = PopulateSpreadsheet.getMaxMinGroupBy(arrayListRows1,tmpArray,1,"DATE","MIN");



//arrayListRows1 = PopulateSpreadsheet.removeColumn(arrayListRows1,1);

arrayListRows2 = PopulateSpreadsheet.getMaxMinGroupBy(arrayListRows2,tmpArray,1,"DATE","MAX");

/*out.println("<table>");
for (int i=0;i<arrayListRows2.size();i++)
{
	out.println("<tr>");
	String[] temp = arrayListRows2.get(i);
	for (int j=0;j<temp.length;j++)
	{
		out.println("<td>" + temp[j] +"</td>");
	}
	out.println("</tr>");
}
out.println("</table>");

if (1==1) return;*/

//arrayListRows2 = PopulateSpreadsheet.removeColumn(arrayListRows2,1);

ArrayList<String[]> arrayListRows = PopulateSpreadsheet.joinListsInner(arrayListRows1,arrayListRows2,0);











ArrayList<String[]> saveListRows = arrayListRows;

arrayListRows = new ArrayList<String[]>();


for (int i=0;i<saveListRows.size();i++)
{
	String[] tmpSave = saveListRows.get(i);
	
	
	String[] tmp = new String[16];
	
	/*for (int j=0;j<tmpSave.length;j++)
	{
		tmp[(j*2)] = tmp[(j*2)+1] = tmpSave[j];
	}*/
	
	tmp[0] = tmp[1] = tmpSave[3];
	
	
	tmp[2] = tmp[3] = tmpSave[2];
	tmp[4] = tmp[5] = tmpSave[7];
	
	
	BigDecimal bdBef = new BigDecimal(tmpSave[2]);
	BigDecimal bdAft = new BigDecimal(tmpSave[7]);
	
	bdAft = bdAft.subtract(bdBef);
	if (bdBef.compareTo(new BigDecimal(0)) == 0)
		//Bad Data
		bdAft = new BigDecimal(0);
	else
		bdAft = bdAft.divide(bdBef,BigDecimal.ROUND_HALF_UP);
	bdAft = bdAft.multiply(new BigDecimal(100));
	
	//bdAft.subtract(bdBef).divide(bdBef).multiply(new BigDecimal("100"));
	
	bdAft = bdAft.setScale(2,BigDecimal.ROUND_HALF_UP);
	
	tmp[6] = tmp[7] =  bdAft.toString();
		
	tmp[8] = tmp[9] = tmpSave[1];
	tmp[10] = tmp[11] = tmpSave[6];
	
	tmp[12] = tmp[13] = tmpSave[5];
	tmp[14] = tmp[15] = tmpSave[4];
	
	arrayListRows.add(tmp);
	
}

Comparator<String[]> comp = new Comparator<String[]>() {
	  public int compare(String[] first, String[] second) {
		BigDecimal bdFirst = new BigDecimal(first[6]);
		BigDecimal bdSecond = new BigDecimal(second[6]);
		
		return bdSecond.compareTo(bdFirst);
		
	    //return first[1].compareTo(second[1]);
	  }
};

Comparator<String[]> comp2 = new Comparator<String[]>() {
	  public int compare(String[] first, String[] second) {
		BigDecimal bdFirst = new BigDecimal(first[6]);
		BigDecimal bdSecond = new BigDecimal(second[6]);
		
		return bdFirst.compareTo(bdSecond);
		
	    //return first[1].compareTo(second[1]);
	  }
};

if ((strOrder !=null) && (strOrder.toUpperCase().equals("DESC")))
	Collections.sort(arrayListRows,comp);
else
	Collections.sort(arrayListRows,comp2);













out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,false));
 %> 
