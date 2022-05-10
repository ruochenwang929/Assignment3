package com.example.assignment3;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.assignment3.gym.GymActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends DrawerActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private CardView gymNearMeButton;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mainView = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(mainView);
        this.setTitle("Home");
        gymNearMeButton = findViewById(R.id.findGymButton);
        gymNearMeButton.setOnClickListener(l -> onGymNearMeClicked());
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