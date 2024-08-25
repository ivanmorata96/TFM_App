package com.main.tfm.details;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.main.tfm.R;
import com.main.tfm.database.UserDB;
import com.main.tfm.support.ScoreInputFilter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import com.main.tfm.APIAccess.Movies_TVShows.Movie;
import com.main.tfm.APIAccess.Movies_TVShows.TMDBInterface;
import com.main.tfm.APIAccess.UserContent;

public class MovieActivity extends AppCompatActivity {

    TextView titleView, releaseDateView, creditsView, overviewView, genresView, studioView, runtimeView, scoreView, isMovieAddedView;
    ImageView posterView;
    AppCompatButton addMovieButton;
    UserContent thisContent;
    final UserDB db = new UserDB(this);
    boolean confirmDelete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String movieID = intent.getStringExtra("id");
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
        scoreView = findViewById(R.id.movieScoreView);
        posterView = findViewById(R.id.posterView);
        isMovieAddedView = findViewById(R.id.isMovieAddedView);
        addMovieButton = findViewById(R.id.addMovieButton);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                m.set(TMDBInterface.getMovieDetails(movieID));
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            handler.post(() -> {
                titleView.setText(m.get().getTitle());
                releaseDateView.setText(m.get().getRelease_date());
                creditsView.setText(m.get().getMovieCredits());
                overviewView.setText(m.get().getOverview());
                genresView.setText(m.get().getGenres());
                studioView.setText(m.get().getStudios());
                runtimeView.setText(String.valueOf(m.get().getRuntime()));
                scoreView.setText(String.valueOf(m.get().getScore()));
                Picasso.get().load(m.get().getPoster()).into(posterView);
            });
        });
        thisContent = db.checkContent(movieID);
        if(thisContent != null){
            isMovieAddedView.setText("This movie is currently on your profile marked as: " + thisContent.getStatus());
            addMovieButton.setText("Edit this Movie");
            addMovieButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddDialog(m.get(), 1);
                }
            });
        }else{
            isMovieAddedView.setText("You can add this movie to your profile.");
            addMovieButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddDialog(m.get(), 2);
                }
            });
        }

    }

    private void showAddDialog(Movie m, int typeOfDialog){
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView;
        if(typeOfDialog == 1){
            dialogView = inflater.inflate(R.layout.edit_media_dialog_layout, null);
            AppCompatButton deleteButton = dialogView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (confirmDelete) {
                        deleteButton.setText("Are you sure you want to delete this content from your page?");
                        confirmDelete = false;
                    } else {
                        db.deleteContentItem(m.getId());
                        recreate();
                    }
                }
            });
        }else{
            dialogView = inflater.inflate(R.layout.add_media_dialog_layout, null);
        }
        Spinner categoryInput = dialogView.findViewById(R.id.category);
        EditText scoreInput = dialogView.findViewById(R.id.userScore);
        EditText reviewInput = dialogView.findViewById(R.id.userReview);
        scoreInput.setFilters(new InputFilter[]{ new ScoreInputFilter("0", "10") });

        new AlertDialog.Builder(this)
                .setTitle("Select how to add this content to your profile.")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    String category = categoryInput.getSelectedItem().toString();
                    int userScore = Integer.parseInt(scoreInput.getText().toString());
                    String userReview = reviewInput.getText().toString();
                    thisContent = new UserContent(m.getId(), m.getTitle(), m.getOverview(), m.getPoster(), "movie", userScore, userReview, category);
                    if(typeOfDialog == 1)
                        db.editContent(thisContent);
                    else db.addContent(thisContent);
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}