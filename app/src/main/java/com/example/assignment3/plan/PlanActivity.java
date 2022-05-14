package com.example.assignment3.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.DrawerActivity;
import com.example.assignment3.R;
import com.example.assignment3.databinding.ActivityPlanBinding;
import com.example.assignment3.entity.WorkoutPlan;
import com.example.assignment3.viewmodel.PlanViewModel;


import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends DrawerActivity {

    private ActivityPlanBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private List<WorkoutPlan> plans;
    private RecyclerViewAdapter adapter;
    private PlanViewModel planViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);
        this.setTitle("Workout Plan");

        plans = new ArrayList<WorkoutPlan>();

        planViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(PlanViewModel.class);
        planViewModel.getAllWorkoutPlan().observe(this, new
                Observer<List<WorkoutPlan>>() {
                    @Override
                    public void onChanged(List<WorkoutPlan> workoutPlans) {
                        plans = workoutPlans;
                    }
                });

        plans.add(new WorkoutPlan("plan1","30","30","Indoor","routines"));
        plans.add(new WorkoutPlan("plan2","20","20","Outdoor","routines"));

        adapter = new RecyclerViewAdapter(plans);
        binding.recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);

    }

    //Only Show add icon in Plan screen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}