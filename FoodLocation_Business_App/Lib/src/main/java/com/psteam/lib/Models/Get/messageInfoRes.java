package com.psteam.lib.Models.Get;

public class messageInfoRes {
    public messageInfoRes(int status, String notification, getInfoRes res) {
        this.status = status;
        this.notification = notification;
        this.res = res;
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

    public getInfoRes getRes() {
        return res;
    }

    public void setRes(getInfoRes res) {
        this.res = res;
    }

    private int status;
    private String notification;
    private getInfoRes res;
}
