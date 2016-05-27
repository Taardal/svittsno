package no.svitts.core.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.exception.NoChangeException;
import no.svitts.core.gson.deserializer.MovieDeserializer;
import no.svitts.core.gson.serializer.MovieSerializer;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static no.svitts.core.testkit.JerseyTestKit.getResourceConfig;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MovieResourceTest extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private MovieBuilder movieBuilder;
    private Gson gson;
    private MovieService movieServiceMock;

    @Override
    protected Application configure() {
        movieServiceMock = mock(MovieService.class);
        return getResourceConfig(new MovieResource(movieServiceMock));
    }

    @Override
    @Before
    public void setUp() throws Exception {
        movieBuilder = new MovieBuilder();
        gson = getGson();
        super.setUp();
    }

    @Test
    public void getMovieById_ValidId_ShouldReturnMovieAsExpectedJson() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.getMovie(movie.getId())).thenReturn(movie);

        Response response = target(MOVIE_RESOURCE).path(movie.getId()).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(gson.toJson(movie), response.readEntity(String.class));
        verify(movieServiceMock, times(1)).getMovie(movie.getId());
        response.close();
    }

    @Test
    public void createMovie_ShouldReturnOk() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.createMovie(any(Movie.class))).thenReturn(movie.getId());
        Entity<String> jsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("create").request().post(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(movieServiceMock, times(1)).createMovie(any(Movie.class));
    }

    @Test
    public void createMovie_Fails_ShouldReturnServerError() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.createMovie(any(Movie.class))).thenReturn(movie.getId());
        Entity<String> jsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("create").request().post(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).createMovie(any(Movie.class));
    }

    @Test
    public void updateMovie_ShouldReturnOk() {
        Movie movie = movieBuilder.build();
        Entity<String> jsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("update").path(movie.getId()).request().put(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(movieServiceMock, times(1)).updateMovie(any(Movie.class));
    }

    @Test
    public void updateMovie_Fails_ShouldReturnServerError() {
        Movie movie = movieBuilder.build();
        Entity<String> jsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("update").path(movie.getId()).request().put(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).updateMovie(any(Movie.class));
    }

    @Test
    public void deleteMovie_ShouldReturnOk() throws NoChangeException {
        Movie movie = movieBuilder.build();

        Response response = target(MOVIE_RESOURCE).path("delete").path(movie.getId()).request().delete();

        assertEquals(200, response.getStatus());
        verify(movieServiceMock, times(1)).deleteMovie(movie.getId());
    }

    @Test
    public void deleteMovie_Fails_ShouldReturnServerError() throws NoChangeException {
        Movie movie = movieBuilder.build();

        Response response = target(MOVIE_RESOURCE).path("delete").path(movie.getId()).request().delete();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).deleteMovie(movie.getId());
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Movie.class, new MovieSerializer())
                .registerTypeAdapter(Movie.class, new MovieDeserializer())
                .create();
    }

}
