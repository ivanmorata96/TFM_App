package com.main.tfm.searches;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.main.tfm.R;
import com.main.tfm.mediaAPIs.Books.Book;
import com.main.tfm.mediaAPIs.Books.GoogleBooksInterface;
import com.main.tfm.mediaAPIs.Movies_TVShows.Movie;
import com.main.tfm.mediaAPIs.Movies_TVShows.TMDBInterface;
import com.main.tfm.mediaAPIs.Movies_TVShows.TVShow;
import com.main.tfm.mediaAPIs.Videogames.RAWGInterface;
import com.main.tfm.mediaAPIs.Videogames.Videogame;
import com.main.tfm.searches.recommendations.Recommendations_Activity;
import com.main.tfm.support.UserContent;
import com.main.tfm.support.database.UserDB;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchFragment extends Fragment {
    AppCompatButton searchButton;
    EditText searchQuery;
    TextView recsTextView, rec1TextView, rec2TextView, rec3TextView, rec4TextView, recsLinkTextView;
    ImageView rec1ImageView, rec2ImageView, rec3ImageView, rec4ImageView;
    Spinner spinner;
    UserDB db;


    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        linkObjectToView(view);
        db = new UserDB(getContext());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.searchOptions,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchResults.class);
                intent.putExtra("item", searchQuery.getText().toString());
                intent.putExtra("type", spinner.getSelectedItemPosition());
                startActivity(intent);
            }
        });
        try {
            loadRecommendations();
        } catch (JSONException | IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        recsLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Recommendations_Activity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void linkObjectToView(View view){
        searchButton = view.findViewById(R.id.searchButton);
        searchQuery = view.findViewById(R.id.searchET);
        spinner = view.findViewById(R.id.searchOptions);
        recsTextView = view.findViewById(R.id.recsTextView);
        rec1TextView = view.findViewById(R.id.recTextView1);
        rec2TextView = view.findViewById(R.id.recTextView2);
        rec3TextView = view.findViewById(R.id.recTextView3);
        rec4TextView = view.findViewById(R.id.recTextView4);
        recsLinkTextView = view.findViewById(R.id.recsLinkTextView);
        rec1ImageView = view.findViewById(R.id.recPoster1);
        rec2ImageView = view.findViewById(R.id.recPoster2);
        rec3ImageView = view.findViewById(R.id.recPoster3);
        rec4ImageView = view.findViewById(R.id.recPoster4);
    }

    private void loadRecommendations() throws JSONException, IOException, ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        String recommendID = db.retrieveRatedIDForTags();
        UserContent referencedMedia = new UserContent(db.retrieveTagsReference(recommendID));

        Future<Movie> movieFuture = executor.submit(() -> {
            String movieTags = referencedMedia.getTags().getTagsAsString("movie");
            return new Movie(TMDBInterface.getSingleMovieByTags(movieTags));
        });
        Future<TVShow> tvShowFuture = executor.submit(() -> {
            String tvShowTags = referencedMedia.getTags().getTagsAsString("tvshow");
            return new TVShow(TMDBInterface.getSingleTVShowByTags(tvShowTags));
        });
        Future<Videogame> videogameFuture = executor.submit(() -> {
            String videogameTags = referencedMedia.getTags().getTagsAsString("videogame");
            return new Videogame(RAWGInterface.getSingleVideogameByTag(videogameTags));
        });
        Future<Book> bookFuture = executor.submit(() -> {
            String bookTags = referencedMedia.getTags().getTagsAsString("book");
            return new Book(GoogleBooksInterface.getSingleBookByTags(bookTags));
        });

        Movie recommendedMovie = movieFuture.get();
        TVShow recommendedTVShow = tvShowFuture.get();
        Videogame recommendedVideogame = videogameFuture.get();
        Book recommendedBook = bookFuture.get();
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                rec1TextView.setText(recommendedMovie.getTitle());
                Picasso.get().load(recommendedMovie.getPoster()).into(rec1ImageView);
                rec2TextView.setText(recommendedTVShow.getTitle());
                Picasso.get().load(recommendedTVShow.getPoster()).into(rec2ImageView);
                rec3TextView.setText(recommendedVideogame.getTitle());
                Picasso.get().load(recommendedVideogame.getPoster()).into(rec3ImageView);
                rec4TextView.setText(recommendedBook.getTitle());
                Picasso.get().load(recommendedBook.getPoster()).into(rec4ImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }
}