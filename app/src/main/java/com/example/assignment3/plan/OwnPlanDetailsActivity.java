package com.example.assignment3.plan;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.DrawerActivity;
import com.example.assignment3.MainActivity;
import com.example.assignment3.databinding.ActivityOwnPlanDetailsBinding;

public class OwnPlanDetailsActivity extends DrawerActivity {

    private ActivityOwnPlanDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOwnPlanDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //click completed
        binding.button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnPlanDetailsActivity.this,
                        PlanActivity.class);
                startActivity(intent);
            } });

        //click edit
        binding.button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnPlanDetailsActivity.this,
                        EditPlanActivity.class);
                startActivity(intent);
            } });

        //click delete
        binding.button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnPlanDetailsActivity.this,
                        MainActivity.class);
                startActivity(intent);
            } });
    }
}
