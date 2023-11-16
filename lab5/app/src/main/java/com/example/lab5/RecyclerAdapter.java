package com.example.lab5;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<Uri> uriArrayList;
    private Context context;

    public RecyclerAdapter(ArrayList<Uri> uriArrayList, Context context) {
        this.uriArrayList = uriArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_image_item, parent, false);

        Button removeImage = view.findViewById(R.id.removeImageButton);

        removeImage.setOnClickListener(v -> {
            ImageView imageView = view.findViewById(R.id.image);
            Uri uri = Uri.parse(imageView.getTag().toString());
            int uriPosition = uriArrayList.indexOf(uri);
            uriArrayList.remove(Uri.parse(imageView.getTag().toString()));
            this.notifyItemRemoved(uriPosition);
            System.out.println(getItemCount());
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        Glide
            .with(context)
            .load(uriArrayList.get(position))
            .dontAnimate()
            .into(holder.imageView);
        holder.imageView.setTag(uriArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
