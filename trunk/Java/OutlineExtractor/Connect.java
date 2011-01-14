/*
    File: Connect.java 1.1, 2006-7-17
    Copyright (c) 1991, 2007 Oracle and / or its affiliates. All rights reserved.
 */
//package com.essbase.samples.japi;

import com.essbase.api.base.*;
import com.essbase.api.session.*;
import com.essbase.api.datasource.IEssCube;
import com.essbase.api.datasource.IEssOlapFileObject;
import com.essbase.api.datasource.IEssOlapServer;
import com.essbase.api.domain.*;

/**
    Connect example signs on to Analytic Server and signs off.

    In order for this sample to work in your environment, make sure to
    change the static variables to suit your environment.

    @author Srini Ranga
    @version 1.1, 17 Jul 06
 */
public class Connect {
    // NOTE: Change the following variables to suit your setup.
    private static String s_userName = "system";
    private static String s_password = "password";
    
    private static String s_olapSvrName = "localhost";
    /* Possible values for s_provider: 
        "Embedded" or "http://localhost:13080/aps/JAPI" */
    private static String s_provider = "Embedded"; // Default
    private static boolean loadData = false;
    
    private static final int FAILURE_CODE = 1;
    
    public static void connectfunc(String[] args) {
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
            String apiVersion = ess.getApiVersion();
            String apiVerDetail = ess.getApiVersionDetail();
            System.out.println("API Version :"+apiVersion);
            System.out.println("API Version Detail :"+apiVerDetail);
            if (loadData) loadData(olapSvr);
            
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
    
    private static void loadData(IEssOlapServer olapSvr) throws EssException {
    	IEssCube cube = olapSvr.getApplication("Sample").getCube("Basic");
    	cube.loadData(IEssOlapFileObject.TYPE_RULES, null, IEssOlapFileObject.TYPE_TEXT,
    			"Calcdat", true);
    	System.out.println("Loaddata done to Sample/Basic");
    }
    
    static void acceptArgs(String[] args) throws EssException {
        if (args.length >= 4) {
            s_userName = args[0];
            s_password = args[1];   
            s_olapSvrName = args[2];
            s_provider = args[3]; //PROVIDER
            if ((args.length > 4) && (args[4] != null) && args[4].equalsIgnoreCase("loadData")) {
            	loadData = true;
            }
        } else if (args.length != 0) {
            System.err.println("ERROR: Incorrect Usage of this sample.");
            System.err.println(
                "Usage: java " + Connect.class.getName() + " <user> <password> <analytic server> <provider>");
            System.exit(FAILURE_CODE); // Simply end
        }
    }
}
