package com.main.tfm.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.main.tfm.APIAccess.UserContent;

import com.main.tfm.R;

public class UserDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object> items;
    private int layout;
    protected static final int VIEW_TYPE_HEADER = 0;
    protected static final int VIEW_TYPE_ITEM = 1;

    public UserDataAdapter(Context context, Map<String, List<UserContent>> categorizedContent, int layout) {
        this.context = context;
        this.items = new ArrayList<>();
        for (Map.Entry<String, List<UserContent>> entry : categorizedContent.entrySet()) {
            items.add(entry.getKey());
            items.addAll(entry.getValue());
        }
        this.layout = layout;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.user_item_header_layout, parent, false);
            return new UserHeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(layout, parent, false);
            return new UserContentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserHeaderViewHolder) {
            String title = (String) items.get(position);
            ((UserHeaderViewHolder) holder).bind(title);
        } else if (holder instanceof UserContentViewHolder) {
            UserContent content = (UserContent) items.get(position);
            ((UserContentViewHolder) holder).bind(content);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private boolean isHeader(int position) {
        return items.get(position) instanceof String;
    }

    private String getCategoryTitle(int position) {
        return (String) items.get(position);
    }

    private UserContent getContent(int position) {
        return (UserContent) items.get(position);
    }

    private int calculateTotalItems() {
        return items.size();
    }

}
