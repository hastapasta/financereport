package com.pikefin.services.inter;

import java.util.List;

import com.pikefin.businessobjects.Entity;
import com.pikefin.businessobjects.EntityGroup;
import com.pikefin.exceptions.GenericException;

public interface EntityGroupService {
	public List<EntityGroup> loadEntityGroupsByEntity(Entity entity) throws GenericException;

}
