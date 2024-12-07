package com.main.tfm.mediaAPIs.Movies_TVShows;
import com.main.tfm.support.Content;

import java.util.ArrayList;

public class Movie extends Content {

    private String release_date;
    private MovieCredits movieCredits;
    private ArrayList<String> genres;
    private ArrayList<String> studios;
    private int runtime;
    private double score;

    public Movie(){
        super();
        release_date="";
        runtime=-1;
        score =-1;
        movieCredits = new MovieCredits();
        genres = studios = new ArrayList<>();
    }

    public Movie(String id, String title, String release_date, MovieCredits movieCredits, String overview, ArrayList<String> genres, String poster, ArrayList<String> studios, int runtime, double score) {
        super(id, title, overview, poster);
        this.release_date = release_date;
        this.movieCredits = new MovieCredits(movieCredits);
        this.genres = new ArrayList<>(genres);
        this.studios = new ArrayList<>(studios);
        this.runtime = runtime;
        this.score = score;
    }

    public Movie(Movie other){
        super(other);
        this.release_date = other.release_date;
        this.movieCredits = new MovieCredits(other.movieCredits);
        this.genres = new ArrayList<>(other.genres);
        this.studios = new ArrayList<>(other.studios);
        this.runtime = other.runtime;
        this.score = other.score;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getMovieCredits() {
        return movieCredits.toString();
    }

    public void setMovieCredits(MovieCredits movieCredits) {
        this.movieCredits = new MovieCredits(movieCredits);
    }

    public String getGenres() {
        String result="";
        for(String g : genres)
            result = result + g + ", ";
        return result.substring(0, result.length()-2);
    }

    public ArrayList<String> getGenresArray(){
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = new ArrayList<>(genres);
    }


    public void setPoster(String poster) {
        super.setPoster(poster.replace("\"", ""));
    }

    public String getStudios() {
        String result="";
        for(String s : studios)
            result = result + s + ", ";
        return result.substring(0, result.length()-2);
    }

    public void setStudios(ArrayList<String> studios) {
        this.studios = new ArrayList<>(studios);
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        String movie = "This movie (" + getId() + ") is called " + getTitle() + ", and was released on " + release_date + ".\n" +
                "It falls under the following genres: " + genres + ".\n" +
                "Here you have an overview of the plot: " + getOverview() + ".\n" +
                "It was produced by: " + studios + ", with a runtime of " + runtime + " minutes. \n" +
                "Here's a link to its poster: " + getPoster() + "\n" +
                "Here's its average score at TheMovieDataBase: " + score + ".\n" +
                "And here's a rundown of its cast and staff: " + movieCredits.toString();
        return movie;
    }
}
