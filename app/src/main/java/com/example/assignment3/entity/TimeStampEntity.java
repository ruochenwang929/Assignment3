package com.example.assignment3.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TimeStampEntity {

    @PrimaryKey
    public int planID;

    @ColumnInfo(name = "plan_timestamp")
    @NonNull
    public String timeStamp;

    @ColumnInfo(name = "user_ID")
    @NonNull
    public String userID;


    public TimeStampEntity(int planID,String timeStamp,String userID) {
        this.planID=planID;
        this.timeStamp=timeStamp;
        this.userID=userID;
    }

    public int getPlanID() {
        return planID;
    }

    @NonNull
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(@NonNull String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @NonNull
    public String getUserID() {
        return userID;
    }

    public void setUserID(@NonNull String userID) {
        this.userID = userID;
    }
}
