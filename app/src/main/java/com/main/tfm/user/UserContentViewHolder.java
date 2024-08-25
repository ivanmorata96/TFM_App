package com.main.tfm.user;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.main.tfm.R;
import com.main.tfm.details.BooksActivity;
import com.main.tfm.details.MovieActivity;
import com.main.tfm.details.TVShowActivity;
import com.main.tfm.details.VideogameActivity;
import com.squareup.picasso.Picasso;

import APIAccess.UserContent;

public class UserContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView imageView;
    TextView textView;
    UserContent item;

    public UserContentViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.item_image);
        textView = itemView.findViewById(R.id.item_text);

        textView.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    void bind(UserContent content) {
        textView.setText(content.getTitle());
        Picasso.get().load(content.getPoster()).into(imageView);
        item = new UserContent(content);
    }

    public void onClick(View v){
        Intent intent = new Intent();
        switch (item.getType()){
            case "movie":
                intent = new Intent(itemView.getContext(), MovieActivity.class);
                break;
            case "tvshow":
                intent = new Intent(itemView.getContext(), TVShowActivity.class);
                break;
            case "videogame":
                intent = new Intent(itemView.getContext(), VideogameActivity.class);
                break;
            case "book":
                intent = new Intent(itemView.getContext(), BooksActivity.class);
                break;
        }
        intent.putExtra("id", item.getId());
        itemView.getContext().startActivity(intent);
    }
}
