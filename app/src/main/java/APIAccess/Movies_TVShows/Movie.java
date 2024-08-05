package APIAccess.Movies_TVShows;

import java.util.ArrayList;

public class Movie {
    private String id;
    private String title;
    private String release_date;
    private MovieCredits movieCredits;
    private String overview;
    private ArrayList<String> genres;
    private String poster;
    private ArrayList<String> studios;
    private int runtime;
    private double score;

    public Movie(){
        id=title=release_date=overview=poster="";
        runtime=-1;
        score =-1;
        movieCredits = new MovieCredits();
        genres = studios = new ArrayList<>();
    }

    public Movie(String id, String title, String release_date, MovieCredits movieCredits, String overview, ArrayList<String> genres, String poster, ArrayList<String> studios, int runtime, double score) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.movieCredits = new MovieCredits(movieCredits);
        this.overview = overview;
        this.genres = new ArrayList<>(genres);
        this.poster = poster;
        this.studios = new ArrayList<>(studios);
        this.runtime = runtime;
        this.score = score;
    }

    public Movie(Movie other){
        this.id = other.id;
        this.title = other.title;
        this.release_date = other.release_date;
        this.movieCredits = new MovieCredits(other.movieCredits);
        this.overview = other.overview;
        this.genres = new ArrayList<>(other.genres);
        this.poster = other.poster;
        this.studios = new ArrayList<>(other.studios);
        this.runtime = other.runtime;
        this.score = other.score;
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

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public MovieCredits getMovieCredits() {
        return movieCredits;
    }

    public void setMovieCredits(MovieCredits movieCredits) {
        this.movieCredits = new MovieCredits(movieCredits);
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = new ArrayList<>(genres);
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
        String movie = "This movie (" + id + ") is called " + title + ", and was released on " + release_date + ".\n" +
                "It falls under the following genres: " + genres + ".\n" +
                "Here you have an overview of the plot: " + overview + ".\n" +
                "It was produced by: " + studios + ", with a runtime of " + runtime + " minutes. \n" +
                "Here's a link to its poster: " + poster + "\n" +
                "Here's its average score at TheMovieDataBase: " + score + ".\n" +
                "And here's a rundown of its cast and staff: " + movieCredits.toString();
        return movie;
    }
}
