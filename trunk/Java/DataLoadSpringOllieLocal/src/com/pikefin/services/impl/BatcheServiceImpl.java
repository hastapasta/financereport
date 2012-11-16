package com.pikefin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.businessobjects.Batches;
import com.pikefin.dao.inter.BatchesDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.BatcheService;
@Service
public class BatcheServiceImpl implements BatcheService{
	@Autowired
	private BatchesDao batchDao;
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Batches saveBatchesInfo(Batches batchesEntity)
			throws GenericException {
		
		return batchDao.saveBatchesInfo(batchesEntity);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Batches updateBatchesInfo(Batches batchesEntity)
			throws GenericException {
		return batchDao.updateBatchesInfo(batchesEntity);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Boolean deleteBatchesInfo(Batches batchesEntity)
			throws GenericException {
		return batchDao.deleteBatchesInfo(batchesEntity);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Boolean deleteBatchesInfoById(Integer batchesId)
			throws GenericException {
		
		return batchDao.deleteBatchesInfoById(batchesId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Batches loadBatchesInfo(Integer batchesId) throws GenericException {
	
		return batchDao.loadBatchesInfo(batchesId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<Batches> loadAllBatchess() throws GenericException {
			return batchDao.loadAllBatchess();
	}

}
