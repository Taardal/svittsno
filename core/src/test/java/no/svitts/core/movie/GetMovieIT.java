package no.svitts.core.movie;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.exception.mapper.ConstraintViolationExceptionMapper;
import no.svitts.core.exception.mapper.WebApplicationExceptionMapper;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import no.svitts.core.module.PersistenceModule;
import no.svitts.core.module.WebModule;
import no.svitts.core.provider.SessionFactoryProvider;
import no.svitts.core.resource.MovieResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class GetMovieIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private MovieBuilder movieBuilder;

    @Override
    protected Application configure() {
        Provider<SessionFactory> sessionFactoryProvider = new SessionFactoryProvider(SessionFactoryProvider.HIBERNATE_ITEST_PROPERTIES);
        Injector injector = Guice.createInjector(new WebModule(), new PersistenceModule(sessionFactoryProvider));
        return getResourceConfig(injector);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        movieBuilder = new MovieBuilder();
        super.setUp();
    }

    @Test
    public void getMovieById_MovieDoesNotExist_ShouldReturnNotFoundResponse() throws IOException {
        client().register(new GsonMessageBodyReader()).register(new GsonMessageBodyWriter());
        Path tempFilePath = Files.createTempFile("svitts_tmp", ".txt");
        Movie movie = movieBuilder.videoFile(tempFilePath).posterImageFile(tempFilePath).backdropImageFile(tempFilePath).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response createMovieResponse = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);
        assertEquals(201, createMovieResponse.getStatus());
        createMovieResponse.close();
        Files.delete(tempFilePath);

//        Response getMovieResponse = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(movie.getId()).request().get();
//        assertEquals(200, getMovieResponse.getStatus());
//        assertEquals(movie, getMovieResponse.readEntity(Movie.class));
//        getMovieResponse.close();
    }

    private Application getResourceConfig(Injector injector) {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(injector.getInstance(MovieResource.class));
        resourceConfig.register(injector.getInstance(WebApplicationExceptionMapper.class));
        resourceConfig.register(injector.getInstance(ConstraintViolationExceptionMapper.class));
        resourceConfig.register(injector.getInstance(GsonMessageBodyReader.class));
        resourceConfig.register(injector.getInstance(GsonMessageBodyWriter.class));
        return resourceConfig;
    }


}
