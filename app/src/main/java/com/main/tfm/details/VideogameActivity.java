package com.main.tfm.details;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.util.Log;
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
import com.main.tfm.support.ContentTag;
import com.main.tfm.support.database.UserDB;
import com.main.tfm.support.ScoreInputFilter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import com.main.tfm.support.UserContent;
import com.main.tfm.mediaAPIs.Videogames.RAWGInterface;
import com.main.tfm.mediaAPIs.Videogames.Videogame;

public class VideogameActivity extends AppCompatActivity {

    ConstraintLayout infoLayout, reviewLayout;
    TextView titleView, overviewView, releaseDateView, developersView, genresView, platformView, scoreView, isGameAddedView, toggleInfoHeader, toggleReviewHeader, userCurrentState, userCurrentScore, userCurrentReview;;
    ImageView posterView;
    AppCompatButton addGameButton;
    UserContent thisContent = new UserContent();
    ContentTag thisContentTags = new ContentTag();
    final UserDB db = new UserDB(this);
    boolean confirmDelete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String vgID = intent.getStringExtra("id");
        Videogame vg;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_videogame);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        linkObjectToView();
        thisContent = db.checkContent(vgID);
        try {
            vg = setMediaInfo(vgID);
            thisContentTags = initContentTag(vgID, vg);
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
            showIfOnDB(vgID, vg);
        }else{
            showIfNotOnDB(vg);
        }
    }

    private void linkObjectToView(){
        infoLayout = findViewById(R.id.infoLayout);
        reviewLayout = findViewById(R.id.reviewLayout);
        titleView = findViewById(R.id.titleView);
        releaseDateView = findViewById(R.id.vgReleaseView);
        developersView = findViewById(R.id.developersView);
        overviewView = findViewById(R.id.videogameOverview);
        genresView = findViewById(R.id.vgGenresView);
        platformView = findViewById(R.id.platformsView);
        scoreView = findViewById(R.id.vgScoreView);
        posterView = findViewById(R.id.posterView);
        isGameAddedView = findViewById(R.id.isGameAddedView);
        addGameButton = findViewById(R.id.addGameButton);
        toggleInfoHeader = findViewById(R.id.toggleInfoHeaderView);
        toggleReviewHeader = findViewById(R.id.toggleReviewHeaderView);
        userCurrentState = findViewById(R.id.userCurrentStateView);
        userCurrentScore = findViewById(R.id.userCurrentScoreView);
        userCurrentReview = findViewById(R.id.userCurrentReviewView);
    }
    private Videogame setMediaInfo(String vgID) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Videogame> future = executor.submit(() -> {
            try {
                return RAWGInterface.getVideogameDetails(vgID);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        });
        Videogame videogame = future.get();
        new Handler(Looper.getMainLooper()).post(() -> {
            titleView.setText(videogame.getTitle());
            releaseDateView.setText(videogame.getRelease_date());
            developersView.setText(videogame.getDevelopers());
            overviewView.setText(videogame.getOverview());
            genresView.setText(videogame.getGenres());
            platformView.setText(videogame.getPlatforms());
            scoreView.setText(String.valueOf(videogame.getScore()));
            Picasso.get().load(videogame.getPoster()).into(posterView);
        });
        return videogame;
    }

    private ContentTag initContentTag(String id, Videogame vg) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ContentTag> future = executor.submit(() -> {
            try {
                ContentTag contentTag = new ContentTag(id, "videogame");
                contentTag.setGenres(vg.getGenresArray());
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
    private void showIfOnDB(String vgID, Videogame vg){
        isGameAddedView.setText("This game is currently on your profile marked as: " + thisContent.getStatus());
        addGameButton.setText("Edit this Game");
        userCurrentScore.setText(Integer.toString(thisContent.getScore()));
        userCurrentReview.setText(thisContent.getReview());
        addGameButton.setOnClickListener(new View.OnClickListener() {
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
    private void showIfNotOnDB(Videogame vg){
        isGameAddedView.setText("You can add this game to your profile.");
        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog(vg);
            }
        });
        if(toggleReviewHeader.getVisibility() == View.VISIBLE)
            toggleReviewHeader.setVisibility(View.GONE);
    }
    private void showAddDialog(Videogame vg) {
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
                    thisContent = new UserContent(vg.getId(), vg.getTitle(), vg.getOverview(), vg.getPoster(), "videogame", userScore, userReview, category, thisContentTags);
                    db.addContent(thisContent);
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void showEditDialog(UserContent vg) {
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
                    db.deleteContentItem(vg.getId());
                    recreate();
                }
            }
        });
        Spinner categoryInput = dialogView.findViewById(R.id.category);
        EditText scoreInput = dialogView.findViewById(R.id.userScore);
        EditText reviewInput = dialogView.findViewById(R.id.userReview);
        scoreInput.setFilters(new InputFilter[]{new ScoreInputFilter("0", "10")});
        scoreInput.setText(Integer.toString(vg.getScore()));
        reviewInput.setText(vg.getReview());
        new AlertDialog.Builder(this)
                .setTitle("Select how to add this content to your profile.")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    String category = categoryInput.getSelectedItem().toString();
                    int userScore = Integer.parseInt(scoreInput.getText().toString());
                    String userReview = reviewInput.getText().toString();
                    thisContent = new UserContent(vg.getId(), vg.getTitle(), vg.getOverview(), vg.getPoster(), "videogame", userScore, userReview, category);
                    db.editContent(thisContent);
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}