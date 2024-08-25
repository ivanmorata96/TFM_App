package com.main.tfm.searches;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.tfm.details.BooksActivity;
import com.main.tfm.details.MovieActivity;
import com.main.tfm.details.TVShowActivity;
import com.main.tfm.details.VideogameActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import APIAccess.Content;
import com.main.tfm.R;


public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private ArrayList<Content> itemList;
    private int contentType;
    private Context context;

    public ItemAdapter(ArrayList<Content> itemList, int contentType, Context context) {
        this.itemList = itemList;
        this.contentType = contentType;
        this.context = context;
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
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (contentType) {
                    case 0:
                        intent = new Intent(context, MovieActivity.class);
                        break;
                    case 1:
                        intent = new Intent(context, TVShowActivity.class);
                        break;
                    case 2:
                        intent = new Intent(context, VideogameActivity.class);
                        break;
                    case 3:
                        intent = new Intent(context, BooksActivity.class);
                        break;
                }
            }
        });
        holder.titleTextView.setText(item.getTitle());
        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                switch (contentType){
                    case 0:
                        intent = new Intent(context, MovieActivity.class);
                        break;
                    case 1:
                        intent = new Intent(context, TVShowActivity.class);
                        break;
                    case 2:
                        intent = new Intent(context, VideogameActivity.class);
                        break;
                    case 3:
                        intent = new Intent(context, BooksActivity.class);
                        break;
                }
                intent.putExtra("id", item.getId());
                context.startActivity(intent);
            }
        });
        holder.descriptionTextView.setText(item.getShortOverview());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
