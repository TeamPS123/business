package com.psteam.foodlocationbusiness.socket.models;

import java.io.Serializable;
import java.util.Comparator;

public class BodySenderFromUser implements Serializable {
    public BodySenderFromUser() {
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
        if (time.contains("SA")) {
            return time.replace("SA", "AM");
        } else if (time.contains("CH")) {
            return time.replace("CH", "PM");
        } else {
            return time;
        }
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

    public BodySenderFromUser(String reserveTableId, int quantity, String time, String restaurantId, String name, String phone, String promotionId, String note, String userId) {
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


    public class CustomComparator implements Comparator<BodySenderFromUser> {
        @Override
        public int compare(BodySenderFromUser o1, BodySenderFromUser o2) {
            return o1.getReserveTableId().compareTo(o2.getReserveTableId());
        }
    }

}
