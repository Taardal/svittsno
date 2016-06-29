package no.svitts.core.movie;

import com.google.inject.Guice;
import com.google.inject.Injector;
import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.exception.mapper.ConstraintViolationExceptionMapper;
import no.svitts.core.exception.mapper.WebApplicationExceptionMapper;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import no.svitts.core.module.PersistenceModule;
import no.svitts.core.module.ResourceModule;
import no.svitts.core.provider.ItestSessionFactoryProvider;
import no.svitts.core.resource.MovieResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static no.svitts.core.testkit.MovieTestKit.assertMovie;
import static org.junit.Assert.assertEquals;

public class GetMovieIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private MovieBuilder movieBuilder;

    @Override
    protected Application configure() {
        Injector injector = Guice.createInjector(new ResourceModule(), new PersistenceModule(new ItestSessionFactoryProvider()));
        MovieResource movieResource = injector.getInstance(MovieResource.class);

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(movieResource);
        resourceConfig.register(new WebApplicationExceptionMapper());
        resourceConfig.register(new ConstraintViolationExceptionMapper());
        resourceConfig.register(new GsonMessageBodyReader());
        resourceConfig.register(new GsonMessageBodyWriter());
        return resourceConfig;
    }

    @Override
    @Before
    public void setUp() throws Exception {
        movieBuilder = new MovieBuilder();
        super.setUp();
    }

    @Test
    public void getMovieById_MovieDoesNotExist_ShouldReturnNotFoundResponse() {
        client().register(new GsonMessageBodyReader()).register(new GsonMessageBodyWriter());
        Movie movie = movieBuilder.build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response createMovieResponse = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);
        assertEquals(201, createMovieResponse.getStatus());
        createMovieResponse.close();

        Response getMovieResponse = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(movie.getId()).request().get();
        assertEquals(200, getMovieResponse.getStatus());
        assertMovie(movie, getMovieResponse.readEntity(Movie.class));
        getMovieResponse.close();
    }



}
