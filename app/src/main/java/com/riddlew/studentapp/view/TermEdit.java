package com.riddlew.studentapp.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.riddlew.studentapp.R;
import com.riddlew.studentapp.database.AppDatabase;
import com.riddlew.studentapp.model.Assessment;
import com.riddlew.studentapp.model.AssessmentType;
import com.riddlew.studentapp.model.Course;
import com.riddlew.studentapp.model.Term;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TermEdit extends AppCompatActivity {

    EditText editTitle;
    TextView detailId;
    TextView editStartDate;
    Button btnPickStart;
    TextView editEndDate;
    Button btnPickEnd;
    Button btnEditAssignedCourses;
    LinearLayout llAssignedCourses;
    Button btnSave;
    DatePickerDialog datePicker;

    List<Course> assignedCourses;

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
        setContentView(R.layout.activity_term_edit);

        editTitle = findViewById(R.id.editTitle);
        detailId = findViewById(R.id.detailId);
        editStartDate = findViewById(R.id.editStartDate);
        btnPickStart = findViewById(R.id.btnPickStart);
        editEndDate = findViewById(R.id.editEndDate);
        btnPickEnd = findViewById(R.id.btnPickEnd);
        btnEditAssignedCourses = findViewById(R.id.btnEditAssignedCourses);
        llAssignedCourses = findViewById(R.id.linearLayoutAssignedCourses);
        btnSave = findViewById(R.id.btnSave);

        // Set up Start Datepicker
        btnPickStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                LocalDate date = LocalDate.parse(editStartDate.getText());
                int day = date.getDayOfMonth();
                int month = date.getMonthValue() - 1;
                int year = date.getYear();
                datePicker = new DatePickerDialog(TermEdit.this, new DatePickerDialog.OnDateSetListener() {
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
                datePicker = new DatePickerDialog(TermEdit.this, new DatePickerDialog.OnDateSetListener() {
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
        // Set values
        Intent intent = getIntent();
        final int termId = intent.getIntExtra("term_id", 0);

        if(termId == 0) {
            // Default information
            detailId.setText("Auto Generate");
            editStartDate.setText(LocalDate.now().toString());
            editEndDate.setText(LocalDate.now().toString());
            assignedCourses = new ArrayList<>();
        } else {
            // Load course information
            Term term = db.termDao().getTerm(termId);
            assignedCourses = db.courseDao().getCoursesAssignedToTermId(termId);

            editTitle.setText(term.getTitle());
            detailId.setText(Integer.toString(termId));
            editStartDate.setText(term.getStartDate().toString());
            editEndDate.setText(term.getEndDate().toString());

            if(assignedCourses.size() > 0) {
                for(Course course : assignedCourses) {
                    TextView viewCourse = new TextView(this);
                    viewCourse.setText(course.getTitle());
                    llAssignedCourses.addView(viewCourse);
                }
            } else {
                TextView viewCourse = new TextView(this);
                viewCourse.setText("No courses assigned.");
                llAssignedCourses.addView(viewCourse);
            }
        }

        List<Course> courses = db.courseDao().getUnassignedCourses();
        if(assignedCourses.size() > 0) courses.addAll(assignedCourses);
        String[] courseTitles = courses.stream().map(Course::getTitle).toArray(String[]::new);
        boolean[] checked = new boolean[courseTitles.length];
        Arrays.fill(checked, false);

        for(int i = 0; i < courses.size(); i++) {
            for(int j = 0; j < assignedCourses.size(); j++) {
                if(courses.get(i).equals(assignedCourses.get(j))) {
                    checked[i] = true;
                }
            }
        }

        final ArrayList<Integer> selectedItems = new ArrayList<>();

        btnEditAssignedCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TermEdit.this);
                builder.setTitle("Choose classes");


                builder.setMultiChoiceItems(courseTitles, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b) {
                            selectedItems.add(i);
                            checked[i] = true;
                        } else if(selectedItems.contains(i)) {
                            selectedItems.remove(Integer.valueOf(i));
                            checked[i] = false;
                        }
                    }
                });

                builder.setPositiveButton("Add / Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        assignedCourses.clear();
                        llAssignedCourses.removeAllViews();
                        for(int idx : selectedItems) {
                            Course course = courses.get(idx);
                            assignedCourses.add(course);
                            TextView tv = new TextView(TermEdit.this);
                            tv.setText(courses.get(idx).getTitle());
                            llAssignedCourses.addView(tv);
                        }
                    }
                });
                builder.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer tId = termId;

                // Validate

                String title = editTitle.getText().toString().trim();
                if(title.isEmpty()) {
                    alertInvalidInput("Title must not be empty.");
                    return;
                }

                // Save
                Term term = new Term();

                term.setTitle(title);
                term.setStartDate(LocalDate.parse(editStartDate.getText().toString()));
                term.setEndDate(LocalDate.parse(editEndDate.getText().toString()));

                if(tId != 0) {
                    term.setId(tId);
                    db.termDao().updateTerm(term);
                } else {
                    tId = (int) db.termDao().insertTerm(term);
                }

                for(Course course : courses) {
                    if(assignedCourses.contains(course)) {
                        course.setTermId(tId);
                        db.courseDao().updateCourse(course);
                    } else {
                        course.setTermId(null);
                        db.courseDao().updateCourse(course);
                    }
                }

                finish();
            }
        });
    }

    private void alertInvalidInput(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TermEdit.this);
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