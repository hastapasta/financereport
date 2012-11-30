package com.pikefin.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.ExtractSingle;
import com.pikefin.dao.inter.ExtractSingleDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.ExtractSingleService;
@Service
public class ExtractSingleServiceImpl implements ExtractSingleService {
	@Autowired
	private ExtractSingleDao extractDao;
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ExtractSingle loadExtractSinglesByDataSet(String dataSet)
			throws GenericException {
			return extractDao.loadExtractSinglesByDataSet(dataSet);
	}

}
