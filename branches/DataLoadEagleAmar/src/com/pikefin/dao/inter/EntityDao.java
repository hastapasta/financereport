package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Entity;
import com.pikefin.exceptions.GenericException;

public interface EntityDao {
	public Entity saveEntityInfo(Entity entityEntity) throws GenericException;
	public Entity updateEntityInfo(Entity entityEntity) throws GenericException;
	public Boolean deleteEntityInfo(Entity entityEntity ) throws GenericException;
	public Boolean deleteEntityInfoById(Integer entityId ) throws GenericException;
	public Entity loadEntityInfo(Integer entityId) throws GenericException;
	public List<Entity> loadAllEntities() throws GenericException;
	public List<Entity> loadEntitiesByTicker(String ticker) throws GenericException;
	public Entity loadEntityByTicker(String ticker) throws GenericException;
	public Entity loadEntityByTickerAndCountry(String ticker, String country) throws GenericException;
}
