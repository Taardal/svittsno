package no.svitts.core.json.deserializer;

import com.google.gson.*;
import no.svitts.core.date.ReleaseDate;
import no.svitts.core.file.MediaFile;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class MovieDeserializer extends CoreDeserializer implements JsonDeserializer<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieDeserializer.class);

    @Override
    public Movie deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = getString(jsonObject.get("id"));
        String name = getString(jsonObject.get("name"));
        String imdbId = getString(jsonObject.get("imdbId"));
        String tagline = getString(jsonObject.get("tagline"));
        String overview = getString(jsonObject.get("overview"));
        int runtime = getInt(jsonObject.get("runtime"));
        ReleaseDate releaseDate = getKeyDate(jsonObject.get("releaseDate"));
        Set<Genre> genres = getGenres(jsonObject.get("genres"));
        MediaFile videoFile = getFile(jsonObject.get("videoFile"));
        MediaFile posterImageFile = getFile(jsonObject.get("posterImageFile"));
        MediaFile backdropImageFile = getFile(jsonObject.get("backdropImageFile"));
        return new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres, videoFile, posterImageFile, backdropImageFile);
    }

    private String getString(JsonElement jsonElement) {
        return isNotNull(jsonElement) && !jsonElement.getAsString().equals("null") ? jsonElement.getAsString() : null;
    }

    private int getInt(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? jsonElement.getAsInt() : 0;
    }

    private ReleaseDate getKeyDate(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? ReleaseDate.fromString(getString(jsonElement)) : null;
    }

    private Set<Genre> getGenres(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? getGenres(jsonElement.getAsJsonArray()) : null;
    }

    private Set<Genre> getGenres(JsonArray jsonArray) {
        Set<Genre> genres = new HashSet<>();
        for (JsonElement jsonElement : jsonArray) {
            String genreString = jsonElement.getAsString().toUpperCase().replaceAll("-", "_");
            genres.add(getGenre(genreString));
        }
        return genres;
    }

    private Genre getGenre(String genreString) {
        try {
            return Genre.valueOf(genreString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Could not convert genre [{}] from string to an appropriate enum.", genreString, e);
            throw e;
        }
    }

    private MediaFile getFile(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? getFile(jsonElement.getAsJsonObject()) : null;
    }

    private MediaFile getFile(JsonObject jsonObject) {
        return isNotNull(jsonObject.get("path")) ? new MediaFile(jsonObject.get("path").getAsString()) : null;
    }

}
