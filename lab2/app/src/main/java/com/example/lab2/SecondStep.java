package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SecondStep extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String firstName2;
    String lastName2;
    String middleName2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_step);

        sharedPreferences = getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE);

        Button nextPageButton = findViewById(R.id.nextPageButton);
        Button prevPageButton = findViewById(R.id.prevPageButton);

        prevPageButton.setOnClickListener(v -> {
            this.firstName2 = String.valueOf(((EditText)findViewById(R.id.firstNameEditText)).getText());
            this.middleName2 = String.valueOf(((EditText)findViewById(R.id.middleNameEditText)).getText());
            this.lastName2 = String.valueOf(((EditText)findViewById(R.id.lastNameEditText)).getText());

            Intent intent = new Intent(this, FirstStep.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstName2", this.firstName2);
        editor.putString("lastName2", this.lastName2);
        editor.putString("middleName2", this.middleName2);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((EditText)findViewById(R.id.firstNameEditText)).setText(sharedPreferences.getString("firstName2", ""));
        ((EditText)findViewById(R.id.lastNameEditText)).setText(sharedPreferences.getString("lastName2", ""));
        ((EditText)findViewById(R.id.middleNameEditText)).setText(sharedPreferences.getString("middleName2", ""));
    }
}