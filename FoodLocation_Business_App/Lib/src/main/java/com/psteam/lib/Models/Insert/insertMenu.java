package com.psteam.lib.Models.Insert;

public class insertMenu {
    public insertMenu(String restaurantId, String name, String userId) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    private String restaurantId ;
    private String name ;
    private String userId ;
}
