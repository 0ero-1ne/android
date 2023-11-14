package com.example.lab5;

import android.net.Uri;

import java.util.ArrayList;

public class Event {
    String Name;
    String Date;
    String Description;
    transient ArrayList<Uri> ImagesList;
    String[] StringImagesList;

    public Event(String name, String date, String description, ArrayList<Uri> uriArrayList) {
        this.Name = name;
        this.Date = date;
        this.Description = description;
        this.ImagesList = uriArrayList;
        setStringImagesList(uriArrayList);
    }

    public String getName() {
        return Name;
    }

    public String getDate() {
        return Date;
    }

    public String getDescription() {
        return Description;
    }

    public ArrayList<Uri> getImagesList() {
        return ImagesList;
    }

    public void castStringsToUri(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] == null) {
                break;
            }
            ImagesList.add(Uri.parse(strings[i]));
        }
    }

    private void setStringImagesList(ArrayList<Uri> uriArrayList) {
        StringImagesList = new String[ImagesList.size()];

        for (int i = 0; i < StringImagesList.length; i++) {
            StringImagesList[i] = uriArrayList.get(i).toString();
        }
    }
}
