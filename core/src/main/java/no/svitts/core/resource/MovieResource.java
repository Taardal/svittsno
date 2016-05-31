package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.error.ErrorMessage;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.movie.Movie;
import no.svitts.core.search.Criteria;
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
        if (id.length() > Id.MAX_LENGTH) {
            ErrorMessage errorMessage = new ErrorMessage(Response.Status.BAD_REQUEST, "Could not validate ID because it exceeded [" + Id.MAX_LENGTH + "] characters");
            return Response.status(errorMessage.getStatus()).entity(gson.toJson(errorMessage)).build();
        }
        try {
            Movie movie = movieService.getMovie(id);
            if (movie != null) {
                return Response.ok().entity(gson.toJson(movie)).build();
            } else {
                ErrorMessage errorMessage = new ErrorMessage(Response.Status.NOT_FOUND, "Could not find movie with ID [" + id + "]");
                return Response.status(Response.Status.NOT_FOUND).entity(gson.toJson(errorMessage)).build();
            }
        } catch (RepositoryException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @GET
    @Path("name")
    public Response getMoviesByName(@QueryParam("name") String name, @QueryParam("limit") int limit) {
        LOGGER.info("Received request to GET max [{}] movie(s) with name [{}]", limit, name);
        Criteria criteria = new Criteria(0, 0);
        if (name == null || name.equals("")) {
            ErrorMessage errorMessage = new ErrorMessage(Response.Status.BAD_REQUEST, "Could not validate search parameter name");
            return Response.status(errorMessage.getStatus()).entity(gson.toJson(errorMessage)).build();
        }
        List<Movie> movies = movieService.getMovies(criteria);
        return Response.ok().entity(gson.toJson(movies)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovie(String json) {
        LOGGER.info("Received request to POST movie by json [{}]", json);
        Movie movie = gson.fromJson(json, Movie.class);
        if (movie.getName().equals("null") || movie.getName().isEmpty() || movie.getName().length() > Movie.NAME_MAX_LENGTH) {
            ErrorMessage errorMessage = new ErrorMessage(Response.Status.BAD_REQUEST, "Could not validate movie name.");
            return Response.status(errorMessage.getStatus()).entity(gson.toJson(errorMessage)).build();
        }
        try {
            String insertedMovieId = movieService.createMovie(movie);
            return Response.created(getLocation(insertedMovieId)).build();
        } catch (RepositoryException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
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
            ErrorMessage errorMessage = new ErrorMessage(Response.Status.BAD_REQUEST, "Could not validate movie name.");
            return Response.status(errorMessage.getStatus()).entity(gson.toJson(errorMessage)).build();
        }
        if (movieService.getMovie(id) == null) {
            ErrorMessage errorMessage = new ErrorMessage(Response.Status.NOT_FOUND, "Could update movie with [" + id + "] because it does not exist. Please create it first using a POST-request.");
            return Response.status(errorMessage.getStatus()).entity(gson.toJson(errorMessage)).build();
        }
        try {
            movieService.updateMovie(movie);
            return Response.ok().build();
        } catch (RepositoryException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMovie(@PathParam("id") String id) {
        LOGGER.info("Received request to DELETE movie with ID [{}]", id);
        if (id.length() > Id.MAX_LENGTH) {
            ErrorMessage errorMessage = new ErrorMessage(Response.Status.BAD_REQUEST, "Could not validate ID because it exceeded [" + Id.MAX_LENGTH + "] characters");
            return Response.status(errorMessage.getStatus()).entity(gson.toJson(errorMessage)).build();
        }
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