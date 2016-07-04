package no.svitts.core.resource;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.criteria.Criteria;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.exception.mapper.ConstraintViolationExceptionMapper;
import no.svitts.core.exception.mapper.WebApplicationExceptionMapper;
import no.svitts.core.id.Id;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;
import no.svitts.core.service.Service;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class MovieResourceTest extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private MovieBuilder movieBuilder;
    private Service<Movie> movieServiceMock;
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
        when(movieServiceMock.getSingle(movie.getId())).thenReturn(movie);

        Response response = client().register(gsonMessageBodyReader).target(getBaseUri()).path(MOVIE_RESOURCE).path(movie.getId()).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(movie, response.readEntity(Movie.class));
        verify(movieServiceMock, times(1)).getSingle(movie.getId());
        response.close();
    }

    @Test
    public void getMovie_MovieNotFound_ShouldReturnOkResponseWithNullEntity() {
        String id = Id.get();
        when(movieServiceMock.getSingle(id)).thenReturn(null);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(id).request().get();
        Movie movie = response.readEntity(Movie.class);

        assertEquals(200, response.getStatus());
        assertNull(movie);
        verify(movieServiceMock, times(1)).getSingle(id);
        response.close();
    }

    @Test
    public void getMovie_ThrowsRepositoryException_ShouldReturnInternalServerErrorResponse() {
        String id = Id.get();
        when(movieServiceMock.getSingle(anyString())).thenThrow(new RepositoryException());

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(id).request().get();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).getSingle(id);
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
        when(movieServiceMock.getMultiple(any(Criteria.class))).thenReturn(movies);

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
        movies.stream().forEach(movie -> moviesFromResponse.stream().forEach(movieFromResponse -> assertEquals(movie, movieFromResponse)));
        response.close();
    }

    @Test
    public void getMovies_NoMoviesFound_ShouldReturnEmptyList() {
        String name = "name";
        String genre = "genre";
        int limit = 10;
        int offset = 10;
        when(movieServiceMock.getMultiple(new Criteria())).thenReturn(new ArrayList<>());

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
        when(movieServiceMock.saveSingle(movie)).thenReturn(movie.getId());
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().register(gsonMessageBodyWriter).target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(201, response.getStatus());
        verify(movieServiceMock, times(1)).saveSingle(any(Movie.class));
        response.close();
    }

    @Test
    public void createMovie_ThrowsRepositoryException_ShouldReturnInternalServerErrorResponse() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.saveSingle(movie)).thenThrow(new RepositoryException());
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().register(gsonMessageBodyWriter).target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).saveSingle(movie);
        response.close();
    }

    @Test
    public void deleteMovie_ValidId_ShouldReturnOkResponse() {
        Movie movie = movieBuilder.build();

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(movie.getId()).request().delete();

        assertEquals(200, response.getStatus());
        verify(movieServiceMock, times(1)).deleteSingle(movie.getId());
        response.close();
    }

    @Test
    public void deleteMovie_ThrowsRepositoryException_ShouldReturnInternalServerErrorResponse() {
        String id = Id.get();
        doThrow(new RepositoryException()).when(movieServiceMock).deleteSingle(id);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(id).request().delete();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).deleteSingle(id);
        response.close();
    }

}
