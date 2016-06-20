package no.svitts.core.movie;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.datasource.DataSource;
import org.glassfish.jersey.test.JerseyTest;

public class CreateMovieIT extends JerseyTest {

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


//    @Test
//    public void createMovie_IdTooLong_ShouldAbortAndReturnServerError() {
//        Movie movie = movieBuilder.id(getRandomString(ID_MAX_LENGTH + 1)).build();
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("save").request().post(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void createMovie_NameTooLong_ShouldAbortAndReturnServerError() {
//        Movie movie = movieBuilder.name(getRandomString(NAME_MAX_LENGTH + 1)).build();
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("save").request().post(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void createMovie_ImdbIdTooLong_ShouldAbortAndReturnServerError() {
//        Movie movie = movieBuilder.imdbId(getRandomString(IMDB_ID_MAX_LENGTH + 1)).build();
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("save").request().post(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void createMovie_TaglineTooLong_ShouldAbortAndReturnServerError() {
//        Movie movie = movieBuilder.tagline(getRandomString(TAGLINE_MAX_LENGTH + 1)).build();
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("save").request().post(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void createMovie_OverviewTooLong_ShouldAbortAndReturnServerError() {
//        Movie movie = movieBuilder.overview(getRandomString(OVERVIEW_MAX_LENGTH + 1)).build();
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("save").request().post(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void createMovie_StringFieldsMaximumLength_ShouldReturnOk() {
//        Movie movie = movieBuilder
//                .name(getRandomString(ID_MAX_LENGTH))
//                .imdbId(getRandomString(IMDB_ID_MAX_LENGTH))
//                .tagline(getRandomString(TAGLINE_MAX_LENGTH))
//                .overview(getRandomString(OVERVIEW_MAX_LENGTH))
//                .build();
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("save").request().post(movieEntity);
//        assertEquals(200, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void createMovie_RequiredFieldsAreNull_ShouldAbortAndReturnServerError() {
//        Movie movie = movieBuilder.id(null).name(null).build();
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("save").request().post(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void createMovie_OptionalFieldsAreNull_ShouldReturnOk() {
//        Movie movie = movieBuilder.imdbId(null).tagline(null).overview(null).releaseDate(null).build();
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("save").request().post(movieEntity);
//
//        assertEquals(200, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void updateMovie_MovieDoesNotExist_ShouldAbortAndReturnServerError() {
//        Movie movie = new MovieBuilder().build();
//        Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
//
//        Response response = target(MOVIE_RESOURCE).path("updateSingle").path(movie.getId()).request().put(movieEntity);
//
//        assertEquals(500, response.getStatus());
//        response.close();
//    }

}
