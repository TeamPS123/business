package com.psteam.lib.Models.Update;

public class updatePromotion {
    public updatePromotion(){

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public updatePromotion(String promotionId, String name, String info, String value, String userId, boolean status) {
        this.promotionId = promotionId;
        this.name = name;
        this.info = info;
        this.value = value;
        this.userId = userId;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private String promotionId ;
    private String name ;
    private String info ;
    private String value ;
    private String userId ;
    private boolean status;
}
