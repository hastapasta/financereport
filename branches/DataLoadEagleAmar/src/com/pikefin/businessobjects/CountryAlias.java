package com.pikefin.businessobjects;


public class CountryAlias implements java.io.Serializable {

	private Integer countryAliasId;
	private String countryAlias;
	private Country country;
	private Boolean isDefault;
	public Integer getCountryAliasId() {
		return countryAliasId;
	}
	public void setCountryAliasId(Integer countryAliasId) {
		this.countryAliasId = countryAliasId;
	}
	public String getCountryAlias() {
		return countryAlias;
	}
	public void setCountryAlias(String countryAlias) {
		this.countryAlias = countryAlias;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	

}
