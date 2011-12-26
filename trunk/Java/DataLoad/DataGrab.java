/*CAREFUL with using a final data extraction pattern that matches the last searched table,row,cell,div pattern. */

//package com.roeschter.jsl; 

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.Charset;
import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.protocol.HTTP;
import java.util.Arrays;
import org.apache.log4j.NDC;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

public class DataGrab extends Thread {

	String returned_content;
	String strCurDataSet;
	String strCurTask = "";
	int nCurTask;
	int nCount;
	String strScheduleId;
	String strRepeatTypeId;
	String strStage1URL;
	String strStage2URL;
	ArrayList<String> jobsArray;

	/* Need to make this a constant */
	// public static int nMaxJobs=10;

	int nTaskBatch;

	/* nJobBatch is not currenlty implemented */
	int nJobBatch;

	UtilityFunctions uf;
	ProcessingFunctions pf;
	boolean bContinue;
	Calendar calJobProcessingStart = null;
	Calendar calJobProcessingEnd = null;
	Calendar calAlertProcessingStart = null;
	Calendar calAlertProcessingEnd = null;
	Calendar calJobProcessingStage1Start = null;
	Calendar calJobProcessingStage1End = null;
	Calendar calJobProcessingStage2Start = null;
	Calendar calJobProcessingStage2End = null;

	/* Variable to filter tickers for quicker debugging. */
	String strStaticTickerLimit = "";

	String strOriginalTicker;
	int nCurrentEntityId;
	String strCurrentTicker;

	DBFunctions dbf;

	public DataGrab(UtilityFunctions tmpUF, DBFunctions dbfparam, String strTask/*Primary key of task but as a string.*/, int nBatchId, String strScheduleIdParam, String strRepeatTypeIdParam) {
	
	  	this.uf = tmpUF;
	  	this.dbf = dbfparam;
	  	this.pf = new ProcessingFunctions(tmpUF,this);
	  	
	  	this.nCurTask = Integer.parseInt(strTask);
	  	this.strScheduleId = strScheduleIdParam;
	  	this.strRepeatTypeId = strRepeatTypeIdParam;
	  	
	  
		//this.nTaskBatch = nBatchParam;
	  	try
	  	{
	  		/*
	  		 * nBatchId should only be set for historical data loads.
	  		 */
	  		if (nBatchId == 0)
	  			this.nTaskBatch = dbf.insertBatchesEntry(this.nCurTask);
	  		else
	  			this.nTaskBatch = nBatchId;
	
	  	
		  	String strQuery = "select jobs_tasks.job_id,tasks.name from jobs_tasks,tasks where tasks.id=jobs_tasks.task_id and jobs_tasks.task_id=" + strTask;
		  	ResultSet rs = dbf.db_run_query(strQuery);
		  	
		  	
		  	jobsArray = new ArrayList<String>();
		  	
		  	//this.strCurTask=rs.getString("tasks.name");
		  	//String tmp = "job_primary_key";
	
		  	while(rs.next())
		  	{
		  		if (strCurTask.isEmpty())
		  			this.strCurTask=rs.getString("tasks.name");
		  		jobsArray.add(rs.getInt("jobs_tasks.job_id")+"");
		   	}
	
	  	}
	  	catch (SQLException sqle)
	  	{
	  		UtilityFunctions.stdoutwriter.writeln("Problem retreiving list of jobs. Aborting task.",Logs.ERROR,"DG12.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			return;
	  	}
		  	
	  	
	  	
	  	
	  	//strCurDataSet = strDataSet;
	  	strCurrentTicker = "";
	  	
	
	  	
	  	
	  	
		calJobProcessingStart = Calendar.getInstance();

	
  }

	public Calendar getJobProcessingStartTime() throws CustomGenericException {
		if (calJobProcessingStart != null)
			return (calJobProcessingStart);
		else
			throw new CustomGenericException();
	}

	public Calendar getJobProcessingEndTime() throws CustomGenericException {
		if (calJobProcessingEnd != null)
			return (calJobProcessingEnd);
		else
			throw new CustomGenericException();
	}

	public Calendar getAlertProcessingStartTime() throws CustomGenericException {
		if (calAlertProcessingStart != null)
			return (calAlertProcessingStart);
		else
			throw new CustomGenericException();
	}

	public Calendar getAlertProcessingEndTime() throws CustomGenericException {
		if (calAlertProcessingEnd != null)
			return (calAlertProcessingEnd);
		else
			throw new CustomGenericException();
	}
	
	public Calendar getStage1StartTime() throws CustomGenericException {
		if (calJobProcessingStage1Start != null)
			return (calJobProcessingStage1Start);
		else
			throw new CustomGenericException();
	}

	public Calendar getStage1EndTime() throws CustomGenericException {
		if (calJobProcessingStage1End != null)
			return (calJobProcessingStage1End);
		else
			throw new CustomGenericException();
	}
	
	public Calendar getStage2StartTime() throws CustomGenericException {
		if (calJobProcessingStage2Start != null)
			return (calJobProcessingStage2Start);
		else
			throw new CustomGenericException();
	}

	public Calendar getStage2EndTime() throws CustomGenericException {
		if (calJobProcessingStage2End != null)
			return (calJobProcessingStage2End);
		else
			throw new CustomGenericException();
	}

	public void startThread() {
		bContinue = true;
		this.start();
	}

	public void stopThread() {
		bContinue = false;
	}

	public boolean getWillTerminate() {
		return (!bContinue);
	}

	public void run() {

		NDC.push("[Task:" + this.nCurTask + "]");

		UtilityFunctions.stdoutwriter.writeln(
				"=========================================================",
				Logs.STATUS1, "");

		UtilityFunctions.stdoutwriter.writeln("PROCESSING TASK " + strCurTask,
				Logs.STATUS1, "DG37");

		if (bContinue == false) {
			NDC.pop();
			dbf.closeConnection();
			return;
		}

		UtilityFunctions.stdoutwriter.writeln("JOB PROCESSING START TIME: "
				+ calJobProcessingStart.getTime().toString(), Logs.STATUS1,
				"DG39");

		UtilityFunctions.stdoutwriter.writeln("INITIATING THREAD",
				Logs.STATUS1, "DG1");

		if (DataLoad.bLoadingHistoricalData == true)
			NDC.push(HistoricalDataLoad.calCurrent.getTime().toString());

		

		for (int i = 0; i < jobsArray.size(); i++) {
			grab_data_set(jobsArray.get(i));
		}

		calJobProcessingEnd = Calendar.getInstance();
		UtilityFunctions.stdoutwriter.writeln("JOB PROCESSING END TIME: "
				+ calJobProcessingEnd.getTime().toString(), Logs.STATUS1,
				"DG38.15");

		if (DataLoad.bLoadingHistoricalData == true)
			NDC.pop();

		/*
		 * Don't process alerts if loading historical data.
		 */
		if (DataLoad.bLoadingHistoricalData == false) {

			calAlertProcessingStart = Calendar.getInstance();
			UtilityFunctions.stdoutwriter.writeln(
					"ALERT PROCESSING START TIME: "
							+ calAlertProcessingStart.getTime().toString(),
					Logs.STATUS1, "DG38.25");

			Alerts al = new Alerts();
			try {
				al.checkAlerts(this);
			} catch (SQLException sqle) {
				UtilityFunctions.stdoutwriter.writeln(
						"Problem process alerts.", Logs.ERROR, "DG11.52");
				UtilityFunctions.stdoutwriter.writeln(sqle);
				// dbf.closeConnection();
				// return;

			}

			calAlertProcessingEnd = Calendar.getInstance();
			UtilityFunctions.stdoutwriter.writeln("ALERT PROCESSING END TIME: "
					+ calAlertProcessingEnd.getTime().toString(), Logs.STATUS1,
					"DG54");
		}
		NDC.pop();
		dbf.closeConnection();

	}

	int regexSeekLoop(String regex, int nCount, int nCurOffset)
			throws TagNotFoundException {
		// nCurOffset =
		// regexSeekLoop("(?i)(<TABLE[^>]*>)",returned_content,tables);
		// String strOpenTableRegex = "(?i)(<TABLE[^>]*>)";
		// String strOpenTableRegex = "START NEW";

		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(returned_content);

		for (int i = 0; i < nCount; i++) {

			if (matcher.find(nCurOffset) == false)
			// Did not find regex
			{
				/* Let whoever catches this decide what to write to the logs. */
				// UtilityFunctions.stdoutwriter.writeln("Regex search exceeded.",Logs.ERROR,"DG2");
				throw new TagNotFoundException();
			}

			nCurOffset = matcher.start() + 1;

			UtilityFunctions.stdoutwriter.writeln("regex iteration " + i
					+ ", offset: " + nCurOffset, Logs.STATUS2, "DG3");

		}
		return (nCurOffset);
	}

	String regexSnipValue(String strBeforeUniqueCode,
			String strAfterUniqueCode, int nCurOffset)
			throws CustomRegexException {

		Pattern pattern;
		String strDataValue = "";
		int nBeginOffset;
		Matcher matcher;

		if ((strBeforeUniqueCode != null)
				&& (strBeforeUniqueCode.isEmpty() != true)) {

			// String strBeforeUniqueCodeRegex = "(?i)(" + strBeforeUniqueCode +
			// ")";
			String strBeforeUniqueCodeRegex = "(" + strBeforeUniqueCode + ")";
			UtilityFunctions.stdoutwriter.writeln(strBeforeUniqueCodeRegex,
					Logs.STATUS2, "DG4");

			pattern = Pattern.compile(strBeforeUniqueCodeRegex);
			UtilityFunctions.stdoutwriter.writeln(
					"after strbeforeuniquecoderegex compile", Logs.STATUS2,
					"DG5");

			matcher = pattern.matcher(returned_content);

			UtilityFunctions.stdoutwriter.writeln(
					"Current offset before final data extraction: "
							+ nCurOffset, Logs.STATUS2, "DG6");

			matcher.find(nCurOffset);

			nBeginOffset = matcher.end();
		} else
			nBeginOffset = nCurOffset;

		UtilityFunctions.stdoutwriter.writeln("begin offset: " + nBeginOffset,
				Logs.STATUS2, "DG7");

		// String strAfterUniqueCodeRegex = "(?i)(" + strAfterUniqueCode + ")";
		String strAfterUniqueCodeRegex = "(" + strAfterUniqueCode + ")";
		pattern = Pattern.compile(strAfterUniqueCodeRegex);
		UtilityFunctions.stdoutwriter.writeln(
				"after strAfterUniqueCodeRegex compile", Logs.STATUS2, "DG8");

		matcher = pattern.matcher(returned_content);

		matcher.find(nBeginOffset);

		int nEndOffset = matcher.start();
		UtilityFunctions.stdoutwriter.writeln("end offset: " + nEndOffset,
				Logs.STATUS2, "DG9");

		if (nEndOffset <= nBeginOffset) {
			/*
			 * If we get here, skip processing this table cell but continue
			 * processing the rest of the table.
			 */
			UtilityFunctions.stdoutwriter.writeln("EndOffset is < BeginOffset",
					Logs.STATUS2, "DG10");
			throw new CustomRegexException();
		}
		strDataValue = returned_content.substring(nBeginOffset, nEndOffset);
		/*
		 * OFP - I'm leaving this test code in here in case I have to deal with extended ASCII characters again.
		 */
		/*if (strDataValue.length() <= 1) {
			try {
				
				Charset c = Charset.defaultCharset(); 
				String tmp = String.format("%x", new BigInteger("x".getBytes(c)));
				String tmp2 = returned_content.substring(nBeginOffset-1,nEndOffset+1);
				String tmp3 = returned_content.substring(nBeginOffset,nEndOffset);
				tmp = String.format("%x", new BigInteger(tmp2.getBytes(c)));
				tmp = String.format("%x", new BigInteger(tmp3.getBytes(c)));
				String output = tmp2.replaceAll("[^\\p{ASCII}]", "");
				tmp = String.format("%x", new BigInteger("".getBytes(c)));
				tmp = String.format("%x", new BigInteger(strDataValue.getBytes(c)));
				UtilityFunctions.stdoutwriter.writeln("blap",
						Logs.STATUS2, "DG10");
				
			}
			catch (Exception e) {
				UtilityFunctions.stdoutwriter.writeln(e);
			}

		}*/

		UtilityFunctions.stdoutwriter.writeln(
				"Raw Data Value: " + strDataValue, Logs.STATUS2, "DG11");
		/*
		 * } catch (IOException ioe) { Sy }
		 */

		return (strDataValue);

	}

	/*
	 * This should be now obsolete.
	 */
	/*
	 * public void clear_run_once() { try { String query =
	 * "update schedules set run_once=0"; dbf.db_update_query(query); } catch
	 * (SQLException sqle) { //OFP 9/26/2010 - Need to put in a pause mechanism
	 * for when running under the jsp pages. //DataLoad.setPause();
	 * UtilityFunctions
	 * .stdoutwriter.writeln("Problem clearing run_once flag",Logs
	 * .ERROR,"DG13"); UtilityFunctions.stdoutwriter.writeln(sqle);
	 * 
	 * } }
	 */

	public String get_value(String local_data_set)
			throws IllegalStateException, TagNotFoundException, SQLException,
			CustomRegexException {
		String strDataValue = "";

		// try
		// {

		// run sql to get info about the data_set
		// Connection con = UtilityFunctions.db_connect();

		String query = "select extract_singles.* from extract_singles,jobs where extract_singles.id=jobs.extract_key and jobs.Data_Set='"
				+ local_data_set + "'";

		// Statement stmt = con.createStatement();
		ResultSet rs = dbf.db_run_query(query);

		rs.next();
		
		if (rs.getInt("parse_post_process")!=0) {
			return(this.returned_content);
		}

		int tables, cells, rows, divs;
		/*
		 * This next line will throw an exception if you meant to do a table
		 * extraction but forgot to set the table_extraction flag in the db.
		 */
		tables = rs.getInt("Table_Count");
		cells = rs.getInt("Cell_Count");
		rows = rs.getInt("Row_Count");
		divs = rs.getInt("Div_Count");

		int nCurOffset = 0;

		/*
		 * Initial regex search.
		 */

		String strInitialOpenUniqueCode = rs
				.getString("Initial_Bef_Unique_Code");
		Pattern pattern;
		Matcher matcher;
		if ((strInitialOpenUniqueCode != null)
				&& (strInitialOpenUniqueCode.isEmpty() == false)) {
			String strInitialOpenUniqueRegex = "(?i)("
					+ strInitialOpenUniqueCode + ")";

			UtilityFunctions.stdoutwriter.writeln("Initial Open Regex: "
					+ strInitialOpenUniqueRegex, Logs.STATUS2, "DG14");

			pattern = Pattern.compile(strInitialOpenUniqueRegex);

			matcher = pattern.matcher(returned_content);

			matcher.find();

			// nCurOffset = matcher.end();
			nCurOffset = matcher.start();

			UtilityFunctions.stdoutwriter.writeln(
					"Offset after initial regex search: " + nCurOffset,
					Logs.STATUS2, "DG15");

		}

		/*
		 * End initial regex search.
		 */

		UtilityFunctions.stdoutwriter.writeln("Before table searches.",
				Logs.STATUS2, "DG16");
		nCurOffset = regexSeekLoop("(?i)(<TABLE[^>]*>)", tables, nCurOffset);

		UtilityFunctions.stdoutwriter.writeln("Before table row searches.",
				Logs.STATUS2, "DG17");
		nCurOffset = regexSeekLoop("(?i)(<tr[^>]*>)", rows, nCurOffset);

		UtilityFunctions.stdoutwriter.writeln("Before table cell searches.",
				Logs.STATUS2, "DG18");
		nCurOffset = regexSeekLoop("(?i)(<td[^>]*>)", cells, nCurOffset);

		UtilityFunctions.stdoutwriter.writeln("Before div searches",
				Logs.STATUS2, "DG19");
		nCurOffset = regexSeekLoop("(?i)(<div[^>]*>)", divs, nCurOffset);

		String strBeforeUniqueCode = rs.getString("Before_Unique_Code");
		String strAfterUniqueCode = rs.getString("After_Unique_Code");
		/*
		 * String strBeforeUniqueCodeRegex = "(?i)(" + strBeforeUniqueCode +
		 * ")";
		 * 
		 * String strAfterUniqueCodeRegex = "(?i)(" + strAfterUniqueCode + ")";
		 */

		strDataValue = regexSnipValue(strBeforeUniqueCode, strAfterUniqueCode,
				nCurOffset);

		/*
		 * OFP 3/28/2011 - Don't remove commas if this is csv format. Let the
		 * postProcessing function handle the commas.
		 */
		if (rs.getBoolean("is_csv_format") == false)
			strDataValue = strDataValue.replace(",", "");

		strDataValue = strDataValue.replace("&nbsp;", "");

		if (strDataValue.compareTo("") != 0) {
			UtilityFunctions.stdoutwriter.writeln(
					"checking for negative data value", Logs.STATUS2, "DG20");
			UtilityFunctions.stdoutwriter.writeln(strDataValue.substring(0, 1),
					Logs.STATUS2, "DG21");
			if (strDataValue.substring(0, 1).compareTo("(") == 0) {

				strDataValue = strDataValue.replace("(", "");
				strDataValue = "-" + strDataValue.replace(")", "");
			}

		}

		UtilityFunctions.stdoutwriter.writeln("Data Value: " + strDataValue,
				Logs.STATUS2, "DG24");

	
		return (strDataValue);

	}

	public ArrayList<String[]> get_table_with_headers(String strTableSet)
			throws SQLException, TagNotFoundException,
			PrematureEndOfDataException {
		String query = "select extract_key_colhead,extract_key_rowhead,multiple_tables from jobs where data_set='"
				+ strTableSet + "'";
		ResultSet rs = dbf.db_run_query(query);
		rs.next();

		ArrayList<String[]> tabledatabody = get_table(strTableSet, "body");

		if (!((rs.getString("extract_key_colhead") == null) || rs.getString(
				"extract_key_colhead").isEmpty())) {
			ArrayList<String[]> tabledatacol = get_table(strTableSet, "colhead");

			tabledatabody.add(0, tabledatacol.get(0));
		} else
			tabledatabody.add(0, null);

		// strTmpTableSet = strTableSet.replace("_body","_rowhead");

		if (!((rs.getString("extract_key_rowhead") == null) || rs.getString(
				"extract_key_rowhead").isEmpty())) {
			ArrayList<String[]> tabledatarow = get_table(strTableSet, "rowhead");

			String[] temp = new String[tabledatarow.size()];
			for (int i = 0; i < tabledatarow.size(); i++) {
				temp[i] = tabledatarow.get(i)[0];

			}

			tabledatabody.add(1, temp);
		}

		return (tabledatabody);

	}

	public void defaultURLProcessing(String strDataSet) throws SQLException,
			MalformedURLException, IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {

		String query;

		query = "select * from jobs where data_set='" + strDataSet + "'";

		ResultSet rs2 = dbf.db_run_query(query);
		rs2.next();
		calJobProcessingStage1Start = Calendar.getInstance();
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;

		if ((rs2.getInt("input_source") != 0)) {
			query = "select * from input_source where id="
					+ rs2.getInt("input_source");
			ResultSet rs3 = dbf.db_run_query(query);
			rs3.next();
			String strStProperties = rs3.getString("form_static_properties");
			String[] listItems = strStProperties.split(":");
			String[] listItems2;
			String data = "";
			for (int i = 0; i < listItems.length; i++) {
				listItems2 = listItems[i].split("=");
				if (i != 0) {
					data += "&";
				}

				// check to see if there is an equal sign or a value on the
				// right side of the equality
				if (listItems2.length == 1)
					data += URLEncoder.encode(listItems2[0], "UTF-8") + "=";
				else
					data += URLEncoder.encode(listItems2[0], "UTF-8") + "="
							+ URLEncoder.encode(listItems2[1], "UTF-8");

			}

			UtilityFunctions.stdoutwriter.writeln(
					"Retrieving URL Form: " + rs3.getString("url_form"),
					Logs.STATUS2, "DG24.5");

			data = "series_id=LNS14000000&survey=ln&format=&html_tables=&delimiter=&catalog=&print_line_length=&lines_per_page=&row_stub_key=&year=&date=&net_change_start=&net_change_end=&percent_change_start=&percent_change_end=";
			HttpPost httppost = new HttpPost(
					"http://data.bls.gov/cgi-bin/surveymost");

			/*
			 * Emulate a browser.
			 */

			httppost.getParams().setParameter("http.protocol.user-agent",
					"Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");

			/*
			 * The following line fixes an issue where a non-fatal error is
			 * displayed about an invalid cookie data format.
			 */
			httppost.getParams().setParameter(
					"http.protocol.cookie-datepatterns",
					Arrays.asList("EEE, dd MMM-yyyy-HH:mm:ss z",
							"EEE, dd MMM yyyy HH:mm:ss z"));

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("series_id", "LNS14000000"));
			nvps.add(new BasicNameValuePair("survey", "ln"));

			httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			response = httpclient.execute(httppost);

		} else {

			this.strStage2URL = this.strStage1URL.replace("${ticker}", this.strCurrentTicker);

			UtilityFunctions.stdoutwriter.writeln("Retrieving URL: " + this.strStage2URL,
					Logs.STATUS2, "DG25");

			if (!this.strStage2URL.contains(":"))
				UtilityFunctions.stdoutwriter.writeln(
						"WARNING: url is not preceeded with a protocol"
								+ this.strStage2URL, Logs.STATUS1, "DG25.5");

			// HttpGet chokes on the ^ character

			this.strStage2URL = this.strStage2URL.replace("^", "%5E");
			this.strStage2URL = this.strStage2URL.replace("|", "%7C");
			// }

			HttpGet httpget = new HttpGet(this.strStage2URL);

			/*
			 * Emulate a browser.
			 */

			httpget.getParams().setParameter("http.protocol.user-agent",
					"Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");
			/*
			 * The following line fixes an issue where a non-fatal error is
			 * displayed about an invalid cookie data format. It turns out that
			 * some sites generate a warning with this code, and others without
			 * it. I'm going to kludge this for now until I get more data on
			 * which urls throw the warning and which don't.
			 * 
			 * warning with code: www.exchange-rates.org
			 */

			if (!(strCurDataSet.contains("xrateorg")
					|| strCurDataSet.contains("google") || strCurDataSet
					.contains("mwatch"))) {
				httpget.getParams().setParameter(
						"http.protocol.cookie-datepatterns",
						Arrays.asList("EEE, dd MMM-yyyy-HH:mm:ss z",
								"EEE, dd MMM yyyy HH:mm:ss z"));
			}

			/*
			 * OFP 3/12/2011 - Need to keep an eye on this next line. There was
			 * an issue where because of a bug in DataLoad initiateJob() the
			 * same task showed up in the running queue and there was a thread
			 * lock on this line. Not exactly sure if this method is thread
			 * safe. I wouldn't think that it's thread-safedness (?) would
			 * depend on if you are issuing the same url, i.e. I would think
			 * that thread locks would occur even with different urls.
			 */

			/*
			 * OFP 3/15/2011 - Had another thread lock with one thread on this
			 * line: response = httpclient.execute(httpget); and another thread
			 * on this line: while ((nTmp = in.read()) != -1)
			 * 
			 * Added the shutdown() call.
			 */

			response = httpclient.execute(httpget);

		}

		HttpEntity entity = response.getEntity();
		calJobProcessingStage1End = Calendar.getInstance();
		calJobProcessingStage2Start = Calendar.getInstance();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				entity.getContent()));

		int nTmp;

		returned_content = "";
		this.nCount = 0;
		Calendar cal = Calendar.getInstance();

		/*
		 * OFP 9/15/2011 - Added workaround for connection reset exception for
		 * bloomberg index tasks.
		 */
		
		boolean bDone = false;
		while (!bDone) {
			try {

				while ((nTmp = in.read()) != -1) {
					this.nCount++;
					returned_content += (char) nTmp;
					if (nCount == 10000 && nCurTask == 6) {
						Calendar cal2 = Calendar.getInstance();
						System.out.println("b");
					}
				}
				bDone = true;

			} catch (SocketException se) {

				if (!(this.nCurTask == 6 || this.nCurTask == 28)) {
					UtilityFunctions.stdoutwriter.writeln(
							"Issue with URL connection.", Logs.ERROR, "DG42.1");
					UtilityFunctions.stdoutwriter.writeln(se);
					bDone = true;
				} else {
					UtilityFunctions.stdoutwriter.writeln(
							"Issue with URL connection.", Logs.WARN, "DG42.3");
					UtilityFunctions.stdoutwriter.writeln(se.getMessage(),
							Logs.WARN, "DG42.4");
					UtilityFunctions.stdoutwriter
							.writeln(
									"Processing one of the bloomberg tasks, resubmitting URL",
									Logs.WARN, "DG42.2");
				}

			}
		}

		in.close();
		calJobProcessingStage2End = Calendar.getInstance();

		httpclient.getConnectionManager().shutdown();

		UtilityFunctions.stdoutwriter.writeln("Done reading url contents",
				Logs.STATUS2, "DG26");

	}

	public void get_url(String strDataSet) throws SQLException,
			MalformedURLException, IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		String query;

		query = "select * from jobs where data_set='" + strDataSet + "'";

		ResultSet rs2 = dbf.db_run_query(query);
		rs2.next();
		String strCustomURLFuncName = rs2.getString("custom_url_func_name");
		if (strCustomURLFuncName == null) {
			defaultURLProcessing(strDataSet);
		} else {

			Class[] args1 = new Class[1];
			args1[0] = String.class;

			Method m = this.getClass().getMethod(strCustomURLFuncName, args1);
			m.invoke(this, strDataSet);
		}

	}

	public ArrayList<String[]> get_table(String strTableSet, String strSection)
			throws SQLException, TagNotFoundException,
			PrematureEndOfDataException {
		// retrieve the table data_set
		ArrayList<String[]> tabledata = new ArrayList<String[]>();
		// try
		// {
		String query;
		if (strSection.equals("body")) {
			query = "select extract_tables.* from extract_tables,jobs where extract_tables.id=jobs.extract_key and jobs.data_set='"
					+ strTableSet + "'";
		} else if (strSection.equals("colhead")) {
			query = "select extract_tables.* from extract_tables,jobs where extract_tables.id=jobs.extract_key_colhead and jobs.data_set='"
					+ strTableSet + "'";
		} else if (strSection.equals("rowhead")) {
			query = "select extract_tables.* from extract_tables,jobs where extract_tables.id=jobs.extract_key_rowhead and jobs.data_set='"
					+ strTableSet + "'";
		} else {
			UtilityFunctions.stdoutwriter.writeln(
					"Table type not found. Exiting.", Logs.ERROR, "DG26.5");
			/*
			 * Lazy here, should create a new exception type but just reusing
			 * one that this function already throws.
			 */
			throw new TagNotFoundException();
		}

		ResultSet rs = dbf.db_run_query(query);
		rs.next();
		/*
		 * if nFixedDataRows is null or zero, then we are grabbing an
		 * indeterminate number of rows.
		 */
		int nFixedDataRows = rs.getInt("rowsofdata");
		int nRowInterval = rs.getInt("rowinterval");
		String strEndDataMarker = rs.getString("end_data_marker");
		boolean bColTHtags = true;
		if (rs.getInt("column_th_tags") != 1)
			bColTHtags = false;

		// seek to the top corner of the table

		String strTableCount = rs.getString("table_count");
		int nBeginTable, nEndTable;

		if (strTableCount.contains(",")) {
			nBeginTable = Integer.parseInt(strTableCount.substring(0,
					strTableCount.indexOf(",")));
			nEndTable = Integer.parseInt(strTableCount.substring(
					strTableCount.indexOf(",") + 1, strTableCount.length()));
		} else {
			nBeginTable = nEndTable = Integer.parseInt(strTableCount);
		}

		for (int nCurTable = nBeginTable; nCurTable <= nEndTable; nCurTable++)

		{

			int nCurOffset = 0;

			UtilityFunctions.stdoutwriter.writeln("Searching table count: "
					+ nCurTable, Logs.STATUS2, "DG27");

			nCurOffset = regexSeekLoop("(?i)(<TABLE[^>]*>)", nCurTable,
					nCurOffset);

			nCurOffset = regexSeekLoop("(?i)(<tr[^>]*>)",
					rs.getInt("top_corner_row"), nCurOffset);

			int nEndTableOffset = regexSeekLoop("(?i)(</TABLE>)", 1, nCurOffset);

			// iterate over rows, iterate over columns, writing values out to a
			// csv file
			boolean done = false;
			int nNumOfColumns = rs.getInt("number_of_columns");

			String[] rowdata;
			int nRowCount = 0;

			String strBeforeUniqueCodeRegex, strAfterUniqueCodeRegex, strDataValue;

			while (!done) {
				UtilityFunctions.stdoutwriter.writeln("row: " + nRowCount,
						Logs.STATUS2, "DG28");
				rowdata = new String[nNumOfColumns];

				for (int i = 0; i < nNumOfColumns; i++) {

					UtilityFunctions.stdoutwriter.writeln("Column: " + i,
							Logs.STATUS2, "DG29");

					if (bColTHtags == false)
						nCurOffset = regexSeekLoop("(?i)(<td[^>]*>)",
								rs.getInt("Column" + (i + 1)), nCurOffset);
					else
						nCurOffset = regexSeekLoop("(?i)(<th[^>]*>)",
								rs.getInt("Column" + (i + 1)), nCurOffset);

					// String strBeforeUniqueCode = rs.getString("bef_code_col"
					// + (i+1));
					strBeforeUniqueCodeRegex = "(?i)("
							+ rs.getString("bef_code_col" + (i + 1)) + ")";
					// String strAfterUniqueCode = rs.getString("aft_code_col" +
					// (i+1));
					strAfterUniqueCodeRegex = "(?i)("
							+ rs.getString("aft_code_col" + (i + 1)) + ")";
					try {
						strDataValue = regexSnipValue(strBeforeUniqueCodeRegex,
								strAfterUniqueCodeRegex, nCurOffset);

						/*
						 * Going to strip out &nbsp; for all data streams, let's
						 * see if this is a problem.
						 */
						/* See below for exit conditions. */
						if (strEndDataMarker != null
								&& (strEndDataMarker.length() > 0))
							if (strDataValue.equals(strEndDataMarker)) {
								/*
								 * Finished grabbing data for this table.
								 */
								nCurOffset = nEndTableOffset; // this breaks us
																// out of the
																// while loop
								break; // this breaks us out of the for loop
							}

						strDataValue = strDataValue.replace("\r", "");
						strDataValue = strDataValue.replace("\n", "");
						strDataValue = strDataValue.trim();
						rowdata[i] = strDataValue.replace("&nbsp;", "");
					} catch (CustomRegexException cre) {
						UtilityFunctions.stdoutwriter
								.writeln(
										"Empty cell in table in url stream. Voiding cell.",
										Logs.STATUS2, "DG30");
						rowdata[i] = "void";
					}

				}

				tabledata.add(rowdata);

				nRowCount++;

				/*
				 * First part of if clause is for data sets with predefined
				 * fixed number of rows.
				 */
				if (nFixedDataRows > 0) {
					if (nFixedDataRows == nRowCount)
						break;

					try {
						nCurOffset = regexSeekLoop("(?i)(<tr[^>]*>)",
								nRowInterval, nCurOffset);
					} catch (TagNotFoundException tnfe) {
						throw new PrematureEndOfDataException();
					}

					if (nEndTableOffset < nCurOffset)
						throw new PrematureEndOfDataException();

				}
				/*
				 * else part is for data sets of an indeterminate size.
				 */
				else {
					try {
						nCurOffset = regexSeekLoop("(?i)(<tr[^>]*>)",
								nRowInterval, nCurOffset);
					} catch (TagNotFoundException tnfe) {
						// No more TR tags in the file - we'll assume it's the
						// end of the table
						break;
					}

					// we're past the end table tag - done collecting table.
					if (nEndTableOffset < nCurOffset)
						break;
				}

			}

		}

		return (tabledata);
	}

	public void grab_data_set(String strJobPrimaryKey) {

		try {

			String query = "select entity_groups.id,tasks.metric_id,tasks.use_group_for_reading,tasks.delay from entity_groups,entity_groups_tasks,tasks where entity_groups.id=entity_groups_tasks.entity_group_id and entity_groups_tasks.task_id=tasks.id and entity_groups_tasks.task_id="
					+ nCurTask;
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			int nMetricId = rs.getInt("tasks.metric_id");
			int nGroupId = rs.getInt("entity_groups.id");
			int nDelay = rs.getInt("tasks.delay");

			boolean bUseGroupForReading = rs
					.getBoolean("tasks.use_group_for_reading");

			boolean bTableExtraction = true;

			query = "select * from jobs where id='" + strJobPrimaryKey + "'";
			rs = dbf.db_run_query(query);
			rs.next();
			
			this.strStage1URL = rs.getString("url_static");

			strCurDataSet = rs.getString("data_set");

			UtilityFunctions.stdoutwriter.writeln("====> PROCESSING JOB:"
					+ strCurDataSet, Logs.STATUS1, "DG1.55");

			if (rs.getInt("table_extraction") == 0)
				bTableExtraction = false;

			pf.preJobProcessing(strCurDataSet);

			if (bUseGroupForReading == false) {
				// this extract process is not associated with a group of
				// companies.
				// if ((strCurDataSet.substring(0,5)).compareTo("table") == 0)
				if (bTableExtraction == true) {
					try {

						pf.preProcessing(strCurDataSet, "");

						get_url(strCurDataSet);

						ArrayList<String[]> tabledata = get_table_with_headers(strCurDataSet);
						// pf.processTableSAndPCoList(tabledata,strCurDataSet,uf);
						// need to add the quarter values somewhere around here.

						// ArrayList<String[]> tabledata2 =
						// pf.postProcessingTable(tabledata, strCurDataSet);
						ArrayList<String[]> tabledata2 = pf.postProcessing(
								tabledata, strCurDataSet);

						ResultSet rs2 = dbf
								.db_run_query("select custom_insert from jobs where data_set='"
										+ strCurDataSet + "'");
						rs2.next();

						/*
						 * Insert directly into fact_data now.
						 */
						if (rs2.getInt("custom_insert") != 1)
							dbf.importTableIntoDB(tabledata2, "fact_data",
									this.nTaskBatch, this.nCurTask, nMetricId);

					} catch (MalformedURLException mue) {
						UtilityFunctions.stdoutwriter.writeln(
								"Badly formed url, processing dataset aborted",
								Logs.ERROR, "DG38.1");
						UtilityFunctions.stdoutwriter.writeln(mue);
					} catch (SQLException sqle) {
						UtilityFunctions.stdoutwriter
								.writeln(
										"Problem issuing sql statement, processing dataset aborted",
										Logs.ERROR, "DG38.2");
						UtilityFunctions.stdoutwriter.writeln(sqle);
					} catch (IOException ioe) {
						UtilityFunctions.stdoutwriter.writeln(
								"Problem with I/O, processing dataset aborted",
								Logs.ERROR, "DG38.3");
						UtilityFunctions.stdoutwriter.writeln(ioe);
					} catch (PrematureEndOfDataException peode) {
						UtilityFunctions.stdoutwriter
								.writeln(
										"A fixed number of rows were defined for the data set but the end of the table or the end of the document was reached first.",
										Logs.ERROR, "DG38.35");
						UtilityFunctions.stdoutwriter.writeln(peode);
					} catch (Exception e) {
						UtilityFunctions.stdoutwriter.writeln(
								"Processing dataset aborted", Logs.ERROR,
								"DG38.4");
						UtilityFunctions.stdoutwriter.writeln(e);
					}

				}
				// Non-group, non-table extraction
				else {
					if (pf.preProcessing(strCurDataSet, strCurrentTicker) != true) {
						throw new CustomEmptyStringException();
					}

					get_url(strCurDataSet);

					if (pf.preNoDataCheck(strCurDataSet) == true) {
						UtilityFunctions.stdoutwriter.writeln(
								"URL contains no data, skipping ticker",
								Logs.STATUS1, "DG46");
						return;
					}

					String strDataValue = get_value(strCurDataSet);

					if (strDataValue.compareTo("") == 0) {
						UtilityFunctions.stdoutwriter.writeln(
								"Returned empty value '', skipping ",
								Logs.STATUS2, "DG47");
						return;
					}

					ArrayList<String[]> tabledata = new ArrayList<String[]>();

					String[] tmp = { strDataValue };

					/*
					 * Create a table even for single values since we now use
					 * importTableIntoDB for everything.
					 */
					tabledata.add(tmp);

					ArrayList<String[]> tabledata2 = pf.postProcessing(
							tabledata, strCurDataSet);

					ResultSet rs2 = dbf
							.db_run_query("select custom_insert from jobs where data_set='"
									+ strCurDataSet + "'");
					rs2.next();
					if (rs2.getInt("custom_insert") != 1) {
						/*
						 * Import directly into fact_data now.
						 */
						dbf.importTableIntoDB(tabledata2, "fact_data",
								this.nTaskBatch, this.nCurTask, nMetricId);
						/*
						 * if (this.strCurDataSet.equals("exchrate_yen_dollar"))
						 * dbf
						 * .importTableIntoDB(tabledata2,"fact_data",this.nBatch
						 * ); else
						 * dbf.importTableIntoDB(tabledata2,"fact_data_stage"
						 * ,this.nBatch);
						 */

					}

				}

			} else {

				// query = "select * from entities where groups like '%" + group
				// + "%' order by ticker";
				query = "select entities.* from entities,entities_entity_groups ";
				query += " where entities_entity_groups.entity_group_id="
						+ nGroupId;
				query += " AND entities_entity_groups.entity_id=entities.id ";

				rs = dbf.db_run_query(query);

				// String strCurTicker="";
				// String fullUrl;
				String strDataValue = "";
				int count = 0;

				/* Loop through the list of group items, e.g. stock tickers */
				while (rs.next()) {
					try {
						/*
						 * Have to maintain original ticker and current ticker
						 * since some websites use a different format for
						 * certain tickers, i.e BRK/A vs BRK-A.
						 * "Original Ticker" is the format that is stored in the
						 * entity table. "Current Ticker" gets modified to the
						 * format that the website uses.
						 */
						this.strOriginalTicker = this.strCurrentTicker = rs
								.getString("ticker");
						this.nCurrentEntityId = rs.getInt("id");

						UtilityFunctions.stdoutwriter.writeln(
								"Processing ticker: " + this.strCurrentTicker,
								Logs.STATUS2, "DG39.1");

						/* Active only to debug individual tickers */
						if (strStaticTickerLimit.isEmpty() != true)
							if (strCurrentTicker
									.compareTo(strStaticTickerLimit) != 0)
								continue;
							else {
								int o = 0;
								o++;
							}

						/*
						 * Group table extraction.
						 */
						if (bTableExtraction == true) {
							try {

								/*
								 * query =
								 * "update extract_tables set url_dynamic='" +
								 * strCurrentTicker + "' where Data_Set='" +
								 * strCurDataSet + "'";
								 * dbf.db_update_query(query);
								 */

								// pf.preProcessingTable(strCurDataSet,strCurrentTicker);
								pf.preProcessing(strCurDataSet,
										strCurrentTicker);

								/*
								 * query = "update jobs set url_dynamic='" +
								 * strCurrentTicker + "' where Data_Set='" +
								 * strCurDataSet + "'";
								 * dbf.db_update_query(query);
								 */

								get_url(strCurDataSet);

								/* perform no data check here */
								/*
								 * Should change this to throw a no data
								 * exception
								 */
								if (pf.preNoDataCheck(strCurDataSet) == true) {
									UtilityFunctions.stdoutwriter
											.writeln(
													"URL contains no data, skipping ticker",
													Logs.STATUS1, "DG40");
									continue;
								}

								ArrayList<String[]> tabledata = get_table_with_headers(strCurDataSet);

								// ArrayList<String[]> tabledata2 =
								// pf.postProcessingTable(tabledata,
								// strCurDataSet);
								ArrayList<String[]> tabledata2 = pf
										.postProcessing(tabledata,
												strCurDataSet);

								ResultSet rs2 = dbf
										.db_run_query("select custom_insert from jobs where data_set='"
												+ strCurDataSet + "'");
								rs2.next();
								if (rs2.getInt("custom_insert") != 1)
									dbf.importTableIntoDB(tabledata2,
											"fact_data", this.nTaskBatch,
											this.nCurTask, nMetricId);

								// UtilityFunctions.createCSV(tabledata,"fact_data_stage.csv",(count==0?false:true));

								// UtilityFunctions.loadCSV("fact_data_stage.csv");

								/*
								 * String[] rowdata; for (int
								 * x=0;x<tabledata.size();x++) { rowdata =
								 * tabledata.get(x); for (int
								 * y=0;y<rowdata.length;y++) {
								 * UtilityFunctions.stdoutwriter
								 * .writeln(rowdata[y]+"     ",Logs.STATUS2);
								 * //System.out.print(rowdata[y]+"     "); }
								 * 
								 * }
								 */
							} catch (SkipLoadException sle) {
								// This is not an error but is thrown by the
								// processing function to indicate that the load
								// method shouldn't be called
								// May want to convert over the custom insert
								// processing functions to use this exception
								UtilityFunctions.stdoutwriter
										.writeln(
												"SkipLoadException thrown, skipping load",
												Logs.STATUS1, "DG40.5");
							} catch (MalformedURLException mue) {
								UtilityFunctions.stdoutwriter.writeln(
										"Badly formed url, skipping ticker",
										Logs.ERROR, "DG41");
								UtilityFunctions.stdoutwriter.writeln(mue);
							}
							catch (CustomEmptyStringException cese) {
								/*
								 * Value returned from source is empty. Only display a warning.
								 */
								UtilityFunctions.stdoutwriter.writeln(
										cese.getMessage(),
										Logs.WARN, "DG41.38");
							} catch (SQLException sqle) {
								UtilityFunctions.stdoutwriter
										.writeln(
												"Problem issuing sql statement, skipping ticker",
												Logs.ERROR, "DG42");
								UtilityFunctions.stdoutwriter.writeln(sqle);
							} catch (IOException ioe) {
								UtilityFunctions.stdoutwriter.writeln(
										"Problem with I/O, skipping ticker",
										Logs.ERROR, "DG43");
								UtilityFunctions.stdoutwriter.writeln(ioe);
							} catch (Exception e) {
								UtilityFunctions.stdoutwriter.writeln(
										"Processing table for ticker "
												+ strCurrentTicker
												+ " failed, skipping",
										Logs.ERROR, "DG44");
								UtilityFunctions.stdoutwriter.writeln(e);
							}

						} else
						/*
						 * Group, non-table extraction.
						 */
						{

							try {

								if (pf.preProcessing(strCurDataSet,
										strCurrentTicker) != true) {
									throw new CustomEmptyStringException();
								}

								get_url(strCurDataSet);

								if (pf.preNoDataCheck(strCurDataSet) == true) {
									UtilityFunctions.stdoutwriter
											.writeln(
													"URL contains no data, skipping ticker",
													Logs.STATUS1, "DG46");
									continue;
								}

								strDataValue = get_value(strCurDataSet);

								if (strDataValue.compareTo("") == 0) {
									UtilityFunctions.stdoutwriter
											.writeln(
													"Returned empty value '', skipping ",
													Logs.STATUS2, "DG47");
									continue;
								}

								ArrayList<String[]> tabledata = new ArrayList<String[]>();

								String[] tmp = { strDataValue };

								/*
								 * Create a table even for single values since
								 * we now use importTableIntoDB for everything.
								 */
								tabledata.add(tmp);

								ArrayList<String[]> tabledata2 = pf
										.postProcessing(tabledata,
												strCurDataSet);

								ResultSet rs2 = dbf
										.db_run_query("select custom_insert from jobs where data_set='"
												+ strCurDataSet + "'");
								rs2.next();
								if (rs2.getInt("custom_insert") != 1)
									dbf.importTableIntoDB(tabledata2,
											"fact_data", this.nTaskBatch,
											this.nCurTask, nMetricId);

							} catch (MalformedURLException mue) {
								UtilityFunctions.stdoutwriter.writeln(
										"Badly formed url, skipping ticker",
										Logs.ERROR, "DG48");
								UtilityFunctions.stdoutwriter.writeln(mue);
							} 
							catch (CustomEmptyStringException cese) {
								/*
								 * Value returned from source is empty. Only display a warning.
								 */
								UtilityFunctions.stdoutwriter.writeln(
										cese.getMessage(),
										Logs.WARN, "DG41.38");
							}
							catch (SQLException sqle) {
								UtilityFunctions.stdoutwriter
										.writeln(
												"Problem issuing sql statement, skipping ticker",
												Logs.ERROR, "DG49");
								UtilityFunctions.stdoutwriter.writeln(sqle);
							} catch (IOException ioe) {
								UtilityFunctions.stdoutwriter.writeln(
										"Problem with I/O, skipping ticker",
										Logs.ERROR, "DG50");
								UtilityFunctions.stdoutwriter.writeln(ioe);
							} catch (Exception e) {
								UtilityFunctions.stdoutwriter.writeln(
										"Processing table for ticker "
												+ strCurrentTicker
												+ " failed, skipping",
										Logs.ERROR, "DG51");
								UtilityFunctions.stdoutwriter.writeln(e);
							}

							// Some of this can be sped up by not running these
							// read queries on every ticker iteration.

							// if the insert is not handled here it should have
							// already been handled in the postProcessing
							// function.

							/*
							 * if (rs2.getBoolean("custom_insert")!=true) {
							 * query =
							 * "INSERT INTO fact_data_stage (data_set,value,adj_quarter,ticker,date_collected,batch) VALUES ('"
							 * + strCurDataSet + "','" + strDataValue + "','" +
							 * Integer.toString(nAdjQuarter) + "','" +
							 * strCurTicker + "',NOW()," +
							 * Integer.toString(uf.getBatchNumber()) + ")";
							 * uf.db_update_query(query);
							 * 
							 * }
							 */

							// uf.db_update_query(query);
						}

					} catch (SQLException sqle) {
						UtilityFunctions.stdoutwriter.writeln(
								"problem with sql statement in grab_data_set",
								Logs.ERROR, "DG52");
						UtilityFunctions.stdoutwriter.writeln(
								"Processing of data_set " + strCurDataSet
										+ " with ticker " + strCurrentTicker
										+ " FAILED ", Logs.ERROR, "DG53");
						UtilityFunctions.stdoutwriter.writeln(sqle);
					}

					count++;
					/*
					 * This delay is being used as a pause between each group
					 * member of a group extraction. Can't remember why this was
					 * put it, but it's not being used currently (all values are
					 * set to 0 in the db).
					 */
					Thread.sleep(nDelay);
				}
			}

			pf.postJobProcessing(strCurDataSet);
			// calEnd = Calendar.getInstance();
			UtilityFunctions.stdoutwriter.writeln("====> FINISHED JOB:"
					+ strCurDataSet, Logs.STATUS1, "DG1.55");
			// UtilityFunctions.stdoutwriter.writeln("DATA SET END TIME: " +
			// calEnd.getTime().toString(),Logs.STATUS1,"DG54");
			// }

		} 
		catch (InvocationTargetException ite) {
			UtilityFunctions.stdoutwriter.writeln("Exception in grab_data_set: " + ite.getTargetException().getMessage(),	Logs.ERROR, "DG56.83");
			//UtilityFunctions.stdoutwriter.writeln(ite.getTargetException());
			
		}
		
		catch (Exception e) {
			UtilityFunctions.stdoutwriter.writeln("Exception in grab_data_set",
					Logs.ERROR, "DG55");
			UtilityFunctions.stdoutwriter.writeln(e);
		}

	}

	public void customYahooBulkURL(String strDataSet) throws SQLException,
			IOException {

		// Get a list of all the tickers

		int nGroupSize = 100;
		
		calJobProcessingStage1Start = Calendar.getInstance();
		calJobProcessingStage1End = Calendar.getInstance();
		calJobProcessingStage2Start = Calendar.getInstance();
		calJobProcessingStage2End = Calendar.getInstance();

		String query2 = "select entity_groups.id,tasks.metric_id,tasks.use_group_for_reading from entity_groups,entity_groups_tasks,tasks where entity_groups.id=entity_groups_tasks.entity_group_id and entity_groups_tasks.task_id=tasks.id and entity_groups_tasks.task_id="
				+ nCurTask;
		ResultSet rs = dbf.db_run_query(query2);
		rs.next();
		int nGroupId = rs.getInt("entity_groups.id");

		String query = "select count(entities.id) as cnt from entities,entities_entity_groups ";
		query += " where entities_entity_groups.entity_group_id=" + nGroupId;
		query += " AND entities_entity_groups.entity_id=entities.id ";
		rs = dbf.db_run_query(query);
		rs.next();

		int nTotalCount = rs.getInt("cnt");

		query = "select * from jobs where data_set='" + strDataSet + "'";

		ResultSet rs2 = dbf.db_run_query(query);
		rs2.next();

		String strURLStatic = rs2.getString("url_static");

		int nGroupCount = 1;
		int nCurrentCount = 0;

		HttpResponse response;

		returned_content = "<begintag>";

		boolean bDone = false;

		while (!bDone) {

			HttpClient httpclient = new DefaultHttpClient();

			query = "select entities.* from entities,entities_entity_groups ";
			query += " where entities_entity_groups.entity_group_id="
					+ nGroupId;
			query += " AND entities_entity_groups.entity_id=entities.id ";
			query += " order by entities.id ";
			query += " limit " + nGroupSize + " offset "
					+ (nGroupSize * (nGroupCount - 1));
			rs = dbf.db_run_query(query);

			String strList = "";
			while (rs.next()) {

				String strTicker = rs.getString("ticker");

				if (strTicker.equals("BF/B")) {
					// dg.strCurrentTicker = "BF-B";
					strTicker = "BF-B";
				} else if (strTicker.equals("BRK/A")) {
					// dg.strCurrentTicker = "BRK-A";
					strTicker = "BRK-A";
				}

				strList += strTicker + ",";

			}

			strList = strList.substring(0, strList.length() - 1);

			String strURL = strURLStatic.replace("${dynamic}", strList);

			HttpGet httpget = null;
			try {
				/*
				 * URI uri = new URI( "http", "search.barnesandnoble.com",
				 * "/booksearch/first book.pdf", null);
				 */
				// URI uri = new URI(strURL);
				strURL = strURL.replace("^", "%5E");
				httpget = new HttpGet(strURL);
			} catch (Exception ex) {
				UtilityFunctions.stdoutwriter.writeln(ex);
			}

			httpget.getParams().setParameter(
					"http.protocol.cookie-datepatterns",
					Arrays.asList("EEE, dd MMM-yyyy-HH:mm:ss z",
							"EEE, dd MMM yyyy HH:mm:ss z"));

			httpget.getParams().setParameter("http.protocol.user-agent",
					"Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");

			response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					entity.getContent()));

			int nTmp;

			String strTemp = "";

			while ((nTmp = in.read()) != -1)
				strTemp += (char) nTmp;

			in.close();

			httpclient.getConnectionManager().shutdown();

			boolean bDone2 = false;

			int nBegin = 0;
			int nEnd = 0;

			Calendar cal = Calendar.getInstance();
			DateFormat formatter = new SimpleDateFormat("M/d/yyyy");

			boolean bResubmit = false;
			
			/*
			 * Right now only perofrm this check on task 10.
			 */
			if (this.nCurTask == 10) {

				while (bDone2 == false) {
	
					nBegin = strTemp.indexOf("<LastTradeDate>", nEnd);
	
					if (nBegin == -1)
						break;
	
					nBegin += "<LastTradeDate>".length();
	
					nEnd = strTemp.indexOf("</LastTradeDate>", nBegin);
	
					String strDate = strTemp.substring(nBegin, nEnd);
	
					nBegin = strTemp.indexOf("<LastTradeTime>", nEnd);
	
					if (nBegin == -1) {
						/*
						 * Technically we should never get here since it means a
						 * lasttradedate tag without a lasttradetime tag.
						 */
						UtilityFunctions.stdoutwriter
								.writeln(
										"Issue processing yahoo data. Lasttradedate tage without lasttradetime tag.",
										Logs.ERROR, "DG60.1");
						bResubmit = true;
						break;
					}
	
					nBegin += "<LastTradeTime>".length();
					nEnd = strTemp.indexOf("</LastTradeTime>", nBegin);
	
					String strTime = strTemp.substring(nBegin, nEnd);
	
					String strHour = strTime.substring(0, strTime.indexOf(":"));
					String strMinute = strTime.substring(strTime.indexOf(":") + 1,
							strTime.indexOf(":") + 3);
					String strPeriod = strTime.substring(strTime.length() - 2,
							strTime.length());
	
					int nHour = Integer.parseInt(strHour);
	
					if (strPeriod.equals("pm") && (nHour != 12))
						nHour += 12;
					else if (strPeriod.equals("am") && (nHour == 12))
						nHour = 0;
	
					String[] datearray = strDate.split("/");
	
					Calendar cal2 = Calendar.getInstance();
					cal2.setTimeZone(TimeZone.getTimeZone("America/New_York"));
					/*
					 * OFP 9/26/2011 I'm using the following method because with the
					 * indivdual set() methods, i.e. cal2.set(Calendar.HOUR,x);, the
					 * logic is not absolute, meaning that what the hour 12 means is
					 * dependent on what AM_PM is currently set to. Also the day can
					 * get flipped over, which is also behavior I don't want.
					 */
					cal2.set(Integer.parseInt(datearray[2]),
							Integer.parseInt(datearray[0]) - 1,
							Integer.parseInt(datearray[1]), nHour,
							Integer.parseInt(strMinute), 0);
	
					// Check if time difference is more than an hour
					if (Math.abs(cal2.getTimeInMillis() - cal.getTimeInMillis()) > 3600000) {
						UtilityFunctions.stdoutwriter.writeln(
								"Bad Yahoo Data, Resubmitting URL", Logs.STATUS1,
								"DG55.10");
						UtilityFunctions.stdoutwriter.writeln("Date Collected:"
								+ strDate + ",Time Collected:" + strTime,
								Logs.STATUS1, "DG55.11");
						bResubmit = true;
						break;
	
					}
	
				}
			}

			if (strTemp.length() < 300) {
				bResubmit = true;
				UtilityFunctions.stdoutwriter.writeln(
						"Bad Yahoo Data, Resubmitting URL", Logs.STATUS1,
						"DG55.30");
				UtilityFunctions.stdoutwriter.writeln("Data length only "
						+ strTemp.length(), Logs.STATUS1, "DG55.66");

			}

			if (bResubmit == true) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException ie) {

				}
				continue;

			}

			returned_content += strTemp;

			if ((nGroupCount * nGroupSize) >= nTotalCount)
				bDone = true;

			nGroupCount++;

		}

		returned_content += "<endtag>";

	}

}

class CustomEmptyStringException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3458604207910623258L;

	// void CustomEmptyStringException()
	// {
	// super();
	// }
}

class CustomRegexException extends Exception {

	private static final long serialVersionUID = 3833467121806555695L;

	/*
	 * void CustomRegexException() {
	 * 
	 * }
	 */
}

class TagNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3350211930488258897L;

	void TagNoutFoundException() {

	}

}

class CustomGenericException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1323548081845621968L;

}

class PrematureEndOfDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7980404235453739422L;

}
