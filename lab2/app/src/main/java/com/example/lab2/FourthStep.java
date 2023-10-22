package com.example.lab2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FourthStep extends AppCompatActivity {
    private final static String FILE_NAME = "students.json";
    List<Student> students;
    String email;
    String phone;
    String socialMedia;
    String image;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_step);

        sharedPreferences = getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE);

        Button backPageButton = findViewById(R.id.prevPageButton);
        Button createStudentButton = findViewById(R.id.createStudentButton);
        ImageView imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(v ->
            ImagePicker.with(FourthStep.this)
                    .crop()
                    .compress(4096)
                    .maxResultSize(300, 300)
                    .start()
        );

        createStudentButton.setOnClickListener(v -> {
            getInputsData();

            if (email.equals("")) {
                ((EditText)findViewById(R.id.emailEditText)).setError("Missing input");
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                ((EditText)findViewById(R.id.emailEditText)).setError("Wrong e-mail address");
                return;
            }

            if (phone.equals("")) {
                ((EditText)findViewById(R.id.phoneEditText)).setError("Missing input");
                return;
            } else if (!phone.matches("^[(][29|33|44|25]{2}[)] [0-9]{3}-[0-9]{2}-[0-9]{2}$")) {
                ((EditText)findViewById(R.id.phoneEditText)).setError("Phone pattern:\n(29|44|33|25) XXX-XX-XX");
                return;
            }

            if (socialMedia.equals("")) {
                ((EditText)findViewById(R.id.socialMediaEditText)).setError("Missing input");
                return;
            }

            if (findViewById(R.id.imageView).getTag().toString().equals("noImage")) {
                Toast.makeText(getApplicationContext(), "Pick image", Toast.LENGTH_SHORT).show();
                return;
            }

            Student student = new Student(
                    sharedPreferences.getString("firstName", ""),
                    sharedPreferences.getString("lastName", ""),
                    sharedPreferences.getString("middleName", ""),
                    sharedPreferences.getString("faculty", ""),
                    sharedPreferences.getString("speciality", ""),
                    sharedPreferences.getString("admissionDate", ""),
                    sharedPreferences.getInt("currentCourse", -1),
                    sharedPreferences.getString("email", this.email),
                    sharedPreferences.getString("phone", this.phone),
                    sharedPreferences.getString("socialMedia", this.socialMedia),
                    this.image
            );

            getStudentsFromFile();
            students.add(student);
            saveStudentsIntoFile();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("clearEdit", true);
            startActivity(intent);
        });

        backPageButton.setOnClickListener(v -> {
            getInputsData();
            Intent intent = new Intent(this, ThirdStep.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri uri = data.getData();
            ((ImageView)findViewById(R.id.imageView)).setImageURI(uri);
            findViewById(R.id.imageView).setTag("image");
            this.image = String.valueOf(uri);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        getInputsData();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("socialMedia", socialMedia);

        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        email = sharedPreferences.getString("email", "");
        phone = sharedPreferences.getString("phone", "");
        socialMedia = sharedPreferences.getString("socialMedia", "");

        ((EditText)findViewById(R.id.phoneEditText)).setText(phone);
        ((EditText)findViewById(R.id.emailEditText)).setText(email);
        ((EditText)findViewById(R.id.socialMediaEditText)).setText(socialMedia);
    }

    private void getInputsData() {
        EditText emailText = findViewById(R.id.emailEditText);
        EditText phoneText = findViewById(R.id.phoneEditText);
        EditText socialMediaText = findViewById(R.id.socialMediaEditText);

        email = emailText.getText().toString();
        phone = phoneText.getText().toString();
        socialMedia = socialMediaText.getText().toString();
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