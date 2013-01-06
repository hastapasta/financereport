package com.pikefin.services;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.springframework.dao.DataAccessException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pikefin.log4jWrapper.Logs;
import twitter4j.internal.logging.Logger;

import com.pikefin.ApplicationSetting;
import com.pikefin.Constants;
import com.pikefin.businessobjects.Entity;
import com.pikefin.businessobjects.Job;
import com.pikefin.exceptions.CustomEmptyStringException;
import com.pikefin.exceptions.GenericException;
import com.pikefin.exceptions.SkipLoadException;
import com.pikefin.services.inter.EntityService;
import com.pikefin.services.inter.JobService;

//@Service
public class ProcessingFunctions {
	private static final Logger logger=Logger.getLogger(ProcessingFunctions.class);
	//@Autowired
	private JobService jobService=ApplicationSetting.getInstance()
	    .getApplicationContext().getBean(JobService.class);
	private EntityService entityService=ApplicationSetting.getInstance()
	    .getApplicationContext().getBean(EntityService.class);
	
	String strDataValue;

	// Right now this is only set during the preProcessing function
	String strTicker, strTemp1, strTemp2, strTemp3;
	
	// Table extraction processing function parameters.
	ArrayList<String[]> propTableData;
	Job j;
	String propStrTableDataSet;
	DataGrabExecutor dg;
	
	public ProcessingFunctions(DataGrabExecutor tmpDG) {
	  this.dg = tmpDG;
	}
	
	public boolean preNoDataCheck(String dataSet) {
		try	{
			Job job=jobService.getJobByDataSet(dataSet);
			String strFunctionName =job.getPreNoDataCheckFunc();
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("Pre No Data Check function name: " +
			      strFunctionName,Logs.STATUS2,"PF1");
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
				ApplicationSetting.getInstance().getStdoutwriter()
				  .writeln("No data check function, assuming data is ok",
				      Logs.STATUS2,"PF2");
				return(false);
			}
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception e) {
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("preNoDataCheck method call failed",Logs.ERROR,"PF3");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(e);
			return(false);
		}
	}
	
	public boolean preJobProcessing(Job j)	{
		try	{
			String strFunctionName = j.getPreJobProcessFuncName();
			
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("Pre Job Process Func Name: " + strFunctionName,
			      Logs.STATUS2,"PF3.2");
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
				ApplicationSetting.getInstance().getStdoutwriter()
				  .writeln("No pre job process function, exiting...",
				      Logs.STATUS2,"PF3.3");
				return(true);
			}
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln(strFunctionName,Logs.STATUS2,"PF3.35");
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception tmpE) {
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("pre Job Processing method call failed",Logs.ERROR,"PF3.4");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(tmpE);
			return(false);
		}
	}
	
	public boolean postJobProcessing(Job j)	{
		try	{
			String strFunctionName = j.getPostJobProcessFuncName();
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("Post Job Process Func Name: " + strFunctionName,
			      Logs.STATUS2,"PF3.5");
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
				ApplicationSetting.getInstance().getStdoutwriter()
				  .writeln("No post job process function, exiting...",
				      Logs.STATUS2,"PF3.7");
				return(true);
			}
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln(strFunctionName,Logs.STATUS2,"PF3.8");
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception tmpE) {
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("post Job Processing method call failed",Logs.ERROR,"PF3.9");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(tmpE);
			return(false);
		}
	}

	public boolean preProcessing(Job j, String strTicker) {
	 	try	{
			String strFunctionName = j.getPreProcessFuncName();
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("Pre Process Func Name: " + strFunctionName,
			      Logs.STATUS2,"PF4");
			this.strTicker = strTicker;
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
				ApplicationSetting.getInstance().getStdoutwriter()
				  .writeln("No pre process function, exiting...",Logs.STATUS2,"PF5");
				return(true);
			}
			// TODO need to check if these class properties are still being used.
			strDataValue = "";
			strTemp1 = "";
			strTemp2 = "";
			strTemp3 = "";
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln(strFunctionName,Logs.STATUS2,"PF6");
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception tmpE) {
			ApplicationSetting.getInstance().getStdoutwriter()
			  .writeln("preProcessing method call failed",Logs.ERROR,"PF7");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(tmpE);
			return(false);
		}

}

	public ArrayList<String []> postProcessing(ArrayList<String []> tabledata,
	    Job inputJob) 
	        throws SkipLoadException,CustomEmptyStringException {
	  if (tabledata.get(0) != null) {
	    strDataValue = tabledata.get(0)[0];
	  }
	  try	{
	    String strFunctionName = inputJob.getPostProcessFuncName();
	    ApplicationSetting.getInstance().getStdoutwriter()
	      .writeln("Post Process Func Name: " +
	          strFunctionName,Logs.STATUS2,"PF8");
	    if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
	      ApplicationSetting.getInstance().getStdoutwriter()
	        .writeln("No post process function, exiting...",Logs.STATUS2,"PF9");
	      return(tabledata);
	    }
	    propTableData = tabledata;
	    propStrTableDataSet = inputJob.getDataSet();

	    j = inputJob;
	    Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
	    m.invoke(this, new Object[] {});
	  }
	  // Need to break this down into individual exceptions.
	  catch (IllegalAccessException iae) {
	    ApplicationSetting.getInstance().getStdoutwriter()
	      .writeln("postProcessing method call failed",Logs.ERROR,"PF16.1");
	    ApplicationSetting.getInstance().getStdoutwriter().writeln(iae);

	  }
	  
	  // Any exceptions thrown by the function pointer are wrapped in an
	  // InvocationTargetException
	  catch (InvocationTargetException ite) {
	    if (ite.getTargetException().getClass()
	        .getSimpleName().equals("SkipLoadException")) {
	      throw new SkipLoadException();
	    }
	    else if (ite.getTargetException().getClass()
	        .getSimpleName().equals("CustomEmptyStringException")) {
	      // TODO: pass along the exception message.
	      throw new CustomEmptyStringException();
	    }
	    else { 
	      ApplicationSetting.getInstance().getStdoutwriter()
	        .writeln("postProcessing method call failed",Logs.ERROR,"PF16.3");
	      if (ite.getCause() != null) {
	        ApplicationSetting.getInstance().getStdoutwriter()
	          .writeln((Exception)ite.getCause());
	      }
	    }
	  }
	  catch (NoSuchMethodException nsme) {
	    ApplicationSetting.getInstance().getStdoutwriter()
	      .writeln("postProcessing method call failed",Logs.ERROR,"PF16.5");
	    ApplicationSetting.getInstance().getStdoutwriter().writeln(nsme);
	  }
	  catch (DataAccessException tmpE) {
	    ApplicationSetting.getInstance().getStdoutwriter()
	      .writeln("postProcessing method call failed",Logs.ERROR,"PF17");
	    ApplicationSetting.getInstance().getStdoutwriter().writeln(tmpE);
	  }

	  return(propTableData);
	}
	
	// @throws SQLException
	public void postProcessYahooSharePriceYQL() throws GenericException {
	  // OFP 2/24/2012 - The Yahoo processsing is really convoluted now. 
	  // We splice together up to 5 xml streams and then we have to strip out
	  // the xml header and tail before putting it through the SAX parser. 
	  // 
	  // There are 2 reasons for all this complexity:
	  // 1) Issue with invalid data retrieval from yahoo where we have to check
	  // the timestamp and if wrong resend the url request.
	  // 2) YQL has a limit where we can only submit 100 tickers at a time 
	  // (or this might be a limit on the URL size, can't remember which).
	  // 
	  // One other way to handle this situation is to break up the entity_group 
	  // into 5 different groups and then have 5 different jobs, 1 for each 
	  // group, under the parent task. But in order to do this, groups need 
	  // to be associated with jobs, not tasks, which I think is how it should 
	  // be anyways.

	  String strTmpValue = propTableData.get(0)[0];

	  // Replace all line separators; carriage returns, whatnot
	  strTmpValue = strTmpValue.replaceAll("\\r|\\n", "");

	  int nBeginMerge = strTmpValue.indexOf("<results>") + 9;

	  int nCountMerge = 0;
	  while (true) {
	    if ((nBeginMerge = strTmpValue
	        .indexOf("<results>",nBeginMerge+1)) != -1) {
	      nCountMerge++;
	    }
	    else {
	      break;
	    }
	  }

	  nBeginMerge = strTmpValue.indexOf("<results>") + 9;
	  int nEndMerge=0;

	  while (nCountMerge > 0) {
	    if ((nBeginMerge = strTmpValue.indexOf("</results>",nBeginMerge)) == -1) {
	      //shouldn't get here
	      ApplicationSetting.getInstance().getStdoutwriter()
	        .writeln("Issue merging XML data",Logs.ERROR,"PF998.5");
	      break;
	    }

	    if ((nEndMerge = strTmpValue.indexOf("<results>",nBeginMerge)) == -1) {
	      //shouldn't get here
	      ApplicationSetting.getInstance().getStdoutwriter()
	        .writeln("Issue merging XML data",Logs.ERROR,"PF998.6");
	      break;
	    }
	    nEndMerge += "<results>".length();
	    String strReplace = strTmpValue.substring(nBeginMerge,nEndMerge);
	    // The \Q and \E tell the regex processor to treat everything
	    // in between as literals.
	    strReplace = "\\Q" + strReplace + "\\E";
	    strTmpValue = strTmpValue.replaceFirst(strReplace,"");	
	    nCountMerge--;
	  }

	  SAXParserFactory factory = SAXParserFactory.newInstance();
	  factory.setValidating(true);

	  class Quote {
	    String symbol;
	    String lastTradeDate;
	    String lastTradeTime;
	    String price;

	    Quote() {
	    }
	  }

	  class CustomHandler extends DefaultHandler {
	    ArrayList<Quote> quoteList;
	    Quote curQuote;
	    String strVal;

	    CustomHandler () {
	      quoteList = new ArrayList<Quote>();
	      strVal = null;
	    }

	    public void startElement(String uri, String localName, String qName,
	        Attributes attributes)
	            throws SAXException {
	      if(qName.equalsIgnoreCase("quote")) {
	        curQuote = new Quote();
	      } 
	    }

	    public void characters(char[] ch, int start, int length)
	        throws SAXException {
	      strVal = new String(ch,start,length);
	    }

	    public void endElement(String uri, String localName, String qName)
	        throws SAXException  {
	      if (qName.equalsIgnoreCase("quote")) {
	        quoteList.add(curQuote);
	      }
	      else if (qName.equalsIgnoreCase("symbol")) {
	        curQuote.symbol = strVal;
	        strVal = null;
	      }
	      else if (qName.equalsIgnoreCase("lasttradedate")) {
	        curQuote.lastTradeDate = strVal;
	      }
	      else if (qName.equalsIgnoreCase("lasttradetime")) {
	        curQuote.lastTradeTime = strVal;
	      }
	      else if (qName.equalsIgnoreCase("lasttradepriceonly")) {
	        curQuote.price = strVal;
	      }
	      strVal = null;
	    }
	  }

	  CustomHandler handler = new CustomHandler();

	  try {
	    SAXParser saxParser = factory.newSAXParser();
	    saxParser.parse(new InputSource( 
	        new StringReader( strTmpValue)), handler);
	  }
	  // TODO: Eventually throw these exceptions outside of the function.
	  catch(ParserConfigurationException pce) {
	    ApplicationSetting.getInstance().getStdoutwriter()
	      .writeln("PostProcessing Exception",Logs.ERROR,"PF1001.1");
	    ApplicationSetting.getInstance().getStdoutwriter().writeln(pce);
	  }
	  catch(SAXException saxe) {
	    String[] tmp = new String[3];
	    tmp[0]="Task:"+this.dg.getCurrentTask().getTaskId();
	    tmp[1]="URL:"+this.dg.strStage2URL;
	    tmp[2]=this.dg.returned_content;
	    if(logger.isDebugEnabled()){
	      logger.debug(tmp.toString());
	    }
	    ApplicationSetting.getInstance().getStdoutwriter()
	      .writeln("PostProcessing Exception",Logs.ERROR,"PF1001.2");
	    ApplicationSetting.getInstance().getStdoutwriter().writeln(saxe);
	  }
	  catch(IOException ioe) {
	    ApplicationSetting.getInstance().getStdoutwriter()
	      .writeln("PostProcessing Exception",Logs.ERROR,"PF1001.3");
	    ApplicationSetting.getInstance().getStdoutwriter().writeln(ioe);
	  }
	  
	  propTableData.remove(0);
	  String[] tmpArray = {"value","date_collected","entity_id"};
	  propTableData.add(tmpArray);
	
	  for (int i=0;i<handler.quoteList.size();i++) {
	    Quote curQuote = handler.quoteList.get(i);
	    String[] rowdata = new String[tmpArray.length];

	    if (curQuote.price.equals("0.00")) {
	      ApplicationSetting.getInstance().getStdoutwriter()
	        .writeln("Issue looking up ticker " + curQuote.symbol + 
	            ",skipping",Logs.WARN,"PF50");
	      System.out.println();
	      continue;
	    }		
	    rowdata[0] = curQuote.price;
	    String[] strDateArray = curQuote.lastTradeDate.split("/");
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.MONTH, Integer.parseInt(strDateArray[0])-1);
	    cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(strDateArray[1]));
	    cal.set(Calendar.YEAR,Integer.parseInt(strDateArray[2]));
	    String strTime = curQuote.lastTradeTime;
	    String strAMPM = strTime
	        .substring(strTime.indexOf("m")-1,strTime.indexOf("m")+1);
	    String strNewTime = strTime.substring(0,strTime.indexOf("m")-1);
	    String[] strTimeArray = strNewTime.split(":");
	    // There's an issue with setting the AMPM value and then the HOUR value.
	    // The conversion happens incorrectly if the 2 values are set separately.
	    int nHour = Integer.parseInt(strTimeArray[0]);
	    if (strAMPM.equals("pm") && nHour != 12) {
	      cal.set(Calendar.HOUR_OF_DAY, nHour + 12);
	    }
	    else if (strAMPM.equals("am") && nHour == 12) {
	      cal.set(Calendar.HOUR_OF_DAY, 0);
	    }
	    else {
	      cal.set(Calendar.HOUR_OF_DAY,nHour);
	    }
	    cal.set(Calendar.MINUTE, Integer.parseInt(strTimeArray[1]));
	    cal.set(Calendar.SECOND, 0);
	    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
	    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    formatter.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
	    Calendar currentCal = Calendar.getInstance();
	    DateFormat formatter2 = new SimpleDateFormat("M/d/yyyy");
	    // Only perform this check for task 10.
	   
	    if (dg.getCurrentTask().getTaskId().equals(10)) {
	      if (!formatter2.format(currentCal.getTime())
	          .equals(formatter2.format(cal.getTime()))) {
	        ApplicationSetting.getInstance().getStdoutwriter()
	          .writeln("Bad Yahoo Data, in postProcessing function for Symbol " +
	            curQuote.symbol,Logs.ERROR,"PF50.5");
	      }
	    }
	    // If the taskid is 10, we are going to use the input time for
	    // date_collected. Otherwise we are going to use real time collection
	    // time - delay (for yahoo, 20 minutes) for date_collected.
	    if (dg.getCurrentTask().getTaskId().equals(10)) {
	      rowdata[1] = "'" + formatter.format(cal.getTime()) + "'";
	    }
	    else {
	      currentCal.add(Calendar.MINUTE, -20);
	      rowdata[1] = "'" + formatter.format(currentCal.getTime()) + "'";
	    }

	    // These are the 5 non US tickers for task 24:
	    // ^N225, ^AORD, ^TWII, ^NZ50, ^AXJO
	    if (Constants.Ticker.TICKER_BRK_A.equals(curQuote.symbol)) {
	      curQuote.symbol = Constants.Ticker.TICKER_BRK_BY_A;
	    }
	    else if (Constants.Ticker.TICKER_BF_B.equals(curQuote.symbol)) {
	      curQuote.symbol =Constants.Ticker.TICKER_BF_BY_B;
	    }

	    try {
	      Entity entity =entityService.loadEntityInfoByTicker(curQuote.symbol);
	      rowdata[2] =String.valueOf(entity.getEntityId());
	    }
	    catch (GenericException sqle) {
	      ApplicationSetting.getInstance().getStdoutwriter()
	        .writeln("Issue looking up ticker " + curQuote.symbol + 
	            ",skipping",Logs.ERROR,"PF50");
	      ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
	      continue;
	    }
	    propTableData.add(rowdata);
	  }
	}

	public void preJobProcessTableXrateorg() throws DataAccessException {

	}


	public void postProcessExchRate() {
	  String[] newrow;
	  String[] tmpArray = {"data_set","value","date_collected"};
	  ArrayList<String[]> newTableData = new ArrayList<String[]>();
	  newTableData.add(tmpArray);

	  newrow = new String[tmpArray.length];
	  newrow[0] = dg.strCurDataSet;
	  newrow[1] = this.propTableData.get(0)[0];
	  newrow[2] = "NOW()";
	  newTableData.add(newrow);

	  propTableData = newTableData;
	}
public boolean preNDCBloombergQuote() {
	/*
	 * Intermittently a quote doesn't return a value.	
	 */
	String strRegex = "(?i)(There are no matches for your search)";
	

	  
	Pattern pattern = Pattern.compile(strRegex);
	
	  
	Matcher matcher = pattern.matcher(dg.returned_content);
	
	String strRegex2 = "(?i)(\" price\">)";
	Pattern pattern2 = Pattern.compile(strRegex2);
	Matcher matcher2 = pattern2.matcher(dg.returned_content);
	  
	

	
	if (matcher.find()) {
		ApplicationSetting.getInstance().getStdoutwriter().writeln("Bloomberg Quote found no match for ticker " + dg.strCurrentTicker + ", skipping",Logs.WARN,"PF23.8");

		String[] tmp = new String[3];
		tmp[0]="Task:"+this.dg.getCurrentTask();
		tmp[1]="URL:"+this.dg.strStage2URL;
		tmp[2]=this.dg.returned_content;

		return(true);
	}
	else if (!matcher2.find()) {
		ApplicationSetting.getInstance().getStdoutwriter().writeln("Bloomberg Quote returned a blank quote for ticker " + dg.strCurrentTicker + ", skipping",Logs.WARN,"PF23.85");
		return true;
	}
	
		
	return(false);
	

	
}


  public void postProcessBloombergCommodities() {
    String[] tmpArray = {"value","date_collected","entity_id"};
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] rowdata, newrow;
    String[] rowheaders = propTableData.get(1);
  
    for (int row=2;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);
      if (rowdata[0].contains("N.A.")) { 
        continue;
      }
      newrow = new String[tmpArray.length];
      newrow[0] = rowdata[0].replace(",", "");
      newrow[1] = "NOW()";
      // If there are 2 sets of parens for the row header, 
      // we want to cutoff at the 2nd left paren.
      String tmp = rowheaders[row-2];
      int i = tmp.indexOf("(");
  
      if (tmp.indexOf("(",i+1)==-1) {
        tmp = tmp.substring(0,i);
      }
      else {
        tmp = tmp.substring(0,tmp.indexOf("(",i+1));
      }
  
      tmp = tmp.trim();
      tmp = tmp.replace("'", "");
  
      try {
        Entity entity=entityService.loadEntityInfoByTicker(tmp);
        newrow[2] = String.valueOf(entity.getEntityId());
        newTableData.add(newrow);
      }
      catch (GenericException sqle) {
        ApplicationSetting.getInstance().getStdoutwriter()
          .writeln("Problem looking up ticker: " 
              + tmp + ",row skipped",Logs.WARN,"PF99.55");
      }
    }
    newTableData.add(0, tmpArray);
    propTableData = newTableData;
  }


  public boolean preProcessYahooEPSEst() throws GenericException {
    // Negative values are enclosed in font changing tags which 
    // have to be removed
    if (Constants.Ticker.TICKER_BF_BY_B.equals(strTicker)) {
      dg.strCurrentTicker=Constants.Ticker.TICKER_BF_B;
    }
    else if (Constants.Ticker.TICKER_BRK_BY_A.equals(strTicker)) {
      dg.strCurrentTicker=Constants.Ticker.TICKER_BRK_A;
    }
    // TODO: Need to get rid of these return values and go with exceptions 
    return(true);
  }
   

  public boolean preProcessGoogleEPS() throws GenericException {
    // Negative values are enclosed in font changing tags which have
    // to be removed
    if (Constants.Ticker.TICKER_BF_BY_B.equals(strTicker)) {
      dg.strCurrentTicker=Constants.Ticker.TICKER_NYSE_COLLON_BF_B;
    }
    else if (Constants.Ticker.TICKER_BRK_BY_A.equals(strTicker)) {
      dg.strCurrentTicker=Constants.Ticker.TICKER_NYSE_COLLON_BRK_A ;
    }
    else if (strTicker.equals("PG") || strTicker.equals("SCHW") ||
        strTicker.equals("DF") || strTicker.equals("MHS") ||
        strTicker.equals("NWL")) {
      dg.strCurrentTicker="NYSE:" + strTicker;
    }
    // TODO: Need to get rid of these return values and go with exceptions 
    return(true);
  }
    

  public void postProcessTableYahooBeginYearVerify() 
      throws CustomEmptyStringException, GenericException { 
    //The purpose of this function is to verify that the quarter end months
    // reported in yahoo match the begin_fiscal_calendar property stated in the
    // entities table.
    // 
    // It can be swapped into the 'table_yahoo_q_eps_est_body' data_set

    String[] colheaders = propTableData.get(0);
    String strQEndMonth = colheaders[0];
    Entity entity=entityService.loadEntityInfoByTicker(this.strTicker);
    int nBeginFiscalCal = MoneyTime
        .convertMonthStringtoInt(entity.getBeginFiscalCalendar());
    PrintWriter fullfilewriter = null;

    try {
      fullfilewriter = new PrintWriter( 
          new FileWriter("YahooVerify.txt",true),true);
    }
    catch (IOException ioe) {
      // TODO: Need to do something here.
    }

    switch(nBeginFiscalCal)
    {
    case 1:
    case 4:
    case 7:
    case 10:
      if (!(strQEndMonth.substring(0,3).equalsIgnoreCase("Mar") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Jun") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Sep") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Dec"))) {
        System.out.println("begin_fiscal_calendar and quote.yahoo" +
        		" mismatch for ticker " + strTicker);
        fullfilewriter.write(strTicker + "\n");
      }
      break;
    case 2:
    case 5:
    case 8:
    case 11:
      if (!(strQEndMonth.substring(0,3).equalsIgnoreCase("Apr") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Jul") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Oct") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Jan"))) {
        System.out.println("begin_fiscal_calendar and quote.yahoo" +
        		" mismatch for ticker " + strTicker);
        fullfilewriter.write(strTicker + "\n");
      }
      break;
    case 3:
    case 6:
    case 9:
    case 12:
      if (!(strQEndMonth.substring(0,3).equalsIgnoreCase("May") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Aug") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Nov") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Feb"))) {
        System.out.println("begin_fiscal_calendar and quote.yahoo" +
        		" mismatch for ticker " + strTicker);
        fullfilewriter.write(strTicker + "\n");
      }
      break;
    }
    fullfilewriter.close();
  }


  public void postProcessTreasuryDebtTable6() {
    int j=0;
    for (int i=0;i<10;i++) {
      j++;
    }
  }
  
  public void postProcessTableXrateorg() {
    String[] rowheaders = propTableData.get(1);
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] tmpArray = {"value","date_collected","entity_id"};
    newTableData.add(tmpArray);
    for (int i=0;i<rowheaders.length;i++) {
      String strTmp = rowheaders[i];
      strTmp = "USD" + strTmp
          .substring(strTmp.indexOf("(")+1,strTmp.indexOf(")"));

      //The first 2 crosses on the europe page are inverse: EURUSD and GBPUSD
      if (propStrTableDataSet.contains("europe")) {
        if (i==0) {
          strTmp = "EURUSD";
        }
        else if (i==1) {
          strTmp = "GBPUSD";
        }
      }
      String[] tmpA = new String[tmpArray.length];
      tmpA[0] = propTableData.get(i+2)[0];
      tmpA[1] = "NOW()";

      try {
        Entity entity=entityService.loadEntityInfoByTicker(strTmp);
        tmpA[2] = String.valueOf(entity.getEntityId());
        newTableData.add(tmpA);
      }
      catch (GenericException sqle) {
        ApplicationSetting.getInstance().getStdoutwriter()
        .writeln("Problem looking up ticker: " 
            + strTmp + ",row skipped",Logs.WARN,"PF99.55");
      }
    }
    propTableData = newTableData;
  }
    

  public void postProcessMWatchEPSEstTable() 
      throws GenericException,SkipLoadException {

    // OFP 11/12/2011 - One issue with this data source is because the column
    // headers are relative (i.e. "this quarter", "next quarter") there is no
    // way to guarantee which quarter marketwatch is referring to. If they are
    // slow in updating the website, then the data will be collected for
    // the wrong quarters.
    // As of 12/15/2010 there is no data for RAND nor VIA and no obvious
    // string to key off of for a no data check.
    String[] rowdata, newrow;
    String[] colheaders = propTableData.get(0);
    Calendar cal = Calendar.getInstance();
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] tmpArray;
    if (propStrTableDataSet.contains("q")) {
      tmpArray=new String[7];
      tmpArray[0]="value";
      tmpArray[1]="date_collected";
      tmpArray[2]="entity_id";
      tmpArray[3]="fiscalyear";
      tmpArray[4]="fiscalquarter";
      tmpArray[5]="calquarter";
      tmpArray[6]="calyear";
      // OFP 11/12/2011 - Ran into an issue where the column headers 
      // weren't being read correctly.
      if (!colheaders[0].toUpperCase().equals("THIS QUARTER")) {
        ApplicationSetting.getInstance().getStdoutwriter()
          .writeln("Invalid column header format for entity_id " + 
              dg.nCurrentEntityId,Logs.STATUS2,"PF46");
        throw new SkipLoadException();
      }
    }else {
      tmpArray=new String[4];
      tmpArray[0]="value";
      tmpArray[1]="date_collected";
      tmpArray[2]="entity_id";
      tmpArray[3]="fiscalyear";
    }
    newTableData.add(tmpArray);
    String tmpVal;
    for (int row=2;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);

      for (int col=0;col<colheaders.length;col++) {
        newrow = new String[7];
        if (rowdata[col].compareTo("void") != 0) {
          tmpVal = rowdata[col];
          if (tmpVal.contains("span"))
            //this is a negative number
            newrow[0] = tmpVal
              .substring(tmpVal.indexOf(">-")+1,tmpVal.indexOf("</"));
          else
            newrow[0] = tmpVal;

          //Berkshire tends to be the only company with an EPS in the thousands.
          newrow[0] = newrow[0].replace(",", "");
          newrow[1] = "NOW()";
          newrow[2] = dg.nCurrentEntityId + "";
          String strFiscalYearQuarter = MoneyTime
              .getFiscalYearAndQuarter(strTicker,cal.get(Calendar.MONTH), 
                  cal.get(Calendar.YEAR));
          int nFiscalQuarter=Integer
              .parseInt(strFiscalYearQuarter.substring(0,1));
          int nFiscalYear = Integer
              .parseInt(strFiscalYearQuarter.substring(1,5));
          if ("NEXT QUARTER".equalsIgnoreCase(colheaders[col])) {
            nFiscalQuarter++;
            if (nFiscalQuarter == 5) {
              nFiscalYear++;
              nFiscalQuarter=1;
            }
          }
          else if (colheaders[col].toUpperCase().equals("NEXT FISCAL")) {
            nFiscalYear++;
          }

          newrow[3] = nFiscalYear + "";
          //don't use the year returned from getFiscalYearAndQuarter - use
          // the one retrieved from the web page
          if (propStrTableDataSet.contains("q")) {
            String strCalYearQuarter = MoneyTime
                .getCalendarYearAndQuarter(strTicker, 
                    nFiscalQuarter, nFiscalYear);
            newrow[4] = nFiscalQuarter + "";
            newrow[5] = strCalYearQuarter.substring(0,1);
            newrow[6] = strCalYearQuarter.substring(1,5);
          }
          newTableData.add(newrow);
        }
      }
    }
    propTableData = newTableData;
  }


  public boolean preNDCNasdaqEPSEst()	{
    String strRegex = "(?i)(No Data Available)";
    ApplicationSetting.getInstance().getStdoutwriter()
      .writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF46");
    Pattern pattern = Pattern.compile(strRegex);
    Matcher matcher = pattern.matcher(dg.returned_content);
    
    // OFP 12/19/2012 - Nasdaq misspelled available.
    String strRegex2 = "(?i)(No data avaiable)";
    Pattern pattern2 = Pattern.compile(strRegex2);
    Matcher matcher2 = pattern2.matcher(dg.returned_content);
    if (matcher.find() || matcher2.find()) {
      ApplicationSetting.getInstance().getStdoutwriter()
        .writeln("Nasdaq no data avialable for ticker " 
            + dg.strCurrentTicker, Logs.WARN,"PF21.38");
      return(true);
    }
    strRegex = "(?i)(feature currently is unavailable)";
    ApplicationSetting.getInstance().getStdoutwriter()
      .writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF46.5");
    pattern = Pattern.compile(strRegex);
    matcher = pattern.matcher(dg.returned_content);
    if (matcher.find()) {
      ApplicationSetting.getInstance().getStdoutwriter()
        .writeln("Nasdaq message feature currently unavailable for ticker " 
            + dg.strCurrentTicker, Logs.WARN,"PF21.40");
      return true;
    }
    return false;
  }
	

  public void postProcessBloombergIndexes() {
    String[] tmpArray = {"value","date_collected","entity_id"};
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] rowdata, newrow;
    String[] rowheaders = propTableData.get(1);
    for (int row=2;row<propTableData.size();row++)	{
      rowdata = propTableData.get(row);
      newrow = new String[tmpArray.length];
      newrow[0] = rowdata[0].replace(",", "");
      newrow[1] = "NOW()";
      String ticker = rowheaders[row-2];
      
      //Next line condenses multiple spaces down to one.
      ticker = ticker.replaceAll("\\s+", " "); 
      if (rowdata[0].equals("pikefinvoid")) {
        ApplicationSetting.getInstance().getStdoutwriter()
          .writeln("Invalid data format for " 
              + ticker + ". Skipping.",Logs.WARN,"PF2.75");
        continue;
      }
      else if (rowdata[0].contains("N.A."))  {	
        ApplicationSetting.getInstance().getStdoutwriter()
          .writeln("N.A. value for ticker " + ticker,Logs.WARN,"PF2.75");
        continue;
      }
      try {
        Entity entity=entityService.loadEntityInfoByTicker(ticker); 
        newrow[2] =String.valueOf(entity.getEntityId());
        newTableData.add(newrow);

      }catch (GenericException sqle) {
        ApplicationSetting.getInstance().getStdoutwriter()
          .writeln("Problem looking up ticker alias: " 
              + ticker + ",row skipped",Logs.WARN,"PF42.51");
        ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
      }
    }
    newTableData.add(0, tmpArray);
    propTableData = newTableData;
  }
  
  // Used to process the schedule of type nasdaq_q_eps.
  // @throws GenericException
  public void postProcessNasdaqEPSTable() throws GenericException {
    // Special Situations That need to be handled:
    // -data values contain date in parentheses 
    // -data values contain &nbsp
    // -negative values use hyphen
 
    // TODO JNJ end of quarter months are incorrect, still need to code for this 

    // Ticker urls currently without data: BF/B, BRK/A, AON
    
    String strTicker = dg.strOriginalTicker;

    String[] rowdata, newrow;
    String[] colheaders = propTableData.get(0);
    String[] rowheaders = propTableData.get(1);

    // This is custom code to fix rowheader #4 since the html is not consistent.
    rowheaders[3] = rowheaders[3].replace("(FYE)","");

    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] tmpArray = {"value","date_collected","entity_id",
        "fiscalquarter","fiscalyear","calquarter","calyear"};
    newTableData.add(tmpArray);

    for (int row=2;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);

      for (int col=0;col<colheaders.length;col++) {
        newrow = new String[tmpArray.length];
        if (rowdata[col].compareTo("void") != 0) {
          if ((rowdata[col].contains("N/A") == true) 
              || (rowdata[col].isEmpty() == true)) {
            ApplicationSetting.getInstance().getStdoutwriter()
              .writeln("N/A value or empty value, skipping...",
                  Logs.STATUS2,"PF39");
            continue;
          }
          else if (rowdata[col].contains("(") == true) {
            newrow[0]=rowdata[col].substring(0,rowdata[col].indexOf("("));
          }
          else {
            ApplicationSetting.getInstance().getStdoutwriter()
              .writeln("Problem with data value formatting",Logs.ERROR,"PF40");
          }

          newrow[1] = "NOW()";
          newrow[2] = String.valueOf(dg.nCurrentEntityId );
          newrow[3] = Integer.toString(row-1);
          newrow[4] = colheaders[col];
          String strCalYearQuarter = MoneyTime
              .getCalendarYearAndQuarter(strTicker, 
                  Integer.parseInt(newrow[3]), Integer.parseInt(newrow[4]));
          newrow[5] = strCalYearQuarter.substring(0,1);
          newrow[6] = strCalYearQuarter.substring(1,5);
          newTableData.add(newrow);
        }
      }
    }
    propTableData = newTableData;
  }

  // This method post process for schedule type google_q_eps_excxtra
  // @throws GenericException
  public void postProcessGoogleEPSTable() throws GenericException {
    String strTicker = dg.strOriginalTicker;
    String[] rowdata, newrow;
    String[] colheaders = propTableData.get(0);
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] tmpArray = {"value","date_collected","entity_id","fiscalquarter",
        "fiscalyear","calquarter","calyear"};
    newTableData.add(tmpArray);
    String tmpVal;
    try {
      for (int row=2;row<propTableData.size();row++) {
        rowdata = propTableData.get(row);
        for (int col=0;col<colheaders.length;col++){
          newrow = new String[7];
          if (rowdata[col].compareTo("void") != 0){
            tmpVal = rowdata[col];
            if (tmpVal.contains("span")){
              //this is a negative number
              Integer startIndexs = tmpVal.indexOf(">-")+1;
              Integer endIndex=tmpVal.indexOf("</");
              newrow[0] = tmpVal.substring(startIndexs,endIndex);
            }
            else {
              newrow[0] = tmpVal;
            }

            // Some tickers have tables with only 4 columns. The table
            // processing will wrap around to the next row in those cases.
            if (newrow[0].contains("Diluted")) {
              continue;
            }
            
            // Berkshire tends to be the only company with an EPS
            // in the thousands.
            newrow[0] = newrow[0].replace(",", "");
            newrow[1] = "NOW()";
            newrow[2] = dg.nCurrentEntityId + "";
            
            // OFP 11/12/2011 - I have to deal with this wacky issue where
            // google has the end of quarter days off by one in some instances. 
            // 
            // CSC is one example. For calendar quarter 2, cal year 2011, they
            // have the end date listed as 7/1/2011 instead of 6/30/2011. 
            // So as a workaround, if the Day of month is 1, I'm going to
            // subtract one day from the date.
            // 
            // It's not known at this time if this will screw up other tickers.
            // If other tickers behave differently then I may have to set up 
            // ticker specific code in here.
            Calendar cal = Calendar.getInstance();
            String[] date = colheaders[col].split("-");
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
            cal.set(Calendar.MONTH,Integer.parseInt(date[1])-1);
            cal.set(Calendar.YEAR,Integer.parseInt(date[0]));
            if (cal.get(Calendar.DAY_OF_MONTH)==1)
              cal.add(Calendar.DAY_OF_MONTH,-1);
         
            String strFiscalYearQuarter = MoneyTime
                .getFiscalYearAndQuarter(strTicker, cal.get(Calendar.MONTH)+1,
                    cal.get(Calendar.YEAR));
            String strCalYearQuarter = MoneyTime
                .getCalendarYearAndQuarter(strTicker, 
                    Integer.parseInt(strFiscalYearQuarter.substring(0,1)), 
                    Integer.parseInt(strFiscalYearQuarter.substring(1,5)));
            newrow[3] = strFiscalYearQuarter.substring(0,1);
            
            // Don't use they year returned from getFiscalYearAndQuarter 
            // - use the one retrieved from the web page.
            newrow[4] = strFiscalYearQuarter.substring(1,5);
            newrow[5] = strCalYearQuarter.substring(0,1);
            newrow[6] = strCalYearQuarter.substring(1,5);
            newTableData.add(newrow);
          }
        }
      }
      propTableData = newTableData;
    // TODO Need More specific exception handling 
    }catch(Exception e){
      ApplicationSetting.getInstance().getStdoutwriter()
      .writeln(" Error in processing data--" 
          + dg.strCurrentTicker, Logs.ERROR,"PF82.23");
      ApplicationSetting.getInstance().getStdoutwriter().writeln(e);
    }
  }
  
  // Post processing function for schedule type nasdaq_y_eps_est for task id 13
  public void postProcessNasdaqEPSEstTable() {
    String[] rowdata, newrow;
    String strTicker;
    String[] tmpArray;
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    
    // OFP 10/17/2010 - For Ticker T there is currently redundant data
    // being displayed.
    // Need to add code to make the query insert column definitions the first
    // row in the table data that is returned.
    if (propStrTableDataSet.contains("_q_") == true) {
      tmpArray = new String[]{"entity_id","date_collected","value","fiscalyear",
          "calyear","fiscalquarter","calquarter"};
    }else {
      tmpArray = new String[]{"entity_id","date_collected",
          "value","fiscalyear"};
    }
    try {
      strTicker = dg.strOriginalTicker;
      String[] rowheaders = propTableData.get(1);
      for (int x=4;x<propTableData.size();x++) {
        rowdata = propTableData.get(x);
        newrow = new String[tmpArray.length];
        newrow[0] = String.valueOf(dg.nCurrentEntityId);
        newrow[1] = "NOW()";
        newrow[2] = rowdata[0];
        String rowHeader=rowheaders[x-2];
        //TODO Commented to fix the number format exception
        //String rowHeader=rowheaders[2];
        MoneyTime mt = new MoneyTime(rowHeader.substring(0,3),
            rowHeader.replace("&nbsp;","").substring(5,7),strTicker);
        newrow[3] = mt.strFiscalYear;
        if (propStrTableDataSet.contains("_q_")) {
          newrow[4] =String.valueOf(mt.nCalAdjYear);
          newrow[5] = mt.strFiscalQtr;
          newrow[6] = String.valueOf(mt.nCalQtr);
        }
        newTableData.add(newrow);
      }
      newTableData.add(0, tmpArray);
      propTableData = newTableData;
    }
    catch (GenericException sqle) {
      ApplicationSetting.getInstance().getStdoutwriter()
        .writeln("Problem processing table data",Logs.ERROR,"PF43");
      ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
    }
  }


/*public void postProcessTreasuryDirect() throws SQLException
{
	
	String[] data = propTableData.get(1);
	
	data[0] = data[0].replace(",", "");
	data[0] = data[0].substring(0,data[0].indexOf("."));
	long lPublicDebt = Long.parseLong(data[0]);
	lPublicDebt = lPublicDebt/10000000;
	
	data[1] = data[1].replace(",", "");
	data[1] = data[1].substring(0,data[1].indexOf("."));
	long lIntraGov = Long.parseLong(data[1]);
	lIntraGov = lIntraGov/10000000;
	Batches batch=new Batches();
	batch.setBatchDateCollected(new Date());
	batch.setBatchTask(dg.getCurrentTask());
	batch.setRandomUnique(ApplicationSetting.getInstance().getUniqueRandom());

	try
	{
		batchService.saveBatchesInfo(batch);
	}
	catch (GenericException sqle)
	{
		ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem with custom insert",Logs.ERROR,"PF55.52");
		ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
	}
	
	

	
	strQuery = "insert into " + this.dg.strFactTable;
	strQuery += " (\"value\",\"scale\",\"date_collected\",\"entity_id\",\"metric_id\",\"batch_id\") ";
	strQuery += " values (";
	strQuery += lPublicDebt;
	strQuery += ",7,NOW(),1360,9," + dg.nTaskBatch + ")";
	FactData fact=new FactData();
	fact.setBatch(batch)
	try
	{
		dbf.db_update_query(strQuery);
	}
	catch (SQLException sqle)
	{
		ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem with custom insert",Logs.ERROR,"PF55.5");
		ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
	}
	
	strQuery = "insert into " + this.dg.strFactTable;
	strQuery += " (\"value\",\"scale\",\"date_collected\",\"entity_id\",\"metric_id\",\"batch\") ";
	strQuery += " values (";
	strQuery += lIntraGov;
	strQuery += ",7,NOW(),1360,10," + dg.nTaskBatch + ")";
	
	try
	{
		dbf.db_update_query(strQuery);
	}
	catch (SQLException sqle)
	{
		ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem with custom insert",Logs.ERROR,"PF55.6");
		ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
	}
	
	
	
	
	
	
}


	
	
	
	
	

public void postProcessTableXrateorg() 
{
	String[] rowheaders = propTableData.get(1);
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	


	String[] tmpArray = {"value","date_collected","entity_id"};
	
	newTableData.add(tmpArray);
	
	
	for (int i=0;i<rowheaders.length;i++)
	{
		String strTmp = rowheaders[i];
		strTmp = "USD" + strTmp.substring(strTmp.indexOf("(")+1,strTmp.indexOf(")"));
		
		//The first 2 crosses on the europe page are inverse: EURUSD and GBPUSD
		if (propStrTableDataSet.contains("europe"))
		{
			if (i==0)
				strTmp = "EURUSD";
			else if (i==1)
				strTmp = "GBPUSD";
		}
		String[] tmpA = new String[tmpArray.length];
		//tmpA[0] = propStrTableDataSet;
		tmpA[0] = propTableData.get(i+2)[0];
		tmpA[1] = "NOW()";
		
		String query = "select * from entities where ticker='"+strTmp+"'";
		
		try
		{
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			tmpA[2] = rs.getInt("id") + "";
			newTableData.add(tmpA);
			
		}
		catch (SQLException sqle)
		{
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem looking up ticker: " + strTmp + ",row skipped",Logs.WARN,"PF42.5");
			
			
			 * This is not a fatal error so we won't display the full exception.
			 
			//ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
		}
		//tmpA[3] = "forex";
	

		
	}
	
	
	propTableData = newTableData;
	
	
	
	
	
}

public void postProcessDescriptionXrateorg() 
{
	 Function to populate the full description for the ticker names, e.g. Argentine Peso for ARS 
	//String[] rowheaders = propTableData.get(1);
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	


	String[] tmpArray = {"data_set","value","date_collected","ticker"};
	
	newTableData.add(tmpArray);
	String query;
	
	
	for (int i=0;i<propTableData.size();i++)
	{
		String strTmp = propTableData.get(i)[0];
		String ticker = "USD" + strTmp.substring(strTmp.indexOf("(")+1,strTmp.indexOf(")"));
		
		strTmp = strTmp.substring(strTmp.indexOf(">")+1,strTmp.indexOf(" ("));
		
		
		//The first 2 crosses on the europe page are inverse: EURUSD and GBPUSD
		if (propStrTableDataSet.contains("europe"))
		{
			if (i==0 || i==1)
				continue;
		}
		
		query = "insert into entities (ticker,full_name) values ('" + ticker + "','" + strTmp +"')";
		
		try
		{
			dbf.db_update_query(query);
		}
		catch (SQLException sqle)
		{
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Failed to insert ticker " + ticker + " into entities table",Logs.STATUS2,"PF43.49");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
		}
		
	}
	
	

	
	
	
}

public void postProcessTableYahooEPSAct() throws CustomEmptyStringException, SQLException
{
	String[] tmpArray = {"data_set","value","date_collected","ticker","fiscalquarter","fiscalyear","calquarter","calyear"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	String[] rowdata, newrow;
	String[] colheaders = propTableData.get(0);
	
	newTableData.add(tmpArray);
	
	for (int row=2;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		for (int col=0;col<colheaders.length;col++)
		{
			newrow = new String[16];
			if (rowdata[col].compareTo("void") != 0)
			{
				
				newrow[0] = propStrTableDataSet;
				if (rowdata[col].equals("N/A"))
				{
					ApplicationSetting.getInstance().getStdoutwriter().writeln("N/A value or empty value, skipping...",Logs.STATUS2,"PF43.5");
					continue;
				}
				
				
				 * Negative values are enclosed in font changing tags which have to be removed
				 
				if (rowdata[col].indexOf("<") != -1)
				{
					rowdata[col] = rowdata[col].substring(rowdata[col].indexOf(">")+1,rowdata[col].length());
					rowdata[col] = rowdata[col].substring(0,rowdata[col].indexOf("<"));
				}
				
				
				newrow[1] = rowdata[col].replace(",","");
				//newrow[4] = "FUNCTION";
				newrow[2] = "NOW()";
				//newrow[6] = "VARCHAR";
				newrow[3] = dg.strOriginalTicker;
				//newrow[8] = "INTEGER";
				
				MoneyTime mt = new MoneyTime(colheaders[col].substring(0,3),colheaders[col].substring(4,6),strTicker);
				
				newrow[4] = mt.strFiscalQtr;
				newrow[5] = mt.strFiscalYear;
				newrow[6] = Integer.toString(mt.nCalQtr);		
				newrow[7] = Integer.toString(mt.nCalAdjYear);
				
				
				
				newTableData.add(newrow);
				
			}
			
			
		}
		
	}
	
	propTableData = newTableData;
	
	

}


public void postProcessTableYahooEPSEst() throws CustomEmptyStringException, SQLException
{
	String query = "select url_dynamic from extract_tables where Data_Set='" + propStrTableDataSet + "'";
	//ResultSet rs = dbf.db_run_query(query);
	//rs.next();
	//String strTicker = rs.getString("url_dynamic");

	String[] rowdata, newrow;
	String[] colheaders = propTableData.get(0);
	//String[] rowheaders = propTableData.get(1);
	
	String[] tmpArray = {"data_set","value","date_collected","ticker","fiscalyear"};//"calquarter","fiscalquarter"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	if (propStrTableDataSet.contains("_q_") == true)
	{
		int i = tmpArray.length;
		tmpArray = UtilityFunctions.extendArray(tmpArray);
		tmpArray = UtilityFunctions.extendArray(tmpArray);
		tmpArray = UtilityFunctions.extendArray(tmpArray);
		tmpArray[i] = "calyear";
		tmpArray[i+1] = "fiscalquarter";
		tmpArray[i+2] = "calquarter";
		
	}

	
	newTableData.add(tmpArray);
	
	for (int row=2;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		for (int col=0;col<colheaders.length;col++)
		{
			newrow = new String[16];
			if (rowdata[col].compareTo("void") != 0)
			{
				//newrow[0] = "VARCHAR";
				newrow[0] = propStrTableDataSet;
				//newrow[2] = "INTEGER";
				
				if (rowdata[col].equals("N/A"))
				{
					ApplicationSetting.getInstance().getStdoutwriter().writeln("N/A value or empty value, skipping...",Logs.STATUS2,"PF43.5");
					continue;
				}
				
				
				 * Negative values are enclosed in font changing tags which have to be removed
				 
				if (rowdata[col].indexOf("<") != -1)
				{
					rowdata[col] = rowdata[col].substring(rowdata[col].indexOf(">")+1,rowdata[col].length());
					rowdata[col] = rowdata[col].substring(0,rowdata[col].indexOf("<"));
				}
				
				
				newrow[1] = rowdata[col];
				//newrow[4] = "FUNCTION";
				newrow[2] = "NOW()";
				//newrow[6] = "VARCHAR";
				newrow[3] = dg.strOriginalTicker;
				//newrow[8] = "INTEGER";
				
				MoneyTime mt = new MoneyTime(colheaders[col].substring(0,3),colheaders[col].substring(4,6),strTicker,dbf);
				
				//int nMonth = uf.convertMontStringtoInt(colheaders[col].substring(0,3));
				//int nYear = Integer.parseInt(colheaders[col].substring(4,8));
				
				//String strFiscalQtrYear = uf.getFiscalYearAndQuarter(strTicker, nMonth, nYear);
				
				
		
				//newrow[10] = "VARCHAR";
				//newrow[4] = "eps_est";
				//newrow[12] = "INTEGER";
				
				//newrow[14] = "INTEGER";
				newrow[4] = mt.strFiscalYear;
	
				if (propStrTableDataSet.contains("_q_") == true)
				{
					newrow[5] = Integer.toString(mt.nCalAdjYear);
					newrow[6] = mt.strFiscalQtr;
					newrow[7] = Integer.toString(mt.nCalQtr);
				}
				newTableData.add(newrow);
				
			}
			
			
		}
		
	}
	
	propTableData = newTableData;
	
}

public void postProcessTableBriefIClaims() throws CustomEmptyStringException, SQLException {
	String[] rowdata, newrow;
	String[] colheaders = propTableData.get(0);
	
	//String[] rowheaders = propTableData.get(1);
	
	String[] tmpArray = {"data_set","value","date_collected","calmonth","calyear","day"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	newTableData.add(tmpArray);
	
	Calendar cal = Calendar.getInstance();
	
	for (int row=2;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		for (int col=0;col<colheaders.length;col++)
		{
			if (rowdata[col] == null)
				break;
			
			newrow = new String[tmpArray.length];
			
			newrow[0] = propStrTableDataSet;
			//This will have to be fixed if we ever get a 7 figure initial claims print
			newrow[1] = rowdata[col].replace("K","000");
			newrow[2] = "NOW()";
			//newrow[3] = "ue_rate";
			
			newrow[3] = Integer.toString(MoneyTime.convertMonthStringtoInt(colheaders[col]));
			newrow[4] = Integer.toString(cal.get(Calendar.YEAR));
			newrow[5] = colheaders[col].substring(colheaders[col].length()-2,colheaders[col].length());
		
		
			newTableData.add(newrow);
			
		}
	}
	
	propTableData = newTableData;
}

public void postProcessIMFGdpPPPActual() throws SQLException {
	
	String[] tmpArray = {"value","date_collected","entity_id","calyear","scale"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	String[] rowdata, newrow;
	//String[] colheaders = propTableData.get(0);
	
	String[] colheaders = propTableData.remove(0);
	String[] rowheaders = propTableData.remove(0);
	
	for (int row=0;row<propTableData.size();row++) {
		rowdata = propTableData.get(row);
		
		for (int col=0;col<colheaders.length;col++)
		{
		
		String strCountry = rowheaders[row];
		newrow = new String[tmpArray.length];
		
		if (rowdata[0].equals("n/a"))
		{
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Retrieved n/a value, skipping processing for entity " + strCountry,Logs.WARN,"PF44");
			continue;
		}
		
		newrow[0] = rowdata[col].replace(",", "");
		BigDecimal bdTmp = new BigDecimal(newrow[0]);
		
		newrow[0] = bdTmp.toString();

		newrow[1] = "NOW()";
		}
		
	}
	
}

public void postProcessIMFGdpPPPEst() throws SQLException {
	
}



public boolean preProcessImfGdp() {
	Calendar cal = Calendar.getInstance();
	int nMaxEndYear = 2016;
	int nMinBeginYear = 2004;
	
	int nTempCurrent = 2011;
	
	//dg.strStage1URL = dg.strStage1URL.replace("${dynamic8}", cal.get(Calendar.YEAR)+"");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic8}", nTempCurrent +"");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic6}", nMinBeginYear + "");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic7}", cal.get(Calendar.YEAR)+"");
	
	
	//dg.strStage1URL = dg.strStage1URL.replace("${dynamic5}", cal.get(Calendar.YEAR)+"");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic5}", nTempCurrent +"");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic3}", cal.get(Calendar.YEAR)+"");
	dg.strStage1URL = dg.strStage1URL.replace("${dynamic4}", nMaxEndYear + "");
	return true;

}

public void postProcessImfGdp() throws SQLException {
	
	String[] tmpArray = {"value","date_collected","entity_id","calyear","scale"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	//String[] rowdata, newrow;
	String[] newrow;
	//String[] colheaders = propTableData.get(0);
	
	String[] colheaders = propTableData.remove(0);
	String[] rowheaders = propTableData.remove(0);
	
	//newTableData.add(tmpArray);
	int nCounter=0;
	//for (int row=0;row<propTableData.size();row++)
	for (String[] rowdata : propTableData)	{
		//rowdata = propTableData.get(row);
		
		
		for (int col=0;col<colheaders.length;col++)		{
		
		String strCountry = rowheaders[nCounter];
		newrow = new String[tmpArray.length];
		
		newrow[0] = rowdata[col].replace(",", "");
		
		if (newrow[0].contains("n/a"))		{
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Retrieved n/a value, skipping processing for entity " + strCountry,Logs.WARN,"PF44");
			continue;
		}
		
		
		BigDecimal bdTmp = null;
		if (this.dg.nCurTask == 29) {
			if (!newrow[0].contains("bgcolor")) {
				bdTmp = new BigDecimal(newrow[0].substring(newrow[0].indexOf("right\">")+7,newrow[0].length()));
			}
			else
				continue;
		}
		else if (this.dg.nCurTask == 30) {
			if (newrow[0].contains("bgcolor")) {
				bdTmp = new BigDecimal(newrow[0].substring(newrow[0].indexOf("\">")+2,newrow[0].length()));
			}
			else
				continue;
		}
		else if (this.dg.nCurTask == 22){
			if (newrow[0].contains("bgcolor")) {
				bdTmp = new BigDecimal(newrow[0].substring(newrow[0].indexOf("\">")+2,newrow[0].length()));
			}
			else
				continue;
		}
		else if (this.dg.nCurTask == 33) {
			if (!newrow[0].contains("bgcolor")) {
				bdTmp = new BigDecimal(newrow[0].substring(newrow[0].indexOf("right\">")+7,newrow[0].length()));
			}
			else
				continue;
			
		}
		else {
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Task id not found, should not have reached this point in the code. Terminating processing of task.",Logs.ERROR,"PF44.32");
			return;
		}
			


		newrow[0] = bdTmp.toString();
		//newrow[0] = rowdata[0].replace(",", "");
		//newrow[4] = "VARCHAR";
		newrow[1] = "NOW()";
		//newrow[6] = "INTEGER";
	
		
		
		 * If there are 2 sets of parens for the row header, we want to cutoff at the 2nd left paren.
		 
		
		
	
		strCountry = strCountry.trim();
		
		strCountry = strCountry.replace("'", "\\'");
		
		if (strCountry.toUpperCase().equals("KOREA"))
			strCountry = "South Korea";
		
		//String query = "select * from entities where ticker='"+strCountry+"'";
		String query = "select entities.id from entities ";
		query += " join countries_entities on countries_entities.entity_id=entities.id ";
		query += " join countries on countries.id=countries_entities.country_id ";
		query += " where ticker='macro' ";
		query += " and countries.name='"+strCountry+"'";
			
		
		try
		{
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			newrow[2] = rs.getInt("entities.id") + "";
			
		}
		catch (SQLException sqle)
		{
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem looking up country: " + strCountry + ",row skipped",Logs.ERROR,"PF43.55");
			continue;	
			
			 * This is not a fatal error so we won't display the full exception.
			 
			//ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
		}
		
		newrow[3] = colheaders[col];
		newrow[4] = "9";
		
		
		
		newTableData.add(newrow);
		
		
			
		}
		nCounter++;
		
		
	}
	
	newTableData.add(0, tmpArray);
	propTableData = newTableData;
	
	
	
	
}

public void postProcessTableBLSUERate() throws CustomEmptyStringException, SQLException
{
	String[] rowdata, newrow;
	String[] colheaders = propTableData.get(0);
	String[] rowheaders = propTableData.get(1);
	
	String[] tmpArray = {"data_set","value","date_collected","calmonth","calyear"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	newTableData.add(tmpArray);
	
	for (int row=2;row<propTableData.size();row++)
	{
		rowdata = propTableData.get(row);
		
		for (int col=0;col<colheaders.length;col++)
		{
			if (rowdata[col] == null)
				break;
			
			newrow = new String[tmpArray.length];
			
			newrow[0] = propStrTableDataSet;
			newrow[1] = rowdata[col];
			newrow[2] = "NOW()";
			//newrow[3] = "ue_rate";
			
			newrow[3] = Integer.toString(MoneyTime.convertMonthStringtoInt(colheaders[col]));
			newrow[4] = rowheaders[row-2];
		
		
			newTableData.add(newrow);
			
		}
	}
	
	propTableData = newTableData;
	
	
	
}




public void postProcessYahooEPSEst() throws CustomEmptyStringException, SQLException
{
	
	
	//Integer nAdjQuarter = uf.retrieveAdjustedQuarter(Integer.parseInt(fiscalyearquarter.substring(0,1)),Integer.parseInt(fiscalyearquarter.substring(3,5)),strTicker);

	if (strDataValue.equals("N/A"))
	{
		ApplicationSetting.getInstance().getStdoutwriter().writeln("Retrieved N/A value, skipping",Logs.ERROR,"PF44");
		throw (new CustomEmptyStringException());
	}
	
	 * Negative values are enclosed in font changing tags which have to be removed
	 
	if (strDataValue.indexOf("<") != -1)
	{
		strDataValue = strDataValue.substring(strDataValue.indexOf(">")+1,strDataValue.length());
		strDataValue = strDataValue.substring(0,strDataValue.indexOf("<"));
	}
	
	String[] tmp = {"data_set","value","adj_quarter","ticker","date_collected","fiscalquarter","fiscalyear"};
	
	propTableData.add(0,tmp);
	
	//String fiscalyearquarter = uf.getFiscalYearAndQuarter(strTicker,-1,-1);
	
	//Integer nFiscalY = Integer.parseInt(fiscalyearquarter.substring(3,5));
	//Integer nFiscalQ = Integer.parseInt(fiscalyearquarter.substring(0,1));
	
	//String[] values=null;
	
	if (DataGrab.strCurDataSet.equals("yahoo_next_q_eps_est") || DataGrab.strCurDataSet.equals("yahoo_cur_q_eps_est"))
	{
		if (DataGrab.strCurDataSet.equals("yahoo_next_q_eps_est"))
		{
			if (nFiscalQ == 4)
			{
				nFiscalQ = 1;
				nFiscalY++;
			}
			else
				nFiscalQ++;
			
		}
		
		int nAdjQtr = uf.retrieveAdjustedQuarter(nFiscalQ,nFiscalY,strTicker);
		
		String[] values2 = {DataGrab.strCurDataSet, strDataValue, Integer.toString(nAdjQtr),strTicker, "NOW()", 
							Integer.toString(nFiscalQ),Integer.toString(nFiscalY)};
		values = values2;
	}
	else //strCurDataSet is either yahoo_next_y_eps_est or yahoo_cur_y_eps_est
	{
		
		if (DataGrab.strCurDataSet.equals("yahoo_next_y_eps_est"))
			nFiscalY++;
		String[] values2 = {DataGrab.strCurDataSet, strDataValue, "" ,strTicker, "NOW()", 
				"",Integer.toString(nFiscalY)};
		
		values = values2;
	}
	
	//propTableData.remove(1);
	//propTableData.add(values);

	
		
}

public boolean preProcessSECFiscalYear() throws SQLException
{
	
	 * Currently no analyst estimates for IVZ or LUK.
	 

	//String query;
	String strNewTicker="";
	
	 * Negative values are enclosed in font changing tags which have to be removed
	 
	if (strTicker.equals("BF/B"))
	{
		strNewTicker = "BFB";		
	}
	else if (strTicker.equals("BRK/A"))
	{
		strNewTicker = "BRKA";
	}
	
	 * Can't find the ticker symbol for CareFusion on the SEC site so using the CIK # instead
	 
	else if (strTicker.equals("CFN"))
	{
		strNewTicker ="0001457543";
	}
	else if (strTicker.equals("QEP"))
	{
		strNewTicker = "0001108827";
	}
	else if (strTicker.equals("SNI"))
	{
		strNewTicker = "0001430602";
	}
	else if (strTicker.equals("ZION"))
	{
		strNewTicker = "0000109380";
	}
	
	if (strNewTicker.equals("") == false)
	{
		dg.strCurrentTicker=strNewTicker;
	}
	
	
	 * Need to get rid of these return values and go with exceptions 
	 
	return(true);
	

	


		
}

public void postProcessSECFiscalYearEndRaw()
{
	 This is for populating actual_fiscal_year_end in the entities table 

	
	String[] tmp = {"ticker","actual_fiscal_year_end"};
		
	propTableData.add(0,tmp);
	
	String[] values = {strTicker,strDataValue};
		
		
	propTableData.remove(1);
	propTableData.add(values);
		
	dbf.updateTableIntoDB(propTableData,"entities");
	
}

public void postProcessSECBeginCalendarYear()
{
	 This is for populating begin_fiscal_calendar in the entities table 
	String strNewDataValue="";
	int nMonth = Integer.parseInt(strDataValue.substring(0,2));
	
	Tickers where the SEC has an incorrect value AZO,DV,EMN,FDO,GME,HES,JNJ,KMX,KO,KR,LLTC,NSC,PFG,PG,SPLS,VFC,WYNN,YUM 
	AZO=September
	PG=LLTC=DV=July
	YUM=WYNN=VFC=PFG=NSC=KO=HES=EMN=January
	FDO=September
	SPLS=KR=JNJ=GME=February
	KMX=March
	
	if ((strTicker.equals("AZO")) || (strTicker.equals("FDO")))
		nMonth=8;
	else if ((strTicker.equals("PG")) || (strTicker.equals("LLTC")) || (strTicker.equals("DV")))
		nMonth=6;
	else if ((strTicker.equals("YUM")) || (strTicker.equals("WYNN")) || (strTicker.equals("VFC")) || 
			(strTicker.equals("PFG")) || (strTicker.equals("NSC")) || (strTicker.equals("KO")) ||
			(strTicker.equals("HES")) || (strTicker.equals("EMN")))
		nMonth=12;
	else if (strTicker.equals("KMX"))
		nMonth=2;
	else if ((strTicker.equals("SPLS")) || (strTicker.equals("KR")) || (strTicker.equals("JNJ")) || 
			(strTicker.equals("GME")))
		nMonth=1;
	
		
	
	
	
	These are mostly retail stocks whose fiscal calendar doesn't end directly at the end of a month 
	if ((strTicker.equals("AVY")) || (strTicker.equals("CERN")) || (strTicker.equals("CPB"))
		|| (strTicker.equals("CSC")) || (strTicker.equals("DIS")) || (strTicker.equals("JEC"))
		|| (strTicker.equals("JNJ"))|| (strTicker.equals("K"))|| (strTicker.equals("MU"))
		|| (strTicker.equals("SNA")) || (strTicker.equals("SNDK")) || (strTicker.equals("SWK"))
		|| (strTicker.equals("SWY"))|| (strTicker.equals("SYMC"))|| (strTicker.equals("TXT"))
		|| (strTicker.equals("VAR")) || (strTicker.equals("VFC")) || (strTicker.equals("XLNX")))
		nMonth--;
	
	//boundary condition
	if (nMonth==0)
		nMonth=12;
	
	if (nMonth == 1)
		strNewDataValue = "February";
	else if (nMonth == 2)
		strNewDataValue = "March";
	else if (nMonth == 3)
		strNewDataValue = "April";
	else if (nMonth == 4)
		strNewDataValue = "May";
	else if (nMonth == 5)
		strNewDataValue = "June";
	else if (nMonth == 6)
		strNewDataValue = "July";
	else if (nMonth == 7)
		strNewDataValue = "August";
	else if (nMonth == 8)
		strNewDataValue = "September";
	else if (nMonth == 9)
		strNewDataValue = "October";
	else if (nMonth == 10)
		strNewDataValue = "November";
	else if (nMonth == 11)
		strNewDataValue = "December";
	else if (nMonth == 12)
		strNewDataValue = "January";
	else
		ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem converting month to begin calendar year month",Logs.ERROR,"PF44.5");
		
	

		
		String[] tmp = {"ticker","begin_fiscal_calendar"};
		
		propTableData.add(0,tmp);
				
		String[] values = {strTicker,strNewDataValue};
				
		propTableData.remove(1);
		propTableData.add(values);
		
		dbf.updateTableIntoDB(propTableData,"entities");
		
	
}

public void postProcessYahooBeginCalendarYear()
{
	Created these 2 yahoo data captures because there were so many errors in the SEC data.
	 * Your tax dollars at work.
	 
	int nMonth = MoneyTime.convertMonthStringtoInt(strDataValue.substring(0,3));
	
	These are mostly retail stocks whose fiscal calendar doesn't end directly at the end of a month 
	if ((strTicker.equals("AVY")) || (strTicker.equals("CERN")) || (strTicker.equals("CPB"))
		|| (strTicker.equals("CSC")) || (strTicker.equals("DIS")) || (strTicker.equals("JEC"))
		|| (strTicker.equals("JNJ"))|| (strTicker.equals("K"))|| (strTicker.equals("MU"))
		|| (strTicker.equals("SNA")) || (strTicker.equals("SNDK")) || (strTicker.equals("SWK"))
		|| (strTicker.equals("SWY"))|| (strTicker.equals("SYMC"))|| (strTicker.equals("TXT"))
		|| (strTicker.equals("VAR")) || (strTicker.equals("VFC")) || (strTicker.equals("XLNX")))
		nMonth--;
	

	
	//We have end of year, need beginning of year 
	nMonth++;
	
	//boundary condition
	if (nMonth==13)
		nMonth=1;
	
	String strMonth = MoneyTime.convertMonthInttoString(nMonth);
	
	String[] tmp = {"ticker","begin_fiscal_calendar"};
	
	propTableData.add(0,tmp);
			
	String[] values = {strTicker,strMonth};
			
	propTableData.remove(1);
	propTableData.add(values);
	
	dbf.updateTableIntoDB(propTableData,"entities");
	
	
	
}

public void postProcessYahooCompanyName()
{
	String[] tmp = {"ticker","full_name"};
	
	propTableData.add(0,tmp);
	
	String[] values = {dg.strOriginalTicker,propTableData.get(1)[0]};
	
	propTableData.remove(1);
	propTableData.add(values);
	
	dbf.updateTableIntoDB(propTableData, "entities");
	
}

public void postProcessYahooFiscalYearEndRaw()
{
	Created these 2 yahoo data captures because there were so many errors in the SEC data.
	 * Your tax dollars at work.
	 
	
	String[] tmp = {"ticker","actual_fiscal_year_end"};
	
	propTableData.add(0,tmp);
	
	String[] values = {strTicker,strDataValue};
		
		
	propTableData.remove(1);
	propTableData.add(values);
		
	dbf.updateTableIntoDB(propTableData,"entities");
	

}

public void postProcessGoogleEPSTable() throws SQLException {


		String strTicker = dg.strOriginalTicker;

		String[] rowdata, newrow;
		String[] colheaders = propTableData.get(0);
		//String[] rowheaders = propTableData.get(1);
		
		
		ArrayList<String[]> newTableData = new ArrayList<String[]>();
		String[] tmpArray = {"value","date_collected","entity_id","fiscalquarter","fiscalyear","calquarter","calyear"};
		newTableData.add(tmpArray);
		String tmpVal;
		for (int row=2;row<propTableData.size();row++)
		{
			rowdata = propTableData.get(row);
			
			for (int col=0;col<colheaders.length;col++)
			{
				newrow = new String[16];
				if (rowdata[col].compareTo("void") != 0)
				{
				
					//newrow[0] = propStrTableDataSet;
					
					tmpVal = rowdata[col];
					
					if (tmpVal.contains("span"))
					//this is a negative number
						newrow[0] = tmpVal.substring(tmpVal.indexOf(">-")+1,tmpVal.indexOf("</"));
					else
						newrow[0] = tmpVal;
					
					
					 * Some tickers have tables with only 4 columns. The table
					 * processing will wrap around to the next row in those cases.
					 
					if (newrow[0].contains("Diluted"))
						continue;
					
					//Berkshire tends to be the only company with an EPS in the thousands.
					newrow[0] = newrow[0].replace(",", "");
			
				
					newrow[1] = "NOW()";
					
					newrow[2] = dg.nCurrentEntityId + "";
					
					
					 * OFP 11/12/2011 - I have to deal with this wacky issue where google has the end of quarter
					 * days off by one in some instances. 
					 * 
					 * CSC is one example. For calendar quarter 2, cal year 2011, they have the end date listed as 7/1/2011
					 * instead of 6/30/2011. 
					 * 
					 * So as a workaround, if the Day of month is 1, I'm going to subtract one day from the date.
					 * 
					 * It's not known at this time if this will screw up other tickers.
					 * 
					 * If other tickers behave differently then I may have to set up ticker specific code in here.
					 
					
					Calendar cal = Calendar.getInstance();
					String[] date = colheaders[col].split("-");
					
					cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
					cal.set(Calendar.MONTH,Integer.parseInt(date[1])-1);
					cal.set(Calendar.YEAR,Integer.parseInt(date[0]));
					
					if (cal.get(Calendar.DAY_OF_MONTH)==1)
						cal.add(Calendar.DAY_OF_MONTH,-1);
					
					//String strFiscalYearQuarter = MoneyTime.getFiscalYearAndQuarter(strTicker,Integer.parseInt(colheaders[col].substring(5,7)), Integer.parseInt(colheaders[col].substring(0,4)),dbf);
					String strFiscalYearQuarter = MoneyTime.getFiscalYearAndQuarter(strTicker, cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR), dbf);
					String strCalYearQuarter = MoneyTime.getCalendarYearAndQuarter(strTicker, Integer.parseInt(strFiscalYearQuarter.substring(0,1)), Integer.parseInt(strFiscalYearQuarter.substring(1,5)),dbf);
					
				
					newrow[3] = strFiscalYearQuarter.substring(0,1);
					
					don't use they year returned from getFiscalYearAndQuarter - use the one retrieved from the web page
				
					newrow[4] = strFiscalYearQuarter.substring(1,5);
					
					
					newrow[5] = strCalYearQuarter.substring(0,1);
					newrow[6] = strCalYearQuarter.substring(1,5);
						
					newTableData.add(newrow);
					
				}
				
				
			}
			
		}
		propTableData = newTableData;

	
}

public void postProcessGasolineEurope() {

	String[] rowdata, newrow;
	final String strDefaultTime = " 14:00:00";
	
	
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	String[] tmpArray = {"value","date_collected","entity_id"};
	
	newTableData.add(tmpArray);
	String strRegex = "(?i)(AND Andorra)";
	Pattern pattern = Pattern.compile(strRegex);
	String strContent = propTableData.get(0)[0];
	

	Matcher matcher = pattern.matcher(strContent);
	
	matcher.find();
	matcher.start();
	
	Scanner scanner = new Scanner(strContent);
	
	 * findWithinHorizon is case sensitive
	 
	scanner.findWithinHorizon("Information Web services /", 0);
	String strDate=scanner.nextLine();
	strDate = strDate.replace("</p>", "").trim();
	String[] date = strDate.split("\\.");
	
	scanner = new Scanner(strContent);
	scanner.findWithinHorizon("Average Price Currency",0);
	scanner.nextLine();

	while (scanner.hasNextLine()) {
	  
	  String strLine = scanner.nextLine();
	  
	  if (strLine.contains("Europa: Fuel Prices"))
		  break;
	  
	  if (strLine.contains("</p>") ||
		  strLine.contains("Portugal /") ||
		  strLine.contains("Canary Islands"))
		  continue;
	  
	  strLine = strLine.replace("Bosnia and Herzegovina", "Bosnia_and_Herzegovina");
	  strLine = strLine.replace("Czech Republic","Czech_Republic");
	  strLine = strLine.replace("Great Britain", "Great_Britain");
	  strLine = strLine.replace("Ireland (Eire)", "Ireland");
	 // strLine = strLine.replace("Spain / Canary Islands", "Canary_Islands");
  
	  newrow = new String[tmpArray.length];
	  //we're using an arbitrary time
	  newrow[1] = "'" + date[2] + "-" + date[1] + "-" + date[0] + strDefaultTime + "'";
	  
	  
	   * For some countries we have to add a dummy token to get the split() to work correctly.
	   
	  if (strLine.contains("Portugal") || strLine.contains("Spain"))
		  strLine = "<token> " + strLine;
	  
	  String[] tokens = strLine.split(" ");
	  
	  // Country: tokens[1]
	  // Currency: tokens[2]
	  // Price: tokens[3] (for Finland and Germany, reduce by 10% since they don't have 95 octane)
	  
	  String strCountry = tokens[1].replace("_", " ").replace("<p>", "").trim();
	  
	  String strQuery = "select entities.id from entities ";
	  strQuery += " join countries_entities on countries_entities.entity_id = entities.id ";
	  strQuery += " left join country_aliases on country_aliases.country_id=countries_entities.country_id ";
	  strQuery += " left join countries on countries.id=countries_entities.country_id ";
	  strQuery += " where ticker='macro'";
	  strQuery += " and (alias='" + strCountry + "' OR countries.name='" + strCountry + "') ";
		
		
	  try	{
		  ResultSet rs = dbf.db_run_query(strQuery);
		  rs.next();
		  newrow[2] = rs.getInt("id") + "";
		
	  }
	  catch (SQLException sqle)	{
		  ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem looking up country name or alias: " + strCountry + ",row skipped",Logs.ERROR,"PF200.25");
		  continue;	
	  }
	  
	  
	  try {
		  BigDecimal bdPrice = UtilityFunctions.convertToGallonsAndDollars(tokens[3], "USD" + tokens[2], newrow[1], dbf);
		  
		  if (strCountry.equalsIgnoreCase("Germany") || strCountry.equalsIgnoreCase("Finland"))
			  bdPrice = bdPrice.multiply(new BigDecimal(".970").setScale(3));
		  
		  newrow[0] = bdPrice.toString();
	  }
	  catch (SQLException sqle) {
		  ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem converting to gallons and dollars for currency cross: USD" + tokens[2] + ",row skipped",Logs.WARN,"PF200.25");
		  continue;	
	  }
	  
	  strQuery = "select value from fact_data ";
	  strQuery += " join entities on entities.id=fact_data.entity_id ";
	  strQuery += " where ticker='USD" + tokens[2] + "'";
	  strQuery += " and date_collected<" + newrow[1];
	  strQuery += " order by date_collected desc";
	  strQuery += " limit 1";
	  
	  try	{
		  ResultSet rs = dbf.db_run_query(strQuery);
		  rs.next();
		  BigDecimal bdRate = rs.getBigDecimal("value");
		  BigDecimal bdPrice = new BigDecimal(tokens[3]);
		  bdRate.setScale(3);
		  bdPrice.setScale(3);
		  //need to adjust for Germany and Finland since they don't sell 95 octane. 
		  //We're using an adjustment of -3% from 98 octane.
		  if (strCountry.equalsIgnoreCase("Germany") || strCountry.equalsIgnoreCase("Finland"))
			  bdPrice = bdPrice.multiply(new BigDecimal(".970").setScale(3));
		  bdPrice = bdPrice.divide(bdRate,BigDecimal.ROUND_UP);
		  bdPrice = bdPrice.divide(UtilityFunctions.bdGallonsPerLiter,BigDecimal.ROUND_UP);
		  newrow[0] = bdPrice.toString();
	  }
	  catch (SQLException sqle)	{
		  ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem looking up exchange rate for currency cross: USD" + tokens[2] + ",row skipped",Logs.WARN,"PF200.25");
		  continue;	
	  }
	  
	  
	  
	  newTableData.add(newrow);
	  
	  
	  
	}
	
	this.propTableData = newTableData;


	
	//(?i)(<TABLE[^>]*>)
	
	
	</p>
	<p>A Austria EUR 1.431      1.565      1.422      1.00000 1.431      1.565      1.422      1.73        1.89        1.71        
	B Belgium EUR 1.713      1.733      1.550      1.00000 1.713      1.733      1.550      2.07        2.09        1.87        
	</p>
	
	
	 * 1. Regex to AND Andorra
	 * 2. readline() until Europa: Fuel Prices, skipping </p> lines
	 * 3. Regex to Information Web Services 
	 * 4. Grab the data
	 
	
}

public void postProcessGasolineUSCanada() throws SQLException {
	
	String[] tmpArray = {"value","date_collected","entity_id"};
	Calendar calToday = Calendar.getInstance();
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	String[] data = propTableData.get(1);
	
	
	for (int i=0;i<data.length;i++) {
		
		String[] newrow = new String[tmpArray.length];
		
		newrow[1] = "'" + formatter.format(calToday.getTime()) + "'";
		
		String strCountry = null;
		if (i==0) {
			newrow[0] = data[i];
			strCountry = "United States";
		}
		else if (i==1){
			
			strCountry = "Canada";
			
			Have to convert liters to gallons and Canadian $s to US $s
			
			try {
				BigDecimal bdPrice = UtilityFunctions.convertToGallonsAndDollars(data[i], "USDCAD", newrow[1], dbf);
				bdPrice = bdPrice.divide(new BigDecimal("100"),BigDecimal.ROUND_HALF_UP);
				newrow[0] = bdPrice.toString();
				
			}
			catch (SQLException sqle) {
				ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem converting to Gallons and Dollars, Country " + strCountry + " skipped",Logs.WARN,"PF200.25");
				continue;	
			}
				
			
		}
		
		String query = "select entities.id from entities ";
		query += " join countries_entities on countries_entities.entity_id=entities.id ";
		query += " join countries on countries.id=countries_entities.country_id ";
		query += " where ticker='macro' ";
		query += " and countries.name='"+strCountry+"'";
		
		try		{
			ResultSet rs = dbf.db_run_query(query);
			rs.next();
			newrow[2] = rs.getInt("id") + "";
			
		}
		catch (SQLException sqle)	{
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem looking up country: " + strCountry + ",row skipped",Logs.ERROR,"PF300.25");
			continue;	
			
			 * This is not a fatal error so we won't display the full exception.
			 
			//ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
		}
		
			
		newTableData.add(newrow);
		
	}
	
	newTableData.add(0, tmpArray);
	propTableData = newTableData;
	
	
}

public void postProcessWikipediaGasoline() throws SQLException {

		final String strDefaultTime = " 14:00:00";
		String[] tmpArray = {"value","date_collected","entity_id"};
		
		ArrayList<String[]> newTableData = new ArrayList<String[]>();
		//String[] rowdata, newrow;
		String[] newrow;
		//String[] colheaders = propTableData.get(0);
		
		String[] colheaders = propTableData.remove(0);
		String[] rowheaders = propTableData.remove(0);
		
		int nCounter=0;
		
		
		 * This is kludgy but don't know of a better way to do this atm. With the way
		 * we are currently reading in countries, we get two identical Vietnams and we only want the
		 * first one.
		 * 
		 * Actually the specialized vietname code isn't even being used right now because the 2nd
		 * vietnam generates a pikefinvoid because of the missing date. But if they ever put the date
		 * back in, then we'll need the specialized code so I left it in.
		 
		int nVietnamCount = 0;

		for (String[] rowdata : propTableData)	{
			
			String strCountry = rowheaders[nCounter++];
			
			if (strCountry.equalsIgnoreCase("Anguilla") || strCountry.equalsIgnoreCase("Aruba")
				||	strCountry.equalsIgnoreCase("Bonaire")
				||	strCountry.equalsIgnoreCase("british virgin islands")
				||	strCountry.equalsIgnoreCase("curacao")
				||	strCountry.equalsIgnoreCase("european union")
				||	strCountry.equalsIgnoreCase("guadeloupe")
				||	strCountry.contains("Livigno")
				||	strCountry.equalsIgnoreCase("martinique")
				||	strCountry.equalsIgnoreCase("montserrat")
				||	strCountry.equalsIgnoreCase("portugal - azores")
				||	strCountry.equalsIgnoreCase("portugal - madeira")
				||	strCountry.equalsIgnoreCase("puerto rico")
				||	strCountry.equalsIgnoreCase("spain - canary islands")
				||	strCountry.equalsIgnoreCase("Switzerland - Samnaun")
				||	strCountry.equalsIgnoreCase("Turks and Caicos")
				||  rowdata[0].contains("pikefinvoid")
				
			)
				continue;
			
			newrow = new String[tmpArray.length];
			
			if (strCountry.equalsIgnoreCase("United States")) {
				String strBeforeToken = ">$";
				newrow[0] = rowdata[0].substring(rowdata[0].indexOf(strBeforeToken) + strBeforeToken.length(),rowdata[0].indexOf("/US gallon"));
			}
			else {
			
			String strBeforeToken = "right\">";
			String strAfterToken = "<";
			
			if (strCountry.contains("Venezuela")) {
				strBeforeToken = ">";
				strAfterToken = "(95)";
				
			}

			
					
			newrow[0] = rowdata[0].substring(rowdata[0].indexOf(strBeforeToken) + strBeforeToken.length(),rowdata[0].length());
			
			if (newrow[0].contains(strAfterToken))
				newrow[0] = newrow[0].substring(0,newrow[0].indexOf(strAfterToken));
			
			newrow[0] = newrow[0].trim();

			
			
			
			 * 2/25/2012 - Right now one of the dates (Oman) has octane info in the cell as well.
			 
			String strDateTmp = rowdata[1];
			if (strDateTmp.contains("("))
				strDateTmp = strDateTmp.substring(0,strDateTmp.indexOf("("));
			strDateTmp = strDateTmp.trim();
			newrow[1] = "'"+strDateTmp + strDefaultTime + "'";

			
			 * If there are 2 sets of parens for the row header, we want to cutoff at the 2nd left paren.
			 

			strCountry = strCountry.trim();
			
			if (strCountry.equalsIgnoreCase("vietnam")) {
				if (nVietnamCount == 1)
					continue;
				else
					nVietnamCount++;
			}
			
			
			strCountry = strCountry.replace("'", "\\'");

			String query = "select entities.id from entities ";
			query += " join countries_entities on countries_entities.entity_id=entities.id ";
			query += " join countries on countries.id=countries_entities.country_id ";
			query += " where ticker='macro' ";
			query += " and countries.name='"+strCountry+"'";
			query += " union ";
			query += " select entities.id from entities ";
			query += " join countries_entities on countries_entities.entity_id=entities.id ";
			query += " join country_aliases on country_aliases.country_id=countries_entities.country_id ";
			query += " where ticker='macro' ";
			query += " and country_aliases.alias='"+strCountry+"'";
						
			
			try		{
				ResultSet rs = dbf.db_run_query(query);
				rs.next();
				newrow[2] = rs.getInt("id") + "";
				
			}
			catch (SQLException sqle)	{
				ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem looking up country: " + strCountry + ",row skipped",Logs.ERROR,"PF99.25");
				continue;	
				
				 * This is not a fatal error so we won't display the full exception.
				 
				//ApplicationSetting.getInstance().getStdoutwriter().writeln(sqle);
			}
			
			//newrow[3] = colheaders[col];
			//newrow[4] = "9";
			
			
			
			newTableData.add(newrow);
			
			
			
			}
			
			
			
	//	}
		
		newTableData.add(0, tmpArray);
		propTableData = newTableData;
		
		
		
		
	}






	public boolean preNDCYahooEPSEst() {
		String strRegex = "(?i)(There is no Analyst Estimates)";
		  ApplicationSetting.getInstance().getStdoutwriter().writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF45");

		  
		  Pattern pattern = Pattern.compile(strRegex);
		  //ApplicationSetting.getInstance().getStdoutwriter().writeln("after strbeforeuniquecoderegex compile", Logs.STATUS2);
		  
		  Matcher matcher = pattern.matcher(dg.returned_content);
		  
		  //ApplicationSetting.getInstance().getStdoutwriter().writeln("Current offset before final data extraction: " + nCurOffset,Logs.STATUS2);
		  
		  return(matcher.find());
		
	}

	public boolean preNDCNasdaqEPSEst()	{
		
	  String strRegex = "(?i)(No Data Available)";
	  ApplicationSetting.getInstance().getStdoutwriter().writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF46");

	  
	  Pattern pattern = Pattern.compile(strRegex);
	  
	  
	  Matcher matcher = pattern.matcher(dg.returned_content);
	  
	  
	   * OFP 12/19/2012 - Nasdaq misspelled available.
	   
	  String strRegex2 = "(?i)(No data avaiable)";
	  Pattern pattern2 = Pattern.compile(strRegex2);
	  Matcher matcher2 = pattern2.matcher(dg.returned_content);
	  
	  if (matcher.find() || matcher2.find()) {
		  ApplicationSetting.getInstance().getStdoutwriter().writeln("Nasdaq no data avialable for ticker " + dg.strCurrentTicker, Logs.WARN,"PF21.38");
		  return(true);
	  }
	  
	  strRegex = "(?i)(feature currently is unavailable)";
	  ApplicationSetting.getInstance().getStdoutwriter().writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF46.5");

	  
	  pattern = Pattern.compile(strRegex);
	  //ApplicationSetting.getInstance().getStdoutwriter().writeln("after strbeforeuniquecoderegex compile", Logs.STATUS2);
	  
	  matcher = pattern.matcher(dg.returned_content);
	  
	  if (matcher.find()) {
		  ApplicationSetting.getInstance().getStdoutwriter().writeln("Nasdaq message feature currently unavailable for ticker " + dg.strCurrentTicker, Logs.WARN,"PF21.40");
		  return true;
	  }
	  
	  return false;
	 
	  
	  
	  
	  

	}
	
	public boolean preNDCNasdaqFiscalYear()
	{
		String strRegex = "(?i)(This feature currently is unavailable)";
		ApplicationSetting.getInstance().getStdoutwriter().writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF47");
		
		Pattern pattern = Pattern.compile(strRegex);
		
		Matcher matcher = pattern.matcher(dg.returned_content);
		
		return(matcher.find());
		
	}	*/
}







