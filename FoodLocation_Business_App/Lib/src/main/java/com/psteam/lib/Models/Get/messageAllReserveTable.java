package com.psteam.lib.Models.Get;

import java.util.ArrayList;

public class messageAllReserveTable {
    public messageAllReserveTable(int status, String notification, ArrayList<getReserveTable> reserveTables) {
        this.status = status;
        this.notification = notification;
        this.reserveTables = reserveTables;
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

    public ArrayList<getReserveTable> getReserveTables() {
        return reserveTables;
    }

    public void setReserveTables(ArrayList<getReserveTable> reserveTables) {
        this.reserveTables = reserveTables;
    }

    private int status;
    private String notification;
    private ArrayList<getReserveTable> reserveTables;
}
