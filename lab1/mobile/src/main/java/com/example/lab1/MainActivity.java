package com.example.lab1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private EditText startDate;
    private EditText endDate;
    private final View.OnClickListener datePickListener = view -> {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String formatDay = day < 10 ? "0" + day : day + "";
                String formatMonth = (month + 1) < 10 ? "0" + (month + 1) : month + 1 + "";
                ((EditText) view).setText(getString(R.string.dateFormat, formatDay, formatMonth, year));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.textView4)).setText("");

        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);

        startDate.setOnClickListener(datePickListener);
        endDate.setOnClickListener(datePickListener);

        ((Button) findViewById(R.id.button)).setOnClickListener(view -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate start = !startDate.getText().toString().equals("") ? LocalDate.parse(startDate.getText(), formatter) : null;
            LocalDate end = !endDate.getText().toString().equals("") ? LocalDate.parse(endDate.getText(), formatter) : null;

            if (start == null || end == null) {
                Toast errorToast = Toast.makeText(getApplicationContext(), "Вы не указали дату(-ы)!", Toast.LENGTH_SHORT);
                errorToast.show();
                return;
            }

            if (start.isAfter(end) || start.equals(end)) {
                Toast errorToast = Toast.makeText(getApplicationContext(), "Неверная дата окончания больничного!", Toast.LENGTH_SHORT);
                errorToast.show();
                ((TextView) findViewById(R.id.textView4)).setText("");
            } else {
                ((TextView) findViewById(R.id.textView4)).setText(getString(R.string.payment_result, 5000));
            }
        });
    }
}