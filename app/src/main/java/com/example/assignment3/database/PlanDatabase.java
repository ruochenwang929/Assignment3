package com.example.assignment3.database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.assignment3.dao.PlanDAO;
import com.example.assignment3.entity.WorkoutPlan;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {WorkoutPlan.class}, version = 1, exportSchema = false)
public abstract class PlanDatabase extends RoomDatabase {
    public abstract PlanDAO planDao();
    private static PlanDatabase INSTANCE;
    //create an ExecutorService with a fixed thread pool so we can later run database operations asynchronously on a background thread.
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    //A synchronized method in a multi threaded environment means that two threads are not allowed to access data at the same time
    public static synchronized PlanDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    PlanDatabase.class, "PlanDatabase").build();    //.fallbackToDestructiveMigration()
        }
        return INSTANCE;
    }
}
