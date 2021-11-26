package com.psteam.foodlocationbusiness.ultilities;

import com.google.android.gms.maps.model.Marker;

public class Para {
    public static double latitude = 0;
    public static double longitude = 0;
    public static String currentAddress = "";
    public static Marker marker;
    public static String cityCode="79";
    public static int numberTabs;

    public static String currentLatLng() {
        return String.format("%s, %s", latitude, longitude);
    }
}
