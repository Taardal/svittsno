package no.svitts.core.resource;

import com.google.inject.Inject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.constraint.Length;
import no.svitts.core.constraint.NonNegative;
import no.svitts.core.constraint.NotNullOrEmpty;
import no.svitts.core.constraint.ValidCharacters;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.search.Search;
import no.svitts.core.search.SearchKey;
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
            value = "Get a single movie by its ID",
            notes = "Requesting a movie that does not exist will generate a \"not found\" response. " +
                    "An invalid ID will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s). "
    )
    public Response getMovie(@PathParam("id") @ValidCharacters @Length(length = Movie.ID_MAX_LENGTH) String id) {
        LOGGER.info("Received request to GET movie with ID [{}]", id);
        try {
            Movie movie = movieService.get(id);
            return Response.ok(movie).build();
        } catch (ServiceException e) {
            throw new InternalServerErrorException("Could not get movie with ID [" + id + "].", e);
        }
    }

    @GET
    @Path("genres/{genre}")
    @ApiOperation(
            value = "Get movies by genre",
            notes = "If no movies are found in the database, an empty list will be returned. " +
                    "An invalid genre will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s). "
    )
    public Response getMoviesByGenre(
            @PathParam("genre") @ValidCharacters @Length(length = Genre.MAX_LENGTH) String genrePathParam,
            @QueryParam("limit") @DefaultValue("10") @NonNegative int limit,
            @QueryParam("offset") @DefaultValue("0") @NonNegative int offset
    ) {
        LOGGER.info("Received request to GET movie(s) with genre [{}] with limit [{}] and offset [{}]", genrePathParam, limit, offset);
        try {
            Genre genre = Genre.valueOf(genrePathParam.toUpperCase().replaceAll("-", "_").replaceAll(" ", "_"));
            Search search = new Search(genre.toString(), SearchKey.GENRE, limit, offset);
            List<Movie> movies = movieService.search(search);
            return Response.ok(movies.toArray()).build();
        } catch (ServiceException | IllegalArgumentException e) {
            throw new InternalServerErrorException("Could not get movies with genre [" + genrePathParam + "], limit [" + limit + "] and offset [" + offset + "].", e);
        }
    }

    @GET
    @Path("search")
    @ApiOperation(
            value = "Search movies by name",
            notes = "Searches the database for movie(s) with similar name to the query. " +
                    "If no movies are found in the database, an empty list will be returned. " +
                    "A query that is empty or contains illegal characters will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s). "
    )
    public Response search(
            @QueryParam("q") @NotNullOrEmpty @ValidCharacters String query,
            @QueryParam("limit") @DefaultValue("10") @NonNegative int limit,
            @QueryParam("offset") @DefaultValue("0") @NonNegative int offset
    ) {
        LOGGER.info("Received request to GET movie(s) by search query [{}] with limit [{}] and offset [{}]", query, limit, offset);
        try {
            Search search = new Search(query, SearchKey.TITLE, limit, offset);
            List<Movie> movies = movieService.search(search);
            return Response.ok(movies.toArray()).build();
        } catch (ServiceException e) {
            throw new InternalServerErrorException("Could not get movies by search query [" + query + "] with limit [" + limit + "] and offset [" + offset + "].", e);
        }
    }

    @POST
    @ApiOperation(
            value = "Create a single movie",
            notes = "Invalid JSON will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s). "
    )
    public Response saveMovie(@Valid Movie movie) {
        LOGGER.info("Received request to POST movie [{}]", movie.toString());
        try {
            String savedMovieId = movieService.save(movie);
            return Response.created(getLocation(savedMovieId)).build();
        } catch (ServiceException e) {
            throw new InternalServerErrorException("Could not save movie [" + movie.toString() + "]. ", e);
        }
    }

    @PUT
    @Path("{id}")
    @ApiOperation(
            value = "Update a single movie",
            notes = "Invalid JSON will generate a \"bad request\" response with a list of error messages to provide more details about the problem(s). "
    )
    public Response updateMovie(@PathParam("id") @ValidCharacters @Length(length = Movie.ID_MAX_LENGTH) String id, @Valid Movie movie) {
        LOGGER.info("Received request to PUT movie [{}]", movie);
        try {
            movieService.update(movie);
            return Response.ok().build();
        } catch (ServiceException e) {
            throw new InternalServerErrorException("Could not update movie with ID [" + id + "] with values [" + movie.toString() + "].", e);
        }
    }

    @DELETE
    @Path("{id}")
    @ApiOperation(
            value = "Delete a single movie by its ID",
            notes = "Invalid ID will generate a \"bad request\"-response with a list of error messages to provide more details about the problem(s). "
    )
    public Response deleteMovie(@PathParam("id") @ValidCharacters @Length(length = Movie.ID_MAX_LENGTH) String id) {
        LOGGER.info("Received request to DELETE movie with ID [{}]", id);
        try {
            movieService.delete(id);
            return Response.ok().build();
        } catch (ServiceException e) {
            throw new InternalServerErrorException("Could not delete movie with ID [" + id + "].", e);
        }
    }

    private URI getLocation(String id) {
        return uriInfo.getAbsolutePathBuilder().path(id).build();
    }

}