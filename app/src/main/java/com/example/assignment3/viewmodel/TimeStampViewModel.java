package com.example.assignment3.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.assignment3.entity.TimeStampEntity;
import com.example.assignment3.repository.TimeStampRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TimeStampViewModel extends AndroidViewModel {
    private TimeStampRepository cRepository2;
    private LiveData<List<TimeStampEntity>> allTimeStamp;

    public TimeStampViewModel(Application application2) {
        super(application2);
        cRepository2 = new TimeStampRepository(application2);
        allTimeStamp = cRepository2.getAllTimeStamp();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)


    public CompletableFuture<TimeStampEntity> findByIDFuture(final int timeStampId){
        return cRepository2.findByIDFuture(timeStampId);
    }
    public LiveData<List<TimeStampEntity>> getAllTimeStamp() {
        return allTimeStamp;
    }
    public void insert(TimeStampEntity timeStamp) {
        cRepository2.insert(timeStamp);
    }

    public void deleteAll() {
        cRepository2.deleteAll();
    }
    public void update(TimeStampEntity timeStamp) { cRepository2.updateTimeStampEntity(timeStamp);}

}
