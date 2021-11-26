package com.psteam.foodlocationbusiness.models;

import java.util.ArrayList;

public class ProvinceModel {
    private String code;
    private String division_type;
    private String codename;
    private String name;
    private String phone_code;

    private ArrayList<DistrictModel> districts;

    public ProvinceModel(String code, String division_type, String codename, String name, String phone_code) {
        this.code = code;
        this.division_type = division_type;
        this.codename = codename;
        this.name = name;
        this.phone_code = phone_code;
    }

    public ProvinceModel(String code, String division_type, String codename, String name, String phone_code, ArrayList<DistrictModel> districts) {
        this.code = code;
        this.division_type = division_type;
        this.codename = codename;
        this.name = name;
        this.phone_code = phone_code;
        this.districts = districts;
    }

    public ProvinceModel() {
    }

    public ArrayList<DistrictModel> getDistricts() {
        return districts;
    }

    public void setDistricts(ArrayList<DistrictModel> districts) {
        this.districts = districts;
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

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    @Override
    public String toString() {
        return name;
    }
}
