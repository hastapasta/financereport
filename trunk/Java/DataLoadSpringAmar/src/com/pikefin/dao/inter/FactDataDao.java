package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.FactData;
import com.pikefin.exceptions.GenericException;

public interface FactDataDao {
	public FactData saveFactDataInfo(FactData factDataEntity) throws GenericException;
	public FactData updateFactDataInfo(FactData factDataEntity) throws GenericException;
	public Boolean deleteFactDataInfo(FactData factDataEntity ) throws GenericException;
	public Boolean deleteFactDataInfoById(Integer factDataId ) throws GenericException;
	public FactData loadFactDataInfo(Integer factDataId) throws GenericException;
	public List<FactData> loadAllFactDatas() throws GenericException;
}
