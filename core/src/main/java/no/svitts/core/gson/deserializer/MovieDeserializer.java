package no.svitts.core.gson.deserializer;

import com.google.gson.*;
import no.svitts.core.date.KeyDate;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MovieDeserializer extends CoreDeserializer implements JsonDeserializer<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieDeserializer.class);

    @Override
    public Movie deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = getId(jsonObject.get("id"));
        String name = jsonObject.get("name").getAsString();
        String imdbId = jsonObject.get("imdbId").getAsString();
        String tagline = jsonObject.get("tagline").getAsString();
        String overview = jsonObject.get("overview").getAsString();
        int runtime = jsonObject.get("runtime").getAsInt();
        KeyDate releaseDate = getKeyDate(jsonObject.get("releaseDate").getAsString());
        List<Genre> genres = getGenres(jsonObject.get("genres").getAsJsonArray());
        File videoFile = getFile(jsonObject.get("videoFile").getAsJsonObject());
        File posterImageFile = getFile(jsonObject.get("posterImageFile").getAsJsonObject());
        File backdropImageFile = getFile(jsonObject.get("backdropImageFile").getAsJsonObject());
        return new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres, videoFile, posterImageFile, backdropImageFile);
    }

    private KeyDate getKeyDate(String releaseDate) {
        return isNull(releaseDate) ? null : new KeyDate(releaseDate);
    }

    private List<Genre> getGenres(JsonArray genresJsonArray) {
        List<Genre> genres = new ArrayList<>();
        for (JsonElement genreJsonElement : genresJsonArray) {
            genres.add(Genre.valueOf(genreJsonElement.getAsString().toUpperCase().replaceAll("-", "_")));
        }
        return genres;
    }

    private File getFile(JsonObject fileJsonObject) {
        if (!isNull(fileJsonObject)) {
            return new File(fileJsonObject.get("path").getAsString());
        } else {
            String errorMessage = "Could not deserialize file.";
            LOGGER.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
    }

}
