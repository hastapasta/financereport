package com.pikefin.businessobjects;


public class Regions  implements java.io.Serializable {


     private Integer regionId;
     private String name;

   
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}




}


