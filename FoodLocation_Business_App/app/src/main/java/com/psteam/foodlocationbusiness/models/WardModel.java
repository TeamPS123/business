package com.psteam.foodlocationbusiness.models;

public class WardModel {
    private String code;
    private String division_type;
    private String district_code;
    private String codename;
    private String name;

    public WardModel(String code, String division_type, String district_code, String codename, String name) {
        this.code = code;
        this.division_type = division_type;
        this.district_code = district_code;
        this.codename = codename;
        this.name = name;
    }

    public WardModel() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDivision_type() {
        return division_type;
    }

    public void setDivision_type(String division_type) {
        this.division_type = division_type;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
