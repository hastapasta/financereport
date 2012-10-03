package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.ExtractSingle;
import com.pikefin.exceptions.GenericException;

public interface ExtractSingleDao {
	public ExtractSingle saveExtractSingleInfo(ExtractSingle extractSingle) throws GenericException;
	public ExtractSingle updateExtractSingleInfo(ExtractSingle extractSingle) throws GenericException;
	public Boolean deleteExtractSingleInfo(ExtractSingle extractSingle ) throws GenericException;
	public Boolean deleteExtractSingleInfoById(Integer extractSingleId ) throws GenericException;
	public ExtractSingle loadExtractSingleInfo(Integer extractSingleId) throws GenericException;
	public List<ExtractSingle> loadAllExtractSingles() throws GenericException;
}
