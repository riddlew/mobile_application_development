package com.riddlew.studentapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.riddlew.studentapp.model.Course;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM courses ORDER BY start_date")
    public List<Course> getCourses();

    @Query("SELECT COUNT(*) FROM courses WHERE term_id = :id")
    public int getCourseCountForTermId(int id);

    @Query("SELECT * FROM courses WHERE term_id = :termId")
    public List<Course> getCoursesAssignedToTermId(int termId);

    @Query("SELECT * FROM courses WHERE term_id IS NULL")
    public List<Course> getUnassignedCourses();

    @Query("SELECT * FROM courses WHERE id = :id")
    public Course getCourse(int id);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertCourse(Course course);

    @Update
    public void updateCourse(Course course);

    @Delete
    public void deleteCourse(Course course);
}
