package no.svitts.core.movie;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.application.ApplicationProperties;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.DataSourceConfig;
import no.svitts.core.datasource.SqlDataSource;
import no.svitts.core.gson.deserializer.MovieDeserializer;
import no.svitts.core.gson.serializer.MovieSerializer;
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

//    @Override
//    public Application configure() {
//
//        ResourceConfig resourceConfig = new ResourceConfig();
//        resourceConfig.register(movieResource);
//        return resourceConfig;
//    }

//    @Test
//    public void createMovie_ValidMovie_ShouldBeInsertedIntoDatabaseAndReturnServerOk() {
//        Movie movie = movieTestDataBuilder.sherlockHolmes().build();
//        Entity<String> jsonEntity = Entity.entity(gson.toJson(movie), MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("create").request().post(jsonEntity, Response.class);
//
//        assertEquals(200, response.getStatus());
//        assertMovieExists(movie);
//        deleteMovie(movie);
//    }

    @Test
    public void createMovie_TooLongName_ShouldHandleSQLExceptionAndReturnServerError() {
        Movie movie = movieTestDataBuilder.name(getRandomString(256)).build();
        Response response = movieResource.createMovie(gson.toJson(movie));
        assertEquals(500, response.getStatus());
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

//    private void assertMovieExists(Movie movie) {
//        Response response = target(MOVIE_RESOURCE).path(movie.getId()).request(MediaType.APPLICATION_JSON).get();
//        assertEquals(200, response.getStatus());
//        Movie movieFromDataSource = gson.fromJson(response.readEntity(String.class), Movie.class);
//        assertMovie(movie, movieFromDataSource);
//    }

    private void assertMovie(Movie expectedMovie, Movie actualMovie) {
        assertEquals(expectedMovie.getId(), actualMovie.getId());
        assertEquals(expectedMovie.getName(), actualMovie.getName());
        assertEquals(expectedMovie.getImdbId(), actualMovie.getImdbId());
        assertEquals(expectedMovie.getTagline(), actualMovie.getTagline());
        assertEquals(expectedMovie.getOverview(), actualMovie.getOverview());
        assertEquals(expectedMovie.getRuntime(), actualMovie.getRuntime());
        assertEquals(expectedMovie.getReleaseDate().getTime(), actualMovie.getReleaseDate().getTime());
        assertTrue(expectedMovie.getGenres().size() == actualMovie.getGenres().size());
        expectedMovie.getGenres().stream().forEach(genre -> assertTrue(actualMovie.getGenres().contains(genre)));
    }

//    private void deleteMovie(Movie movie) {
//        Response response = target(MOVIE_RESOURCE).path("delete").path(movie.getId()).request().delete();
//        assertEquals(200, response.getStatus());
//        assertMovieDoesNotExist(movie);
//    }
//
//    private void assertMovieDoesNotExist(Movie movie) {
//        Response response = target(MOVIE_RESOURCE).path(movie.getId()).request(MediaType.APPLICATION_JSON).get();
//        assertEquals(200, response.getStatus());
//        Movie movieFromDataSource = gson.fromJson(response.readEntity(String.class), Movie.class);
//        assertEquals(MovieRepository.UNKNOWN_MOVIE_ID, movieFromDataSource.getId());
//    }

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
