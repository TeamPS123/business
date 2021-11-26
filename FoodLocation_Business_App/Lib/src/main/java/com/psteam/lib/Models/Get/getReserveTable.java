package com.psteam.lib.Models.Get;

public class getReserveTable {
    public getReserveTable() {
    }

    public String getReserveTableId() {
        return reserveTableId;
    }

    public void setReserveTableId(String reserveTableId) {
        this.reserveTableId = reserveTableId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public getReserveTable(String reserveTableId, int quantity, String time, String restaurantId, String name, String phone, String promotionId, String note, String userId) {
        this.reserveTableId = reserveTableId;
        this.quantity = quantity;
        this.time = time;
        this.restaurantId = restaurantId;
        this.name = name;
        this.phone = phone;
        this.promotionId = promotionId;
        this.note = note;
        this.userId = userId;
    }

    private String reserveTableId;
    private int quantity;
    private String time;
    private String restaurantId;
    private String name;
    private String phone;
    private String promotionId;
    private String note;
    //userId is user sender
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
