package no.svitts.core.json.deserializer;

import com.google.gson.*;
import no.svitts.core.date.ReleaseDate;
import no.svitts.core.file.SubtitleFile;
import no.svitts.core.file.VideoFile;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class MovieDeserializer extends Deserializer implements JsonDeserializer<Movie> {

    @Override
    public Movie deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Movie movie = new Movie(getString(jsonObject.get("id")));
        movie.setTitle(getString(jsonObject.get("title")));
        movie.setImdbId(getString(jsonObject.get("imdbId")));
        movie.setTagline(getString(jsonObject.get("tagline")));
        movie.setOverview(getString(jsonObject.get("overview")));
        movie.setLanguage(getString(jsonObject.get("language")));
        movie.setEdition(getString(jsonObject.get("edition")));
        movie.setRuntime(getInt(jsonObject.get("runtime")));
        movie.setReleaseDate(getReleaseDate(jsonObject.get("releaseDate")));
        movie.setGenres(getGenres(jsonObject.get("genres")));
        movie.setVideoFile(getVideoFile(jsonObject.get("videoFile")));
        movie.setSubtitleFiles(getSubtitleFiles(jsonObject.get("subtitleFiles")));
        movie.setPosterPath(getString(jsonObject.get("posterPath")));
        movie.setBackdropPath(getString(jsonObject.get("backdropPath")));
        return movie;
    }

    private ReleaseDate getReleaseDate(JsonElement jsonElement) {
        if (isNotNull(jsonElement)) {
            if (jsonElement.isJsonObject()) {
                JsonElement timeJsonElement = jsonElement.getAsJsonObject().get("time");
                if (isNotNull(timeJsonElement)) {
                    return new ReleaseDate(getLong(timeJsonElement));
                }
            } else {
                return ReleaseDate.fromString(jsonElement.getAsString());
            }
        }
        return null;
    }

    private Set<Genre> getGenres(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? getGenres(jsonElement.getAsJsonArray()) : null;
    }

    private Set<Genre> getGenres(JsonArray jsonArray) {
        Set<Genre> genres = new HashSet<>();
        for (JsonElement jsonElement : jsonArray) {
            String genre = jsonElement.getAsString().toUpperCase().replaceAll("-", "_").replaceAll(" ", "_");
            genres.add(Genre.valueOf(genre));
        }
        return genres;
    }

    private VideoFile getVideoFile(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? getVideoFile(jsonElement.getAsJsonObject()) : null;
    }

    private VideoFile getVideoFile(JsonObject jsonObject) {
        if (isNotNull(jsonObject.get("path"))) {
            String path = getString(jsonObject.get("path"));
            String videoFormat = getString(jsonObject.get("videoFormat"));
            String audioFormat = getString(jsonObject.get("audioFormat"));
            return new VideoFile(path, videoFormat, audioFormat);
        } else {
            return null;
        }
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
                String language = getString(jsonObject.get("language"));
                return new SubtitleFile(path, language);
            }
        }
        return null;
    }

}
