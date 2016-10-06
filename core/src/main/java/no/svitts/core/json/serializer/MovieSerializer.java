package no.svitts.core.json.serializer;

import com.google.gson.*;
import no.svitts.core.date.ReleaseDate;
import no.svitts.core.file.SubtitleFile;
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
            jsonObject.add("language", getJsonPrimitive(movie.getLanguage()));
            jsonObject.add("edition", getJsonPrimitive(movie.getEdition()));
            jsonObject.add("runtime", getJsonPrimitive(movie.getRuntime()));
            jsonObject.add("releaseDate", getReleaseDateAsJsonObject(movie.getReleaseDate()));
            jsonObject.add("genres", getGenresAsJsonArray(movie.getGenres()));
            jsonObject.add("videoFile", getVideoFileAsJsonObject(movie.getVideoFile()));
            jsonObject.add("subtitleFiles", getSubtitleFilesAsJsonArray(movie.getSubtitleFiles()));
            jsonObject.add("posterPath", getJsonPrimitive(movie.getPosterPath()));
            jsonObject.add("backdropPath", getJsonPrimitive(movie.getBackdropPath()));
        }
        return jsonObject;
    }

    private JsonObject getReleaseDateAsJsonObject(ReleaseDate releaseDate) {
        if (releaseDate != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("time", getJsonPrimitive(releaseDate.getTime()));
            return jsonObject;
        } else {
            LOGGER.warn("Could not serialize release date because it was null");
            return null;
        }
    }

    private JsonArray getGenresAsJsonArray(Set<Genre> genres) {
        if (genres != null) {
            JsonArray jsonArray = new JsonArray();
            for (Genre genre : genres) {
                jsonArray.add(genre.getValue());
            }
            return jsonArray;
        } else {
            LOGGER.warn("Could not serialize genres set because it was null.");
            return null;
        }
    }

    private JsonElement getVideoFileAsJsonObject(VideoFile videoFile) {
        if (videoFile != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("name", getJsonPrimitive(videoFile.getName()));
            jsonObject.add("path", getJsonPrimitive(videoFile.getPath()));
            jsonObject.add("size", getJsonPrimitive(videoFile.getSize()));
            jsonObject.add("videoFormat", getJsonPrimitive(videoFile.getVideoFormat()));
            jsonObject.add("audioFormat", getJsonPrimitive(videoFile.getAudioFormat()));
            return jsonObject;
        } else {
            LOGGER.warn("Could not serialize video file because it was null.");
            return null;
        }
    }

    private JsonElement getSubtitleFilesAsJsonArray(Set<SubtitleFile> subtitleFiles) {
        if (subtitleFiles != null) {
            JsonArray jsonArray = new JsonArray();
            for (SubtitleFile subtitleFile : subtitleFiles) {
                jsonArray.add(getSubtitleFileAsJsonObject(subtitleFile));
            }
            return jsonArray;
        } else {
            LOGGER.warn("Could not serialize subtitle file set because it was null.");
            return null;
        }
    }

    private JsonElement getSubtitleFileAsJsonObject(SubtitleFile subtitleFile) {
        if (subtitleFile != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("name", getJsonPrimitive(subtitleFile.getName()));
            jsonObject.add("path", getJsonPrimitive(subtitleFile.getPath()));
            jsonObject.add("language", getJsonPrimitive(subtitleFile.getLanguage()));
            return jsonObject;
        } else {
            LOGGER.warn("Could not serialize subtitle file because it was null.");
            return null;
        }
    }

}
