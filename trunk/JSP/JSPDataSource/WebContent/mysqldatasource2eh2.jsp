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
DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");

String strTimeFrame = request.getParameter("timeframe");
String strBeginDate = request.getParameter("begindate"); 
String strEndDate = request.getParameter("enddate");
//String strTaskId = request.getParameter("taskid");
String strMetricId = request.getParameter("metricid");
String strEntityGroupId = request.getParameter("entitygroupid");
String strTopMovers = request.getParameter("topmovers");

String strFileName = null;
if (request.getParameter("filename") != null)
	strFileName = request.getParameter("filename");




if (strTimeFrame==null && (strBeginDate == null || strEndDate == null))
{
	out.println("No timeframe or begindate/enddate parameters passed in the url. Exiting.");
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

if (strTimeFrame != null) {

	
	if (Debug.RELEASE == true)   {
		calEnd.set(Calendar.YEAR,2011);
		calEnd.set(Calendar.DAY_OF_MONTH,21);
		calEnd.set(Calendar.MONTH,8);
		
		calBegin.set(Calendar.YEAR,2011);
		calBegin.set(Calendar.DAY_OF_MONTH,21);
		calBegin.set(Calendar.MONTH,8);

		
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
}
else {
	
	calBegin.setTime(new Date(Long.parseLong(strBeginDate)));
	calEnd.setTime(new Date(Long.parseLong(strEndDate)));

}




/*
	Update 3/17/2012: I changed the 2 day adjustement to 4 days since 3 day weekends were causing problems.
*   Here's where this gets a little funky... we will add two days to calBegin and then get the minimum.
*   And we will subtract 4 days from calEnd and take the maximum. 4 days just being an arbitrary number to try
*    to ensure that we grab at least one data point without grabbing too much data.

	Time line looks like this (for Time frames > 4 days) 
    <Earlier-------------------------------------------------Later>		


		<----calBegin-----calBeginAdjust-------calEndAdjust-------calEnd->

   And like this (for time frames < 4 days): 
	   
	   <-----calEndAdjust------calBegin----calEnd-------calBeginAdjust---->
   
   OFP 5/17/2011 - Just noticed an issue with the "< 4 days" query in that potentially
   the calEnd time could come before calBegin
   Update on this issue: I added a check on the data returned, if calEnd < calBegin then an error message
   is display "date range too narrow, please pick a wider date range.
*/

Calendar calBeginAdjust = Calendar.getInstance();
Calendar calEndAdjust = Calendar.getInstance();

calBeginAdjust.setTime(calBegin.getTime());
calEndAdjust.setTime(calEnd.getTime());


Calendar calBeginThreshold1 = Calendar.getInstance();
calBeginThreshold1.setTime(formatter.parse("2011-01-19 00:00:00"));
Calendar calBeginThreshold2 = Calendar.getInstance();
calBeginThreshold2.setTime(formatter.parse("2010-12-01 00:00:00"));

Calendar calEndThreshold1 = Calendar.getInstance();
calEndThreshold1.setTime(formatter.parse("2011-02-01 00:00:00"));

if (calBeginAdjust.after(calBeginThreshold1))
	calBeginAdjust.add(Calendar.DAY_OF_MONTH,4);
else if (calBeginAdjust.after(calBeginThreshold2))
	calBeginAdjust.setTime(formatter.parse("2011-01-22 23:59:59"));
else
	calBeginAdjust.add(Calendar.MONTH,1);

if (calEndAdjust.after(calEndThreshold1))
	calEndAdjust.add(Calendar.DAY_OF_MONTH,-4);
else
	calEndAdjust.add(Calendar.MONTH,-1);

//System.out.println(calBegin.getTime().toString());

String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null) {
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
String[] blap1 = {"entityid", "entityid", "number"};
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

String[] blap10 = {"metricid","metricid","number"};

//String[] blap10 = {"te period last","te period last","datetime"};
//String[] blap11 = {"te period next","te period next","datetime"};





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
arrayListCols.add(blap1);
arrayListCols.add(blap10);
//arrayListCols.add(blap10);
//arrayListCols.add(blap11);

//DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");



String query2a = "select fact_data.entity_id, min(date_format(date_collected,'%Y-%m-%d %T')) as date_begin, ";
query2a += " fact_data.metric_id ";
query2a += " from fact_data ";
query2a += " JOIN entities_entity_groups on fact_data.entity_id=entities_entity_groups.entity_id ";
query2a += " where date_collected>'" + formatter.format(calBegin.getTime()) + "' ";
query2a += " AND date_collected< '" + formatter.format(calBeginAdjust.getTime()) + "' ";
query2a += " AND entities_entity_groups.entity_group_id=" + strEntityGroupId;
query2a += " AND metric_id=" + strMetricId;
query2a += " group by entity_id,metric_id ";




String query2 = "select 'mysqldatasource2eh2',fact_data.entity_id,'placeholder',date_format(fact_data.date_collected,'%Y-%m-%d %T') as date_begin,value,ticker ";
query2 += " ,countries.name,entities.full_name,fact_data.metric_id ";
query2 += " from fact_data ";
query2 += " JOIN (" + query2a + ") as tbl2 on tbl2.date_begin=fact_data.date_collected and tbl2.metric_id=fact_data.metric_id and tbl2.entity_id=fact_data.entity_id ";
query2 += " JOIN entities on entities.id=fact_data.entity_id ";
query2 += " LEFT JOIN countries_entities on countries_entities.entity_id=fact_data.entity_id ";
query2 += " LEFT JOIN countries on countries_entities.country_id=countries.id ";
//query2 += " LEFT JOIN countries on entities.country_id=countries.id ";
//query2 += " JOIN entities_entity_groups on fact_data.entity_id=entities_entity_groups.entity_id ";
//query2 += " where 1=1 ";
//query2 += " AND date_collected>'" + formatter.format(calBegin.getTime()) + "' ";
//query2 += " AND date_collected< '" + formatter.format(calBeginAdjust.getTime()) + "' ";
//query2 += " AND entities_entity_groups.entity_group_id=" + strEntityGroupId;
//query2 += " AND fact_data.metric_id=" + strMetricId;
query2 += " ORDER BY fact_data.entity_id ";

//out.println(query2); if (1==1) return;


String query3a = "select fact_data.entity_id, max(date_format(date_collected,'%Y-%m-%d %T')) as date_begin, ";
query3a += " fact_data.metric_id ";
query3a += " from fact_data ";
query3a += " JOIN entities_entity_groups on fact_data.entity_id=entities_entity_groups.entity_id ";
query3a += " where date_collected<'" + formatter.format(calEnd.getTime()) + "' ";
query3a += " AND date_collected> '" + formatter.format(calEndAdjust.getTime()) + "' ";
query3a += " AND entities_entity_groups.entity_group_id=" + strEntityGroupId;
query3a += " AND metric_id=" + strMetricId;
query3a += " group by entity_id,metric_id ";





String query3 = "select 'mysqldatasource2eh2',fact_data.entity_id,'placeholder',date_format(date_collected,'%Y-%m-%d %T') as date_end,value,ticker ";
query3 += " ,countries.name,entities.full_name,fact_data.metric_id ";
query3 += " from fact_data ";
query3 += " JOIN (" + query3a + ") as tbl2 on tbl2.date_begin=fact_data.date_collected and tbl2.metric_id=fact_data.metric_id and tbl2.entity_id=fact_data.entity_id ";
query3 += " JOIN entities on entities.id=fact_data.entity_id ";
query3 += " LEFT JOIN countries_entities on countries_entities.entity_id=fact_data.entity_id ";
query3 += " LEFT JOIN countries on countries_entities.country_id=countries.id ";
//query3 += " LEFT JOIN countries on entities.country_id=countries.id ";
//query3 += " JOIN entities_entity_groups on fact_data.entity_id=entities_entity_groups.entity_id ";
//query3 += " where 1=1 ";
//query3 += " AND date_collected>'" + formatter.format(calEndAdjust.getTime()) + "'  ";
//query3 += " AND date_collected<'" + formatter.format(calEnd.getTime()) + "' ";
//query3 += " AND entities_entity_groups.entity_group_id=" + strEntityGroupId;
//query3 += " AND fact_data.metric_id=" + strMetricId;
query3 += " ORDER BY fact_data.entity_id ";

//out.println(query3); if (1==1) return;  



//Calendar calEndTimer = null;



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
		//calEndTimer = Calendar.getInstance();
		code=3;
		while (dbf.rs.next()) {
			String [] tmp = new String[7];
			
			tmp[0] = dbf.rs.getString("fact_data.entity_id");
			tmp[1] = dbf.rs.getString("date_begin");
			tmp[2] = dbf.rs.getString("value");
			tmp[3] = dbf.rs.getString("ticker");
			tmp[4] = dbf.rs.getString("full_name");
			tmp[5] = dbf.rs.getString("name");
			tmp[6] = dbf.rs.getString("fact_data.metric_id");
			
	
		
			
			
			
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



//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows1,1000));if (1==1) return; 

bDone = false;

bException = false;
ArrayList<String[]> arrayListRows2 = new ArrayList<String[]>();
while (!bDone) {
	try
	{
		dbf = new DBFunctions();
		dbf.db_run_query(query3);
	
		while (dbf.rs.next()) {
			String [] tmp = new String[7];
			
			tmp[0] = dbf.rs.getString("fact_data.entity_id");
			tmp[1] = dbf.rs.getString("date_end");
			tmp[2] = dbf.rs.getString("value");
			tmp[3] = dbf.rs.getString("ticker");
			tmp[4] = dbf.rs.getString("full_name");
			tmp[5] = dbf.rs.getString("name");
			tmp[6] = dbf.rs.getString("fact_data.metric_id");
	
			
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
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_no_data","No data returned. Please select a different date range.","PF ERROR CODE 2eh2-3"));
	return;	
}

if (arrayListRows2.size() == 0) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_no_data","No data returned. Please select a different date range.","PF ERROR CODE 2eh2-4"));
	return;	
}

//arrayListRows1 = PopulateSpreadsheet.getMaxMinGroupBy(arrayListRows1,tmpArray,1,"DATE","MIN");



//arrayListRows1 = PopulateSpreadsheet.removeColumn(arrayListRows1,1);

//arrayListRows2 = PopulateSpreadsheet.getMaxMinGroupBy(arrayListRows2,tmpArray,1,"DATE","MAX");



ArrayList<String[]> arrayListRows = PopulateSpreadsheet.joinListsInner(arrayListRows1,arrayListRows2,0);

Calendar calA = Calendar.getInstance();
Calendar calB = Calendar.getInstance();

for (int k=0;k<arrayListRows.size();k++) {
	String[] row = arrayListRows.get(k);
	calA.setTime(formatter.parse(row[1]));
	calB.setTime(formatter.parse(row[7]));
	if (calA.after(calB)) {
		out.println(PopulateSpreadsheet.createGoogleError(strReqId,"narrow_date_range","Date range is too narrow. Please pick a wider date range.","PF ERROR CODE 2eh2-5"));
		return;	
	}
		
		
	
}




//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 






ArrayList<String[]> saveListRows = arrayListRows;

arrayListRows = new ArrayList<String[]>();


for (int i=0;i<saveListRows.size();i++)
{
	String[] tmpSave = saveListRows.get(i);
	
	
	String[] tmp = new String[20];
	
	/*for (int j=0;j<tmpSave.length;j++)
	{
		tmp[(j*2)] = tmp[(j*2)+1] = tmpSave[j];
	}*/
	
	tmp[0] = tmp[1] = tmpSave[3];
	
	
	tmp[2] = tmp[3] = tmpSave[2];
	tmp[4] = tmp[5] = tmpSave[8];
	
	
	BigDecimal bdBef = new BigDecimal(tmpSave[2]);
	BigDecimal bdAft = new BigDecimal(tmpSave[8]);
	
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
	tmp[10] = tmp[11] = tmpSave[7];
	
	tmp[12] = tmp[13] = tmpSave[5];
	tmp[14] = tmp[15] = tmpSave[4];
	tmp[16] = tmp[17] = tmpSave[0];
	tmp[18] = tmp[19] = tmpSave[6];
	
	arrayListRows.add(tmp);
	
}
/*
 * Code to take only the X top movers (in both directions)
 *
 */

if (strTopMovers != null) {
	Comparator<String[]> comp3 = new Comparator<String[]>() {
		  public int compare(String[] first, String[] second) {
			BigDecimal bdFirst = new BigDecimal(first[6]);
			bdFirst = bdFirst.abs();
			BigDecimal bdSecond = new BigDecimal(second[6]);
			bdSecond = bdSecond.abs();
			
			return bdSecond.compareTo(bdFirst);
			
		    //return first[1].compareTo(second[1]);
		  }
	};
	
	
	Collections.sort(arrayListRows,comp3);
	
	arrayListRows = PopulateSpreadsheet.limitRows(arrayListRows,strTopMovers);
	
	
}




//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,10));if (1==1) return; 

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



//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,100));if (1==1) return; 



String strOutput = PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,false);

if (strFileName == null)
	out.println(strOutput);
else {
	//File I/O
	out.println(PopulateSpreadsheet.createFile(strOutput,strFileName)); 
}
 %> 
