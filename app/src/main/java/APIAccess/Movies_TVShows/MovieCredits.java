package APIAccess.Movies_TVShows;

import java.util.ArrayList;

public class MovieCredits {
    private ArrayList<String> cast;
    private String director;
    private String composer;
    private String screenplay;
    private String producer;

    public MovieCredits(){
        cast = new ArrayList<>();
        director= composer = screenplay = producer ="";
    }

    public MovieCredits(ArrayList<String> cast, String director, String composer, String screenplay, String producer) {
        this.cast = cast;
        this.director = director;
        this.composer = composer;
        this.screenplay = screenplay;
        this.producer = producer;
    }

    public MovieCredits(MovieCredits otro){
        this.cast = new ArrayList<>(otro.cast);
        this.director = otro.director;
        this.composer = otro.composer;
        this.screenplay = otro.screenplay;
        this.producer = otro.producer;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public void setCast(ArrayList<String> cast) {
        this.cast = new ArrayList<>(cast);
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getScreenplay() {
        return screenplay;
    }

    public void setScreenplay(String screenplay) {
        this.screenplay = screenplay;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @Override
    public String toString() {
        return "Credits{" +
                "cast=" + cast +
                ", director='" + director + '\'' +
                ", composer='" + composer + '\'' +
                ", screenplay='" + screenplay + '\'' +
                ", producer='" + producer + '\'' +
                '}';
    }
}

