package com.pikefin.businessobjects;

// Generated Jun 18, 2012 7:54:36 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;


public class EntityGroup implements java.io.Serializable {

	private Integer entityGroupId;
    private String name;
    private String type;
    private String description;
    private Integer parentId;
    private Integer lft;
    private Integer rght;
	private Set<Entity> entities = new HashSet(0);

	
	public Integer getEntityGroupId() {
		return this.entityGroupId;
	}

	public void setEntityGroupId(Integer entityGroupId) {
		this.entityGroupId = entityGroupId;
	}

		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getLft() {
		return lft;
	}

	public void setLft(Integer lft) {
		this.lft = lft;
	}

	public Integer getRght() {
		return rght;
	}

	public void setRght(Integer rght) {
		this.rght = rght;
	}

	public void setEntities(Set<Entity> entities) {
		this.entities = entities;
	}

	public Set<Entity> getEntities() {
		return entities;
	}

}
