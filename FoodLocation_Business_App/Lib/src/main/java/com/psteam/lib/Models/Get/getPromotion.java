package com.psteam.lib.Models.Get;

public class getPromotion {
    public getPromotion(){

    }

    public getPromotion(String promotionId, String name, String info, String value) {
        this.promotionId = promotionId;
        this.name = name;
        this.info = info;
        this.value = value;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
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

    private String promotionId ;
    private String name ;
    private String info ;
    private String value ;
}
