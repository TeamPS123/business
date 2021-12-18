package com.psteam.lib.Models.Get;

import java.util.List;

public class messageStatistic {
    public messageStatistic(int status, String notification, List<getStatistic> getStatis) {
        this.status = status;
        this.notification = notification;
        this.getStatis = getStatis;
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

    public List<getStatistic> getGetStatic() {
        return getStatis;
    }

    public void setGetStatic(List<getStatistic> getStatis) {
        this.getStatis = getStatis;
    }

    private int status;
    private String notification;
    private List<getStatistic> getStatis;

}
