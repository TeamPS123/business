package com.psteam.lib.Models.Get;

public class getCategory {
    private String name;
    private String id;
    private boolean status;

    public getCategory(String name, String id, boolean status) {
        this.name = name;
        this.id = id;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }
}
