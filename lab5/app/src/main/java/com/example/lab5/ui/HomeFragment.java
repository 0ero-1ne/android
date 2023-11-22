package com.example.lab5.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.EditEventActivity;
import com.example.lab5.Event;
import com.example.lab5.EventActivity;
import com.example.lab5.EventSavier;
import com.example.lab5.R;
import com.example.lab5.RecyclerAdapter;
import com.example.lab5.databinding.FragmentHomeBinding;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {
    SimpleAdapter simpleAdapter;
    List<Map<String, String>> listItems;
    private FragmentHomeBinding binding;
    ArrayList<Bitmap> imagesList;
    ArrayList<Uri> uri;
    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    Event event;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Event> eventsList = EventSavier.getEvents(requireContext());
        ListView listView = binding.eventsList;

        listItems = new ArrayList<>();

        String[] eventsNames = new String[eventsList.size()];
        String[] eventsDates = new String[eventsList.size()];

        for (int i = 0; i < eventsList.size(); i++) {
            eventsNames[i] = eventsList.get(i).getName();
            eventsDates[i] = eventsList.get(i).getDate();
        }

        for (int i = 0; i < eventsList.size(); i++) {
            Map<String, String> item = new HashMap<>(2);
            item.put("Line 1", eventsNames[i]);
            item.put("Line 2", eventsDates[i]);
            listItems.add(item);
        }

        simpleAdapter = new SimpleAdapter(
            requireContext(),
            listItems,
            android.R.layout.simple_list_item_2,
            new String[] { "Line 1", "Line 2" },
            new int[] { android.R.id.text1, android.R.id.text2 }
        );

        listView.setAdapter(simpleAdapter);

        listView.setOnItemLongClickListener(showPopupMenu);
        listView.setOnItemClickListener(showDetails);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] mFields = popupMenu.getClass().getDeclaredFields();
            for (Field field : mFields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> popupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method mMethods = popupHelper.getMethod("setForceShowIcon", boolean.class);
                    mMethods.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private final AdapterView.OnItemClickListener showDetails = (parent, view, position, id) -> {
        if (requireActivity().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            return;
        }
        event = EventSavier.getEventById(position, requireContext());

        TextView eventTitle = binding.eventTitle;
        TextView eventDescription = binding.eventDescription;
        TextView eventDate = binding.eventDate;

        recyclerView = binding.eventImages;
        imagesList = event.getImagesList();
        uri = event.getUriImagesList();

        recyclerAdapter = new RecyclerAdapter(imagesList, uri, requireContext(), false);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        recyclerView.setAdapter(recyclerAdapter);

        eventTitle.setText(event.getName());
        eventDescription.setText(event.getDescription());
        eventDate.setText(event.getDate());
    };

    private final AdapterView.OnItemLongClickListener showPopupMenu = (parent, view, position, id) -> {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);

        setForceShowIcon(popupMenu);

        popupMenu.inflate(R.menu.context_menu);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            Intent intent;
            switch (Objects.requireNonNull(item.getTitle()).toString()) {
                case "Preview":
                    intent = new Intent(requireContext(), EventActivity.class);
                    intent.putExtra("eventId", position);
                    requireActivity().startActivity(intent);
                    break;
                case "Edit":
                    intent = new Intent(requireContext(), EditEventActivity.class);
                    intent.putExtra("eventId", position);
                    requireActivity().startActivity(intent);
                    break;
                case "Delete":
                    listItems.remove(position);
                    simpleAdapter.notifyDataSetChanged();
                    EventSavier.deleteEventById(position, requireContext());
                    break;
                default:
                    break;
            }

            return false;
        });

        return false;
    };
}