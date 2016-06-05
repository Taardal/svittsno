package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.error.ErrorMessage;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.movie.Movie;
import no.svitts.core.search.Criteria;
import no.svitts.core.search.SearchKey;
import no.svitts.core.service.MovieService;
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

@Api(value = "MovieResource")
@Path("movies")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource extends CoreResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieResource.class);

    @Context
    private UriInfo uriInfo;

    private MovieService movieService;

    public MovieResource(MovieService movieService) {
        this.movieService = movieService;
    }

    @ApiOperation(value = "Get movie by ID", notes = "Lists a specific movie as JSON. Invalid/non-existing ID will give 'required fields invalid' error.", response = Response.class)
    @GET
    @Path("{id}")
    public Response getMovie(@PathParam("id") String id) {
        LOGGER.info("Received request to GET movie with ID [{}]", id);
        if (Movie.isIdValid(id)) {
            return getErrorResponse(Response.Status.BAD_REQUEST, "Could not validate ID because it exceeded [" + Id.MAX_LENGTH + "] characters");
        }
        try {
            Movie movie = movieService.getMovie(id);
            if (movie != null) {
                return Response.ok().entity(gson.toJson(movie)).build();
            } else {
                return getErrorResponse(Response.Status.NOT_FOUND, "Could not find movie with ID [" + id + "]");
            }
        } catch (RepositoryException e) {
            return getErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GET
    public Response getMovies(@QueryParam("name") String name, @QueryParam("genre") String genre, @DefaultValue("10") @QueryParam("limit") int limit, @DefaultValue("0") @QueryParam("offset") int offset) {
        LOGGER.info("Received request to GET max [{}] movie(s) with name [{}]", limit, name);
        Criteria criteria = getCriteria(name, genre, limit, offset);
        if (name == null || name.equals("")) {
            return getErrorResponse(Response.Status.BAD_REQUEST, "Could not validate search parameter name");
        }
        try {
            List<Movie> movies = movieService.getMovies(criteria);
            return Response.ok().entity(gson.toJson(movies)).build();
        } catch (RepositoryException e) {
            return getErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovie(String json) {
        LOGGER.info("Received request to POST movie by json [{}]", json);
        Movie movie = gson.fromJson(json, Movie.class);
        if (movie.getName().equals("null") || movie.getName().isEmpty() || movie.getName().length() > Movie.NAME_MAX_LENGTH) {
            return getErrorResponse(Response.Status.BAD_REQUEST, "Could not validate movie name.");
        }
        try {
            String insertedMovieId = movieService.createMovie(movie);
            return Response.created(getLocation(insertedMovieId)).build();
        } catch (RepositoryException e) {
            return getErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "update", notes = "Requires full object body", response = Response.class)
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(@PathParam("id") String id, String json) {
        LOGGER.info("Received request to PUT movie by json [{}]", json);
        Movie movie = gson.fromJson(json, Movie.class);
        if (movie.getName().equals("null") || movie.getName().isEmpty() || movie.getName().length() > Movie.NAME_MAX_LENGTH) {
            return getErrorResponse(Response.Status.BAD_REQUEST, "Could not validate movie name.");
        }
        if (movieService.getMovie(id) == null) {
            return getErrorResponse(Response.Status.NOT_FOUND, "Could update movie with [" + id + "] because it does not exist. Please create it first using a POST-request.");
        }
        try {
            movieService.updateMovie(movie);
            return Response.ok().build();
        } catch (RepositoryException e) {
            return getErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMovie(@PathParam("id") String id) {
        LOGGER.info("Received request to DELETE movie with ID [{}]", id);
        if (id.length() > Id.MAX_LENGTH) {
            return getErrorResponse(Response.Status.BAD_REQUEST, "Could not validate ID because it exceeded [" + Id.MAX_LENGTH + "] characters");
        }
        try {
            movieService.deleteMovie(id);
            return Response.ok().build();
        } catch (RepositoryException e) {
            return getErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private Response getErrorResponse(Response.Status status, String message) {
        ErrorMessage errorMessage = new ErrorMessage(status, message);
        return Response.status(errorMessage.getStatus()).entity(gson.toJson(errorMessage)).build();
    }

    private Criteria getCriteria(String name, String genre, int limit, int offset) {
        Criteria criteria = new Criteria();
        criteria.addCriteria(SearchKey.NAME, name);
        criteria.addCriteria(SearchKey.GENRE, genre);
        criteria.setLimit(limit);
        criteria.setOffset(offset);
        return criteria;
    }

    private URI getLocation(String id) {
        return uriInfo.getAbsolutePathBuilder().path(id).build();
    }

}