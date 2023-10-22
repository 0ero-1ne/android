package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Gson gson = new Gson();
    List<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean clearEdit = getIntent().getBooleanExtra("clearEdit", false);

        if (clearEdit) {
            getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE).edit().clear().apply();
        }

        File dir = getFilesDir();
        File file = new File(dir, "students.json");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileInputStream fin = openFileInput("students.json");
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String json = new String (bytes);
            students = gson.fromJson(json, new TypeToken<List<Student>>() {}.getType());
            fin.close();

            if (students == null) {
                students = new ArrayList<>();
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        ListView listView = findViewById(R.id.studentsList);
        String[] studentsInfo = new String[students.size()];

        for (int i = 0; i < students.size(); i++) {
            studentsInfo[i] = students.get(i).getStudentInfo();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<> (
            this,
            android.R.layout.simple_list_item_1,
            studentsInfo
        );

        listView.setAdapter(adapter);

        Button btn = findViewById(R.id.createStudent);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, FirstStep.class);
            startActivity(intent);
            finish();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, StudentInfo.class);
            intent.putExtra("student", new Gson().toJson(students.get(position)));
            startActivity(intent);
        });
    }
}