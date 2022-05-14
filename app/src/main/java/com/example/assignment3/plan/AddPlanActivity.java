package com.example.assignment3.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment3.DrawerActivity;
import com.example.assignment3.R;
import com.example.assignment3.databinding.ActivityEditPlanBinding;
import com.example.assignment3.entity.WorkoutPlan;

import java.util.List;


public class AddPlanActivity extends DrawerActivity {
    private ActivityEditPlanBinding binding;
    private String[] categoryArray = {"Indoor exercise","Outdoor exercise"};
    private com.example.assignment3.viewmodel.PlanViewModel PlanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Add New Plan");
        binding = ActivityEditPlanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initSpinner();

        PlanViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(com.example.assignment3.viewmodel.PlanViewModel.class);
        PlanViewModel.getAllWorkoutPlan().observe(this, new Observer<List<WorkoutPlan>>() {
            @Override
            public void onChanged(@Nullable final List<WorkoutPlan> workoutPlans) {
                String allWorkoutPlan = "";
                for (WorkoutPlan temp : workoutPlans) {
                    //String workoutplanDetails = (temp.pid + " " + temp.planName + " " + temp.planLength + " " + temp.time + " " + temp.category + " " + temp.planRoutine);
                    String workoutplanDetails = ("PlanID:" + temp.pid + "  PlanName:" + temp.planName + "  time:" + temp.time + "mins  planRoutine:" + temp.planRoutine);
                    allWorkoutPlan = allWorkoutPlan + System.getProperty("line.separator") + workoutplanDetails;
                }
                //binding.textViewRead.setText("All plans: " + allWorkoutPlan);
            }
        });

        //click save button
        binding.button9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name = binding.editTextTextPersonName.getText().toString();
                String length = binding.editTextTime2.getText().toString();
                String time = binding.editTextTime.getText().toString();
                String category = binding.spinner.getSelectedItem().toString();
                String planRoutine = binding.editTextTextMultiLine.getText().toString();

                if ((!name.isEmpty()) && (!time.isEmpty()) && (!planRoutine.isEmpty()) && (!length.isEmpty()) && (!category.isEmpty())) {
                    WorkoutPlan workoutplan = new WorkoutPlan(name,length,time,category,planRoutine);
                    PlanViewModel.insert(workoutplan);
                    //binding.textViewRead.setText("plan"+name + time + planRoutine);
                }
            }});

        //click clear button
        binding.button10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                binding.editTextTextPersonName.setText("");
                binding.editTextTime2.setText("");
                binding.editTextTime.setText("");
                //binding.categoryTextField.getEditText().setText("");
                binding.editTextTextMultiLine.setText("");
            }
        });

        //click deleteAll button
        /*binding.button11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PlanViewModel.deleteAll();
                //binding.textViewRead.setText("All data was deleted");
            }
        });*/
    }

    private void initSpinner(){
        //Declare an array adapter with a drop-down list
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, R.layout.item_select,categoryArray);
        //Sets the layout style of the array adapter
        categoryAdapter.setDropDownViewResource(R.layout.item_dropdown);
        //Get sp_dialog drop down box from layout file
        Spinner sp = findViewById(R.id.spinner);
        //Set the title of the drop-down box
        //sp.setPrompt("Please select the type of fitness");
        //Set the array adapter of the drop-down box
        sp.setAdapter(categoryAdapter);
        //Set the drop-down box to display the first item by default
        sp.setSelection(0);
        //Set the selection listener for the drop-down box.
        // Once the user selects an item, the onItemSelected() of the listener will be triggered
        //sp.setOnItemSelectedListener(new MySelectedListener());
    }

    /*
    Override OnItemSelectedListener()
    class MySelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(EditPlanActivity.this,"The one you choose is："+categoryArray[i],Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }*/
}