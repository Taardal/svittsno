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

public class MovieDeserializer extends CoreDeserializer implements JsonDeserializer<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieDeserializer.class);

    @Override
    public Movie deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = getString(jsonObject.get("id"));
        String name = getString(jsonObject.get("name"));
        String imdbId = getString(jsonObject.get("imdb_id"));
        String tagline = getString(jsonObject.get("tagline"));
        String overview = getString(jsonObject.get("overview"));
        int runtime = getInt(jsonObject.get("runtime"));
        ReleaseDate releaseDate = getReleaseDate(jsonObject.get("release_date"));
        Set<Genre> genres = getGenres(jsonObject.get("genres"));
        VideoFile videoFile = getVideoFile(jsonObject.get("video_file"));
        String posterPath = getString(jsonObject.get("poster_path"));
        String backdropPath = getString(jsonObject.get("backdrop_path"));
        return new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres, videoFile, posterPath, backdropPath);
    }

    private ReleaseDate getReleaseDate(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? ReleaseDate.fromString(getString(jsonElement)) : null;
    }

    private Set<Genre> getGenres(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? getGenres(jsonElement.getAsJsonArray()) : null;
    }

    private Set<Genre> getGenres(JsonArray jsonArray) {
        Set<Genre> genres = new HashSet<>();
        if (jsonArray.size() > 0) {
            for (JsonElement jsonElement : jsonArray) {
                String genre = getGenre(jsonElement).toUpperCase().replaceAll("-", "_").replaceAll(" ", "_");
                genres.add(Genre.valueOf(genre));
            }
        }
        return genres;
    }

    private String getGenre(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject().get("name").getAsString();
        } else {
            return jsonElement.getAsString();
        }
    }

    private VideoFile getVideoFile(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? getVideoFile(jsonElement.getAsJsonObject()) : null;
    }

    private VideoFile getVideoFile(JsonObject jsonObject) {
        return isNotNull(jsonObject.get("path")) ? new VideoFile(jsonObject.get("path").getAsString()) : null;
    }

}
