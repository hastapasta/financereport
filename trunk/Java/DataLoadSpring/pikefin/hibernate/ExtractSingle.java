package pikefin.hibernate;

// Generated Jun 13, 2012 10:24:38 AM by Hibernate Tools 3.4.0.CR1

/**
 * ExtractSingle generated by hbm2java
 */
public class ExtractSingle implements java.io.Serializable {

	private Integer extractSingleId;
	private Integer tableCount;
	private Integer rowCount;
	private Integer cellCount;
	private Integer divCount;
	private boolean parsePostProcess;
	private boolean isCSVFormat;
	private String initialBefUniqueCode;
	private String beforeUniqueCode;
	private String afterUniqueCode;

	public ExtractSingle() {
	}

	public ExtractSingle(Integer tableCount, Integer rowCount,
			Integer cellCount, Integer divCount, boolean parsePostProcess,
			boolean isCSVFormat, String initialBefUniqueCode,
			String beforeUniqueCode, String afterUniqueCode) {
		this.tableCount = tableCount;
		this.rowCount = rowCount;
		this.cellCount = cellCount;
		this.divCount = divCount;
		this.parsePostProcess = parsePostProcess;
		this.isCSVFormat = isCSVFormat;
		this.initialBefUniqueCode = initialBefUniqueCode;
		this.beforeUniqueCode = beforeUniqueCode;
		this.afterUniqueCode = afterUniqueCode;
	}

	public Integer getExtractSingleId() {
		return this.extractSingleId;
	}

	public void setExtractSingleId(Integer extractSingleId) {
		this.extractSingleId = extractSingleId;
	}

	public Integer getTableCount() {
		return this.tableCount;
	}

	public void setTableCount(Integer tableCount) {
		this.tableCount = tableCount;
	}

	public Integer getRowCount() {
		return this.rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public Integer getCellCount() {
		return this.cellCount;
	}

	public void setCellCount(Integer cellCount) {
		this.cellCount = cellCount;
	}

	public Integer getDivCount() {
		return this.divCount;
	}

	public void setDivCount(Integer divCount) {
		this.divCount = divCount;
	}

	public boolean isParsePostProcess() {
		return this.parsePostProcess;
	}

	public void setParsePostProcess(boolean parsePostProcess) {
		this.parsePostProcess = parsePostProcess;
	}

	public boolean isIsCSVFormat() {
		return this.isCSVFormat;
	}

	public void setIsCSVFormat(boolean isCSVFormat) {
		this.isCSVFormat = isCSVFormat;
	}

	public String getInitialBefUniqueCode() {
		return this.initialBefUniqueCode;
	}

	public void setInitialBefUniqueCode(String initialBefUniqueCode) {
		this.initialBefUniqueCode = initialBefUniqueCode;
	}

	public String getBeforeUniqueCode() {
		return this.beforeUniqueCode;
	}

	public void setBeforeUniqueCode(String beforeUniqueCode) {
		this.beforeUniqueCode = beforeUniqueCode;
	}

	public String getAfterUniqueCode() {
		return this.afterUniqueCode;
	}

	public void setAfterUniqueCode(String afterUniqueCode) {
		this.afterUniqueCode = afterUniqueCode;
	}

}