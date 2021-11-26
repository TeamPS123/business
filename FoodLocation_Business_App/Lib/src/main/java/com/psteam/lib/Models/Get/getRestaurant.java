package com.psteam.lib.Models.Get;

import java.util.List;

public class getRestaurant {
    public getRestaurant(String restaurantId, String name, String line, String city, String district, String longLat, String openTime, String closeTime, String distance, List<String> pic, List<String> categoryRes) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.line = line;
        this.city = city;
        this.district = district;
        this.longLat = longLat;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.distance = distance;
        this.pic = pic;
        this.categoryRes = categoryRes;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        return line;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getLongLat() {
        return longLat;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public String getDistance() {
        return distance;
    }

    public List<String> getPic() {
        return pic;
    }

    public List<String> getCategoryRes() {
        return categoryRes;
    }

    private String restaurantId ;
    private String name ;
    private String line ;
    private String city ;
    private String district ;
    private String longLat ;
    private String openTime ;
    private String closeTime ;
    private String distance ;
    private List<String> pic ;
    private List<String> categoryRes ;
}
