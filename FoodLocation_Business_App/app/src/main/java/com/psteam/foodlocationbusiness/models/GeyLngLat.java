package com.psteam.foodlocationbusiness.models;

import java.util.ArrayList;

public class GeyLngLat {
    private ArrayList<Results> results;

    private String status;

    public ArrayList<Results> getResults ()
    {
        return results;
    }

    public void setResults (ArrayList<Results> results)
    {
        this.results = results;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }
}
