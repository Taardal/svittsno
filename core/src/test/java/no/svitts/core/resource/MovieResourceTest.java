package no.svitts.core.resource;

import com.google.gson.Gson;
import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.exception.mapper.WebApplicationExceptionMapper;
import no.svitts.core.util.Id;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static no.svitts.core.testkit.MovieTestKit.getGson;
import static no.svitts.core.util.StringUtil.getRandomString;
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
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new MovieResource(movieServiceMock));
        resourceConfig.register(new WebApplicationExceptionMapper());
        return resourceConfig;
    }

    @Override
    @Before
    public void setUp() throws Exception {
        movieBuilder = new MovieBuilder();
        gson = getGson();
        super.setUp();
    }

    @Test
    public void getMovie_ValidId_ShouldReturnMovieAsExpectedJson(){
        Movie movie = movieBuilder.build();
        when(movieServiceMock.getMovie(movie.getId())).thenReturn(movie);
        String expectedJson = gson.toJson(movie);

        Response response = target(MOVIE_RESOURCE).path(movie.getId()).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(expectedJson, response.readEntity(String.class));
        verify(movieServiceMock, times(1)).getMovie(movie.getId());
        response.close();
    }

    @Test
    public void getMovie_IdTooLong_ShouldReturnBadRequestResponse() {
        String id = getRandomString(Id.MAX_LENGTH + 1);
        Response response = target(MOVIE_RESOURCE).path(id).request().get();

        assertEquals(400, response.getStatus());
        verify(movieServiceMock, times(0)).getMovie(id);
        response.close();
    }

    @Test
    public void getMovie_MovieNotFound_ShouldReturnNotFoundResponse() {
        String id = Id.get();
        when(movieServiceMock.getMovie(anyString())).thenReturn(null);

        Response response = target(MOVIE_RESOURCE).path(id).request().get();

        assertEquals(404, response.getStatus());
        verify(movieServiceMock, times(1)).getMovie(id);
        response.close();
    }

    @Test
    public void getMovie_ThrowsRepositoryException_ShouldReturnInternalServerErrorResponse() {
        String id = Id.get();
        when(movieServiceMock.getMovie(anyString())).thenThrow(new RepositoryException());

        Response response = target(MOVIE_RESOURCE).path(id).request().get();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).getMovie(id);
        response.close();
    }

    @Test
    public void createMovie_ValidJSON_ShouldReturnCreatedResponse() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.createMovie(movie)).thenReturn(movie.getId());
        Entity<String> jsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).request().post(jsonEntity, Response.class);

        assertEquals(201, response.getStatus());
        verify(movieServiceMock, times(1)).createMovie(any(Movie.class));
        response.close();
    }

    @Test
    public void createMovie_InvalidName_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.name(null).build();
        Movie movie1 = movieBuilder.name("").build();
        Movie movie2 = movieBuilder.name(getRandomString(Movie.NAME_MAX_LENGTH + 1)).build();
        Entity<String> jsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);
        Entity<String> jsonEntity1 = Entity.entity(gson.toJson(movie1), MediaType.APPLICATION_JSON);
        Entity<String> jsonEntity2 = Entity.entity(gson.toJson(movie2), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).request().post(jsonEntity, Response.class);
        Response response1 = target(MOVIE_RESOURCE).request().post(jsonEntity1, Response.class);
        Response response2 = target(MOVIE_RESOURCE).request().post(jsonEntity2, Response.class);

        assertEquals(400, response.getStatus());
        assertEquals(400, response1.getStatus());
        assertEquals(400, response2.getStatus());
        verify(movieServiceMock, times(0)).getMovie(movie.getId());
        response.close();
        response1.close();
        response2.close();
    }

    @Test
    public void createMovie_ThrowsRepositoryException_ShouldReturnInternalServerErrorResponse() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.createMovie(movie)).thenThrow(new RepositoryException());
        Entity<String> jsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).request().post(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).createMovie(movie);
        response.close();
    }

    @Test
    public void updateMovie_ValidMovie_ShouldReturnOkResponse() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.getMovie(movie.getId())).thenReturn(movie);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path(movie.getId()).request().put(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(movieServiceMock, times(1)).updateMovie(movie);
        response.close();
    }

    @Test
    public void updateMovie_InvalidName_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.name(null).build();
        Movie movie1 = movieBuilder.name("").build();
        Movie movie2 = movieBuilder.name(getRandomString(Movie.NAME_MAX_LENGTH + 1)).build();
        Entity<String> jsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);
        Entity<String> jsonEntity1 = Entity.entity(gson.toJson(movie1), MediaType.APPLICATION_JSON);
        Entity<String> jsonEntity2 = Entity.entity(gson.toJson(movie2), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path(movie.getId()).request().put(jsonEntity, Response.class);
        Response response1 = target(MOVIE_RESOURCE).path(movie1.getId()).request().put(jsonEntity1, Response.class);
        Response response2 = target(MOVIE_RESOURCE).path(movie2.getId()).request().put(jsonEntity2, Response.class);

        assertEquals(400, response.getStatus());
        assertEquals(400, response1.getStatus());
        assertEquals(400, response2.getStatus());
        verify(movieServiceMock, times(0)).getMovie(movie.getId());
        response.close();
        response1.close();
        response2.close();
    }

    @Test
    public void updateMovie_ThrowsRepositoryException_ShouldReturnInternalServerErrorResponse() {
        Movie movie = movieBuilder.build();
        when(movieServiceMock.getMovie(movie.getId())).thenReturn(movie);
        doThrow(new RepositoryException()).when(movieServiceMock).updateMovie(movie);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path(movie.getId()).request().put(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).updateMovie(movie);
        response.close();
    }

    @Test
    public void deleteMovie_ShouldReturnOkResponse() {
        Movie movie = movieBuilder.build();

        Response response = target(MOVIE_RESOURCE).path(movie.getId()).request().delete();

        assertEquals(200, response.getStatus());
        verify(movieServiceMock, times(1)).deleteMovie(movie.getId());
        response.close();
    }

    @Test
    public void deleteMovie_IdTooLong_ShouldReturnBadRequestResponse(){
        String id = getRandomString(Id.MAX_LENGTH + 1);
        Response response = target(MOVIE_RESOURCE).path(id).request().delete();

        assertEquals(400, response.getStatus());
        verify(movieServiceMock, times(0)).deleteMovie(id);
        response.close();
    }

    @Test
    public void deleteMovie_ThrowsRepositoryException_ShouldReturnInternalServerErrorResponse() {
        String id = Id.get();
        doThrow(new RepositoryException()).when(movieServiceMock).deleteMovie(id);

        Response response = target(MOVIE_RESOURCE).path(id).request().delete();

        assertEquals(500, response.getStatus());
        verify(movieServiceMock, times(1)).deleteMovie(id);
        response.close();
    }

}
