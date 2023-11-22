package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lab5.databinding.ActivityEventBinding;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {
    ActivityEventBinding binding;
    ArrayList<Bitmap> imagesList;
    ArrayList<Uri> uri;
    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int eventId = getIntent().getIntExtra("eventId", -1);
        if (eventId == -1) {
            return;
        }

        Event event = EventSavier.getEventById(eventId, this);

        TextView eventTitle = binding.eventTitle;
        TextView eventDescription = binding.eventDescription;
        TextView eventDate = binding.eventDate;

        recyclerView = binding.eventImages;
        imagesList = event.getImagesList();
        uri = event.getUriImagesList();

        recyclerAdapter = new RecyclerAdapter(imagesList, uri,this, false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(recyclerAdapter);

        MaterialToolbar toolbar = binding.appToolbar;
        toolbar.setOnMenuItemClickListener(backButtonListener);

        eventTitle.setText(event.getName());
        eventDescription.setText(event.getDescription());
        eventDate.setText(event.getDate());
    }

    private final MaterialToolbar.OnMenuItemClickListener backButtonListener = (item) -> {
        if (item.getItemId() == R.id.navigation_back) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return false;
    };
}