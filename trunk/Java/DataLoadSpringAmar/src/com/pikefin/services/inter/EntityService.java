package com.pikefin.services.inter;

import java.util.List;

import com.pikefin.businessobjects.Entity;
import com.pikefin.exceptions.GenericException;

public interface EntityService {
	public List<Entity> loadAllEntitiesForGroupId(Integer entitygroupId) throws GenericException;
	public Entity loadEntityInfo(Integer entityId) throws GenericException;

}
