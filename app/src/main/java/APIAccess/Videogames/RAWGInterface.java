package APIAccess.Videogames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import APIAccess.Content;

public class RAWGInterface {

    private static final String API_KEY = "b3bbc9668e8c4b779eabf907b675f517";

    public static ArrayList<Content> searchVideogame(String name) throws IOException, JSONException {
        name = name.replace(" ", "%20");
        ArrayList<Content> result = new ArrayList<>();
        ArrayList<String> currentGamePlatforms;
        JSONObject jsonResponse, game, platformObject, platform;
        JSONArray games, platforms;
        String busqueda = "https://api.rawg.io/api/games?key=" + API_KEY + "&search=" + name;
        HttpURLConnection conn = (HttpURLConnection) new URL(busqueda).openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            jsonResponse = new JSONObject(response.toString());
            games = jsonResponse.getJSONArray("results");
            for (int i = 0; i < games.length(); i++) {
                game = games.getJSONObject(i);
                Videogame v = new Videogame();
                v.setId(String.valueOf(game.getInt("id")));
                v.setTitle(game.getString("name"));
                v.setRelease_date(game.optString("released"));
                v.setPoster(game.optString("background_image"));
                v.setScore(game.optInt("metacritic"));
                currentGamePlatforms = new ArrayList<>();
                platforms = game.optJSONArray("platforms");
                if (platforms != null) {
                    for (int j = 0; j < platforms.length(); j++) {
                        platformObject = platforms.optJSONObject(j);
                        if (platformObject != null) {
                            platform = platformObject.optJSONObject("platform");
                            if (platform != null) {
                                String platformName = platform.optString("name", "Unknown");
                                currentGamePlatforms.add(platformName);
                            }
                        }
                    }
                }
                v.setPlatforms(currentGamePlatforms);
                result.add(v);
            }
        } else {
            System.out.println("Error en la solicitud: " + conn.getResponseCode());
        }
        return result;
    }

    public static Videogame getVideogameDetails(String id) throws IOException, JSONException {
        Videogame result = new Videogame();
        ArrayList<String> currentGamePlatforms, currentGameDevelopers, currentGameGenres;
        JSONObject game, platformObject, platform, developerObject, genreObject;
        JSONArray platforms, developers, genres;
        String busqueda = "https://api.rawg.io/api/games/"+ id + "?key=" + API_KEY;
        System.out.println(busqueda);
        HttpURLConnection conn = (HttpURLConnection) new URL(busqueda).openConnection();
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            game = new JSONObject(response.toString());
            result.setId(id);
            result.setTitle(game.getString("name"));
            result.setRelease_date(game.optString("released"));
            result.setPoster(game.optString("background_image"));
            result.setScore(game.optInt("metacritic"));
            result.setOverview(game.optString("description"));
            currentGamePlatforms = new ArrayList<>();
            platforms = game.optJSONArray("platforms");
            if (platforms != null) {
                for (int j = 0; j < platforms.length(); j++) {
                    platformObject = platforms.optJSONObject(j);
                    if (platformObject != null) {
                        platform = platformObject.optJSONObject("platform");
                        if (platform != null) {
                            String platformName = platform.optString("name", null);
                            currentGamePlatforms.add(platformName);
                        }
                    }
                }
            }
            result.setPlatforms(currentGamePlatforms);
            currentGameDevelopers = new ArrayList<>();
            developers = game.optJSONArray("developers");
            if (developers != null) {
                for (int j = 0; j < developers.length(); j++) {
                    developerObject = developers.optJSONObject(j);
                    if (developerObject != null) {
                        String developerName = developerObject.optString("name", null);
                        currentGameDevelopers.add(developerName);
                    }
                }
            }
            result.setDevelopers(currentGameDevelopers);
            currentGameGenres = new ArrayList<>();
            genres = game.optJSONArray("genres");
            if (genres != null) {
                for (int j = 0; j < genres.length(); j++) {
                    genreObject = genres.optJSONObject(j);
                    if (genreObject != null) {
                        String genreName = genreObject.optString("name", null);
                        currentGameGenres.add(genreName);
                    }
                }
            }
            result.setGenres(currentGameGenres);
        }

        return result;
    }

    public static void main(String[] args) throws IOException, JSONException {
        /*
        ArrayList<Videogame> busqueda = new ArrayList<>();
        busqueda = searchVideogame("The Witcher 3");
        for(Videogame current : busqueda){
            System.out.println(current.toString());
            System.out.println("-------------------------------------------------------------------------------");
        }*/
        Videogame v = getVideogameDetails("3328");
        System.out.println(v.toString());
    }
}
