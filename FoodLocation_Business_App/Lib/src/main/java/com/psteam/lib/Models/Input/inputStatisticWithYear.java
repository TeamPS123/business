package com.psteam.lib.Models.Input;

public class inputStatisticWithYear {
    public inputStatisticWithYear(){

    }

    public inputStatisticWithYear(String userId, String restaurantId, String year1, String year2) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.year1 = year1;
        this.year2 = year2;
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

    public String getYear1() {
        return year1;
    }

    public void setYear1(String year1) {
        this.year1 = year1;
    }

    public String getYear2() {
        return year2;
    }

    public void setYear2(String year2) {
        this.year2 = year2;
    }

    private String userId;
    private String restaurantId;
    private String year1;
    private String year2;
}
