package com.pikefin.services.inter;

import com.pikefin.exceptions.GenericException;
import com.pikefin.services.DataGrabExecutor;

public interface AlertService {
public void checkAlerts(DataGrabExecutor dataGrab) throws GenericException;
}
