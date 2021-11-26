package com.psteam.lib.Models.Insert;

import java.util.List;

public class reserveFood {
    public reserveFood(String userId, String reserveTableId, List<food1> foods) {
        this.userId = userId;
        this.reserveTableId = reserveTableId;
        this.foods = foods;
    }

    private String userId;
    private String reserveTableId;
    private List<food1> foods;
}
