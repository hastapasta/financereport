package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Column;
import com.pikefin.exceptions.GenericException;

public interface ColumnDao {
	public Column saveColumnInfo(Column columnEntity) throws GenericException;
	public Column updateColumnInfo(Column columnEntity) throws GenericException;
	public Boolean deleteColumnInfo(Column columnEntity ) throws GenericException;
	public Boolean deleteColumnInfoById(Integer columnId ) throws GenericException;
	public Column loadColumnInfo(Integer columnId) throws GenericException;
	public List<Column> loadAllColumns() throws GenericException;

}
