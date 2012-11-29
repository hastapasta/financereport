/*
    File: Connect.java 1.1, 2006-7-17
    Copyright (c) 1991, 2007 Oracle and / or its affiliates. All rights reserved.
 */
//package com.essbase.samples.japi;

import com.essbase.api.base.*;
import com.essbase.api.session.*;
import com.essbase.api.dataquery.IEssCubeView;
import com.essbase.api.dataquery.IEssGridView;
import com.essbase.api.dataquery.IEssOpKeepOnly;
import com.essbase.api.dataquery.IEssOpPivot;
import com.essbase.api.dataquery.IEssOpRemoveOnly;
import com.essbase.api.dataquery.IEssOpReport;
import com.essbase.api.dataquery.IEssOpRetrieve;
import com.essbase.api.dataquery.IEssOpZoomIn;
import com.essbase.api.dataquery.IEssOpZoomOut;
import com.essbase.api.dataquery.IEssOperation;
import com.essbase.api.datasource.IEssCube;
//import com.essbase.api.datasource.IEssOlapFileObject;
import com.essbase.api.datasource.IEssOlapServer;
//import com.essbase.api.datasource.IEssOlapApplication;
import com.essbase.api.metadata.IEssCubeOutline;
import com.essbase.api.metadata.IEssMember;
import com.essbase.api.metadata.IEssMemberSelection;
import com.essbase.api.domain.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
    Connect example signs on to Analytic Server and signs off.

    In order for this sample to work in your environment, make sure to
    change the static variables to suit your environment.

    @author Srini Ranga
    @version 1.1, 17 Jul 06
 */
public class EssbaseConnect {
    // NOTE: Change the following variables to suit your setup.
    private static String s_userName = "admin";
    private static String s_password = "madmax1.";
    
    private static String s_olapSvrName = "oracleepm";
    /* Possible values for s_provider: 
        "Embedded" or "http://localhost:13080/aps/JAPI" */
    private static String s_provider = "http://oracleepm:13080/aps/JAPI"; // Default
    
    private static String s_csvpath = "/home/ollie/workspace/pdi/fed_bs_date.csv";
    
    private static String s_strApplication = "Sample";
    //private static String s_strApplication = "demo";
    
    //private static String s_strCube = "Fed";
    private static String s_strCube = "basic";
    
    private static IEssDomain s_dom;
    private static IEssbase s_ess;
    //private static boolean loadData = false;
    
    private static final int FAILURE_CODE = 1;
    
    //private static IEssOlapServer s_olapserver = null;
    //private static IEssOlapApplication s_application = null;
    private static IEssCube s_cube = null;
    private static IEssCubeOutline s_cubeOutline = null;
    
    public static void connect(String strApp, String strCube) {
        int statusCode = 0; // will set this to FAILURE only if err/exception occurs.
        //IEssbase ess = null;
        try {
            //acceptArgs(args);
            // Create JAPI instance.
        	s_strCube = strCube;
        	s_strApplication = strApp;
            s_ess = IEssbase.Home.create(IEssbase.JAPI_VERSION);
            // Sign On to the Provider
            s_dom = s_ess.signOn(s_userName, s_password, false, null, s_provider);
            IEssOlapServer olapSvr = s_dom.getOlapServer(s_olapSvrName);
            olapSvr.connect();
            
            System.out.println("Connection to Analyic server '" +olapSvr.getName()+ "' was successful.");
            
            s_cube = olapSvr.getApplication(s_strApplication).getCube(s_strCube);
            
            System.out.println("Successfully obtained " + s_strApplication + "." + s_strCube + " cube.");
            
            //s_cubeOutline = s_cube.openOutline(false, true, true);
            
            //System.out.println("Successfully obtained cube outline");
            /*String apiVersion = ess.getApiVersion();
            String apiVerDetail = ess.getApiVersionDetail();
            /System.out.println("API Version :"+apiVersion);
            System.out.println("API Version Detail :"+apiVerDetail);
            if (loadData) loadData(olapSvr);*/
            //setSubVars(s_csvpath);
            //sortMembers();
            
            //s_cubeOutline.save();
    		//s_cubeOutline.restructureCube(IEssCube.EEssRestructureOption.KEEP_ALL_DATA);
    		//s_cubeOutline.close();
        	
            
		} catch (EssException x) {
            System.err.println("Error: " + x.getMessage());
            statusCode = FAILURE_CODE;
       
            // Sign off.
            try {
                if (s_ess != null && s_ess.isSignedOn() == true)
                    s_ess.signOff();
            } catch (EssException ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
        // Set status to failure only if exception occurs and do abnormal termination
        // otherwise, it will by default terminate normally 
        if (statusCode == FAILURE_CODE) System.exit(FAILURE_CODE);        
    }
    
    public static void queryCube()
    {
    	try
    	{
	    	IEssCubeView cv = null;
	    	cv = s_dom.openCubeView("Data Query Example", s_olapSvrName, s_strApplication,
	                s_strCube);
    	}
    	catch (EssException ee)
    	{
    		ee.printStackTrace();
    	}
    	
    	
    }
    
    public static void disconnect()
    {
    	
    	 try {
             if (s_ess != null && s_ess.isSignedOn() == true)
                 s_ess.signOff();
         } catch (EssException x) {
             System.err.println("Error: " + x.getMessage());
         }
    }
    
    static void performMemberSelection()
    {
    	try
    	{
    	IEssCubeView cv = s_dom.openCubeView("Data Query Example", s_olapSvrName, s_strApplication,
                s_strCube);
String fldSel = "<OutputType Binary <SelectMbrInfo (MemberName, MemberLevel, MemberGen, Consolidation, MemberFormula, MemberAlias, DimensionName, Expense,  MemberNumber, DimensionNumber, ChildMemberName, ParentMemberName, PreviousMemberName, NextMemberName)",
       mbrSel = "@ichild(Measures), @ichild(Regions)";
IEssMember[] mbrs = cv.memberSelection(mbrSel, fldSel);
for (int i = 0; i < mbrs.length; i++) {
    IEssMember mbr = mbrs[i];
    System.out.println("Name: " + mbr.getName());
    System.out.println("Level: " + mbr.getLevelNumber());
    System.out.println("Generation: " + mbr.getGenerationNumber());
    System.out.println("Consolidation: " + mbr.getConsolidationType());
    System.out.println("Formula: " + mbr.getFormula());
    System.out.println("Dimension name: " + mbr.getDimensionName());
    // IEssMember.getChildCount() operation not allowed on Members obtained from IEssCubeView.memberSelection.
//    System.out.println("Child count: " + mbr.getChildCount());
    System.out.println("Parent name: " + mbr.getParentMemberName());
    System.out.println("Member number: " + mbr.getMemberNumber());
    System.out.println("Dimension number: " + mbr.getDimensionNumber());
    System.out.println("--------------------------");
}

mbrs = cv.memberSelection("Year", IEssMemberSelection.QUERY_TYPE_CHILDREN,
    IEssMemberSelection.QUERY_OPTION_MEMBERSONLY, "Year", "", "");
 for (int i = 0; i < mbrs.length; i++) {
    IEssMember mbr = mbrs[i];
    System.out.println("Name: " + mbr.getName() +
        ", Desc: " + mbr.getDescription() +
        ", Level Num: " + mbr.getLevelNumber() +
        ", Gen Num: " + mbr.getGenerationNumber() +
//        ", Child count: " + mbr.getChildCount() +
        ", Dim Name: " + mbr.getDimensionName() /*+
        ", Dim Category: " + mbr.getDimensionCategory().stringValue()*/);
 }
    	}
    	catch (EssException ee)
    	{
    		ee.printStackTrace();
    	}
}
    
    static ArrayList<String[]> performCubeViewOperation2(String query) {
    	IEssCubeView cv=null;
    	try
    	{
    	cv = s_dom.openCubeView("Data Query Example", s_olapSvrName, s_strApplication,
                s_strCube);
        // Create a grid view with the input for the operation.
        IEssGridView grid = cv.getGridView();
        grid.setSize(3, 4);
        //grid.setValue(2, 0, "Measures");
       // grid.setValue(0, 3, "Regions");
        //grid.setValue(0, 3, "Market");
        //grid.setValue(1, 1, "Measures"); ;
        //grid.setValue(0, 2, "2010-12-29");
        //grid.setValue(0, 3, "2010-12-22");
       // grid.setValue(2, 0, "Measures");
        //grid.setValue(2, 1, "Sales");

        // Create the operation specification.
        IEssOperation op = null;
   
        op = cv.createIEssOpRetrieve();
       /* ((IEssOpRetrieve)op).setQuery("{TabDelim}<Column (Regions)" +
            "<Row (Measures) <Idesc Measures !",
            IEssCubeView.EEssQueryGrammar.ESSBASE);*/
        /*((IEssOpRetrieve)op).setQuery("{TabDelim}<Page (Regions) <Column (Years) (\"2010-12-29\",\"2010-12-22\")" +
                "<Row (Measures) <LINK (<DIMBOTTOM(Measures) AND <DESCENDANTS(Capital)) !",
                IEssCubeView.EEssQueryGrammar.ESSBASE);*/
        ((IEssOpRetrieve)op).setQuery(query,IEssCubeView.EEssQueryGrammar.ESSBASE);
        

        // Perform the operation.
        cv.performOperation(op);

        // Get the result and print the output.
        int cntRows = grid.getCountRows(), cntCols = grid.getCountColumns();
        System.out.print("Query Results for the Operation: " + "\n" +
            "-----------------------------------------------------\n");
        ArrayList<String[]> tmp = new ArrayList<String[]>();
        for (int i = 0; i < cntRows; i++) {
        	String[] tmpArray = new String[cntCols];
            for (int j = 0; j < cntCols; j++)
            {   	
            	tmpArray[j] = grid.getValue(i,j) + "";
                System.out.print(grid.getValue(i, j) + "\t");
            }
            tmp.add(tmpArray);
            System.out.println();
        }
        System.out.println("\n");
        return(tmp);
    	}
    	catch (EssException ee)
    	{
    		ee.printStackTrace();
    	}
    	finally
    	{
    		try
    		{
    			if (cv!=null)
    				cv.close();
    		}
    		catch(EssException ex)
    		{
    			ex.printStackTrace();
    		}
    	}
    	
    	return(null);
    }
    
    static void performCubeViewOperation(String opStr) {
    	IEssCubeView cv=null;
    	try
    	{
    	cv = s_dom.openCubeView("Data Query Example", s_olapSvrName, s_strApplication,
                s_strCube);
        // Create a grid view with the input for the operation.
        IEssGridView grid = cv.getGridView();
        grid.setSize(3, 5);
        grid.setValue(0, 2, "Product");
        grid.setValue(0, 3, "Market");
        grid.setValue(1, 2, "Jan"); ;
        grid.setValue(1, 3, "Feb");
        grid.setValue(1, 4, "Mar");
        grid.setValue(2, 0, "Actual");
        grid.setValue(2, 1, "Sales");

        // Create the operation specification.
        IEssOperation op = null;
        if (opStr.equals("conditionalRetrieve")) {
            op = cv.createIEssOpRetrieve();
            ((IEssOpRetrieve)op).setQuery("{TabDelim}<Column (Scenario, Year)" +
                "<Row (Market, Product) <Ichild Market <Ichild Product !",
                IEssCubeView.EEssQueryGrammar.ESSBASE);
        } else if (opStr.equals("report")) {
            op = cv.createIEssOpReport();
            ((IEssOpReport)op).set(false, "{TabDelim}<idesc Year !", false);
        } else if (opStr.equals("reportFile")) {
            op = cv.createIEssOpReport();
            ((IEssOpReport)op).set("Actsales", false, false);
        } else if (opStr.equals("report_with_no_parsing")) {
            op = cv.createIEssOpReport();
            ((IEssOpReport)op).set(false, "{TabDelim}<idesc Year !", false);
            ((IEssOpReport)op).setNoParsing(true);
            cv.performOperation(op);
            System.out.print("Query Results for the Operation: " + opStr + "\n" +
            "-----------------------------------------------------\n");
            System.out.println(grid.toString());
            return;
        } else if (opStr.equals("reportFile_with_no_parsing")) {
            op = cv.createIEssOpReport();
            ((IEssOpReport)op).set("Actsales", false, false);
            ((IEssOpReport)op).setNoParsing(true);
            cv.performOperation(op);
            System.out.print("Query Results for the Operation: " + opStr + "\n" +
            "-----------------------------------------------------\n");
            System.out.println(grid.toString());
            return;
        } else if (opStr.equals("retrieve")) {
            op = cv.createIEssOpRetrieve();
        } else if (opStr.equals("zoomIn")) {
            op = cv.createIEssOpZoomIn();
            ((IEssOpZoomIn)op).addRange(0, 3, 1, 1);
        } else if (opStr.equals("conditionalZoomIn")) {
            IEssOpZoomIn opCzi = cv.createIEssOpZoomIn();
            opCzi.addRange(0, 3, 1, 1);
            opCzi.setPreference(true,
                IEssOpZoomIn.EEssZoomInPreference.BOTTOM_LEVEL);
            opCzi.setQuery(true, "<child Market !",
                IEssCubeView.EEssQueryGrammar.ESSBASE);
            op = opCzi;
        } else if (opStr.equals("zoomOut")) {
            op = cv.createIEssOpZoomOut();
            ((IEssOpZoomOut)op).addRange(1, 2, 1, 1);
        } else if (opStr.equals("keepOnly")) {
            op = cv.createIEssOpKeepOnly();
            ((IEssOpKeepOnly)op).addRange(1, 3, 1, 1);
        } else if (opStr.equals("removeOnly")) {
            op = cv.createIEssOpRemoveOnly();
            ((IEssOpRemoveOnly)op).addRange(1, 4, 1, 1);
        } else if (opStr.equals("pivot")) {
            op = cv.createIEssOpPivot();
            ((IEssOpPivot)op).set(0, 3);
        } else
            throw new EssException("Operation not supported.");

        // Perform the operation.
        cv.performOperation(op);

        // Get the result and print the output.
        int cntRows = grid.getCountRows(), cntCols = grid.getCountColumns();
        System.out.print("Query Results for the Operation: " + opStr + "\n" +
            "-----------------------------------------------------\n");
        for (int i = 0; i < cntRows; i++) {
            for (int j = 0; j < cntCols; j++)
                System.out.print(grid.getValue(i, j) + "\t");
            System.out.println();
        }
        System.out.println("\n");
    	}
    	catch (EssException ee)
    	{
    		ee.printStackTrace();
    	}
    	finally
    	{
    		try
    		{
    			if (cv!=null)
    				cv.close();
    		}
    		catch(EssException ex)
    		{
    			ex.printStackTrace();
    		}
    	}
    }
    
    private static void setSubVars(String csvFilePath) throws EssException
    {
    	//IEssOlapApplication app = olapSvr.getApplication(s_application);
    	String[][] subvarsArray = null;
    	//String[][] subvarsArray2 = null;
    	//String[][] subvarsArray3 = null;
    	//subvarsArray = olapSvr.getSubstitutionVariables();
    	//subvarsArray2 = olapSvr.getApplication(s_application).getSubstitutionVariables();
    	subvarsArray = s_cube.getSubstitutionVariables();
    	
    	//String sPrevPeriod="";
    	String sCurPeriod="";
    	for (int i=0;i<subvarsArray.length;i++)
    	{
    		/*
    		 * Added the following line because if the subvars are global, then the app and cube values
    		 * will be null.
    		*/
    		if ((subvarsArray[i][2] != null) && (subvarsArray[i][3] != null))
    		{
	    		if (subvarsArray[i][2].equals(s_strApplication) && subvarsArray[i][3].equals(s_strCube)
	    				&& subvarsArray[i][0].equals("curperiod"))
	    		{
	    			sCurPeriod = subvarsArray[i][1];
	    			break;
	    		}
    		}
	
    	}
    	
    	if (sCurPeriod.isEmpty())
    	{
    		System.out.println("ERROR: curperiod substitution variable is empty. Subvars were not set.");
    		return;
    	}
    	

    	s_cube.createSubstitutionVariable("prevperiod", sCurPeriod);
    	
    	/*
    	 * Need to change the consolidation operator for the prevperiod member. Couldn't figure out how to do this
    	 * during the dim build process so we're doing it here. 
    	 */
 
    	changeConsolidationOperator(sCurPeriod);
    	
    	
    	String[] field;
    	
    	try
    	{
	    	File file = new File(csvFilePath);
	    	BufferedReader bufRdr  = new BufferedReader(new FileReader(file));

	    	//skip the header
	    	bufRdr.readLine();
	    	
	    	field = bufRdr.readLine().split(","); 
	    	
	
			bufRdr.close();
    	}
    	catch (FileNotFoundException fnfe)
    	{
    		System.out.println("ERROR: Problem reading new member csv file. Curperiod subvar not set.");
    		fnfe.printStackTrace();
    		return;
    	}
    	catch (IOException ioe)
    	{
    		System.out.println("ERROR: problem closing csv file. Curperiod subvar not set.");
    		ioe.printStackTrace();
    		return;
    
    	}
    	
    	s_cube.createSubstitutionVariable("curperiod", "\"" + field[1] + "\"");
     	
    }
    
    private static void sortMembers() throws EssException
    {
    	//IEssCube cube = olapSvr.getApplication(s_application).getCube(s_cube);
    	//IEssCubeOutline cubeOutline = s_cube.openOutline(false, true, true);
		IEssMember member = s_cubeOutline.findMember("Year");
		member.sortChildren(true);
		
    	
    }
    
    private static void changeConsolidationOperator(String strMember)
    {
    	try
    	{
    		/*
    		 * Strip the quotes.
    		 */
    		strMember = strMember.trim( );
    	    if ( strMember.startsWith( "\"" ) && strMember.endsWith( "\"" ) )
    	      strMember = strMember.substring( 1, strMember.length( ) - 1 );
    	    
    	    //IEssCubeOutline cubeOutline = s_cube.openOutline(false, true, true);

    		IEssMember member = s_cubeOutline.findMember(strMember);
    		
    		member.setConsolidationType(IEssMember.EEssConsolidationType.IGNORE);
    		member.updatePropertyValues();


    	}
    	catch (EssException esse)
    	{
    		System.out.println("ERROR: Problem setting consolidation operator for member " + strMember);
    		esse.printStackTrace();
    	}
    	
    	
    }
    
   /* private static void loadData(IEssOlapServer olapSvr) throws EssException {
    	IEssCube cube = olapSvr.getApplication("Sample").getCube("Basic");
    	cube.loadData(IEssOlapFileObject.TYPE_RULES, null, IEssOlapFileObject.TYPE_TEXT,
    			"Calcdat", true);
    	System.out.println("Loaddata done to Sample/Basic");
    }*/
    
    static void acceptArgs(String[] args) throws EssException {
        if (args.length >= 5) {
            s_userName = args[0];
            s_password = args[1];   
            s_olapSvrName = args[2];
            s_provider = "http://" + args[2] + ":13080/aps/JAPI";
            s_strApplication = args[3];
            s_strCube = args[4];
            //s_csvpath = args[5];

            
        } else if (args.length != 0) {
            System.err.println("ERROR: Incorrect Usage of this sample.");
            System.err.println(
                "Usage: java " + EssbaseConnect.class.getName() + " <user> <password> <analytic server> <provider>");
            System.exit(FAILURE_CODE); // Simply end
        }
    }
}
