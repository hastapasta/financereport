package com.pikefin.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.Country;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.CountryDao;
import com.pikefin.exceptions.GenericException;

public class CountryDaoImpl extends AbstractDao<Country> implements CountryDao {


	private SessionFactory sessionFactory;
	public CountryDaoImpl(SessionFactory sessionFactory){
		super(Country.class);
		this.sessionFactory=sessionFactory;
	}
	@Override
	/**
	 * Saves the Country information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the new instance of the Country entity
	 * @returns returns the persisted Country instance
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Country saveCountryInfo(Country countryEntity)
			throws GenericException {
		try{
			countryEntity=super.save(countryEntity);
			
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_SAVE_COUNTRY_DATA,e.getMessage(),e.getCause());
		}
		return countryEntity;
	}
	
	/**
	 * Updates the Country information into the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Country entity
	 * @returns returns the persisted Country instance
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Country updateCountryInfo(Country countryEntity)
			throws GenericException {
		try{
			countryEntity=super.update(countryEntity);
			
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_UPDATE_COUNTRY_DATA,e.getMessage(),e.getCause());
		}
		return countryEntity;
	}

	@Override
	/**
	 * Deleted the Country information from the database
	 * @author Amar_Deep_Singh
	 * @param takes the detached Country entity
	 * @returns returns true if the Country is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteCountryInfo(Country countryEntity)
			throws GenericException {
		try{
			return super.delete(countryEntity);
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_DELETE_COUNTRY_INFORMATION,e.getMessage(),e.getCause());
		}
	}

	@Override
	/**
	 * Deleted the Country information from the database based on supplied CountryID. It first loads the Country then try to delete
	 * @author Amar_Deep_Singh
	 * @param takes the CountryId of the Country entity
	 * @returns returns true if the Country is deleted else return false
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean deleteCountryInfoById(Integer countryId)
			throws GenericException {
		Country groupEntity=loadCountryInfo(countryId);
		try{
			super.delete(groupEntity);
			
		}catch (Exception e) {
		throw new GenericException(ErrorCode.COULD_NOT_DELETE_COUNTRY_INFORMATION,e.getMessage(),e.getCause());
		}
		return false;
	}

	@Override
	/**
	 * Retrieve the Country information from the database based on supplied CountryID.
	 * @author Amar_Deep_Singh
	 * @param takes the CountryId of the Country entity
	 * @returns returns Country entity if the Country is found else throws exception
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Country loadCountryInfo(Integer countryId) throws GenericException {
		Country countryEntity;
		try{
			countryEntity=super.find(countryId);
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_COUNTRY_DATA_WITH_ID,e.getMessage(),e.getCause());
		}
		return countryEntity;
	}

	@Override
	/**
	 * Retrieve the list of all Countries entities from the database.
	 * @author Amar_Deep_Singh
	 * @returns returns List<Country> 
	 * @throws GenericException
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Country> loadAllCountries() throws GenericException {
		List<Country> country=null;
		try{
			Session session=sessionFactory.openSession();
			Criteria criteria=session.createCriteria(Country.class);
			country=(List<Country>)criteria.list();
			
		}catch (HibernateException e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}catch (Exception e) {
			throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
		}
		return country;
	}
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

}
