package pikefin;



import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.*;



import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.BaseEntry;
//import com.google.gdata.data.Link;
//import com.google.gdata.data.batch.BatchOperationType;
//import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
//import com.google.gdata.data.spreadsheet.Worksheet;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;


public class PopulateSpreadsheet {
	
	private SpreadsheetService service;
	private FeedURLFactory factory;
	private URL cellFeedUrl;
	private URL listFeedUrl;
	private DBFunctions dbf;
	private UtilityFunctions uf;
	private SpreadsheetFeed feed;
 	private SpreadsheetEntry spreadsheet;
 	private List<BatchSample.CellAddress> cellAddrs;
 	public ArrayList<String[]> tmpArrayList;
 	
 	private CustomBufferedWriter cbw;
	
	public void getNewFeed(String docName,int worksheetIndex) throws IOException,ServiceException
	{
		feed = service.getFeed(this.factory.getSpreadsheetsFeedUrl(),
	  		      SpreadsheetFeed.class);
		
		System.out.print("Processing worksheet " + worksheetIndex + "\n");
		
		 List spreadsheets = feed.getEntries();
		  //int spreadsheetIndex = getIndexFromUser(reader, spreadsheets, 
		    //  "spreadsheet");
		  int spreadsheetIndex = 0;
		  
		  for (int i = 0; i < spreadsheets.size(); i++) {
		      BaseEntry entry = (BaseEntry) spreadsheets.get(i);
		      if (entry.getTitle().getPlainText().equals(docName))
		   	  {
		    	  spreadsheetIndex = i;
		    	  break;
		   	  }
		    		  
		    } 
	  		
  		spreadsheet = feed.getEntries().get(spreadsheetIndex);
  		
  		/*Make sure we have enough rows*/
  		WorksheetEntry wsheet = spreadsheet.getWorksheets().get(worksheetIndex);
  	  	
  	  	wsheet.setRowCount(1500);
  	  	wsheet.update();
  		

  	
  		cellFeedUrl = spreadsheet.getWorksheets().get(worksheetIndex).getCellFeedUrl();
  		listFeedUrl = spreadsheet.getWorksheets().get(worksheetIndex).getListFeedUrl();
  		CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
  		
  		cellAddrs = new ArrayList<BatchSample.CellAddress>();
	}
	
	public ArrayList<String[]> getArrayList()
	{
		return this.tmpArrayList;
	}
	
	public static void writeMsg(String str)
	{
		UtilityFunctions.stdoutwriter.writeln(str,Logs.STATUS1,"A2.73");
	}
	
	public static String createGoogleError(String strReqId,String strReason,String strMessage,String strDetailed)
	{
		try
		{
		
		JSONObject jsonTop = new JSONObject();
		jsonTop.put("version","0.6");
		jsonTop.put("reqId", strReqId);
		jsonTop.put("status","error");
		
		JSONArray jsonArray = new JSONArray();
		
		JSONObject jsonBottom = new JSONObject();
		
		jsonBottom.put("reason",strReason);
		jsonBottom.put("message", strMessage);
		jsonBottom.put("detailed_message", strDetailed);
		
		jsonArray.put(jsonBottom);
		
		jsonTop.put("errors",jsonArray);
		
		//google.visualization.Query.setResponse({version:'0.6',reqId:'0',status:'error',errors:[{reason:'access_denied',message:'Access denied',detailed_message:'Access Denied'}]});
		
		return("google.visualization.Query.setResponse(" + jsonTop.toString() + ");");
		}
		catch (JSONException je)
		{
			return(je.toString());
		}
		
		
	}

	public static String createGoogleJSON(ArrayList<String[]> arrayListCols, ArrayList<String[]> arrayListRows,String strReqId,boolean bEmptyZeros) 
	{
		
		/*
		 * OFP 4/1/2011 - Added bEmptyZeros to indicate if zero values should be replaced with the empty string.
		 * 
		 */
		try 
		{
		UtilityFunctions.stdoutwriter.writeln("This is a test from inside createGoogleJSON.",Logs.STATUS1,"A2.73");
		
		JSONObject jsonTop = new JSONObject(); 
		jsonTop.put("version","0.6");
		//jsonTop.put("reqId","0");
		jsonTop.put("reqId", strReqId);
		jsonTop.put("status","ok");

		JSONObject jsonTable = new JSONObject();

		JSONArray jsonArrayCols = new JSONArray();


		for (int i=0;i<arrayListCols.size();i++)
		{
			JSONObject tmp = new JSONObject();
			String[] foo1 = arrayListCols.get(i);
			tmp.put("id",foo1[0]);
			tmp.put("label",foo1[1]);
			tmp.put("type",foo1[2]);
			jsonArrayCols.put(tmp);
		}
		
		jsonTable.put("cols",jsonArrayCols);

		JSONArray jsonArrayRows = new JSONArray();

		/*
		*Begin Row 1 
		*/


		for (int i=0;i<arrayListRows.size();i++)
		{
			JSONArray tmpArray = new JSONArray();
			//JSONObject cJSON = new JSONObject();
			

			String[] foo2 = arrayListRows.get(i);
			for (int k=0;k<arrayListCols.size();k++)
			{
				JSONObject tmp = new JSONObject();
				if (arrayListCols.get(k)[2].equals("number"))
				{
					
					writeMsg("value: " + foo2[2*k]);
					if (!foo2[2*k].isEmpty())
					{
						double dTmp = Double.valueOf(foo2[2*k]);
						if ((dTmp == 0) && (bEmptyZeros == true))
						{
							tmp.put("v", "");
							if (k!=0)
								tmp.put("f","");
						}
						else
						{
							tmp.put("v", dTmp);
							if (k!=0)
								tmp.put("f",dTmp);
						}
					}
					else
					{
						tmp.put("v", "");
						if (k!=0)
							tmp.put("f","");
					}
					//tmp.put("v",Integer.parseInt(foo2[2*k]));
				}
				else if (arrayListCols.get(k)[2].equals("date"))
				{
					if (!foo2[2*k].isEmpty())
					{	
						
						tmp.put("v", new JSONDate(foo2[2*k]));
						if (k!=0)
							tmp.put("f",foo2[2*k]);
						
					}
					else
					{
						tmp.put("v", "");
						if (k!=0)
							tmp.put("f","");
					}
					
					
					
					
					
				}			
				else
				{
					tmp.put("v",foo2[2*k]);
					if (k!=0)
						tmp.put("f",foo2[(2*k)+1]);
				}
				
				tmpArray.put(tmp);
				
			}

			jsonArrayRows.put(new JSONObject().put("c",tmpArray));
			
			
		}
		
		jsonTable.put("rows",jsonArrayRows);




		jsonTop.put("table",jsonTable);
		
		return("google.visualization.Query.setResponse(" + jsonTop.toString() + ");");
		
		}
		catch (JSONException je)
		{
			return(je.toString());
		}

		
		
		
	}
	
	
	
	public PopulateSpreadsheet()
	{ 
		uf = new UtilityFunctions();
		
		UtilityFunctions.stdoutwriter.writeln("This is a test from inside PopulateSpreadsheet 2.",Logs.STATUS1,"A2.73");
		
		try
		{
			
			EssbaseConnect.connect("Sample","Fed");
			String rptscript1;
			String rptscript2;
			String rptscript3;
			//ArrayList<String[]> tmpArrayList;
			
			/*
			 * Populate fedbalsheet yearly
			 */
			
			/*String rptscript1 = "{TabDelim}<Page (Regions) <Row (Years) <LINK (<CHILDREN(\"Years\")) <Column (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(\"Total Assets\")) !";
			String rptscript2 = "{TabDelim}<Page (Regions) <Row (Years) <LINK (<CHILDREN(\"Years\")) <Column (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(\"Total Liabilities\")) !";
			String rptscript3 = "{TabDelim}<Page (Regions) <Row (Years) <LINK (<CHILDREN(\"Years\")) <Column (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(Capital)) !";
			
			ArrayList<String[]> tmpArrayList = EssbaseConnect.performCubeViewOperation2(rptscript1);
			tmpArrayList = transformEssbaseList(tmpArrayList);
			batchLoadSheetFromList("fedbalsheet - yearly",1,tmpArrayList);
			
			tmpArrayList = EssbaseConnect.performCubeViewOperation2(rptscript2);
			tmpArrayList = transformEssbaseList(tmpArrayList);
			batchLoadSheetFromList("fedbalsheet - yearly",2,tmpArrayList);
			
			tmpArrayList = EssbaseConnect.performCubeViewOperation2(rptscript3);
			tmpArrayList = transformEssbaseList(tmpArrayList);
			batchLoadSheetFromList("fedbalsheet - yearly",3,tmpArrayList);*/
			
			
			
			/*
			 * Populate fedbalsheet weekly
			 */
			
			rptscript1 = "{TabDelim}<Page (Regions) <Column (Years) &curperiod,&prevperiod,\"diff\" <Row (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(\"Total Assets\")) !";
			rptscript2 = "{TabDelim}<Page (Regions) <Column (Years) &curperiod,&prevperiod,\"diff\" <Row (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(\"Total Liabilities\")) !";
			rptscript3 = "{TabDelim}<Page (Regions) <Column (Years) &curperiod,&prevperiod,\"diff\" <Row (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(\"Capital\")) !";
			
			tmpArrayList = EssbaseConnect.performCubeViewOperation2(rptscript1);
			tmpArrayList = transformEssbaseList(tmpArrayList);
			/*batchLoadSheetFromList("fedbalsheet - weekly",1,tmpArrayList);
			
			tmpArrayList = EssbaseConnect.performCubeViewOperation2(rptscript2);
			tmpArrayList = transformEssbaseList(tmpArrayList);
			batchLoadSheetFromList("fedbalsheet - weekly",2,tmpArrayList);
			
			tmpArrayList = EssbaseConnect.performCubeViewOperation2(rptscript3);
			tmpArrayList = transformEssbaseList(tmpArrayList);
			batchLoadSheetFromList("fedbalsheet - weekly",3,tmpArrayList);*/
			
			//EssbaseConnect.disconnect();
			
			
			
			

			
			
		}
		/*catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch(ServiceException se)
		{
			se.printStackTrace();
		}*/
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		/*CellEntry newEntry = new CellEntry(row, col, formulaOrValue);
		 service.insert(cellFeedUrl, newEntry);
		 out.println("Added!");*/
		
		//EssbaseConnect.disconnect();
		
	}
	
	public void login(String username, String password)
    throws AuthenticationException {

  // Authenticate
  service.setUserCredentials(username, password);
	}
	        
	
	/*public static void main(String[] args)
	{
		 int row = 0;
		 int col = 0;
		 String formulaOrValue = "blap";
		 
		 
		 
		 
		 PopulateSpreadsheet tmp = new PopulateSpreadsheet();
		 
		 
		 
		
		
		
		
		
	}*/
	
	
	
	
	public void loadSheet(String sheetName) throws IOException,
    ServiceException {
  // Get the spreadsheet to load
  SpreadsheetFeed feed = service.getFeed(this.factory.getSpreadsheetsFeedUrl(),
      SpreadsheetFeed.class);
		
  List spreadsheets = feed.getEntries();
  //int spreadsheetIndex = getIndexFromUser(reader, spreadsheets, 
    //  "spreadsheet");
  int spreadsheetIndex = 0;
  
  for (int i = 0; i < spreadsheets.size(); i++) {
      BaseEntry entry = (BaseEntry) spreadsheets.get(i);
      if (entry.getTitle().getPlainText().equals("motion chart sample"))
   	  {
    	  spreadsheetIndex = i;
    	  break;
   	  }
    		  
    }
  
    
 
  	
  	 
  	//cellFeedUrl = spreadsheet.getWorksheets().get(0).getCellFeedUrl();
  	
 
  	
  	//spreadsheet.getWorksheets().get(0).setRowCount(500);
  	
  	//spreadsheet.getWorksheets().get(0).update();
  	
  	//spreadsheet.update();
  	
  	 	  	
  	//int x = spreadsheet.getWorksheets().get(0).getRowCount();
  	
  	/*String query2 = "select count(distinct ticker) as count1 from fact_data where data_set like '%xrateorg%'";
  	
  	int uniquetickers = 0;
  	try
  	{
  		ResultSet rs2 = dbf.db_run_query(query2);
  		rs2.next();
  		uniquetickers = rs2.getInt("count1");
  	}
  	catch (SQLException sqle)
  	{
  		sqle.printStackTrace();
  	}*/
  	
  	String[] regions = {"europe","americas","asia","mideast","africa"};
  	
  	String[] colheaders = {"ticker","time","value","pct_change","region"};
  	
  	
  	
  	
  	for (int z=1;z<6;z++)
  	{
  		
  		
  		
  		
  		/*
  		 * Something happens in BatchSample that requires me to get this spreadsheetfeed again.
  		 */
  		
  		
  		
  		getNewFeed("motion chart sample",z);
  	
  	
  		System.out.println("Sheet loaded.");
  		
  		
  		String query2 = "select count(distinct ticker) as count1 from fact_data where data_set like '%" + regions[z-1] + "%' AND ticker NOT IN ('EURUSD','EURGBP')";
  		
  		int uniquetickers = 0;
  	  	try
  	  	{
  	  		ResultSet rs2 = dbf.db_run_query(query2);
  	  		rs2.next();
  	  		uniquetickers = rs2.getInt("count1");
  	  	}
  	  	catch (SQLException sqle)
  	  	{
  	  		sqle.printStackTrace();
  	  	}
  	
	  	String query = "select avg(value) as value1,ticker,date_format(date_collected,'%m/%d/%y') as date1,data_set from fact_data" + 
	  	" where data_set like '%" + regions[z-1] + "%' AND ticker NOT IN ('EURUSD','EURGBP') " + 
	  	"group by date1,ticker,data_set order by date1";
	  	try
	  	{
	  		CellEntry newEntry;
	  		ListEntry newListEntry; 
	
	  		ResultSet rs = dbf.db_run_query(query);
	  		int row = 2;
	  		int rowcount = spreadsheet.getWorksheets().get(z).getRowCount();
	  		int groupcount =0;
	  	  	while (rs.next())
	  	  	{
	  	  		
	  	  		//System.out.println(row);
	  	  		
	  	  		/*
	  	  		 * 
	  	  		 * Code to enter data by list feed.
	  	  		 * Doesn't work with forumals or spaces in column headers.
	  	  		 */
	  	  		
	  	  		/*newListEntry = new ListEntry();
	  	  		
	  	  		String tmp1 = rs.getString("value1");
	  	  		
	  	  		newListEntry.getCustomElements().setValueLocal(colheaders[0], rs.getString("ticker"));
	  	  		newListEntry.getCustomElements().setValueLocal(colheaders[1], rs.getString("date1"));
	  	  		newListEntry.getCustomElements().setValueLocal(colheaders[2], tmp1);
	  	  		
	  	  		
	  	  		
	  	  		//Formulas are not support with list feeds.
	  	  		String tmp = "(C" + (row) + "-C" + (groupcount+2) + ")/C" + (groupcount+2);
	  	  		newListEntry.getCustomElements().setValueLocal(colheaders[3], tmp);
	  	  		
	  	  		String strRegion = "";
		  	  	if (rs.getString("data_set").contains("europe"))
			  			strRegion = "europe";
		  		else if (rs.getString("data_set").contains("americas"))
		  			strRegion = "americas";
		  		else if (rs.getString("data_set").contains("africa"))
		  			strRegion = "africa";
		  		else if (rs.getString("data_set").contains("asia"))
		  			strRegion = "asia & pacific";
		  		else if (rs.getString("data_set").contains("mideast"))
		  			strRegion = "middle east";
		  	  	
		  	  	strRegion = "blap";
		  	  	newListEntry.getCustomElements().setValueLocal(colheaders[4], strRegion);
		  	  	
		  	  	ListEntry insertedRow = service.insert(listFeedUrl, newListEntry);*/
	  	  		
	  	  		/*
	  	  		 * End code section for enterind data by list feed.
	  	  		 */
	  	  		
	  	  		
	  	  		/*
	  	  		 * 
	  	  		 * Code to enter data cell by cell. Will leave this in for now as a reference.
	  	  		 * 
	  	  		 * */
	  	  		
	  	  		/*newEntry = new CellEntry(row,1,rs.getString("ticker"));
	  	  		service.insert(cellFeedUrl, newEntry);
	  	  		newEntry = new CellEntry(row,2,rs.getString("date1"));
	  	  		service.insert(cellFeedUrl, newEntry);
	  	  		newEntry = new CellEntry(row,3,rs.getString("value1"));
	  	  		service.insert(cellFeedUrl, newEntry);
	  	  		
	  	  		newEntry = new CellEntry(row,4,("=(C" + (row) + "-C" + (groupcount+2) + ")/C" + (groupcount+2))); 	  		
	  	  		service.insert(cellFeedUrl, newEntry);
	  	  		
	  	  		if (rs.getString("data_set").contains("europe"))
	  	  			newEntry = new CellEntry(row,5,"europe");
	  	  		else if (rs.getString("data_set").contains("americas"))
	  	  			newEntry = new CellEntry(row,5,"americas");
	  	  		else if (rs.getString("data_set").contains("africa"))
		  			newEntry = new CellEntry(row,5,"africa");
	  	  		else if (rs.getString("data_set").contains("asia"))
	  	  			newEntry = new CellEntry(row,5,"asia & pacific");
	  	  		else if (rs.getString("data_set").contains("mideast"))
	  	  			newEntry = new CellEntry(row,5,"middle east");
	  	  		
	  	  		service.insert(cellFeedUrl, newEntry);*/
	  	  		
	  	  		/*
	  	  		 * End section enter data cell by cell.
	  	  		 */
	  	  		
	  	  		String tmp = "=(C" + (row) + "-C" + (groupcount+2) + ")/C" + (groupcount+2);
	  	  		
	  	  		cellAddrs.add(new BatchSample.CellAddress(row,1,rs.getString("ticker")));
	  	  		cellAddrs.add(new BatchSample.CellAddress(row,2,rs.getString("date1")));
	  	  		cellAddrs.add(new BatchSample.CellAddress(row,3,rs.getString("value1")));
	  	  		cellAddrs.add(new BatchSample.CellAddress(row,4,tmp));
	  	  		
		  	  	String strRegion = "";
		  	  	if (rs.getString("data_set").contains("europe"))
			  			strRegion = "europe";
		  		else if (rs.getString("data_set").contains("americas"))
		  			strRegion = "americas";
		  		else if (rs.getString("data_set").contains("africa"))
		  			strRegion = "africa";
		  		else if (rs.getString("data_set").contains("asia"))
		  			strRegion = "asia & pacific";
		  		else if (rs.getString("data_set").contains("mideast"))
		  			strRegion = "middle east";
		  	  	
		  	  	cellAddrs.add(new BatchSample.CellAddress(row,5,strRegion));
		  	  	
		  	  	
	  	  		
	  	  		
	  	  		
	  	  		
	  	  		
		  	  	
		  	  	
		  	  	
		  /*	  	List<CellAddress> cellAddrs = new ArrayList<CellAddress>();
		  	  	cellAddrs.add(new CellAddress(2, 1));
		  	  	cellAddrs.add(new CellAddress(2, 2));
		  	  	cellAddrs.add(new CellAddress(2, 3));
		  	  	cellAddrs.add(new CellAddress(2, 4));
		  	  	cellAddrs.add(new CellAddress(2, 5));
		  	  	
		  	  	Map<String,CellEntry> cellEntries = getCellEntryMap(service, cellFeedUrl, cellAddrs);
		  	  	
		
		  	  	
		  	  	CellFeed batchRequest = new CellFeed();
		  	    for (CellAddress cellAddr : cellAddrs) {
		  	      URL entryUrl = new URL(cellFeedUrl.toString() + "/" + cellAddr.idString);
		  	      CellEntry batchEntry = new CellEntry(cellEntries.get(cellAddr.idString));
		  	      
		  	      //batchEntry.changeInputValueLocal("999");
		  	      //batchEntry.update();
		  	      
		  	      batchEntry.changeInputValueLocal(cellAddr.idString);
		  	      BatchUtils.setBatchId(batchEntry, cellAddr.idString);
		  	      BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);
		  	      batchRequest.getEntries().add(batchEntry);
		  	    }
		  	    
		  	    Link batchLink = cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
		  	    CellFeed batchResponse = service.batch(new URL(batchLink.getHref()), batchRequest);
	  	  		
	  	  		*/
		  	  	
		  	  	if (row%250 == 0)
	  	  		{
	  	  			BatchSample.batchPopulate(service,cellFeedUrl,cellAddrs);
	  	  			getNewFeed("motion chart sample",z);
	  	  			
	  	  		}
	  	  		
	  	  		
	  	  		row++;
	  	  		
	  	  		/* This doesn't work for some reason */
	  	  		/*if (rowcount == row)
	  	  		{
	  	  			rowcount +=100;
	  	  			spreadsheet.getWorksheets().get(z).setRowCount(rowcount);
	  	  			spreadsheet.getWorksheets().get(z).update();
	  	  			
	  	  			
	  	  		}*/
	  	  		
	  	  		
	  	  		
	  	  	
	  	  		
	  	  
	  	  		
	  	  		
	  	  		
	  	  		
	  	  		
	  	  		groupcount++;
	  	  		if (groupcount==uniquetickers)
	  	  			groupcount=0;
	  	  		
	  	  		
	  	  		
	  	  		
	  	  		break;
	  	  		
	  	  
	  	  		
	  	  	}
	  	  	
	  	  	BatchSample.batchPopulate(service, cellFeedUrl,cellAddrs);
	  	  
	  	  	
	  	  	
	  	}
	  	catch (SQLException sqle)
	  	{
	  		sqle.printStackTrace();
	  	}
  	
  	
  	
  	
  	
  	
  	
  	}
  	

  	
  	

  	


}

	
	
	public void currencyDataToList()
	{
		
		String[] regions = {"europe","americas","asia","mideast","africa"};
		
		
		
		
		for (int z=1;z<6;z++)
	  	{
			ArrayList<String[]> rows = new ArrayList<String[]>();
			String query2 = "select count(distinct ticker) as count1 from fact_data where data_set like '%" + regions[z-1] + "%' AND ticker NOT IN ('EURUSD','EURGBP')";
			int uniquetickers = 0;
	  	  	try
	  	  	{
	  	  		ResultSet rs2 = dbf.db_run_query(query2);
	  	  		rs2.next();
	  	  		uniquetickers = rs2.getInt("count1");
	  	  	}
	  	  	catch (SQLException sqle)
	  	  	{
	  	  		sqle.printStackTrace();
	  	  	}
	  		String query = "select avg(value) as value1,ticker,date_format(date_collected,'%m/%d/%y') as date1,data_set from fact_data" + 
		  	" where data_set like '%" + regions[z-1] + "%' AND ticker NOT IN ('EURUSD','EURGBP') " + 
		  	"group by date1,ticker,data_set order by date1";
	  		String[] tmpRow;
		  	try
		  	{
		  		//CellEntry newEntry;
		  		//ListEntry newListEntry; 
		
		  		ResultSet rs = dbf.db_run_query(query);
		  		int row = 2;
		  		//int rowcount = spreadsheet.getWorksheets().get(z).getRowCount();
		  		int groupcount =0;
		  	  	while (rs.next())
		  	  	{
		  	  		tmpRow = new String[5];
		  	  		tmpRow[0]=rs.getString("ticker");
		  	  		tmpRow[1]=rs.getString("date1");
		  	  		tmpRow[2]=rs.getString("value1");
		  	  	
		  	  		
		  	  		String strFormula = "=(C" + (row) + "-C" + (groupcount+2) + ")/C" + (groupcount+2);
		  	  		tmpRow[3]=strFormula;
		  	  		
			  	  	String strRegion = "";
			  	  	if (rs.getString("data_set").contains("europe"))
				  			strRegion = "europe";
			  		else if (rs.getString("data_set").contains("americas"))
			  			strRegion = "americas";
			  		else if (rs.getString("data_set").contains("africa"))
			  			strRegion = "africa";
			  		else if (rs.getString("data_set").contains("asia"))
			  			strRegion = "asia & pacific";
			  		else if (rs.getString("data_set").contains("mideast"))
			  			strRegion = "middle east";
			  	  	
			  	  	tmpRow[4]=strRegion;
			  	  	
			  	  	rows.add(tmpRow);
			  	  	
			  		groupcount++;
		  	  		if (groupcount==uniquetickers)
		  	  			groupcount=0;
		  	  	
		  	  		//break;
		  	  	}
			
			
		  	}
			catch (SQLException sqle)
		  	{
		  		sqle.printStackTrace();
		  	}
		
			try
			{
				batchLoadSheetFromList("motion chart sample",z,rows);
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
				
			}
			catch (ServiceException se)
			{
				se.printStackTrace();
			}
		
	  	}
		
		
	}
	
	
	public ArrayList<String[]> transformEssbaseList(ArrayList<String[]> listData)
	{
		ArrayList<String[]> newData = new ArrayList<String[]>();
		String[] tmpArray;;
		
		listData.remove(0);
		/*for (int i=1;i<listData.size();i++)
		{
			tmpArray = new String[listData.get(i).length];
			for (int j=0;j<tmpArray.length;j++)
				tmpArray[j] = 
			if (i==1)
			{
				tmpArray[0]="";
				tmpArray[1]=listData.get(i)[1];
				tmpArray[2]=listData.get(i)[2];
			}
			else
			{
		
			
				tmpArray[0]=listData.get(i)[0];
				tmpArray[1]=listData.get(i)[1];
				tmpArray[2]=listData.get(i)[2];
				
			}
			
			newData.add(tmpArray);
			
			
			
			
		}*/
		
		return(listData);
		
		
	}
	
  	public void batchLoadSheetFromList(String strDocName, int nWorkSheetIndex, ArrayList<String[]> rows) throws IOException,  ServiceException 
  	{
  		

  		  
  		getNewFeed(strDocName,nWorkSheetIndex);
  	
  		for (int row=0;row<rows.size();row++)
  		{
  			for (int col=0;col<rows.get(row).length;col++)
  			{
		  		cellAddrs.add(new BatchSample.CellAddress(row+1,col+1,rows.get(row)[col]));		  		
  			}
  	  
  	  	
	  	  	if ((row!=0) && (row%250 == 0))
	  		{
	  	  		System.out.println("populating up to row " + (row+2));
	  			BatchSample.batchPopulate(service,cellFeedUrl,cellAddrs);
	  			
	  			getNewFeed(strDocName,nWorkSheetIndex);
	  	  			
	  		}
	  	  		
  	  		
  
  	  		
  		}

  		System.out.println("populating up to row " + (rows.size()+2));
  	  	BatchSample.batchPopulate(service, cellFeedUrl,cellAddrs);
  		
  		
  		
  	}
  	
  	public static ArrayList<String[]> removeLastColumn(ArrayList<String[]> inputArrayList)
  	{
  		//Removes the last index from all of the String arrays
  		
  		ArrayList<String[]> outputArrayList = new ArrayList<String[]>();
  		
  		for (int i=0;i<inputArrayList.size();i++)
  		{
  			String[] inRow = inputArrayList.get(i);
  			
  			String[] outRow = new String[inRow.length - 1];
  			
  			for (int j=0;j<outRow.length;j++)
  			{
  				outRow[j]=inRow[j];
  			}
  			
  			outputArrayList.add(outRow);
  			
  		}
  		
  		return(outputArrayList);
  		
  	
  	}
  	
  	/*
  	 * 
  	 * It should be simple to also create a getLastGroupBy() function.
  	 *
  	 */
  	
	public static ArrayList<String[]> getLastGroupBy(ArrayList<String[]> inputArrayList,int[] x, int y)
  	{
		
		/*
  		 * Return the row with a particular maximum value in a group of values. This is to avoid
  		 * having to do nested select queries.
  		 * 
  		 * Assumptions: 
  		 * 		- index x1 is the first group column
  		 *  	- index x2 is the second group column
  		 *      - index y is the max column (currently this can only be an int)
  		 *      - data has been ordered by x1 and then x2
  		 * 
  		 * 
  		 */
  		ArrayList<String[]> outputArrayList = new ArrayList<String[]>();
  		
  		String[] previousRow = inputArrayList.get(0);
  		String[] row = null;
  		
  		for (int i=1;i<inputArrayList.size();i++)
  		{
  			row = inputArrayList.get(i);
  			boolean bResult=false;
  			for (int j=0;j<x.length;j++)
  			{
  				if (!row[x[j]].equals(previousRow[x[j]]))
  					bResult=true;
  				
  			}
  			//if (!((row[x1].equals(previousRow[x1])) && (row[x2].equals(previousRow[x2]))))
  			if (bResult==true)
  				// we're on to the next group
  			{
  				outputArrayList.add(previousRow);
  				
  				
  				 				
  			}
  			
  			previousRow = row;
  			
  			//if (Integer.parseInt(row[y]) > Integer.parseInt(maxRow[y]))
  				//maxRow = row;		
  		}
  		
  		outputArrayList.add(row);
  		
  		
  		return(outputArrayList);
	
  	}
  	
  	//public static ArrayList<String[]> getMaxGroupBy(ArrayList<String[]> inputArrayList,int x1, int x2,int y)
	public static ArrayList<String[]> getMaxGroupBy(ArrayList<String[]> inputArrayList,int[] x,int y)
  	{
  		/*
  		 * Return the row with a particular maximum value in a group of values. This is to avoid
  		 * having to do nested select queries.
  		 * 
  		 * Assumptions: 
  		 * 		- index x1 is the first group column
  		 *  	- index x2 is the second group column
  		 *      - index y is the max column (currently this can only be an int)
  		 *      - data has been ordered by x1 and then x2
  		 * 
  		 * 
  		 */
  		ArrayList<String[]> outputArrayList = new ArrayList<String[]>();
  		
  		String[] maxRow = inputArrayList.get(0);
  		String[] row;
  		
  		for (int i=1;i<inputArrayList.size();i++)
  		{
  			row = inputArrayList.get(i);
  			boolean bResult=false;
  			for (int j=0;j<x.length;j++)
  			{
  				if (!row[x[j]].equals(maxRow[x[j]]))
  					bResult=true;
  				
  			}
  			//if (!((row[x1].equals(maxRow[x1])) && (row[x2].equals(maxRow[x2]))))
  			if (bResult==true)
  				// we're on to the next group
  			{
  				outputArrayList.add(maxRow);
  				
  				maxRow = row;
  				
  				
  			}
  			
  			if (Integer.parseInt(row[y]) > Integer.parseInt(maxRow[y]))
  				maxRow = row;
  			
  		}
  		
  		outputArrayList.add(maxRow);
  		
  		
  		return(outputArrayList);
  		
  		
  		
  	}
  	
  	
  	public static ArrayList<String[]> pivotRowsToColumnsArrayList(ArrayList<String[]> inputArrayList)
  	{
  		/*
  		 * Assumptions: 
  		 * -data is ordered by col0 and then col1
  		 * -combination of col0 and col1 is unique
  		 * -we are pivoting on col1 (may make this variable at some point)
  		 * -array size of the input ArrayList<String[]> is 3 (may expand this but the combination of the first n-1 columns has to be unique)
  		 * 
  		 * 
  		 * Behavior:
  		 * -we will handle missing data values
  		 * -we're returning the pivoted column names as row 0
  		 * 
  		 * 
  		 */
  		
  		ArrayList<String[]> outputArrayList = new ArrayList<String[]>();
  		
  		String strTmp = inputArrayList.get(0)[0];
  		//int nGlobalMax=1;
  		//int nCurrentMax=1;
  		//int nGlobalMaxRowNumber=0;
  		
  		ArrayList<String> uniqueValueList = new ArrayList<String>();
  		
  		
  		/*
  		 * 
  		 * Create a list of unique values
  		 */
  		
  		
  		for (int i=0;i<inputArrayList.size();i++)
  		{
  			
  			boolean bInList = false;
  			for (int j=0;j<uniqueValueList.size();j++)
  			{
  				if (inputArrayList.get(i)[1].equals(uniqueValueList.get(j)))
  					bInList = true;
  			}
  			
  			if (bInList==false)
  				uniqueValueList.add(inputArrayList.get(i)[1]);
  			
  			
  		}
  		
  		/*
  		 * 
  		 * Determine the length of the longest sequence
  		 */
  		
  		
  		/*for (int i=1;i<inputArrayList.size();i++)
  		{
  			if (!inputArrayList.get(i)[0].equals(strTmp))
  			{
  				if (nCurrentMax>nGlobalMax)
  				{
  					nGlobalMax=nCurrentMax;
  					nGlobalMaxRowNumber=i-nCurrentMax;
  					
  				}
  				
  				nCurrentMax=1;
				strTmp = inputArrayList.get(i)[0];
  			
  		
  				
  				
  			}
  			else
  				nCurrentMax++;
  			
  			
  		}
  		
  		
  		if (nCurrentMax>nGlobalMax)
  			nGlobalMax=nCurrentMax;*/
  		
  		/*
  		 * Create list of unique values
  		 */
  		/*String[] arrayUniqueValues = new String[nGlobalMax];
  		
  		for (int j=0;j<nGlobalMax;j++)
  		{
  			arrayUniqueValues[j] = inputArrayList.get(j+nGlobalMaxRowNumber)[1];
  		}*/
  		
  		/*
  		 * Create output ArrayList
  		 */
  		boolean done = false;
  		int nInputRow=0;
  		String[] inputArray = inputArrayList.get(0);
  		String[] outputArray = new String[uniqueValueList.size()+1];
  		for (int z=0;z<outputArray.length;z++)
  			outputArray[z]="";
  		String strFirst = inputArray[0];
  		
  		
  		while(!done)
  		{
  			outputArray[0] = inputArray[0]; 
  			
  			//Lookup value in arrayUniqueValues
  			
  			//for (int y=0;y<arrayUniqueValues.length;y++)
  			for (int y=0;y<uniqueValueList.size();y++)
  			{
  				if (uniqueValueList.get(y).equals(inputArray[1]))
  				//if (arrayUniqueValues[y].equals(inputArray[1]))
  				{
  						outputArray[y+1] = inputArray[2];
  						break;
  				}
  			}
  			
  			nInputRow++;
  			if (nInputRow==inputArrayList.size())
  			{
  				outputArrayList.add(outputArray);
  				break;
  			}
  			else
  			{
  				inputArray = inputArrayList.get(nInputRow);
  				if (!inputArray[0].equals(strFirst))
  				{
  					outputArrayList.add(outputArray);
  					outputArray = new String[uniqueValueList.size()+1];
  					for (int z=0;z<outputArray.length;z++)
  			  			outputArray[z]="";
  					strFirst = inputArray[0];
  				}
  					
  					
  					
  					
  					
  			}
  		}
  		
  		/*
  		 * Create the column header array
  		 */
  		
  		String[] output = new String[uniqueValueList.size()];
  		
  		for (int i=0;i<uniqueValueList.size();i++)
  		{
  			output[i]= uniqueValueList.get(i);
  		}
  			
  		outputArrayList.add(0,output);
  		return(outputArrayList);
  	}
  	
  	public void individualLoadSheetFromList(String strDocName, int nSheetIndex, ArrayList<String[]> rows) throws IOException,  ServiceException 
  	{
  		getNewFeed(strDocName,nSheetIndex);
  	
  		for (int row=1;row<=rows.size();row++)
  		{
  			for (int col=0;col<rows.get(row).length;col++)
  			{
		  		cellAddrs.add(new BatchSample.CellAddress(row,col,rows.get(row)[col]));		  		
  			}
  	  
  	  	
	  	  	if (row%250 == 0)
	  		{
	  			BatchSample.batchPopulate(service,cellFeedUrl,cellAddrs);
	  			getNewFeed(strDocName,nSheetIndex);
	  	  			
	  		}
	  	  		
  	  		
  
  	  		
  		}
  	  		
  	  	
  	  		
  	  	
  	  		
 	
  		
  	  	BatchSample.batchPopulate(service, cellFeedUrl,cellAddrs);
  		
  		
  		
  		
  		
  		
  	}
  	
  	public void listFeedLoadSheetFromList(int nSheetIndex, ArrayList<String[]> rows) throws IOException,  ServiceException 
  	{
  	
  		for (int row=1;row<=rows.size();row++)
  		{
  			for (int col=0;col<rows.get(row).length;col++)
  			{
		  		cellAddrs.add(new BatchSample.CellAddress(row,col,rows.get(row)[col]));		  		
  			}
  	  
  	  	
	  	  	if (row%250 == 0)
	  		{
	  			BatchSample.batchPopulate(service,cellFeedUrl,cellAddrs);
	  			getNewFeed("motion chart sample",nSheetIndex);
	  	  			
	  		}
	  	  		
  	  		
  
  	  		
  		}
  	  		
  	  	
  	  		
  	  	
  	  		
 	
  	  	BatchSample.batchPopulate(service, cellFeedUrl,cellAddrs);
  		
  		
  		
  		
  		
  		
  	}
  	
	


	


}

class JSONDate implements JSONString
	{
	/*
	 * Examples of date structure that google gadgets accept:
	 * 
	 * {v:new Date(2008,1,28,0,31,26),f:'2/28/08 12:31 AM'}
	 * 
	 * new Date(2008, 1 ,2)
	 * 
	 */
	Calendar value;
	
	JSONDate(String date)
	{
		String[] date2;
		if (date.contains("/"))
			date2 = date.split("/");
		else
			date2 = date.split("-");
		this.value = Calendar.getInstance();
		this.value.set(Calendar.YEAR, Integer.parseInt(date2[2]));
		this.value.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date2[1]));
		this.value.set(Calendar.MONTH, Integer.parseInt(date2[0]) - 1);
		
	}
	
	public String toJSONString()
	{
		
		return("new Date(" + value.get(Calendar.YEAR) + "," + value.get(Calendar.MONTH) + "," + value.get(Calendar.DAY_OF_MONTH) + ")");
		
	}
	

	
		
		
	}

