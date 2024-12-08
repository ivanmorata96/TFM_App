package com.main.tfm.searches.recommendations;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.main.tfm.R;
import com.main.tfm.mediaAPIs.Books.GoogleBooksInterface;
import com.main.tfm.mediaAPIs.Movies_TVShows.TMDBInterface;
import com.main.tfm.mediaAPIs.Videogames.RAWGInterface;
import com.main.tfm.support.Content;
import com.main.tfm.support.database.UserDB;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Recommendations_Activity extends AppCompatActivity {

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private Recommendations_Adapter adapter;
    private String filter;
    private UserDB db = new UserDB(this);
    private ArrayList<Content> movies = new ArrayList<>();
    private ArrayList<Content> tvshows = new ArrayList<>();
    private ArrayList<Content> videogames = new ArrayList<>();
    private ArrayList<Content> books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recommendations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        try {
            initContentLists();
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        tabLayout = findViewById(R.id.tabLayout);
        recyclerView = findViewById(R.id.recyclerView);
        filter = "Movies";
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Recommendations_Adapter(movies);
        recyclerView.setAdapter(adapter);
        String[] tabs = {"Movies", "TV Shows", "Videogames", "Books"};
        for (String tab : tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filter = tab.getText().toString();
                updateResults(filter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void updateResults(String filter) {
        ArrayList<Content> activeResults = new ArrayList<>();
        switch (filter) {
            case "Movies":
                activeResults = movies;
                break;
            case "TV Shows":
                activeResults = tvshows;
                break;
            case "Videogames":
                activeResults = videogames;
                break;
            case "Books":
                activeResults = books;
                break;
        }
        adapter.updateData(activeResults);
    }

    private void initContentLists() throws IOException, JSONException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try {
            HashMap<String, String> tags = db.retrieveUserTags();
            Future<ArrayList<Content>> moviesFuture = executorService.submit(() -> TMDBInterface.searchMoviesByTags(tags));
            Future<ArrayList<Content>> tvShowsFuture = executorService.submit(() -> TMDBInterface.searchTVShowsByTags(tags));
            Future<ArrayList<Content>> videogamesFuture = executorService.submit(() -> RAWGInterface.searchVideogamesByTags(tags));
            Future<ArrayList<Content>> booksFuture = executorService.submit(() -> GoogleBooksInterface.searchBooksByTag(tags));
            movies = moviesFuture.get();
            tvshows = tvShowsFuture.get();
            videogames = videogamesFuture.get();
            books = booksFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}