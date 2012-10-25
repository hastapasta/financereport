package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Alert;
import com.pikefin.businessobjects.AlertTarget;
import com.pikefin.exceptions.GenericException;

public interface AlertTargetDao {
	public AlertTarget saveAlertTargetInfo(AlertTarget alertTargetEntity) throws GenericException;
	public AlertTarget updateAlertTargetInfo(AlertTarget alertTargetEntity) throws GenericException;
	public Boolean deleteAlertTargetInfo(AlertTarget alertTargetEntity ) throws GenericException;
	public Boolean deleteAlertTargetInfoById(Integer alertTargetId ) throws GenericException;
	public AlertTarget loadAlertTargetInfo(Integer alertTargetId) throws GenericException;
	public List<AlertTarget> loadAllAlertTargets() throws GenericException;
	public List<AlertTarget> loadAllTargets(Alert alert) throws GenericException;

}
