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
import com.pikefin.businessobjects.Country;
import com.pikefin.dao.inter.CountryDao;
import com.pikefin.exceptions.GenericException;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
//@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class CountryDaoTest {
@Autowired
private  CountryDao countryDao;
	Logger log=Logger.getLogger(CountryDaoTest.class);
	@Before
	public  void setUp(){
	}
	private static String COUNTRY_NAME="testCountry";
	private static String COUNTRY_NAME_SHORT="shortName";
	private static String COUNTRY_NAME_SHORT_UPDATED="shortNameUpdated";

	private static String COUNTRY_NAME_UPDATED="testCountryUpdated";
	private static String COUNTRY_HASH="#hash";
	private static Integer COUNTRY_ID;
	@Test
	public void  testCountrySave() throws Exception{
		Country country=new Country();
		country.setName(COUNTRY_NAME);
		country.setShortName(COUNTRY_NAME_SHORT);
		country.setHash(COUNTRY_HASH);
		country=countryDao.saveCountryInfo(country);
		assertNotNull(country);
		assertNotNull(country.getCountryId());
		assertEquals(COUNTRY_NAME, country.getName());
		assertEquals(COUNTRY_HASH, country.getHash());
		assertEquals(COUNTRY_NAME_SHORT, country.getShortName());
		COUNTRY_ID=country.getCountryId();
		log.debug("Group entity saved sucessfully with id-"+COUNTRY_ID);
	}
	@Test
	public void  testCountryLoad() throws GenericException{
	Country	country =countryDao.loadCountryInfo(COUNTRY_ID);
	assertNotNull(country);
	assertEquals(COUNTRY_ID, country.getCountryId());
	assertEquals(COUNTRY_NAME, country.getName());
	assertEquals(COUNTRY_HASH, country.getHash());
	assertEquals(COUNTRY_NAME_SHORT, country.getShortName());
	log.debug("Country loaded sucessfully with id-"+COUNTRY_ID);

	}
	
	@Test	
	public void  testCountryUpdate() throws GenericException{
		Country	country =countryDao.loadCountryInfo(COUNTRY_ID);
		assertNotNull(country);
		assertEquals(COUNTRY_ID, country.getCountryId());
		assertEquals(COUNTRY_NAME, country.getName());
		assertEquals(COUNTRY_HASH, country.getHash());
		assertEquals(COUNTRY_NAME_SHORT, country.getShortName());
		country.setName(COUNTRY_NAME_UPDATED);
		country.setShortName(COUNTRY_NAME_SHORT_UPDATED);
		country=countryDao.updateCountryInfo(country);
		assertNotNull(country);
		assertNotSame(COUNTRY_NAME, country.getName());
		assertEquals(COUNTRY_NAME_UPDATED, country.getName());
		assertEquals(COUNTRY_ID, country.getCountryId());
		assertNotSame(COUNTRY_NAME_SHORT, country.getShortName());
		assertEquals(COUNTRY_NAME_SHORT_UPDATED, country.getShortName());
		assertEquals(COUNTRY_HASH, country.getHash());
		log.debug("Country update executed sucessfully  with id-"+COUNTRY_ID);
	}
	
	public void  testLoadAllCountries() throws GenericException{
		List<Country>	countries =countryDao.loadAllCountries();
		assertNotNull(countries);
		assertNotSame(0, countries.size());
		for(Country temp:countries){
			assertNotNull(temp);
		}
		log.debug("Country update executed sucessfully  with id-"+COUNTRY_ID);
	}
	
	@Test
	public void  testCountryDelete() throws GenericException{
		Country	country =countryDao.loadCountryInfo(COUNTRY_ID);
		assertNotNull(country);
		assertNotSame(COUNTRY_NAME, country.getName());
		assertEquals(COUNTRY_NAME_UPDATED, country.getName());
		assertEquals(COUNTRY_ID, country.getCountryId());
		assertNotSame(COUNTRY_NAME_SHORT, country.getShortName());
		boolean result =countryDao.deleteCountryInfo(country);
		assertTrue(result);
		country =countryDao.loadCountryInfo(COUNTRY_ID);
		assertNull(country);
		log.debug("Country deleted sucessfully with id-"+COUNTRY_ID);
	}
}
