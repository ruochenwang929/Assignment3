package com.example.assignment3.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.assignment3.dao.TimeStampDAO;
import com.example.assignment3.entity.TimeStampEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TimeStampEntity.class}, version = 1, exportSchema = false)
public abstract class TimeStampDatabase extends RoomDatabase {
    public abstract TimeStampDAO timeStampDao();
    private static TimeStampDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static synchronized TimeStampDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TimeStampDatabase.class, "TimeStampDatabase").build();
        }
        return INSTANCE;
    }












}
