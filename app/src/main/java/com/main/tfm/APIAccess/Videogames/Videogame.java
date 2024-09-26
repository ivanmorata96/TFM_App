package com.main.tfm.APIAccess.Videogames;

import com.main.tfm.APIAccess.Content;

import java.util.ArrayList;

public class Videogame extends Content {

    private String release_date;
    private ArrayList<String> platforms;
    private ArrayList<String> developers;
    private ArrayList<String> genres;
    private int score;

    public Videogame(){
        super();
        release_date="";
        platforms = new ArrayList<>();
        score=-1;
    }

    public Videogame(String id, String name, String release_date, String overview, String poster, ArrayList<String> platforms, ArrayList<String> developers, ArrayList<String> genres, int score) {
        super(id, name, overview, poster);
        this.release_date = release_date;
        this.platforms = new ArrayList<>(platforms);
        this.developers = new ArrayList<>(developers);
        this.genres = new ArrayList<>(genres);
        this.score = score;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setOverview(String overview) {
        overview = overview.replace("<p>", "");
        overview = overview.replace("</p>", "");
        if(overview.length() > 500)
            overview = overview.substring(0, 500) + "...";
        super.setOverview(overview);
    }

    public String getPlatforms() {
        String result = "Platforms\n";
        for(String p : platforms)
            result = result + p + "\n";
        return result;
    }

    public void setPlatforms(ArrayList<String> platforms) {
        this.platforms = new ArrayList<>(platforms);
    }

    public String getDevelopers() {
        String result = "Developers\n";
        for(String d : developers)
            result = result + d + "\n";
        return result;
    }

    public void setDevelopers(ArrayList<String> developers) {
        this.developers = new ArrayList<>(developers);
    }

    public String getGenres() {
        String result = "Genres\n";
        for(String g : genres)
            result = result + g + "\n";
        return result;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = new ArrayList<>(genres);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        String videogame = "This videogame (" + getId() +") is called " + getTitle() + ", and was released on " + release_date + ".\n" +
                "This game falls under the following genres: " + genres + ".\n" +
                "Here you have an overview of the plot: " + getOverview() + ".\n" +
                "It was produced by: " + developers + ", on the following platforms " + platforms + ". \n" +
                "Here's a link to its poster: " + getPoster() + "\n" +
                "Here's its average score at Metacritic: " + score + ".\n";
        return videogame;
    }
}
