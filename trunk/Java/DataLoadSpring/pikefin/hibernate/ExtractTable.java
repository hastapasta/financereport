package pikefin.hibernate;

// Generated Jun 18, 2012 7:54:36 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * ExtractTable generated by hbm2java
 */
public class ExtractTable implements java.io.Serializable {

	private Integer extractTableId;
	private String description;
	private String tableCount;
	private Integer topCornerRow;
	private Integer numberOfColumns;
	private boolean columnThTags;
	private Integer rowsOfData;
	private Integer rowInterval;
	private String endDataMarker;
	private Set columns = new HashSet(0);

	public ExtractTable() {
	}

	public ExtractTable(String description, String tableCount,
			Integer topCornerRow, Integer numberOfColumns,
			boolean columnThTags, Integer rowsOfData, Integer rowInterval,
			String endDataMarker, Set columns) {
		this.description = description;
		this.tableCount = tableCount;
		this.topCornerRow = topCornerRow;
		this.numberOfColumns = numberOfColumns;
		this.columnThTags = columnThTags;
		this.rowsOfData = rowsOfData;
		this.rowInterval = rowInterval;
		this.endDataMarker = endDataMarker;
		this.columns = columns;
	}

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

	public boolean isColumnThTags() {
		return this.columnThTags;
	}

	public void setColumnThTags(boolean columnThTags) {
		this.columnThTags = columnThTags;
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

	public Set getColumns() {
		return this.columns;
	}

	public void setColumns(Set columns) {
		this.columns = columns;
	}

}