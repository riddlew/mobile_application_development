package com.riddlew.studentapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riddlew.studentapp.R;
import com.riddlew.studentapp.database.AppDatabase;
import com.riddlew.studentapp.model.Assessment;
import com.riddlew.studentapp.model.Course;
import com.riddlew.studentapp.model.Term;

import java.util.List;

public class TermView extends AppCompatActivity {

    private TextView viewTitle;
    private TextView viewId;
    private TextView viewStartDate;
    private TextView viewEndDate;
    private LinearLayout llAssignedCourses;

    private int termId;
    Term term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_view);

        viewTitle = findViewById(R.id.detailTitle);
        viewId = findViewById(R.id.detailId);
        viewStartDate = findViewById(R.id.detailStartDate);
        viewEndDate = findViewById(R.id.detailEndDate);
        llAssignedCourses = findViewById(R.id.linearLayoutAssignedCourses);

        Intent intent = getIntent();
        this.termId = intent.getIntExtra("term_id", 0);
        term = new Term();
        populateFormData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(this.termId == 0) return true;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu_no_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_edit:
                Intent intent = new Intent(this, TermEdit.class);
                intent.putExtra("term_id", this.termId);
                startActivity(intent);
                return true;
            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this term?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppDatabase db = AppDatabase.getInstance(TermView.this);
                        int courseCount = db.courseDao().getCourseCountForTermId(termId);
                        if(courseCount == 0) {
                            db.termDao().deleteTerm(term);
                            finish();
                        } else {
                            AlertDialog.Builder failed = new AlertDialog.Builder(TermView.this);
                            failed.setTitle("Cannot Delete Term");
                            failed.setMessage("This term contains courses and could not be deleted. Remove courses to delete this term.");
                            failed.setPositiveButton("OK", null);
                            failed.show();
                        }
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFormData();
    }

    private void populateFormData() {
        if(this.termId != 0) {
            AppDatabase db = AppDatabase.getInstance(TermView.this);
            term = db.termDao().getTerm(this.termId);
            List<Course> courses = db.courseDao().getCoursesAssignedToTermId(this.termId);

            viewTitle.setText(term.getTitle());
            viewId.setText(Integer.toString(term.getId()));
            viewStartDate.setText(term.getStartDate().toString());
            viewEndDate.setText(term.getEndDate().toString());
            llAssignedCourses.removeAllViews();

            for(Course course : courses) {
                TextView viewCourse = new TextView(this);
                viewCourse.setText(course.getTitle());
                llAssignedCourses.addView(viewCourse);
            }

        } else {
            viewTitle.setText("Error loading term");
            viewId.setText("N/A");
            viewStartDate.setText("N/A");
            viewEndDate.setText("N/A");
        }
    }
}