package com.psteam.lib.Models.Insert;

import java.io.Serializable;

public class signUp implements Serializable {
    public signUp(String fullName, String phone, String pass, Boolean business, Boolean gender) {
        this.fullName = fullName;
        this.phone = phone;
        this.pass = pass;
        this.business = business;
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getPass() {
        return pass;
    }

    public Boolean getBusiness() {
        return business;
    }

    public Boolean getGender() {
        return gender;
    }

    private String fullName ;
    private String phone ;
    private String pass ;
    private Boolean business ;
    private Boolean gender ;
}
