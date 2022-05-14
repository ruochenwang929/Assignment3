package com.example.assignment3.plan;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.databinding.ActivityDatePickerBinding;
import com.example.assignment3.databinding.ActivityEditPlanBinding;

public class DatePickerActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener{
    private ActivityDatePickerBinding binding;
    private int cal_year=0;
    private int cal_month=0;
    private int cal_day=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatePickerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initTime();

        onDateChanged(binding.datePicker1, cal_year,cal_month,cal_day);

        //click BACK button
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatePickerActivity.this,
                        EditPlanActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = new Bundle();
        //click SAVE button
        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatePickerActivity.this,
                        EditPlanActivity.class);
                bundle.putInt("YEAR",cal_year);
                bundle.putInt("MONTH",cal_month);
                bundle.putInt("DAY",cal_day);
                intent.putExtras(bundle);
                startActivity(intent);
                //intent.putExtra("MONTH",cal_month);
                //intent.putExtra("DAY",cal_day);
                //setResult(RESULT_OK,intent);
                //finish();
            }
        });


    }

    public void initTime(){
        Calendar calendar=Calendar.getInstance();
        cal_year=calendar.get(Calendar.YEAR);
        cal_month=calendar.get(Calendar.MONTH);
        cal_day=calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onDateChanged(DatePicker view, int year,int month,int day){
        this.cal_year = year;
        this.cal_month =month;
        this.cal_day = day;
        Toast.makeText(DatePickerActivity.this,"the date is："+year+(month+1)+day,Toast.LENGTH_SHORT).show();
    }

}