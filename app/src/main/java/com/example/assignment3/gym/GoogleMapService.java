package com.example.assignment3.gym;

import okhttp3.ResponseBody;
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
    Call<ResponseBody> getImageByPhotoReference(@Query("photo_reference") String ref,
                                                @Query("maxheight") Integer maxHeight,
                                                @Query("maxwidth") Integer maxWidth,
                                                @Query("key") String mapKey);

    @GET("place/details/json")
    Call<PlaceDetail> getPlaceDetail(@Query("place_id") String placeId,
                                      @Query("key") String mapKey);
}
