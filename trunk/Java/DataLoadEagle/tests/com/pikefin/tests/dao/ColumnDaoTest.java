package com.pikefin.tests.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.pikefin.businessobjects.Column;
import com.pikefin.businessobjects.Country;
import com.pikefin.businessobjects.ExtractTable;
import com.pikefin.dao.inter.ColumnDao;
import com.pikefin.dao.inter.CountryDao;
import com.pikefin.dao.inter.ExtractTableDao;
import com.pikefin.exceptions.GenericException;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
//@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class ColumnDaoTest {
@Autowired
private  ColumnDao columnDao;
@Autowired
private ExtractTableDao extractTableDao;
	Logger log=Logger.getLogger(CountryDaoTest.class);
	@Before
	public  void setUp(){
		try{
			ex=extractTableDao.loadExtractTableInfo(EXTRACT_TABLE_ID);
		}catch (GenericException e) {
			log.debug("Could not load the extract Table Data "+e.getErrorMessage()+"-" +e.getErrorDescription());		
			}
		
	}
	private static String COLUMN_AFTER_CODE="afterCode";
	private static String COLUMN_AFTER_CODE_UPDATED="afterCode";
	private static String COLUMN_BEFORE_CODE="beforeCOde";
	private static Integer COLUMN_COUNT=1;
	private static Integer COLUMN_COUNT_UPDATED=2;
	private static Integer EXTRACT_TABLE_ID=100001;
	private static Integer COLUMN_POSITION=1; 

	ExtractTable ex;
	private static Integer COLUMN_ID;
	@Test
	public void  testColumnSave() throws Exception{
		Column column=new Column();
		column.setAftCode(COLUMN_AFTER_CODE);
		column.setBefCode(COLUMN_BEFORE_CODE);
		column.setColCount(COLUMN_COUNT);
		column.setColPosition(COLUMN_POSITION);
		column.setExtractTable(ex);
		column=columnDao.saveColumnInfo(column);
		assertNotNull(column);
		assertNotNull(column.getColumnId());
		assertEquals(COLUMN_AFTER_CODE, column.getAftCode());
		assertEquals(COLUMN_BEFORE_CODE, column.getBefCode());
		assertEquals(COLUMN_COUNT, column.getColCount());
		assertEquals(COLUMN_POSITION, column.getColPosition());
		assertNotNull(column.getExtractTable());
		assertEquals(EXTRACT_TABLE_ID, column.getExtractTable().getExtractTableId());
		COLUMN_ID=column.getColumnId();
		log.debug("Group entity saved sucessfully with id-"+COLUMN_ID);
	}
	@Test
	public void  testColumnLoad() throws GenericException{
	Column	column =columnDao.loadColumnInfo(COLUMN_ID);
	assertNotNull(column);
	assertEquals(COLUMN_ID, column.getColumnId());
	assertNotNull(column);
	assertNotNull(column.getColumnId());
	assertEquals(COLUMN_AFTER_CODE, column.getAftCode());
	assertEquals(COLUMN_BEFORE_CODE, column.getBefCode());
	assertEquals(COLUMN_COUNT, column.getColCount());
	assertEquals(COLUMN_POSITION, column.getColPosition());
	assertNotNull(column.getExtractTable());
	assertEquals(EXTRACT_TABLE_ID, column.getExtractTable().getExtractTableId());
	log.debug("Country loaded sucessfully with id-"+COLUMN_ID);

	}
	
	@Test	
	public void  testColumnUpdate() throws GenericException{
		Column	column =columnDao.loadColumnInfo(COLUMN_ID);
		assertNotNull(column);
		assertNotNull(column.getColumnId());
		assertEquals(COLUMN_AFTER_CODE, column.getAftCode());
		assertEquals(COLUMN_BEFORE_CODE, column.getBefCode());
		assertEquals(COLUMN_COUNT, column.getColCount());
		assertEquals(COLUMN_POSITION, column.getColPosition());
		assertNotNull(column.getExtractTable());
		assertEquals(EXTRACT_TABLE_ID, column.getExtractTable().getExtractTableId());
		column.setAftCode(COLUMN_AFTER_CODE_UPDATED);
		column.setColCount(COLUMN_COUNT_UPDATED);
		
		column=columnDao.updateColumnInfo(column);
		assertNotNull(column);
		assertEquals(COLUMN_ID,column.getColumnId());
		assertNotSame(COLUMN_AFTER_CODE, column.getAftCode());
		assertEquals(COLUMN_AFTER_CODE_UPDATED, column.getAftCode());
		assertEquals(COLUMN_BEFORE_CODE, column.getBefCode());
		assertNotSame(COLUMN_COUNT, column.getColCount());
		assertEquals(COLUMN_COUNT_UPDATED, column.getColCount());
		assertEquals(COLUMN_POSITION, column.getColPosition());
		assertNotNull(column.getExtractTable());
		assertEquals(EXTRACT_TABLE_ID, column.getExtractTable().getExtractTableId());
		log.debug("Country update executed sucessfully  with id-"+COLUMN_ID);
	}
	
	public void  testLoadAllColumns() throws GenericException{
		List<Column>	columns =columnDao.loadAllColumns();
		assertNotNull(columns);
		Column	column =columnDao.loadColumnInfo(COLUMN_ID);
		assertNotNull(column);
		assertNotSame(0, columns.size());
		for(Column temp:columns){
			assertNotNull(columns);
			assertTrue(columns.contains(column));
		}
		log.debug("loadAllColumns method tested sucessfully");
	}
	
	@Test
	public void  testCountryDelete() throws GenericException{
		Column	column =columnDao.loadColumnInfo(COLUMN_ID);
		assertNotNull(column);
		
		boolean result =columnDao.deleteColumnInfo(column);
		assertTrue(result);
		column =columnDao.loadColumnInfo(COLUMN_ID);
		assertNull(column);
		log.debug("Country deleted sucessfully with id-"+COLUMN_ID);
	}
}
