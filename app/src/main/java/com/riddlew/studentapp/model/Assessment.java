package com.riddlew.studentapp.model;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity(
        tableName = "assessments",
        foreignKeys = {
                @ForeignKey(
                        entity = Course.class,
                        parentColumns = "id",
                        childColumns = "course_id",
                        onDelete = SET_NULL,
                        onUpdate = CASCADE
                )
        }
)
public class Assessment implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @NonNull
    @ColumnInfo(name = "type")
    private AssessmentType mType;

    @NonNull
    @ColumnInfo(name = "start_date")
    private LocalDate mStartDate;

    @NonNull
    @ColumnInfo(name = "end_date")
    private LocalDate mEndDate;

    @ColumnInfo(name = "course_id")
    private Integer mCourseId;

    @NonNull
    @ColumnInfo(name = "notify")
    private boolean mShouldNotifyDates;

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public AssessmentType getType() {
        return this.mType;
    }

    public void setType(AssessmentType type) {
        this.mType = type;
    }

    public String getTypeAsString() {
        switch (this.mType) {
            case OA:
                return "Objective Assessment";
            case PA:
                return "Performance Assessment";
            default:
                return "";
        }
    }

    public LocalDate getStartDate() {
        return this.mStartDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.mStartDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.mEndDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.mEndDate = endDate;
    }

    public Integer getCourseId() {
        return mCourseId;
    }

    public void setCourseId(Integer courseId) {
        this.mCourseId = courseId;
    }

    public boolean shouldNotifyDates() {
        return mShouldNotifyDates;
    }

    public void setShouldNotifyDates(boolean shouldNotifyDates) {
        this.mShouldNotifyDates = shouldNotifyDates;
    }

    @NonNull
    @Override
    public String toString() {
        return this.mTitle;
    }
}
