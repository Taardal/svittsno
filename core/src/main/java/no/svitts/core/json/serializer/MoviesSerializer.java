package no.svitts.core.json.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import no.svitts.core.movie.Movie;

import java.lang.reflect.Type;

public class MoviesSerializer implements JsonSerializer<Movie[]> {

    @Override
    public JsonElement serialize(Movie[] movies, Type typeOfSrc, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();
        if (movies != null) {
            for (Movie movie : movies) {
                jsonArray.add(jsonSerializationContext.serialize(movie, Movie.class));
            }
        }
        return jsonArray;
    }

}
