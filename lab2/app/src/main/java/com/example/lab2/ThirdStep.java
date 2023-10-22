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

import java.util.Calendar;

public class ThirdStep extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String admissionDate;
    int currentCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_step);

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

        Button nextPageButton = findViewById(R.id.saveButton);
        nextPageButton.setOnClickListener(view -> {
            if (admissionDate.equals("")) {
                dateEditText.setError("Pick admission date");
                return;
            }

            if (currentCourse == -1) {
                ((RadioButton)findViewById(R.id.radio5)).setError("Choose your current course");
                return;
            }

            Intent intent = new Intent(this, FourthStep.class);
            startActivity(intent);
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
}