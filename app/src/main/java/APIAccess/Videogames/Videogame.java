package APIAccess.Videogames;

import java.util.ArrayList;

public class Videogame {
    private String id;
    private String name;
    private String release_date;
    private String overview;
    private String poster;
    private ArrayList<String> platforms;
    private ArrayList<String> developers;
    private ArrayList<String> genres;
    private int score;

    public Videogame(){
        name=release_date=overview=poster="";
        platforms = new ArrayList<>();
        score=-1;
    }

    public Videogame(String id, String name, String release_date, String overview, String poster, ArrayList<String> platforms, ArrayList<String> developers, ArrayList<String> genres, int score) {
        this.id = id;
        this.name = name;
        this.release_date = release_date;
        this.overview = overview;
        this.poster = poster;
        this.platforms = new ArrayList<>(platforms);
        this.developers = new ArrayList<>(developers);
        this.genres = new ArrayList<>(genres);
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
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

    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<String> platforms) {
        this.platforms = new ArrayList<>(platforms);
    }

    public ArrayList<String> getDevelopers() {
        return developers;
    }

    public void setDevelopers(ArrayList<String> developers) {
        this.developers = new ArrayList<>(developers);
    }

    public ArrayList<String> getGenres() {
        return genres;
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
        String videogame = "This videogame is called " + name + ", and was released on " + release_date + ".\n" +
                "This game falls under the following genres: " + genres + ".\n" +
                "Here you have an overview of the plot: " + overview + ".\n" +
                "It was produced by: " + developers + ", on the following platforms " + platforms + ". \n" +
                "Here's a link to its poster: " + poster + "\n" +
                "Here's its average score at Metacritic: " + score + ".\n";
        return videogame;
    }
}
