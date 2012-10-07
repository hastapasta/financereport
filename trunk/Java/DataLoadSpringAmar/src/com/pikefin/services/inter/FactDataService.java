package com.pikefin.services.inter;

import java.util.ArrayList;

import com.pikefin.businessobjects.Batches;
import com.pikefin.businessobjects.Metric;
import com.pikefin.exceptions.GenericException;

public interface FactDataService {

	public void importFactDataInBatch(ArrayList<String[]> tabledata, Batches currentBatch,Metric metric) throws GenericException;

}
