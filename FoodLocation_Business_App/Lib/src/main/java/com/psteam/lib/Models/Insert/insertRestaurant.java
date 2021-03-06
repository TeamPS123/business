package com.psteam.lib.Models.Insert;

public class insertRestaurant {
    public insertRestaurant() {
    }


    public insertRestaurant(String name, String userId, String line, String city, String district, String longLat, String openTime, String closeTime, String phone, int categoryResId) {
        this.name = name;
        this.userId = userId;
        this.line = line;
        this.city = city;
        this.district = district;
        this.longLat = longLat;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.phone = phone;
        this.categoryResId = categoryResId;
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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLongLat() {
        return longLat;
    }

    public void setLongLat(String longLat) {
        this.longLat = longLat;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCategoryResId() {
        return categoryResId;
    }

    public void setCategoryResId(int categoryResId) {
        this.categoryResId = categoryResId;
    }

    private String name ;
    private String userId ;
    private String line ;
    private String city ;
    private String district ;
    private String longLat ;
    private String openTime ;
    private String closeTime ;
    private String phone;
    private int categoryResId;
}
