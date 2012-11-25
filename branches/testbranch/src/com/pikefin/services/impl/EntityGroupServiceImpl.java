package com.pikefin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.Entity;
import com.pikefin.businessobjects.EntityGroup;
import com.pikefin.dao.inter.EntityGroupDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.EntityGroupService;
@Service
public class EntityGroupServiceImpl implements EntityGroupService{
	@Autowired
	private EntityGroupDao entityDao;
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<EntityGroup> loadEntityGroupsByEntity(Entity entity)
			throws GenericException {
		
		return entityDao.loadEntityGroupsByEntity(entity);
	}

}
