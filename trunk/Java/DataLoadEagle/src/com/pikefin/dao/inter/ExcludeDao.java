package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Exclude;
import com.pikefin.exceptions.GenericException;

public interface ExcludeDao {
	public Exclude saveExcludeInfo(Exclude excludeEntity) throws GenericException;
	public Exclude updateExcludeInfo(Exclude excludeEntity) throws GenericException;
	public Boolean deleteExcludeInfo(Exclude excludeEntity ) throws GenericException;
	public Boolean deleteExcludeInfoById(Integer excludeId ) throws GenericException;
	public Exclude loadExcludeInfo(Integer excludeId) throws GenericException;
	public List<Exclude> loadAllExcludes() throws GenericException;
	public List<Exclude> loadAllExcludesByTaskId(Integer taskId) throws GenericException;
}
