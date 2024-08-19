package com.main.tfm.user;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import APIAccess.UserContent;
import com.main.tfm.R;
import com.squareup.picasso.Picasso;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataViewHolder> {
    private Context context;
    private ArrayList<UserContent> itemList;
    private int currentRV;

    public UserDataAdapter(Context context, ArrayList<UserContent> itemList, int currentRV) {
        this.context = context;
        this.itemList = itemList;
        this.currentRV = currentRV;
    }

    @NonNull
    @Override
    public UserDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(currentRV, parent, false);
        return new UserDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDataViewHolder holder, int position) {
        UserContent item = itemList.get(position);
        Picasso.get().load(item.getPoster()).into(holder.imageView);
        holder.textView.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
