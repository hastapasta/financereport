package com.pikefin.businessobjects;


public class Column implements java.io.Serializable {

	private Integer columnId;
	private Integer colCount;
	private Integer colPosition;
	private String befCode;
	private String aftCode;
	private ExtractTable extractTable;

	public Column() {
	}

	public Column(ExtractTable extractTable) {
		this.extractTable = extractTable;
	}

	

	public Integer getColumnId() {
		return this.columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public Integer getColCount() {
		return this.colCount;
	}

	public void setColCount(Integer colCount) {
		this.colCount = colCount;
	}

	public Integer getColPosition() {
		return this.colPosition;
	}

	public void setColPosition(Integer colPosition) {
		this.colPosition = colPosition;
	}

	public String getBefCode() {
		return this.befCode;
	}

	public void setBefCode(String befCode) {
		this.befCode = befCode;
	}

	public String getAftCode() {
		return this.aftCode;
	}

	public void setAftCode(String aftCode) {
		this.aftCode = aftCode;
	}

	public ExtractTable getExtractTable() {
		return this.extractTable;
	}

	public void setExtractTable(ExtractTable extractTable) {
		this.extractTable = extractTable;
	}

	@Override
	public boolean equals(Object obj){
		
		if(obj!=null && obj instanceof Column ){
			Column a=(Column)obj;
			return a.columnId.equals(this.columnId);
		}else
			return false;
	}
}
