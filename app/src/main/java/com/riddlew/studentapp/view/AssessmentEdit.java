package com.riddlew.studentapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.riddlew.studentapp.NotifyReceiver;
import com.riddlew.studentapp.R;
import com.riddlew.studentapp.database.AppDatabase;
import com.riddlew.studentapp.model.Assessment;
import com.riddlew.studentapp.model.AssessmentType;
import com.riddlew.studentapp.model.Course;
import com.riddlew.studentapp.model.CourseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class AssessmentEdit extends AppCompatActivity {

    private static int ALERT_ID = 0;

    EditText editTitle;
    TextView detailId;
    Spinner editType;
    TextView editStartDate;
    Button btnPickStart;
    TextView editEndDate;
    Button btnPickEnd;
    Spinner editCourse;
    CheckBox editNotificationCB;
    Button btnSave;
    DatePickerDialog datePicker;

    List<Course> courses;

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
        setContentView(R.layout.activity_assessment_edit);

        editTitle = findViewById(R.id.editTitle);
        detailId = findViewById(R.id.detailId);
        editType = findViewById(R.id.editType);
        editStartDate = findViewById(R.id.editStartDate);
        btnPickStart = findViewById(R.id.btnPickStart);
        editEndDate = findViewById(R.id.editEndDate);
        btnPickEnd = findViewById(R.id.btnPickEnd);
        editCourse = findViewById(R.id.editCourse);
        editNotificationCB = findViewById(R.id.editNotificationCB);
        btnSave = findViewById(R.id.btnSave);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.assessment_type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editType.setAdapter(adapter);
        editType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                datePicker = new DatePickerDialog(AssessmentEdit.this, new DatePickerDialog.OnDateSetListener() {
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
                datePicker = new DatePickerDialog(AssessmentEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        LocalDate date = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                        editEndDate.setText(date.toString());
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        // Load all courses
        AppDatabase db = AppDatabase.getInstance(this);
        courses = db.courseDao().getCourses();

        ArrayAdapter<Course> courseAdapter = new ArrayAdapter<Course>(this, android.R.layout.simple_spinner_dropdown_item, courses);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCourse.setAdapter(courseAdapter);
        editCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("SPINNER", adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Set values
        Intent intent = getIntent();
        int assessmentId = intent.getIntExtra("assessment_id", 0);

        if(assessmentId == 0) {
            // Default information
            detailId.setText("Auto Generate");
            editType.setSelection(0);
            editStartDate.setText(LocalDate.now().toString());
            editEndDate.setText(LocalDate.now().toString());
        } else {
            // Load course information
            Assessment assessment = db.assessmentDao().getAssessment(assessmentId);
            editTitle.setText(assessment.getTitle());
            detailId.setText(Integer.toString(assessmentId));
            editType.setSelection(assessment.getType().getValue());
            editStartDate.setText(assessment.getStartDate().toString());
            editEndDate.setText(assessment.getEndDate().toString());
            if(assessment.getCourseId() != null) {
                editCourse.setSelection(
                        courses.indexOf(
                                courses.stream()
                                        .filter(x -> x.getId() == assessment.getCourseId())
                                        .findFirst()
                                        .get()
                        )
                );
            }
            editNotificationCB.setChecked(assessment.shouldNotifyDates());
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

                Course course = (Course) editCourse.getSelectedItem();
                if(course == null) {
                    alertInvalidInput("Course must not be empty.");
                    return;
                }


                // Save
                Assessment assessment = new Assessment();

                assessment.setTitle(title);

                assessment.setType(AssessmentType.fromString(editType.getSelectedItem().toString()));
                assessment.setStartDate(LocalDate.parse(editStartDate.getText().toString()));
                assessment.setEndDate(LocalDate.parse(editEndDate.getText().toString()));
                assessment.setCourseId(((Course) editCourse.getSelectedItem()).getId());
                assessment.setShouldNotifyDates(editNotificationCB.isChecked());

                if(assessmentId != 0) {
                    assessment.setId(assessmentId);
                    db.assessmentDao().updateAssessment(assessment);
                } else {
                    db.assessmentDao().insertAssessment(assessment);
                }

                if(assessment.shouldNotifyDates()) {
                    Intent startIntent = new Intent(AssessmentEdit.this, NotifyReceiver.class);
                    startIntent.putExtra("text", "Assessment " + assessment.getTitle() + " starts today!");
                    PendingIntent pendingStart = PendingIntent.getBroadcast(AssessmentEdit.this, ALERT_ID, startIntent, 0);
                    AlarmManager startAM = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    LocalDateTime ldtStart = assessment.getStartDate().atStartOfDay();
                    ZonedDateTime zdtStart = ldtStart.atZone(ZoneId.systemDefault());
                    long longStart = zdtStart.toInstant().toEpochMilli();
                    startAM.set(AlarmManager.RTC_WAKEUP, longStart, pendingStart);
                    ALERT_ID++;

                    Intent endIntent = new Intent(AssessmentEdit.this, NotifyReceiver.class);
                    endIntent.putExtra("text", "Assessment " + assessment.getTitle() + " ends today!");
                    PendingIntent pendingEnd = PendingIntent.getBroadcast(AssessmentEdit.this, ALERT_ID, endIntent, 0);
                    AlarmManager endAM = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    LocalDateTime ldtEnd = assessment.getEndDate().atStartOfDay();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentEdit.this);
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

//    @Override
//    public void onAssessmentSelected(int courseId) {
//        Log.d("TEST", "ADD ASSESSMENT ID " + courseId);
//    }
}