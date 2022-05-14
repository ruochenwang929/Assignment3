package com.example.assignment3.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WorkoutPlan {
    @PrimaryKey(autoGenerate = true)
    public int pid;

    @ColumnInfo(name = "plan_name")
    @NonNull
    public String planName;

    @ColumnInfo(name = "plan_length")
    @NonNull
    public String planLength;

    @ColumnInfo(name = "plan_time")
    @NonNull
    public String time;

    @ColumnInfo(name = "plan_category")
    @NonNull
    public String category;

    public String planRoutine;

    public WorkoutPlan(@NonNull String planName, @NonNull String planLength, @NonNull String time, @NonNull String category, String planRoutine) {
        this.planName = planName;
        this.planLength = planLength;
        this.time = time;
        this.category = category;
        this.planRoutine = planRoutine;
    }
}

