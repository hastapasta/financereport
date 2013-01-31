package com.pikefin.services.inter;

import com.pikefin.businessobjects.MetaSets;
import com.pikefin.exceptions.GenericException;

public interface MetaSetService {
	public MetaSets loadMetaSetsInfo(Integer metaSetsId) throws GenericException;

}
