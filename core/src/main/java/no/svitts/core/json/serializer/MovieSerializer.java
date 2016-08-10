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

public class MovieSerializer extends CoreSerializer implements JsonSerializer<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieSerializer.class);

    @Override
    public JsonElement serialize(Movie movie, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        if (movie != null) {
            jsonObject.add("id", getJsonPrimitive(movie.getId()));
            jsonObject.add("name", getJsonPrimitive(movie.getTitle()));
            jsonObject.add("imdb_id", getJsonPrimitive(movie.getImdbId()));
            jsonObject.add("tagline", getJsonPrimitive(movie.getTagline()));
            jsonObject.add("overview", getJsonPrimitive(movie.getOverview()));
            jsonObject.add("runtime", getJsonPrimitive(movie.getRuntime()));
            jsonObject.add("release_date", getReleaseDateJsonPrimitive(movie.getReleaseDate()));
            jsonObject.add("genres", getGenresAsJsonArray(movie.getGenres()));
            jsonObject.add("video_file", getFileAsJsonObject(movie.getVideoFile()));
            jsonObject.add("poster_path", getJsonPrimitive(movie.getPosterPath()));
            jsonObject.add("backdrop_path", getJsonPrimitive(movie.getBackdropPath()));
        }
        return jsonObject;
    }

    private JsonPrimitive getReleaseDateJsonPrimitive(ReleaseDate releaseDate) {
        if (releaseDate != null) {
            return new JsonPrimitive(releaseDate.toString());
        } else {
            LOGGER.warn("Could not serialize release date because it was null");
            return null;
        }
    }

    private JsonElement getFileAsJsonObject(VideoFile videoFile) {
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
