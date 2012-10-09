package com.pikefin.services.impl;

import org.springframework.stereotype.Service;

import com.pikefin.businessobjects.ExtractSingle;
import com.pikefin.dao.inter.ExtractSingleDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.ExtractSingleService;
@Service
public class ExtractSingleServiceImpl implements ExtractSingleService {

	private ExtractSingleDao extractDao;
	@Override
	public ExtractSingle loadExtractSinglesByDataSet(String dataSet)
			throws GenericException {
		
		return extractDao.loadExtractSinglesByDataSet(dataSet);
	}

}
