package pikefin.hibernate;

// Generated Aug 1, 2012 11:34:38 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Task generated by hbm2java
 */
public class Task implements java.io.Serializable {

	private Integer taskId;
	private String name;
	private boolean useGroupForReading;
	private Integer delay;
	private String description;
	private Metric metric;
	private Set jobs = new HashSet(0);
	private Set entityGroups = new HashSet(0);

	public Task() {
	}

	public Task(Metric metric) {
		this.metric = metric;
	}

	public Task(String name, boolean useGroupForReading, Integer delay,
			String description, Metric metric, Set jobs, Set entityGroups) {
		this.name = name;
		this.useGroupForReading = useGroupForReading;
		this.delay = delay;
		this.description = description;
		this.metric = metric;
		this.jobs = jobs;
		this.entityGroups = entityGroups;
	}

	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUseGroupForReading() {
		return this.useGroupForReading;
	}

	public void setUseGroupForReading(boolean useGroupForReading) {
		this.useGroupForReading = useGroupForReading;
	}

	public Integer getDelay() {
		return this.delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Metric getMetric() {
		return this.metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public Set getJobs() {
		return this.jobs;
	}

	public void setJobs(Set jobs) {
		this.jobs = jobs;
	}

	public Set getEntityGroups() {
		return this.entityGroups;
	}

	public void setEntityGroups(Set entityGroups) {
		this.entityGroups = entityGroups;
	}

}
