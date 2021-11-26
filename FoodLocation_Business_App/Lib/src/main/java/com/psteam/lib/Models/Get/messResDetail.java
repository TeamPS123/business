package com.psteam.lib.Models.Get;

public class messResDetail {

    public messResDetail(int status, String notification, getRestaurant restaurant) {
        this.status = status;
        this.notification = notification;
        this.restaurant = restaurant;
    }

    public int getStatus() {
        return status;
    }

    public String getNotification() {
        return notification;
    }

    public getRestaurant getRestaurant() {
        return restaurant;
    }

    private int status ;
    private String notification;
    private getRestaurant restaurant;
}
