package no.svitts.core.resource;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.exception.mapper.ConstraintViolationExceptionMapper;
import no.svitts.core.exception.mapper.WebApplicationExceptionMapper;
import no.svitts.core.gson.GsonMessageBodyReader;
import no.svitts.core.gson.GsonMessageBodyWriter;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;
import no.svitts.core.util.Id;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static no.svitts.core.testkit.MovieTestKit.assertMovie;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MovieResourceTest extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private MovieBuilder movieBuilder;
    private MovieService movieServiceMock;
    private GsonMessageBodyWriter gsonMessageBodyWriter;
    private GsonMessageBodyReader gsonMessageBodyReader;

    @Override
    protected Application configure() {
        movieServiceMock = mock(MovieService.class);
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new MovieResource(movieServiceMock));
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
        gsonMessageBodyWriter = new GsonMessageBodyWriter();
        gsonMessageBodyReader = new GsonMessageBodyReader();
        super.setUp();
    }

    @Test
    public void getMovie_ValidRequest_ShouldReturnMovie() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.getMovie(movie.getId())).thenReturn(movie);

        Response response = client().register(gsonMessageBodyReader).target(getBaseUri()).path(MOVIE_RESOURCE).path(movie.getId()).request().get();

        assertEquals(200, response.getStatus());
        assertMovie(movie, response.readEntity(Movie.class));
        verify(movieServiceMock, times(1)).getMovie(movie.getId());
        response.close();
    }

    @Test
    public void getMovie_MovieNotFound_ShouldReturnNotFoundResponse() {
        String id = Id.get();
        when(movieServiceMock.getMovie(anyString())).thenReturn(null);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(id).request().get();

        assertEquals(404, response.getStatus());
        verify(movieServiceMock, times(1)).getMovie(id);
        response.close();
    }

    @Test
    public void getMovie_ThrowsRepositoryException_ShouldReturnInternalServerErrorResponse() {
        String id = Id.get();
        when(movieServiceMock.getMovie(anyString())).thenThrow(new RepositoryException());

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(id).request().get();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).getMovie(id);
        response.close();
    }

    @Test
    public void getMovies_ValidRequest_ShouldReturnMovies() {
        String name = "name";
        String genre = "genre";
        int limit = 10;
        int offset = 10;
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.build());
        movies.add(movieBuilder.build());
        when(movieServiceMock.getMovies(name, genre, limit, offset)).thenReturn(movies);

        Response response = client().register(gsonMessageBodyReader).target(getBaseUri()).path(MOVIE_RESOURCE)
                .queryParam("name", name)
                .queryParam("genre", genre)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .request()
                .get();
        List<Movie> moviesFromResponse = Arrays.asList(response.readEntity(Movie[].class));

        assertEquals(200, response.getStatus());
        assertEquals(movies.size(), moviesFromResponse.size());
        movies.stream().forEach(movie -> moviesFromResponse.stream().forEach(movieFromResponse -> assertMovie(movie, movieFromResponse)));
        response.close();
    }

    @Test
    public void getMovies_NoMoviesFound_ShouldReturnEmptyList() {
        String name = "name";
        String genre = "genre";
        int limit = 10;
        int offset = 10;
        when(movieServiceMock.getMovies(name, genre, limit, offset)).thenReturn(new ArrayList<>());

        Response response = client().register(gsonMessageBodyReader).target(getBaseUri()).path(MOVIE_RESOURCE)
                .queryParam("name", name)
                .queryParam("genre", genre)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .request()
                .get();
        List<Movie> moviesFromResponse = Arrays.asList(response.readEntity(Movie[].class));

        assertEquals(200, response.getStatus());
        assertEquals(0, moviesFromResponse.size());
        response.close();
    }

    @Test
    public void createMovie_ValidMovie_ShouldReturnCreatedResponse() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.createMovie(movie)).thenReturn(movie.getId());
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().register(gsonMessageBodyWriter).target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(201, response.getStatus());
        verify(movieServiceMock, times(1)).createMovie(any(Movie.class));
        response.close();
    }

    @Test
    public void createMovie_ThrowsRepositoryException_ShouldReturnInternalServerErrorResponse() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.createMovie(movie)).thenThrow(new RepositoryException());
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().register(gsonMessageBodyWriter).target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).createMovie(movie);
        response.close();
    }

    @Test
    public void updateMovie_ValidMovie_ShouldReturnOkResponse() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.getMovie(movie.getId())).thenReturn(movie);
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().register(gsonMessageBodyWriter).target(getBaseUri()).path(MOVIE_RESOURCE).path(movie.getId()).request().put(movieEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(movieServiceMock, times(1)).updateMovie(movie);
        response.close();
    }

    @Test
    public void updateMovie_ThrowsRepositoryException_ShouldReturnInternalServerErrorResponse() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.getMovie(movie.getId())).thenReturn(movie);
        doThrow(new RepositoryException()).when(movieServiceMock).updateMovie(movie);
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().register(gsonMessageBodyWriter).target(getBaseUri()).path(MOVIE_RESOURCE).path(movie.getId()).request().put(movieEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).updateMovie(movie);
        response.close();
    }

    @Test
    public void deleteMovie_ValidId_ShouldReturnOkResponse() {
        Movie movie = movieBuilder.build();

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(movie.getId()).request().delete();

        assertEquals(200, response.getStatus());
        verify(movieServiceMock, times(1)).deleteMovie(movie.getId());
        response.close();
    }

    @Test
    public void deleteMovie_ThrowsRepositoryException_ShouldReturnInternalServerErrorResponse() {
        String id = Id.get();
        doThrow(new RepositoryException()).when(movieServiceMock).deleteMovie(id);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(id).request().delete();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).deleteMovie(id);
        response.close();
    }

}
