package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.client.TheMovieDatabaseClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("tmdb")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "The Movie Database resource", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
public class TheMovieDatabaseResource {

    private TheMovieDatabaseClient theMovieDatabaseClient;

    public TheMovieDatabaseResource(TheMovieDatabaseClient theMovieDatabaseClient) {
        this.theMovieDatabaseClient = theMovieDatabaseClient;
    }

    @GET
    @Path("{imdbId}")
    @ApiOperation(value = "Get a movie from The Movie Database by a movie's IMDB-ID")
    public Response getMovie(@PathParam("imdbId") String imdbId) {
        return Response.ok(theMovieDatabaseClient.getMovie(imdbId)).build();
    }

}
