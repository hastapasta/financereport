package com.pikefin.businessobjects;

import java.util.Date;


public class Splits  implements java.io.Serializable {


     private Integer splitId;
     private Date dateIssued;
     private Entity entity;
     private Date cutoffDate;
     private String ratio;
	public Integer getSplitId() {
		return splitId;
	}
	public void setSplitId(Integer splitId) {
		this.splitId = splitId;
	}
	public Date getDateIssued() {
		return dateIssued;
	}
	public void setDateIssued(Date dateIssued) {
		this.dateIssued = dateIssued;
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public Date getCutoffDate() {
		return cutoffDate;
	}
	public void setCutoffDate(Date cutoffDate) {
		this.cutoffDate = cutoffDate;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

    
}


