package pikefin.hibernate;

// Generated Jun 18, 2012 7:54:36 AM by Hibernate Tools 3.4.0.CR1

/**
 * Column generated by hbm2java
 */
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

	public Column(Integer colCount, Integer colPosition, String befCode,
			String aftCode, ExtractTable extractTable) {
		this.colCount = colCount;
		this.colPosition = colPosition;
		this.befCode = befCode;
		this.aftCode = aftCode;
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

}