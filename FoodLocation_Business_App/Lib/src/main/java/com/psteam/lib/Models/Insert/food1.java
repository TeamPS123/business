package com.psteam.lib.Models.Insert;

public class food1 {
    public food1(String foodId, double price, int quantity) {
        this.foodId = foodId;
        this.price = price;
        this.quantity = quantity;
    }

    public String getFoodId() {
        return foodId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    private String foodId ;
    private double price ;
    private int quantity ;
}
