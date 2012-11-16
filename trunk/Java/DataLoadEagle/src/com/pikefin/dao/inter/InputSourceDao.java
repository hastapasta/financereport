package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.InputSource;
import com.pikefin.exceptions.GenericException;

public interface InputSourceDao {
	public InputSource saveInputSourceInfo(InputSource inputSourceEntity) throws GenericException;
	public InputSource updateInputSourceInfo(InputSource inputSourceEntity) throws GenericException;
	public Boolean deleteInputSourceInfo(InputSource inputSourceEntity ) throws GenericException;
	public Boolean deleteInputSourceInfoById(Integer inputSourceId ) throws GenericException;
	public InputSource loadInputSourceInfo(Integer inputSourceId) throws GenericException;
	public List<InputSource> loadAllInputSources() throws GenericException;
}
