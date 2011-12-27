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

This data source is being used to verify gdp estimates and actuals.


*/

Calendar calBeginTimer = Calendar.getInstance(); 

Logger fulllogger = Logger.getLogger("FullLogging");




//String strEntityId = request.getParameter("entityid");
String strCountryId = request.getParameter("countryid");

String strGroupBy = "false";
if (request.getParameter("groupby") != null)
	strGroupBy = request.getParameter("groupby");



if (strCountryId==null) {
	out.println("No countryid passed in the url. Exiting.");
	return;
}






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


/*String query3 = "select fact_data.calyear || 'Y' || fact_data.calquarter || 'Q' as calyearquarter, fact_data.calquarter, fact_data.calyear, tasks.name, fact_data.date_collected ";
query3 += " ,fact_data.value, fact_data.meta_set_id ";
query3 += " from fact_data ";
query3 += " join batches on fact_data.batch_id=batches.id ";
query3 += " join tasks on batches.task_id=tasks.id ";
query3 += " where entity_id=" + strEntityId;
query3 += " and fact_data.metric_id in (4,5) ";
query3 += " order by calyearquarter asc, tasks.name, fact_data.date_collected ";*/

String query3 = "select fact_data.calyear || 'Y' || fact_data.calquarter || 'Q' as calyearquarter, fact_data.calquarter, fact_data.calyear, tasks.name, fact_data.date_collected ";
query3 += " ,fact_data.value, fact_data.meta_set_id,fact_data.metric_id ";
query3 += " ,countries.name ";
query3 += " from fact_data ";
query3 += " join batches on fact_data.batch_id=batches.id ";
query3 += " join tasks on batches.task_id=tasks.id ";
query3 += " join countries_entities on countries_entities.entity_id=fact_data.entity_id ";
query3 += " join entities on entities.id=countries_entities.entity_id ";
query3 += " join countries on countries_entities.country_id=countries.id ";
//query3 += " where entity_id=" + strEntityId;
query3 += " where ticker='macro' ";
query3 += " and countries_entities.country_id=" + strCountryId;
query3 += " and fact_data.metric_id in (2,3) ";
query3 += " order by calyear, fact_data.date_collected ";



//out.println(query3);if (1==1) return;



boolean bDone = false;
DBFunctions dbf = null;
boolean bException = false;
int code=1;

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
			
			tmp[0] = dbf.rs.getString("calyear");
			tmp[1] = dbf.rs.getString("tasks.name");
			tmp[2] = dbf.rs.getString("fact_data.date_collected");
			tmp[3] = dbf.rs.getString("fact_data.value");
			tmp[4] = dbf.rs.getString("fact_data.metric_id");
			tmp[5] = dbf.rs.getString("countries.name");
			
				
			
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


if (arrayListRows2.size() == 0) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_no_data","Query returned no data.","PF ERROR CODE 2eh2-4"));
	return;	
}

if (strGroupBy.compareToIgnoreCase("false") == 0) {
	out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows2,1000));
	if (1==1) return;
}



int[] tmpArray = {0,1};
arrayListRows2 = PopulateSpreadsheet.getLastGroupBy(arrayListRows2,tmpArray);

out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows2,1000));if (1==1) return;






//out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,false));
 %> 
