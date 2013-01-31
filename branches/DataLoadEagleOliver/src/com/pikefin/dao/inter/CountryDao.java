package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Country;
import com.pikefin.exceptions.GenericException;

public interface CountryDao {
	public Country saveCountryInfo(Country countryEntity) throws GenericException;
	public Country updateCountryInfo(Country countryEntity) throws GenericException;
	public Boolean deleteCountryInfo(Country countryEntity ) throws GenericException;
	public Boolean deleteCountryInfoById(Integer countryId ) throws GenericException;
	public Country loadCountryInfo(Integer countryId) throws GenericException;
	public List<Country> loadAllCountries() throws GenericException;
}
