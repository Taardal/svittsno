package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "MovieResource")
@Path("/movie")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource extends CoreResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieResource.class);
    private Repository<Movie> movieRepository;

    public MovieResource(Repository<Movie> movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GET
    public String getMovieByName(@QueryParam("name") String name) {
        LOGGER.info("Received request to GET movie with name {}", name);
        return gson.toJson(movieRepository.getByAttributes(name));
    }

    @ApiOperation(value = "Foo", notes = "Bar", response = Response.class)
    @GET
    @Path("/{id}")
    public String getMovieByID(@PathParam("id") String id) {
        LOGGER.info("Received request to GET movie with ID {}", id);
        return gson.toJson(movieRepository.getById(id));
    }

    @GET
    @Path("/all")
    public String getAllMovies() {
        LOGGER.info("Received request to GET all movies");
        return gson.toJson(movieRepository.getAll());
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovie(String json) {
        LOGGER.info("Received request to CREATE movie by json {}", json);
        Movie movie = gson.fromJson(json, Movie.class);
        return getResponse(movieRepository.insertSingle(movie));
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(@PathParam("id") String id, String json) {
        LOGGER.info("Received request to UPDATE movie by json {}", json);
        Movie movie = gson.fromJson(json, Movie.class);
        return getResponse(movieRepository.updateSingle(movie));
    }

    @DELETE
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMovie(@PathParam("id") String id) {
        LOGGER.info("Received request to DELETE movie with ID {}", id);
        return getResponse(movieRepository.deleteSingle(id));
    }

}