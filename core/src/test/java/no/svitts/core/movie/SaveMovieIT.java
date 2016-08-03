package no.svitts.core.movie;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.file.MediaFile;
import no.svitts.core.json.GsonMessageBodyWriter;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static no.svitts.core.CoreTestKit.getITestApplication;
import static no.svitts.core.util.StringUtil.getRandomString;
import static org.junit.Assert.assertEquals;

public class SaveMovieIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private MovieBuilder movieBuilder;
    private GsonMessageBodyWriter gsonMessageBodyWriter;

    @Override
    protected Application configure() {
        return getITestApplication();
    }

    @Override
    @Before
    public void setUp() throws Exception {
        movieBuilder = new MovieBuilder();
        gsonMessageBodyWriter = new GsonMessageBodyWriter();
        super.setUp();
    }

    @Test
    public void saveMovie_NullableFieldsAreNull_ShouldReturnCreatedResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.imdbId(null).tagline(null).overview(null).releaseDate(null).genres(null).videoFile(null).posterImageFile(null).backdropImageFile(null).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(201, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieIdContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.id("#").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieIdIsTooLong_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.id(getRandomString(Movie.ID_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieNameIsNull_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.name(null).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieNameIsEmpty_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.name("").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieNameContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.name("#").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieNameIsTooLong_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.name(getRandomString(Movie.NAME_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieImdbIdContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.imdbId("#").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieImdbIdIsTooLong_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.imdbId(getRandomString(Movie.IMDB_ID_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieTaglineContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.tagline("#").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieTaglineIsTooLong_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.tagline(getRandomString(Movie.TAGLINE_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieOverviewContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.overview("#").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieOverviewIsTooLong_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.overview(getRandomString(Movie.OVERVIEW_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieRuntimeIsNegativeNumber_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.runtime(-1).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieVideoFilePathIsEmpty_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.videoFile(new MediaFile("")).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MoviePosterImageFilePathIsEmpty_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.posterImageFile(new MediaFile("")).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_MovieBackdropFilePathIsEmpty_ShouldReturnBadRequestResponse() {
        client().register(gsonMessageBodyWriter);
        Movie movie = movieBuilder.backdropImageFile(new MediaFile("")).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

}
