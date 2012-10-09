package com.pikefin.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pikefin.businessobjects.Entity;
import com.pikefin.businessobjects.EntityGroup;
import com.pikefin.dao.inter.EntityDao;
import com.pikefin.dao.inter.EntityGroupDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.EntityService;

@Service
public class EntityServiceImpl implements EntityService{
	@Autowired
	private EntityDao entityDao;
	@Autowired
	private EntityGroupDao entityGroupDao;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Entity> loadAllEntitiesForGroupId(Integer entityGroupId)
			throws GenericException {
	EntityGroup entityGroup=entityGroupDao.loadEntityGroupInfo(entityGroupId);
	if(entityGroup!=null){
		return new ArrayList<Entity>(entityGroup.getEntities()) ;	
	}
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Entity loadEntityInfo(Integer entityId) throws GenericException {
		
		return entityDao.loadEntityInfo(entityId);
	}

}