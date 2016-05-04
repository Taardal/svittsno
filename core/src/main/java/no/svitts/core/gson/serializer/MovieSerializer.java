package no.svitts.core.gson.serializer;

import com.google.gson.*;
import no.svitts.core.date.KeyDate;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;

import java.lang.reflect.Type;
import java.util.List;

public class MovieSerializer implements JsonSerializer<Movie> {

    @Override
    public JsonElement serialize(Movie movie, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", getJsonPrimitive(movie.getId()));
        jsonObject.add("name", getJsonPrimitive(movie.getName()));
        jsonObject.add("imdbId", getJsonPrimitive(movie.getImdbId()));
        jsonObject.add("tagline", getJsonPrimitive(movie.getTagline()));
        jsonObject.add("overview", getJsonPrimitive(movie.getOverview()));
        jsonObject.add("runtime", getJsonPrimitive(movie.getRuntime()));
        jsonObject.add("releaseDate", getJsonPrimitive(movie.getReleaseDate()));
        jsonObject.add("genres", getGenresAsJsonArray(movie.getGenres()));
        return jsonObject;
    }

    private JsonPrimitive getJsonPrimitive(String string) {
        return string != null ? new JsonPrimitive(string) : new JsonPrimitive("null");
    }

    private JsonPrimitive getJsonPrimitive(int integer) {
        return integer >= 0 ? new JsonPrimitive(integer) : new JsonPrimitive(0);
    }

    private JsonPrimitive getJsonPrimitive(KeyDate keyDate) {
        return keyDate != null ? getJsonPrimitive(keyDate.toString()) : new JsonPrimitive("null");
    }

    private JsonArray getGenresAsJsonArray(List<Genre> genres) {
        JsonArray jsonArray = new JsonArray();
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                jsonArray.add(genre.getValue());
            }
        }
        return jsonArray;
    }

}
