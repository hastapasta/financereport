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
This data source pulls macro data from fact_data (e.g. gdp)
*/

String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null) {
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";

int nMinCalYear = 2009;
if (request.getParameter("mincalyear")!=null) {
	nMinCalYear = Integer.parseInt(request.getParameter("mincalyear"));
}

//String strMetricId = request.getParameter("metricid");

String[] strMetricIds = null;

if (request.getParameter("metricids")!=null && request.getParameter("metricids").contains(",")){
	strMetricIds = request.getParameter("metricids").split(",");
}

if (strMetricIds==null) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","No metricids request parameter or bad format.","PF ERROR CODE 7-1"));
	//out.println("No begindate request parameter.");
	return;
}

String strFileName = null;
if (request.getParameter("filename") != null)
	strFileName = request.getParameter("filename");

//out.println(strTimeEventId + "," + strUserId + "," + strTaskId + "," + strReqId); if (1==1) return;


UtilityFunctions uf = new UtilityFunctions();

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();

String[] blap1 = {"country","country","string"};
String[] blap2 = {"year","year","number"};
String[] blap3 = {"pctchange","pctchange","number"};


arrayListCols.add(blap1);
arrayListCols.add(blap2);
arrayListCols.add(blap3);


//DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");

/*
* In most cases strMetricIds[0] will be an actual and strMetricIds[1] will be an estimate.
*/


String query2 = "select countries.name, fact_data.value as initval, fact_data.calyear ";
query2 += "from fact_data ";
//query2 += "join (select min(calyear) as minyear,entity_id from fact_data where metric_id=" + strMetricIds[0] + " group by entity_id) as t1 on fact_data.entity_id=t1.entity_id";
query2 += " JOIN countries_entities on countries_entities.entity_id=fact_data.entity_id ";
query2 += " JOIN countries on countries_entities.country_id=countries.id ";
query2 += " where calyear = " + nMinCalYear;
query2 += " AND (metric_id=" + strMetricIds[0] + " OR metric_id=" + strMetricIds[1] + ") ";
query2 += " order by countries.name,fact_data.metric_id,fact_data.date_collected ";


//out.println(query2); if (1==1) return;



//String query = "select fact_data.calyear,countries.name,round(((fact_data.value - t2.initval)/t2.initval),3)*100 as pctchange ";
String query = "select fact_data.calyear,countries.name,fact_data.value as currentval ";
//query += ",t2.initval as initialval ";
query += "from fact_data ";
query += "JOIN entities on fact_data.entity_id=entities.id ";
query += " JOIN countries_entities on countries_entities.entity_id=fact_data.entity_id ";
query += " JOIN countries on countries_entities.country_id=countries.id ";
//query += "JOIN countries on entities.country_id=countries.id ";
query += "JOIN batches on fact_data.batch_id=batches.id ";
query += "JOIN tasks on batches.task_id=tasks.id ";
//query += "JOIN (" + query2 + ") as t2 on t2.entity_id=fact_data.entity_id ";
query += " where ";
query += " (fact_data.metric_id=" + strMetricIds[0] + " OR fact_data.metric_id=" + strMetricIds[1] + ") ";
query += " and calyear >=" + (nMinCalYear + 1);
//query += " AND entities.ticker like 'A%'";
query += " order by fact_data.calyear,countries.name,fact_data.metric_id,fact_data.date_collected ";

//out.println(query); if (1==1) return;





DBFunctions dbf = null;
boolean bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
try {
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
}

dbf = null;
bException = false;
ArrayList<String[]> arrayListRows2 = new ArrayList<String[]>();
try {
	dbf = new DBFunctions();
	dbf.db_run_query(query2);
	while (dbf.rs.next()) {
		String [] tmp = new String[3];
		tmp[0] = dbf.rs.getString("countries.name");
		tmp[1] = dbf.rs.getString("fact_data.calyear");
	
		tmp[2] = dbf.rs.getString("initval");


		
		
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

arrayListRows2 = PopulateSpreadsheet.getLastGroupBy(arrayListRows2,tmpArray);

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows2,1000));if (1==1) return; 

arrayListRows = PopulateSpreadsheet.getLastGroupBy(arrayListRows,tmpArray);

arrayListRows = PopulateSpreadsheet.unionLists(arrayListRows2, arrayListRows); 

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 

arrayListRows = PopulateSpreadsheet.joinListsInner(arrayListRows,arrayListRows2,0);

arrayListRows = PopulateSpreadsheet.removeColumn(arrayListRows,3);

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 

ArrayList<String[]> arrayListRowsTmp = new ArrayList<String[]>();
for (int i=0;i<arrayListRows.size();i++) {

	String[] tmp = new String[6];
	String[] orig = arrayListRows.get(i);
	
	tmp[0] = tmp[1] = orig[0];
	tmp[2] = tmp[3] = orig[1];
	
	/*
	Calculate percent change.
	*/
	
	BigDecimal bdBef = new BigDecimal(orig[3]);
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
	
	tmp[4] = tmp[5] =  bdAft.toString();
		
	/*
	end calculate percent change.
	*/

	
	arrayListRowsTmp.add(tmp);
	
	
	
}
arrayListRows = arrayListRowsTmp;

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return;

String strOutput = PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,false);

if (strFileName == null)
	out.println(strOutput);
else {
	//File I/O
	out.println(PopulateSpreadsheet.createFile(strOutput,strFileName)); 
}

%> 
