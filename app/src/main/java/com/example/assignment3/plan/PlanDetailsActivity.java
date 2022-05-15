package com.example.assignment3.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.DrawerActivity;
import com.example.assignment3.MainActivity;
import com.example.assignment3.databinding.ActivityPlanDetailsBinding;
import com.example.assignment3.userProfile.EditProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PlanDetailsActivity extends DrawerActivity {

    private ActivityPlanDetailsBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = user.getEmail();
                int planId = (int) bundle.get("id");
                String planName = (String) bundle.get("name");
                String planTime = (String) bundle.get("time");
                long timestamp = Calendar.getInstance().getTimeInMillis();

                Map<String, Object> record = new HashMap<>();
                record.put("email",email);
                record.put("PlanId",planId);
                record.put("PlanName",planName);
                record.put("PlanTime",planTime);
                record.put("RecordTime",timestamp);


                CollectionReference recordRef = db.collection("Workout Record");
                recordRef.document(email+timestamp)
                        .set(record)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PlanDetailsActivity.this, "You have complete the plan successfully! ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                                                  @Override
                                                  public void onFailure(@NonNull Exception e) {
                                                      System.out.println("Error--------");
                                                      Toast.makeText(PlanDetailsActivity.this, "Error: Please try again later", Toast.LENGTH_SHORT).show();
                                                  }
                                              });

            } });

    }
}