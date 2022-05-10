package com.example.assignment3.gym;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlaceViewModel extends ViewModel {
    private MutableLiveData<String> name;
    private MutableLiveData<String> location;
    private MutableLiveData<Double> rating;
    private MutableLiveData<String> weekday;
    private MutableLiveData<String> photo;
    private String googleApiKey;
    private boolean ifNewMarkClicked;

    public PlaceViewModel() {
        name = new MutableLiveData<>();
        location = new MutableLiveData<>();
        rating = new MutableLiveData<>();
        weekday = new MutableLiveData<>();
        photo = new MutableLiveData<>();
        ifNewMarkClicked = false;
    }

    public LiveData<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public LiveData<String> getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location.setValue(location);
    }

    public LiveData<Double> getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating.setValue(rating);
    }

    public LiveData<String> getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday.setValue(weekday);
    }

    public LiveData<String> getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo.setValue(photo);
    }


    public String getGoogleApiKey() {
        return googleApiKey;
    }

    public void setGoogleApiKey(String googleApiKey) {
        this.googleApiKey = googleApiKey;
    }


    public boolean isIfNewMarkClicked() {
        return ifNewMarkClicked;
    }

    public void setIfNewMarkClicked(boolean ifNewMarkClicked) {
        this.ifNewMarkClicked = ifNewMarkClicked;
    }
}