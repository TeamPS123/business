package com.psteam.foodlocationbusiness.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class RestaurantModel implements ClusterItem {

    private String name;
    private String address;
    private String distance;
    private int image;

    private String latLng;
    private double latitude;
    private double longitude;


    public RestaurantModel(String name, String address, String distance, int image) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.image = image;
    }

    public RestaurantModel(String name, String address, String distance, int image, String latLng) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.image = image;
        this.latLng = latLng;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public double getLatitude() {
        String[] latLng_Array = getLatLng().split(",");
        return Double.parseDouble(latLng_Array[0]);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LatLng LatLng() {
        String[] latLng_Array = getLatLng().split(",");
        LatLng latLng = new LatLng(Double.parseDouble(latLng_Array[0]), Double.parseDouble(latLng_Array[1]));
        return latLng;
    }

    public double getLongitude() {
        String[] latLng_Array = getLatLng().split(",");
        return Double.parseDouble(latLng_Array[1]);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return LatLng();
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }
}
