package no.svitts.core.resource;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.exception.mapper.ConstraintViolationExceptionMapper;
import no.svitts.core.exception.mapper.WebApplicationExceptionMapper;
import no.svitts.core.file.VideoFile;
import no.svitts.core.genre.Genre;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import no.svitts.core.movie.Movie;
import no.svitts.core.search.Search;
import no.svitts.core.service.MovieService;
import no.svitts.core.service.Service;
import no.svitts.core.util.Id;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MovieResourceTest extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private MovieBuilder movieBuilder;
    private Service<Movie> movieServiceMock;

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
    public void getMovie_ValidRequest_ShouldReturnMovie() {
        Movie movie = movieBuilder.testMovie().build();
        when(movieServiceMock.get(movie.getId())).thenReturn(movie);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(movie.getId()).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(movie, response.readEntity(Movie.class));
        verify(movieServiceMock, times(1)).get(movie.getId());
        response.close();
    }

    @Test
    public void getMovie_MovieNotFound_ShouldReturnOkResponseWithNullEntity() {
        String id = Id.get();
        when(movieServiceMock.get(id)).thenReturn(null);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(id).request().get();
        Movie movie = response.readEntity(Movie.class);

        assertEquals(200, response.getStatus());
        assertNull(movie);
        verify(movieServiceMock, times(1)).get(id);
        response.close();
    }

    @Test
    public void getMovie_ThrowsServiceException_ShouldReturnInternalServerErrorResponse() {
        String id = Id.get();
        when(movieServiceMock.get(anyString())).thenThrow(new ServiceException());

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(id).request().get();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).get(id);
        response.close();
    }

    @Test
    public void getMoviesByGenre_ValidRequest_ShouldReturnMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.testMovie().build());
        movies.add(movieBuilder.testMovie().build());
        when(movieServiceMock.search(any(Search.class))).thenReturn(movies);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("genres").path(Genre.FILM_NOIR.toString()).request().get();

        assertEquals(200, response.getStatus());
        Movie[] moviesFromResponse = response.readEntity(Movie[].class);
        assertEquals(movies.size(), moviesFromResponse.length);
        assertArrayEquals(movies.toArray(), moviesFromResponse);
        verify(movieServiceMock, times(1)).search(any(Search.class));
        response.close();
    }

    @Test
    public void getMoviesByGenre_GenreInLowerCase_ShouldReturnMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.testMovie().build());
        movies.add(movieBuilder.testMovie().build());
        when(movieServiceMock.search(any(Search.class))).thenReturn(movies);
        String genre = "action";

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("genres").path(genre).request().get();

        assertEquals(200, response.getStatus());
        Movie[] moviesFromResponse = response.readEntity(Movie[].class);
        assertEquals(movies.size(), moviesFromResponse.length);
        assertArrayEquals(movies.toArray(), moviesFromResponse);
        verify(movieServiceMock, times(1)).search(any(Search.class));
        response.close();
    }

    @Test
    public void getMoviesByGenre_GenreWithHyphen_ShouldReturnMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.testMovie().build());
        movies.add(movieBuilder.testMovie().build());
        when(movieServiceMock.search(any(Search.class))).thenReturn(movies);
        String genre = "Film-Noir";

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("genres").path(genre).request().get();

        assertEquals(200, response.getStatus());
        Movie[] moviesFromResponse = response.readEntity(Movie[].class);
        assertEquals(movies.size(), moviesFromResponse.length);
        assertArrayEquals(movies.toArray(), moviesFromResponse);
        verify(movieServiceMock, times(1)).search(any(Search.class));
        response.close();
    }

    @Test
    public void getMoviesByGenre_GenreWithSpaces_ShouldReturnMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.testMovie().build());
        movies.add(movieBuilder.testMovie().build());
        when(movieServiceMock.search(any(Search.class))).thenReturn(movies);
        String genre = "Science Fiction";

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("genres").path(genre).request().get();

        assertEquals(200, response.getStatus());
        Movie[] moviesFromResponse = response.readEntity(Movie[].class);
        assertEquals(movies.size(), moviesFromResponse.length);
        assertArrayEquals(movies.toArray(), moviesFromResponse);
        verify(movieServiceMock, times(1)).search(any(Search.class));
        response.close();
    }

    @Test
    public void getMoviesByGenre_ThrowsServiceException_ShouldReturnInternalServerErrorResponse() {
        when(movieServiceMock.search(any(Search.class))).thenThrow(new ServiceException());

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("genres").path(Genre.FILM_NOIR.toString()).request().get();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).search(any(Search.class));
        response.close();
    }

    @Test
    public void getMoviesByGenre_InvalidGenreParameter_ShouldReturnInternalServerErrorResponse() {
        String genre = "wrong genre";

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("genres").path(genre).request().get();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(0)).search(any(Search.class));
        response.close();
    }

    @Test
    public void search_ValidRequest_ShouldReturnMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.testMovie().build());
        movies.add(movieBuilder.testMovie().build());
        when(movieServiceMock.search(any(Search.class))).thenReturn(movies);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("search").queryParam("q", "query").request().get();

        assertEquals(200, response.getStatus());
        Movie[] moviesFromResponse = response.readEntity(Movie[].class);
        assertEquals(movies.size(), moviesFromResponse.length);
        assertArrayEquals(movies.toArray(), moviesFromResponse);
        verify(movieServiceMock, times(1)).search(any(Search.class));
        response.close();
    }

    @Test
    public void search_ThrowsServiceException_ShouldReturnInternalServerErrorResponse() {
        when(movieServiceMock.search(any(Search.class))).thenThrow(new ServiceException());

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path("search").queryParam("q", "query").request().get();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).search(any(Search.class));
        response.close();
    }

    @Test
    public void saveMovie_ValidMovie_ShouldReturnCreatedResponse() {
        Movie movie = movieBuilder.testMovie().title("title").videoFile(new VideoFile("such/path")).build();
        when(movieServiceMock.save(movie)).thenReturn(movie.getId());
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(201, response.getStatus());
        verify(movieServiceMock, times(1)).save(any(Movie.class));
        response.close();
    }

    @Test
    public void saveMovie_ThrowsServiceException_ShouldReturnInternalServerErrorResponse() {
        Movie movie = movieBuilder.testMovie().title("title").videoFile(new VideoFile("such/path")).build();
        when(movieServiceMock.save(movie)).thenThrow(new ServiceException());
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).save(movie);
        response.close();
    }

    @Test
    public void deleteMovie_ValidId_ShouldReturnOkResponse() {
        Movie movie = movieBuilder.testMovie().build();

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(movie.getId()).request().delete();

        assertEquals(200, response.getStatus());
        verify(movieServiceMock, times(1)).delete(movie.getId());
        response.close();
    }

    @Test
    public void deleteMovie_ThrowsServiceException_ShouldReturnInternalServerErrorResponse() {
        String id = Id.get();
        doThrow(new ServiceException()).when(movieServiceMock).delete(id);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).path(id).request().delete();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).delete(id);
        response.close();
    }

}
