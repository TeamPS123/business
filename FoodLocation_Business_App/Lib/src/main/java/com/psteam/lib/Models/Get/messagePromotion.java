package com.psteam.lib.Models.Get;

import java.util.ArrayList;

public class messagePromotion {
    public messagePromotion(int status, String notification, ArrayList<getPromotion> proList) {
        this.status = status;
        this.notification = notification;
        this.proList = proList;
    }

    public int getStatus() {
        return status;
    }

    public String getNotification() {
        return notification;
    }

    public ArrayList<getPromotion> getProList() {
        return proList;
    }

    private int status;
    private String notification;
    private ArrayList<getPromotion> proList;
}
