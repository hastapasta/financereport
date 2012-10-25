package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Alert;
import com.pikefin.businessobjects.Task;
import com.pikefin.exceptions.GenericException;
/**
 * AlertDao contains the methods related to all Alert operations
 * 
 * @author Amar_Deep_Singh
 *
 */
public interface AlertDao {
	public Alert saveAlertInfo(Alert alertEntity) throws GenericException;
	public Alert updateAlertInfo(Alert alertEntity) throws GenericException;
	public Boolean deleteAlertInfo(Alert alertEntity ) throws GenericException;
	public Boolean deleteAlertInfoById(Integer alertId ) throws GenericException;
	public Alert loadAlertInfo(Integer alertId) throws GenericException;
	public List<Alert> loadAllAlerts() throws GenericException;
	public List<Alert> loadAllAlertsByTask(Task currentTask) throws GenericException;


}
