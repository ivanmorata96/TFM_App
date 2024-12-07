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
import com.main.tfm.mediaAPIs.Movies_TVShows.Movie;
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

import com.main.tfm.mediaAPIs.Movies_TVShows.TMDBInterface;
import com.main.tfm.mediaAPIs.Movies_TVShows.TVShow;
import com.main.tfm.support.UserContent;

public class TVShowActivity extends AppCompatActivity {

    ConstraintLayout infoLayout, reviewLayout;
    TextView titleView, releaseDateView, castView, overviewView, genresView, studioView, seasonsView, episodesView, statusView, scoreView, isTVShowAddedView, toggleInfoHeader, toggleReviewHeader, userCurrentState, userCurrentScore, userCurrentReview;;
    ImageView posterView;
    AppCompatButton addTVShowButton;
    UserContent thisContent = new UserContent();
    ContentTag thisContentTags = new ContentTag();
    final UserDB db = new UserDB(this);
    boolean confirmDelete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String tvshowID = intent.getStringExtra("id");
        TVShow tv = new TVShow();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tvshow);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        linkObjectToView();
        thisContent = db.checkContent(tvshowID);
        try {
            tv = setMediaInfo(tvshowID);
            thisContentTags = initContentTag(tvshowID, tv);
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
            showIfOnDB(tvshowID, tv);
        }else{
            showIfNotOnDB(tv);
        }
    }

    private void linkObjectToView(){
        infoLayout = findViewById(R.id.infoLayout);
        reviewLayout = findViewById(R.id.reviewLayout);
        titleView = findViewById(R.id.titleView);
        releaseDateView = findViewById(R.id.releaseDateView);
        castView = findViewById(R.id.castView);
        overviewView = findViewById(R.id.overviewView);
        genresView = findViewById(R.id.genresView);
        studioView = findViewById(R.id.studioView);
        seasonsView = findViewById(R.id.seasonsView);
        episodesView = findViewById(R.id.episodesView);
        statusView = findViewById(R.id.statusView);
        scoreView = findViewById(R.id.TVscoreView);
        posterView = findViewById(R.id.posterView);
        isTVShowAddedView = findViewById(R.id.isTVShowAddedView);
        addTVShowButton = findViewById(R.id.addTVShowButton);
        toggleInfoHeader = findViewById(R.id.toggleInfoHeaderView);
        toggleReviewHeader = findViewById(R.id.toggleReviewHeaderView);
        userCurrentState = findViewById(R.id.userCurrentStateView);
        userCurrentScore = findViewById(R.id.userCurrentScoreView);
        userCurrentReview = findViewById(R.id.userCurrentReviewView);
    }

    private TVShow setMediaInfo(String tvShowID) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<TVShow> future = executor.submit(() -> {
            try {
                return TMDBInterface.getTVShowDetails(tvShowID);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        });
        TVShow tv = future.get();
        new Handler(Looper.getMainLooper()).post(() -> {
            titleView.setText(tv.getTitle());
            releaseDateView.setText(tv.getRelease_date());
            castView.setText(tv.getCast());
            overviewView.setText(tv.getOverview());
            genresView.setText(tv.getGenres());
            studioView.setText(tv.getStudios());
            seasonsView.setText(tv.getNumber_of_seasons());
            episodesView.setText(tv.getNumber_of_episodes());
            statusView.setText(tv.getStatus());
            scoreView.setText(String.valueOf(tv.getScore()));
            Picasso.get().load(tv.getPoster()).into(posterView);
        });
        return tv;
    }

    private ContentTag initContentTag(String tvShowID, TVShow tv) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ContentTag> future = executor.submit(() -> {
            try {
                ContentTag contentTag = new ContentTag(tvShowID, "tvshow");
                contentTag.setGenres(tv.getGenresArray());
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

    private void showIfOnDB(String tvShowID, TVShow tv){
        isTVShowAddedView.setText("This show is currently on your profile marked as: " + thisContent.getStatus());
        addTVShowButton.setText("Edit this Show");
        userCurrentScore.setText(Integer.toString(thisContent.getScore()));
        userCurrentReview.setText(thisContent.getReview());
        addTVShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(thisContent);
            }
        });
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

    private void showIfNotOnDB(TVShow tv){
        isTVShowAddedView.setText("You can add this show to your profile.");
        addTVShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog(tv);
            }
        });
        if(toggleReviewHeader.getVisibility() == View.VISIBLE)
            toggleReviewHeader.setVisibility(View.GONE);
    }

    private void showAddDialog(TVShow tv) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView;
        dialogView = inflater.inflate(R.layout.add_media_dialog_layout, null);
        Spinner categoryInput = dialogView.findViewById(R.id.category);
        EditText scoreInput = dialogView.findViewById(R.id.userScore);
        EditText reviewInput = dialogView.findViewById(R.id.userReview);
        scoreInput.setFilters(new InputFilter[]{new ScoreInputFilter("0", "10")});

        new AlertDialog.Builder(this)
                .setTitle("Select how to add this content to your profile.")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    String category = categoryInput.getSelectedItem().toString();
                    int userScore = Integer.parseInt(scoreInput.getText().toString());
                    String userReview = reviewInput.getText().toString();
                    thisContent = new UserContent(tv.getId(), tv.getTitle(), tv.getOverview(), tv.getPoster(), "tvshow", userScore, userReview, category);
                    db.addContent(thisContent);
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditDialog(UserContent tv) {
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
                    db.deleteContentItem(tv.getId());
                    recreate();
                }
            }
        });
        Spinner categoryInput = dialogView.findViewById(R.id.category);
        EditText scoreInput = dialogView.findViewById(R.id.userScore);
        EditText reviewInput = dialogView.findViewById(R.id.userReview);
        scoreInput.setFilters(new InputFilter[]{new ScoreInputFilter("0", "10")});
        scoreInput.setText(Integer.toString(tv.getScore()));
        reviewInput.setText(tv.getReview());
        new AlertDialog.Builder(this)
                .setTitle("Select how to add this content to your profile.")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    String category = categoryInput.getSelectedItem().toString();
                    int userScore = Integer.parseInt(scoreInput.getText().toString());
                    String userReview = reviewInput.getText().toString();
                    thisContent = new UserContent(tv.getId(), tv.getTitle(), tv.getOverview(), tv.getPoster(), "videogame", userScore, userReview, category);
                    db.editContent(thisContent);
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}