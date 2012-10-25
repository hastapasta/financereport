package com.pikefin.services.inter;

import com.pikefin.exceptions.GenericException;
import com.pikefin.services.DataGrabExecuter;

public interface AlertService {
public void checkAlerts(DataGrabExecuter dataGrab) throws GenericException;
}
