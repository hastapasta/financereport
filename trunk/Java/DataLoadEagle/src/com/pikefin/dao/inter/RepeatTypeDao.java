package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.RepeatTypeEnum;
import com.pikefin.businessobjects.RepeatType;
import com.pikefin.exceptions.GenericException;

public interface RepeatTypeDao {
	public RepeatType saveRepeatTypeInfo(RepeatType repeatTypeEntity) throws GenericException;
	public RepeatType updateRepeatTypeInfo(RepeatType repeatTypeEntity) throws GenericException;
	public List<RepeatType> updateRepeatTypeBatch(List<RepeatType> batchEntities) throws GenericException;
	public Boolean deleteRepeatTypeInfo(RepeatType repeatTypeEntity ) throws GenericException;
	public Boolean deleteRepeatTypeInfoById(Integer repeatTypeId ) throws GenericException;
	public RepeatType loadRepeatTypeInfo(Integer repeatTypeId) throws GenericException;
	public List<RepeatType> loadAllRepeatTypes() throws GenericException;
	public List<RepeatType> loadAllActiveRepeatTypes() throws GenericException;
	public List<RepeatType> loadRepeatTypesByType(RepeatTypeEnum repeatType) throws GenericException;

}
