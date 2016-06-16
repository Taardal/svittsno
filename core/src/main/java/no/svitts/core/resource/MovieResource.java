package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.constraint.*;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.movie.Movie;
import no.svitts.core.search.Criteria;
import no.svitts.core.service.Service;
import no.svitts.core.util.Id;
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

    private Service<Movie> movieService;

    public MovieResource(Service<Movie> movieService) {
        this.movieService = movieService;
    }

    @ApiOperation(
            value = "Get a single movie by its ID.",
            notes = "Requesting a movie that does not exist will generate a \"not found\" response." +
                    "An invalid ID will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s)."
    )
    @GET
    @Path("{id}")
    public Response getMovie(@ValidId @PathParam("id") String id) {
        LOGGER.info("Received request to GET movie with ID [{}]", id);
        try {
            Movie movie = movieService.getOne(id);
            return Response.ok().entity(movie).build();
        } catch (ServiceException e) {
            throw new InternalServerErrorException("Could not get movie with ID [" + id + "]. This is most likely due to an unavailable data source or an invalid request to the database.", e);
        }
    }

    @ApiOperation(
            value = "Get multiple movies.",
            notes = "Results can be narrowed down with the \"name\" and \"genre\" parameters. If no name or genre is specified, the server returns all movies." +
                    "Number of results can be limited with the \"limit\" parameter." +
                    "Pagination can be achieved with the \"offset\" parameter." +
                    "Invalid parameter(s) will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s)."
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
            List<Movie> movies = movieService.getByCriteria(new Criteria());
            return Response.ok().entity(movies).build();
        } catch (RepositoryException e) {
            throw new InternalServerErrorException("Could not get movies with name [" + name + "] and genre [" + genre + "] with limit [" + limit + "] and offset [" + offset + "]. This is most likely due to an unavailable data source or an invalid request to the database.", e);
        }
    }

    @ApiOperation(
            value = "Create a single movie",
            notes = "Invalid JSON will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s)."
    )
    @POST
    public Response createMovie(@ValidMovie Movie movie) {
        LOGGER.info("Received request to POST movie [{}]", movie.toString());
        try {
            Id id = movieService.save(movie);
            return Response.created(getLocation(id)).build();
        } catch (RepositoryException e) {
            throw new InternalServerErrorException("Could not save movie [" + movie.toString() + "]. This is most likely due to an unavailable data source or an invalid request to the database.", e);
        }
    }

    @ApiOperation(
            value = "Update a single movie.",
            notes = "Invalid JSON will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s)."
    )
    @PUT
    @Path("{id}")
    public Response updateMovie(@ValidId @PathParam("id") String id, @ValidMovie Movie movie) {
        LOGGER.info("Received request to PUT movie [{}]", movie);
        try {
            movieService.save(movie);
            return Response.ok().build();
        } catch (RepositoryException e) {
            throw new InternalServerErrorException("Could not update movie with ID [" + id + "] with values [" + movie.toString() + "]. This is most likely due to an unavailable data source or an invalid request to the database.", e);
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
            movieService.delete(id);
            return Response.ok().build();
        } catch (RepositoryException e) {
            throw new InternalServerErrorException("Could not delete movie with ID [" + id + "]. This is most likely due to an unavailable data source or an invalid request to the database.", e);
        }
    }

    private URI getLocation(Id id) {
        return uriInfo.getAbsolutePathBuilder().path(id.toString()).build();
    }

}