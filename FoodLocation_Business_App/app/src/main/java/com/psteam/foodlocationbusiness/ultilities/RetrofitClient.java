package com.psteam.foodlocationbusiness.ultilities;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static Retrofit retrofitGoogleMap;
    private final static String BASE_URL="https://provinces.open-api.vn";
    private final static String GOOGLE_MAP_URL = "https://maps.googleapis.com";
    public static Retrofit getRetrofit(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }

    public static Retrofit getRetrofitGoogleMapAPI() {
        if (retrofitGoogleMap == null) {
            retrofitGoogleMap = new Retrofit.Builder().baseUrl(GOOGLE_MAP_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitGoogleMap;
    }

    public static String getGoogleMapUrl(){return GOOGLE_MAP_URL;}
}
