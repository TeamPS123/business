package com.psteam.lib.Models.Get;

import java.util.ArrayList;
import java.util.List;

public class messageAllCategory {
    public messageAllCategory(int status, String notification, ArrayList<getCategory> categories) {
        this.status = status;
        this.notification = notification;
        this.categories = categories;
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

    public ArrayList<getCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<getCategory> categories) {
        this.categories = categories;
    }

    private int status;
    private String notification;
    private ArrayList<getCategory> categories;
}
