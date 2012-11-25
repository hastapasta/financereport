package com.pikefin.businessobjects;


public class Country implements java.io.Serializable {

	private Integer countryId;
	private String name;
	private String hash;
	private String shortName;

	public Country() {
	}

	public Country(String name, String hash, String shortName) {
		this.name = name;
		this.hash = hash;
		this.shortName = shortName;
	}

	public Integer getCountryId() {
		return this.countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHash() {
		return this.hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
