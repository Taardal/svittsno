package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.constraint.*;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Api(value = "Movie resource", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
@Path("movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieResource.class);

    @Context
    private UriInfo uriInfo;

    private MovieService movieService;

    public MovieResource(MovieService movieService) {
        this.movieService = movieService;
    }

    @ApiOperation(
            value = "Get a single movie by its ID.",
            notes = "Requesting a movie that does not exist will generate a \"not found\"-response.\n" +
                    "An invalid ID will generate a \"bad request\"-response with a list of error messages to provide more details about the problem(s)."
    )
    @GET
    @Path("{id}")
    public Response getMovie(@ValidId @PathParam("id") String id) {
        LOGGER.info("Received request to GET movie with ID [{}]", id);
        try {
            Movie movie = movieService.getMovie(id);
            if (movie != null) {
                return Response.ok().entity(movie).build();
            } else {
                throw new NotFoundException("Could not find movie with ID [" + id + "]");
            }
        } catch (RepositoryException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @ApiOperation(
            value = "Get multiple movies.",
            notes = "Number of results can be limited with the \"limit\" parameter. Default value = 10.\n" +
                    "Pagination can be achieved with the \"offset\" parameter. Default value = 0.\n" +
                    "Results can be narrowed down with the \"name\" and \"genre\" parameters. If no name or genre is specified, the server returns all movies (Limited by the \"limit\" parameter.\n" +
                    "Invalid parameter(s) will generate a \"bad request\"-response with a list of error messages to provide more details about the problem(s)."
    )
    @GET
    public Response getMovies(
            @ValidName @QueryParam("name") String name,
            @ValidGenre @QueryParam("genre") String genre,
            @ValidLimit @QueryParam("limit") @DefaultValue("10") int limit,
            @ValidOffset @QueryParam("offset") @DefaultValue("0") int offset
    ) {
        LOGGER.info("Received request to GET movie(s) with name [{}] and genre [{}] with limit [{}] and offset [{}]", name, genre, limit, offset);
        try {
            List<Movie> movies = movieService.getMovies(name, genre, limit, offset);
            return Response.ok().entity(movies).build();
        } catch (RepositoryException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @ApiOperation(
            value = "Create a single movie",
            notes = "Invalid JSON will generate a \"bad request\"-response with a list of error messages to provide more details about the problem(s)."
    )
    @POST
    public Response createMovie(@ValidMovie Movie movie) {
        LOGGER.info("Received request to POST movie [{}]", movie.toString());
        try {
            String insertedMovieId = movieService.createMovie(movie);
            return Response.created(getLocation(insertedMovieId)).build();
        } catch (RepositoryException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @ApiOperation(
            value = "Update a single movie.",
            notes = "Invalid JSON will generate a \"bad request\"-response with a list of error messages to provide more details about the problem(s)."
    )
    @PUT
    @Path("{id}")
    public Response updateMovie(@ValidId @PathParam("id") String id, @ValidMovie Movie movie) {
        LOGGER.info("Received request to PUT movie [{}]", movie);
        if (movieService.getMovie(id) == null) {
            throw new NotFoundException("Could update movie with [" + id + "] because it does not exist. Please create it first using a POST-request.");
        }
        try {
            movieService.updateMovie(movie);
            return Response.ok().build();
        } catch (RepositoryException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @ApiOperation(
            value = "Delete a single movie by its ID.",
            notes = "Invalid ID will generate a \"bad request\"-response with a list of error messages to provide more details about the problem(s)."
    )
    @DELETE
    @Path("{id}")
    public Response deleteMovie(@ValidId @PathParam("id") String id) {
        LOGGER.info("Received request to DELETE movie with ID [{}]", id);
        try {
            movieService.deleteMovie(id);
            return Response.ok().build();
        } catch (RepositoryException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    private URI getLocation(String id) {
        return uriInfo.getAbsolutePathBuilder().path(id).build();
    }

}