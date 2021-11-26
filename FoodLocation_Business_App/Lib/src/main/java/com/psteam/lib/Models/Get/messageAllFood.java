package com.psteam.lib.Models.Get;

import java.util.ArrayList;

public class messageAllFood {
    public messageAllFood(int status, String notification, ArrayList<getFood> foodList) {
        this.status = status;
        this.notification = notification;
        this.foodList = foodList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public ArrayList<getFood> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<getFood> foodList) {
        this.foodList = foodList;
    }

    private int status;
    private String notification;
    private ArrayList<getFood> foodList;
}
