package com.example.lab5;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Event {
    String Name;
    String Date;
    String Description;
    transient ArrayList<Bitmap> ImagesList;
    transient ArrayList<Uri> uri;
    String[] StringImagesList;
    String[] UriImagesList;

    public Event(
            String name,
            String date,
            String description,
            ArrayList<Bitmap> bitmapArrayList,
            ArrayList<Uri> uri)
    {
        this.Name = name;
        this.Date = date;
        this.Description = description;
        this.ImagesList = bitmapArrayList;
        this.uri = uri;
        setStringImagesList(bitmapArrayList);
        setStringImagesListFromUri(uri);
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

    public ArrayList<Bitmap> getImagesList() { return ImagesList; }

    public ArrayList<Uri> getUriImagesList() { return uri; }

    public void castStringsToBitmap(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] == null) {
                break;
            }
            try {
                byte [] encodeByte=Base64.decode(strings[i],Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                ImagesList.add(bitmap);
            } catch(Exception e) {
                e.getMessage();
                return;
            }
        }
    }

    public void castStringsToUri(String[] strings)
    {
        uri = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] == null) {
                break;
            }
            uri.add(Uri.parse(strings[i]));
        }
    }

    private void setStringImagesList(ArrayList<Bitmap> bitmapArrayList) {
        StringImagesList = new String[ImagesList.size()];

        for (int i = 0; i < StringImagesList.length; i++) {
            ByteArrayOutputStream baos = new  ByteArrayOutputStream();
            bitmapArrayList.get(i).compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            StringImagesList[i] = temp;
        }
    }

    private void setStringImagesListFromUri(ArrayList<Uri> uriArrayList) {
        UriImagesList = new String[ImagesList.size()];

        for (int i = 0; i < UriImagesList.length; i++) {
            UriImagesList[i] = uriArrayList.get(i).toString();
        }
    }
}
