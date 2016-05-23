package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.criteria.SearchCriteria;
import no.svitts.core.criteria.SearchKey;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import no.svitts.core.status.ServerResponse;
import no.svitts.core.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "MovieResource")
@Path("movie")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource extends CoreResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieResource.class);

    private Repository<Movie> movieRepository;

    public MovieResource(Repository<Movie> movieRepository) {
        this.movieRepository = movieRepository;
    }

    @ApiOperation(value = "MovieResource", notes = "Lists a specific movie stored in the database as JSON. Invalid/non-existing ID will list an unknown movie", response = Response.class)
    @GET
    @Path("{id}")
    public Response getMovieById(@PathParam("id") String id) {
        LOGGER.info("Received request to GET movie with ID [{}]", id);
        ServerResponse serverResponse = movieRepository.getById(id);
        if (serverResponse.getStatus() == Status.OK && serverResponse.getPayload() != null) {
            return Response.ok().entity(gson.toJson(serverResponse.getPayload())).build();
        } else {
            return Response.serverError().entity(serverResponse.getMessage()).build();
        }

    }

    @GET
    @Path("name")
    public Response getMoviesByName(@QueryParam("name") String name, @QueryParam("limit") int limit) {
        LOGGER.info("Received request to GET max [{}] movie(s) with name [{}]", limit, name);
        List<Movie> movies = movieRepository.search(new SearchCriteria(SearchKey.NAME, name, limit));
        return Response.ok().entity(gson.toJson(movies)).build();
    }

    @GET
    @Path("genre")
    public Response getMoviesByGenre(@QueryParam("genre") String genre, @QueryParam("limit") int limit) {
        LOGGER.info("Received request to GET max [{}] movie(s) with genre [{}]", limit, genre);
        List<Movie> movies = movieRepository.search(new SearchCriteria(SearchKey.GENRE, genre, limit));
        return Response.ok().entity(gson.toJson(movies)).build();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovie(String json) {
        LOGGER.info("Received request to POST movie by json [{}]", json);
        Movie movie = gson.fromJson(json, Movie.class);
        return getResponse(movieRepository.insert(movie));
    }

    @PUT
    @Path("update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(@PathParam("id") String id, String json) {
        LOGGER.info("Received request to PUT movie by json [{}]", json);
        Movie movie = gson.fromJson(json, Movie.class);
        return getResponse(movieRepository.update(movie));
    }

    @DELETE
    @Path("delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMovie(@PathParam("id") String id) {
        LOGGER.info("Received request to DELETE movie with ID [{}]", id);
        return getResponse(movieRepository.delete(id));
    }

}