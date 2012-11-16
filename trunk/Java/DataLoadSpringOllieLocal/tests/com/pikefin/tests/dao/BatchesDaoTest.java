package com.pikefin.tests.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.pikefin.businessobjects.Batches;
import com.pikefin.businessobjects.Groups;
import com.pikefin.businessobjects.Batches;
import com.pikefin.businessobjects.Task;
import com.pikefin.dao.inter.BatchesDao;
import com.pikefin.dao.inter.BatchesDao;
import com.pikefin.dao.inter.TaskDao;
import com.pikefin.exceptions.GenericException;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
@TransactionConfiguration(transactionManager="transactionManager")
public class BatchesDaoTest {
@Autowired
private  TaskDao taskDao;
@Autowired
private  BatchesDao batchesDao;
	Logger log=Logger.getLogger(BatchesDaoTest.class);
	private static Integer BATCH_ID;
	private static Integer INITIAL_COUNT=0;
	private static Integer TASK_ID=1;
	@Test
	public void  testBatchesSave() throws Exception{
		Batches batch=new Batches();
		Task batchTask=taskDao.loadTaskInfo(TASK_ID);
		assertNotNull(batchTask);
		batch.setBatchDateCollected(new Date());
		batch.setCount(INITIAL_COUNT);
		Random rand=new Random();
		Integer random=rand.nextInt();
		batch.setRandomUnique(random);
		batch.setBatchTask(batchTask);
		batch=batchesDao.saveBatchesInfo(batch);
		assertNotNull(batch);
		assertNotNull(batch.getBatchId());
		BATCH_ID=batch.getBatchId();
		assertEquals(random, batch.getRandomUnique());
		assertEquals(new Integer(0), batch.getCount());
		assertNotNull( batch.getBatchTask());
		assertEquals(TASK_ID, batch.getBatchTask().getTaskId());
		log.debug("Batches Added sucessfully with id-"+BATCH_ID);
	}
	
	@Test
	public void  testBatchesLoad() throws GenericException{
	Batches batch =batchesDao.loadBatchesInfo(BATCH_ID);
	assertNotNull(batch);
	assertNotNull(batch.getBatchId());
	assertNotNull(batch.getRandomUnique());
	assertEquals(new Integer(0), batch.getCount());
	assertNotNull(batch.getBatchTask());
	assertEquals(TASK_ID, batch.getBatchTask().getTaskId());
	log.debug("Batches Entity loaded sucessfully with id-"+BATCH_ID);

	}
	
	@Test	
	public void  testBatchesUpdate() throws GenericException{
		Batches batch =batchesDao.loadBatchesInfo(BATCH_ID);
		assertNotNull(batch);
		assertEquals(BATCH_ID, batch.getBatchId());
		assertNotNull(batch.getBatchId());
		Integer count=batch.getCount()+1;
		batch.setCount(count);
		Random random=new Random();
		Integer updatedRandom=random.nextInt();
		batch.setRandomUnique(updatedRandom) ;
		batch=batchesDao.updateBatchesInfo(batch);
		assertNotNull(batch);
		assertEquals(BATCH_ID, batch.getBatchId());
		assertNotNull(batch.getBatchId());
		assertEquals(updatedRandom, batch.getRandomUnique());
		assertEquals(count, batch.getCount());
		assertNotSame(INITIAL_COUNT, batch.getCount());
		log.debug("Batches update executed sucessfully  with id-"+BATCH_ID);
	}
	
	@Test
	public void  testBatchesDelete() throws GenericException{
		Batches batch;
		boolean result =batchesDao.deleteBatchesInfoById(BATCH_ID);
		assertTrue(result);
		batch =batchesDao.loadBatchesInfo(BATCH_ID);
		assertNull(batch);
		log.debug("Batches deleted sucessfully with id-"+BATCH_ID);
	}
}
