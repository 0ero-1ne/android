package com.example.lab5;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventSavier {
    public static void saveEvent(Event event, Context context) {
        List<Event> events = getEvents(context);
        if (event == null) {
            return;
        }
        events.add(event);
        saveEvents(events, context);
    }

    public static void saveEvents(List<Event> events, Context context) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            FileOutputStream fos = context.openFileOutput("events.json", android.content.Context.MODE_PRIVATE);
            String output = gson.toJson(events);
            System.out.println(output);
            fos.write(output.getBytes());
            fos.close();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static List<Event> getEvents(Context context) {
        Gson gson = new Gson();
        List<Event> events = null;

        try {
            FileInputStream fin = context.openFileInput("events.json");
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String input = new String(bytes).equals("") ? "[]" : new String(bytes);
            events = gson.fromJson(input, new TypeToken<List<Event>>() {}.getType());

            for (Event event: events) {
                event.ImagesList = new ArrayList<>();
                event.castStringsToUri(event.StringImagesList);
            }
            
            fin.close();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return events;
    }

    public static Event getEventById(int eventId, Context context) {
        List<Event> events = getEvents(context);
        return events.get(eventId);
    }

    public static void deleteEventById(int eventId, Context context) {
        List<Event> events = getEvents(context);
        events.remove(eventId);
        saveEvents(events, context);
    }
}
