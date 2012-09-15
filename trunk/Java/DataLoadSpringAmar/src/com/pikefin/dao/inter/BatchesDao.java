package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Batches;
import com.pikefin.exceptions.GenericException;
/**
 * BatchesDao contains the methods related to all Batches operations
 * 
 * @author Amar_Deep_Singh
 *
 */
public interface BatchesDao {
	public Batches saveBatchesInfo(Batches BatchesEntity) throws GenericException;
	public Batches updateBatchesInfo(Batches BatchesEntity) throws GenericException;
	public Boolean deleteBatchesInfo(Batches BatchesEntity ) throws GenericException;
	public Boolean deleteBatchesInfoById(Integer BatchesId ) throws GenericException;
	public Batches loadBatchesInfo(Integer BatchesId) throws GenericException;
	public List<Batches> loadAllBatchess() throws GenericException;
	
}
