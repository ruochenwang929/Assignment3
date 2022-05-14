package com.example.assignment3.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.DrawerActivity;
import com.example.assignment3.MainActivity;
import com.example.assignment3.databinding.ActivityPlanDetailsBinding;

public class PlanDetailsActivity extends DrawerActivity {

    private ActivityPlanDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlanDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.setTitle("Plan Details");

        Bundle bundle = getIntent().getExtras();

        binding.name.setText((String) bundle.get("name"));
        binding.length.setText((String) bundle.get("length") + " days");
        binding.time.setText((String) bundle.get("time") + " minutes");
        binding.category.setText((String) bundle.get("category"));
        binding.routine.setText((String) bundle.get("routine"));



        binding.completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanDetailsActivity.this,
                        PlanActivity.class);
                startActivity(intent);
            } });

    }
}