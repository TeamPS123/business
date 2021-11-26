package com.psteam.lib.Models.Input;

public class confirmTable {


    public confirmTable(String userId, String reserveTableId, int status) {
        this.userId = userId;
        this.reserveTableId = reserveTableId;
        this.status = status;
    }

    private String userId;
    private String reserveTableId;
    private int status;
}
