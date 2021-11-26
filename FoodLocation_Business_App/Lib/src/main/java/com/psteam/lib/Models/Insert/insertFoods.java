package com.psteam.lib.Models.Insert;

import java.util.List;

public class insertFoods {
    public insertFoods(String menuId, String userId, List<food> foods) {
        this.menuId = menuId;
        this.userId = userId;
        this.foods = foods;
    }

    public String getMenuId() {
        return menuId;
    }

    public String getUserId() {
        return userId;
    }

    public List<food> getFoods() {
        return foods;
    }

    private String menuId ;
    private String userId ;
    private List<food> foods ;
}
