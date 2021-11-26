package com.psteam.lib.Models.Insert;

public class food {
    public food(String name, double price, String unit, String categoryId) {
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    public String getCategoryId() {
        return categoryId;
    }

    private String name ;
    private double price ;
    private String unit ;
    private String categoryId ;
}
