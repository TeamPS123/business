package com.psteam.lib.Models.Get;

import java.util.List;

public class messageAllMenu {
    public messageAllMenu(int status, String notification, List<getMenu> menuList) {
        this.status = status;
        this.notification = notification;
        this.menuList = menuList;
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

    public List<getMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<getMenu> menuList) {
        this.menuList = menuList;
    }

    private int status;
    private String notification;
    private List<getMenu> menuList;
}
