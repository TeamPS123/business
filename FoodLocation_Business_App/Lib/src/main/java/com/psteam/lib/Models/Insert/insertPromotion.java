package com.psteam.lib.Models.Insert;

public class insertPromotion {
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

    private String restaurantId ;
    private String name ;
    private String info ;
    private String value ;
    private String userId ;
}
