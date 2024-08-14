package com.main.tfm.details;

import static APIAccess.Books.GoogleBooksInterface.*;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.main.tfm.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import APIAccess.Books.Book;

public class BooksActivity extends AppCompatActivity {

    TextView titleView, overviewView, releaseDateView, authorView, genresView, publisherView, isbnView, pagesView, scoreView;
    ImageView posterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                b.set(getBookDetails("2zgRDXFWkm8C"));
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            handler.post(() -> {
                Uri uri = Uri.parse(b.get().getCover());
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
    }
}