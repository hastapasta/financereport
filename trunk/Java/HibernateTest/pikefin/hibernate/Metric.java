package pikefin.hibernate;

// Generated Aug 1, 2012 11:34:38 AM by Hibernate Tools 3.4.0.CR1

/**
 * Metric generated by hbm2java
 */
public class Metric implements java.io.Serializable {

	private Integer metricId;
	private String name;

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

}
