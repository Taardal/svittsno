package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.genre.Genre;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.stream.Collectors;

@Path("genres")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Genre resource", produces = MediaType.APPLICATION_JSON)
public class GenreResource {

    @GET
    @ApiOperation(value = "Get all genres")
    public Response getGenres() {
        return Response.ok(Arrays.stream(Genre.values()).map(Genre::getValue).collect(Collectors.toList())).build();
    }

}
