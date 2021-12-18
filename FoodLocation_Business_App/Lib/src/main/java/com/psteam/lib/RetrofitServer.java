package com.psteam.lib;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServer {

    private static Retrofit retrofit;
    private static String BASE_URL = "https://pamle.pro/api/";
    private static String GOOGLE_MAP_URL = "https://maps.googleapis.com";

    public static Retrofit getRetrofit_lib() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitGoogleMapAPI() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(GOOGLE_MAP_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String getBase_Url() {
        return BASE_URL;
    }

    public static String getGoogleMap_Url() {
        return GOOGLE_MAP_URL;
    }
}
