package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.genre.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(GenreResource.class);

    @GET
    @ApiOperation(value = "Get all genres")
    public Response getGenres() {
        LOGGER.info("Received request to get all genres.");
        return Response.ok(Arrays.stream(Genre.values()).map(Genre::getValue).collect(Collectors.toList())).build();
    }

}
