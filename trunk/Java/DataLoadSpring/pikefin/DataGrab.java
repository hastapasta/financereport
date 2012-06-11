package pikefin;
/*CAREFUL with using a final data extraction pattern that matches the last searched table,row,cell,div pattern. */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.reflect.Method;
import java.net.*;

import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import pikefin.log4jWrapper.Logs;
import pikefin.hibernate.*;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.protocol.HTTP;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFText2HTML;
import org.apache.pdfbox.util.PDFTextStripper;
import org.hibernate.Criteria;
//import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Subqueries;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.Arrays;
import java.lang.reflect.InvocationTargetException;


/*
 * Using Thread here instead of Runnable. Broker uses getState which Runnable doesn't have. We aren't extending from another
 * class so there's no real need to use Runnable.
 * 
 * Update 5/29/2012 - Apparently ThreadPoolTaskExecutor expects a Runnable and not a Thread so if we ever switch over to using TPTE, this class will have to
 * be switched to Runnable.
 */
class DataGrab extends Thread {

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
	String strFactTable;
	boolean bVerify; //boolean for verify mode. Loads data and skips alert processing.

	int nTaskBatch;

	/* nJobBatch is not currenlty implemented */
	int nJobBatch;
	
	int nPriority;

	
	boolean bContinue = true;
	Calendar calJobProcessingStart = null;
	Calendar calJobProcessingEnd = null;
	Calendar calAlertProcessingStart = null;
	Calendar calAlertProcessingEnd = null;
	Calendar calJobProcessingStage1Start = null;
	Calendar calJobProcessingStage1End = null;
	Calendar calJobProcessingStage2Start = null;
	Calendar calJobProcessingStage2End = null;
	final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/* Variable to filter tickers for quicker debugging. */
	String strStaticTickerLimit = "";

	String strOriginalTicker;
	int nCurrentEntityId;
	String strCurrentTicker;

	DBFunctions dbf;
	//UtilityFunctions uf;
	ProcessingFunctions pf;
	
	
	public void setProcessingFunctions(ProcessingFunctions inputPF) {
		pf = inputPF;
	}
	
	public void setDbFunctions(DBFunctions inputDBF) {
		dbf = inputDBF;
	}
		
	/*public void setUtilityFunctions(UtilityFunctions inputUF) {
		uf = inputUF;
	}*/

	DataGrab(/*UtilityFunctions tmpUF,*/ DBFunctions dbfparam, String strTask/*Primary key of task but as a string.*/, int nBatchId, String strScheduleIdParam, String strRepeatTypeIdParam, boolean bVerifyMode, int nInputPriority) {
	
	  	//this.uf = tmpUF;
	  	this.dbf = dbfparam;
	  	this.pf = new ProcessingFunctions(this);
	  	
	  	this.nPriority = nInputPriority;
	  	
	  	
	  	this.nCurTask = Integer.parseInt(strTask);
	  	this.strScheduleId = strScheduleIdParam;
	  	this.strRepeatTypeId = strRepeatTypeIdParam;
	  	this.bVerify = bVerifyMode;
	  	
	  	if (bVerifyMode == true)
	  		this.strFactTable = "verify_fact_data";
	  	else
	  		this.strFactTable = "fact_data";
	  	

	  	try	{
	  		/*
	  		 * nBatchId should only be set for historical data loads.
	  		 */
	  		if (nBatchId == 0)
	  			this.nTaskBatch = dbf.insertBatchesEntrySynchronized(this.nCurTask,this.bVerify);
	  		else
	  			this.nTaskBatch = nBatchId;
	
	  	
		  	//String strQuery = "select jobs_tasks1.job_id,tasks1.name from jobs_tasks as jobs_tasks1,tasks as tasks1 where tasks1.id=jobs_tasks1.task_id and jobs_tasks1.task_id=" + strTask;
		  	
		  	String strQuery = "from Task t ";
		  	strQuery += " inner join t.job j ";
		  	strQuery += " where t.id=" + strTask;
		  	
		  	//ResultSet rs = dbf.db_run_query(strQuery);
		  	//SqlRowSet rs = dbf.dbSpringRunQuery(strQuery);
		  	List<Object[]> l = dbf.dbHibernateRunQuery(strQuery);
		  	
		  	
		  	jobsArray = new ArrayList<String>();
		  	
		  	//this.strCurTask=rs.getString("tasks.name");
		  	//String tmp = "job_primary_key";
	
		  	//while(rs.next()) {
		  	for (Object[] objArray : l) {
		  		Task t = (Task)objArray[0];
		  		Job j = (Job)objArray[1];
		  		
		  		if (strCurTask.isEmpty())
		  			this.strCurTask=t.getName();
		  		
		  		jobsArray.add(j.getJobId() + "");
		  		//jobsArray.add(rs.getInt("jobs_tasks1.job_id")+"");
		   	}
	
	  	}
	  	catch (DataAccessException sqle) {
	  		UtilityFunctions.stdoutwriter.writeln("Problem retreiving list of jobs. Aborting task.",Logs.ERROR,"DG12.5");
			UtilityFunctions.stdoutwriter.writeln(sqle);
			return;
	  	}
		  	
	  	
	  	
	  	
	  	//strCurDataSet = strDataSet;
	  	strCurrentTicker = "";
	  	
	
	  	
	  	
	  	
		calJobProcessingStart = Calendar.getInstance();

	
  }
	
	public Calendar getTaskMetric(TaskMetrics tm) throws CustomGenericException {
	
	
		switch (tm) {
		case JOB_START:
			if (calJobProcessingStart != null)
				return(calJobProcessingStart);
			else
				throw new CustomGenericException();
			//break;
			
		case JOB_END:
			if (calJobProcessingEnd != null)
				return(calJobProcessingEnd);
			else
				throw new CustomGenericException();
			//break;
		case ALERT_START:
			/*
			 * We aren't processing alerts if in verify mode.
			 */
			if (this.bVerify == false) {
				if (calAlertProcessingStart != null)
					return(calAlertProcessingStart);
				else 
					throw new CustomGenericException();
			}
			else
				return(null);
			//break;
		case ALERT_END:
			if (this.bVerify == false) {
				if (calAlertProcessingEnd != null)
					return(calAlertProcessingEnd);
				else
					throw new CustomGenericException();
			}
			else 
				return(null);
			//break;
			
		/*
		 * The Stage performance markers are only used for default url processing.
		 */
		case STAGE1_START:
			return(calJobProcessingStage1Start);
			//break;

		case STAGE1_END:
			return(calJobProcessingStage1End);
			//break;
		
			
		case STAGE2_START:
			return(calJobProcessingStage2Start);
			//break;
		
			
		case STAGE2_END:
			return(calJobProcessingStage2End);
			//break;
			
		default:
			throw new CustomGenericException();
			//break;

		}
		
		/*if (strRet == null)
			throw new CustomGenericException();
		else if (strRet.isEmpty())
			return "null";
		else
			return "'" + strRet + "'";*/
		
	}

	@Deprecated
	public Calendar getJobProcessingStartTime() throws CustomGenericException {
		if (calJobProcessingStart != null)
			return (calJobProcessingStart);
		else
			throw new CustomGenericException();
	}

	@Deprecated
	public Calendar getJobProcessingEndTime() throws CustomGenericException {
		if (calJobProcessingEnd != null)
			return (calJobProcessingEnd);
		else
			throw new CustomGenericException();
	}
	
	
	@Deprecated
	public Calendar getAlertProcessingStartTime() throws CustomGenericException {
		if (calAlertProcessingStart != null)
			return (calAlertProcessingStart);
		else
			throw new CustomGenericException();
	}
	
	@Deprecated
	public Calendar getAlertProcessingEndTime() throws CustomGenericException {
		if (calAlertProcessingEnd != null)
			return (calAlertProcessingEnd);
		else
			throw new CustomGenericException();
	}
	
	@Deprecated
	public String getAlertProcessingStartTimeString() throws CustomGenericException {
		if (this.bVerify == true)
			return "";
		else if (calAlertProcessingStart != null)
			return(this.formatter.format(calAlertProcessingStart.getTime()));
		else
			throw new CustomGenericException();
	}
	
	@Deprecated
	public String getAlertProcessingEndTimeString() throws CustomGenericException {
		if (this.bVerify == true)
			return "";
		else if (calAlertProcessingEnd != null)
			return(this.formatter.format(calAlertProcessingEnd.getTime()));
		else
			throw new CustomGenericException();
	}

	@Deprecated
	public Calendar getStage1StartTime() throws CustomGenericException {
		if (calJobProcessingStage1Start != null)
			return (calJobProcessingStage1Start);
		else
			throw new CustomGenericException();
	}
	
	@Deprecated
	public Calendar getStage1EndTime() throws CustomGenericException {
		if (calJobProcessingStage1End != null)
			return (calJobProcessingStage1End);
		else
			throw new CustomGenericException();
	}
	
	@Deprecated
	public Calendar getStage2StartTime() throws CustomGenericException {
		if (calJobProcessingStage2Start != null)
			return (calJobProcessingStage2Start);
		else
			throw new CustomGenericException();
	}

	@Deprecated
	public Calendar getStage2EndTime() throws CustomGenericException {
		if (calJobProcessingStage2End != null)
			return (calJobProcessingStage2End);
		else
			throw new CustomGenericException();
	}
	
	/*
	 * OFP 2/12/2012 - These thread related helper methods were intended to provide the ability
	 * to terminate a thread with some kind of cleanup but that cleanup/terminate feature 
	 * is not being used (and no plans to implement it further).
	 */

	/*public void startThread() {
		bContinue = true;
		this.start();
	}*/

	/*public void stopThread() {
		bContinue = false;
	}

	public boolean getWillTerminate() {
		return (!bContinue);
	}*/
	
	/*
	 * end thread related helper methods.
	 */
	
	

	public void run() {

		UtilityFunctions.stdoutwriter.wrapperNDCPush("[Task:" + this.nCurTask + "]");

		UtilityFunctions.stdoutwriter.writeln(
				"=========================================================",
				Logs.STATUS1, "");

		UtilityFunctions.stdoutwriter.writeln("PROCESSING TASK " + strCurTask,
				Logs.STATUS1, "DG37");

		if (bContinue == false) {
			UtilityFunctions.stdoutwriter.wrapperNDCPop();
			//dbf.closeConnection();
			return;
		}
		
		if (this.bVerify == true) {
			UtilityFunctions.stdoutwriter.writeln("SCHEDULE RUNNING IN VERIFY MODE!", Logs.STATUS1,
					"DG38.95");
			UtilityFunctions.stdoutwriter.wrapperNDCPush("[Verify Mode]");
		}

		UtilityFunctions.stdoutwriter.writeln("JOB PROCESSING START TIME: "
				+ calJobProcessingStart.getTime().toString(), Logs.STATUS1,
				"DG39");

		UtilityFunctions.stdoutwriter.writeln("INITIATING THREAD",
				Logs.STATUS1, "DG1");

		if (Broker.getLoadingHistoricalData() == true)
			UtilityFunctions.stdoutwriter.wrapperNDCPush(HistoricalDataLoad.calCurrent.getTime().toString());

		

		for (int i = 0; i < jobsArray.size(); i++) {
			grab_data_set(jobsArray.get(i));
		}

		calJobProcessingEnd = Calendar.getInstance();
		UtilityFunctions.stdoutwriter.writeln("JOB PROCESSING END TIME: "
				+ calJobProcessingEnd.getTime().toString(), Logs.STATUS1,
				"DG38.15");

		if (Broker.getLoadingHistoricalData() == true)
			UtilityFunctions.stdoutwriter.wrapperNDCPop();
		
		if (this.bVerify == true)
			UtilityFunctions.stdoutwriter.wrapperNDCPop();
		


		/*
		 * Don't process alerts if loading historical data or in verfiy mode where we just 
		 * verify the data collection process.
		 */
		if (Broker.getLoadingHistoricalData() == false && this.bVerify == false) {

			calAlertProcessingStart = Calendar.getInstance();
			UtilityFunctions.stdoutwriter.writeln(
					"ALERT PROCESSING START TIME: "
							+ calAlertProcessingStart.getTime().toString(),
					Logs.STATUS1, "DG38.25");

			Alerts al = new Alerts();
			try {
				al.checkAlerts(this);
			} catch (DataAccessException sqle) {
				UtilityFunctions.stdoutwriter.writeln(
						"Problem processing alerts.", Logs.ERROR, "DG11.52");
				UtilityFunctions.stdoutwriter.writeln(sqle);
				// dbf.closeConnection();
				// return;

			} catch (PikefinException pe) {
				UtilityFunctions.stdoutwriter.writeln(
						"Problem processing alerts.", Logs.ERROR, "DG11.558");
				UtilityFunctions.stdoutwriter.writeln(pe);
				
			}
			calAlertProcessingEnd = Calendar.getInstance();
			UtilityFunctions.stdoutwriter.writeln("ALERT PROCESSING END TIME: "
					+ calAlertProcessingEnd.getTime().toString(), Logs.STATUS1,
					"DG54");
		}
		UtilityFunctions.stdoutwriter.wrapperNDCPop();
		//dbf.closeConnection();

	}

	

	


	public String get_value(String local_data_set)
			throws IllegalStateException, TagNotFoundException, DataAccessException,
			CustomRegexException {
		String strDataValue = "";

		// try
		// {

		// run sql to get info about the data_set
		// Connection con = UtilityFunctions.db_connect();

		//String query = "select extract_singles.* from extract_singles,jobs where extract_singles.id=jobs.extract_key and jobs.Data_Set='"
		//		+ local_data_set + "'";
		
		Criteria criteria = dbf.dbHibernateGetCriteria(ExtractSingle.class);
		DetachedCriteria existsCriteria = DetachedCriteria.forClass(Job.class);
		existsCriteria.add(Property.forName("Job.extract_key").eqProperty("ExtractSingle.id"));
		criteria.add(Subqueries.exists(existsCriteria.setProjection(Projections.property("Job.jobId"))));
		ExtractSingle es = (ExtractSingle)criteria.uniqueResult();
		
		
		

		// Statement stmt = con.createStatement();
		//ResultSet rs = dbf.db_run_query(query);
		//SqlRowSet rs = dbf.dbSpringRunQuery(query);

		//rs.next();
		
		
		if (es.isParsePostProcess() == true) {
			//We do the parsing in the post process function
			return(this.returned_content);
		}

		int tables, cells, rows, divs;
		/*
		 * This next line will throw an exception if you meant to do a table
		 * extraction but forgot to set the table_extraction flag in the db.
		 */
		tables = es.getTableCount();
		cells = es.getCellCount();
		rows = es.getRowCount();
		divs = es.getDivCount();

		int nCurOffset = 0;

		/*
		 * Initial regex search.
		 */

		String strInitialOpenUniqueCode =es.getInitialBefUniqueCode();
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
		nCurOffset = UtilityFunctions.regexSeekLoop("(?i)(<TABLE[^>]*>)", tables, nCurOffset,returned_content);

		UtilityFunctions.stdoutwriter.writeln("Before table row searches.",
				Logs.STATUS2, "DG17");
		nCurOffset = UtilityFunctions.regexSeekLoop("(?i)(<tr[^>]*>)", rows, nCurOffset,returned_content);

		UtilityFunctions.stdoutwriter.writeln("Before table cell searches.",
				Logs.STATUS2, "DG18");
		nCurOffset = UtilityFunctions.regexSeekLoop("(?i)(<td[^>]*>)", cells, nCurOffset,returned_content);

		UtilityFunctions.stdoutwriter.writeln("Before div searches",
				Logs.STATUS2, "DG19");
		nCurOffset = UtilityFunctions.regexSeekLoop("(?i)(<div[^>]*>)", divs, nCurOffset,returned_content);

		String strBeforeUniqueCode = es.getBeforeUniqueCode();
		String strAfterUniqueCode = es.getAfterUniqueCode();
		/*
		 * String strBeforeUniqueCodeRegex = "(?i)(" + strBeforeUniqueCode +
		 * ")";
		 * 
		 * String strAfterUniqueCodeRegex = "(?i)(" + strAfterUniqueCode + ")";
		 */

		strDataValue = UtilityFunctions.regexSnipValue(strBeforeUniqueCode, strAfterUniqueCode,
				nCurOffset,this.returned_content);

		/*
		 * OFP 3/28/2011 - Don't remove commas if this is csv format. Let the
		 * postProcessing function handle the commas.
		 */
		if (es.isIsCSVFormat() == false)
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

	private ArrayList<String[]> get_table_with_headers(String strTableSet)
			throws DataAccessException, TagNotFoundException,
			PrematureEndOfDataException {
		String query = "select extract_key_colhead,extract_key_rowhead,multiple_tables from jobs where data_set='"
				+ strTableSet + "'";
		//ResultSet rs = dbf.db_run_query(query);
		SqlRowSet rs = dbf.dbSpringRunQuery(query);
		rs.next();

		//ArrayList<String[]> tabledatabody = get_table(strTableSet, "body");
		ArrayList<String[]> tabledatabody = new EnhancedTable(this,strTableSet,"body").enhancedGetTable();

		if (!((rs.getString("extract_key_colhead") == null) || rs.getString(
				"extract_key_colhead").isEmpty())) {
			//ArrayList<String[]> tabledatacol = get_table(strTableSet, "colhead");
			ArrayList<String[]> tabledatacol = new EnhancedTable(this,strTableSet,"colhead").enhancedGetTable();

			tabledatabody.add(0, tabledatacol.get(0));
		} else
			tabledatabody.add(0, null);

		// strTmpTableSet = strTableSet.replace("_body","_rowhead");

		if (!((rs.getString("extract_key_rowhead") == null) || rs.getString(
				"extract_key_rowhead").isEmpty())) {
			//ArrayList<String[]> tabledatarow = get_table(strTableSet, "rowhead");
			ArrayList<String[]> tabledatarow = new EnhancedTable(this,strTableSet,"rowhead").enhancedGetTable();

			String[] temp = new String[tabledatarow.size()];
			for (int i = 0; i < tabledatarow.size(); i++) {
				temp[i] = tabledatarow.get(i)[0];

			}

			tabledatabody.add(1, temp);
		}

		return (tabledatabody);

	}

	
	private void defaultURLProcessing(String strDataSet) throws DataAccessException,
			MalformedURLException, IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {

		String query;

		query = "select * from jobs where data_set='" + strDataSet + "'";

		//ResultSet rs2 = dbf.db_run_query(query);
		SqlRowSet rs2 = dbf.dbSpringRunQuery(query);
		rs2.next();
		calJobProcessingStage1Start = Calendar.getInstance();
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;

		if ((rs2.getInt("input_source") != 0)) {
			query = "select * from input_source where id="
					+ rs2.getInt("input_source");
			//ResultSet rs3 = dbf.db_run_query(query);
			SqlRowSet rs3 = dbf.dbSpringRunQuery(query);
			rs3.next();
			String strStProperties = rs3.getString("form_static_properties");
			String[] listItems = strStProperties.split(":");
			//String[] listItems2;
			//String data = "";
			for (int i = 0; i < listItems.length; i++) {
				//listItems2 = listItems[i].split("=");
				/*if (i != 0) {
					data += "&";
				}*/

				// check to see if there is an equal sign or a value on the
				// right side of the equality
				/*if (listItems2.length == 1)
					data += URLEncoder.encode(listItems2[0], "UTF-8") + "=";
				else
					data += URLEncoder.encode(listItems2[0], "UTF-8") + "="
							+ URLEncoder.encode(listItems2[1], "UTF-8");*/

			}

			UtilityFunctions.stdoutwriter.writeln(
					"Retrieving URL Form: " + rs3.getString("url_form"),
					Logs.STATUS2, "DG24.5");

			//data = "series_id=LNS14000000&survey=ln&format=&html_tables=&delimiter=&catalog=&print_line_length=&lines_per_page=&row_stub_key=&year=&date=&net_change_start=&net_change_end=&percent_change_start=&percent_change_end=";
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
		//Calendar cal = Calendar.getInstance();

		/*
		 * OFP 9/15/2011 - Added workaround for connection reset exception for
		 * bloomberg index tasks.
		 */
		
		StringBuilder sb = new StringBuilder();
		boolean bDone = false;
		while (!bDone) {
			try {

				while ((nTmp = in.read()) != -1) {
					this.nCount++;
					sb.append((char)nTmp);
					//returned_content += (char) nTmp;
					//if (nCount == 10000 && nCurTask == 6) {
						//Calendar cal2 = Calendar.getInstance();
						//System.out.println("b");
					//}
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
					sb = new StringBuilder();
				}

			}
		}
		returned_content = sb.toString();

		in.close();
		calJobProcessingStage2End = Calendar.getInstance();

		httpclient.getConnectionManager().shutdown();

		UtilityFunctions.stdoutwriter.writeln("Done reading url contents",
				Logs.STATUS2, "DG26");

	}

	private void get_url(String strDataSet) throws DataAccessException,
			MalformedURLException, IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		String query;

		query = "select * from jobs where data_set='" + strDataSet + "'";

		//ResultSet rs2 = dbf.db_run_query(query);
		SqlRowSet rs2 = dbf.dbSpringRunQuery(query);
		
		rs2.next();
		String strCustomURLFuncName = rs2.getString("custom_url_func_name");
		if (strCustomURLFuncName == null || strCustomURLFuncName.isEmpty()) {
			defaultURLProcessing(strDataSet);
		} else {

			Class[] args1 = new Class[1];
			args1[0] = String.class;

			Method m = this.getClass().getMethod(strCustomURLFuncName, args1);
			m.invoke(this, strDataSet);
		}

	}

	/*private ArrayList<String[]> obsolete_get_table(String strTableSet, String strSection)
			throws DataAccessException, TagNotFoundException,
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
			#*
			 * Lazy here, should create a new exception type but just reusing
			 * one that this function already throws.
			 *#
			throw new TagNotFoundException();
		}

		ResultSet rs = dbf.db_run_query(query);
		rs.next();
		#*
		 * if nFixedDataRows is null or zero, then we are grabbing an
		 * indeterminate number of rows.
		 *#
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

			nCurOffset = this.uf.regexSeekLoop("(?i)(<TABLE[^>]*>)", nCurTable,
					nCurOffset,returned_content);

			nCurOffset = this.uf.regexSeekLoop("(?i)(<tr[^>]*>)",
					rs.getInt("top_corner_row"), nCurOffset,returned_content);

			int nEndTableOffset = this.uf.regexSeekLoop("(?i)(</TABLE>)", 1, nCurOffset,returned_content);

			// iterate over rows, iterate over columns
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
						nCurOffset = this.uf.regexSeekLoop("(?i)(<td[^>]*>)",
								rs.getInt("Column" + (i + 1)), nCurOffset,returned_content);
					else
						nCurOffset = this.uf.regexSeekLoop("(?i)(<th[^>]*>)",
								rs.getInt("Column" + (i + 1)), nCurOffset,returned_content);

					// String strBeforeUniqueCode = rs.getString("bef_code_col"
					// + (i+1));
					strBeforeUniqueCodeRegex = "(?i)("
							+ rs.getString("bef_code_col" + (i + 1)) + ")";
					// String strAfterUniqueCode = rs.getString("aft_code_col" +
					// (i+1));
					strAfterUniqueCodeRegex = "(?i)("
							+ rs.getString("aft_code_col" + (i + 1)) + ")";
					try {
						strDataValue = this.uf.regexSnipValue(strBeforeUniqueCodeRegex,
								strAfterUniqueCodeRegex, nCurOffset,this.returned_content);

						#*
						 * Going to strip out &nbsp; for all data streams, let's
						 * see if this is a problem.
						 *#
						#* See below for exit conditions. *#
						if (strEndDataMarker != null
								&& (strEndDataMarker.length() > 0))
							if (strDataValue.equals(strEndDataMarker)) {
								#*
								 * Finished grabbing data for this table.
								 *#
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

				#*
				 * First part of if clause is for data sets with predefined
				 * fixed number of rows.
				 *#
				if (nFixedDataRows > 0) {
					if (nFixedDataRows == nRowCount)
						break;

					try {
						nCurOffset = this.uf.regexSeekLoop("(?i)(<tr[^>]*>)",
								nRowInterval, nCurOffset,returned_content);
					} catch (TagNotFoundException tnfe) {
						throw new PrematureEndOfDataException();
					}

					if (nEndTableOffset < nCurOffset)
						throw new PrematureEndOfDataException();

				}
				#*
				 * else part is for data sets of an indeterminate size.
				 *#
				else {
					try {
						nCurOffset = this.uf.regexSeekLoop("(?i)(<tr[^>]*>)",
								nRowInterval, nCurOffset,returned_content);
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
	}*/
	
	
	
	
	private void grab_data_set(String strJobPrimaryKey) {

		try {

			//String query = "select entity_groups.id,tasks.metric_id,tasks.use_group_for_reading,tasks.delay from entity_groups,entity_groups_tasks,tasks where entity_groups.id=entity_groups_tasks.entity_group_id and entity_groups_tasks.task_id=tasks.id and entity_groups_tasks.task_id="
			//		+ nCurTask;
			
			String query = " from Task t ";
			query += " join t.entityGroup eg ";
			query += " join t.metric m ";
			query += " where t.taskId=" + nCurTask;
			
			//ResultSet rs = dbf.db_run_query(query);
			
			List<Object[]> l2 = dbf.dbHibernateRunQuery(query);
			Object[] objArray  = l2.get(0);
			Task t = (Task)objArray[0];
			EntityGroup eg = (EntityGroup)objArray[1];
			Metric m = (Metric)objArray[2];
			
			//SqlRowSet rs = dbf.dbSpringRunQuery(query);
			
			
			//rs.next();
			int nMetricId = m.getMetricId();
			int nGroupId = eg.getEntityGroupId();
			int nDelay = t.getDelay();
			

			boolean bUseGroupForReading = t.isUseGroupForReading();

			boolean bTableExtraction = true;
			
			String query2 = " from Job j";
			query2 += " where j.jobId=" + strJobPrimaryKey;
			
			Job j  = (Job) dbf.dbHibernateRunQueryUnique(query2);
			
			
			//query = "select * from jobs where id='" + strJobPrimaryKey + "'";
			//rs = dbf.db_run_query(query);
			//rs = dbf.dbSpringRunQuery(query);
			///rs.next();
			
			this.strStage1URL = j.getUrlStatic();

			strCurDataSet = j.getDataSet();

			UtilityFunctions.stdoutwriter.writeln("====> PROCESSING JOB:"
					+ strCurDataSet, Logs.STATUS1, "DG1.55");

			//if (j.isTableExtraction() == 0)
			bTableExtraction = j.isTableExtraction();

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

						//ResultSet rs2 = dbf
						//		.db_run_query("select custom_insert from jobs where data_set='"
						//				+ strCurDataSet + "'");
						/*String query = " from "
						SqlRowSet rs2 = dbf.dbSpringRunQuery("select custom_insert from jobs where data_set='"
								+ strCurDataSet + "'");
						
						rs2.next();*/

						/*
						 * Insert directly into fact_data now.
						 */
						if (j.isCustomInsert() == false)
							dbf.importTableIntoDB(tabledata2, this.strFactTable,
									this.nTaskBatch, this.nCurTask, nMetricId);

					} catch (MalformedURLException mue) {
						UtilityFunctions.stdoutwriter.writeln(
								"Badly formed url, processing dataset aborted",
								Logs.ERROR, "DG38.1");
						UtilityFunctions.stdoutwriter.writeln(mue);
					} catch (DataAccessException sqle) {
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

					//ResultSet rs2 = dbf
					//		.db_run_query("select custom_insert from jobs where data_set='"
					//				+ strCurDataSet + "'");
					
					SqlRowSet rs2 = dbf.dbSpringRunQuery("select custom_insert from jobs where data_set='"
									+ strCurDataSet + "'");
					
					rs2.next();
					if (rs2.getInt("custom_insert") != 1) {
						/*
						 * Import directly into fact_data now.
						 */
						dbf.importTableIntoDB(tabledata2, this.strFactTable,
								this.nTaskBatch, this.nCurTask, nMetricId);
					

					}

				}

			} else {

				// query = "select * from entities where groups like '%" + group
				// + "%' order by ticker";
				/*query = "select entities.* from entities,entities_entity_groups ";
				query += " where entities_entity_groups.entity_group_id="
						+ nGroupId;
				query += " AND entities_entity_groups.entity_id=entities.id ";*/
				
				query = " from EntityGroup eg ";
				query += " join eg.entity e ";
				query += " where eg.groupId=" + nGroupId;
				
				List<Object[]> l = dbf.dbHibernateRunQuery(query);
				

				//rs = dbf.db_run_query(query);
				//rs = dbf.dbSpringRunQuery(query);
				

				// String strCurTicker="";
				// String fullUrl;
				String strDataValue = "";
				//int count = 0;

				/* Loop through the list of group items, e.g. stock tickers */
				for (Object[] objArray3 : l) {
					Entity e = (Entity)objArray3[1];
				//while (rs.next()) {
					try {
						/*
						 * Have to maintain original ticker and current ticker
						 * since some websites use a different format for
						 * certain tickers, i.e BRK/A vs BRK-A.
						 * "Original Ticker" is the format that is stored in the
						 * entity table. "Current Ticker" gets modified to the
						 * format that the website uses.
						 */
						this.strOriginalTicker = this.strCurrentTicker = e.getTicker();
						this.nCurrentEntityId = e.getEntityId();

						UtilityFunctions.stdoutwriter.writeln(
								"Processing ticker: " + this.strCurrentTicker,
								Logs.STATUS2, "DG39.1");

						/* Active only to debug individual tickers */
						if (strStaticTickerLimit.isEmpty() != true)
							if (strCurrentTicker
									.compareTo(strStaticTickerLimit) != 0)
								continue;
							/*else {
								int o = 0;
								o++;
							}*/

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

								//ResultSet rs2 = dbf
								//		.db_run_query("select custom_insert from jobs where data_set='"
								//				+ strCurDataSet + "'");
								
								SqlRowSet rs2 = dbf.dbSpringRunQuery("select custom_insert from jobs where data_set='"
												+ strCurDataSet + "'");
								
								rs2.next();
								if (rs2.getInt("custom_insert") != 1)
									dbf.importTableIntoDB(tabledata2,
											this.strFactTable, this.nTaskBatch,
											this.nCurTask, nMetricId);

					
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
							} catch (DataAccessException sqle) {
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
							} catch (Exception ex) {
								UtilityFunctions.stdoutwriter.writeln(
										"Processing table for ticker "
												+ strCurrentTicker
												+ " failed, skipping",
										Logs.ERROR, "DG44");
								UtilityFunctions.stdoutwriter.writeln(ex);
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

								//ResultSet rs2 = dbf
								//		.db_run_query("select custom_insert from jobs where data_set='"
								//				+ strCurDataSet + "'");
								SqlRowSet rs2 = dbf.dbSpringRunQuery("select custom_insert from jobs where data_set='"
												+ strCurDataSet + "'");
								
								
								
								rs2.next();
								if (rs2.getInt("custom_insert") != 1)
									dbf.importTableIntoDB(tabledata2,
											this.strFactTable, this.nTaskBatch,
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
							catch (DataAccessException sqle) {
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
							} catch (Exception ex) {
								UtilityFunctions.stdoutwriter.writeln(
										"Processing table for ticker "
												+ strCurrentTicker
												+ " failed, skipping",
										Logs.ERROR, "DG51");
								UtilityFunctions.stdoutwriter.writeln(ex);
								
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

					} catch (DataAccessException sqle) {
						UtilityFunctions.stdoutwriter.writeln(
								"problem with sql statement in grab_data_set",
								Logs.ERROR, "DG52");
						UtilityFunctions.stdoutwriter.writeln(
								"Processing of data_set " + strCurDataSet
										+ " with ticker " + strCurrentTicker
										+ " FAILED ", Logs.ERROR, "DG53");
						UtilityFunctions.stdoutwriter.writeln(sqle);
					}

					//count++;
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
	
	public void customPDFURL(String strDataSet) throws DataAccessException,IOException {
		//HttpResponse response;
		//HttpGet httpget;
		//HttpClient httpclient = new DefaultHttpClient();
		
		
		String query = "select * from jobs where data_set='" + strDataSet + "'";

		//ResultSet rs2 = dbf.db_run_query(query);
		SqlRowSet rs2 = dbf.dbSpringRunQuery(query);
		
		rs2.next();
		
		String strURL = rs2.getString("url_static");
		
		URL url = new URL(strURL);
		PDDocument pd2 = PDDocument.load(url, true);
		
		PDFTextStripper stripper = new PDFText2HTML("utf-8");
		
		stripper.setStartPage(1);
		stripper.setEndPage(1);
		
		returned_content = stripper.getText(pd2);
		
	}

	/* 
	 * function is called dynamically 
	 * It has to be public or the dynamic invocation doesn't work.
	 *
	 */
	public void customYahooBulkURL(String strDataSet) throws DataAccessException,
			IOException {

		// Get a list of all the tickers

		int nGroupSize = 100;
		
		/*calJobProcessingStage1Start = Calendar.getInstance();
		calJobProcessingStage1End = Calendar.getInstance();
		calJobProcessingStage2Start = Calendar.getInstance();
		calJobProcessingStage2End = Calendar.getInstance();*/

		String query2 = "select entity_groups.id,tasks.metric_id,tasks.use_group_for_reading from entity_groups,entity_groups_tasks,tasks where entity_groups.id=entity_groups_tasks.entity_group_id and entity_groups_tasks.task_id=tasks.id and entity_groups_tasks.task_id="
				+ nCurTask;
		//ResultSet rs = dbf.db_run_query(query2);
		SqlRowSet rs = dbf.dbSpringRunQuery(query2);
		
		rs.next();
		int nGroupId = rs.getInt("entity_groups.id");

		String query = "select count(entities.id) as cnt from entities,entities_entity_groups ";
		query += " where entities_entity_groups.entity_group_id=" + nGroupId;
		query += " AND entities_entity_groups.entity_id=entities.id ";
		
		//rs = dbf.db_run_query(query);
		rs = dbf.dbSpringRunQuery(query);
		
		rs.next();

		int nTotalCount = rs.getInt("cnt");

		query = "select * from jobs where data_set='" + strDataSet + "'";

		//ResultSet rs2 = dbf.db_run_query(query);
		SqlRowSet rs2 = dbf.dbSpringRunQuery(query);
		rs2.next();

		String strURLStatic = rs2.getString("url_static");

		int nGroupCount = 1;
		//int nCurrentCount = 0;

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
			//rs = dbf.db_run_query(query);
			rs = dbf.dbSpringRunQuery(query);

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

			String strURL = strURLStatic.replace("${ticker}", strList);

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
			//DateFormat formatter = new SimpleDateFormat("M/d/yyyy");

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
						nBegin = (nBegin - 150 < 0 ? 0 : nBegin -150);
						nEnd = (nEnd + 150 > strTemp.length() ? strTemp.length() : nEnd + 150);
						UtilityFunctions.stdoutwriter.writeln(strTemp.substring(nBegin,nEnd),Logs.STATUS1,"DG55.12");
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

class EnhancedTable {
	
	int nRowCount;
	int nNumOfColumns;
	String strTableSet;
	String strSection;
	DataGrab dg;
	boolean bColTHTags;
	String strEndDataMarker;
	
	boolean bDoneProcessing;
	
	//ResultSet rs;
	SqlRowSet rs;
	
	
	public EnhancedTable(DataGrab dg,String inputTableSet, String inputSection) {
		this.dg = dg;
		this.strTableSet = inputTableSet;
		this.strSection = inputSection;
		this.bDoneProcessing = false;
		
	}
	
	//public ArrayList<String[]> enhancedGetTable(String strTableSet, String strSection)
	public ArrayList<String[]> enhancedGetTable()
	throws DataAccessException, TagNotFoundException,	PrematureEndOfDataException {
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
		
		//rs = dg.dbf.db_run_query(query);
		rs = dg.dbf.dbSpringRunQuery(query);
		
		rs.next();
		/*
		 * if nFixedDataRows is null or zero, then we are grabbing an
		 * indeterminate number of rows.
		 */
		int nFixedDataRows = rs.getInt("rowsofdata");
		int nRowInterval = rs.getInt("rowinterval");
		strEndDataMarker = rs.getString("end_data_marker");
		this.bColTHTags = true;
		if (rs.getInt("column_th_tags") != 1)
			this.bColTHTags = false;
		
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
		
			nCurOffset = UtilityFunctions.regexSeekLoop("(?i)(<TABLE[^>]*>)", nCurTable,
					nCurOffset,dg.returned_content);
		
			nCurOffset = UtilityFunctions.regexSeekLoop("(?i)(<tr[^>]*>)",
					rs.getInt("top_corner_row"), nCurOffset, dg.returned_content);
			
			int nEndRowOffset = UtilityFunctions.regexSeekLoop("(?i)(</tr>)",
					1, nCurOffset,dg.returned_content);
		
			int nEndTableOffset = UtilityFunctions.regexSeekLoop("(?i)(</TABLE>)", 1, nCurOffset,dg.returned_content);
		
			// iterate over rows, iterate over columns
			boolean done = false;
			this.nNumOfColumns = rs.getInt("number_of_columns");
		
			String[] rowdata;
			this.nRowCount = 0;
		
			
		
			while (!done) {
				
				
				rowdata = enhancedProcessRow(dg.returned_content.substring(nCurOffset,nEndRowOffset));
				
				/*
				 * This check is for when end_data_marker indicates the end of the table.
				 */
				if (this.bDoneProcessing == true)
					break;
						

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
						nCurOffset = UtilityFunctions.regexSeekLoop("(?i)(<tr[^>]*>)",
								nRowInterval, nCurOffset,dg.returned_content);
						nEndRowOffset = UtilityFunctions.regexSeekLoop("(?i)(</tr>)",
								1, nCurOffset,dg.returned_content);
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
						nCurOffset = UtilityFunctions.regexSeekLoop("(?i)(<tr[^>]*>)",
								nRowInterval, nCurOffset,dg.returned_content);
						nEndRowOffset = UtilityFunctions.regexSeekLoop("(?i)(</tr>)",
								1, nCurOffset,dg.returned_content);
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
	
	private String[] enhancedProcessRow(String strRow) throws DataAccessException {
		
		String[] rowdata;
		UtilityFunctions.stdoutwriter.writeln("row: " + nRowCount,
				Logs.STATUS2, "DG28");
		rowdata = new String[nNumOfColumns];
		int nRowOffset=0;
		
		String strBeforeUniqueCodeRegex, strAfterUniqueCodeRegex, strDataValue;
		
		try {

			for (int i = 0; i < nNumOfColumns; i++) {
	
				UtilityFunctions.stdoutwriter.writeln("Column: " + i,
						Logs.STATUS2, "DG29");
	
				if (bColTHTags == false)
					nRowOffset = UtilityFunctions.regexSeekLoop("(?i)(<td[^>]*>)",
							rs.getInt("Column" + (i + 1)), nRowOffset,strRow);
				else
					nRowOffset = UtilityFunctions.regexSeekLoop("(?i)(<th[^>]*>)",
							rs.getInt("Column" + (i + 1)), nRowOffset,strRow);
	
				// String strBeforeUniqueCode = rs.getString("bef_code_col"
				// + (i+1));
				strBeforeUniqueCodeRegex = "(?i)("
						+ rs.getString("bef_code_col" + (i + 1)) + ")";
				// String strAfterUniqueCode = rs.getString("aft_code_col" +
				// (i+1));
				strAfterUniqueCodeRegex = "(?i)("
						+ rs.getString("aft_code_col" + (i + 1)) + ")";
				try {
					strDataValue = UtilityFunctions.regexSnipValue(strBeforeUniqueCodeRegex,
							strAfterUniqueCodeRegex, nRowOffset,strRow);
	
					
					/* See below for exit conditions. */
					if (strEndDataMarker != null
							&& (strEndDataMarker.length() > 0))
						if (strDataValue.equals(strEndDataMarker)) {
							this.bDoneProcessing = true;
							return null;
						}
	
					strDataValue = strDataValue.replace("\r", "");
					strDataValue = strDataValue.replace("\n", "");
					strDataValue = strDataValue.trim();
					/*
					 * Going to strip out &nbsp; for all data streams, let's
					 * see if this is a problem.
					 */
					rowdata[i] = strDataValue.replace("&nbsp;", "");
				} catch (CustomRegexException cre) {
					UtilityFunctions.stdoutwriter
							.writeln(
									"Empty cell in table in url stream. Voiding cell.",
									Logs.STATUS2, "DG30");
					rowdata[i] = "pikefinvoid";
				} catch (IllegalStateException ise) {
					UtilityFunctions.stdoutwriter.writeln("Invalid or Irregularly formed table row, inserting pikefinvoid.",Logs.WARN, "DG30.999");
					rowdata[0] = "pikefinvoid";
					/*
					 * We have to return a value so that the rowheaders synch up with the body
					 */
					return(rowdata);
				}
	
			}
			
			return(rowdata);
		}
		catch (TagNotFoundException tnfe) {
			UtilityFunctions.stdoutwriter.writeln("Invalid or Irregularly formed table row, inserting pikefinvoid.",Logs.WARN, "DG30.27");
			rowdata[0] = "pikefinvoid";
			return(rowdata);
		}
	}

	
	
	
	
	
	
	
}

enum TaskMetrics {
	JOB_START(0,"JobStart"),
	JOB_END(1,"JobEnd"),
	ALERT_START(2,"AlertStart"),
	ALERT_END(3,"AlertEnd"),
	STAGE1_START(4,"Stage1Start"),
	STAGE1_END(5,"Stage1End"),
	STAGE2_START(6,"Stage2Start"),
	STAGE2_END(7,"Stage2End");
	
	
	private int code;
	private String metric;
	
	private TaskMetrics(int c, String m) {
		code = c;
		metric = m;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMetric() {
		return metric;
	}
	
	
	
}
