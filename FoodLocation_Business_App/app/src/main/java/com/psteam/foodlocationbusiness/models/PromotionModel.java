package com.psteam.foodlocationbusiness.models;

public class PromotionModel {
    private String name;
    private String info;
    private String value;

    public PromotionModel(String name, String info, String value) {
        this.name = name;
        this.info = info;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}