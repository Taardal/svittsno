package no.svitts.core.gson.deserializer;

import com.google.gson.*;
import no.svitts.core.date.KeyDate;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MovieDeserializer extends CoreDeserializer implements JsonDeserializer<Movie> {

    @Override
    public Movie deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = getId(jsonObject);
        String name = jsonObject.get("name").getAsString();
        String imdbId = jsonObject.get("imdbId").getAsString();
        String tagline = jsonObject.get("tagline").getAsString();
        String overview = jsonObject.get("overview").getAsString();
        int runtime = jsonObject.get("runtime").getAsInt();
        KeyDate releaseDate = new KeyDate(jsonObject.get("releaseDate").getAsString());
        List<Genre> genres = getGenres(jsonObject.get("genres").getAsJsonArray());
        return new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres);
    }

    private List<Genre> getGenres(JsonArray jsonArray) {
        List<Genre> genres = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            String genreString = jsonElement.getAsString().toUpperCase();
            genres.add(Genre.valueOf(genreString));
        }
        return genres;
    }

}
