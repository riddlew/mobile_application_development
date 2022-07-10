package com.riddlew.studentapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.riddlew.studentapp.Converters;
import com.riddlew.studentapp.model.Assessment;
import com.riddlew.studentapp.model.Course;
import com.riddlew.studentapp.model.Instructor;
import com.riddlew.studentapp.model.Note;
import com.riddlew.studentapp.model.Term;

@Database(
    entities = {
        Course.class,
        Assessment.class,
        Term.class
    },
    version = 1
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "database.db";
    private static AppDatabase mAppDatabase;

    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();
    public abstract TermDao termDao();

    public static AppDatabase getInstance(Context context) {
        if(mAppDatabase == null) {
            mAppDatabase = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }

        return mAppDatabase;
    }
}
