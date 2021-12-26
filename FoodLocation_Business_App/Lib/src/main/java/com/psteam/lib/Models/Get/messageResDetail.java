package com.psteam.lib.Models.Get;

public class messageResDetail {
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

    public getResDetail getResDetail() {
        return resDetail;
    }

    public void setResDetail(getResDetail resDetail) {
        this.resDetail = resDetail;
    }

    private int status;
    private String notification;
    private getResDetail resDetail;


    public messageResDetail(int status, String notification, getResDetail resDetail) {
        this.status = status;
        this.notification = notification;
        this.resDetail = resDetail;
    }
}
