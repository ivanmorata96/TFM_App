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

import com.main.tfm.APIAccess.UserContent;
import com.main.tfm.APIAccess.Videogames.RAWGInterface;
import com.main.tfm.APIAccess.Videogames.Videogame;

public class VideogameActivity extends AppCompatActivity {

    TextView titleView, overviewView, releaseDateView, developersView, genresView, platformView, scoreView, isGameAddedView;
    ImageView posterView;
    AppCompatButton addGameButton;
    UserContent thisContent;
    final UserDB db = new UserDB(this);
    boolean confirmDelete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String vgID = intent.getStringExtra("id");
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
        isGameAddedView = findViewById(R.id.isGameAddedView);
        addGameButton = findViewById(R.id.addGameButton);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                vg.set(RAWGInterface.getVideogameDetails(vgID));
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            handler.post(() -> {
                titleView.setText(vg.get().getTitle());
                releaseDateView.setText(vg.get().getRelease_date());
                developersView.setText(vg.get().getDevelopers());
                overviewView.setText(vg.get().getOverview());
                genresView.setText(vg.get().getGenres());
                platformView.setText(vg.get().getPlatforms());
                scoreView.setText(String.valueOf(vg.get().getScore()));
                Picasso.get().load(vg.get().getPoster()).into(posterView);
            });
        });
        thisContent = db.checkContent(vgID);
        if(thisContent != null){
            isGameAddedView.setText("This game is currently on your profile marked as: " + thisContent.getStatus());
            addGameButton.setText("Edit this Game");
            addGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddDialog(thisContent, 1);
                }
            });
        }else{
            isGameAddedView.setText("You can add this game to your profile.");
            addGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddDialog(thisContent, 2);
                }
            });
        }
    }

    private void showAddDialog(UserContent vg, int typeOfDialog){
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
                        db.deleteContentItem(vg.getId());
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
        if(typeOfDialog == 1){
            scoreInput.setText(vg.getScore());
            reviewInput.setText(vg.getReview());
        }
        new AlertDialog.Builder(this)
                .setTitle("Select how to add this content to your profile.")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    String category = categoryInput.getSelectedItem().toString();
                    int userScore = Integer.parseInt(scoreInput.getText().toString());
                    String userReview = reviewInput.getText().toString();
                    thisContent = new UserContent(vg.getId(), vg.getTitle(), vg.getOverview(), vg.getPoster(), "videogame", userScore, userReview, category);
                    if(typeOfDialog == 1)
                        db.editContent(thisContent);
                    else db.addContent(thisContent);
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}