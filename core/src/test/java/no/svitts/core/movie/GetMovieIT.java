package no.svitts.core.movie;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static no.svitts.core.CoreTestKit.getITestApplication;
import static no.svitts.core.util.StringUtil.getRandomString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GetMovieIT extends JerseyTest {

    private static final String RESOURCE = "movies";

    private MovieBuilder movieBuilder;

    @Override
    protected Application configure() {
        return getITestApplication();
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(new GsonMessageBodyWriter()).register(new GsonMessageBodyReader());
        super.configureClient(config);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        movieBuilder = new MovieBuilder();
        super.setUp();
    }

    @Test
    public void getMovie_IdContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        Response response = client().target(getBaseUri()).path(RESOURCE).path("#").request().get();
        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void getMovie_IdIsTooLong_ShouldReturnBadRequestResponse() {
        String id = getRandomString(Movie.ID_MAX_LENGTH + 1);
        Response response = client().target(getBaseUri()).path(RESOURCE).path(id).request().get();
        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void getMovie_MovieExistsAndValidId_ShouldReturnExpectedMovie() {
        Movie movie = movieBuilder.testMovie().build();
        URI savedMovieURI = saveMovie(movie);

        Response response = client().target(savedMovieURI).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(movie, response.readEntity(Movie.class));
        response.close();
    }

    @Test
    public void getMovie_MovieDoesNotExist_ShouldReturnOkResponseWithNoContent() {
        Response response = client().target(getBaseUri()).path(RESOURCE).path(getRandomString(10)).request().get();

        assertEquals(200, response.getStatus());
        assertNull(response.readEntity(Movie.class));
        response.close();
    }

    private URI saveMovie(Movie movie) {
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
        Response response = client().target(getBaseUri()).path(RESOURCE).request().post(movieEntity, Response.class);
        assertEquals(201, response.getStatus());
        URI savedMovieURI = response.getLocation();
        response.close();
        return savedMovieURI;
    }

}
