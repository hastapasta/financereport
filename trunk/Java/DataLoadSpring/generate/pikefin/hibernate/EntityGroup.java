package pikefin.hibernate;

// Generated Jun 13, 2012 10:24:38 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * EntityGroup generated by hbm2java
 */
public class EntityGroup implements java.io.Serializable {

	private Integer entityGroupId;
	private Set entity = new HashSet(0);

	public EntityGroup() {
	}

	public EntityGroup(Set entity) {
		this.entity = entity;
	}

	public Integer getEntityGroupId() {
		return this.entityGroupId;
	}

	public void setEntityGroupId(Integer entityGroupId) {
		this.entityGroupId = entityGroupId;
	}

	public Set getEntity() {
		return this.entity;
	}

	public void setEntity(Set entity) {
		this.entity = entity;
	}

}