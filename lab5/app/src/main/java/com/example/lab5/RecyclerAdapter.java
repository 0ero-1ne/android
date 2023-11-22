package com.example.lab5;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<Bitmap> bitmapArrayList;
    private ArrayList<Uri> uriArrayList;
    private final Context context;
    private final Boolean isRemoveVisible;

    public RecyclerAdapter(ArrayList<Bitmap> bitmapArrayList, ArrayList<Uri> uri, Context context, Boolean isVisible) {
        this.bitmapArrayList = bitmapArrayList;
        this.context = context;
        this.uriArrayList = uri;
        this.isRemoveVisible = isVisible;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_image_item, parent, false);

        Button removeImage = view.findViewById(R.id.removeImageButton);
        if (!isRemoveVisible)
        {
            removeImage.setVisibility(View.INVISIBLE);
        } else {
            removeImage.setOnClickListener(v -> {
                ImageView imageView = view.findViewById(R.id.image);
                Uri uri = Uri.parse(imageView.getTag().toString());
                int uriPosition = uriArrayList.indexOf(uri);
                uriArrayList.remove(Uri.parse(imageView.getTag().toString()));
                bitmapArrayList.remove(uriPosition);
                this.notifyItemRemoved(uriPosition);
            });
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        //holder.imageView.setImageBitmap(bitmapArrayList.get(position));
        holder.imageView.setImageBitmap(bitmapArrayList.get(position));
        holder.imageView.setTag(uriArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmapArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
