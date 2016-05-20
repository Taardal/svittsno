package no.svitts.core.movie;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.CoreJerseyTest;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.gson.deserializer.MovieDeserializer;
import no.svitts.core.gson.serializer.MovieSerializer;
import no.svitts.core.id.Id;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.resource.MovieResource;
import no.svitts.core.testdatabuilder.MovieTestDataBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MovieIT extends CoreJerseyTest {

    private static final String MOVIE_RESOURCE = "movie";
    private static final int ID_MAX_LENGTH = 255;
    private static final int NAME_MAX_LENGTH = 255;
    private static final int IMDB_ID_MAX_LENGTH = 255;
    private static final int TAGLINE_MAX_LENGTH = 255;
    private static final int OVERVIEW_MAX_LENGTH = 510;

    private DataSource dataSource;
    private Gson gson;
    private MovieTestDataBuilder movieTestDataBuilder;

    @Override
    protected Application configure() {
        dataSource = getDataSource();
        return getResourceConfig(new MovieResource(new MovieRepository(dataSource, videoFileRepository, imageFileRepository)));
    }

    @Override
    @Before
    public void setUp() throws Exception {
        gson = getGson();
        movieTestDataBuilder = new MovieTestDataBuilder();
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        dataSource.close();
        super.tearDown();
    }

    @Test
    public void getMovieById_MovieDoesNotExist_ShouldReturnUnknownMovie() {
        Response response = target(MOVIE_RESOURCE).path(Id.get()).request().get();
        Movie movie = gson.fromJson(response.readEntity(String.class), Movie.class);
        assertEquals(MovieRepository.UNKNOWN_MOVIE_ID, movie.getId());
        response.close();
    }

    @Test
    public void createMovie_IdTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.id(getRandomString(ID_MAX_LENGTH + 1)).build();
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("create").request().post(movieJsonEntity);

        assertEquals(500, response.getStatus());
        assertMovieDoesNotExist(movie);
        response.close();
    }

    @Test
    public void createMovie_NameTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.name(getRandomString(NAME_MAX_LENGTH + 1)).build();
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("create").request().post(movieJsonEntity);

        assertEquals(500, response.getStatus());
        assertMovieDoesNotExist(movie);
        response.close();
    }

    @Test
    public void createMovie_ImdbIdTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.imdbId(getRandomString(IMDB_ID_MAX_LENGTH + 1)).build();
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("create").request().post(movieJsonEntity);

        assertEquals(500, response.getStatus());
        assertMovieDoesNotExist(movie);
        response.close();
    }

    @Test
    public void createMovie_TaglineTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.tagline(getRandomString(TAGLINE_MAX_LENGTH + 1)).build();
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("create").request().post(movieJsonEntity);

        assertEquals(500, response.getStatus());
        assertMovieDoesNotExist(movie);
        response.close();
    }

    @Test
    public void createMovie_OverviewTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.overview(getRandomString(OVERVIEW_MAX_LENGTH + 1)).build();
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("create").request().post(movieJsonEntity);

        assertEquals(500, response.getStatus());
        assertMovieDoesNotExist(movie);
        response.close();
    }

    @Test
    public void createMovie_StringFieldsMaximumLength_ShouldReturnOk() {
        Movie movie = movieTestDataBuilder
                .name(getRandomString(ID_MAX_LENGTH))
                .imdbId(getRandomString(IMDB_ID_MAX_LENGTH))
                .tagline(getRandomString(TAGLINE_MAX_LENGTH))
                .overview(getRandomString(OVERVIEW_MAX_LENGTH))
                .build();
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("create").request().post(movieJsonEntity);
        assertEquals(200, response.getStatus());
        deleteMovie(movie);
        response.close();
    }

    @Test
    public void createMovie_RequiredFieldsAreNull_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.id(null).name(null).build();
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("create").request().post(movieJsonEntity);

        assertEquals(500, response.getStatus());
        response.close();
    }

    @Test
    public void createMovie_OptionalFieldsAreNull_ShouldReturnOk() {
        Movie movie = movieTestDataBuilder.imdbId(null).tagline(null).overview(null).releaseDate(null).build();
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("create").request().post(movieJsonEntity);
        
        assertEquals(200, response.getStatus());
        deleteMovie(movie);
        response.close();
    }

    @Test
    public void updateMovie_MovieDoesNotExist_ShouldAbortAndReturnServerError() {
        Movie movie = new MovieTestDataBuilder().build();
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);

        Response response = target(MOVIE_RESOURCE).path("update").path(movie.getId()).request().put(movieJsonEntity);
        
        assertEquals(500, response.getStatus());
        response.close();
    }

    @Test
    public void updateMovie_NameTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);
        
        movie.setName(getRandomString(NAME_MAX_LENGTH + 1));
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);
        Response response = target(MOVIE_RESOURCE).path("update").path(movie.getId()).request().put(movieJsonEntity);
        
        assertEquals(500, response.getStatus());
        deleteMovie(movie);
        response.close();
    }

    @Test
    public void updateMovie_ImdbIdTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);
        
        movie.setImdbId(getRandomString(IMDB_ID_MAX_LENGTH + 1));
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);
        Response response = target(MOVIE_RESOURCE).path("update").path(movie.getId()).request().put(movieJsonEntity);
        
        assertEquals(500, response.getStatus());
        deleteMovie(movie);
        response.close();
    }

    @Test
    public void updateMovie_TaglineTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);
        
        movie.setTagline(getRandomString(TAGLINE_MAX_LENGTH + 1));
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);
        Response response = target(MOVIE_RESOURCE).path("update").path(movie.getId()).request().put(movieJsonEntity);
        
        assertEquals(500, response.getStatus());
        deleteMovie(movie);
        response.close();
    }

    @Test
    public void updateMovie_OverviewTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);
        
        movie.setOverview(getRandomString(OVERVIEW_MAX_LENGTH + 1));
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);
        Response response = target(MOVIE_RESOURCE).path("update").path(movie.getId()).request().put(movieJsonEntity);
        
        assertEquals(500, response.getStatus());
        deleteMovie(movie);
        response.close();
    }

    @Test
    public void updateMovie_StringFieldsMaximumLength_ShouldReturnOk() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);
        
        movie.setName(getRandomString(ID_MAX_LENGTH));
        movie.setImdbId(getRandomString(IMDB_ID_MAX_LENGTH));
        movie.setTagline(getRandomString(TAGLINE_MAX_LENGTH));
        movie.setOverview(getRandomString(OVERVIEW_MAX_LENGTH));
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);
        Response response = target(MOVIE_RESOURCE).path("update").path(movie.getId()).request().put(movieJsonEntity);
        
        assertEquals(200, response.getStatus());
        deleteMovie(movie);
        response.close();
    }

    @Test
    public void updateMovie_RequiredFieldsAreNull_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);

        movie.setName(null);
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);
        Response response = target(MOVIE_RESOURCE).path("update").path(movie.getId()).request().put(movieJsonEntity);
        
        assertEquals(500, response.getStatus());
        deleteMovie(movie);
        response.close();
    }

    @Test
    public void updateMovie_OptionalFieldsAreNull_ShouldReturnOk() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);

        movie.setImdbId(null);
        movie.setTagline(null);
        movie.setOverview(null);
        movie.setReleaseDate(null);
        Entity<String> movieJsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);
        Response response = target(MOVIE_RESOURCE).path("update").path(movie.getId()).request().put(movieJsonEntity);
        
        assertEquals(200, response.getStatus());
        deleteMovie(movie);
        response.close();
    }

    @Test
    public void deleteMovie_MovieDoesNotExist_ShouldAbortAndReturnServerError() {
        Response response = target(MOVIE_RESOURCE).path("delete").path(Id.get()).request().delete();
        assertEquals(500, response.getStatus());
        response.close();
    }

    @Override
    protected Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Movie.class, new MovieSerializer())
                .registerTypeAdapter(Movie.class, new MovieDeserializer())
                .create();
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
        assertEquals(MovieRepository.UNKNOWN_MOVIE_ID, movieFromResource.getId());
        response.close();
    }

}
