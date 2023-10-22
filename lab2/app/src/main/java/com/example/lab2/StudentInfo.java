package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class StudentInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        Student student = new Gson().fromJson(getIntent().getStringExtra("student"), Student.class);

        TextView label = findViewById(R.id.studentLabel);
        TextView phone = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);
        TextView faculty = findViewById(R.id.faculty);
        TextView socialMedia = findViewById(R.id.socialMedia);
        ImageView imageView = findViewById(R.id.imageView);

        label.setText(String.format("%s %s %s", student.lastName, student.firstName, student.middleName));
        phone.setText(getString(R.string.phone_format, student.phone));
        email.setText(getString(R.string.email_format, student.email));
        faculty.setText(getString(R.string.faculty_format, student.faculty, student.course, student.speciality));
        socialMedia.setText(getString(R.string.social_media_format, student.socialMedia));
        imageView.setImageURI(Uri.parse(student.image));

        Button mainPageButton = findViewById(R.id.mainPageButton);

        mainPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        phone.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + "80 " + student.phone));
            startActivity(callIntent);
        });

        email.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:" + student.email);
            intent.setData(data);
            startActivity(intent);
        });

        socialMedia.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri profile = Uri.parse("https://vk.com/" + student.socialMedia);
            intent.setData(profile);
            startActivity(intent);
        });
    }
}