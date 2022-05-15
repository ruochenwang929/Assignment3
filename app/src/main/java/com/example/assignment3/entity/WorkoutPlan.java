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

    public int getPlanID() {
        return pid;
    }

    @NonNull
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(@NonNull String planName) {
        this.planName = planName;
    }

    @NonNull
    public String getPlanLength() {
        return planLength;
    }

    public void setPlanLength(@NonNull String planLength) {
        this.planLength = planLength;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public String getPlanRoutine() {
        return planRoutine;
    }

    public void setPlanRoutine(String planRoutine) {
        this.planRoutine = planRoutine;
    }
}

