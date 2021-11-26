package com.psteam.lib.Models.Insert;

public class insertFood {
    public insertFood(String name, double price, String unit, String menuId, String categoryId, String userId) {
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.menuId = menuId;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public insertFood() {
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

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String name ;
    private double price ;
    private String unit ;
    private String menuId ;
    private String categoryId ;
    private String userId ;
}
