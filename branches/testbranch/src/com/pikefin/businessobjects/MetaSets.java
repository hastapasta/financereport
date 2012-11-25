package com.pikefin.businessobjects;


public class MetaSets  implements java.io.Serializable {


     private Integer metaSetId;
     private String name;
 
   
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

	public Integer getMetaSetId() {
		return metaSetId;
	}

	public void setMetaSetId(Integer metaSetId) {
		this.metaSetId = metaSetId;
	}




}


