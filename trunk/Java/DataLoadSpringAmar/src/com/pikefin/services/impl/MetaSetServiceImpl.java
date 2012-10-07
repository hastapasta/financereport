package com.pikefin.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.pikefin.businessobjects.MetaSets;
import com.pikefin.dao.inter.MetaSetsDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.MetaSetService;

public class MetaSetServiceImpl implements MetaSetService{

	@Autowired
	private MetaSetsDao metaSetDao;
	@Override
	public MetaSets loadMetaSetsInfo(Integer metaSetsId)
			throws GenericException {
		return metaSetDao.loadMetaSetsInfo(metaSetsId);
	}

}
