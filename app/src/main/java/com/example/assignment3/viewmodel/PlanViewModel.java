package com.example.assignment3.viewmodel;


import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.assignment3.entity.WorkoutPlan;
import com.example.assignment3.repository.PlanRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlanViewModel extends AndroidViewModel {
    private PlanRepository cRepository;
    private LiveData<List<WorkoutPlan>> allWorkoutPlan;
    public PlanViewModel(Application application) {
        super(application);
        cRepository = new PlanRepository(application);
        allWorkoutPlan = cRepository.getAllWorkoutPlan();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<WorkoutPlan> findByIDFuture(final int workoutplanId){
        return cRepository.findByIDFuture(workoutplanId);
    }
    public LiveData<List<WorkoutPlan>> getAllWorkoutPlan() {
        return allWorkoutPlan;
    }
    public void insert(WorkoutPlan workoutplan) {
        cRepository.insert(workoutplan);
    }

    public void deleteAll() {
        cRepository.deleteAll();
    }
    public void update(WorkoutPlan workoutplan) {
        cRepository.updateWorkoutPlan(workoutplan);
    }
}
