package com.psteam.foodlocationbusiness.services;


import com.psteam.foodlocationbusiness.models.DistrictModel;
import com.psteam.foodlocationbusiness.models.GoogleMapApiModels.DirectionResponses;
import com.psteam.foodlocationbusiness.models.ProvinceModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI {
    @GET("api/p/")
    Call<ArrayList<ProvinceModel>> GetProvinces();

    @GET("api/p/{code}?depth=2")
    Call<ProvinceModel> GetDistricts(@Path("code") String code);

    @GET("api/d/{code}?depth=2")
    Call<DistrictModel> GetWards(@Path("code") String code);

    @GET("maps/api/directions/json")
    Call<DirectionResponses> getDirection(@Query("origin") String origin,
                                          @Query("destination") String destination,
                                          @Query("key") String apiKey);

    @GET("maps/api/directions/json")
    Call<DirectionResponses> getLatLng(@Query("address") String origin,
                                          @Query("key") String apiKey);
}
