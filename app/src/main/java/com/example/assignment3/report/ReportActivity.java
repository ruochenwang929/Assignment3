package com.example.assignment3.report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment3.BuildConfig;
import com.example.assignment3.DrawerActivity;
import com.example.assignment3.R;
import com.example.assignment3.databinding.ActivityReportBinding;
import com.example.assignment3.entity.WorkoutPlan;
import com.example.assignment3.viewmodel.PlanViewModel;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.utilities.Tree;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ReportActivity extends DrawerActivity {

    String[] chartTypes = {"Bar Chart","Pie Chart"};

    private ActivityReportBinding binding;
    private String chartType;
    private Calendar now;
    private Calendar aWeekAgo;
    private Long start;
    private Long end;
    final private int ONE_DAY = 1000*60*60*24;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUserEmail;
    private List<Map<String, Object>> records;
    private PlanViewModel planViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        super.addContentView(view);
        this.setTitle("Personal Report");

        planViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(PlanViewModel.class);

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

        //calculate default date (a week ago from now)
        now = Calendar.getInstance();
        aWeekAgo = Calendar.getInstance();
        aWeekAgo.add(Calendar.DATE,-6);

        //set default selected date range for text view
        SimpleDateFormat format = new SimpleDateFormat("MMM d");
        String formatted_today = format.format(now.getTime());
        String formatted_ago = format.format(aWeekAgo.getTime());
        binding.selectedDateTextView.setText(formatted_ago+" - "+formatted_today);


        //Datepicker constraint, no future selection
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointBackward.now());

        //Datepicker setup
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("SELECT A DATE RANGE");
        builder.setTheme(R.style.MaterialCalendarTheme);
        builder.setSelection(new Pair(aWeekAgo.getTimeInMillis(),now.getTimeInMillis()));
        builder.setCalendarConstraints(constraintBuilder.build());
        final MaterialDatePicker materialDatePicker = builder.build();


        binding.mDatePickerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"DATE_RANGE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override public void onPositiveButtonClick(Pair<Long,Long> selection) {
                binding.selectedDateTextView.setText(materialDatePicker.getHeaderText());
                start = selection.first;
                end = selection.second;
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserEmail = user.getEmail();

        records = new ArrayList<>();

        db.collection("Workout Record")
                .whereEqualTo("email", currentUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                records.add(document.getData());
                            }

                        } else {
                            Log.d("Read Firebase Fail", "Error getting documents: ", task.getException());
                        }
                    }
                });

//        //default bar chart
//        generateBarChart();

        //update chart according to user input
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


        if (start == null || end == null){
            start = aWeekAgo.getTimeInMillis();
            end = now.getTimeInMillis();
        }

        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(start));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(end));


        //Reading testing data into bar entries
        List<BarEntry> barEntries = new ArrayList<>();
        Map<String,Integer> history = new TreeMap<>();


        Long helper = start;

        while(helper<=end){
            history.put(new SimpleDateFormat("yyyy-MM-dd").format(helper),0);
            helper+=ONE_DAY;
        }


        for (Map<String, Object> r:records){
            long time = (long) r.get("RecordTime");

            if (end.compareTo(time-ONE_DAY)>=0 && start.compareTo(time+ONE_DAY)<=0){
                String strDate = new SimpleDateFormat("yyyy-MM-dd").format(time);
                int current = history.get(strDate);
                int add = Integer.parseInt((String) r.get("PlanTime"));
                history.put(strDate, current+add);
            }
        }
        Set<Map.Entry<String, Integer> > entrySet
                = history.entrySet();
        Map.Entry<String, Integer>[] entryArray
                = entrySet.toArray(
                new Map.Entry[entrySet.size()]);


        for (int i=0;i<history.size();i++){
            barEntries.add(new BarEntry(i,entryArray[i].getValue()));
        }

        history.entrySet().forEach(h->{
            System.out.println(h.getKey()+" "+h.getValue());
        });
//
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(start));
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(end));




        BarDataSet barDataSet = new BarDataSet(barEntries, "Workout Time");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();

        //Generate X axis label according to user input
        List<String> xAxisValues = new ArrayList<>();
        SimpleDateFormat barFormat = new SimpleDateFormat("MM-dd");
        Long date = start;
        while (date <= end){
            xAxisValues.add(barFormat.format(date));
            date+=ONE_DAY;
        }


        binding.barChart.getXAxis().setCenterAxisLabels(false);
        binding.barChart.getXAxis().setGranularity(1f);
        binding.barChart.getXAxis().setValueFormatter(new
                com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
        BarData barData = new BarData(barDataSet);
        binding.barChart.setData(barData);
        barData.setBarWidth(1.0f);
        binding.barChart.animateY(500);

        Description description = new Description();
        description.setText("Daily Workout Time");
        description.setTextSize(16f);
        binding.barChart.setDescription(description);

        //Clink on barchart to show information dialog
        binding.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String name = binding.barChart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), binding.barChart.getXAxis());
                BarEntry x = (BarEntry) e;
                int minutes = (int) x.getY();
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                builder.setCancelable(true);
                builder.setMessage("You have spend "+minutes+" minutes on this day");
                AlertDialog alert = builder.create();
                alert.setTitle("Date: " + name);
                alert.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        binding.barChart.invalidate();

    }

    public void generatePieChart(){
        binding.barChart.setVisibility(View.INVISIBLE);
        binding.pieChart.setVisibility(View.VISIBLE);


        if (start == null || end == null){
            start = aWeekAgo.getTimeInMillis()-ONE_DAY;
            end = now.getTimeInMillis();
        }


        //Toast.makeText(ReportActivity.this, start+" + "+end, Toast.LENGTH_SHORT).show();

        //Reading data into pie entries
        List<PieEntry> pieEntries = new ArrayList<>();
        Map<String,Integer> history = new TreeMap<>();


        for (Map<String, Object> r:records){
            long time = (long) r.get("RecordTime");
            if (end.compareTo(time-ONE_DAY)>=0 && start.compareTo(time)<=0){
                String name = (String) r.get("PlanName");
                int current = 0;
                if (history.get(name)!=null){
                    current = history.get(name);
                }
                int add = Integer.parseInt((String) r.get("PlanTime"));
                history.put(name, current+add);
            }
        }

        history.entrySet().forEach(h->{
            pieEntries.add(new PieEntry(h.getValue(),h.getKey()));
        });

//        pieEntries.add(new PieEntry(342,planViewModel.findById()));
//        pieEntries.add(new PieEntry(22,"plan2"));
//        pieEntries.add(new PieEntry(121,"plan3"));
//        pieEntries.add(new PieEntry(50,"plan4"));
//        pieEntries.add(new PieEntry(87,"plan5"));

        binding.pieChart.setDrawHoleEnabled(true);

        String descriptionStr = "Percentage of Different Workout Plans Completed";
        Description description = new Description();
        description.setText(descriptionStr);
        description.setTextSize(12f);

        binding.pieChart.setExtraLeftOffset(10f);
        binding.pieChart.setExtraTopOffset(32f);
        binding.pieChart.setExtraRightOffset(10f);
        binding.pieChart.setExtraBottomOffset(32f);
        binding.pieChart.setCenterText("Completed Workout Plans");
        binding.pieChart.setCenterTextSize(24);

        Legend l = binding.pieChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Workout Plan");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS   );
        pieDataSet.setValueLinePart1OffsetPercentage(80f);
        pieDataSet.setSliceSpace(4f);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        binding.pieChart.setData(pieData);
        binding.pieChart.setDescription(description);
        binding.pieChart.animateY(500);

        //Click on slice in pie chart shows details
        binding.pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry x = (PieEntry) e;
                String name = x.getLabel();
                int minutes = (int) x.getValue();
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                builder.setCancelable(true);
                builder.setMessage("You have spend "+minutes+" minutes on this plan.");
                AlertDialog alert = builder.create();
                alert.setTitle(name);
                alert.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        binding.pieChart.invalidate();
    }

    //Only Show share icon in Report screen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    //Share to Facebook
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        switch (item.getItemId()){

            case R.id.shareButton:

                final ShareDialog shareDialog = new ShareDialog(this);
                ShareHashtag hashtag = new ShareHashtag
                        .Builder()
                        .setHashtag("#FitnessNow")
                        .build();


                //Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.share_test);
                Bitmap image;
                if (chartType == "Bar Chart"){
                    image = binding.barChart.getChartBitmap();
                } else{
                    image = binding.pieChart.getChartBitmap();
                }

                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();



                if (ShareDialog.canShow(SharePhotoContent.class)){
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .setShareHashtag(hashtag)
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                } else{
                    Toast.makeText(ReportActivity.this,"You don't have Facebook installed on your phone! Please install and try again later. ",Toast.LENGTH_SHORT).show();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

}