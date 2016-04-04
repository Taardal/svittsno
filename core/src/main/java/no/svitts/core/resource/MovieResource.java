package no.svitts.core.resource;

import com.google.gson.Gson;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/movie")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource extends SvittsResource {

    private static final Logger LOGGER = Logger.getLogger(MovieResource.class.getName());
    private MovieService movieService;
    private Gson gson;

    public MovieResource(MovieService movieService) {
        this.movieService = movieService;
        gson = new Gson();
    }

    @GET
    @Path("/all")
    public String getAllMovies() {
        LOGGER.log(Level.INFO, "Received request to GET all movies");
        return gson.toJson(movieService.getAll());
    }

    @GET
    @Path("/{id}")
    public String getMovie(@PathParam("id") String id) {
        LOGGER.log(Level.INFO, "Received request to GET movie with ID [" + id + "]");
        return gson.toJson(movieService.getMovie(id));
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovie(String json) {
        LOGGER.log(Level.INFO, "Received request to CREATE movie by JSON [" + json + "]");
        Movie movie = gson.fromJson(json, Movie.class);
        return getRespone(movieService.createMovie(movie));
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(@PathParam("id") String id, String json) {
        LOGGER.log(Level.INFO, "Received request to UPDATE movie by JSON [" + json + "]");
        Movie movie = gson.fromJson(json, Movie.class);
        return getRespone(movieService.updateMovie(movie));
    }

    @PUT
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMovie(@PathParam("id") String id) {
        LOGGER.log(Level.INFO, "Received request to DELETE movie with ID [" + id + "]");
        return getRespone(movieService.deleteMovie(id));
    }
}