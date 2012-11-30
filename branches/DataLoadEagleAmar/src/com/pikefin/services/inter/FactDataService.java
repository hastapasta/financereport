package com.pikefin.services.inter;

import java.util.ArrayList;
import java.util.List;

import com.pikefin.businessobjects.Alert;
import com.pikefin.businessobjects.Batches;
import com.pikefin.businessobjects.FactData;
import com.pikefin.businessobjects.Metric;
import com.pikefin.businessobjects.Task;
import com.pikefin.exceptions.GenericException;

public interface FactDataService {

	public void importFactDataInBatch(ArrayList<String[]> tabledata, Batches currentBatch,Metric metric) throws GenericException;
	public List<FactData> loadFactDataByTaskForMaxBatch(Task taskDetail) throws GenericException;
	public FactData loadFactDataForAlerts(Alert alertDetails,Task currentTask) throws GenericException;


}
