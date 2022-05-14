package com.example.assignment3.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.assignment3.entity.WorkoutPlan;

import java.util.List;

@Dao
public interface PlanDAO {
    @Query("SELECT * FROM workoutplan ORDER BY pid ASC")
    LiveData<List<WorkoutPlan>> getAll();

    @Query("SELECT * FROM workoutplan WHERE pid = :workoutplanId LIMIT 1")
    WorkoutPlan findByID(int workoutplanId);

    @Insert
    void insert(WorkoutPlan workoutplan);

    @Delete
    void delete(WorkoutPlan workoutplan);

    @Update
    void updateWorkoutPlan(WorkoutPlan workoutplan);

    @Query("DELETE FROM workoutplan")
    void deleteAll();
}