package com.psteam.lib.Models.Get;

import java.util.ArrayList;

public class messageRestaurant {
    public int getStatus() {
        return status;
    }

    public String getNotification() {
        return notification;
    }

    public ArrayList<getRestaurant> getResList() {
        return resList;
    }

    private int status;
    private String notification;
    private ArrayList<getRestaurant> resList;

    public messageRestaurant(int status, String notification, ArrayList<getRestaurant> resList) {
        this.status = status;
        this.notification = notification;
        this.resList = resList;
    }
}
