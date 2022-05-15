package com.example.assignment3.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.assignment3.dao.PlanDAO;
import com.example.assignment3.database.PlanDatabase;
import com.example.assignment3.entity.WorkoutPlan;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class PlanRepository {
    private PlanDAO planDao;
    private LiveData<List<WorkoutPlan>> allWorkoutPlan;
    public PlanRepository(Application application){
        PlanDatabase db = PlanDatabase.getInstance(application);
        planDao =db.planDao();
        allWorkoutPlan= planDao.getAll();
    }

    public LiveData<List<WorkoutPlan>> getAllWorkoutPlan() {
        return allWorkoutPlan;
    }

    public void insert(final WorkoutPlan workoutPlan){
        PlanDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                planDao.insert(workoutPlan);
            }
        });
    }

    public void deleteAll(){
        PlanDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                planDao.deleteAll();
            }
        });
    }

    public void delete(final WorkoutPlan workoutPlan){
        PlanDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                planDao.delete(workoutPlan);
            }
        });
    }
    public void updateWorkoutPlan(final WorkoutPlan workoutPlan){
        PlanDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                planDao.updateWorkoutPlan(workoutPlan);
            }
        });
    }

    //@RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<WorkoutPlan> findByIDFuture(final int workoutplanId) {
        return CompletableFuture.supplyAsync(new Supplier<WorkoutPlan>() {
            @Override
            public WorkoutPlan get() {
                return planDao.findByID(workoutplanId);
            }
        }, PlanDatabase.databaseWriteExecutor);
    }
}

