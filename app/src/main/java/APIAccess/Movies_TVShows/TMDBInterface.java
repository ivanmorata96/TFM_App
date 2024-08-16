package APIAccess.Movies_TVShows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import APIAccess.Content;

public class TMDBInterface {
    private static String APIKey = "6a4260c509a9bde12e02539ca7e739b7";
    private static MovieCredits getMovieCredits(String movieID) throws IOException, JSONException {
        int index = 0;
        MovieCredits result = new MovieCredits();
        URL url = new URL("https://api.themoviedb.org/3/movie/" + movieID + "/credits?language=en-US&api_key=" + APIKey);
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
        JSONArray cast = jsonResponse.getJSONArray("cast");
        ArrayList<String> actores = new ArrayList<>();
        while(((index < cast.length()-1) && cast != null) && index < 5){
            JSONObject currentActor = cast.getJSONObject(index);
            actores.add((currentActor.getString("name") + " as " + currentActor.getString("character")));
            index++;
        }
        result.setCast(actores);

        JSONArray crew = jsonResponse.getJSONArray("crew");
        String directors="", producers="", screenplay="";
        for(int i = 0; i < crew.length(); i++){
            JSONObject currentCrew = crew.getJSONObject(i);
            switch(currentCrew.getString("job")){
                case "Director":
                    directors = directors + currentCrew.getString("name") + ", ";
                    result.setDirector(directors.substring(0, directors.length()-2));
                    break;
                case "Original Music Composer":
                    result.setComposer(currentCrew.getString("name"));
                    break;
                case "Producer":
                    producers = producers + currentCrew.getString("name") + ", ";
                    result.setProducer(producers.substring(0, producers.length()-2));
                    break;
                case "Screenplay":
                    screenplay = screenplay + currentCrew.getString("name") + ", ";
                    result.setScreenplay(screenplay.substring(0, screenplay.length()-2));
                    break;
            }
        }
        return result;
    }
    private static ArrayList<String> getTVCast(String tvID) throws IOException, JSONException {
        int index = 0;
        ArrayList<String> resultado = new ArrayList<>();
        URL url = new URL("https://api.themoviedb.org/3/tv/"+ tvID + "/credits?language=en-US&api_key=" + APIKey);
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
        JSONArray cast = jsonResponse.getJSONArray("cast");
        while(((index < cast.length()-1) && cast != null) && index < 10){
            JSONObject currentActor = cast.getJSONObject(index);
            resultado.add((currentActor.getString("name") + " as " + currentActor.getString("character")));
            index++;
        }
        return resultado;
    }
    private static String arrangePoster(JSONObject media){
        return "https://image.tmdb.org/t/p/w1280" + media.optString("poster_path");
    }
    private static ArrayList<String> arrangeStudios(JSONObject media) throws JSONException {
        ArrayList<String> result = new ArrayList<>();
        JSONArray studioList = media.getJSONArray("production_companies");
        if(studioList == null)
            result.add("N/A");
        else {
            for (int i = 0; i < studioList.length(); i++) {
                JSONObject currentStudio = studioList.getJSONObject(i);
                result.add(currentStudio.getString("name"));
            }
        }
        return result;
    }
    private static ArrayList<String> arrangeGenres(JSONObject media) throws JSONException {
        ArrayList<String> result = new ArrayList<>();
        JSONArray genreList = media.getJSONArray("genres");
        if(genreList == null)
            result.add("N/A");
        else {
            for (int i = 0; i < genreList.length(); i++) {
                JSONObject currentGenre = genreList.getJSONObject(i);
                result.add(currentGenre.getString("name"));
            }
        }
        return result;
    }
    public static ArrayList<Content> searchMovie(String movieSearch) throws IOException, JSONException {
        ArrayList<Content> results = new ArrayList<>();
        movieSearch = movieSearch.replace(" ", "%20");
        URL url = new URL("https://api.themoviedb.org/3/search/movie?query=" + movieSearch + "&include_adult=false&language=en-US&page=1&api_key=" + APIKey);
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
        JSONArray movies = jsonResponse.getJSONArray("results");
        for(int i = 0; i < movies.length(); i++){
            JSONObject currentMovie = movies.getJSONObject(i);
            Movie movie = new Movie();
            movie.setId(String.valueOf(currentMovie.getInt("id")));
            movie.setTitle(currentMovie.getString("title"));
            movie.setRelease_date(currentMovie.getString("release_date"));
            movie.setOverview(currentMovie.getString("overview"));
            movie.setPoster(arrangePoster(currentMovie));
            results.add(movie);
        }
        return results;
    }
    public static Movie getMovieDetails(String movieID) throws IOException, JSONException  {
        Movie result = new Movie();
        URL url = new URL("https://api.themoviedb.org/3/movie/"+ movieID + "?language=en-US&api_key=" + APIKey);
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

        JSONObject movie = new JSONObject(sb.toString());
        result.setId(String.valueOf(movie.getInt("id")));
        result.setTitle(movie.getString("title"));
        result.setRelease_date(movie.getString("release_date"));
        result.setMovieCredits(getMovieCredits(movieID));
        result.setOverview(movie.getString("overview"));
        result.setGenres(arrangeGenres(movie));
        result.setPoster(arrangePoster(movie));
        result.setStudios(arrangeStudios(movie));
        result.setRuntime(movie.getInt("runtime"));
        result.setScore(movie.getDouble("vote_average"));
        return result;
    }
    public static ArrayList<Content> searchTVShow(String tvSearch) throws IOException, JSONException {
        ArrayList<Content> results = new ArrayList<>();
        tvSearch = tvSearch.replace(" ", "%20");
        URL url = new URL("https://api.themoviedb.org/3/search/tv?query=" + tvSearch + "&include_adult=false&language=en-US&page=1&api_key=" + APIKey);
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
        JSONArray tvShows = jsonResponse.getJSONArray("results");
        for(int i = 0; i < tvShows.length(); i++){
            JSONObject currentShow = tvShows.getJSONObject(i);
            TVShow tvShow = new TVShow();
            tvShow.setId(String.valueOf(currentShow.getInt("id")));
            tvShow.setTitle(currentShow.getString("name"));
            tvShow.setOverview(currentShow.getString("overview"));
            tvShow.setRelease_date(currentShow.getString("first_air_date"));
            tvShow.setPoster(arrangePoster(currentShow));
            results.add(tvShow);
        }
        return results;
    }
    public static TVShow getTVShowDetails(String tvID) throws IOException, JSONException {
        TVShow result = new TVShow();
        URL url = new URL("https://api.themoviedb.org/3/tv/"+ tvID + "?language=en-US&api_key=" + APIKey);
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

        JSONObject tv = new JSONObject(sb.toString());
        result.setId(String.valueOf(tv.getInt("id")));
        result.setTitle(tv.getString("name"));
        result.setRelease_date(tv.getString("first_air_date"));
        result.setNumber_of_episodes(tv.getInt("number_of_episodes"));
        result.setNumber_of_seasons(tv.getInt("number_of_seasons"));
        result.setCast(getTVCast(tvID));
        result.setOverview(tv.getString("overview"));
        result.setStudios(arrangeStudios(tv));
        result.setGenres(arrangeGenres(tv));
        result.setPoster(arrangePoster(tv));
        result.setScore(tv.getDouble("vote_average"));
        return result;
    }
}
