package com.pikefin.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.MetaSets;
import com.pikefin.dao.inter.MetaSetsDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.MetaSetService;
@Service
public class MetaSetServiceImpl implements MetaSetService{

	@Autowired
	private MetaSetsDao metaSetDao;
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public MetaSets loadMetaSetsInfo(Integer metaSetsId)
			throws GenericException {
		return metaSetDao.loadMetaSetsInfo(metaSetsId);
	}

}
