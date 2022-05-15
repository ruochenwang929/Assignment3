package com.example.assignment3;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.assignment3.entity.WorkoutPlan;
import com.example.assignment3.plan.PlanService;
import com.example.assignment3.repository.PlanRepository;
import com.example.assignment3.viewmodel.PlanViewModel;
import com.google.gson.Gson;

public class UpdateManager extends Worker {
    private static final String TAG = "WorkerManager";
    private WorkoutPlan plan;
    private PlanService service;
    private PlanViewModel planViewModel;

    public UpdateManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        service = new PlanService();

        String strPlan = getInputData().getString("plan");
        WorkoutPlan plan = new Gson().fromJson(strPlan,WorkoutPlan.class);

        System.out.println(plan.getPlanID());

        service.addPlans(plan);
        try{
            Thread.sleep(0);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ListenableWorker.Result.failure();
        }


        return ListenableWorker.Result.success();
    }
}

