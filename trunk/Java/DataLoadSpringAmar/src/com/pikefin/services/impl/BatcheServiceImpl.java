package com.pikefin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikefin.businessobjects.Batches;
import com.pikefin.dao.inter.BatchesDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.BatcheService;
@Service
public class BatcheServiceImpl implements BatcheService{
	@Autowired
	private BatchesDao batchDao;
	@Override
	public Batches saveBatchesInfo(Batches batchesEntity)
			throws GenericException {
		
		return batchDao.saveBatchesInfo(batchesEntity);
	}

	@Override
	public Batches updateBatchesInfo(Batches batchesEntity)
			throws GenericException {
		return batchDao.updateBatchesInfo(batchesEntity);
	}

	@Override
	public Boolean deleteBatchesInfo(Batches batchesEntity)
			throws GenericException {
		return batchDao.deleteBatchesInfo(batchesEntity);
	}

	@Override
	public Boolean deleteBatchesInfoById(Integer batchesId)
			throws GenericException {
		
		return batchDao.deleteBatchesInfoById(batchesId);
	}

	@Override
	public Batches loadBatchesInfo(Integer batchesId) throws GenericException {
	
		return batchDao.loadBatchesInfo(batchesId);
	}

	@Override
	public List<Batches> loadAllBatchess() throws GenericException {
		
		return batchDao.loadAllBatchess();
	}

}
