package com.main.tfm.searches;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.main.tfm.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView titleTextView;
    TextView descriptionTextView;

    public ItemViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.itemPoster);
        titleTextView = itemView.findViewById(R.id.itemTitle);
        descriptionTextView = itemView.findViewById(R.id.itemDescription);
    }
}
