package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.MetaSets;
import com.pikefin.exceptions.GenericException;

public interface MetaSetsDao {
	public MetaSets saveMetaSetsInfo(MetaSets metaSetsEntity) throws GenericException;
	public MetaSets updateMetaSetsInfo(MetaSets metaSetsEntity) throws GenericException;
	public Boolean deleteMetaSetsInfo(MetaSets metaSetsEntity ) throws GenericException;
	public Boolean deleteMetaSetsInfoById(Integer metaSetsId ) throws GenericException;
	public MetaSets loadMetaSetsInfo(Integer metaSetsId) throws GenericException;
	public List<MetaSets> loadAllMetaSets() throws GenericException;
}
