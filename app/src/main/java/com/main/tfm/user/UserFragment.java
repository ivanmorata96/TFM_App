package com.main.tfm.user;

import android.os.Bundle;
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

        ongoingRV.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3 items por línea
        UserDataAdapter adapter1 = new UserDataAdapter(getContext(), db.retrieveContentByStatus("ongoing"), R.id.ongoingRV);
        ongoingRV.setAdapter(adapter1);

        backlogRV.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3 items por línea
        UserDataAdapter adapter2 = new UserDataAdapter(getContext(), db.retrieveContentByStatus("backlog"), R.id.backlogRV);
        backlogRV.setAdapter(adapter2);

        completedRV.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3 items por línea
        UserDataAdapter adapter3 = new UserDataAdapter(getContext(), db.retrieveContentByStatus("completed"), R.id.completedRV);
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
}