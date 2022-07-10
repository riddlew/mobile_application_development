package com.riddlew.studentapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riddlew.studentapp.R;
import com.riddlew.studentapp.database.AppDatabase;
import com.riddlew.studentapp.model.Assessment;
import com.riddlew.studentapp.model.Course;
import com.riddlew.studentapp.model.Term;

import java.util.List;

public class CourseView extends AppCompatActivity {

    private TextView viewTitle;
    private TextView viewId;
    private TextView viewTerm;
    private TextView viewStatus;
    private TextView viewStartDate;
    private TextView viewEndDate;
    private TextView viewInstructorName;
    private TextView viewInstructorPhone;
    private TextView viewInstructorEmail;
    private ImageView viewNotificationCB;
    private LinearLayout llAssessments;
    private LinearLayout llNotes;

    private int courseId;
    private Course course;

    // Also needs:
    // - Set alert for start and end dates that will trigger even if app is closed.
    // - Share notes via email or SMS that automatically pre-populates with the notes.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        viewTitle = findViewById(R.id.detailTitle);
        viewId = findViewById(R.id.detailId);
        viewTerm = findViewById(R.id.detailTerm);
        viewStatus = findViewById(R.id.detailStatus);
        viewStartDate = findViewById(R.id.detailStartDate);
        viewEndDate = findViewById(R.id.detailEndDate);
        viewInstructorName = findViewById(R.id.detailInstructorName);
        viewInstructorPhone = findViewById(R.id.detailInstructorPhone);
        viewInstructorEmail = findViewById(R.id.detailInstructorEmail);
        viewNotificationCB = findViewById(R.id.detailNotificationCB);
        llAssessments = findViewById(R.id.linearLayoutAssessments);
        llNotes = findViewById(R.id.linearLayoutNotes);

        Intent intent = getIntent();
        this.courseId = intent.getIntExtra("course_id", 0);
        this.course = new Course();
        populateFormData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(this.courseId == 0) return true;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_edit: {
                Intent intent = new Intent(this, CourseEdit.class);
                intent.putExtra("course_id", this.courseId);
//                startActivityFor(intent);
                startActivity(intent);
                return true;
            }
            case R.id.menu_share: {
                Uri uri = Uri.parse("smsto:");
                StringBuilder sb = new StringBuilder();
                sb.append("Course: " + course.getTitle());
                sb.append("\nStatus: " + course.getStatusAsString());
                sb.append("\nStart Date: " + course.getStartDate().toString());
                sb.append("\nEnd Date: " + course.getEndDate().toString());
                sb.append("\nInstructor Name: " + course.getInstructorName());
                sb.append("\nInstructor Phone: " + course.getInstructorPhone());
                sb.append("\nInstructor Email: " + course.getInstructorEmail());
                sb.append("\nNotes: " + course.getNotes());
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", sb.toString());
                startActivity(intent);
                return true;
            }
            case R.id.menu_delete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this course?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppDatabase db = AppDatabase.getInstance(CourseView.this);
                        db.courseDao().deleteCourse(course);
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true;
            }
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
        TextView assessmentsHeader = new TextView(new ContextThemeWrapper(this, R.style.detailTableHeader));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        assessmentsHeader.setLayoutParams(lp);
        assessmentsHeader.setText("Assessments:");
        llAssessments.removeAllViews();
        llAssessments.addView(assessmentsHeader);

        TextView notesHeader = new TextView(new ContextThemeWrapper(this, R.style.detailTableHeader));
        notesHeader.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        notesHeader.setText("Notes:");
        llNotes.removeAllViews();
        llNotes.addView(notesHeader);

        if(this.courseId != 0) {
            AppDatabase db = AppDatabase.getInstance(CourseView.this);
            this.course = db.courseDao().getCourse(this.courseId);

            if(course.getTermId() != null) {
                Term term = db.termDao().getTerm(course.getTermId());
                viewTerm.setText(term.getTitle());
            } else {
                viewTerm.setText("Unassigned");
            }

            viewTitle.setText(course.getTitle());
            viewId.setText(Integer.toString(course.getId()));
            viewStatus.setText(course.getStatusAsString());
            viewStartDate.setText(course.getStartDate().toString());
            viewEndDate.setText(course.getEndDate().toString());
            viewInstructorName.setText(course.getInstructorName());
            viewInstructorPhone.setText(course.getInstructorPhone());
            viewInstructorEmail.setText(course.getInstructorEmail());

            if (course.shouldNotifyDates()) {
                viewNotificationCB.setImageDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));
            } else {
                viewNotificationCB.setImageDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
            }

            List<Assessment> assessments = db.assessmentDao().getAssessmentsByCourseId(this.courseId);
            if(assessments.size() > 0) {
                for(Assessment assessment : assessments) {
                    TextView viewAssessment = new TextView(this);
                    viewAssessment.setText(assessment.getTitle());
                    llAssessments.addView(viewAssessment);
                }
            } else {
                TextView noAssessments = new TextView(this);
                noAssessments.setText("No assessments assigned.");
                llAssessments.addView(noAssessments);
            }

            TextView noNotes = new TextView(this);
            if(course.getNotes().isEmpty()) {
                noNotes.setText("No notes.");
            } else {
                noNotes.setText(course.getNotes());
            }
            llNotes.addView(noNotes);

        } else {
            viewTitle.setText("Error loading course");
            viewId.setText("N/A");
            viewTerm.setText("N/A");
            viewStatus.setText("N/A");
            viewStartDate.setText("N/A");
            viewEndDate.setText("N/A");
            viewInstructorName.setText("N/A");
            viewInstructorPhone.setText("N/A");
            viewInstructorEmail.setText("N/A");

            TextView noAssessments = new TextView(this);
            noAssessments.setText("N/A");
            llAssessments.addView(noAssessments);

            TextView noNotes = new TextView(this);
            noNotes.setText("N/A");
            llNotes.addView(noNotes);
        }
    }
}