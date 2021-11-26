package com.psteam.foodlocationbusiness.socket.models;

import java.io.Serializable;

public class BodySenderFromRes implements Serializable {
    public BodySenderFromRes(String notification, String reserveTableId) {
        this.notification = notification;
        this.reserveTableId = reserveTableId;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getReserveTableId() {
        return reserveTableId;
    }

    public void setReserveTableId(String reserveTableId) {
        this.reserveTableId = reserveTableId;
    }

    private String notification;
    private String reserveTableId;
}
