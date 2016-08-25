package no.svitts.core.json.deserializer;

import com.google.gson.*;
import no.svitts.core.date.ReleaseDate;
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
        int runtime = getInt(jsonObject.get("runtime"));
        ReleaseDate releaseDate = getReleaseDate(jsonObject.get("releaseDate"));
        Set<Genre> genres = getGenres(jsonObject.get("genres"));
        VideoFile videoFile = getVideoFile(jsonObject.get("videoFile"));
        String posterPath = getString(jsonObject.get("posterPath"));
        String backdropPath = getString(jsonObject.get("backdropPath"));
        return new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres, videoFile, posterPath, backdropPath);
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
        return isNotNull(jsonObject.get("path")) ? new VideoFile(jsonObject.get("path").getAsString()) : null;
    }

}
