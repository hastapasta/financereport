package pikefin;



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.*;
import java.text.ParseException;



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
	//this is a comment
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
		  
		  int i = 0;
		  for(Iterator<List> listIterator = spreadsheets.iterator(); listIterator.hasNext();i++){

		  //for (int i = 0; i < spreadsheets.size(); i++) {
		      //BaseEntry entry = (BaseEntry) spreadsheets.get(i);
			  BaseEntry entry = (BaseEntry) listIterator.next();
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
	
	public static String getEntityGroupList(String strEntityGroup) throws SQLException
	{
		
		DBFunctions dbf = null;
		
		String strList = strEntityGroup;
		
	
		String strMessage = "";
		
		
		String query = "select * from entity_groups where parent_id=" + strEntityGroup;
		boolean bException = false;
		
		try
		{
			dbf = new DBFunctions();
			dbf.db_run_query(query);
			while(dbf.rs.next())
			{
				/*String strReturn = getEntityGroupList(dbf.rs.getInt("id") + "");	
				if (!strReturn.isEmpty())
					strList += "," + strReturn;		*/
				strList += "," + getEntityGroupList(dbf.rs.getInt("id") + "");
			}
			
			
		}
		catch (SQLException sqle)
		{
			//out.println(sqle.toString());
			bException = true;
			strMessage = sqle.getMessage();
		}
		finally
		{
			dbf.closeConnection();
			
		}
		if (bException == true)
		{
			throw new SQLException(strMessage);
		}
		else
			return(strList);
		
		
		
		
		
		//getEntityList();
		
		//return(strList);
		
		
		
	}
	
	public static ArrayList<String[]> joinListsLeftOuter(ArrayList<String[]> arrayList1,ArrayList<String[]> arrayList2,int nCol)
	{
		/*
		 * Assumptions:
		 * 		-Same column index is used in both lists for join (nCol)
		 * 		-left outer join
		 * 		-the arrays in each list are the same size
		 * 
		 */
		
		ArrayList<String[]> outputList = new ArrayList<String[]>();
			
			/*if (arrayList1.size() > arrayList2.size())
			{
				shortList = arrayList2;
				longList = arrayList1;
			}
			else
			{
				shortList = arrayList1;
				longList = arrayList2;
			}*/
			
			int nIndexList1 = 0;
			int nIndexList2 = 0;
			
			while (nIndexList1<arrayList1.size())
			{
				
				String[] row1 = arrayList1.get(nIndexList1);
				String[] row2 = null;
				
				boolean bDone = false;
				
				/*
				 * If there are no elements in arrayList2, we'll just skip searching for the element
				 * and go to filling with nulls.
				 */
				if (arrayList2.size()==0)
					bDone = true;
				
				
				
				boolean bMatch = false;
				
				int initialIndex;
				
				if (!(nIndexList1>=arrayList2.size()))
					initialIndex = nIndexList1;
				else
					initialIndex = 0;
				
				//int initialIndex = (nIndexList1>nIndexList2?nIndexList1:nIndexList2);
				
				int index = initialIndex;
				
				while (!bDone)
				{
					row2 = arrayList2.get(index);
					
					if (row2[nCol].equals(row1[nCol]))
					{
						bMatch = true;
						break;
					}
					
					index++;
					
					//We're at the end, wrap around.
					if (index == arrayList2.size())
						index = 0;
					
					//We've come full circle, no match.
					if (index == initialIndex)
						break;
					
					
					
					
					
				}
				
				if (bMatch == true)
				{
					
					String[] joinArray = new String[(row1.length*2)-1];
					
					for (int i=0;i<row1.length;i++)
					{
						joinArray[i] = row1[i];
					}
					
					
					for (int i=0,j=row1.length;i<row2.length;j++,i++)
					{
						if (i==nCol)
							j--;
						else
							joinArray[j] = row2[i];
							
					}
					
					outputList.add(joinArray);
					
					
				}
				else
				{
					String[] joinArray = new String[(row1.length*2)-1];
					
					for (int i=0;i<row1.length;i++)
					{
						joinArray[i] = row1[i];
					}
					
					
					for (int i = row1.length;i<joinArray.length;i++)
					{
						joinArray[i] = null;
							
					}
					
					outputList.add(joinArray);
					
					
					
					
					
					
				}
					
				
				nIndexList1++;
				
				
			}
			
			
			
			
			return(outputList);
		
		
		
	}
	
	public static ArrayList<String[]> unionLists(ArrayList<String[]> arrayList1, ArrayList<String[]> arrayList2) throws CustomInvalidInputException {
		/*
		 * Peforms a union on arrayLists 1 and 2, putting 1 before 2
		 * Assumptions:
		 * 1) The String arrays in both ArrayLists must be the same size.
		 */
		String[] tmp1 = arrayList1.get(0);
		String[] tmp2 = arrayList2.get(0);
		 
		if (tmp1.length != tmp2.length) {
			throw new CustomInvalidInputException("Invalid input. String arrays in both array lists must be the same size.");
		}
		
		//for (int i=0; i<arrayList1.size(); i++) {
		int i = 0;
		for (String[] tmp3 : arrayList1) {
			//tmp3 = arrayList1.get(i);
			arrayList2.add(i,tmp3);
			i++;
		}
		
		return (arrayList2);
		
		
		
	}
	
	public static ArrayList<String[]> duplicateArrayList(ArrayList<String[]> inputList) {
		//ArrayList<String[]> newList = new ArrayList<String[]>();
		//for (String[] item : inputList) {
		for (int j=0;j<inputList.size();j++) {
			String[] item = inputList.get(j);
			String[] tmp = new String[2*item.length];
			for (int i=0;i<item.length;i++) {
				tmp[2*i] = item[i];
				tmp[(2*i)+1] = item[i];
			}
			inputList.set(j,tmp);

		}
		
		
		return inputList;
	}
	
	public static ArrayList<String[]> joinListsInner(ArrayList<String[]> arrayList1,ArrayList<String[]> arrayList2,int nCol)
	{
		/*
		 * Assumptions:
		 * 		-Same column index is used in both lists for join (nCol)
		 * 		-inside join
		 * 		-the arrays in each list are the same size
		 * 
		 */
		
		//ArrayList<String[]> shortList;
		//ArrayList<String[]> longList;
		
		ArrayList<String[]> outputList = new ArrayList<String[]>();
		
		/*if (arrayList1.size() > arrayList2.size())
		{
			shortList = arrayList2;
			longList = arrayList1;
		}
		else
		{
			shortList = arrayList1;
			longList = arrayList2;
		}*/
		
		int nIndexList1 = 0;
		//int nIndexList2 = 0;
		
		while (nIndexList1<arrayList1.size())
		{
			
			String[] row1 = arrayList1.get(nIndexList1);
			String[] row2 = null;
			
			boolean bDone = false;
			
			/*
			 * If there are no elements in arrayList2, we'll just skip searching for the element
			 * and go to filling with nulls.
			 */
			if (arrayList2.size()==0)
				bDone = true;
			
			boolean bMatch = false;
			
			//int initialIndex = (nIndexList1>nIndexList2?nIndexList2:nIndexList1);
			int initialIndex;
			
			if (!(nIndexList1>=arrayList2.size()))
				initialIndex = nIndexList1;
			else
				initialIndex = 0;
			
			int index = initialIndex;
			
			while (!bDone)
			{
				row2 = arrayList2.get(index);
				
				if (row2[nCol].equals(row1[nCol]))
				{
					bMatch = true;
					break;
				}
				
				index++;
				
				//We're at the end, wrap around.
				if (index == arrayList2.size())
					index = 0;
				
				//We've come full circle, no match.
				if (index == initialIndex)
					break;
				
				
				
				
				
			}
			
			if (bMatch == true)
			{
				
				String[] joinArray = new String[(row1.length*2)-1];
				
				for (int i=0;i<row1.length;i++)
				{
					joinArray[i] = row1[i];
				}
				
				
				for (int i=0,j=row1.length;i<row2.length;j++,i++)
				{
					if (i==nCol)
						j--;
					else
						joinArray[j] = row2[i];
						
				}
				
				outputList.add(joinArray);
				
				
			}
				
			
			nIndexList1++;
			
			
		}
		
		
		
		
		return(outputList);
		
		
		
	}
	
	public static String createGoogleError(String strReqId,String strReason,String strMessage,String strDetailed) {
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
	
	public static String createFile (String strContent, String strFileName) {
		
		String strFolder = "/var/www/html/PHP/json/";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(strFolder
					+ strFileName + ".html"));
			out.write(strContent);
			out.close();
		}
		catch (IOException ioe) {
			return("Failed: " + ioe.getMessage());
		}
		return("Success");
		
		
		
	}
	
	public static String createJSONFromArrayList(ArrayList<String[]> listCols,ArrayList<String[]> listRows)	{
		/*
		 * The main use of this function is to easily create non-google structured output from google
		 * structured inputs.
		 */
		

		try 
		{
			
	
			JSONArray jsonArray = new JSONArray();
	

			
			for(String[] row : listRows)
			{
				JSONObject tmp = new JSONObject();
				
				for(int i=0;i<listCols.size();i++)
				{
					tmp.put(listCols.get(i)[0], row[i*2]);					
				}
				
				jsonArray.put(tmp);
				
			}
	
	
			
			
			
			return(jsonArray.toString());
			
			//return("google.visualization.Query.setResponse(" + jsonTop.toString() + ");");
		
		}
		catch (JSONException je)
		{
			return(je.getMessage());
		}


		
		
		
	}
	


	public static String createJSONFromResultSet(ResultSet rs) 
	{
		

		try 
		{
			//UtilityFunctions.stdoutwriter.writeln("This is a test from inside createGoogleJSON.",Logs.STATUS1,"A2.73");
			
			//JSONObject jsonTop = new JSONObject(); 
			
			//JSONObject jsonTable = new JSONObject();
	
			JSONArray jsonArray = new JSONArray();
	
			ResultSetMetaData rsmd = rs.getMetaData();
		    int numColumns = rsmd.getColumnCount();
			
			while(rs.next())
			{
				JSONObject tmp = new JSONObject();
				
				for(int i=1;i<=numColumns;i++)
				{
					if (rs.getString(rsmd.getColumnName(i))!=null)
						tmp.put(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
					else
						tmp.put(rsmd.getColumnName(i), "");
						
	
					
				}
				
				jsonArray.put(tmp);
				
			}
	
	
			
			
			
			return(jsonArray.toString());
			
			//return("google.visualization.Query.setResponse(" + jsonTop.toString() + ");");
		
		}
		catch (JSONException je)
		{
			return(je.getMessage());
		}
		catch (SQLException sqle)
		{
			return(sqle.getMessage());
		}

		
		
		
	}
	
	public static String createGoogleJSON(ArrayList<String[]> arrayListCols, ArrayList<String[]> arrayListRows,String strReqId,boolean bEmptyZeros) {

		/*
		 * OFP 4/1/2011 - Added bEmptyZeros to indicate if zero values should be replaced with the empty string.
		 * OFP 7/22/2012 - Can't remember why empty string is preferrable to a zero.
		 * 
		 * OFP 7/22/2012 - This can't handle null values, the caller has to make sure that something else is passed instead of null.
		 * 
		 */
		try 
		{
		//UtilityFunctions.stdoutwriter.writeln("This is a test from inside createGoogleJSON.",Logs.STATUS1,"A2.73");
		
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


		//for (int i=0;i<arrayListRows.size();i++) {
		int i = 0;
		for (String[] foo2 : arrayListRows)	{
			JSONArray tmpArray = new JSONArray();
			//JSONObject cJSON = new JSONObject();
			

			//String[] foo2 = arrayListRows.get(i);
			for (int k=0;k<arrayListCols.size();k++)
			{
				JSONObject tmp = new JSONObject();
				if (arrayListCols.get(k)[2].equals("number"))
				{
					
					//writeMsg("value: " + foo2[2*k]);
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
			
			i++;
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
		//uf = new UtilityFunctions();
		
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
  	
  	public static ArrayList<String[]> removeColumn(ArrayList<String[]> inputArrayList,int nCol) throws CustomInvalidInputException
  	{
  		//Removes an element from each array in the arraylist. nCol is zero based.
  		
  		ArrayList<String[]> outputArrayList = new ArrayList<String[]>();
  		

  		
  		//for (int i=0;i<inputArrayList.size();i++)
  		for (String[] inRow : inputArrayList) {
  			//String[] inRow = inputArrayList.get(i);
  			
  	  		if (nCol >= inRow.length)
				throw new CustomInvalidInputException("Invalid input. Column is zero based and needs to be less than the array length.");
  			
  			String[] outRow = new String[inRow.length - 1];
  			
  			for (int j=0,n=0;j<inRow.length;j++)
  			{
  				if (j==nCol)
  					continue;
  				else
  				{
  					outRow[n]=inRow[j];
  					n++;
  				}
  				
  			}
  			
  			outputArrayList.add(outRow);
  			
  		}
  		
  		return(outputArrayList);
  		
  	
  	}
  	
  	public static ArrayList<String[]> removeLastColumn(ArrayList<String[]> inputArrayList) throws CustomInvalidInputException
  	{
  		//Removes the last index from all of the String arrays
  		
  	
  		
  		ArrayList<String[]> outputArrayList = new ArrayList<String[]>();
  		
  		//for (int i=0;i<inputArrayList.size();i++)
  		for (String[] inRow : inputArrayList) {
  			//String[] inRow = inputArrayList.get(i);
  			
  		
  			
  			String[] outRow = new String[inRow.length - 1];
  			
  			for (int j=0;j<outRow.length;j++)
  			{
  				outRow[j]=inRow[j];
  			}
  			
  			outputArrayList.add(outRow);
  			
  		}
  		
  		return(outputArrayList);
  		
  	
  	} 
  	
  	public static ArrayList<String[]> fillDateSeries(ArrayList<String[]> inputArrayList,String strBeginRange,String strEndRange, int nDateCol) throws ParseException,CustomInvalidInputException {
  		
  		DateFormat formatter = new SimpleDateFormat("yyyy-MM");
  		DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
  		
  		Calendar calBeginRange = Calendar.getInstance();
  		Calendar calEndRange = Calendar.getInstance();
  		
  		
  		calBeginRange.setTime(formatter2.parse(strBeginRange));
  		calEndRange.setTime(formatter2.parse(strEndRange));
  		
  		
  		ArrayList<String[]> dateList = new ArrayList<String[]>();
  		ArrayList<String[]> finalList = new ArrayList<String[]>();
  		
  		Calendar calCurrent = (Calendar)calBeginRange.clone();
  		
  	
  		boolean bDone = false;
  		
  		String[] sample = inputArrayList.get(1);
  		
  		
  		
  		if (calCurrent.after(calEndRange))
				bDone=true;
  		
  		while (!bDone==true) {
  			
  			String[] tmp = new String[sample.length];
  			for (int i=0;i<sample.length;i++) {
  				if (i!=nDateCol)
  					tmp[i] = "";
  				else
  					tmp[i] = formatter.format(calCurrent.getTime());
  			}
  			
  			dateList.add(tmp);
  			
  			calCurrent.add(Calendar.MONTH, 1);
  			
  			if (calCurrent.after(calEndRange))
  				bDone=true;
  			
  			
  		}
  		
  		finalList = joinListsLeftOuter(dateList,inputArrayList,nDateCol);
  		
  		/*
  		 * 1/5/2012: Need to remove the extra columns from the join
  		 */
  
  		for (int i=1;i<=dateList.get(0).length-1;i++) {
  			
  			finalList = PopulateSpreadsheet.removeColumn(finalList, 1);
  		}
	

  		
  		
  		return finalList;
  		
  	}
  	
	public static ArrayList<String[]> getFirstGroupBy(ArrayList<String[]> inputArrayList,int[] x) throws CustomInvalidInputException {
		
		/*
  		 * Return the row with a particular maximum value in a group of values. This is to avoid
  		 * having to do nested select queries.
  		 * 
  		 * Assumptions: 
  		 * 		- index x1 is the first group column
  		 *  	- index x2 is the second group column
  		 *      - data has been ordered by x1 and then x2
  		 * 
  		 * 
  		 */
  		ArrayList<String[]> outputArrayList = new ArrayList<String[]>();
  		
  		if (inputArrayList.size() == 0)
  			throw new CustomInvalidInputException("Input contains no data.");
  		
  		String[] firstRow = inputArrayList.get(0);
  		String[] previousRow = firstRow;
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
  				outputArrayList.add(firstRow);
  				firstRow = row;
  				
  				
  				
  				 				
  			}
  			
  			previousRow = row;
  			
  			//if (Integer.parseInt(row[y]) > Integer.parseInt(maxRow[y]))
  				//maxRow = row;		
  		}
  		
  		outputArrayList.add(row);
  		
  		
  		return(outputArrayList);
	
  	}
  	
  	
	public static ArrayList<String[]> getLastGroupBy(ArrayList<String[]> inputArrayList,int[] x) throws CustomInvalidInputException
  	{
		
		/*
  		 * Return the row with a particular maximum value in a group of values. This is to avoid
  		 * having to do nested select queries.
  		 * 
  		 * Assumptions: 
  		 * 		- index x1 is the first group column
  		 *  	- index x2 is the second group column
  		 *      - data has been ordered by x1 and then x2
  		 * 
  		 * 
  		 */
  		ArrayList<String[]> outputArrayList = new ArrayList<String[]>();
  		
  		if (inputArrayList.size() == 0)
  			throw new CustomInvalidInputException("Input contains no data.");
  		
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
	
	public static ArrayList<String[]> getMaxMinGroupBy(ArrayList<String[]> inputArrayList,int[] x,int y,String strType,String strComparison) throws ParseException,CustomInvalidInputException
  	{
  		/*
  		 * Return the row with a particular maximum value in a group of values. This is to avoid
  		 * having to do nested select queries. 
  		 * 
  		 * Assumptions: 
  		 * 		- index x1 is the first group column
  		 *  	- index x2 is the second group column
  		 *      - index y is the min column, and strDataType indicates the type of comparison (currently DATE or NUMBER)
  		 *      - strComparison is either MAX or MIN
  		 *      - data has been ordered by x1 and then x2
  		 * 
  		 * NOTE: If you only have one group by column in your data, you can add a placeholder column and 
  		 * then remove it after calling this function.
  		 * 
  		 */
  		ArrayList<String[]> outputArrayList = new ArrayList<String[]>();
  		
  		if (inputArrayList.size() == 0)
  			throw new CustomInvalidInputException("Input contains no data.");
  		String[] minmaxRow = inputArrayList.get(0);
  		String[] row;
  		
  		for (int i=1;i<inputArrayList.size();i++)
  		{
  			row = inputArrayList.get(i);
  			boolean bResult=false;
  			for (int j=0;j<x.length;j++)
  			{
  				if (!row[x[j]].equals(minmaxRow[x[j]]))
  					bResult=true;
  				
  			}
  			//if (!((row[x1].equals(maxRow[x1])) && (row[x2].equals(maxRow[x2]))))
  			if (bResult==true)
  				// we're on to the next group
  			{
  				outputArrayList.add(minmaxRow);
  				
  				minmaxRow = row;
  				
  				
  			}
  			
  			
  			if (strType.toUpperCase().equals("DATE"))
  			{
  				
  				SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  				
  				Calendar cal1 = Calendar.getInstance();
  				Calendar cal2 = Calendar.getInstance();
  				

	  			cal1.setTime(inputFormat.parse(row[y]));
	  			cal2.setTime(inputFormat.parse(minmaxRow[y]));
  		
  				
	  			if (strComparison.toUpperCase().equals("MIN"))
  	  		   {
	  				if (cal1.before(cal2))
	  					minmaxRow = row;
	  				//if (Double.parseDouble(row[y]) < Double.parseDouble(minmaxRow[y]))
	  				//	minmaxRow = row;
  	  		   }
  	  		   else //assume max
  	  		   {
  	  			   if (cal1.after(cal2))
  	  				   minmaxRow = row;
  	  			   //if (Double.parseDouble(row[y]) > Double.parseDouble(minmaxRow[y]))
  	  				//   minmaxRow = row;
  	  		   }
  				
  			}
  			else //assume number
  			{
	  		   if (strComparison.toUpperCase().equals("MIN"))
	  		   {
	  			if (Double.parseDouble(row[y]) < Double.parseDouble(minmaxRow[y]))
	  				minmaxRow = row;
	  		   }
	  		   else //assume max
	  		   {
	  			 if (Double.parseDouble(row[y]) > Double.parseDouble(minmaxRow[y]))
	   				minmaxRow = row;
	  		   }
  			}
  			
  		}
  		
  		outputArrayList.add(minmaxRow);
  		
  		
  		return(outputArrayList);
  		
  		
  		
  	}
	
	public static ArrayList<String[]> limitRows(ArrayList<String[]> inputArrayList,String strRows)
	{
		int nRows = Integer.parseInt(strRows);
		if (nRows >= inputArrayList.size())
			return(inputArrayList);
		
		ArrayList<String[]> outputArrayList = new ArrayList<String[]>();
		
		for (int i=0;i<nRows;i++)
		{
			outputArrayList.add(inputArrayList.get(i));		
		}
		
		return(outputArrayList);
	
	}

  	
  	//public static ArrayList<String[]> getMaxGroupBy(ArrayList<String[]> inputArrayList,int x1, int x2,int y)
	public static ArrayList<String[]> getMaxGroupBy(ArrayList<String[]> inputArrayList,int[] x,int y) throws CustomInvalidInputException
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
  		 * NOTE: If you only have one group by column in your data, you can add a placeholder column and 
  		 * then remove it after calling this function.
  		 */
  		ArrayList<String[]> outputArrayList = new ArrayList<String[]>();
  		
  		if (inputArrayList.size() == 0)
  			throw new CustomInvalidInputException("Input contains no data.");
  		
  	
  		
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
  			
  			/*if (Integer.parseInt(row[y]) > Integer.parseInt(maxRow[y]))
  				maxRow = row;*/
  			if (Double.parseDouble(row[y]) > Double.parseDouble(maxRow[y]))
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
  	
  	public static String displayDebugTable(ArrayList<String[]> arrayListRows,int nRowLimit) {
		
		String output = "<table border=\"2\">";
		
		//out.println("<table>");
		
		
		if (nRowLimit > arrayListRows.size())
			nRowLimit = arrayListRows.size();
		
		nRowLimit = (nRowLimit == 0 ? arrayListRows.size() : nRowLimit);
		
		for (int i=0;i<nRowLimit;i++)
		{
			//out.println("<tr>");
			output += "<tr>";
			String[] temp = arrayListRows.get(i);
			for (int j=0;j<temp.length;j++)
			{
				//out.println("<td>" + temp[j] +"</td>");
				output += "<td>" + temp[j] +"</td>";
			}
			//out.println("</tr>");
			output += "</tr>";
		}
		//out.println("</table>");
		output += "</table>";
	
	
		return(output);
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
	boolean bDateOnly;
	
	JSONDate(String date)
	{
		String[] date2;
		String[] time2;
		String strDatePortion = "";
		String strTimePortion = "";
		
		date = date.trim();
		bDateOnly = true;
		
		//check if it contains a space then it is a date-time value
		if (date.contains(" ")) {
			bDateOnly = false;
			strDatePortion = date.substring(0,date.indexOf(" "));
			strTimePortion = date.substring(date.indexOf(" ") + 1,date.length());
			
		}
		else {
			strDatePortion = date;
		}
		
		if (strDatePortion.contains("/"))
			date2 = strDatePortion.split("/");
		else
			date2 = strDatePortion.split("-");
		this.value = Calendar.getInstance();
		this.value.set(Calendar.YEAR, Integer.parseInt(date2[2]));
		this.value.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date2[1]));
		this.value.set(Calendar.MONTH, Integer.parseInt(date2[0]) - 1);
		
		if (!strTimePortion.isEmpty()) {
			time2 = strTimePortion.split(":");
			this.value.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time2[0]));
			this.value.set(Calendar.MINUTE, Integer.parseInt(time2[1]));
			this.value.set(Calendar.SECOND, Integer.parseInt(time2[2]));
			
			
			
		}
		
	}
	
	public String toJSONString()
	{
		if (bDateOnly == true)
			return("new Date(" + value.get(Calendar.YEAR) + "," + value.get(Calendar.MONTH) + "," + value.get(Calendar.DAY_OF_MONTH) + ")");
		else
			return("new Date(" + value.get(Calendar.YEAR) + "," + value.get(Calendar.MONTH) + "," + value.get(Calendar.DAY_OF_MONTH) + "," +
					value.get(Calendar.HOUR_OF_DAY) + "," + value.get(Calendar.MINUTE) + "," + value.get(Calendar.SECOND) + ")");
		
	}
	
	

	
		
		
	}

class CustomEntityGroupException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1264200616496045940L;
	
}

class CustomInvalidInputException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1981345432124138523L;
	
	public CustomInvalidInputException(String msg){
		super(msg);
		}
	
	
}

