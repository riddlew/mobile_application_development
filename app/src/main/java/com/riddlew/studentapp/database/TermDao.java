package com.riddlew.studentapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.riddlew.studentapp.model.Course;
import com.riddlew.studentapp.model.Term;

import java.util.List;

@Dao
public interface TermDao {

    @Query("SELECT * FROM terms ORDER BY start_date")
    public List<Term> getTerms();

    @Query("SELECT * FROM terms WHERE id = :id")
    public Term getTerm(int id);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public long insertTerm(Term term);

    @Update
    public void updateTerm(Term term);

    @Delete
    public void deleteTerm(Term term);
}
