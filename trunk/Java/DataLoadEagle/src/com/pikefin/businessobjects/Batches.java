package com.pikefin.businessobjects;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
/**
 * Batches class related to batches table in database
 * @author Amar_Deep_Singh
 *
 */
public class Batches implements Serializable{

	
	private Integer batchId;
	private Date batchDateCollected;
	private Task batchTask;
	private Integer count;
	private Integer randomUnique;
	private Integer garbageCollect=0;
	
	
	
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public Date getBatchDateCollected() {
		return batchDateCollected;
	}
	public void setBatchDateCollected(Date batchDateCollected) {
		this.batchDateCollected = batchDateCollected;
	}
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public Task getBatchTask() {
		return batchTask;
	}
	public void setBatchTask(Task batchTask) {
		this.batchTask = batchTask;
	}
	public Integer getRandomUnique() {
		return randomUnique;
	}
	public void setRandomUnique(Integer randomUnique) {
		this.randomUnique = randomUnique;
	}
	public Integer getGarbageCollect() {
		return garbageCollect;
	}
	public void setGarbageCollect(Integer garbageCollect) {
		this.garbageCollect = garbageCollect;
	}
}
