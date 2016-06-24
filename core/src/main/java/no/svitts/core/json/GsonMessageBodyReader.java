package no.svitts.core.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.json.deserializer.MovieDeserializer;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
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
public class GsonMessageBodyReader implements MessageBodyReader<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GsonMessageBodyReader.class);

    private Gson gson;

    public GsonMessageBodyReader() {
        gson = new GsonBuilder().registerTypeAdapter(Movie.class, new MovieDeserializer()).create();
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (mediaType.getType().equals(MediaType.APPLICATION_JSON_TYPE.getType()) || mediaType.getType().equals(MediaType.APPLICATION_JSON)) {
            return true;
        } else {
            LOGGER.warn("Could not read message body due to unsupported media type.");
            return false;
        }
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(entityStream, StandardCharsets.UTF_8)) {
            return gson.fromJson(inputStreamReader, type);
        } catch (IOException e) {
            String errorMessage = "Could not convert JSON to type [" + type + "]";
            LOGGER.error(errorMessage);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

}
