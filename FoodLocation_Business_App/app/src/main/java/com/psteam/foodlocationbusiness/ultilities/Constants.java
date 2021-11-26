package com.psteam.foodlocationbusiness.ultilities;

public class Constants {
    public static final String KEY_PREFERENCE_NAME = "FoodLocationPreference";
    public static final String PACKAGE_NAME = "com.psteam.foodlocation";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_CITY = PACKAGE_NAME + ".RESULT_CITY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final int SUCCESS_RESULT = 1;
    public static final int FAILURE_RESULT = 0;
    public static final String TAG_RESTAURANT = "tag_restaurant";

    public static final int RADIUS = 5000;

    public static final int LOCATION_SERVICE_ID = 175;
    public static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";

    public static final int TAB_POSITION_PENDING = 0;
    public static final int TAB_POSITION_PROCESSING = 1;
    public static final int TAB_POSITION_CONFIRMED = 2;
    public static final int TAB_POSITION_LATE = 3;

    public static final int FLAG_CHANGE_PASSWORD = 1;
    public static final int FLAG_UPDATE_USER_INFO = 2;
}
