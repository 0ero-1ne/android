package com.example.lab5.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lab5.EditEventActivity;
import com.example.lab5.Event;
import com.example.lab5.EventActivity;
import com.example.lab5.EventSavier;
import com.example.lab5.R;
import com.example.lab5.databinding.FragmentSearchBinding;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private ListView listView;
    private List<Event> eventsList;
    SimpleAdapter simpleAdapter;
    List<Map<String, String>> listItems;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button searchButton = binding.eventSearchButton;
        searchButton.setOnClickListener(searchEvents);

        eventsList = EventSavier.getEvents(requireContext());
        listView = binding.eventsList;

        listView.setOnItemLongClickListener(showPopupMenu);

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

    private View.OnClickListener searchEvents = (view) -> {
        String eventName = Objects.requireNonNull(binding.eventNameEditText.getText()).toString().toLowerCase();
        listItems = new ArrayList<>();
        Event event;

        String[] eventsNames;
        String[] eventsDates;

        if (eventName.equals("")) {
            eventsNames = new String[eventsList.size()];
            eventsDates = new String[eventsList.size()];

            for (int i = 0; i < eventsNames.length; i++) {
                eventsNames[i] = eventsList.get(i).getName();
                eventsDates[i] = eventsList.get(i).getDate();
            }
        } else {
            event = eventsList.stream().filter(ev -> eventName.equals(ev.getName())).findFirst().orElse(null);

            if (event != null) {
                eventsNames = new String[] { event.getName() };
                eventsDates = new String[] { event.getDate() };
            } else {
                eventsNames = new String[eventsList.size()];
                eventsDates = new String[eventsList.size()];

                for (int i = 0; i < eventsNames.length; i++) {
                    eventsNames[i] = eventsList.get(i).getName();
                    eventsDates[i] = eventsList.get(i).getDate();
                }
            }
        }

        for (int i = 0; i < eventsNames.length; i++) {
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
    };
}