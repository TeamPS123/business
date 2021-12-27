package com.psteam.lib.Models.Get;

import java.io.Serializable;
import java.util.List;

public class getFood implements Serializable {

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


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    private String menuId ;
    private String foodId ;
    private String name ;
    private double price ;
    private String unit ;
    private int quantity;
    private String categoryName ;
    private String categoryId;

    public getFood(String menuId, String foodId, String name, double price, String unit, int quantity, String categoryName, String categoryId, List<String> pic, Boolean status) {
        this.menuId = menuId;
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.quantity = quantity;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.pic = pic;
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    private List<String> pic ;
    private Boolean status;
//
//    public getFood(String menuId, String foodId, String name, double price, String unit, String categoryName, String categoryId, List<String> pic, Boolean status) {
//        this.menuId = menuId;
//        this.foodId = foodId;
//        this.name = name;
//        this.price = price;
//        this.unit = unit;
//        this.categoryName = categoryName;
//        this.categoryId = categoryId;
//        this.pic = pic;
//        this.status = status;
//    }
}
