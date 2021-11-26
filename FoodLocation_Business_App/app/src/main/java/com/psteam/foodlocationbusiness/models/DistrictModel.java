package com.psteam.foodlocationbusiness.models;

import java.util.ArrayList;

public class DistrictModel {
    private String code;
    private String division_type;
    private String codename;
    private String name;
    private String province_code;
    private ArrayList<WardModel> wards;

    public DistrictModel(String code, String division_type, String codename, String name, String province_code) {
        this.code = code;
        this.division_type = division_type;
        this.codename = codename;
        this.name = name;
        this.province_code = province_code;
    }

    public DistrictModel() {
    }

    public ArrayList<WardModel> getWards() {
        return wards;
    }

    public void setWards(ArrayList<WardModel> wards) {
        this.wards = wards;
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

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    @Override
    public String toString() {
        return name;
    }
}
