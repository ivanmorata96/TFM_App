package com.main.tfm.APIAccess.Movies_TVShows;
import com.main.tfm.support.Content;

import java.util.ArrayList;

public class TVShow extends Content {
    private String release_date;
    private int number_of_episodes;
    private int number_of_seasons;
    private ArrayList<String> cast;
    private ArrayList<String> studios;
    private ArrayList<String> genres;
    private String status;
    private double score;

    public TVShow(){
        super();
        release_date=status="";
        number_of_episodes=number_of_seasons=-1;
        score = -1;
        cast = studios = new ArrayList<>();
    }

    public TVShow(String id, String name, String release_date, int number_of_episodes, int number_of_seasons, ArrayList<String> cast, String overview, ArrayList<String> studios, ArrayList<String> genres, String poster, String status, double score) {
        super(id, name, overview, poster);
        this.release_date = release_date;
        this.number_of_episodes = number_of_episodes;
        this.number_of_seasons = number_of_seasons;
        this.cast = new ArrayList<>(cast);
        this.studios = new ArrayList<>(studios);
        this.genres = new ArrayList<>(genres);
        this.status = status;
        this.score = score;
    }

    public TVShow(TVShow other){
        super(other);
        this.release_date = other.release_date;
        this.number_of_episodes = other.number_of_episodes;
        this.number_of_seasons = other.number_of_seasons;
        this.cast = new ArrayList<>(other.cast);
        this.studios = new ArrayList<>(other.studios);
        this.genres = new ArrayList<>(other.genres);
        this.status = other.status;
        this.score = other.score;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getNumber_of_episodes() {
        return number_of_episodes + " episodes.";
    }

    public void setNumber_of_episodes(int number_of_episodes) {
        this.number_of_episodes = number_of_episodes;
    }

    public String getNumber_of_seasons() {
        return number_of_seasons + " seasons.";
    }

    public void setNumber_of_seasons(int number_of_seasons) {
        this.number_of_seasons = number_of_seasons;
    }

    public String getCast() {
        String result="Actors\n";
        for(String c : cast)
            result = result + c + "\n";
        return result;
    }

    public void setCast(ArrayList<String> cast) {
        this.cast = new ArrayList<>(cast);
    }

    public void setPoster(String poster) {
        super.setPoster(poster.replace("\"", ""));
    }

    public String getStudios() {
        String result="";
        for(String s : studios)
            result = result + s + ".\n";
        return result;
    }

    public void setStudios(ArrayList<String> studios) {
        this.studios = new ArrayList<>(studios);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        String tv = "This TV Show (" + getId() + ") is called " + getTitle() + ", and it aired first on " + release_date + ".\n" +
                "It falls under the following genres: " + genres + ".\n" +
                "Here you have an overview of the plot: " + getOverview() + ".\n" +
                "It was produced by: " + studios + ", with a total of " + number_of_episodes + " episodes across " + number_of_seasons + " seasons. \n" +
                "Here's a link to its poster: " + getPoster() + "\n" +
                "Here's its average score at TheMovieDataBase: " + score + ".\n" +
                "And here's a rundown of its cast and staff: " + cast;
        return tv;
    }
}

