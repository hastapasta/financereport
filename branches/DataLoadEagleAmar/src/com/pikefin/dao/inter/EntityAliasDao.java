package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.EntityAlias;
import com.pikefin.exceptions.GenericException;

public interface EntityAliasDao {
	public EntityAlias saveEntityAliasInfo(EntityAlias entityAliasEntity) throws GenericException;
	public EntityAlias updateEntityAliasInfo(EntityAlias entityAliasEntity) throws GenericException;
	public Boolean deleteEntityAliasInfo(EntityAlias entityAliasEntity ) throws GenericException;
	public Boolean deleteEntityAliasInfoById(Integer entityAliasId ) throws GenericException;
	public EntityAlias loadEntityAliasInfo(Integer entityAliasId) throws GenericException;
	public List<EntityAlias> loadAllEntityAlias() throws GenericException;
}
