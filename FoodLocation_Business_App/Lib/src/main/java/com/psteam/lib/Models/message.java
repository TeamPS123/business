package com.psteam.lib.Models;

public class message {

    public message(int status, String notification, String id) {
        this.status = status;
        this.notification = notification;
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public String getNotification() {
        return notification;
    }

    public String getId() {
        return id;
    }

    private int status;
    private String notification;
    private String id;
}
