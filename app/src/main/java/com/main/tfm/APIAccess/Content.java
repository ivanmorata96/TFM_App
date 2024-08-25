package com.main.tfm.APIAccess;

public abstract class Content{
    private String id;
    private String title;
    private String overview;
    private String poster;

    public Content(){
        id = title = overview = poster = "";
    }

    public Content(String id, String title, String overview, String poster) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.poster = poster;
    }

    public Content(Content other){
        this.id = other.id;
        this.title = other.title;
        this.overview = other.overview;
        this.poster = other.poster;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }
    public String getShortOverview(){
        if(overview.length() > 100)
            return overview.substring(0, 100) + "...";
        else return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }


}
