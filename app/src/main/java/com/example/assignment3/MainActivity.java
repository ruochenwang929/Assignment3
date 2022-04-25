package com.example.assignment3;


import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Home");
    }

    @Override
    public void onBackPressed(){
        FirebaseAuth.getInstance().signOut();
        super.onBackPressed();
    }
}