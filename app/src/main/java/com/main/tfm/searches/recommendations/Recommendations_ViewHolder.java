package com.main.tfm.searches.recommendations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.tfm.R;

public class Recommendations_ViewHolder extends RecyclerView.ViewHolder {
    ImageView imageCover;
    TextView textTitle, textDescription;

    public Recommendations_ViewHolder(View itemView) {
        super(itemView);
        imageCover = itemView.findViewById(R.id.imageCover);
        textTitle = itemView.findViewById(R.id.textTitle);
        textDescription = itemView.findViewById(R.id.textDescription);
    }
}