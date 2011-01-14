/*
    File: Connect.java 1.1, 2006-7-17
    Copyright (c) 1991, 2007 Oracle and / or its affiliates. All rights reserved.
 */
//package com.essbase.samples.japi;

import com.essbase.api.base.*;
import com.essbase.api.session.*;
import com.essbase.api.datasource.IEssCube;
//import com.essbase.api.datasource.IEssOlapFileObject;
import com.essbase.api.datasource.IEssOlapServer;
//import com.essbase.api.datasource.IEssOlapApplication;
import com.essbase.api.metadata.IEssCubeOutline;
import com.essbase.api.metadata.IEssMember;
import com.essbase.api.domain.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

/**
    Connect example signs on to Analytic Server and signs off.

    In order for this sample to work in your environment, make sure to
    change the static variables to suit your environment.

    @author Srini Ranga
    @version 1.1, 17 Jul 06
 */
public class Connect {
    // NOTE: Change the following variables to suit your setup.
    private static String s_userName = "admin";
    private static String s_password = "madmax1.";
    
    private static String s_olapSvrName = "oracleepm";
    /* Possible values for s_provider: 
        "Embedded" or "http://localhost:13080/aps/JAPI" */
    private static String s_provider = "http://oracleepm:13080/aps/JAPI"; // Default
    
    private static String s_csvpath = "/home/ollie/workspace/pdi/fed_bs_date.csv";
    
    private static String s_strApplication = "Sample";
    
    private static String s_strCube = "Fed";
    //private static boolean loadData = false;
    
    private static final int FAILURE_CODE = 1;
    
    //private static IEssOlapServer s_olapserver = null;
    //private static IEssOlapApplication s_application = null;
    private static IEssCube s_cube = null;
    private static IEssCubeOutline s_cubeOutline = null;
    
    public static void main(String[] args) {
        int statusCode = 0; // will set this to FAILURE only if err/exception occurs.
        IEssbase ess = null;
        try {
            acceptArgs(args);
            // Create JAPI instance.
            ess = IEssbase.Home.create(IEssbase.JAPI_VERSION);
            // Sign On to the Provider
            IEssDomain dom 
                = ess.signOn(s_userName, s_password, false, null, s_provider);
            IEssOlapServer olapSvr = dom.getOlapServer(s_olapSvrName);
            olapSvr.connect();
            
            System.out.println("Connection to Analyic server '" +olapSvr.getName()+ "' was successful.");
            
            s_cube = olapSvr.getApplication(s_strApplication).getCube(s_strCube);
            
            System.out.println("Successfully obtained " + s_strApplication + "." + s_strCube + " cube.");
            
            s_cubeOutline = s_cube.openOutline(false, true, true);
            
            System.out.println("Successfully obtained cube outline");
            /*String apiVersion = ess.getApiVersion();
            String apiVerDetail = ess.getApiVersionDetail();
            /System.out.println("API Version :"+apiVersion);
            System.out.println("API Version Detail :"+apiVerDetail);
            if (loadData) loadData(olapSvr);*/
            setSubVars(s_csvpath);
            sortMembers();
            
            s_cubeOutline.save();
    		s_cubeOutline.restructureCube(IEssCube.EEssRestructureOption.KEEP_ALL_DATA);
    		s_cubeOutline.close();
        	
            
		} catch (EssException x) {
            System.err.println("Error: " + x.getMessage());
            statusCode = FAILURE_CODE;
        } finally {
            // Sign off.
            try {
                if (ess != null && ess.isSignedOn() == true)
                    ess.signOff();
            } catch (EssException x) {
                System.err.println("Error: " + x.getMessage());
            }
        }
        // Set status to failure only if exception occurs and do abnormal termination
        // otherwise, it will by default terminate normally 
        if (statusCode == FAILURE_CODE) System.exit(FAILURE_CODE);        
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
    	/*
    	 * Need to sort the current year member.
    	 */
    	Calendar cal = Calendar.getInstance();
		IEssMember member = s_cubeOutline.findMember(cal.get(Calendar.YEAR)+"");
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
                "Usage: java " + Connect.class.getName() + " <user> <password> <analytic server> <provider>");
            System.exit(FAILURE_CODE); // Simply end
        }
    }
}
