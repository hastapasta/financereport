package com.pikefin.services.inter;

import com.pikefin.businessobjects.Job;
import com.pikefin.exceptions.GenericException;

/*import java.util.ArrayList;
import com.pikefin.businessobjects.Job;
import com.pikefin.exceptions.CustomEmptyStringException;
import com.pikefin.exceptions.GenericException;
import com.pikefin.exceptions.SkipLoadException;*/


public interface JobService {
	/*public boolean preJobProcessing(Job j);
	public boolean postJobProcessing(Job j)	;
	public boolean preProcessing(Job j, String strTicker);
	public void postProcessExchRate();
	public boolean preNoDataCheck(String strDataSet) ;
	public void preJobProcessTableXrateorg() throws GenericException;
	public ArrayList<String []> postProcessing(ArrayList<String []> tabledata , Job inputJob) throws SkipLoadException,CustomEmptyStringException;*/
	public Job getJobByDataSet(String dataSet) throws GenericException;
}
