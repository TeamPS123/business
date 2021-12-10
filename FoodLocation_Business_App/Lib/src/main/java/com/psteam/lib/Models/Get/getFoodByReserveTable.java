package com.psteam.lib.Models.Get;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class getFoodByReserveTable {
    public getFoodByReserveTable(String foodId, String name, double price, String unit, String menuName, String categoryName, int amount, ArrayList<String> pic) {
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.menuName = menuName;
        this.categoryName = categoryName;
        this.amount = amount;
        this.pic = pic;
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

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }

    private String foodId ;
    private String name ;
    private double price ;
    private String unit ;
    private String menuName ;
    private String categoryName ;
    private int amount ;
    private ArrayList<String> pic ;
}
