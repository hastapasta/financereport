import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Link;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.Worksheet;
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
	
	
	
	public PopulateSpreadsheet()
	{
		service = new SpreadsheetService("Populate Spreadsheet");
		
		try
		{
			dbf = new DBFunctions("testdataload","3306","findata","root","madmax1.");
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
		
		uf = new UtilityFunctions();
		
		try
		{
			login("hastapasta99@gmail.com","madmax1.");
		}
		catch (AuthenticationException ae)
		{
			ae.printStackTrace();
		}
		
		this.factory = FeedURLFactory.getDefault();
		
		try
		{
			loadSheet("this sheet");
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch(ServiceException se)
		{
			se.printStackTrace();
		}
		
		/*CellEntry newEntry = new CellEntry(row, col, formulaOrValue);
		 service.insert(cellFeedUrl, newEntry);
		 out.println("Added!");*/
		 
		
		
	}
	
	public void login(String username, String password)
    throws AuthenticationException {

  // Authenticate
  service.setUserCredentials(username, password);
	}
	        
	
	public static void main(String[] args)
	{
		 int row = 0;
		 int col = 0;
		 String formulaOrValue = "blap";
		 
		 
		 
		 
		 PopulateSpreadsheet tmp = new PopulateSpreadsheet();
		 
		 
		 
		
		
		
		
		
	}
	
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
  	
  	
  	
  	
  	for (int z=0;z<5;z++)
  	{
  		
  		
  		
  		
  		/*
  		 * Something happens in BatchSample that requires me to get this spreadsheetfeed again.
  		 */
  		
  		
  		
  		getNewFeed("motion chart sample",z);
  	
  	
  		System.out.println("Sheet loaded.");
  		
  		
  		String query2 = "select count(distinct ticker) as count1 from fact_data where data_set like '%" + regions[z] + "%' AND ticker NOT IN ('EURUSD','EURGBP')";
  		
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
	  	" where data_set like '%" + regions[z] + "%' AND ticker NOT IN ('EURUSD','EURGBP') " + 
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
	  	  		
	  	  		
	  	  		
	  	  		
	  	  		
	  	  		
	  	  
	  	  		
	  	  	}
	  	  	
	  	  	BatchSample.batchPopulate(service, cellFeedUrl,cellAddrs);
	  	  
	  	  	
	  	  	
	  	}
	  	catch (SQLException sqle)
	  	{
	  		sqle.printStackTrace();
	  	}
  	
  	
  	
  	
  	
  	
  	
  	}
  	
  	

  	

  

  // Get the worksheet to load
 /* if (spreadsheet.getWorksheets().size() == 1) {
    cellFeedUrl = spreadsheet.getWorksheets().get(0).getCellFeedUrl();
  } else {
    List worksheets = spreadsheet.getWorksheets();
    int worksheetIndex = getIndexFromUser(reader, worksheets, "worksheet");
    WorksheetEntry worksheet = (WorksheetEntry) worksheets
        .get(worksheetIndex);
    cellFeedUrl = worksheet.getCellFeedUrl();
  }
  System.out.println("Sheet loaded.");*/
}
	


}
