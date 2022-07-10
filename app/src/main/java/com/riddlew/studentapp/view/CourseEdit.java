package com.riddlew.studentapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.riddlew.studentapp.NotifyReceiver;
import com.riddlew.studentapp.R;
import com.riddlew.studentapp.database.AppDatabase;
import com.riddlew.studentapp.model.Assessment;
import com.riddlew.studentapp.model.Course;
import com.riddlew.studentapp.model.CourseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CourseEdit extends AppCompatActivity {

    private static int ALERT_ID = 0;

    EditText editTitle;
    TextView detailId;
    Spinner editStatus;
    TextView editStartDate;
    Button btnPickStart;
    TextView editEndDate;
    Button btnPickEnd;
    EditText editInstructorName;
    EditText editInstructorPhone;
    EditText editInstructorEmail;
    CheckBox editNotificationCB;
    LinearLayout llAssessments;
    EditText editNotes;
    Button btnSave;
    DatePickerDialog datePicker;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        editTitle = findViewById(R.id.editTitle);
        detailId = findViewById(R.id.detailId);
        editStatus = findViewById(R.id.editStatus);
        editStartDate = findViewById(R.id.editStartDate);
        btnPickStart = findViewById(R.id.btnPickStart);
        editEndDate = findViewById(R.id.editEndDate);
        btnPickEnd = findViewById(R.id.btnPickEnd);
        editInstructorName = findViewById(R.id.editInstructorName);
        editInstructorPhone = findViewById(R.id.editInstructorPhone);
        editInstructorEmail = findViewById(R.id.editInstructorEmail);
        editNotificationCB = findViewById(R.id.editNotificationCB);
        llAssessments = findViewById(R.id.linearLayoutAssessments);
        editNotes = findViewById(R.id.editNotes);
        btnSave = findViewById(R.id.btnSave);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.course_status, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editStatus.setAdapter(adapter);
        editStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Set up Start Datepicker
        btnPickStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                LocalDate date = LocalDate.parse(editStartDate.getText());
                int day = date.getDayOfMonth();
                int month = date.getMonthValue() - 1;
                int year = date.getYear();
                datePicker = new DatePickerDialog(CourseEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        LocalDate date = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                        editStartDate.setText(date.toString());
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        // Set up End Datepicker
        btnPickEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                LocalDate date = LocalDate.parse(editEndDate.getText());
                int day = date.getDayOfMonth();
                int month = date.getMonthValue() - 1;
                int year = date.getYear();
                datePicker = new DatePickerDialog(CourseEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        LocalDate date = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                        editEndDate.setText(date.toString());
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        // Set values
        Intent intent = getIntent();
        int courseId = intent.getIntExtra("course_id", 0);

        if(courseId == 0) {
            // Default information
            detailId.setText("Auto Generate");
            editStatus.setSelection(CourseStatus.PLAN_TO_TAKE.getValue());
            editStartDate.setText(LocalDate.now().toString());
            editEndDate.setText(LocalDate.now().toString());
            TextView noAssessments = new TextView(this);
            noAssessments.setText("No assessments assigned.");
            llAssessments.addView(noAssessments);
        } else {
            // Load course information
            AppDatabase db = AppDatabase.getInstance(this);
            Course course = db.courseDao().getCourse(courseId);

            editTitle.setText(course.getTitle());
            detailId.setText(Integer.toString(courseId));
            editStatus.setSelection(course.getStatus().getValue());
            editStartDate.setText(course.getStartDate().toString());
            editEndDate.setText(course.getEndDate().toString());
            editInstructorName.setText(course.getInstructorName());
            editInstructorPhone.setText(course.getInstructorPhone());
            editInstructorEmail.setText(course.getInstructorEmail());
            editNotificationCB.setChecked(course.shouldNotifyDates());

            List<Assessment> assessments = db.assessmentDao().getAssessmentsByCourseId(courseId);
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

            editNotes.setText(course.getNotes());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Validate

                String title = editTitle.getText().toString().trim();
                if(title.isEmpty()) {
                    alertInvalidInput("Title must not be empty.");
                    return;
                }

                String instructorName = editInstructorName.getText().toString().trim();
                if(instructorName.isEmpty()) {
                    alertInvalidInput("Instructor name must not be empty.");
                    return;
                }

                String instructorPhone = editInstructorPhone.getText().toString().trim();
                if(instructorPhone.isEmpty()) {
                    alertInvalidInput("Instructor phone number must not be empty.");
                    return;
                }

                String instructorEmail = editInstructorEmail.getText().toString().trim();
                if(instructorEmail.isEmpty()) {
                    alertInvalidInput("Instructor email must not be empty.");
                    return;
                }


                // Save
                Course course = new Course();

                course.setTitle(title);
                course.setStatus(CourseStatus.fromString(editStatus.getSelectedItem().toString()));
                course.setStartDate(LocalDate.parse(editStartDate.getText().toString()));
                course.setEndDate(LocalDate.parse(editEndDate.getText().toString()));
                course.setInstructorName(instructorName);
                course.setInstructorPhone(instructorPhone);
                course.setInstructorEmail(instructorEmail);
                course.setShouldNotifyDates(editNotificationCB.isChecked());
                course.setNotes(editNotes.getText().toString());

                AppDatabase db = AppDatabase.getInstance(CourseEdit.this);

                if(courseId != 0) {
                    course.setId(courseId);
                    db.courseDao().updateCourse(course);
                } else {
                    db.courseDao().insertCourse(course);
                }

                if(course.shouldNotifyDates()) {
                    Intent startIntent = new Intent(CourseEdit.this, NotifyReceiver.class);
                    startIntent.putExtra("text", "Course " + course.getTitle() + " starts today!");
                    PendingIntent pendingStart = PendingIntent.getBroadcast(CourseEdit.this, ALERT_ID, startIntent, 0);
                    AlarmManager startAM = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    LocalDateTime ldtStart = course.getStartDate().atStartOfDay();
                    ZonedDateTime zdtStart = ldtStart.atZone(ZoneId.systemDefault());
                    long longStart = zdtStart.toInstant().toEpochMilli();
                    startAM.set(AlarmManager.RTC_WAKEUP, longStart, pendingStart);
                    ALERT_ID++;

                    Intent endIntent = new Intent(CourseEdit.this, NotifyReceiver.class);
                    endIntent.putExtra("text", "Course " + course.getTitle() + " ends today!");
                    PendingIntent pendingEnd = PendingIntent.getBroadcast(CourseEdit.this, ALERT_ID, endIntent, 0);
                    AlarmManager endAM = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    LocalDateTime ldtEnd = course.getEndDate().atStartOfDay();
                    ZonedDateTime zdtEnd = ldtEnd.atZone(ZoneId.systemDefault());
                    long longEnd = zdtEnd.toInstant().toEpochMilli();
                    endAM.set(AlarmManager.RTC_WAKEUP, longEnd, pendingEnd);
                    ALERT_ID++;
                }

                finish();
            }
        });


    }

    private void alertInvalidInput(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CourseEdit.this);
        builder.setTitle("Invalid Input");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}