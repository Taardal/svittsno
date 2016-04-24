package no.svitts.core.gson.serializer;

import com.google.gson.*;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;

import java.lang.reflect.Type;
import java.util.List;

public class MovieSerializer implements JsonSerializer<Movie> {

    @Override
    public JsonElement serialize(Movie movie, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(movie.getId()));
        jsonObject.add("name", new JsonPrimitive(movie.getName()));
        jsonObject.add("imdbId", new JsonPrimitive(movie.getImdbId()));
        jsonObject.add("tagline", new JsonPrimitive(movie.getTagline()));
        jsonObject.add("overview", new JsonPrimitive(movie.getOverview()));
        jsonObject.add("runtime", new JsonPrimitive(movie.getRuntime()));
        jsonObject.add("releaseDate", new JsonPrimitive(movie.getReleaseDate().toString()));
        jsonObject.add("genres", getGenresAsJsonArray(movie.getGenres()));
        return jsonObject;
    }

    private JsonArray getGenresAsJsonArray(List<Genre> genres) {
        JsonArray jsonArray = new JsonArray();
        for (Genre genre : genres) {
            jsonArray.add(genre.getValue());
        }
        return jsonArray;
    }

}
