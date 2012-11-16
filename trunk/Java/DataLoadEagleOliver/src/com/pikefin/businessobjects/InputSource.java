package com.pikefin.businessobjects;


public class InputSource implements java.io.Serializable {

	private Integer inputSourceId;
	private String urlStatic;
	private String urlDynamic;
	private String urlForm;
	private String formStaticProperties;

	public InputSource() {
	}

	public InputSource(String urlStatic, String urlDynamic, String urlForm,
			String formStaticProperties) {
		this.urlStatic = urlStatic;
		this.urlDynamic = urlDynamic;
		this.urlForm = urlForm;
		this.formStaticProperties = formStaticProperties;
	}

	public Integer getInputSourceId() {
		return this.inputSourceId;
	}

	public void setInputSourceId(Integer inputSourceId) {
		this.inputSourceId = inputSourceId;
	}

	public String getUrlStatic() {
		return this.urlStatic;
	}

	public void setUrlStatic(String urlStatic) {
		this.urlStatic = urlStatic;
	}

	public String getUrlDynamic() {
		return this.urlDynamic;
	}

	public void setUrlDynamic(String urlDynamic) {
		this.urlDynamic = urlDynamic;
	}

	public String getUrlForm() {
		return this.urlForm;
	}

	public void setUrlForm(String urlForm) {
		this.urlForm = urlForm;
	}

	public String getFormStaticProperties() {
		return this.formStaticProperties;
	}

	public void setFormStaticProperties(String formStaticProperties) {
		this.formStaticProperties = formStaticProperties;
	}

}
