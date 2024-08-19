package com.main.tfm.user;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.main.tfm.R;

public class UserDataViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView textView;

    public UserDataViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.item_image);
        textView = itemView.findViewById(R.id.item_text);
    }
}
