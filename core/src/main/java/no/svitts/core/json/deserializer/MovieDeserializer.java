package no.svitts.core.json.deserializer;

import com.google.gson.*;
import no.svitts.core.date.ReleaseDate;
import no.svitts.core.file.SubtitleFile;
import no.svitts.core.file.VideoFile;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class MovieDeserializer extends Deserializer implements JsonDeserializer<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieDeserializer.class);

    @Override
    public Movie deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = getString(jsonObject.get("id"));
        String name = getString(jsonObject.get("title"));
        String imdbId = getString(jsonObject.get("imdbId"));
        String tagline = getString(jsonObject.get("tagline"));
        String overview = getString(jsonObject.get("overview"));
        String language = getString(jsonObject.get("language"));
        String edition = getString(jsonObject.get("edition"));
        int runtime = getInt(jsonObject.get("runtime"));
        ReleaseDate releaseDate = getReleaseDate(jsonObject.get("releaseDate"));
        Set<Genre> genres = getGenres(jsonObject.get("genres"));
        VideoFile videoFile = getVideoFile(jsonObject.get("videoFile"));
        Set<SubtitleFile> subtitleFiles = getSubtitleFiles(jsonObject.get("subtitleFiles"));
        String posterPath = getString(jsonObject.get("posterPath"));
        String backdropPath = getString(jsonObject.get("backdropPath"));
        return new Movie(id, name, imdbId, tagline, overview, language, edition, runtime, releaseDate, genres, videoFile, subtitleFiles, posterPath, backdropPath);
    }

    private Set<SubtitleFile> getSubtitleFiles(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? getSubtitleFiles(jsonElement.getAsJsonArray()) : null;
    }

    private Set<SubtitleFile> getSubtitleFiles(JsonArray jsonArray) {
        Set<SubtitleFile> subtitleFiles = new HashSet<>();
        if (jsonArray.size() > 0) {
            for (JsonElement jsonElement : jsonArray) {
                subtitleFiles.add(getSubtitleFile(jsonElement));
            }
        }
        return subtitleFiles;
    }

    private SubtitleFile getSubtitleFile(JsonElement jsonElement) {
        if (isNotNull(jsonElement)) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (isNotNull(jsonObject.get("path"))) {
                String path = getString(jsonObject.get("path"));
                String language = "";
                if (isNotNull(jsonObject.get("language"))) {
                    language = getString(jsonObject.get("language"));
                }
                return new SubtitleFile(path, language);
            }
        }
        return null;
    }

    private ReleaseDate getReleaseDate(JsonElement jsonElement) {
        if (isNotNull(jsonElement)) {
            JsonElement timeJsonElement = jsonElement.getAsJsonObject().get("time");
            if (isNotNull(timeJsonElement)) {
                return new ReleaseDate(getLong(timeJsonElement));
            }
        }
        return null;
    }

    private Set<Genre> getGenres(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? getGenres(jsonElement.getAsJsonArray()) : null;
    }

    private Set<Genre> getGenres(JsonArray jsonArray) {
        Set<Genre> genres = new HashSet<>();
        if (jsonArray.size() > 0) {
            for (JsonElement jsonElement : jsonArray) {
                String genre = jsonElement.getAsString().toUpperCase().replaceAll("-", "_").replaceAll(" ", "_");
                genres.add(Genre.valueOf(genre));
            }
        }
        return genres;
    }

    private VideoFile getVideoFile(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? getVideoFile(jsonElement.getAsJsonObject()) : null;
    }

    private VideoFile getVideoFile(JsonObject jsonObject) {
        if (isNotNull(jsonObject.get("path"))) {
            String path = getString(jsonObject.get("path"));
            String videoFormat = "";
            String audioFormat = "";
            if (isNotNull(jsonObject.get("videoFormat"))) {
                videoFormat = getString(jsonObject.get("videoFormat"));
            }
            if (isNotNull(jsonObject.get("audioFormat"))) {
                audioFormat = getString(jsonObject.get("audioFormat"));
            }
            return new VideoFile(path, videoFormat, audioFormat);
        } else {
            return null;
        }
    }

}
