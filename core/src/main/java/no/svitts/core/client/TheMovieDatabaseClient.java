package no.svitts.core.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.json.deserializer.MovieDeserializer;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class TheMovieDatabaseClient {

    private static final String API_BASE_URL = "http://api.themoviedb.org/3/";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String API_KEY = "b041b0681fa9947874d41095ea1ca5ae";
    private static final Logger LOGGER = LoggerFactory.getLogger(TheMovieDatabaseClient.class);

    private final Client client;
    private final Gson gson;

    public TheMovieDatabaseClient() {
        client = ClientBuilder.newClient();
        gson = getGson();
    }

    private Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(Movie.class, new MovieDeserializer()).create();
    }

    public Movie getMovie(String imdbId) {
        LOGGER.info("Getting movie with ID [{}] from The Movie Database", imdbId);
        Response response = client.target(API_BASE_URL).path("movies").path(imdbId).queryParam("api_key", API_KEY).request().get();
        Movie movie = gson.fromJson(response.readEntity(String.class), Movie.class);
        response.close();
        return movie;
    }

}
