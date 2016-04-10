package no.svitts.core.resource;

import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/movie")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource extends SvittsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieResource.class);
    private MovieService movieService;

    public MovieResource(MovieService movieService) {
        this.movieService = movieService;
    }

    @GET
    @Path("/")
    public String getMovieByName(@QueryParam("name") String name) {
        LOGGER.info("Received request to GET movie with name {}", name);
        return gson.toJson(movieService.getMovieByName(name));
    }

    @GET
    @Path("/{id}")
    public String getMovieByID(@PathParam("id") String id) {
        LOGGER.info("Received request to GET movie with ID {}", id);
        return gson.toJson(movieService.getMovieById(id));
    }

    @GET
    @Path("/all")
    public String getAllMovies() {
        LOGGER.info("Received request to GET all movies");
        return gson.toJson(movieService.getAll());
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovie(String json) {
        LOGGER.info("Received request to CREATE movie by json {}", json);
        Movie movie = gson.fromJson(json, Movie.class);
        return movieService.createMovie(movie) ? Response.created(getURI("/" + movie.getId())).build() : Response.serverError().build();
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(@PathParam("id") String id, String json) {
        LOGGER.info("Received request to UPDATE movie by json {}", json);
        Movie movie = gson.fromJson(json, Movie.class);
        return getResponse(movieService.updateMovie(movie));
    }

    @PUT
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMovie(@PathParam("id") String id) {
        LOGGER.info("Received request to DELETE movie with ID {}", id);
        return getResponse(movieService.deleteMovie(id));
    }

    private URI getURI(String path) {
        try {
            return new URI(path);
        } catch (URISyntaxException e) {
            LOGGER.error("Could not create URI", e);
            return getURI("");
        }
    }
}