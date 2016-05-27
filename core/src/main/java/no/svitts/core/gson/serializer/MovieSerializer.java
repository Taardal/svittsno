package no.svitts.core.gson.serializer;

import com.google.gson.*;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class MovieSerializer extends CoreSerializer implements JsonSerializer<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieSerializer.class);

    @Override
    public JsonElement serialize(Movie movie, Type type, JsonSerializationContext jsonSerializationContext) {
        if (movie != null) {
            JsonObject jsonObject = new JsonObject();
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
            return jsonObject;
        } else {
            String errorMessage = "Could not serialize movie because the object was null";
            LOGGER.error(errorMessage);
            throw new InternalServerErrorException(errorMessage);
        }
    }

    private JsonElement getFileAsJsonObject(File file) {
        JsonObject jsonObject = new JsonObject();
        if (file != null) {
            jsonObject.add("name", getJsonPrimitive(file.getName()));
            jsonObject.add("path", getJsonPrimitive(file.getPath()));
            jsonObject.add("size", getJsonPrimitive(file.length()));
        } else {
            LOGGER.warn("Could not serialize file because the object was null");
        }
        return jsonObject;
    }

    private JsonArray getGenresAsJsonArray(List<Genre> genres) {
        JsonArray jsonArray = new JsonArray();
        if (genres != null) {
            for (Genre genre : genres) {
                jsonArray.add(genre.getValue());
            }
        }
        return jsonArray;
    }

}
