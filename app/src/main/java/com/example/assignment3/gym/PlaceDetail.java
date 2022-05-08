package com.example.assignment3.gym;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceDetail {

    @SerializedName("result")
    PlaceDetailInfo detail;

    public PlaceDetailInfo getDetail() {
        return detail;
    }

    public void setDetail(PlaceDetailInfo detail) {
        this.detail = detail;
    }

    public class PlaceDetailInfo {
        @SerializedName("name")
        private String name;
        @SerializedName("formatted_address")
        private String location;
        @SerializedName("photos")
        private List<PhotoRef> photoRefs;
        @SerializedName("rating")
        private double rating;

        @SerializedName("opening_hours")
        private OpeningHour openingHours;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public List<PhotoRef> getPhotoRef() {
            return photoRefs;
        }

        public void setPhotoRef(List<PhotoRef> photoRef) {
            this.photoRefs = photoRef;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public OpeningHour getOpeningHours() {
            return openingHours;
        }

        public void setOpeningHours(OpeningHour openingHours) {
            this.openingHours = openingHours;
        }


    }

    public class PhotoRef {
        @SerializedName("photo_reference")
        String photoRef;

        public String getPhotoRef() {
            return photoRef;
        }

        public void setPhotoRef(String photoRef) {
            this.photoRef = photoRef;
        }
    }

    public class OpeningHour {
        @SerializedName("weekday_text")
        private List<String> weekday;
        public List<String> getWeekday() {
            return weekday;
        }

        public void setWeekday(List<String> weekday) {
            this.weekday = weekday;
        }
    }

}
