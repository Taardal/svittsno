package no.svitts.core.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.repository.Repository;
import no.svitts.core.testdatabuilder.MovieTestDataBuilder;
import no.svitts.core.gson.deserializer.MovieDeserializer;
import no.svitts.core.gson.serializer.MovieSerializer;
import no.svitts.core.movie.Movie;
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

    private Gson gson;
    private MovieTestDataBuilder movieTestDataBuilder;
    private Repository<Movie> mockMovieRepository;

    @Override
    protected Application configure() {
        gson = getGson();
        movieTestDataBuilder = new MovieTestDataBuilder();
        mockMovieRepository = mock(MovieRepository.class);
        return getResourceConfig(mockMovieRepository);
    }

    @Test
    public void getMovieById_ShouldReturnExpectedMovieAsJson() {
        Movie sherlockHolmes = movieTestDataBuilder.sherlockHolmes().build();
        when(mockMovieRepository.getById(sherlockHolmes.getId())).thenReturn(sherlockHolmes);
        String expectedJson = gson.toJson(sherlockHolmes);

        String json = target("movie").path(sherlockHolmes.getId()).request().get(String.class);

        assertEquals(expectedJson, json);
        verify(mockMovieRepository, times(1)).getById(sherlockHolmes.getId());
    }

    @Test
    public void getAllMovies_ShouldReturnExpectedMoviesAsJson() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movieTestDataBuilder.sherlockHolmes().build());
        movies.add(movieTestDataBuilder.sherlockHolmesAGameOfShadows().build());
        when(mockMovieRepository.getAll()).thenReturn(movies);
        String expectedJson = gson.toJson(movies);

        String json = target("movie").path("all").request().get(String.class);

        assertEquals(expectedJson, json);
        verify(mockMovieRepository, times(1)).getAll();
    }

    @Test
    public void createMovie_ServiceReturnsTrue_ShouldReturnOk() {
        Movie sherlockHolmes = movieTestDataBuilder.sherlockHolmes().build();
        when(mockMovieRepository.insertSingle(any(Movie.class))).thenReturn(true);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(sherlockHolmes), MediaType.APPLICATION_JSON);

        Response response = target("movie").path("create").request().post(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(mockMovieRepository, times(1)).insertSingle(any(Movie.class));
    }

    @Test
    public void createMovie_ServiceReturnsFalse_ShouldReturnServerError() {
        Movie sherlockHolmes = movieTestDataBuilder.sherlockHolmes().build();
        when(mockMovieRepository.insertSingle(any(Movie.class))).thenReturn(false);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(sherlockHolmes), MediaType.APPLICATION_JSON);

        Response response = target("movie").path("create").request().post(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(mockMovieRepository, times(1)).insertSingle(any(Movie.class));
    }

    @Test
    public void updateMovie_ServiceReturnsTrue_ShouldReturnOk() {
        Movie sherlockHolmes = movieTestDataBuilder.sherlockHolmes().build();
        when(mockMovieRepository.updateSingle(any(Movie.class))).thenReturn(true);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(sherlockHolmes), MediaType.APPLICATION_JSON);

        Response response = target("movie").path("update").path(sherlockHolmes.getId()).request().put(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(mockMovieRepository, times(1)).updateSingle(any(Movie.class));
    }

    @Test
    public void updateMovie_ServiceReturnsFalse_ShouldReturnServerError() {
        Movie sherlockHolmes = movieTestDataBuilder.sherlockHolmes().build();
        when(mockMovieRepository.updateSingle(any(Movie.class))).thenReturn(false);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(sherlockHolmes), MediaType.APPLICATION_JSON);

        Response response = target("movie").path("update").path(sherlockHolmes.getId()).request().put(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(mockMovieRepository, times(1)).updateSingle(any(Movie.class));
    }

    @Test
    public void deleteMovie_ServiceReturnsTrue_ShouldReturnOk() {
        Movie sherlockHolmes = movieTestDataBuilder.sherlockHolmes().build();
        when(mockMovieRepository.deleteSingle(sherlockHolmes.getId())).thenReturn(true);

        Response response = target("movie").path("delete").path(sherlockHolmes.getId()).request().delete();

        assertEquals(200, response.getStatus());
        verify(mockMovieRepository, times(1)).deleteSingle(sherlockHolmes.getId());
    }

    @Test
    public void deleteMovie_ServiceReturnsFalse_ShouldReturnServerError() {
        Movie sherlockHolmes = movieTestDataBuilder.sherlockHolmes().build();
        when(mockMovieRepository.deleteSingle(sherlockHolmes.getId())).thenReturn(false);

        Response response = target("movie").path("delete").path(sherlockHolmes.getId()).request().delete();

        assertEquals(500, response.getStatus());
        verify(mockMovieRepository, times(1)).deleteSingle(sherlockHolmes.getId());
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Movie.class, new MovieSerializer())
                .registerTypeAdapter(Movie.class, new MovieDeserializer())
                .create();
    }

    private Application getResourceConfig(Repository<Movie> movieRepository) {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new MovieResource(movieRepository));
        return resourceConfig;
    }
}