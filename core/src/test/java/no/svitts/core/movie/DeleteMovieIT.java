package no.svitts.core.movie;

import com.google.gson.Gson;
import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.id.Id;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.resource.MovieResource;
import no.svitts.core.service.MovieService;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static no.svitts.core.testkit.ITestKit.getDataSource;
import static no.svitts.core.testkit.JerseyTestKit.getResourceConfig;
import static no.svitts.core.testkit.MovieTestKit.getGson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeleteMovieIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";
    private static final int ID_MAX_LENGTH = 255;
    private static final int NAME_MAX_LENGTH = 255;
    private static final int IMDB_ID_MAX_LENGTH = 255;
    private static final int TAGLINE_MAX_LENGTH = 255;
    private static final int OVERVIEW_MAX_LENGTH = 510;

    private DataSource dataSource;
    private Gson gson;
    private MovieBuilder movieBuilder;

    @Override
    protected Application configure() {
        dataSource = getDataSource();
        return getResourceConfig(new MovieResource(new MovieService(new MovieRepository(dataSource))));
    }

    @Override
    @Before
    public void setUp() throws Exception {
        gson = getGson();
        movieBuilder = new MovieBuilder();
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        dataSource.close();
        super.tearDown();
    }

    @Test
    public void deleteMovie_MovieDoesNotExist_ShouldAbortAndReturnServerError() {
        Response response = target(MOVIE_RESOURCE).path("delete").path(Id.get()).request().delete();
        assertEquals(500, response.getStatus());
        response.close();
    }

    private void createMovie(Movie movie) {
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);
        Response response = target(MOVIE_RESOURCE).path("create").request().post(movieJsonEntity);
        assertEquals(200, response.getStatus());
        assertMovieExists(movie);
        response.close();
    }

    private void assertMovieExists(Movie movie) {
        Response response = target(MOVIE_RESOURCE).path(movie.getId()).request().get();
        assertMovie(movie, gson.fromJson(response.readEntity(String.class), Movie.class));
        response.close();
    }

    private void assertMovie(Movie expectedMovie, Movie actualMovie) {
        assertEquals(expectedMovie.getId(), actualMovie.getId());
        assertEquals(expectedMovie.getName(), actualMovie.getName());
        assertEquals(expectedMovie.getImdbId(), actualMovie.getImdbId());
        assertEquals(expectedMovie.getTagline(), actualMovie.getTagline());
        assertEquals(expectedMovie.getOverview(), actualMovie.getOverview());
        assertEquals(expectedMovie.getRuntime(), actualMovie.getRuntime());
        assertEquals(expectedMovie.getReleaseDate().toString(), actualMovie.getReleaseDate().toString());
        assertTrue(expectedMovie.getGenres().size() == actualMovie.getGenres().size());
        expectedMovie.getGenres().stream().forEach(genre -> assertTrue(actualMovie.getGenres().contains(genre)));
    }

    private void deleteMovie(Movie movie) {
        Response response = target(MOVIE_RESOURCE).path("delete").path(movie.getId()).request().delete();
        assertEquals(200, response.getStatus());
        assertMovieDoesNotExist(movie);
        response.close();
    }

    private void assertMovieDoesNotExist(Movie movie) {
        Response response = target(MOVIE_RESOURCE).path(movie.getId()).request().get();
        Movie movieFromResource = gson.fromJson(response.readEntity(String.class), Movie.class);
        response.close();
    }

}
