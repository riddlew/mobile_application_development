package com.riddlew.studentapp.model;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "courses",
        foreignKeys = {
                @ForeignKey(
                        entity = Term.class,
                        parentColumns = "id",
                        childColumns = "term_id",
                        onDelete = SET_NULL,
                        onUpdate = CASCADE
                )
        }
)
public class Course implements Serializable {

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

    @NonNull
    @ColumnInfo(name = "status")
    private CourseStatus mStatus;

    @NonNull
    @ColumnInfo(name = "instructor_name")
    private String mInstructorName;

    @NonNull
    @ColumnInfo(name = "instructor_phone")
    private String mInstructorPhone;

    @NonNull
    @ColumnInfo(name = "instructor_email")
    private String mInstructorEmail;

    @NonNull
    @ColumnInfo(name = "notify")
    private boolean mShouldNotifyDates;

    @ColumnInfo(name = "notes")
    private String mNotes;

    @ColumnInfo(name = "term_id")
    private Integer mTermId;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public CourseStatus getStatus() {
        return mStatus;
    }

    public void setStatus(CourseStatus status) {
        this.mStatus = status;
    }

    public String getStatusAsString() {
        switch(this.mStatus) {
            case COMPLETED:
                return "Completed";
            case DROPPED:
                return "Dropped";
            case IN_PROGRESS:
                return "In Progress";
            case PLAN_TO_TAKE:
                return "Plan to Take";
            default:
                return "";
        }
    }

    public LocalDate getStartDate() {
        return mStartDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.mStartDate = startDate;
    }

    public LocalDate getEndDate() {
        return mEndDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.mEndDate = endDate;
    }

    public boolean shouldNotifyDates() {
        return mShouldNotifyDates;
    }

    public void setShouldNotifyDates(boolean shouldNotifyDates) {
        this.mShouldNotifyDates = shouldNotifyDates;
    }

    @NonNull
    public String getInstructorName() {
        return mInstructorName;
    }

    public void setInstructorName(@NonNull String instructorName) {
        this.mInstructorName = instructorName;
    }

    @NonNull
    public String getInstructorPhone() {
        return mInstructorPhone;
    }

    public void setInstructorPhone(@NonNull String instructorPhone) {
        this.mInstructorPhone = instructorPhone;
    }

    @NonNull
    public String getInstructorEmail() {
        return mInstructorEmail;
    }

    public void setInstructorEmail(@NonNull String instructorEmail) {
        this.mInstructorEmail = instructorEmail;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

    public Integer getTermId() { return mTermId; }

    public void setTermId(Integer termId) { this.mTermId = termId; }

    @NonNull
    @Override
    public String toString() {
        return this.getTitle();
    }
}
