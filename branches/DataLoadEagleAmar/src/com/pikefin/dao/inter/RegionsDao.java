package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Regions;
import com.pikefin.exceptions.GenericException;

public interface RegionsDao {
	public Regions saveRegionsInfo(Regions regionsEntity) throws GenericException;
	public Regions updateRegionsInfo(Regions regionsEntity) throws GenericException;
	public Boolean deleteRegionsInfo(Regions regionsEntity ) throws GenericException;
	public Boolean deleteRegionsInfoById(Integer regionId ) throws GenericException;
	public Regions loadRegionsInfo(Integer regionId) throws GenericException;
	public List<Regions> loadAllRegionss() throws GenericException;
}
