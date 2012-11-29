package com.pikefin.businessobjects;


public class Metric implements java.io.Serializable {

	private Integer metricId;
	private String name;
	 private String description;

	public Metric() {
	}

	public Metric(String name) {
		this.name = name;
	}

	public Integer getMetricId() {
		return this.metricId;
	}

	public void setMetricId(Integer metricId) {
		this.metricId = metricId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
