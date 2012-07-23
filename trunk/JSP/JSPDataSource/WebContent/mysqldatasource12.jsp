<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>


   

<%

/*
* This data_source pulls earnings estimates from the fact_data table based off of an entity id
*/

UtilityFunctions uf = new UtilityFunctions();

String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null) {
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";

String strEntityId = request.getParameter("entityid");
String strTicker = request.getParameter("ticker");

if (strEntityId==null && strTicker==null) {
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","Url parameter entityid or ticker is required.","PF ERROR CODE 11-2"));
	return;
}

/*
* Look up the id for the ticker.
*/
String query;
DBFunctions dbf;
boolean bException;

if (strTicker != null) {


	//String[] tickerarray = strTickers.split(",");
	dbf = null;
	bException = false;

	
	try	{
		query = "select id from entities where ticker='" + strTicker + "'";
		/*for (String strTick : tickerarray) { 
			query += "'" + strTick + "',";
		}*/
		//query = query.substring(0,query.length()-1);
		//query += ")";
		
		dbf = new DBFunctions();
		dbf.db_run_query(query);
			
		if (dbf.rs.next())
			strEntityId = dbf.rs.getInt("id") + "";
		else {
			out.println(PopulateSpreadsheet.createGoogleError(strReqId,"data_issue","No entity id from for ticker " + strTicker,"PF ERROR CODE 15s-20"));
			bException = true;
		}
		/*while (dbf.rs.next()) {
			strEntityId +=  dbf.rs.getInt("id") + ",";
		    
		}*/
		//strEntityId = strEntityId.substring(0,strEntityId.length()-1);
	}
	catch (SQLException sqle) {
		//out.println(sqle.toString());
		out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15s-5"));
		bException = true;
	}
	finally	{
		if (dbf !=null) dbf.closeConnection();
		if (bException == true)
			return;
	}
}





ArrayList<String[]> arrayListCols = new ArrayList<String[]>();


String query2 = "select value as initval,entity_id,(calyear*10+calquarter) as cyq,calyear,calquarter ";
query2 += "from fact_data,metrics ";
query2 += "where fact_data.metric_id=metrics.id AND ";
query2 += " entity_id=" + strEntityId;
query2 += " AND metric_id=5 ";
query2 += " order by cyq asc limit 1";

//ResultSet rstmp = null;
int nMonth=0;
int nYear = 0;
dbf = null;
bException = false;
try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query2);
	dbf.rs.next();
	nMonth = dbf.rs.getInt("calquarter") * 3;
	nYear = dbf.rs.getInt("calyear");
	
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 12-2"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}




String strCase = "case month(date_collected) when 1 then 1 when 2 then 1 when 3 then 1 ";
strCase += " when 4 then 2 when 5 then 2 when 6 then 2 when 7 then 3 when 8 then 3 when 9 then 3 ";
strCase += " when 10 then 4 when 11 then 4 when 12 then 4 end ";




String query3 = "select value as initval,entity_id, (year(date_collected)*10 + (" + strCase + ")) as initcyq ";
query3 += " from fact_data ";
query3 += "where ";
query3 += " entity_id=" + strEntityId;
query3 += " and metric_id=1 and month(date_collected)=" + nMonth + " and year(date_collected)=" + nYear;
query3 += " order by date_collected DESC";



//out.println(query3); if (1==1) return;







/*String query = "(select metrics.name,(fact_data.calyear*10+fact_data.calquarter) as cyq,value,batch_id,t1.initval,";
query += "(if (initval=0,initval,round(((value - initval)/(if (initval<0,initval*-1,initval))) * 100,2)))as pctchange ";
query += " from fact_data ";
query += " join metrics on fact_data.metric_id=metrics.id ";
query += " join (" + query2 + ") as t1 on t1.entity_id=fact_data.entity_id ";
query += " where metric_id=4 and fact_data.entity_id=" + strEntityId + " and metrics.id=fact_data.metric_id) ";
query += " union ";
query += " (select metrics.name,(fact_data.calyear*10+fact_data.calquarter) as cyq,value,batch_id,t1.initval, ";
query += "(if (initval=0,initval,round(((value - initval)/(if (initval<0,initval*-1,initval))) * 100,2))) as pctchange ";
query += " from fact_data ";
query += " join metrics on fact_data.metric_id=metrics.id ";
query += " join (" + query2 + ") as t1 on t1.entity_id=fact_data.entity_id ";
query += " where metric_id=5 and fact_data.entity_id=" + strEntityId + " and metrics.id=fact_data.metric_id) ";
query += " union ";
query += " (select metrics.name,(year(date_collected)*10+( " + strCase + ")) as cyq, ";
query += " value,batch_id,t2.initval,";
query += "(if (initval=0,initval,round(((value - initval)/initval) * 100,2))) as pctchange ";
query += " from fact_data ";
query += " join metrics on fact_data.metric_id=metrics.id ";
query += " join (" + query3 + ") as t2 on t2.entity_id=fact_data.entity_id ";
query += " where metric_id=1 and fact_data.entity_id=" + strEntityId + " and metrics.id=fact_data.metric_id ";
query += " and t2.initcyq<= (year(date_collected)*10+( " + strCase + ")))";
query += " order by cyq asc,name,batch_id ";*/

query = "(select metrics.name,(fact_data.calyear*10+fact_data.calquarter) as cyq,value,batch_id,t1.initval,";
query += "(if (initval=0,initval,round(((value - initval)/(if (initval<0,initval*-1,initval))),2)))as pctchange ";
query += " from fact_data ";
query += " join metrics on fact_data.metric_id=metrics.id ";
query += " join (" + query2 + ") as t1 on t1.entity_id=fact_data.entity_id ";
query += " where metric_id in (4,5) and fact_data.entity_id=" + strEntityId + " and metrics.id=fact_data.metric_id) ";
query += " union ";
query += " (select metrics.name,(year(date_collected)*10+( " + strCase + ")) as cyq, ";
query += " value,batch_id,t2.initval,";
query += "(if (initval=0,initval,round(((value - initval)/initval),2))) as pctchange ";
query += " from fact_data ";
query += " join metrics on fact_data.metric_id=metrics.id ";
query += " join (" + query3 + ") as t2 on t2.entity_id=fact_data.entity_id ";
query += " where metric_id=1 and fact_data.entity_id=" + strEntityId + " and metrics.id=fact_data.metric_id ";
query += " and t2.initcyq<= (year(date_collected)*10+( " + strCase + ")))";
query += " order by cyq asc,name,batch_id ";


//out.println(query); if (1==1) return;


dbf = new DBFunctions();

/*
 * This data source needs to be fixed... if the initval is 
 */


//ResultSet rs=null;
ArrayList<String[]> arrayListRows = new ArrayList<String[]>();

try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		String [] tmp = new String[4];
		tmp[0] = dbf.rs.getString("cyq");
		tmp[1] = dbf.rs.getString("name");
		tmp[2] = dbf.rs.getString("pctchange");
		tmp[3] = dbf.rs.getString("batch_id");
				
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	//out.println(sqle.toString());
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 12-3"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}



/*out.println(query); if (1==1) return;*/

int[] tmpArray = {0,1};
arrayListRows = PopulateSpreadsheet.getLastGroupBy(arrayListRows,tmpArray);



arrayListRows = PopulateSpreadsheet.removeLastColumn(arrayListRows);







arrayListRows = PopulateSpreadsheet.pivotRowsToColumnsArrayList(arrayListRows);












/*
*
* If doing a pivot, have to populate the columns after the rows
*/

String[] pivotColumns = arrayListRows.remove(0);

String[] col1 = {"quarter/year","quarter/year","string"};
arrayListCols.add(col1);

for (int i=0;i<pivotColumns.length;i++)
{
	if (pivotColumns[i].equals("price"))
		pivotColumns[i] = "share price";
	String[] col3 = new String[3];
	col3[0] = pivotColumns[i];
	col3[1] = pivotColumns[i];
	col3[2] = "number";
	
	arrayListCols.add(col3);
}

//String[] col2 = {"value","value1","number"};
//arrayListCols.add(col2);

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

//out.println(PopulateSpreadsheet.displayDebugTable(arrayListRows,1000));if (1==1) return;



out.println(PopulateSpreadsheet.createGoogleJSON(arrayListCols,arrayListRows,strReqId,false));



%> 
