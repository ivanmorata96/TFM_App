package com.main.tfm.support;

import com.main.tfm.mediaAPIs.Books.GoogleBooksInterface;
import com.main.tfm.mediaAPIs.Movies_TVShows.TMDBInterface;
import com.main.tfm.mediaAPIs.Videogames.RAWGInterface;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class ContentTag {

    private String id;
    private ArrayList<String> tags;
    private ArrayList<String> genres;
    private int userScore;

    private static ArrayList<String> relevantTags = new ArrayList<>();

    public ContentTag(){
        this.id = "";
        tags = new ArrayList<>();
        genres = new ArrayList<>();
        userScore = 0;
        fillRelevantTags();
    }

    public ContentTag(String id, String type) throws JSONException, IOException {
        this.id = id;
        switch(type){
            case "movie":
                this.tags = new ArrayList<>(TMDBInterface.getTMDBTagsString(id, 1));
                break;
            case "tvshow":
                this.tags = new ArrayList<>(TMDBInterface.getTMDBTagsString(id, 2));
                break;
            case "videogame":
                this.tags = new ArrayList<>(RAWGInterface.getVideogameTags(id));
                break;
            case "book":
                this.tags = new ArrayList<>(GoogleBooksInterface.getBookTags(id));
                break;
            default:
                this.tags = new ArrayList<>();
        }
        this.genres = new ArrayList<>();
        this.userScore = -1;
    }

    public ContentTag(String id, ArrayList<String> tags, ArrayList<String> genres, int userScore) {
        this.id = id;
        this.tags = new ArrayList<>(tags);
        sortTags();
        this.genres = new ArrayList<>(genres);
        this.userScore = userScore;
        fillRelevantTags();
    }

    public ContentTag(ContentTag other){
        this.id = other.id;
        this.tags = new ArrayList<>(other.tags);
        this.genres = new ArrayList<>(other.genres);
        this.userScore = other.userScore;
        fillRelevantTags();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getTagsAsString(String type){
        String result = "";
        if(type.equals("book") || type.equals("videogame")){
            for (String t : genres) {
                result += (t + "+");
            }
        }else{
            for(String t : tags){
                result += (t + ",");
            }
        }
        result = result.substring(0, result.length()-1);
        result = result.replace(" ", "%20");
        return result;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = new ArrayList<>(tags);
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public String getGenresAsString(){
        String result = "";
        for(String g : genres){
            result += (g + ",");
        }
        result = result.substring(0, result.length()-1);
        return result;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = new ArrayList<>(genres);
    }

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    private static void fillRelevantTags(){
        relevantTags.add("Fantasy");
        relevantTags.add("Science Fiction");
        relevantTags.add("Horror");
        relevantTags.add("Comedy");
        relevantTags.add("Drama");
        relevantTags.add("Romance");
        relevantTags.add("Mystery");
        relevantTags.add("Thriller");
    }

    private void sortTags(){
        ArrayList<String> sortedTags = new ArrayList<>();
        for(String t : tags){
            if(relevantTags.contains(t))
                sortedTags.add(t);
        }
        tags.removeAll(sortedTags);
        tags = new ArrayList<>(sortedTags);
    }

    @Override
    public String toString() {
        return "ContentTag{" +
                "id='" + id + '\'' +
                ", tags=" + tags +
                ", genres=" + genres +
                ", userScore=" + userScore +
                '}';
    }
}
