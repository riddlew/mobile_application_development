package com.riddlew.studentapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.riddlew.studentapp.R;
import com.riddlew.studentapp.controller.AssessmentAdapter;
import com.riddlew.studentapp.controller.CourseAdapter;
import com.riddlew.studentapp.database.AppDatabase;
import com.riddlew.studentapp.model.Assessment;
import com.riddlew.studentapp.model.Course;

import java.util.ArrayList;
import java.util.List;

public class AssessmentList extends AppCompatActivity {

    AssessmentAdapter adapter;
    RecyclerView recyclerView;
    List<Assessment> assessments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        recyclerView = findViewById(R.id.recyclerView);
//        Repository repo = new Repository(getApplication());
//        List<Course> courses = repo.getAllCourses();
        assessments = new ArrayList<>();
//        Course course1 = new Course(
//            1,
//            "Android Development and Applications",
//            LocalDate.parse("2020-01-02"),
//            LocalDate.parse("2020-02-02"),
//            CourseStatus.IN_PROGRESS,
//            new Instructor("John Doe", "123-456-7890", "jdoe@college.com")
//        );
//        Assessment assessment1 = new Assessment(1, "AD&A OA Assessment", AssessmentType.OA, LocalDate.parse("2020-01-05"), LocalDate.parse("2020-01-06"));
//        Assessment assessment2 = new Assessment(2, "AD&A PA Assessment", AssessmentType.PA, LocalDate.parse("2020-01-07"), LocalDate.parse("2020-01-08"));
//        course1.addAssessment(assessment1);
//        course1.addAssessment(assessment2);
//        course1.setShouldNotifyDates(true);
//        course1.addNote("This class is really hard!");
//        course1.addNote("There is some material that is outdated and not relevant to the newer Android versions. I recommend looking at Udemy classes for Android development versions >= 8.0 (Oreo).");
//        courses.add(course1);
//        courses.add(new Course(
//                2,
//                "Test 2",
//                LocalDate.parse("2020-01-02"),
//                LocalDate.parse("2020-03-02"),
//                CourseStatus.IN_PROGRESS,
//                new Instructor("Jane Doe", "123-456-7890", "jdoe@college.com")
//        ));
//        courses.add(new Course(
//                3,
//                "Test 3",
//                LocalDate.parse("2020-05-02"),
//                LocalDate.parse("2020-06-20"),
//                CourseStatus.COMPLETED,
//                new Instructor("Derek Zoolander", "123-456-7890", "dzoolander@college.com")
//        ));
        AppDatabase db = AppDatabase.getInstance(this);
        assessments = db.assessmentDao().getAssessments();
        adapter = new AssessmentAdapter(AssessmentList.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter.setAssessments(assessments);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TESTING", "ONRESUME");
        AppDatabase db = AppDatabase.getInstance(this);
        assessments = db.assessmentDao().getAssessments();
        adapter.setAssessments(assessments);
    }

    public void fabButtonClick(View view) {
        Intent intent = new Intent(this, AssessmentEdit.class);
        startActivity(intent);
    }
}