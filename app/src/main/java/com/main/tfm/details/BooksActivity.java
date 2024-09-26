package com.main.tfm.details;

import static com.main.tfm.APIAccess.Books.GoogleBooksInterface.getBookDetails;

import android.content.Intent;
import android.net.Uri;
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

import com.main.tfm.APIAccess.Books.Book;
import com.main.tfm.APIAccess.UserContent;

public class BooksActivity extends AppCompatActivity {

    TextView titleView, overviewView, releaseDateView, authorView, genresView, publisherView, isbnView, pagesView, scoreView, isBookAddedView;
    ImageView posterView;
    AppCompatButton addBookButton;
    UserContent thisContent;
    final UserDB db = new UserDB(this);
    boolean confirmDelete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String bookID = intent.getStringExtra("id");
        AtomicReference<Book> b = new AtomicReference<>(new Book());
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_books);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                b.set(getBookDetails(bookID));
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            handler.post(() -> {
                Uri uri = Uri.parse(b.get().getPoster());
                titleView.setText(b.get().getTitle());
                releaseDateView.setText(b.get().getDate_of_publishing());
                authorView.setText(b.get().getAuthor());
                overviewView.setText(b.get().getOverview());
                genresView.setText(b.get().getGenres());
                publisherView.setText(b.get().getPublisher());
                isbnView.setText(b.get().getISBN());
                pagesView.setText(String.valueOf(b.get().getPages()) + " pages");
                scoreView.setText(String.valueOf(b.get().getScore()));
                Picasso.get().load(uri).into(posterView);
            });
        });
        thisContent = db.checkContent(bookID);
        if(thisContent != null){
            isBookAddedView.setText("This book is currently on your profile marked as: " + thisContent.getStatus());
            addBookButton.setText("Edit this Book");
            addBookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditDialog(thisContent);
                }
            });
        }else{
            isBookAddedView.setText("You can add this book to your profile.");
            addBookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddDialog(b.get());

                }
            });
        }
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