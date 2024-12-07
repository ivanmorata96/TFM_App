package com.main.tfm.details;

import static com.main.tfm.mediaAPIs.Books.GoogleBooksInterface.getBookDetails;

import android.content.Intent;
import android.net.Uri;
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
import com.main.tfm.mediaAPIs.Books.GoogleBooksInterface;
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

import com.main.tfm.mediaAPIs.Books.Book;
import com.main.tfm.support.UserContent;

public class BooksActivity extends AppCompatActivity {

    ConstraintLayout infoLayout, reviewLayout;
    TextView titleView, overviewView, releaseDateView, authorView, genresView, publisherView, isbnView, pagesView, scoreView, isBookAddedView, toggleInfoHeader, toggleReviewHeader, userCurrentState, userCurrentScore, userCurrentReview;;
    ImageView posterView;
    AppCompatButton addBookButton;
    UserContent thisContent = new UserContent();
    ContentTag thisContentTags = new ContentTag();
    final UserDB db = new UserDB(this);
    boolean confirmDelete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String bookID = intent.getStringExtra("id");
        Book b;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_books);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        linkObjectToView();
        thisContent = db.checkContent(bookID);
        try {
            b = setMediaInfo(bookID);
            thisContentTags = initContentTag(bookID, b);
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
            showIfOnDB(bookID, b);
        }else {
            showIfNotOnDB(b);
        }
    }

    private void linkObjectToView(){
        infoLayout = findViewById(R.id.infoLayout);
        reviewLayout = findViewById(R.id.reviewLayout);
        titleView = findViewById(R.id.titleView);
        releaseDateView = findViewById(R.id.dopView);
        authorView = findViewById(R.id.authorView);
        overviewView = findViewById(R.id.overviewView);
        genresView = findViewById(R.id.bookGenresView);
        publisherView = findViewById(R.id.publisherView);
        isbnView = findViewById(R.id.isbnView);
        pagesView = findViewById(R.id.pagesView);
        scoreView = findViewById(R.id.bookScoreView);
        posterView = findViewById(R.id.posterView);
        isBookAddedView = findViewById(R.id.isBookAddedView);
        addBookButton = findViewById(R.id.addBookButton);
        toggleInfoHeader = findViewById(R.id.toggleInfoHeaderView);
        toggleReviewHeader = findViewById(R.id.toggleReviewHeaderView);
        userCurrentState = findViewById(R.id.userCurrentStateView);
        userCurrentScore = findViewById(R.id.userCurrentScoreView);
        userCurrentReview = findViewById(R.id.userCurrentReviewView);
    }

    private Book setMediaInfo(String bookID) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Book> future = executor.submit(() -> {
            try {
                return GoogleBooksInterface.getBookDetails(bookID);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        });
        Book b = future.get();
        new Handler(Looper.getMainLooper()).post(() -> {
            Uri uri = Uri.parse(b.getPoster());
            titleView.setText(b.getTitle());
            releaseDateView.setText(b.getDate_of_publishing());
            authorView.setText(b.getAuthor());
            overviewView.setText(b.getOverview());
            genresView.setText(b.getGenres());
            publisherView.setText(b.getPublisher());
            isbnView.setText(b.getISBN());
            pagesView.setText(String.valueOf(b.getPages()) + " pages");
            scoreView.setText(String.valueOf(b.getScore()));
            Picasso.get().load(uri).into(posterView);
        });
        return b;
    }

    private ContentTag initContentTag(String bookID, Book b) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ContentTag> future = executor.submit(() -> {
            try {
                ContentTag contentTag = new ContentTag(bookID, "book");
                contentTag.setGenres(b.getGenresArray());
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

    private void showIfOnDB(String bookID, Book b){
        isBookAddedView.setText("This book is currently on your profile marked as: " + thisContent.getStatus());
        addBookButton.setText("Edit this Book");
        userCurrentScore.setText(Integer.toString(thisContent.getScore()));
        userCurrentReview.setText(thisContent.getReview());
        addBookButton.setOnClickListener(new View.OnClickListener() {
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

    private void showIfNotOnDB(Book b){
            isBookAddedView.setText("You can add this book to your profile.");
            addBookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddDialog(b);
                }
            });
            if(toggleReviewHeader.getVisibility() == View.VISIBLE)
                toggleReviewHeader.setVisibility(View.GONE);
    }

    private void showAddDialog(Book b) {
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
                    thisContent = new UserContent(b.getId(), b.getTitle(), b.getOverview(), b.getPoster(), "book", userScore, userReview, category);
                    db.addContent(thisContent);
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditDialog(UserContent b) {
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
                    db.deleteContentItem(b.getId());
                    recreate();
                }
            }
        });
        Spinner categoryInput = dialogView.findViewById(R.id.category);
        EditText scoreInput = dialogView.findViewById(R.id.userScore);
        EditText reviewInput = dialogView.findViewById(R.id.userReview);
        scoreInput.setFilters(new InputFilter[]{new ScoreInputFilter("0", "10")});
        scoreInput.setText(Integer.toString(b.getScore()));
        reviewInput.setText(b.getReview());
        new AlertDialog.Builder(this)
                .setTitle("Select how to add this content to your profile.")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    String category = categoryInput.getSelectedItem().toString();
                    int userScore = Integer.parseInt(scoreInput.getText().toString());
                    String userReview = reviewInput.getText().toString();
                    thisContent = new UserContent(b.getId(), b.getTitle(), b.getOverview(), b.getPoster(), "videogame", userScore, userReview, category);
                    db.editContent(thisContent);
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}