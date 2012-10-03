package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Splits;
import com.pikefin.exceptions.GenericException;

public interface SplitsDao {
	public Splits saveSplitsInfo(Splits splitEntity) throws GenericException;
	public Splits updateSplitsInfo(Splits splitEntity) throws GenericException;
	public Boolean deleteSplitsInfo(Splits splitEntity ) throws GenericException;
	public Boolean deleteSplitsInfoById(Integer splitsId ) throws GenericException;
	public Splits loadSplitsInfo(Integer splitsId) throws GenericException;
	public List<Splits> loadAllSplits() throws GenericException;
}
