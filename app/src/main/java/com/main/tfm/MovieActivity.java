package com.main.tfm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import APIAccess.Movies_TVShows.Movie;
import APIAccess.Movies_TVShows.TMDBInterface;

public class MovieActivity extends AppCompatActivity {

    TextView titleView, releaseDateView, creditsView, overviewView, genresView, studioView, runtimeView, scoreView;
    ImageView posterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AtomicReference<Movie> m = new AtomicReference<>(new Movie());

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        titleView = findViewById(R.id.titleView);
        releaseDateView = findViewById(R.id.releaseDateView);
        creditsView = findViewById(R.id.creditsView);
        overviewView = findViewById(R.id.overviewView);
        genresView = findViewById(R.id.genresView);
        studioView = findViewById(R.id.studioView);
        runtimeView = findViewById(R.id.runtimeView);
        scoreView = findViewById(R.id.scoreView);
        posterView = findViewById(R.id.posterView);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                m.set(TMDBInterface.getMovieDetails("680"));
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            handler.post(() -> {
                titleView.setText(m.get().getTitle());
                releaseDateView.setText(m.get().getRelease_date());
                creditsView.setText(m.get().getMovieCredits().toString());
                overviewView.setText(m.get().getOverview());
                genresView.setText(m.get().getGenres().toString());
                studioView.setText(m.get().getStudios().toString());
                runtimeView.setText(String.valueOf(m.get().getRuntime()));
                scoreView.setText(String.valueOf(m.get().getScore()));
                Picasso.get().load(m.get().getPoster()).into(posterView);
            });
        });
    }
}