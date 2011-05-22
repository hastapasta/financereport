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

String strTqx = request.getParameter("tqx");
String strReqId=null;
if (strTqx!=null)
{
	strReqId = strTqx.substring(strTqx.indexOf("reqId"),strTqx.length());
	strReqId = strReqId.substring(strReqId.indexOf(":")+1,strReqId.length());
}
else
	strReqId="0";

String strEntityId = request.getParameter("entityid");

if (strEntityId==null)
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"missing_parameter","No entityid request parameter.","PF ERROR CODE 11-1"));
	return;
}





UtilityFunctions uf = new UtilityFunctions();

ArrayList<String[]> arrayListCols = new ArrayList<String[]>();



String query = "(select name,(calyear*10+calquarter) as cyq,value,batch ";
query += " from fact_data,metrics ";
query += " where metric_id=4 and entity_id=" + strEntityId + " and metrics.id=fact_data.metric_id) ";
query += " union ";
query += " (select name,(calyear*10+calquarter) as cyq,value,batch ";
query += " from fact_data,metrics where metric_id=5 and entity_id=" + strEntityId + " and metrics.id=fact_data.metric_id) ";
query += " order by cyq asc,name";

ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
DBFunctions dbf = null;
boolean bException = false;
try
{
	dbf = new DBFunctions();
	dbf.db_run_query(query);
	while (dbf.rs.next()) {
		String [] tmp = new String[4];
		tmp[0] = dbf.rs.getString("cyq");
		tmp[1] = dbf.rs.getString("name");
		tmp[2] = dbf.rs.getString("value");
		tmp[3] = dbf.rs.getString("batch");
				
		
		arrayListRows.add(tmp);
	    
	}
}
catch (SQLException sqle)
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 11-2"));
	bException = true;
}
finally
{
	if (dbf !=null) dbf.closeConnection();
	if (bException == true)
		return;
}

if (arrayListRows.size() == 0)
{
	out.println(PopulateSpreadsheet.createGoogleError(strReqId,"no_rows_in_data","No data to display.","PF ERROR CODE 11-3"));
	return;
}

int[] tmpArray = {0,1};
arrayListRows = PopulateSpreadsheet.getMaxGroupBy(arrayListRows,tmpArray,3);

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
