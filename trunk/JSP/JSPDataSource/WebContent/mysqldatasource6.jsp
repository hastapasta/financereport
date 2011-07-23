<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.Calendar" %>




<%

/*
* This data source is similar to datasource5 except it returns % values.
* This data_source pulls from the fact_data based off of task_id
*
* There is a bPivot parameter which determines if the data is displayed year by country name or vice
* versa. 
*/

Logger fulllogger = Logger.getLogger("FullLogging");



String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null)
{
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";

boolean bPivot = true;
if (request.getParameter("pivot")!=null)
	if (request.getParameter("pivot").toUpperCase().equals("FALSE"))
		bPivot=false;

String strEntityGroupId = request.getParameter("entitygroupid");

if (strEntityGroupId==null)
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","No entitygroupid request parameter.","PF ERROR CODE 6-1"));
	//out.println("No begindate request parameter.");
	return;
}

String strMetricId = request.getParameter("metricid");

if (strMetricId==null)
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","No metricid request parameter.","PF ERROR CODE 6-2"));
	//out.println("No begindate request parameter.");
	return;
}



Calendar cal = Calendar.getInstance();
String strBaseYear="2011";
//strBaseYear=cal.get(Calendar.YEAR);




UtilityFunctions uf = new UtilityFunctions();

String query3 = "select max(fact_data.batch) as mbatch ";
query3 += " from fact_data ";
query3 += " where metric_id=" + strMetricId;

DBFunctions dbf = null;
boolean bException = false;
String strBatch = null;
int code=1;
try
{
	dbf = new DBFunctions();
	code=2;
	dbf.db_run_query(query3); 
	
	dbf.rs.next();
	
	strBatch = dbf.rs.getString("mbatch");
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 2eh2-1,subcode:"+code));
	fulllogger.info("PF ERROR CODE 2eh2-1,subcode:"+code);
	fulllogger.info(sqle.getMessage());	
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}

String query4 = "select distinct calyear ";
query4 += " from fact_data ";
query4 += " where batch=" + strBatch;
query4 += " order by calyear asc ";

dbf = null;
bException = false;
code=1;
String[] calyears=null;
try
{
	dbf = new DBFunctions();
	code=2;
	dbf.db_run_query(query4); 
	String strTmp="";
	while (dbf.rs.next()) {
		strTmp += dbf.rs.getString("calyear") + ",";
	}
	strTmp = strTmp.substring(0,strTmp.length()-2);
	calyears = strTmp.split(",");
	

}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 2eh2-1,subcode:"+code));
	fulllogger.info("PF ERROR CODE 2eh2-1,subcode:"+code);
	fulllogger.info(sqle.getMessage());	
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}






//DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");

String query2 = "select fact_data.value as initval, fact_data.entity_id ";
query2 += "from fact_data ";
//query2 += "join (select min(calyear) as minyear,entity_id from fact_data where metric_id=" + strMetricId + " group by entity_id) as t1 on fact_data.entity_id=t1.entity_id";
//query2 += " where calyear = t1.minyear ";
query2 += " where calyear = " + strBaseYear;
query2 += " AND metric_id=" + strMetricId;
query2 += " AND batch=" + strBatch;


//out.println(query2); if (1==1) return;



String query = "select countries.name,fact_data.calyear";

query += ",round(((fact_data.value - t2.initval)/t2.initval),3)*100 as pctchange ";
//String query = "select fact_data.calyear,countries.name,entities.id ";
query += "from fact_data ";
query += "JOIN entities on fact_data.entity_id=entities.id ";
query += "JOIN countries_entities on countries_entities.entity_id=entities.id ";
query += "JOIN countries on countries.id=countries_entities.country_id ";
query += "JOIN tasks on fact_data.task_id=tasks.id ";
query += "JOIN (" + query2 + ") as t2 on t2.entity_id=fact_data.entity_id ";
query += " where ";
//query += " fact_data.metric_id=" + strMetricId;
query += " fact_data.batch = " +  strBatch;
query += " AND calyear >=" + strBaseYear;
//query += " AND (countries.name like 'A%' OR countries.name like 'B%') ";
if (bPivot==true)
	query += " order by fact_data.calyear,countries.name";
else
	query += " order by countries.name,fact_data.calyear ";



//out.println(query); if (1==1) return;








dbf = null;
bException = false;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		String [] tmp = new String[4];
		//tmp[0] = dbf.rs.getString("entities.id");
		if (bPivot==true) {
			tmp[0] = dbf.rs.getString("fact_data.calyear");
			tmp[1] = dbf.rs.getString("countries.name");
		}
		else {
			tmp[0] = dbf.rs.getString("countries.name");
			tmp[1] = dbf.rs.getString("fact_data.calyear");
		}
		tmp[2] = dbf.rs.getString("pctchange");
		


		
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 6-2"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}

//ArrayList<String[]> arrayListRows3 = PopulateSpreadsheet.joinListsInner(arrayListRows,arrayListRows2,0);



ArrayList<String[]> arrayListCols = null;

arrayListRows = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows);
//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return; 



	
	
	
	/*
	*
	* If doing a pivot, have to populate the columns after the rows
	*/
	
	
	
	String[] pivotColumns = arrayListRows.remove(0);
	
	arrayListCols = new ArrayList<String[]>();
	String[] col1a = {"year","year","string"};
	String[] col1b = {"country","country","string"};
	if (bPivot==true)
		arrayListCols.add(col1a);
	else
		arrayListCols.add(col1b);
	
	for (int i=0;i<pivotColumns.length;i++)
	{
		String[] col3 = new String[3];
		col3[0] = pivotColumns[i];
		col3[1] = pivotColumns[i];
		col3[2] = "number";
		
		arrayListCols.add(col3);
	}
	

	
	


ArrayList<String[]> saveListRows = arrayListRows;

arrayListRows = new ArrayList<String[]>();


for (int i=0;i<saveListRows.size();i++)
{
	String[] tmpSave = saveListRows.get(i);
	String[] tmp = new String[(2*tmpSave.length)];
	
	for (int j=0;j<tmpSave.length;j++)
	{
		tmp[(j*2)] = tmp[(j*2)+1] = tmpSave[j];
	}
	
	arrayListRows.add(tmp);
	
}




out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,false));



%> 
