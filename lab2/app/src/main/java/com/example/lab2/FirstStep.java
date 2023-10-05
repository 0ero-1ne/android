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
    String firstName;
    String lastName;
    String middleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_step);

        sharedPreferences = getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE);

        Button btn = findViewById(R.id.nextPageButton);
        btn.setOnClickListener(v -> {
            this.firstName = String.valueOf(((EditText)findViewById(R.id.firstNameEditText)).getText());
            this.middleName = String.valueOf(((EditText)findViewById(R.id.middleNameEditText)).getText());
            this.lastName = String.valueOf(((EditText)findViewById(R.id.lastNameEditText)).getText());

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
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            intent.putExtra("middleName", middleName);

            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstName", this.firstName);
        editor.putString("lastName", this.lastName);
        editor.putString("middleName", this.middleName);
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