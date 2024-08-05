package APIAccess.Movies_TVShows;

import java.util.ArrayList;

public class TVShow {
    private String id;
    private String name;
    private String release_date;
    private int number_of_episodes;
    private int number_of_seasons;
    private ArrayList<String> cast;
    private String overview;
    private ArrayList<String> studios;
    private ArrayList<String> genres;
    private String poster;
    private String status;
    private double score;

    public TVShow(){
        id=name= release_date =overview=poster=status="";
        number_of_episodes=number_of_seasons=-1;
        score = -1;
        cast = studios = new ArrayList<>();
    }

    public TVShow(String id, String name, String release_date, int number_of_episodes, int number_of_seasons, ArrayList<String> cast, String overview, ArrayList<String> studios, ArrayList<String> genres, String poster, String status, double score) {
        this.id = id;
        this.name = name;
        this.release_date = release_date;
        this.number_of_episodes = number_of_episodes;
        this.number_of_seasons = number_of_seasons;
        this.cast = new ArrayList<>(cast);
        this.overview = overview;
        this.studios = new ArrayList<>(studios);
        this.genres = new ArrayList<>(genres);
        this.poster = poster;
        this.status = status;
        this.score = score;
    }

    public TVShow(TVShow other){
        this.id = other.id;
        this.name = other.name;
        this.release_date = other.release_date;
        this.number_of_episodes = other.number_of_episodes;
        this.number_of_seasons = other.number_of_seasons;
        this.cast = new ArrayList<>(other.cast);
        this.overview = other.overview;
        this.studios = new ArrayList<>(other.studios);
        this.genres = new ArrayList<>(other.genres);
        this.poster = other.poster;
        this.status = other.status;
        this.score = other.score;
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

    public int getNumber_of_episodes() {
        return number_of_episodes;
    }

    public void setNumber_of_episodes(int number_of_episodes) {
        this.number_of_episodes = number_of_episodes;
    }

    public int getNumber_of_seasons() {
        return number_of_seasons;
    }

    public void setNumber_of_seasons(int number_of_seasons) {
        this.number_of_seasons = number_of_seasons;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public void setCast(ArrayList<String> cast) {
        this.cast = new ArrayList<>(cast);
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
        this.poster = poster.replace("\"", "");
    }

    public ArrayList<String> getStudios() {
        return studios;
    }

    public void setStudios(ArrayList<String> studios) {
        this.studios = new ArrayList<>(studios);
    }

    public ArrayList<String> getGenres() {
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
        String tv = "This TV Show (" + id + ") is called " + name + ", and it aired first on " + release_date + ".\n" +
                "It falls under the following genres: " + genres + ".\n" +
                "Here you have an overview of the plot: " + overview + ".\n" +
                "It was produced by: " + studios + ", with a total of " + number_of_episodes + " episodes across " + number_of_seasons + " seasons. \n" +
                "Here's a link to its poster: " + poster + "\n" +
                "Here's its average score at TheMovieDataBase: " + score + ".\n" +
                "And here's a rundown of its cast and staff: " + cast;
        return tv;
    }
}

