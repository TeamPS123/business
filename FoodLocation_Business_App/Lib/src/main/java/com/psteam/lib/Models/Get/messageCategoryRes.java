package com.psteam.lib.Models.Get;

import java.util.ArrayList;

public class messageCategoryRes {
    public messageCategoryRes(int status, String notification, ArrayList<getCategoryRes> categoryResList) {
        this.status = status;
        this.notification = notification;
        this.categoryResList = categoryResList;
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

    public ArrayList<getCategoryRes> getCategoryResList() {
        return categoryResList;
    }

    public void setCategoryResList(ArrayList<getCategoryRes> categoryResList) {
        this.categoryResList = categoryResList;
    }

    private int status;
    private String notification;
    private ArrayList<getCategoryRes> categoryResList;
}
