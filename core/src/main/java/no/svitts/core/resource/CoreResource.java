package no.svitts.core.resource;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.gson.deserializer.MovieDeserializer;
import no.svitts.core.gson.serializer.MovieSerializer;
import no.svitts.core.movie.Movie;

public abstract class CoreResource {

    protected Gson gson;

    CoreResource() {
        gson = getGson();
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Movie.class, new MovieSerializer())
                .registerTypeAdapter(Movie.class, new MovieDeserializer())
                .create();
    }
}
