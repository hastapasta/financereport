package com.pikefin.businessobjects;


public class PageCounters  implements java.io.Serializable {


     private Integer pageCounterId;
     private String uri;
     private int hits;
	public Integer getPageCounterId() {
		return pageCounterId;
	}
	public void setPageCounterId(Integer pageCounterId) {
		this.pageCounterId = pageCounterId;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}

  


}


