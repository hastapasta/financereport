package com.pikefin.services;
 
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import pikefin.log4jWrapper.Logs;
import com.pikefin.ApplicationSetting;
import com.pikefin.businessobjects.Job;
import com.pikefin.exceptions.CustomEmptyStringException;
import com.pikefin.exceptions.SkipLoadException;
import com.pikefin.services.inter.JobService;

//@Service
public class ProcessingFunctions {
	//@Autowired
	private JobService jobService=ApplicationSetting.getInstance().getApplicationContext().getBean(JobService.class);
	String strDataValue;

	/*RIght now this is only set during the preProcessing function */
	String strTicker;
	String strTemp1, strTemp2, strTemp3;
	
	/*table extraction processing function parameters*/
	ArrayList<String[]> propTableData;
	Job j;
	
	
	DataGrabExecuter dg;
	
	//CustomBufferedWriter stdoutwriter;
	
	public ProcessingFunctions(DataGrabExecuter tmpDG) {
		//this.uf = tmpUF;
		this.dg = tmpDG;
	}
	
	/*public ProcessingFunctions() {
		
	}*/
	
	public boolean preNoDataCheck(String dataSet) {
		try	{
			Job job=jobService.getJobByDataSet(dataSet);
			String strFunctionName =job.getPreNoDataCheckFunc();
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Pre No Data Check function name: " + strFunctionName,Logs.STATUS2,"PF1");
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
			{
				ApplicationSetting.getInstance().getStdoutwriter().writeln("No data check function, assuming data is ok",Logs.STATUS2,"PF2");
				return(false);
			}
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception e)
		{
			ApplicationSetting.getInstance().getStdoutwriter().writeln("preNoDataCheck method call failed",Logs.ERROR,"PF3");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(e);
			return(false);
			
		}
		
	}
	
	public boolean preJobProcessing(Job j)	{
		try	{
			String strFunctionName = j.getPreJobProcessFuncName();
			
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Pre Job Process Func Name: " + strFunctionName,Logs.STATUS2,"PF3.2");
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
				ApplicationSetting.getInstance().getStdoutwriter().writeln("No pre job process function, exiting...",Logs.STATUS2,"PF3.3");
				return(true);
			}
			ApplicationSetting.getInstance().getStdoutwriter().writeln(strFunctionName,Logs.STATUS2,"PF3.35");
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception tmpE)
		{
			ApplicationSetting.getInstance().getStdoutwriter().writeln("pre Job Processing method call failed",Logs.ERROR,"PF3.4");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(tmpE);
			return(false);
		}
	}
	
	public boolean postJobProcessing(Job j)	{
		try	{
			String strFunctionName = j.getPostJobProcessFuncName();
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Post Job Process Func Name: " + strFunctionName,Logs.STATUS2,"PF3.5");
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
			{
				ApplicationSetting.getInstance().getStdoutwriter().writeln("No post job process function, exiting...",Logs.STATUS2,"PF3.7");
				return(true);
			}
			ApplicationSetting.getInstance().getStdoutwriter().writeln(strFunctionName,Logs.STATUS2,"PF3.8");
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception tmpE)
		{
			ApplicationSetting.getInstance().getStdoutwriter().writeln("post Job Processing method call failed",Logs.ERROR,"PF3.9");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(tmpE);
			return(false);
		}
	}
	

	public boolean preProcessing(Job j, String strTicker) {
	 	try	{
			String strFunctionName = j.getPreProcessFuncName();
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Pre Process Func Name: " + strFunctionName,Logs.STATUS2,"PF4");
			this.strTicker = strTicker;
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0))
			{
				ApplicationSetting.getInstance().getStdoutwriter().writeln("No pre process function, exiting...",Logs.STATUS2,"PF5");
				return(true);
			}
			strDataValue = "";
			strTemp1 = "";
			strTemp2 = "";
			strTemp3 = "";
			ApplicationSetting.getInstance().getStdoutwriter().writeln(strFunctionName,Logs.STATUS2,"PF6");
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			return((Boolean)m.invoke(this, new Object[] {}));
		}
		catch (Exception tmpE)
		{
			ApplicationSetting.getInstance().getStdoutwriter().writeln("preProcessing method call failed",Logs.ERROR,"PF7");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(tmpE);
			return(false);
		}

}


public ArrayList<String []> postProcessing(ArrayList<String []> tabledata , Job inputJob) throws SkipLoadException,CustomEmptyStringException {
		if (tabledata.get(0) != null)
			strDataValue = tabledata.get(0)[0];
		try	{
			String strFunctionName = inputJob.getPostProcessFuncName();
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Post Process Func Name: " + strFunctionName,Logs.STATUS2,"PF8");
			if ((strFunctionName == null) || (strFunctionName.compareTo("") == 0)) {
				ApplicationSetting.getInstance().getStdoutwriter().writeln("No post process function, exiting...",Logs.STATUS2,"PF9");
				return(tabledata);
			}
			propTableData = tabledata;
			j = inputJob;
			Method m = this.getClass().getMethod(strFunctionName,new Class[] {});
			m.invoke(this, new Object[] {});
		}
		/* Need to break this down into individual exceptions */
		catch (IllegalAccessException iae)
		{
			ApplicationSetting.getInstance().getStdoutwriter().writeln("postProcessing method call failed",Logs.ERROR,"PF16.1");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(iae);
		
		}
		//any exceptions thrown by the function pointer are wrapped in an InvocationTargetException
		catch (InvocationTargetException ite)
		{
			if (ite.getTargetException().getClass().getSimpleName().equals("SkipLoadException"))
				throw new SkipLoadException();
			else if (ite.getTargetException().getClass().getSimpleName().equals("CustomEmptyStringException")) {
				/*
				 * To do: pass along the exception message.
				 */
				throw new CustomEmptyStringException();
			}
			else{ 
				ApplicationSetting.getInstance().getStdoutwriter().writeln("postProcessing method call failed",Logs.ERROR,"PF16.3");
				if (ite.getCause() != null) {
					ApplicationSetting.getInstance().getStdoutwriter().writeln((Exception)ite.getCause());
				}

			}
		
		}
		catch (NoSuchMethodException nsme) {
			ApplicationSetting.getInstance().getStdoutwriter().writeln("postProcessing method call failed",Logs.ERROR,"PF16.5");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(nsme);
		}
		catch (DataAccessException tmpE) {
			ApplicationSetting.getInstance().getStdoutwriter().writeln("postProcessing method call failed",Logs.ERROR,"PF17");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(tmpE);
		}
		
		return(propTableData);
	



}

public void preJobProcessTableXrateorg() throws DataAccessException
{
	
}



public void postProcessExchRate()
{
	String[] newrow;
	String[] tmpArray = {"data_set","value","date_collected"};
	
	ArrayList<String[]> newTableData = new ArrayList<String[]>();
	
	newTableData.add(tmpArray);
	
	newrow = new String[tmpArray.length];
	
	newrow[0] = dg.strCurDataSet;
	newrow[1] = this.propTableData.get(0)[0];
	newrow[2] = "NOW()";
	//newrow[3] = "forex";
	
	newTableData.add(newrow);
	
	propTableData = newTableData;

	

}
	
	
}







