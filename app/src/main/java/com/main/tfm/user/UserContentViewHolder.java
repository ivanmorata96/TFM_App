package com.main.tfm.user;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.main.tfm.R;
import com.squareup.picasso.Picasso;

import APIAccess.UserContent;

public class UserContentViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView textView;

    public UserContentViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.item_image);
        textView = itemView.findViewById(R.id.item_text);
    }

    void bind(UserContent content) {
        textView.setText(content.getTitle());
        Picasso.get().load(content.getPoster()).into(imageView);
    }
}
