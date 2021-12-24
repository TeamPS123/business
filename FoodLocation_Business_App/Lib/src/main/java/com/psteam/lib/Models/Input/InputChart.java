package com.psteam.lib.Models.Input;

public class InputChart {
    private String date2;

    private String restaurantId;

    private String date1;

    private String userId;

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public InputChart(String date2, String restaurantId, String date1, String userId) {
        this.date2 = date2;
        this.restaurantId = restaurantId;
        this.date1 = date1;
        this.userId = userId;
    }
}