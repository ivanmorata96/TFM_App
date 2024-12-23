package com.main.tfm.mediaAPIs.Books;
import android.util.Log;

import com.main.tfm.support.Content;
import com.main.tfm.support.ContentTag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleBooksInterface {
    private static final String API_KEY = "AIzaSyDgdOeUefmkshxMrOSiYi6U2Pn_zOslpmI";
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

    public static ArrayList<Content> searchBooksByTag(HashMap<String, String> tags) throws IOException, JSONException{
        ArrayList<String> tempTags = new ArrayList<>(tags.keySet());
        String tagList = formatTags(tempTags);
        ArrayList<Content> results = new ArrayList<>();
        URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=subject:" + tagList + "&key=" + API_KEY);
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

    public static Book getSingleBookByTags(String tags) throws IOException, JSONException{
        tags = tags.replace("+", "+OR+");
        Book result = new Book();
        URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=subject:("+ tags +")&key=" + API_KEY);
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
        if(jsonResponse.has("items")){
            JSONArray books = jsonResponse.getJSONArray("items");
            int randomIndex = (int) (Math.random()*books.length());
            JSONObject currentBook = books.getJSONObject(randomIndex);
            result.setId(currentBook.getString("id"));
            result.setTitle(arrangeTitle(currentBook));
            result.setAuthor(arrangeAuthors(currentBook));
            result.setOverview(arrangeOverview(currentBook));
            result.setDate_of_publishing(arrangeDoP(currentBook));
            result.setPoster(arrangeCoverSearch(currentBook));
        }
        return result;
    }

    public static HashMap<String, String> getBookTags(String id) throws IOException, JSONException{
        HashMap<String, String> result = new HashMap<>();
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
        ArrayList<String> arrangedJson = arrangeGenre(bookJSON);
        for(String t : arrangedJson){
            String tagID = ContentTag.returnTMDBTagID(t);
            result.put(t, tagID);
        }
        return result;
    }

    private static String formatTags(ArrayList<String> tags){
        String result ="";
        for(String t : tags){
            result += (t + ",");
        }
        result = result.substring(0, result.length()-1);
        result = result.replace(" ", "%20");
        return result;
    }
}
