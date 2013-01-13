package com.pikefin.services;
 
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.dao.DataAccessException;

import pikefin.log4jWrapper.Logs;

import com.pikefin.ApplicationSetting;
import com.pikefin.businessobjects.Job;
import com.pikefin.exceptions.CustomEmptyStringException;
import com.pikefin.exceptions.SkipLoadException;
import com.pikefin.services.inter.BatcheService;
import com.pikefin.services.inter.EntityService;
import com.pikefin.services.inter.JobService;

// GENERAL COMMENTS ON PROCESSING FUNCTIONS
// OFP 12/13/2010: Since the data_group field was removed from the fact_table 
// (because it can be referenced in the jobs table through the data_set), it
// was removed from all processing functions that were inserting it into 
// the fact_table.
// 
// NOTES on Stage1 and Stage2 URL fields in DataGrab: 
// Stage 2 url is for group processing where the ${ticker} field
// has to be updated for each group member. This happens in 
// defaultURLProcessing().
// 
// Stage 1 url (initially) contains the url string as it was read from the
// database.
// 
// Don't populate the Stage 2 url in a preprocessing function. It will get
// overwritten in defaultURLProcessing().

class ProcessingFunctionsOliver {
  //@Autowired
  private JobService jobService=ApplicationSetting
      .getInstance().getApplicationContext().getBean(JobService.class);
  private EntityService entityService=ApplicationSetting
      .getInstance().getApplicationContext().getBean(EntityService.class);
  private BatcheService batchService=ApplicationSetting
      .getInstance().getApplicationContext().getBean(BatcheService.class);

	String strDataValue;

	//Right now this is only set during the preProcessing function
	String strTicker;
	String strTemp1, strTemp2, strTemp3;
	
	Job j;
	
	//table extraction processing function parameters
	ArrayList<String[]> propTableData;
	String propStrTableDataSet;
	
	DataGrabExecutor dg; 
		
	public ProcessingFunctionsOliver(DataGrabExecutor tmpDG) {
		this.dg = tmpDG;
	}
	
	public boolean preNoDataCheck(String dataSet) {
	  try {
	    Job job=jobService.getJobByDataSet(dataSet);
	    String strFunctionName =job.getPreNoDataCheckFunc();
	    ApplicationSetting.getInstance().getStdoutwriter().
	      writeln("Pre No Data Check function name: "
	      + strFunctionName,Logs.STATUS2,"PF1");
	    if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
	      ApplicationSetting.getInstance()
	        .getStdoutwriter().writeln("No data check function, " +
	        "assuming data is ok",Logs.STATUS2,"PF2");
	      return(false);
	    }
	    Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
	    return((Boolean)m.invoke(this, new Object[] {}));
	  }
	  catch (Exception e) {
	    ApplicationSetting
	      .getInstance()
	      .getStdoutwriter()
	      .writeln("preNoDataCheck method call failed",Logs.ERROR,"PF3");
	    ApplicationSetting.getInstance().getStdoutwriter().writeln(e);
	    return(false);

	  }
	}
		
	public boolean preJobProcessing(Job j) {
	  try {
	    String strFunctionName = j.getPreJobProcessFuncName();

	    ApplicationSetting.getInstance().getStdoutwriter()
	      .writeln("Pre Job Process Func Name: "
	      + strFunctionName,Logs.STATUS2,"PF3.2");
	    if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
	      ApplicationSetting.getInstance().getStdoutwriter()
	        .writeln("No pre job process" +
	        " function, exiting...",Logs.STATUS2,"PF3.3");
	      return(true);
	    }
	    ApplicationSetting
	      .getInstance()
	      .getStdoutwriter().writeln(strFunctionName,Logs.STATUS2,"PF3.35");
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
		  
  public boolean postJobProcessing(Job j) {
	  try {
	    String strFunctionName = j.getPostJobProcessFuncName();
	    ApplicationSetting.getInstance().getStdoutwriter()
	      .writeln("Post Job Process Func Name: " 
	      + strFunctionName,Logs.STATUS2,"PF3.5");
	    if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
	      ApplicationSetting
	        .getInstance()
	        .getStdoutwriter()
	        .writeln("No post job process function, " +
	        "exiting...",Logs.STATUS2,"PF3.7");
	      return(true);
	    }
	    ApplicationSetting.getInstance().getStdoutwriter()
	      .writeln(strFunctionName,Logs.STATUS2,"PF3.8");
	    Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
	    return((Boolean)m.invoke(this, new Object[] {}));
	  }
	  catch (Exception tmpE) {
	    ApplicationSetting
	      .getInstance()
	      .getStdoutwriter()
	      .writeln("post Job Processing method call failed",Logs.ERROR,"PF3.9");
	    ApplicationSetting.getInstance().getStdoutwriter().writeln(tmpE);
	    return(false);
	  }
	}
  
  public boolean preProcessing(Job j, String strTicker) {
    try {
      String strFunctionName = j.getPreProcessFuncName();
      ApplicationSetting.getInstance().getStdoutwriter()
        .writeln("Pre Process Func Name: "+strFunctionName,Logs.STATUS2,"PF4");
      this.strTicker = strTicker;
      if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
        ApplicationSetting.getInstance().getStdoutwriter()
          .writeln("No pre process function, exiting...",Logs.STATUS2,"PF5");
        return(true);
      }
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

  public ArrayList<String []> postProcessing(ArrayList<String []> tabledata ,
      Job inputJob) throws SkipLoadException,CustomEmptyStringException {

    if (tabledata.get(0) != null)
      strDataValue = tabledata.get(0)[0];
    try {
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
    // Need to break this down into individual exceptions 
    catch (IllegalAccessException iae) {
      ApplicationSetting.getInstance().getStdoutwriter()
        .writeln("postProcessing method call failed",Logs.ERROR,"PF16.1");
      ApplicationSetting.getInstance().getStdoutwriter().writeln(iae);

    }
    //any exceptions thrown by the function pointer are wrapped in an 
    // InvocationTargetException
    catch (InvocationTargetException ite) {
      if (ite.getTargetException().getClass().getSimpleName()
          .equals("SkipLoadException"))
        throw new SkipLoadException();
      else if (ite.getTargetException().getClass().getSimpleName()
          .equals("CustomEmptyStringException")) {
        // TODO: pass along the exception message.
        throw new CustomEmptyStringException();
      }
      else{ 
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

  // This function is still here for historical data loads.
  /*
  public void postProcessYahooSharePrice() throws SQLException {
    String[] strTmpValue = propTableData.get(0);

    String[] values = strTmpValue[0].split(",");

    String[] tmpArray = {"value","date_collected","entity_id"};
    String[] rowdata = new String[tmpArray.length];
    
    if (DataLoad.bLoadingHistoricalData==false) {
      rowdata[0] = values[0];
      rowdata[1] = "NOW()";
    }
    else {
      // Use market close in arizona time zone
      rowdata[0] = values[4];
      rowdata[1] = "'" + values[0] + " 14:00:00'";
    }
    rowdata[2] = dg.nCurrentEntityId + "";
   
    propTableData.remove(0);
    propTableData.add(tmpArray);
    propTableData.add(rowdata);

  }
*/
  
  /*
  public void postProcessCNBCCDSJson() throws SQLException {

    String[] tmpArray = {"value","date_collected","entity_id"};
    String[] rowdata;

    try {
      JSONObject jsonTop = new JSONObject(this.propTableData.get(0)[0]);

      propTableData.remove(0);
      propTableData.add(tmpArray);
      JSONObject jObject = jsonTop.getJSONObject("QuickQuoteResult");
      JSONArray jArray = jObject.getJSONArray("QuickQuote");

      for (int i=0;i<jArray.length();i++) {

        rowdata = new String[tmpArray.length];
        jObject = jArray.getJSONObject(i);
        String strSymbol = (String)jObject.get("symbol");
        rowdata[0] = (String)jObject.get("last");
        String query = "select * from entity_aliases where ticker_alias='" 
            + strSymbol + "'";
        String strEntityIndex;

        try {
          ResultSet rs = dbf.db_run_query(query);
          rs.next();
          strEntityIndex = rs.getInt("entity_id") + "";		
        }
        catch (SQLException sqle) {
          UtilityFunctions.stdoutwriter
            .writeln("Problem looking up ticker alias: " 
            + strSymbol + ",row skipped",Logs.WARN,"PF652.34");
          continue;
        }

        rowdata[2] = strEntityIndex;

        // Before I was using the collection date/time, and will continue to
        // do so for now. At some point may switch over to using the provided 
        // last trade date/time.
        //String str3 = (String)jObject.get("last_time");
        
        rowdata[1] = "NOW()";
        propTableData.add(rowdata);
      }
     
    }
    catch (JSONException jsone) {
      UtilityFunctions.stdoutwriter
        .writeln("Issue parsing JSON data, task aborted.",
        Logs.ERROR,"PF1001.11");
      UtilityFunctions.stdoutwriter.writeln(jsone);
    }

  }
*/
  
  /*
  public void postProcessYahooSharePriceYQL() throws SQLException {
    // OFP 2/24/2012 - The Yahoo processsing is really convoluted now. We 
    // splice together up to 5 xml streams and then we have to strip out the 
    // xml header and tail before putting it through the SAX parser. 
    // 
	  // There are 2 reasons for all this complexity:
    // 1) Issue with invalid data retrieval from yahoo where we have to check 
    // the timestamp and if wrong resend the url request.
    // 2) YQL has a limit where we can only submit 100 tickers at a time (or
    // this might be a limit on the URL size, can't remember which).
    // 
    // One other way to handle this situation is to break up the entity_group 
    // into 5 different groups and then have 5 different jobs, 1 for each 
    // group, under the parent task. But in order to do this, groups need to 
    // be associated with jobs, not tasks, which I think is how it should 
    // be anyways.
    
    String strTmpValue = propTableData.get(0)[0];

    // Replace all line separators, carriage returns, whatnot
    strTmpValue = strTmpValue.replaceAll("\\r|\\n", "");

    int nBeginMerge = strTmpValue.indexOf("<results>") + 9;

    int nCountMerge = 0;
    while (true) {
      if ((nBeginMerge = strTmpValue.indexOf("<results>",nBeginMerge+1)) != -1)
        nCountMerge++;
      else
        break;
    }

    nBeginMerge = strTmpValue.indexOf("<results>") + 9;
    int nEndMerge=0;

    while (nCountMerge > 0) {
      if ((nBeginMerge = strTmpValue.indexOf("</results>",nBeginMerge)) == -1) {
        //shouldn't get here
        UtilityFunctions.stdoutwriter
          .writeln("Issue merging XML data",Logs.ERROR,"PF998.5");
        break;
      }

      if ((nEndMerge = strTmpValue.indexOf("<results>",nBeginMerge)) == -1) {
        //shouldn't get here
        UtilityFunctions.stdoutwriter
          .writeln("Issue merging XML data",Logs.ERROR,"PF998.6");
        break;
      }
      nEndMerge += "<results>".length();
      String strReplace = strTmpValue.substring(nBeginMerge,nEndMerge);
      
      // replaceFirst uses regex so we have to replace backslash and dollar 
      // with escaped characters
      // strReplace = strReplace.replace("\\","\\\\");
		  // strReplace = strReplace.replace("$", "\\$");
		  // strReplace = strReplace.replace("?", "\\?");
       
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
          Attributes attributes) throws SAXException {
        if(qName.equalsIgnoreCase("quote")) {
          //create a new instance of employee
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
    //TODO Eventually throw these exceptions outside of the function.
    catch(ParserConfigurationException pce) {
      UtilityFunctions.stdoutwriter
        .writeln("PostProcessing Exception",Logs.ERROR,"PF1001.1");
      UtilityFunctions.stdoutwriter.writeln(pce);
    }
    catch(SAXException saxe) {
      String[] tmp = new String[3];
      tmp[0]="Task:"+this.dg.nCurTask;
      tmp[1]="URL:"+this.dg.strStage2URL;
      tmp[2]=this.dg.returned_content;
      UtilityFunctions.dumpStrings(tmp, "/tmp/dump/dumpstring.txt");
      UtilityFunctions.stdoutwriter
        .writeln("PostProcessing Exception",Logs.ERROR,"PF1001.2");
      UtilityFunctions.stdoutwriter.writeln(saxe);
    }
    catch(IOException ioe) {
      UtilityFunctions.stdoutwriter
        .writeln("PostProcessing Exception",Logs.ERROR,"PF1001.3");
      UtilityFunctions.stdoutwriter.writeln(ioe);
    }
    
    boolean bDone = false;
    propTableData.remove(0);
    String[] tmpArray = {"value","date_collected","entity_id"};
    propTableData.add(tmpArray);

    int nBegin=0, nEnd = 0, nCount = 0, nCount2 = 0;

    for (int i=0;i<handler.quoteList.size();i++) {
      Quote curQuote = handler.quoteList.get(i);
      nCount2++;
      String[] rowdata = new String[tmpArray.length];

      if (curQuote.price.equals("0.00")) {
        //UtilityFunctions.stdoutwriter
        //  .writeln("Issue looking up ticker " +
        //  strSymbol + ",skipping",Logs.S,"PF50");
        System.out.println(curQuote.symbol);
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
      if (strAMPM.equals("pm") && nHour != 12)
        cal.set(Calendar.HOUR_OF_DAY, nHour + 12);
      else if (strAMPM.equals("am") && nHour == 12)
        cal.set(Calendar.HOUR_OF_DAY, 0);
      else
        cal.set(Calendar.HOUR_OF_DAY,nHour);

      cal.set(Calendar.MINUTE, Integer.parseInt(strTimeArray[1]));
      cal.set(Calendar.SECOND, 0);
      cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      formatter.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
      Calendar currentCal = Calendar.getInstance();
      DateFormat formatter2 = new SimpleDateFormat("M/d/yyyy");
      
      // Only perform this check for task 10.
      if (dg.nCurTask == 10) {
        if (!formatter2.format(currentCal.getTime()) 
            .equals(formatter2.format(cal.getTime()))) {
          UtilityFunctions.stdoutwriter
          .writeln("Bad Yahoo Data, in postProcessing function for Symbol " +
              curQuote.symbol,Logs.ERROR,"PF50.5");
        }
      }
      
      // If the taskid is 10, we are going to use the input time 
      // for date_collected. Otherwise we are going to use real time collection 
      //time - delay (for yahoo, 20 minutes) for date_collected.
      if (dg.nCurTask == 10) {
        rowdata[1] = "'" + formatter.format(cal.getTime()) + "'";
      }
      else {
        currentCal.add(Calendar.MINUTE, -20);
        rowdata[1] = "'" + formatter.format(currentCal.getTime()) + "'";
      }

      // These are the 5 non US tickers for task 24:
      // ^N225, ^AORD, ^TWII, ^NZ50, ^AXJO
      if (curQuote.symbol.equals("BRK-A")) {
        curQuote.symbol = "BRK/A";
      }
      else if (curQuote.symbol.equals("BF-B")) {
        curQuote.symbol = "BF/B";
      }

      try {
        String query = "select id from entities where ticker='" +
          curQuote.symbol + "'";
        ResultSet rs = dbf.db_run_query(query);
        rs.next();
        rowdata[2] = rs.getInt("id") + "";
      }
      catch (SQLException sqle) {
        UtilityFunctions.stdoutwriter
          .writeln("Issue looking up ticker " +
          curQuote.symbol + ",skipping",Logs.ERROR,"PF50");
        UtilityFunctions.stdoutwriter.writeln(sqle);
        continue;
      }

      nCount++;
      propTableData.add(rowdata);
    }
  }
  */

  /*
  public void postProcessGoogleSharePrice() throws SQLException {
   
    String strTmpValue = propTableData.get(0)[0];
    String lines[] = strTmpValue.split("\\r?\\n");
    propTableData.remove(0);
    String[] tmpArray = {"value","date_collected","entity_id"};
    propTableData.add(tmpArray);
    int nCount = 0, nCount2 = 0;

    //skip the first and last lines 
    for (int i=1;i<lines.length-1;i++) {
      nCount2++;
      String[] rowdata = new String[tmpArray.length];
      String[] inputrow = lines[i].split(",");
      if (inputrow[2].contains("#")) {
        UtilityFunctions.stdoutwriter
          .writeln("Invalid data returned for ticker " +
          inputrow[0],Logs.WARN,"PF62.3");			
        continue;
      }

      String strSymbol = inputrow[0];
      if (strSymbol.equals("BRK.A")) {
        strSymbol = "BRK/A";
      }
      else if (strSymbol.equals("BF.B")) {
        strSymbol = "BF/B";
      }

      try {
        String query = "select id from entities where ticker='" +
          strSymbol + "'";
        ResultSet rs = dbf.db_run_query(query);
        rs.next();
        rowdata[2] = rs.getInt("id") + "";
      }
      catch (SQLException sqle) {
        UtilityFunctions.stdoutwriter.writeln("Issue looking up ticker " +
          strSymbol + ",skipping",Logs.ERROR,"PF50");
        UtilityFunctions.stdoutwriter.writeln(sqle);
        continue;
      }

      rowdata[0] = inputrow[1];
      inputrow[2].trim();
      String strDate = inputrow[2].substring(0,inputrow[2].indexOf(" "));
      String strTime = inputrow[2].substring(inputrow[2]
          .indexOf(" ") + 1,inputrow[2].length());

      String[] strDateArray = strDate.split("/");
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.MONTH, Integer.parseInt(strDateArray[0])-1);
      cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(strDateArray[1]));
      cal.set(Calendar.YEAR,Integer.parseInt(strDateArray[2]));

      String[] strTimeArray = strTime.split(":");
      cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(strTimeArray[0]));
      cal.set(Calendar.MINUTE, Integer.parseInt(strTimeArray[1]));
      cal.set(Calendar.SECOND, Integer.parseInt(strTimeArray[2]));
      cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));

      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      formatter.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
      rowdata[1] = "'" + formatter.format(cal.getTime()) + "'";
      nCount++;

      propTableData.add(rowdata);
    }
  }
  */

  /*
  public void postProcessNasdaqSharesOut() throws SQLException {
    strDataValue.replace(",", "");
    String query = "update entities set shares_outstanding=" +
        this.propTableData.get(0)[0] + " where ticker='" + this.strTicker + "'";
    dbf.db_update_query(query);
  }
  */
  
  /*
  public void postNasdaqFiscalYear() {
    // This is the main data set for populating the
    // entities->begin_fiscal_calendar field. Some tickers have to be
    // corrected with the sec_fiscal_calendar_field. These are designated with
    // the nasdaq_begin_fiscal_year_correct group in the entities table.
    UtilityFunctions.stdoutwriter
      .writeln("Inside postNasdaqFiscalYear function",Logs.STATUS2,"PF34");

    if (strDataValue.compareTo("January") == 0) {
      strDataValue = "November";
    }
    else if (strDataValue.compareTo("February") == 0) {
      strDataValue = "December";
    }
    else if (strDataValue.compareTo("March") == 0) {
      strDataValue = "January";
    }
    else if (strDataValue.compareTo("April") == 0) {
      strDataValue = "February";
    }
    else if (strDataValue.compareTo("May") == 0) {
      strDataValue = "March";
    }
    else if (strDataValue.compareTo("June") == 0) {
      strDataValue = "April";
    }
    else if (strDataValue.compareTo("July") == 0) {
      strDataValue = "May";
    }
    else if (strDataValue.compareTo("August") == 0) {
      strDataValue = "June";
    }
    else if (strDataValue.compareTo("September") == 0) {
      strDataValue = "July";
    }
    else if (strDataValue.compareTo("October") == 0) {
      strDataValue = "August";
    }
    else if (strDataValue.compareTo("November") == 0) {
      strDataValue = "September";
    }
    else if (strDataValue.compareTo("December") == 0) {
      strDataValue = "October";
    }

    // Use the following update statement for populating fiscal_calendar_begin
    try {
      String[] tmp = {"ticker","begin_fiscal_calendar"};
      propTableData.add(0,tmp);
      String[] values = {strTicker,strDataValue};

      propTableData.remove(1);
      propTableData.add(values);
    }
    catch(Exception e) {
      UtilityFunctions.stdoutwriter
        .writeln("Problem updating begin_fiscal_calendar field.",
        Logs.ERROR,"PF35");
      UtilityFunctions.stdoutwriter.writeln(e);
    }
  }
  */
  
  /*
  public void processTableSAndPCoList(ArrayList<String[]> tabledata,
      String strDataSet,UtilityFunctions tmpUF) {
    String[] rowdata;
    String query, groups;
    UtilityFunctions.stdoutwriter
      .writeln("In function processTableSAndPCoList",Logs.STATUS2,"PF36");
    try {
      ResultSet rs;
      for (int x=0;x<tabledata.size();x++) {
        rowdata = tabledata.get(x);
        // Some custom processing logic
        rowdata[0] = rowdata[0].replace("VIA.B", "VIA");
        rowdata[0] = rowdata[0].replace("BRK.B", "BRK.A");
        
        // BRK.A and BF.B data can't be found on nasdaq but can be
        // found on msnmoney
      
        // End custom processing logic

        // Determine if ticker is already in entities table, if not, add it
        query = "select * from entities where ticker='" + rowdata[0] + "'";
        rs = dbf.db_run_query(query);
        if (rs.next() == false) {
          query = "insert into entities (ticker, groups) values ('" +
            rowdata[0] + "','sandp')";
          dbf.db_update_query(query);
        }
        else {
          groups = rs.getString("groups");
          if (groups.indexOf("sandp") == -1) {
            groups = groups + ",sandp";
            query = "update entities set groups='" + 
              groups + "' where ticker='" + rowdata[0] + "'";
            dbf.db_update_query(query);
          }
        }
      }
    }
    catch (SQLException sqle) {
      UtilityFunctions.stdoutwriter
        .writeln("Problem processing S and P company list" +
       	" table data",Logs.ERROR,"PF37");
      UtilityFunctions.stdoutwriter.writeln(sqle);		
    }
  }
  */

  /*
  public void postProcessTableNasdaqUpdateEPS() 
      throws SQLException,SkipLoadException {
    // The purpose of this function is to only load the most recent quarter.
    // 
    // IF YOU WANT TO DO A FULL RELOAD OF DATA, set the postProcessFunction 
    // to postProcessNasdaqEPSTable.
    String[] colheaders = propTableData.get(0);
    String[] rowheaders = propTableData.get(1);

    //This is custom code to fix rowheader #4 since the html is not consistent.
    rowheaders[3] = rowheaders[3].replace("(FYE)","");
    int nRecentFisQtr=4;

    // find the most recent quarter
    for (int row=2;row<propTableData.size();row++) {
      if (propTableData.get(row)[0].equals("void") || 
          propTableData.get(row)[0].isEmpty()) {
        // hopefully the first row isn't void, if it is, then we will assume
        // the fiscal 4th quarter is the most recent.
        if (row==2) {
          //leaving FisQtr the same
          nRecentFisQtr=4;
          //need some special code since this situation screws up our 
          // colheaders processing logic below
          String[] tmp = new String[1];
          tmp[0] = colheaders[1];
          colheaders = tmp;
          break;
        }
        else {
          String[] tmp = new String[1];
          tmp[0] = colheaders[0];
          colheaders = tmp;
          nRecentFisQtr=row-2;
          break;
        }
      }
      // didn't run into any emptys or voids but still have to reset colheaders
      else if (row==5) {
        String[] tmp = new String[1];
        tmp[0] = colheaders[0];
        colheaders = tmp;
      }
    }

    //check to see if the data already exists
    String query="select * from fact_data where ticker='" +
      dg.strOriginalTicker + "' and data_set='" + dg.strCurDataSet +"'";
    query += " and fiscalquarter=" + nRecentFisQtr +
      " and fiscalyear=" + colheaders[0];
    ResultSet rs = dbf.db_run_query(query);
    if (rs.next()) {
      UtilityFunctions.stdoutwriter
        .writeln("MRQ already loaded, skipping",Logs.STATUS1,"PF38.5");
      propTableData = null;
      throw new SkipLoadException();
    }
    
    //void out all the other quarters so only the most recent gets loaded
    for (int i=2;i<propTableData.size();i++) {
      if (i-1!=nRecentFisQtr) {
        propTableData.get(i)[0] = "void";
      }
    }

    propTableData.set(0,colheaders);
    this.postProcessNasdaqEPSTable();
  }
  */

  /*
  public void postProcessNasdaqEPSTable() throws SQLException {
    // Special Situations That need to be handled:
    // -data values contain date in parentheses 
    // -data values contain &nbsp
    // -negative values use hyphen

    //TODO JNJ end of quarter months are incorrect, still need to code for this
    // Ticker urls currently without data: BF/B, BRK/A, AON

    String strTicker = dg.strOriginalTicker;
    String[] rowdata, newrow;
    String[] colheaders = propTableData.get(0);
    String[] rowheaders = propTableData.get(1);

    //This is custom code to fix rowheader #4 since the html is not consistent.
    rowheaders[3] = rowheaders[3].replace("(FYE)","");

    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] tmpArray = {"value","date_collected","entity_id","fiscalquarter",
      "fiscalyear","calquarter","calyear"};
    newTableData.add(tmpArray);

    for (int row=2;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);
      for (int col=0;col<colheaders.length;col++) {
        newrow = new String[16];
        if (rowdata[col].compareTo("void") != 0) {
          if ((rowdata[col].contains("N/A") == true) 
              || (rowdata[col].isEmpty() == true)) {
            UtilityFunctions.stdoutwriter
              .writeln("N/A value or empty value, skipping...",
                  Logs.STATUS2,"PF39");
            continue;
          }
          else if (rowdata[col].contains("(") == true) {
            newrow[0]=rowdata[col].substring(0,rowdata[col].indexOf("("));
          }
          else {
            UtilityFunctions.stdoutwriter
              .writeln("Problem with data value formatting",Logs.ERROR,"PF40");
          }
          
          newrow[1] = "NOW()";
          newrow[2] = dg.nCurrentEntityId + "";
          newrow[3] = Integer.toString(row-1);
          newrow[4] = colheaders[col];
          String strCalYearQuarter = MoneyTime
              .getCalendarYearAndQuarter(strTicker, Integer.parseInt(newrow[3]),
                  Integer.parseInt(newrow[4]),dbf);
          newrow[5] = strCalYearQuarter.substring(0,1);
          newrow[6] = strCalYearQuarter.substring(1,5);
          newTableData.add(newrow);
        }
      }
    }
    propTableData = newTableData;
  }
  */
  
  /*
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
      // If there are 2 sets of parens for the row header, we want to cutoff
      // at the 2nd left paren.
      String tmp = rowheaders[row-2];
      
      int i = tmp.indexOf("(");

      if (tmp.indexOf("(",i+1)==-1) {
        tmp = tmp.substring(0,i);
      }
      else {
        tmp = tmp.substring(0,tmp.indexOf("(",i+1));
      }
      tmp = tmp.trim();
      
      //remove single quotes e.g. (coffee 'c')
      tmp = tmp.replace("'", "");
      String query = "select * from entities where ticker='"+tmp+"'";

      try {
        ResultSet rs = dbf.db_run_query(query);
        rs.next();
        newrow[2] = rs.getInt("id") + "";
        newTableData.add(newrow);
      }
      catch (SQLException sqle) {
        // This is not a fatal error so we won't display the full exception.
        UtilityFunctions.stdoutwriter
          .writeln("Problem looking up ticker: " + tmp +
              ",row skipped",Logs.WARN,"PF99.55");
      }
    }
    newTableData.add(0, tmpArray);
    propTableData = newTableData;
  }
  */
  
  /*
  public void postProcessBloombergCommoditiesV2() {
    String[] tmpArray = {"value","date_collected","entity_id"};
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] rowdata, newrow;
    String[] rowheaders = propTableData.get(1);
    
    for (int row=2;row<propTableData.size();row++)	{
      rowdata = propTableData.get(row);

      if (rowdata[0].contains("N.A.")) {
        continue;
      }

      newrow = new String[tmpArray.length];
      newrow[0] = rowdata[0].replace(",", "");
      newrow[1] = "NOW()";
      // If there are 2 sets of parens for the row header, we want to cutoff at
      // the 2nd left paren.
      String tmp = rowheaders[row-2];
      tmp = tmp.trim();
      tmp = tmp.replace("'", "");
      String query = "select * from entity_aliases where ticker_alias='"
          + tmp + "'";

      try {
        ResultSet rs = dbf.db_run_query(query);
        rs.next();
        newrow[2] = rs.getInt("entity_id") + "";	
        newTableData.add(newrow);
      }
      catch (SQLException sqle) {
        UtilityFunctions.stdoutwriter
          .writeln("Problem looking up ticker alias: " + 
              tmp + ",row skipped",Logs.WARN,"PF99.754");
        continue;
      }
    }
    newTableData.add(0, tmpArray);
    propTableData = newTableData;
  }
  */

  /*
  
  */
  
  /*
  public void postProcessOneTimeYahooIndex() {
    String[] rowdata;
    for (int row=1;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);
      String strInsert = "insert into entities_yahoo (ticker,full_name) " +
      		"values ('" + rowdata[0] + "','" + rowdata[1] + "')";
      try {
        dbf.db_update_query(strInsert);
      }
      catch (SQLException sqle) {
        UtilityFunctions.stdoutwriter
          .writeln("Problem inserting row" + strInsert,Logs.ERROR,"PF88.1");
      }
    }
  }
  */

  public void postProcessInputDataTest() {
    String[] tmpArray = {"value","date_collected","entity_id"};
    String[] rowheaders = propTableData.get(1);
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] newrow = null;

    for (int row=2;row<propTableData.size();row++) {
      newrow = new String[tmpArray.length];
      newrow[0] = "";
      newrow[1] = "";
      newrow[2] = "";
    }
    newTableData.add(0, tmpArray);
    propTableData = newTableData;
  }

  /*
  public void postProcessBloombergGovtBonds() {
    // OFP 2/6/2012
    // This was originally set up to extract price but price tells you nothing
    // when new bonds are issued, so it was converted over to extract rate.
    // 
    // The hong kong rates are not displayed in the same format as for the
    // other countries. Skipping these for now.

    String[] tmpArray = {"value","date_collected","entity_id"};
    String[] rowheaders = propTableData.get(1);
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] rowdata, newrow;

    for (int row=2;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);
      newrow = new String[tmpArray.length];
      String strQuery = null;
      String strTicker = null;
      newrow[1] = "NOW()";
      newrow[0] = rowdata[0].substring(rowdata[0]
          .indexOf("/"),rowdata[0].length()).trim();
      
      if (dg.strCurDataSet.contains("_japan")) {
        strTicker = rowheaders[row-2].replace("-","_").toLowerCase() + "_japan";
        strQuery = "select id from entities where ticker='" + strTicker + "'";
        newrow[0] = rowdata[0]
            .substring(rowdata[0].indexOf("/")+1,rowdata[0].length()).trim();
      }
      else if (dg.strCurDataSet.contains("_australia")) {
        strTicker = rowheaders[row-2].replace("-","_").toLowerCase() +
            "_australia";
        strQuery = "select id from entities where ticker='" + strTicker + "'";
        newrow[0] = rowdata[0]
            .substring(rowdata[0].indexOf("/")+1,rowdata[0].length()).trim();
      }
      else if (dg.strCurDataSet.contains("_us")) {
        // Prices aren't displayed and haven't figured out the proper
        // way to cacluate them yet.
        if (rowheaders[row-2].equals("12-Month")) {
          rowheaders[row-2] = "1-Year";
        }
        newrow[0] = rowdata[0].substring(rowdata[0].indexOf("/")+1,
            rowdata[0].length()).trim();
        strTicker = rowheaders[row-2].replace("-","_").toLowerCase() + "_us";
        strQuery = "select id from entities where ticker='" + strTicker + "'";
      }
      else if (dg.strCurDataSet.contains("_uk")) {
        strTicker = rowheaders[row-2].replace("-","_").toLowerCase() + "_uk";
        strQuery = "select id from entities where ticker='" + strTicker + "'";
        newrow[0] = rowdata[0]
            .substring(rowdata[0].indexOf("/")+1,rowdata[0].length()).trim();
      }
      else if (dg.strCurDataSet.contains("_germany")) {
        strTicker = rowheaders[row-2]
            .replace("-","_").toLowerCase() + "_germany";
        strQuery = "select id from entities where ticker='" + strTicker + "'";
        newrow[0] = rowdata[0]
            .substring(rowdata[0].indexOf("/")+1,rowdata[0].length()).trim();
      }
      else if (dg.strCurDataSet.contains("_brazil")) {
        strTicker = rowheaders[row-2]
            .replace("-","_").toLowerCase() + "_brazil";
        strQuery = "select id from entities where ticker='" + strTicker + "'";
        newrow[0] = rowdata[0]
            .substring(rowdata[0].indexOf("/")+1,rowdata[0].length()).trim();
      }
      else if (dg.strCurDataSet.contains("_hongkong")) {
        if (rowheaders[row-2]
            .contains("Bill") || rowheaders[row-2].contains("HKSAR")) {
          continue;
        }
        newrow[0] = rowdata[0].trim();
        strTicker = rowheaders[row-2].substring(0,3);
        if (strTicker.contains("W")) {
          strTicker = strTicker.replace("W","_week_");
        }
        else if (strTicker.contains("M")) {
          strTicker = strTicker.replace("M","_month_");
        }
        else if (strTicker.contains("Y")) {
          strTicker = strTicker.replace("Y","_year_");
        }
        strTicker += "hongkong";
        strTicker = strTicker.replace(" ","");
        strQuery = "select id from entities where ticker='" + strTicker + "'";
      }
      else {
        UtilityFunctions.stdoutwriter
          .writeln("Unable to find dataset " +
              dg.strCurDataSet,Logs.ERROR,"PF44.51");
        continue;
      }
      newrow[0] = newrow[0].replace(",", "");
      
      // This dataset can involve extended ascii characters.
      newrow[0] = newrow[0].replaceAll("[^\\p{ASCII}]", "");
      if (newrow[0].isEmpty()) {
        UtilityFunctions.stdoutwriter
          .writeln("Ticker returned an empty value: " +
              strTicker + ", skipping",Logs.WARN,"PF44.59");
        continue;
      }
      
      try {
        ResultSet rs = dbf.db_run_query(strQuery);
        rs.next();
        newrow[2] = rs.getInt("id") + "";
      }
      catch (SQLException sqle) {
        UtilityFunctions.stdoutwriter
          .writeln("Problem looking up ticker: " + 
              strTicker  + ",row skipped",Logs.WARN,"PF43.51");
        continue;
      }
      newTableData.add(newrow);
    }
    newTableData.add(0, tmpArray);
    propTableData = newTableData;
  }
  */
  
  /*
  public void postProcessBloombergQuote() 
      throws CustomEmptyStringException {

    String[] tmpArray = {"value","date_collected","entity_id"};
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] rowdata, newrow;
    rowdata = this.propTableData.get(0);
    newrow = new String[tmpArray.length];
    newrow[0] = rowdata[0].replace("\n","").trim();
    
    if (newrow[0].isEmpty()) {
      UtilityFunctions.stdoutwriter
        .writeln("Empty Value Retrieved for ticker " +
            dg.strCurrentTicker + ",row skipped",Logs.WARN,"PF48.33");
      throw (new CustomEmptyStringException());
    }
    else if (newrow[0].equals("N.A.")) {
      UtilityFunctions.stdoutwriter
        .writeln("N.A. Value Retrieved for ticker " +
            dg.strCurrentTicker + ",row skipped",Logs.WARN,"PF48.40");
      throw (new CustomEmptyStringException());
    }

    newrow[1] = "NOW()";
    String query = "select id from entities where ticker='"
        + dg.strCurrentTicker + "'";
    
    try {
      ResultSet rs = dbf.db_run_query(query);
      rs.next();
      newrow[2] = rs.getInt("id") + "";
    } catch (SQLException sqle) {
      UtilityFunctions.stdoutwriter
        .writeln("Problem looking up ticker: " +
            dg.strCurrentTicker + ",row skipped",Logs.WARN,"PF47.33");
      return;
    }

    UtilityFunctions.stdoutwriter
      .writeln("Processing ticker: " +
          dg.strCurrentTicker,Logs.STATUS1,"PF100.1");
    newTableData.add(tmpArray);
    newTableData.add(newrow);
    propTableData = newTableData;
  }
  */
  
  /*
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
      
      // Next line condenses multiple spaces down to one.
      ticker = ticker.replaceAll("\\s+", " "); 

      if (rowdata[0].equals("pikefinvoid")) {
        UtilityFunctions.stdoutwriter
          .writeln("Invalid data format for " + ticker +
              ". Skipping.",Logs.WARN,"PF2.75");
        continue;
      }
      else if (rowdata[0].contains("N.A."))  {	
        UtilityFunctions.stdoutwriter
          .writeln("N.A. value for ticker " + ticker,Logs.WARN,"PF2.75");
        continue;
      }
      // OFP 4/27/2012 - Switched lookup to use entity_aliases.
      String query = "select * from entity_aliases where ticker_alias='" +
          ticker + "'";

      try {
        ResultSet rs = dbf.db_run_query(query);
        rs.next();
        newrow[2] = rs.getInt("entity_id") + "";
        newTableData.add(newrow);
      }
      catch (SQLException sqle) {
        // This is not a fatal error so we won't display the full exception.
        UtilityFunctions.stdoutwriter
          .writeln("Problem looking up ticker alias: " +
              ticker + ",row skipped",Logs.WARN,"PF42.51");
      }
    }
    newTableData.add(0, tmpArray);
    propTableData = newTableData;
  }
  */

  /*
  public void postProcessNasdaqEPSEstTable() {
    String[] rowdata, newrow;
    String strTicker;
    String[] tmpArray = {"entity_id","date_collected","value","fiscalyear"};
    ArrayList<String[]> newTableData = new ArrayList<String[]>();

    // OFP 10/17/2010 - For Ticker T there is currently redundant data
    // being displayed 

    // TODO Need to add code to make the query insert column definitions the 
    // first row in the table data that is returned.

    if (propStrTableDataSet.contains("_q_") == true) {
      int i = tmpArray.length;
      tmpArray = UtilityFunctions.extendArray(tmpArray);
      tmpArray = UtilityFunctions.extendArray(tmpArray);
      tmpArray = UtilityFunctions.extendArray(tmpArray);
      tmpArray[i] = "calyear";
      tmpArray[i+1] = "fiscalquarter";
      tmpArray[i+2] = "calquarter";
    }
    
    try {
      String query = "select url_dynamic from jobs where jobs.Data_Set='" +
          propStrTableDataSet + "'";
      strTicker = dg.strOriginalTicker;
      String[] rowheaders = propTableData.get(1);
      for (int x=2;x<propTableData.size();x++) {
        rowdata = propTableData.get(x);
        newrow = new String[tmpArray.length];
        newrow[0] = dg.nCurrentEntityId + "";
        newrow[1] = "NOW()";
        newrow[2] = rowdata[0];
        MoneyTime mt = new MoneyTime(rowheaders[x-2].substring(0,3),
            rowheaders[x-2].replace("&nbsp;","").substring(5,7),
            strTicker,dbf);
        newrow[3] = mt.strFiscalYear;

        if (propStrTableDataSet.contains("_q_") == true) {
          newrow[4] = Integer.toString(mt.nCalAdjYear);
          newrow[5] = mt.strFiscalQtr;
          newrow[6] = Integer.toString(mt.nCalQtr);

        }
        newTableData.add(newrow);
      }
      newTableData.add(0, tmpArray);
      propTableData = newTableData;
    }
    catch (SQLException sqle) {
      UtilityFunctions.stdoutwriter
        .writeln("Problem processing table data",Logs.ERROR,"PF43");
      UtilityFunctions.stdoutwriter.writeln(sqle);
    }
  }
  */

  public void postProcessMsnMoneyEPSEst() {
    // Negative values are enclosed in span tags which have to be removed.
    if (strDataValue.indexOf("<") != -1) {
      strDataValue = strDataValue.substring(strDataValue
          .indexOf(">")+1,strDataValue.length());
      strDataValue = strDataValue.substring(0,strDataValue.indexOf("<"));
    }
  }

  public boolean preProcessYahooEPSEst() throws SQLException {
    // Negative values are enclosed in font changing tags which
    // have to be removed
    
    if (strTicker.equals("BF/B")) {
      dg.strCurrentTicker="BF-B";
    }
    else if (strTicker.equals("BRK/A")) {
      dg.strCurrentTicker="BRK-A";
    }
    
    // TODO Switch from return values to exceptions
    return(true);
  }

  public boolean preProcessGoogleEPS() throws SQLException {
    // Negative values are enclosed in font changing tags which 
    // have to be removed
    if (strTicker.equals("BF/B")) {
      dg.strCurrentTicker="NYSE:BF.B";
    }
    else if (strTicker.equals("BRK/A")) {
      dg.strCurrentTicker="NYSE:BRK.A";
    }
    else if (strTicker.equals("PG") || strTicker.equals("SCHW") || 
        strTicker.equals("DF") || strTicker.equals("MHS") ||
        strTicker.equals("NWL")) {
      dg.strCurrentTicker="NYSE:" + strTicker;
    }
    
    // TODO Need to get rid of these return values and go with exceptions 
    return(true);
  }

  /*
  public void postProcessTableYahooBeginYearVerify() 
      throws CustomEmptyStringException, SQLException { 
    // The purpose of this function is to verify that the quarter end months
    // reported in yahoo match the begin_fiscal_calendar property stated in the
    // entities table.

    // It can be swapped into the 'table_yahoo_q_eps_est_body' data_set

    String[] colheaders = propTableData.get(0);
    String strQEndMonth = colheaders[0];
    String query = "select begin_fiscal_calendar from entities where ticker='"
        + this.strTicker + "'";
    ResultSet rs = dbf.db_run_query(query);
    rs.next();
    int nBeginFiscalCal = MoneyTime
        .convertMonthStringtoInt(rs.getString("begin_fiscal_calendar"));
    PrintWriter fullfilewriter = null;

    try {
      fullfilewriter = new PrintWriter(
          new FileWriter("YahooVerify.txt",true),true);
    }
    catch (IOException ioe) {
    }

    switch(nBeginFiscalCal) {
    case 1:
    case 4:
    case 7:
    case 10:
      if (!(strQEndMonth.substring(0,3).equalsIgnoreCase("Mar") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Jun") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Sep") ||
          strQEndMonth.substring(0,3).equalsIgnoreCase("Dec"))) {
        // This is custom verification so not using typical log files
        System.out.println("begin_fiscal_calendar and quote.yahoo mismatch" +
        		" for ticker " + strTicker);
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
        // This is custom verification so not using typical log files
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
        // This is custom verification so not using typical log files
        System.out.println("begin_fiscal_calendar and quote.yahoo mismatch" +
        		" for ticker " + strTicker);
        fullfilewriter.write(strTicker + "\n");
      }
      break;

    }
    fullfilewriter.close();
  }
  */

  public void postProcessTreasuryDebtTable6() {
    int j=0;
    for (int i=0;i<10;i++) {
      j++;
    }
  }

  /*
  public void postProcessTreasuryDirect() throws SQLException {
    String[] data = propTableData.get(1);
    data[0] = data[0].replace(",", "");
    data[0] = data[0].substring(0,data[0].indexOf("."));
    long lPublicDebt = Long.parseLong(data[0]);
    lPublicDebt = lPublicDebt/10000000;
    data[1] = data[1].replace(",", "");
    data[1] = data[1].substring(0,data[1].indexOf("."));
    long lIntraGov = Long.parseLong(data[1]);
    lIntraGov = lIntraGov/10000000;
    String strQuery="";
    
    if (this.dg.bVerify == false) {
      strQuery = "insert into batches ";
    }
    else {
      strQuery = "insert into verify_batches ";
    }
    
    strQuery += " (id,date_collected,task_id) values (";
    strQuery += dg.nTaskBatch + ",NOW()," + dg.nCurTask;
    strQuery += ")";

    try {
      dbf.db_update_query(strQuery);
    }
    catch (SQLException sqle) {
      UtilityFunctions.stdoutwriter.writeln("Problem with custom insert",
          Logs.ERROR,"PF55.52");
      UtilityFunctions.stdoutwriter.writeln(sqle);
    }
    
    strQuery = "insert into " + this.dg.strFactTable;
    strQuery += " (\"value\",\"scale\",\"date_collected\",\"entity_id\"," +
    		"\"metric_id\",\"batch_id\") ";
    strQuery += " values (";
    strQuery += lPublicDebt;
    strQuery += ",7,NOW(),1360,9," + dg.nTaskBatch + ")";

    try {
      dbf.db_update_query(strQuery);
    }
    catch (SQLException sqle) {
      UtilityFunctions.stdoutwriter
        .writeln("Problem with custom insert",Logs.ERROR,"PF55.5");
      UtilityFunctions.stdoutwriter.writeln(sqle);
    }

    strQuery = "insert into " + this.dg.strFactTable;
    strQuery += " (\"value\",\"scale\",\"date_collected\",\"entity_id\"" +
    		",\"metric_id\",\"batch\") ";
    strQuery += " values (";
    strQuery += lIntraGov;
    strQuery += ",7,NOW(),1360,10," + dg.nTaskBatch + ")";

    try {
      dbf.db_update_query(strQuery);
    }
    catch (SQLException sqle) {
      UtilityFunctions.stdoutwriter
        .writeln("Problem with custom insert",Logs.ERROR,"PF55.6");
      UtilityFunctions.stdoutwriter.writeln(sqle);
    }
  }

  public boolean preJobProcessDataInputTest() throws SQLException {
    String strQuery = "select * from input_test_controls ";
    strQuery += " where task_id=" + dg.nCurTask;

    try	{
      ResultSet rs = dbf.db_run_query(strQuery);
      rs.next();
      dg.strStage1URL = dg.strStage1URL.replace("${seqnumber}",
          rs.getInt("sequence")+"");
      dg.strStage1URL = dg.strStage1URL.replace("${dataset}",
          rs.getInt("data_set") + "");
    }
    catch (SQLException sqle)	{
      UtilityFunctions.stdoutwriter
        .writeln("SQL Problem with preProcessDataInputTest",
            Logs.ERROR,"PF42.5");
      return(false);
    }
    return(true);
  }
  */
  
  /*
  public void postJobProcessDataInputTest() throws SQLException {

    String[] newrow;
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] tmpArray = {"entity_id","date_collected","value"};
    newrow = new String[tmpArray.length];
    String[] colheaders = propTableData.get(0);
    String[] rowheaders = propTableData.get(1);
    String[] data = propTableData.get(2);

    try {
      String query = " select * from entities where ticker='" + 
          colheaders[0] + "'";
      ResultSet rs = dbf.db_run_query(query);
      rs.next();
      newrow[0] = rs.getInt("id") +"";
    }
    catch (SQLException sqle)	{
      UtilityFunctions.stdoutwriter
        .writeln("SQL Problem with entity id lookup",Logs.ERROR,"PF42.5");
      return;
    }

    newrow[1] = "'" + rowheaders[0] + "'";
    newrow[2] = data[0];
    newTableData.add(newrow);
    String strQuery = "update input_test_controls set sequence=sequence+1 ";
    strQuery += " where task_id=" + dg.nCurTask;

    try	{
      dbf.db_update_query(strQuery);
    }
    catch (SQLException sqle)	{
      UtilityFunctions.stdoutwriter
        .writeln("SQL Problem with postProcessDataInputTest",
            Logs.ERROR,"PF42.5");
    }
    newTableData.add(0, tmpArray);
    propTableData = newTableData;
  }
  */

  public void preJobProcessTableXrateorg() throws SQLException {

  }

  /*
  public void postProcessTableXrateorg() {
    String[] rowheaders = propTableData.get(1);
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] tmpArray = {"value","date_collected","entity_id"};
    newTableData.add(tmpArray);
    
    for (int i=0;i<rowheaders.length;i++) {
      String strTmp = rowheaders[i];
      strTmp = "USD" + strTmp.substring(strTmp.indexOf("(")+1,
          strTmp.indexOf(")"));

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

      String query = "select * from entities where ticker='"+strTmp+"'";

      try {
        ResultSet rs = dbf.db_run_query(query);
        rs.next();
        tmpA[2] = rs.getInt("id") + "";
        newTableData.add(tmpA);
      }
      catch (SQLException sqle) {
        // This is not a fatal error so we won't display the full exception.
        UtilityFunctions.stdoutwriter.writeln("Problem looking up ticker: " + 
            strTmp + ",row skipped",Logs.WARN,"PF42.5");
      }
    }
    propTableData = newTableData;
  }
  */

  /*
  public void postProcessDescriptionXrateorg() {
    // Function to populate the full description for the ticker names, 
    // e.g. Argentine Peso for ARS 
    // String[] rowheaders = propTableData.get(1);
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] tmpArray = {"data_set","value","date_collected","ticker"};
    newTableData.add(tmpArray);
    String query;

    for (int i=0;i<propTableData.size();i++) {
      String strTmp = propTableData.get(i)[0];
      String ticker = "USD" + strTmp.substring(strTmp.indexOf("(")+1,
          strTmp.indexOf(")"));

      strTmp = strTmp.substring(strTmp.indexOf(">")+1,strTmp.indexOf(" ("));

      //The first 2 crosses on the europe page are inverse: EURUSD and GBPUSD
      if (propStrTableDataSet.contains("europe")) {
        if (i==0 || i==1) {
          continue;
        }
      }

      query = "insert into entities (ticker,full_name) values ('"
          + ticker + "','" + strTmp +"')";

      try {
        dbf.db_update_query(query);
      }
      catch (SQLException sqle) {
        UtilityFunctions.stdoutwriter
          .writeln("Failed to insert ticker " + ticker + " into entities table",
              Logs.STATUS2,"PF43.49");
        UtilityFunctions.stdoutwriter.writeln(sqle);
      }
    }
  }
  */

  /*
  public void postProcessTableYahooEPSAct() 
      throws CustomEmptyStringException, SQLException {
    String[] tmpArray = {"data_set","value","date_collected","ticker",
        "fiscalquarter","fiscalyear","calquarter","calyear"};
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] rowdata, newrow;
    String[] colheaders = propTableData.get(0);
    newTableData.add(tmpArray);

    for (int row=2;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);

      for (int col=0;col<colheaders.length;col++) {
        newrow = new String[16];
        if (rowdata[col].compareTo("void") != 0) {
          
          newrow[0] = propStrTableDataSet;
          
          if (rowdata[col].equals("N/A")) {
            UtilityFunctions.stdoutwriter
              .writeln("N/A value or empty value, skipping...",
                  Logs.STATUS2,"PF43.5");
            continue;
          }
          
          // Negative values are enclosed in font changing tags which have
          // to be removed
          if (rowdata[col].indexOf("<") != -1) {
            rowdata[col] = rowdata[col].substring(rowdata[col].indexOf(">")+1,
                rowdata[col].length());
            rowdata[col] = rowdata[col].substring(0,rowdata[col].indexOf("<"));
          }
          
          newrow[1] = rowdata[col].replace(",","");
          newrow[2] = "NOW()";
          newrow[3] = dg.strOriginalTicker;
          
          MoneyTime mt = new MoneyTime(colheaders[col].substring(0,3),
              colheaders[col].substring(4,6),strTicker,dbf);

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
  */
  
  /*
  public void postProcessTableYahooEPSEst() 
      throws CustomEmptyStringException, SQLException {
  
    String[] rowdata, newrow;
    String[] colheaders = propTableData.get(0);
    String[] tmpArray = {"data_set","value","date_collected"
        ,"ticker","fiscalyear"};

    ArrayList<String[]> newTableData = new ArrayList<String[]>();

    if (propStrTableDataSet.contains("_q_") == true) {
      int i = tmpArray.length;
      tmpArray = UtilityFunctions.extendArray(tmpArray);
      tmpArray = UtilityFunctions.extendArray(tmpArray);
      tmpArray = UtilityFunctions.extendArray(tmpArray);
      tmpArray[i] = "calyear";
      tmpArray[i+1] = "fiscalquarter";
      tmpArray[i+2] = "calquarter";
    }

    newTableData.add(tmpArray);
    for (int row=2;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);

      for (int col=0;col<colheaders.length;col++) {
        newrow = new String[16];
        if (rowdata[col].compareTo("void") != 0) {
          
          newrow[0] = propStrTableDataSet;
          
          if (rowdata[col].equals("N/A")) {
            UtilityFunctions.stdoutwriter
              .writeln("N/A value or empty value, skipping...",
                  Logs.STATUS2,"PF43.5");
            continue;
          }

          // Negative values are enclosed in font changing tags which
          // have to be removed
          if (rowdata[col].indexOf("<") != -1) {
            rowdata[col] = rowdata[col].substring(rowdata[col].indexOf(">")+1,
                rowdata[col].length());
            rowdata[col] = rowdata[col].substring(0,rowdata[col].indexOf("<"));
          }

          newrow[1] = rowdata[col];
          newrow[2] = "NOW()";
          newrow[3] = dg.strOriginalTicker;
          MoneyTime mt = new MoneyTime(colheaders[col].substring(0,3),
              colheaders[col].substring(4,6),strTicker,dbf);
          newrow[4] = mt.strFiscalYear;

          if (propStrTableDataSet.contains("_q_") == true) {
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
  */

  public void postProcessTableBriefIClaims()
      throws CustomEmptyStringException, SQLException {
    String[] rowdata, newrow;
    String[] colheaders = propTableData.get(0);
    String[] tmpArray = {"data_set","value",
        "date_collected","calmonth","calyear","day"};

    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    newTableData.add(tmpArray);
    Calendar cal = Calendar.getInstance();

    for (int row=2;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);
      
      for (int col=0;col<colheaders.length;col++) {
        if (rowdata[col] == null) {
          break;
        }
        newrow = new String[tmpArray.length];
        newrow[0] = propStrTableDataSet;
        
        // This will have to be fixed if we ever get a 7 figure initial
        // claims print
        newrow[1] = rowdata[col].replace("K","000");
        newrow[2] = "NOW()";
        newrow[3] = Integer.toString(MoneyTime
            .convertMonthStringtoInt(colheaders[col]));
        newrow[4] = Integer.toString(cal.get(Calendar.YEAR));
        newrow[5] = colheaders[col]
            .substring(colheaders[col].length()-2,colheaders[col].length());

        newTableData.add(newrow);
      }
    }
    propTableData = newTableData;
  }

  /*
  public boolean preProcessImfGdp() {
    Calendar cal = Calendar.getInstance();
    int nMaxEndYear = 2016;
    int nMinBeginYear = 2004;

    int nTempCurrent = 2012;

    // OFP 10/6/2012 - 01 in the url is used for data that is release in April. 
    // 				   02 in the url is used for data that is released in October.
    // I don't know if there is a set schedule for when each data set becomes
    // available. Most likely this will have to be manually monitored.

    dg.strStage1URL = dg.strStage1URL.replace("${dynamic8}", nTempCurrent +"");
    dg.strStage1URL = dg.strStage1URL
        .replace("${dynamic6}", nMinBeginYear + "");
    dg.strStage1URL = dg.strStage1URL
        .replace("${dynamic7}", cal.get(Calendar.YEAR)+"");
    dg.strStage1URL = dg.strStage1URL.replace("${dynamic5}", nTempCurrent +"");
    dg.strStage1URL = dg.strStage1URL
        .replace("${dynamic3}", cal.get(Calendar.YEAR)+"");
    dg.strStage1URL = dg.strStage1URL.replace("${dynamic4}", nMaxEndYear + "");

    return true;
  }
  */

  /*
  public void postProcessImfGdp() throws SQLException {
    String[] tmpArray = {"value","date_collected","entity_id",
        "calyear","scale"};

    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] newrow;
    String[] colheaders = propTableData.remove(0);
    String[] rowheaders = propTableData.remove(0);
    int nCounter=0;

    for (String[] rowdata : propTableData)	{
      for (int col=0;col<colheaders.length;col++)		{
        String strCountry = rowheaders[nCounter];
        newrow = new String[tmpArray.length];
        newrow[0] = rowdata[col].replace(",", "");

        if (newrow[0].contains("n/a"))		{
          UtilityFunctions.stdoutwriter
            .writeln("Retrieved n/a value, skipping processing for entity " +
                strCountry,Logs.WARN,"PF44");
          continue;
        }

        BigDecimal bdTmp = null;
        if (this.dg.nCurTask == 29) {
          if (!newrow[0].contains("bgcolor")) {
            bdTmp = new BigDecimal(newrow[0].substring(newrow[0]
                .indexOf("right\">")+7,newrow[0].length()));
          }
          else {
            continue;
          }
        }
        else if (this.dg.nCurTask == 30) {
          if (newrow[0].contains("bgcolor")) {
            bdTmp = new BigDecimal(newrow[0].substring(newrow[0]
                .indexOf("\">")+2,newrow[0].length()));
          }
          else {
            continue;
          }
        }
        else if (this.dg.nCurTask == 22){
          if (newrow[0].contains("bgcolor")) {
            bdTmp = new BigDecimal(newrow[0].substring(newrow[0]
                .indexOf("\">")+2,newrow[0].length()));
          }
          else {
            continue;
          }
        }
        else if (this.dg.nCurTask == 33) {
          if (!newrow[0].contains("bgcolor")) {
            bdTmp = new BigDecimal(newrow[0].substring(newrow[0]
                .indexOf("right\">")+7,newrow[0].length()));
          }
          else {            
            continue;
          }

        }
        else {
          UtilityFunctions.stdoutwriter
            .writeln("Task id not found, this point in the code should not" +
            		" have been reached. Terminating processing of task.",
            		Logs.ERROR,"PF44.32");
          return;
        }

        newrow[0] = bdTmp.toString();
        newrow[1] = "NOW()";
        
        // If there are 2 sets of parens for the row header, we want to cutoff
        // at the 2nd left paren.
        strCountry = strCountry.trim();
        
        strCountry = strCountry.replace("'", "\\'");
        if (strCountry.toUpperCase().equals("KOREA")) {
          strCountry = "South Korea";
        }

        String query = "select entities.id from entities ";
        query += " join countries_entities on countries_entities." +
        		"entity_id=entities.id ";
        query += " join countries on countries.id=" +
        		"countries_entities.country_id ";
        query += " where ticker='macro' ";
        query += " and countries.name='"+strCountry+"'";

        try {
          ResultSet rs = dbf.db_run_query(query);
          rs.next();
          newrow[2] = rs.getInt("entities.id") + "";
        }
        catch (SQLException sqle) {
          // This is not a fatal error so we won't display the full exception.
          UtilityFunctions.stdoutwriter
            .writeln("Problem looking up country: " + strCountry
                + ",row skipped",Logs.ERROR,"PF43.55");
          continue;	
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
  */

  public void postProcessTableBLSUERate() 
      throws CustomEmptyStringException, SQLException {
    String[] rowdata, newrow;
    String[] colheaders = propTableData.get(0);
    String[] rowheaders = propTableData.get(1);
    String[] tmpArray = {"data_set","value","date_collected",
        "calmonth","calyear"};
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    newTableData.add(tmpArray);

    for (int row=2;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);
      for (int col=0;col<colheaders.length;col++) {
        if (rowdata[col] == null) {
          break;
        }

        newrow = new String[tmpArray.length];
        newrow[0] = propStrTableDataSet;
        newrow[1] = rowdata[col];
        newrow[2] = "NOW()";
        newrow[3] = Integer.toString(MoneyTime.
            convertMonthStringtoInt(colheaders[col]));
        newrow[4] = rowheaders[row-2];
        newTableData.add(newrow);
      }
    }
    propTableData = newTableData;
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

  /*
  public void postProcessYahooEPSEst() 
      throws CustomEmptyStringException, SQLException {
    
    if (strDataValue.equals("N/A")) {
      UtilityFunctions.stdoutwriter.writeln("Retrieved N/A value, skipping",
          Logs.ERROR,"PF44");
      throw (new CustomEmptyStringException());
    }
    
    // Negative values are enclosed in font changing tags which
    // have to be removed
    if (strDataValue.indexOf("<") != -1) {
      strDataValue = strDataValue
          .substring(strDataValue.indexOf(">")+1,strDataValue.length());
      strDataValue = strDataValue.substring(0,strDataValue.indexOf("<"));
    }

    String[] tmp = {"data_set","value","adj_quarter","ticker",
        "date_collected","fiscalquarter","fiscalyear"};

    propTableData.add(0,tmp);

  }
  */

  /*
  public void postProcessSECFiscalYearEndRaw() {
    // This is for populating actual_fiscal_year_end in the entities table
    String[] tmp = {"ticker","actual_fiscal_year_end"};

    propTableData.add(0,tmp);
    String[] values = {strTicker,strDataValue};
    propTableData.remove(1);
    propTableData.add(values);
    dbf.updateTableIntoDB(propTableData,"entities");
  }
  */
  
  /*
  public void postProcessSECBeginCalendarYear() {
    // This is for populating begin_fiscal_calendar in the entities table
    String strNewDataValue="";
    int nMonth = Integer.parseInt(strDataValue.substring(0,2));

    // Tickers where the SEC has an incorrect value 
    // AZO,DV,EMN,FDO,GME,HES,JNJ,KMX,KO,KR,LLTC,NSC,PFG,PG,SPLS,VFC,WYNN,YUM
    
    // AZO=September
    // PG=LLTC=DV=July
    // YUM=WYNN=VFC=PFG=NSC=KO=HES=EMN=January
    // FDO=September
    // SPLS=KR=JNJ=GME=February
    // KMX=March

    if ((strTicker.equals("AZO")) || (strTicker.equals("FDO"))) {
      nMonth=8;
    }
    else if ((strTicker.equals("PG")) || (strTicker.equals("LLTC")) ||
        (strTicker.equals("DV"))) {
      nMonth=6;
    }
    else if ((strTicker.equals("YUM")) || (strTicker.equals("WYNN")) || 
        (strTicker.equals("VFC")) || (strTicker.equals("PFG")) || 
        (strTicker.equals("NSC")) || (strTicker.equals("KO")) ||
        (strTicker.equals("HES")) || (strTicker.equals("EMN"))) {
      nMonth=12;
    }
    else if (strTicker.equals("KMX")) {
      nMonth=2;
    }
    else if ((strTicker.equals("SPLS")) || (strTicker.equals("KR")) || 
        (strTicker.equals("JNJ")) || (strTicker.equals("GME"))) {
      nMonth=1;
    }

    // These are mostly retail stocks whose fiscal calendar doesn't end
    // directly at the end of a month
    if ((strTicker.equals("AVY")) || (strTicker.equals("CERN")) || 
        (strTicker.equals("CPB")) || (strTicker.equals("CSC")) ||
        (strTicker.equals("DIS")) || (strTicker.equals("JEC")) ||
        (strTicker.equals("JNJ")) || (strTicker.equals("K")) || 
        (strTicker.equals("MU")) || (strTicker.equals("SNA")) ||
        (strTicker.equals("SNDK")) || (strTicker.equals("SWK")) ||
        (strTicker.equals("SWY")) || (strTicker.equals("SYMC")) ||
        (strTicker.equals("TXT")) || (strTicker.equals("VAR")) ||
        (strTicker.equals("VFC")) || (strTicker.equals("XLNX"))) {
      nMonth--;
    }

    //boundary condition
    if (nMonth==0) {
      nMonth=12;
    }

    if (nMonth == 1) {
      strNewDataValue = "February";
    }
    else if (nMonth == 2) {
      strNewDataValue = "March";
    }
    else if (nMonth == 3) {
      strNewDataValue = "April";
    }
    else if (nMonth == 4) {
      strNewDataValue = "May";
    }
    else if (nMonth == 5) {
      strNewDataValue = "June"; 
    }
    else if (nMonth == 6) {
      strNewDataValue = "July";
    }
    else if (nMonth == 7) {
      strNewDataValue = "August";
    }
    else if (nMonth == 8) {
      strNewDataValue = "September";
    }
    else if (nMonth == 9) {
      strNewDataValue = "October";
    }
    else if (nMonth == 10) {
      strNewDataValue = "November";
    }
    else if (nMonth == 11) {
      strNewDataValue = "December";
    }
    else if (nMonth == 12) {
      strNewDataValue = "January";
    }
    else {
      UtilityFunctions.stdoutwriter
        .writeln("Problem converting month to begin calendar year month"
            ,Logs.ERROR,"PF44.5");
    }

    String[] tmp = {"ticker","begin_fiscal_calendar"};
    propTableData.add(0,tmp);
    String[] values = {strTicker,strNewDataValue};
    propTableData.remove(1);
    propTableData.add(values);

    dbf.updateTableIntoDB(propTableData,"entities");
  }
  */
  
  /*
  public void postProcessYahooBeginCalendarYear() {
    // Created these 2 yahoo data captures because there were so many errors
    // in the SEC data.
    // Your tax dollars at work.
    
    int nMonth = MoneyTime.convertMonthStringtoInt(strDataValue.substring(0,3));

    // These are mostly retail stocks whose fiscal calendar doesn't end
    // directly at the end of a month.
    if ((strTicker.equals("AVY")) || (strTicker.equals("CERN")) ||
        (strTicker.equals("CPB")) || (strTicker.equals("CSC")) ||
        (strTicker.equals("DIS")) || (strTicker.equals("JEC")) ||
        (strTicker.equals("JNJ")) || (strTicker.equals("K")) ||
        (strTicker.equals("MU"))  || (strTicker.equals("SNA")) ||
        (strTicker.equals("SNDK")) || (strTicker.equals("SWK"))||
        (strTicker.equals("SWY")) || (strTicker.equals("SYMC")) ||
        (strTicker.equals("TXT")) || (strTicker.equals("VAR")) ||
        (strTicker.equals("VFC")) || (strTicker.equals("XLNX"))) {
      nMonth--;
    }

    //We have end of year, need beginning of year 
    nMonth++;

    //boundary condition
    if (nMonth==13) {
      nMonth=1;
    }

    String strMonth = MoneyTime.convertMonthInttoString(nMonth);
    String[] tmp = {"ticker","begin_fiscal_calendar"};
    propTableData.add(0,tmp);
    String[] values = {strTicker,strMonth};
    propTableData.remove(1);
    propTableData.add(values);
    dbf.updateTableIntoDB(propTableData,"entities");
  }
  */
  
  /*
  public void postProcessYahooCompanyName() {
    String[] tmp = {"ticker","full_name"};
    propTableData.add(0,tmp);
    String[] values = {dg.strOriginalTicker,propTableData.get(1)[0]};
    propTableData.remove(1);
    propTableData.add(values);
    dbf.updateTableIntoDB(propTableData, "entities");
  }
  */
  
  /*
  public void postProcessYahooFiscalYearEndRaw() {
    //Created these 2 yahoo data captures because there were so many errors
    // in the SEC data.
    // Your tax dollars at work.
    
    String[] tmp = {"ticker","actual_fiscal_year_end"};
    propTableData.add(0,tmp);
    String[] values = {strTicker,strDataValue};
    propTableData.remove(1);
    propTableData.add(values);

    dbf.updateTableIntoDB(propTableData,"entities");
  }
  */
  
  /*
  public void postProcessGoogleEPSTable() throws SQLException {
    String strTicker = dg.strOriginalTicker;
    String[] rowdata, newrow;
    String[] colheaders = propTableData.get(0);
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] tmpArray = {"value","date_collected","entity_id","fiscalquarter",
        "fiscalyear","calquarter","calyear"};
    newTableData.add(tmpArray);
    String tmpVal;
    
    for (int row=2;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);

      for (int col=0;col<colheaders.length;col++) {
        newrow = new String[16];
        if (rowdata[col].compareTo("void") != 0) {
          tmpVal = rowdata[col];

          if (tmpVal.contains("span")) {
            //this is a negative number
            newrow[0] = tmpVal.substring(tmpVal.indexOf(">-")+1,
                tmpVal.indexOf("</"));
          }
          else {
            newrow[0] = tmpVal;
          }
          
          // Some tickers have tables with only 4 columns. The table
          // processing will wrap around to the next row in those cases.
          if (newrow[0].contains("Diluted")) {
            continue;
          }

          // Berkshire tends to be the only company with an EPS in
          // the thousands.
          newrow[0] = newrow[0].replace(",", "");
          newrow[1] = "NOW()";
          newrow[2] = dg.nCurrentEntityId + "";

          // OFP 11/12/2011 - I have to deal with this wacky issue where
          // google has the end of quarter days off by one in some instances. 
          // 
          // CSC is one example. For calendar quarter 2, cal year 2011, they
          // have the end date listed as 7/1/2011 instead of 6/30/2011. 
          // 
          // So as a workaround, if the Day of month is 1, I'm going to 
          // subtract one day from the date.
          // 
          // It's not known at this time if this will screw up other tickers.
          // 
          // If other tickers behave differently then I may have to set up
          // ticker specific code in here.
          Calendar cal = Calendar.getInstance();
          String[] date = colheaders[col].split("-");

          cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
          cal.set(Calendar.MONTH,Integer.parseInt(date[1])-1);
          cal.set(Calendar.YEAR,Integer.parseInt(date[0]));

          if (cal.get(Calendar.DAY_OF_MONTH)==1) {
            cal.add(Calendar.DAY_OF_MONTH,-1);
          }

          String strFiscalYearQuarter = MoneyTime
              .getFiscalYearAndQuarter(strTicker, cal.get(Calendar.MONTH)+1, 
                  cal.get(Calendar.YEAR), dbf);
          String strCalYearQuarter = MoneyTime
              .getCalendarYearAndQuarter(strTicker, 
                  Integer.parseInt(strFiscalYearQuarter.substring(0,1)), 
                  Integer.parseInt(strFiscalYearQuarter.substring(1,5)),dbf);

          newrow[3] = strFiscalYearQuarter.substring(0,1);

          // Don't use they year returned from getFiscalYearAndQuarter - use 
          // the one retrieved from the web page
          newrow[4] = strFiscalYearQuarter.substring(1,5);
          newrow[5] = strCalYearQuarter.substring(0,1);
          newrow[6] = strCalYearQuarter.substring(1,5);
          
          newTableData.add(newrow);
        }
      }
    }
    propTableData = newTableData;
  }
  */
  
  /*
  
  */

  /*
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
        
        // Have to convert liters to gallons and Canadian $s to US $s
        try {
          BigDecimal bdPrice = UtilityFunctions
              .convertToGallonsAndDollars(data[i], "USDCAD", newrow[1], dbf);
          bdPrice = bdPrice
              .divide(new BigDecimal("100"),BigDecimal.ROUND_HALF_UP);
          newrow[0] = bdPrice.toString();
        }
        catch (SQLException sqle) {
          UtilityFunctions.stdoutwriter
            .writeln("Problem converting to Gallons and Dollars, Country " + 
                strCountry + " skipped",Logs.WARN,"PF200.25");
          continue; 
        }
      }

      String query = "select entities.id from entities ";
      query += " join countries_entities on countries_entities.entity_id" +
          " = entities.id ";
      query += " join countries on countries.id=countries_entities.country_id ";
      query += " where ticker='macro' ";
      query += " and countries.name='"+strCountry+"'";

      try   {
        ResultSet rs = dbf.db_run_query(query);
        rs.next();
        newrow[2] = rs.getInt("id") + "";
      }
      catch (SQLException sqle) {
        // This is not a fatal error so we won't display the full exception.
        UtilityFunctions.stdoutwriter
          .writeln("Problem looking up country: " + strCountry + 
              ",row skipped",Logs.ERROR,"PF300.25");
        continue; 
      }
      newTableData.add(newrow);
    }
    newTableData.add(0, tmpArray);
    propTableData = newTableData;
  }
  */
  
  /*
 
  */

  /*
  public void postProcessMWatchEPSEstTable()
      throws SQLException,SkipLoadException {
    // OFP 11/12/2011 - One issue with this data source is because the column
    // headers are relative (i.e. "this quarter", "next quarter") there is no
    // way to guarantee which quarter marketwatch is referring to. If they
    // are slow in updating the website, then the data will be collected for
    // the wrong quarters.
    
    // As of 12/15/2010 there is no data for RAND nor VIA and no obvious
    // string to key off of for a no data check.

    String[] rowdata, newrow;
    String[] colheaders = propTableData.get(0);
    Calendar cal = Calendar.getInstance();
    ArrayList<String[]> newTableData = new ArrayList<String[]>();

    String[] tmpArray = {"value","date_collected","entity_id","fiscalyear"};

    if (propStrTableDataSet.contains("q")) {
      int nSize = tmpArray.length;
      tmpArray = UtilityFunctions.extendArray(tmpArray);
      tmpArray = UtilityFunctions.extendArray(tmpArray);
      tmpArray = UtilityFunctions.extendArray(tmpArray);
      tmpArray[nSize] = "fiscalquarter";
      tmpArray[nSize+1] = "calquarter";
      tmpArray[nSize+2] = "calyear";
      
      // OFP 11/12/2011 - Ran into an issue where the column headers weren't
      // being read correctly.
      if (!colheaders[0].toUpperCase().equals("THIS QUARTER")) {
        UtilityFunctions.stdoutwriter
          .writeln("Invalid column header format for entity_id " +
              dg.nCurrentEntityId,Logs.STATUS2,"PF46");
        throw new SkipLoadException();
      }
    }
    
    newTableData.add(tmpArray);
    String tmpVal;
    
    for (int row=2;row<propTableData.size();row++) {
      rowdata = propTableData.get(row);

      for (int col=0;col<colheaders.length;col++) {
        newrow = new String[7];
        if (rowdata[col].compareTo("void") != 0) {
          tmpVal = rowdata[col];

          if (tmpVal.contains("span")) {
            //this is a negative number
            newrow[0] = tmpVal.substring(tmpVal.indexOf(">-")+1,
                tmpVal.indexOf("</"));
          }
          else {
            newrow[0] = tmpVal;
          }

          // Berkshire tends to be the only company with an EPS in
          // the thousands.
          newrow[0] = newrow[0].replace(",", "");
          newrow[1] = "NOW()";
          newrow[2] = dg.nCurrentEntityId + "";
          String strFiscalYearQuarter = MoneyTime
              .getFiscalYearAndQuarter(strTicker,cal.get(Calendar.MONTH),
                  cal.get(Calendar.YEAR),dbf);
          int nFiscalQuarter=Integer
              .parseInt(strFiscalYearQuarter.substring(0,1));
          int nFiscalYear = Integer
              .parseInt(strFiscalYearQuarter.substring(1,5));

          if (colheaders[col].toUpperCase().equals("NEXT QUARTER")) {
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

          // don't use the year returned from getFiscalYearAndQuarter - use
          // the one retrieved from the web page
          if (propStrTableDataSet.contains("q")) {
            String strCalYearQuarter = MoneyTime
                .getCalendarYearAndQuarter(strTicker, nFiscalQuarter,
                    nFiscalYear,dbf);
            
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
  */
  
  /*
  public boolean preNDCBloombergQuote() {
    // Intermittently a quote doesn't return a value. 
    String strRegex = "(?i)(There are no matches for your search)";
    Pattern pattern = Pattern.compile(strRegex);
    Matcher matcher = pattern.matcher(dg.returned_content);
    
    // OFP 2/18/2012 - Bloomberg changed the format of their quote page and
    // several tickers were just showing blank quotes without any messages so
    // we added this search for the existence of the " price"> tag.
    String strRegex2 = "(?i)(\" price\">)";
    Pattern pattern2 = Pattern.compile(strRegex2);
    Matcher matcher2 = pattern2.matcher(dg.returned_content);
    
    if (matcher.find()) {
      UtilityFunctions.stdoutwriter
        .writeln("Bloomberg Quote found no match for ticker " +
            dg.strCurrentTicker + ", skipping",Logs.WARN,"PF23.8");

      String[] tmp = new String[3];
      tmp[0]="Task:"+this.dg.nCurTask;
      tmp[1]="URL:"+this.dg.strStage2URL;
      tmp[2]=this.dg.returned_content;
      UtilityFunctions.dumpStrings(tmp, "/tmp/dump/dumpstring.txt");
      return(true);
    }
    else if (!matcher2.find()) {
      UtilityFunctions.stdoutwriter
        .writeln("Bloomberg Quote returned a blank quote for ticker " + 
            dg.strCurrentTicker + ", skipping",Logs.WARN,"PF23.85");
      return true;
    }
    return(false);
  }
  */
  
  /*
  public boolean preNDCYahooEPSEst() {
    String strRegex = "(?i)(There is no Analyst Estimates)";
    UtilityFunctions.stdoutwriter
      .writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF45");

    Pattern pattern = Pattern.compile(strRegex);
  
    Matcher matcher = pattern.matcher(dg.returned_content);
    
    return(matcher.find());
  }
  */
  
  /*
  public boolean preNDCNasdaqEPSEst() {

    String strRegex = "(?i)(No Data Available)";
    UtilityFunctions.stdoutwriter
      .writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF46");

    Pattern pattern = Pattern.compile(strRegex);
    Matcher matcher = pattern.matcher(dg.returned_content);
    
    // OFP 12/19/2012 - Nasdaq misspelled available.
    String strRegex2 = "(?i)(No data avaiable)";
    Pattern pattern2 = Pattern.compile(strRegex2);
    Matcher matcher2 = pattern2.matcher(dg.returned_content);

    if (matcher.find() || matcher2.find()) {
      UtilityFunctions.stdoutwriter
        .writeln("Nasdaq no data avialable for ticker " + dg.strCurrentTicker,
            Logs.WARN,"PF21.38");
      return(true);
    }

    strRegex = "(?i)(feature currently is unavailable)";
    UtilityFunctions.stdoutwriter.writeln("NDC regex: " + strRegex,
        Logs.STATUS2,"PF46.5");

    pattern = Pattern.compile(strRegex);
    matcher = pattern.matcher(dg.returned_content);

    if (matcher.find()) {
      UtilityFunctions.stdoutwriter.writeln("Nasdaq message feature " +
          "currently unavailable for ticker " + dg.strCurrentTicker,
          Logs.WARN,"PF21.40");
      return true;
    }
    return false;
  }
  */
  
  /*
  public boolean preNDCNasdaqFiscalYear() {
    String strRegex = "(?i)(This feature currently is unavailable)";
    UtilityFunctions.stdoutwriter
      .writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF47");

    Pattern pattern = Pattern.compile(strRegex);
    Matcher matcher = pattern.matcher(dg.returned_content);
    return(matcher.find());

  }
  */
  
  /*
  public boolean preNDCDataInputTest() {
    String strRegex = "(?i)(End of Data)";
    UtilityFunctions.stdoutwriter
      .writeln("NDC regex: " + strRegex,Logs.STATUS2,"PF47");
    
    Pattern pattern = Pattern.compile(strRegex);
    Matcher matcher = pattern.matcher(dg.returned_content);
    return(matcher.find());
  }
  */
  
  /*
  public void postProcessDataInputTest2() throws SQLException {
    ArrayList<String[]> newTableData = new ArrayList<String[]>();
    String[] tmpArray = {"entity_id","date_collected","value"};
    propTableData.remove(0);

    for (String[] row : propTableData) {
      String[] newrow = new String[tmpArray.length];

      newrow[0] = row[1];
      newrow[1] = "'" + row[0] + "'";
      newrow[2] = row[2];
      newTableData.add(newrow);
    }

    String strQuery = "update input_test_controls set sequence=sequence+1 ";
    strQuery += " where task_id=" + dg.nCurTask;

    try {
      dbf.db_update_query(strQuery);
    }
    catch (SQLException sqle) {
      UtilityFunctions.stdoutwriter
        .writeln("SQL Problem with postProcessDataInputTest",
            Logs.ERROR,"PF42.5");
    }
    newTableData.add(0, tmpArray);
    propTableData = newTableData;
  }
  */
  
  /*
  public boolean preJobProcessDataInputTest2() throws SQLException {
    String strQuery = "select * from input_test_controls ";
    strQuery += " where task_id=" + dg.nCurTask;

    try {
      ResultSet rs = dbf.db_run_query(strQuery);
      rs.next();
      dg.strStage1URL = dg.strStage1URL.replace("${seqnumber}", 
          rs.getInt("sequence")+"");
      dg.strStage1URL = dg.strStage1URL.replace("${dataset}", 
          rs.getInt("data_set") + "");
      dg.strStage1URL = dg.strStage1URL.replace("${startbatchid}", 
          rs.getInt("start_batch_id") + "");
    }
    catch (SQLException sqle) {
      UtilityFunctions.stdoutwriter
        .writeln("SQL Problem with preProcessDataInputTest",
            Logs.ERROR,"PF42.5");
      return(false);
    }

    return(true);
  }
  */
  
  public boolean preProcessSECFiscalYear() throws SQLException {
    // Currently no analyst estimates for IVZ or LUK.
    
    String strNewTicker="";
        
    if (strTicker.equals("BF/B")) {
      strNewTicker = "BFB";		
    }
    else if (strTicker.equals("BRK/A")) {
      strNewTicker = "BRKA";
    }
    // Can't find the ticker symbol for CareFusion on the SEC site so using 
    // the CIK # instead
    else if (strTicker.equals("CFN")) {
      strNewTicker ="0001457543";
    }
    else if (strTicker.equals("QEP")) {
      strNewTicker = "0001108827";
    }
    else if (strTicker.equals("SNI")) {
      strNewTicker = "0001430602";
    }
    else if (strTicker.equals("ZION")) {
      strNewTicker = "0000109380";
    }
    
    if (strNewTicker.equals("") == false) {
      dg.strCurrentTicker=strNewTicker;
    }
    // TODO Need to get rid of these return values and go with exceptions 
    return(true);
  }

}
