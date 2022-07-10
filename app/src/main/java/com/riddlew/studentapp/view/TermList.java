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
import com.riddlew.studentapp.controller.CourseAdapter;
import com.riddlew.studentapp.controller.TermAdapter;
import com.riddlew.studentapp.database.AppDatabase;
import com.riddlew.studentapp.model.Course;
import com.riddlew.studentapp.model.Term;

import java.util.ArrayList;
import java.util.List;

public class TermList extends AppCompatActivity {

    TermAdapter adapter;
    RecyclerView recyclerView;
    List<Term> terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        recyclerView = findViewById(R.id.recyclerView);
        terms = new ArrayList<>();
        AppDatabase db = AppDatabase.getInstance(this);
        terms = db.termDao().getTerms();
        adapter = new TermAdapter(TermList.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter.setTerms(terms);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppDatabase db = AppDatabase.getInstance(this);
        terms = db.termDao().getTerms();
        adapter.setTerms(terms);
    }

    public void fabButtonClick(View view) {
        Intent intent = new Intent(this, TermEdit.class);
        startActivity(intent);
    }
}