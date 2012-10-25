package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.EntityGroup;
import com.pikefin.exceptions.GenericException;
import com.pikefin.businessobjects.Entity;
public interface EntityGroupDao {
	public EntityGroup saveEntityGroupInfo(EntityGroup entityGroupEntity) throws GenericException;
	public EntityGroup updateEntityGroupInfo(EntityGroup entityGroupEntity) throws GenericException;
	public Boolean deleteEntityGroupInfo(EntityGroup entityGroupEntity ) throws GenericException;
	public Boolean deleteEntityGroupInfoById(Integer entityGroupId ) throws GenericException;
	public EntityGroup loadEntityGroupInfo(Integer entityGroupId) throws GenericException;
	public List<EntityGroup> loadAllEntityGroups() throws GenericException;
	public List<EntityGroup> loadEntityGroupsByEntity(Entity entity) throws GenericException;

}
