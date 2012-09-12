package com.pikefin.businessobjects;

import java.util.HashSet;
import java.util.Set;

public class Job implements java.io.Serializable {

	private Integer jobId;
	private String dataSet;
	private String urlStatic;
	private boolean tableExtraction;
	private boolean customInsert;
	private String preJobProcessFuncName;
	private String postJobProcessFuncName;
	private String preProcessFuncName;
	private String postProcessFuncName;
	private String preNoDataCheckFunc;
	private String customUrlFuncName;
	private ExtractTable extractKeyColhead;
	private ExtractTable extractKeyRowhead;
	private ExtractTable extractKeyBody;
	private InputSource inputSource;
    private String source;
	private String dataGroup;
	private String dataSetAlias;
	private String obsoleteUrlDynamic;
	private String obsoleteMultipleTables;
	private Set<Task> tasks = new HashSet<Task>();

	
	public Integer getJobId() {
		return this.jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getDataSet() {
		return this.dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public String getUrlStatic() {
		return this.urlStatic;
	}

	public void setUrlStatic(String urlStatic) {
		this.urlStatic = urlStatic;
	}

	public boolean isTableExtraction() {
		return this.tableExtraction;
	}

	public void setTableExtraction(boolean tableExtraction) {
		this.tableExtraction = tableExtraction;
	}

	public boolean isCustomInsert() {
		return this.customInsert;
	}

	public void setCustomInsert(boolean customInsert) {
		this.customInsert = customInsert;
	}

	public String getPreJobProcessFuncName() {
		return this.preJobProcessFuncName;
	}

	public void setPreJobProcessFuncName(String preJobProcessFuncName) {
		this.preJobProcessFuncName = preJobProcessFuncName;
	}

	public String getPostJobProcessFuncName() {
		return this.postJobProcessFuncName;
	}

	public void setPostJobProcessFuncName(String postJobProcessFuncName) {
		this.postJobProcessFuncName = postJobProcessFuncName;
	}

	public String getPreProcessFuncName() {
		return this.preProcessFuncName;
	}

	public void setPreProcessFuncName(String preProcessFuncName) {
		this.preProcessFuncName = preProcessFuncName;
	}

	public String getPostProcessFuncName() {
		return this.postProcessFuncName;
	}

	public void setPostProcessFuncName(String postProcessFuncName) {
		this.postProcessFuncName = postProcessFuncName;
	}

	public String getPreNoDataCheckFunc() {
		return this.preNoDataCheckFunc;
	}

	public void setPreNoDataCheckFunc(String preNoDataCheckFunc) {
		this.preNoDataCheckFunc = preNoDataCheckFunc;
	}

	public String getCustomUrlFuncName() {
		return this.customUrlFuncName;
	}

	public void setCustomUrlFuncName(String customUrlFuncName) {
		this.customUrlFuncName = customUrlFuncName;
	}

	public ExtractTable getExtractKeyColhead() {
		return this.extractKeyColhead;
	}

	public void setExtractKeyColhead(ExtractTable extractKeyColhead) {
		this.extractKeyColhead = extractKeyColhead;
	}

	public ExtractTable getExtractKeyRowhead() {
		return this.extractKeyRowhead;
	}

	public void setExtractKeyRowhead(ExtractTable extractKeyRowhead) {
		this.extractKeyRowhead = extractKeyRowhead;
	}

	public ExtractTable getExtractKeyBody() {
		return this.extractKeyBody;
	}

	public void setExtractKeyBody(ExtractTable extractKeyBody) {
		this.extractKeyBody = extractKeyBody;
	}

	public InputSource getInputSource() {
		return this.inputSource;
	}

	public void setInputSource(InputSource inputSource) {
		this.inputSource = inputSource;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDataGroup() {
		return dataGroup;
	}

	public void setDataGroup(String dataGroup) {
		this.dataGroup = dataGroup;
	}

	public String getDataSetAlias() {
		return dataSetAlias;
	}

	public void setDataSetAlias(String dataSetAlias) {
		this.dataSetAlias = dataSetAlias;
	}

	public String getObsoleteUrlDynamic() {
		return obsoleteUrlDynamic;
	}

	public void setObsoleteUrlDynamic(String obsoleteUrlDynamic) {
		this.obsoleteUrlDynamic = obsoleteUrlDynamic;
	}

	public String getObsoleteMultipleTables() {
		return obsoleteMultipleTables;
	}

	public void setObsoleteMultipleTables(String obsoleteMultipleTables) {
		this.obsoleteMultipleTables = obsoleteMultipleTables;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

}
