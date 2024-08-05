package APIAccess.Movies_TVShows;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TMDBTest {
    private static MovieCredits getMovieCredits(String movieID) throws IOException, InterruptedException {
        int index = 0;
        MovieCredits resultado = new MovieCredits();
        String json="", rol="", directores="", productores="", guionistas="";
        ArrayList<String> actores = new ArrayList<>();
        JsonObject jobj;
        JsonArray lista;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + movieID + "/credits?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2YTQyNjBjNTA5YTliZGUxMmUwMjUzOWNhN2U3MzliNyIsIm5iZiI6MTcyMTU3MzAwNS4zNjI2OCwic3ViIjoiNWNkMzBmMWI5MjUxNDE3Y2NlMmY2MWNiIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.XUwfebRebvZ31ZqCcgUx9ExHOSS0q6wti9KJeN4F6Tw")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        json = response.body();
        //En primer lugar, se busca en el JSON obtenido los cinco primeros miembros del subarray de cast, y se trasladan a un array de String con los actores principales.
        jobj = new Gson().fromJson(json, JsonObject.class);
        lista = jobj.getAsJsonArray("cast");
        while(((index < lista.size()-1) && !lista.isEmpty()) && index < 5){
            actores.add((lista.get(index).getAsJsonObject().get("name").getAsString() + " as " + (lista.get(index).getAsJsonObject().get("character").getAsString())));
            index++;
        }
        resultado.setCast(actores);
        //A continuación, se busca en el subarray de crew al resto de posiciones.
        lista = jobj.getAsJsonArray("crew");
        for(JsonElement current : lista){
            rol = current.getAsJsonObject().get("job").getAsString();
            switch (rol){
                case "Director":
                    directores = directores + current.getAsJsonObject().get("name").getAsString() + ", ";
                    resultado.setDirector(directores.substring(0, directores.length()-2));
                    break;
                case "Original Music Composer":
                    resultado.setComposer(current.getAsJsonObject().get("name").getAsString());
                    break;
                case "Producer":
                    productores = productores + current.getAsJsonObject().get("name").getAsString() + ", ";
                    resultado.setProducer(productores.substring(0, productores.length()-2));
                    break;
                case "Screenplay":
                    guionistas = guionistas + current.getAsJsonObject().get("name").getAsString() + ", ";
                    resultado.setScreenplay(guionistas.substring(0, guionistas.length()-2));
                    break;
            }
        }
        return resultado;
    }
    private static ArrayList<String> getTVCast(String tvID) throws IOException, InterruptedException {
        int index = 0;
        ArrayList<String> resultado = new ArrayList<>();
        String json;
        JsonObject jobj;
        JsonArray lista;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/tv/"+ tvID + "/credits?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2YTQyNjBjNTA5YTliZGUxMmUwMjUzOWNhN2U3MzliNyIsIm5iZiI6MTcyMTU3MzAwNS4zNjI2OCwic3ViIjoiNWNkMzBmMWI5MjUxNDE3Y2NlMmY2MWNiIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.XUwfebRebvZ31ZqCcgUx9ExHOSS0q6wti9KJeN4F6Tw")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        json = response.body();
        //Se busca en el JSON obtenido los diez primeros miembros del subarray de cast, y se trasladan a un array de String con los actores principales.
        jobj = new Gson().fromJson(json, JsonObject.class);
        lista = jobj.getAsJsonArray("cast");
        while(((index < lista.size()-1) && !lista.isEmpty()) && index < 10){
            resultado.add((lista.get(index).getAsJsonObject().get("name").getAsString() + " as " + (lista.get(index).getAsJsonObject().get("character").getAsString())));
            index++;
        }
        return resultado;
    }
    private static String arrangePoster(JsonObject media){
        return "https://image.tmdb.org/t/p/w1280" + media.get("poster_path");
    }
    private static ArrayList<String> arrangeStudios(JsonObject media){
        ArrayList<String> result = new ArrayList<>();
        JsonArray studioList = media.getAsJsonArray("production_companies");
        if(studioList.isEmpty())
            result.add("N/A");
        else {
            for (JsonElement current_studio : studioList) {
                result.add(current_studio.getAsJsonObject().get("name").getAsString());
            }
        }
        return result;
    }
    private static ArrayList<String> arrangeGenres(JsonObject media){
        ArrayList<String> result = new ArrayList<>();
        JsonArray genreList = media.getAsJsonArray("genres");
        if(genreList.isEmpty())
            result.add("N/A");
        else {
            for (JsonElement current_genre : genreList) {
                result.add(current_genre.getAsJsonObject().get("name").getAsString());
            }
        }
        return result;
    }

    public static ArrayList<TVShow> searchTVShow(String tv) throws IOException, InterruptedException {
        ArrayList<TVShow> results = new ArrayList<>();
        String json="", currentID="", currentTV="", studio="";
        JsonObject jobj;
        JsonArray list, studios;
        tv = tv.replace(" ", "%20");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/search/tv?query=" + tv + "&include_adult=false&language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2YTQyNjBjNTA5YTliZGUxMmUwMjUzOWNhN2U3MzliNyIsIm5iZiI6MTcyMTU3MzAwNS4zNjI2OCwic3ViIjoiNWNkMzBmMWI5MjUxNDE3Y2NlMmY2MWNiIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.XUwfebRebvZ31ZqCcgUx9ExHOSS0q6wti9KJeN4F6Tw")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        json = response.body();
        jobj = new Gson().fromJson(json, JsonObject.class);
        list = jobj.getAsJsonArray("results");
        for(JsonElement current : list) {
            TVShow tvShow = new TVShow();
            tvShow.setId(current.getAsJsonObject().get("id").getAsString());
            tvShow.setName(current.getAsJsonObject().get("name").getAsString());
            tvShow.setOverview(current.getAsJsonObject().get("overview").getAsString());
            tvShow.setRelease_date(current.getAsJsonObject().get("first_air_date").getAsString());
            tvShow.setPoster(arrangePoster(current.getAsJsonObject()));
            results.add(tvShow);
        }
        return results;
    }

    public static TVShow getTVShowDetails(String tvShowID) throws IOException, InterruptedException {
        TVShow tvshow = new TVShow();
        JsonObject tvJson;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/tv/+" + tvShowID + "?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2YTQyNjBjNTA5YTliZGUxMmUwMjUzOWNhN2U3MzliNyIsIm5iZiI6MTcyMTU3MzAwNS4zNjI2OCwic3ViIjoiNWNkMzBmMWI5MjUxNDE3Y2NlMmY2MWNiIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.XUwfebRebvZ31ZqCcgUx9ExHOSS0q6wti9KJeN4F6Tw")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        tvJson = new Gson().fromJson(response.body(), JsonObject.class);
        tvshow.setId(tvJson.get("id").getAsString());
        tvshow.setName(tvJson.get("name").getAsString());
        tvshow.setRelease_date(tvJson.get("first_air_date").getAsString());
        tvshow.setNumber_of_episodes(tvJson.get("number_of_episodes").getAsInt());
        tvshow.setNumber_of_seasons(tvJson.get("number_of_seasons").getAsInt());
        tvshow.setCast(getTVCast(tvShowID));
        tvshow.setOverview(tvJson.get("overview").getAsString());
        tvshow.setStudios(arrangeStudios(tvJson));
        tvshow.setGenres(arrangeGenres(tvJson));
        tvshow.setPoster(arrangePoster(tvJson));
        tvshow.setScore(tvJson.get("vote_average").getAsDouble());
        return tvshow;
    }

    /**
     * Este método realiza una búsqueda superficial de películas
     * @param movie Título de la película proporcionado por el usuario
     * @return Un ArrayList de películas obtenidas como resultado de la búsqueda desde la API cuya información está parcialmente completa
     * @throws IOException En caso de que la comunicación con la API falle
     * @throws InterruptedException En caso de que la comunicación con la API falle
     */
    public static ArrayList<Movie> searchMovie(String movie) throws IOException, InterruptedException {
        ArrayList<Movie> results = new ArrayList<>();
        JsonObject movieJson;
        JsonArray movieList;
        movie = movie.replace(" ", "%20");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/search/movie?query=" + movie + "&include_adult=false&language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2YTQyNjBjNTA5YTliZGUxMmUwMjUzOWNhN2U3MzliNyIsIm5iZiI6MTcyMTU3MzAwNS4zNjI2OCwic3ViIjoiNWNkMzBmMWI5MjUxNDE3Y2NlMmY2MWNiIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.XUwfebRebvZ31ZqCcgUx9ExHOSS0q6wti9KJeN4F6Tw")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        movieJson = new Gson().fromJson(response.body(), JsonObject.class);
        movieList = movieJson.getAsJsonArray("results");
        for(JsonElement current : movieList) {
            Movie currentMovie = new Movie();
            currentMovie.setId(current.getAsJsonObject().get("id").getAsString());
            currentMovie.setTitle(current.getAsJsonObject().get("title").getAsString());
            currentMovie.setRelease_date(current.getAsJsonObject().get("release_date").getAsString());
            currentMovie.setOverview(current.getAsJsonObject().get("overview").getAsString());
            currentMovie.setPoster(arrangePoster(current.getAsJsonObject()));
            results.add(currentMovie);
        }
        return results;
    }

    public static Movie getMovieDetails(String movieID) throws IOException, InterruptedException {
        Movie movie = new Movie();
        JsonObject movieJson;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/"+ movieID + "?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2YTQyNjBjNTA5YTliZGUxMmUwMjUzOWNhN2U3MzliNyIsIm5iZiI6MTcyMTU3MzAwNS4zNjI2OCwic3ViIjoiNWNkMzBmMWI5MjUxNDE3Y2NlMmY2MWNiIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.XUwfebRebvZ31ZqCcgUx9ExHOSS0q6wti9KJeN4F6Tw")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        movieJson = new Gson().fromJson(response.body(), JsonObject.class);
        movie.setId(movieJson.get("id").getAsString());
        movie.setTitle(movieJson.get("title").getAsString());
        movie.setRelease_date(movieJson.get("release_date").getAsString());
        movie.setMovieCredits(getMovieCredits(movieID));
        movie.setOverview(movieJson.get("overview").getAsString());
        movie.setGenres(arrangeGenres(movieJson));
        movie.setPoster(arrangePoster(movieJson));
        movie.setStudios(arrangeStudios(movieJson));
        movie.setRuntime(movieJson.get("runtime").getAsInt());
        movie.setScore(movieJson.get("vote_average").getAsDouble());
        return movie;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        /*
        ArrayList<Movie> peliculas = searchMovie("Pulp Fiction");
        for(Movie m : peliculas){
            System.out.println(m.toString());
            System.out.println("--------------------------------------------------------------");
        }
        */

        /*
        Movie m = new Movie(getMovieDetails("680"));
        System.out.println(m.toString());
        */
        /*
        ArrayList<TVShow> series = searchTVShow("Friends");
        for(TVShow tv : series){
            System.out.println(tv.toString());
            System.out.println("--------------------------------------------------------------");
        }
        */
        TVShow tv = new TVShow(getTVShowDetails("1668"));
        System.out.println(tv.toString());
    }
}

