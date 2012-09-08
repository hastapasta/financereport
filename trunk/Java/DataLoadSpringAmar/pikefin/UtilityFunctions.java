package pikefin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import java.util.HashMap;

import java.util.Properties;

import java.io.*;
import java.math.BigDecimal;

import java.util.regex.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.rosaloves.bitlyj.Url;
import static com.rosaloves.bitlyj.Bitly.*;

import org.apache.commons.mail.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import pikefin.log4jWrapper.MainWrapper;
import pikefin.log4jWrapper.Logs;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine; 


/*
 * This class should be threadsafe. All of the class properties are set once at startup and
 * MainWrapper functions are synchronized.
 * 
 * This is basically a hybrid singleton/static class. One one instance is needed for the entire application.
 * 
 * Functionally the only difference I've seen between a static and a singleton class is how the class loader handles them. Static
 * class is automatically loaded by class loader at the beginning whereas a singleton class can have its loading managed.
 */


public class UtilityFunctions {
	

	//static public boolean bCalledByJsp;
	//String strCapturedOutput;

	static MainWrapper stdoutwriter;
	
	private static String strEmailUser;
	private static String strEmailPassword;
	private static String strEmailHost;
	private static int nEmailPort;
	private static String strEmailFromAddy;
	private static String strEmailCakeUrl;
	private static String strEmailPhpUrl;
	private static String strEmailSubjectText;
	
	private static boolean bDebugMode;
	
	private static VelocityEngine ve;
	
	
	static final BigDecimal bdGallonsPerLiter = new BigDecimal(".264");
	

	
	/*public Connection getConnection()
	{
		return(UtilityFunctions.con);
		
	}*/
	
	public void setMainWrapper(MainWrapper inputMW) {
		stdoutwriter = inputMW;
		
	}
	
	public void setVe(VelocityEngine inputVE) {
		ve = inputVE;
		ve.init();
	}
	
	
	public static String getEmailPassword() {
		return strEmailPassword;
	}
	
	public static boolean getDebugMode() {
		return bDebugMode;
	}
	
	public static String getEmailUser() {
		return strEmailUser;
	}
	
	public void setEmailPassword(String inputPass) {
		strEmailPassword = inputPass;
	}
	
	public void setDebugMode(boolean inputDebugMode) {
		bDebugMode = inputDebugMode;
	}
	
	public void setEmailUser(String inputUser) {
		strEmailUser = inputUser;
	}
	
	public void setEmailPort(String inputPort) {
		nEmailPort = Integer.parseInt(inputPort);
	}
	
	public static int getEmailPort() {
		return nEmailPort;
	}
	
	public static String getEmailHost() {
		return strEmailHost;
	}
	
	public void setEmailHost(String inputHost) {
		strEmailHost = inputHost;
	}
	
	public static String getEmailFromAddy() {
		return strEmailFromAddy;
	}
	
	public void setEmailFromAddy(String inputFromAddy) {
		strEmailFromAddy = inputFromAddy;
	}
	
	public static String getEmailCakeUrl() {
		return strEmailCakeUrl;
	}
	
	public void setEmailCakeUrl(String inputCakeUrl) {
		strEmailCakeUrl = inputCakeUrl;
	}
	
	public static String getEmailPhpUrl() {
		return strEmailPhpUrl;
	}
	
	public void setEmailPhpUrl(String inputPhpUrl) {
		strEmailPhpUrl = inputPhpUrl;
	}
	
	public static String getEmailSubjectText() {
		return strEmailSubjectText;
	}
	
	public void setEmailSubjectText(String input) {
		strEmailSubjectText = input;
	}
		
		
	
	
	

		
	
	public UtilityFunctions() {

		/*try {

			//UtilityFunctions.stdoutwriter = new MainWrapper();
			//UtilityFunctions.stdoutwriter.writeln("instantiating utility functions",Logs.STATUS1,"UF20.1");
	

		}
		catch (Exception e)	{
			System.out.println("UtilityFunctions constructor failed.");
			e.printStackTrace();
		}*/
			

	}
	
	/*
	 * The following 2 functions were written because I thought there was a multithreading 
	 * issue with how HttpClient was being utilitzed. The issue at that time turned out to
	 * be caused by something else. I still think that the current HttpCLient implementation
	 * is thread-safe and I don't want to rework something right now that looks to be working.
	 * But having said that, I'm leaving these functions in here for the time being in case it's later
	 * deemed necessary to make a change and go with the THreadSafeConnectionManager.
	 * 
	 * 
	 */
	
	/*private void createThreadSafeConnectionManager()
	{
		
		this.params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, 100);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        
        // Create and initialize scheme registry 
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        this.cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        this.httpClient = new DefaultHttpClient(this.cm, this.params);	
				
	}
	
	public HttpClient getHttpClient()
	{
		return(this.httpClient);
	}*/
	
	/*public static void db_update_query(String strUpdateStmt) throws DataAccessException
	{

	

		//try
		//{
			stdoutwriter.writeln("Executing SQL: " + strUpdateStmt,Logs.SQL,"UF1");
			stdoutwriter.writeln(strUpdateStmt,Logs.SQL,"UF2");
			Statement stmt = con.createStatement();
			stmt.execute(strUpdateStmt);
			if (strUpdateStmt.substring(0,6).equals("update") && stmt.getUpdateCount() == 0)
				stdoutwriter.writeln("No rows were updated",Logs.ERROR,"UF2.5");
				
			
		///}
		//catch (DataAccessException sqle)
		//{
		//	stdoutwriter.writeln("1 SQL statement failed: " + strUpdateStmt);
		//	stdoutwriter.writeln(sqle);
	//
			
		//}
		
		
		
		
	}*/
	
	public static int regexSeekLoop(String regex, int nCount, int nCurOffset, String strHayStack)
	throws TagNotFoundException {
		// nCurOffset =
		// regexSeekLoop("(?i)(<TABLE[^>]*>)",returned_content,tables);
		// String strOpenTableRegex = "(?i)(<TABLE[^>]*>)";
		// String strOpenTableRegex = "START NEW";
		
		Pattern pattern = Pattern.compile(regex);
		
		Matcher matcher = pattern.matcher(strHayStack);
		
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
					+ ", offset: " + nCurOffset, Logs.STATUS2, "UF3");
		
		}
		return (nCurOffset);
	}
	
	public static String regexSnipValue(String strBeforeUniqueCode,
			String strAfterUniqueCode, int nCurOffset,String strHayStack)
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
					Logs.STATUS2, "UF4");

			pattern = Pattern.compile(strBeforeUniqueCodeRegex);
			UtilityFunctions.stdoutwriter.writeln(
					"after strbeforeuniquecoderegex compile", Logs.STATUS2,
					"UF5");

			matcher = pattern.matcher(strHayStack);

			UtilityFunctions.stdoutwriter.writeln(
					"Current offset before final data extraction: "
							+ nCurOffset, Logs.STATUS2, "UF6");

			matcher.find(nCurOffset);

			nBeginOffset = matcher.end();
		} else
			nBeginOffset = nCurOffset;

		UtilityFunctions.stdoutwriter.writeln("begin offset: " + nBeginOffset,
				Logs.STATUS2, "UF7");

		// String strAfterUniqueCodeRegex = "(?i)(" + strAfterUniqueCode + ")";
		String strAfterUniqueCodeRegex = "(" + strAfterUniqueCode + ")";
		pattern = Pattern.compile(strAfterUniqueCodeRegex);
		UtilityFunctions.stdoutwriter.writeln(
				"after strAfterUniqueCodeRegex compile", Logs.STATUS2, "UF8");

		matcher = pattern.matcher(strHayStack);

		matcher.find(nBeginOffset);

		int nEndOffset = matcher.start();
		UtilityFunctions.stdoutwriter.writeln("end offset: " + nEndOffset,
				Logs.STATUS2, "UF9");

		if (nEndOffset <= nBeginOffset) {
			/*
			 * If we get here, skip processing this table cell but continue
			 * processing the rest of the table.
			 */
			UtilityFunctions.stdoutwriter.writeln("EndOffset is < BeginOffset",
					Logs.STATUS2, "UF10");
			throw new CustomRegexException();
		}
		strDataValue = strHayStack.substring(nBeginOffset, nEndOffset);
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
						Logs.STATUS2, "UF10");
				
			}
			catch (Exception e) {
				UtilityFunctions.stdoutwriter.writeln(e);
			}

		}*/

		UtilityFunctions.stdoutwriter.writeln(
				"Raw Data Value: " + strDataValue, Logs.STATUS2, "UF11");
		/*
		 * } catch (IOException ioe) { Sy }
		 */

		return (strDataValue);

	}

	

	public static HashMap<TheKey,HashMap<String,String>> convertRowSetToHashMap(SqlRowSet rs,String strColumnKey1,String strColumnKey2) throws DataAccessException, PikefinException	{
		//HashMap<String,HashMap<String,String>> parentHash = null;
		HashMap<TheKey,HashMap<String,String>> parentHash = null;
	
		SqlRowSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();
		String strHashKey1=null;
		String strHashKey2=null;
		
		while (rs.next())
		{
			if (parentHash==null)
				//parentHash = new HashMap<String,HashMap<String,String>>();
				parentHash = new HashMap<TheKey,HashMap<String,String>>();
				
			//String[] tmpArray = new String[numberOfColumns];
			HashMap<String,String> childHash = new HashMap<String,String>();
		
			for (int i=1;i<=numberOfColumns;i++)
			{
				int j = rsMetaData.getColumnType(i);
				String strValue="";
				switch(j)
				{
				
			
					case java.sql.Types.BIT:
						strValue = "false";
						if (rs.getInt(i) != 0)
							strValue = "true";
						break;
						
					case java.sql.Types.DATE:
						if (rs.getDate(i) != null)
							strValue = rs.getDate(i).toString();
						else
							strValue = null;
						break;
						
						
						
					case java.sql.Types.INTEGER:
						strValue = rs.getInt(i) + "";
						if (rs.wasNull())
							strValue=null;
						break;
						
					case java.sql.Types.VARCHAR:
						strValue = rs.getString(i);
						break;
						
					case java.sql.Types.DECIMAL:
						if (rs.getBigDecimal(i) == null)
							strValue = null;
						else
							strValue = rs.getBigDecimal(i).toString();
						break;
					
					case java.sql.Types.TIMESTAMP:
						if (rs.getDate(i) == null)
							strValue = null;
						else
						{
							//rs.getDate() drops the time						
							strValue = rs.getTimestamp(i).toString();
						}
						break;
						
					default:
						stdoutwriter.writeln("Problem converting ResultSet to HashMap - unhandled column type",Logs.ERROR,"UF18.5");
						throw new PikefinException("Custom SQL Exception - Unable to convert ResultSet to HashMap");
						
						
				}
				if (rsMetaData.getColumnName(i).equals(strColumnKey1))
					strHashKey1 = strValue;
				if (rsMetaData.getColumnName(i).equals(strColumnKey2))
					strHashKey2 = strValue;
					
				childHash.put(rsMetaData.getTableName(i)+"."+rsMetaData.getColumnName(i), strValue);
				//tmpArray[i-1] = strValue;
			
			}
			if (strHashKey1 == null) {
				stdoutwriter.writeln("Problem converting ResultSet to HashMap - column for hash key not found",Logs.ERROR,"UF18.7");
				throw new PikefinException("Custom SQL Exception - Unable to convert ResultSet to HashMap");
			}
			
			TheKey tk = new TheKey(strHashKey1,strHashKey2);
			//parentHash.put(strHashKey,childHash);
			parentHash.put(tk, childHash);
		}
		
		return(parentHash);
	}
	
	public static ArrayList<String[]> convertRowSetToArrayList2(SqlRowSet rs) throws DataAccessException, PikefinException {
		ArrayList<String[]> tmpArrayList = new ArrayList<String[]>();
		SqlRowSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();
		//int nRow=0;
		
		while (rs.next()) {
			String[] tmpArray = new String[numberOfColumns];
			for (int i=1;i<=numberOfColumns;i++)
			{
				int j = rsMetaData.getColumnType(i);
				String strValue="";
				switch(j)
				{
				
			
					case java.sql.Types.BIT:
						strValue = "false";
						if (rs.getInt(i) != 0)
							strValue = "true";
						break;
						
					case java.sql.Types.DATE:
						if (rs.getDate(i) != null)
							strValue = rs.getDate(i).toString();
						else
							strValue = null;
						break;
						
					case java.sql.Types.INTEGER:
						strValue = rs.getInt(i) + "";
						break;
						
					case java.sql.Types.VARCHAR:
						strValue = rs.getString(i);
						break;
						
					case java.sql.Types.DECIMAL:
						if (rs.getBigDecimal(i) == null)
							strValue = null;
						else
							strValue = rs.getBigDecimal(i).toString();
						break;
						  
						  //try to find the fact key in fact_data
					case java.sql.Types.TIMESTAMP:
						if (rs.getDate(i) == null)
							strValue = null;
						else
							strValue = rs.getTimestamp(i).toString();
						break;
						
					default:
						stdoutwriter.writeln("Problem converting ResultSet to ArrayList - unhandled column type",Logs.ERROR,"UF18.5");
						throw new PikefinException("Custom SQL Exception - Unable to convert ResultSet to ArrayList");
						
						
				}
				//tmpHash.put(rsMetaData.getTableName(i)+"."+rsMetaData.getColumnName(i), strValue);
				tmpArray[i-1] = strValue;
				
			}
			tmpArrayList.add(tmpArray);
			//nRow++;
			/*if (nRow==ntmp)
			{
				String tmp = "blap";
			}*/
		}
		
		
		return(tmpArrayList);
		
		
		
		
		
		
		
	}
	
	//public static ArrayList<HashMap<String,String>> convertResultSetToArrayList(ResultSet rs) throws DataAccessException
	public static ArrayList<HashMap<String,String>> convertRowSetToArrayList(SqlRowSet rs,String strUnique) throws DataAccessException, PikefinException {
		
		//ArrayList<HashMap<String,String>> tmpArrayList = null;
		ArrayList<HashMap<String,String>> tmpArrayList = null;
		SqlRowSetMetaData rsMetaData = rs.getMetaData();
		
		HashMap<String,String> hashUnique = new HashMap<String,String>();
		int numberOfColumns = rsMetaData.getColumnCount();
		//int nRow=0;
		//int ntmp = 200;
		while (rs.next()) {
			if (tmpArrayList == null)
				//tmpArrayList = new ArrayList<HashMap<String,String>>();
				tmpArrayList = new ArrayList<HashMap<String,String>>();
			
			//HashMap<String,String> tmpHash = new HashMap<String,String>();
			HashMap<String,String> tmpHash = new HashMap<String,String>();
			for (int i=1;i<=numberOfColumns;i++) {
				int j = rsMetaData.getColumnType(i);
				String strValue="";
				switch(j)
				{
				
			
					case java.sql.Types.BIT:
						strValue = "false";
						if (rs.getInt(i) != 0)
							strValue = "true";
						break;
						
					case java.sql.Types.DATE:
						if (rs.getDate(i) != null)
							strValue = rs.getDate(i).toString();
						else
							strValue = null;
						break;
						
					case java.sql.Types.INTEGER:
						strValue = rs.getInt(i) + "";
						if (rs.wasNull())
							strValue = null;
						break;
						
					case java.sql.Types.VARCHAR:
						strValue = rs.getString(i);
						break;
						
					case java.sql.Types.DECIMAL:
						if (rs.getBigDecimal(i) == null)
							strValue = null;
						else
							strValue = rs.getBigDecimal(i).toString();
						break;
						  
						  //try to find the fact key in fact_data
					case java.sql.Types.TIMESTAMP:
						if (rs.getDate(i) == null)
							strValue = null;
						else
							strValue = rs.getTimestamp(i).toString();
						break;
						
					default:
						stdoutwriter.writeln("Problem converting ResultSet to ArrayList - unhandled column type",Logs.ERROR,"UF18.5");
						throw new PikefinException("Custom SQL Exception - Unable to convert ResultSet to ArrayList");
						
						
				}
				String strTableColumn = rsMetaData.getTableName(i)+"."+rsMetaData.getColumnName(i);
				if (strUnique != null) {
					if (strTableColumn.equalsIgnoreCase(strUnique)) {
						if (hashUnique.get(strValue) != null) {
							stdoutwriter.writeln("Duplicate value " + strValue + " for column " + strUnique + " which was designated as unique, check your query",Logs.ERROR,"UF100");
							continue;
						}
						else
							hashUnique.put(strValue, "1");
					}
				}

				tmpHash.put(strTableColumn, strValue);
				
			}
			tmpArrayList.add(tmpHash);
			//nRow++;
			/*if (nRow==ntmp)
			{
				String tmp = "blap";
			}*/
		}
		
		
		return(tmpArrayList);
	}
	
	
	
	
	public static void createCSV(ArrayList<String[]> tabledata,String filename,boolean append)	{
		try	{
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename,append));
			String strLine="";
			String[] rowdata;
	
	    for (int x=0;x<tabledata.size();x++) {
	    	rowdata = tabledata.get(x);
	    	strLine="";
				for (int y=0;y<rowdata.length;y++) {
					if (y !=0)
						strLine = strLine +",";
						
		    	strLine = strLine + "\"" + rowdata[y] + "\"";
		    	   
		    }
		    writer.write(strLine);
		    writer.newLine();
	     
	    }
	
	
	    writer.close();  // Close to unlock and flush to disk.
	  }
	  catch (IOException ioe)  {
	  	stdoutwriter.writeln("Problem writing CSV file",Logs.ERROR,"UF18");
	  	stdoutwriter.writeln(ioe);
	  }
		
	}
	
	public static ArrayList<String[]> convertCSV(String strData, String strDelimiter, String strEncloseString) {
		//not fully implemented yet
		return null;
		
		
	}
	
	
	public static ArrayList<String[]> readInCSV(String strFilename, String strDelimiter, String strEncloseString) {
		/*This code will not correctly handle the delimiter character (e.g. ",") being inside a quoted
		string (e.g. "5,600,000"). One way to handle this is to do the following:
		1) Replace ,,, with |||
		2) Replace ,, with ||
		3) Replace "," with "|"
		4) Replace , with <blank>
		5) Replace | with ,
		
		You may optionally need to do:
		6) Replace ,, with ,"", (2 X)
		*/
		int nLineCount=0;
		String nTmp="";
		try
		{
			String strCurToken;
			//StringTokenizer st;
			//int nTokenCount;
			ArrayList<String[]> arraylist = new ArrayList<String[]>();
			ArrayList<String> rowarraylist;
			boolean bEncloseBefore = false;
			boolean bEncloseAfter = false;
			
			BufferedReader in = new BufferedReader(new FileReader(strFilename));
			
			while ((nTmp = in.readLine()) != null)
			{
				/*if ((nLineCount < 70) || (nLineCount > 80))
					continue;*/
					
				rowarraylist = new ArrayList<String>();
				/*was using string tokenizer, switch to split() since it returns tokens for consecutive delimiters, e.g ,, */
				//st = new StringTokenizer(nTmp, strDelimiter);
				String[] tokens = nTmp.split(strDelimiter);
				//nTokenCount = 0;
				for (int z=0;z<tokens.length;z++)
				{
					//strCurToken = st.nextToken();
					strCurToken = tokens[z];
					bEncloseBefore = false;
					bEncloseAfter = false;
	
					if (strCurToken.length() > 2)
					{
						if (strCurToken.substring(0,1).compareTo(strEncloseString) == 0)
							bEncloseBefore = true;
						if (strCurToken.substring(strCurToken.length() -1,strCurToken.length()).compareTo(strEncloseString) == 0)
							bEncloseAfter = true;
						if (((bEncloseBefore == true) && (bEncloseAfter != true)) ||
								((bEncloseBefore != true) && (bEncloseAfter == true)))
						/* there's an issue with the formatting where there's only one enclosure character*/
						{
							stdoutwriter.writeln("Problem with enclosure characters @ line " + nLineCount,Logs.ERROR,"UF19");
							return(null);
						}
						if ((bEncloseBefore == true) && (bEncloseAfter == true))
						/* Strip out the enclosure characters */
						{
					
							strCurToken = strCurToken.substring(1,strCurToken.length() - 1);
						}
					}
					rowarraylist.add(strCurToken);
					
						
					//nTokenCount++;
				}
				
				String[] strArray = new String[rowarraylist.size()];
				for (int i =0;i<rowarraylist.size();i++)
				{
					strArray[i] = rowarraylist.get(i);
					if (i==0)
						stdoutwriter.writeln(strArray[i],Logs.STATUS2,"UF20");
					else
						stdoutwriter.writeln("," + strArray[i],Logs.STATUS2,"UF21");
				}

				
				arraylist.add(strArray);
							
				nLineCount++;	
			}
			stdoutwriter.writeln(nLineCount + " lines processed.",Logs.STATUS2,"UF22");
			return(arraylist);
			
		} 
		catch (Exception e)
		{
			stdoutwriter.writeln("Problem reading CSV @ line " + nLineCount,Logs.ERROR,"UF23");
			stdoutwriter.writeln(e);
			return(null);
		}
	}
	
	
	
	public static void scrubCSV(String strFilename, String strDelimiter, String strEnclosure, String strOutFilename)
	{
		String strCurLine;
		String strNewLine = "";
		BufferedReader in = null;
		BufferedWriter out = null;
		try
		{
		
			in = new BufferedReader(new FileReader(strFilename));
			
			out = new BufferedWriter(new FileWriter(strOutFilename));
			
			
		
			Matcher matcher;
			
			String strRegex = "(" + strEnclosure + ")";
			Pattern pattern = Pattern.compile(strRegex);
			
			//int nOpenEnclosure, nCloseEnclosure;
			int nCurOffset=0;
			int nLineCount =0;
	
	    
			while((strCurLine = in.readLine()) != null)
			{
				strNewLine = "";
				matcher = pattern.matcher(strCurLine);
				stdoutwriter.writeln(strCurLine,Logs.STATUS2,"UF25");
				//nOpenEnclosure = 0;
				//nCloseEnclosure = 0;
				nCurOffset=0;
				while (true)
				{
					//search for opening enclosure
					//System.out.println("1 curOffset: " + nCurOffset);
					if (matcher.find(nCurOffset) == false)
						break;
					//else
						//nOpenEnclosure = matcher.end();
					
					strNewLine = strNewLine + strCurLine.substring(nCurOffset,matcher.end());
					nCurOffset = matcher.end();
					//System.out.println(strNewLine);
					
					//search for ending enclosure
					if (matcher.find(nCurOffset) == false)
					{
						stdoutwriter.writeln("Invalid syntax for enclosure characters at line " + nLineCount,Logs.ERROR,"UF26");
						break;
					}
					//else
						//nCloseEnclosure = matcher.end();
					
					strNewLine = strNewLine + strCurLine.substring(nCurOffset,matcher.end()).replace(strDelimiter,"");
					//System.out.println(strNewLine);
					//System.out.println(strCurLine.length());
					if (strCurLine.length() == matcher.end())
						break;
					else
						nCurOffset = matcher.end();
					
					
					
				}
			
				
				out.write(strNewLine);
				out.newLine();
				stdoutwriter.writeln(strNewLine,Logs.STATUS2,"UF27");
				nLineCount++;	
			}
			out.close();
			in.close();
			
		}
		catch(Exception e)
		{
			stdoutwriter.writeln("Problem Scrubbing csv file",Logs.ERROR,"UF28");
			stdoutwriter.writeln(e);
		}

		
		
		
		
		
	}
	

	
	public static String getElapsedTimeHoursMinutesSecondsString(Long elapsedTime) 
	{        
		String format = String.format("%%0%dd", 2);   
	    elapsedTime = elapsedTime / 1000;   
	    String seconds = String.format(format, elapsedTime % 60);   
	    String minutes = String.format(format, (elapsedTime % 3600) / 60);   
	    String hours = String.format(format, elapsedTime / 3600);   
	    String time =  hours + ":" + minutes + ":" + seconds;   
	    return time;   
	}  

	
	
	
	public static String[] extendArray(String[] inputArray)
	{
		String[] tmpArray = new String[inputArray.length + 1];
		for (int i=0; i<inputArray.length;i++)
		{
			tmpArray[i] = inputArray[i];
		}
		return(tmpArray);
		
	}
	
	public static void mail(String strEmail, String strMessage, String strSubject, String strFromAddy) {	
	  	//String host = "smtp.gmail.com";
	  	//int port = 587;
	  	//String username = "hastapasta99";
	  	//String password = "ginger1.";
		String username = UtilityFunctions.getEmailUser();
		String password = UtilityFunctions.getEmailPassword();

	  	Properties props = new Properties();
	  	//props.put("mail.smtp.port","587");
	  	//props.put("mail.smtp.host", "smtp.gmail.com");
	  	props.put("mail.smtp.port", UtilityFunctions.getEmailPort());
	  	props.put("mail.smtp.host", UtilityFunctions.getEmailHost());
	  	props.put("mail.smtp.auth", "true");
	  	props.put("mail.smtp.starttls.enable", "true");
	  	props.put("mail.debug", "false");
	  	
	  	/*
	  	 * Setting these timeout values since sending mail sometimes hangs.
	  	 */
	  	props.put("mail.smtp.timeout", 60000);
	  	props.put("mail.smtp.connectiontimeout", 60000);


	  	//Session session = Session.getInstance(props);
	  	Session session = Session.getInstance(props,new MyPasswordAuthenticator(username, password));



	  	try {

		    Message message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(strFromAddy));
		    message.setRecipients(Message.RecipientType.TO, 
	                      InternetAddress.parse(strEmail));
		    message.setSubject(strSubject);
		    message.setText(strMessage);

		    //Transport transport = session.getTransport("smtp");
		    //transport.connect(host, port, username, password);
		    //transport.connect(host,username,password);
		    
		    
		    //emails are disabled for testing
		    if (UtilityFunctions.getDebugMode() == false)
		    	Transport.send(message);

		    Broker.nMailMessageCount++;	

	  	} catch (MessagingException e) {
	  		
	  	    //throw new RuntimeException(e);
	  		stdoutwriter.writeln("Problem sending email.",Logs.ERROR,"UF28.5");
	  		stdoutwriter.writeln("Message Count: " + Broker.nMailMessageCount,Logs.ERROR,"UF28.52");
	  		stdoutwriter.writeln(e);
	  	}
	  	
	  	
	  }
	
	public static void htmlMail(String strEmail, String strMessage, String strSubject) {
		
		HtmlEmail email = new HtmlEmail();

		try {
			email.setHostName(UtilityFunctions.getEmailHost());
			email.setAuthentication(UtilityFunctions.getEmailUser(), UtilityFunctions.getEmailPassword());
			email.setSmtpPort(UtilityFunctions.getEmailPort());
			email.setFrom(UtilityFunctions.getEmailFromAddy());
			email.addTo(strEmail);
			email.setSubject(strSubject);
	
			email.setTextMsg("Pikefin Alert");
			email.setHtmlMsg(strMessage);
			email.setTLS(true);
	
			email.setDebug(false);
			
	
			email.send();
		}
		catch (EmailException ee) {
			stdoutwriter.writeln("Problem sending html email.",Logs.ERROR,"UF30.5");
	  		//stdoutwriter.writeln("Message Count: " + DataLoad.nMailMessageCount,Logs.ERROR,"UF28.52");
	  		stdoutwriter.writeln(ee);
			
		}

		
	}
	
	public static BigDecimal convertToGallonsAndDollars(String strValue, String strCurrencyCross, String strDay, DBFunctions dbf) throws DataAccessException {
		
		
		
		String strQuery = "select value from fact_data ";
		strQuery += " join entities on entities.id=fact_data.entity_id ";
		strQuery += " where ticker='" + strCurrencyCross + "'";
		strQuery += " and date_collected<" + strDay;
		strQuery += " order by date_collected desc";
		strQuery += " limit 1";
		
		// try	{
			  //ResultSet rs = dbf.db_run_query(strQuery);
			SqlRowSet rs = dbf.dbSpringRunQuery(strQuery);
			  rs.next();
			  BigDecimal bdRate = rs.getBigDecimal("value");
			  BigDecimal bdPrice = new BigDecimal(strValue);
			  
			  /*
			   * Little carry over from numerical analysis days. Set higher precision for 
			   * calculations and then set precision to 3 before outputting final #.
			   */
			  
			  bdRate.setScale(5);
			  bdPrice.setScale(5);
			 
			  bdPrice = bdPrice.divide(bdRate,BigDecimal.ROUND_HALF_UP);
			  bdPrice = bdPrice.divide(UtilityFunctions.bdGallonsPerLiter,BigDecimal.ROUND_HALF_UP);
			  bdPrice.setScale(3,BigDecimal.ROUND_HALF_UP);

			  return(bdPrice);
		 // }
		/*  catch (DataAccessException sqle)	{
			  UtilityFunctions.stdoutwriter.writeln("Problem looking up exchange rate for currency cross: USDCAD,row skipped",Logs.WARN,"PF200.25");
			  continue;	
		  }
		
		return(new BigDecimal(""));*/
	}
	
	
	public static String tweet(String strTweet,String strUser, String strPass, String strAuth1, String strAuth2, String strAuth3, String strAuth4) {	
	  	//String host = "smtp.gmail.com";
	  	//int port = 587;
	  	//String username = "hastapasta99";
	  	//String password = "madmax1.";
		
		//boolean bSuccess = true;
		String strErrorMsg = "";
		try	{
			ConfigurationBuilder cb = new ConfigurationBuilder();
	
			/*cb.setUser("pikefindotcom");
			cb.setPassword("ginger1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("5Fe0I6bfXMgizg1MQ1jMQ")
			  .setOAuthConsumerSecret("sSbfFG5GEqAxUqSy1newCgkEAvTuIHf7y9HwB2QNA0")
			  .setOAuthAccessToken("260464945-DQseZfWVTzAzuOSqz8ZkVCJqSm2Y44p5TQdVxhzU")
			  .setOAuthAccessTokenSecret("1UwoxCB63MhTPSs0EECCHLAPxn6p9iKZNqM298Sqs88");
			
			
			cb.setUser("pfin_forex");
			cb.setPassword("madmax1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("KPkMWKCAV8CYXQeyCG9VA")
			  .setOAuthConsumerSecret("cp4bW9UgQ04NWK9pcQHjesUiR6Ya37ahtvdOXOGyCOw")
			  .setOAuthAccessToken("382701296-AZNdnD7E5xZK610LZHYD4IkqxinI12Td9hA5CQNy")
			  .setOAuthAccessTokenSecret("KhnKstEri9xMA7oeoLzzY4br9H4iyjBjMvCNEpvahcQ");
			
			cb.setUser("pfin_commodity");
			cb.setPassword("madmax1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("7GWDzfijJ9KGaW9q8oLOvw")
			  .setOAuthConsumerSecret("AbkTMtmMdC3IEOnyxJw1SB5EHcXXfdlEt6pEPif6HnQ")
			  .setOAuthAccessToken("382679101-KBaGgDu8ry1Rl2VwlNGpXW7WzM2L4FgCt5Fyrabb")
			  .setOAuthAccessTokenSecret("V5IKgsNQCBccJy3w6DW4Keuk9Qh95MJkVQtsVrIuM");
			
			cb.setUser("pfin_us_stocks");
			cb.setPassword("madmax1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("wyxaktdjEu6OmQWEcRdCw")
			  .setOAuthConsumerSecret("PuXYGpfWbW1OE1RnmC0YlF8gHxiUwbmDspMhrmrcSW0")
			  .setOAuthAccessToken("382684624-adVHfgSxLXiJK8Xr6RHINraC8HBr626gSbNNr2IB")
			  .setOAuthAccessTokenSecret("eKdTKG1602YW1kDOqpzFScbg3pH56MJnr3TVHVEtk");
			
			cb.setUser("pfin_equity_idx");
			cb.setPassword("madmax1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("nuRiyfSn9kLiXXCNtIu7g")
			  .setOAuthConsumerSecret("00lfsRJEhpwtEiN3rIo7TSp24ykvNbm4DjLmVZUs9sI")
			  .setOAuthAccessToken("382688292-2VkgKjeZUp1qcPsURlMveGqmBenFvl6lg04f3ptb")
			  .setOAuthAccessTokenSecret("fSuZttQlp7RiJGxDHeudg9t74AZc1Ksj5WLNfyj7uU");*/
			
			/*cb.setUser("pfin_gdp");
			cb.setPassword("madmax1.");
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("PWXUU8IZMw9ECnyHulpaAQ")
			  .setOAuthConsumerSecret("iMWNlt0epLYVoCa7WNqHa9wsd2Vxd2cEKKBUg3NqE")
			  .setOAuthAccessToken("382694496-BGmNTLvseK7HXUC49271xJzArE3bHBJdeZr3fe8")
			  .setOAuthAccessTokenSecret("M1ECA4FJadLK4WY5ZVQDAB0T3nRHlRpdjx1L4opu0U");*/
			
			cb.setUser(strUser);
			cb.setPassword(strPass);
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey(strAuth1)
			  .setOAuthConsumerSecret(strAuth2)
			  .setOAuthAccessToken(strAuth3)
			  .setOAuthAccessTokenSecret(strAuth4);
			
			

			
					
			TwitterFactory tf = new TwitterFactory(cb.build());
			
			Twitter twitter = tf.getInstance();
		
			
			/*c.setUser(strUser);
			c.setPassword(strPass);
			c.setOtherThings(UNIQUE);
			c.setOAuthConsumer/Secret(CONSTANT);
			c.OAuthAccessToken/Secret(UNIQUE);*/
			
			//AccessToken accessToken = loadAccessToken(Integer.parseInt(args[0]));
			
			
			if (strTweet.length() > 140) {
			
				stdoutwriter.writeln("Tweet longer than 140 characters; tweet truncated.",Logs.WARN,"UF49.5");
				stdoutwriter.writeln(strTweet,Logs.WARN,"UF49.5");
				strErrorMsg = "Tweet longer than 140 characters; tweet truncated.";
			}
				
			
			if (UtilityFunctions.getDebugMode() == false)
				twitter.updateStatus(strTweet);
		
		}
		catch (TwitterException te)	{
		    //te.printStackTrace();
		    //System.out.println("Failed to get timeline: " + te.getMessage());
		    //System.exit(-1);
			stdoutwriter.writeln("Problem sending tweet",Logs.ERROR,"UF48.5");
			stdoutwriter.writeln(te);
			strErrorMsg = te.getMessage();
		}
		
		
		return(strErrorMsg);
		
		    
	}
	
	/*
	 * For troubleshooting purposes to dump out the contents of an array of strings. A date & time
	 * stamp is attached to the file name.
	 * We a primarily using this to dump the contents of returned_content when there is an issue
	 * parsing data.
	 */
	public static void dumpStrings(String[] inputArray, String strFileName) {
		Calendar cal = Calendar.getInstance();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		strFileName += "." + formatter.format(cal.getTime());
		try {
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(strFileName));
			for(int i=0;i<inputArray.length;i++) {
				outputStream.write(inputArray[i]);
				outputStream.newLine();
				outputStream.newLine();
			}
			outputStream.close();
		}
		catch (IOException ioe) {
			stdoutwriter.writeln("Problem writing to debug file " + strFileName,Logs.ERROR,"UF63.5");
			return;
		}
		
		
	}
	  	
	public static String shortenURL(String strInputURL)	{
		
		try	{
			Url url = as("pikefin", "R_f08326b12abd18288243b65ef2c71c40").call(shorten(strInputURL));
			return(url.getShortUrl());
		}
		catch (Exception ex) {
			stdoutwriter.writeln("Failed to shorten url: " + strInputURL,Logs.ERROR,"UF51.5");
			stdoutwriter.writeln(ex);
		}
		
		return("");

		
		
		
	}  
	
	public static String generateVelocity(ArrayList<HashMap<String, String>> hm, String strListName, String strTemplate) {
		
		VelocityContext vc = new VelocityContext();
		Template t = ve.getTemplate( strTemplate );
		vc.put(strListName, hm);
		/*for (Map.Entry<String, String> entry : hm.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
		    vc.put(key, value);
		}*/
		
		StringWriter writer = new StringWriter();
		t.merge(vc,writer);
		
		return(writer.toString());
		

	}
	
	public static String generateVelocity2() {
		
		VelocityContext vc = new VelocityContext();
		Template t = ve.getTemplate( "twitter.vm" );
		vc.put("name","World");
		/*for (Map.Entry<String, String> entry : hm.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
		    vc.put(key, value);
		}*/
		
		StringWriter writer = new StringWriter();
		t.merge(vc,writer);
		
		return(writer.toString());
		

	}
	


	
	
	
	
}
	
	class PikefinException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4426155592602941937L;
		
		public PikefinException(String input) {
			super(input,null);
		}
		


		/*void TestException()
		{
			return;
		}*/
		
		
	}
	
	class SkipLoadException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6553765592848567969L;
		
	}
	

	class MyPasswordAuthenticator extends Authenticator {
		String user;
		String pw;
		public MyPasswordAuthenticator (String username, String password)
		{
			super();
			this.user = username;
			this.pw = password;
		}
		public PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(user, pw);
			
		}
	}
	
	
		 
		 
		
		
		
	

	
	 

	
	
	
	







