package com.main.tfm.searches.recommendations;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import com.main.tfm.support.Content;
import com.main.tfm.R;

public class Recommendations_Adapter extends RecyclerView.Adapter<Recommendations_ViewHolder> {

    private ArrayList<Content> results;

    public Recommendations_Adapter(ArrayList<Content> results) {
        this.results = results;
    }
    @NonNull
    @Override
    public Recommendations_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommendations_results, parent, false);
        return new Recommendations_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Recommendations_ViewHolder holder, int position) {
        Content item = results.get(position);
        holder.textTitle.setText(item.getTitle());
        holder.textDescription.setText(item.getShortOverview());
        Picasso.get().load(item.getPoster()).into(holder.imageCover);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateData(ArrayList<Content> newResults) {
        results = newResults;
        notifyDataSetChanged();
    }
}