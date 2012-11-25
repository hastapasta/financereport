package com.pikefin.services.inter;

import com.pikefin.businessobjects.ExtractSingle;
import com.pikefin.exceptions.GenericException;

public interface ExtractSingleService {
	public ExtractSingle loadExtractSinglesByDataSet(String dataSet) throws GenericException;

}
