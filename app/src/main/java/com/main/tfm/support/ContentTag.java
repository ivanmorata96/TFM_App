package com.main.tfm.support;

import android.util.Log;

import com.main.tfm.mediaAPIs.Books.GoogleBooksInterface;
import com.main.tfm.mediaAPIs.Movies_TVShows.TMDBInterface;
import com.main.tfm.mediaAPIs.Videogames.RAWGInterface;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ContentTag {

    private String id;
    private HashMap<String, String> tags;
    private ArrayList<String> genres;
    private int userScore;

    private static HashMap<String, String> relevantTags = new HashMap<>();

    public ContentTag(){
        this.id = "";
        tags = new HashMap<>();
        genres = new ArrayList<>();
        userScore = 0;
        fillRelevantTags();
    }

    public ContentTag(String id, String type) throws JSONException, IOException {
        this.id = id;
        switch(type){
            case "movie":
                this.tags = new HashMap<>(TMDBInterface.getTMDBTagsString(id, 1));
                break;
            case "tvshow":
                this.tags = new HashMap<>(TMDBInterface.getTMDBTagsString(id, 2));
                break;
            case "videogame":
                this.tags = new HashMap<>(RAWGInterface.getVideogameTags(id));
                break;
            case "book":
                this.tags = new HashMap<>(GoogleBooksInterface.getBookTags(id));
                break;
            default:
                this.tags = new HashMap<>();
        }
        this.genres = new ArrayList<>();
        this.userScore = -1;
    }

    public ContentTag(String id, HashMap<String, String> tags, ArrayList<String> genres, int userScore) {
        fillRelevantTags();
        this.id = id;
        this.tags = new HashMap<>(tags);
        sortTags();
        this.genres = new ArrayList<>(genres);
        this.userScore = userScore;
    }

    public ContentTag(ContentTag other){
        this.id = other.id;
        this.tags = new HashMap<>(other.tags);
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

    public HashMap<String, String> getTags() {
        return tags;
    }

    public String getTagsAsString(String type){
        String result = "";
        if (!tags.isEmpty()) {
            if (type.equals("book")) {
                for (String t : genres) {
                    result += (t + "+");
                }
            } else if(type.equals("videogame")) {
                for (String t : tags.values()) {
                    result += (t + "+");
                }
            }else{
                for (String t : tags.values()) {
                    result += (t + ",");
                }
            }
            result = result.substring(0, result.length() - 1);
            result = result.replace(" ", "%20");
        }
        return result;
    }

    public String getRandomTag(String type){
        if(type.equals("movie") || type.equals("tvshow")){
            ArrayList<String> currentTags = new ArrayList<>(tags.values());
            int randomIndex = (int) (Math.random()*currentTags.size());
            return currentTags.get(randomIndex);
        }else{
            ArrayList<String> currentTags = new ArrayList<>(tags.keySet());
            int randomIndex = (int) (Math.random()*currentTags.size());
            return currentTags.get(randomIndex);
        }
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = new HashMap<>(tags);
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

    private static void fillRelevantTags(){ //Los números son las ID para las API de TMDB
        relevantTags.put("fantasy", "293198");
        relevantTags.put("science ciction", "281358");
        relevantTags.put("Horror", "315058");
        relevantTags.put("comedy", "322268");
        relevantTags.put("drama", "316421");
        relevantTags.put("romance", "9840");
        relevantTags.put("mystery", "316332");
        relevantTags.put("thriller", "316362");
        relevantTags.put("epic", "6917");
        relevantTags.put("historical", "15126");
        relevantTags.put("history", "282633");
        relevantTags.put("historical fiction", "12995");
        relevantTags.put("revenge", "9748");
        relevantTags.put("theft", "14604");
        relevantTags.put("melodramatic", "325835");
        relevantTags.put("speculative", "190531");
    }

    private void sortTags(){
        HashMap<String, String> sortedTags = new HashMap<>();
        for(String t : tags.keySet()){
            if(relevantTags.containsKey(t)){
                sortedTags.put(t, tags.get(t));
            }
        }
        tags = new HashMap<>(sortedTags);
    }

    public static String returnTMDBTagID(String tagName){
        return relevantTags.getOrDefault(tagName, "");
    }

    public static String returnTMDBTagName(String tagID){
        String result = "";
        for(String t : relevantTags.keySet()){
            if(relevantTags.get(t).equals(tagID))
                result = t;
        }
        return result;
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
