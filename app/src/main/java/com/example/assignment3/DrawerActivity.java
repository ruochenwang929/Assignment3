package com.example.assignment3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.assignment3.auth.LoginActivity;
import com.example.assignment3.gym.GymActivity;
import com.example.assignment3.report.ReportActivity;
import com.example.assignment3.userProfile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;
    public FrameLayout frameLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_drawer);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,  R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(View view) {
        frameLayout = findViewById(R.id.drawer_frame);
        frameLayout.addView(view);
        //LayoutInflater.from(this).inflate(layoutResID, frameLayout);
        //super.setContentView(drawerLayout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.nav_home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
                finish();
                return true;
            case R.id.nav_plan:
                // TODO: jump to another activity
                finish();
                return true;
            case R.id.nav_map:
                Intent gymIntent = new Intent(this, GymActivity.class);
                startActivity(gymIntent);
                finish();
                return true;
            case R.id.nav_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            case R.id.nav_report:
                Intent reportIntent = new Intent(this, ReportActivity.class);
                startActivity(reportIntent);
                return true;
            default:
                finish();
                return true;
        }
    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void addContentView(View view) {
        drawerLayout.addView(view,0);
    }
}