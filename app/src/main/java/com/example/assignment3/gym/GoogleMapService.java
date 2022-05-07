package com.example.assignment3.gym;

import android.content.Intent;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapService {
    String MAP_URL = "https://maps.googleapis.com/maps/api/";

    @GET("place/nearbysearch/json")
    Call<PlaceList> searchNearbyLocationByKeyword(@Query("location") String location,
                                                   @Query("type") String keyword,
                                                   @Query("radius") Integer radius,
                                                   @Query("key") String mapKey);

    @GET("place/photo")
    Call<JSONObject> getImageByPhotoReference(@Query("photo_reference") String ref,
                                              @Query("height") Integer maxHeight,
                                              @Query("width") Integer maxWidth,
                                              @Query("key") String mapKey);
}
