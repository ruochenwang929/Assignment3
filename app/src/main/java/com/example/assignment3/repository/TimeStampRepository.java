package com.example.assignment3.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import com.example.assignment3.dao.TimeStampDAO;
import com.example.assignment3.database.PlanDatabase;
import com.example.assignment3.database.TimeStampDatabase;
import com.example.assignment3.entity.TimeStampEntity;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class TimeStampRepository {
    private TimeStampDAO timeStampDao;
    private LiveData<List<TimeStampEntity>> allTimeStamp;
    public TimeStampRepository(Application application){
        TimeStampDatabase db = TimeStampDatabase.getInstance(application);
        timeStampDao =db.timeStampDao();
        allTimeStamp= timeStampDao.getAll();
    }

    public LiveData<List<TimeStampEntity>> getAllTimeStamp() {
        return allTimeStamp;
    }

    public void insert(final TimeStampEntity timeStamp){
        TimeStampDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                timeStampDao.insert(timeStamp);
            }
        });
    }

    public void deleteAll(){
        TimeStampDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                timeStampDao.deleteAll();
            }
        });
    }

    public void delete(final TimeStampEntity timeStamp){
        TimeStampDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                timeStampDao.delete(timeStamp);
            }
        });
    }
    public void updateTimeStampEntity(final TimeStampEntity timeStamp){
        TimeStampDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                timeStampDao.updateTimeStampEntity(timeStamp);
            }
        });
    }

    //@RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<TimeStampEntity> findByIDFuture(final int timeStampId) {
        return CompletableFuture.supplyAsync(new Supplier<TimeStampEntity>() {
            @Override
            public TimeStampEntity get() {
                return timeStampDao.findByID(timeStampId);
            }
        }, TimeStampDatabase.databaseWriteExecutor);
    }
}
