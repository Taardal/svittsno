package no.svitts.core.resource;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.error.ClientErrorMessage;
import no.svitts.core.gson.deserializer.MovieDeserializer;
import no.svitts.core.gson.serializer.MovieSerializer;
import no.svitts.core.movie.Movie;

import javax.ws.rs.core.Response;

public abstract class CoreResource {

    protected Gson gson;

    CoreResource() {
        gson = getGson();
    }

    Response getClientErrorResponse(Response.Status status, String message) {
        ClientErrorMessage clientErrorMessage = new ClientErrorMessage(status, message);
        return Response.status(clientErrorMessage.getStatus()).entity(clientErrorMessage).build();
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Movie.class, new MovieSerializer())
                .registerTypeAdapter(Movie.class, new MovieDeserializer())
                .create();
    }
}
