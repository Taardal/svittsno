package no.svitts.core.resource;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.file.VideoFile;
import no.svitts.core.gson.deserializer.MovieDeserializer;
import no.svitts.core.gson.serializer.MovieSerializer;
import no.svitts.core.gson.serializer.VideoFileSerializer;
import no.svitts.core.movie.Movie;

import javax.ws.rs.core.Response;

public abstract class CoreResource {

    protected Gson gson;

    protected CoreResource() {
        gson = getGson();
    }

    protected Response getResponse(boolean success) {
        return success ?  Response.ok().build() : Response.serverError().build();
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Movie.class, new MovieSerializer())
                .registerTypeAdapter(Movie.class, new MovieDeserializer())
                .registerTypeAdapter(VideoFile.class, new VideoFileSerializer())
                .create();
    }
}
