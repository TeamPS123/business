package com.psteam.lib.Models.Get;

public class getResDetail {
    public getResDetail(int amountDay, int amountWeek, boolean status) {
        this.amountDay = amountDay;
        this.amountWeek = amountWeek;
        this.status = status;
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
}
