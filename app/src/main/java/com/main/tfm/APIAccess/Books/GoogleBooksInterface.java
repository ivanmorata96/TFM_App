package com.main.tfm.APIAccess.Books;

import com.main.tfm.support.Content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleBooksInterface {
    private static final String API_KEY = "AIzaSyDgdOeUefmkshxMrOSiYi6U2Pn_zOslpmI"; // Reemplaza con tu clave de API

    private static String arrangeTitle(JSONObject book) throws JSONException {
        String result = "N/A";
        JSONObject bookInfo = book.optJSONObject("volumeInfo");
        if(bookInfo != null){
            result = bookInfo.getString("title");
        }
        return result;
    }

    private static ArrayList<String> arrangeAuthors(JSONObject book) throws JSONException {
        ArrayList<String> authorList = new ArrayList<>();
        JSONObject bookInfo = book.optJSONObject("volumeInfo");
        if (bookInfo != null) {
            JSONArray authors = bookInfo.optJSONArray("authors");
            if (authors != null) {
                for (int i = 0; i < authors.length(); i++) {
                    authorList.add(authors.get(i).toString());
                }
            } else authorList.add("N/A");
        }
        return authorList;
    }

    private static String arrangeOverview(JSONObject book){
        String result = "N/A";
        JSONObject bookInfo = book.optJSONObject("volumeInfo");
        if(bookInfo != null){
            result = bookInfo.optString("description");
        }
        return result;
    }

    private static String arrangePublisher(JSONObject book){
        String result = "N/A";
        JSONObject bookInfo = book.optJSONObject("volumeInfo");
        if(bookInfo != null){
            result = bookInfo.optString("publisher");
        }
        return result;
    }

    private static String arrangeDoP(JSONObject book){
        String result = "N/A";
        JSONObject bookInfo = book.optJSONObject("volumeInfo");
        if(bookInfo != null){
            result = bookInfo.optString("publishedDate");
        }
        return result;
    }

    private static String arrangeISBN(JSONObject book) throws JSONException {
        String result = "N/A";
        JSONObject bookInfo = book.optJSONObject("volumeInfo");
        if (bookInfo != null) {
            JSONArray identifiers = bookInfo.optJSONArray("industryIdentifiers");
            if (identifiers != null) {
                for (int i = 0; i < identifiers.length(); i++) {
                    result = identifiers.getJSONObject(0).getString("identifier");
                }
            }
        }
        return result;
    }

    private static int arrangePages(JSONObject book){
        int pages = -1;
        JSONObject bookInfo = book.optJSONObject("volumeInfo");
        if(bookInfo != null){
            pages = bookInfo.optInt("pageCount");
        }
        return pages;
    }

    private static ArrayList<String> arrangeGenre(JSONObject book) throws JSONException {
        ArrayList<String> result = new ArrayList<>();
        JSONObject bookInfo = book.optJSONObject("volumeInfo");
        JSONArray genres = bookInfo.getJSONArray("categories");
        if(genres != null){
            for(int i = 0; i < genres.length(); i++){
                result.add(genres.getString(i));
            }
        }else result.add("N/A");
        return result;
    }

    private static String arrangeCoverDetails(JSONObject book) throws JSONException {
        String result = "N/A";
        JSONObject bookInfo = book.optJSONObject("volumeInfo");
        JSONObject coverLinks = bookInfo.getJSONObject("imageLinks");
        if(coverLinks != null){
            result = coverLinks.optString("medium");
        }
        return result;
    }

    private static String arrangeCoverSearch(JSONObject book) throws JSONException {
        String result = "N/A";
        JSONObject bookInfo = book.optJSONObject("volumeInfo");
        JSONObject coverLinks = bookInfo.optJSONObject("imageLinks");
        if(coverLinks != null){
            result = coverLinks.optString("thumbnail");
        }else result = "http://developers.elementor.com/docs/assets/img/elementor-placeholder-image.png";
        return result;
    }

    public static ArrayList<Content> searchBooks(String title) throws IOException, JSONException {
        title = title.replace(" ", "%20");
        ArrayList<Content> results = new ArrayList<>();
        URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=+" + title + "&key=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        conn.disconnect();

        JSONObject jsonResponse = new JSONObject(sb.toString());
        JSONArray books = jsonResponse.getJSONArray("items");
        for (int i = 0; i < books.length(); i++) {
            JSONObject currentBook = books.getJSONObject(i);
            Book book = new Book();
            book.setId(currentBook.getString("id"));
            book.setTitle(arrangeTitle(currentBook));
            book.setAuthor(arrangeAuthors(currentBook));
            book.setOverview(arrangeOverview(currentBook));
            book.setDate_of_publishing(arrangeDoP(currentBook));
            book.setPoster(arrangeCoverSearch(currentBook));
            results.add(book);
        }
        return results;
    }

    public static Book getBookDetails(String id) throws IOException, JSONException {
        Book result = new Book();
        URL url = new URL("https://www.googleapis.com/books/v1/volumes/" + id + "?key=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        conn.disconnect();

        JSONObject bookJSON = new JSONObject(sb.toString());
        result.setId(bookJSON.getString("id"));
        result.setTitle(arrangeTitle(bookJSON));
        result.setAuthor(arrangeAuthors(bookJSON));
        result.setOverview(arrangeOverview(bookJSON));
        result.setPublisher(arrangePublisher(bookJSON));
        result.setDate_of_publishing(arrangeDoP(bookJSON));
        result.setISBN(arrangeISBN(bookJSON));
        result.setPages(arrangePages(bookJSON));
        result.setGenres(arrangeGenre(bookJSON));
        result.setScore(bookJSON.getJSONObject("volumeInfo").optDouble("averageRating"));
        result.setPoster(arrangeCoverDetails(bookJSON));
        return result;
    }

    public static void main(String[] args) throws IOException {
        /*
        ArrayList<Book> libros;
        libros = searchBooks("Harry Potter");
        for(Book b: libros){
            System.out.println(b.toString());
            System.out.println("-----------------------------------------------------------------------------------------");
        }*/
        /*
        Book b = new Book(getBookDetails("2zgRDXFWkm8C"));
        System.out.println(b.toString());*/
    }
}
