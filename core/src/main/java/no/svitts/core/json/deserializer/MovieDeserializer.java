package no.svitts.core.json.deserializer;

import com.google.gson.*;
import no.svitts.core.date.ReleaseDate;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
        List<Genre> genres = getGenres(jsonObject.get("genres"));
        File videoFile = getFile(jsonObject.get("videoFile"));
        File posterImageFile = getFile(jsonObject.get("posterImageFile"));
        File backdropImageFile = getFile(jsonObject.get("backdropImageFile"));
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

    private List<Genre> getGenres(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? getGenres(jsonElement.getAsJsonArray()) : null;
    }

    private List<Genre> getGenres(JsonArray jsonArray) {
        List<Genre> genres = new ArrayList<>();
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
            LOGGER.error("Could not convert genre [" + genreString + "] from string to an appropriate enum.", e);
            // Letting invalid genre pass as null because it should be handled in movie constraint validator.
            return null;
        }
    }

    private File getFile(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? getFile(jsonElement.getAsJsonObject()) : null;
    }

    private File getFile(JsonObject jsonObject) {
        return isNotNull(jsonObject.get("path")) ? new File(jsonObject.get("path").getAsString()) : null;
    }

}
