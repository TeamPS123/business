package com.psteam.lib.Models.Get;

public class getStatistic {
    public getStatistic(String time, String amountComplete, String amountExpired) {
        this.time = time;
        this.amountComplete = amountComplete;
        this.amountExpired = amountExpired;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmountComplete() {
        return amountComplete;
    }

    public void setAmountComplete(String amountComplete) {
        this.amountComplete = amountComplete;
    }

    public String getAmountExpired() {
        return amountExpired;
    }

    public void setAmountExpired(String amountExpired) {
        this.amountExpired = amountExpired;
    }

    private String time;
    private String amountComplete ;
    private String amountExpired ;
}
