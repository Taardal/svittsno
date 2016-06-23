package no.svitts.core.json.serializer;

import com.google.gson.*;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class MovieSerializer extends CoreSerializer implements JsonSerializer<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieSerializer.class);

    @Override
    public JsonElement serialize(Movie movie, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        if (movie != null) {
            jsonObject.add("id", getJsonPrimitive(movie.getId()));
            jsonObject.add("name", getJsonPrimitive(movie.getName()));
            jsonObject.add("imdbId", getJsonPrimitive(movie.getImdbId()));
            jsonObject.add("tagline", getJsonPrimitive(movie.getTagline()));
            jsonObject.add("overview", getJsonPrimitive(movie.getOverview()));
            jsonObject.add("runtime", getJsonPrimitive(movie.getRuntime()));
            jsonObject.add("releaseDate", getJsonPrimitive(movie.getReleaseDate()));
            jsonObject.add("genres", getGenresAsJsonArray(movie.getGenres()));
            jsonObject.add("videoFile", getFileAsJsonObject(movie.getVideoFile()));
            jsonObject.add("posterImageFile", getFileAsJsonObject(movie.getPosterImageFile()));
            jsonObject.add("backdropImageFile", getFileAsJsonObject(movie.getBackdropImageFile()));
        }
        return jsonObject;
    }

    private JsonElement getFileAsJsonObject(File file) {
        if (file != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("name", getJsonPrimitive(file.getName()));
            jsonObject.add("path", getJsonPrimitive(file.getPath()));
            jsonObject.add("size", getJsonPrimitive(file.length()));
            return jsonObject;
        } else {
            return null;
        }
    }

    private JsonArray getGenresAsJsonArray(List<Genre> genres) {
        if (genres != null) {
            JsonArray jsonArray = new JsonArray();
            for (Genre genre : genres) {
                jsonArray.add(genre.getValue());
            }
            return jsonArray;
        } else {
            return null;
        }
    }

}
