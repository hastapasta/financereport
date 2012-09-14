package com.pikefin.businessobjects;



public class SecurityAccounts  implements java.io.Serializable {


     private Integer id;
     private String username;
     private String password;
     private String auth1;
     private String auth2;
     private String auth3;
     private String auth4;

    public SecurityAccounts() {
    }

  
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAuth1() {
        return this.auth1;
    }
    
    public void setAuth1(String auth1) {
        this.auth1 = auth1;
    }
    public String getAuth2() {
        return this.auth2;
    }
    
    public void setAuth2(String auth2) {
        this.auth2 = auth2;
    }
    public String getAuth3() {
        return this.auth3;
    }
    
    public void setAuth3(String auth3) {
        this.auth3 = auth3;
    }
    public String getAuth4() {
        return this.auth4;
    }
    
    public void setAuth4(String auth4) {
        this.auth4 = auth4;
    }




}


