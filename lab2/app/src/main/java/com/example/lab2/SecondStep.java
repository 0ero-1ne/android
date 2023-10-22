package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class SecondStep extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    String[] faculties = new String[]{
        "Choose faculty...", "ФИТ", "ТОВ", "ХТиТ"
    };

    String[][] specialities = new String[][] {
        { "ПОИТ", "ИСИТ", "ДЭВИ", "ПОИБМС" },
        { "ПНГиПОС", "ПППМ", "ТПБ", "ФХМПКПП", "ПБ", "ТЛП" },
        { "АТПП", "ТМО", "ПКМ", "ПИТТ", "ТНВ", "ИЭ" }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_step);

        sharedPreferences = getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE);

        Spinner facultySpinner = findViewById(R.id.facultySpinner);
        facultySpinner.setDropDownVerticalOffset(150);

        ArrayAdapter<String> facultyAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            faculties
        );

        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facultySpinner.setAdapter(facultyAdapter);

        facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    findViewById(R.id.specialitySpinner).setEnabled(false);
                    ((Spinner)findViewById(R.id.specialitySpinner)).setAdapter(null);
                    return;
                }

                Spinner specialitySpinner = findViewById(R.id.specialitySpinner);
                specialitySpinner.setEnabled(true);
                specialitySpinner.setDropDownVerticalOffset(150);

                String[] speciality = specialities[position - 1];

                ArrayAdapter<String> specialityAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    android.R.layout.simple_spinner_item,
                    speciality
                );
                specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                specialitySpinner.setAdapter(specialityAdapter);

                String savedSpecialityIndex = sharedPreferences.getString("speciality", "");
                int specialityIndex = Arrays.asList(speciality).indexOf(savedSpecialityIndex);
                specialitySpinner.setSelection(Math.max(specialityIndex, 0));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button nextPageButton = findViewById(R.id.nextPageButton);
        Button prevPageButton = findViewById(R.id.prevPageButton);

        prevPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FirstStep.class);
            startActivity(intent);
        });

        nextPageButton.setOnClickListener(v -> {
            String faculty = facultySpinner.getSelectedItem().toString();

            if (faculty.equals(faculties[0])) {
                ((TextView)facultySpinner.getSelectedView()).setError("");
                return;
            }

            Intent intent = new Intent(this, ThirdStep.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("faculty", ((Spinner)findViewById(R.id.facultySpinner)).getSelectedItem().toString());

        if (((Spinner) findViewById(R.id.facultySpinner)).getSelectedItem().toString().equals("Choose faculty...")) {
            editor.putString("speciality", "");
        } else {
            editor.putString("speciality", ((Spinner)findViewById(R.id.specialitySpinner)).getSelectedItem().toString());
        }

        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Spinner specialitySpinner = findViewById(R.id.specialitySpinner);
        Spinner facultySpinner = findViewById(R.id.facultySpinner);
        String faculty = sharedPreferences.getString("faculty", "Choose faculty...");

        int facultyIndex = Arrays.asList(faculties).indexOf(faculty);

        String speciality = sharedPreferences.getString("speciality", "");

        facultySpinner.setSelection(facultyIndex);

        if (facultyIndex == 0) {
            specialitySpinner.setAdapter(null);
            return;
        }

        int specialityIndex = Arrays.asList(specialities[facultyIndex - 1]).indexOf(speciality);
        specialitySpinner.setSelection(specialityIndex);
    }
}