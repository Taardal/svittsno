package no.svitts.core.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.gson.deserializer.MovieDeserializer;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class GsonMessageBodyReader implements MessageBodyReader<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GsonMessageBodyReader.class);

    private Gson gson;

    public GsonMessageBodyReader() {
        gson = new GsonBuilder().registerTypeAdapter(Movie.class, new MovieDeserializer()).create();
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Movie.class && mediaType.getType().equals(MediaType.APPLICATION_JSON_TYPE.getType());
    }

    @Override
    public Movie readFrom(Class<Movie> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(entityStream, StandardCharsets.UTF_8)) {
            return gson.fromJson(inputStreamReader, genericType);
        } catch (IOException e) {
            String errorMessage = "Could not convert JSON to movie";
            LOGGER.error(errorMessage);
            throw new WebApplicationException(errorMessage, e);
        }
    }


}
