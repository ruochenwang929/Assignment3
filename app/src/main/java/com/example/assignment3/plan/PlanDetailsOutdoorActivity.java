package com.example.assignment3.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.assignment3.DrawerActivity;
import com.example.assignment3.MainActivity;
import com.example.assignment3.databinding.ActivityPlanDetailsBinding;
import com.example.assignment3.databinding.ActivityPlanDetailsOutdoorBinding;

public class PlanDetailsOutdoorActivity extends DrawerActivity {

    private ActivityPlanDetailsOutdoorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlanDetailsOutdoorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanDetailsOutdoorActivity.this,
                        PlanActivity.class);
                startActivity(intent);
            } });

    }
}