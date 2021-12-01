package com.psteam.lib.Models.Insert;

public class insertPromotion {
    public insertPromotion(){

    }

    public insertPromotion(String restaurantId, String name, String info, String value, String userId) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.info = info;
        this.value = value;
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getValue() {
        return value;
    }

    public String getUserId() {
        return userId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String restaurantId ;
    private String name ;
    private String info ;
    private String value ;
    private String userId ;
}
