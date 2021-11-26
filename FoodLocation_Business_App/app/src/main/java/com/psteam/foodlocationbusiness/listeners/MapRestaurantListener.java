package com.psteam.foodlocationbusiness.listeners;


import com.psteam.foodlocationbusiness.models.RestaurantModel;

public interface MapRestaurantListener {
    void onRestaurantGuideClicked(RestaurantModel restaurantModel);
    void onRestaurantClicked(RestaurantModel restaurantModel);
}
