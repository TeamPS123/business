package com.psteam.lib.Models.Insert;

public class insertCategory {


    public insertCategory(String name, String userId, String restaurantId) {
        this.name = name;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    private String name;
    private String userId;
    private String restaurantId;
}
