package com.main.tfm;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.io.IOException;

import APIAccess.Movies_TVShows.Movie;
import APIAccess.Movies_TVShows.TMDBInterface;

public class MovieActivity extends AppCompatActivity {

    TextView titleView, releaseDateView, creditsView, overviewView, genresView, studioView, runtimeView, scoreView;
    ImageView posterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            Movie m = TMDBInterface.getMovieDetails("680");
            titleView = findViewById(R.id.titleView);
            releaseDateView = findViewById(R.id.releaseDateView);
            creditsView = findViewById(R.id.creditsView);
            overviewView = findViewById(R.id.overviewView);
            genresView = findViewById(R.id.genresView);
            studioView = findViewById(R.id.studioView);
            runtimeView = findViewById(R.id.runtimeView);
            scoreView = findViewById(R.id.scoreView);
            posterView = findViewById(R.id.posterView);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}