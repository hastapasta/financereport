import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.essbase.api.dataquery.IEssGridView;
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
			//String rptscript1 = "{TabDelim}<Page (Regions) <Column (2010-12-29,2010-12-22) <Row (Measures) <LINK (<DESCENDANTS(\"Total Assets\") AND <DIMBOTTOM(Measures)) !";
			/*String rptscript1 = "{TabDelim}<Page (Regions) <Row (Years) (\"2010-12-29\",\"2010-12-22\") <Column (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(\"Total Assets\")) !";
			String rptscript2 = "{TabDelim}<Page (Regions) <Row (Years) (\"2010-12-29\",\"2010-12-22\") <Column (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(\"Total Liabilities\")) !";
			String rptscript3 = "{TabDelim}<Page (Regions) <Row (Years) (\"2010-12-29\",\"2010-12-22\") <Column (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(Capital)) !";*/
			
			String rptscript1 = "{TabDelim}<Page (Regions) <Row (Years) <LINK (<CHILDREN(\"Years\")) <Column (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(\"Total Assets\")) !";
			String rptscript2 = "{TabDelim}<Page (Regions) <Row (Years) <LINK (<CHILDREN(\"Years\")) <Column (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(\"Total Liabilities\")) !";
			String rptscript3 = "{TabDelim}<Page (Regions) <Row (Years) <LINK (<CHILDREN(\"Years\")) <Column (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(Capital)) !";
			
			EssbaseConnect.connect("Sample","Fed");
			ArrayList<String[]> tmpArrayList = EssbaseConnect.performCubeViewOperation2(rptscript1);
			//ArrayList<String[]> tmpArrayList = EssbaseConnect.performCubeViewOperation2("report");
			
			tmpArrayList = transformEssbaseList(tmpArrayList);
			batchLoadSheetFromList("fedbalsheet",1,tmpArrayList);
			
			tmpArrayList = EssbaseConnect.performCubeViewOperation2(rptscript2);
			//ArrayList<String[]> tmpArrayList = EssbaseConnect.performCubeViewOperation2("report");
			
			tmpArrayList = transformEssbaseList(tmpArrayList);
			batchLoadSheetFromList("fedbalsheet",2,tmpArrayList);
			
			tmpArrayList = EssbaseConnect.performCubeViewOperation2(rptscript3);
			//ArrayList<String[]> tmpArrayList = EssbaseConnect.performCubeViewOperation2("report");
			
			tmpArrayList = transformEssbaseList(tmpArrayList);
			batchLoadSheetFromList("fedbalsheet",3,tmpArrayList);

			
			//loadSheet("this sheet");
			//currencyDataToList();
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
		EssbaseConnect.disconnect();
		//EssbaseConnect.disconnect();
		
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
