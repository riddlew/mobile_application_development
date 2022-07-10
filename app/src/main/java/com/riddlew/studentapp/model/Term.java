package com.riddlew.studentapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "terms")
public class Term {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @NonNull
    @ColumnInfo(name = "start_date")
    private LocalDate mStartDate;

    @NonNull
    @ColumnInfo(name = "end_date")
    private LocalDate mEndDate;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull String title) {
        this.mTitle = title;
    }

    @NonNull
    public LocalDate getStartDate() {
        return mStartDate;
    }

    public void setStartDate(@NonNull LocalDate startDate) {
        this.mStartDate = startDate;
    }

    @NonNull
    public LocalDate getEndDate() {
        return mEndDate;
    }

    public void setEndDate(@NonNull LocalDate endDate) {
        this.mEndDate = endDate;
    }
}
