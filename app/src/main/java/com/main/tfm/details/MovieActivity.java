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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.main.tfm.R;
import com.main.tfm.mediaAPIs.Videogames.RAWGInterface;
import com.main.tfm.mediaAPIs.Videogames.Videogame;
import com.main.tfm.support.ContentTag;
import com.main.tfm.support.database.UserDB;
import com.main.tfm.support.ScoreInputFilter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import com.main.tfm.mediaAPIs.Movies_TVShows.Movie;
import com.main.tfm.mediaAPIs.Movies_TVShows.TMDBInterface;
import com.main.tfm.support.UserContent;

public class MovieActivity extends AppCompatActivity {

    ConstraintLayout infoLayout, reviewLayout;
    TextView titleView, releaseDateView, creditsView, overviewView, genresView, studioView, runtimeView, scoreView, isMovieAddedView, toggleInfoHeader, toggleReviewHeader, userCurrentState, userCurrentScore, userCurrentReview;
    ImageView posterView;
    AppCompatButton addMovieButton;
    UserContent thisContent = new UserContent();
    ContentTag thisContentTags = new ContentTag();
    final UserDB db = new UserDB(this);
    boolean confirmDelete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String movieID = intent.getStringExtra("id");
        Movie m;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        linkObjectToView();
        thisContent = db.checkContent(movieID);
        try {
            m = setMediaInfo(movieID);
            thisContentTags = initContentTag(movieID, m);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        toggleInfoHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(infoLayout.getVisibility() == View.GONE) {
                    infoLayout.setVisibility(View.VISIBLE);
                    reviewLayout.setVisibility(View.GONE);
                }
                else infoLayout.setVisibility(View.GONE);
            }
        });

        if(thisContent != null){
            showIfOnDB(movieID, m);
        }else{
            showIfNotOnDB(m);
        }
    }

    private void linkObjectToView(){
        infoLayout = findViewById(R.id.infoLayout);
        reviewLayout = findViewById(R.id.reviewLayout);
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
        toggleInfoHeader = findViewById(R.id.toggleInfoHeaderView);
        toggleReviewHeader = findViewById(R.id.toggleReviewHeaderView);
        userCurrentState = findViewById(R.id.userCurrentStateView);
        userCurrentScore = findViewById(R.id.userCurrentScoreView);
        userCurrentReview = findViewById(R.id.userCurrentReviewView);
    }

    private Movie setMediaInfo(String movieID) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Movie> future = executor.submit(() -> {
            try {
                return TMDBInterface.getMovieDetails(movieID);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        });
        Movie movie = future.get();
        new Handler(Looper.getMainLooper()).post(() -> {
            titleView.setText(movie.getTitle());
            releaseDateView.setText(movie.getRelease_date());
            creditsView.setText(movie.getMovieCredits());
            overviewView.setText(movie.getOverview());
            genresView.setText(movie.getGenres());
            studioView.setText(movie.getStudios());
            runtimeView.setText(String.valueOf(movie.getRuntime()));
            scoreView.setText(String.valueOf(movie.getScore()));
            Picasso.get().load(movie.getPoster()).into(posterView);
        });

        return movie;
    }

    private ContentTag initContentTag(String id, Movie m) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ContentTag> future = executor.submit(() -> {
            try {
                ContentTag contentTag = new ContentTag(id, "movie");
                contentTag.setGenres(m.getGenresArray());
                return contentTag;
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        });
        ContentTag contentTag = future.get();
        new Handler(Looper.getMainLooper()).post(() -> {
            if (thisContent != null) {
                thisContent.setTags(contentTag);
            }
        });
        return contentTag;
    }

    private void showIfOnDB(String movieID, Movie m){
        isMovieAddedView.setText("This movie is currently on your profile marked as: " + thisContent.getStatus());
        addMovieButton.setText("Edit this Movie");
        userCurrentScore.setText(Integer.toString(thisContent.getScore()));
        userCurrentReview.setText(thisContent.getReview());
        addMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(thisContent);
            }
        });
        if(toggleReviewHeader.getVisibility() == View.GONE)
            toggleReviewHeader.setVisibility(View.VISIBLE);
        toggleReviewHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reviewLayout.getVisibility() == View.GONE) {
                    reviewLayout.setVisibility(View.VISIBLE);
                    infoLayout.setVisibility(View.GONE);
                }
                else reviewLayout.setVisibility(View.GONE);
            }
        });
    }

    private void showIfNotOnDB(Movie m){
        isMovieAddedView.setText("You can add this movie to your profile.");
        addMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog(m);
            }
        });
        if(toggleReviewHeader.getVisibility() == View.VISIBLE)
            toggleReviewHeader.setVisibility(View.GONE);
    }

    private void showAddDialog(Movie m){
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView;
        dialogView = inflater.inflate(R.layout.add_media_dialog_layout, null);
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
                    thisContent = new UserContent(m.getId(), m.getTitle(), m.getOverview(), m.getPoster(), "movie", userScore, userReview, category, thisContentTags);
                    db.addContent(thisContent);
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditDialog(UserContent m) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView;
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
        Spinner categoryInput = dialogView.findViewById(R.id.category);
        EditText scoreInput = dialogView.findViewById(R.id.userScore);
        EditText reviewInput = dialogView.findViewById(R.id.userReview);
        scoreInput.setFilters(new InputFilter[]{new ScoreInputFilter("0", "10")});
        scoreInput.setText(Integer.toString(m.getScore()));
        reviewInput.setText(m.getReview());
        new AlertDialog.Builder(this)
                .setTitle("Select how to add this content to your profile.")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    String category = categoryInput.getSelectedItem().toString();
                    int userScore = Integer.parseInt(scoreInput.getText().toString());
                    String userReview = reviewInput.getText().toString();
                    thisContent = new UserContent(m.getId(), m.getTitle(), m.getOverview(), m.getPoster(), "videogame", userScore, userReview, category);
                    db.editContent(thisContent);
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}