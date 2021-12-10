package com.psteam.lib.Models.Update;

public class updateFood {
    public  updateFood(){

    }

    public updateFood(String userId, String foodId, String name, double price, String unit, String categoryId, Boolean status) {
        this.userId = userId;
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.categoryId = categoryId;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    private String userId;
    private String foodId;
    private String name;
    private double price;
    private String unit;
    private String categoryId;
    private Boolean status;
}
