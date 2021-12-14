package com.psteam.lib.Models.Get;

public class messageStatistic {
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

    public getStatistic getGetStatic() {
        return getStatic;
    }

    public void setGetStatic(getStatistic getStatic) {
        this.getStatic = getStatic;
    }

    private int status;
    private String notification;
    private getStatistic getStatic;

    public messageStatistic(int status, String notification, getStatistic getStatic) {
        this.status = status;
        this.notification = notification;
        this.getStatic = getStatic;
    }
}
