package com.example.lab5.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lab5.Event;
import com.example.lab5.EventSavier;
import com.example.lab5.MainActivity;
import com.example.lab5.R;
import com.example.lab5.RecyclerAdapter;
import com.example.lab5.databinding.FragmentNewItemBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class NewItemFragment extends Fragment {
    private FragmentNewItemBinding binding;
    private ArrayList<Uri> uri;
    private RecyclerAdapter recyclerAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        uri = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(uri, requireContext());

        TextInputEditText eventDate = binding.eventDateEditText;

        RecyclerView eventRecyclerView = binding.eventPickedImages;
        eventRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        eventRecyclerView.setAdapter(recyclerAdapter);

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            assert data != null;
            if (data.getData() != null) {
                if (!uri.contains(data.getData())) {
                    uri.add(data.getData());
                }
            } else if (data.getClipData() != null) {
                int dataClipCount = data.getClipData().getItemCount();

                for (int i = 0; i < dataClipCount; i++) {
                    if (!uri.contains(data.getClipData().getItemAt(i).getUri())) {
                        uri.add(data.getClipData().getItemAt(i).getUri());
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, y, m, d) -> {
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
                uri
        );
        EventSavier.saveEvent(event, getContext());

        startActivity(new Intent(requireContext(), MainActivity.class));
        requireActivity().finish();
    };
}