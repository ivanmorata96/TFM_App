package com.main.tfm.support;

import java.util.ArrayList;

public class ContentTag {

    private String id;
    private ArrayList<String> tags;
    private ArrayList<String> genres;
    private int userScore;

    private static ArrayList<String> relevantTags = new ArrayList<>();

    public ContentTag(String id, ArrayList<String> tags, ArrayList<String> genres, int userScore) {
        fillRelevantTags();
        this.id = id;
        this.tags = new ArrayList<>(tags);
        sortTags();
        this.genres = new ArrayList<>(genres);
        this.userScore = userScore;
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
        String result="";
        if(type.equals("book")){
            for (String t : genres) {
                result += (t + "+");
            }
        }else if (type.equals("movie") || type.equals("tvshow")) {
            for(String t : tags){
                result += (t + ",");
            }
        }else{
            for (String t : tags) {
                result += (t + "+");
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
