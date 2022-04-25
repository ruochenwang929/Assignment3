package com.example.assignment3.profile.service;

import android.util.Log;

import com.example.assignment3.utils.Result;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserProfileService {
    private final static String TAG = "userPorfileService";
    private CollectionReference userProfileRef;

    public UserProfileService() {
        userProfileRef = FirebaseFirestore.getInstance().collection("userProfiles");
    }

    public Result addUserProfile(UserProfile profile) {
        Result result = new Result();

        userProfileRef.add(profile)
                .addOnSuccessListener(documentReference -> {
                    Log.w(TAG, "Successfully add user profile");
                    result.setState(true);
                    result.setMessage("Successfully add user profile");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document", e);
                    result.setState(false);
                    result.setMessage(e.getLocalizedMessage());
                });
        return result;
    }

}
