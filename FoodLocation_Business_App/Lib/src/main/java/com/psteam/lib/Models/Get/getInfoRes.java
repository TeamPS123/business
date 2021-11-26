package com.psteam.lib.Models.Get;

public class getInfoRes {
    public getInfoRes(String name, String pic) {
        this.name = name;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    private String name;
    private String pic;
}
