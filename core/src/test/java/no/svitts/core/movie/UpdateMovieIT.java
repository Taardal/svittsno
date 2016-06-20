package no.svitts.core.movie;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.datasource.DataSource;
import org.glassfish.jersey.test.JerseyTest;

public class UpdateMovieIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private DataSource dataSource;
    private MovieBuilder movieBuilder;

//    @Override
//    protected Application configure() {
//        dataSource = getDataSource();
//        ResourceConfig resourceConfig = new ResourceConfig();
//        return resourceConfig;
//    }
//
//    @Override
//    @Before
//    public void setUp() throws Exception {
//        movieBuilder = new MovieBuilder();
//        super.setUp();
//    }
//
//    @Override
//    @After
//    public void tearDown() throws Exception {
//        dataSource.close();
//        super.tearDown();
//    }
//
//    @Test
//    public void updateMovie_NameTooLong_ShouldAbortAndReturnServerError() {
//        Movie movie = movieBuilder.build();
//        movie.setName(getRandomString(NAME_MAX_LENGTH + 1));
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("updateSingle").path(movie.getId()).request().put(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void updateMovie_ImdbIdTooLong_ShouldAbortAndReturnServerError() {
//        Movie movie = movieBuilder.build();
//        movie.setImdbId(getRandomString(IMDB_ID_MAX_LENGTH + 1));
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("updateSingle").path(movie.getId()).request().put(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void updateMovie_TaglineTooLong_ShouldAbortAndReturnServerError() {
//        Movie movie = movieBuilder.build();
//        movie.setTagline(getRandomString(TAGLINE_MAX_LENGTH + 1));
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("updateSingle").path(movie.getId()).request().put(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void updateMovie_OverviewTooLong_ShouldAbortAndReturnServerError() {
//        Movie movie = movieBuilder.build();
//        movie.setOverview(getRandomString(OVERVIEW_MAX_LENGTH + 1));
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("updateSingle").path(movie.getId()).request().put(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void updateMovie_StringFieldsMaximumLength_ShouldReturnOk() {
//        Movie movie = movieBuilder.build();
//        movie.setName(getRandomString(ID_MAX_LENGTH));
//        movie.setImdbId(getRandomString(IMDB_ID_MAX_LENGTH));
//        movie.setTagline(getRandomString(TAGLINE_MAX_LENGTH));
//        movie.setOverview(getRandomString(OVERVIEW_MAX_LENGTH));
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("updateSingle").path(movie.getId()).request().put(movieEntity);
//
//        assertEquals(200, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void updateMovie_RequiredFieldsAreNull_ShouldAbortAndReturnServerError() {
//        Movie movie = movieBuilder.build();
//        movie.setName(null);
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("updateSingle").path(movie.getId()).request().put(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void updateMovie_OptionalFieldsAreNull_ShouldReturnOk() {
//        Movie movie = movieBuilder.build();
//        movie.setImdbId(null);
//        movie.setTagline(null);
//        movie.setOverview(null);
//        movie.setReleaseDate(null);
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("updateSingle").path(movie.getId()).request().put(movieEntity);
//
//        assertEquals(200, response.getStatus());
//        response.close();
//    }

}
