package com.riddlew.studentapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
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

public class AssessmentView extends AppCompatActivity {

    private TextView viewTitle;
    private TextView viewId;
    private TextView viewType;
    private TextView viewStartDate;
    private TextView viewEndDate;
    private TextView viewCourse;
    private ImageView viewNotificationCB;

    private int assessmentId;
    Assessment assessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_view);

        viewTitle = findViewById(R.id.detailTitle);
        viewId = findViewById(R.id.detailId);
        viewType = findViewById(R.id.detailType);
        viewStartDate = findViewById(R.id.detailStartDate);
        viewEndDate = findViewById(R.id.detailEndDate);
        viewCourse = findViewById(R.id.detailCourse);
        viewNotificationCB = findViewById(R.id.detailNotificationCB);

        Intent intent = getIntent();
        this.assessmentId = intent.getIntExtra("assessment_id", 0);
        assessment = new Assessment();
        populateFormData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(this.assessmentId == 0) return true;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu_no_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_edit:
                Intent intent = new Intent(this, AssessmentEdit.class);
                intent.putExtra("assessment_id", this.assessmentId);
                startActivity(intent);
                return true;
            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this assessment?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppDatabase db = AppDatabase.getInstance(AssessmentView.this);
                        db.assessmentDao().deleteAssessment(assessment);
                        finish();
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
        if(this.assessmentId != 0) {
            AppDatabase db = AppDatabase.getInstance(AssessmentView.this);
            assessment = db.assessmentDao().getAssessment(this.assessmentId);

            viewTitle.setText(assessment.getTitle());
            viewId.setText(Integer.toString(assessment.getId()));
            viewType.setText(assessment.getTypeAsString());
            viewStartDate.setText(assessment.getStartDate().toString());
            viewEndDate.setText(assessment.getEndDate().toString());

            if(assessment.getCourseId() != null) {
                Course course = db.courseDao().getCourse(assessment.getCourseId());
                viewCourse.setText(course.getTitle());
            } else {
                viewCourse.setText("None");
            }

            if (assessment.shouldNotifyDates()) {
                viewNotificationCB.setImageDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));
            } else {
                viewNotificationCB.setImageDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
            }

        } else {
            viewTitle.setText("Error loading assessment");
            viewId.setText("N/A");
            viewType.setText("N/A");
            viewStartDate.setText("N/A");
            viewEndDate.setText("N/A");
            viewCourse.setText("N/A");
        }
    }
}