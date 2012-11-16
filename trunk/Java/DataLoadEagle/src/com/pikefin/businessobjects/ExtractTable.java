package com.pikefin.businessobjects;



import java.util.HashSet;
import java.util.Set;


public class ExtractTable implements java.io.Serializable {

	private Integer extractTableId;
	private String description;
	private String tableCount;
	private Integer topCornerRow;
	private Integer numberOfColumns;
	private Boolean columnThTags;
	private Integer rowsOfData;
	private Integer rowInterval;
	private String endDataMarker;
	private Set<Column> columns = new HashSet<Column>(0);



	public Integer getExtractTableId() {
		return this.extractTableId;
	}

	public void setExtractTableId(Integer extractTableId) {
		this.extractTableId = extractTableId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTableCount() {
		return this.tableCount;
	}

	public void setTableCount(String tableCount) {
		this.tableCount = tableCount;
	}

	public Integer getTopCornerRow() {
		return this.topCornerRow;
	}

	public void setTopCornerRow(Integer topCornerRow) {
		this.topCornerRow = topCornerRow;
	}

	public Integer getNumberOfColumns() {
		return this.numberOfColumns;
	}

	public void setNumberOfColumns(Integer numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	

	public Integer getRowsOfData() {
		return this.rowsOfData;
	}

	public void setRowsOfData(Integer rowsOfData) {
		this.rowsOfData = rowsOfData;
	}

	public Integer getRowInterval() {
		return this.rowInterval;
	}

	public void setRowInterval(Integer rowInterval) {
		this.rowInterval = rowInterval;
	}

	public String getEndDataMarker() {
		return this.endDataMarker;
	}

	public void setEndDataMarker(String endDataMarker) {
		this.endDataMarker = endDataMarker;
	}

	public Boolean getColumnThTags() {
		return columnThTags;
	}

	public void setColumnThTags(Boolean columnThTags) {
		this.columnThTags = columnThTags;
	}

	public Set<Column> getColumns() {
		return columns;
	}

	public void setColumns(Set<Column> columns) {
		this.columns = columns;
	}

	
}
