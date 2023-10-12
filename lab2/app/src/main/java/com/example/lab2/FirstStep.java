package com.example.lab2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class FirstStep extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_step);

        sharedPreferences = getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE);

        Button prevPageButton = findViewById(R.id.prevPageButton);
        prevPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        Button nextPageButton = findViewById(R.id.nextPageButton);
        nextPageButton.setOnClickListener(v -> {
            String firstName = String.valueOf(((EditText)findViewById(R.id.firstNameEditText)).getText());
            String middleName = String.valueOf(((EditText)findViewById(R.id.middleNameEditText)).getText());
            String lastName = String.valueOf(((EditText)findViewById(R.id.lastNameEditText)).getText());

            if (firstName.equals("")) {
                ((EditText)findViewById(R.id.firstNameEditText)).setError("Empty first name");
                return;
            }

            if (middleName.equals("")) {
                ((EditText)findViewById(R.id.middleNameEditText)).setError("Empty second name");
                return;
            }

            if (lastName.equals("")) {
                ((EditText)findViewById(R.id.lastNameEditText)).setError("Empty last name");
                return;
            }

            Intent intent = new Intent(this, SecondStep.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstName", ((EditText)findViewById(R.id.firstNameEditText)).getText().toString());
        editor.putString("lastName", ((EditText)findViewById(R.id.lastNameEditText)).getText().toString());
        editor.putString("middleName", ((EditText)findViewById(R.id.middleNameEditText)).getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((EditText)findViewById(R.id.firstNameEditText)).setText(sharedPreferences.getString("firstName", ""));
        ((EditText)findViewById(R.id.lastNameEditText)).setText(sharedPreferences.getString("lastName", ""));
        ((EditText)findViewById(R.id.middleNameEditText)).setText(sharedPreferences.getString("middleName", ""));
    }
}