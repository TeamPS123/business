package com.psteam.lib.Models.Get;

public class getResDetail {
    public getResDetail(int amountDay, int amountWeek, boolean status, String statusCo) {
        this.amountDay = amountDay;
        this.amountWeek = amountWeek;
        this.status = status;
        this.statusCo = statusCo;
    }

    public int getAmountDay() {
        return amountDay;
    }

    public void setAmountDay(int amountDay) {
        this.amountDay = amountDay;
    }

    public int getAmountWeek() {
        return amountWeek;
    }

    public void setAmountWeek(int amountWeek) {
        this.amountWeek = amountWeek;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private int amountDay;
    private int amountWeek;
    private boolean status;
    private String statusCo;

    public String getStatusCo() {
        return statusCo;
    }

    public void setStatusCo(String statusCo) {
        this.statusCo = statusCo;
    }
}
