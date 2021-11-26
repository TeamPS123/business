package com.psteam.lib.Models.Get;

public class getPromotion {
    public getPromotion(String promotionId, String restaurantName, String name, String info, String value, String line, String district, String city, String image, Double distance) {
        this.promotionId = promotionId;
        this.restaurantName = restaurantName;
        this.name = name;
        this.info = info;
        this.value = value;
        this.line = line;
        this.district = district;
        this.city = city;
        this.image = image;
        this.distance = distance;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public String getRestaurantName() {
        return restaurantName;
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

    public String getLine() {
        return line;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getImage() {
        return image;
    }

    public Double getDistance() {
        return distance;
    }

    private String promotionId ;
    private String restaurantName ;
    private String name ;
    private String info ;
    private String value ;
    private String line ;
    private String district ;
    private String city ;
    private String image ;
    private Double distance;
}
