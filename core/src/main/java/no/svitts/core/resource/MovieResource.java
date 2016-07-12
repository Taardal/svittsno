package no.svitts.core.resource;

import com.google.inject.Inject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.constraint.Length;
import no.svitts.core.constraint.NonNegative;
import no.svitts.core.constraint.ValidCharacters;
import no.svitts.core.criteria.Criteria;
import no.svitts.core.criteria.CriteriaKey;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Movie resource", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
public class MovieResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieResource.class);

    private Service<Movie> movieService;

    @Context
    private UriInfo uriInfo;

    @Inject
    public MovieResource(Service<Movie> movieService) {
        this.movieService = movieService;
    }

    @GET
    @Path("{id}")
    @ApiOperation(
            value = "Get a single movie by its ID. ",
            notes = "Requesting a movie that does not exist will generate a \"not found\" response. " +
                    "An invalid ID will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s). "
    )
    public Response getMovie(@PathParam("id") @ValidCharacters @Length(length = Movie.ID_MAX_LENGTH) String id) {
        LOGGER.info("Received request to GET movie with ID [{}]", id);
        try {
            Movie movie = movieService.getSingle(id);
            return Response.ok(movie, MediaType.APPLICATION_JSON).build();
        } catch (ServiceException e) {
            throw new InternalServerErrorException("Could not get movie with ID [" + id + "]. This is most likely due to an unavailable data source or an invalid request to the database.", e);
        }
    }

    @GET
    @ApiOperation(
            value = "Get multiple movies. ",
            notes = "Results can be narrowed down with the \"name\" and \"genre\" parameters. If no name or genre is specified, the server returns all movies. " +
                    "Number of results can be limited with the \"limit\" parameter. " +
                    "Pagination can be achieved with the \"offset\" parameter. " +
                    "Invalid parameter(s) will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s). "
    )
    public Response getMovies(
            @QueryParam("name") @DefaultValue("") @ValidCharacters @Length(length = Movie.NAME_MAX_LENGTH) String name,
            @QueryParam("genre") @DefaultValue("") @ValidCharacters @Length(length = Genre.MAX_LENGTH) String genre,
            @QueryParam("limit") @DefaultValue("10") @NonNegative int limit,
            @QueryParam("offset") @DefaultValue("0") @NonNegative int offset
    ) {
        LOGGER.info("Received request to GET movie(s) with name [{}] and genre [{}] with limit [{}] and offset [{}]", name, genre, limit, offset);
        try {
            Criteria criteria = getCriteria(name, genre, limit, offset);
            List<Movie> movies = movieService.getMultiple(criteria);
            return Response.ok(movies.toArray(), MediaType.APPLICATION_JSON).build();
        } catch (RepositoryException e) {
            throw new InternalServerErrorException("Could not get movies with name [" + name + "] and genre [" + genre + "] with limit [" + limit + "] and offset [" + offset + "]. This is most likely due to an unavailable data source or an invalid request to the database.", e);
        }
    }

    @POST
    @ApiOperation(
            value = "Create a single movie. ",
            notes = "Invalid JSON will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s). "
    )
    public Response createMovie(@Valid Movie movie) {
        LOGGER.info("Received request to POST movie [{}]", movie.toString());
        try {
            String createdMovieId = movieService.saveSingle(movie);
            return Response.created(getLocation(createdMovieId)).build();
        } catch (ServiceException e) {
            throw new InternalServerErrorException("Could not saveSingle movie [" + movie.toString() + "]. ", e);
        }
    }

    @PUT
    @Path("{id}")
    @ApiOperation(
            value = "Update a single movie. ",
            notes = "Invalid JSON will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s). "
    )
    public Response updateMovie(@PathParam("id") @ValidCharacters @Length(length = Movie.ID_MAX_LENGTH) String id, @Valid Movie movie) {
        LOGGER.info("Received request to PUT movie [{}]", movie);
        try {
            movieService.updateSingle(movie);
            return Response.ok().build();
        } catch (ServiceException e) {
            throw new InternalServerErrorException("Could not update movie with ID [" + id + "] with values [" + movie.toString() + "]. This is most likely due to an unavailable data source or an invalid request to the database.", e);
        }
    }

    @DELETE
    @Path("{id}")
    @ApiOperation(
            value = "Delete a single movie by its ID. ",
            notes = "Invalid ID will generate a \"bad request\"-response with a list of error messages to provide more details about the problem(s). "
    )
    public Response deleteMovie(@PathParam("id") @ValidCharacters @Length(length = Movie.ID_MAX_LENGTH) String id) {
        LOGGER.info("Received request to DELETE movie with ID [{}]", id);
        try {
            movieService.deleteSingle(id);
            return Response.ok().build();
        } catch (ServiceException e) {
            throw new InternalServerErrorException("Could not delete movie with ID [" + id + "]. This is most likely due to an unavailable data source or an invalid request to the database.", e);
        }
    }

    private Criteria getCriteria(String name, String genre, int limit, int offset) {
        Criteria criteria = new Criteria();
        criteria.add(CriteriaKey.NAME, name);
        criteria.add(CriteriaKey.GENRE, genre);
        criteria.setLimit(limit);
        criteria.setOffset(offset);
        return criteria;
    }

    private URI getLocation(String id) {
        return uriInfo.getAbsolutePathBuilder().path(id).build();
    }

}