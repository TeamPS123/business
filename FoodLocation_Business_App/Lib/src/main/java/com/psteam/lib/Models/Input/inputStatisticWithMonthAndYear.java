package com.psteam.lib.Models.Input;

public class inputStatisticWithMonthAndYear {
    public inputStatisticWithMonthAndYear(){

    }

    public inputStatisticWithMonthAndYear(String userId, String restaurantId, String month1, String year1, String month2, String year2) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.month1 = month1;
        this.year1 = year1;
        this.month2 = month2;
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

    public String getMonth1() {
        return month1;
    }

    public void setMonth1(String month1) {
        this.month1 = month1;
    }

    public String getYear1() {
        return year1;
    }

    public void setYear1(String year1) {
        this.year1 = year1;
    }

    public String getMonth2() {
        return month2;
    }

    public void setMonth2(String month2) {
        this.month2 = month2;
    }

    public String getYear2() {
        return year2;
    }

    public void setYear2(String year2) {
        this.year2 = year2;
    }

    private String userId;
    private String restaurantId;
    private String month1;
    private String year1;
    private String month2;
    private String year2;
}
