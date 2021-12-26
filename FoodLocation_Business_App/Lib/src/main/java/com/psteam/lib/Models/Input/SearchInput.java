package com.psteam.lib.Models.Input;

public class SearchInput
{
    private String restaurantId;

    private String userId;

    private String key;

    public String getRestaurantId ()
    {
        return restaurantId;
    }

    public void setRestaurantId (String restaurantId)
    {
        this.restaurantId = restaurantId;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getKey ()
    {
        return key;
    }

    public void setKey (String key)
    {
        this.key = key;
    }

    public SearchInput(String restaurantId, String userId, String key) {
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.key = key;
    }
}