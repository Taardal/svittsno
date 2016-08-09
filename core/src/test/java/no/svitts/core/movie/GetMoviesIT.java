package no.svitts.core.movie;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.genre.Genre;
import no.svitts.core.util.Id;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static no.svitts.core.CoreTestKit.getITestApplication;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetMoviesIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private MovieBuilder movieBuilder;
    private GsonMessageBodyReader gsonMessageBodyReader;
    private GsonMessageBodyWriter gsonMessageBodyWriter;

    @Override
    protected Application configure() {
        return getITestApplication();
    }

    @Override
    @Before
    public void setUp() throws Exception {
        movieBuilder = new MovieBuilder();
        gsonMessageBodyReader = new GsonMessageBodyReader();
        gsonMessageBodyWriter = new GsonMessageBodyWriter();
        super.setUp();
    }

    @Test
    public void getMovies_NoMoviesExist_ShouldReturnOkResponseWithEmptyArray() {
        client().register(gsonMessageBodyReader);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(0, response.readEntity(Movie[].class).length);
        response.close();
    }

    @Test
    public void getMovies_NoQueryParams_ShouldReturnAllMovies() throws IOException {
        client().register(gsonMessageBodyReader).register(gsonMessageBodyWriter);
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.id(Id.get()).build());
        movies.add(movieBuilder.id(Id.get()).build());
        createMovies(movies);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(2, response.readEntity(Movie[].class).length);
        response.close();
    }

    @Test
    public void getMovies_LimitSmallerThanNumberOfMoviesInDatabase_ShouldReturnNumberOfMoviesEqualToLimit() throws IOException {
        client().register(gsonMessageBodyReader).register(gsonMessageBodyWriter);
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.id(Id.get()).build());
        movies.add(movieBuilder.id(Id.get()).build());
        createMovies(movies);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).queryParam("limit", 1).request().get();

        assertEquals(200, response.getStatus());
        assertEquals(1, response.readEntity(Movie[].class).length);
        response.close();
    }


    @Test
    public void getMovies_OffsetGreaterThanZero_ShouldReturnArrayWhereFirstMovieInArrayIsMovieInsertedAtOffsetIndex() throws IOException {
        client().register(gsonMessageBodyReader).register(gsonMessageBodyWriter);
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.id(Id.get()).build());
        movies.add(movieBuilder.id(Id.get()).build());
        createMovies(movies);

        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).queryParam("offset", 1).request().get();

        assertEquals(200, response.getStatus());
        Movie[] moviesFromResource = response.readEntity(Movie[].class);
        assertEquals(1, moviesFromResource.length);
        assertEquals(movies.get(1), moviesFromResource[0]);
        response.close();
    }

    @Test
    public void getMovies_NameSpecified_ShouldReturnMoviesWithNameLikeSpecifiedName() throws IOException {
        client().register(gsonMessageBodyReader).register(gsonMessageBodyWriter);
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.id(Id.get()).name("Iron Man").build());
        movies.add(movieBuilder.id(Id.get()).name("Iron Man 2").build());
        movies.add(movieBuilder.id(Id.get()).name("Iron Sky").build());
        movies.add(movieBuilder.id(Id.get()).name("Batman").build());
        createMovies(movies);

        String nameQueryParam = "Iron";
        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).queryParam("name", nameQueryParam).request().get();

        assertEquals(200, response.getStatus());
        Movie[] moviesFromResource = response.readEntity(Movie[].class);
        assertEquals(3, moviesFromResource.length);
        for (Movie movie : moviesFromResource) {
            assertTrue(movie.getName().contains(nameQueryParam));
        }
        response.close();
    }

    @Test
    public void getMovies_GenreSpecified_ShouldReturnMoviesWithGenreLikeSpecifiedGenre() throws IOException {
        client().register(gsonMessageBodyReader).register(gsonMessageBodyWriter);
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.id(Id.get()).genres(Arrays.stream(new Genre[]{Genre.WESTERN, Genre.WAR, Genre.THRILLER}).collect(Collectors.toSet())).build());
        movies.add(movieBuilder.id(Id.get()).genres(Arrays.stream(new Genre[]{Genre.WESTERN, Genre.ADVENTURE, Genre.ANIMATION}).collect(Collectors.toSet())).build());
        movies.add(movieBuilder.id(Id.get()).genres(Arrays.stream(new Genre[]{Genre.FAMILY, Genre.FANTASY, Genre.FILM_NOIR}).collect(Collectors.toSet())).build());
        createMovies(movies);

        String genreQueryParam = Genre.WESTERN.getValue();
        Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).queryParam("genre", genreQueryParam).request().get();

        assertEquals(200, response.getStatus());
        Movie[] moviesFromResource = response.readEntity(Movie[].class);
        assertEquals(2, moviesFromResource.length);
        Arrays.stream(moviesFromResource)
                .forEach(movie -> assertTrue(movie.getGenres().stream()
                        .anyMatch(genre -> genre.toString().contains(genreQueryParam.toUpperCase()))));
        response.close();
    }

    private void createMovies(List<Movie> movies) {
        for (Movie movie : movies) {
            Entity<Movie> movieEntity = Entity.entity(movie, MediaType.APPLICATION_JSON);
            Response response = client().target(getBaseUri()).path(MOVIE_RESOURCE).request().post(movieEntity, Response.class);
            assertEquals(201, response.getStatus());
            response.close();
        }
    }

}
