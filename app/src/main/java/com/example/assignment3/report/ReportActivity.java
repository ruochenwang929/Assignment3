package com.example.assignment3.report;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.assignment3.DrawerActivity;
import com.example.assignment3.R;
import com.example.assignment3.databinding.ActivityReportBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends DrawerActivity {

    String[] chartTypes = {"Bar Chart","Pie Chart"};

    private ActivityReportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, chartTypes);
        binding.chartSpinner.setAdapter(spinnerAdapter);



    }


}