package no.svitts.core.movie;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.application.ApplicationProperties;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.DataSourceConfig;
import no.svitts.core.datasource.SqlDataSource;
import no.svitts.core.gson.deserializer.MovieDeserializer;
import no.svitts.core.gson.serializer.MovieSerializer;
import no.svitts.core.id.Id;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.resource.MovieResource;
import no.svitts.core.testdatabuilder.MovieTestDataBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MovieIT {

    private static final int OK = 200;
    private static final int SERVER_ERROR = 500;
    private static final int DEFAULT_MAX_LENGTH = 255;
    private static final int OVERVIEW_MAX_LENGTH = DEFAULT_MAX_LENGTH * 2;

    private Gson gson;
    private MovieTestDataBuilder movieTestDataBuilder;
    private DataSource dataSource;
    private MovieResource movieResource;

    @Before
    public void setup() {
        gson = getGson();
        movieTestDataBuilder = new MovieTestDataBuilder();
        dataSource = getDataSource();
        movieResource = new MovieResource(new MovieRepository(dataSource));
    }

    @Test
    public void getMovieById_MovieDoesNotExist_ShouldReturnUnknownMovie() {
        Movie movie = gson.fromJson(movieResource.getMovieById(Id.get()), Movie.class);
        assertEquals(MovieRepository.UNKNOWN_MOVIE_ID, movie.getId());
    }


    @Test
    public void createMovie_NameTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.name(getRandomString(DEFAULT_MAX_LENGTH + 1)).build();
        Response response = movieResource.createMovie(gson.toJson(movie));
        assertEquals(SERVER_ERROR, response.getStatus());
        assertMovieDoesNotExist(movie);
    }

    @Test
    public void createMovie_ImdbIdTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.imdbId(getRandomString(DEFAULT_MAX_LENGTH + 1)).build();
        Response response = movieResource.createMovie(gson.toJson(movie));
        assertEquals(SERVER_ERROR, response.getStatus());
        assertMovieDoesNotExist(movie);
    }

    @Test
    public void createMovie_TaglineTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.tagline(getRandomString(DEFAULT_MAX_LENGTH + 1)).build();
        Response response = movieResource.createMovie(gson.toJson(movie));
        assertEquals(SERVER_ERROR, response.getStatus());
        assertMovieDoesNotExist(movie);
    }

    @Test
    public void createMovie_OverviewTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.overview(getRandomString(OVERVIEW_MAX_LENGTH + 1)).build();
        Response response = movieResource.createMovie(gson.toJson(movie));
        assertEquals(SERVER_ERROR, response.getStatus());
        assertMovieDoesNotExist(movie);
    }

    @Test
    public void createMovie_StringFieldsMaximumLength_ShouldReturnOk() {
        Movie movie = movieTestDataBuilder
                .name(getRandomString(DEFAULT_MAX_LENGTH))
                .imdbId(getRandomString(DEFAULT_MAX_LENGTH))
                .tagline(getRandomString(DEFAULT_MAX_LENGTH))
                .overview(getRandomString(OVERVIEW_MAX_LENGTH))
                .build();
        Response response = movieResource.createMovie(gson.toJson(movie));
        assertEquals(OK, response.getStatus());
        deleteMovie(movie);
    }

    @Test
    public void createMovie_OptionalFieldsAreNull_ShouldReturnOk() {
        Movie movie = movieTestDataBuilder.imdbId(null).tagline(null).overview(null).releaseDate(null).build();
        Response response = movieResource.createMovie(gson.toJson(movie));
        assertEquals(OK, response.getStatus());
        deleteMovie(movie);
    }

    @Test
    public void updateMovie_MovieDoesNotExist_ShouldAbortAndReturnServerError() {
        Movie movie = new MovieTestDataBuilder().build();
        Response response = movieResource.updateMovie(movie.getId(), gson.toJson(movie));
        assertEquals(SERVER_ERROR, response.getStatus());
    }

    @Test
    public void updateMovie_NameTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);

        movie.setName(getRandomString(DEFAULT_MAX_LENGTH + 1));

        Response response = movieResource.updateMovie(movie.getId(), gson.toJson(movie));
        assertEquals(SERVER_ERROR, response.getStatus());
        deleteMovie(movie);
    }

    @Test
    public void updateMovie_ImdbIdTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);

        movie.setImdbId(getRandomString(DEFAULT_MAX_LENGTH + 1));

        Response response = movieResource.updateMovie(movie.getId(), gson.toJson(movie));
        assertEquals(SERVER_ERROR, response.getStatus());
        deleteMovie(movie);
    }

    @Test
    public void updateMovie_TaglineTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);

        movie.setTagline(getRandomString(DEFAULT_MAX_LENGTH + 1));

        Response response = movieResource.updateMovie(movie.getId(), gson.toJson(movie));
        assertEquals(SERVER_ERROR, response.getStatus());
        deleteMovie(movie);
    }

    @Test
    public void updateMovie_OverviewTooLong_ShouldAbortAndReturnServerError() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);

        movie.setOverview(getRandomString(OVERVIEW_MAX_LENGTH + 1));

        Response response = movieResource.updateMovie(movie.getId(), gson.toJson(movie));
        assertEquals(SERVER_ERROR, response.getStatus());
        deleteMovie(movie);
    }

    @Test
    public void updateMovie_StringFieldsMaximumLength_ShouldReturnOk() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);

        movie.setName(getRandomString(DEFAULT_MAX_LENGTH));
        movie.setImdbId(getRandomString(DEFAULT_MAX_LENGTH));
        movie.setTagline(getRandomString(DEFAULT_MAX_LENGTH));
        movie.setOverview(getRandomString(OVERVIEW_MAX_LENGTH));

        Response response = movieResource.updateMovie(movie.getId(), gson.toJson(movie));
        assertEquals(OK, response.getStatus());
        deleteMovie(movie);
    }

    @Test
    public void updateMovie_OptionalFieldsAreNull_ShouldReturnOk() {
        Movie movie = movieTestDataBuilder.build();
        createMovie(movie);

        movie.setImdbId(null);
        movie.setTagline(null);
        movie.setOverview(null);
        movie.setReleaseDate(null);

        Response response = movieResource.updateMovie(movie.getId(), gson.toJson(movie));
        assertEquals(OK, response.getStatus());
        deleteMovie(movie);
    }

    @Test
    public void deleteMovie_MovieDoesNotExist_ShouldAbortAndReturnServerError() {
        Response response = movieResource.deleteMovie(Id.get());
        assertEquals(SERVER_ERROR, response.getStatus());
    }

    @After
    public void tearDown() {
        dataSource.close();
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Movie.class, new MovieSerializer())
                .registerTypeAdapter(Movie.class, new MovieDeserializer())
                .create();
    }

    private SqlDataSource getDataSource() {
        return new SqlDataSource(getDataSourceConfig(new ApplicationProperties()));
    }

    private DataSourceConfig getDataSourceConfig(ApplicationProperties applicationProperties) {
        return new DataSourceConfig(
                applicationProperties.get("db.driver"),
                applicationProperties.get("db.username"),
                applicationProperties.get("db.password"),
                applicationProperties.get("db.itest.url")
        );
    }

    private void createMovie(Movie movie) {
        Response response = movieResource.createMovie(gson.toJson(movie));
        assertEquals(OK, response.getStatus());
        assertMovieExists(movie);
    }

    private void assertMovieExists(Movie movie) {
        String json = movieResource.getMovieById(movie.getId());
        assertMovie(movie, gson.fromJson(json, Movie.class));
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
        Response response = movieResource.deleteMovie(movie.getId());
        assertEquals(OK, response.getStatus());
        assertMovieDoesNotExist(movie);
    }

    private void assertMovieDoesNotExist(Movie movie) {
        String json = movieResource.getMovieById(movie.getId());
        Movie movieFromResource = gson.fromJson(json, Movie.class);
        assertEquals(MovieRepository.UNKNOWN_MOVIE_ID, movieFromResource.getId());
    }

    private String getRandomString(int length) {
        if (length > 0) {
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            Random random = new Random();
            String string = "";
            for (int i = 0; i < length; i++) {
                int j = random.nextInt(alphabet.length());
                string += alphabet.charAt(j);
            }
            return string;
        } else {
            throw new IllegalArgumentException("Length of requested string must be greater than 0");
        }
    }

}
