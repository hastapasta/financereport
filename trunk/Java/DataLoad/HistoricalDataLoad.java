import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.NDC;


public class HistoricalDataLoad {
	
	public static Calendar calCurrent;
	public static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void initiateHistoricalDataLoad(DBFunctions dbf,int nMaxBatch)
	{
		//yahooFinanceVerify(dbf,nMaxBatch);
		//yahooFinanceDataLoad(dbf,nMaxBatch);
		//xrateorgDataLoad(dbf,194660);
		//commodityDataLoad(dbf,194661);
		//bloombergIndexDataLoadFromFile(dbf,0);
		//updateBatchDate(dbf);
		
		//testFunc();
		testFunc2();
		
	}
	
	public static void testFunc2(){

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE,40);
		cal.set(Calendar.AM_PM,Calendar.AM);
		System.out.println("1 UTC -4 Hour:" + cal.get(Calendar.HOUR_OF_DAY));
		System.out.println("1 UTC -4 Day:" + cal.get(Calendar.DAY_OF_MONTH));
		System.out.println("1 UTC -7 Time Stamp:" + cal.getTime().toString());
		cal.set(Calendar.HOUR,12);
		System.out.println("2 UTC -4 Hour:" + cal.get(Calendar.HOUR_OF_DAY));
		System.out.println("2 UTC -4 Day:" + cal.get(Calendar.DAY_OF_MONTH));
		System.out.println("2 UTC -7 Time Stamp:" + cal.getTime().toString());
		cal.setTimeZone(TimeZone.getTimeZone("America/New_York")); //set time zone to UTC -4
		System.out.println("3 UTC -4 Hour:" + cal.get(Calendar.HOUR_OF_DAY));
		System.out.println("3 UTC -4 Day:" + cal.get(Calendar.DAY_OF_MONTH));
		System.out.println("3 UTC -7 Time Stamp:" + cal.getTime().toString());
		cal.set(Calendar.HOUR,12);
		System.out.println("4 UTC -4 Hour:" + cal.get(Calendar.HOUR_OF_DAY));
		System.out.println("4 UTC -4 Day:" + cal.get(Calendar.DAY_OF_MONTH));
		System.out.println("4 UTC -7 Time Stamp:" + cal.getTime().toString());
		
		
	}
	
	public static void testFunc() {
		String strTime = "12:40pm";
		String strHour = strTime.substring(0,strTime.indexOf(":"));
		String strMinute = strTime.substring(strTime.indexOf(":")+1,strTime.indexOf(":")+3);
		String strPeriod = strTime.substring(strTime.length()-2,strTime.length());
		
		Calendar cal2 = Calendar.getInstance();
		//cal2.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		//cal2.set(Calendar.)
		//cal2.set(Calendar.HOUR, Integer.parseInt(strHour) );
		cal2.set(Calendar.HOUR, 12);
		cal2.set(Calendar.MINUTE,Integer.parseInt(strMinute));
		
		if (strPeriod.equals("pm"))
			cal2.set(Calendar.AM_PM,Calendar.PM);
		else
			cal2.set(Calendar.AM_PM,Calendar.AM);
		cal2.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		
		Calendar cal3 = Calendar.getInstance();
		int nHour = Integer.parseInt(strHour);
		cal3.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		//cal2.set(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH),cal2.get(Calendar.DAY_OF_MONTH,nHour,nMinute,0);
		
		
		
	}
	
	public static void updateBatchDate(DBFunctions dbf) {
		try {
			String tmp = "select * from fact_data2 where entity_id=1";
			ResultSet rs = dbf.db_run_query(tmp);
			while (rs.next()) {
				
				String strUpdate = "update batches3 set batch_date_collected='" + formatter.format(rs.getTimestamp("date_collected")) + "'";
				strUpdate += " where id=" + rs.getString("batch_id");
				
				dbf.db_update_query(strUpdate);
				
				
				
				
			}
		} 
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		
	}

	public static void yahooFinanceVerify(DBFunctions dbf, int nMaxBatch)
	{
		
		String strTask = "10";
		
		try
		{
			DBFunctions tmpdbf = new DBFunctions((String)DataLoad.props.get("dbhost"),(String)DataLoad.props.get("dbport"),(String)DataLoad.props.get("database"),(String)DataLoad.props.get("dbuser"),(String)DataLoad.props.get("dbpass"));
			DataGrab dg = new DataGrab(DataLoad.uf,tmpdbf,strTask,nMaxBatch, "", "");
		}
		catch (SQLException sqle)
		{
			UtilityFunctions.stdoutwriter.writeln("Issue with database",Logs.ERROR,"DL110.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
		}
		
		
		
		
	}
	
	public static void commodityDataLoad(DBFunctions dbf, int nMaxBatch) {
		
		
		nMaxBatch = 194661;
		
		
		DBFunctions tmpdbf = null;
		try {
			tmpdbf = new DBFunctions((String)DataLoad.props.get("dbhost"),(String)DataLoad.props.get("dbport"),(String)DataLoad.props.get("database"),(String)DataLoad.props.get("dbuser"),(String)DataLoad.props.get("dbpass"));
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Issue instantiating DBFunctions",Logs.ERROR,"HDL101.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
		}
		
		ArrayList<String []> data = UtilityFunctions.readInCSV("/tmp/commodity_data.csv", ",", "\"");
		
		data.remove(0);
		String[] arrayEntityIds = (String[])data.remove(0);
		
		ArrayList<String[]> insertData = null;
		
		Calendar cal = Calendar.getInstance();
		
		for (int i=0;i<data.size();i++) {
			
			insertData = new ArrayList<String[]>();
			String[] tmpArray = {"value","date_collected","entity_id"};
			insertData.add(tmpArray);
			String[] values = data.get(i);
			
			for (int j=1;j<values.length;j++) {
				if (values[j].isEmpty())
					continue;
				
				String[] tmpArray2 = new String[3];
				tmpArray2[0] = values[j];
				
				String[] strData = values[0].split("/");
				
				
				cal.set(Calendar.MONTH,Integer.parseInt(strData[0])-1);
				cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(strData[1]));
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE,59);
				cal.set(Calendar.SECOND,0);
				
				tmpArray2[1] = "'" + formatter.format(cal.getTime()) + "'";
				
				tmpArray2[2] = arrayEntityIds[j];
				
				insertData.add(tmpArray2);
	
				
			}
			
			
			
			
			tmpdbf.importTableIntoDB(insertData, "fact_data_stage", nMaxBatch, 12, 11);
			
			String insertBatch = "insert into batches (id,batch_date_collected,task_id) values (" + nMaxBatch + ",'" + formatter.format(cal.getTime()) + "',12)";
			
			try {
				tmpdbf.db_update_query(insertBatch);
			}
			catch (SQLException sqle) {
				UtilityFunctions.stdoutwriter.writeln(sqle);
			}
			
			UtilityFunctions.stdoutwriter.writeln("Inserted data for day " + formatter.format(cal.getTime()) + " and batch " + nMaxBatch,Logs.STATUS1,"HDL102.5");
			
			
			
			
			
			
			nMaxBatch += 10;
		}
		
		
		
		System.out.println("test");
		
		
		
		
		
		
		
		
	}
	
public static void bloombergIndexDataLoadFromFile(DBFunctions dbf, int nMaxBatch) {
		
		
		int nBaseBatch = 194663;
		
		
		DBFunctions tmpdbf = null;
		try {
			tmpdbf = new DBFunctions((String)DataLoad.props.get("dbhost"),(String)DataLoad.props.get("dbport"),(String)DataLoad.props.get("database"),(String)DataLoad.props.get("dbuser"),(String)DataLoad.props.get("dbpass"));
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Issue instantiating DBFunctions",Logs.ERROR,"HDL101.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
		}
		
		ArrayList<String []> data = UtilityFunctions.readInCSV("/tmp/bloomberg_index_sheet3.csv", ",", "\"");
		
		data.remove(0);
		//String[] arrayEntityIds = (String[])data.remove(0);
		
		//ArrayList<String[]> insertData = null;
		
		Calendar cal = Calendar.getInstance();
		
		for (int i=0;i<data.size();i++) {
			
			String[] values = data.get(i);
			
			if (values.length < 2)
				continue;
			
			
			
			String[] dateValues = values[1].split("/");
			
			if (dateValues[0].equals("7") && dateValues[1].equals("25"))
				continue;
			
			//insertData = new ArrayList<String[]>();
			//String[] tmpArray = {"value","date_collected","entity_id"};
			//insertData.add(tmpArray);
			
			
			
			cal.set(Calendar.MONTH, Integer.parseInt(dateValues[0])-1);
			cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateValues[1]));
			cal.set(Calendar.YEAR,2011);
			cal.set(Calendar.HOUR_OF_DAY,15);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND,0);
			
			String lookupquery = "select id from entities where ticker='" + values[0] + "'";
			String strEntityId = "";
			
			try {
				ResultSet rs = tmpdbf.db_run_query(lookupquery);
				rs.next();
				strEntityId = rs.getInt("id") + "";
				
			}
			catch (SQLException sqle) {
				UtilityFunctions.stdoutwriter.writeln("Unable to locate entity ticker, skipping load",Logs.ERROR,"HDL5.2");
				UtilityFunctions.stdoutwriter.writeln(sqle);
				continue;
			}
			
			int nBatch;
			
			if (dateValues[0].equals("7"))
				nBatch = nBaseBatch + (10 * (Integer.parseInt(dateValues[1]) - 26));
			else if (dateValues[0].equals("8"))
				nBatch = nBaseBatch + 50 + (10 * Integer.parseInt(dateValues[1]));
			else {
				UtilityFunctions.stdoutwriter.writeln("Invalid month value, skipping load",Logs.ERROR,"HDL5.3");
				continue;
			}
			
			/*run this code on the first data set */
			
			
			/*String insertbatchquery = "insert into batches5 (id,batch_date_collected,task_id) values (";
			insertbatchquery += nBatch + ",";
			insertbatchquery += "'" + HistoricalDataLoad.formatter.format(cal.getTime()) + "',"; //date collected
			insertbatchquery += "6)";
			
			try {
				tmpdbf.db_update_query(insertbatchquery);
			}
			catch (SQLException sqle) {
				UtilityFunctions.stdoutwriter.writeln(sqle);
			}*/
			
			
			
			/*
			 * End batch insertion code. Remove this block after running once.
			 */
			
			
				
			
			
			
			if (values.length == 2)
				continue;
			
			String strValue = values[2].replace(",", "");
			
			String query = "insert into fact_data5 (value,date_collected,entity_id,metric_id,batch_id) values ( ";
			query += strValue + ","; //value
			query += "'" + HistoricalDataLoad.formatter.format(cal.getTime()) + "',"; //date collected
			query += strEntityId + ","; //entity id
			query += "1,"; //metric id
			query += nBatch + ")"; //batch id
			
			/*
			 * 
			 */
			try {
				tmpdbf.db_update_query(query);
			}
			catch (SQLException sqle) {
				UtilityFunctions.stdoutwriter.writeln(sqle);
			}
			
			
			/*
			 * Also remove this code after running the first time
			 */
			
			//if (dateValues[0].equals("8") && dateValues[1].equals("31"))
				//break;
			
			/*
			 * End remove block
			 */
			
		
			
			
			
			
		
			
			UtilityFunctions.stdoutwriter.writeln("Inserted data for ticker " + values[0] + ", day " + formatter.format(cal.getTime()) + " and batch " + nBatch,Logs.STATUS1,"HDL102.5");
			
			

		}
		
		
		
	
		
		
		
		
		
		
		
	}
	
	public static void xrateorgDataLoad(DBFunctions dbf, int nMaxBatch) {
		
		
		//loop through dates 
		//need to collect data from 7/26 to 8/27
		boolean bdone = false;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH,26);
		cal.set(Calendar.MONTH,6);
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
	
		int nBatch = 0;
		DBFunctions tmpdbf = null;
		ArrayList<String[]> tabledata = new ArrayList<String[]>();
		String[] firstrow = {"value","date_collected","entity_id"};
		tabledata.add(firstrow);
		HttpResponse response;
		
		try {
			tmpdbf = new DBFunctions((String)DataLoad.props.get("dbhost"),(String)DataLoad.props.get("dbport"),(String)DataLoad.props.get("database"),(String)DataLoad.props.get("dbuser"),(String)DataLoad.props.get("dbpass"));
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln("Issue instantiating DBFunctions",Logs.ERROR,"HDL101.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
		}
		
		String update = "update extract_singles set row_count=138 where id=111186";
		
		try {
			tmpdbf.db_update_query(update);
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		
		
		DataGrab dg = new DataGrab(DataLoad.uf,tmpdbf,"1",nMaxBatch,"","");
		
		String strURL;
		
		String strQuery = "select ticker,entities.id from entities_entity_groups,entities where entity_group_id=3 and entities_entity_groups.entity_id=entities.id";
		
		String strJobQuery = "select * from jobs where id=11253";
		String strRawURL = "";
		
		try {
			ResultSet rs1 = tmpdbf.db_run_query(strJobQuery);
			rs1.next();
			strRawURL = rs1.getString("url_static");
				
		}
		catch (SQLException sqle) {
			UtilityFunctions.stdoutwriter.writeln(sqle);
		}
		
		
		
		while (bdone == false) {
			
			//set the table row and batch #
			
			//loop through crosses
			
			try {
				ResultSet rs = tmpdbf.db_run_query(strQuery);
				while (rs.next()) {
					
					String strCross = rs.getString("ticker");
					
					String strTmp = strCross.substring(3,6);
					strTmp += "/" + strCross.substring(0,3);
					
					strURL = strRawURL.replace("${dynamic}", strTmp);
					String strTemp="";
					
					HttpClient httpclient = new DefaultHttpClient();
					
					HttpGet httpget = new HttpGet(strURL); 
					
					httpget.getParams().setParameter("http.protocol.cookie-datepatterns", 
							Arrays.asList("EEE, dd MMM-yyyy-HH:mm:ss z", "EEE, dd MMM yyyy HH:mm:ss z"));
					
					httpget.getParams().setParameter("http.protocol.user-agent", "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");
					
					try {
						response = httpclient.execute(httpget);
						
						HttpEntity entity = response.getEntity();
						
						BufferedReader in = new BufferedReader(
								new InputStreamReader(
								entity.getContent()));
						
						int nTmp;	
						
						
				
						while ((nTmp = in.read()) != -1)
							strTemp += (char)nTmp;
				
				
						in.close();
					  
						httpclient.getConnectionManager().shutdown();
						
						
						dg.returned_content = strTemp;
						try {
							String strVal = dg.get_value("xrateorg_historical");
							String[] tmpArray = new String[3];
							tmpArray[0] = strVal;
							tmpArray[1] = "'" + formatter.format(cal.getTime()) + "'";
							tmpArray[2] = rs.getString("id");
							
							tabledata.add(tmpArray);
							UtilityFunctions.stdoutwriter.writeln("Collected value " + strVal + " for cross " + strCross,Logs.STATUS1,"HDL102.5");
						}
						catch (CustomRegexException cree) {
							UtilityFunctions.stdoutwriter.writeln(cree);
							
						}
						catch (TagNotFoundException tnfe) {
							UtilityFunctions.stdoutwriter.writeln(tnfe);
						}
						
						
					}
					catch (IOException ioe) {
						UtilityFunctions.stdoutwriter.writeln(ioe);
					}
					
				
					
					
				
					
					
					
				}
			}
			catch (SQLException sqle) {
				UtilityFunctions.stdoutwriter.writeln("Issue running query",Logs.ERROR,"HDL102.5");
				UtilityFunctions.stdoutwriter.writeln(sqle);
				
			}
			
			
			tmpdbf.importTableIntoDB(tabledata, "fact_data_stage", nMaxBatch, 1, 1);
			
			String insertBatch = "insert into batches (id,batch_date_collected,task_id) values (" + nMaxBatch + ",'" + formatter.format(cal.getTime()) + "',1)";
			
			try {
				tmpdbf.db_update_query(insertBatch);
			}
			catch (SQLException sqle) {
				UtilityFunctions.stdoutwriter.writeln(sqle);
			}
			
			UtilityFunctions.stdoutwriter.writeln("Inserted data for day " + formatter.format(cal.getTime()) + " and batch " + nMaxBatch,Logs.STATUS1,"HDL102.5");
		
			update = "update extract_singles set row_count=row_count+1 where id=111186";
			
			try {
				tmpdbf.db_update_query(update);
			}
			catch (SQLException sqle) {
				UtilityFunctions.stdoutwriter.writeln(sqle);
			}
			
			
			nMaxBatch += 4;
			
			cal.add(Calendar.DAY_OF_MONTH, 1);
			
			if (cal.get(Calendar.DAY_OF_MONTH)==28 && cal.get(Calendar.MONTH)== 7)
				break;
			
		}
		
		
		
		
		
		
	}
	
	public static void yahooFinanceDataLoad(DBFunctions dbf, int nMaxBatch)
	{
		String url = "http://ichart.finance.yahoo.com/table.csv?s=INTC&a=01&b=23&c=2009&d=02&e=05&f=2009&g=m";
		
		String baseURL="http://ichart.finance.yahoo.com/table.csv?g=d";
		
		nMaxBatch = 194662;
		
		calCurrent = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		
		calCurrent.set(Calendar.DAY_OF_MONTH, 26);
		calCurrent.set(Calendar.MONTH,6);
		calCurrent.set(Calendar.YEAR,2011);
		calCurrent.set(Calendar.HOUR_OF_DAY,16);
		calCurrent.set(Calendar.MINUTE,15);
		calCurrent.set(Calendar.SECOND,0);
		
		calEnd.set(Calendar.DAY_OF_MONTH, 29);
		calEnd.set(Calendar.MONTH,7);
		calEnd.set(Calendar.YEAR,2011);
		
		/* Set day to last day of month */
		//calCurrent.add(Calendar.MONTH, 1);
		//calCurrent.add(Calendar.DAY_OF_YEAR,-1);
		
		try
		{
			String strTask="25";
			String currentURL;
			
			//Need to use earlier batch #s
			//nMaxBatch = 60047;
			
			
			while (calEnd.after(calCurrent))
			{
				
				if ((calCurrent.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calCurrent.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
					calCurrent.add(Calendar.DAY_OF_MONTH,1);
					nMaxBatch += 10;
					continue;
				}
				
				
				NDC.push(calCurrent.getTime().toString());
				
				//UtilityFunctions.stdoutwriter.writeln("This is a test",Logs.STATUS1,"HDL4");
				
				
			
				
				currentURL = baseURL;
				
				currentURL += "&b=" + calCurrent.get(Calendar.DAY_OF_MONTH);
				currentURL += "&a=" + calCurrent.get(Calendar.MONTH);
				currentURL += "&c=" + calCurrent.get(Calendar.YEAR);
				
				
							
				currentURL += "&e=" + calCurrent.get(Calendar.DAY_OF_MONTH);
				currentURL += "&d=" + calCurrent.get(Calendar.MONTH);
				currentURL += "&f=" + calCurrent.get(Calendar.YEAR);
				
				
				

								
				currentURL += "&s=${dynamic}";
				
				String update = "update jobs set url_static='" + currentURL + "' where id=11246";
				
				dbf.db_update_query(update);		
				
				DBFunctions tmpdbf = new DBFunctions((String)DataLoad.props.get("dbhost"),(String)DataLoad.props.get("dbport"),(String)DataLoad.props.get("database"),(String)DataLoad.props.get("dbuser"),(String)DataLoad.props.get("dbpass"));
				DataGrab dg = new DataGrab(DataLoad.uf,tmpdbf,strTask,nMaxBatch,"","");
				
				dg.startThread();
				UtilityFunctions.stdoutwriter.writeln("Initiated DataGrab thread " + dg.getName(),Logs.STATUS1,"HDL4");
				
				while (dg.getState() != Thread.State.TERMINATED)
				{
					try
					{
						Thread.sleep(5000);
					}
					catch(InterruptedException ie)
					{
						UtilityFunctions.stdoutwriter.writeln(ie);
					}
				}
				
				//check if data got inserted if not, go back a day in the month
				/*String select = "select count(id) as cnt from fact_data where batch=" + nMaxBatch;
				
				ResultSet rs1 = dbf.db_run_query(select);
				rs1.next();
				if (rs1.getInt("cnt")==0)
				{
					
					
					calCurrent.add(Calendar.DAY_OF_YEAR,-1);					
				}
				else
				{
					
					nMaxBatch++;
					
					calCurrent.set(Calendar.DAY_OF_MONTH, 1);
					calCurrent.add(Calendar.MONTH, 2);
					calCurrent.add(Calendar.DAY_OF_YEAR, -1);
										
				}*/
				
				nMaxBatch += 10;
				calCurrent.add(Calendar.DAY_OF_MONTH,1);
				
				NDC.pop();
				  
				
				
				
			}
		}
		catch (SQLException sqle)
		{
			
			UtilityFunctions.stdoutwriter.writeln("Issue instantiating DBFunctions",Logs.ERROR,"DL100.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			
			
		}
		
		
		
	}
}
