package com.pikefin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.Alert;
import com.pikefin.businessobjects.AlertTarget;
import com.pikefin.dao.inter.AlertTargetDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.AlertTargetService;
@Service
public class AlertTargetServiceImpl implements AlertTargetService{
	@Autowired
	private AlertTargetDao alertTargetDao;
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<AlertTarget> loadAllTargets(Alert alert)
			throws GenericException {
		return alertTargetDao.loadAllTargets(alert);
	}

}
