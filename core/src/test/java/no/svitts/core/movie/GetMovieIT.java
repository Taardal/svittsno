package no.svitts.core.movie;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static no.svitts.core.CoreTestKit.getITestApplication;
import static no.svitts.core.util.StringUtil.getRandomString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GetMovieIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private MovieBuilder movieBuilder;
    private GsonMessageBodyReader gsonMessageBodyReader;
    private GsonMessageBodyWriter gsonMessageBodyWriter;

    @Override
    protected Application configure() {
        return getITestApplication();
    }

    @Override
    @Before
    public void setUp() throws Exception {
        movieBuilder = new MovieBuilder();
        gsonMessageBodyReader = new GsonMessageBodyReader();
        gsonMessageBodyWriter = new GsonMessageBodyWriter();
        super.setUp();
    }

    @Test
    public void getMovie_IdContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("#").request().get();
        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void getMovie_IdIsTooLong_ShouldReturnBadRequestResponse() {
        String id = getRandomString(Movie.ID_MAX_LENGTH + 1);
        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(id).request().get();
        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void getMovie_MovieExistsAndValidId_ShouldReturnExpectedMovie() {
        client().register(gsonMessageBodyReader).register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.build();
        createMovie(movie);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(movie.getId()).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(movie, response.readEntity(Movie.class));
        response.close();
    }

    @Test
    public void getMovie_MovieDoesNotExist_ShouldReturnOkResponseWithNoContent() {
        client().register(gsonMessageBodyReader);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(getRandomString(10)).request().get();

        assertEquals(200, response.getStatus());
        assertNull(response.readEntity(Movie.class));
        response.close();
    }

    private void createMovie(Movie movie) {
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);
        assertEquals(201, response.getStatus());
        response.close();
    }

}
