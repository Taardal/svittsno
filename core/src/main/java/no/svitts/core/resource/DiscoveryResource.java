package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.LocalDiscoveryService;
import no.svitts.core.service.RemoteDiscoveryService;
import no.svitts.core.serverinfo.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("discover")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Discovery resource", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryResource.class);

    private LocalDiscoveryService localDiscoveryService;
    private RemoteDiscoveryService remoteDiscoveryService;

    @Inject
    public DiscoveryResource(LocalDiscoveryService localDiscoveryService, RemoteDiscoveryService remoteDiscoveryService) {
        this.localDiscoveryService = localDiscoveryService;
        this.remoteDiscoveryService = remoteDiscoveryService;
    }

    @GET
    @Path("local")
    @ApiOperation(value = "Discover movies on the servers local file system.")
    public Response discoverLocalMovies(@Context HttpHeaders httpHeaders) {
        try {
            List<Movie> movies = localDiscoveryService.discover(getPath(httpHeaders));
            LOGGER.info("Discovered [{}] local movies [{}]", movies.size(), movies);
            return Response.ok(movies.toArray()).build();
        } catch (Exception e) {
            String errorMessage = "Could not discover local movies.";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage);
        }
    }

    @GET
    @Path("remote")
    @ApiOperation(value = "Discover movies shared on the network.")
    public Response discoverRemoteMovies(@Context HttpHeaders httpHeaders) {
        try {
            List<Movie> movies = remoteDiscoveryService.discover(getPath(httpHeaders), getServerInfo(httpHeaders));
            return Response.ok(movies.toArray()).build();
        } catch (Exception e) {
            String errorMessage = "Could not discover remote movies.";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage);
        }
    }

    private ServerInfo getServerInfo(HttpHeaders httpHeaders) {
        MultivaluedMap<String, String> requestHeaders = httpHeaders.getRequestHeaders();
        String host = requestHeaders.getFirst("svitts_host");
        String username = requestHeaders.getFirst("svitts_username");
        String password = requestHeaders.getFirst("svitts_password");
        if (username != null && password != null) {
            return new ServerInfo(host, username, password);
        } else {
            return new ServerInfo(host, "", "");
        }
    }

    private String getPath(HttpHeaders httpHeaders) {
        return httpHeaders.getRequestHeaders().getFirst("svitts_path");
    }

}
