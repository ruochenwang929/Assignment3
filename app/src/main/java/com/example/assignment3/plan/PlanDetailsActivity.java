package com.example.assignment3.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment3.DrawerActivity;
import com.example.assignment3.MainActivity;
import com.example.assignment3.databinding.ActivityPlanDetailsBinding;
import com.example.assignment3.entity.TimeStampEntity;
import com.example.assignment3.entity.WorkoutPlan;
import com.example.assignment3.viewmodel.TimeStampViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class PlanDetailsActivity extends DrawerActivity {

    private ActivityPlanDetailsBinding binding;
    private com.example.assignment3.viewmodel.TimeStampViewModel TimeStampViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlanDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.setTitle("Plan Details");

        Bundle bundle = getIntent().getExtras();

        int planID = (Integer) bundle.get("planID");
        binding.name.setText((String) bundle.get("name"));
        binding.length.setText((String) bundle.get("length") + " days");
        binding.time.setText((String) bundle.get("time") + " minutes");
        binding.category.setText((String) bundle.get("category"));
        binding.routine.setText((String) bundle.get("routine"));




        TimeStampViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(com.example.assignment3.viewmodel.TimeStampViewModel.class);
        TimeStampViewModel.getAllTimeStamp().observe(this, new Observer<List<TimeStampEntity>>() {
            @Override
            public void onChanged(@Nullable final List<TimeStampEntity> timeStamps) {
                String AllTimeStamp = "";
                for (TimeStampEntity temp : timeStamps) {
                    String timeStampDetails = ("");
                    AllTimeStamp = AllTimeStamp + System.getProperty("line.separator") + timeStampDetails;
                }
            }
        });


        binding.completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStamp time = new TimeStamp();
                time.logTimeStamp();
                time.logTime();
                String timeStampBox = time.getTime();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = user.getEmail();
                if ((!timeStampBox.isEmpty()) && !(planID==0)) {
                    TimeStampEntity timeStamp = new TimeStampEntity(planID,timeStampBox,email);
                    TimeStampViewModel.insert(timeStamp);
                    Toast.makeText(PlanDetailsActivity.this," added successfully! ",Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(PlanDetailsActivity.this," field! ",Toast.LENGTH_SHORT).show();
                }

            } });

    }
}