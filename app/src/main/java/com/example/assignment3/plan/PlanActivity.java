package com.example.assignment3.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.assignment3.DrawerActivity;
import com.example.assignment3.databinding.ActivityPlanBinding;

public class PlanActivity extends DrawerActivity {

    private ActivityPlanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        //click f1
        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanActivity.this, PlanDetailsActivity.class);
                startActivity(intent);
            } });


        //click f2
        binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanActivity.this, PlanDetailsOutdoorActivity.class);
                startActivity(intent);
            } });

        //click f3
        binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanActivity.this, OwnPlanDetailsActivity.class);
                startActivity(intent);
            } });
    }
}