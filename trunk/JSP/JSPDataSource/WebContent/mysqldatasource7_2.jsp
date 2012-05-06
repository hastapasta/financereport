<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.math.BigDecimal" %>




<%
/*
This data source is for global gasoline prices.
Note that currently 2 data points are populated into the fact_data table per batch
e.g. Norway and Italy. This query sorts by date and takes the most recent one.
*/

String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null) {
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";

int nMinCalYear = 2007;
if (request.getParameter("mincalyear")!=null) {
	nMinCalYear = Integer.parseInt(request.getParameter("mincalyear"));
}

/*
* CountryId parameter is for debugging purposes.
*/
String strCountryId = null;
if (request.getParameter("countryid")!=null) {
	strCountryId = request.getParameter("countryid");
}

/*
* bDateCollected indicates if the date_collected value should be included 
* in the output.
*/

boolean bDateCollected = false;
String blap = request.getParameter("datecollected");
if (request.getParameter("datecollected")!=null) {
	if (request.getParameter("datecollected").equalsIgnoreCase("true"))
			bDateCollected = true;
}

out.println(blap);


/*String[] strMetricIds = null;

if (request.getParameter("metricids")!=null && request.getParameter("metricids").contains(",")){
	strMetricIds = request.getParameter("metricids").split(",");
}

if (strMetricIds==null) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","No metricids request parameter or bad format.","PF ERROR CODE 7-1"));
	//out.println("No begindate request parameter.");
	return;
}*/

String strFileName = null;
if (request.getParameter("filename") != null)
	strFileName = request.getParameter("filename");

//out.println(strTimeEventId + "," + strUserId + "," + strTaskId + "," + strReqId); if (1==1) return;


UtilityFunctions uf = new UtilityFunctions();

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();

String[] blap1 = {"country","country","string"};
//String[] blap2 = {"date","date","datetime"};
String[] blap2 = {"year","year","number"};
String[] blap3 = {"Gasoline Price (USD/Gallon)","Gasoline Price (USD/Gallon)","number"};
String[] blap4 = {"Region", "Region","string"};
String[] blap5 = {"Updated", "Updated", "datetime" };


arrayListCols.add(blap1);
arrayListCols.add(blap2);
arrayListCols.add(blap3);
arrayListCols.add(blap4);
if (bDateCollected)
	arrayListCols.add(blap5);



/*
* In most cases strMetricIds[0] will be an actual and strMetricIds[1] will be an estimate.
*/

String query3 = "select *,year(batch_date_collected) as bdcy from batches where task_id=39 ";
query3 += " order by batch_date_collected desc ";
query3 += " limit 1";
String strMaxBatch = null;
String strBatchDateCollected = null;

DBFunctions dbf = null;
boolean bException = false;
try {
	dbf = new DBFunctions();
	dbf.db_run_query(query3);
	dbf.rs.next();
	strMaxBatch = dbf.rs.getString("id");
	strBatchDateCollected = dbf.rs.getString("bdcy");
}
catch (SQLException sqle) {
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 7-4"));
	bException = true;
}
finally {
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}



String query2 = "select countries.name, fact_data.value as price, fact_data.date_collected, regions.name ";
query2 += "from fact_data ";
query2 += " JOIN countries_entities on countries_entities.entity_id=fact_data.entity_id ";
query2 += " JOIN countries on countries_entities.country_id=countries.id ";
query2 += " JOIN countries_regions on countries_regions.country_id=countries.id ";
query2 += " JOIN regions on regions.id=countries_regions.region_id ";
//query2 += " where calyear = " + nMinCalYear;
//query2 += " AND (metric_id=" + strMetricIds[0] + " OR metric_id=" + strMetricIds[1] + ") ";
query2 += " and metric_id=1002";
query2 += " and batch_id=" + strMaxBatch;
query2 += " order by countries.name,fact_data.date_collected desc";


//out.println(query2); if (1==1) return;







dbf = null;
bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
/*try {
	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		String [] tmp = new String[3];
		tmp[0] = dbf.rs.getString("countries.name");
		tmp[1] = dbf.rs.getString("fact_data.calyear");
		//tmp[4] = tmp[5] = dbf.rs.getString("pctchange");
		tmp[2] = dbf.rs.getString("currentval");
		//tmp[6] = tmp[7] = dbf.rs.getString("initialval");

		
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle) {
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 7-2"));
	bException = true;
}
finally {
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}

if (arrayListRows.size() == 0) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_no_data","No data returned.","PF ERROR CODE 7-3"));
	return;	
}*/

dbf = null;
bException = false;
ArrayList<String[]> arrayListRows2 = new ArrayList<String[]>();
try {
	dbf = new DBFunctions();
	dbf.db_run_query(query2);
	while (dbf.rs.next()) {
		
		//String [] tmp = new String[8];
		String[] tmp = new String[arrayListCols.size() * 2];
		tmp[0] = tmp[1] = dbf.rs.getString("countries.name");
		//tmp[2] = tmp[3] = dbf.rs.getString("fact_data.date_collected");
		tmp[2] = tmp[3] = strBatchDateCollected;
	
		tmp[4] = tmp[5] = dbf.rs.getString("price");
		tmp[6] = tmp[7] = dbf.rs.getString("regions.name");
		if (bDateCollected == true)
			tmp[8] = tmp[9] = dbf.rs.getString("date_collected");
		


		
		
		arrayListRows2.add(tmp);
	    
	}
}
catch (SQLException sqle) {
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 7-4"));
	bException = true;
}
finally {
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}

if (arrayListRows2.size() == 0) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_no_data","No data returned.","PF ERROR CODE 7-5"));
	return;	
}

int[] tmpArray = {0,1};

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows2,1000));if (1==1) return;

arrayListRows2 = PopulateSpreadsheet.getFirstGroupBy(arrayListRows2,tmpArray);

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows2,1000));if (1==1) return; 

//arrayListRows = PopulateSpreadsheet.getLastGroupBy(arrayListRows,tmpArray);

//arrayListRows = PopulateSpreadsheet.unionLists(arrayListRows2, arrayListRows); 

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 

//arrayListRows = PopulateSpreadsheet.joinListsInner(arrayListRows,arrayListRows2,0);

//arrayListRows = PopulateSpreadsheet.removeColumn(arrayListRows,3);

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 

/*ArrayList<String[]> arrayListRowsTmp = new ArrayList<String[]>();
for (int i=0;i<arrayListRows.size();i++) {

	String[] tmp = new String[6];
	String[] orig = arrayListRows.get(i);
	
	tmp[0] = tmp[1] = orig[0];
	tmp[2] = tmp[3] = orig[1];*/
	
	/*
	Calculate percent change.
	*/
	
	/*BigDecimal bdBef = new BigDecimal(orig[3]);
	BigDecimal bdAft = new BigDecimal(orig[2]);
	
	bdAft = bdAft.subtract(bdBef);
	if (bdBef.compareTo(new BigDecimal(0)) == 0)
		//Bad Data
		bdAft = new BigDecimal(0);
	else
		bdAft = bdAft.divide(bdBef,BigDecimal.ROUND_HALF_UP);
	bdAft = bdAft.multiply(new BigDecimal(100));
	
	//bdAft.subtract(bdBef).divide(bdBef).multiply(new BigDecimal("100"));
	
	bdAft = bdAft.setScale(2,BigDecimal.ROUND_HALF_UP);
	
	tmp[4] = tmp[5] =  bdAft.toString();*/
		
	/*
	end calculate percent change.
	*/

	
	/*arrayListRowsTmp.add(tmp);
	
	
	
}
arrayListRows = arrayListRowsTmp;*/  

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return;

String strOutput = PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows2,strReqId,false);

if (strFileName == null)
	out.println(strOutput);
else {
	//File I/O
	out.println(PopulateSpreadsheet.createFile(strOutput,strFileName)); 
}

%> 
