package com.main.tfm.searches;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.tfm.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.main.tfm.APIAccess.Books.GoogleBooksInterface;
import com.main.tfm.APIAccess.Content;
import com.main.tfm.APIAccess.Movies_TVShows.TMDBInterface;
import com.main.tfm.APIAccess.Videogames.RAWGInterface;

public class SearchResults extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Content> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycler_view);
        Intent intent = getIntent();
        String searchQuery = intent.getStringExtra("item");
        int searchType = intent.getIntExtra("type", -1);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            switch (searchType){
                case 0: //Movies
                    try {
                        results = TMDBInterface.searchMovie(searchQuery);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 1: //TV Shows
                    try {
                        results = TMDBInterface.searchTVShow(searchQuery);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2: //Videogames
                    try {
                        results = RAWGInterface.searchVideogame(searchQuery);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3: //Books
                    try {
                        results = GoogleBooksInterface.searchBooks(searchQuery);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    //TODO
            }
            handler.post(() -> {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new ItemAdapter(results, searchType, this));
            });
        });
    }
}