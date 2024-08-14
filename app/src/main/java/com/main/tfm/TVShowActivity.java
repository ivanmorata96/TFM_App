package com.main.tfm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import APIAccess.Movies_TVShows.TMDBInterface;
import APIAccess.Movies_TVShows.TVShow;

public class TVShowActivity extends AppCompatActivity {

    TextView titleView, releaseDateView, castView, overviewView, genresView, studioView, seasonsView, episodesView, statusView, scoreView;
    ImageView posterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AtomicReference<TVShow> tv = new AtomicReference<>(new TVShow());

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tvshow);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        titleView = findViewById(R.id.titleView);
        releaseDateView = findViewById(R.id.releaseDateView);
        castView = findViewById(R.id.castView);
        overviewView = findViewById(R.id.overviewView);
        genresView = findViewById(R.id.genresView);
        studioView = findViewById(R.id.studioView);
        seasonsView = findViewById(R.id.seasonsView);
        episodesView = findViewById(R.id.episodesView);
        statusView = findViewById(R.id.statusView);
        scoreView = findViewById(R.id.statusView);
        posterView = findViewById(R.id.posterView);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                tv.set(TMDBInterface.getTVShowDetails("1668"));
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            handler.post(() -> {
                titleView.setText(tv.get().getName());
                releaseDateView.setText(tv.get().getRelease_date());
                castView.setText(tv.get().getCast());
                overviewView.setText(tv.get().getOverview());
                genresView.setText(tv.get().getGenres());
                studioView.setText(tv.get().getStudios());
                seasonsView.setText(tv.get().getNumber_of_seasons());
                episodesView.setText(tv.get().getNumber_of_episodes());
                statusView.setText(tv.get().getStatus());
                scoreView.setText(String.valueOf(tv.get().getScore()));
                Picasso.get().load(tv.get().getPoster()).into(posterView);
            });
        });
    }
}