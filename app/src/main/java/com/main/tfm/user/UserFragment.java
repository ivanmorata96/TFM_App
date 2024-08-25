package com.main.tfm.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.tfm.R;
import com.main.tfm.database.UserDB;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import APIAccess.Content;
import APIAccess.UserContent;

public class UserFragment extends Fragment {

    private UserDB db;
    private TextView userMovies, userTVShows, userVideogames, userBooks;
    private RecyclerView ongoingRV, backlogRV, completedRV;
    private boolean isOngoingVisible = false;
    private boolean isBacklogVisible = false;
    private boolean isCompletedVisible = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        userMovies = view.findViewById(R.id.userMoviesView);
        userTVShows = view.findViewById(R.id.userTVShowsView);
        userVideogames = view.findViewById(R.id.userVideogameView);
        userBooks = view.findViewById(R.id.userBooksView);
        ongoingRV = view.findViewById(R.id.ongoingRV);
        backlogRV = view.findViewById(R.id.backlogRV);
        completedRV = view.findViewById(R.id.completedRV);
        db = new UserDB(getContext());
        ArrayList<Content> test = db.retrieveContentList();
        for(Content c : test){
            Log.i("Test", c.toString());
        }

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 3);
        List<UserContent> ongoingContent = db.retrieveContentByStatus("On-going");
        ongoingRV.setLayoutManager(gridLayoutManager1);
        UserDataAdapter adapter1 = new UserDataAdapter(getContext(), categorizeContent(ongoingContent), R.layout.user_item_layout);
        gridLayoutManager1.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // El adaptador determinará si es un header o un item regular
                // Si es un header, ocupará las 3 columnas, si es un item regular ocupará 1 columna
                return adapter1.getItemViewType(position) == UserDataAdapter.VIEW_TYPE_HEADER ? gridLayoutManager1.getSpanCount() : 1;
            }
        });
        ongoingRV.setAdapter(adapter1);

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 3);
        List<UserContent> backlogContent = db.retrieveContentByStatus("Backlog");
        backlogRV.setLayoutManager(gridLayoutManager2);
        UserDataAdapter adapter2 = new UserDataAdapter(getContext(), categorizeContent(backlogContent), R.layout.user_item_layout);
        gridLayoutManager2.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // El adaptador determinará si es un header o un item regular
                // Si es un header, ocupará las 3 columnas, si es un item regular ocupará 1 columna
                return adapter2.getItemViewType(position) == UserDataAdapter.VIEW_TYPE_HEADER ? gridLayoutManager1.getSpanCount() : 1;
            }
        });
        backlogRV.setAdapter(adapter2);

        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getContext(), 3);
        List<UserContent> completedContent = db.retrieveContentByStatus("Completed");
        completedRV.setLayoutManager(gridLayoutManager3);
        UserDataAdapter adapter3 = new UserDataAdapter(getContext(), categorizeContent(completedContent), R.layout.user_item_layout);
        gridLayoutManager3.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // El adaptador determinará si es un header o un item regular
                // Si es un header, ocupará las 3 columnas, si es un item regular ocupará 1 columna
                return adapter3.getItemViewType(position) == UserDataAdapter.VIEW_TYPE_HEADER ? gridLayoutManager1.getSpanCount() : 1;
            }
        });
        completedRV.setAdapter(adapter3);

        view.findViewById(R.id.ongoingDropdown).setOnClickListener(v -> {
            isOngoingVisible = !isOngoingVisible;
            ongoingRV.setVisibility(isOngoingVisible ? View.VISIBLE : View.GONE);
        });
        view.findViewById(R.id.backlogDropdown).setOnClickListener(v -> {
            isBacklogVisible = !isBacklogVisible;
            backlogRV.setVisibility(isBacklogVisible ? View.VISIBLE : View.GONE);
        });
        view.findViewById(R.id.completedDropdown).setOnClickListener(v -> {
            isCompletedVisible = !isCompletedVisible;
            completedRV.setVisibility(isCompletedVisible ? View.VISIBLE : View.GONE);
        });

        return view;
    }

    private Map<String, List<UserContent>> categorizeContent(List<UserContent> contentList) {
        Map<String, List<UserContent>> categorizedContent = new LinkedHashMap<>();
        for (UserContent content : contentList) {
            String type = content.getType();
            if (!categorizedContent.containsKey(type)) {
                categorizedContent.put(type, new ArrayList<>());
            }
            categorizedContent.get(type).add(content);
        }
        return categorizedContent;
    }
}
