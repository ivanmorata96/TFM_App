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
import APIAccess.Videogames.RAWGInterface;
import APIAccess.Videogames.Videogame;

public class VideogameActivity extends AppCompatActivity {

    TextView titleView, overviewView, releaseDateView, developersView, genresView, platformView, scoreView;
    ImageView posterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AtomicReference<Videogame> vg = new AtomicReference<>(new Videogame());
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_videogame);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        titleView = findViewById(R.id.titleView);
        releaseDateView = findViewById(R.id.vgReleaseView);
        developersView = findViewById(R.id.developersView);
        overviewView = findViewById(R.id.videogameOverview);
        genresView = findViewById(R.id.vgGenresView);
        platformView = findViewById(R.id.platformsView);
        scoreView = findViewById(R.id.vgScoreView);
        posterView = findViewById(R.id.posterView);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                vg.set(RAWGInterface.getVideogameDetails("3328"));
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            handler.post(() -> {
                titleView.setText(vg.get().getName());
                releaseDateView.setText(vg.get().getRelease_date());
                developersView.setText(vg.get().getDevelopers());
                overviewView.setText(vg.get().getOverview());
                genresView.setText(vg.get().getGenres());
                platformView.setText(vg.get().getPlatforms());
                scoreView.setText(String.valueOf(vg.get().getScore()));
                Picasso.get().load(vg.get().getPoster()).into(posterView);
            });
        });

    }
}