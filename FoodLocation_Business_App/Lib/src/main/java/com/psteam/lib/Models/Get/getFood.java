package com.psteam.lib.Models.Get;

import java.io.Serializable;
import java.util.List;

public class getFood implements Serializable {
    public getFood(String menuId, String foodId, String name, double price, String unit, String categoryName, List<String> pic) {
        this.menuId = menuId;
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.categoryName = categoryName;
        this.pic = pic;
    }

    public getFood() {
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    private String menuId ;
    private String foodId ;
    private String name ;
    private double price ;
    private String unit ;
    private String categoryName ;
    private List<String> pic ;
}
