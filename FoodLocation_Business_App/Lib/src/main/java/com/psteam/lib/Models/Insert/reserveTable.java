package com.psteam.lib.Models.Insert;

public class reserveTable {
    public reserveTable(int quantity, String time, String restaurantId, String promotionId, String userId) {
        this.quantity = quantity;
        this.time = time;
        this.restaurantId = restaurantId;
        this.promotionId = promotionId;
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTime() {
        return time;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public String getUserId() {
        return userId;
    }

    private int quantity ;
    private String time ;
    private String restaurantId ;
    private String promotionId ;
    private String userId ;
}
