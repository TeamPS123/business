package com.psteam.lib.Models.Get;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class getMenu implements Serializable {
    public getMenu(String menuId, String name, ArrayList<getFood> foodList) {
        this.menuId = menuId;
        this.name = name;
        this.foodList = foodList;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<getFood> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<getFood> foodList) {
        this.foodList = foodList;
    }

    private String menuId ;
    private String name ;
    private ArrayList<getFood> foodList ;
}
