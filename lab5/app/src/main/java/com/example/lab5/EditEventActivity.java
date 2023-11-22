package com.example.lab5;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab5.databinding.ActivityEditEventBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EditEventActivity extends AppCompatActivity {
    ActivityEditEventBinding binding;
    ArrayList<Bitmap> bitmaps;
    ArrayList<Uri> uri;
    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    List<Event> eventList;
    int eventId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        eventId = getIntent().getIntExtra("eventId", -1);
        eventList = EventSavier.getEvents(this);
        Event event = eventList.get(eventId);
        bitmaps = event.getImagesList();
        uri = event.getUriImagesList();

        TextView eventName = binding.eventNameEditText;
        TextView eventDate = binding.eventDateEditText;
        TextView eventDescription = binding.eventDescriptionEditText;

        recyclerView = binding.eventPickedImages;
        recyclerAdapter = new RecyclerAdapter(bitmaps, uri, this, true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();

        eventName.setText(event.getName());
        eventDate.setText(event.getDate());
        eventDescription.setText(event.getDescription());

        Button eventSaveButton = binding.eventSaveButton;
        eventSaveButton.setOnClickListener(saveEvent);

        Button eventPickImages = binding.eventPickImagesButton;
        eventPickImages.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Pick images"), 1);
        });

        eventDate.setOnClickListener(datePickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            assert data != null;
            if (data.getData() != null) {
                if (!uri.contains(data.getData())) {
                    try {
                        bitmaps.add(
                                ImageDecoder.decodeBitmap(
                                        ImageDecoder.createSource(
                                                getContentResolver(),
                                                data.getData()
                                        )
                                )
                        );
                        uri.add(data.getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (data.getClipData() != null) {
                int dataClipCount = data.getClipData().getItemCount();

                for (int i = 0; i < dataClipCount; i++) {
                    if (!uri.contains(data.getClipData().getItemAt(i).getUri())) {
                        try {
                            bitmaps.add(
                                    ImageDecoder.decodeBitmap(
                                            ImageDecoder.createSource(
                                                    getContentResolver(),
                                                    data.getClipData().getItemAt(i).getUri()
                                            )
                                    )
                            );
                            uri.add(data.getClipData().getItemAt(i).getUri());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    private final View.OnClickListener datePickListener = v -> {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, y, m, d) -> {
            String formatDay = d < 10 ? "0" + d : d + "";
            String formatMonth = (m + 1) < 10 ? "0" + (m + 1) : m + 1 + "";

            binding.eventDateEditText.setText(getString(R.string.date_format, formatDay, formatMonth, y));
            binding.eventDateEditText.setError(null);
        }, year, month, day);

        datePickerDialog.show();
    };

    private final View.OnClickListener saveEvent = v -> {
        if (binding.eventNameEditText.getText().toString().equals("")) {
            binding.eventNameEditText.setError("Missing input");
            return;
        }

        if (binding.eventDateEditText.getText().toString().equals("")) {
            binding.eventDateEditText.setError("Missing input");
            return;
        }

        if (binding.eventDescriptionEditText.getText().toString().equals("")) {
            binding.eventDescriptionEditText.setError("Missing input");
            return;
        }

        Event event = new Event(
                binding.eventNameEditText.getText().toString(),
                binding.eventDateEditText.getText().toString(),
                binding.eventDescriptionEditText.getText().toString(),
                bitmaps,
                uri
        );

        eventList.set(eventId, event);
        EventSavier.saveEvents(eventList, this);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    };
}