package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.ExtractTable;
import com.pikefin.exceptions.GenericException;

public interface ExtractTableDao {
	public ExtractTable saveExtractTableInfo(ExtractTable extractTableEntity) throws GenericException;
	public ExtractTable updateExtractTableInfo(ExtractTable extractTableEntity) throws GenericException;
	public Boolean deleteExtractTableInfo(ExtractTable extractTableEntity ) throws GenericException;
	public Boolean deleteExtractTableInfoById(Integer extractTableId ) throws GenericException;
	public ExtractTable loadExtractTableInfo(Integer extractTableId) throws GenericException;
	public List<ExtractTable> loadAllExtractTables() throws GenericException;
}
