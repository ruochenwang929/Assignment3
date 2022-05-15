package com.example.assignment3.plan;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.util.Date;
import java.util.Locale;

public class TimeStamp {

    SimpleDateFormat sdfTwo =new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss/E", Locale.getDefault());
    long timeGetTime =new Date().getTime();
    String time = sdfTwo.format(timeGetTime);

    public TimeStamp(){ }

    public void logTimeStamp(){
        Log.d("test", "  currentTimeStamp->:"+timeGetTime);
    }

    public void logTime(){
        Log.d("test", timeGetTime +"  CurrentTime-->:" + time);
    }
    public String getTime(){
        return time;
    }

}
