package no.svitts.core.movie;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.file.SubtitleFile;
import no.svitts.core.file.VideoFile;
import no.svitts.core.genre.Genre;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

import static no.svitts.core.CoreTestKit.getITestApplication;
import static no.svitts.core.util.StringUtil.getRandomString;
import static org.junit.Assert.assertEquals;

public class SaveMovieIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private MovieBuilder movieBuilder;

    @Override
    protected Application configure() {
        return getITestApplication();
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
    public void saveMovie_NullableFieldsAreNull_ShouldReturnCreatedResponse() {
        Movie movie = movieBuilder
                .testMovie()
                .imdbId(null)
                .tagline(null)
                .overview(null)
                .language(null)
                .edition(null)
                .releaseDate(null)
                .genres((Set<Genre>) null)
                .videoFile(null)
                .subtitleFiles(null)
                .posterPath(null)
                .backdropPath(null)
                .build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(201, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_IdContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().id("#").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_IdIsTooLong_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().id(getRandomString(Movie.ID_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_TitleIsNull_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().title(null).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_TitleIsEmpty_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().title("").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_TitleContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().title("#").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_TitleIsTooLong_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().title(getRandomString(Movie.TITLE_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_ImdbIdContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().imdbId("#").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_ImdbIdIsTooLong_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().imdbId(getRandomString(Movie.IMDB_ID_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_TaglineContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().tagline("#").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_TaglineIsTooLong_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().tagline(getRandomString(Movie.TAGLINE_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_OverviewContainsIllegalCharacters_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().overview("#").build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_OverviewIsTooLong_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().overview(getRandomString(Movie.OVERVIEW_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_LanguageIsTooLong_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().language(getRandomString(Movie.LANGUAGE_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_EditionIsTooLong_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().edition(getRandomString(Movie.EDITION_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_RuntimeIsNegativeNumber_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().runtime(-1).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_VideoFilePathIsEmpty_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().videoFile(new VideoFile("", "videoFormat", "audioFormat")).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_VideoFileVideoFormatIsTooLong_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().videoFile(new VideoFile("path", getRandomString(VideoFile.VIDEO_FORMAT_MAX_LENGTH + 1), "audioFormat")).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_VideoFileAudioFormatIsTooLong_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().videoFile(new VideoFile("path", "videoFormat", getRandomString(VideoFile.AUDIO_FORMAT_MAX_LENGTH + 1))).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_SubtitleFilePathIsEmpty_ShouldReturnBadRequestResponse() {
        Set<SubtitleFile> subtitleFiles = new HashSet<>();
        subtitleFiles.add(new SubtitleFile("", "language"));
        Movie movie = movieBuilder.testMovie().subtitleFiles(subtitleFiles).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_SubtitleFileLanguageIsTooLong_ShouldReturnBadRequestResponse() {
        Set<SubtitleFile> subtitleFiles = new HashSet<>();
        subtitleFiles.add(new SubtitleFile("path", getRandomString(SubtitleFile.LANGUAGE_MAX_LENGTH + 1)));
        Movie movie = movieBuilder.testMovie().subtitleFiles(subtitleFiles).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_PosterPathIsTooLong_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().posterPath(getRandomString(Movie.POSTER_PATH_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void saveMovie_BackdropPathIsTooLong_ShouldReturnBadRequestResponse() {
        Movie movie = movieBuilder.testMovie().backdropPath(getRandomString(Movie.BACKDROP_PATH_MAX_LENGTH + 1)).build();
        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);

        assertEquals(400, response.getStatus());
        response.close();
    }
}
