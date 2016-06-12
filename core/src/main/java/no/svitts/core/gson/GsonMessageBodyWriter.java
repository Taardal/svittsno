package no.svitts.core.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.gson.serializer.MovieSerializer;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class GsonMessageBodyWriter implements MessageBodyWriter<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GsonMessageBodyWriter.class);

    private Gson gson;

    public GsonMessageBodyWriter() {
        gson = new GsonBuilder().registerTypeAdapter(Movie.class, new MovieSerializer()).serializeNulls().create();
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return mediaType.getType().equals(MediaType.APPLICATION_JSON_TYPE.getType());
    }

    @Override
    public long getSize(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(entityStream, StandardCharsets.UTF_8)) {
            gson.toJson(object, genericType, outputStreamWriter);
        } catch (IOException e) {
            String errorMessage = "Could not read JSON from type [" + type + "]";
            LOGGER.error(errorMessage);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }


}
