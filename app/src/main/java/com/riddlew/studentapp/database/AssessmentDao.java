package com.riddlew.studentapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.riddlew.studentapp.model.Assessment;
import com.riddlew.studentapp.model.Course;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface AssessmentDao {

    @Query("SELECT * FROM assessments")
    public List<Assessment> getAssessments();
//
//    @Query("SELECT * FROM assessments WHERE start_date = :date OR end_date = :date AND notified = 0")
//    public List<Assessment> getAssessmentsToday(LocalDate date);

    @Query("SELECT * FROM assessments WHERE id = :id")
    public Assessment getAssessment(int id);

    @Query("SELECT * FROM assessments WHERE course_id = :courseId")
    public List<Assessment> getAssessmentsByCourseId(int courseId);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertAssessment(Assessment assessment);

    @Update
    public void updateAssessment(Assessment assessment);

    @Delete
    public void deleteAssessment(Assessment assessment);
}
