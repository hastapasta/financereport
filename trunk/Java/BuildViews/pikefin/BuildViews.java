package pikefin;

//import UtilityFunctions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Calendar;

//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
//import org.apache.http.protocol.HTTP;

import pikefin.CustomInvalidInputException;



public class BuildViews {
	
	static UtilityFunctions uf;
	
	//static String strJSPHost = "www.pikefin.com/testjsp/JSPDataSource/";
	static String strJSPHost = "localhost:8080/JSPDataSource/";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//UtilityFunctions uf = new UtilityFunctions();
		uf = new UtilityFunctions("localhost", "findata","root","madmax1.","full.log","error.log","sql.log","thread.log");
		if (args.length == 0) {
			System.out.println("valid commands are: DS7, DS15, DS2EH2");
			System.err.println("valid commands are: DS7, DS15, DS2EH2");
			return;
		}
		if (args[0].toUpperCase().equals("DS15")) {
			mysqldatasource15multiplecountry();	
		}
		else if (args[0].toUpperCase().equals("DS2EH2")) {
			/*
			 * Index.php market snapshots.
			 */
			mysqldatasource2eh2();
		}
		else if (args[0].toUpperCase().equals("DS12")) {
			/*
			 * EPS Estimate, EPS Actual, Stock price comparison
			 */
			mysqldatasource12();
		}
		else if (args[0].toUpperCase().equals("DS7")) {
			/*
			 * GDP Motion chart
			 */
			mysqldatasource7();
		}
		else {
			System.out.println("valid commands are: DS7, DS15, DS2EH2");
			System.err.println("valid commands are: DS7, DS15, DS2EH2");
		}
		

	}
	
	public static void mysqldatasource7() {
		
		String strURL = "http://" + strJSPHost + "mysqldatasource7.jsp?metricids=3,2&filename=gdpmotion";
		BuildViews.issueURL(strURL);
		
	}
	
	public static void mysqldatasource12() {
		
		String strURL = "http://" + strJSPHost + "mysqldatasource7.jsp?metricid=2&filename=gdpmotion";
		BuildViews.issueURL(strURL);
		
	}
	
	
	
	
	
	public static void mysqldatasource2eh2 () {
		String baseurl = "http://" + strJSPHost + "mysqldatasource2eh2.jsp?topmovers=8&order=DESC";
		
		String[] timeframes = {"day","week","month","year"};
		String[] assets = {"forex", "index", "commodity", "futures"};
		int[] entitygroupids = {3,5,4,1008};
		int[] metricids = {1,1,11,11};
				
		
		int nOuter = 0;
		
		
		
		
		for (String strTimeFrame : timeframes) {

			String baseurl2 = baseurl + "&timeframe=" + strTimeFrame;
			if (nOuter != 0)
				baseurl2 += "todate";
			
			
			int nInner = 0;
			
			//for (int i=0;i<4;i++) {
			for (String strAsset : assets) {
				
				
				
				String url3 = baseurl2 + "&entitygroupid=" + entitygroupids[nInner];
				url3 += "&filename=" + strTimeFrame + strAsset;
				url3 += "&metricid=" + metricids[nInner];
				url3 += "&tqx=reqId:" + ((nOuter * assets.length) + nInner);
				
				/*switch (i) {
				
				case 0: 
					baseurl += "&entitygroupid=" + entitygroupids[nInner];
					baseurl += "&filename=forex";
					baseurl += "&metricid=1";
					break;
				case 1:
					baseurl += "&entitygroupid=5";
					baseurl += "&filename=index";
					baseurl += "&metricid=1";
					break;
				case 2:
					baseurl += "&entitygroupid=4";
					baseurl += "&filename=commodity";
					baseurl += "&metricid=11";
					break;
				case 3:
					baseurl += "&entitygroupid=1008";
					baseurl += "&filename=futures";
					baseurl += "&metricid=11";
					break;
					
				}*/
				
				/* Don't have to worry about random number since not dealing with browser cache.
				baseurl += "&randNum="; //+ some random number;*/
				
				
				
				BuildViews.issueURL(url3);
				
				nInner++;
				
			}
			nOuter++;
		}
		
		
		
		
	
		
		
		
	}
	
	public static void processCountry(int nId, String strName) throws SQLException {
		
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		
		
		DBFunctions dbf = new DBFunctions("localhost","3306","findata","root","madmax1.");
		
		/*
		 * Need to grab all of the equity indexes for each country.
		 */
		
		String query3 = "select entities.id ";
		query3 += " from entities,entities_entity_groups ";
		query3 += " where entities.id=entities_entity_groups.entity_id ";
		query3 += " and entities.country_id=" + nId;
		query3 += " and entities_entity_groups.entity_group_id=5";
		
		
		rs3 = dbf.db_run_query(query3);
		int nRowCount = 0;
		String strWhereClause = " where (1=1) AND (";
		while (rs3.next()) {
			nRowCount++;
			strWhereClause += "(entities.id=" + rs3.getInt("id") + " AND fact_data.metric_id=1) OR";
			

		}
		if (nRowCount == 0) {
			UtilityFunctions.stdoutwriter.writeln("No equity indexes found for country " + strName + ",id " + nId +  ",skipping.",Logs.WARN,"BV3.9");
			dbf.closeConnection();
			return;
		}
		strWhereClause = strWhereClause.substring(0,strWhereClause.length()-3);
		strWhereClause += ") AND date_format(fact_data.date_collected,'%Y-%m-%d')>'2010-01-01' ";
		
		String query4 = "select id from entities where ticker='macro' and country_id=" + nId;
		
		rs4 = dbf.db_run_query(query4);
		if (rs4.next() == false) {
			UtilityFunctions.stdoutwriter.writeln("No macro entity found for country " + strName + ",id " + nId + ",skipping.",Logs.WARN,"BV1.8");
			dbf.closeConnection();
			return;				
		}
		String strWhereClause2 = " where (1=1) AND (";
		strWhereClause2 += "(entities.id=" + rs4.getInt("id") + " AND fact_data.metric_id=12 AND tasks.id=30)";
		strWhereClause2 += " OR (entities.id=" + rs4.getInt("id") + " AND fact_data.metric_id=13 AND tasks.id=29) ) ";
		strWhereClause2 += " AND concat(calyear+1,'-01-01')>='2010-01-01' ";
		
		String query2 =  "";
		query2 += " select country_id,date_format(fact_data.date_collected,'%Y-%m') as date_col, date_format(fact_data.date_collected,'%d-%H:%i:%s') as time_col, fact_data.batch_id,fact_data.value as fdvalue,entities.ticker as ticker,date_format(fact_data.date_collected,'%Y-%m-%d') as date_col2,null ";
		query2 += " ,batch_id ";
		query2 += " from fact_data ";
		query2 += " JOIN entities on fact_data.entity_id=entities.id ";
		query2 += " JOIN batches on fact_data.batch_id = batches.id ";
		query2 += " JOIN tasks on batches.task_id=tasks.id ";
		query2 += " JOIN metrics on fact_data.metric_id = metrics.id ";
		query2 += strWhereClause;
		/*query2 += " where (1=1) AND ((entities.id= 715 AND fact_data.metric_id=1) OR ";
		query2 += " (entities.id= 716 AND fact_data.metric_id=1) OR (entities.id= 717 AND fact_data.metric_id=1) ";
		query2 += " OR (entities.id= 718 AND fact_data.metric_id=1) OR (entities.id= 719 AND fact_data.metric_id=1) ";
		query2 += " OR (entities.id= 720 AND fact_data.metric_id=1) OR (entities.id= 721 AND fact_data.metric_id=1) ) ";
		query2 += " AND date_format(fact_data.date_collected,'%Y-%m-%d')>'2010-01-01' ";*/
		query2 += " UNION ";
		query2 += " select country_id,concat(calyear+1,'-01') as date_col, '01-00:00:00' as time_col, fact_data.batch_id,fact_data.value as fdvalue,'GDP' as ticker ,concat(calyear+1,'-01-01') date_col2,null ";
		query2 += " ,batch_id ";
		query2 += " from fact_data ";
		query2 += " JOIN entities on fact_data.entity_id=entities.id ";
		query2 += " JOIN batches on fact_data.batch_id=batches.id ";
		query2 += " JOIN tasks on batches.task_id=tasks.id ";
		query2 += " JOIN metrics on fact_data.metric_id = metrics.id ";
		//query2 += " where (1=1) AND ((entities.id= 1243 AND fact_data.metric_id=12 AND tasks.id=30) OR (entities.id= 1243 AND fact_data.metric_id=13 AND tasks.id=29) ) AND concat(calyear+1,'-01-01')>='2010-01-01' ";
		query2 += strWhereClause2;
		query2 += " order by date_col ASC ,ticker ASC, time_col ASC ";
		
		
		/*try {
			dbf.db_run_query(query2);
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Issue with insert statement for country id " + rs.getInt("id") + ",skipping.",Logs.ERROR,"BV2.0");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			continue;
		}*/
		
		//DBFunctions dbf2 = null;
		boolean bException = false;
		ArrayList<String[]> arrayListRows = new ArrayList<String[]>();
		ArrayList<String[]> arrayListRows2 = null;

		DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

		try
		{
			//dbf = new DBFunctions();
			rs2 = dbf.db_run_query(query2);
			while (rs2.next()) {
				String [] tmp = new String[7];
				
				/*if ((dbf.rs.getInt("fact_data.metric_id") == 2) || (dbf.rs.getInt("fact_data.metric_id") == 3)) {
					tmp[0] = "12-31-" + dbf.rs.getString("fact_data.calyear");
					tmp[1] = dbf.rs.getString("metrics.name");
					tmp[2] = dbf.rs.getString("fdvalue");
					tmp[3] = "00:00:01";
				}
				else {*/
					tmp[0] = rs2.getString("date_col");
					tmp[1] = rs2.getString("ticker");
					tmp[2] = rs2.getString("fdvalue");
					tmp[3] = rs2.getString("date_col2");
					tmp[4] = rs2.getString("country_id");
					tmp[5] = rs2.getString("batch_id");
					tmp[6] = rs2.getString("time_col");
				//}


				
				arrayListRows.add(tmp);
			    
			}
		}
		catch (SQLException sqle)
		{
			//out.println(sqle.toString());
			//System.out.println(PopulateSpreadsheet.createGoogleError(strReqId,"sql_exception",sqle.getMessage(),"PF ERROR CODE 15-2"));
			UtilityFunctions.stdoutwriter.writeln(sqle);
			bException = true;
		}
		finally
		{
			//if (dbf !=null) dbf.closeConnection();
			if (bException == true)
				return;
		}
		
		int[] tmpArray = {0,1};
		
		try {
			arrayListRows2 = PopulateSpreadsheet.getFirstGroupBy(arrayListRows,tmpArray);
		}
		
		catch (CustomInvalidInputException ciie) {
			UtilityFunctions.stdoutwriter.writeln(ciie);				
		}
		
		
		String[] columnNames = {"date_col","ticker","fdvalue","date_col2","country_id","batch_id","time_col"};
		arrayListRows2.add(0,columnNames);
		
		/*
		 * TO DO!!!!!!!!
		 * Need to decide if I'm going to save in database or in json format in a file.
		 */
		//dbf.importTableIntoDB(arrayListRows2, "mysqldatasource15multiplecountry", 0);

		
		/*String query2 =  "insert into mysqldatasource15multiplecountry ";
		query2 += " select country_id,date_format(fact_data.date_collected,'%Y-%m') as date_col, date_format(fact_data.date_collected,'%d-%H:%i:%s') as time_col, fact_data.batch_id,fact_data.value as fdvalue,entities.ticker as ticker,date_format(fact_data.date_collected,'%Y-%m-%d') as date_col2,null ";
		query2 += " from fact_data ";
		query2 += " JOIN entities on fact_data.entity_id=entities.id ";
		query2 += " JOIN batches on fact_data.batch_id = batches.id ";
		query2 += " JOIN tasks on batches.task_id=tasks.id ";
		query2 += " JOIN metrics on fact_data.metric_id = metrics.id ";
		query2 += strWhereClause;
		#*query2 += " where (1=1) AND ((entities.id= 715 AND fact_data.metric_id=1) OR ";
		query2 += " (entities.id= 716 AND fact_data.metric_id=1) OR (entities.id= 717 AND fact_data.metric_id=1) ";
		query2 += " OR (entities.id= 718 AND fact_data.metric_id=1) OR (entities.id= 719 AND fact_data.metric_id=1) ";
		query2 += " OR (entities.id= 720 AND fact_data.metric_id=1) OR (entities.id= 721 AND fact_data.metric_id=1) ) ";
		query2 += " AND date_format(fact_data.date_collected,'%Y-%m-%d')>'2010-01-01' ";*#
		query2 += " UNION ";
		query2 += " select country_id,concat(calyear+1,'-01') as date_col, '01-00:00:00' as time_col, fact_data.batch_id,fact_data.value as fdvalue,'GDP' as ticker ,concat(calyear+1,'-01-01') date_col2,null ";
		query2 += " from fact_data ";
		query2 += " JOIN entities on fact_data.entity_id=entities.id ";
		query2 += " JOIN batches on fact_data.batch_id=batches.id ";
		query2 += " JOIN tasks on batches.task_id=tasks.id ";
		query2 += " JOIN metrics on fact_data.metric_id = metrics.id ";
		//query2 += " where (1=1) AND ((entities.id= 1243 AND fact_data.metric_id=12 AND tasks.id=30) OR (entities.id= 1243 AND fact_data.metric_id=13 AND tasks.id=29) ) AND concat(calyear+1,'-01-01')>='2010-01-01' ";
		query2 += strWhereClause2;
		query2 += " order by date_col ASC ,ticker ASC, time_col ASC ";
		
		
		try {
			dbf.db_update_query(query2);
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Issue with insert statement for country id " + rs.getInt("id") + ",skipping.",Logs.ERROR,"BV2.0");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			continue;
		}*/
		
		arrayListRows.clear();
		arrayListRows = null;
		
		arrayListRows2.clear();
		arrayListRows2 = null;
		
		if (rs2 != null) {
			rs2.close();
			rs2 = null;
		}
		
		if (rs3 != null) {
			rs3.close();
			rs3 = null;
		}
		
		if (rs4 != null) {
			rs4.close();
			rs4 = null;
		}
	
		
		
		UtilityFunctions.stdoutwriter.writeln("Successfully inserted data for country " + strName,Logs.STATUS1,"BV3.0");
		
		dbf.closeConnection();
		dbf = null;
		
		System.gc();
		
		
	
		
		
		
		
		
		
	}
		
		
		
	
	
	
	public static void mysqldatasource15multiplecountry() {
		
		
	
		
		//UtilityFunctions uf = new UtilityFunctions();
		DBFunctions dbf2 = null;
		DBFunctions dbf = null;
		
		try {
			dbf2 = new DBFunctions("localhost","3306","findata","root","madmax1.");
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Problem creating DBFucntions",Logs.ERROR,"BV1.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
		}
		
		String query1 = "select id,name from countries";
		
		try {
			ResultSet rs = dbf2.db_run_query(query1);
			
			while (rs.next()) {
				
				//if (rs.getInt("id") != 57)
				//	continue;
				processCountry(rs.getInt("id"),rs.getString("name"));


			}
				
			
				
			
			
			
			
		}
		catch (SQLException sqle) {
			uf.stdoutwriter.writeln(sqle);
		}
		
		
		
		
	}
	
	public static String issueURL(String strURL) {
		int nTmp;
		String returned_content="";
		HttpResponse response;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(strURL);
		try {
			UtilityFunctions.stdoutwriter.writeln("Issuing URL: " + strURL,Logs.STATUS1,"BV1.7");
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			while ((nTmp = in.read()) != -1) {
				//this.nCount++;
				returned_content += (char) nTmp;
			}
			in.close();
			httpclient.getConnectionManager().shutdown();
			UtilityFunctions.stdoutwriter.writeln(returned_content.replace("\n",""),Logs.STATUS1,"BV2.0");
			
		}
		catch (ClientProtocolException pe) {
			UtilityFunctions.stdoutwriter.writeln(pe);
		}
		catch (IOException ioe) {
			UtilityFunctions.stdoutwriter.writeln(ioe);
		}
		return(returned_content);
		
		
	}

}
