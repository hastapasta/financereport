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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import org.springframework.dao.DataAccessException;
import com.pikefin.ErrorCode;
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
import com.pikefin.exceptions.CustomRegexException;
import com.pikefin.exceptions.GenericException;
import com.pikefin.exceptions.PrematureEndOfDataException;
import com.pikefin.exceptions.SkipLoadException;
import com.pikefin.exceptions.TagNotFoundException;
import com.pikefin.services.inter.AlertService;
import com.pikefin.services.inter.BatchService;
import com.pikefin.services.inter.EntityService;
import com.pikefin.services.inter.ExtractSingleService;
import com.pikefin.services.inter.FactDataService;
import com.pikefin.services.inter.JobService;

public class DataGrabExecutor extends Thread {
	Logger log=Logger.getLogger(DataGrabExecutor.class);
	//@Autowired
	private BatchService batchService = ApplicationSetting.getInstance()
	    .getApplicationContext().getBean(BatchService.class);
	//@Autowired
	private JobService jobService = ApplicationSetting.getInstance()
	    .getApplicationContext().getBean(JobService.class);
	//@Autowired
	private ExtractSingleService extractSingleService=
	    ApplicationSetting.getInstance()
	    .getApplicationContext().getBean(ExtractSingleService.class);
	//@Autowired
	private EntityService entityService = ApplicationSetting.getInstance()
	    .getApplicationContext().getBean(EntityService.class);
	//	@Autowired
	private FactDataService factDataService = ApplicationSetting.getInstance()
	    .getApplicationContext().getBean(FactDataService.class);
	//	@Autowired
	private AlertService alertService = ApplicationSetting.getInstance()
	    .getApplicationContext().getBean(AlertService.class);
	String returned_content;
	String strCurDataSet;
	private Job currentJob;
	private Task currentTask;
	private Schedule schedule;
	private RepeatType repeatType;
	private int currentTaskId;
	private Batches currentBatch;
	private int priority;
	private int batchId;
	
	private String strStage1URL;
	String strStage2URL;
	
	// boolean for verify mode. Loads data and skips alert processing.
	private boolean verifyMode;
	int nJobBatch;
		
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

	//@Autowired
	ProcessingFunctions pf;

	public DataGrabExecutor(Task task, int batchId, Schedule schedule, 
	    RepeatType repeatType, boolean verifyMode, int priority) {
	  pf=new ProcessingFunctions(this);
	  this.priority = priority;
	  this.currentTaskId = task.getTaskId();
	  this.schedule = schedule;
	  this.repeatType = repeatType;
	  this.verifyMode = verifyMode;


	  try	{
	    if (batchId == 0){
	      Batches batchesEntity=new Batches();
	      batchesEntity.setBatchDateCollected(new Date());
	      batchesEntity.setBatchTask(task);
	      batchesEntity.setRandomUnique(ApplicationSetting
	          .getInstance().getUniqueRandom());

	      batchesEntity=batchService.saveBatchesInfo(batchesEntity);
	      this.batchId =batchesEntity.getBatchId();
	      currentBatch=batchesEntity;
	    }
	    else{
	      currentBatch=batchService.loadBatchesInfo(batchId);
	      this.batchId = batchId;
	    }
	    this.currentTask = task;
	  }
	  catch (GenericException sqle) {
	    ApplicationSetting.getInstance().getStdoutwriter()
	    .writeln("Problem retreiving list of jobs. Aborting task.",
	        Logs.ERROR,"DG12.5");
	    ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
	    return;
	  }
	  strCurrentTicker = "";
	  calJobProcessingStart = Calendar.getInstance();
	}
	
	public Calendar getTaskMetric(TaskMetricsEnum tm) throws GenericException {
	  switch (tm) {
	  case JOB_START:
	    if (calJobProcessingStart != null) {
	      return(calJobProcessingStart);
	    }
	    else {
	      throw new GenericException(
	          ErrorCode.COULD_NOT_FIND_JOB_PROCESSING_START_TIME,
	          "calJobProcessingStart value is null",null);
	    }
	    //break;

	  case JOB_END:
	    if (calJobProcessingEnd != null) {
	      return(calJobProcessingEnd);
	    }
	    else {
	      throw new GenericException(
	          ErrorCode.COULD_NOT_FIND_JOB_PROCESSING_END_TIME,
	          "calJobProcessingEnd value is null",null);
	    }
	    //break;
	      
	  case ALERT_START:
	    // We aren't processing alerts in verify mode.
	    if (this.verifyMode == false) {
	      if (calAlertProcessingStart != null) {
	        return(calAlertProcessingStart);
	      }
	      else {
	        throw new GenericException(
	            ErrorCode.COULD_NOT_FIND_JOB_ALERT_START_TIME,
	            "calAlertProcessingStart value is null",null);
	      }
	    }
	    else {
	      return(null);
	    }
	    //break;
	    
	  case ALERT_END:
	    if (this.verifyMode == false) {
	      if (calAlertProcessingEnd != null) {
	        return(calAlertProcessingEnd);
	      }
	      else {
	        throw new GenericException(
	            ErrorCode.COULD_NOT_FIND_JOB_ALERT_END_TIME,
	            "calAlertProcessingEnd value is null",null);
	      }
	    }
	    else {
	      return(null);
	    }
	    //break;

	  // The Stage performance markers are only used for default url processing.
	  //
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
	}

	public void run() {
		ApplicationSetting.getInstance().getStdoutwriter()
		  .wrapperNDCPush("[Task:" + this.currentTask.getTaskId() + "]");
		ApplicationSetting.getInstance().getStdoutwriter()
		  .writeln("=========================================================",
				Logs.STATUS1, "");
		ApplicationSetting.getInstance().getStdoutwriter()
		  .writeln("PROCESSING TASK " + this.getCurrentTask().getTaskId(),
				Logs.STATUS1, "DG37");
		if (bContinue == false) {
			ApplicationSetting.getInstance().getStdoutwriter().wrapperNDCPop();
			return;
		}
		if (this.verifyMode == true) {
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("SCHEDULE RUNNING IN VERIFY MODE!", Logs.STATUS1,
					"DG38.95");
			ApplicationSetting.getInstance().getStdoutwriter()
			  .wrapperNDCPush("[Verify Mode]");
		}
		
		ApplicationSetting.getInstance().getStdoutwriter()
		  .writeln("JOB PROCESSING START TIME: "
				+ calJobProcessingStart.getTime().toString(), Logs.STATUS1,
				"DG39");
		ApplicationSetting.getInstance().getStdoutwriter().writeln("INITIATING THREAD",
				Logs.STATUS1, "DG1");
	
		Set<Job> tmpJobs = currentTask.getJobs();
		for (Job j : tmpJobs) {
			currentJob = j;
			grabDataSet();
		}
		
		calJobProcessingEnd = Calendar.getInstance();
		ApplicationSetting.getInstance().getStdoutwriter()
		  .writeln("JOB PROCESSING END TIME: "
				+ calJobProcessingEnd.getTime().toString(), Logs.STATUS1,
				"DG38.15");
		if (ApplicationSetting.getInstance().isLoadHistoricalData() == true)
			ApplicationSetting.getInstance().getStdoutwriter().wrapperNDCPop();
		
		if (this.verifyMode == true)
			ApplicationSetting.getInstance().getStdoutwriter().wrapperNDCPop();
	
		// Don't process alerts if loading historical data or in verfiy mode where we just 
		// verify the data collection process.
		if (ApplicationSetting.getInstance()
		    .isLoadHistoricalData() == false && this.verifyMode == false) {
		  calAlertProcessingStart = Calendar.getInstance();
		  ApplicationSetting.getInstance().getStdoutwriter().writeln(
		      "ALERT PROCESSING START TIME: "
		          + calAlertProcessingStart.getTime().toString(),
		          Logs.STATUS1, "DG38.25");

		  try {
		    if (this.verifyMode != true){
		      alertService.checkAlerts(this);
		    }
		  } 
		  catch (GenericException pe) {
		    ApplicationSetting.getInstance().getStdoutwriter()
		      .writeln("Problem processing alerts.", Logs.ERROR, "DG11.558");
		    ApplicationSetting.getInstance().getStdoutwriter().writeln(pe);
		  }
		  calAlertProcessingEnd = Calendar.getInstance();
		  ApplicationSetting.getInstance().getStdoutwriter()
		    .writeln("ALERT PROCESSING END TIME: "
		      + calAlertProcessingEnd.getTime().toString(), Logs.STATUS1,
		      "DG54");
		}
		ApplicationSetting.getInstance().getStdoutwriter().wrapperNDCPop();
	}

	public String getValue(String local_data_set)
			throws IllegalStateException, TagNotFoundException, GenericException,
			CustomRegexException {
		String strDataValue = "";
		ExtractSingle es =extractSingleService
		    .loadExtractSinglesByDataSet(local_data_set) ;
		if (es.getParsePostProcess() == true) {
			//We do the parsing in the post process function
			return(this.returned_content);
		}

		int tables, cells, rows, divs;

		// This next line will throw an exception if you meant to do a table
		// extraction but forgot to set the table_extraction flag in the db.
		tables = es.getTableCount();
		cells = es.getCellCount();
		rows = es.getRowCount();
		divs = es.getDivCount();

		int nCurOffset = 0;

		// Initial regex search.

		String strInitialOpenUniqueCode =es.getInitialBefUniqueCode();
		Pattern pattern;
		Matcher matcher;
		if ((strInitialOpenUniqueCode != null)
				&& (strInitialOpenUniqueCode.isEmpty() == false)) {
			String strInitialOpenUniqueRegex = "(?i)("
					+ strInitialOpenUniqueCode + ")";
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("Initial Open Regex: "
					+ strInitialOpenUniqueRegex, Logs.STATUS2, "DG14");
			pattern = Pattern.compile(strInitialOpenUniqueRegex);
			matcher = pattern.matcher(returned_content);
			matcher.find();
			nCurOffset = matcher.start();
			ApplicationSetting.getInstance().getStdoutwriter().writeln(
					"Offset after initial regex search: " + nCurOffset,
					Logs.STATUS2, "DG15");
		}

		// End initial regex search.
		ApplicationSetting.getInstance().getStdoutwriter()
		  .writeln("Before table searches.",
				Logs.STATUS2, "DG16");
		nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<TABLE[^>]*>)", tables,
		    nCurOffset,returned_content);

		ApplicationSetting.getInstance().getStdoutwriter()
		  .writeln("Before table row searches.",
				Logs.STATUS2, "DG17");
		nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<tr[^>]*>)", 
		    rows, nCurOffset,returned_content);

		ApplicationSetting.getInstance().getStdoutwriter()
		  .writeln("Before table cell searches.", Logs.STATUS2, "DG18");
		nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<td[^>]*>)",
		    cells, nCurOffset,returned_content);

		ApplicationSetting.getInstance().getStdoutwriter()
		  .writeln("Before div searches",
				Logs.STATUS2, "DG19");
		nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<div[^>]*>)", divs,
		    nCurOffset,returned_content);

		String strBeforeUniqueCode = es.getBeforeUniqueCode();
		String strAfterUniqueCode = es.getAfterUniqueCode();
		strDataValue = PikefinUtil
		    .regexSnipValue(strBeforeUniqueCode, strAfterUniqueCode,
				nCurOffset,this.returned_content);
		
		// OFP 3/28/2011 - Don't remove commas if this is csv format. Let the
		// postProcessing function handle the commas.
		if (es.getIsCSVFormat() == false) {
			strDataValue = strDataValue.replace(",", "");
		}

		strDataValue = strDataValue.replace("&nbsp;", "");

		if (strDataValue.compareTo("") != 0) {
			ApplicationSetting.getInstance().getStdoutwriter().writeln(
					"checking for negative data value", Logs.STATUS2, "DG20");
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln(strDataValue.substring(0, 1),
					Logs.STATUS2, "DG21");
			if (strDataValue.substring(0, 1).compareTo("(") == 0) {
				strDataValue = strDataValue.replace("(", "");
				strDataValue = "-" + strDataValue.replace(")", "");
			}
		}

		ApplicationSetting.getInstance().getStdoutwriter()
		  .writeln("Data Value: " + strDataValue,
				Logs.STATUS2, "DG24");
		
		return (strDataValue);
	}

	private ArrayList<String[]> getTableWithHeaders(String strTableSet)
			throws GenericException, TagNotFoundException, 
			PrematureEndOfDataException {
		Job j = jobService.getJobByDataSet(strTableSet);

		EnhancedTable enhance=new EnhancedTable(this, strTableSet, 
		    Constants.EnhancedTableSection.ENHANCED_TABLE_BODY_SECTION, j);
		ArrayList<String[]> tabledatabody = enhance.enhancedGetTable();

		if (j.getExtractKeyColhead()!= null) {
			ArrayList<String[]> tabledatacol = new EnhancedTable(this,
			    strTableSet,Constants.EnhancedTableSection
			    .ENHANCED_TABLE_COL_HEAD_SECTION,j).enhancedGetTable();
			tabledatabody.add(0, tabledatacol.get(0));
		} else {
			tabledatabody.add(0, null);
		}
		
		if (j.getExtractKeyRowhead()!= null) {
			ArrayList<String[]> tabledatarow = new EnhancedTable(this,
			    strTableSet,Constants.EnhancedTableSection.
			    ENHANCED_TABLE_ROW_HEAD_SECTION,j).enhancedGetTable();

			String[] temp = new String[tabledatarow.size()];
			for (int i = 0; i < tabledatarow.size(); i++) {
				temp[i] = tabledatarow.get(i)[0];
			}
			tabledatabody.add(1, temp);
		}
		return (tabledatabody);
	}
	
	@SuppressWarnings("deprecation")
	private void defaultURLProcessing(String strDataSet) 
	    throws DataAccessException, MalformedURLException, IOException,
	    IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	  
		calJobProcessingStage1Start = Calendar.getInstance();
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;

		if (currentJob.getInputSource() != null) {
			// TODO Commenting the below code because it will trough 
		  // the Null pointer exception since inputSource is already null and we 
		  // can't get the getFormStaticProperties from null
			 String strStProperties = currentJob.getInputSource()
		    .getFormStaticProperties();
			 String[] listItems = strStProperties.split(":");
			 for (int i = 0; i < listItems.length; i++) {
			 }

			// ApplicationSetting.getInstance().getStdoutwriter().writeln(
			//		"Retrieving URL Form: " + currentJob.getInputSource().getUrlForm(),
			//		Logs.STATUS2, "DG24.5");*/

			HttpPost httppost = new HttpPost(
					Constants.Urls.SURVEY_MOST_URL);
			
			// Emulate a browser.
			httppost.getParams().setParameter(Constants.HttpSetting.PARAM_USER_AGENT,
					Constants.HttpSetting.BROWSER_MOZILA);
			
			// The following line fixes an issue where a non-fatal error is
			// displayed about an invalid cookie data format.
			httppost.getParams().setParameter(
			    Constants.HttpSetting.PARAM_COKKIE_DATE_PATTERN,
			    Arrays.asList(Constants.HttpSetting.COKKIE_DATE_PATTERN_1,
			        Constants.HttpSetting.COKKIE_DATE_PATTERN_2));

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("series_id", "LNS14000000"));
			nvps.add(new BasicNameValuePair("survey", "ln"));
			httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			response = httpclient.execute(httppost);
			ApplicationSetting.getInstance().getStdoutwriter().writeln(
			    "START LOADING URL  -"+Constants.Urls.SURVEY_MOST_URL,
		      Logs.STATUS2, "DG24.59");
		}
		else {
		  this.strStage2URL = this.strStage1URL.replace("${ticker}",
		      this.strCurrentTicker);
		  ApplicationSetting.getInstance().getStdoutwriter()
		    .writeln("Retrieving URL: " + this.strStage2URL,Logs.STATUS2, "DG25");

		  if (!this.strStage2URL.contains(":")) {
		    ApplicationSetting.getInstance().getStdoutwriter().writeln(
		        "WARNING: url is not preceeded with a protocol"
		            + this.strStage2URL, Logs.STATUS1, "DG25.5");
		  }

		  // HttpGet chokes on the ^ character
		  this.strStage2URL = this.strStage2URL.replace("^", "%5E");
		  this.strStage2URL = this.strStage2URL.replace("|", "%7C");

		  HttpGet httpget = new HttpGet(this.strStage2URL);
		  ApplicationSetting.getInstance().getStdoutwriter().writeln(
		      "START LOADING URL  -" + this.strStage2URL,
		      Logs.STATUS2, "DG24.59");

		  // Emulate a browser.
		  httpget.getParams().setParameter(Constants
		      .HttpSetting.PARAM_USER_AGENT,Constants.HttpSetting.BROWSER_MOZILA);

		  // The following line fixes an issue where a non-fatal error is
		  // displayed about an invalid cookie data format. It turns out that
		  // some sites generate a warning with this code, and others without
		  // it. I'm going to kludge this for now until I get more data on
		  // which urls throw the warning and which don't.
		  // 
		  // warning with code: www.exchange-rates.org
			if (!(strCurDataSet.contains("xrateorg")
					|| strCurDataSet.contains("google") || strCurDataSet
					.contains("mwatch"))) {
				httpget.getParams().setParameter(
				    Constants.HttpSetting.PARAM_COKKIE_DATE_PATTERN,
						Arrays.asList(Constants.HttpSetting.COKKIE_DATE_PATTERN_1,
						    Constants.HttpSetting.COKKIE_DATE_PATTERN_2));
			}

			// OFP 3/12/2011 - Need to keep an eye on this next line. There was
			// an issue where because of a bug in DataLoad initiateJob() the
			// same task showed up in the running queue and there was a thread
			// lock on this line. Not exactly sure if this method is thread
			// safe. I wouldn't think that it's thread-safedness (?) would
			// depend on if you are issuing the same url, i.e. I would think
			// that thread locks would occur even with different urls.

			// OFP 3/15/2011 - Had another thread lock with one thread on this
			// line: response = httpclient.execute(httpget); and another thread
			// on this line: while ((nTmp = in.read()) != -1)
			// 
			// Added the shutdown() call.
			response = httpclient.execute(httpget);
		}

		HttpEntity entity = response.getEntity();
		calJobProcessingStage1End = Calendar.getInstance();
		calJobProcessingStage2Start = Calendar.getInstance();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				entity.getContent()));
		int nTmp;
		returned_content = "";
		
	  // OFP 9/15/2011 - Added workaround for connection reset exception for
		// bloomberg index tasks.
	  StringBuilder sb = new StringBuilder();
		boolean bDone = false;
		while (!bDone) {
			try {
				while ((nTmp = in.read()) != -1) {
				  sb.append((char)nTmp);
				}
				bDone = true;
			}
			catch (SocketException se) {
				if (!(this.currentTaskId == 6 || this.currentTaskId == 28)) {
					ApplicationSetting.getInstance().getStdoutwriter().writeln(
							"Issue with URL connection.", Logs.ERROR, "DG42.1");
					ApplicationSetting.getInstance().getStdoutwriter().writeln(se);
					bDone = true;
				}
				else {
					ApplicationSetting.getInstance().getStdoutwriter().writeln(
							"Issue with URL connection.", Logs.WARN, "DG42.3");
					ApplicationSetting.getInstance().getStdoutwriter()
					  .writeln(se.getMessage(), Logs.WARN, "DG42.4");
					ApplicationSetting.getInstance().getStdoutwriter()
							.writeln("Processing one of the bloomberg tasks," +
									" resubmitting URL", Logs.WARN, "DG42.2");
					sb = new StringBuilder();
				}
			}
		}
		returned_content = sb.toString();
		
		in.close();
		/*if(log.isDebugEnabled()){
			log.debug("\n\n#################################Loaded the content from URL--"+strStage2URL+"#####################################\n\n"+returned_content);
		}*/
		calJobProcessingStage2End = Calendar.getInstance();
		httpclient.getConnectionManager().shutdown();
		ApplicationSetting.getInstance().getStdoutwriter()
		  .writeln("Done reading url contents",	Logs.STATUS2, "DG26");
	}

	private void getUrl(String strDataSet) 
	    throws DataAccessException, MalformedURLException, IOException,
	    IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	  String strCustomURLFuncName = currentJob.getCustomUrlFuncName();
	  if (strCustomURLFuncName == null || strCustomURLFuncName.isEmpty()) {
	    defaultURLProcessing(strDataSet);
	  } else {
	    Class[] args1 = new Class[1];
	    args1[0] = String.class;
	    Method m = this.getClass().getMethod(strCustomURLFuncName, args1);
	    log.debug("INVOKING METHOD-"+strCustomURLFuncName +" FROM GETURL ");
	    m.invoke(this, strDataSet);
	  }
	}
	
	private void grabDataSet() {

	  try {
	    
	    int nGroupId = ((EntityGroup)currentTask.getEntityGroups()
	        .iterator().next()).getEntityGroupId();
	    int nDelay = currentTask.getDelay();
	    boolean bUseGroupForReading = currentTask.isUseGroupForReading();
	    boolean bTableExtraction = true;
	    this.strStage1URL = currentJob.getUrlStatic();
	    strCurDataSet = currentJob.getDataSet();
	    ApplicationSetting.getInstance().getStdoutwriter()
	      .writeln("====> PROCESSING JOB:" + strCurDataSet, 
	          Logs.STATUS1, "DG1.55");
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
	            factDataService.importFactDataInBatch(tabledata2, 
	                currentBatch, currentTask.getMetric());
	          }
	        }
	        catch (MalformedURLException mue) {
	          ApplicationSetting.getInstance().getStdoutwriter().writeln(
	              "Badly formed url, processing dataset aborted",
	              Logs.ERROR, "DG38.1");
	          ApplicationSetting.getInstance().getStdoutwriter().writeln(mue);
	        }
	        catch (DataAccessException sqle) {
	          ApplicationSetting.getInstance().getStdoutwriter()
	          .writeln(
	              "Problem issuing sql statement, processing dataset aborted",
	              Logs.ERROR, "DG38.2");
	          ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
	        }
	        catch (IOException ioe) {
	          ApplicationSetting.getInstance().getStdoutwriter().writeln(
	              "Problem with I/O, processing dataset aborted",
	              Logs.ERROR, "DG38.3");
	          ApplicationSetting.getInstance().getStdoutwriter().writeln(ioe);
	        }
	        catch (PrematureEndOfDataException peode) {
	          ApplicationSetting.getInstance().getStdoutwriter()
	          .writeln(
	              "A fixed number of rows were defined for the data set but " +
	              "the end of the table or the end of the document was" +
	              " reached first.",Logs.ERROR, "DG38.35");
	          ApplicationSetting.getInstance().getStdoutwriter().writeln(peode);
	        }
	        catch (Exception e) {
	          ApplicationSetting.getInstance().getStdoutwriter().writeln(
	              "Processing dataset aborted", Logs.ERROR, "DG38.4");
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

					ArrayList<String[]> tabledata2 = 
					    pf.postProcessing(tabledata, currentJob);
		
					if (currentJob.isCustomInsert() != true) {
							factDataService.importFactDataInBatch(tabledata2, 
							    currentBatch, currentTask.getMetric());
					}
				}
			}
	    else {
	      List<Entity> entitiesList =	
	          entityService.loadAllEntitiesForGroupId(nGroupId);

	      String strDataValue = "";
	      for (Entity entity : entitiesList) {
	        try {
	          this.strOriginalTicker = this.strCurrentTicker = entity.getTicker();
	          this.nCurrentEntityId = entity.getEntityId();

	          ApplicationSetting.getInstance().getStdoutwriter().writeln(
	              "Processing ticker: " + this.strCurrentTicker,
	              Logs.STATUS2, "DG39.1");

	          // Active only to debug individual tickers
	          if (strStaticTickerLimit.isEmpty() != true) {
	            if (strCurrentTicker.compareTo(strStaticTickerLimit) != 0) {
	              continue;
	            }
	          }
	          if (bTableExtraction == true) {
	            try {
	              pf.preProcessing(currentJob, strCurrentTicker);
	              getUrl(strCurDataSet);
	              
	              if (pf.preNoDataCheck(strCurDataSet) == true) {
	                ApplicationSetting.getInstance().getStdoutwriter()
	                .writeln("URL contains no data, skipping ticker",
	                    Logs.STATUS1, "DG40");
	                continue;
	              }

	              ArrayList<String[]> tabledata
	                = getTableWithHeaders(strCurDataSet);
	              ArrayList<String[]> tabledata2
	                = pf.postProcessing(tabledata,currentJob);
	              if (currentJob.isCustomInsert() == false){
	                factDataService.importFactDataInBatch(tabledata2, 
	                    currentBatch, currentTask.getMetric());
	              }
	            }
	            catch (SkipLoadException sle) {
	              // This is not an error but is thrown by the
	              // processing function to indicate that the load
	              // method shouldn't be called
	              // May want to convert over the custom insert
	              // processing functions to use this exception
	              ApplicationSetting.getInstance().getStdoutwriter()
	                .writeln("SkipLoadException thrown, skipping load",
	                    Logs.STATUS1, "DG40.5");
	            }
	            catch (MalformedURLException mue) {
	              ApplicationSetting.getInstance().getStdoutwriter()
	                .writeln("Badly formed url, skipping ticker",
	                  Logs.ERROR, "DG41");
	              ApplicationSetting.getInstance().getStdoutwriter().writeln(mue);
	            }
	            catch (CustomEmptyStringException cese) {
	              // Value returned from source is empty. Only display a warning.
	             	ApplicationSetting.getInstance().getStdoutwriter()
	             	  .writeln(cese.getMessage(), Logs.WARN, "DG41.38");
	            } 
	            catch (DataAccessException sqle) {
	              ApplicationSetting.getInstance().getStdoutwriter()
	              .writeln( "Problem issuing sql statement, skipping ticker",
	                  Logs.ERROR, "DG42");
	              ApplicationSetting.getInstance().getStdoutwriter()
	                .writeln(sqle);
	            }
	            catch (IOException ioe) {
	              ApplicationSetting.getInstance().getStdoutwriter()
	                .writeln("Problem with I/O, skipping ticker",
	                    Logs.ERROR, "DG43");
	              ApplicationSetting.getInstance().getStdoutwriter().writeln(ioe);
	            }
	            catch (Exception ex) {
	              ApplicationSetting.getInstance().getStdoutwriter()
	                .writeln("Processing table for ticker " + strCurrentTicker
	                      + " failed, skipping", Logs.ERROR, "DG44");
	              ApplicationSetting.getInstance().getStdoutwriter().writeln(ex);
	            }
	          }
	          else {
            // Group, non-table extraction.
	            try {
	              if (pf.preProcessing(currentJob, strCurrentTicker) != true) {
	                throw new CustomEmptyStringException();
	              }

	              getUrl(strCurDataSet);

	              if (pf.preNoDataCheck(strCurDataSet) == true) {
	                ApplicationSetting.getInstance().getStdoutwriter()
	                .writeln("URL contains no data, skipping ticker",
	                    Logs.STATUS1, "DG46");
	                continue;
	              }

	              strDataValue = getValue(strCurDataSet);

	              if (strDataValue.compareTo("") == 0) {
	                ApplicationSetting.getInstance().getStdoutwriter()
	                .writeln("Returned empty value '', skipping ",
	                    Logs.STATUS2, "DG47");
	                continue;
	              }

	              ArrayList<String[]> tabledata = new ArrayList<String[]>();
	              String[] tmp = { strDataValue };
	              
	              // Create a table even for single values since
	              // we now use importTableIntoDB for everything.

	              tabledata.add(tmp);

	              ArrayList<String[]> tabledata2 
	                = pf.postProcessing(tabledata,currentJob);
	              
	              if (currentJob.isCustomInsert() == false){
	                factDataService.importFactDataInBatch(tabledata2, 
	                    currentBatch, currentTask.getMetric());
	              }

	            }
	            catch (MalformedURLException mue) {
	              ApplicationSetting.getInstance().getStdoutwriter().writeln(
	                  "Badly formed url, skipping ticker",
	                  Logs.ERROR, "DG48");
	              ApplicationSetting.getInstance().getStdoutwriter().writeln(mue);
	            } 
	            catch (CustomEmptyStringException cese) {
	              // Value returned from source is empty. Only display a warning.
	              ApplicationSetting.getInstance().getStdoutwriter().writeln(
	                  cese.getMessage(), Logs.WARN, "DG41.38");
							}
							catch (DataAccessException sqle) {
								ApplicationSetting.getInstance().getStdoutwriter()
										.writeln("Problem issuing sql statement, skipping ticker",
												Logs.ERROR, "DG49");
								ApplicationSetting.getInstance().getStdoutwriter()
								.writeln(sqle);
							}
	            catch (IOException ioe) {
								ApplicationSetting.getInstance().getStdoutwriter()
								  .writeln("Problem with I/O, skipping ticker",
								      Logs.ERROR, "DG50");
								ApplicationSetting.getInstance().getStdoutwriter().writeln(ioe);
							}
	            catch (Exception ex) {
								ApplicationSetting.getInstance().getStdoutwriter()
								  .writeln("Processing table for ticker " + strCurrentTicker
								      + " failed, skipping",Logs.ERROR, "DG51");
								ApplicationSetting.getInstance().getStdoutwriter().writeln(ex);
	            }
						}
					}
	        catch (DataAccessException sqle) {
						ApplicationSetting.getInstance().getStdoutwriter()
						  .writeln("problem with sql statement in grab_data_set",
								Logs.ERROR, "DG52");
						ApplicationSetting.getInstance().getStdoutwriter()
						  .writeln("Processing of data_set " + strCurDataSet
										+ " with ticker " + strCurrentTicker
										+ " FAILED ", Logs.ERROR, "DG53");
						ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
					}

	        // This delay is being used as a pause between each group
					// member of a group extraction. Can't remember why this was
					// put it, but it's not being used currently (all values are
					// set to 0 in the db).
					//Thread.sleep(nDelay);
				}
			}
			pf.postJobProcessing(currentJob);
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("====> FINISHED JOB:" + strCurDataSet, Logs.STATUS1, "DG1.55");
		} 
		catch (InvocationTargetException ite) {
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("Exception in grab_data_set: "
			      + ite.getTargetException().getMessage(),	Logs.ERROR, "DG56.83");
		}
		catch (Exception e) {
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("Exception in grab_data_set", Logs.ERROR, "DG55");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(e);
		}
	}
	
	public void customPDFURL(String strDataSet) 
	    throws DataAccessException,IOException {
		String strURL = currentJob.getUrlStatic();
		URL url = new URL(strURL);
		PDDocument pd2 = PDDocument.load(url, true);
		
		PDFTextStripper stripper = new PDFText2HTML("utf-8");
		stripper.setStartPage(1);
		stripper.setEndPage(1);
		returned_content = stripper.getText(pd2);
	}

	// function is called dynamically 
	// It has to be public or the dynamic invocation doesn't work.
	public void customYahooBulkURL(String strDataSet)
	    throws DataAccessException, IOException {

		// Get a list of all the tickers
		int nGroupSize = 100;
		EntityGroup eg 
		  = (EntityGroup)currentTask.getEntityGroups().iterator().next();
		Set<Entity> entities = eg.getEntities();
		int nTotalCount = eg.getEntities().size();
		String strURLStatic = currentJob.getUrlStatic();
		int nGroupCount = 1;
		HttpResponse response;
		returned_content = "<begintag>";
		boolean bDone = false;
		Iterator<Entity> i = entities.iterator();
		while (!bDone) {
			HttpClient httpclient = new DefaultHttpClient();
			String strList = "";
			int nOffset = nGroupSize * (nGroupCount - 1);
			for (int j=nOffset;j<nOffset + nGroupSize && i.hasNext();j++) {
				Entity e = i.next();
				String strTicker = e.getTicker();

				if (Constants.Ticker.TICKER_BF_BY_B.equals(strTicker)) {
					strTicker = Constants.Ticker.TICKER_BF_B;
				} else if (Constants.Ticker.TICKER_BRK_BY_A.equals(strTicker)) {
					strTicker =Constants.Ticker.TICKER_BRK_A;
				}
				strList += strTicker + ",";
			}
			strList = strList.substring(0, strList.length() - 1);
			String strURL = strURLStatic.replace("${ticker}", strList);
			HttpGet httpget = null;
			try {
				strURL = strURL.replace("^", "%5E");
				httpget = new HttpGet(strURL);
			} 
			catch (Exception ex) {
				ApplicationSetting.getInstance().getStdoutwriter().writeln(ex);
			}

			httpget.getParams().setParameter(				
					Constants.HttpSetting.PARAM_COKKIE_DATE_PATTERN,
					Arrays.asList(Constants.HttpSetting.COKKIE_DATE_PATTERN_1,
					    Constants.HttpSetting.COKKIE_DATE_PATTERN_2));

			httpget.getParams().setParameter(
			    Constants.HttpSetting.PARAM_USER_AGENT,
			    Constants.HttpSetting.BROWSER_MOZILA);

			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			BufferedReader in 
			  = new BufferedReader(new InputStreamReader(entity.getContent()));
			int nTmp;
			String strTemp = "";
			while ((nTmp = in.read()) != -1) {
				strTemp += (char) nTmp;
			}
			in.close();
			httpclient.getConnectionManager().shutdown();
			boolean bDone2 = false;
			int nBegin = 0;
			int nEnd = 0;
			Calendar cal = Calendar.getInstance();
			boolean bResubmit = false;
			
			// Right now only perform this check on task 10.
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
					  // Technically we should never get here since it means a
						// lasttradedate tag without a lasttradetime tag.
						ApplicationSetting.getInstance().getStdoutwriter()
								.writeln("Issue processing yahoo data. Lasttradedate tage " +
										"without lasttradetime tag.", Logs.ERROR, "DG60.1");
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
	
					if (strPeriod.equals("pm") && (nHour != 12)) {
						nHour += 12;
					}
					else if (strPeriod.equals("am") && (nHour == 12)) {
						nHour = 0;
					}
	
					String[] datearray = strDate.split("/");
					Calendar cal2 = Calendar.getInstance();
					cal2.setTimeZone(TimeZone.getTimeZone("America/New_York"));
	
					// OFP 9/26/2011 I'm using the following method because with the
					// indivdual set() methods, i.e. cal2.set(Calendar.HOUR,x);, the
					// logic is not absolute, meaning that what the hour 12 means is
					// dependent on what AM_PM is currently set to. Also the day can
					// get flipped over, which is also behavior I don't want.
					 
					cal2.set(Integer.parseInt(datearray[2]),
							Integer.parseInt(datearray[0]) - 1,
							Integer.parseInt(datearray[1]), nHour,
							Integer.parseInt(strMinute), 0);
	
					// Check if time difference is more than an hour
					if (Math.abs(cal2.getTimeInMillis() - cal.getTimeInMillis())
					    > 3600000*2*24) {
						ApplicationSetting.getInstance().getStdoutwriter()
						  .writeln("Bad Yahoo Data, Resubmitting URL", Logs.STATUS1,
								"DG55.10");
						nBegin = (nBegin - 150 < 0 ? 0 : nBegin -150);
						nEnd = (nEnd + 150 > strTemp.length() ? 
						    strTemp.length() : nEnd + 150);
						ApplicationSetting.getInstance().getStdoutwriter()
						  .writeln(strTemp.substring(nBegin,nEnd),Logs.STATUS1,"DG55.12");
						ApplicationSetting.getInstance().getStdoutwriter()
						  .writeln("Date Collected:" + strDate + ",Time Collected:"
						      + strTime, Logs.STATUS1, "DG55.11");
						bResubmit = true;
						break;
					}
				}
			}
			if (strTemp.length() < 300) {
				bResubmit = true;
				ApplicationSetting.getInstance().getStdoutwriter()
				  .writeln("Bad Yahoo Data, Resubmitting URL", Logs.STATUS1,"DG55.30");
				ApplicationSetting.getInstance().getStdoutwriter()
				  .writeln("Data length only " + strTemp.length(),
				      Logs.STATUS1, "DG55.66");
			}
			if (bResubmit == true) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException ie) {
				  // TODO Need to put something here
				}
				continue;
			}
			
			returned_content += strTemp;
			
			if ((nGroupCount * nGroupSize) >= nTotalCount) {
				bDone = true;
			}
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

	public Batches getCurrentBatch() {
		return currentBatch;
	}

	public boolean isVerifyMode() {
		return verifyMode;
	}

	public String getStrStage1URL() {
		return strStage1URL;
	}

	public void setStrStage1URL(String strStage1URL) {
		this.strStage1URL = strStage1URL;
	}

}
