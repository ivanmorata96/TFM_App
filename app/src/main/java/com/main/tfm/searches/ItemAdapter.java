package com.main.tfm.searches;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import APIAccess.Content;
import com.main.tfm.R;


public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private ArrayList<Content> itemList;

    public ItemAdapter(ArrayList<Content> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Content item = itemList.get(position);
        Picasso.get().load(item.getPoster()).into(holder.imageView);
        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getShortOverview());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
