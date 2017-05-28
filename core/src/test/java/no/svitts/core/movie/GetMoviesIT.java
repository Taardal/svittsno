package no.svitts.core.movie;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.genre.Genre;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static no.svitts.core.CoreTestKit.getITestApplication;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetMoviesIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

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
    public void getMoviesByGenre_NoMoviesWithGenreExist_ShouldReturnOkResponseWithEmptyArray() {
        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("genres").path(Genre.BIOGRAPHY.toString()).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(0, response.readEntity(Movie[].class).length);
        response.close();
    }

    @Test
    public void getMoviesByGenre_ValidGenre_ShouldReturnAllMoviesWithGenre() throws IOException {
        List<Movie> movies = Arrays.asList(
                movieBuilder.testMovie().genres(Genre.COMEDY, Genre.WAR).build(),
                movieBuilder.testMovie().genres(Genre.WAR).build());

        createMovies(movies);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("genres").path(Genre.COMEDY.toString()).request().get();
        Movie[] moviesFromResponse = response.readEntity(Movie[].class);

        assertEquals(200, response.getStatus());
        assertEquals(1, moviesFromResponse.length);
        assertTrue(movies.contains(moviesFromResponse[0]));
        response.close();
    }

    @Test
    public void getMoviesByGenre_LimitSmallerThanNumberOfMoviesInDatabase_ShouldReturnNumberOfMoviesEqualToLimit() throws IOException {
        List<Movie> movies = Arrays.asList(
                movieBuilder.testMovie().genres(Genre.COMEDY).build(),
                movieBuilder.testMovie().genres(Genre.COMEDY).build());

        createMovies(movies);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("genres").path(Genre.COMEDY.toString()).queryParam("limit", 1).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(1, response.readEntity(Movie[].class).length);
        response.close();
    }


    @Test
    public void getMovies_OffsetGreaterThanZero_ShouldReturnArrayWhereFirstMovieInArrayIsMovieInsertedAtOffsetIndex() throws IOException {
        List<Movie> movies = Arrays.asList(
                movieBuilder.testMovie().genres(Genre.COMEDY).build(),
                movieBuilder.testMovie().genres(Genre.COMEDY).build());

        createMovies(Arrays.asList(
                movieBuilder.testMovie().genres(Genre.COMEDY).build(),
                movieBuilder.testMovie().genres(Genre.COMEDY).build()));

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("genres").path(Genre.COMEDY.toString()).queryParam("offset", 1).request().get();

        assertEquals(200, response.getStatus());
        Movie[] moviesFromResource = response.readEntity(Movie[].class);
        assertEquals(1, moviesFromResource.length);
        assertEquals(movies.get(1), moviesFromResource[0]);
        response.close();
    }

    @Test
    public void search_ValidQuery_ShouldReturnMoviesWithTitleLikeQuery() throws IOException {
        List<Movie> movies = Arrays.asList(
                movieBuilder.testMovie().title("Iron Man").build(),
                movieBuilder.testMovie().title("Iron Man 2").build(),
                movieBuilder.testMovie().title("Iron Sky").build(),
                movieBuilder.testMovie().title("Gladiator").build());

        createMovies(movies);

        String query = "Iron";
        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("search").queryParam("q", query).request().get();

        assertEquals(200, response.getStatus());
        Movie[] moviesFromResource = response.readEntity(Movie[].class);
        assertEquals(3, moviesFromResource.length);
        Arrays.stream(moviesFromResource).forEach(movie -> assertTrue(movie.getTitle().contains(query)));
        response.close();
    }

    @Test
    public void search_LimitSmallerThanNumberOfMoviesInDatabase_ShouldReturnNumberOfMoviesEqualToLimit() throws IOException {
        List<Movie> movies = Arrays.asList(
                movieBuilder.testMovie().title("Iron Man").build(),
                movieBuilder.testMovie().title("Iron Man 2").build(),
                movieBuilder.testMovie().title("Iron Sky").build(),
                movieBuilder.testMovie().title("Gladiator").build());

        createMovies(movies);

        String query = "Iron";
        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("search").queryParam("q", query).queryParam("limit", 1).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(1, response.readEntity(Movie[].class).length);
        response.close();
    }


    @Test
    public void search_OffsetGreaterThanZero_ShouldReturnArrayWhereFirstMovieInArrayIsMovieInsertedAtOffsetIndex() throws IOException {
        List<Movie> movies = Arrays.asList(
                movieBuilder.testMovie().title("Iron Man").build(),
                movieBuilder.testMovie().title("Iron Man 2").build(),
                movieBuilder.testMovie().title("Iron Sky").build(),
                movieBuilder.testMovie().title("Gladiator").build());

        createMovies(movies);

        String query = "Iron";
        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("search").queryParam("q", query).queryParam("offset", 1).request().get();

        assertEquals(200, response.getStatus());
        Movie[] moviesFromResource = response.readEntity(Movie[].class);
        assertEquals(2, moviesFromResource.length);
        assertEquals(movies.get(1), moviesFromResource[0]);
        response.close();
    }

    private void createMovies(List<Movie> movies) {
        for (Movie movie : movies) {
            Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
            Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);
            assertEquals(201, response.getStatus());
            response.close();
        }
    }

}
