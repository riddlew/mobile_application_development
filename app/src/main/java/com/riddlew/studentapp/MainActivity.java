package com.riddlew.studentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.riddlew.studentapp.view.AssessmentList;
import com.riddlew.studentapp.view.CourseList;
import com.riddlew.studentapp.view.TermList;

public class MainActivity extends AppCompatActivity {

    Button btnCourses;
    Button btnTerms;
    Button btnAssessments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCourses = findViewById(R.id.btnCourses);
        btnTerms = findViewById(R.id.btnTerms);
        btnAssessments = findViewById(R.id.btnAssessments);

        btnCourses.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseList.class);
            startActivity(intent);
        });

        btnTerms.setOnClickListener(view -> {
            Intent intent = new Intent(this, TermList.class);
            startActivity(intent);
        });

        btnAssessments.setOnClickListener(view -> {
            Intent intent = new Intent(this, AssessmentList.class);
            startActivity(intent);
        });
    }
}