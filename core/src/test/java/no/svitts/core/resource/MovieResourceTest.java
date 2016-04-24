package no.svitts.core.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.gson.deserializer.MovieDeserializer;
import no.svitts.core.gson.serializer.MovieSerializer;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MovieResourceTest extends JerseyTest {

    private MovieService mockMovieService;
    private MovieBuilder movieBuilder;
    private Gson gson;

    @Override
    protected Application configure() {
        mockMovieService = mock(MovieService.class);
        movieBuilder = new MovieBuilder();
        gson = getGson();
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new MovieResource(mockMovieService));
        return resourceConfig;
    }

    @Test
    public void getMovieByName_ShouldReturnExpectedMovieAsJson() {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();
        when(mockMovieService.getMovieByName(sherlockHolmes.getName())).thenReturn(sherlockHolmes);
        String expectedJson = gson.toJson(sherlockHolmes);

        String json = target("movie").queryParam("name", sherlockHolmes.getName()).request().get(String.class);

        assertEquals(expectedJson, json);
        verify(mockMovieService, times(1)).getMovieByName(sherlockHolmes.getName());
    }

    @Test
    public void getMovieById_ShouldReturnExpectedMovieAsJson() {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();
        when(mockMovieService.getMovieById(sherlockHolmes.getId())).thenReturn(sherlockHolmes);
        String expectedJson = gson.toJson(sherlockHolmes);

        String json = target("movie").path(sherlockHolmes.getId()).request().get(String.class);

        assertEquals(expectedJson, json);
        verify(mockMovieService, times(1)).getMovieById(sherlockHolmes.getId());
    }

    @Test
    public void getAllMovies_ShouldReturnExpectedMoviesAsJson() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.sherlockHolmes().build());
        movies.add(movieBuilder.sherlockHolmesAGameOfShadows().build());
        when(mockMovieService.getAll()).thenReturn(movies);
        String expectedJson = gson.toJson(movies);

        String json = target("movie").path("all").request().get(String.class);

        assertEquals(expectedJson, json);
        verify(mockMovieService, times(1)).getAll();
    }

    @Test
    public void createMovie_ShouldReturn200() {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();
        when(mockMovieService.createMovie(any(Movie.class))).thenReturn(true);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(sherlockHolmes), MediaType.APPLICATION_JSON);

        Response response = target("movie").path("create").request().post(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(mockMovieService, times(1)).createMovie(any(Movie.class));
    }

    @Test
    public void createMovie_Failed_ShouldReturn500() {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();
        when(mockMovieService.createMovie(any(Movie.class))).thenReturn(false);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(sherlockHolmes), MediaType.APPLICATION_JSON);

        Response response = target("movie").path("create").request().post(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(mockMovieService, times(1)).createMovie(any(Movie.class));
    }

    @Test
    public void updateMovie_ShouldReturn200() {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();
        when(mockMovieService.updateMovie(any(Movie.class))).thenReturn(true);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(sherlockHolmes), MediaType.APPLICATION_JSON);

        Response response = target("movie").path("update").path(sherlockHolmes.getId()).request().put(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(mockMovieService, times(1)).updateMovie(any(Movie.class));
    }

    @Test
    public void updateMovie_Failed_ShouldReturn500() {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();
        when(mockMovieService.updateMovie(any(Movie.class))).thenReturn(false);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(sherlockHolmes), MediaType.APPLICATION_JSON);

        Response response = target("movie").path("update").path(sherlockHolmes.getId()).request().put(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(mockMovieService, times(1)).updateMovie(any(Movie.class));
    }

    @Test
    public void deleteMovie_ShouldReturn200() {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();
        when(mockMovieService.deleteMovie(sherlockHolmes.getId())).thenReturn(true);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(sherlockHolmes), MediaType.APPLICATION_JSON);

        Response response = target("movie").path("delete").path(sherlockHolmes.getId()).request().put(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(mockMovieService, times(1)).deleteMovie(sherlockHolmes.getId());
    }

    @Test
    public void deleteMovie_Failed_ShouldReturn500() {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();
        when(mockMovieService.deleteMovie(sherlockHolmes.getId())).thenReturn(false);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(sherlockHolmes), MediaType.APPLICATION_JSON);

        Response response = target("movie").path("delete").path(sherlockHolmes.getId()).request().put(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(mockMovieService, times(1)).deleteMovie(sherlockHolmes.getId());
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Movie.class, new MovieSerializer())
                .registerTypeAdapter(Movie.class, new MovieDeserializer())
                .create();
    }
}
