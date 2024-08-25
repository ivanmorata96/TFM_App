package com.main.tfm.user;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.tfm.R;

public class UserHeaderViewHolder extends RecyclerView.ViewHolder {
    private TextView headerTitle;

    public UserHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        headerTitle = itemView.findViewById(R.id.header_text);
    }

    void bind(String title) {
        headerTitle.setText(title);
    }
}
