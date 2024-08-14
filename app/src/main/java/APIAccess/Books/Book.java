package APIAccess.Books;

import java.util.ArrayList;
import java.util.ListResourceBundle;

public class Book {
    private String id;
    private String title;
    private ArrayList<String> author;
    private String overview;
    private String publisher;
    private String date_of_publishing;
    private String ISBN;
    private int pages;
    private ArrayList<String> genres;
    private double score;
    private String cover;

    public Book(){
        id=title=publisher=date_of_publishing=ISBN=cover="";
        author=genres=new ArrayList<>();
        pages=-1;
        score=-1;
    }

    public Book(String id, String title, ArrayList<String> author, String overview, String publisher, String date_of_publishing, String ISBN, int pages, ArrayList<String> genres, double score, String cover) {
        this.id = id;
        this.title = title;
        this.author = new ArrayList<>(author);
        this.overview = overview;
        this.publisher = publisher;
        this.date_of_publishing = date_of_publishing;
        this.ISBN = ISBN;
        this.pages = pages;
        this.genres = new ArrayList<>(genres);
        this.score = score;
        this.cover = cover;
    }

    public Book(Book other){
        this.id = other.id;
        this.title = other.title;
        this.author = new ArrayList<>(other.author);
        this.overview = other.overview;
        this.publisher = other.publisher;
        this.date_of_publishing = other.date_of_publishing;
        this.ISBN = other.ISBN;
        this.pages = other.pages;
        this.genres = new ArrayList<>(other.genres);
        this.score = other.score;
        this.cover = other.cover;
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

    public String getAuthor() {
        String result = "";
        for(String a : author){
            result = result + a + ", ";
        }
        return result.substring(0, result.length()-2);
    }

    public void setAuthor(ArrayList<String> author) {
        this.author = new ArrayList<>(author);
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        overview = overview.replace("<p>", "");
        overview = overview.replace("</p>", "");
        overview = overview.replace("<br>", "");
        overview = overview.substring(0, 500) + "...";
        this.overview = overview;
    }

    public String getDate_of_publishing() {
        return date_of_publishing;
    }

    public void setDate_of_publishing(String date_of_publishing) {
        this.date_of_publishing = date_of_publishing;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getGenres() {
        String result="";
        String [] split;
        split = genres.get(0).split("/");
        for(int i = 0; i < split.length; i++){
            result = result+ split[i];
        }
        return result;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = new ArrayList<>(genres);
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getCover() {
        String[] split;
        split = cover.split("http");
        cover = "https" + split[1];
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        String book = "This book (" + id + ") is called " + title + " and was written by " + author + ".\n" +
                "It was first published in " + date_of_publishing + " by " + publisher + ", identified with the ISBN code " + ISBN + ".\n" +
                "It falls under the following genres: " + genres + ".\n" +
                "It has a total of " + pages + " pages. It has a rating of " + score + ".\n" +
                "Here you have an overview: " + overview + ".\n" +
                "Here you can check its cover: " + cover + ".\n";
        return book;
    }
}
