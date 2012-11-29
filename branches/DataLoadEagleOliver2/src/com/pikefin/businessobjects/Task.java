package com.pikefin.businessobjects;


import java.util.HashSet;
import java.util.Set;


public class Task implements java.io.Serializable {

	private Integer taskId;
	private String name;
	private Boolean useGroupForReading;
	private Integer delay;
	private String description;
	private Metric metric;
	private Set<Job> jobs = new HashSet<Job>(0);
	private Set<EntityGroup> entityGroups = new HashSet<EntityGroup>(0);
    private Integer epsEstPriority;
    private String source;
    private boolean allowAlerts;
   
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
	
	
	public Boolean getUseGroupForReading() {
		return useGroupForReading;
	}

	public void setUseGroupForReading(Boolean useGroupForReading) {
		this.useGroupForReading = useGroupForReading;
	}

	public Metric getMetric() {
		return metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public Integer getEpsEstPriority() {
		return epsEstPriority;
	}

	public void setEpsEstPriority(Integer epsEstPriority) {
		this.epsEstPriority = epsEstPriority;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isAllowAlerts() {
		return allowAlerts;
	}

	public void setAllowAlerts(boolean allowAlerts) {
		this.allowAlerts = allowAlerts;
	}

	public void setJobs(Set<Job> jobs) {
		this.jobs = jobs;
	}

	public void setEntityGroups(Set<EntityGroup> entityGroups) {
		this.entityGroups = entityGroups;
	}

	public Set<Job> getJobs() {
		return jobs;
	}

	public Set<EntityGroup> getEntityGroups() {
		return entityGroups;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}

}
