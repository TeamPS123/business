package com.psteam.lib.Models.Get;

public class getStatistic {
    public getStatistic(String amountWait, String amountDeny, String amountConfirm, String amountComplete, String amountExpired) {
        this.amountWait = amountWait;
        this.amountDeny = amountDeny;
        this.amountConfirm = amountConfirm;
        this.amountComplete = amountComplete;
        this.amountExpired = amountExpired;
    }

    public String getAmountWait() {
        return amountWait;
    }

    public void setAmountWait(String amountWait) {
        this.amountWait = amountWait;
    }

    public String getAmountDeny() {
        return amountDeny;
    }

    public void setAmountDeny(String amountDeny) {
        this.amountDeny = amountDeny;
    }

    public String getAmountConfirm() {
        return amountConfirm;
    }

    public void setAmountConfirm(String amountConfirm) {
        this.amountConfirm = amountConfirm;
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

    private String amountWait ;
    private String amountDeny ;
    private String amountConfirm ;
    private String amountComplete ;
    private String amountExpired ;
}
