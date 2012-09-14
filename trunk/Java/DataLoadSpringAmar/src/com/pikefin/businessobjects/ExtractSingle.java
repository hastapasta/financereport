package com.pikefin.businessobjects;

public class ExtractSingle implements java.io.Serializable {

	private Integer extractSingleId;
	private Integer tableCount;
	private Integer rowCount;
	private Integer cellCount;
	private Integer divCount;
	private Boolean parsePostProcess;
	private Boolean isCSVFormat;
	private String initialBefUniqueCode;
	private String beforeUniqueCode;
	private String afterUniqueCode;
	private String obsDatSet;
	public Integer getExtractSingleId() {
		return extractSingleId;
	}
	public void setExtractSingleId(Integer extractSingleId) {
		this.extractSingleId = extractSingleId;
	}
	public Integer getTableCount() {
		return tableCount;
	}
	public void setTableCount(Integer tableCount) {
		this.tableCount = tableCount;
	}
	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
	public Integer getCellCount() {
		return cellCount;
	}
	public void setCellCount(Integer cellCount) {
		this.cellCount = cellCount;
	}
	public Integer getDivCount() {
		return divCount;
	}
	public void setDivCount(Integer divCount) {
		this.divCount = divCount;
	}
	public Boolean getParsePostProcess() {
		return parsePostProcess;
	}
	public void setParsePostProcess(Boolean parsePostProcess) {
		this.parsePostProcess = parsePostProcess;
	}
	public Boolean getIsCSVFormat() {
		return isCSVFormat;
	}
	public void setIsCSVFormat(Boolean isCSVFormat) {
		this.isCSVFormat = isCSVFormat;
	}
	public String getInitialBefUniqueCode() {
		return initialBefUniqueCode;
	}
	public void setInitialBefUniqueCode(String initialBefUniqueCode) {
		this.initialBefUniqueCode = initialBefUniqueCode;
	}
	public String getBeforeUniqueCode() {
		return beforeUniqueCode;
	}
	public void setBeforeUniqueCode(String beforeUniqueCode) {
		this.beforeUniqueCode = beforeUniqueCode;
	}
	public String getAfterUniqueCode() {
		return afterUniqueCode;
	}
	public void setAfterUniqueCode(String afterUniqueCode) {
		this.afterUniqueCode = afterUniqueCode;
	}
	public String getObsDatSet() {
		return obsDatSet;
	}
	public void setObsDatSet(String obsDatSet) {
		this.obsDatSet = obsDatSet;
	}

	
}
