package com.example.assignment3.gym;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.R;

import java.util.ArrayList;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.SingleItemRowHolder> {

    private ArrayList<Bitmap> itemsList;


    public ImageListAdapter(ArrayList<Bitmap> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_card, null);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        Bitmap img = itemsList.get(i);

        holder.itemImage.setImageBitmap(img);

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public void addItem(Bitmap image) {
        itemsList.add(image);
//        notifyDataSetChanged();
        notifyItemInserted(itemsList.size()-1);
    }

    public void setItemsList(ArrayList<Bitmap> bmps) {
        itemsList = bmps;
    }

    public ArrayList<Bitmap> getItemsList() {
        return itemsList;
    }

    public void cleanData() {
        itemsList = new ArrayList<>();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);

//            view.setOnClickListener(v -> {
//
//            });
        }
    }
}
