package com.example.assignment3.plan;


import android.util.Log;

import androidx.work.WorkManager;

import com.example.assignment3.entity.WorkoutPlan;
import com.example.assignment3.profile.service.UserProfile;
import com.example.assignment3.utils.Result;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlanService {
    private final static String TAG = "planService";
    private CollectionReference planRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PlanService() {
        planRef = db.collection("Plans");
    }

    public Result addPlans(WorkoutPlan plan) {
        Result result = new Result();
        result.setState(true);

        Map<String, Object> planMap = new HashMap<>();
        planMap.put("Name", plan.getPlanName());
        planMap.put("Length", plan.getPlanLength());
        planMap.put("Time", plan.getTime());
        planMap.put("Category", plan.getCategory());
        planMap.put("Routine", plan.getPlanRoutine());

        planRef.document(""+plan.getPlanID()).set(planMap)
                .addOnSuccessListener(documentReference -> {
                    Log.w(TAG, "Successfully uploading Room data to Firebase. Plan Id: "+plan.getPlanID());
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document, Plan Id: "+plan.getPlanName(), e);
                    result.setState(false);
                    result.setMessage(e.getLocalizedMessage());
                });

        return result;
    }

}
