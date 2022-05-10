package com.example.assignment3.report;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.example.assignment3.BuildConfig;
import com.example.assignment3.DrawerActivity;
import com.example.assignment3.R;
import com.example.assignment3.databinding.ActivityReportBinding;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportActivity extends DrawerActivity {

    String[] chartTypes = {"Bar Chart","Pie Chart"};

    private ActivityReportBinding binding;
    private String chartType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        super.addContentView(view);
        this.setTitle("Personal Report");

        //Spinner setup
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, chartTypes);
        binding.chartSpinner.setAdapter(spinnerAdapter);
        binding.chartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chartType = binding.chartSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });




        Calendar now = Calendar.getInstance();
        Calendar aWeekAgo = Calendar.getInstance();
        aWeekAgo.add(Calendar.DATE,-7);

        //set default selected date range for text view
        SimpleDateFormat format = new SimpleDateFormat("MMM d");
        String formatted_today = format.format(now.getTime());
        String formatted_ago = format.format(aWeekAgo.getTime());
        binding.selectedDateTextView.setText(formatted_ago+" - "+formatted_today);


        //Datepicker constraint
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointBackward.now());

        //Datepicker setup
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("SELECT A DATE RANGE");
        builder.setTheme(R.style.MaterialCalendarTheme);
        builder.setSelection(new Pair(now.getTimeInMillis()-1000*60*60*24*7,now.getTimeInMillis()));
        builder.setCalendarConstraints(constraintBuilder.build());
        final MaterialDatePicker materialDatePicker = builder.build();


        binding.mDatePickerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"DATE_RANGE_PICKER");

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                binding.selectedDateTextView.setText(materialDatePicker.getHeaderText());
            }
        });


        generateBarChart();

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chartType == "Bar Chart"){
                    generateBarChart();
                } else {
                    generatePieChart();
                }
            }
        });

    }

    public void generateBarChart(){
        binding.barChart.setVisibility(View.VISIBLE);
        binding.pieChart.setVisibility(View.INVISIBLE);
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 6766));
        barEntries.add(new BarEntry(1, 4444));
        barEntries.add(new BarEntry(2, 2222));
        barEntries.add(new BarEntry(3, 5555));
        barEntries.add(new BarEntry(4, 1111));
        barEntries.add(new BarEntry(5, 3656));
        barEntries.add(new BarEntry(6, 3435));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Steps");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("Sun", "Mon", "Tues",
                "Wed", "Thurs", "Fri","Sat"));
        binding.barChart.getXAxis().setValueFormatter(new
                com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
        BarData barData = new BarData(barDataSet);
        binding.barChart.setData(barData);
        barData.setBarWidth(1.0f);
        binding.barChart.setVisibility(View.VISIBLE);
        binding.barChart.animateY(4000);
        Description description = new Description();
        description.setText("Daily Steps");
        binding.barChart.setDescription(description);
        binding.barChart.invalidate();
    }

    public void generatePieChart(){
        binding.barChart.setVisibility(View.INVISIBLE);
        binding.pieChart.setVisibility(View.VISIBLE);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(12.0f,"未违章"));
        entries.add(new PieEntry(88.0f,"违章"));

        binding.pieChart.setDrawHoleEnabled(true);

        String descriptionStr = "平台上有违章车辆和没违章车辆的占比统计";
        Description description = new Description();
        description.setText(descriptionStr);

        binding.pieChart.setExtraLeftOffset(0f);
        binding.pieChart.setExtraTopOffset(32f);
        binding.pieChart.setExtraRightOffset(0f);
        binding.pieChart.setExtraBottomOffset(32f);

        PieDataSet pieDataSet = new PieDataSet(entries,"违章情况");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setValueLinePart1OffsetPercentage(80f);
        pieDataSet.setSliceSpace(4f);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        binding.pieChart.setData(pieData);
        binding.pieChart.setDescription(description);

        binding.pieChart.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        switch (item.getItemId()){

            case R.id.shareButton:
                FacebookSdk.sdkInitialize(this.getApplicationContext());

                CallbackManager callbackManager = CallbackManager.Factory.create();

                final ShareDialog shareDialog = new ShareDialog(this);
                ShareHashtag hashtag = new ShareHashtag
                        .Builder()
                        .setHashtag("#FacebookSDKAndroid")
                        .build();


                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.share_test);
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();

//                SharePhotoContent content1 = new SharePhotoContent.Builder()
//                        .setShareHashtag(hashtag)
//                        .addPhoto(photo)
//                        .build();
//                shareDialog.show(content1, ShareDialog.Mode.AUTOMATIC);

                if (ShareDialog.canShow(SharePhotoContent.class)){
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .setShareHashtag(hashtag)
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                } else{
                    Toast.makeText(ReportActivity.this,"Fail!",Toast.LENGTH_SHORT).show();
                }

                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(ReportActivity.this,"Share successful!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ReportActivity.this,"Share cancel!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(ReportActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

                break;

        }
        return super.onOptionsItemSelected(item);
    }


}