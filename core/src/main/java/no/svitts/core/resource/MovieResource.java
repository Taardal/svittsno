package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.search.SearchCriteria;
import no.svitts.core.search.SearchKey;
import no.svitts.core.exception.NoChangeException;
import no.svitts.core.exception.RequiredFieldsInvalidException;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Api(value = "MovieResource")
@Path("movies")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource extends CoreResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieResource.class);

    private MovieService movieService;

    public MovieResource(MovieService movieService) {
        this.movieService = movieService;
    }

    @ApiOperation(value = "MovieResource", notes = "Lists a specific movie stored in the database as JSON. Invalid/non-existing ID will give 'required fields invalid' error.", response = Response.class)
    @GET
    @Path("{id}")
    public Response getMovie(@PathParam("id") String id) throws RequiredFieldsInvalidException {
        LOGGER.info("Received request to GET movie with ID [{}]", id);
        Movie movie = movieService.getMovie(id);
        return Response.ok().entity(gson.toJson(movie)).build();
    }

    @GET
    @Path("name")
    public Response getMoviesByName(@QueryParam("name") String name, @QueryParam("limit") int limit) throws RequiredFieldsInvalidException {
        LOGGER.info("Received request to GET max [{}] movie(s) with name [{}]", limit, name);
        SearchCriteria searchCriteria = new SearchCriteria(SearchKey.NAME, name, limit);
        List<Movie> movies = movieService.getMovies(searchCriteria);
        return Response.ok().entity(gson.toJson(movies)).build();
    }

    @GET
    @Path("genre")
    public Response getMoviesByGenre(@QueryParam("genre") String genre, @QueryParam("limit") int limit) throws RequiredFieldsInvalidException {
        LOGGER.info("Received request to GET max [{}] movie(s) with genre [{}]", limit, genre);
        SearchCriteria searchCriteria = new SearchCriteria(SearchKey.GENRE, genre, limit);
        List<Movie> movies = movieService.getMovies(searchCriteria);
        return Response.ok().entity(gson.toJson(movies)).build();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovie(String json) throws NoChangeException, RequiredFieldsInvalidException {
        LOGGER.info("Received request to POST movie by json [{}]", json);
        String insertedMovieId = movieService.createMovie(gson.fromJson(json, Movie.class));
        return Response.created(getLocation(insertedMovieId)).build();
    }

    @PUT
    @Path("update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(@PathParam("id") String id, String json) throws NoChangeException, RequiredFieldsInvalidException {
        LOGGER.info("Received request to PUT movie by json [{}]", json);
        movieService.updateMovie(gson.fromJson(json, Movie.class));
        return Response.ok().build();
    }

    @DELETE
    @Path("delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMovie(@PathParam("id") String id) throws NoChangeException, RequiredFieldsInvalidException {
        LOGGER.info("Received request to DELETE movie with ID [{}]", id);
        movieService.deleteMovie(id);
        return Response.ok().build();
    }

    private URI getLocation(String id) {
        try {
            return new URI(id);
        } catch (URISyntaxException e) {
            LOGGER.warn("Could not create URI for location of inserted movie with ID {[}]", id);
            return null;
        }
    }

}