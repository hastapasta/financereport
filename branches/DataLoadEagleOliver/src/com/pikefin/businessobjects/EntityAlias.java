package com.pikefin.businessobjects;


public class EntityAlias implements java.io.Serializable {

	private Integer entityAliasId;
	private String tickerAlias;
	private Entity entity;
	private Boolean isDefault;

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public EntityAlias() {
	}

	public EntityAlias(Entity entity) {
		this.entity = entity;
	}

	public EntityAlias(String tickerAlias, Entity entity) {
		this.tickerAlias = tickerAlias;
		this.entity = entity;
	}

	public Integer getEntityAliasId() {
		return this.entityAliasId;
	}

	public void setEntityAliasId(Integer entityAliasId) {
		this.entityAliasId = entityAliasId;
	}

	public String getTickerAlias() {
		return this.tickerAlias;
	}

	public void setTickerAlias(String tickerAlias) {
		this.tickerAlias = tickerAlias;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

}
