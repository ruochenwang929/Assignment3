package com.example.assignment3.gym;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceList {
    @SerializedName("results")
    List<PlaceModel> placeModelList;

    public List<PlaceModel> getPlaceModelList() {
        return placeModelList;
    }

    public void setPlaceModelList(List<PlaceModel> placeModelList) {
        this.placeModelList = placeModelList;
    }
}
