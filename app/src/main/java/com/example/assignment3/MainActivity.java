package com.example.assignment3;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.assignment3.entity.WorkoutPlan;
import com.example.assignment3.gym.GymActivity;
import com.example.assignment3.plan.PlanActivity;
import com.example.assignment3.plan.PlanService;
import com.example.assignment3.plan.RecyclerViewAdapter;
import com.example.assignment3.report.ReportActivity;
import com.example.assignment3.viewmodel.PlanViewModel;
import com.example.assignment3.weather.Root;
import com.example.assignment3.weather.WeatherApiInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends DrawerActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private CardView gymNearMeButton;
    private TextView tempTextView;
    private ImageView weatherImage;
    private TextView mainTextView;
    private Button workManagerBtn;

    private CardView allPlan;
    private CardView report;

    private RecyclerViewAdapter adapter;
    private PlanViewModel planViewModel;
    private List<WorkoutPlan> plans;
    private PlanService planService;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    //private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mainView = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(mainView);
        this.setTitle("Home");
        gymNearMeButton = findViewById(R.id.findGymButton);
        gymNearMeButton.setOnClickListener(l -> onGymNearMeClicked());


        allPlan = findViewById(R.id.allPlanButton);
        allPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        PlanActivity.class);
                startActivity(intent);
            } });

        report = findViewById(R.id.reportButton);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        ReportActivity.class);
                startActivity(intent);
            } });


        tempTextView = findViewById(R.id.tempTextView);
        weatherImage = findViewById(R.id.weatherImage);
        mainTextView = findViewById(R.id.mainTextView);


        int brightness = -35; //RGB offset. Negative numbers indicate darkening
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(matrix);

        //The Retrofit class generates an implementation of the weatherApi interface.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create()) //Convert extracted data to Gson
                .build();

        //create retrofit
        WeatherApiInterface weatherApiInterface = retrofit.create(WeatherApiInterface.class);

        Call<Root> call = weatherApiInterface.getWeather();

        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) { //response gets the JSON file
                Root root = response.body();
                double a = root.getMain().getTemp();
                int b = (int) (a - 273.15); //Change Kelvin to Celsius
                String temp = String.valueOf(b);
                tempTextView.setText(temp + "℃");

                String str = root.getWeather().get(0).getMain();

                if (str.equals("Rain"))
                {
                    weatherImage.setImageDrawable(getResources().getDrawable(R.drawable.rain));
                    mainTextView.setText(str + ", recommended indoor exercise");
                }
                else if (str.equals("Extreme"))
                {
                    weatherImage.setImageDrawable(getResources().getDrawable(R.drawable.extreme));
                    mainTextView.setText(str + ", recommended indoor exercise");
                }
                else if (str.equals("Snow"))
                {
                    weatherImage.setImageDrawable(getResources().getDrawable(R.drawable.snow));
                    weatherImage.setColorFilter(cmcf);
                    mainTextView.setText(str + ", recommended indoor exercise");
                }
                else
                {
                    //clear
                    weatherImage.setImageDrawable(getResources().getDrawable(R.drawable.clear));
                    weatherImage.setColorFilter(cmcf);
                    mainTextView.setText(str + ", recommended outdoor exercise");
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                System.out.println("Error," + t.getMessage());
            }

        });

        //Work Manager
        planService = new PlanService();
        plans = new ArrayList<>();
        planViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(PlanViewModel.class);
        planViewModel.getAllWorkoutPlan().observe(this, new
                Observer<List<WorkoutPlan>>() {
                    @Override
                    public void onChanged(@Nullable List<WorkoutPlan> workoutPlans) {
                        plans.addAll(workoutPlans);
                    }
                });


        //One time WorkRequest for trigger button
        workManagerBtn = findViewById(R.id.workManager);
        workManagerBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                for (WorkoutPlan p:plans) {
                    String strPlan = new Gson().toJson(p);
                    Data data = new Data.Builder()
                            .putString("plan", strPlan)
                            .build();
                    OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(UploadManager.class)
                            .setInputData(data)
                            .build();

                    PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(UploadManager.class, 24, TimeUnit.HOURS)
                    .setInputData(data)
                    .addTag("upload Room data to Firebase")
                    .build();

                    WorkManager.getInstance(v.getContext()).enqueue(oneTimeWorkRequest);
                    WorkManager.getInstance(v.getContext()).enqueue(periodicWorkRequest);

                }
            }
        });


//        Map<String, Object> city = new HashMap<>();
//        city.put("Address", "LA");
//        city.put("Email", "aaa@bbb.com");
//        city.put("Name", "Jacky Chen");
//
//        db.collection("Test").document("Profile2")
//                .set(city)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(MainActivity.this, "The user data is successfully added", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, "Error with Firestore", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    @Override
    public void onBackPressed(){
        FirebaseAuth.getInstance().signOut();
        super.onBackPressed();
    }

    public void onGymNearMeClicked() {
        if (!checkPermissionGranted()) {
            getLocationPermission();
            return;
        }

        Intent intent = new Intent(this, GymActivity.class);
        startActivity(intent);
    }

    private void getLocationPermission() {
        String[] permission = {FINE_LOCATION, COURSE_LOCATION};

        if (!checkPermissionGranted())
            ActivityCompat.requestPermissions(this, permission, 1);
    }

    private boolean checkPermissionGranted() {
        return (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                &&
                (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //If user presses allow
                    Intent intent = new Intent(this, GymActivity.class);
                    startActivity(intent);
                } else {
                    //If user presses deny
                    Toast.makeText(this, "Please allow position permission in settings", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}