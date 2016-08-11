package no.svitts.core.json.serializer;

import com.google.gson.*;
import no.svitts.core.date.ReleaseDate;
import no.svitts.core.file.VideoFile;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Set;

public class MovieSerializer extends Serializer implements JsonSerializer<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieSerializer.class);

    @Override
    public JsonElement serialize(Movie movie, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        if (movie != null) {
            jsonObject.add("id", getJsonPrimitive(movie.getId()));
            jsonObject.add("title", getJsonPrimitive(movie.getTitle()));
            jsonObject.add("imdbId", getJsonPrimitive(movie.getImdbId()));
            jsonObject.add("tagline", getJsonPrimitive(movie.getTagline()));
            jsonObject.add("overview", getJsonPrimitive(movie.getOverview()));
            jsonObject.add("runtime", getJsonPrimitive(movie.getRuntime()));
            jsonObject.add("releaseDate", getReleaseDateAsJsonPrimitive(movie.getReleaseDate()));
            jsonObject.add("genres", getGenresAsJsonArray(movie.getGenres()));
            jsonObject.add("videoFile", getVideoFileAsJsonObject(movie.getVideoFile()));
            jsonObject.add("posterPath", getJsonPrimitive(movie.getPosterPath()));
            jsonObject.add("backdropPath", getJsonPrimitive(movie.getBackdropPath()));
        }
        return jsonObject;
    }

    private JsonObject getReleaseDateAsJsonPrimitive(ReleaseDate releaseDate) {
        if (releaseDate != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("time", getJsonPrimitive(releaseDate.getTime()));
            return jsonObject;
        } else {
            LOGGER.warn("Could not serialize release date because it was null");
            return null;
        }
    }

    private JsonElement getVideoFileAsJsonObject(VideoFile videoFile) {
        if (videoFile != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("name", getJsonPrimitive(videoFile.getName()));
            jsonObject.add("path", getJsonPrimitive(videoFile.getPath()));
            jsonObject.add("size", getJsonPrimitive(videoFile.getSize()));
            return jsonObject;
        } else {
            LOGGER.warn("Could not serialize media file because it was null.");
            return null;
        }
    }

    private JsonArray getGenresAsJsonArray(Set<Genre> genres) {
        if (genres != null) {
            JsonArray jsonArray = new JsonArray();
            for (Genre genre : genres) {
                jsonArray.add(genre.toString());
            }
            return jsonArray;
        } else {
            LOGGER.warn("Could not serialize genres set because it was null.");
            return null;
        }
    }

}
