package com.pikefin.businessobjects;

// Generated Jun 18, 2012 7:54:36 AM by Hibernate Tools 3.4.0.CR1

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Entity implements java.io.Serializable {

	private Integer entityId;
	private String ticker;
	private String beginFiscalCalendar;
	private Double sharesOutstanding;
	private Integer lastReportedQuarter;
	private Date nextReportDate;
	private String obsoleteGroups;
	private String actualFiscalYearEnd;
	private String fullName;
	private Date depricated;
	private Integer obsoleteCountryId;
	private String ticker2;
	private String hash;
	private List<Country> countries = new ArrayList<Country>(0);
	private Set<EntityGroup> entityGroups = new HashSet<EntityGroup>(0);

	public Entity() {
	}

	public Integer getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getBeginFiscalCalendar() {
		return this.beginFiscalCalendar;
	}

	public void setBeginFiscalCalendar(String beginFiscalCalendar) {
		this.beginFiscalCalendar = beginFiscalCalendar;
	}

	public Double getSharesOutstanding() {
		return sharesOutstanding;
	}

	public void setSharesOutstanding(Double sharesOutstanding) {
		this.sharesOutstanding = sharesOutstanding;
	}

	public Integer getLastReportedQuarter() {
		return lastReportedQuarter;
	}

	public void setLastReportedQuarter(Integer lastReportedQuarter) {
		this.lastReportedQuarter = lastReportedQuarter;
	}

	public Date getNextReportDate() {
		return nextReportDate;
	}

	public void setNextReportDate(Date nextReportDate) {
		this.nextReportDate = nextReportDate;
	}

	public String getObsoleteGroups() {
		return obsoleteGroups;
	}

	public void setObsoleteGroups(String obsoleteGroups) {
		this.obsoleteGroups = obsoleteGroups;
	}

	public String getActualFiscalYearEnd() {
		return actualFiscalYearEnd;
	}

	public void setActualFiscalYearEnd(String actualFiscalYearEnd) {
		this.actualFiscalYearEnd = actualFiscalYearEnd;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getDepricated() {
		return depricated;
	}

	public void setDepricated(Date depricated) {
		this.depricated = depricated;
	}

	public Integer getObsoleteCountryId() {
		return obsoleteCountryId;
	}

	public void setObsoleteCountryId(Integer obsoleteCountryId) {
		this.obsoleteCountryId = obsoleteCountryId;
	}

	public String getTicker2() {
		return ticker2;
	}

	public void setTicker2(String ticker2) {
		this.ticker2 = ticker2;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public Set<EntityGroup> getEntityGroups() {
		return entityGroups;
	}

	public void setEntityGroups(Set<EntityGroup> entityGroups) {
		this.entityGroups = entityGroups;
	}

}
