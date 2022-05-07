package com.example.assignment3.gym;

import java.util.List;

public class PlaceViewModel {
    private String name;
    private String location;
    private List<String> photoRef;
    private double rating;
    private String weekday;


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

    public List<String> getPhotoRef() {
        return photoRef;
    }

    public void setPhotoRef(List<String> photoRef) {
        this.photoRef = photoRef;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

}
