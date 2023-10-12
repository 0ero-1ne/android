package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LastStep extends AppCompatActivity {
    private final static String FILE_NAME = "students.json";
    SharedPreferences sharedPreferences;
    String admissionDate;
    int currentCourse;
    List<Student> students;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_step);

        sharedPreferences = getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE);

        EditText dateEditText = findViewById(R.id.dateEditText);

        dateEditText.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
                String formatDay = day < 10 ? "0" + day : day + "";
                String formatMonth = (month + 1) < 10 ? "0" + (month + 1) : month + 1 + "";
                ((EditText) view).setText(getString(R.string.date_format, formatDay, formatMonth, year));
                ((EditText) view).setError(null);
                admissionDate = getString(R.string.date_format, formatDay, formatMonth, year);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });

        RadioGroup courseGroup = findViewById(R.id.coursesGroup);
        courseGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            currentCourse = i;
            ((RadioButton)findViewById(R.id.radio5)).setError(null);
        });

        Button prevPageButton = findViewById(R.id.prevPageButton);

        prevPageButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SecondStep.class);
            startActivity(intent);
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            if (admissionDate.equals("")) {
                dateEditText.setError("Pick admission date");
                return;
            }

            if (currentCourse == -1) {
                ((RadioButton)findViewById(R.id.radio5)).setError("Choose your current course");
            }

            int formattedCourse = Integer.parseInt(((RadioButton)findViewById(currentCourse)).getText().toString());

            Student student = new Student(
                    sharedPreferences.getString("firstName", ""),
                    sharedPreferences.getString("lastName", ""),
                    sharedPreferences.getString("middleName", ""),
                    sharedPreferences.getString("faculty", ""),
                    sharedPreferences.getString("speciality", ""),
                    sharedPreferences.getString("admissionDate", ""),
                    formattedCourse
            );

            getStudentsFromFile();
            students.add(student);
            saveStudentsIntoFile();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("clearEdit", true);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("admissionDate", admissionDate);
        editor.putInt("currentCourse", currentCourse);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        admissionDate = sharedPreferences.getString("admissionDate", "");
        currentCourse = sharedPreferences.getInt("currentCourse", -1);

        ((EditText) findViewById(R.id.dateEditText)).setText(admissionDate);

        if (currentCourse != -1) {
            RadioButton radioButton = findViewById(currentCourse);
            radioButton.setChecked(true);
        }
    }

    private void getStudentsFromFile() {
        Gson gson = new Gson();

        try {
            FileInputStream fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String input = new String(bytes);
            students = gson.fromJson(input, new TypeToken<List<Student>>() {}.getType());
            fin.close();

            if (students == null) {
                students = new ArrayList<>();
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void saveStudentsIntoFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            String output = gson.toJson(students);
            fos.write(output.getBytes());
            fos.close();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}