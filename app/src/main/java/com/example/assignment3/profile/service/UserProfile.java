package com.example.assignment3.profile.service;

import com.google.firebase.firestore.PropertyName;

public class UserProfile {
    @PropertyName("userUID")
    private String userUID;
    @PropertyName("firstName")
    private String firstName;
    @PropertyName("lastName")
    private String lastName;
    @PropertyName("address")
    private String address;


    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
