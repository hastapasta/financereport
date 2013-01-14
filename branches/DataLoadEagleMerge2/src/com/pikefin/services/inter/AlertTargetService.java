package com.pikefin.services.inter;

import java.util.List;

import com.pikefin.businessobjects.Alert;
import com.pikefin.businessobjects.AlertTarget;
import com.pikefin.exceptions.GenericException;

public interface AlertTargetService {
	public List<AlertTarget> loadAllTargets(Alert alert) throws GenericException;

}
