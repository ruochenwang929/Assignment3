package com.example.assignment3.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.assignment3.entity.TimeStampEntity;

import java.util.List;

@Dao
public interface TimeStampDAO {

    @Query("SELECT * FROM timestampentity ORDER BY planID ASC")
    LiveData<List<TimeStampEntity>> getAll();

    @Query("SELECT * FROM timestampentity WHERE planID = :timeStampId LIMIT 1")
    TimeStampEntity findByID(int timeStampId);

    @Insert
    void insert(TimeStampEntity timeStamp);

    @Delete
    void delete(TimeStampEntity timeStamp);

    @Update
    void updateTimeStampEntity(TimeStampEntity timeStamp);

    @Query("DELETE FROM timestampentity")
    void deleteAll();

}
