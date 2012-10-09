package com.pikefin.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFText2HTML;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import pikefin.log4jWrapper.Logs;

import com.pikefin.ApplicationSetting;
import com.pikefin.Constants;
import com.pikefin.PikefinUtil;
import com.pikefin.TaskMetricsEnum;
import com.pikefin.businessobjects.Batches;
import com.pikefin.businessobjects.Entity;
import com.pikefin.businessobjects.EntityGroup;
import com.pikefin.businessobjects.ExtractSingle;
import com.pikefin.businessobjects.Job;
import com.pikefin.businessobjects.RepeatType;
import com.pikefin.businessobjects.Schedule;
import com.pikefin.businessobjects.Task;
import com.pikefin.exceptions.CustomEmptyStringException;
import com.pikefin.exceptions.CustomGenericException;
import com.pikefin.exceptions.CustomRegexException;
import com.pikefin.exceptions.GenericException;
import com.pikefin.exceptions.PrematureEndOfDataException;
import com.pikefin.exceptions.SkipLoadException;
import com.pikefin.exceptions.TagNotFoundException;
import com.pikefin.services.inter.BatcheService;
import com.pikefin.services.inter.EntityService;
import com.pikefin.services.inter.ExtractSingleService;
import com.pikefin.services.inter.FactDataService;
import com.pikefin.services.inter.JobService;



public class DataGrabExecuter extends Thread {
	Logger log=Logger.getLogger(DataGrabExecuter.class);
	@Autowired
	private BatcheService batchService;
	@Autowired
	private JobService jobService;
	@Autowired
	private ExtractSingleService extractSingleService;
	@Autowired
	private EntityService entityService;
	@Autowired
	private FactDataService factDataService;
	String returned_content;
	String strCurDataSet;
	private Job currentJob;
	private Task currentTask;
	private Schedule schedule;
	private RepeatType repeatType;
	private int currentTaskId;
	private Batches currentBatche;
	int nCount;
	
	String strStage1URL;
	String strStage2URL;
	String strFactTable;
	private boolean verifyMode; //boolean for verify mode. Loads data and skips alert processing.
	private int batchId;
	int nJobBatch;
	private int priority;

	
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

	@Autowired
	ProcessingFunctions pf;
	
	
	

	public DataGrabExecuter(Task task, int batchId, Schedule schedule, RepeatType repeatType, boolean verifyMode, int priority) {
	  	this.priority = priority;
	  	this.currentTaskId = task.getTaskId();
	  	this.schedule = schedule;
	  	this.repeatType = repeatType;
	  	this.verifyMode = verifyMode;
	  /**
	   * This  code might be omitted, because we will not support the verify mode in future	
	   */
	  	if (this.verifyMode == true)
	  		this.strFactTable = "verify_fact_data";
	  	else
	  		this.strFactTable = "fact_data";
	   	try	{
	    		if (batchId == 0){
	  			Batches batchesEntity=new Batches();
	  			batchesEntity.setBatchDateCollected(new Date());
	  			batchesEntity.setBatchTask(task);
	  			// No Support for verify mode
	  			batchesEntity=batchService.saveBatchesInfo(batchesEntity);
	  			this.batchId =batchesEntity.getBatchId();
	  			currentBatche=batchesEntity;
	  		}
	  		else{
	  			currentBatche=batchService.loadBatchesInfo(batchId);
	  			this.batchId = batchId;
	  		}
	  			  	
		  	this.currentTask = task;
	
	  	}
	  	catch (GenericException sqle) {
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem retreiving list of jobs. Aborting task.",Logs.ERROR,"DG12.5");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
			return;
	  	}
		  	
	   	strCurrentTicker = "";
	  	calJobProcessingStart = Calendar.getInstance();
  }
	
	public Calendar getTaskMetric(TaskMetricsEnum tm) throws GenericException {
	
	
		switch (tm) {
		case JOB_START:
			if (calJobProcessingStart != null)
				return(calJobProcessingStart);
			else
				throw new GenericException();
			//break;
			
		case JOB_END:
			if (calJobProcessingEnd != null)
				return(calJobProcessingEnd);
			else
				throw new GenericException();
			//break;
		case ALERT_START:
			/*
			 * We aren't processing alerts if in verify mode.
			 */
			if (this.verifyMode == false) {
				if (calAlertProcessingStart != null)
					return(calAlertProcessingStart);
				else 
					throw new GenericException();
			}
			else
				return(null);
			//break;
		case ALERT_END:
			if (this.verifyMode == false) {
				if (calAlertProcessingEnd != null)
					return(calAlertProcessingEnd);
				else
					throw new GenericException();
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
			throw new GenericException();
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
		if (this.verifyMode == true)
			return "";
		else if (calAlertProcessingStart != null)
			return(this.formatter.format(calAlertProcessingStart.getTime()));
		else
			throw new CustomGenericException();
	}
	
	@Deprecated
	public String getAlertProcessingEndTimeString() throws CustomGenericException {
		if (this.verifyMode == true)
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
	
	
	

	public void run() {
		ApplicationSetting.getInstance().getStdoutwriter().wrapperNDCPush("[Task:" + this.currentTask.getTaskId() + "]");

		ApplicationSetting.getInstance().getStdoutwriter().writeln(
				"=========================================================",
				Logs.STATUS1, "");

		ApplicationSetting.getInstance().getStdoutwriter().writeln("PROCESSING TASK " + this.getCurrentTask().getTaskId(),
				Logs.STATUS1, "DG37");
		if (bContinue == false) {
			ApplicationSetting.getInstance().getStdoutwriter().wrapperNDCPop();
			return;
		}
		if (this.verifyMode == true) {
			ApplicationSetting.getInstance().getStdoutwriter().writeln("SCHEDULE RUNNING IN VERIFY MODE!", Logs.STATUS1,
					"DG38.95");
			ApplicationSetting.getInstance().getStdoutwriter().wrapperNDCPush("[Verify Mode]");

		}
		ApplicationSetting.getInstance().getStdoutwriter().writeln("JOB PROCESSING START TIME: "
				+ calJobProcessingStart.getTime().toString(), Logs.STATUS1,
				"DG39");

		ApplicationSetting.getInstance().getStdoutwriter().writeln("INITIATING THREAD",
				Logs.STATUS1, "DG1");
		/*if (ApplicationSetting.getInstance().isLoadHistoricalData()== true)
			ApplicationSetting.getInstance().getStdoutwriter().wrapperNDCPush(HistoricalDataLoad.calCurrent.getTime().toString());
	*/	
		HashSet<Job> tmpJobs = (HashSet<Job>)currentTask.getJobs();
		for (Job j : tmpJobs) {
			currentJob = j;
			grabDataSet();
		}
		
		calJobProcessingEnd = Calendar.getInstance();
		ApplicationSetting.getInstance().getStdoutwriter().writeln("JOB PROCESSING END TIME: "
				+ calJobProcessingEnd.getTime().toString(), Logs.STATUS1,
				"DG38.15");
		if (ApplicationSetting.getInstance().isLoadHistoricalData() == true)
			ApplicationSetting.getInstance().getStdoutwriter().wrapperNDCPop();
		
		if (this.verifyMode == true)
			ApplicationSetting.getInstance().getStdoutwriter().wrapperNDCPop();
		


		/*
		 * Don't process alerts if loading historical data or in verfiy mode where we just 
		 * verify the data collection process.
		 */
		if (ApplicationSetting.getInstance().isLoadHistoricalData() == false && this.verifyMode == false) {

			calAlertProcessingStart = Calendar.getInstance();
			ApplicationSetting.getInstance().getStdoutwriter().writeln(
					"ALERT PROCESSING START TIME: "
							+ calAlertProcessingStart.getTime().toString(),
					Logs.STATUS1, "DG38.25");

			/*Alerts al = new Alerts();
			//TODO need to remove comments and start using Alerts
			try {
				al.checkAlerts(this);
			} catch (DataAccessException sqle) {
				ApplicationSetting.getInstance().getStdoutwriter().writeln(
						"Problem processing alerts.", Logs.ERROR, "DG11.52");
				ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
				// dbf.closeConnection();
				// return;

			} catch (PikefinException pe) {
				ApplicationSetting.getInstance().getStdoutwriter().writeln(
						"Problem processing alerts.", Logs.ERROR, "DG11.558");
				ApplicationSetting.getInstance().getStdoutwriter().writeln(pe);
				
			}*/
			calAlertProcessingEnd = Calendar.getInstance();
			ApplicationSetting.getInstance().getStdoutwriter().writeln("ALERT PROCESSING END TIME: "
					+ calAlertProcessingEnd.getTime().toString(), Logs.STATUS1,
					"DG54");
		}
		ApplicationSetting.getInstance().getStdoutwriter().wrapperNDCPop();

	}

	

	


	public String getValue(String local_data_set)
			throws IllegalStateException, TagNotFoundException, GenericException,
			CustomRegexException {
		String strDataValue = "";
		ExtractSingle es =extractSingleService.loadExtractSinglesByDataSet(local_data_set) ;
		if (es.getParsePostProcess() == true) {
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
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Initial Open Regex: "
					+ strInitialOpenUniqueRegex, Logs.STATUS2, "DG14");
			pattern = Pattern.compile(strInitialOpenUniqueRegex);
			matcher = pattern.matcher(returned_content);
			matcher.find();
			nCurOffset = matcher.start();
			ApplicationSetting.getInstance().getStdoutwriter().writeln(
					"Offset after initial regex search: " + nCurOffset,
					Logs.STATUS2, "DG15");
		}

		/*
		 * End initial regex search.
		 */

		ApplicationSetting.getInstance().getStdoutwriter().writeln("Before table searches.",
				Logs.STATUS2, "DG16");
		nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<TABLE[^>]*>)", tables, nCurOffset,returned_content);

		ApplicationSetting.getInstance().getStdoutwriter().writeln("Before table row searches.",
				Logs.STATUS2, "DG17");
		nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<tr[^>]*>)", rows, nCurOffset,returned_content);

		ApplicationSetting.getInstance().getStdoutwriter().writeln("Before table cell searches.",
				Logs.STATUS2, "DG18");
		nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<td[^>]*>)", cells, nCurOffset,returned_content);

		ApplicationSetting.getInstance().getStdoutwriter().writeln("Before div searches",
				Logs.STATUS2, "DG19");
		nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<div[^>]*>)", divs, nCurOffset,returned_content);

		String strBeforeUniqueCode = es.getBeforeUniqueCode();
		String strAfterUniqueCode = es.getAfterUniqueCode();
		strDataValue = PikefinUtil.regexSnipValue(strBeforeUniqueCode, strAfterUniqueCode,
				nCurOffset,this.returned_content);

		/*
		 * OFP 3/28/2011 - Don't remove commas if this is csv format. Let the
		 * postProcessing function handle the commas.
		 */
		if (es.getIsCSVFormat() == false)
			strDataValue = strDataValue.replace(",", "");

		strDataValue = strDataValue.replace("&nbsp;", "");

		if (strDataValue.compareTo("") != 0) {
			ApplicationSetting.getInstance().getStdoutwriter().writeln(
					"checking for negative data value", Logs.STATUS2, "DG20");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(strDataValue.substring(0, 1),
					Logs.STATUS2, "DG21");
			if (strDataValue.substring(0, 1).compareTo("(") == 0) {

				strDataValue = strDataValue.replace("(", "");
				strDataValue = "-" + strDataValue.replace(")", "");
			}

		}

		ApplicationSetting.getInstance().getStdoutwriter().writeln("Data Value: " + strDataValue,
				Logs.STATUS2, "DG24");

	
		return (strDataValue);

	}

	private ArrayList<String[]> getTableWithHeaders(String strTableSet)
			throws GenericException, TagNotFoundException,
			PrematureEndOfDataException {
		Job j = jobService.getJobByDataSet(strTableSet);
		EnhancedTable enhance=new EnhancedTable(this, strTableSet, Constants.EnhancedTableSection.ENHANCED_TABLE_BODY_SECTION, j);
		ArrayList<String[]> tabledatabody = enhance.enhancedGetTable();

		if (j.getExtractKeyColhead()!= null) {
			ArrayList<String[]> tabledatacol = new EnhancedTable(this,strTableSet,Constants.EnhancedTableSection.ENHANCED_TABLE_COL_HEAD_SECTION,j).enhancedGetTable();
			tabledatabody.add(0, tabledatacol.get(0));
		} else{
			tabledatabody.add(0, null);
		}
		

		if (j.getExtractKeyRowhead()!= null) {
			ArrayList<String[]> tabledatarow = new EnhancedTable(this,strTableSet,Constants.EnhancedTableSection.ENHANCED_TABLE_ROW_HEAD_SECTION,j).enhancedGetTable();

			String[] temp = new String[tabledatarow.size()];
			for (int i = 0; i < tabledatarow.size(); i++) {
				temp[i] = tabledatarow.get(i)[0];
			}

			tabledatabody.add(1, temp);
		}
		return (tabledatabody);
	}

	
	@SuppressWarnings("deprecation")
	private void defaultURLProcessing(String strDataSet) throws DataAccessException,
			MalformedURLException, IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {

		//String query;

		//query = "select * from jobs where data_set='" + strDataSet + "'";

		//ResultSet rs2 = dbf.db_run_query(query);
		//SqlRowSet rs2 = dbf.dbSpringRunQuery(query);
		//rs2.next();
		calJobProcessingStage1Start = Calendar.getInstance();
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;

		if (currentJob.getInputSource() == null) {
		//if ((rs2.getInt("input_source") != 0)) {
			//query = "select * from input_source where id="
			//		+ rs2.getInt("input_source");
			//ResultSet rs3 = dbf.db_run_query(query);
			//SqlRowSet rs3 = dbf.dbSpringRunQuery(query);
			//rs3.next();
			String strStProperties = currentJob.getInputSource().getFormStaticProperties();
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

			ApplicationSetting.getInstance().getStdoutwriter().writeln(
					"Retrieving URL Form: " + currentJob.getInputSource().getUrlForm(),
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

			ApplicationSetting.getInstance().getStdoutwriter().writeln("Retrieving URL: " + this.strStage2URL,
					Logs.STATUS2, "DG25");

			if (!this.strStage2URL.contains(":"))
				ApplicationSetting.getInstance().getStdoutwriter().writeln(
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
					//if (nCount == 10000 && currentTaskId == 6) {
						//Calendar cal2 = Calendar.getInstance();
						//System.out.println("b");
					//}
				}
				bDone = true;

			} catch (SocketException se) {

				if (!(this.currentTaskId == 6 || this.currentTaskId == 28)) {
					ApplicationSetting.getInstance().getStdoutwriter().writeln(
							"Issue with URL connection.", Logs.ERROR, "DG42.1");
					ApplicationSetting.getInstance().getStdoutwriter().writeln(se);
					bDone = true;
				} else {
					ApplicationSetting.getInstance().getStdoutwriter().writeln(
							"Issue with URL connection.", Logs.WARN, "DG42.3");
					ApplicationSetting.getInstance().getStdoutwriter().writeln(se.getMessage(),
							Logs.WARN, "DG42.4");
					ApplicationSetting.getInstance().getStdoutwriter()
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

		ApplicationSetting.getInstance().getStdoutwriter().writeln("Done reading url contents",
				Logs.STATUS2, "DG26");

	}

	private void getUrl(String strDataSet) throws DataAccessException,
			MalformedURLException, IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		String strCustomURLFuncName = currentJob.getCustomUrlFuncName();
		if (strCustomURLFuncName == null || strCustomURLFuncName.isEmpty()) {
			defaultURLProcessing(strDataSet);
		} else {

			Class[] args1 = new Class[1];
			args1[0] = String.class;

			Method m = this.getClass().getMethod(strCustomURLFuncName, args1);
			m.invoke(this, strDataSet);
		}

	}

	
	
	
	
	private void grabDataSet() {

		try {

			String query = " from Task t ";
			query += " join t.entityGroup eg ";
			query += " join t.metric m ";
			query += " where t.taskId=" + currentTaskId;
			int nMetricId = currentTask.getMetric().getMetricId();
			int nGroupId = ((EntityGroup)currentTask.getEntityGroups().iterator().next()).getEntityGroupId();
			int nDelay = currentTask.getDelay();
			boolean bUseGroupForReading = currentTask.isUseGroupForReading();
			boolean bTableExtraction = true;
			this.strStage1URL = currentJob.getUrlStatic();
			strCurDataSet = currentJob.getDataSet();
			ApplicationSetting.getInstance().getStdoutwriter().writeln("====> PROCESSING JOB:"
					+ strCurDataSet, Logs.STATUS1, "DG1.55");
			bTableExtraction = currentJob.isTableExtraction();

			pf.preJobProcessing(currentJob);

			if (bUseGroupForReading == false) {
				if (bTableExtraction == true) {
					try {
					pf.preProcessing(currentJob, "");
					getUrl(strCurDataSet);

						ArrayList<String[]> tabledata = getTableWithHeaders(strCurDataSet);
						ArrayList<String[]> tabledata2 = pf.postProcessing(
								tabledata, currentJob);
						if (currentJob.isCustomInsert() == false){
							factDataService.importFactDataInBatch(tabledata2, currentBatche, currentTask.getMetric());
						}
							

					} catch (MalformedURLException mue) {
						ApplicationSetting.getInstance().getStdoutwriter().writeln(
								"Badly formed url, processing dataset aborted",
								Logs.ERROR, "DG38.1");
						ApplicationSetting.getInstance().getStdoutwriter().writeln(mue);
					} catch (DataAccessException sqle) {
						ApplicationSetting.getInstance().getStdoutwriter()
								.writeln(
										"Problem issuing sql statement, processing dataset aborted",
										Logs.ERROR, "DG38.2");
						ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
					} catch (IOException ioe) {
						ApplicationSetting.getInstance().getStdoutwriter().writeln(
								"Problem with I/O, processing dataset aborted",
								Logs.ERROR, "DG38.3");
						ApplicationSetting.getInstance().getStdoutwriter().writeln(ioe);
					} catch (PrematureEndOfDataException peode) {
						ApplicationSetting.getInstance().getStdoutwriter()
								.writeln(
										"A fixed number of rows were defined for the data set but the end of the table or the end of the document was reached first.",
										Logs.ERROR, "DG38.35");
						ApplicationSetting.getInstance().getStdoutwriter().writeln(peode);
					} catch (Exception e) {
						ApplicationSetting.getInstance().getStdoutwriter().writeln(
								"Processing dataset aborted", Logs.ERROR,
								"DG38.4");
						ApplicationSetting.getInstance().getStdoutwriter().writeln(e);
					}

				}
				// Non-group, non-table extraction
				else {
					if (pf.preProcessing(currentJob, strCurrentTicker) != true) {
						throw new CustomEmptyStringException();
					}

					getUrl(strCurDataSet);

					if (pf.preNoDataCheck(strCurDataSet) == true) {
						ApplicationSetting.getInstance().getStdoutwriter().writeln(
								"URL contains no data, skipping ticker",
								Logs.STATUS1, "DG46");
						return;
					}

					String strDataValue = getValue(strCurDataSet);

					if (strDataValue.compareTo("") == 0) {
						ApplicationSetting.getInstance().getStdoutwriter().writeln(
								"Returned empty value '', skipping ",
								Logs.STATUS2, "DG47");
						return;
					}

					ArrayList<String[]> tabledata = new ArrayList<String[]>();

					String[] tmp = { strDataValue };
					tabledata.add(tmp);

					ArrayList<String[]> tabledata2 = pf.postProcessing(
							tabledata, currentJob);
		
					if (currentJob.isCustomInsert() != true) {
							factDataService.importFactDataInBatch(tabledata2, currentBatche, currentTask.getMetric());
					}

				}

			} else {

				// query = "select * from entities where groups like '%" + group
				// + "%' order by ticker";
				/*query = "select entities.* from entities,entities_entity_groups ";
				query += " where entities_entity_groups.entity_group_id="
						+ nGroupId;
				query += " AND entities_entity_groups.entity_id=entities.id ";*/
			List<Entity> entitiesList=	entityService.loadAllEntitiesForGroupId(nGroupId);
				
				String strDataValue = "";
				for (Entity entity : entitiesList) {
					try {
						this.strOriginalTicker = this.strCurrentTicker = entity.getTicker();
						this.nCurrentEntityId = entity.getEntityId();

						ApplicationSetting.getInstance().getStdoutwriter().writeln(
								"Processing ticker: " + this.strCurrentTicker,
								Logs.STATUS2, "DG39.1");

						/* Active only to debug individual tickers */
						if (strStaticTickerLimit.isEmpty() != true)
							if (strCurrentTicker
									.compareTo(strStaticTickerLimit) != 0)
								continue;
						if (bTableExtraction == true) {
							try {
								pf.preProcessing(currentJob,
										strCurrentTicker);
								getUrl(strCurDataSet);
								if (pf.preNoDataCheck(strCurDataSet) == true) {
									ApplicationSetting.getInstance().getStdoutwriter()
											.writeln(
													"URL contains no data, skipping ticker",
													Logs.STATUS1, "DG40");
									continue;
								}

								ArrayList<String[]> tabledata = getTableWithHeaders(strCurDataSet);
								ArrayList<String[]> tabledata2 = pf
										.postProcessing(tabledata,
												currentJob);
								if (currentJob.isCustomInsert() == false){
									factDataService.importFactDataInBatch(tabledata2, currentBatche, currentTask.getMetric());
								}
														
							} catch (SkipLoadException sle) {
								// This is not an error but is thrown by the
								// processing function to indicate that the load
								// method shouldn't be called
								// May want to convert over the custom insert
								// processing functions to use this exception
								ApplicationSetting.getInstance().getStdoutwriter()
										.writeln(
												"SkipLoadException thrown, skipping load",
												Logs.STATUS1, "DG40.5");
							} catch (MalformedURLException mue) {
								ApplicationSetting.getInstance().getStdoutwriter().writeln(
										"Badly formed url, skipping ticker",
										Logs.ERROR, "DG41");
								ApplicationSetting.getInstance().getStdoutwriter().writeln(mue);
							}
							catch (CustomEmptyStringException cese) {
								/*
								 * Value returned from source is empty. Only display a warning.
								 */
								ApplicationSetting.getInstance().getStdoutwriter().writeln(
										cese.getMessage(),
										Logs.WARN, "DG41.38");
							} catch (DataAccessException sqle) {
								ApplicationSetting.getInstance().getStdoutwriter()
										.writeln(
												"Problem issuing sql statement, skipping ticker",
												Logs.ERROR, "DG42");
								ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
							} catch (IOException ioe) {
								ApplicationSetting.getInstance().getStdoutwriter().writeln(
										"Problem with I/O, skipping ticker",
										Logs.ERROR, "DG43");
								ApplicationSetting.getInstance().getStdoutwriter().writeln(ioe);
							} catch (Exception ex) {
								ApplicationSetting.getInstance().getStdoutwriter().writeln(
										"Processing table for ticker "
												+ strCurrentTicker
												+ " failed, skipping",
										Logs.ERROR, "DG44");
								ApplicationSetting.getInstance().getStdoutwriter().writeln(ex);
							}

						} else
						/*
						 * Group, non-table extraction.
						 */
						{

							try {

								if (pf.preProcessing(currentJob,
										strCurrentTicker) != true) {
									throw new CustomEmptyStringException();
								}

								getUrl(strCurDataSet);

								if (pf.preNoDataCheck(strCurDataSet) == true) {
									ApplicationSetting.getInstance().getStdoutwriter()
											.writeln(
													"URL contains no data, skipping ticker",
													Logs.STATUS1, "DG46");
									continue;
								}

								strDataValue = getValue(strCurDataSet);

								if (strDataValue.compareTo("") == 0) {
									ApplicationSetting.getInstance().getStdoutwriter()
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
												currentJob);
								if (currentJob.isCustomInsert() == false){
									factDataService.importFactDataInBatch(tabledata2, currentBatche, currentTask.getMetric());
								}
									
							} catch (MalformedURLException mue) {
								ApplicationSetting.getInstance().getStdoutwriter().writeln(
										"Badly formed url, skipping ticker",
										Logs.ERROR, "DG48");
								ApplicationSetting.getInstance().getStdoutwriter().writeln(mue);
							} 
							catch (CustomEmptyStringException cese) {
								/*
								 * Value returned from source is empty. Only display a warning.
								 */
								ApplicationSetting.getInstance().getStdoutwriter().writeln(
										cese.getMessage(),
										Logs.WARN, "DG41.38");
							}
							catch (DataAccessException sqle) {
								ApplicationSetting.getInstance().getStdoutwriter()
										.writeln(
												"Problem issuing sql statement, skipping ticker",
												Logs.ERROR, "DG49");
								ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
							} catch (IOException ioe) {
								ApplicationSetting.getInstance().getStdoutwriter().writeln(
										"Problem with I/O, skipping ticker",
										Logs.ERROR, "DG50");
								ApplicationSetting.getInstance().getStdoutwriter().writeln(ioe);
							} catch (Exception ex) {
								ApplicationSetting.getInstance().getStdoutwriter().writeln(
										"Processing table for ticker "
												+ strCurrentTicker
												+ " failed, skipping",
										Logs.ERROR, "DG51");
								ApplicationSetting.getInstance().getStdoutwriter().writeln(ex);
								
							}

						}

					} catch (DataAccessException sqle) {
						ApplicationSetting.getInstance().getStdoutwriter().writeln(
								"problem with sql statement in grab_data_set",
								Logs.ERROR, "DG52");
						ApplicationSetting.getInstance().getStdoutwriter().writeln(
								"Processing of data_set " + strCurDataSet
										+ " with ticker " + strCurrentTicker
										+ " FAILED ", Logs.ERROR, "DG53");
						ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
					}

					
					/*
					 * This delay is being used as a pause between each group
					 * member of a group extraction. Can't remember why this was
					 * put it, but it's not being used currently (all values are
					 * set to 0 in the db).
					 */
					Thread.sleep(nDelay);
				}
			}

			pf.postJobProcessing(currentJob);
			ApplicationSetting.getInstance().getStdoutwriter().writeln("====> FINISHED JOB:"
					+ strCurDataSet, Logs.STATUS1, "DG1.55");
			

		} 
		catch (InvocationTargetException ite) {
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Exception in grab_data_set: " + ite.getTargetException().getMessage(),	Logs.ERROR, "DG56.83");
			//ApplicationSetting.getInstance().getStdoutwriter().writeln(ite.getTargetException());
			
		}
		
		catch (Exception e) {
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Exception in grab_data_set",
					Logs.ERROR, "DG55");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(e);
		}

	}
	
	public void customPDFURL(String strDataSet) throws DataAccessException,IOException {
		String strURL = currentJob.getUrlStatic();
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
				+ currentTaskId;
		//ResultSet rs = dbf.db_run_query(query2);
		//SqlRowSet rs = dbf.dbSpringRunQuery(query2);
		
		//rs.next();
		EntityGroup eg = (EntityGroup)currentTask.getEntityGroups().iterator().next();
		int nGroupId = eg.getEntityGroupId();
		HashSet<Entity> entities = (HashSet<Entity>)eg.getEntities();

		//String query = "select count(entities.id) as cnt from entities,entities_entity_groups ";
		//query += " where entities_entity_groups.entity_group_id=" + nGroupId;
		//query += " AND entities_entity_groups.entity_id=entities.id ";
		
		//rs = dbf.db_run_query(query);
		//rs = dbf.dbSpringRunQuery(query);
		
		//rs.next();

		int nTotalCount = eg.getEntities().size();

		//query = "select * from jobs where data_set='" + strDataSet + "'";

		//ResultSet rs2 = dbf.db_run_query(query);
		//SqlRowSet rs2 = dbf.dbSpringRunQuery(query);
		//rs2.next();

		String strURLStatic = currentJob.getUrlStatic();

		int nGroupCount = 1;
		//int nCurrentCount = 0;

		HttpResponse response;

		returned_content = "<begintag>";

		boolean bDone = false;

		while (!bDone) {

			HttpClient httpclient = new DefaultHttpClient();

			//query = "select entities.* from entities,entities_entity_groups ";
			//query += " where entities_entity_groups.entity_group_id="
				//	+ nGroupId;
			//query += " AND entities_entity_groups.entity_id=entities.id ";
			//query += " order by entities.id ";
			//query += " limit " + nGroupSize + " offset "
			///		+ (nGroupSize * (nGroupCount - 1));
			//rs = dbf.db_run_query(query);
			//rs = dbf.dbSpringRunQuery(query);

			String strList = "";
			int nOffset = nGroupSize * (nGroupCount - 1);
			for (int j=nOffset;j<nOffset + nGroupSize;j++) {
				Entity e = entities.iterator().next();
				String strTicker = e.getTicker();

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
				ApplicationSetting.getInstance().getStdoutwriter().writeln(ex);
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
			if (this.currentTaskId == 10) {

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
						ApplicationSetting.getInstance().getStdoutwriter()
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
						ApplicationSetting.getInstance().getStdoutwriter().writeln(
								"Bad Yahoo Data, Resubmitting URL", Logs.STATUS1,
								"DG55.10");
						nBegin = (nBegin - 150 < 0 ? 0 : nBegin -150);
						nEnd = (nEnd + 150 > strTemp.length() ? strTemp.length() : nEnd + 150);
						ApplicationSetting.getInstance().getStdoutwriter().writeln(strTemp.substring(nBegin,nEnd),Logs.STATUS1,"DG55.12");
						ApplicationSetting.getInstance().getStdoutwriter().writeln("Date Collected:"
								+ strDate + ",Time Collected:" + strTime,
								Logs.STATUS1, "DG55.11");
						bResubmit = true;
						break;
	
					}
	
				}
			}

			if (strTemp.length() < 300) {
				bResubmit = true;
				ApplicationSetting.getInstance().getStdoutwriter().writeln(
						"Bad Yahoo Data, Resubmitting URL", Logs.STATUS1,
						"DG55.30");
				ApplicationSetting.getInstance().getStdoutwriter().writeln("Data length only "
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

	public Task getCurrentTask() {
		return currentTask;
	}

	

	public Schedule getSchedule() {
		return schedule;
	}

	

	public RepeatType getRepeatType() {
		return repeatType;
	}

	public Job getCurrentJob() {
		return currentJob;
	}

	public Batches getCurrentBatche() {
		return currentBatche;
	}

	public boolean isVerifyMode() {
		return verifyMode;
	}

	
	

}
